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
package com.snaplogic.util;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * A jackson module for deserializing our custom data types.
 *
 * @author tstack
 */
public class SnapTypeModule extends SimpleModule {
    public SnapTypeModule() {
        addDeserializer(Object.class, new SnapTypeDeserializer());
    }
}
