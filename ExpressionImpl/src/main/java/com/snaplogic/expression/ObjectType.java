package com.snaplogic.expression;

import com.google.common.collect.ImmutableMap;
import com.snaplogic.expression.classes.JavascriptClass;
import com.snaplogic.expression.methods.JavascriptFunction;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Enum for object return type in expression language.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public enum ObjectType {
    // ordinal 0 - 7
    STRING("String"),
    BIG_INTEGER("BigInteger"),
    BIG_DECIMAL("BigDecimal"),
    INTEGER("Integer"),
    DOUBLE("Double"),
    SHORT("Short"),
    FLOAT("Float"),
    LONG("Long"),
    // ordinal 8 and onwards
    NULL("null"),
    MAP("Map"),
    BOOLEAN("Boolean"),
    LIST("List"),
    DATE("Date"),
    LOCAL_DATE("LocalDate"),
    LOCAL_TIME("LocalTime"),
    LOCAL_DATETIME("LocalDateTime"),
    PATH_REFERENCE("PathReference"),
    REGEXP("RegExp"),
    BYTE_ARRAY("byte[]"),
    FUNCTION("Function"),
    OTHER("Other"),
    CLASS("Class");

    private String name;

    private ObjectType(String name) {
        this.name = name;
    }

    private static final Map<Class, ObjectType> CLASS_TO_TYPE =
            ImmutableMap.<Class, ObjectType>builder()
                    .put(BigInteger.class, BIG_INTEGER)
                    .put(BigDecimal.class, BIG_DECIMAL)
                    .put(Integer.class, INTEGER)
                    .put(Double.class, DOUBLE)
                    .put(Short.class, SHORT)
                    .put(Float.class, FLOAT)
                    .put(Long.class, LONG)
                    .put(String.class, STRING)
                    .put(Boolean.class, BOOLEAN)
                    .put(DateTime.class, DATE)
                    .put(LocalDate.class, LOCAL_DATE)
                    .put(LocalTime.class, LOCAL_TIME)
                    .put(LocalDateTime.class, LOCAL_DATETIME)
                    .put(PathReference.class, PATH_REFERENCE)
                    .put(Regex.class, REGEXP)
                    .put(byte[].class, BYTE_ARRAY)
                    .build();

    public static final ObjectType objectToType(Object obj) {
        if (obj == null) {
            return NULL;
        } else if (obj instanceof Map) {
            return MAP;
        } else if (obj instanceof List) {
            return LIST;
        } else if (obj instanceof JavascriptClass) {
            return CLASS;
        } else if (obj instanceof JavascriptFunction) {
            return FUNCTION;
        }
        ObjectType type = CLASS_TO_TYPE.get(obj.getClass());
        return type == null ? OTHER : type;
    }

    public static Object attemptToConvertToBigDecimal(Object obj) {
        switch (ObjectType.objectToType(obj)) {
            case FLOAT:
                Float fl = (Float) obj;
                if (fl.isInfinite()) {
                    if (fl == Float.POSITIVE_INFINITY) {
                        obj = Double.POSITIVE_INFINITY;
                    } else {
                        obj = Double.NEGATIVE_INFINITY;
                    }
                } else if (fl.isNaN()) {
                    obj = Double.NaN;
                } else {
                    obj = new BigDecimal(obj.toString());
                }
                break;
            case DOUBLE:
                Double d = (Double) obj;
                if (!d.isInfinite() && !d.isNaN()) {
                    obj = new BigDecimal(obj.toString());
                }
                break;
            case BIG_DECIMAL:
                break;
            case BYTE_ARRAY:
                try {
                    obj = new BigDecimal(new String((byte[]) obj, StandardCharsets.UTF_8));
                } catch (Exception e) {
                    // Ignore exception
                }
                break;
            case BIG_INTEGER:
                obj = new BigDecimal((BigInteger) obj);
                break;
            case INTEGER:
            case SHORT:
            case LONG:
                obj = new BigDecimal(obj.toString());
                break;
            case DATE:
                obj = ObjectType.dateToBigDecimal((DateTime) obj);
                break;
            case STRING:
                try {
                    obj = new BigDecimal(obj.toString());
                } catch (Exception e) {
                    // Ignore exception and keep the string value
                }
                break;
            case BOOLEAN:
                obj = ((boolean) obj) ? BigDecimal.ONE : BigDecimal.ZERO;
                break;
        }
        return obj;
    }

    /**
     * Checks if a given object is the numeric NaN.
     *
     * @param obj The object to check.
     * @return True equals double or float NaN.
     */
    public static boolean isNaN(Object obj) {
        if (obj instanceof Double) {
            Double d = (Double) obj;

            return d.isNaN();
        } else if (obj instanceof Float) {
            Float fl = (Float) obj;

            return fl.isNaN();
        }
        return false;
    }

    /**
     * Convert a Date object into a BigDecimal that holds the millisecond value.
     *
     * @param date The date to convert.
     * @return A BigDecimal that represents the milliseconds from the epoch.
     */
    public static BigDecimal dateToBigDecimal(final DateTime date) {
        return new BigDecimal(String.valueOf(date.getMillis()));
    }

    /**
     * @param val The boolean value to convert to an integer.
     * @return One if val was true, 0 otherwise.
     */
    public static int booleanToInt(Boolean val) {
        return val ? 1 : 0;
    }

    /**
     * Compare a number with another value after coercion is done.
     *
     * @param left The number to compare.
     * @param right The right hand side to compare the number against.
     * @param rightType The type of 'right'.
     * @return Zero if left is equal to right, -1 if left is less than right, and 1
     *         if right is greater than left.
     */
    public static int compareNumber(final Number left, Object right, final ObjectType rightType) {
        BigDecimal leftDec = new BigDecimal(left.toString());
        switch (rightType) {
            case DATE:
                return leftDec.compareTo(dateToBigDecimal((DateTime) right));
            case BOOLEAN:
                return leftDec.compareTo(BigDecimal.valueOf(booleanToInt((Boolean) right)));
            case STRING:
            case BIG_DECIMAL:
            case BIG_INTEGER:
            case INTEGER:
            case DOUBLE:
            case SHORT:
            case FLOAT:
            case LONG:
                return leftDec.compareTo(new BigDecimal(right.toString()));
            case BYTE_ARRAY:
                return leftDec.compareTo(new BigDecimal(new String((byte[]) right,
                        StandardCharsets.UTF_8)));
            default:
                throw new ExecutionException(CANNOT_COMPARE_VALUE_TO_NUMBER)
                        .formatWith(right)
                        .withReason(String.format(CANNOT_COMPARE_VALUE_TO_NUMBER, right))
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION);
        }
    }

    /**
     * Compare objects without doing any type coercion.
     *
     * @param left
     * @param right
     * @return True
     */
    public static boolean equals(Object left, Object right) {
        ObjectType leftType = ObjectType.objectToType(left);
        ObjectType rightType = ObjectType.objectToType(right);
        final boolean retval;
        if (leftType == ObjectType.BIG_DECIMAL && rightType == ObjectType.BIG_DECIMAL) {
            // The BigDecimal equals() method pays attention to the scale
            // of the number, which means "0.0" is not equals to "0.00".
            // This result does not match the behavior of JavaScript, so
            // we use the compareTo() method, which does not pay attention
            // to the scale.
            retval = ((Comparable) left).compareTo(right) == 0;
        } else if (ObjectType.isNaN(left) || ObjectType.isNaN(right)) {
            retval = false;
        } else if (leftType == BYTE_ARRAY && rightType == ObjectType.BYTE_ARRAY) {
            retval = Arrays.equals((byte[]) left, (byte[]) right);
        } else {
            retval = ObjectUtils.equals(left, right);
        }
        return retval;
    }

    /**
     * Convert an object to a boolean value based on the ECMAScript semantics.
     *
     * @param obj The object to convert.
     * @return The boolean representation of obj.
     */
    public static boolean toBoolean(Object obj) {
        ObjectType type = objectToType(obj);
        boolean retval = true;

        switch (type) {
            case FLOAT:
                Float fl = (Float) obj;
                if (fl.isNaN()) {
                    return false;
                } else if (fl.isInfinite()) {
                    return true;
                }
                break;
            case DOUBLE:
                Double d = (Double) obj;
                if (d.isNaN()) {
                    return false;
                } else if (d.isInfinite()) {
                    return true;
                }
                break;
        }

        switch (type) {
            case STRING:
                retval = StringUtils.isNotEmpty((String) obj);
                break;
            case NULL:
                retval = false;
                break;
            case BOOLEAN:
                retval = (Boolean) obj;
                break;
            case BIG_DECIMAL:
            case BIG_INTEGER:
            case INTEGER:
            case DOUBLE:
            case SHORT:
            case FLOAT:
            case LONG:
                retval = compareNumber((Number) obj, 0, ObjectType.INTEGER) != 0;
                break;
            case BYTE_ARRAY:
                byte[] bits = (byte[]) obj;

                retval = bits.length > 0;
                break;
        }
        return retval;
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return String.valueOf(obj);
        }
        Class cl = obj.getClass();
        if (cl == byte[].class) {
            return new String((byte[]) obj, StandardCharsets.UTF_8);
        }
        if (cl == BigDecimal.class) {
            return ((BigDecimal) obj).toPlainString();
        }
        return String.valueOf(obj);
    }

    public static Number toNumber(Object obj, Number defaultValue) {
        if (obj instanceof Number) {
            Number num = (Number) obj;

            return num;
        } else {
            obj = ObjectType.attemptToConvertToBigDecimal(obj);

            if (obj instanceof Number) {
                Number num = (Number) obj;

                return num;
            }
        }
        return defaultValue;
    }

    /**
     * Custom Map wrapper so that we can give meaningful errors.
     */
    private static class ReadOnlyMap extends AbstractMapDecorator {
        public ReadOnlyMap(Map map) {
            super(map);
        }

        @Override
        public void clear() {
            throw new SnapDataException("Unmodifiable object cannot be cleared")
                    .withReason(GLOBAL_OBJ_CANNOT_BE_CHANGED)
                    .withResolution(COPY_OBJ_RESOLUTION);
        }

        @Override
        public Object put(final Object key, final Object value) {
            throw new SnapDataException("Cannot set the property '%s' on an unmodifiable object")
                    .formatWith(key)
                    .withReason(GLOBAL_OBJ_CANNOT_BE_CHANGED)
                    .withResolution(COPY_OBJ_RESOLUTION);
        }

        @Override
        public void putAll(final Map mapToCopy) {
            throw new SnapDataException("Properties cannot be changed in an unmodifiable object")
                    .withReason(GLOBAL_OBJ_CANNOT_BE_CHANGED)
                    .withResolution(COPY_OBJ_RESOLUTION);
        }

        @Override
        public Object remove(final Object key) {
            throw new SnapDataException("Properties cannot be removed in an unmodifiable object")
                    .withReason(GLOBAL_OBJ_CANNOT_BE_CHANGED)
                    .withResolution(COPY_OBJ_RESOLUTION);
        }
    }

    /**
     * Custom List wrapper so that we can give meaningful errors.
     */
    private static class ReadOnlyList extends AbstractListDecorator {
        public ReadOnlyList(List list) {
            super(list);
        }

        @Override
        public List subList(final int fromIndex, final int toIndex) {
            return new ReadOnlyList(super.subList(fromIndex, toIndex));
        }

        @Override
        public Object remove(final int index) {
            throw new SnapDataException("Elements cannot be removed from an unmodifiable array")
                    .withReason(GLOBAL_ARRAY_CANNOT_BE_CHANGED)
                    .withResolution(COPY_ARRAY_RESOLUTION);
        }

        @Override
        public Object set(final int index, final Object object) {
            throw new SnapDataException("Cannot change element #%s in an unmodifiable array")
                    .formatWith(index)
                    .withReason(GLOBAL_ARRAY_CANNOT_BE_CHANGED)
                    .withResolution(COPY_ARRAY_RESOLUTION);
        }

        @Override
        public void add(final int index, final Object object) {
            throw new SnapDataException("Cannot insert element at index %s in an unmodifiable " +
                    "array")
                    .formatWith(index)
                    .withReason(GLOBAL_ARRAY_CANNOT_BE_CHANGED)
                    .withResolution(COPY_ARRAY_RESOLUTION);
        }

        @Override
        public boolean addAll(final int index, final Collection coll) {
            throw new SnapDataException("Elements cannot be added to an unmodifiable array")
                    .withReason(GLOBAL_ARRAY_CANNOT_BE_CHANGED)
                    .withResolution(COPY_ARRAY_RESOLUTION);
        }
    }

    /**
     * Wrap a hierarchy of objects so that all values are unmodifiable.  This method should be used
     * on data values that global and expected to be shared across expressions.
     *
     * @param obj The root of the object hierarchy to wrap.
     * @param <T>
     * @return An unmodifiable hierarchy of objects.
     */
    public static <T> T toUnmodifiable(T obj) {
        if (obj instanceof ReadOnlyList || obj instanceof ReadOnlyMap) {
            // Nothing to do
            return obj;
        }
        try {
            if (obj instanceof Map) {
                Map<Object, Object> map = ((Map) obj);
                Map<Object, Object> wrapper = new LinkedHashMap<>(map.size());

                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    wrapper.put(entry.getKey(), toUnmodifiable(entry.getValue()));
                }

                return (T) new ReadOnlyMap(wrapper);
            } else if (obj instanceof List) {
                List list = ((List) obj);
                List wrapper = new ArrayList(list.size());

                for (Object elem : list) {
                    wrapper.add(toUnmodifiable(elem));
                }

                return (T) new ReadOnlyList(wrapper);
            } else {
                return obj;
            }
        } catch (StackOverflowError e) {
            throw new ExecutionException("Unable to construct object")
                    .withReason("Object possibly contains a reference cycle, which is not " +
                            "supported")
                    .withResolution("Break the reference cycle");
        }
    }

    /**
     * Attempt to coerce the result of the expression evaluation to the properties specified
     * type.
     *
     * @param type
     * @param result The expression result.
     * @return The coerced value, if possible, otherwise the original result value.
     */
    public static Object attemptToCoerce(final SnapType type, Object result) {
        switch (type) {
            case NUMBER:
                return attemptToConvertToBigDecimal(result);
            case INTEGER: {
                Object possibleBigDecimal = attemptToConvertToBigDecimal(result);

                if (possibleBigDecimal instanceof BigDecimal) {
                    BigDecimal number = (BigDecimal) possibleBigDecimal;
                    return number.toBigInteger();
                }
                return result;
            }
            case STRING:
                if (result == null) {
                    return result;
                }
                return String.valueOf(result);
            case BOOLEAN:
                return toBoolean(result);
            default:
                // TODO: handle date-time objects and such
                return result;
        }
    }

    public String getName() {
        return name;
    }
}
