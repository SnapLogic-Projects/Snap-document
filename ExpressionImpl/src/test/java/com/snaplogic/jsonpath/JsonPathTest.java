/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2013, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.jsonpath;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.common.jsonpath.InvalidPathException;
import com.snaplogic.common.jsonpath.JsonPath;
import com.snaplogic.common.jsonpath.WalkResult;
import com.snaplogic.expression.EnvironmentScope;
import com.snaplogic.expression.JsonPathExpressionService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Test for the JsonPath convenience class.
 *
 * @author tstack
 */
public class JsonPathTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper() {
        {
            configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true);
            configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        }
    };
    private static final String JSON_DATA_2 = "/json_files/json_data_2.json";
    private static final String EXPECTED_WALK_DATA = "/json_files/expected_walk_data_2.json";

    private static final List<Pair<String, Object>> TEST_DATA = Arrays.asList(
            // Basic read
            Pair.of("$.foo_num", (Object) BigInteger.valueOf(10)),
            // Read a key with a non-alpha character.
            Pair.of("$.@special", (Object) Boolean.TRUE),
            // Read a key that is a reserved character.
            Pair.of("$['*']", (Object) "Reserved character"),
            // Read a key with spaces, technically, this should probably be in a subscript.
            Pair.of("$.key with spaces", (Object) BigInteger.valueOf(42)),
            // Wildcard read
            Pair.of("$.foo_array[*]", (Object) Arrays.asList(
                    BigInteger.valueOf(10),
                    BigInteger.valueOf(20),
                    BigInteger.valueOf(30),
                    BigInteger.valueOf(40)
            )),
            // Union array read
            Pair.of("$.foo_array[2,0]", (Object) Arrays.asList(
                    BigInteger.valueOf(30),
                    BigInteger.valueOf(10)
            )),
            // Union object read
            Pair.of("$.tree.characters[0]['first_name', 'last_name']", (Object) Arrays.asList(
                    "Emily", "Thorne")),
            // Array read relative to the end of the array.
            Pair.of("$.foo_array[-1]", (Object) BigInteger.valueOf(40)),
            // Array read using an expression.
            Pair.of("$.foo_array[(@.length - 2)]", (Object) BigInteger.valueOf(30)),
            // Descendant read
            Pair.of("$..first_name",
                    (Object) Arrays.asList("Emily", "Conrad", "Daniel")),
            // Wildcard read of a child object
            Pair.of("$.tree.characters[*].first_name",
                    (Object) Arrays.asList("Emily", "Conrad", "Daniel")),
            // Filter read
            Pair.of("$.tree.characters[ ?( @.age >= 30 ) ].first_name",
                    (Object) Arrays.asList("Emily", "Conrad")),
            // Filter based on the key.
            Pair.of("$.tree[?(key.match(/^loc.*/))]", (Object) Arrays.asList(Arrays.asList(
                    new HashMap<String, Object>() {{
                        put("title", "Mansion");
                    }},
                    new HashMap<String, Object>() {{
                        put("title", "Beach House");
                    }}
            ))),
            Pair.of("$.foo_array[?(key % 2)]", (Object) Arrays.asList(
                    BigInteger.valueOf(20),
                    BigInteger.valueOf(40)
            )),
            // Wildcard read that doesn't use brackets.
            Pair.of("$.tree.locations[0].*",
                    (Object) Arrays.asList("Mansion")),
            // Operations on an empty array.
            Pair.of("$.empty_array[*]", (Object) Collections.emptyList()),
            Pair.of("$.empty_array[?(@ > 0.0)]", (Object) Collections.emptyList()),
            Pair.of("$.empty_array[:]", (Object) Collections.emptyList()),
            Pair.of("$.empty_array[0:-1]", (Object) Collections.emptyList()),
            Pair.of("$.empty_array..name", (Object) Collections.emptyList()),
            // Filter
            Pair.of("$.tree.mixed-types[?(@ > 0.0)]",
                    (Object) Arrays.asList(new BigDecimal("1.0"), Boolean.TRUE)),
            // Simple array slices
            Pair.of("$.foo_array[1:2]",
                    (Object) Arrays.asList(
                            BigInteger.valueOf(20)
                    )),
            Pair.of("$.foo_array[1:3]",
                    (Object) Arrays.asList(
                            BigInteger.valueOf(20),
                            BigInteger.valueOf(30)
                    )),
            Pair.of("$.foo_array[::2]",
                    (Object) Arrays.asList(
                            BigInteger.valueOf(10),
                            BigInteger.valueOf(30)
                    )),
            // Step through the array in reverse.
            Pair.of("$.foo_array[::-2]",
                    (Object) Arrays.asList(
                            BigInteger.valueOf(40),
                            BigInteger.valueOf(20)
                    )),
            Pair.of("$.foo_array[::-1]",
                    (Object) Arrays.asList(
                            BigInteger.valueOf(40),
                            BigInteger.valueOf(30),
                            BigInteger.valueOf(20),
                            BigInteger.valueOf(10)
                    )),
            // Make a copy of the array.
            Pair.of("$.foo_array[::]",
                    (Object) Arrays.asList(
                            BigInteger.valueOf(10),
                            BigInteger.valueOf(20),
                            BigInteger.valueOf(30),
                            BigInteger.valueOf(40)
                    )),
            // Negative indexes.
            Pair.of("$.foo_array[:-1]",
                    (Object) Arrays.asList(
                            BigInteger.valueOf(10),
                            BigInteger.valueOf(20),
                            BigInteger.valueOf(30)
                    )),
            Pair.of("$.foo_array[-1:]",
                    (Object) Arrays.asList(
                            BigInteger.valueOf(40)
                    )),
            Pair.of("$.foo_array[-2:]",
                    (Object) Arrays.asList(
                            BigInteger.valueOf(30),
                            BigInteger.valueOf(40)
                    )),
            Pair.of("$.foo_array[-2:-1]",
                    (Object) Arrays.asList(
                            BigInteger.valueOf(30)
                    )),
            // Out-of-range indexes.
            Pair.of("$.foo_array[-50:]",
                    (Object) Arrays.asList(
                            BigInteger.valueOf(10),
                            BigInteger.valueOf(20),
                            BigInteger.valueOf(30),
                            BigInteger.valueOf(40)
                    )),
            Pair.of("$.foo_array[:-50]",
                    (Object) Arrays.asList()),
            // Get the age of a particular character
            Pair.of("$.tree.characters[?(value.first_name == 'Emily')].eval(value[0].age)",
                    (Object) BigInteger.valueOf(30)),
            // Divide all numbers in an array by 10.
            Pair.of("$.foo_array.map(value / 10)", (Object) Arrays.asList(
                    new BigDecimal("1"),
                    new BigDecimal("2"),
                    new BigDecimal("3"),
                    new BigDecimal("4")
            )),
            // Select the last element in a mapped array.
            Pair.of("$.foo_array.map(value / 10)[-1]", (Object) new BigDecimal("4")),
            // Select the first two elements in a mapped array.
            Pair.of("$.foo_array.map(value / 10)[:2]", (Object) Arrays.asList(
                    new BigDecimal("1"),
                    new BigDecimal("2")
            )),
            // Divide all numbers in an array by 10 and sort the array.
            Pair.of("$.foo_array.map(value / 10).sort_desc(value)", (Object) Arrays.asList(
                    new BigDecimal("4"),
                    new BigDecimal("3"),
                    new BigDecimal("2"),
                    new BigDecimal("1")
            )),
            // Do a map that references the element indexes
            Pair.of("$.foo_array.map([key, value == $.foo_array[key]])", (Object) Arrays.asList(
                    Arrays.asList(new BigInteger("0"), true),
                    Arrays.asList(new BigInteger("1"), true),
                    Arrays.asList(new BigInteger("2"), true),
                    Arrays.asList(new BigInteger("3"), true)
            )),
            Pair.of("$.tree.characters[0].map(value instanceof String ? value + '!' : value)",
                    (Object) new HashMap<String, Object>() {{
                        put("first_name", "Emily!");
                        put("last_name", "Thorne!");
                        put("age", new BigInteger("30"));
                    }}),
            // Sort an array of mixed types
            Pair.of("$.tree.mixed-types.sort_asc(value)", (Object) Arrays.asList(
                    null,
                    BigInteger.ZERO,
                    new BigDecimal("1.0"),
                    Arrays.asList(BigInteger.ZERO),
                    false,
                    true,
                    new HashMap<String, Object>() {{
                        put("key", "value");
                    }}
            )),
            // Group characters by their last name.
            Pair.of("$.tree.characters.group_by(value.last_name)", (Object)
                    new HashMap<String, Object>() {{
                        put("Thorne", Arrays.asList(new HashMap<String, Object>() {{
                            put("first_name", "Emily");
                            put("last_name", "Thorne");
                            put("age", BigInteger.valueOf(30));
                        }}));
                        put("Grayson", Arrays.asList(new HashMap<String, Object>() {{
                            put("first_name", "Conrad");
                            put("last_name", "Grayson");
                            put("age", BigInteger.valueOf(50));
                        }},
                        new HashMap<String, Object>() {{
                            put("first_name", "Daniel");
                            put("last_name", "Grayson");
                            put("age", BigInteger.valueOf(25));
                        }}));
                    }}),
            // Group by element indexes
            Pair.of("$.foo_array.group_by(key % 2)", (Object)
                    new HashMap<String, Object>() {{
                        put("0", Arrays.asList(
                                new BigInteger("10"),
                                new BigInteger("30")
                        ));
                        put("1", Arrays.asList(
                                new BigInteger("20"),
                                new BigInteger("40")
                        ));
                    }}),
            Pair.of("$.tree.characters[0].group_by(key.endsWith('name'))", (Object)
                    new HashMap<String, Object>() {{
                        put("false", Arrays.asList(new BigInteger("30")));
                        put("true", Arrays.asList("Emily", "Thorne"));
                    }}),
            Pair.of("jsonPath($, '$.foo_num')", (Object) BigInteger.valueOf(10))
    );

    private static final String[] BAD_TEST_DATA = {
            // Use a field name for an array
            "$.foo_array['bar']",
            // Sort a map instead of a list
            "$.tree.sort_asc(value)",
            // Bad expression syntax
            "$.foo_array[(1 -)]",
            // Runtime error in expression
            "$.foo_array[(1 / 0)]",
            "$.foo_array[NaN]",
            // Bad field reference in expression
            "$.foo_array[(@.foo)]",
            // Bad expression in slice.
            "$.foo_array['a':2]",
            // Zero step value
            "$.foo_array[1:2:0]",
            // Array reference for an object
            "$[0]",
            // Missing field.
            "$.non_existent",
            // Missing intermediate object.
            "$.tree.non_existent.foo",
            // Out-of-bounds array index.
            "$.foo_array[23]",
            "$.foo_empty[0]",
            "$.foo_empty[-1]",
            // Field reference on an int.
            "$.foo_num.bad",
            // Invalid field reference when wildcarding an array
            "$.tree.mixed-types[*].key",
            // Group by an object instead of a primitive value
            "$.tree.characters.group_by(value)"
    };

    private static final List<Pair<String, List<String>>> JOIN_DATA = Arrays.asList(
            Pair.of("$", Arrays.asList("$", "$")),
            Pair.of("$.foo.bar", Arrays.asList("$.foo", "$.bar")),
            Pair.of("$.foo.map(value)", Arrays.asList("$.foo", "$.map(value)")),
            Pair.of("$.foo.map(value).bar.group_by(value.baz)", Arrays.asList("$.foo",
                    "$.map(value)", "$.bar.group_by(value.baz)"))
    );

    private static final List<Pair<String, List<Object>>> RAW_PATHS = Arrays.asList(
            Pair.of("$[0]", Arrays.asList((Object) 0)),
            Pair.of("$.foo", Arrays.asList((Object) "foo")),
            Pair.of("$['quotes\\\"\\'']", Arrays.asList((Object) "quotes\"'")),
            Pair.of("$.foo[' bar ']", Arrays.<Object>asList("$", "foo", " bar ")),
            Pair.of("$.foo['[bar]']", Arrays.<Object>asList("$", "foo", "[bar]"))
    );

    private static Map readSourceData(String fileName) throws IOException {
        return OBJECT_MAPPER.readValue(JsonPathTest.class.getResourceAsStream(fileName),
                Map.class);
    }

    @Test
    public void testRead() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);

        for (Pair<String, Object> pair : TEST_DATA) {
            try {
                Object result = JsonPathImpl.compile(pair.getLeft()).read(sourceData);

                assertEquals(pair.getRight(), result);
            } catch (Throwable th) {
                throw new AssertionError(pair.getLeft(), th);
            }
        }
    }

    @Test
    public void testBadRead() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);

        for (String path : BAD_TEST_DATA) {
            try {
                JsonPathImpl.compile(path).read(sourceData);
                fail("Expected an InvalidPathException for path -- " + path);
            } catch (InvalidPathException e) {
                assertNotEquals(path, "", e.getPath());
                assertTrue(path, StringUtils.isNotEmpty(e.getResolution()));
            }
        }
    }

    @Test
    public void testRawPaths() throws Exception {
        for (Pair<String, List<Object>> pair : RAW_PATHS) {
            JsonPath path = JsonPathImpl.compile(pair.getRight());
            assertEquals(JsonPathImpl.compile(pair.getLeft()), path);
        }
    }

    @Test
    public void testWithEnvData() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        JsonPathExpressionService jsonPathExpressionService = new JsonPathExpressionService();
        ScopeStack scopeStack = new ScopeStack();
        scopeStack.push(new EnvironmentScope(
                new ImmutableMap.Builder<String, Object>()
                        .put("name", "foo_num")
                        .build()));
        BigInteger num = JsonPathImpl.compile("$[(_name)]", jsonPathExpressionService)
                .withScopes(scopeStack)
                .read(sourceData);

        assertEquals(10, num.intValue());
    }

    @Test
    public void testSimpleWrite() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        BigInteger newValue = BigInteger.valueOf(20);

        JsonPathImpl.compile("$.foo_num").write(sourceData, newValue);
        assertEquals(newValue, JsonPathImpl.compile("$.foo_num").read(sourceData));
    }

    @Test
    public void testRootMapWrite() throws Exception {
        Map oldData = new HashMap();
        Map newData = readSourceData(JSON_DATA_2);

        JsonPathImpl.compile("$").write(oldData, newData);
        assertEquals(newData, oldData);
    }

    @Test
    public void testRootMapNullWrite() throws Exception {
        Map oldData = new HashMap();
        Map newData = null;

        oldData.put("dummy", "data");
        JsonPathImpl.compile("$").write(oldData, newData);
        assertTrue(oldData.isEmpty());
    }

    @Test
    public void testRootListWrite() throws Exception {
        List oldData = new ArrayList();
        List newData = Arrays.asList(1, 2, 3);

        JsonPathImpl.compile("$").write(oldData, newData);
        assertEquals(newData, oldData);
    }

    @Test
    public void testRootListNullWrite() throws Exception {
        List oldData = new ArrayList(Arrays.asList(1, 2, 3));
        List newData = null;

        JsonPathImpl.compile("$").write(oldData, newData);
        assertTrue(oldData.isEmpty());
    }

    @Test(expected = UnexpectedTypeException.class)
    public void testRootWriteWithIncompatibleData() throws Exception {
        List oldData = new ArrayList();
        Map newData = readSourceData(JSON_DATA_2);

        JsonPathImpl.compile("$").write(oldData, newData);
    }

    @Test(expected = UnexpectedTypeException.class)
    public void testRootWriteWithInteger() throws Exception {
        List oldData = new ArrayList();

        JsonPathImpl.compile("$").write(oldData, 1);
    }

    @Test
    public void testArrayWrite() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        BigInteger newValue = BigInteger.valueOf(20);

        JsonPathImpl.compile("$.foo_array[0]").write(sourceData, newValue);
        assertEquals(newValue, JsonPathImpl.compile("$.foo_array[0]").read(sourceData));
    }

    @Test
    public void testNewArrayWriteDeep() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        BigInteger newValue = BigInteger.valueOf(20);

        JsonPathImpl.compile("$.new_array[1].a").write(sourceData, newValue);
        assertEquals(newValue, JsonPathImpl.compile("$.new_array[1].a").read(sourceData));
    }

    @Test
    public void testNewFieldWrite() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        BigInteger newValue = BigInteger.valueOf(20);

        JsonPathImpl.compile("$.bar").write(sourceData, newValue);
        assertEquals(newValue, JsonPathImpl.compile("$.bar").read(sourceData));
    }

    @Test
    public void testWildcardWrite() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        BigInteger newValue = BigInteger.valueOf(20);

        JsonPathImpl.compile("$.foo_array[*]").write(sourceData, newValue);
        assertEquals(Arrays.asList(newValue, newValue, newValue, newValue),
                JsonPathImpl.compile("$.foo_array[*]").read(sourceData));
    }

    @Test
    public void testNewObjectPathWrite() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        BigInteger newValue = BigInteger.valueOf(20);
        JsonPath path = JsonPathImpl.compile("$.non_existent");

        path.write(sourceData, newValue);
        assertEquals(newValue, path.read(sourceData));
    }

    @Test
    public void testNewWildcardPathWrite() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        BigInteger newValue = BigInteger.valueOf(20);
        JsonPath path = JsonPathImpl.compile("$.new_array[*]");

        path.write(sourceData, newValue);
        assertEquals(Arrays.asList(newValue), path.read(sourceData));
    }

    @Test
    public void testSimpleInsert() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        List array = JsonPathImpl.compile("$.foo_array").read(sourceData);
        int oldSize = array.size();
        BigInteger newValue = BigInteger.valueOf(-10);

        JsonPathImpl.compile("$.foo_array[0]").insert(sourceData, newValue);
        assertEquals(oldSize + 1, array.size());
        assertEquals(newValue, array.get(0));
    }

    @Test
    public void testEndInsert() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        List array = JsonPathImpl.compile("$.foo_array").read(sourceData);
        int oldSize = array.size();
        BigInteger newValue = BigInteger.valueOf(-10);

        JsonPathImpl.compile("$.foo_array[(@.length)]").insert(sourceData, newValue);
        assertEquals(oldSize + 1, array.size());
        assertEquals(newValue, array.get(oldSize));
    }

    @Test
    public void testSimpleDelete() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);

        JsonPathImpl.compile("$.foo_num").delete(sourceData);
        assertFalse(sourceData.containsKey("foo_num"));
        // Make sure we haven't deleted everything.
        assertTrue(sourceData.containsKey("foo_array"));
    }

    @Test
    public void testEvalDelete() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);

        JsonPathImpl.compile("$.foo_array.eval(value)[0]").delete(sourceData);
        assertEquals(Arrays.asList(BigInteger.valueOf(20),
                BigInteger.valueOf(30),
                BigInteger.valueOf(40)), sourceData.get("foo_array"));
    }

    @Test(expected = FieldNotFoundException.class)
    public void testMissingDelete() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);

        JsonPathImpl.compile("$.non_existent").delete(sourceData);
    }

    @Test(expected = IndexNotFoundException.class)
    public void testMissingArrayDelete() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);

        JsonPathImpl.compile("$.foo_array[23]").delete(sourceData);
    }

    @Test
    public void testWildcardListDelete() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        List characters = JsonPathImpl.compile("$.tree.characters").read(sourceData);

        JsonPathImpl.compile("$.tree.characters[*]").delete(sourceData);
        assertEquals(0, characters.size());
    }

    @Test
    public void testWildcardMapDelete() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        Map tree = JsonPathImpl.compile("$.tree").read(sourceData);

        JsonPathImpl.compile("$.tree[*]").delete(sourceData);
        assertEquals(0, tree.size());
    }

    @Test
    public void testFilterDelete() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        List characters = JsonPathImpl.compile("$.tree.characters").read(sourceData);
        List expected = JsonPathImpl.compile(
                "$.tree.characters[?(@.last_name != 'Grayson')]").read(sourceData);

        JsonPathImpl.compile("$.tree.characters[?(@.last_name == 'Grayson')]").delete(sourceData);
        assertEquals(1, characters.size());
        assertEquals(expected, characters);
    }

    @Test
    public void testUnionDelete() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        List array = JsonPathImpl.compile("$.foo_array").read(sourceData);
        JsonPathImpl.compile("$.foo_array[0,0,3]").delete(sourceData);
        assertEquals(Arrays.asList(BigInteger.valueOf(20), BigInteger.valueOf(30)), array);
    }

    @Test
    public void testDescendantDelete() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        List<Map> characters = JsonPathImpl.compile("$.tree.characters").read(sourceData);

        JsonPathImpl.compile("$..first_name").delete(sourceData);
        for (Map character : characters) {
            assertFalse(character.containsKey("first_name"));
        }
    }

    @Test
    public void testWalk() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        List<WalkResult> result = JsonPathImpl.compile(
                "$..locations[*]").walk(sourceData);
        List[] expectedPaths = new List[] {
                Arrays.asList("$", "tree", "locations", 0),
                Arrays.asList("$", "tree", "locations", 1)
        };

        for (int lpc = 0; lpc < result.size(); lpc++) {
            List<Object> actualPath = new ArrayList<>();

            for (Pair<Object, Object> pair : result.get(lpc).getParentPath()) {
                actualPath.add(pair.getRight());
            }

            assertEquals(expectedPaths[lpc], actualPath);
        }
    }

    @Test
    public void testWalkWithParents() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        Map expectedData = readSourceData(EXPECTED_WALK_DATA);
        List<Object> expectedParentMaps = JsonPathImpl.compile("$.results").read(expectedData);
        List<WalkResult> result = JsonPathImpl.compile(
                "$..locations[*]").walk(sourceData);

        for (int lpc = 0; lpc < result.size(); lpc++) {
            Map<Object, Object> actualParentMap = result.get(lpc).getParentHierarchy();
            assertEquals(expectedParentMaps.get(lpc), actualParentMap);
        }
    }

    @Test
    public void testWalkAndSet() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        Object newRoot = new Object();
        List<WalkResult> results = JsonPathImpl.compile("$").walk(sourceData);
        WalkResult walkResult = results.get(0);
        assertEquals(sourceData, walkResult.getValue());
        assertEquals(newRoot, walkResult.setValue(newRoot));

        for (WalkResult walkResult1: JsonPathImpl.compile("$.tree.locations[*].title").walk(
                sourceData)) {
            walkResult1.setValue("blank");
        }
        List<String> titles = JsonPathImpl.compile("$.tree.locations[*].title").read(sourceData);
        assertEquals(Arrays.asList("blank", "blank"), titles);
    }

    @Test
    public void testWalkWithMap() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        JsonPath path = JsonPathImpl.compile("$.tree.map({key: key, value: value}).*");
        List<WalkResult> result = path.walk(sourceData);
        List<Object> expected = path.read(sourceData);
        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), result.get(i).getValue());
        }
    }

    @Test
    public void testWalkRoot() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        JsonPath path = JsonPaths.compile("$");
        List<WalkResult> result = path.walk(sourceData);
        assertEquals(1, result.size());
        assertEquals(sourceData, result.get(0).getValue());
    }

    @Test
    public void testWalkSimplePath() throws Exception {
        Map sourceData = readSourceData(JSON_DATA_2);
        JsonPath path = JsonPaths.compile("$.foo_array");
        List<WalkResult> result = path.walk(sourceData);
        assertEquals(1, result.size());
        List<Pair<Object, Object>> parentPath = result.get(0).getParentPath();
        assertEquals("$", parentPath.get(0).getRight());
        assertEquals("foo_array", parentPath.get(1).getRight());
    }

    @Test
    public void testInvalidPath() throws IOException {
        try {
            Map sourceData = readSourceData(JSON_DATA_2);
            JsonPathImpl.compile("$.aaa.bbb").read(sourceData);
        } catch (InvalidPathException e) {
            // Make sure the exception mentions the actual part of the path that is invalid
            // and not the trailing bits.
            assertEquals("$.aaa", e.getPath());
            assertEquals("$", JsonPathImpl.compile(e.getParentPath()).toString());
        }
    }

    @Test
    public void testResolvePath() throws Exception {
        List<Object> resolvedPath = JsonPathImpl.compileStatic("$.foo.bar").resolvedPath(null);
        assertEquals(Arrays.asList("foo", "bar"), resolvedPath);
    }

    @Test
    public void testStartsWith() throws Exception {
        JsonPath path = JsonPaths.compileStatic("$.foo.bar");
        assertTrue(path.startsWith(JsonPaths.compileStatic("$.foo")));
        assertTrue(path.startsWith(JsonPaths.compileStatic("$['foo']")));
        assertFalse(path.startsWith(JsonPaths.compileStatic("fooooo")));
    }

    @Test
    public void testEndsWith() throws Exception {
        JsonPath path = JsonPathImpl.compileStatic("$.foo.bar");
        assertTrue(path.endsWith(JsonPathImpl.compileStatic("bar")));
        assertTrue(path.endsWith(JsonPathImpl.compileStatic("$['bar']")));
        assertFalse(path.endsWith(JsonPathImpl.compileStatic("foo")));
    }

    @Test
    public void testJoin() throws Exception {
        for (Pair<String, List<String>> pair : JOIN_DATA) {
            JsonPath path = JsonPaths.root();

            for (String subpath : pair.getValue()) {
                path = path.join(JsonPaths.compileStatic(subpath));
            }
            assertEquals(pair.getLeft(), path.toString());
        }
    }

    @Test
    public void testSubPath() throws Exception {
        JsonPath path = JsonPaths.compile("$.foo.bar.*.baz");

        assertEquals(JsonPaths.compile("$.foo"), path.subPath(1, 2));
        assertEquals(JsonPaths.compile("$.*.baz"), path.subPath(-2));
        assertEquals(JsonPaths.root(), path.subPath(1, 1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void badSubPath() throws Exception {
        JsonPath path = JsonPaths.compile("$.foo.bar.*.baz");

        path.subPath(-50);
    }

    @Test
    public void testEmptyPath() {
        assertEquals(JsonPaths.root(), JsonPaths.compileStatic(""));
    }
}
