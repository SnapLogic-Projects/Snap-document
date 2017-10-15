package com.snaplogic.expression;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Tests for javascript parseFloat method.
 *
 * @author jinloes
 */
public class ParseFloatTest extends ExpressionTest {
    @Test
    public void testParseFloat() {
        assertEquals(new BigDecimal("3.14"), eval("parseFloat('3.14')"));
    }

    @Test
    public void testParseFloatExponent() {
        assertEquals(new BigDecimal("3.14e-2"), eval("parseFloat('3.14e-2')"));
    }

    @Test
    public void testParseFloatExponentLarge() {
        assertEquals(new BigDecimal("3.14"), eval("parseFloat('0.0314E+2')"));
    }

    @Test
    public void testParseFloatAndString() {
        assertEquals(new BigDecimal("3.14"), eval("parseFloat(\"3.14more non-digit characters\")"));
    }

    @Test
    public void testParseFloatNan() {
        assertEquals((Object) Double.NaN, eval("parseFloat('ABC')"));
    }

    @Test
    public void testParseFloatNoArgs() {
        assertEquals(null, eval("parseFloat()"));
    }
}
