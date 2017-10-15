/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2013, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression;

import com.google.common.collect.ImmutableSet;
import com.snaplogic.api.FeatureFlag;
import com.snaplogic.common.expressions.Scope;
import com.snaplogic.expression.methods.JavascriptFunction;
import com.snaplogic.expression.methods.Method;
import com.snaplogic.expression.methods.UnknownMethodException;
import com.snaplogic.expression.methods.string.StringMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.SimpleBindings;

/**
 * Wraps a javascript regex.
 *
 * @author jinloes
 */
public class Regex {
    @FeatureFlag(description = "Use the JVM's JavaScript engine to evaluate regular expressions")
    private static volatile boolean USE_JAVASCRIPT = false;

    private static final Logger LOG = LoggerFactory.getLogger(Regex.class);
    private static final CompiledScript COMPILED_MATCH_SCRIPT = StringMethod.compileJavaScript(
            "val.match(new RegExp(pattern, flags))");
    private static final CompiledScript COMPILED_REPLACE_SCRIPT = StringMethod.compileJavaScript(
            "val.replace(new RegExp(pattern, flags), sub)");
    private static final CompiledScript COMPILED_SPLIT_SCRIPT = StringMethod.compileJavaScript(
            "val.split(new RegExp(pattern, flags), limit)");
    private static final CompiledScript COMPILED_SEARCH_SCRIPT = StringMethod.compileJavaScript(
            "val.search(new RegExp(pattern, flags))");

    private final Bindings bindings;
    private final String pattern;
    private final String flags;
    private final boolean isGlobal;
    private Pattern compiled;

    public Regex(Bindings bindings, final String pattern, final String flags) {
        this.bindings = bindings;
        this.pattern = pattern;
        this.flags = flags;


        int patternFlags = 0;
        if (flags.contains("i")) {
            patternFlags |= Pattern.CASE_INSENSITIVE;
        }
        if (flags.contains("m")) {
            patternFlags |= Pattern.MULTILINE;
        }
        isGlobal = flags.contains("g");

        try {
            this.compiled = Pattern.compile(pattern, patternFlags);
        } catch (PatternSyntaxException e) {
            LOG.error("Unable to compile regular expression: {}", pattern, e);
            this.compiled = null;
        }
    }

