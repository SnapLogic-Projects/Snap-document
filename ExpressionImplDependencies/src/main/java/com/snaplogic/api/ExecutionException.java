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

package com.snaplogic.api;

/**
 * This exception should be used by the snap author to notify the platform about any exception that
 * happens during the snap execution.
 *
 * @author ksubramanian
 */
public class ExecutionException extends SnapException {

    public ExecutionException(String message) {
        super(message);
    }

    public ExecutionException(String message, String errorMessageForUser) {
        super(message, errorMessageForUser);
    }

    public ExecutionException(Throwable cause, String message) {
        super(cause, message);
    }

    public ExecutionException(Throwable cause, String message, String... params) {
        super(cause, message, params);
    }
}
