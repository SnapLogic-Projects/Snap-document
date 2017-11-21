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

package sl;

import com.snaplogic.api.ExecutionException;
import com.snaplogic.common.expressions.DataValueHandler;
import com.snaplogic.common.expressions.Scope;
import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.common.jsonpath.InvalidPathException;
import com.snaplogic.common.jsonpath.JsonPath;
import com.snaplogic.expression.ExpressionUtil;
import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.PathReference;
import com.snaplogic.expression.Regex;
import com.snaplogic.expression.SnapLogicExpression;
import com.snaplogic.expression.classes.JavascriptClass;
import com.snaplogic.expression.methods.JavascriptFunction;
import com.snaplogic.expression.methods.Method;
import com.snaplogic.expression.methods.TypedMethod;
import com.snaplogic.expression.methods.UnknownMethodException;
import com.snaplogic.expression.methods.array.ListMethod;
import com.snaplogic.expression.methods.bytearray.ByteArrayMethod;
import com.snaplogic.expression.methods.date.DateMethod;
import com.snaplogic.expression.methods.map.MapMethod;
import com.snaplogic.expression.methods.number.NumberMethod;
import com.snaplogic.expression.methods.string.StringMethod;
import com.snaplogic.expression.util.LiteralUtils;
import com.snaplogic.grammars.SnapExpressionsParser;
import com.snaplogic.jsonpath.JsonPaths;
import com.snaplogic.snap.api.SnapDataException;
import com.snaplogic.util.DefaultValueHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.flink.types.Row;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import row.SnapRow;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.Bindings;
import javax.script.SimpleBindings;

import static sl.Messages.*;

/**
 * EvaluatorUtils is the class providing static utility methods to carry out expression
 * evaluation. Package naming is deliberately small to keep the generated code compact.
 *
 * @author ksubramanian
 * @since 2015
 */
public final class EvaluatorUtils {
    /**
     * Holds some context when doing an expression evaluation.
     */
    public static class ExpressionContext {
        public Object doc;
        public ScopeStack scopes;
        public DataValueHandler<Object> handler;
        // Impure methods, like Math.random(), should set this flag to let the optimizer know that
        // the results of the method cannot be reused.
        public boolean impure = false;
        // This flag is set when an impure function is called so that we can propagate impurity
        // through user-defined functions.
        public boolean callerImpure = false;

        public void clear() {
            doc = null;
            scopes = null;
            handler = null;
            impure = false;
            callerImpure = false;
        }

        public boolean consumeImpureFlag() {
            boolean retval = this.impure;
            this.impure = false;
            if (retval) {
                callerImpure = true;
            }
            return retval;
        }

        public void propagateImpure() {
            consumeImpureFlag();
            impure = callerImpure;
            callerImpure = false;
        }
    }

    /**
     * Thread-local that stores the current context for this expression evaluation
     */
    public static final class ContextThreadLocal extends ThreadLocal<ExpressionContext> {

        @Override
        protected ExpressionContext initialValue() {
            return new ExpressionContext();
        }

        public void setContext(Object doc, ScopeStack scopes, DataValueHandler<Object> handler) {
            ExpressionContext expressionContext = get();
            expressionContext.doc = doc;
            expressionContext.scopes = scopes;
            expressionContext.handler = handler;
        }

        public void clear() {
            get().clear();
        }

        // This should be called right after a method call to check for impurity.
        public boolean consumeImpureFlag() {
            return get().consumeImpureFlag();
        }

        // This method should be called at the end of arrow functions to propagate impurity to the
        // expressions that called the arrow function.
        public void propagateImpure() {
            get().propagateImpure();
        }
    }

    public static final ContextThreadLocal CONTEXT_THREAD_LOCAL = new ContextThreadLocal();
    protected static final ConcurrentHashMap<String, TypedMethod[]>
            FUNC_TO_TYPE = new ConcurrentHashMap<>();

    /**
     * Performs a logical-or as per javascript requirement.
     *
     * @param values
     * @return logicalOr
     */
    public static Object logicalOrEval(List values) {
        Object value = null;
        for (int i = 0; i < values.size(); i++) {
            value = values.get(i);
            if (ObjectType.toBoolean(value)) {
                return value;
            }
        }
        return value;
    }

    /**
     * Performs a logical-and as per javascript requirement.
     *
     * @param values
     * @return logicalOr
     */
    public static Object logicalAndEval(List values) {
        Object value = null;
        for (int i = 0; i < values.size(); i++) {
            value = values.get(i);
            if (!ObjectType.toBoolean(value)) {
                return value;
            }
        }
        return value;
    }

