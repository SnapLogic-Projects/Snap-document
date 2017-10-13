package com.snaplogic.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a static field in a class as a feature flag to be updated through a
 * system property of the same name (i.e {fully-qualified class name}.{field-name})
 *
 * @author tstack
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FeatureFlag {
    /**
     * A description of the feature flags purpose.
     *
     * @return
     */
    String description();

    /**
     * The default value for the flag
     *
     * @return
     */
    String defaultValue() default "";

    /**
     * The minimum value for a numeric flag.
     *
     * @return
     */
    double minValue() default Double.MIN_VALUE;

    /**
     * The maximum value for a numeric flag.
     *
     * @return
     */
    double maxValue() default Double.MAX_VALUE;

    /**
     * The minimum length for a string value.
     *
     * @return
     */
    int minLength() default 0;

    /**
     * The maximum length for a string value.
     *
     * @return
     */
    int maxLength() default Integer.MAX_VALUE;
}