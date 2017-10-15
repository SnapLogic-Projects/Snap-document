/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2016, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Executes tests in the Test-262 test suite using the expression language.
 *
 * @see https://github.com/tc39/test262/
 *
 * @author tstack
 */
public class Test262 extends ExpressionTest {

    private static InputStream readSourceData(String fileName) throws IOException {
        return Test262.class.getResourceAsStream(fileName);
    }

    private static final String SL_ROOT = "SL_ROOT";
    private static final String TEST262_ROOT = "TEST262_ROOT";

    private static final String TEST262_PATH;

    static {
        String root = System.getenv(TEST262_ROOT);
        Path test262root = null;

        if (root == null) {
            root = System.getenv(SL_ROOT);
            if (root != null) {
                Path slRoot = Paths.get(root);
                Path parent = slRoot.getParent();

                if (parent != null) {
                    test262root = parent.resolve("test262");
                }
            }
        } else {
            test262root = Paths.get(root);
        }

        if (test262root != null) {
            TEST262_PATH = test262root.resolve("test/built-ins").toAbsolutePath().toString();
        } else {
            TEST262_PATH = StringUtils.EMPTY;
        }
    }

    private static final String[] SEARCH_TESTS = {
            "S15.5.4.12_A1_T4.js",
            "S15.5.4.12_A1_T14.js",
            "S15.5.4.12_A1_T10.js",
            "S15.5.4.12_A2_T1.js",
            "S15.5.4.12_A2_T2.js",
            "S15.5.4.12_A2_T3.js",
            "S15.5.4.12_A2_T4.js",
            "S15.5.4.12_A2_T5.js",
            "S15.5.4.12_A2_T6.js",
            "S15.5.4.12_A2_T7.js",
            "S15.5.4.12_A3_T1.js",
            "S15.5.4.12_A3_T2.js",
    };

    private static final String[] MATCH_TESTS = {
            "S15.5.4.10_A2_T1.js",
            "S15.5.4.10_A2_T2.js",
            "S15.5.4.10_A2_T3.js",
            "S15.5.4.10_A2_T4.js",
            "S15.5.4.10_A2_T5.js",
            "S15.5.4.10_A2_T6.js",
            "S15.5.4.10_A2_T7.js",
            "S15.5.4.10_A2_T8.js",
            "S15.5.4.10_A2_T9.js",
            "S15.5.4.10_A2_T10.js",
            "S15.5.4.10_A2_T11.js",
            "S15.5.4.10_A2_T12.js",
            "S15.5.4.10_A2_T13.js",
            "S15.5.4.10_A2_T14.js",
            "S15.5.4.10_A2_T15.js",
            "S15.5.4.10_A2_T16.js",
            "S15.5.4.10_A2_T17.js",
            "S15.5.4.10_A2_T18.js",
    };

    private static final String[] REPLACE_TESTS = {
            "S15.5.4.11_A2_T1.js",
            "S15.5.4.11_A2_T2.js",
            "S15.5.4.11_A2_T3.js",
            "S15.5.4.11_A2_T4.js",
            "S15.5.4.11_A2_T5.js",
            "S15.5.4.11_A2_T6.js",
            "S15.5.4.11_A2_T7.js",
            "S15.5.4.11_A2_T8.js",
            "S15.5.4.11_A2_T9.js",
            "S15.5.4.11_A2_T10.js",

            "S15.5.4.11_A3_T1.js",
            "S15.5.4.11_A3_T2.js",
            "S15.5.4.11_A3_T3.js",

            "S15.5.4.11_A5_T1.js",
    };

