package com.snaplogic.common.expressions;

import com.snaplogic.Document;
import com.snaplogic.common.services.SnapExpressionService;
import com.snaplogic.snap.api.SnapDataException;

import javax.annotation.Nullable;
import java.util.Stack;

/**
 * Allows to provide a data handler to intercept expression values before they are being
 * evaluated and to provide functionality for expression evaluation itself.
 *
 * @param <I> as the checked return type.
 *
 * @author mklumpp
 */
public interface DataValueHandler<I> {

    /**
     * Allows to modify the value and return it before its being evaluated.
     *
     * @param value     as the value needing pre-processing
     * @param fieldName as the name of the variables
     * @param isFromEnv indicates a param substitution
     *
     * @return as the modified value
     */
    Object handleValue(Object value, String fieldName, boolean isFromEnv);

    /**
     * Handles the evaluation of the value
     *
     * @param snapExpressionService the expression service
     * @param expression            the expression being evaluated
     * @param document              the document
     * @param data                  the document data
     * @param scopes                the scopes
     *
     * @return the result of the expression evaluation.
     */
    I handleEval(SnapExpressionService snapExpressionService, String expression,
                 Document document, Object data, @Nullable Stack<Scope> scopes);

    /**
     * Handles the exception when a path is not found in the provided data.
     *
     * @param originalDocument the original document
     * @param fieldName        the field name
     * @param fieldPath        the path of the field
     *
     * @throws SnapDataException
     */
    Object handleUndefinedReference(Document originalDocument, String fieldName,
                                    String fieldPath) throws SnapDataException;
}

