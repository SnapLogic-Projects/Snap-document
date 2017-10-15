/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2014, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression.methods.string;

import com.snaplogic.expression.methods.Method;
import com.snaplogic.snap.api.SnapDataException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.util.Collection;
import java.util.IllegalFormatException;
import java.util.List;

import static com.snaplogic.expression.methods.string.Messages.INVALID_FORMAT_FOR_ARGUMENTS;
import static com.snaplogic.expression.methods.string.Messages.MAKE_SURE_THE_ARGS_ARE_COMPATIBLE;

/**
 * Implementation of the sprintf extension method.
 */
public enum Sprintf implements Method {
    INSTANCE;

    /**
     * The Java string formatting functionality will only format Date or Calendar
     * objects, so we have to convert the joda versions before apply the format.
     */
    private static Transformer DATE_TRANSFORMER = new Transformer() {
        @Override
        public Object transform(Object o) {
            if (o instanceof DateTime) {
                DateTime dateTime = (DateTime) o;
                o = dateTime.toGregorianCalendar();
            } else if (o instanceof LocalDate) {
                LocalDate localDate = (LocalDate) o;
                o = localDate.toDate();
            } else if (o instanceof LocalDateTime) {
                LocalDateTime localDate = (LocalDateTime) o;
                o = localDate.toDate();
            } else if (o instanceof LocalTime) {
                LocalTime localTime = (LocalTime) o;
                o = localTime.toDateTimeToday().toGregorianCalendar();
            }
            return o;
        }
    };

    @Override public Object evaluate(final Object member, final List args) {
        String str = (String) member;
        try {
            // A note for readers, CollectionUtils.transformedCollection() does not transform
            // the elements that are already in the collection, it only changes elements that
            // are added after the fact.
            Collection transformedArgs = CollectionUtils.collect(args, DATE_TRANSFORMER);
            return String.format(str, transformedArgs.toArray());
        } catch (IllegalFormatException e) {
            throw new SnapDataException(e, INVALID_FORMAT_FOR_ARGUMENTS)
                    .formatWith(member, args)
                    .withReason(e.getMessage())
                    .withResolution(MAKE_SURE_THE_ARGS_ARE_COMPATIBLE);
        }
    }
}
