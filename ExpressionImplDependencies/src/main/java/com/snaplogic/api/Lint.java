package com.snaplogic.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.snaplogic.LintLevel;
import com.snaplogic.LintReporter;

/**
 * A lint defines a non-fatal issue that the user should be notified of.  To define a Lint, a
 * static field should be created with an instance of this class, the field should be annotated
 * with @Notification, and the Notifications.register() method should be called on the class.  The
 * register method will take care of filling out the name of the notification based on the field
 * name in the class.
 *
 * @author tstack
 */
public class Lint extends NotificationDefinition {
    /**
     * A globally unique ID for this lint that LintReporters can use to quickly record whether this
     * lint has been encountered.
     */
    @JsonIgnore
    int ord = -1;
    @JsonIgnore
    private final LintLevel level;

    /**
     * @param level The default level for this lint.
     */
    public Lint(final LintLevel level) {
        this.level = level;
    }

    public Lint() {
        this(LintLevel.WARN);
    }

    @JsonIgnore
    public LintLevel getDefaultLevel() {
        return level;
    }

    @JsonIgnore
    public int ordinal() {
        return ord;
    }

    /**
     * Report a case of this lint to the user.
     */
    public void report() {
        LintReporter.LOCAL_HOLDER.report(this);
    }
}
