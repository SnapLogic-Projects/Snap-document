package com.snaplogic.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import com.snaplogic.api.SnapException;

import javax.annotation.CheckForNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.snaplogic.util.Messages.*;

/**
 * JsonPathUtil
 *
 * @author mklumpp
 */
@Singleton
public class JsonPathUtil {
    /**
     * API to read and write json path objects
     */
    @Deprecated
    private static class PropertyData {
        // TODO - MK: Ignore this class, will be removed once JsonPath.write provide the same
        // functionality then addKeyValuePath, currently a path such as a.b.c[0].e.f can not be
        // written.

        static final Pattern ALL_VALUE_PATTERN = Pattern.compile("\\[\\*\\]");
        static final Pattern INVALID_ARRAY_INDEX_PATTERN = Pattern.compile("\\[[a-zA-Z+]\\]");

        /**
         * Adds the provides value for the given keyword under the given path in the data.
         * <p>
         * As an example:
         * <p>path = {@literal class_map.read.filename}
         * <p>keyword = {@literal value}
         * <p>value = {@literal file:///filename.txt} will result in:
         * {@literal
         * {
         *   "class_map" :{
         *      "read":{
         *        "filename":{
         *          "value":"file:///filename.txt"
         *        }
         *      }
         *    }
         * }
         * }
         *
         * @param curObject as the raw collection object. Expected is either a {@link Map} or
         *                  {@link List}.
         * @param jsonPath  as the JSON path.
         * @param value     as the value.
         */
        @CheckForNull
        public static void addKeyValueForPath(final Object curObject, String jsonPath,
                                              final Object value) {
            Preconditions.checkNotNull(curObject, NO_COLLECTION_OBJECT);
            Preconditions.checkNotNull(jsonPath, NO_JSON_PATH);
            if ((!(curObject instanceof Map)) && (!(curObject instanceof List))) {
                throw new SnapException(INVALID_COLLECTION_OBJECT).formatWith(curObject.getClass());
            }
            try {
                JsonPathImpl.compileStatic(jsonPath).write(curObject, value);
            } catch (InvalidPathException e) {
                throw new ExecutionException(e, "Unable to write key: %s")
                        .formatWith(e.getPath())
                        .withReason(e.getMessage())
                        .withResolutionAsDefect();
            }
        }

        /**
         * Inserts a new list item into the list using the provided listIndex.
         * <p> The list will be re-sized using the listIndex value in case the current list size is
         * smaller the listIndex.
         *
         * @param list      as the list
         * @param listIndex as the index
         * @param listItem  as the new list item
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        public static void updateList(List list, int listIndex, Object listItem) {
            int listSize = list.size();
            if (listIndex >= listSize) {
                Object[] arr = list.toArray();
                // This will create nulls entries if there are no entries.
                Object[] newArr = Arrays.copyOfRange(arr, listSize, listIndex);
                List arrList = Lists.newArrayList(Arrays.asList(newArr));
                arrList.add((listIndex - listSize), listItem);
                list.addAll(arrList);
            } else {
                list.set(listIndex, listItem);
            }
        }
    }

    public JsonPathUtil() {
        // use injection to instantiate this
    }

    public static final String INVALID_PATH = "Invalid path %s";

    /**
     * Retrieves a value from the data using a provided JSON path.
     *
     * @param jsonPath as the JSON path to retrieve the property data from the snap data
     * @param data     as the data being read
     *
     * @return the value read by using the path
     */
    public <R> R read(String jsonPath, Object data) throws InvalidPathException {
        JsonPath path = JsonPathImpl.compile(jsonPath);
        return path.read(data);
    }

    /**
     * Retrieves a value from the data using a provided JSON path, the value might not exist,
     * in that case null is returned.
     *
     * @param jsonPath as the JSON path to retrieve the property data from the snap data
     * @param data     as the data being read
     *
     * @return the value read by using the path
     */
    public <R> R nullableRead(String jsonPath, Object data) {
        try {
            return read(jsonPath, data);
        } catch (InvalidPathException e) {
            // ignore
            return null;
        }
    }

    /**
     * Gather a list of values from a list of paths.
     *
     * @param paths The paths to read data from.
     * @param data The data to read.
     * @return The values from the corresponding paths.
     * @throws InvalidPathException If there was a problem reading some data.
     */
    public List<Object> gather(List<JsonPath> paths, Object data) throws InvalidPathException {
        List<Object> retval = Lists.newArrayListWithExpectedSize(paths.size());

        for (JsonPath path : paths) {
            retval.add(path.read(data));
        }
        return retval;
    }

    /**
     * Adds the value for the given path to the provided current object
     *
     * @param curObject the object that will receive the value for the given path
     * @param jsonPath  the json path
     * @param value     the value being added
     */
    public void write(final Object curObject, final String jsonPath,
                      final Object value) {
        //TODO - MK: replace with JsonPath.write
        PropertyData.addKeyValueForPath(curObject, jsonPath, value);
    }

    /**
     * Extracts the property name from the path
     *
     * @param path as the json path
     *
     * @return the property name
     */
    public static String basename(String path) {
        // TODO - MK: replace with JsonPath.basename
        if (path == null) {
            return null;
        }
        int index = indexOfLastCurrentElement(path);
        if (index == -1) {
            return removeRootPathElement(path);
        }
        return path.substring(index + 1);
    }

