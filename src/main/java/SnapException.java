import com.google.common.base.Throwables;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.regex.Pattern;

/**
 * This is the root exception for the jsdk module.
 *
 * <p> All the other exceptions in jsdk module should derive from this exception. This exception
 * should not be used directly by the snap author. Snap author should use
 * {@link com.snaplogic.api.ConfigurationException} or
 * {@link com.snaplogic.api.ExecutionException}
 */
public class SnapException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String message;
    private String reason;
    private String resolution;

    private static Pattern STACK_TRACE_PATTERN = Pattern.compile("\r?\n[ \t]*at[ \t]+");

    public SnapException(String message) {
        super();
        this.message = stripStackTrace(message);
    }

    public SnapException(String message, String reason) {
        this(message);
        this.reason = stripStackTrace(reason);
    }

    public SnapException(Throwable cause, String message) {
        this(message);
        super.initCause(cause);
    }

    public SnapException(Throwable cause, String message, String... params) {
        this(cause, stringFormat(message, params));
        this.reason = this.getMessage();
    }

    /**
     * Applies the given parameter values to the message template string.
     *
     * @param params
     * @return this
     */
    public <T extends SnapException> T formatWith(Object... params) {
        String message = getMessage();
        this.message = stripStackTrace(stringFormat(message, params));
        return (T) this;
    }

    @Override
    public final String getMessage() {
        return this.message;
    }

    @Override
    public final String getLocalizedMessage() {
        return this.message;
    }

    /**
     * Sets the exception message for the user.
     *
     * @param reason
     * @return this
     */
    public final <T extends SnapException> T  withReason(final String reason) {
        this.reason = stripStackTrace(reason);
        return (T) this;
    }

    public final <T extends SnapException> T  withReason(final String reason, Object... params) {
        return withReason(String.format(reason, params));
    }

    /**
     * Sets the resolution for fixing this exception.
     *
     * @param resolution
     * @return this
     */
    public final <T extends SnapException> T withResolution(String resolution) {
        this.resolution = stripStackTrace(resolution);
        return (T) this;
    }

    public final <T extends SnapException> T withResolution(String resolution, Object... params) {
        return withResolution(String.format(resolution, params));
    }

    public final <T extends SnapException> T  withResolutionAsDefect() {
        this.resolution = Messages.PLEASE_FILE_A_DEFECT_AGAINST_SNAP;
        return (T) this;
    }

    /**
     * Adds a suppressed causal exception. Used when the causal exception is too verbose and
     * should not be included in exception message.
     *
     * @param exception the suppressed exception to add
     * @return this
     */
    public final <T extends SnapException> T  withSuppressed(Throwable exception) {
        addSuppressed(exception);
        return (T) this;
    }

    /**
     * Returns the error message that is specified provided for the user.
     *
     * @return userErrorMessage
     */
    public final String getReason() {
        Throwable cause = getCause();
        if (reason == null && cause != null) {
            return Throwables.getRootCause(cause).getMessage();
        }
        return reason;
    }

    /**
     * Returns the resolution for this exception set by the user.
     *
     * @return resolution
     */
    public final String getResolution() {
        return resolution;
    }
    //------------------------ Private Methods -------------------------------------
    /**
     * A string format function that doesn't throw an exception.
     * It's the last thing we want while throwing the exception,
     * that creating the exception object fails because of incorrectly
     * specified format string, or string passed in as a parameter
     * to have illegal characters that the formatter cannot handle.
     *
     * @return Formatted string
     */
    private static String stringFormat(String template, Object...params) {
        // TODO: move this into a common class
        try {
            if (template == null && (params == null || params.length == 0)) {
                return "null";
            } else {
                return String.format(template, params);
            }
        } catch (IllegalFormatException e) {
            return String.valueOf(template) + " " + Arrays.deepToString(params);
        } catch (NullPointerException e) {
            return String.valueOf(template) + " " + Arrays.deepToString(params);
        }
    }

    /**
     * Strip out stack trace information from input string. If input has anything
     * which looks like a java stack trace "\n at a.b.c.java", then strip it out.
     *
     * @param input the input message to strip
     * @return the stripped input
     */
    public static String stripStackTrace(String input) {
        if (StringUtils.isEmpty(input)) {
            return input;
        }

        String[] messages = STACK_TRACE_PATTERN.split(input, 2);
        if (messages.length > 0) {
            return messages[0];
        } else {
            return input;
        }
    }
}