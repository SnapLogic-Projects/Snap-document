// Generated from /Users/yliu/IdeaProjects/Snap-document/ExpressionImpl/src/main/antlr4/com/snaplogic/grammars/SnapExpressions.g4 by ANTLR 4.7

import java.math.BigInteger;
import java.math.BigDecimal;

import com.snaplogic.api.ExecutionException;

import com.snaplogic.expression.BailErrorStrategy;
import com.snaplogic.expression.Regex;
import com.snaplogic.expression.util.LiteralUtils;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SnapExpressionsParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SnapExpressionsVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#eval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEval(SnapExpressionsParser.EvalContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp(SnapExpressionsParser.ExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#logicalOr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOr(SnapExpressionsParser.LogicalOrContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#logicalAnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalAnd(SnapExpressionsParser.LogicalAndContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#logicalXor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalXor(SnapExpressionsParser.LogicalXorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#inOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInOperator(SnapExpressionsParser.InOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#equality}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquality(SnapExpressionsParser.EqualityContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#relational}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelational(SnapExpressionsParser.RelationalContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#addition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddition(SnapExpressionsParser.AdditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#multiplication}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplication(SnapExpressionsParser.MultiplicationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#unary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary(SnapExpressionsParser.UnaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#atomicExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomicExp(SnapExpressionsParser.AtomicExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty(SnapExpressionsParser.PropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(SnapExpressionsParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#parens}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(SnapExpressionsParser.ParensContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#parensCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParensCall(SnapExpressionsParser.ParensCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#memberSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberSuffix(SnapExpressionsParser.MemberSuffixContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#indexSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexSuffix(SnapExpressionsParser.IndexSuffixContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#propertyRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyRef(SnapExpressionsParser.PropertyRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#method}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethod(SnapExpressionsParser.MethodContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#expOrSpread}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpOrSpread(SnapExpressionsParser.ExpOrSpreadContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(SnapExpressionsParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Arr}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArr(SnapExpressionsParser.ArrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ObjectLiteral}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectLiteral(SnapExpressionsParser.ObjectLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code True}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrue(SnapExpressionsParser.TrueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code False}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalse(SnapExpressionsParser.FalseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Null}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNull(SnapExpressionsParser.NullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NaN}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNaN(SnapExpressionsParser.NaNContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Infinity}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfinity(SnapExpressionsParser.InfinityContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(SnapExpressionsParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code String}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(SnapExpressionsParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Regex}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegex(SnapExpressionsParser.RegexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Id}
	 * labeled alternative in {@link SnapExpressionsParser#atomic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(SnapExpressionsParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(SnapExpressionsParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#propertyNameAndValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyNameAndValue(SnapExpressionsParser.PropertyNameAndValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#propertyNameOrExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyNameOrExpr(SnapExpressionsParser.PropertyNameOrExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#propertyNameExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyNameExpr(SnapExpressionsParser.PropertyNameExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#propertyName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyName(SnapExpressionsParser.PropertyNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#objliteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjliteral(SnapExpressionsParser.ObjliteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DecimalNumber}
	 * labeled alternative in {@link SnapExpressionsParser#integer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalNumber(SnapExpressionsParser.DecimalNumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code HexNumber}
	 * labeled alternative in {@link SnapExpressionsParser#integer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHexNumber(SnapExpressionsParser.HexNumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OctalNumber}
	 * labeled alternative in {@link SnapExpressionsParser#integer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOctalNumber(SnapExpressionsParser.OctalNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#fpnum}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFpnum(SnapExpressionsParser.FpnumContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#qstring}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQstring(SnapExpressionsParser.QstringContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#idstring}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdstring(SnapExpressionsParser.IdstringContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#jsregex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsregex(SnapExpressionsParser.JsregexContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#parameterWithDefault}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterWithDefault(SnapExpressionsParser.ParameterWithDefaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#restParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRestParameters(SnapExpressionsParser.RestParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#coverParenthesisedExpressionAndArrowParameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCoverParenthesisedExpressionAndArrowParameterList(SnapExpressionsParser.CoverParenthesisedExpressionAndArrowParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#arrowParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowParameters(SnapExpressionsParser.ArrowParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#conciseBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConciseBody(SnapExpressionsParser.ConciseBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SnapExpressionsParser#arrowFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowFunction(SnapExpressionsParser.ArrowFunctionContext ctx);
}