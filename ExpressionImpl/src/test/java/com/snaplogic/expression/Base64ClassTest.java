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

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the methods in the Base64 class.
 *
 * @author tstack
 */
public class Base64ClassTest extends ExpressionTest {

    private static final String TEST_MESSAGE = "Hello, World!";
    private static final String TEST_MESSAGE64 = "SGVsbG8sIFdvcmxkIQ==";
    private static final byte[] TEST_DATA = { 0x00, (byte) 0xff };

    @Test
    public void testEncodeBasic() {
        assertEquals(TEST_MESSAGE64, eval("Base64.encode($)", TEST_MESSAGE));
    }

    @Test
    public void testEncodeAsBinaryBasic() {
        assertArrayEquals(TEST_MESSAGE64.getBytes(), (byte[]) eval("Base64.encodeAsBinary($)",
                TEST_MESSAGE));
    }

    @Test
    public void testDecodeBasic() {
        assertEquals(TEST_MESSAGE, eval("Base64.decode($)", TEST_MESSAGE64));
    }

    @Test
    public void testDecodeByteArray() {
        assertEquals(TEST_MESSAGE, eval("Base64.decode($)", TEST_MESSAGE64.getBytes
                (StandardCharsets.UTF_8)));
    }

    @Test
    public void testEncodeBinary() {
        assertArrayEquals(TEST_DATA, (byte[]) eval("Base64.decodeAsBinary(Base64.encode($))",
                TEST_DATA));
    }

    @Test
    public void testEncodeGzippedBinary() {
        byte[] compressedData = eval("GZip.compress($)", TEST_MESSAGE.getBytes());
        byte[] uncompressedData = eval("GZip.decompress($)", compressedData);
        assertEquals(TEST_MESSAGE, new String(uncompressedData));

        String encodedData = eval("Base64.encode($)", compressedData);
        byte[] decodedData = eval("Base64.decodeAsBinary($)", encodedData);
        String uncompressedString = new String((byte[]) eval("GZip.decompress($)", decodedData));
        assertEquals(TEST_MESSAGE, uncompressedString);

        assertEquals(TEST_MESSAGE, uncompressedString);
    }


    @Test
    public void testRoundTrip() {
        assertEquals(TEST_MESSAGE, eval("Base64.decode(Base64.encode($))", TEST_MESSAGE));
    }

    @Test(expected = SnapDataException.class)
    public void testBadDecode() {
        eval("Base64.decode('a')");
    }

    @Test(expected = SnapDataException.class)
    public void testBadEncodeArgType() {
        eval("Base64.encode(1)");
    }

    @Test(expected = SnapDataException.class)
    public void testBadDecodeArgType() {
        eval("Base64.decode(1)");
    }

    @Test(expected = SnapDataException.class)
    public void testBadDecodeNoArgs() {
        eval("Base64.decode()");
    }

    @Test(expected = SnapDataException.class)
    public void testBadEncodeNoArgs() {
        eval("Base64.encode()");
    }
}
