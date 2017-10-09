import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;

/**
 * Utility for making deep copies (vs. clone()'s shallow copies) of
 * objects. Objects are first serialized and then deserialized. Error
 * checking is fairly minimal in this implementation. If an object is
 * encountered that cannot be serialized (or that references an object
 * that cannot be serialized) an error is printed to System.err and
 * null is returned. Depending on your specific application, it might
 * make more sense to have copy(...) re-throw the exception.
 *
 * Credit:
 * http://javatechniques.com/blog/faster-deep-copies-of-java-objects/
 *
 * @param <T> the object being deep-copied
 */
public class DeepUtils<T> {
    private static final String HASH_MAP = "java.util.HashMap";
    private static final String LINKED_HASH_MAP = "java.util.LinkedHashMap";
    private static final String ARRAY_LIST = "java.util.ArrayList";
    private static final String LINKED_LIST = "java.util.LinkedList";
    private static final String HASH_SET = "java.util.HashSet";
    private static final String LINKED_HASH_SET = "java.util.LinkedHashSet";
    private static final String STRING = "java.lang.String";
    private static final String BOOLEAN = "java.lang.Boolean";
    private static final String BIG_INTEGER = "java.math.BigInteger";
    private static final String BIG_DECIMAL = "java.math.BigDecimal";
    private static final String INTEGER = "java.lang.Integer";
    private static final String LONG = "java.lang.Long";
    private static final String SHORT = "java.lang.Short";
    private static final String BYTE = "java.lang.Byte";
    private static final String FLOAT = "java.lang.Float";
    private static final String DOUBLE = "java.lang.Double";
    private static final String DATE_TIME = "org.joda.time.DateTime";
    private static final String LOCAL_DATE_TIME = "org.joda.time.LocalDateTime";
    private static final String LOCAL_DATE = "org.joda.time.LocalDate";
    private static final String LOCAL_TIME = "org.joda.time.LocalTime";

    /**
     * ByteArrayInputStream implementation that does not synchronize methods.
     */
    public static class FastByteArrayInputStream extends InputStream {
        /**
         * Our byte buffer
         */
        protected byte[] buf = null;

        /**
         * Number of bytes that we can read from the buffer
         */
        protected int count = 0;

        /**
         * Number of bytes that have been read from the buffer
         */
        protected int pos = 0;

        public FastByteArrayInputStream(byte[] buf, int count) {
            this.buf = buf.clone();
            this.count = count;
        }

        @Override
        public final int available() {
            return count - pos;
        }

        @Override
        public final int read() {
            return (pos < count) ? (buf[pos++] & 0xff) : -1;
        }

        @Override
        public final int read(byte[] b, int off, int len) {
            if (pos >= count) {
                return -1;
            }

            if ((pos + len) > count) {
                len = (count - pos);
            }

            System.arraycopy(buf, pos, b, off, len);
            pos += len;
            return len;
        }

        @Override
        public final long skip(long n) {
            if ((pos + n) > count) {
                n = count - pos;
            }
            if (n < 0) {
                return 0;
            }
            pos += n;
            return n;
        }
    }

    /**
     * ByteArrayOutputStream implementation that doesn't synchronize methods
     * and doesn't copy the data on toByteArray().
     */
    public static class FastByteArrayOutputStream extends OutputStream {
        /**
         * Buffer and size
         */
        protected byte[] buf = null;
        protected int size = 0;

        /**
         * Constructs a stream with buffer capacity size 5K
         */
        public FastByteArrayOutputStream() {
            // Call another constructor taking buffer size parameter
            this(5 * 1024);
        }

        /**
         * Constructs a stream with the given initial size
         */
        public FastByteArrayOutputStream(int initSize) {
            this.size = 0;
            this.buf = new byte[initSize];
        }

        /**
         * Ensures that we have a large enough buffer for the given size.
         */
        private void verifyBufferSize(int sz) {
            if (sz > buf.length) {
                byte[] old = buf;
                buf = new byte[Math.max(sz, 2 * buf.length)];
                System.arraycopy(old, 0, buf, 0, old.length);
                old = null;
            }
        }