    /**
     * Performs a logical-xor as per javascript requirement.
     *
     * @param values
     * @return logicalXor
     */
    public static Object logicalXorEval(List values) {
        Object value = null;
        if (values.size() > 1) {
            Object left = values.get(0);
            for (int i = 1; i < values.size(); i++) {
                Object right = values.get(i);
                try {
                    Boolean leftVal = (Boolean) left;
                    Boolean rightVal = (Boolean) right;
                    left = leftVal ^ rightVal;
                } catch (ClassCastException e) {
                    ObjectType leftType = ObjectType.objectToType(left);
                    ObjectType rightType = ObjectType.objectToType(right);
                    throw new SnapDataException(e, BOOLEAN_OPERATOR_EXPECTS)
                            .formatWith(OR_OP, leftType.getName(), rightType.getName())
                            .withReason(BOOLEAN_OPERATOR_REQUIRES)
                            .withResolution(CHECK_OPERAND_TYPES);
                }
            }
            return left;
        } else if (values.size() == 1) {
            return values.get(0);
        }
        return value;
    }

    private static Object mathOperation(List values, int[] operators) {
        Object value = values.get(0);
        ObjectType valueType = ObjectType.objectToType(value);
        if (values.size() > 1) {
            for (int i = 1; i < values.size(); i++) {
                Object right = values.get(i);
                int operator = operators[i - 1];
                try {
                    value = handleMathOperation(value, right, operator);
                } catch (Exception e) {
                    ObjectType rightType = ObjectType.objectToType(right);
                    throw new SnapDataException(MATH_OPERATOR_TYPE_MISMATCH)
                            .formatWith(getCapitalizedOpName(operator), valueType.getName(),
                                    rightType.getName())
                            .withReason(String.format(MATH_OPERATOR_REQUIRES_NUMBERS, operator))
                            .withResolution(CHECK_OPERAND_TYPES);
                }
            }
        }
        return value;
    }

    public static TypedMethod[] getTypedMethods(String functionName) {

        if (FUNC_TO_TYPE.containsKey(functionName)) {
            return FUNC_TO_TYPE.get(functionName);
        }

        ObjectType[] enumTypes = ObjectType.values();
        TypedMethod pair[] = new TypedMethod[enumTypes.length];
        Method found;

        for (ObjectType type : enumTypes) {
            try {
                switch (type) {
                    case STRING :
                        found = StringMethod.getMethod(functionName);
                        pair[ObjectType.STRING.ordinal()] = new TypedMethod(ObjectType.
                                STRING, found);
                        break;
                    case MAP:
                        found = MapMethod.getMethod(functionName);
                        pair[ObjectType.MAP.ordinal()] = new TypedMethod(ObjectType.
                                MAP, found);
                        break;
                    case BIG_INTEGER:
                    case BIG_DECIMAL:
                    case INTEGER:
                    case DOUBLE:
                    case SHORT:
                    case FLOAT:
                    case LONG:
                        found = NumberMethod.getMethod(functionName);
                        pair[ObjectType.INTEGER.ordinal()] = new TypedMethod(ObjectType.
                                INTEGER, found);
                        break;
                    case LIST:
                        found = ListMethod.getMethod(functionName);
                        pair[ObjectType.LIST.ordinal()] = new TypedMethod(ObjectType.
                                LIST, found);
                        break;
                    case DATE:
                        found = DateMethod.getMethod(functionName);
                        pair[ObjectType.DATE.ordinal()] = new TypedMethod(ObjectType.
                                DATE, found);
                        break;
                    case BYTE_ARRAY:
                        found = ByteArrayMethod.getMethod(functionName);
                        pair[ObjectType.BYTE_ARRAY.ordinal()] = new TypedMethod(ObjectType.
                                BYTE_ARRAY, found);
                        break;
                    default:
                        break;
                }
            } catch (UnknownMethodException e) {
            }
        }
        FUNC_TO_TYPE.put(functionName, pair);
        return pair;
    }

    public static Object addition(List values, int[] operators) {
        return mathOperation(values, operators);
    }

    public static Object multiply(List values, int[] operators) {
        return mathOperation(values, operators);
    }

    public static Object unary(Object val, int op) {
        if (op == -1) {
            return val;
        }
        Object result;
        switch (op) {
            case SnapExpressionsParser.TYPEOF:
                if (val instanceof Number) {
                    result = TYPEOF_NUMBER;
                } else if (val != null && val.getClass().isArray() || val instanceof List) {
                    result = TYPEOF_ARRAY;
                } else if (val instanceof Boolean) {
                    result = TYPEOF_BOOLEAN;
                } else if (val instanceof String) {
                    result = TYPEOF_STRING;
                } else {
                    result = TYPEOF_OBJECT;
                }
                break;
            case SnapExpressionsParser.ADD:
                result = ObjectType.attemptToConvertToBigDecimal(val);
                if (!(result instanceof Number)) {
                    result = Double.NaN;
                }
                break;
            case SnapExpressionsParser.SUB:
                if (val == null) {
                    val = BigInteger.ZERO;
                }
                try {
                    Object maybeDecimal = ObjectType.attemptToConvertToBigDecimal(val);
                    if (maybeDecimal instanceof Double) {
                        Double d = (Double) maybeDecimal;

                        if (d == Double.POSITIVE_INFINITY) {
                            result = Double.NEGATIVE_INFINITY;
                        } else if (d == Double.NEGATIVE_INFINITY) {
                            result = Double.POSITIVE_INFINITY;
                        } else {
                            result = maybeDecimal;
                        }
                    } else {
                        BigDecimal bigDecimal = (BigDecimal) maybeDecimal;
                        result = bigDecimal.negate();
                    }
                } catch (ClassCastException e) {
                    result = Double.NaN;
                }
                break;
            default:
                result = !ObjectType.toBoolean(val);
                break;
        }
        return result;
    }

