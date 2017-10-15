package com.snaplogic.expression.methods.string;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.snaplogic.api.ExecutionException;
import com.snaplogic.expression.methods.Method;
import com.snaplogic.expression.methods.UnknownMethodException;
import com.snaplogic.expression.methods.object.ObjectMethod;
import com.snaplogic.expression.util.ScriptEngineProvider;
import com.snaplogic.snap.api.SnapDataException;

import javax.script.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.snaplogic.expression.methods.string.Messages.EVALUATE_EXPRESSION_FAILED;
import static com.snaplogic.expression.methods.string.Messages.PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX;
import static com.snaplogic.expression.methods.string.Messages.UNABLE_TO_COMPILE_JS_EXPRESSION;
import static com.snaplogic.expression.methods.string.Messages.UNEXPECTED_JAVA_SCRIPT_ENGINE_RESULT;

/**
 * Wrapper class for string functions in the expression language.
 *
 * @author jinloes
 */
public class StringMethod extends ObjectMethod {
    private static final String METHOD_SEPARATOR = ",";
    private static final ScriptEngine SCRIPT_ENGINE;
    private static final ThreadLocal<SimpleScriptContext> SCRIPT_CONTEXT = new
            ThreadLocal<SimpleScriptContext>() {
                @Override
                protected SimpleScriptContext initialValue() {
                    return new SimpleScriptContext();
                }
            };

    static {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngineProvider scriptEngineProvider = new ScriptEngineProvider(scriptEngineManager,
                ScriptEngineProvider.JAVASCRIPT, ScriptEngineProvider.JAVASCRIPT_ENGINE_ORDER);

        SCRIPT_ENGINE = scriptEngineProvider.get();
    }


    @SuppressWarnings("HardCodedStringLiteral")
    private static final Map<String, Method> STRING_METHODS = ImmutableMap.<String, Method>builder()
            .put("indexOf", IndexOf.INSTANCE)
            .put("match", Match.INSTANCE)
            .put("charAt", CharAt.INSTANCE)
            .put("charCodeAt", CharCodeAt.INSTANCE)
            .put("concat", Concat.INSTANCE)
            .put("contains", Contains.INSTANCE)
            .put("endsWith", EndsWith.INSTANCE)
            .put("lastIndexOf", LastIndexOf.INSTANCE)
            .put("localeCompare", LocaleCompare.INSTANCE)
            .put("repeat", Repeat.INSTANCE)
            .put("replace", Replace.INSTANCE)
            .put("replaceAll", ReplaceAll.INSTANCE)
            .put("search", Search.INSTANCE)
            .put("slice", Slice.INSTANCE)
            .put("split", Split.INSTANCE)
            .put("sprintf", Sprintf.INSTANCE)
            .put("startsWith", StartsWith.INSTANCE)
            .put("substr", SubStr.INSTANCE)
            .put("substring", Substring.INSTANCE)
            .put("toLowerCase", ToLowerCase.INSTANCE)
            .put("toUpperCase", ToUpperCase.INSTANCE)
            .put("trim", Trim.INSTANCE)
            .put("trimLeft", TrimLeft.INSTANCE)
            .put("trimRight", TrimRight.INSTANCE)
            .build();

    public static final Method getMethod(String methodName) {
        Method method = STRING_METHODS.get(methodName);
        if (method == null) {
            throw new UnknownMethodException(STRING_METHODS.keySet());
        }
        return method;
    }

    /**
     * Statically compile a JavaScript expression using the builtin JS script engine.
     *
     * @param expr The expression to compile.
     * @return The compiled version of the expression.
     */
    public static CompiledScript compileJavaScript(String expr) {
        Compilable compilable = (Compilable) SCRIPT_ENGINE;
        try {
            return compilable.compile(expr);
        } catch (ScriptException e) {
            throw new ExecutionException(e, UNABLE_TO_COMPILE_JS_EXPRESSION)
                    .formatWith(expr)
                    .withResolutionAsDefect();
        }
    }

    /**
     * Evaluate a compiled script using the given bindings.
     *
     * @param script The script to execute.
     * @param bindings The variable bindings to use when executing the script.
     * @return The result of the execution.
     */
    public static Object evaluateJavaScript(CompiledScript script, Bindings bindings) {
        try {
            // Use a thread-local context since it's expensive to create for each execution.
            SimpleScriptContext context = SCRIPT_CONTEXT.get();
            context.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
            return script.eval(context);
        } catch (ScriptException e) {
            throw new SnapDataException(e, EVALUATE_EXPRESSION_FAILED)
                    .withReason(e.getMessage())
                    .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
        }
    }

    /**
     * Helper method that converts the result of a JavaScript expression evaluation into a
     * Java List.  Unfortunately, the return values from JDK 1.7 and before is different from
     * JDK 1.8, so we need to check for both possibilities.
     *
     * @param jsResult The result of an expression evaluation.
     * @return A List object.
     */
    public static List<Object> convertArrayToList(Object jsResult) {
        if (jsResult == null) {
            return null;
        }
        List<Object> retval = Lists.newArrayList();
        if (jsResult instanceof Bindings) {
            Bindings resultBindings = (Bindings) jsResult;
            for (int i = 0; ; i++) {
                String elem = (String) resultBindings.get(String.valueOf(i));
                if (elem == null) {
                    break;
                }
                retval.add(elem);
            }
        } else if (jsResult instanceof Collection) {
            Collection<Object> resultCollection = (Collection) jsResult;
            retval.addAll(resultCollection);
        } else {
            throw new ExecutionException(UNEXPECTED_JAVA_SCRIPT_ENGINE_RESULT)
                    .formatWith(jsResult)
                    .withResolutionAsDefect();
        }
        return retval;
    }
}

