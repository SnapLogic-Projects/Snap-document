/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2014, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression.util;

import com.snaplogic.api.ExecutionException;
import com.snaplogic.api.Lint;
import com.snaplogic.api.Notification;
import com.snaplogic.api.Notifications;
import com.snaplogic.expression.HexUnescaper;
import com.snaplogic.expression.Regex;
import org.apache.commons.lang3.text.translate.*;

import javax.script.*;

import static com.snaplogic.expression.util.Messages.*;

/**
 * Utility functions for dealing with literals in the expression language.
 *
 * @author tstack
 */
public class LiteralUtils {
    private static final ScriptEngine SCRIPT_ENGINE;
    @Notification(
            message = "Unsupported string literal",
            reason = "Multi-line string constants are not supported",
            resolution = "Add a backslash at the end of the line to continue " +
                    "a string literal onto the next line")
    private static final Lint MULTI_LINE_STRING = new Lint();

    public static final boolean CHECK_FOR_MULTI_LINE = false;

    static {
        Notifications.register(LiteralUtils.class);
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngineProvider scriptEngineProvider = new ScriptEngineProvider(scriptEngineManager,
                ScriptEngineProvider.JAVASCRIPT, ScriptEngineProvider.JAVASCRIPT_ENGINE_ORDER);

        SCRIPT_ENGINE = scriptEngineProvider.get();
    }
    
    /**
     * The JavaScript unescaper in commons-lang does not seem to handle hex values,
     * so we have to make a custom unescaper.
     */
    private static final CharSequenceTranslator UNESCAPE_JAVASCRIPT =
            new AggregateTranslator(
                    new OctalUnescaper(),
                    new HexUnescaper(),
                    new UnicodeUnescaper(),
                    new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_UNESCAPE()),
                    new LookupTranslator(
                            new String[][] {
                                    {"\\\\", "\\"},
                                    {"\\\"", "\""},
                                    {"\\'", "'"},
                                    {"\\", ""}
                            })
                    );
    public static final String PATTERN = "pattern";
    public static final String FLAGS = "flags";
    private static final String NEW_REGEXP_EXPR = "new RegExp(pattern, flags)";

    /**
     * @param text The string literal to unescape.
     * @return The string literal value.
     */
    public static String unescapeString(String text) {
        //removes all newlines from strings with backslashes before a newline character
        text = text.replaceAll("\\\\\n", "");
        if (CHECK_FOR_MULTI_LINE && text.contains("\n")) {
            MULTI_LINE_STRING.report();
        }
        // Remove wrapping ' or " from string
        final int unquotedStart = 1;
        final int unquotedEnd = text.length() - 1;
        final String unquotedText = text.substring(unquotedStart, unquotedEnd);
        try {
            String subText = UNESCAPE_JAVASCRIPT.translate(unquotedText);
            return subText;
        } catch (IllegalArgumentException e) {
            throw new ExecutionException(INVALID_STRING_LITERAL)
                    .formatWith(unquotedText)
                    .withReason(String.format(INVALID_STRING_LITERAL, unquotedText))
                    .withResolution(PLEASE_CHECK_EXPRESSION_SYNTAX);
        }
    }

    /**
     * @param pattern The regex pattern to wrap in a RegExp object.
     * @param flags The flags for the RegExp object.
     * @return A
     */
    public static Regex compileRegex(String pattern, String flags) {
        Bindings bindings = new SimpleBindings();
        bindings.put(PATTERN, pattern);
        bindings.put(FLAGS, flags);
        try {
            SCRIPT_ENGINE.eval(NEW_REGEXP_EXPR, bindings);
            return new Regex(bindings, pattern, flags);
        } catch (ScriptException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new ExecutionException(cause, INVALID_REGULAR_EXPRESSION)
                    .formatWith(pattern, flags)
                    .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
        }
    }
}
