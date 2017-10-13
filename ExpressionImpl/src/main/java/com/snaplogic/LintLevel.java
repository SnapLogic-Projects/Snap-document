package com.snaplogic;

/**
 * Different levels for a lint.  At some point, we might want to allow users to define how the
 * system should react when a lint is reported.
 *
 * TODO Actually make use of this
 *
 * @author tstack
 */
public enum LintLevel {
    ALLOW,
    WARN,
    DENY
}