    private static int indexOfLastCurrentElement(String path) {
        return path.lastIndexOf(JsonPathBuilder.CURRENT_ELEMENT);
    }

    /**
     * Removes the root path element from the given path
     *
     * @param path as the json path
     * @return the path without the {@link JsonPathBuilder#ROOT_ELEMENT}
     */
    public static String removeRootPathElement(String path) {
        // property is right under the root, such as $propName.
        // We want to return propName
        if (path.startsWith("$.")) {
            return path.substring(2);
        } else if (path.startsWith("$")) {
            return path.substring(1);
        }
        return path;
    }

    /**
     * Removes the last element from the path.
     *
     * @param path as the path
     * @return the last element of the path
     */
    public static String removeLastElement(String path) {
        int lastElementIndex = path.lastIndexOf(JsonPathBuilder.CURRENT_ELEMENT);
        if (lastElementIndex > -1) {
            return path.substring(0, lastElementIndex);
        }
        return JsonPathBuilder.ROOT_ELEMENT;
    }

    /**
     * Retrieves a value from the data using a provided JSON path.
     *
     * @param jsonPath as the JSON path to retrieve the property data from the snap data
     * @param data     as the data being read
     *
     * @return the value read by using the path
     */
    @Deprecated
    public static <R> R uncheckedRead(String jsonPath, Object data) throws InvalidPathException {
        // TODO - MK: remove this once properties are using injection
        JsonPath path = JsonPathImpl.compile(jsonPath);
        return path.read(data);
    }

    /**
     * Retrieves a value from the data using a provided JSON path.
     *
     * @param jsonPath as the JSON path to retrieve the property data from the snap data
     * @param data     as the data being read
     *
     * @return the value read by using the path
     */
    @Deprecated
    public static <R> R checkedRead(String jsonPath, Object data) {
        // TODO - MK: remove this once properties are using injection
        try {
            JsonPath path = JsonPathImpl.compile(jsonPath);
            return path.read(data);
        } catch (InvalidPathException e) {
            // ignore
            return null;
        }
    }

    /**
     * Adds the value for the given path to the provided current object
     *
     * @param curObject the object that will receive the value for the given path
     * @param jsonPath  the json path
     * @param value     the value being added
     */
    @Deprecated
    public static void addValue(final Object curObject, final String jsonPath,
                                final Object value) {
        // TODO - MK: remove this one JsonPath.write does the same
        try {
            JsonPath path = JsonPathImpl.compile(jsonPath);
            path.insert(curObject, value);
        } catch (InvalidPathException e) {
            throw new SnapException(e, INVALID_PATH).formatWith(jsonPath);
        }
    }

    /**
     * Inserts a new list item into the list using the provided listIndex.
     * <p> The list will be re-sized using the listIndex value in case the current list size is
     * smaller the listIndex.
     *
     * @param list      as the list
     * @param listIndex as the index
     * @param listItem  as the new list item
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Deprecated
    public static void updateList(List list, int listIndex, Object listItem) {
        // TODO - MK: remove this one JsonPath.write does the same
        int listSize = list.size();
        if (listIndex >= listSize) {
            Object[] arr = list.toArray();
            // This will create nulls entries if there are no entries.
            Object[] newArr = Arrays.copyOfRange(arr, listSize, listIndex);
            List arrList = Lists.newArrayList(Arrays.asList(newArr));
            arrList.add((listIndex - listSize), listItem);
            list.addAll(arrList);
        } else {
            list.set(listIndex, listItem);
        }
    }

    @CheckForNull
    @SuppressWarnings("unchecked")
    @Deprecated
    public void update(Object curData, Map<String, Object> newPropertyMap) {
        // TODO - MK: write or insert should be used
        Preconditions.checkNotNull(curData, UNDEFINED_SOURCE_MAP);
        Preconditions.checkNotNull(newPropertyMap, UNDEFINED_TARGET_MAP);
        Map<String, Object> data = (Map<String, Object>) curData;
        if (data.isEmpty()) {
            data.putAll(newPropertyMap);
        } else {
            deepMerge(data, newPropertyMap, JsonPathBuilder.ROOT_ELEMENT);
        }
    }

    private void deepMerge(Map<String, Object> curData, Map<String, Object> newPropertyMap,
                           String curPath) {
        for (Map.Entry<String, Object> entry : newPropertyMap.entrySet()) {
            String key = entry.getKey();
            final String newPath = new JsonPathBuilder(curPath).appendCurrentElement(key)
                    .toString();
            Object curObject = nullableRead(newPath, curData);
            Object value = entry.getValue();
            if (curObject == null || !(value instanceof Map)) {
                write(curData, newPath, value);
            } else {
                deepMerge(curData, (Map<String, Object>) value, newPath);
            }
        }
    }

    /**
     * Adds the value for the given path to the provided current object
     *
     * @param curObject the object that will receive the value for the given path
     * @param jsonPath  the json path
     * @param value     the value being added
     */
    @Deprecated
    public static void addKeyValueForPath(final Object curObject, final String jsonPath,
                                          final Object value) {
        //TODO - MK: there are some classes that call this in a static block
        PropertyData.addKeyValueForPath(curObject, jsonPath, value);
    }
}
