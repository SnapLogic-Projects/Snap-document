package com.snaplogic.expression.methods.object;

import com.snaplogic.api.Lint;
import com.snaplogic.api.Notification;
import com.snaplogic.api.Notifications;
import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.methods.Method;
import com.snaplogic.util.JsonPathUtil;

import java.util.List;
import java.util.Map;

/**
 * Implements javascript Object hasOwnProperty method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/
 * Object/hasOwnProperty
 *
 * @author jinloes
 */
public enum HasOwnProperty implements Method {
    INSTANCE;

    @Notification(message = "Passing a JSON-Path to hasOwnProperty() is " +
            "not supported and will fail in the future",
            resolution = "Use hasOwnProperty() for each component of the path")
    private static final Lint INVALID_VALUE = new Lint();

    @Notification(message = "Expecting hasOwnProperty() to return false for " +
            "a null value is a bug that will be fixed",
            resolution = "You must explicitly check for a null value")
    private static final Lint NULL_VALUE = new Lint();

    static {
        Notifications.register(HasOwnProperty.class);
    }

    public Object evaluate(final Object member, final List args) {
        Object propName = args.get(0);
        Object result = null;
        if (member instanceof Map && propName instanceof String) {
            String attribute = ObjectType.toString(propName);
            if (attribute.startsWith("$") || attribute.startsWith("[")) {
                INVALID_VALUE.report();
            } else if (((Map) member).containsKey(ObjectType.toString(propName))
                    && ((Map) member).get(propName) == null) {
                NULL_VALUE.report();
            }
            Map<?, ?> map = (Map) member;
            result = JsonPathUtil.checkedRead((String) propName, map);
        }
        return result != null;
    }
}
