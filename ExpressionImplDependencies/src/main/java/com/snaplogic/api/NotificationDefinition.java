package com.snaplogic.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Generic container for storing notifications that will be displayed to the user.  These values
 * should be populated using the @Notification annotation.
 *
 * @author tstack
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class NotificationDefinition {
    protected String name;
    protected String message;
    protected String reason;
    protected String resolution;

    /**
     * @return The name of the notification.  This value is automatically derived from the static
     * field that contains the definition.
     */
    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getReason() {
        return reason;
    }

    public String getResolution() {
        return resolution;
    }
}