    public static Object relational(List values, int opType) {
        if (values.size() < 2) {
            return values.get(0);
        }
        Object result;
        Object left = values.get(0);
        Object right = values.get(1);
        if (opType == SnapExpressionsParser.INSTANCEOF) {
            if (!(right instanceof JavascriptClass)) {
                throw new SnapDataException(EXPECTING_TYPE_FOR_INSTANCEOF)
                        .formatWith(right)
                        .withReason(THE_INSTANCEOF_OPERATOR_REQUIRES_A_TYPE)
                        .withResolution(INSTANCEOF_RESOLUTION);
            }
            JavascriptClass javascriptClass = (JavascriptClass) right;
            result = javascriptClass.evalInstanceOf(left);
        } else {
            try {
                int compareVal = getCompareVal(left, right);
                switch (opType) {
                    case SnapExpressionsParser.GT:
                        result = compareVal > 0;
                        break;
                    case SnapExpressionsParser.GTE:
                        result = compareVal >= 0;
                        break;
                    case SnapExpressionsParser.LTE:
                        result = compareVal <= 0;
                        break;
                    default:
                        result = compareVal < 0;
                }
            } catch (SnapDataException e) {
                // Bubble up data errors
                throw e;
            } catch (Exception e) {
                // Any other error return false
                result = false;
            }
        }
        return result;
    }

    public static Regex createRegex(String pattern, String flags) {
        Bindings bindings = new SimpleBindings();
        bindings.put(LiteralUtils.PATTERN, pattern);
        bindings.put(LiteralUtils.FLAGS, flags);
        return new Regex(bindings, pattern, flags);
    }

    public static JsonPath jsonPathCompile(String path) {
        try {
            return JsonPaths.compile(path);
        } catch (InvalidPathException e) {
            throw new SnapDataException(e, INVALID_JSON_PATH)
                    .formatWith(path)
                    .withReason(e.getMessage())
                    .withResolution(e.getResolution());
        }
    }

    public static Object jsonPathRead(JsonPath path, ScopeStack scopes, Object obj) {
        try {
            return path.withScopes(scopes).read(obj);
        } catch (InvalidPathException e) {
            throw new SnapDataException(e, INVALID_JSON_PATH)
                    .formatWith(path)
                    .withReason(e.getMessage())
                    .withResolution(e.getResolution());
        }
    }

    public static Object equality(List values, List operators) {
        Object left = values.get(0);
        if (values.size() > 1) {
            for (int i = 1; i < values.size(); i++) {
                int operator = ((Number) operators.get(i - 1)).intValue();
                Object right = values.get(i);
                ObjectType leftType = ObjectType.objectToType(left);
                ObjectType rightType = ObjectType.objectToType(right);
                boolean leftEqualsRight;
                if (leftType != rightType) {
                    left = ObjectType.attemptToConvertToBigDecimal(left);
                    right = ObjectType.attemptToConvertToBigDecimal(right);
                }
                leftEqualsRight = ObjectType.equals(left, right);
                switch (operator) {
                    case SnapExpressionsParser.EQUALS:
                        left = leftEqualsRight;
                        break;
                    default:
                        left = !leftEqualsRight;
                }
            }
        }
        return left;
    }

    public static Object method(ScopeStack scopes, DataValueHandler handler, String functionName,
                                String currentPath, TypedMethod pair[], Object member, Object[] args) {
        try {
            return methodInternal(scopes, handler, functionName, currentPath, pair,
                    member, args);
        } catch (UnknownMethodException e) {
            undefinedResolution(currentPath, e.getMethodNames(), functionName, "methods", new
                    SnapDataException("%s type does not have a method named: %s")
                    .formatWith(ObjectType.objectToType(member).getName(), functionName));
        }
        return null;
    }

