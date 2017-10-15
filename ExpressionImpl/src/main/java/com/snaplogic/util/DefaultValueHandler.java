package com.snaplogic.util;

import com.google.inject.Singleton;
import com.snaplogic.Document;
import com.snaplogic.api.ExecutionException;
import com.snaplogic.common.expressions.DataValueHandler;
import com.snaplogic.common.expressions.Scope;
import com.snaplogic.common.services.SnapExpressionService;
import com.snaplogic.snap.api.SnapDataException;

import java.io.Serializable;
import java.util.Stack;

import static com.snaplogic.util.Messages.*;

/**
 * The default handler for expressions
 *
 * @author mklumpp
 */
@Singleton
public class DefaultValueHandler implements DataValueHandler<Object>, Serializable {

    @Override
    public Object handleValue(final Object value, String fieldName, boolean isFromEnv) {
        return value;
    }

    public Object handleEval(SnapExpressionService snapExpressionService, String expression,
                             Document document, Object data, Stack<Scope> scopes) {
        try {
            if (expression == null) {
                throw new ExecutionException(NO_EXPRESSION_DEFINED)
                        .withReason(NO_EXPRESSION_DEFINED_REASON)
                        .withResolution(NO_EXPRESSION_DEFINED_RESOLUTION);
            }
            return snapExpressionService.evaluateExpression(expression, data, document,
                    scopes, this);
        } catch (ExecutionException | SnapDataException e) {
            throw e;
        } catch (ClassCastException e) {
            throw new ExecutionException(e, EXPRESSION_EVAL_MUST_RETURN_STRING)
                    .withReason(EXPRESSION_EVAL_MUST_RETURN_STRING_REASON)
                    .withResolution(EXPRESSION_EVAL_MUST_RETURN_STRING_RESOLUTION);
        } catch (Throwable e) {
            throw new ExecutionException(e, EXPRESSION_EVAL_ERROR)
                    .withResolutionAsDefect();
        }
    }

    @Override
    public Object handleUndefinedReference(final Document originalDocument, final String fieldName,
                                           final String fieldPath) throws SnapDataException {
        return handleDefaultUndefinedReference(originalDocument, fieldName, fieldPath);
    }

    public static Object handleDefaultUndefinedReference(final Document originalDocument,
                                                         final String fieldName, final String fieldPath) throws SnapDataException {
        throw new SnapDataException(UNDEFINED_REFERENCE)
                .formatWith(fieldPath)
                .withReason(REFERENCE_NOT_FOUND, fieldName)
                .withResolution(PLEASE_CHECK_YOUR_EXPRESSION);
    }
}
