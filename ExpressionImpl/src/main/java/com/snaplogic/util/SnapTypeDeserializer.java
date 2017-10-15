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

package com.snaplogic.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Customized deserialization that interprets our custom types.
 *
 * @author mklumpp
 */
public class SnapTypeDeserializer extends UntypedObjectDeserializer {

    @Override
    protected Object mapObject(JsonParser jp, DeserializationContext context) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
        }
        // 1.6: minor optimization; let's handle 1 and 2 entry cases separately
        if (t != JsonToken.FIELD_NAME) { // and empty one too
            // empty map might work; but caller may want to modify... so better just give small
            // modifiable
            return new LinkedHashMap<>(4);
        }
        String field1 = jp.getText();
        jp.nextToken();
        Object value1 = deserialize(jp, context);
        if (jp.nextToken() != JsonToken.FIELD_NAME) { // single entry; but we want modifiable
            Object value = JsonSnapTypes.castToSnapType(field1, value1);
            if (value != null) {
                return value;
            }
            Map result = new LinkedHashMap<>(4);
            result.put(field1, value1);
            return result;
        }
        // And then the general case; default map size is 16
        Map result = new LinkedHashMap<>();
        result.put(field1, value1);
        do {
            String fieldName = jp.getText();
            jp.nextToken();
            Object value = deserialize(jp, context);
            result.put(fieldName, value);
        } while (jp.nextToken() != JsonToken.END_OBJECT);
        return result;
    }
}