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

package sl;

import com.snaplogic.expression.methods.map.Extend;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MapBuilder is the utility class that helps is building hash map with initial data. This is
 * similar to guava's immutable map builder. Difference is this one allows addition of null values.
 *
 * @author ksubramanian
 * @since 2015
 */
public class MapBuilder {

    private final Map hashMap;

    private MapBuilder() {
        this.hashMap = new LinkedHashMap();
    }

    /**
     * Returns a map builder.
     *
     * @return builder
     */
    public static MapBuilder builder() {
        return new MapBuilder();
    }

    /**
     * Adds an entry to the map.
     *
     * @param key
     * @param value
     * @return builder
     */
    public MapBuilder put(Object key, Object value) {
        this.hashMap.put(key, value);
        return this;
    }

    public MapBuilder putAll(Object in) {
        Map map = Extend.convertToMap(in);
        if (map != null) {
            hashMap.putAll(map);
        }
        return this;
    }

    /**
     * Returns the map that was built.
     *
     * @return map
     */
    public Map get() {
        return this.hashMap;
    }
}
