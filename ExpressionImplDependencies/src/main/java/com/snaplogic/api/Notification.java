package com.snaplogic.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to identify and hold text for notifications that will be shown to the end user.  The
 * text in these annotations will eventually be extracted and used for localization.  The name of
 * the notification will be derived from the field name.  For example, if this annotation was
 * applied to a NotificationDefinition static field named MESSAGE in the class MyClass, the name
 * would be MyClass.MESSAGE.
 *
 * @author tstack
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Notification {
    /**
     * @return The main text of the notification.
     */
    String message();

    /**
     * @return If available, more detail on the reason for the notification.
     */
    String reason() default "";

    /**
     * @return If available, an explanation on how to resolve the issue reported by the
     * notification.
     */
    String resolution() default "";
}
