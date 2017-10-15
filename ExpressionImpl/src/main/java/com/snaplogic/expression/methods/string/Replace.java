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
import com.snaplogic.expression.methods.JavascriptFunction;
import com.snaplogic.expression.methods.Method;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Implements the javascript string replace method:
 * http://msdn.microsoft.com/en-us/library/ie/t0kbytzc(v=vs.94).aspx
 *
 * @author jinloes
 */
public enum Replace implements Method {
    INSTANCE;

    @Override public Object evaluate(final Object member, final List args) {
        String input = (String) member;

        if (args.isEmpty()) {
            return input;
        }

        Object pattern = args.get(0);
        Object replacement = args.size() >= 2 ? args.get(1) : null;
        if (replacement instanceof JavascriptFunction) {
            Regex regex;

            if (pattern instanceof Regex) {
                regex = ((Regex) pattern);
            } else {
                regex = new Regex(null, Pattern.quote(ObjectType.toString(pattern)), org.apache
                        .commons.lang3.StringUtils.EMPTY);
            }
            return regex.replace(input, ((JavascriptFunction) replacement));
        } else {
            String replaceText = ObjectType.toString(replacement);
            if (pattern instanceof Regex) {
                return ((Regex) pattern).replace(input, replaceText);
            } else {
                return StringUtils.replaceOnce(input, String.valueOf(pattern), replaceText);
            }
        }
    }
}
