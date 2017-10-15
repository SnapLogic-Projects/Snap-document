/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2012, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */
package com.snaplogic.snap.api;

/**
 * Interface for {@link ViewType and SnapType} to allow type definition for {@link BaseProperty}
 *
 * @author mklumpp
 */
public interface PropertyType {
    // TODO - MK: parse could be defined here and toString
    default String getJsonType() {
        return toString();
    }

    default String getJsonFormat() {
        return null;
    }
}
