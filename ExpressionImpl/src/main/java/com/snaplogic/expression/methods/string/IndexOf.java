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

package com.snaplogic.expression.methods.string;

import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.methods.Method;
import com.snaplogic.expression.methods.MethodUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implements javascript string method indexOf:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/indexOf
 * ?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FString%2FindexOf
 *
 * @author jinloes
 */
public enum IndexOf implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {

        String memberVal = (String) member;
        if (args.size() < 1){
            return BigDecimal.ONE.negate();
        } else if (args.size() == 1) {
            return new BigDecimal(StringUtils.indexOf(memberVal,
                    ObjectType.toString(args.get(0))));
        } else {
            return new BigDecimal(StringUtils.indexOf(memberVal,
                    ObjectType.toString(args.get(0)),
                    MethodUtils.getArgAsNumber(args, 1).intValue()));
        }
    }
}
