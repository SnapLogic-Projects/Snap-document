/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2015, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression;

import com.google.common.collect.Lists;
import com.google.inject.Provider;
import com.snaplogic.Document;
import com.snaplogic.api.ExecutionException;
import com.snaplogic.common.expressions.DataValueHandler;
import com.snaplogic.common.expressions.Scope;
import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.common.jsonpath.JsonPath;
import com.snaplogic.expression.methods.JavascriptFunction;
import com.snaplogic.expression.methods.TypedMethod;
import com.snaplogic.expression.util.JaninoUtils;
import com.snaplogic.expression.util.LiteralUtils;
import com.snaplogic.grammars.SnapExpressionsParser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.compiler.Location;
import org.codehaus.janino.Java;
import org.codehaus.janino.Mod;
import org.codehaus.janino.UnparseVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sl.ListBuilder;
import sl.MapBuilder;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.snaplogic.expression.util.JaninoUtils.*;

/**
 * ANTLR visitor for the expression language parse trees that generates java strings for Janino
 *
 * @author ksubramanian
 */
public class JaninoStringGeneratorVisitor extends BaseVisitorImpl<JaninoStringGeneratorVisitor
        .AtomWrapper> {
    /**
     * Wrapper class for holding the Java Atom, and other metadata needed for optimizing the code
     * generation.
     */
    class AtomWrapper {
        TerminalNode origin;
        Java.Atom atom;
        final List<String> purityFlags = new ArrayList<>();
        boolean constant = false;

        public AtomWrapper(TerminalNode origin, Java.Atom atom) {
            this.origin = origin;
            this.atom = atom;
        }

        public AtomWrapper() {
        }

        public AtomWrapper withOperand(AtomWrapper atomWrapper) {
            if (origin == null) {
                origin = atomWrapper.origin;
            }
            purityFlags.addAll(atomWrapper.purityFlags);
            return this;
        }

        public Java.Atom withOperandAndReturnAtom(AtomWrapper atomWrapper) {
            // We need to get the value of an operand early so that any later re-used
            // sub-expressions do not change the value that would normally be obtained.
            String resultName = String.format("operandSave%s", tmpCounter++);
            Java.LocalVariableDeclarationStatement declarationStatement = new Java
                    .LocalVariableDeclarationStatement(atomWrapper.atom.getLocation(),
                    new Java.Modifiers().add(Mod.FINAL), newType(Object.class), new Java
                    .VariableDeclarator[] {
                    new Java.VariableDeclarator(null, resultName, 0, atomWrapper.atom.toRvalue())
            });
            blockStack.peek().addStatement(declarationStatement);
            withOperand(atomWrapper);
            return var(atomWrapper.atom.getLocation(), resultName);
        }

        public AtomWrapper withAtom(Java.Atom atom) {
            this.atom = atom;
            return this;
        }

        public AtomWrapper withPurityFlag(String flagName) {
            purityFlags.add(flagName);
            return this;
        }

        public AtomWrapper withPurityFlags(List<String> flags) {
            purityFlags.addAll(flags);
            return this;
        }
    }
    private static final Logger LOG = LoggerFactory.getLogger(JaninoStringGeneratorVisitor.class);
    protected static final String AND = "and";
    protected static final String OR = "or";
    protected static final String ID_START_STRING_$ = "$";
    protected static final int LEFT_OP_IDX = 0;
    protected static final int RIGHT_OP_IDX = 1;
    protected static final Location BLANK_LOC = new Location("blank", (short) 0, (short) 0);
    /**
     * We need to create temporary variables at some points, so this keeps track of the lexical
     * scopes for those.
     */
    protected final Stack<Java.Block> blockStack = new Stack<>();
    /**
     * Constants are stored in here so that we can create variable declarations for them later.
     */
    protected final List<Pair<Java.Type, Java.VariableDeclarator[]>> constants = new ArrayList<>();
    protected final Map<String, Java.LocalVariableDeclarationStatement> captures = new HashMap<>();
    protected int constCounter = 0;
    protected int captureCounter = 0;
    protected int tmpCounter = 0;
    protected int resultCounter = 0;
    protected int pathCounter = 0;
    protected int litCounter = 0;
    protected int insideCircuitry = 0;
    /**
     * Counter for arrow function variable names.
     */
    protected int arrowCounter = 0;
    /**
     * Counter for variable names used to store arrow function arguments.
     */
    protected int argCounter = 0;
    /**
     * Stack of scopes for arrow function parameter names.  The scopes map the arrow function
     * parameter name to the local variable used to store the arrow function argument.
     */
    protected final ScopeStack arrowScopes = new ScopeStack();
    protected final Stack<String> objVars = new Stack<>();
    protected final Map<String, String> varMap = new HashMap<>();

    /**
     * Tracks variables used to store the results of previous method calls.
     */
    static class MethodVariable {
        String resultName;
        String flagName;
        final List<String> purityFlags = new ArrayList<>();

        public MethodVariable(String tmpName, final String flagName, final
            List<String> purityFlags) {
            this.resultName = tmpName;
            this.flagName = flagName;
            this.purityFlags.addAll(purityFlags);
        }
    }

    /**
     * Map of method calls to a pair that stores the variable names of the first result and the
     * saved impurity flag for that call.
     */
    protected final Map<String, MethodVariable> methodVarMap = new HashMap<>();
    protected String expressionText;

    public JaninoStringGeneratorVisitor() {
        this(null, null);
    }

    public JaninoStringGeneratorVisitor(Document originalDocument,
                                        final DataValueHandler valueEscapeHandler) {
        super(originalDocument, valueEscapeHandler == null ? DEFAULT_HANDLER : valueEscapeHandler);
        this.scopes.push(GLOBAL_SCOPE);
        blockStack.push(new Java.Block(null));
    }

    public JaninoStringGeneratorVisitor(Object data, Document originalDocument,
                                        Map<String, Object> envData) {
        this(originalDocument, DEFAULT_HANDLER);
        this.scopes.push(new EnvironmentScope(envData));
        this.scopes.push(new DocumentScope(data));
    }

    public Java.Block buildBlock(ParseTree tree) {
        AtomWrapper wrapper = visit(tree);
        Java.Atom atom = wrapper.atom;

        if (wrapper.origin == null) {
            // We need to make sure the AtomWrapper was constructed properly.  Wrappers must
            // originate from a terminal in the grammar, like number or ID.  Any non-terminals
            // should either pass the original wrapper through or construct a new one that
            // references one of the terminals covered by the non-terminal.  Maintaining the
            // lineage ensures that we are tracking the purity information correctly.
            throw new ExecutionException("Atom wrapper lineage was not maintained");
        }

        assert blockStack.size() == 1;

        Java.Block stmtBlock = blockStack.peek();
        stmtBlock.statements.add(0, exprStmt(methodInvoke(context(), "setContext", docVar(),
                scopesVar(), handlerVar())));
        for (Java.LocalVariableDeclarationStatement decl : captures.values()) {
            stmtBlock.statements.add(1, decl);
        }
        if (arrowCounter > 0) {
            // We don't want anybody returning an arrow function as a result since nothing can
            // deal with that, so we check and raise an error.
            atom = checkReturnValue(atom);
        }
        stmtBlock.addStatement(new Java.ReturnStatement(null, atom.toRvalue()));

        Java.Block finallyBlock = new Java.Block(null);
        finallyBlock.addStatement(exprStmt(methodInvoke(context(), "remove")));

        Java.TryStatement tryStatement = new Java.TryStatement(null, stmtBlock, Collections
                .EMPTY_LIST, finallyBlock);

        Java.Block outer = new Java.Block(null);
        outer.addStatement(tryStatement);

        /* this is used for printing out java code statements */

        // for (int i = 0; i < blockStack.peek().getStatements().length; i++) {
        //     System.out.println(blockStack.peek().getStatements()[i]);
        // }

        return outer;
    }

    public SnapLogicExpression buildExpression(final String expr, ParseTree tree) {
        expressionText = expr;
        Java.Block block = buildBlock(tree);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        UnparseVisitor unparseVisitor = new UnparseVisitor(writer);
        block.accept(unparseVisitor);
        unparseVisitor.close();
        ExprEvaluator scriptEvaluator = new ExprEvaluator();
        scriptEvaluator.setConstants(constants);
        scriptEvaluator.setReturnType(Object.class);
        scriptEvaluator.setThrownExceptions(new Class[] { Throwable.class });
        try {
            String code = outputStream.toString(StandardCharsets.UTF_8.toString());
            return (SnapLogicExpression) scriptEvaluator.createFastEvaluator(
                    code, SnapLogicExpression.class, new String[]{"doc", "scopes", "handler"});
        } catch (CompileException|UnsupportedEncodingException e) {
            LOG.error("Expression: {}", tree.getText());
            LOG.error("Compiled expression: {}", outputStream.toString());
            LOG.error("Compilation failed for script: {}", outputStream.toString(), e);
            throw new ExecutionException(e, "Unable to compile expression")
                    .withResolutionAsDefect();
        }
    }

    //------------------------------ Visitor Methods --------------------------------------------//

    @Override
    public AtomWrapper visitAddition(SnapExpressionsParser.AdditionContext ctx) {
        List<ParseTree> contexts = ctx.children;
        List<Java.Atom> operands = new ArrayList<>(contexts.size());
        List<Java.Atom> operators = new ArrayList<>(contexts.size());
        if (contexts.size() > 1) {
            AtomWrapper retval = new AtomWrapper();
            operands.add(retval.withOperandAndReturnAtom(visit(contexts.get(0))));
            for (int i = 1; i < contexts.size(); i++) {
                Token operator = ((TerminalNode) contexts.get(i)).getSymbol();
                operators.add(primitiveIntLiteral(operator.getType()));
                operands.add(retval.withOperandAndReturnAtom(visit(contexts.get(++i))));
            }

            Java.Atom constOperators = declareConst(arrayType(basicType(Java.BasicType.INT)),
                    newInitializedArray(arrayType(basicType(Java.BasicType.INT)), operators)
                            .toRvalue());

            return retval.withAtom(evalAdd(operands, constOperators));
        }
        return visit(contexts.get(0));
    }

    @Override
    public AtomWrapper visitMultiplication(SnapExpressionsParser.MultiplicationContext ctx) {
        List<ParseTree> contexts = ctx.children;
        List<Java.Atom> operands = new LinkedList<>();
        List<Java.Atom> operators = new ArrayList<>(contexts.size());
        if (contexts.size() > 1) {
            AtomWrapper retval = new AtomWrapper();
            operands.add(retval.withOperandAndReturnAtom(visit(contexts.get(0))));
            for (int i = 1; i < contexts.size(); i++) {
                Token operator = ((TerminalNode) contexts.get(i)).getSymbol();
                operators.add(primitiveIntLiteral(operator.getType()));
                operands.add(retval.withOperandAndReturnAtom(visit(contexts.get(++i))));
            }

            Java.Atom constOperators = declareConst(arrayType(basicType(Java.BasicType.INT)),
                    newInitializedArray(arrayType(basicType(Java.BasicType.INT)), operators)
                            .toRvalue());

            return retval.withAtom(evalMultiply(operands, constOperators));
        }
        return visit(contexts.get(0));
    }

    @Override
    public AtomWrapper visitUnary(SnapExpressionsParser.UnaryContext ctx) {
        AtomWrapper val = visit(ctx.atomicExp());
        if (ctx.op == null) {
            return val;
        }
        return new AtomWrapper()
                .withAtom(evalUnary(val.atom, primitiveIntLiteral(ctx.op.getType())))
                .withOperand(val);
    }

    private AtomWrapper buildShortCircuitry(final List contexts, boolean logicalOr) {

        if (contexts.size() == 1) {
            // If there's only one operand, there's nothing to do, just generate the code to
            // evaluate the expression.
            return visit((ParseTree) contexts.get(0));
        }
        AtomWrapper retval = new AtomWrapper();
        // The result of the short-circuit evaluation will be put in this variable.
        String resultName = String.format("result%s", resultCounter++);
        int i;

        Java.LocalVariableDeclarationStatement resultDeclaration = new Java
                .LocalVariableDeclarationStatement(null, new Java.Modifiers().add(Mod.FINAL),
                newType(Object.class), new Java.VariableDeclarator[] {
                new Java.VariableDeclarator(null, resultName, 0, null)
        });

        blockStack.peek().addStatement(resultDeclaration);

        Java.Block outerBlock = null;
        Java.Statement lastStatement = null;

        // The short-circuit logic is handled by a bunch of nested if-statements.  The operands
        // are evaluated in order, first checking the result before moving on to the next operand.
        // This loop builds the if-statements inside-out, so we start with the last operand.
        //
        // For example, the expression "0 || 1 || 2" would generate code like the following:
        //
        //   Object result0;
        //   Object tmp0 = 0;
        //   if (ObjectType.toBoolean(tmp0)) {
        //     result0 = tmp0;
        //   } else {
        //     Object tmp1 = 1;
        //     if (ObjectType.toBoolean(tmp1)) {
        //       result0 = tmp1;
        //     } else {
        //       Object tmp2 = 2;
        //       if (ObjectType.toBoolean(tmp2)) {
        //         result0 = tmp2;
        //       }
        //     }
        //   }
        //
        for (i = contexts.size() - 1; i >= 0; i--) {
            outerBlock = new Java.Block(null);
            blockStack.push(outerBlock);

            AtomWrapper operand = visit((ParseTree) contexts.get(i));
            retval.withOperand(operand);
            Java.Atom tmpResult = operand.atom;
            String tmpName = String.format("tmp%s", tmpCounter++);
            Java.LocalVariableDeclarationStatement declarationStatement = new Java
                    .LocalVariableDeclarationStatement(null, new Java.Modifiers().add(Mod.FINAL),
                    newType(Object.class), new Java.VariableDeclarator[] {
                    new Java.VariableDeclarator(null, tmpName, 0, tmpResult.toRvalue())
            });

            outerBlock.addStatement(declarationStatement);

            Java.Statement assignStatement = assign(var(resultName), var(tmpName));
            if (lastStatement == null) {
                outerBlock.addStatement(assignStatement);
            } else {
                Java.Block thenBlock = new Java.Block(null);
                Java.Atom condition = toBoolean(var(tmpName));
                if (!logicalOr) {
                    condition = not(condition);
                }
                thenBlock.addStatement(assignStatement);
                outerBlock.addStatement(new Java.IfStatement(null, condition.toRvalue(),
                        thenBlock, lastStatement));
            }

            lastStatement = blockStack.pop();
        }

        blockStack.peek().addStatement(outerBlock);
        return retval.withAtom(var(resultName).toRvalue());
    }

    @Override
    public AtomWrapper visitLogicalOr(SnapExpressionsParser.LogicalOrContext ctx) {
        return buildShortCircuitry(ctx.logicalAnd(), true);
    }

    @Override
    public AtomWrapper visitLogicalAnd(SnapExpressionsParser.LogicalAndContext ctx) {
        return buildShortCircuitry(ctx.logicalXor(), false);
    }

    @Override
    public AtomWrapper visitLogicalXor(SnapExpressionsParser.LogicalXorContext ctx) {
        List<SnapExpressionsParser.InOperatorContext> contexts = ctx.inOperator();
        if (contexts.size() == 1) {
            return visit(contexts.get(0));
        }
        AtomWrapper retval = new AtomWrapper();
        List<Java.Atom> operands = new LinkedList<>();
        for (int i = 0; i < contexts.size(); i++) {
            operands.add(retval.withOperandAndReturnAtom(visit(contexts.get(i))));
        }
        return retval.withAtom(evalLogicalXor(operands));
    }

    @Override
    public AtomWrapper visitRelational(SnapExpressionsParser.RelationalContext ctx) {
        if (ctx.getChildCount() < 2) {
            return visit(ctx.addition(LEFT_OP_IDX));
        }
        AtomWrapper retval = new AtomWrapper();
        List<Java.Atom> operands = new LinkedList<>();
        operands.add(retval.withOperandAndReturnAtom(visit(ctx.addition(LEFT_OP_IDX))));
        operands.add(retval.withOperandAndReturnAtom(visit(ctx.addition(RIGHT_OP_IDX))));
        return retval.withAtom(evalRelational(operands, primitiveIntLiteral(ctx.op.getType())));
    }

    @Override
    public AtomWrapper visitExp(SnapExpressionsParser.ExpContext ctx) {
        List<SnapExpressionsParser.LogicalOrContext> contexts = ctx.logicalOr();
        if (contexts.size() == 1) {
            return visit(contexts.get(0));
        }
        AtomWrapper retval = new AtomWrapper();

        String resultName = String.format("condResult%s", tmpCounter++);
        Java.LocalVariableDeclarationStatement declarationStatement = new Java
                .LocalVariableDeclarationStatement(null, new Java.Modifiers(),
                newType(Object.class), new Java.VariableDeclarator[] {
                new Java.VariableDeclarator(null, resultName, 0, null)
        });
        blockStack.peek().addStatement(declarationStatement);

        Java.Atom cond = toBoolean(retval.withOperandAndReturnAtom(visit(contexts.get(0))));

        Java.Block trueBlock = new Java.Block(BLANK_LOC);
        blockStack.push(trueBlock);

        AtomWrapper lhs = visit(contexts.get(1));
        trueBlock.addStatement(assign(var(resultName), lhs.atom));

        blockStack.pop();

        Java.Block falseBlock = new Java.Block(BLANK_LOC);
        blockStack.push(falseBlock);

        AtomWrapper rhs = visit(contexts.get(2));
        falseBlock.addStatement(assign(var(resultName), rhs.atom));

        blockStack.pop();

        blockStack.peek().addStatement(ifStatement(cond, trueBlock, falseBlock));

        return retval.withAtom(var(resultName))
                .withOperand(lhs)
                .withOperand(rhs);
    }

    @Override
    public AtomWrapper visitId(SnapExpressionsParser.IdContext ctx) {
        String contextText = ctx.getText();
        Object arrowArg = arrowScopes.lookupVariable(contextText);
        Object globalVar = GLOBAL_SCOPE.get(contextText);
        Java.Atom idAtom;

        if ("__root__".equals(contextText) && !objVars.isEmpty()) {
            return new AtomWrapper(ctx.idstring().Id(), peekIntoBuilder(var(objVars.get(0))));
        } else if ("__parent__".equals(contextText) && objVars.size() >= 2) {
            return new AtomWrapper(ctx.idstring().Id(), peekIntoBuilder(var(objVars.get(objVars
                    .size() - 2))));
        } else if ("this".equals(contextText) && !objVars.isEmpty()) {
            return new AtomWrapper(ctx.idstring().Id(), peekIntoBuilder(var(objVars.peek())));
        } else if (arrowArg != Scope.UNDEFINED) {
            // We found an arrow function parameter, so we need to get the variable that represents
            // the parameter.
            idAtom = var((String) arrowArg);
        } else if (contextText.equals("$")) {
            idAtom = ambiguousName(new String[]{ "doc" });
        } else if (contextText.startsWith(ID_START_STRING_$)) {
            Java.Atom docAtom = ambiguousName(new String[]{ "doc" });

            idAtom = evalPropertyRef(string(getCurrentPath(ctx)), string(contextText),
                    docAtom, string(contextText.substring(1)));
        } else if (globalVar != Scope.UNDEFINED) {
            return new AtomWrapper(ctx.idstring().Id(), fieldInstance(globalVar.getClass()));
        } else if (arrowScopes.isEmpty()) {
            idAtom = evalLookupVariable(string(contextText));
        } else {
            idAtom = captureIntoLocal(contextText, lookupInScope(string(contextText)).toRvalue());
            idAtom = finishLookup(idAtom, string(contextText));
        }

        return new AtomWrapper(ctx.idstring().Id(), handleValue(idAtom, string(contextText)));
    }

    @Override
    public AtomWrapper visitEquality(SnapExpressionsParser.EqualityContext ctx) {
        List<ParseTree> contexts = ctx.children;
        if (contexts.size() == 1) {
            return visit(contexts.get(0));
        }
        AtomWrapper retval = new AtomWrapper();
        List<Java.Atom> operands = new ArrayList<>(ctx.children.size());
        List<Java.Atom> operators = new ArrayList<>(ctx.children.size() - 1);
        operands.add(retval.withOperandAndReturnAtom(visit(contexts.get(0))));
        for (int i = 1; i < contexts.size(); i++) {
            int operator = ((TerminalNode) contexts.get(i)).getSymbol().getType();
            operators.add(primitiveIntLiteral(operator));
            operands.add(retval.withOperandAndReturnAtom(visit(contexts.get(++i))));
        }
        return retval.withAtom(evalEquality(operands, operators));
    }

    @Override
    public AtomWrapper visitProperty(final SnapExpressionsParser.PropertyContext ctx) {
        AtomWrapper result = visit(ctx.atomic());

        return visitSuffixesOfMember(result, ctx.memberSuffix());
    }

    public AtomWrapper visitSuffixesOfMember(AtomWrapper result, List<SnapExpressionsParser
            .MemberSuffixContext> ctx) {
        for (SnapExpressionsParser.MemberSuffixContext suffix : ctx) {
            if (suffix.children.isEmpty()) {
                continue;
            }
            ParseTree child = suffix.getChild(0);
            if (child instanceof SnapExpressionsParser.MethodContext) {
                result = visitMethod(result, (SnapExpressionsParser.MethodContext) child);
            } else if (child instanceof SnapExpressionsParser.IndexSuffixContext) {
                result = visitIndexSuffix(result, (SnapExpressionsParser.IndexSuffixContext) child);
            } else if (child instanceof SnapExpressionsParser.PropertyRefContext) {
                result = visitPropertyRef(result, (SnapExpressionsParser.PropertyRefContext) child);
            } else if (child instanceof SnapExpressionsParser.ArgumentsContext) {
                AtomWrapper retval = new AtomWrapper();
                AtomWrapper args = visit(child);
                Java.Atom argsAsList = methodInvoke(classAtom(Arrays.class), AS_LIST, args.atom);
                retval.withAtom(evalCall(cast(newType(JavascriptFunction.class), result.atom),
                        argsAsList))
                        .withOperand(result)
                        .withOperand(args);
                result = retval;
            }
        }
        return result;
    }

    @Override
    public AtomWrapper visitArguments(final SnapExpressionsParser.ArgumentsContext ctx) {
        AtomWrapper retval = new AtomWrapper();
        Token stopToken = ctx.getStop();

        if (stopToken.getType() != SnapExpressionsParser.RPAREN) {
            Token startToken = ctx.getStart();
            String objText = ctx.getText();

            throw new ExecutionException(BailErrorStrategy.extractSubExpression(objText,
                    objText.length(), objText.length() - 1))
                    .withReason(String.format("Function call starting at line %d:%d is missing " +
                            "a closing parentheses", startToken.getLine(), startToken
                            .getCharPositionInLine()))
                    .withResolution("Insert a right parentheses ')' to terminate the argument " +
                            "list");
        }

        boolean hasSpread = false;
        for (SnapExpressionsParser.ExpOrSpreadContext expContext : ctx.args) {
            if (expContext.ELLIPSIS() != null) {
                hasSpread = true;
            }
        }

        if (hasSpread) {
            AtomWrapper argList = buildList(ctx.args);
            Java.Atom toArray = methodInvoke(argList.atom, "toArray");

            return retval.withOperand(argList)
                    .withAtom(toArray);
        } else {
            List<Java.Atom> args = Lists.newArrayList();

            for (SnapExpressionsParser.ExpOrSpreadContext expContext : ctx.args) {
                args.add(retval.withOperandAndReturnAtom(visit(expContext)));
            }
            return retval.withAtom(newInitializedArray(arrayType(newType(Object.class)), args));
        }
    }

    /*
    The Optimization taking place in visitMethod works as follows: Any time a method is called,
    we collect the Object the method was called on, the method name, and the arguments to hash that
    into a map as the key to a pair that stores the variable names of the first result and the
    saved impurity flag for that call.
    */
    public AtomWrapper visitMethod(AtomWrapper member, SnapExpressionsParser.MethodContext ctx) {
        AtomWrapper retval = new AtomWrapper();
        Java.Block block = blockStack.peek();
        String functionName = StringUtils.stripStart(ctx.PropertyRef().getText(), ".");
        AtomWrapper args = visit(ctx.arguments());
        retval.withOperand(args);
        Java.Atom c = declareConst(TypedMethod[].class, getTypedMethods(string(functionName))
                .toRvalue());
        Java.Atom method = evalMethod(string(functionName),
                string(StringEscapeUtils.escapeJava(getCurrentPath(ctx))), c,
                retval.withOperandAndReturnAtom(member),
                args.atom);
        MethodVariable pair = methodVarMap.get(member.atom.toString() + ctx.getText());

        if (member.atom.toString().startsWith("lit") || insideCircuitry > 0) {
            return retval.withAtom(method);
        }

        if (pair != null) {
            Java.Atom result = new Java.ConditionalExpression(null,
                var(pair.flagName).toRvalue(),
                method.toRvalue(),
                var(pair.resultName).toRvalue());
            String resultName = String.format("callSave%s", tmpCounter++);
            Java.LocalVariableDeclarationStatement declarationStatement = new Java
                    .LocalVariableDeclarationStatement(null, new Java.Modifiers().add(Mod.FINAL),
                    newType(Object.class), new Java.VariableDeclarator[] {
                    new Java.VariableDeclarator(null, resultName, 0, result.toRvalue())
            });
            block.addStatement(declarationStatement);
            return retval.withAtom(var(resultName))
                    .withPurityFlags(pair.purityFlags);
        }

        String tmpName = String.format("methodSave%s", tmpCounter++);
        String flagName = String.format("flagSave%s", tmpCounter++);
        Java.LocalVariableDeclarationStatement declarationStatement = new Java
                .LocalVariableDeclarationStatement(null, new Java.Modifiers().add(Mod.FINAL),
                newType(Object.class), new Java.VariableDeclarator[] {
                new Java.VariableDeclarator(null, tmpName, 0, method.toRvalue())
        });
        block.addStatement(declarationStatement);
        Java.LocalVariableDeclarationStatement flagDeclarationStatement = new Java
                .LocalVariableDeclarationStatement(null, new Java.Modifiers(),
                basicType(Java.BasicType.BOOLEAN), new Java.VariableDeclarator[] {
                new Java.VariableDeclarator(null, flagName, 0,
                        methodInvoke(context(), "consumeImpureFlag").toRvalue())
        });

        block.addStatement(flagDeclarationStatement);
        if (!retval.purityFlags.isEmpty()) {
            Java.Block propagateBlock = new Java.Block(null);
            for (String operandFlag : retval.purityFlags) {
                propagateBlock.addStatement(assign(var(operandFlag), new Java
                        .BooleanLiteral(null, "true")));
            }
            block.addStatement(ifStatement(var(flagName), propagateBlock, null));
        }
        retval.withPurityFlag(flagName);
        pair = new MethodVariable(tmpName, flagName, retval.purityFlags);
        methodVarMap.put(member.atom.toString() + ctx.getText(), pair);

        return retval.withAtom(var(tmpName));
    }

    public AtomWrapper visitPropertyRef(AtomWrapper member, SnapExpressionsParser.PropertyRefContext
            ctx) {
        AtomWrapper retval = new AtomWrapper();
        String field = StringUtils.stripStart(ctx.getText(), ".");
        return retval.withAtom(evalPropertyRef(string(getCurrentPath(ctx)),
                string(StringEscapeUtils.escapeJava(ctx.getText())),
                retval.withOperandAndReturnAtom(member), string(field)));
    }

    public AtomWrapper visitIndexSuffix(AtomWrapper member, SnapExpressionsParser.IndexSuffixContext
            ctx) {
        AtomWrapper retval = new AtomWrapper();
        AtomWrapper index = visit(ctx.exp());
        return retval.withAtom(evalIndexSuffix(
                retval.withOperandAndReturnAtom(member),
                retval.withOperandAndReturnAtom(index), string(getCurrentPath(ctx)),
                string(StringEscapeUtils.escapeJava(ctx.getText()))));
    }

    @Override
    public AtomWrapper visitObjliteral(SnapExpressionsParser.ObjliteralContext ctx) {
        AtomWrapper retval = new AtomWrapper();
        Token stopToken = ctx.getStop();
        if (stopToken.getType() != SnapExpressionsParser.RCURLY) {
            Token startToken = ctx.getStart();

            throw new ExecutionException(BailErrorStrategy.extractSubExpression(expressionText,
                    stopToken.getStopIndex() + 1, stopToken.getStopIndex()))
                    .withReason(String.format("Object literal starting at line %d:%d was not " +
                            "terminated", startToken.getLine(), startToken
                            .getCharPositionInLine()))
                    .withResolution("Insert a right curly brace '}' to close the literal");
        }
        Java.Block block = blockStack.peek();
        List<SnapExpressionsParser.PropertyNameAndValueContext> properties;
        String litName = String.format("lit%s", litCounter++);
        Java.Atom builder = mapBuilder();
        Java.LocalVariableDeclarationStatement declarationStatement = new Java
                .LocalVariableDeclarationStatement(null, new Java.Modifiers().add(Mod.FINAL),
                newType(MapBuilder.class), new Java.VariableDeclarator[] {
                new Java.VariableDeclarator(null, litName, 0, builder.toRvalue())
        });

        block.addStatement(declarationStatement);

        objVars.push(litName);
        properties = ctx.propertyNameAndValue();
        for (SnapExpressionsParser.PropertyNameAndValueContext property : properties) {
            if (property.ELLIPSIS() != null) {
                block.addStatement(exprStmt(mapPutAll(var(litName),
                        retval.withOperandAndReturnAtom(visit(property.exp())))));
            } else {
                Java.Atom name = retval.withOperandAndReturnAtom(visit(property
                        .propertyNameOrExpr()));
                Java.Atom value = retval.withOperandAndReturnAtom(visit(property.exp()));
                if (!(name instanceof Java.StringLiteral)) {
                    name = methodInvoke(name, "toString");
                }

                Location location = value.getLocation();
                if (location != null && location.equals(JaninoUtils.PEEK_LOC)) {
                    String original = property.exp().getText();
                    throw new ExecutionException("Cannot store '%s' in an object literal")
                            .formatWith(original)
                            .withReason("Storing this variable would create a cycle, which is not" +
                                    " supported")
                            .withResolution("You probably meant to reference another " +
                                            "field within the given variable (e.g. %s.other)",
                                    original);
                }
                block.addStatement(exprStmt(mapPut(var(litName), name, value)));
            }
        }
        objVars.pop();
        if (properties.isEmpty()) {
            return new AtomWrapper(ctx.LCURLY(), build(var(litName)));
        }
        return retval.withAtom(build(var(litName)));
    }

    @Override
    public AtomWrapper visitPropertyName(final SnapExpressionsParser.PropertyNameContext ctx) {
        return new AtomWrapper(ctx.Id(), string(StringEscapeUtils.escapeJava(ctx.value)));
    }

    @Override
    public AtomWrapper visitPropertyNameExpr(final SnapExpressionsParser.PropertyNameExprContext
                                                     ctx) {
        return visit(ctx.exp());
    }

    @Override
    public AtomWrapper visitAtomicExp(final SnapExpressionsParser.AtomicExpContext ctx) {
        try {
            // Record the current atomic expression so that we can give a meaningful
            // error message if there is a dereference of an undefined value.
            currentAtomic.push(ctx);
            return super.visitAtomicExp(ctx);
        } finally {
            currentAtomic.pop();
        }
    }

    @Override
    public AtomWrapper visitArrowFunction(final SnapExpressionsParser.ArrowFunctionContext ctx) {
        final SnapExpressionsParser.ArrowParametersContext arrowParametersContext = ctx
                .arrowParameters();
        List<Pair<String, SnapExpressionsParser.ExpContext>> parameterNames = new ArrayList<>();
        Set<String> allParameterNames = new HashSet<>();
        String arrowName = String.format("arrow%s", arrowCounter++);
        String argsName = String.format("%sArgs", arrowName);
        Map<String, Object> argBacking = new HashedMap();
        BasicScope argScope = new BasicScope(argBacking);
        String restName = null;

        arrowScopes.push(argScope);

        Java.Block methodBlock = new Java.Block(null);
        blockStack.push(methodBlock);

        if (arrowParametersContext.coverParenthesisedExpressionAndArrowParameterList() != null) {
            SnapExpressionsParser.CoverParenthesisedExpressionAndArrowParameterListContext
                    coverContext = arrowParametersContext
                    .coverParenthesisedExpressionAndArrowParameterList();
            Token stopToken = coverContext.getStop();

            if (stopToken.getType() != SnapExpressionsParser.RPAREN) {
                Token startToken = coverContext.getStart();
                String objText = coverContext.getText();

                throw new ExecutionException(BailErrorStrategy.extractSubExpression(objText,
                        objText.length(), objText.length() - 1))
                        .withReason(String.format("Arrow function parameter list starting at line" +
                                        " %d:%d is missing a closing right parentheses",
                                startToken.getLine(), startToken.getCharPositionInLine()))
                        .withResolution("Insert a right parentheses ')' to terminate the " +
                                "parameter list");
            }

            List<SnapExpressionsParser.ParameterWithDefaultContext> parameterContexts =
                    coverContext.parameterWithDefault();

            // Build a list of arrow function parameter names and their default values.
            for (SnapExpressionsParser.ParameterWithDefaultContext param : parameterContexts) {
                String paramName = param.idstring().getText();
                if (allParameterNames.contains(paramName)) {
                    Token idToken = param.idstring().getStart();

                    throw new ExecutionException(BailErrorStrategy.extractSubExpression(
                            expressionText, idToken.getStartIndex(), idToken.getStopIndex()))
                            .withReason(String.format("Parameter name '%s' has already been used",
                                    paramName))
                            .withResolution("Use a different name for each parameters");
                }
                allParameterNames.add(paramName);
                parameterNames.add(Pair.of(paramName, param.exp()));
            }

            // Check if the function has a '...rest' parameter that captures any extra arguments
            // that were passed.
            if (coverContext.restParameters() != null) {
                restName = coverContext.restParameters().idstring().getText();
            }
        } else {
            // Simple form of an arrow function, accepts one parameter that defaults to null if it
            // is not given.
            parameterNames.add(Pair.<String, SnapExpressionsParser.ExpContext>of(
                    arrowParametersContext.idstring().getText(), null));
        }

        // Create the local variables used to store the argument values (or defaults).
        int argIndex = 0;
        for (Pair<String, SnapExpressionsParser.ExpContext> pair : parameterNames) {
            String argName = String.format("arrowArg%s", argCounter);
            Java.Atom defaultValue = pair.getRight() != null ? visit(pair.getRight()).atom :
                    nullLiteral();
            argCounter += 1;

            methodBlock.addStatement(declareVar(Object.class, argName, getArg(var(argsName),
                    primitiveIntLiteral(argIndex), defaultValue).toRvalue()));

            argBacking.put(pair.getLeft(), argName);
            argIndex += 1;
        }

        if (restName != null) {
            // Create a "rest" local variable to store any extra arguments that are passed.
            String argName = String.format("arrowArg%s", argCounter);
            argCounter += 1;

            Java.Atom subList = methodInvoke(var(argsName), "subList", primitiveIntLiteral(
                    argIndex), methodInvoke(var(argsName), "size"));
            Java.Atom newList = newClassInstance(newType(ArrayList.class), subList);
            methodBlock.addStatement(declareVar(Object.class, argName, newList.toRvalue()));
            argBacking.put(restName, argName);
        }

        // Generate the body of the function,
        Java.Atom result = visit(ctx.conciseBody()).atom;
        // ... propagate impurity, and
        methodBlock.addStatement(exprStmt(methodInvoke(context(), "propagateImpure")));
        // ... return the result.
        methodBlock.addStatement(new Java.ReturnStatement(null, result.toRvalue()));
        blockStack.pop();

        arrowScopes.pop();

        // Create the anonymous JavascriptFunction class and eval() method.
        Java.AnonymousClassDeclaration anonDecl = new Java.AnonymousClassDeclaration(null,
                newType(JavascriptFunction.class));
        Java.FunctionDeclarator.FormalParameter formalParameters[] = {
                new Java.FunctionDeclarator.FormalParameter(null, true, newType(List.class),
                        argsName),
        };
        anonDecl.addDeclaredMethod(new Java.MethodDeclarator(null, null, new Java
                .Modifiers(Mod.PUBLIC), newType(Object.class), "eval", new Java
                .FunctionDeclarator.FormalParameters(null, formalParameters, false), new Java
                .Type[0], Arrays.asList(methodBlock)));

        // Add in a "toString()" method so that we can create error messages with the arrow
        // function definition.
        Java.Block toStringBlock = new Java.Block(null);
        Token startToken = ctx.getStart();
        Token stopToken = ctx.getStop();
        String escapedExpression = StringEscapeUtils.escapeJava(expressionText.substring(
                startToken.getStartIndex(), stopToken.getStopIndex() + 1));
        toStringBlock.addStatement(new Java.ReturnStatement(null, string(String.format(
                "/* ArrowFunction @ line %s:%s */ %s", startToken.getLine(), startToken
                        .getCharPositionInLine(), escapedExpression, ctx.getText())).toRvalue()));
        anonDecl.addDeclaredMethod(new Java.MethodDeclarator(null, null, new Java
                .Modifiers(Mod.PUBLIC), newType(String.class), "toString", new Java
                .FunctionDeclarator.FormalParameters(null, new Java.FunctionDeclarator
                .FormalParameter[0], false), new Java
                .Type[0], Arrays.asList(toStringBlock)));

        blockStack.peek().addStatement(declareVar(JavascriptFunction.class, arrowName, new Java
                .NewAnonymousClassInstance(null, null, anonDecl, EMPTY_ARGS)));

        return new AtomWrapper(ctx.ARROW(), var(arrowName));
    }

    @Override
    public AtomWrapper visitFunctionCall(final SnapExpressionsParser.FunctionCallContext ctx) {
        SnapExpressionsParser.ArgumentsContext argumentsContext = ctx.arguments();

        if ("jsonPath".equals(ctx.atomic().getText()) && argumentsContext.args.size() == 2) {
            Java.Atom arg1 = visit(argumentsContext.args.get(1)).atom;

            if (arg1 instanceof Java.StringLiteral) {
                String pathVarName = String.format("path%s", pathCounter++);

                Java.LocalVariableDeclarationStatement pathVarDecl = new Java
                        .LocalVariableDeclarationStatement(null, new Java.Modifiers().add(Mod
                        .FINAL),
                        newType(JsonPath.class), new Java.VariableDeclarator[] {
                        new Java.VariableDeclarator(null, pathVarName, 0, null)
                });

                blockStack.peek().addStatement(pathVarDecl);

                // Janino doesn't allow us to declare static variables, so we have to create
                // an anonymous inner-class with a static-variable, construct the object and
                // then call a method that returns the static variable.
                String pathProviderName = String.format("pathProvider%s", pathCounter++);
                Java.AnonymousClassDeclaration anonDecl = new Java.AnonymousClassDeclaration(
                        null, newType(Provider.class));
                anonDecl.addFieldDeclaration(new Java.FieldDeclaration(null, null, new Java
                        .Modifiers((short) (Mod.STATIC | Mod.FINAL)),
                        newType(JsonPath.class), new Java.VariableDeclarator[] {
                        new Java.VariableDeclarator(null, "PATH", 0,
                                jsonPathCompile(arg1).toRvalue())
                }));
                anonDecl.addDeclaredMethod(new Java.MethodDeclarator(null, null, new Java
                        .Modifiers(Mod.PUBLIC), newType(Object.class), "get", new Java
                        .FunctionDeclarator.FormalParameters(), new Java.Type[0], Arrays
                        .asList(new Java.ReturnStatement(null, var("PATH").toRvalue()))));

                Java.LocalVariableDeclarationStatement pathProviderVarDecl = new Java
                        .LocalVariableDeclarationStatement(null, new Java.Modifiers().add(Mod
                        .FINAL), newType(Provider.class), new Java.VariableDeclarator[] {
                        new Java.VariableDeclarator(null, pathProviderName, 0,
                                new Java.NewAnonymousClassInstance(null, null, anonDecl,
                                        EMPTY_ARGS))
                });

                // The static initializer can fail with a bad JSON-Path, so we need to catch
                // it and rethrow it.
                Java.Block tryBlock = new Java.Block(null);
                tryBlock.addStatement(pathProviderVarDecl);
                Java.Atom pathVar = var(pathVarName);
                Java.Atom pathProviderVar = new Java.Cast(null, newType(JsonPath.class),
                        methodInvoke(var(pathProviderName), "get").toRvalue());
                tryBlock.addStatement(assign(pathVar, pathProviderVar));

                Java.Block catchBlock = new Java.Block(null);
                catchBlock.addStatement(new Java.ThrowStatement(null,
                        cast(newType(RuntimeException.class),
                                methodInvoke(var("e"), "getException")).toRvalue()));

                Java.TryStatement handleBadPath = new Java.TryStatement(null, tryBlock,
                        Arrays.asList(new Java.CatchClause(null, new Java.FunctionDeclarator
                                .FormalParameter(null, false, newType
                                (ExceptionInInitializerError.class), "e"), catchBlock)), null);

                blockStack.peek().addStatement(handleBadPath);

                // Finally, call the helper function to actually read the value from the path.
                AtomWrapper result = new AtomWrapper();

                result.withAtom(runtimeEval("jsonPathRead", var(pathVarName), scopesVar(),
                        result.withOperandAndReturnAtom(visit(argumentsContext.args.get(0)))));

                return visitSuffixesOfMember(result, ctx.memberSuffix());
            }
        }

        AtomWrapper retval = new AtomWrapper();
        Java.Atom methodToCall = retval.withOperandAndReturnAtom(visit(ctx.atomic()));
        AtomWrapper args = visit(argumentsContext);
        Java.Atom argsAsList = methodInvoke(classAtom(Arrays.class), AS_LIST, args.atom);

        retval.withAtom(evalCall(cast(newType(JavascriptFunction.class), methodToCall), argsAsList))
                .withOperand(args);

        return visitSuffixesOfMember(retval, ctx.memberSuffix());
    }

    @Override
    public AtomWrapper visitParensCall(final SnapExpressionsParser.ParensCallContext ctx) {
        AtomWrapper retval = new AtomWrapper();
        Java.Atom methodToCall = retval.withOperandAndReturnAtom(visit(ctx.exp()));
        AtomWrapper args = visit(ctx.arguments());
        Java.Atom argsAsList = methodInvoke(classAtom(Arrays.class), AS_LIST, args.atom);

        return retval.withAtom(evalCall(cast(newType(JavascriptFunction.class), methodToCall),
                argsAsList))
                .withOperand(args);
    }

    @Override
    public AtomWrapper visitParens(SnapExpressionsParser.ParensContext ctx) {
        AtomWrapper result = visit(ctx.exp());

        return visitSuffixesOfMember(result, ctx.memberSuffix());
    }

    @Override
    public AtomWrapper visitEval(final SnapExpressionsParser.EvalContext ctx) {
        return visit(ctx.exp());
    }

    @Override
    public AtomWrapper visitJsregex(SnapExpressionsParser.JsregexContext ctx) {
        return new AtomWrapper(ctx.Regex(), declareConst(Regex.class, compileRegex(ctx.value)
                .toRvalue()));
    }

    @Override
    public AtomWrapper visitArray(SnapExpressionsParser.ArrayContext ctx) {
        AtomWrapper retval = new AtomWrapper();
        Token stopToken = ctx.getStop();

        if (stopToken.getType() != SnapExpressionsParser.RBRACKET) {
            Token startToken = ctx.getStart();
            String objText = ctx.getText();

            throw new ExecutionException(BailErrorStrategy.extractSubExpression(objText,
                    objText.length(), objText.length() - 1))
                    .withReason(String.format("Array literal starting at line %d:%d was not " +
                            "terminated", startToken.getLine(), startToken
                            .getCharPositionInLine()))
                    .withResolution("Insert a right curly bracket (]) to close the literal");
        }

        if (CollectionUtils.isEmpty(ctx.expOrSpread())) {
            return new AtomWrapper(ctx.LBRACKET(), newArrayListWithValues(Collections
                    .EMPTY_LIST));
        }

        return buildList(ctx.expOrSpread());
    }

    private AtomWrapper buildList(final List<SnapExpressionsParser.ExpOrSpreadContext> elems) {
        AtomWrapper retval = new AtomWrapper();
        Java.Block block = blockStack.peek();
        String litName = String.format("lit%s", litCounter++);
        Java.Atom builder = listBuilder();
        Java.LocalVariableDeclarationStatement declarationStatement = new Java
                .LocalVariableDeclarationStatement(null, new Java.Modifiers().add(Mod.FINAL),
                newType(ListBuilder.class), new Java.VariableDeclarator[] {
                new Java.VariableDeclarator(null, litName, 0, builder.toRvalue())
        });
        block.addStatement(declarationStatement);
        for (SnapExpressionsParser.ExpOrSpreadContext context : elems) {
            Java.Atom value = retval.withOperandAndReturnAtom(visit(context.exp()));
            if (context.ELLIPSIS() != null) {
                block.addStatement(exprStmt(listAddAll(var(litName), value)));
            } else {
                block.addStatement(exprStmt(listAdd(var(litName), value)));
            }
        }
        return retval.withAtom(build(var(litName)));
    }

    @Override
    public AtomWrapper visitInOperator(SnapExpressionsParser.InOperatorContext ctx) {
        if (ctx.equality().size() < 2) {
            return visit(ctx.equality(0));
        }
        List<SnapExpressionsParser.EqualityContext> contexts = ctx.equality();
        AtomWrapper retval = new AtomWrapper();
        Java.Atom result = retval.withOperandAndReturnAtom(visit(contexts.get(0)));
        for (int i = 1; i < contexts.size(); i++) {
            result = evalIn(result, retval.withOperandAndReturnAtom(visit(contexts.get(i))));
        }
        return retval.withAtom(result);
    }

    /**
     * Simple override methods
     */

    @Override
    public AtomWrapper visitTrue(SnapExpressionsParser.TrueContext ctx) {
        return new AtomWrapper(ctx.True(), bool(true));
    }

    @Override
    public AtomWrapper visitFalse(SnapExpressionsParser.FalseContext ctx) {
        return new AtomWrapper(ctx.False(), bool(false));
    }

    @Override
    public AtomWrapper visitDecimalNumber(SnapExpressionsParser.DecimalNumberContext ctx) {
        if (this.varMap.containsKey(ctx.Digit.getText())) {
            return new AtomWrapper(ctx.Digit(), var(this.varMap.get(ctx.Digit.getText()))
                    .toRvalue());
        }
        Java.Atom num = declareConst(BigInteger.class, newBigInteger(string(ctx.value.toString()))
                .toRvalue());
        varMap.put(ctx.Digit.getText(),String.format("CONST%d", constCounter - 1));
        return new AtomWrapper(ctx.Digit(), num);
    }

    @Override
    public AtomWrapper visitHexNumber(SnapExpressionsParser.HexNumberContext ctx) {
        if (this.varMap.containsKey(ctx.HexNumber.getText())) {
            return new AtomWrapper(ctx.HexNumber(), var(this.varMap.get(ctx.HexNumber.getText()))
                    .toRvalue());
        }
        Java.Atom num = declareConst(BigInteger.class, newBigInteger(string(ctx.value.toString()))
                .toRvalue());
        varMap.put(ctx.HexNumber.getText(),String.format("CONST%d", constCounter - 1));
        return new AtomWrapper(ctx.HexNumber(), num);
    }

    @Override
    public AtomWrapper visitOctalNumber(SnapExpressionsParser.OctalNumberContext ctx) {
        if (this.varMap.containsKey(ctx.OctNumber.getText())) {
            return new AtomWrapper(ctx.OctNumber(), var(this.varMap.get(ctx.OctNumber.getText()))
                    .toRvalue());
        }
        Java.Atom num = declareConst(BigInteger.class, newBigInteger(string(ctx.value.toString()))
                .toRvalue());
        varMap.put(ctx.OctNumber.getText(),String.format("CONST%d", constCounter - 1));
        return new AtomWrapper(ctx.OctNumber(), num);
    }

    @Override
    public AtomWrapper visitFpnum(SnapExpressionsParser.FpnumContext ctx) {
        if (this.varMap.containsKey(ctx.Float.getText())) {
            return new AtomWrapper(ctx.Float(), var(this.varMap.get(ctx.Float.getText()))
                    .toRvalue());
        }
        Java.Atom num = declareConst(BigDecimal.class, newBigDecimal(string(ctx.value.toString()))
                .toRvalue());
        varMap.put(ctx.Float.getText(),String.format("CONST%d", constCounter - 1));
        return new AtomWrapper(ctx.Float(), num);
    }
    @Override
    public AtomWrapper visitInfinity(SnapExpressionsParser.InfinityContext ctx) {
        return new AtomWrapper(ctx.Infinity(), fieldAccessExpr(classAtom(Double.class),
                POSITIVE_INFINITY));
    }

    @Override
    public AtomWrapper visitString(SnapExpressionsParser.StringContext ctx) {
        // Input string will have all the escape characters removed already
//        if (this.varMap.containsKey((ctx.qstring()).getText())) {
//            return var(this.varMap.get(ctx.qstring().getText())).toRvalue();
//        }
        String input = ctx.qstring().QuotedString().toString();
        String sanitizedString = LiteralUtils.unescapeString(input);
        sanitizedString = StringEscapeUtils.escapeJava(sanitizedString);
        return new AtomWrapper(ctx.qstring().QuotedString(), string(sanitizedString));
        /* This is Optimizing string Constants works for all junit tests but curious
        what you think, & if its neccesary. */
//        if (sanitizedString.startsWith("$") || sanitizedString.startsWith("_") ||
//                !CharMatcher.ASCII.matchesAllOf(input)) {
//
//        }
//        Java.Atom v = declareConst(String.class, string(sanitizedString).toRvalue());
//        varMap.put(ctx.qstring().getText(), String.format("CONST%d", constCounter - 1));
//        return v;
    }

    @Override
    public AtomWrapper visitNull(SnapExpressionsParser.NullContext ctx) {
        return new AtomWrapper(ctx.Null(), new Java.NullLiteral(null, NULL));
    }

    @Override
    public AtomWrapper visitNaN(SnapExpressionsParser.NaNContext ctx) {
        return new AtomWrapper(ctx.NaN(), fieldAccessExpr(classAtom(Double.class), FIELD_NAN));
    }


    //---------------------------- Protected Methods --------------------------------------------//

    protected Java.Atom declareConst(Java.Type type, Java.Rvalue initializer) {
        String name = String.format("CONST%d", constCounter++);
        Pair<Java.Type, Java.VariableDeclarator[]> pair = Pair.of(type, new Java
                .VariableDeclarator[] { new Java.VariableDeclarator(BLANK_LOC, name, 0,
                initializer) });

        constants.add(pair);

        return var(name);
    }

    protected Java.Atom declareConst(Class cl, Java.Rvalue initializer) {
        return declareConst(newType(cl), initializer);
    }

    /**
     * Capture a value into a local.  Primarily used for capturing global variable references in
     * arrow functions.
     *
     * @param initializer The value to store in the local
     * @return The name of local.
     */
    protected Java.Atom captureIntoLocal(String varname, Java.Rvalue initializer) {
        if (captures.containsKey(varname)) {
            Java.LocalVariableDeclarationStatement decl = captures.get(varname);

            return var(decl.variableDeclarators[0].name);
        }

        String name = String.format("CAPTURE%d", captureCounter++);
        Java.LocalVariableDeclarationStatement decl = new Java
                .LocalVariableDeclarationStatement(null, new Java.Modifiers().add(Mod.FINAL),
                newType(Object.class), new Java.VariableDeclarator[] {
                new Java.VariableDeclarator(null, name, 0, initializer)
        });

        captures.put(varname, decl);

        return var(name);
    }
}