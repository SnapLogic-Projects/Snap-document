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
import com.snaplogic.expression.Regex;
import com.snaplogic.expression.methods.Method;
import com.snaplogic.expression.methods.MethodUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implements the javascript string split method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/split
 * ?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FString%2Fsplit
 *
 * @author jinloes
 */
public enum Split implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        String str = (String) member;

        if (args.isEmpty()) {
            List<String> retval = new ArrayList<>();

            retval.add(str);
            return retval;
        }

        Object separator = args.get(0);
        int limit = Integer.MAX_VALUE;
        if (args.size() > 1) {
            limit = MethodUtils.getIndex(args.get(1));
        }
        Object result = stringSplit(str, separator, limit);
        return StringMethod.convertArrayToList(result);
    }

    private Object stringSplit(String str, Object separator, int limit) {
        if (separator instanceof Regex) {
            Regex regex = ((Regex) separator);

            return regex.split(str, limit);
        } else {
            String separatorString = ObjectType.toString(separator);

            if (separatorString.isEmpty()) {
                List<String> retval = new ArrayList<>();

                for (int lpc = 0; lpc < str.length(); lpc++) {
                    retval.add(String.valueOf(str.charAt(lpc)));
                }

                return retval;
            }

            List<String> splitList;
            if (limit == Integer.MAX_VALUE) {
                splitList = Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(str,
                        separatorString));
                if (splitList.isEmpty()) {
                    splitList = new ArrayList<>(Arrays.asList(str));
                }
            } else {
                splitList = Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(str,
                        separatorString, limit + 1));
                if (limit < splitList.size()) {
                    splitList = splitList.subList(0, limit);
                } else if (limit > 0 && splitList.isEmpty()) {
                    splitList = new ArrayList<>(Arrays.asList(str));
                }
            }
            return splitList;
        }
    }
}