        public int getSize() {
            return size;
        }

        /**
         * Returns the byte array containing the written data. Note that this
         * array will almost always be larger than the amount of data actually
         * written.
         */
        public byte[] getByteArray() {
            return buf.clone();
        }

        @Override
        public final void write(byte b[]) {
            verifyBufferSize(size + b.length);
            System.arraycopy(b, 0, buf, size, b.length);
            size += b.length;
        }

        @Override
        public final void write(byte b[], int off, int len) {
            verifyBufferSize(size + len);
            System.arraycopy(b, off, buf, size, len);
            size += len;
        }

        @Override
        public final void write(int b) {
            verifyBufferSize(size + 1);
            buf[size++] = (byte) b;
        }

        public void reset() {
            size = 0;
        }

        /**
         * Returns a ByteArrayInputStream for reading back the written data
         */
        public InputStream getInputStream() {
            return new FastByteArrayInputStream(buf, size);
        }

    }

    /**
     * Returns a deep copy of an object by recursive instantiation.
     * It falls back to the serialization method if an object class is not recognized.
     * Any value field may be null if the object is not of a known class nor can be serialized.
     *
     * @param orig the object to be copied, should never be {@code null}
     *
     * @return deep copied object
     *
     * @throws IllegalArgumentException if {@code orig} is null
     */
    @SuppressWarnings("unchecked")
    public static <T> T copy(Object orig) {
        if (orig == null) {
            throw new IllegalArgumentException(Messages.ERR_NULL_OBJECT);
        }
        return (T) cloneObject(orig);
    }

    private static Object deepMerge(Object orig, Object override) {
        if (orig instanceof Map && override instanceof Map) {
            Map<String, Object> map = (Map) orig;
            Map<String, Object> overrideMap = ((Map) override);

            for (Map.Entry<String, Object> entry : overrideMap.entrySet()) {
                Object origValue = map.get(entry.getKey());

                map.put(entry.getKey(), deepMerge(origValue, entry.getValue()));
            }
        } else if (orig instanceof List && override instanceof List) {
            List<Object> origList = ((List) orig);
            List<Object> overrideList = ((List) override);

            for (int lpc = 0; lpc < overrideList.size(); lpc++) {
                if (lpc >= origList.size()) {
                    origList.add(DeepUtils.copy(overrideList.get(lpc)));
                } else {
                    origList.set(lpc, deepMerge(origList.get(lpc), overrideList.get(lpc)));
                }
            }
        } else {
            return override;
        }

        return orig;
    }

    /**
     * Perform a deep merge of the given objects.  The merge operation will try to add/overwrite
     * values in the base with the values in override.
     *
     * @param base The base object to overwrite with the values in the override parameter.
     * @param override The object to
     * @param <T>
     * @return The base object if a nested merge was done or 'override' if the base object was
     *   completely overwritten.
     */
    public static <T> T merge(Object base, Object override) {
        return (T) deepMerge(base, override);
    }

