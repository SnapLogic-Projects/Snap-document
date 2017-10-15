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

package com.snaplogic.expression;

import com.snaplogic.snap.api.SnapDataException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the methods in the GZip class.
 *
 * @author ksubramanian
 */
public class GZipClassTest extends ExpressionTest {

    private static final String TEST_MESSAGE = "Hello, World!";
    private static final String TEST_CORRUPT_DATA = "\u001F�\b\u0000\u0000\u0000\u0000\u0000" +
            "\u0000\u0000�H����Q\b�/�IQ\u0024\u0000��J�\n\u0000\u0002\u0000";

    @Test
    public void testCompressDecompress() throws UnsupportedEncodingException {
        byte[] compressedData = (byte[]) eval("GZip.compress($)", TEST_MESSAGE.getBytes());
        byte[] uncompressedData = (byte[]) eval("GZip.decompress($)", compressedData);
        assertEquals(TEST_MESSAGE, new String(uncompressedData));
    }

    @Test(expected = SnapDataException.class)
    public void testDecompressCorruptData() throws Exception {
        eval("GZip.decompress($)", TEST_CORRUPT_DATA.getBytes());
    }

    @Test(expected = SnapDataException.class)
    public void testBadCompressArgType() {
        eval("GZip.compress(1)");
    }

    @Test(expected = SnapDataException.class)
    public void testBadDecompressArgType() {
        eval("GZip.decompress(1)");
    }

    @Test(expected = SnapDataException.class)
    public void testBadDecompressNoArgs() {
        eval("GZip.decompress()");
    }

    @Test(expected = SnapDataException.class)
    public void testBadCompressNoArgs() {
        eval("GZip.compress()");
    }
}