    /*
    This method is checking to see if the method Object collected at compile time matches the
    Objects type at runtime, and if so Eliminates the extra hash lookup to find the method
    object, which has already been done at compile time. With this optimization,
    The getMethod hash lookup, will only happen once at compile time for every built
    in method.
     */
    public static Object methodInternal(ScopeStack scopes, DataValueHandler handler, String
            functionName, String currentPath, TypedMethod pair[], Object member, Object[] argsA) {
        ObjectType memberType = ObjectType.objectToType(member);
        // TODO tstack: We should just pass the object array around instead of creating a list.
        List<Object> args = Arrays.asList(argsA);

        if (pair[memberType.ordinal()] != null) {
            return pair[memberType.ordinal()].method.evaluate(member, args);
        }
        switch (memberType) {
            case STRING:
                return StringMethod.getMethod(functionName).evaluate(member, args);
            case CLASS:
                return ((JavascriptClass) member).evalStaticMethod(scopes, functionName, args);
            case REGEXP:
                return Regex.getMethod(functionName).evaluate(member, args);
            case LIST:
                return ListMethod.getMethod(functionName).evaluate(member, args);
            case MAP:
                Map<String, Object> map = (Map<String, Object>) member;
                Object obj = map.get(functionName);
                if (obj instanceof JavascriptFunction) {
                    JavascriptFunction func = ((JavascriptFunction) obj);

                    try {
                        return func.eval(args);
                    } catch (StackOverflowError e) {
                        throw new SnapDataException(e, "Too much recursion in method: %s")
                                .formatWith(functionName)
                                .withReason("The function called itself too many times")
                                .withResolution("Ensure there is a stop condition when using " +
                                        "recursion");
                    }
                } else if (obj != null) {
                    throw new SnapDataException("Property '%s' is not a function")
                            .formatWith(functionName)
                            .withReason(String.format("Property value is a %s and not a function",
                                    ObjectType.objectToType(obj)))
                            .withResolution(PLEASE_CHECK_YOUR_EXPRESSION);
                }
                return MapMethod.getMethod(functionName).evaluate(member, args);
            case DATE:
                return DateMethod.getMethod(functionName).evaluate(member, args);
            case BIG_DECIMAL:
            case BIG_INTEGER:
            case INTEGER:
            case DOUBLE:
            case SHORT:
            case FLOAT:
            case LONG:
                return NumberMethod.getMethod(functionName).evaluate(member, args);
            case BYTE_ARRAY:
                return ByteArrayMethod.getMethod(functionName).evaluate(member, args);
            case FUNCTION:
                if ("apply".equals(functionName)) {
                    List<Object> applyArgs;

                    if (args.size() > 1) {
                        Object arg1 = args.get(1);

                        if (!(arg1 instanceof List)) {
                            throw new SnapDataException("Invalid argument")
                                    .withReason(String.format("Expecting an array of arguments, " +
                                            "found: %s", ObjectType.objectToType(arg1)))
                                    .withResolution("Pass an array of arguments as the second " +
                                            "argument to this function");
                        }

                        applyArgs = ((List) arg1);
                    } else {
                        applyArgs = Collections.emptyList();
                    }

                    return ((JavascriptFunction) member).eval(applyArgs);
                }
            default:
                if (member != null && "toString".equals(functionName)) {
                    return ObjectType.toString(member);
                } else {
                    try {
                        return handler.handleUndefinedReference(null, functionName, currentPath);
                    } catch (SnapDataException e) {
                        if (member != null) {
                            throw new SnapDataException("Unknown method: %s")
                                    .formatWith(functionName)
                                    .withReason("Object has an unknown type (%s)", member.getClass()
                                            .getCanonicalName())
                                    .withResolution(PLEASE_CHECK_EXPRESSION_SYNTAX);
                        } else {
                            throw new SnapDataException("Cannot invoke a method on a null value")
                                    .withReason("Value referenced in the sub-expression '%s' is " +
                                                    "null",
                                            currentPath)
                                    .withResolution(PLEASE_CHECK_EXPRESSION_SYNTAX);
                        }
                    }
                }
        }
    }

    //-------------------------------- Other public methods ------------------------------------//


    public static Object handleMathOperation(Object left, Object right, int opType) {
        if (left instanceof Number || left instanceof Boolean) {
            left = ObjectType.attemptToConvertToBigDecimal(left);
            if (right instanceof String) {
                try {
                    right = new BigDecimal(right.toString());
                } catch (NumberFormatException e) {
                    return executeStringOperation(left.toString(), right.toString(), opType);
                }
            } else if (!(right instanceof BigDecimal)) {
                right = ObjectType.attemptToConvertToBigDecimal(right);
                if (!(right instanceof BigDecimal)) {
                    return executeStringOperation(ObjectType.toString(left), ObjectType
                            .toString(right), opType);
                }
            }
            return executeBigDecimalOperation((BigDecimal) left, (BigDecimal) right, opType);
        }
        try {
            if (opType == SnapExpressionsParser.ADD) {
                return executeStringOperation(ObjectType.toString(left), right, opType);
            }
            left = ObjectType.attemptToConvertToBigDecimal(left);
            if (left instanceof Number) {
                return handleMathOperation(left, right, opType);
            } else {
                return executeStringOperation(ObjectType.toString(left), right, opType);
            }
        } catch (NumberFormatException e) {
            return executeStringOperation(ObjectType.toString(left), right, opType);
        }
    }

