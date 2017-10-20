/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2012, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */
package com.snaplogic.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.snaplogic.api.SnapException;
import com.snaplogic.common.jsonpath.JsonPath;
import com.snaplogic.jsonpath.JsonPathImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * PropertyDataTest
 *
 * @author mklumpp
 */
public class PropertyDataTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private JsonPath DELIM_PATH = JsonPathImpl.compileStatic(
            "$property_values.read.delim");
    private JsonPathUtil jsonPathUtil;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(final Binder binder) {
            }
        });
        jsonPathUtil = injector.getInstance(JsonPathUtil.class);
    }

    @Test
    public void testCreateData() {
        Map<String, Object> rawData = Maps.newHashMap();
        final String jsonPath = "$property_values.read.delim.value1";
        addAndValidate(rawData, jsonPath, "test");
    }

    @Test
    public void testCreateListData() {
        Map<String, Object> rawData = Maps.newHashMap();
        String jsonPath = "$property_values.read.delim[0].value1";
        addAndValidate(rawData, jsonPath, "test1");
        jsonPath = "$property_values.read.delim[1].value2";
        addAndValidate(rawData, jsonPath, "test2");
        jsonPath = "$property_values.read.delim[1].value3";
        addAndValidate(rawData, jsonPath, "test3");
    }

    @Test
    public void testCreateRootMapEntries() {
        Map<String, Object> rawData = Maps.newHashMap();
        addAndValidate(rawData, "$a", "test1");
        addAndValidate(rawData, "$b", "test2");
    }

    @Test
    public void testCreateRootListEntries() {
        List<String> rawData = Lists.newArrayList();
        addAndValidate(rawData, "$[0]", "test1");
        addAndValidate(rawData, "$[1]", "test2");
        org.junit.Assert.assertEquals(rawData.size(), 2);
    }

    @Test
    public void testCreateIndexedRootListEntries() {
        List<String> rawData = new ArrayList<>(3);
        addAndValidate(rawData, "$[1]", "test1");
        addAndValidate(rawData, "$[2]", "test2");
        addAndValidate(rawData, "$[0]", "test0");
        addAndValidate(rawData, "$[8]", "test8");
        addAndValidate(rawData, "$[2]", "test5");
        addAndValidate(rawData, "$[7]", "test7");
        org.junit.Assert.assertEquals(rawData.size(), 9);
    }

    @Test
    public void testCreateMoreListData() {
        Map<String, Object> rawData = Maps.newHashMap();
        String jsonPath = "$property_values.read.delim[0].label.value1";
        addAndValidate(rawData, jsonPath, "test1");
        jsonPath = "$property_values.read.delim[1].label.value2";
        addAndValidate(rawData, jsonPath, "test2");
        jsonPath = "$property_values.read.delim[2].label.value3";
        addAndValidate(rawData, jsonPath, "test3");
        org.junit.Assert.assertEquals(rawData.size(), 1);
        List<Object> list = DELIM_PATH.readStatic(rawData);
        org.junit.Assert.assertEquals(list.size(), 3);
    }

    @Test
    public void testCreateMoreListDataIndexed() {
        Map<String, Object> rawData = Maps.newHashMap();
        String jsonPath = "$property_values.read.delim[1].label.value1";
        addAndValidate(rawData, jsonPath, "test1");
        jsonPath = "$property_values.read.delim[2].label.value2";
        addAndValidate(rawData, jsonPath, "test2");
        jsonPath = "$property_values.read.delim[0].label.value3";
        addAndValidate(rawData, jsonPath, "test3");
        jsonPath = "$property_values.read.delim[8].label.value8";
        addAndValidate(rawData, jsonPath, "test8");
        jsonPath = "$property_values.read.delim[5].label.value5";
        addAndValidate(rawData, jsonPath, "test5");
        List<Object> list = DELIM_PATH.readStatic(rawData);
        org.junit.Assert.assertEquals(list.size(), 9);
    }

    @Test
    public void testCreateMoreAndMoreListData() {
        Map<String, Object> rawData = Maps.newHashMap();
        String jsonPath = "$property_values.read.delim[0]";
        addAndValidate(rawData, jsonPath, "test1");
        jsonPath = "$property_values.read.delim[1]";
        addAndValidate(rawData, jsonPath, "test2");
        jsonPath = "$property_values.read.delim[2]";
        addAndValidate(rawData, jsonPath, "test3");
        List<Object> list = DELIM_PATH.readStatic(rawData);
        org.junit.Assert.assertEquals(list.size(), 3);
    }

    @Test
    public void testCreateListOfListData() {
        Map<String, Object> rawData = Maps.newHashMap();
        String jsonPath = "$test[0][0]";
        addAndValidate(rawData, jsonPath, "test00");
        jsonPath = "$test[0][1]";
        addAndValidate(rawData, jsonPath, "test01");
        jsonPath = "$test[1][1]";
        addAndValidate(rawData, jsonPath, "test11");
        jsonPath = "$test[8][8]";
        addAndValidate(rawData, jsonPath, "test88");
        jsonPath = "$test[5][8]";
        addAndValidate(rawData, jsonPath, "test58");
        List<Object> list = JsonPathImpl.compileStatic("$test").readStatic(rawData);
        org.junit.Assert.assertEquals(list.size(), 9);
    }

    @Test
    public void testMoveAllSingleVal() {
        Map<String, Object> data = Maps.newHashMap();
        jsonPathUtil.write(data, "$names[*]", "John");
        Map<String, Object> expectedData = ImmutableMap.<String, Object>of(
                "names", Lists.newArrayList("John"));
        assertThat(data, equalTo(expectedData));
    }

    @Test
    public void testMoveAllWithField() {
        Map<String, Object> data = Maps.newHashMap();
        jsonPathUtil.write(data, "$names[*].name", "Jim");
        Map<String, Object> expectedData = ImmutableMap.<String, Object>of(
                "names", Lists.newArrayList(ImmutableMap.of("name", "Jim")));
        assertThat(data, equalTo(expectedData));
    }

    private void addAndValidate(Object rawData, String jsonPath, Object value) {
        jsonPathUtil.write(rawData, jsonPath, value);
        Object val = JsonPathImpl.compileStatic(jsonPath).readStatic(rawData);
        org.junit.Assert.assertEquals(value, val);
    }

    @Test
    public void testInvalidListData() {
        thrown.expect(SnapException.class);
        Map<String, Object> rawData = Maps.newHashMap();
        String jsonPath = "$property_values.read.delim[x].label.value1";
        String value = "test1";
        jsonPathUtil.write(rawData, jsonPath, value);
        thrown.expectMessage("The provided JSON path");
    }
}

