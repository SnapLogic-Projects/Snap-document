package com.snaplogic.jsonpath;

import com.snaplogic.common.jsonpath.JsonPath;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * A partial result of a walk over a document.  An instance of this class
 * will contain a final value and the parent path that led to that value.
 *
 * @author tstack
 */
public interface WalkResult {
    /**
     * @return A list of object/key pairs that represent the path traversed
     *   to reach a value.  The object is the parent and the key is the
     *   field-name or array-index used to find the next value in the path.
     */
    List<Pair<Object, Object>> getParentPath();

    /**
     * @return A JsonPath object that represents the actual path to this result.
     */
    JsonPath getActualPath();

    /**
     * @return elements at each level of hierarchy in the parent path of
     * WalkResult.
     */
    Map<Object, Object> getParentHierarchy();

    /**
     * @return The final value found at the end of a JSON-path traversal.
     */
    <T> T getValue();

    /**
     * @param value The new value for the object referred to by this path.
     * @return A reference to the root object.  If this result refers to the
     * document root (i.e. '$'), then the value passed in will be returned,
     * otherwise the original root will be returned.
     */
    <T> T setValue(Object value);
}
