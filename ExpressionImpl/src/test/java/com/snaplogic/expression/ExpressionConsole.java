package com.snaplogic.expression;

import com.snaplogic.common.expressions.ScopeStack;
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
import org.apache.flink.types.Row;
import row.SnapRow;
import sl.EvaluatorUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * You can type in any snaplogic expression language to test the expression language functionality
 * We have a json data and a env parameter in this class
 * Json data:
 * {
 *     "firstName":"Dean",
 *     "age":26,
 *     "title":"student"
 * }
 * You can access jason data by using $, such as $firstName, $age, $title
 * For more details, please go to https://docs-snaplogic.atlassian.net/wiki/spaces/SD/pages/1438170/JSONPath
 * Env parameter:
 *
 * foo=bar
 * param='FIRST_NAME' = $firstName
 *
 * You can access env parameter by using _, such as _foo, _param
 * For more details, please go to https://docs-snaplogic.atlassian.net/wiki/spaces/SD/pages/1438163/Parameters+and+Fields
 */
public class ExpressionConsole {

//    private HashMap<String, Object> jsonData;
//    private SnapRow rowData;
    private Row row;
    private HashMap<String, Object> envParam;

    public ExpressionConsole(){
//        jsonData = new HashMap<String, Object>() {{
//            put("name", new HashMap<String, Object>(){{
//                put("firstName","Yiding");
//                put("lastName","Liu");
//            }});
//            put("age", new BigInteger("26"));
//            put("title", "student");
//        }};

//        HashMap<String, Integer> fieldMap = new HashMap<String,Integer>(){{
//            put("firstName",0);
//            put("lastName",1);
//        }};
//        SnapRow nameData = new SnapRow(2,fieldMap);
//        nameData.setField(0,"Yiding");
//        nameData.setField(1,"Liu");
//        rowData = new SnapRow(3);
//        rowData.setField("name",0,nameData);
//        rowData.setField("age",1,new BigInteger("26"));
//        rowData.setField("title",2,"student");

        row= new Row(3);
        Row name = new Row(2);
        name.setField(0,"Yiding");
        name.setField(1,"Liu");
        row.setField(0,name);
        row.setField(1,new BigInteger("23"));
        row.setField(2,"student");
        envParam = new HashMap<String, Object>() {{
            put("foo", "bar");
            put("param", "'FIRST_NAME = ' + $firstName");
            put("name",0);
            put("age",1);
            put("title",2);
            put("firstName",0);
            put("lastName",1);
        }};
    }

    public static void main(String[] args) {
        ExpressionConsole console = new ExpressionConsole();
        Scanner scanner = new Scanner(System.in);
        String line = "";
        System.out.println("Ready for input");
        while(true){
            line = scanner.nextLine();
            if(line.equals("exit") || line.equals("q")) return;
            if(line.charAt(0)!='$'){
                System.out.println(console.eval(String.format("eval(%s)",line),console.row,console.envParam).toString());
            }
            else {
                System.out.println(console.eval(line,console.row,console.envParam).toString());
            }
        }
    }

    public <T> T eval(String inputStr, Object data, Map<String, Object> envData) {
        ParseTree tree = InitializeANTLR(inputStr);
        ScopeStack scopeStack = InitializeEnvData(envData);

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
                    double dval = (double) retval;
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
    public ParseTree InitializeANTLR(String inputStr){
        CharStream input = new ANTLRInputStream(inputStr);
        SnapExpressionsLexer lexer = new BailSnapExpressionsLexer(inputStr, input);
        TokenStream tokens = new CommonTokenStream(lexer);
        SnapExpressionsParser parser = new SnapExpressionsParser(tokens);
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy(inputStr));
        ParseTree tree = parser.eval();

        return tree;
    }
    public ScopeStack InitializeEnvData(Map<String, Object> envData) {
        ScopeStack scopeStack = new ScopeStack();
        scopeStack.push(new GlobalScope());
        if (envData != null) {
            scopeStack.push(new EnvironmentScope(envData));
        }
        return scopeStack;
    }
}
