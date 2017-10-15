/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2012 - 2013, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */
package com.snaplogic.common;

import com.snaplogic.common.jsonpath.JsonPath;
import com.snaplogic.snap.api.PropertyType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This represents internal snaplogic field type: boolean, number, string, date,
 * time, datetime or binary.
 *
 * @author Marco Massenzio (marco@snaplogic.com)
 */
public enum SnapType implements PropertyType {
    // TODO: - MK: COMPOSITE and TABLE does not work for document schema, those
    //             are specific to SnapProperties. We will need another Type enum.
    /**
     * A number is represented as a BigDecimal. This type must be used for all numbers that are
     * non-floating and exceed {@link Integer#MAX_VALUE} and {@link Integer#MIN_VALUE} or are
     * floating numbers.
     * Defining a property as #NUMBER requires to expect the returning value as a {@link
     * BigDecimal}
     */
    NUMBER("number", BigDecimal.class, Double.class, Float.class, Number.class),
    /**
     * An integer is represented as a BigInteger, however {@link Integer#MAX_VALUE} and {@link
     * Integer#MIN_VALUE} can not be exceeded
     */
    INTEGER("integer", BigInteger.class, Long.class, Integer.class),
    STRING("string", String.class),
    JSONPATH("string", "json-path", JsonPath.class),
    DATETIME("string", "date-time", DateTime.class),
    LOCALDATETIME("string", "local-date-time", LocalDateTime.class),
    BOOLEAN("boolean", Boolean.class),
    DATE("string", "date", LocalDate.class),
    TIME("string", "time", LocalTime.class),
    BINARY("string", "binary", Byte.class),
    TABLE("array", List.class),
    ANY("any", Object.class),
    COMPOSITE("object", Map.class),
    BYTES("string", "bytes", byte[].class);

    private static final Map<Pair<String, String>, SnapType> nameToTypeMap = new HashMap<>();
    private static final Map<String, SnapType> formatToTypeMap = new HashMap<>();
    private static final Map<Class, SnapType> classToTypeMap = new HashMap<>();

    static {
        for (SnapType type : SnapType.values()) {
            nameToTypeMap.put(Pair.of(type.name, type.format), type);
            if (type.format != null) {
                formatToTypeMap.put(type.format, type);
            }
            classToTypeMap.put(type.javaType, type);
            for (Class alias : type.aliases) {
                classToTypeMap.put(alias, type);
            }
        }
        // Add an alias for byte-arrays.
        nameToTypeMap.put(Pair.of("byte[]", StringUtils.EMPTY), BINARY);
    }

    private final String name;
    private final String format;
    private final Class<?> javaType;
    private final Class<?>[] aliases;

    private SnapType(String type, String format, Class<?> javaType, Class<?> ...aliases) {
        this.name = type;
        this.format = format;
        this.javaType = javaType;
        this.aliases = aliases;
    }

    private SnapType(String type, Class<?> javaType, Class<?> ...aliases) {
        this(type, StringUtils.EMPTY, javaType, aliases);
    }

    /**
     * Parse JSON representation of the data type into an enum type.
     *
     * <p>NOTE - please do not use {@link Enum#valueOf(Class, String)} because in
     * JSON we use literals that do not match the types' names: SnapString,
     * SnapNumber etc.
     *
     * <p>Example:
     *
     * <pre>
     * SnapFieldType.parse(&quot;string&quot;) == SnapFieldType.SnapString
     * </pre>
     *
     * but:
     * <pre>
     * SnapFieldType.valueOf("string") --> exception
     * </pre>
     *
     * @param type the literal representation of the type
     *
     * @return the corresponding enum object, or {@code null} if it does not match
     *         any of the known types
     */
    public static SnapType parse(String type, String format) {
        if (format == null) {
            format = StringUtils.EMPTY;
        }
        return nameToTypeMap.get(Pair.of(type, format));
    }

    public static SnapType parse(String typeOrFormat) {
        SnapType retval = formatToTypeMap.get(typeOrFormat);

        if (retval != null) {
            return retval;
        }

        return parse(typeOrFormat, StringUtils.EMPTY);
    }

    public static SnapType fromClass(Class cl) {
        if (cl.isEnum()) {
            return STRING;
        }
        return classToTypeMap.get(cl);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getJsonType() {
        return name;
    }

    @Override
    public String getJsonFormat() {
        return format;
    }

    /**
     * Return the Java Class that internally represents this Snap type
     *
     * @return the Class of the type represented by this enum
     */
    public Class<?> toJavaType() {
        return javaType;
    }
}
