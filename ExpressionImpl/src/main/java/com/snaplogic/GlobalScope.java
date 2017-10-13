package com.snaplogic;

import com.google.common.collect.ImmutableMap;
import com.snaplogic.expression.classes.JavascriptArray;
import com.snaplogic.expression.classes.JavascriptBoolean;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Container for any global objects in the expression language.
 *
 * @author tstack
 */
public class GlobalScope extends BasicScope {
    private static final Map<String, Object> BACKING = new ImmutableMap.Builder<String, Object>()
            .put("Array", JavascriptArray.INSTANCE)
            .put("Boolean", JavascriptBoolean.INSTANCE)
            .put("Date", Date.INSTANCE)
            .put("DateTime", DateTime.INSTANCE)
            .put("LocalDate", LocalDate.INSTANCE)
            .put("LocalDateTime", LocalDateTime.INSTANCE)
            .put("LocalTime", LocalTime.INSTANCE)
            .put("sl", SnapLogicExpressionClass.INSTANCE)
            .put("JSON", JsonExpressionClass.INSTANCE)
            .put("Base64", Base64ExpressionClass.INSTANCE)
            .put("GZip", GZipExpressionClass.INSTANCE)
            .put("Digest", DigestExpressionClass.INSTANCE)
            .put("Math", MathClass.INSTANCE)
            .put("Null", JavascriptNull.INSTANCE)
            .put("Object", JavascriptObject.INSTANCE)
            .put("Number", JavascriptNumber.INSTANCE)
            .put("String", JavascriptString.INSTANCE)
            .put("Uint8Array", JavascriptByteArray.INSTANCE)

            .put("decodeURIComponent", DecodeUriComponentFunction.INSTANCE)
            .put("encodeURIComponent", EncodeUriComponentFunction.INSTANCE)
            .put("eval", EvalFunction.INSTANCE)
            .put("isNaN", IsNaNFunction.INSTANCE)
            .put("jsonPath", JsonPathFunction.INSTANCE)
            .put("parseInt", ParseIntFunction.INSTANCE)
            .put("parseFloat", ParseFloatFunction.INSTANCE)
            .build();

    public GlobalScope() {
        super(BACKING);
    }

