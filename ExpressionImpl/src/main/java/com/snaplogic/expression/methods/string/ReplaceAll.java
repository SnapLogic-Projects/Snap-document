/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2017, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression.methods.string;

import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.methods.Method;
import com.snaplogic.snap.api.SnapDataException;

import java.util.List;
import java.util.regex.Pattern;

import static com.snaplogic.expression.methods.string.Messages.*;

/**
 * Implements the string replaceAll method without any regex
 */
public enum ReplaceAll implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        String input = (String) member;

        if (args.size() != 2) {
            throw new SnapDataException(INVALID_ARGUMENTS_NUMBER_FOR_METHOD)
                    .formatWith(this.getClass().getSimpleName(), args.size())
                    .withReason(String.format(ARGUMENTS_NUMBER_IS_NOT_APPROPRIATE, 2))
                    .withResolution(REPLACE_ALL_RESOLUTION);
        }

        String searchString = ObjectType.toString(args.get(0));
        String replaceText = ObjectType.toString(args.get(1));
        return input.replaceAll(Pattern.quote(searchString), replaceText);
    }
}
