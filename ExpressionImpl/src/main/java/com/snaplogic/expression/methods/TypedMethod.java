package com.snaplogic.expression.methods;

import com.snaplogic.expression.ObjectType;

/**
 * Class Containing the method type and the method Object associated with that type for type
 * Used checking overlapping named methods such as 'concat' for String Method and Array Method.
 */
public class TypedMethod {
    public final ObjectType type;
    public final Method method;

    public TypedMethod(ObjectType type, Method method) {
        this.type = type;
        this.method = method;
    }
}