    private static final String[] SPLIT_TESTS = {
            "S15.5.4.14_A2_T1.js",
            "S15.5.4.14_A2_T2.js",
            "S15.5.4.14_A2_T3.js",
            "S15.5.4.14_A2_T4.js",
            "S15.5.4.14_A2_T5.js",
            "S15.5.4.14_A2_T6.js",
            "S15.5.4.14_A2_T7.js",
            "S15.5.4.14_A2_T8.js",
            "S15.5.4.14_A2_T9.js",
            "S15.5.4.14_A2_T10.js",
            "S15.5.4.14_A2_T11.js",
            "S15.5.4.14_A2_T12.js",
            "S15.5.4.14_A2_T12.js",
            "S15.5.4.14_A2_T13.js",
            "S15.5.4.14_A2_T14.js",
            "S15.5.4.14_A2_T15.js",
            "S15.5.4.14_A2_T16.js",
            "S15.5.4.14_A2_T17.js",
            "S15.5.4.14_A2_T18.js",
            "S15.5.4.14_A2_T19.js",
            "S15.5.4.14_A2_T20.js",
            "S15.5.4.14_A2_T21.js",
            "S15.5.4.14_A2_T22.js",
            "S15.5.4.14_A2_T23.js",
            "S15.5.4.14_A2_T24.js",
            "S15.5.4.14_A2_T25.js",
            "S15.5.4.14_A2_T26.js",
            "S15.5.4.14_A2_T27.js",
            "S15.5.4.14_A2_T28.js",
            "S15.5.4.14_A2_T29.js",
            "S15.5.4.14_A2_T30.js",
            "S15.5.4.14_A2_T31.js",
            "S15.5.4.14_A2_T32.js",
            "S15.5.4.14_A2_T33.js",
            "S15.5.4.14_A2_T34.js",
            "S15.5.4.14_A2_T35.js",
            "S15.5.4.14_A2_T36.js",
            // "S15.5.4.14_A2_T37.js",
            "S15.5.4.14_A2_T38.js",
            "S15.5.4.14_A2_T39.js",
            "S15.5.4.14_A2_T40.js",
            "S15.5.4.14_A2_T41.js",
            "S15.5.4.14_A2_T42.js",
            "S15.5.4.14_A2_T43.js",

            "S15.5.4.14_A4_T1.js",
            "S15.5.4.14_A4_T2.js",
            "S15.5.4.14_A4_T3.js",
            "S15.5.4.14_A4_T4.js",
            "S15.5.4.14_A4_T5.js",
            "S15.5.4.14_A4_T6.js",
            "S15.5.4.14_A4_T7.js",
            "S15.5.4.14_A4_T8.js",
            "S15.5.4.14_A4_T9.js",
            "S15.5.4.14_A4_T10.js",
            "S15.5.4.14_A4_T11.js",
            "S15.5.4.14_A4_T12.js",
            "S15.5.4.14_A4_T13.js",
            "S15.5.4.14_A4_T14.js",
            "S15.5.4.14_A4_T15.js",
            "S15.5.4.14_A4_T16.js",
            "S15.5.4.14_A4_T17.js",
            "S15.5.4.14_A4_T18.js",
            "S15.5.4.14_A4_T19.js",
            "S15.5.4.14_A4_T20.js",
            "S15.5.4.14_A4_T20.js",
            "S15.5.4.14_A4_T21.js",
            "S15.5.4.14_A4_T22.js",
            "S15.5.4.14_A4_T23.js",
            "S15.5.4.14_A4_T24.js",
            "S15.5.4.14_A4_T25.js",
    };

    @Before
    public void setup() {
        Assume.assumeTrue("Clone the following repo into SL_ROOT to run these " +
                        "tests: https://github.com/tc39/test262/",
                StringUtils.isNotBlank(TEST262_PATH) && Files.exists(Paths.get(TEST262_PATH)));
    }

    private void testMethod(String type, String dir, String[] tests) throws Exception {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        Bindings bindings = new SimpleBindings();
        scriptEngineManager.setBindings(bindings);
        ScriptEngine engine = scriptEngineManager.getEngineByName("js");

        engine.eval(new InputStreamReader(readSourceData("/test262/init.js")));
        for (int lpc = 0; lpc < tests.length; lpc++) {
            try {
                engine.eval(new FileReader(String.format("%s/%s/prototype/%s/%s", TEST262_PATH,
                        type, dir, tests[lpc])));
            } catch (Exception e) {
                throw new Exception("test262 test case failed: " + tests[lpc], e);
            }
        }
    }

    @Test
    public void testSearch() throws Exception {
        testMethod("String", "search", SEARCH_TESTS);
    }

    @Test
    public void testMatch() throws Exception {
        testMethod("String", "match", MATCH_TESTS);
    }

    @Test
    public void testReplace() throws Exception {
        testMethod("String", "replace", REPLACE_TESTS);
    }

    @Test
    public void testSplit() throws Exception {
        testMethod("String", "split", SPLIT_TESTS);
    }
}