    public static Object executeStringOperation(String left, Object right, int op) {
        switch (op) {
            case SnapExpressionsParser.ADD: {
                String leftString = ObjectType.toString(left);
                String rightString = ObjectType.toString(right);

                return leftString.concat(rightString);
            }
            case SnapExpressionsParser.SUB:
                return Double.NaN;
            case SnapExpressionsParser.MULTIPLY:
                return Double.NaN;
            case SnapExpressionsParser.DIVIDE:
                return Double.NaN;
            case SnapExpressionsParser.MOD:
                return Double.NaN;
            default:
                throw new SnapDataException(OPERATOR_NOT_SUPPORTED)
                        .formatWith(op)
                        .withReason(String.format(OPERATOR_NOT_SUPPORTED, op))
                        .withResolution(PLEASE_CHECK_EXPRESSION_SYNTAX);
        }
    }

    public static Object executeBigDecimalOperation(BigDecimal leftNum, BigDecimal rightNum,
                                                    int op) {
        switch (op) {
            case SnapExpressionsParser.ADD:
                return leftNum.add(rightNum, MathContext.DECIMAL128);
            case SnapExpressionsParser.SUB:
                return leftNum.subtract(rightNum, MathContext.DECIMAL128);
            case SnapExpressionsParser.MULTIPLY:
                return leftNum.multiply(rightNum, MathContext.DECIMAL128);
            case SnapExpressionsParser.DIVIDE:
                try {
                    return leftNum.divide(rightNum, MathContext.DECIMAL128);
                } catch (ArithmeticException e) {
                    if (rightNum.compareTo(BigDecimal.ZERO) == 0) {
                        return Double.POSITIVE_INFINITY;
                    }
                    throw e;
                }
            case SnapExpressionsParser.MOD:
                return leftNum.remainder(rightNum, MathContext.DECIMAL128);
            default:
                throw new SnapDataException(OPERATOR_NOT_SUPPORTED)
                        .formatWith(op)
                        .withReason(String.format(OPERATOR_NOT_SUPPORTED, op))
                        .withResolution(PLEASE_CHECK_EXPRESSION_SYNTAX);
        }
    }

    public static Object tryToConvertString(String str) {
        try {
            return new BigInteger(str);
        } catch (NumberFormatException e) {
            return new BigDecimal(str);
        }
    }

    public static String getCapitalizedOpName(int opType) {
        return WordUtils.capitalize(getOpName(opType));
    }

