import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.expression.*;
import com.snaplogic.grammars.SnapExpressionsLexer;
import com.snaplogic.grammars.SnapExpressionsParser;
import com.snaplogic.snap.api.SnapDataException;
import com.snaplogic.util.DefaultValueHandler;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.tuple.Pair;
import sl.EvaluatorUtils;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;


public class ExpressionEnv implements Externalizable {

    private HashMap<String, Object> jsonData;
    private HashMap<String, Object> envParam;
    ParseTree tree;
    ScopeStack scopeStack;

    public ExpressionEnv(){
        jsonData = new HashMap<String, Object>() {{
            put("firstName", "Dean");
            put("age", new BigInteger("26"));
            put("title", "student");
        }};

        envParam = new HashMap<String, Object>() {{
            put("foo", "bar");
            put("param", "'FIRST_NAME = ' + $firstName");
        }};
    }

    public ExpressionEnv(Map<String, Object> jsonData, ParseTree tree, ScopeStack scopeStack) {

        this.jsonData = new HashMap<String, Object>(jsonData);
        this.envParam = new HashMap<String, Object>();
        this.tree = tree;
        this.scopeStack = scopeStack;
    }

    public ExpressionEnv(Map<String, Object> jsonData) {

        this.jsonData = new HashMap<String, Object>(jsonData);
        this.envParam = new HashMap<String, Object>();
    }

    public ExpressionEnv(ParseTree tree, ScopeStack scopeStack) {
        this.tree = tree;
        this.scopeStack = scopeStack;
    }

    public ExpressionEnv(ScopeStack scopeStack) {
        this.scopeStack = scopeStack;
    }

//    public void init(String inputStr, Map<String, Object> envData) {
//
//        tree = InitializeANTLR(inputStr);
//        scopeStack = InitializeEnvData(envData);
//    }

    public <T> T eval(String inputStr, Object data) {

        JaninoStringGeneratorVisitor visitor = new JaninoStringGeneratorVisitor(data, null, null);

        Pair<ParseTree, JaninoStringGeneratorVisitor> parseTreeVisitorPair = Pair.of(tree, visitor);
        JaninoStringGeneratorVisitor janinoStringGeneratorVisitor = parseTreeVisitorPair.getRight();
        SnapLogicExpression evaluator = janinoStringGeneratorVisitor.buildExpression(inputStr, parseTreeVisitorPair.getKey());
        try {
            Object retval = evaluator.evaluate(data, scopeStack, new DefaultValueHandler());

            if (retval instanceof Number) {
                boolean validNumber = false;

                if (retval instanceof BigDecimal || retval instanceof BigInteger) {
                    validNumber = true;
                }
                if (retval instanceof Double) {
                    double dval = (Double) retval;
                    if (Double.isInfinite(dval) || Double.isNaN(dval)) {
                        validNumber = true;
                    }
                }
                if (!validNumber) {
                    fail("Expression language numbers should be BitIntegers or BigDecimals");
                }
            }
            return (T) retval;
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable th) {
            throw new SnapDataException(th, "Unhandled exception");
        } finally {
            EvaluatorUtils.ExpressionContext expressionContext = EvaluatorUtils
                    .CONTEXT_THREAD_LOCAL.get();
            assertNull(expressionContext.scopes);
        }
    }
//    public Pair<ParseTree, JaninoStringGeneratorVisitor> parse(String inputStr, Object data) {
//        CharStream input = new ANTLRInputStream(inputStr);
//        SnapExpressionsLexer lexer = new BailSnapExpressionsLexer(inputStr, input);
//        TokenStream tokens = new CommonTokenStream(lexer);
//        SnapExpressionsParser parser = new SnapExpressionsParser(tokens);
//        parser.removeErrorListeners();
//        parser.setErrorHandler(new BailErrorStrategy(inputStr));
//        ParseTree tree = parser.eval();
//        JaninoStringGeneratorVisitor visitor = new JaninoStringGeneratorVisitor(data, null,
//                null);
//        return Pair.of(tree, visitor);
//    }

    static public ParseTree InitializeANTLR(String inputStr){
        CharStream input = new ANTLRInputStream(inputStr);
        SnapExpressionsLexer lexer = new BailSnapExpressionsLexer(inputStr, input);
        TokenStream tokens = new CommonTokenStream(lexer);
        SnapExpressionsParser parser = new SnapExpressionsParser(tokens);
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy(inputStr));
        ParseTree tree = parser.eval();

        return tree;
    }
    static public ScopeStack InitializeEnvData(Map<String, Object> envData) {
        ScopeStack scopeStack = new ScopeStack();
        scopeStack.push(new GlobalScope());
        if (envData != null) {
            scopeStack.push(new EnvironmentScope(envData));
        }
        return scopeStack;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }
}
