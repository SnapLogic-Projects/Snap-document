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

import com.snaplogic.snap.api.SnapDataException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the methods in the DigestExpression class.
 *
 * @author vsoni
 */
public class DigestClassTest extends ExpressionTest {

    private static final String TEST_MESSAGE = "1";
    private static final String TEST_OUTPUT_MD5 = "c4ca4238a0b923820dcc509a6f75849b";
    private static final String TEST_OUTPUT_SHA1 = "356a192b7913b04c54574d18c28d46e6395428ab";
    private static final String TEST_OUTPUT_SHA256 =
            "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b";
    private static final String TEST_MESSAGE_STR = "JOHNNY";
    private static final String TEST_OUTPUT_MD5_STR = "bd6f3a5e1874c8d16bea06f06b043515";
    private static final String TEST_OUTPUT_SHA1_STR = "85581bab614ad552c999f8f00a8bd076ca318a61";
    private static final String TEST_OUTPUT_SHA256_STR =
            "beda9438cdd8c5e2bd21d6191b73700a7ed9c82d8dcd9d21cdbcc7fdd31c07be";
    private static final double ILLEGAL_DATA = 1234.4;

    @Test
    public void testMd5() throws IOException {
        String md5 = (String) eval("Digest.md5($)", TEST_MESSAGE.getBytes());
        assertEquals(TEST_OUTPUT_MD5, md5);
        md5 = (String) eval("Digest.md5($)", TEST_MESSAGE_STR);
        assertEquals(TEST_OUTPUT_MD5_STR, md5);
    }

    @Test
    public void testSha1() throws IOException {
        String sha1 = (String) eval("Digest.sha1($)", TEST_MESSAGE.getBytes());
        assertEquals(TEST_OUTPUT_SHA1, sha1);
        sha1 = (String) eval("Digest.sha1($)", TEST_MESSAGE_STR);
        assertEquals(TEST_OUTPUT_SHA1_STR, sha1);
    }

    @Test
    public void testSha256() throws IOException {
        String sha256 = (String) eval("Digest.sha256($)", TEST_MESSAGE.getBytes());
        assertEquals(TEST_OUTPUT_SHA256, sha256);
        sha256 = (String) eval("Digest.sha256($)", TEST_MESSAGE_STR);
        assertEquals(TEST_OUTPUT_SHA256_STR, sha256);
    }

    @Test(expected = SnapDataException.class)
    public void testInvalidData() throws IOException {
        String md5 = (String) eval("Digest.md5($)", ILLEGAL_DATA);
    }

    @Test(expected = SnapDataException.class)
    public void testNullData() throws IOException {
        String md5 = (String) eval("Digest.md5($)", null);
    }

    @Test
    public void testJaninoDigest() throws IOException {
        String md5 = eval("Digest.md5($)", TEST_MESSAGE.getBytes());
        assertEquals(TEST_OUTPUT_MD5, new String(md5));
        String sha1 = eval("Digest.sha1($)", TEST_MESSAGE.getBytes());
        assertEquals(TEST_OUTPUT_SHA1, new String(sha1));
        String sha256 = eval("Digest.sha256($)", TEST_MESSAGE.getBytes());
        assertEquals(TEST_OUTPUT_SHA256, new String(sha256));
    }
}
