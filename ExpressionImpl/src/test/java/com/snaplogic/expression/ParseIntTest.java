package com.snaplogic.expression;

import org.junit.Test;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the javascript parseInt global function.
 *
 * @author jinloes
 */
public class ParseIntTest extends ExpressionTest {

    // TODO Jon this commented out code will be used later to test hex
    /*@Test
    public void testParseInt() {
        assertEquals(BigInteger.valueOf(15), eval("parseInt(\" 0xF\", 16)"));
    }*/

    @Test
    public void testParseInt() {
        assertEquals(BigInteger.valueOf(10), eval("parseInt(\"10\")"));
    }

    @Test
    public void testParseNullInt() {
        Map<String, Object> data = new HashMap<>();
        data.put("a", null);
        assertEquals(null, eval("parseInt($a)", (Object) data));
    }

    @Test
    public void testParseNumbersToInt() {
        Map<String, Object> data = new HashMap<>();
        data.put("a", 1);
        assertEquals(BigInteger.ONE, eval("parseInt($a)", (Object) data));
        data.put("a", 1d);
        assertEquals(BigInteger.ONE, eval("parseInt($a)", (Object) data));
        data.put("a", 1f);
        assertEquals(BigInteger.ONE, eval("parseInt($a)", (Object) data));
        data.put("a", 1l);
        assertEquals(BigInteger.ONE, eval("parseInt($a)", (Object) data));
    }

    @Test
    public void testParseIntFloat() {
        assertEquals(BigInteger.valueOf(10), eval("parseInt(\"10.8\")"));
    }

    @Test
    public void testParseIntRadix() {
        assertEquals(BigInteger.valueOf(10), eval("parseInt(\"10\", 10)"));
    }

    @Test
    public void testParseIntRadix8() {
        assertEquals(BigInteger.valueOf(15), eval("parseInt(\"17\", 8)"));
    }

    @Test
    public void testParseIntFloatRadix8() {
        assertEquals(BigInteger.valueOf(15), eval("parseInt(\"17.5\", 8)"));
    }

    @Test
    // Note: Javascript actually passes this
    public void testParseIntBadFloatRadix() {
        assertEquals(BigInteger.valueOf(15), eval("parseInt(\"17.a.5\", 8)"));
    }

    @Test
    public void testParseIntFloatRadixFloat() {
        assertEquals(BigInteger.valueOf(15), eval("parseInt(\"17.5\", 8.8)"));
    }

    @Test
    public void testParseIntNan() {
        assertEquals((Object) Double.NaN, eval("parseInt(\"abc\")"));
    }

    @Test
    public void testParseIntNanRadix() {
        assertEquals((Object) Double.NaN, eval("parseInt(\"abc\", 8)"));
    }

    @Test
    public void testParseIntFromFloat() {
        assertEquals(BigInteger.valueOf(0), eval("parseInt(1 / 2)"));
    }

    @Test
    public void testParseIntFromFloatLarge() {
        assertEquals(BigInteger.valueOf(15), eval("parseInt(15.89)"));
    }

    @Test
    public void testParseIntWithLeadingWhiteSpace() {
        assertEquals(BigInteger.valueOf(12), eval("parseInt(\"  0x0c\")"));
    }

    @Test
    public void testParseIntWithHex() {
        assertEquals(BigInteger.valueOf(12), eval("parseInt(\"0x0c\")"));
    }

    @Test
    public void testParseIntWithNegativeHex() {
        assertEquals(BigInteger.valueOf(-12), eval("parseInt(\"-0x0c\")"));
    }

    @Test
    public void testParseIntWithPositiveHex() {
        assertEquals(BigInteger.valueOf(12), eval("parseInt(\" +0x0c\")"));
    }

    @Test
    public void testParseIntWithPrefix() {
        assertEquals(BigInteger.valueOf(42780), eval("parseInt(\"0x0c\", 36)"));
    }

    @Test
    public void testParseIntNoArgs() {
        assertEquals(null, eval("parseInt()"));
    }

    @Test
    public void testParseIntWithGarbage() {
        assertEquals(BigInteger.valueOf(123), eval("parseInt('123abc')"));
    }

    @Test
    public void testParseIntOfHexWithGarbage() {
        assertEquals(BigInteger.valueOf(0x123abc), eval("parseInt('0x123abcZ')"));
    }

    @Test
    public void testParseIntWithNegativeNumber() {
        assertEquals(new BigInteger("-9"), eval("parseInt(-9.0)"));
    }
}
