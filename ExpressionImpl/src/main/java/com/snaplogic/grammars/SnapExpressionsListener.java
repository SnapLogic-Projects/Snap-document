package com.snaplogic.grammars;// Generated from /Users/yliu/IdeaProjects/Snap-document/ExpressionImpl/src/main/antlr4/com/snaplogic/grammars/SnapExpressions.g4 by ANTLR 4.7

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SnapExpressionsParser}.
 */
public interface SnapExpressionsListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#eval}.
	 * @param ctx the parse tree
	 */
	void enterEval(SnapExpressionsParser.EvalContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#eval}.
	 * @param ctx the parse tree
	 */
	void exitEval(SnapExpressionsParser.EvalContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExp(SnapExpressionsParser.ExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExp(SnapExpressionsParser.ExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#logicalOr}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOr(SnapExpressionsParser.LogicalOrContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#logicalOr}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOr(SnapExpressionsParser.LogicalOrContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#logicalAnd}.
	 * @param ctx the parse tree
	 */
	void enterLogicalAnd(SnapExpressionsParser.LogicalAndContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#logicalAnd}.
	 * @param ctx the parse tree
	 */
	void exitLogicalAnd(SnapExpressionsParser.LogicalAndContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#logicalXor}.
	 * @param ctx the parse tree
	 */
	void enterLogicalXor(SnapExpressionsParser.LogicalXorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#logicalXor}.
	 * @param ctx the parse tree
	 */
	void exitLogicalXor(SnapExpressionsParser.LogicalXorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#inOperator}.
	 * @param ctx the parse tree
	 */
	void enterInOperator(SnapExpressionsParser.InOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#inOperator}.
	 * @param ctx the parse tree
	 */
	void exitInOperator(SnapExpressionsParser.InOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#equality}.
	 * @param ctx the parse tree
	 */
	void enterEquality(SnapExpressionsParser.EqualityContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#equality}.
	 * @param ctx the parse tree
	 */
	void exitEquality(SnapExpressionsParser.EqualityContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#relational}.
	 * @param ctx the parse tree
	 */
	void enterRelational(SnapExpressionsParser.RelationalContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#relational}.
	 * @param ctx the parse tree
	 */
	void exitRelational(SnapExpressionsParser.RelationalContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#addition}.
	 * @param ctx the parse tree
	 */
	void enterAddition(SnapExpressionsParser.AdditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#addition}.
	 * @param ctx the parse tree
	 */
	void exitAddition(SnapExpressionsParser.AdditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#multiplication}.
	 * @param ctx the parse tree
	 */
	void enterMultiplication(SnapExpressionsParser.MultiplicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#multiplication}.
	 * @param ctx the parse tree
	 */
	void exitMultiplication(SnapExpressionsParser.MultiplicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#unary}.
	 * @param ctx the parse tree
	 */
	void enterUnary(SnapExpressionsParser.UnaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#unary}.
	 * @param ctx the parse tree
	 */
	void exitUnary(SnapExpressionsParser.UnaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#atomicExp}.
	 * @param ctx the parse tree
	 */
	void enterAtomicExp(SnapExpressionsParser.AtomicExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#atomicExp}.
	 * @param ctx the parse tree
	 */
	void exitAtomicExp(SnapExpressionsParser.AtomicExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#property}.
	 * @param ctx the parse tree
	 */
	void enterProperty(SnapExpressionsParser.PropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#property}.
	 * @param ctx the parse tree
	 */
	void exitProperty(SnapExpressionsParser.PropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(SnapExpressionsParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(SnapExpressionsParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#parens}.
	 * @param ctx the parse tree
	 */
	void enterParens(SnapExpressionsParser.ParensContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#parens}.
	 * @param ctx the parse tree
	 */
	void exitParens(SnapExpressionsParser.ParensContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#parensCall}.
	 * @param ctx the parse tree
	 */
	void enterParensCall(SnapExpressionsParser.ParensCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#parensCall}.
	 * @param ctx the parse tree
	 */
	void exitParensCall(SnapExpressionsParser.ParensCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#memberSuffix}.
	 * @param ctx the parse tree
	 */
	void enterMemberSuffix(SnapExpressionsParser.MemberSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#memberSuffix}.
	 * @param ctx the parse tree
	 */
	void exitMemberSuffix(SnapExpressionsParser.MemberSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#indexSuffix}.
	 * @param ctx the parse tree
	 */
	void enterIndexSuffix(SnapExpressionsParser.IndexSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#indexSuffix}.
	 * @param ctx the parse tree
	 */
	void exitIndexSuffix(SnapExpressionsParser.IndexSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#propertyRef}.
	 * @param ctx the parse tree
	 */
	void enterPropertyRef(SnapExpressionsParser.PropertyRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#propertyRef}.
	 * @param ctx the parse tree
	 */
	void exitPropertyRef(SnapExpressionsParser.PropertyRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#method}.
	 * @param ctx the parse tree
	 */
	void enterMethod(SnapExpressionsParser.MethodContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#method}.
	 * @param ctx the parse tree
	 */
	void exitMethod(SnapExpressionsParser.MethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#expOrSpread}.
	 * @param ctx the parse tree
	 */
	void enterExpOrSpread(SnapExpressionsParser.ExpOrSpreadContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#expOrSpread}.
	 * @param ctx the parse tree
	 */
	void exitExpOrSpread(SnapExpressionsParser.ExpOrSpreadContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(SnapExpressionsParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(SnapExpressionsParser.ArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Arr}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void enterArr(SnapExpressionsParser.ArrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Arr}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void exitArr(SnapExpressionsParser.ArrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ObjectLiteral}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void enterObjectLiteral(SnapExpressionsParser.ObjectLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ObjectLiteral}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void exitObjectLiteral(SnapExpressionsParser.ObjectLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code True}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void enterTrue(SnapExpressionsParser.TrueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code True}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void exitTrue(SnapExpressionsParser.TrueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code False}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void enterFalse(SnapExpressionsParser.FalseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code False}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void exitFalse(SnapExpressionsParser.FalseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Null}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void enterNull(SnapExpressionsParser.NullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Null}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void exitNull(SnapExpressionsParser.NullContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NaN}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void enterNaN(SnapExpressionsParser.NaNContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NaN}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void exitNaN(SnapExpressionsParser.NaNContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Infinity}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void enterInfinity(SnapExpressionsParser.InfinityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Infinity}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void exitInfinity(SnapExpressionsParser.InfinityContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Number}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void enterNumber(SnapExpressionsParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void exitNumber(SnapExpressionsParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code String}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void enterString(SnapExpressionsParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code String}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void exitString(SnapExpressionsParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Regex}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void enterRegex(SnapExpressionsParser.RegexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Regex}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void exitRegex(SnapExpressionsParser.RegexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Id}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void enterId(SnapExpressionsParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Id}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 */
	void exitId(SnapExpressionsParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#array}.
	 * @param ctx the parse tree
	 */
	void enterArray(SnapExpressionsParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#array}.
	 * @param ctx the parse tree
	 */
	void exitArray(SnapExpressionsParser.ArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#propertyNameAndValue}.
	 * @param ctx the parse tree
	 */
	void enterPropertyNameAndValue(SnapExpressionsParser.PropertyNameAndValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#propertyNameAndValue}.
	 * @param ctx the parse tree
	 */
	void exitPropertyNameAndValue(SnapExpressionsParser.PropertyNameAndValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#propertyNameOrExpr}.
	 * @param ctx the parse tree
	 */
	void enterPropertyNameOrExpr(SnapExpressionsParser.PropertyNameOrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#propertyNameOrExpr}.
	 * @param ctx the parse tree
	 */
	void exitPropertyNameOrExpr(SnapExpressionsParser.PropertyNameOrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#propertyNameExpr}.
	 * @param ctx the parse tree
	 */
	void enterPropertyNameExpr(SnapExpressionsParser.PropertyNameExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#propertyNameExpr}.
	 * @param ctx the parse tree
	 */
	void exitPropertyNameExpr(SnapExpressionsParser.PropertyNameExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#propertyName}.
	 * @param ctx the parse tree
	 */
	void enterPropertyName(SnapExpressionsParser.PropertyNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#propertyName}.
	 * @param ctx the parse tree
	 */
	void exitPropertyName(SnapExpressionsParser.PropertyNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#objliteral}.
	 * @param ctx the parse tree
	 */
	void enterObjliteral(SnapExpressionsParser.ObjliteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#objliteral}.
	 * @param ctx the parse tree
	 */
	void exitObjliteral(SnapExpressionsParser.ObjliteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DecimalNumber}
	 * labeled alternative in {@link SnapExpressionsParser#integer}.
	 * @param ctx the parse tree
	 */
	void enterDecimalNumber(SnapExpressionsParser.DecimalNumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DecimalNumber}
	 * labeled alternative in {@link SnapExpressionsParser#integer}.
	 * @param ctx the parse tree
	 */
	void exitDecimalNumber(SnapExpressionsParser.DecimalNumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HexNumber}
	 * labeled alternative in {@link SnapExpressionsParser#integer}.
	 * @param ctx the parse tree
	 */
	void enterHexNumber(SnapExpressionsParser.HexNumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HexNumber}
	 * labeled alternative in {@link SnapExpressionsParser#integer}.
	 * @param ctx the parse tree
	 */
	void exitHexNumber(SnapExpressionsParser.HexNumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OctalNumber}
	 * labeled alternative in {@link SnapExpressionsParser#integer}.
	 * @param ctx the parse tree
	 */
	void enterOctalNumber(SnapExpressionsParser.OctalNumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OctalNumber}
	 * labeled alternative in {@link SnapExpressionsParser#integer}.
	 * @param ctx the parse tree
	 */
	void exitOctalNumber(SnapExpressionsParser.OctalNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#fpnum}.
	 * @param ctx the parse tree
	 */
	void enterFpnum(SnapExpressionsParser.FpnumContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#fpnum}.
	 * @param ctx the parse tree
	 */
	void exitFpnum(SnapExpressionsParser.FpnumContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#qstring}.
	 * @param ctx the parse tree
	 */
	void enterQstring(SnapExpressionsParser.QstringContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#qstring}.
	 * @param ctx the parse tree
	 */
	void exitQstring(SnapExpressionsParser.QstringContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#idstring}.
	 * @param ctx the parse tree
	 */
	void enterIdstring(SnapExpressionsParser.IdstringContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#idstring}.
	 * @param ctx the parse tree
	 */
	void exitIdstring(SnapExpressionsParser.IdstringContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#jsregex}.
	 * @param ctx the parse tree
	 */
	void enterJsregex(SnapExpressionsParser.JsregexContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#jsregex}.
	 * @param ctx the parse tree
	 */
	void exitJsregex(SnapExpressionsParser.JsregexContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#parameterWithDefault}.
	 * @param ctx the parse tree
	 */
	void enterParameterWithDefault(SnapExpressionsParser.ParameterWithDefaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#parameterWithDefault}.
	 * @param ctx the parse tree
	 */
	void exitParameterWithDefault(SnapExpressionsParser.ParameterWithDefaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#restParameters}.
	 * @param ctx the parse tree
	 */
	void enterRestParameters(SnapExpressionsParser.RestParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#restParameters}.
	 * @param ctx the parse tree
	 */
	void exitRestParameters(SnapExpressionsParser.RestParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#coverParenthesisedExpressionAndArrowParameterList}.
	 * @param ctx the parse tree
	 */
	void enterCoverParenthesisedExpressionAndArrowParameterList(SnapExpressionsParser.CoverParenthesisedExpressionAndArrowParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#coverParenthesisedExpressionAndArrowParameterList}.
	 * @param ctx the parse tree
	 */
	void exitCoverParenthesisedExpressionAndArrowParameterList(SnapExpressionsParser.CoverParenthesisedExpressionAndArrowParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#arrowParameters}.
	 * @param ctx the parse tree
	 */
	void enterArrowParameters(SnapExpressionsParser.ArrowParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#arrowParameters}.
	 * @param ctx the parse tree
	 */
	void exitArrowParameters(SnapExpressionsParser.ArrowParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#conciseBody}.
	 * @param ctx the parse tree
	 */
	void enterConciseBody(SnapExpressionsParser.ConciseBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#conciseBody}.
	 * @param ctx the parse tree
	 */
	void exitConciseBody(SnapExpressionsParser.ConciseBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SnapExpressionsParser#arrowFunction}.
	 * @param ctx the parse tree
	 */
	void enterArrowFunction(SnapExpressionsParser.ArrowFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SnapExpressionsParser#arrowFunction}.
	 * @param ctx the parse tree
	 */
	void exitArrowFunction(SnapExpressionsParser.ArrowFunctionContext ctx);
}