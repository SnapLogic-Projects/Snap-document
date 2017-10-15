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

package com.snaplogic.expression.util;

import org.codehaus.janino.ExpressionEvaluator;
import org.codehaus.janino.Java;

import static org.junit.Assert.fail;

/**
 * JaninoUtilsTest is the unit test for {@link JaninoUtils}
 *
 * @author ksubramanian
 * @since 2015
 */
public class JaninoUtilsTest {
//    @Test
//    public void testEvaluate() throws Exception {
//        Java.Atom evaluate = evaluate(StringMethod.getMethod("toLowerCase"), string("SNAPLOGIC"),
//                Collections.<Java.Atom>emptyList());
//        assertEquals(ToLowerCase.INSTANCE.evaluate("SNAPLOGIC", Arrays.asList()), eval(evaluate));
//    }

//    @Test
//    public void testEvalStatic() throws Exception {
//        Java.Atom evaluate = evalStaticMethod(fieldAccessExpr(classAtom(LocalDate.class),
//                INSTANCE), "parse", Arrays.asList(string("2012-07-07")));
//        assertEquals(LocalDate.INSTANCE.evalStaticMethod(null, "parse",
//                        Arrays.<Object>asList("2012-07-07")),
//                eval(evaluate));
//    }

    //------------------------------------- Private methods ------------------------------------//
    private static final Object eval(Java.Atom atom) {
        try {
            ExpressionEvaluator evaluator = new ExpressionEvaluator(atom.toString(), Object.class,
                    new String[]{}, new Class[]{});
            return evaluator.evaluate(new Object[0]);
        } catch (Exception e) {
            fail(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
