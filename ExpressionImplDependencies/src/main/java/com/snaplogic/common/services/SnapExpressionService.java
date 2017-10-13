package com.snaplogic.common.services;

import com.snaplogic.Document;
import com.snaplogic.common.expressions.DataValueHandler;
import com.snaplogic.common.expressions.Scope;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;

public interface SnapExpressionService {

    /**
     * Gets values for substitution in a SQL statement.
     *
     * @param document document
     * @param matcher  matcher
     *
     * @return list of values to be substituted in the sql statement
     */
    @SuppressWarnings("unused")
    // this is used by the select component
    @Deprecated
    List<Object> getSubstitutionValues(Document document, Matcher matcher);

    /**
     * Evaluates the expression and returns the result.
     *
     * @param expression expression
     * @param document   document
     *
     * @return expression result
     */
    @Deprecated
    Object evaluateExpression(String expression, Document document);


    /**
     * Evaluates the expression and returns the result.
     *
     * @param expression       expression
     * @param dataCopy         document data copy
     * @param originalDocument original document so it can be used for error reporting
     *
     * @return expression result value
     */
    Object evaluateExpression(String expression, Object dataCopy, Document originalDocument);

    /**
     * Evaluates an expression with the given data and environment data.
     *
     * @param expression       expression
     * @param dataCopy         data copy
     * @param originalDocument original document so that it can be used for error reporting
     * @param envData          environment data
     *
     * @return expression result value
     */
    Object evaluateExpression(String expression, Object dataCopy, Document originalDocument,
                              @Nullable Stack<Scope> scopes);

    /**
     * Evaluates an expression with the given data and environment data.
     *
     * @param expression         expression
     * @param dataCopy           data copy
     * @param originalDocument   original document so that it can be used for error reporting
     * @param envData            environment data
     * @param valueEscapeHandler as the escape handler
     *
     * @return expression result value
     */
    Object evaluateExpression(String expression, Object dataCopy, Document originalDocument,
                              @Nullable Stack<Scope> scopes, @Nullable DataValueHandler
                                      valueEscapeHandler);
}