    public static String getOpName(int opType) {
        switch (opType) {
            case SnapExpressionsParser.GT:
                return GREATER_THAN;
            case SnapExpressionsParser.GTE:
                return GREATER_THAN_OR_EQUAL_TO;
            case SnapExpressionsParser.LT:
                return LESS_THAN;
            case SnapExpressionsParser.LTE:
                return LESS_THAN_OR_EQUAL_TO;
            case SnapExpressionsParser.ADD:
                return ADDITION;
            case SnapExpressionsParser.SUB:
                return SUBTRACTION;
            case SnapExpressionsParser.MULTIPLY:
                return MULTIPLICATION;
            case SnapExpressionsParser.DIVIDE:
                return DIVISION;
            case SnapExpressionsParser.MOD:
                return MODULUS;
            case SnapExpressionsParser.AND:
                return AND;
            case SnapExpressionsParser.OR:
                return OR;
            case SnapExpressionsParser.XOR:
                return XOR;
            case SnapExpressionsParser.INSTANCEOF:
                return INSTANCEOF;
            case SnapExpressionsParser.IN:
                return IN_OPERATOR;
            default:
                throw new ExecutionException(OPERATOR_NOT_SUPPORTED)
                        .formatWith(opType)
                        .withReason(String.format(OPERATOR_NOT_SUPPORTED, opType))
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION);
        }
    }

    public static int getCompareVal(Object left, Object right) {
        ObjectType leftType = ObjectType.objectToType(left);
        ObjectType rightType = ObjectType.objectToType(right);
        switch (leftType) {
            case LOCAL_DATETIME:
                // Class cast exception will be caught by caller and false will be returned
                // if not same type
                return ((LocalDateTime) left).compareTo((LocalDateTime) right);
            case LOCAL_TIME:
                // Class cast exception will be caught by caller and false will be returned
                // if not same type
                return ((LocalTime) left).compareTo((LocalTime) right);
            case LOCAL_DATE:
                // Class cast exception will be caught by caller and false will be returned
                // if not same type
                return ((LocalDate) left).compareTo((LocalDate) right);
            case DATE:
                return compareDate((DateTime) left, right, rightType);
            case BYTE_ARRAY: {
                int retval = 0;
                if (right == null) {
                    return 1;
                }
                byte[] leftBits = (byte[]) left;
                byte[] rightBits = (byte[]) right;

                for (int i = 0; i < Math.min(leftBits.length, rightBits.length); i++) {
                    retval = Byte.compare(leftBits[i], rightBits[i]);
                    if (retval != 0) {
                        return retval;
                    }
                }

                if (leftBits.length < rightBits.length) {
                    return -1;
                } else if (leftBits.length == rightBits.length) {
                    return 0;
                }
                return 1;
            }
            case BOOLEAN:
                left = (Boolean) left ? BigInteger.ONE : BigInteger.ZERO;
            case BIG_DECIMAL:
            case BIG_INTEGER:
            case INTEGER:
            case DOUBLE:
            case SHORT:
            case FLOAT:
            case LONG:
                return ObjectType.compareNumber((Number) left, right, rightType);
            case STRING:
                return ObjectType.compareNumber(new BigDecimal((String) left), right, rightType);
            case NULL:
                if (right == null) {
                    return 0;
                }
                return -1;
            default:
                // Note: This is going to be caught in visitRelational
                throw new ExecutionException(TYPE_NOT_SUPPORTED_FOR_COMPARISON)
                        .formatWith(leftType.getName())
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION);
        }
    }

    public static int compareDate(final DateTime left, Object right, ObjectType rightType) {
        if (rightType == ObjectType.STRING) {
            try {
                right = tryToConvertString((String) right);
                rightType = ObjectType.objectToType(right);
            } catch (Exception e) {
                // Do nothing preserve original data type
            }
        }
        switch (rightType) {
            case DATE:
                return left.compareTo((DateTime) right);
            case BOOLEAN:
                right = ObjectType.booleanToInt((Boolean) right);
            case BIG_DECIMAL:
            case BIG_INTEGER:
            case INTEGER:
            case DOUBLE:
            case SHORT:
            case FLOAT:
            case LONG:
                return new BigDecimal(String.valueOf(left.getMillis())).compareTo(
                        new BigDecimal(right.toString()));
            default:
                throw new SnapDataException(CANNOT_COMPARE_VALUE_TO_DATE)
                        .formatWith(right)
                        .withReason(String.format(CANNOT_COMPARE_VALUE_TO_DATE, right))
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION);
        }
    }

    /**
     * Handle an eval() function call.
     *
     * @param inputStr The expression to evaluate.
     * @return The result of the evaluation.
     */
    public static Object handleEval(String inputStr) {
        try {
            SnapLogicExpression expr = ExpressionUtil.compile(inputStr);
            ExpressionContext expressionContext = CONTEXT_THREAD_LOCAL.get();
            CONTEXT_THREAD_LOCAL.remove();

            try {
                return expr.evaluate(expressionContext.doc, expressionContext.scopes,
                        expressionContext.handler);
            } finally {
                CONTEXT_THREAD_LOCAL.setContext(expressionContext.doc, expressionContext.scopes,
                        expressionContext.handler);
            }
        } catch (SnapDataException e) {
            throw e;
        } catch (ExecutionException e) {
            throw new SnapDataException(e, e.getMessage())
                    .withReason(e.getReason())
                    .withResolution(e.getResolution());
        } catch (Throwable th) {
            throw new SnapDataException(th, "Unexpected error occurred while evaluating " +
                    "expression: %s")
                    .formatWith(inputStr)
                    .withResolution("Please check your expression");
        }
    }

    public static Object propertyRef(String path, String text, Object obj, String field,DataValueHandler<Object> handler) {
        ObjectType currentObjType = ObjectType.objectToType(obj);
        switch (currentObjType) {
            case SNAP_ROW:
                SnapRow sr = (SnapRow)obj;
                return sr.getField(field);
            case ROW:
                ScopeStack ss = EvaluatorUtils.CONTEXT_THREAD_LOCAL.get().scopes;
                Integer index = (Integer) ss.get(1).get(String.format("_%s",field)) ;
                Row row = (Row) obj;
                return row.getField(index);
            case MAP:
                Map<?, ?> map = (Map<?, ?>) obj;
                Object val = map.get(field);
                if (val == null && !map.containsKey(field)) {
                    try {
                        return handler.handleUndefinedReference(null, field, path);
                    } catch (SnapDataException e) {
                        undefinedProperty(path, map.keySet(), field);
                    }
                }
                return val;
            case PATH_REFERENCE:
                return new PathReference(path);
            case STRING:
                if (StringUtils.equals(field, PROPERTY_LENGTH)) {
                    String str = (String) obj;
                    return BigInteger.valueOf(str.length());
                }
            case LIST:
                if (StringUtils.equals(field, PROPERTY_LENGTH)) {
                    List<?> list = (List) obj;
                    return BigInteger.valueOf(list.size());
                }
            case BYTE_ARRAY:
                if (StringUtils.equals(field, PROPERTY_LENGTH)) {
                    byte[] bits = (byte[]) obj;
                    return BigInteger.valueOf(bits.length);
                }
            case CLASS:
                if (obj instanceof Scope) {
                    Scope scope = ((Scope) obj);
                    Object value = scope.get(field);
                    if (value == Scope.UNDEFINED) {
                        try {
                            value = handler.handleUndefinedReference(null, text, path);
                        } catch (SnapDataException e) {
                            undefinedProperty(path, scope.keySet(), field);
                        }
                    }
                    return value;
                }
            case NULL:
                try {
                    return handler.handleUndefinedReference(null, field, path);
                } catch (SnapDataException e) {
                    throw new SnapDataException("Cannot lookup a property on a null value")
                            .withReason("Value referenced in the sub-expression '%s' is null",
                                    path)
                            .withResolution(PLEASE_CHECK_YOUR_EXPRESSION);
                }
            default:
                throw new SnapDataException(EVAL_ACCESSOR_FAILED)
                        .formatWith(field)
                        .withReason(String.format(KEY_ACCESS_ON_NON_KEY_VALUE,
                                ObjectType.objectToType(obj).getName()))
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION);
        }
    }

    public static Object indexSuffix(Object currentObj, Object index, String currentPath,
                                     String text, DataValueHandler<Object> handler) {
        ObjectType objectType = ObjectType.objectToType(currentObj);
        switch (objectType) {
            case MAP: {
                Map<Object, Object> map = (Map<Object, Object>) currentObj;
                String stringResult = String.valueOf(index);
                Object memberValue = map.get(stringResult);
                if (memberValue == null && !map.containsKey(stringResult)) {
                    try {
                        memberValue = handler.handleUndefinedReference(null, stringResult,
                                currentPath);
                    } catch (SnapDataException e) {
                        undefinedProperty(currentPath, map.keySet(), stringResult);
                    }
                }
                return memberValue;
            }
            case LIST:
            case STRING:
            case BYTE_ARRAY: {
                Number num = ObjectType.toNumber(index, Double.NaN);

                if (ObjectType.isNaN(num)) {
                    throw new SnapDataException(INVALID_ACCESSOR_IN_PATH)
                            .formatWith(currentObj, currentPath)
                            .withReason(EXPECTING_AN_INTEGER_TO_ACCESS_AN_ARRAY_ELEMENT)
                            .withResolution(PLEASE_CHECK_YOUR_EXPRESSION);
                }

                if (objectType == ObjectType.LIST) {
                    List<?> list = (List<?>) currentObj;
                    try {
                        return list.get(num.intValue());
                    } catch (IndexOutOfBoundsException e) {
                        throw new SnapDataException(e, BAD_ARRAY_INDEX)
                                .formatWith(num.intValue(), list.size())
                                .withReason(ARRAY_DOES_NOT_CONTAIN_THE_GIVEN_INDEX)
                                .withResolution(PLEASE_CHECK_YOUR_EXPRESSION);
                    }
                }
                if (objectType == ObjectType.STRING) {
                    String str = (String) currentObj;
                    try {
                        int intValue = num.intValue();
                        return str.substring(intValue, intValue + 1);
                    } catch (IndexOutOfBoundsException e) {
                        throw new SnapDataException(e, BAD_STRING_INDEX)
                                .formatWith(num.intValue(), str.length())
                                .withReason(STRING_DOES_NOT_CONTAIN_THE_GIVEN_INDEX)
                                .withResolution(PLEASE_CHECK_YOUR_EXPRESSION);
                    }
                }
                if (objectType == ObjectType.BYTE_ARRAY) {
                    byte[] bits = (byte[]) currentObj;
                    try {
                        int intValue = num.intValue();
                        return BigInteger.valueOf(bits[intValue] & 0xff);
                    } catch (IndexOutOfBoundsException e) {
                        throw new SnapDataException(e, BAD_STRING_INDEX)
                                .formatWith(num.intValue(), bits.length)
                                .withReason("Byte array does not contain the given index")
                                .withResolution(PLEASE_CHECK_YOUR_EXPRESSION);
                    }
                }
            }
            default:
                return handler.handleUndefinedReference(null, index.toString(), currentPath);
        }
    }

    public static Object finishLookup(Object retval, ScopeStack scopes, DataValueHandler<Object>
            handler, String name) {
        if (retval == Scope.UNDEFINED) {
            try {
                retval = handler.handleUndefinedReference(null, name, name);
            } catch (SnapDataException e) {
                undefinedProperty(name, scopes.keySet(), name);
            }
        }
        return retval;
    }

    public static Object lookupVariable(ScopeStack scopes, DataValueHandler<Object> handler, String
            name) {
        return finishLookup(scopes.lookupVariable(name), scopes, handler, name);
    }

    public static Object checkReturnValue(Object obj) {
        if (obj instanceof JavascriptFunction) {
            throw new SnapDataException("Unsupported expression result: %s")
                    .formatWith(obj)
                    .withReason("Arrow functions cannot be used as the result of an " +
                            "expression")
                    .withResolution("If you meant to call the function, append parentheses, like " +
                            "so: (x => x + 1)(1)");
        }
        return obj;
    }

    public static Object in(final Object arg, final Object member) {
        if (!(member instanceof Map) && !(member instanceof List)) {
            throw new SnapDataException("Unsupported type used for in operator: %s")
                    .formatWith(member)
                    .withReason("Only arrays and objects can be used on the " +
                            "right-hand-side of the 'in' operator")
                    .withResolution("Please check the type of the object " +
                            "and assure it is an array or a map");
        }
        if (member instanceof Map && ((Map) member).containsKey(ObjectType.toString(arg))) {
            return true;
        }
        if (arg instanceof BigInteger || arg instanceof BigDecimal) {
            int index;
            if (arg instanceof BigDecimal) {
                try {
                    BigInteger bigindex = ((BigDecimal) arg).toBigIntegerExact();
                    index = bigindex.intValue();
                } catch (Exception ArithmeticException) {
                    return false;
                }
            } else {
                index = ((BigInteger) arg).intValue();
            }
            if (index > -1 && member instanceof List && index < ((List) member).size()) {
                return true;
            }
        }
        return false;
    }

    private static class Possibility implements Comparable<Possibility> {
        public final int distance;
        public final String value;

        private Possibility(final int distance, final String value) {
            this.distance = distance;
            this.value = value;
        }

        @Override
        public int compareTo(final Possibility o) {
            int retval = Integer.compare(distance, o.distance);

            if (retval == 0) {
                retval = value.compareTo(o.value);
            }

            return retval;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static void undefinedStaticMethod(String className, Set keySet, String methodName) {
        undefinedRef(String.format("%s.%s", className, methodName), keySet, methodName, "methods");
    }

    private static Object[] getPossibleProperties(Set keySet, String undefinedKey) {
        List retval = new ArrayList<>();

        for (Object keyObject : keySet) {
            if (!(keyObject instanceof String)) {
                continue;
            }

            String key = ((String) keyObject);
            int distance = StringUtils.getLevenshteinDistance(key, undefinedKey);

            if (distance < 4) {
                retval.add(new Possibility(distance, key));
            }
        }

        if (retval.isEmpty()) {
            retval.addAll(keySet);
        }

        Collections.sort(retval);

        return retval.subList(0, Math.min(5, retval.size())).toArray();
    }

    private static final String IN_RESOLUTION = "You can check for the existence of a field with " +
            "the 'in' operator (e.g. 'abc' in $obj)";

    private static void undefinedProperty(String path, Set keySet, String field) {
        undefinedRef(path, keySet, field, "properties");
    }

    private static void undefinedRef(String path, Set keySet, String field, String type) {
        SnapDataException ex = new SnapDataException("%s is undefined")
                .formatWith(path);

        undefinedResolution(path, keySet, field, type, ex);
    }

    private static void undefinedResolution(String path, Set keySet, String field, String type,
                                            SnapDataException ex) {
        Object[] possibilities = getPossibleProperties(keySet, field);

        if (!field.equals(path)) {
            ex.withReason("'%s' was not found while evaluating the sub-expression '%s'", field,
                    path);
        }

        if (path.equals(field) && possibilities.length > 0) {
            ex.withResolution("Perhaps you meant one of the following %s: %s",
                    type, StringUtils.join(possibilities, ", "));
        } else if (possibilities.length > 0) {
            ex.withResolution("%s or perhaps you meant to reference one of the following " +
                    "%s: %s", IN_RESOLUTION, type, StringUtils.join(possibilities,  ", "));
        } else {
            ex.withResolution(IN_RESOLUTION);
        }
        throw ex;
    }

    //------------------------------------- Private constants------------------------------------//
    protected static final String GREATER_THAN = "greater than";
    protected static final String GREATER_THAN_OR_EQUAL_TO = "greater than or equal to";
    protected static final String LESS_THAN = "less than";
    protected static final String LESS_THAN_OR_EQUAL_TO = "less than or equal to";
    protected static final String ADDITION = "addition";
    protected static final String SUBTRACTION = "subtraction";
    protected static final String MULTIPLICATION = "multiplication";
    protected static final String DIVISION = "division";
    protected static final String MODULUS = "modulus";
    protected static final String AND = "and";
    protected static final String OR = "or";
    protected static final String XOR = "xor";
    protected static final String INSTANCEOF = "instanceof";
    protected static final String OR_OP = "Or";
    protected static final String TYPEOF_NUMBER = "number";
    protected static final String TYPEOF_ARRAY = "array";
    protected static final String TYPEOF_BOOLEAN = "boolean";
    protected static final String TYPEOF_STRING = "string";
    protected static final String TYPEOF_OBJECT = "object";
    protected static final String IN_OPERATOR = "in";
    protected static final String GET_TYPE = "getTypedMethods";
    public static final DefaultValueHandler DEFAULT_VALUE_HANDLER = new DefaultValueHandler();
}
