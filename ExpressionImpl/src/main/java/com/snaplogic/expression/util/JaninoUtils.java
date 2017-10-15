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

package com.snaplogic.expression.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.snaplogic.api.ExecutionException;
import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.Regex;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.compiler.Location;
import org.codehaus.janino.Java;
import org.codehaus.janino.Mod;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import sl.EvaluatorUtils;
import sl.ListBuilder;
import sl.MapBuilder;

import javax.script.Bindings;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * JaninoUtils holds various janino related utility methods.
 *
 * @author ksubramanian
 * @since 2015
 */
public class JaninoUtils {

    private static final String BYTE_ARRAY = "byte[]";
    private static final String CREATE_REGEX = "createRegex";
    protected static final Location BLANK_LOC = new Location("blank", (short) 0, (short) 0);
    public static final Location PEEK_LOC = new Location("peek", (short) 0, (short) 0);

    private static final CharSequenceTranslator ESCAPE_JAVA =
            new LookupTranslator(
                    new String[][] {
                            {"\"", "\\\""},
                            {"\\", "\\\\"},
                    }).with(
                    new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE())
            );

    public static Java.Atom arraysAsList(final List<Java.Atom> argsAtoms) {
        return methodInvoke(classAtom(Arrays.class), AS_LIST, rValues(argsAtoms.toArray(new Java
                .Atom[0])));
    }

    public static Java.Atom trim(final Java.Atom stringAtom) {
        return methodInvoke(stringAtom, TRIM, EMPTY_ARGS);
    }

    public static Java.Atom newBigDecimal(final Java.Atom leftAtom) {
        return new Java.NewClassInstance(BLANK_LOC, null,
                newType(BigDecimal.class), new Java.Rvalue[]{leftAtom.toRvalue()});
    }

    public static Java.Type newType(Class<?> klass) {
        return new Java.ReferenceType(BLANK_LOC, splitClassName(klass), null);
    }

    public static Java.Type basicType(int type) {
        return new Java.BasicType(BLANK_LOC, type);
    }

    public static Java.Statement assign(Java.Atom lvalue, Java.Atom rvalue) {
        try {
            return new Java.ExpressionStatement(new Java.Assignment(BLANK_LOC, lvalue.toLvalue(),
                    "=", rvalue.toRvalue()));
        } catch (CompileException e) {
            throw new ExecutionException(e, "Unable to compile expression")
                    .withResolutionAsDefect();
        }
    }

    public static Java.Statement exprStmt(Java.Atom expr) {
        try {
            return new Java.ExpressionStatement(expr.toRvalue());
        } catch (CompileException e) {
            throw new ExecutionException(e, "Unable to compile expression")
                    .withResolutionAsDefect();
        }
    }

    public static Java.Atom string(final String value) {
        return new Java.StringLiteral(BLANK_LOC, quote(value));
    }

    public static Java.Atom rawStringToLiteral(String value) {
        return string(ESCAPE_JAVA.translate(value));
    }

    public static Java.Atom byteArray(final byte[] byteArrayValue) {
        Java.ArrayInitializerOrRvalue[] rvalueArray = new Java.ArrayInitializerOrRvalue[
                byteArrayValue.length];
        for (int i = 0; i < byteArrayValue.length; i++) {
            byte byteValue = byteArrayValue[i];
            rvalueArray[i] = new Java.IntegerLiteral(BLANK_LOC, Byte.toString(byteValue))
                    .toRvalue();
        }
        return new Java.NewInitializedArray(BLANK_LOC, new Java.ArrayType(basicType(Java
                .BasicType.BYTE)), new Java.ArrayInitializer(BLANK_LOC, rvalueArray));
    }

    public static Java.FunctionDeclarator.FormalParameter formalParameter(Java.Type type, String
            name) {
        return new Java.FunctionDeclarator.FormalParameter(BLANK_LOC, true, type, name);
    }

    public static Java.MethodDeclarator declareMethod(Java.Type returnType, String name, Java
            .FunctionDeclarator.FormalParameter[] parameters, Java.Block body) {
        return new Java.MethodDeclarator(BLANK_LOC, null, modifiers(Mod.PUBLIC),
                returnType, name, new Java.FunctionDeclarator.FormalParameters(BLANK_LOC,
                parameters, false), new Java.Type[0], Arrays.asList(body));
    }

    public static Java.Atom methodInvoke(Java.Atom targetAtom, String methodName,
                                         Java.Atom... args) {
        Java.Rvalue[] rvalueArgs = rValues(args);
        return methodInvoke(targetAtom, methodName, rvalueArgs);
    }

    public static Java.Rvalue[] rValues(final Java.Atom... args) {
        Java.Rvalue[] rvalueArgs = new Java.Rvalue[args.length];
        for (int i = 0; i < args.length; i++) {
            rvalueArgs[i] = args[i].toRvalue();
        }
        return rvalueArgs;
    }

    public static Java.Atom newBigInteger(Java.Atom... args) {
        return newClassInstance(newType(BigInteger.class), args);
    }

    public static Java.Atom newClassInstance(Java.Type type, Java.Atom... args) {
        return new Java.NewClassInstance(BLANK_LOC, null, type, rValues(args));
    }

    public static Java.Atom length(final Java.Atom atom) {
        return methodInvoke(atom, LENGTH);
    }

    public static Java.Atom methodInvoke(Java.Atom targetAtom, String methodName) {
        return new Java.MethodInvocation(BLANK_LOC, targetAtom, methodName, EMPTY_ARGS);
    }

    public static Java.Atom methodInvoke(Java.Atom targetAtom, String methodName,
                                         Java.Rvalue[] rvalueArgs) {
        return new Java.MethodInvocation(BLANK_LOC, targetAtom, methodName, rvalueArgs);
    }

    public static Java.Atom binaryOp(Java.Atom lhs, String operation, Java.Atom rhs) {
        return new Java.BinaryOperation(BLANK_LOC, lhs.toRvalue(), operation, rhs.toRvalue());
    }

    public static Java.Atom ifAtom(Java.Atom condition, Java.Atom result,
                                   Java.Atom elseResult) {
        return new Java.ConditionalExpression(BLANK_LOC, condition.toRvalue(), result.toRvalue(),
                elseResult.toRvalue());
    }

    public static Java.Statement ifStatement(Java.Atom condition, Java.Statement thenStatement,
            Java.Statement elseStatement) {
        return new Java.IfStatement(BLANK_LOC, condition.toRvalue(), thenStatement, elseStatement);
    }

    public static Java.Atom not(final Java.Atom paranthesizedExpr) {
        return new Java.UnaryOperation(BLANK_LOC, OP_NOT, paranthesizedExpr.toRvalue());
    }

    public static Java.Atom parenthesize(final Java.Atom instanceOf) {
        return new Java.ParenthesizedExpression(BLANK_LOC,
                instanceOf.toRvalue());
    }

    public static Java.Atom cast(Java.Type type, Java.Atom value) {
        return parenthesize(new Java.Cast(BLANK_LOC, type, value.toRvalue()));
    }

    public static String quote(String value) {
        return String.format(PATTERN_QUOTE, value);
    }

    public static Java.AmbiguousName var(final String name) {
        return new Java.AmbiguousName(BLANK_LOC, new String[]{name});
    }

    public static Java.AmbiguousName var(Location loc, final String name) {
        return new Java.AmbiguousName(loc, new String[]{name});
    }

    public static Java.Atom scopesVar() {
        return var("scopes");
    }

    public static Java.Atom docVar() {
        return var("doc");
    }

    public static Java.Atom handlerVar() {
        return var("handler");
    }

    public static Java.Modifiers modifiers(int ...mods) {
        Java.Modifiers retval = new Java.Modifiers();

        for (int mod : mods) {
            retval = retval.add(mod);
        }

        return retval;
    }

    public static Java.Statement declareVar(Class cl, String name, Java.ArrayInitializerOrRvalue
            initializer) {
        return new Java.LocalVariableDeclarationStatement(BLANK_LOC, modifiers(Mod.FINAL),
                newType(cl), new Java.VariableDeclarator[] {
                new Java.VariableDeclarator(BLANK_LOC, name, 0, initializer)
        });
    }

    public static Java.Statement declareCounter(String name) {
        return new Java.LocalVariableDeclarationStatement(BLANK_LOC, new Java.Modifiers(),
                basicType(Java.BasicType.INT), new Java.VariableDeclarator[] {
                new Java.VariableDeclarator(BLANK_LOC, name, 0, primitiveIntLiteral(0).toRvalue())
        });
    }

    public static Java.Statement declareArray(Class cl, String name, Java.ArrayInitializerOrRvalue
            initializer) {
        return new Java.LocalVariableDeclarationStatement(BLANK_LOC, modifiers(Mod.FINAL),
                newType(cl), new Java.VariableDeclarator[] {
                new Java.VariableDeclarator(BLANK_LOC, name, 1, initializer)
        });
    }

    public static Java.Atom eqNull(final Java.Atom valAtom) {
        return eq(valAtom, nullLiteral());
    }

    public static Java.Atom eq(Java.Atom lhs, Java.Atom rhs) {
        return binaryOp(lhs, OP_EQ, rhs);
    }

    public static Java.Rvalue nullLiteral() {
        return new Java.NullLiteral(BLANK_LOC, NULL);
    }

    public static Java.Atom intLiteral(int value) {
        return newBigInteger(string(String.valueOf(value)));
    }

    public static Java.Atom intLiteral(String value) {
        return newBigInteger(string(value));
    }

    public static Java.Atom primitiveIntLiteral(int value) {
        return new Java.IntegerLiteral(BLANK_LOC, String.valueOf(value));
    }

    public static Java.Atom primitiveDoubleLiteral(double value) {
        return new Java.FloatingPointLiteral(BLANK_LOC, String.valueOf(value));
    }

    public static Java.Atom primitiveNumberLiteral(JsonNode node) {
        switch (node.numberType()) {
            case INT:
            case LONG:
            case BIG_INTEGER:
                return primitiveIntLiteral(node.asInt());
            case FLOAT:
            case DOUBLE:
            case BIG_DECIMAL:
                return primitiveDoubleLiteral(node.asDouble());
        }

        throw new IllegalArgumentException();
    }

    public static Java.Atom toBoolean(Java.Atom value) {
        return methodInvoke(classAtom(ObjectType.class), "toBoolean", value);
    }

    public static Java.Atom dateTimeParse(DateTime dateTime) {
        return methodInvoke(classAtom(DateTime.class), PARSE, string(dateTime.toString()));
    }

    public static Java.Atom localDateParse(LocalDate localDate) {
        return methodInvoke(classAtom(LocalDate.class), PARSE, string(localDate.toString()));
    }

    public static Java.Atom localTimeParse(LocalTime localTime) {
        return methodInvoke(classAtom(LocalTime.class), PARSE, string(localTime.toString()));
    }

    public static Java.Atom localDateTimeParse(LocalDateTime localDateTim) {
        return methodInvoke(classAtom(LocalTime.class), PARSE, string(localDateTim.toString()));
    }

    public static Java.Atom bool(final Boolean value) {
        if (value) {
            return fieldAccessExpr(classAtom(Boolean.class), CAPITAL_TRUE);
        } else {
            return fieldAccessExpr(classAtom(Boolean.class), CAPITAL_FALSE);
        }
    }

    public static Java.Atom startsWith(Java.Atom argAtom, String str) {
        return methodInvoke(argAtom, STARTS_WITH, string(str));
    }

    public static Java.Atom gt(final Java.Atom lhs, final Java.Atom rhs) {
        return binaryOp(lhs, OP_GT, rhs);
    }

    public static Java.Atom listAdd(Java.Atom listBuilder, Java.Atom value) {
        return methodInvoke(listBuilder, ADD, value);
    }

    public static Java.Atom listAddAll(Java.Atom listBuilder, Java.Atom value) {
        return methodInvoke(listBuilder, ADD_ALL, value);
    }

    public static Java.Atom builder(Java.Type type) {
        return methodInvoke(type, BUILDER);
    }

    public static Java.Atom build(final Java.Atom builderAtom) {
        return methodInvoke(builderAtom, "get");
    }

    public static Java.Atom peekIntoBuilder(final Java.Atom builderAtom) {
        return new Java.MethodInvocation(PEEK_LOC, builderAtom, "get", EMPTY_ARGS);
    }

    public static Java.Atom mapPut(Java.Atom builderAtom, Java.Atom key, Java.Atom value) {
        return methodInvoke(builderAtom, PUT, key, value);
    }

    public static Java.Atom mapPutAll(Java.Atom builderAtom, Java.Atom value) {
        return methodInvoke(builderAtom, PUT_ALL, value);
    }

    /**
     * Get the value of an argument, or it's default, and return it.
     *
     * @param list The argument list.
     * @param index The index of the argument in the list.
     * @param defaultValue The default value, if the argument is not in the list.
     * @return An expression that returns the value of an argument.
     */
    public static Java.Atom getArg(Java.Atom list, Java.Atom index, Java.Atom defaultValue) {
        Java.Atom listSize = methodInvoke(list, "size");
        Java.Atom indexLessThanSize = binaryOp(index, "<", listSize);
        Java.Atom getter = methodInvoke(list, "get", index);

        // Note that we use a conditional expression here since we only want to evaluate the
        // default value if argument is not in there.
        return ifAtom(indexLessThanSize, getter, defaultValue);
    }

    public static Java.Atom mapBuilder() {
        return builder(newType(MapBuilder.class));
    }

    public static Java.Atom listBuilder() {
        return builder(newType(ListBuilder.class));
    }

    public static Java.Atom lt(final Java.Atom lhs, final Java.Atom rhs) {
        return binaryOp(lhs, OP_LT, rhs);
    }

    public static Java.Atom intValue(final Java.Atom atom) {
        return methodInvoke(atom, INT_VALUE);
    }

    public static Java.Atom split(final Java.Atom argAtom, Java.Atom splitStringAtom) {
        return methodInvoke(argAtom, SPLIT, splitStringAtom);
    }

    public static Java.Atom contains(final Java.Atom argAtom, Java.Atom stringAtom) {
        return methodInvoke(argAtom, CONTAINS, stringAtom);
    }

    public static Java.Atom newInitializedArray(Java.ArrayType arrayType, Java.Atom[] initValues) {
        return new Java.NewInitializedArray(BLANK_LOC, arrayType, arrayInitializer(initValues));
    }

    public static Java.Atom newInitializedArray(Java.ArrayType arrayType, List<Java.Atom>
            initValues) {
        return newInitializedArray(arrayType, initValues.toArray(new Java.Atom[initValues.size()]));
    }

    public static Java.ArrayInitializer arrayInitializer(final Java.Atom[] atoms) {
        return new Java.ArrayInitializer(BLANK_LOC, rValues(atoms));
    }

    public static Java.ArrayType arrayType(Java.Type type) {
        return new Java.ArrayType(type);
    }

    public static Java.Atom get(Java.Atom atom, Java.Atom field) {
        return methodInvoke(atom, GET, field);
    }

    public static Java.Atom newInstance(final Class<?> classObject) {
        return new Java.NewClassInstance(BLANK_LOC, null, new Java.ReferenceType(BLANK_LOC,
                splitClassName(classObject), null), null);
    }

    public static Java.Atom newInstance(Java.Type classType) {
        return new Java.NewClassInstance(BLANK_LOC, null, classType, null);
    }

    public static String[] splitClassName(Class<?> className) {
        return className.getName().split(REGEX_SPLIT_AT_DOT);
    }

    public static Java.Atom classAtom(Class<?> klass) {
        return ambiguousName(splitClassName(klass));
    }

    public static Java.AmbiguousName ambiguousName(String[] name) {
        return new Java.AmbiguousName(BLANK_LOC, name);
    }

    public static Java.Atom size(final Java.Atom atom) {
        return methodInvoke(atom, SIZE);
    }

    public static Java.Atom fieldInstance(Class<?> lhsClass) {
        return fieldAccessExpr(classAtom(lhsClass), INSTANCE);
    }

    public static Java.Atom fieldAccessExpr(Java.Atom lhs, String fieldName) {
        return new Java.FieldAccessExpression(BLANK_LOC, lhs, fieldName);
    }

    public static Java.Atom context() {
        return fieldAccessExpr(classAtom(EvaluatorUtils.class), "CONTEXT_THREAD_LOCAL");
    }

    public static Java.Atom compileRegex(final Regex regex) {
        Bindings bindings = regex.getBindings();
        String pattern = (String) bindings.get(LiteralUtils.PATTERN);
        String flags = (String) bindings.get(LiteralUtils.FLAGS);
        return runtimeEval(CREATE_REGEX, rawStringToLiteral(pattern), rawStringToLiteral(flags));
    }

    public static Java.Atom evalLogicalXor(final List<Java.Atom> atoms) {
        return runtimeEval(LOGICAL_XOR_EVAL, arraysAsList(atoms));
    }

    public static Java.Atom evalAdd(List<Java.Atom> atoms, Java.Atom operators) {
        return runtimeEval(ADDITION, arraysAsList(atoms), operators);
    }

    public static Java.Atom evalIn(Java.Atom atom, Java.Atom object) {
        return runtimeEval(IN, atom, object);
    }

    public static Java.Atom evalMultiply(List<Java.Atom> atoms, Java.Atom operators) {
        return runtimeEval(MULTIPLY, arraysAsList(atoms), operators);
    }

    public static  Java.Atom evalUnary(Java.Atom value, Java.Atom operator) {
        return runtimeEval(UNARY, value, operator);
    }

    public static  Java.Atom getTypedMethods(Java.Atom function) {
        return runtimeEval(GET_TYPE, function);
    }

    public static  Java.Atom evalGlobal(Java.Atom functionType, List<Java.Atom> args) {
        return runtimeEval(GLOBAL, functionType, docVar(), scopesVar(), arraysAsList(args),
                handlerVar());
    }

    public static Java.Atom evalCall(Java.Atom method, Java.Atom args) {
        return methodInvoke(method, "eval", args);
    }

    public static Java.Atom evalRelational(List<Java.Atom> values, Java.Atom opType) {
        return runtimeEval(RELATIONAL, arraysAsList(values), opType);
    }

    public static Java.Atom evalEquality(List<Java.Atom> atoms, List<Java.Atom> operators) {
        return runtimeEval(EQUALITY, arraysAsList(atoms), arraysAsList(operators));
    }

    public static Java.Atom evalMethod(Java.Atom functionName, Java.Atom
            currentPath, Java.Atom pair, Java.Atom member, Java.Atom args) {
        return runtimeEval(METHOD, scopesVar(), handlerVar(), functionName, currentPath,
                pair, member, args);
    }

    public static Java.Atom evalLookupVariable(Java.Atom name) {
        return runtimeEval("lookupVariable", scopesVar(), handlerVar(), name);
    }

    public static Java.Atom lookupInScope(Java.Atom name) {
        return methodInvoke(scopesVar(), "lookupVariable", name);
    }

    public static Java.Atom finishLookup(Java.Atom var, Java.Atom name) {
        return runtimeEval("finishLookup", var, scopesVar(), handlerVar(), name);
    }

    public static Java.Atom evalPropertyRef(Java.Atom currentPath, Java.Atom text, Java.Atom obj,
                                            Java.Atom field) {
        return runtimeEval(PROPERTY_REF, currentPath, text, obj, field, handlerVar());
    }

    public static Java.Atom handleValue(Java.Atom value, Java.Atom text) {
        return methodInvoke(handlerVar(), "handleValue", value, text, bool(false));
    }

    public static Java.Atom evalIndexSuffix(Java.Atom result, Java.Atom index,
                                            Java.Atom currentPath, Java.Atom text) {
        return runtimeEval(INDEX_SUFFIX, result, index, currentPath, text, handlerVar());
    }

    public static Java.Atom runtimeEval(String function, Java.Atom... atoms) {
        return methodInvoke(classAtom(EvaluatorUtils.class), function, atoms);
    }

    public static Java.Atom jsonPathCompile(Java.Atom path) {
        return runtimeEval("jsonPathCompile", path);
    }

    public static Java.Atom patternCompile(String pattern) {
        return methodInvoke(classAtom(Pattern.class), "compile", rawStringToLiteral(pattern));
    }

    public static Java.Atom newArrayList(Java.Atom... args) {
        return newClassInstance(newType(ArrayList.class), args);
    }

    public static Java.Atom newArrayListWithValues(final List<Java.Atom> result) {
        return newArrayList(arraysAsList(result));
    }

    public static Java.Atom containsKey(Java.Atom atom, String field) {
        return methodInvoke(atom, CONTAINS_KEY, string(field));
    }

    public static Java.Atom evalInstanceOf(Java.Atom leftAtom, Java.Atom rightAtom) {
        return methodInvoke(leftAtom, EVAL_INSTANCE_OF, rightAtom);
    }

    public static Java.Atom compareTo(final Java.Atom left, final Java.Atom right) {
        return methodInvoke(left, COMPARE_TO, right);
    }

    public static Java.Atom objectToType(final Java.Atom atom) {
        return methodInvoke(classAtom(ObjectType.class), OBJECT_TO_TYPE, atom);
    }

    public static Java.Atom getMillis(final Java.Atom atom) {
        return methodInvoke(atom, GET_MILLIS, EMPTY_ARGS);
    }

    public static Java.Atom checkReturnValue(final Java.Atom atom) {
        return runtimeEval("checkReturnValue", atom);
    }

    public static Java.Atom isInstance(Java.Atom left, Class type) {
        return new Java.Instanceof(BLANK_LOC, left.toRvalue(), newType(type));
    }

    public static Java.Statement returnStatement() {
        return new Java.ReturnStatement(BLANK_LOC, null);
    }

    public static Java.Statement returnStatement(Java.Atom value) {
        return new Java.ReturnStatement(BLANK_LOC, value != null ? value.toRvalue() : null);
    }

    //------------------------------------ Constants --------------------------------------------//
    public static final String REGEX_SPLIT_AT_DOT = "\\.";
    public static final String CAPITAL_FALSE = "FALSE";
    public static final String CAPITAL_TRUE = "TRUE";
    public static final String LENGTH = "length";
    public static final String FIELD_NAN = "NaN";
    public static final String POSITIVE_INFINITY = "POSITIVE_INFINITY";
    public static final String LOGICAL_OR_EVAL = "logicalOrEval";
    public static final String LOGICAL_AND_EVAL = "logicalAndEval";
    public static final String LOGICAL_XOR_EVAL = "logicalXorEval";
    public static final String CONTAINS = "contains";
    public static final String TRIM = "trim";
    public static final String SPLIT = "split";
    public static final String NULL = "null";;
    public static final String MULTIPLY = "multiply";
    public static final String UNARY = "unary";
    public static final String STARTS_WITH = "startsWith";
    public static final String COMPARE_TO = "compareTo";
    public static final String ON = "on";
    public static final String GET = "get";
    public static final String INSTANCE = "INSTANCE";
    public static final String AS_LIST = "asList";
    public static final String PATTERN_QUOTE = "\"%s\"";
    public static final String NAME = "name";
    public static final String INT_VALUE = "intValue";
    public static final String SIZE = "size";
    public static final String CONTAINS_KEY = "containsKey";
    public static final String PUT = "put";
    public static final String PUT_ALL = "putAll";
    public static final String COMPILE_REGEX = "compileRegex";
    public static final String EVAL_INSTANCE_OF = "evalInstanceOf";
    public static final String OBJECT_TO_TYPE = "objectToType";
    public static final String GET_MILLIS = "getMillis";
    public static final String ADDITION = "addition";
    public static final String RELATIONAL = "relational";
    public static final String EXP = "exp";
    public static final String GLOBAL = "globalFunction";
    public static final String EQUALITY = "equality";
    public static final String METHOD = "method";
    public static final String PROPERTY_REF = "propertyRef";
    public static final String INDEX_SUFFIX = "indexSuffix";
    public static final String BUILDER = "builder";
    public static final String BUILD = "build";
    public static final String ADD = "add";
    public static final String ADD_ALL = "addAll";
    public static final String PARSE = "parse";
    public static final String IN = "in";
    public static final String GET_TYPE = "getTypedMethods";

    /**
     * Operators
     */
    public static final String OP_NOT = "!";
    public static final String OP_EQ = "==";
    public static final String OP_GT = ">";
    public static final String OP_LT = "<";

    /**
     * Others
     */
    public static final Java.Rvalue[] EMPTY_ARGS = new Java.Rvalue[]{};
}