    public Bindings getBindings() {
        return bindings;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    @Override
    public int hashCode() {
        return bindings.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Regex) {
            Regex other = (Regex) obj;

            return this.bindings.equals(other.bindings);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(bindings);
    }

    private Bindings getArgs(String val) {
        Bindings args = new SimpleBindings();
        args.put("val", val);
        if (bindings != null) {
            args.putAll(bindings);
        } else {
            args.put("pattern", pattern);
            args.put("flags", flags);
        }

        return args;
    }

    /**
     * This method implements the replacement patterns as described in the JavaScript
     * String.replace() method.
     *
     *   $$ - Inserts a '$'
     *   $& - Inserts the matched substring.
     *   $` - Inserts the portion of the string that precedes the matched substring.
     *   $' - Inserts the portion of the string that follows the matched substring.
     *   $n - Where n is a non-negative integer less than 100, inserts the n-th parenthesized
     *        submatch string.
     *
     * @param input The input string.
     * @param matcher The matched part of the string
     * @param sb The replacement string that is being built.
     * @param replacement The replacement string given by the user.
     */
    private void appendReplacement(String input, Matcher matcher, StringBuilder sb, String
            replacement) {
        for (int index = 0; index < replacement.length(); index++) {
            char ch = replacement.charAt(index);

            switch (ch) {
                // Check for a directive
                case '$': {
                    char r1 = 0, r2 = 0;
                    boolean isGroup = false;

                    if ((index + 1) < replacement.length()) {
                        r1 = replacement.charAt(index + 1);
                        if (Character.isDigit(r1)) {
                            // If it's a digit, then we're inserting a capture
                            isGroup = true;
                        }
                    }
                    // Check for a second digit for the capture group.
                    if (isGroup && (index + 2) < replacement.length() &&
                            Character.isDigit(replacement.charAt(index + 2))) {
                        r2 = replacement.charAt(index + 2);
                    }

                    if (isGroup) {
                        int groupNumber = r1 - '0';

                        if (r2 != 0 && (groupNumber * 10 <= matcher.groupCount())) {
                            groupNumber *= 10;
                            groupNumber += r2 - '0';
                            index += 1;
                        }

                        if (groupNumber <= matcher.groupCount()) {
                            // Insert the capture from the match.
                            sb.append(matcher.group(groupNumber));
                        } else {
                            // If the group number is out-of-range, then just insert the original
                            // replacement text and don't treat it specially.
                            sb.append(ch);
                            sb.append(r1);
                        }
                        index += 1;
                    } else {
                        // Insert one of the other directives.
                        switch (r1) {
                            case '$':
                                sb.append(r1);
                                break;
                            case '&':
                                sb.append(matcher.group());
                                break;
                            case '`':
                                sb.append(input.substring(matcher.regionStart(), matcher.start()));
                                break;
                            case '\'':
                                sb.append(input.substring(matcher.end()));
                                break;
                            default:
                                sb.append(ch);
                                sb.append(r1);
                                break;
                        }
                        index += 1;
                    }

                    break;
                }
                default:
                    sb.append(ch);
                    break;
            }
        }
    }

    public Object replace(String src, String replace) {
        if (this.compiled == null || USE_JAVASCRIPT) {
            Bindings args = getArgs(src);
            args.put("sub", replace);

            return StringMethod.evaluateJavaScript(COMPILED_REPLACE_SCRIPT, args);
        } else {
            Matcher matcher = compiled.matcher(src);
            StringBuilder retval = new StringBuilder();
            int start = 0;

            do {
                if (matcher.find(start)) {
                    retval.append(src.substring(start, matcher.start()));
                    appendReplacement(src, matcher, retval, replace);
                    start = matcher.end();
                } else {
                    break;
                }
            } while (isGlobal);

            retval.append(src.substring(start));

            return retval.toString();
        }
    }

    public String replace(String src, JavascriptFunction replacer) {
        Matcher matcher = compiled.matcher(src);
        StringBuilder retval = new StringBuilder();
        int start = 0;

        do {
            if (matcher.find(start)) {
                retval.append(src.substring(start, matcher.start()));
                List<Object> args = new ArrayList<>();

                for (int lpc = 0; lpc <= matcher.groupCount(); lpc++) {
                    args.add(matcher.group(lpc));
                }
                args.add(matcher.start());
                args.add(src);
                Object result = replacer.eval(args);
                retval.append(ObjectType.toString(result));
                start = matcher.end();
            } else {
                break;
            }
        } while (isGlobal);

        retval.append(src.substring(start));

        return retval.toString();
    }

    public Object match(String src) {
        if (this.compiled == null || USE_JAVASCRIPT) {
            Object result = StringMethod.evaluateJavaScript(COMPILED_MATCH_SCRIPT, getArgs(src));
            return StringMethod.convertArrayToList(result);
        } else {
            Matcher matcher = compiled.matcher(src);
            RegExpResult retval = null;
            int start = 0;

            do {
                if (matcher.find(start)) {
                    if (retval == null) {
                        retval = new RegExpResult(src, matcher.start(), matcher.end());
                    }
                    for (int lpc = 0; lpc <= matcher.groupCount(); lpc++) {
                        retval.add(matcher.group(lpc));
                        if (isGlobal) {
                            break;
                        }
                    }

                    start = matcher.end();
                } else {
                    break;
                }
            } while (isGlobal);

            return retval;
        }
    }

    public Object split(String src, int limit) {
        if (this.compiled == null || USE_JAVASCRIPT) {
            Bindings args = getArgs(src);
            args.put("limit", limit);

            return StringMethod.evaluateJavaScript(COMPILED_SPLIT_SCRIPT, args);
        } else {
            Matcher matcher = compiled.matcher(src);
            List<String> retval = new ArrayList<>();
            int start = 0, splitCount = 0;
            boolean emptyMatch = false;

            while ((splitCount < limit) && start < src.length() && matcher.find(start)) {
                int matchSize = matcher.end() - matcher.start();

                if (matchSize > 0) {
                    retval.add(src.substring(start, matcher.start()));
                } else {
                    emptyMatch = true;
                    retval.add(src.substring(start, matcher.start() + 1));
                }
                for (int lpc = 1; lpc <= matcher.groupCount(); lpc++) {
                    retval.add(matcher.group(lpc));
                }

                if (limit >= 0) {
                    splitCount += 1;
                }

                if (matchSize > 0) {
                    start = matcher.end();
                } else {
                    start = matcher.end() + 1;
                }
            }

            if (splitCount < limit && (!emptyMatch && start <= src.length())) {
                retval.add(src.substring(start));
            }

            return retval;
        }
    }

    public int search(String src) {
        if (this.compiled == null || USE_JAVASCRIPT) {
            Number result = (Integer) StringMethod.evaluateJavaScript(COMPILED_SEARCH_SCRIPT,
                    getArgs(src));
            return result.intValue();
        } else {
            Matcher matcher = compiled.matcher(src);

            if (matcher.find()) {
                return matcher.start();
            }

            return -1;
        }
    }

    private static class RegExpResult extends ArrayList<String> implements Scope {
        private static final ImmutableSet<String> KEY_SET = ImmutableSet.of("input", "index",
                "lastIndex");
        private final String input;
        private final int index;
        private final int lastIndex;

        public RegExpResult(String input, int index, int lastIndex) {
            this.input = input;
            this.index = index;
            this.lastIndex = lastIndex;
        }

        @Override
        public Object get(final String name) {
            switch (name) {
                case "input":
                    return input;
                case "index":
                    return index;
                case "lastIndex":
                    return lastIndex;
                default:
                    return UNDEFINED;
            }
        }

        @Override
        public Set<String> keySet() {
            return KEY_SET;
        }
    }

    public static Method getMethod(String functionName) {
        Set<String> noMethod = new TreeSet<>();
        noMethod.add("No Regex Methods");
        throw new UnknownMethodException(noMethod);
    }
}