    /**
     * Implements the decodeURIComponent Javascript function.
     */
    public enum DecodeUriComponentFunction implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List args) {
            try {
                Object argVal = args.isEmpty() ? null : args.get(0);
                return URLDecoder.decode(ObjectType.toString(argVal), StandardCharsets
                        .UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                throw new ExecutionException(e, UTF_8_ENCODING_NOT_FOUND)
                        .withResolutionAsDefect();
            }
        }
    }

    /**
     * Implements the encodeURIComponent Javascript function.
     */
    public enum EncodeUriComponentFunction implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List args) {
            try {
                Object argVal = args.isEmpty() ? null : args.get(0);
                String encoded = URLEncoder.encode(ObjectType.toString(argVal),
                        StandardCharsets.UTF_8.name());
                return StringUtils.replace(encoded, "+", "%20");
            } catch (UnsupportedEncodingException e) {
                throw new ExecutionException(e, UTF_8_ENCODING_NOT_FOUND)
                        .withResolutionAsDefect();
            }
        }
    }

    /**
     * Implements the eval Javascript function.
     */
    public enum EvalFunction implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List args) {
            Object argVal = args.isEmpty() ? null : args.get(0);

            if (argVal instanceof String) {
                return EvaluatorUtils.handleEval((String) argVal);
            } else {
                return argVal;
            }
        }
    }

    /**
     * Implements the isNaN Javascript function.
     */
    public enum IsNaNFunction implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List args) {
            Object argVal = args.isEmpty() ? null : args.get(0);
            if ((argVal != null) && !(argVal instanceof Boolean) && (ObjectType.isNaN(argVal) ||
                    !(ObjectType.attemptToConvertToBigDecimal(argVal) instanceof Number))) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Implements the jsonPath Javascript function.
     */
    public enum JsonPathFunction implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List args) {
            Object argVal = args.isEmpty() ? null : args.get(0);
            if (args.size() > 1) {
                Object exprVal = args.get(1);
                try {
                    // TODO tstack: We can probably improve performance here
                    JsonPathExpressionService jsonPathExpressionService =
                            new JsonPathExpressionService();
                    JsonPath jsonPath = JsonPathImpl.compile(String.valueOf(exprVal),
                            jsonPathExpressionService);
                    return jsonPath.read(argVal);
                } catch (InvalidPathException e) {
                    throw new SnapDataException(e, INVALID_JSON_PATH)
                            .formatWith(exprVal)
                            .withReason(e.getMessage())
                            .withResolution(e.getResolution());
                }
            } else {
                return argVal;
            }
        }
    }

    /**
     * Implements the parseInt Javascript function.
     */
    public enum ParseIntFunction implements JavascriptFunction {
        INSTANCE;

        protected static final String[] HEX_PREFIXES = {"0x", "0X"};
        protected static final int HEX_RADIX = 16;
        protected static final String PERIOD = ".";
        protected static final String MINUS_PREFIX = "-";
        protected static final String PLUS_PREFIX = "+";

        @Override
        public Object eval(final List args) {
            Object argVal = null;
            if (!args.isEmpty()) {
                argVal = args.get(0);
            }
            ObjectType argType = ObjectType.objectToType(argVal);
            Object result;
            try {
                if (argVal == null) {
                    // XXX Restore old behavior where a null arg would return null.
                    return null;
                }
                if (argType.ordinal() > 7) {
                    return Double.NaN;
                }
                result = handleParseInt(argVal, argType, args);
            } catch (Exception e) {
                result = Double.NaN;
            }
            return result;
        }

        public Object handleParseInt(Object argVal, ObjectType type, List args) {
            BigInteger result;
            int radixInt = 0, sign = 1;
            boolean stripPrefix = true;
            if (args.size() > 1) {
                Object radix = args.get(1);

                switch (ObjectType.objectToType(radix)) {
                    case BIG_INTEGER:
                    case BIG_DECIMAL:
                    case INTEGER:
                    case DOUBLE:
                    case SHORT:
                    case FLOAT:
                    case LONG:
                        radixInt = ((Number) radix).intValue();
                }
                if (radixInt != 0) {
                    if (radixInt < 2 || radixInt > 36) {
                        return Double.NaN;
                    }
                    if (radixInt != 16) {
                        stripPrefix = false;
                    }
                }
            }
            switch (type) {
                case BIG_DECIMAL:
                    argVal = ((BigDecimal) argVal).setScale(0, RoundingMode.DOWN);
                    break;
                case INTEGER:
                case DOUBLE:
                case SHORT:
                case FLOAT:
                case LONG:
                    argVal = new BigDecimal(argVal.toString()).setScale(0, RoundingMode.DOWN);
                    break;
                default:
                    break;
            }
            String argAsString = ObjectType.toString(argVal).trim();
            argAsString = argAsString.trim();
            if (argAsString.startsWith(MINUS_PREFIX)) {
                sign = 0;
                argAsString = argAsString.substring(1);
            } else if (argAsString.startsWith(PLUS_PREFIX)) {
                argAsString = argAsString.substring(1);
            }
            if (argAsString.contains(PERIOD)) {
                argAsString = StringUtils.split(argAsString, PERIOD)[0];
            }
            if (stripPrefix && StringUtils.startsWithAny(argAsString, HEX_PREFIXES)) {
                argAsString = argAsString.substring(2);
                radixInt = HEX_RADIX;
            }
            if (radixInt == 0) {
                radixInt = 10;
            }
            // Seek up to any trailing characters.
            int lastNumber = 0;
            while (lastNumber < argAsString.length()) {
                char ch = argAsString.charAt(lastNumber);
                if (Character.digit(ch, radixInt) == -1) {
                    break;
                }
                lastNumber += 1;
            }
            result = new BigInteger(argAsString.substring(0, lastNumber), radixInt);
            if (sign == 0) {
                result = result.negate();
            }
            return result;
        }
    }

    /**
     * Implements the parseFloat Javascript function.
     */
    public enum ParseFloatFunction implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List args) {
            Object argVal = null;
            if (!args.isEmpty()) {
                argVal = args.get(0);
            }
            ObjectType argType = ObjectType.objectToType(argVal);
            Object result;
            try {
                if (argVal == null) {
                    // XXX Restore old behavior where a null arg would return null.
                    return null;
                }
                if (argType.ordinal() > 7) {
                    return Double.NaN;
                }
                String argAsString = argVal.toString().trim();
                int lastNumber = 0;
                while (lastNumber < argAsString.length()) {
                    char ch = argAsString.charAt(lastNumber);
                    if (!Character.isDigit(ch) && (ch != '-') && (ch != '+') &&
                            (ch != 'E') && (ch != 'e') && (ch != '.')) {
                        break;
                    }
                    lastNumber += 1;
                }
                result = new BigDecimal(argAsString.substring(0, lastNumber));
            } catch (Exception e) {
                result = Double.NaN;
            }
            return result;
        }
    }
}
