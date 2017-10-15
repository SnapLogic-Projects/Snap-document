/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2016, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */
package com.snaplogic.expression.util;

import com.google.inject.Provider;
import com.snaplogic.api.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Arrays;

import static com.snaplogic.expression.util.Messages.LOADING_SCRIPT_ENGINE_PROPERTY_CHOSE_PREFERRED;
import static com.snaplogic.expression.util.Messages.UNABLE_TO_LOCATE_SCRIPT_ENGINE;

/**
 * Provider used to lazy initialize ScriptEngines.
 */
public class ScriptEngineProvider implements Provider<ScriptEngine> {
    public static final String COMMA = ",";
    private static final Logger log = LoggerFactory.getLogger(ScriptEngineProvider.class);

    // Script engine names
    public static final String PYTHON = "python";
    public static final String JAVASCRIPT = "JavaScript";
    public static final String RUBY = "ruby";
    public static final String JAVASCRIPT_ENGINE_ORDER = "nashorn,javascript";
    private static final String PROPERTY_FORMAT = "scriptengine.%s";

    private final ScriptEngineManager manager;
    private final String name;
    private final String defaultNames;

    /**
     * @param manager The ScriptEngineManager that has the engines to use.
     * @param name The name of the language.
     * @param defaultNames A comma-separated list of engine/language names to use for this
     *                     script language.
     */
    public ScriptEngineProvider(final ScriptEngineManager manager, final String name, final
        String defaultNames) {
        this.manager = manager;
        this.name = name;
        this.defaultNames = defaultNames;
    }

    @Override
    public ScriptEngine get() {
        String propName = String.format(PROPERTY_FORMAT, name);
        String propValue = System.getProperty(propName, defaultNames);
        String[] names = propValue.split(COMMA);
        for (String engineName : names) {
            ScriptEngine retval = manager.getEngineByName(engineName);
            if (retval != null) {
                log.info(LOADING_SCRIPT_ENGINE_PROPERTY_CHOSE_PREFERRED,
                        propName, engineName, propValue);
                return retval;
            }
        }
        throw new ExecutionException(UNABLE_TO_LOCATE_SCRIPT_ENGINE)
                .formatWith(Arrays.asList(names))
                .withResolutionAsDefect();
    }
}