    /**
     * Returns a deep copy of the object, or null if the object cannot
     * be serialized.
     *
     * @param orig the object to be copied, should never be {@code null}
     *
     * @return deep-copied object
     *
     * @throws IOException              if the object cannot be serialized
     * @throws ClassNotFoundException   if the copy cannot be deserialized
     */
    public static <T> T copySerializable(Object orig) {
        ObjectInputStream in = null;
        try {
            // Write the object out to a byte array
            FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(fbos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Retrieve an input stream from the byte array and read
            // a copy of the object back in.
            in = new ObjectInputStream(fbos.getInputStream());
            @SuppressWarnings("unchecked")
            T res = (T) in.readObject();
            in.close();
            return res;
        } catch (IOException | ClassNotFoundException e) {
            throw new SnapException(e, "Unable to create a deep copy of the object");
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * Clones an object if Map or List or array.
     *
     * @param value -  value to be cloned if Map or List
     *
     * @return cloned value
     */
    @SuppressWarnings("unchecked")
    private static Object cloneObject(Object value) {
        if (value != null) {
            String className = value.getClass().getName();
            switch (className) {
                case HASH_MAP:
                case LINKED_HASH_MAP:
                    return cloneMap((Map<Object, Object>) value);
                case ARRAY_LIST:
                case LINKED_LIST:
                    return cloneList((List<Object>) value);
                case HASH_SET:
                case LINKED_HASH_SET:
                    return cloneSet((Set<Object>) value);
                case STRING:
                case BOOLEAN:
                case BIG_INTEGER:
                case BIG_DECIMAL:
                case INTEGER:
                case LONG:
                case SHORT:
                case BYTE:
                case FLOAT:
                case DOUBLE:
                    // todo - psung: the following list should be updated if we start to use new
                    // Java 8 date time classes
                case DATE_TIME:
                case LOCAL_DATE_TIME:
                case LOCAL_DATE:
                case LOCAL_TIME:
                    return value;
                default:
                    if (value.getClass().isArray()) {
                        return copyArray(value);
                    } else if (value instanceof Map) {
                        return cloneMap((Map<Object, Object>) value);
                    } else if (value instanceof List) {
                        return cloneList((List<Object>) value);
                    }
                    return DeepUtils.copySerializable(value);
            }
        }
        return null;
    }

    /**
     * Clones a Map object as a deep-copy recursively.  Note that the keys are not cloned since
     * they are expected to be stable.
     *
     * @param map - Map object to be cloned
     *
     * @return cloned Map data
     */
    private static Map<Object, Object> cloneMap(Map<Object, Object> map) {
        Map<Object, Object> newMap = new LinkedHashMap<>(map.size());
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            newMap.put(entry.getKey(), cloneObject(value));
        }
        return newMap;
    }

    /**
     * Clones a List object as a deep-copy recursively.
     *
     * @param list - List object to be cloned
     *
     * @return cloned List object
     */
    private static List<Object> cloneList(List<Object> list) {
        List<Object> newList = new ArrayList<>(list.size());
        for (Object value : list) {
            newList.add(cloneObject(value));
        }
        return newList;
    }

    /**
     * Clones a Set object as a deep-copy recursively.
     *
     * @param set - List object to be cloned
     *
     * @return cloned Set object
     */
    private static Set<Object> cloneSet(Set<Object> set) {
        Set<Object> newSet = new LinkedHashSet<>(set.size());
        for (Object value : set) {
            newSet.add(cloneObject(value));
        }
        return newSet;
    }

    /**
     * Clones an object array as a deep-copy recursively.
     *
     * @param value - object array to be cloned
     *
     * @return cloned object array
     */
    private static Object copyArray(Object value) {
        if (value instanceof Object[]) {
            Object[] objects = (Object[]) value;
            return Arrays.copyOf(objects, objects.length);
        } else if (value instanceof byte[]) {
            byte[] bytes = (byte[]) value;
            return Arrays.copyOf(bytes, bytes.length);
        } else if (value instanceof char[]) {
            char[] chars = (char[]) value;
            return Arrays.copyOf(chars, chars.length);
        } else if (value instanceof int[]) {
            int[] integers = (int[]) value;
            return Arrays.copyOf(integers, integers.length);
        } else if (value instanceof short[]) {
            short[] shorts = (short[]) value;
            return Arrays.copyOf(shorts, shorts.length);
        } else if (value instanceof long[]) {
            long[] longs = (long[]) value;
            return Arrays.copyOf(longs, longs.length);
        } else if (value instanceof float[]) {
            float[] floats = (float[]) value;
            return Arrays.copyOf(floats, floats.length);
        } else if (value instanceof double[]) {
            double[] doubles = (double[]) value;
            return Arrays.copyOf(doubles, doubles.length);
        } else if (value instanceof boolean[]) {
            boolean[] booleans = (boolean[]) value;
            return Arrays.copyOf(booleans, booleans.length);
        } else {
            return DeepUtils.copySerializable(value);
        }
    }
}