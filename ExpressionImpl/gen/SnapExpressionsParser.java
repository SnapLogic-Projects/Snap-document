// Generated from /Users/yliu/IdeaProjects/Snap-document/ExpressionImpl/src/main/antlr4/com/snaplogic/grammars/SnapExpressions.g4 by ANTLR 4.7

import java.math.BigInteger;
import java.math.BigDecimal;

import com.snaplogic.api.ExecutionException;

import com.snaplogic.expression.BailErrorStrategy;
import com.snaplogic.expression.Regex;
import com.snaplogic.expression.util.LiteralUtils;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SnapExpressionsParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		OctNumber=1, HexNumber=2, Float=3, Digit=4, FloatDigit=5, QuotedString=6, 
		NonTerminatedString=7, NonTerminatedDoubleString=8, MULTICOMMENT=9, NonTerminatedComment=10, 
		LINECOMMENT=11, Regex=12, Escape=13, EscapeSlash=14, OR=15, AND=16, XOR=17, 
		EQUALS=18, NOT_EQUALS=19, GT=20, LT=21, GTE=22, LTE=23, ADD=24, SUB=25, 
		MULTIPLY=26, DIVIDE=27, MOD=28, NOT=29, QMARK=30, COLON=31, INSTANCEOF=32, 
		TYPEOF=33, ARROW=34, ASSIGN=35, IN=36, LBRACKET=37, RBRACKET=38, LCURLY=39, 
		RCURLY=40, LPAREN=41, RPAREN=42, ELLIPSIS=43, COMMA=44, DOT=45, True=46, 
		False=47, New=48, Null=49, NaN=50, Infinity=51, OCT_PREFIX=52, HEX_PREFIX=53, 
		Id=54, IdStart=55, IdPart=56, PropertyRef=57, WS=58;
	public static final int
		RULE_eval = 0, RULE_exp = 1, RULE_logicalOr = 2, RULE_logicalAnd = 3, 
		RULE_logicalXor = 4, RULE_inOperator = 5, RULE_equality = 6, RULE_relational = 7, 
		RULE_addition = 8, RULE_multiplication = 9, RULE_unary = 10, RULE_atomicExp = 11, 
		RULE_property = 12, RULE_functionCall = 13, RULE_parens = 14, RULE_parensCall = 15, 
		RULE_memberSuffix = 16, RULE_indexSuffix = 17, RULE_propertyRef = 18, 
		RULE_method = 19, RULE_expOrSpread = 20, RULE_arguments = 21, RULE_atomic = 22, 
		RULE_array = 23, RULE_propertyNameAndValue = 24, RULE_propertyNameOrExpr = 25, 
		RULE_propertyNameExpr = 26, RULE_propertyName = 27, RULE_objliteral = 28, 
		RULE_integer = 29, RULE_fpnum = 30, RULE_qstring = 31, RULE_idstring = 32, 
		RULE_jsregex = 33, RULE_parameterWithDefault = 34, RULE_restParameters = 35, 
		RULE_coverParenthesisedExpressionAndArrowParameterList = 36, RULE_arrowParameters = 37, 
		RULE_conciseBody = 38, RULE_arrowFunction = 39;
	public static final String[] ruleNames = {
		"eval", "exp", "logicalOr", "logicalAnd", "logicalXor", "inOperator", 
		"equality", "relational", "addition", "multiplication", "unary", "atomicExp", 
		"property", "functionCall", "parens", "parensCall", "memberSuffix", "indexSuffix", 
		"propertyRef", "method", "expOrSpread", "arguments", "atomic", "array", 
		"propertyNameAndValue", "propertyNameOrExpr", "propertyNameExpr", "propertyName", 
		"objliteral", "integer", "fpnum", "qstring", "idstring", "jsregex", "parameterWithDefault", 
		"restParameters", "coverParenthesisedExpressionAndArrowParameterList", 
		"arrowParameters", "conciseBody", "arrowFunction"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, "'\\/'", "'||'", "'&&'", "'^'", "'=='", "'!='", "'>'", "'<'", 
		"'>='", "'<='", "'+'", "'-'", "'*'", "'/'", "'%'", "'!'", "'?'", "':'", 
		"'instanceof'", "'typeof'", "'=>'", "'='", "'in'", "'['", "']'", "'{'", 
		"'}'", "'('", "')'", "'...'", "','", "'.'", "'true'", "'false'", "'new'", 
		"'null'", "'NaN'", "'Infinity'", "'0'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "OctNumber", "HexNumber", "Float", "Digit", "FloatDigit", "QuotedString", 
		"NonTerminatedString", "NonTerminatedDoubleString", "MULTICOMMENT", "NonTerminatedComment", 
		"LINECOMMENT", "Regex", "Escape", "EscapeSlash", "OR", "AND", "XOR", "EQUALS", 
		"NOT_EQUALS", "GT", "LT", "GTE", "LTE", "ADD", "SUB", "MULTIPLY", "DIVIDE", 
		"MOD", "NOT", "QMARK", "COLON", "INSTANCEOF", "TYPEOF", "ARROW", "ASSIGN", 
		"IN", "LBRACKET", "RBRACKET", "LCURLY", "RCURLY", "LPAREN", "RPAREN", 
		"ELLIPSIS", "COMMA", "DOT", "True", "False", "New", "Null", "NaN", "Infinity", 
		"OCT_PREFIX", "HEX_PREFIX", "Id", "IdStart", "IdPart", "PropertyRef", 
		"WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "SnapExpressions.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SnapExpressionsParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class EvalContext extends ParserRuleContext {
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public EvalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eval; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterEval(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitEval(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitEval(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EvalContext eval() throws RecognitionException {
		EvalContext _localctx = new EvalContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_eval);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			exp();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpContext extends ParserRuleContext {
		public List<LogicalOrContext> logicalOr() {
			return getRuleContexts(LogicalOrContext.class);
		}
		public LogicalOrContext logicalOr(int i) {
			return getRuleContext(LogicalOrContext.class,i);
		}
		public TerminalNode QMARK() { return getToken(SnapExpressionsParser.QMARK, 0); }
		public TerminalNode COLON() { return getToken(SnapExpressionsParser.COLON, 0); }
		public ExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpContext exp() throws RecognitionException {
		ExpContext _localctx = new ExpContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_exp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			logicalOr();
			setState(88);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(83);
				match(QMARK);
				setState(84);
				logicalOr();
				setState(85);
				match(COLON);
				setState(86);
				logicalOr();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LogicalOrContext extends ParserRuleContext {
		public List<LogicalAndContext> logicalAnd() {
			return getRuleContexts(LogicalAndContext.class);
		}
		public LogicalAndContext logicalAnd(int i) {
			return getRuleContext(LogicalAndContext.class,i);
		}
		public List<TerminalNode> OR() { return getTokens(SnapExpressionsParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(SnapExpressionsParser.OR, i);
		}
		public LogicalOrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalOr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterLogicalOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitLogicalOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitLogicalOr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicalOrContext logicalOr() throws RecognitionException {
		LogicalOrContext _localctx = new LogicalOrContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_logicalOr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			logicalAnd();
			setState(95);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(91);
					match(OR);
					setState(92);
					logicalAnd();
					}
					} 
				}
				setState(97);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LogicalAndContext extends ParserRuleContext {
		public List<LogicalXorContext> logicalXor() {
			return getRuleContexts(LogicalXorContext.class);
		}
		public LogicalXorContext logicalXor(int i) {
			return getRuleContext(LogicalXorContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(SnapExpressionsParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(SnapExpressionsParser.AND, i);
		}
		public LogicalAndContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalAnd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterLogicalAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitLogicalAnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitLogicalAnd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicalAndContext logicalAnd() throws RecognitionException {
		LogicalAndContext _localctx = new LogicalAndContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_logicalAnd);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			logicalXor();
			setState(103);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(99);
					match(AND);
					setState(100);
					logicalXor();
					}
					} 
				}
				setState(105);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LogicalXorContext extends ParserRuleContext {
		public List<InOperatorContext> inOperator() {
			return getRuleContexts(InOperatorContext.class);
		}
		public InOperatorContext inOperator(int i) {
			return getRuleContext(InOperatorContext.class,i);
		}
		public List<TerminalNode> XOR() { return getTokens(SnapExpressionsParser.XOR); }
		public TerminalNode XOR(int i) {
			return getToken(SnapExpressionsParser.XOR, i);
		}
		public LogicalXorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalXor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterLogicalXor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitLogicalXor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitLogicalXor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicalXorContext logicalXor() throws RecognitionException {
		LogicalXorContext _localctx = new LogicalXorContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_logicalXor);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			inOperator();
			setState(111);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(107);
					match(XOR);
					setState(108);
					inOperator();
					}
					} 
				}
				setState(113);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InOperatorContext extends ParserRuleContext {
		public List<EqualityContext> equality() {
			return getRuleContexts(EqualityContext.class);
		}
		public EqualityContext equality(int i) {
			return getRuleContext(EqualityContext.class,i);
		}
		public List<TerminalNode> IN() { return getTokens(SnapExpressionsParser.IN); }
		public TerminalNode IN(int i) {
			return getToken(SnapExpressionsParser.IN, i);
		}
		public InOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterInOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitInOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitInOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InOperatorContext inOperator() throws RecognitionException {
		InOperatorContext _localctx = new InOperatorContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_inOperator);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
			equality();
			setState(119);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(115);
					match(IN);
					setState(116);
					equality();
					}
					} 
				}
				setState(121);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EqualityContext extends ParserRuleContext {
		public Token op;
		public List<RelationalContext> relational() {
			return getRuleContexts(RelationalContext.class);
		}
		public RelationalContext relational(int i) {
			return getRuleContext(RelationalContext.class,i);
		}
		public List<TerminalNode> EQUALS() { return getTokens(SnapExpressionsParser.EQUALS); }
		public TerminalNode EQUALS(int i) {
			return getToken(SnapExpressionsParser.EQUALS, i);
		}
		public List<TerminalNode> NOT_EQUALS() { return getTokens(SnapExpressionsParser.NOT_EQUALS); }
		public TerminalNode NOT_EQUALS(int i) {
			return getToken(SnapExpressionsParser.NOT_EQUALS, i);
		}
		public EqualityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equality; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterEquality(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitEquality(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitEquality(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EqualityContext equality() throws RecognitionException {
		EqualityContext _localctx = new EqualityContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_equality);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
			relational();
			setState(127);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(123);
					((EqualityContext)_localctx).op = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==EQUALS || _la==NOT_EQUALS) ) {
						((EqualityContext)_localctx).op = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(124);
					relational();
					}
					} 
				}
				setState(129);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RelationalContext extends ParserRuleContext {
		public Token op;
		public List<AdditionContext> addition() {
			return getRuleContexts(AdditionContext.class);
		}
		public AdditionContext addition(int i) {
			return getRuleContext(AdditionContext.class,i);
		}
		public List<TerminalNode> GT() { return getTokens(SnapExpressionsParser.GT); }
		public TerminalNode GT(int i) {
			return getToken(SnapExpressionsParser.GT, i);
		}
		public List<TerminalNode> LT() { return getTokens(SnapExpressionsParser.LT); }
		public TerminalNode LT(int i) {
			return getToken(SnapExpressionsParser.LT, i);
		}
		public List<TerminalNode> GTE() { return getTokens(SnapExpressionsParser.GTE); }
		public TerminalNode GTE(int i) {
			return getToken(SnapExpressionsParser.GTE, i);
		}
		public List<TerminalNode> LTE() { return getTokens(SnapExpressionsParser.LTE); }
		public TerminalNode LTE(int i) {
			return getToken(SnapExpressionsParser.LTE, i);
		}
		public List<TerminalNode> INSTANCEOF() { return getTokens(SnapExpressionsParser.INSTANCEOF); }
		public TerminalNode INSTANCEOF(int i) {
			return getToken(SnapExpressionsParser.INSTANCEOF, i);
		}
		public RelationalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relational; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterRelational(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitRelational(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitRelational(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationalContext relational() throws RecognitionException {
		RelationalContext _localctx = new RelationalContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_relational);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			addition();
			setState(135);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(131);
					((RelationalContext)_localctx).op = _input.LT(1);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GT) | (1L << LT) | (1L << GTE) | (1L << LTE) | (1L << INSTANCEOF))) != 0)) ) {
						((RelationalContext)_localctx).op = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(132);
					addition();
					}
					} 
				}
				setState(137);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AdditionContext extends ParserRuleContext {
		public List<MultiplicationContext> multiplication() {
			return getRuleContexts(MultiplicationContext.class);
		}
		public MultiplicationContext multiplication(int i) {
			return getRuleContext(MultiplicationContext.class,i);
		}
		public List<TerminalNode> ADD() { return getTokens(SnapExpressionsParser.ADD); }
		public TerminalNode ADD(int i) {
			return getToken(SnapExpressionsParser.ADD, i);
		}
		public List<TerminalNode> SUB() { return getTokens(SnapExpressionsParser.SUB); }
		public TerminalNode SUB(int i) {
			return getToken(SnapExpressionsParser.SUB, i);
		}
		public AdditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_addition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterAddition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitAddition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitAddition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AdditionContext addition() throws RecognitionException {
		AdditionContext _localctx = new AdditionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_addition);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(138);
			multiplication();
			setState(143);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(139);
					_la = _input.LA(1);
					if ( !(_la==ADD || _la==SUB) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(140);
					multiplication();
					}
					} 
				}
				setState(145);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MultiplicationContext extends ParserRuleContext {
		public Token op;
		public List<UnaryContext> unary() {
			return getRuleContexts(UnaryContext.class);
		}
		public UnaryContext unary(int i) {
			return getRuleContext(UnaryContext.class,i);
		}
		public List<TerminalNode> MULTIPLY() { return getTokens(SnapExpressionsParser.MULTIPLY); }
		public TerminalNode MULTIPLY(int i) {
			return getToken(SnapExpressionsParser.MULTIPLY, i);
		}
		public List<TerminalNode> DIVIDE() { return getTokens(SnapExpressionsParser.DIVIDE); }
		public TerminalNode DIVIDE(int i) {
			return getToken(SnapExpressionsParser.DIVIDE, i);
		}
		public List<TerminalNode> MOD() { return getTokens(SnapExpressionsParser.MOD); }
		public TerminalNode MOD(int i) {
			return getToken(SnapExpressionsParser.MOD, i);
		}
		public MultiplicationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplication; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterMultiplication(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitMultiplication(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitMultiplication(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicationContext multiplication() throws RecognitionException {
		MultiplicationContext _localctx = new MultiplicationContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_multiplication);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			unary();
			setState(151);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(147);
					((MultiplicationContext)_localctx).op = _input.LT(1);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MULTIPLY) | (1L << DIVIDE) | (1L << MOD))) != 0)) ) {
						((MultiplicationContext)_localctx).op = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(148);
					unary();
					}
					} 
				}
				setState(153);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnaryContext extends ParserRuleContext {
		public Token op;
		public AtomicExpContext atomicExp() {
			return getRuleContext(AtomicExpContext.class,0);
		}
		public TerminalNode SUB() { return getToken(SnapExpressionsParser.SUB, 0); }
		public TerminalNode ADD() { return getToken(SnapExpressionsParser.ADD, 0); }
		public TerminalNode NOT() { return getToken(SnapExpressionsParser.NOT, 0); }
		public TerminalNode TYPEOF() { return getToken(SnapExpressionsParser.TYPEOF, 0); }
		public UnaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unary; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterUnary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitUnary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitUnary(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryContext unary() throws RecognitionException {
		UnaryContext _localctx = new UnaryContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_unary);
		int _la;
		try {
			setState(157);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OctNumber:
			case HexNumber:
			case Float:
			case Digit:
			case QuotedString:
			case Regex:
			case LBRACKET:
			case LCURLY:
			case LPAREN:
			case True:
			case False:
			case Null:
			case NaN:
			case Infinity:
			case Id:
				enterOuterAlt(_localctx, 1);
				{
				setState(154);
				atomicExp();
				}
				break;
			case ADD:
			case SUB:
			case NOT:
			case TYPEOF:
				enterOuterAlt(_localctx, 2);
				{
				setState(155);
				((UnaryContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << TYPEOF))) != 0)) ) {
					((UnaryContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(156);
				atomicExp();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomicExpContext extends ParserRuleContext {
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public PropertyContext property() {
			return getRuleContext(PropertyContext.class,0);
		}
		public ArrowFunctionContext arrowFunction() {
			return getRuleContext(ArrowFunctionContext.class,0);
		}
		public ParensContext parens() {
			return getRuleContext(ParensContext.class,0);
		}
		public ParensCallContext parensCall() {
			return getRuleContext(ParensCallContext.class,0);
		}
		public AtomicExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomicExp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterAtomicExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitAtomicExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitAtomicExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomicExpContext atomicExp() throws RecognitionException {
		AtomicExpContext _localctx = new AtomicExpContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_atomicExp);
		try {
			setState(164);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(159);
				functionCall();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(160);
				property();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(161);
				arrowFunction();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(162);
				parens();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(163);
				parensCall();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyContext extends ParserRuleContext {
		public AtomicContext atomic() {
			return getRuleContext(AtomicContext.class,0);
		}
		public List<MemberSuffixContext> memberSuffix() {
			return getRuleContexts(MemberSuffixContext.class);
		}
		public MemberSuffixContext memberSuffix(int i) {
			return getRuleContext(MemberSuffixContext.class,i);
		}
		public PropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_property; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyContext property() throws RecognitionException {
		PropertyContext _localctx = new PropertyContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_property);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			atomic();
			setState(170);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(167);
					memberSuffix();
					}
					} 
				}
				setState(172);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionCallContext extends ParserRuleContext {
		public AtomicContext atomic() {
			return getRuleContext(AtomicContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public List<MemberSuffixContext> memberSuffix() {
			return getRuleContexts(MemberSuffixContext.class);
		}
		public MemberSuffixContext memberSuffix(int i) {
			return getRuleContext(MemberSuffixContext.class,i);
		}
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitFunctionCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_functionCall);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(173);
			atomic();
			setState(174);
			arguments();
			setState(178);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(175);
					memberSuffix();
					}
					} 
				}
				setState(180);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParensContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(SnapExpressionsParser.LPAREN, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(SnapExpressionsParser.RPAREN, 0); }
		public List<MemberSuffixContext> memberSuffix() {
			return getRuleContexts(MemberSuffixContext.class);
		}
		public MemberSuffixContext memberSuffix(int i) {
			return getRuleContext(MemberSuffixContext.class,i);
		}
		public ParensContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parens; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterParens(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitParens(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitParens(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParensContext parens() throws RecognitionException {
		ParensContext _localctx = new ParensContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_parens);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(181);
			match(LPAREN);
			setState(182);
			exp();
			setState(183);
			match(RPAREN);
			setState(187);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(184);
					memberSuffix();
					}
					} 
				}
				setState(189);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParensCallContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(SnapExpressionsParser.LPAREN, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(SnapExpressionsParser.RPAREN, 0); }
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public ParensCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parensCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterParensCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitParensCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitParensCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParensCallContext parensCall() throws RecognitionException {
		ParensCallContext _localctx = new ParensCallContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_parensCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			match(LPAREN);
			setState(191);
			exp();
			setState(192);
			match(RPAREN);
			setState(193);
			arguments();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MemberSuffixContext extends ParserRuleContext {
		public IndexSuffixContext indexSuffix() {
			return getRuleContext(IndexSuffixContext.class,0);
		}
		public MethodContext method() {
			return getRuleContext(MethodContext.class,0);
		}
		public PropertyRefContext propertyRef() {
			return getRuleContext(PropertyRefContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public MemberSuffixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberSuffix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterMemberSuffix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitMemberSuffix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitMemberSuffix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberSuffixContext memberSuffix() throws RecognitionException {
		MemberSuffixContext _localctx = new MemberSuffixContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_memberSuffix);
		try {
			setState(199);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(195);
				indexSuffix();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(196);
				method();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(197);
				propertyRef();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(198);
				arguments();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IndexSuffixContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(SnapExpressionsParser.LBRACKET, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode RBRACKET() { return getToken(SnapExpressionsParser.RBRACKET, 0); }
		public IndexSuffixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_indexSuffix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterIndexSuffix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitIndexSuffix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitIndexSuffix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IndexSuffixContext indexSuffix() throws RecognitionException {
		IndexSuffixContext _localctx = new IndexSuffixContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_indexSuffix);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(201);
			match(LBRACKET);
			setState(202);
			exp();
			setState(203);
			match(RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyRefContext extends ParserRuleContext {
		public TerminalNode PropertyRef() { return getToken(SnapExpressionsParser.PropertyRef, 0); }
		public PropertyRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterPropertyRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitPropertyRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitPropertyRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyRefContext propertyRef() throws RecognitionException {
		PropertyRefContext _localctx = new PropertyRefContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_propertyRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(205);
			match(PropertyRef);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodContext extends ParserRuleContext {
		public TerminalNode PropertyRef() { return getToken(SnapExpressionsParser.PropertyRef, 0); }
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public MethodContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_method; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterMethod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitMethod(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitMethod(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodContext method() throws RecognitionException {
		MethodContext _localctx = new MethodContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_method);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			match(PropertyRef);
			setState(208);
			arguments();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpOrSpreadContext extends ParserRuleContext {
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode ELLIPSIS() { return getToken(SnapExpressionsParser.ELLIPSIS, 0); }
		public ExpOrSpreadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expOrSpread; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterExpOrSpread(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitExpOrSpread(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitExpOrSpread(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpOrSpreadContext expOrSpread() throws RecognitionException {
		ExpOrSpreadContext _localctx = new ExpOrSpreadContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_expOrSpread);
		try {
			setState(213);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OctNumber:
			case HexNumber:
			case Float:
			case Digit:
			case QuotedString:
			case Regex:
			case ADD:
			case SUB:
			case NOT:
			case TYPEOF:
			case LBRACKET:
			case LCURLY:
			case LPAREN:
			case True:
			case False:
			case Null:
			case NaN:
			case Infinity:
			case Id:
				enterOuterAlt(_localctx, 1);
				{
				setState(210);
				exp();
				}
				break;
			case ELLIPSIS:
				enterOuterAlt(_localctx, 2);
				{
				setState(211);
				match(ELLIPSIS);
				setState(212);
				exp();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentsContext extends ParserRuleContext {
		public ExpOrSpreadContext expOrSpread;
		public List<ExpOrSpreadContext> args = new ArrayList<ExpOrSpreadContext>();
		public TerminalNode LPAREN() { return getToken(SnapExpressionsParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(SnapExpressionsParser.RPAREN, 0); }
		public List<ExpOrSpreadContext> expOrSpread() {
			return getRuleContexts(ExpOrSpreadContext.class);
		}
		public ExpOrSpreadContext expOrSpread(int i) {
			return getRuleContext(ExpOrSpreadContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SnapExpressionsParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SnapExpressionsParser.COMMA, i);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitArguments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_arguments);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(215);
			match(LPAREN);
			setState(224);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				{
				setState(216);
				((ArgumentsContext)_localctx).expOrSpread = expOrSpread();
				((ArgumentsContext)_localctx).args.add(((ArgumentsContext)_localctx).expOrSpread);
				setState(221);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(217);
						match(COMMA);
						setState(218);
						((ArgumentsContext)_localctx).expOrSpread = expOrSpread();
						((ArgumentsContext)_localctx).args.add(((ArgumentsContext)_localctx).expOrSpread);
						}
						} 
					}
					setState(223);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
				}
				}
				break;
			}
			setState(227);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				setState(226);
				match(RPAREN);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomicContext extends ParserRuleContext {
		public AtomicContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomic; }
	 
		public AtomicContext() { }
		public void copyFrom(AtomicContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ArrContext extends AtomicContext {
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public ArrContext(AtomicContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterArr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitArr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitArr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NullContext extends AtomicContext {
		public TerminalNode Null() { return getToken(SnapExpressionsParser.Null, 0); }
		public NullContext(AtomicContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterNull(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitNull(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitNull(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ObjectLiteralContext extends AtomicContext {
		public ObjliteralContext objliteral() {
			return getRuleContext(ObjliteralContext.class,0);
		}
		public ObjectLiteralContext(AtomicContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterObjectLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitObjectLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitObjectLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InfinityContext extends AtomicContext {
		public TerminalNode Infinity() { return getToken(SnapExpressionsParser.Infinity, 0); }
		public InfinityContext(AtomicContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterInfinity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitInfinity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitInfinity(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NumberContext extends AtomicContext {
		public FpnumContext fpnum() {
			return getRuleContext(FpnumContext.class,0);
		}
		public IntegerContext integer() {
			return getRuleContext(IntegerContext.class,0);
		}
		public NumberContext(AtomicContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RegexContext extends AtomicContext {
		public JsregexContext jsregex() {
			return getRuleContext(JsregexContext.class,0);
		}
		public RegexContext(AtomicContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterRegex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitRegex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitRegex(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TrueContext extends AtomicContext {
		public TerminalNode True() { return getToken(SnapExpressionsParser.True, 0); }
		public TrueContext(AtomicContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterTrue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitTrue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitTrue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NaNContext extends AtomicContext {
		public TerminalNode NaN() { return getToken(SnapExpressionsParser.NaN, 0); }
		public NaNContext(AtomicContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterNaN(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitNaN(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitNaN(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FalseContext extends AtomicContext {
		public TerminalNode False() { return getToken(SnapExpressionsParser.False, 0); }
		public FalseContext(AtomicContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterFalse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitFalse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitFalse(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringContext extends AtomicContext {
		public QstringContext qstring() {
			return getRuleContext(QstringContext.class,0);
		}
		public StringContext(AtomicContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdContext extends AtomicContext {
		public IdstringContext idstring() {
			return getRuleContext(IdstringContext.class,0);
		}
		public IdContext(AtomicContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomicContext atomic() throws RecognitionException {
		AtomicContext _localctx = new AtomicContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_atomic);
		try {
			setState(243);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRACKET:
				_localctx = new ArrContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(229);
				array();
				}
				break;
			case LCURLY:
				_localctx = new ObjectLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(230);
				objliteral();
				}
				break;
			case True:
				_localctx = new TrueContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(231);
				match(True);
				}
				break;
			case False:
				_localctx = new FalseContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(232);
				match(False);
				}
				break;
			case Null:
				_localctx = new NullContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(233);
				match(Null);
				}
				break;
			case NaN:
				_localctx = new NaNContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(234);
				match(NaN);
				}
				break;
			case Infinity:
				_localctx = new InfinityContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(235);
				match(Infinity);
				}
				break;
			case OctNumber:
			case HexNumber:
			case Float:
			case Digit:
				_localctx = new NumberContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(238);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Float:
					{
					setState(236);
					fpnum();
					}
					break;
				case OctNumber:
				case HexNumber:
				case Digit:
					{
					setState(237);
					integer();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case QuotedString:
				_localctx = new StringContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(240);
				qstring();
				}
				break;
			case Regex:
				_localctx = new RegexContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(241);
				jsregex();
				}
				break;
			case Id:
				_localctx = new IdContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(242);
				idstring();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(SnapExpressionsParser.LBRACKET, 0); }
		public List<ExpOrSpreadContext> expOrSpread() {
			return getRuleContexts(ExpOrSpreadContext.class);
		}
		public ExpOrSpreadContext expOrSpread(int i) {
			return getRuleContext(ExpOrSpreadContext.class,i);
		}
		public TerminalNode RBRACKET() { return getToken(SnapExpressionsParser.RBRACKET, 0); }
		public List<TerminalNode> COMMA() { return getTokens(SnapExpressionsParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SnapExpressionsParser.COMMA, i);
		}
		public ArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayContext array() throws RecognitionException {
		ArrayContext _localctx = new ArrayContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_array);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(245);
			match(LBRACKET);
			setState(257);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				{
				setState(246);
				expOrSpread();
				setState(251);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(247);
						match(COMMA);
						setState(248);
						expOrSpread();
						}
						} 
					}
					setState(253);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
				}
				setState(255);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
				case 1:
					{
					setState(254);
					match(COMMA);
					}
					break;
				}
				}
				break;
			}
			setState(260);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				setState(259);
				match(RBRACKET);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyNameAndValueContext extends ParserRuleContext {
		public PropertyNameOrExprContext propertyNameOrExpr() {
			return getRuleContext(PropertyNameOrExprContext.class,0);
		}
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode ELLIPSIS() { return getToken(SnapExpressionsParser.ELLIPSIS, 0); }
		public PropertyNameAndValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyNameAndValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterPropertyNameAndValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitPropertyNameAndValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitPropertyNameAndValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyNameAndValueContext propertyNameAndValue() throws RecognitionException {
		PropertyNameAndValueContext _localctx = new PropertyNameAndValueContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_propertyNameAndValue);
		try {
			setState(268);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OctNumber:
			case HexNumber:
			case Digit:
			case QuotedString:
			case LBRACKET:
			case Id:
				enterOuterAlt(_localctx, 1);
				{
				setState(262);
				propertyNameOrExpr();
				setState(263);
				match(COLON);
				setState(264);
				exp();
				}
				break;
			case ELLIPSIS:
				enterOuterAlt(_localctx, 2);
				{
				setState(266);
				match(ELLIPSIS);
				setState(267);
				exp();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyNameOrExprContext extends ParserRuleContext {
		public PropertyNameContext propertyName() {
			return getRuleContext(PropertyNameContext.class,0);
		}
		public PropertyNameExprContext propertyNameExpr() {
			return getRuleContext(PropertyNameExprContext.class,0);
		}
		public PropertyNameOrExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyNameOrExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterPropertyNameOrExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitPropertyNameOrExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitPropertyNameOrExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyNameOrExprContext propertyNameOrExpr() throws RecognitionException {
		PropertyNameOrExprContext _localctx = new PropertyNameOrExprContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_propertyNameOrExpr);
		try {
			setState(272);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OctNumber:
			case HexNumber:
			case Digit:
			case QuotedString:
			case Id:
				enterOuterAlt(_localctx, 1);
				{
				setState(270);
				propertyName();
				}
				break;
			case LBRACKET:
				enterOuterAlt(_localctx, 2);
				{
				setState(271);
				propertyNameExpr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyNameExprContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(SnapExpressionsParser.LBRACKET, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode RBRACKET() { return getToken(SnapExpressionsParser.RBRACKET, 0); }
		public PropertyNameExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyNameExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterPropertyNameExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitPropertyNameExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitPropertyNameExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyNameExprContext propertyNameExpr() throws RecognitionException {
		PropertyNameExprContext _localctx = new PropertyNameExprContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_propertyNameExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(274);
			match(LBRACKET);
			setState(275);
			exp();
			setState(276);
			match(RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyNameContext extends ParserRuleContext {
		public String value;
		public Token Id;
		public QstringContext qstring;
		public IntegerContext integer;
		public TerminalNode Id() { return getToken(SnapExpressionsParser.Id, 0); }
		public QstringContext qstring() {
			return getRuleContext(QstringContext.class,0);
		}
		public IntegerContext integer() {
			return getRuleContext(IntegerContext.class,0);
		}
		public PropertyNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterPropertyName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitPropertyName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitPropertyName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyNameContext propertyName() throws RecognitionException {
		PropertyNameContext _localctx = new PropertyNameContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_propertyName);
		try {
			setState(286);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Id:
				enterOuterAlt(_localctx, 1);
				{
				setState(278);
				((PropertyNameContext)_localctx).Id = match(Id);
				 ((PropertyNameContext)_localctx).value =  (((PropertyNameContext)_localctx).Id!=null?((PropertyNameContext)_localctx).Id.getText():null); 
				}
				break;
			case QuotedString:
				enterOuterAlt(_localctx, 2);
				{
				setState(280);
				((PropertyNameContext)_localctx).qstring = qstring();
				 ((PropertyNameContext)_localctx).value =  ((PropertyNameContext)_localctx).qstring.value; 
				}
				break;
			case OctNumber:
			case HexNumber:
			case Digit:
				enterOuterAlt(_localctx, 3);
				{
				setState(283);
				((PropertyNameContext)_localctx).integer = integer();
				 ((PropertyNameContext)_localctx).value =  ((PropertyNameContext)_localctx).integer.value.toString(); 
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjliteralContext extends ParserRuleContext {
		public TerminalNode LCURLY() { return getToken(SnapExpressionsParser.LCURLY, 0); }
		public List<PropertyNameAndValueContext> propertyNameAndValue() {
			return getRuleContexts(PropertyNameAndValueContext.class);
		}
		public PropertyNameAndValueContext propertyNameAndValue(int i) {
			return getRuleContext(PropertyNameAndValueContext.class,i);
		}
		public TerminalNode RCURLY() { return getToken(SnapExpressionsParser.RCURLY, 0); }
		public List<TerminalNode> COMMA() { return getTokens(SnapExpressionsParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SnapExpressionsParser.COMMA, i);
		}
		public ObjliteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objliteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterObjliteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitObjliteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitObjliteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjliteralContext objliteral() throws RecognitionException {
		ObjliteralContext _localctx = new ObjliteralContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_objliteral);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(288);
			match(LCURLY);
			setState(300);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				setState(289);
				propertyNameAndValue();
				setState(294);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(290);
						match(COMMA);
						setState(291);
						propertyNameAndValue();
						}
						} 
					}
					setState(296);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
				}
				setState(298);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
				case 1:
					{
					setState(297);
					match(COMMA);
					}
					break;
				}
				}
				break;
			}
			setState(303);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				{
				setState(302);
				match(RCURLY);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntegerContext extends ParserRuleContext {
		public BigInteger value;
		public IntegerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integer; }
	 
		public IntegerContext() { }
		public void copyFrom(IntegerContext ctx) {
			super.copyFrom(ctx);
			this.value = ctx.value;
		}
	}
	public static class DecimalNumberContext extends IntegerContext {
		public Token Digit;
		public TerminalNode Digit() { return getToken(SnapExpressionsParser.Digit, 0); }
		public DecimalNumberContext(IntegerContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterDecimalNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitDecimalNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitDecimalNumber(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OctalNumberContext extends IntegerContext {
		public Token OctNumber;
		public TerminalNode OctNumber() { return getToken(SnapExpressionsParser.OctNumber, 0); }
		public OctalNumberContext(IntegerContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterOctalNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitOctalNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitOctalNumber(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class HexNumberContext extends IntegerContext {
		public Token HexNumber;
		public TerminalNode HexNumber() { return getToken(SnapExpressionsParser.HexNumber, 0); }
		public HexNumberContext(IntegerContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterHexNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitHexNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitHexNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntegerContext integer() throws RecognitionException {
		IntegerContext _localctx = new IntegerContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_integer);
		try {
			setState(311);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Digit:
				_localctx = new DecimalNumberContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(305);
				((DecimalNumberContext)_localctx).Digit = match(Digit);
				 ((DecimalNumberContext)_localctx).value =  new BigInteger((((DecimalNumberContext)_localctx).Digit!=null?((DecimalNumberContext)_localctx).Digit.getText():null)); 
				}
				break;
			case HexNumber:
				_localctx = new HexNumberContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(307);
				((HexNumberContext)_localctx).HexNumber = match(HexNumber);
				 ((HexNumberContext)_localctx).value =  new BigInteger((((HexNumberContext)_localctx).HexNumber!=null?((HexNumberContext)_localctx).HexNumber.getText():null).substring(2), 16); 
				}
				break;
			case OctNumber:
				_localctx = new OctalNumberContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(309);
				((OctalNumberContext)_localctx).OctNumber = match(OctNumber);
				 ((OctalNumberContext)_localctx).value =  new BigInteger((((OctalNumberContext)_localctx).OctNumber!=null?((OctalNumberContext)_localctx).OctNumber.getText():null).substring(1), 8); 
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FpnumContext extends ParserRuleContext {
		public BigDecimal value;
		public Token Float;
		public TerminalNode Float() { return getToken(SnapExpressionsParser.Float, 0); }
		public FpnumContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fpnum; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterFpnum(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitFpnum(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitFpnum(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FpnumContext fpnum() throws RecognitionException {
		FpnumContext _localctx = new FpnumContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_fpnum);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(313);
			((FpnumContext)_localctx).Float = match(Float);
			 ((FpnumContext)_localctx).value =  new BigDecimal((((FpnumContext)_localctx).Float!=null?((FpnumContext)_localctx).Float.getText():null)); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QstringContext extends ParserRuleContext {
		public String value;
		public Token QuotedString;
		public TerminalNode QuotedString() { return getToken(SnapExpressionsParser.QuotedString, 0); }
		public QstringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qstring; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterQstring(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitQstring(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitQstring(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QstringContext qstring() throws RecognitionException {
		QstringContext _localctx = new QstringContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_qstring);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(316);
			((QstringContext)_localctx).QuotedString = match(QuotedString);
			 ((QstringContext)_localctx).value =  LiteralUtils.unescapeString((((QstringContext)_localctx).QuotedString!=null?((QstringContext)_localctx).QuotedString.getText():null)); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdstringContext extends ParserRuleContext {
		public String original;
		public String remaining;
		public Token Id;
		public TerminalNode Id() { return getToken(SnapExpressionsParser.Id, 0); }
		public IdstringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idstring; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterIdstring(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitIdstring(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitIdstring(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdstringContext idstring() throws RecognitionException {
		IdstringContext _localctx = new IdstringContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_idstring);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(319);
			((IdstringContext)_localctx).Id = match(Id);

			               ((IdstringContext)_localctx).original =  (((IdstringContext)_localctx).Id!=null?((IdstringContext)_localctx).Id.getText():null);
			               if ((((IdstringContext)_localctx).Id!=null?((IdstringContext)_localctx).Id.getText():null).startsWith("$")) {
			                   ((IdstringContext)_localctx).remaining =  (((IdstringContext)_localctx).Id!=null?((IdstringContext)_localctx).Id.getText():null).substring(1);
			               } else {
			                   ((IdstringContext)_localctx).remaining =  null;
			               }
			           
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JsregexContext extends ParserRuleContext {
		public Regex value;
		public Token Regex;
		public TerminalNode Regex() { return getToken(SnapExpressionsParser.Regex, 0); }
		public JsregexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsregex; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterJsregex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitJsregex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitJsregex(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsregexContext jsregex() throws RecognitionException {
		JsregexContext _localctx = new JsregexContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_jsregex);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(322);
			((JsregexContext)_localctx).Regex = match(Regex);

			        int lastSlash = (((JsregexContext)_localctx).Regex!=null?((JsregexContext)_localctx).Regex.getText():null).lastIndexOf("/");
			        String pattern = (((JsregexContext)_localctx).Regex!=null?((JsregexContext)_localctx).Regex.getText():null).substring(1, lastSlash);
			        String flags = (((JsregexContext)_localctx).Regex!=null?((JsregexContext)_localctx).Regex.getText():null).substring(lastSlash + 1);
			        ((JsregexContext)_localctx).value =  LiteralUtils.compileRegex(pattern, flags);
			    
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParameterWithDefaultContext extends ParserRuleContext {
		public IdstringContext idstring() {
			return getRuleContext(IdstringContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(SnapExpressionsParser.ASSIGN, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public ParameterWithDefaultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterWithDefault; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterParameterWithDefault(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitParameterWithDefault(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitParameterWithDefault(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterWithDefaultContext parameterWithDefault() throws RecognitionException {
		ParameterWithDefaultContext _localctx = new ParameterWithDefaultContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_parameterWithDefault);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(325);
			idstring();
			setState(328);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(326);
				match(ASSIGN);
				setState(327);
				exp();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RestParametersContext extends ParserRuleContext {
		public TerminalNode ELLIPSIS() { return getToken(SnapExpressionsParser.ELLIPSIS, 0); }
		public IdstringContext idstring() {
			return getRuleContext(IdstringContext.class,0);
		}
		public RestParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_restParameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterRestParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitRestParameters(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitRestParameters(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RestParametersContext restParameters() throws RecognitionException {
		RestParametersContext _localctx = new RestParametersContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_restParameters);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(330);
			match(ELLIPSIS);
			setState(331);
			idstring();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CoverParenthesisedExpressionAndArrowParameterListContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(SnapExpressionsParser.LPAREN, 0); }
		public List<ParameterWithDefaultContext> parameterWithDefault() {
			return getRuleContexts(ParameterWithDefaultContext.class);
		}
		public ParameterWithDefaultContext parameterWithDefault(int i) {
			return getRuleContext(ParameterWithDefaultContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(SnapExpressionsParser.RPAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(SnapExpressionsParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SnapExpressionsParser.COMMA, i);
		}
		public RestParametersContext restParameters() {
			return getRuleContext(RestParametersContext.class,0);
		}
		public CoverParenthesisedExpressionAndArrowParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_coverParenthesisedExpressionAndArrowParameterList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterCoverParenthesisedExpressionAndArrowParameterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitCoverParenthesisedExpressionAndArrowParameterList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitCoverParenthesisedExpressionAndArrowParameterList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CoverParenthesisedExpressionAndArrowParameterListContext coverParenthesisedExpressionAndArrowParameterList() throws RecognitionException {
		CoverParenthesisedExpressionAndArrowParameterListContext _localctx = new CoverParenthesisedExpressionAndArrowParameterListContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_coverParenthesisedExpressionAndArrowParameterList);
		int _la;
		try {
			int _alt;
			setState(356);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(333);
				match(LPAREN);
				setState(346);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Id) {
					{
					setState(334);
					parameterWithDefault();
					setState(339);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(335);
							match(COMMA);
							setState(336);
							parameterWithDefault();
							}
							} 
						}
						setState(341);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
					}
					setState(344);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(342);
						match(COMMA);
						setState(343);
						restParameters();
						}
					}

					}
				}

				setState(349);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==RPAREN) {
					{
					setState(348);
					match(RPAREN);
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(351);
				match(LPAREN);
				setState(352);
				restParameters();
				setState(354);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==RPAREN) {
					{
					setState(353);
					match(RPAREN);
					}
				}

				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrowParametersContext extends ParserRuleContext {
		public IdstringContext idstring() {
			return getRuleContext(IdstringContext.class,0);
		}
		public CoverParenthesisedExpressionAndArrowParameterListContext coverParenthesisedExpressionAndArrowParameterList() {
			return getRuleContext(CoverParenthesisedExpressionAndArrowParameterListContext.class,0);
		}
		public ArrowParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrowParameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterArrowParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitArrowParameters(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitArrowParameters(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrowParametersContext arrowParameters() throws RecognitionException {
		ArrowParametersContext _localctx = new ArrowParametersContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_arrowParameters);
		try {
			setState(360);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Id:
				enterOuterAlt(_localctx, 1);
				{
				setState(358);
				idstring();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(359);
				coverParenthesisedExpressionAndArrowParameterList();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConciseBodyContext extends ParserRuleContext {
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public ConciseBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conciseBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterConciseBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitConciseBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitConciseBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConciseBodyContext conciseBody() throws RecognitionException {
		ConciseBodyContext _localctx = new ConciseBodyContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_conciseBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(362);
			exp();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrowFunctionContext extends ParserRuleContext {
		public ArrowParametersContext arrowParameters() {
			return getRuleContext(ArrowParametersContext.class,0);
		}
		public TerminalNode ARROW() { return getToken(SnapExpressionsParser.ARROW, 0); }
		public ConciseBodyContext conciseBody() {
			return getRuleContext(ConciseBodyContext.class,0);
		}
		public ArrowFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrowFunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).enterArrowFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SnapExpressionsListener ) ((SnapExpressionsListener)listener).exitArrowFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SnapExpressionsVisitor ) return ((SnapExpressionsVisitor<? extends T>)visitor).visitArrowFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrowFunctionContext arrowFunction() throws RecognitionException {
		ArrowFunctionContext _localctx = new ArrowFunctionContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_arrowFunction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(364);
			arrowParameters();
			setState(365);
			match(ARROW);
			setState(366);
			conciseBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3<\u0173\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\3\2\3\2\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\5\3[\n\3\3\4\3\4\3\4\7\4`\n\4\f\4\16\4c\13\4\3\5\3"+
		"\5\3\5\7\5h\n\5\f\5\16\5k\13\5\3\6\3\6\3\6\7\6p\n\6\f\6\16\6s\13\6\3\7"+
		"\3\7\3\7\7\7x\n\7\f\7\16\7{\13\7\3\b\3\b\3\b\7\b\u0080\n\b\f\b\16\b\u0083"+
		"\13\b\3\t\3\t\3\t\7\t\u0088\n\t\f\t\16\t\u008b\13\t\3\n\3\n\3\n\7\n\u0090"+
		"\n\n\f\n\16\n\u0093\13\n\3\13\3\13\3\13\7\13\u0098\n\13\f\13\16\13\u009b"+
		"\13\13\3\f\3\f\3\f\5\f\u00a0\n\f\3\r\3\r\3\r\3\r\3\r\5\r\u00a7\n\r\3\16"+
		"\3\16\7\16\u00ab\n\16\f\16\16\16\u00ae\13\16\3\17\3\17\3\17\7\17\u00b3"+
		"\n\17\f\17\16\17\u00b6\13\17\3\20\3\20\3\20\3\20\7\20\u00bc\n\20\f\20"+
		"\16\20\u00bf\13\20\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\5\22\u00ca"+
		"\n\22\3\23\3\23\3\23\3\23\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3\26\5\26"+
		"\u00d8\n\26\3\27\3\27\3\27\3\27\7\27\u00de\n\27\f\27\16\27\u00e1\13\27"+
		"\5\27\u00e3\n\27\3\27\5\27\u00e6\n\27\3\30\3\30\3\30\3\30\3\30\3\30\3"+
		"\30\3\30\3\30\5\30\u00f1\n\30\3\30\3\30\3\30\5\30\u00f6\n\30\3\31\3\31"+
		"\3\31\3\31\7\31\u00fc\n\31\f\31\16\31\u00ff\13\31\3\31\5\31\u0102\n\31"+
		"\5\31\u0104\n\31\3\31\5\31\u0107\n\31\3\32\3\32\3\32\3\32\3\32\3\32\5"+
		"\32\u010f\n\32\3\33\3\33\5\33\u0113\n\33\3\34\3\34\3\34\3\34\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\35\3\35\5\35\u0121\n\35\3\36\3\36\3\36\3\36\7\36"+
		"\u0127\n\36\f\36\16\36\u012a\13\36\3\36\5\36\u012d\n\36\5\36\u012f\n\36"+
		"\3\36\5\36\u0132\n\36\3\37\3\37\3\37\3\37\3\37\3\37\5\37\u013a\n\37\3"+
		" \3 \3 \3!\3!\3!\3\"\3\"\3\"\3#\3#\3#\3$\3$\3$\5$\u014b\n$\3%\3%\3%\3"+
		"&\3&\3&\3&\7&\u0154\n&\f&\16&\u0157\13&\3&\3&\5&\u015b\n&\5&\u015d\n&"+
		"\3&\5&\u0160\n&\3&\3&\3&\5&\u0165\n&\5&\u0167\n&\3\'\3\'\5\'\u016b\n\'"+
		"\3(\3(\3)\3)\3)\3)\3)\2\2*\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$"+
		"&(*,.\60\62\64\668:<>@BDFHJLNP\2\7\3\2\24\25\4\2\26\31\"\"\3\2\32\33\3"+
		"\2\34\36\5\2\32\33\37\37##\2\u0183\2R\3\2\2\2\4T\3\2\2\2\6\\\3\2\2\2\b"+
		"d\3\2\2\2\nl\3\2\2\2\ft\3\2\2\2\16|\3\2\2\2\20\u0084\3\2\2\2\22\u008c"+
		"\3\2\2\2\24\u0094\3\2\2\2\26\u009f\3\2\2\2\30\u00a6\3\2\2\2\32\u00a8\3"+
		"\2\2\2\34\u00af\3\2\2\2\36\u00b7\3\2\2\2 \u00c0\3\2\2\2\"\u00c9\3\2\2"+
		"\2$\u00cb\3\2\2\2&\u00cf\3\2\2\2(\u00d1\3\2\2\2*\u00d7\3\2\2\2,\u00d9"+
		"\3\2\2\2.\u00f5\3\2\2\2\60\u00f7\3\2\2\2\62\u010e\3\2\2\2\64\u0112\3\2"+
		"\2\2\66\u0114\3\2\2\28\u0120\3\2\2\2:\u0122\3\2\2\2<\u0139\3\2\2\2>\u013b"+
		"\3\2\2\2@\u013e\3\2\2\2B\u0141\3\2\2\2D\u0144\3\2\2\2F\u0147\3\2\2\2H"+
		"\u014c\3\2\2\2J\u0166\3\2\2\2L\u016a\3\2\2\2N\u016c\3\2\2\2P\u016e\3\2"+
		"\2\2RS\5\4\3\2S\3\3\2\2\2TZ\5\6\4\2UV\7 \2\2VW\5\6\4\2WX\7!\2\2XY\5\6"+
		"\4\2Y[\3\2\2\2ZU\3\2\2\2Z[\3\2\2\2[\5\3\2\2\2\\a\5\b\5\2]^\7\21\2\2^`"+
		"\5\b\5\2_]\3\2\2\2`c\3\2\2\2a_\3\2\2\2ab\3\2\2\2b\7\3\2\2\2ca\3\2\2\2"+
		"di\5\n\6\2ef\7\22\2\2fh\5\n\6\2ge\3\2\2\2hk\3\2\2\2ig\3\2\2\2ij\3\2\2"+
		"\2j\t\3\2\2\2ki\3\2\2\2lq\5\f\7\2mn\7\23\2\2np\5\f\7\2om\3\2\2\2ps\3\2"+
		"\2\2qo\3\2\2\2qr\3\2\2\2r\13\3\2\2\2sq\3\2\2\2ty\5\16\b\2uv\7&\2\2vx\5"+
		"\16\b\2wu\3\2\2\2x{\3\2\2\2yw\3\2\2\2yz\3\2\2\2z\r\3\2\2\2{y\3\2\2\2|"+
		"\u0081\5\20\t\2}~\t\2\2\2~\u0080\5\20\t\2\177}\3\2\2\2\u0080\u0083\3\2"+
		"\2\2\u0081\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082\17\3\2\2\2\u0083\u0081"+
		"\3\2\2\2\u0084\u0089\5\22\n\2\u0085\u0086\t\3\2\2\u0086\u0088\5\22\n\2"+
		"\u0087\u0085\3\2\2\2\u0088\u008b\3\2\2\2\u0089\u0087\3\2\2\2\u0089\u008a"+
		"\3\2\2\2\u008a\21\3\2\2\2\u008b\u0089\3\2\2\2\u008c\u0091\5\24\13\2\u008d"+
		"\u008e\t\4\2\2\u008e\u0090\5\24\13\2\u008f\u008d\3\2\2\2\u0090\u0093\3"+
		"\2\2\2\u0091\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092\23\3\2\2\2\u0093"+
		"\u0091\3\2\2\2\u0094\u0099\5\26\f\2\u0095\u0096\t\5\2\2\u0096\u0098\5"+
		"\26\f\2\u0097\u0095\3\2\2\2\u0098\u009b\3\2\2\2\u0099\u0097\3\2\2\2\u0099"+
		"\u009a\3\2\2\2\u009a\25\3\2\2\2\u009b\u0099\3\2\2\2\u009c\u00a0\5\30\r"+
		"\2\u009d\u009e\t\6\2\2\u009e\u00a0\5\30\r\2\u009f\u009c\3\2\2\2\u009f"+
		"\u009d\3\2\2\2\u00a0\27\3\2\2\2\u00a1\u00a7\5\34\17\2\u00a2\u00a7\5\32"+
		"\16\2\u00a3\u00a7\5P)\2\u00a4\u00a7\5\36\20\2\u00a5\u00a7\5 \21\2\u00a6"+
		"\u00a1\3\2\2\2\u00a6\u00a2\3\2\2\2\u00a6\u00a3\3\2\2\2\u00a6\u00a4\3\2"+
		"\2\2\u00a6\u00a5\3\2\2\2\u00a7\31\3\2\2\2\u00a8\u00ac\5.\30\2\u00a9\u00ab"+
		"\5\"\22\2\u00aa\u00a9\3\2\2\2\u00ab\u00ae\3\2\2\2\u00ac\u00aa\3\2\2\2"+
		"\u00ac\u00ad\3\2\2\2\u00ad\33\3\2\2\2\u00ae\u00ac\3\2\2\2\u00af\u00b0"+
		"\5.\30\2\u00b0\u00b4\5,\27\2\u00b1\u00b3\5\"\22\2\u00b2\u00b1\3\2\2\2"+
		"\u00b3\u00b6\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b4\u00b5\3\2\2\2\u00b5\35"+
		"\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b7\u00b8\7+\2\2\u00b8\u00b9\5\4\3\2\u00b9"+
		"\u00bd\7,\2\2\u00ba\u00bc\5\"\22\2\u00bb\u00ba\3\2\2\2\u00bc\u00bf\3\2"+
		"\2\2\u00bd\u00bb\3\2\2\2\u00bd\u00be\3\2\2\2\u00be\37\3\2\2\2\u00bf\u00bd"+
		"\3\2\2\2\u00c0\u00c1\7+\2\2\u00c1\u00c2\5\4\3\2\u00c2\u00c3\7,\2\2\u00c3"+
		"\u00c4\5,\27\2\u00c4!\3\2\2\2\u00c5\u00ca\5$\23\2\u00c6\u00ca\5(\25\2"+
		"\u00c7\u00ca\5&\24\2\u00c8\u00ca\5,\27\2\u00c9\u00c5\3\2\2\2\u00c9\u00c6"+
		"\3\2\2\2\u00c9\u00c7\3\2\2\2\u00c9\u00c8\3\2\2\2\u00ca#\3\2\2\2\u00cb"+
		"\u00cc\7\'\2\2\u00cc\u00cd\5\4\3\2\u00cd\u00ce\7(\2\2\u00ce%\3\2\2\2\u00cf"+
		"\u00d0\7;\2\2\u00d0\'\3\2\2\2\u00d1\u00d2\7;\2\2\u00d2\u00d3\5,\27\2\u00d3"+
		")\3\2\2\2\u00d4\u00d8\5\4\3\2\u00d5\u00d6\7-\2\2\u00d6\u00d8\5\4\3\2\u00d7"+
		"\u00d4\3\2\2\2\u00d7\u00d5\3\2\2\2\u00d8+\3\2\2\2\u00d9\u00e2\7+\2\2\u00da"+
		"\u00df\5*\26\2\u00db\u00dc\7.\2\2\u00dc\u00de\5*\26\2\u00dd\u00db\3\2"+
		"\2\2\u00de\u00e1\3\2\2\2\u00df\u00dd\3\2\2\2\u00df\u00e0\3\2\2\2\u00e0"+
		"\u00e3\3\2\2\2\u00e1\u00df\3\2\2\2\u00e2\u00da\3\2\2\2\u00e2\u00e3\3\2"+
		"\2\2\u00e3\u00e5\3\2\2\2\u00e4\u00e6\7,\2\2\u00e5\u00e4\3\2\2\2\u00e5"+
		"\u00e6\3\2\2\2\u00e6-\3\2\2\2\u00e7\u00f6\5\60\31\2\u00e8\u00f6\5:\36"+
		"\2\u00e9\u00f6\7\60\2\2\u00ea\u00f6\7\61\2\2\u00eb\u00f6\7\63\2\2\u00ec"+
		"\u00f6\7\64\2\2\u00ed\u00f6\7\65\2\2\u00ee\u00f1\5> \2\u00ef\u00f1\5<"+
		"\37\2\u00f0\u00ee\3\2\2\2\u00f0\u00ef\3\2\2\2\u00f1\u00f6\3\2\2\2\u00f2"+
		"\u00f6\5@!\2\u00f3\u00f6\5D#\2\u00f4\u00f6\5B\"\2\u00f5\u00e7\3\2\2\2"+
		"\u00f5\u00e8\3\2\2\2\u00f5\u00e9\3\2\2\2\u00f5\u00ea\3\2\2\2\u00f5\u00eb"+
		"\3\2\2\2\u00f5\u00ec\3\2\2\2\u00f5\u00ed\3\2\2\2\u00f5\u00f0\3\2\2\2\u00f5"+
		"\u00f2\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f5\u00f4\3\2\2\2\u00f6/\3\2\2\2"+
		"\u00f7\u0103\7\'\2\2\u00f8\u00fd\5*\26\2\u00f9\u00fa\7.\2\2\u00fa\u00fc"+
		"\5*\26\2\u00fb\u00f9\3\2\2\2\u00fc\u00ff\3\2\2\2\u00fd\u00fb\3\2\2\2\u00fd"+
		"\u00fe\3\2\2\2\u00fe\u0101\3\2\2\2\u00ff\u00fd\3\2\2\2\u0100\u0102\7."+
		"\2\2\u0101\u0100\3\2\2\2\u0101\u0102\3\2\2\2\u0102\u0104\3\2\2\2\u0103"+
		"\u00f8\3\2\2\2\u0103\u0104\3\2\2\2\u0104\u0106\3\2\2\2\u0105\u0107\7("+
		"\2\2\u0106\u0105\3\2\2\2\u0106\u0107\3\2\2\2\u0107\61\3\2\2\2\u0108\u0109"+
		"\5\64\33\2\u0109\u010a\7!\2\2\u010a\u010b\5\4\3\2\u010b\u010f\3\2\2\2"+
		"\u010c\u010d\7-\2\2\u010d\u010f\5\4\3\2\u010e\u0108\3\2\2\2\u010e\u010c"+
		"\3\2\2\2\u010f\63\3\2\2\2\u0110\u0113\58\35\2\u0111\u0113\5\66\34\2\u0112"+
		"\u0110\3\2\2\2\u0112\u0111\3\2\2\2\u0113\65\3\2\2\2\u0114\u0115\7\'\2"+
		"\2\u0115\u0116\5\4\3\2\u0116\u0117\7(\2\2\u0117\67\3\2\2\2\u0118\u0119"+
		"\78\2\2\u0119\u0121\b\35\1\2\u011a\u011b\5@!\2\u011b\u011c\b\35\1\2\u011c"+
		"\u0121\3\2\2\2\u011d\u011e\5<\37\2\u011e\u011f\b\35\1\2\u011f\u0121\3"+
		"\2\2\2\u0120\u0118\3\2\2\2\u0120\u011a\3\2\2\2\u0120\u011d\3\2\2\2\u0121"+
		"9\3\2\2\2\u0122\u012e\7)\2\2\u0123\u0128\5\62\32\2\u0124\u0125\7.\2\2"+
		"\u0125\u0127\5\62\32\2\u0126\u0124\3\2\2\2\u0127\u012a\3\2\2\2\u0128\u0126"+
		"\3\2\2\2\u0128\u0129\3\2\2\2\u0129\u012c\3\2\2\2\u012a\u0128\3\2\2\2\u012b"+
		"\u012d\7.\2\2\u012c\u012b\3\2\2\2\u012c\u012d\3\2\2\2\u012d\u012f\3\2"+
		"\2\2\u012e\u0123\3\2\2\2\u012e\u012f\3\2\2\2\u012f\u0131\3\2\2\2\u0130"+
		"\u0132\7*\2\2\u0131\u0130\3\2\2\2\u0131\u0132\3\2\2\2\u0132;\3\2\2\2\u0133"+
		"\u0134\7\6\2\2\u0134\u013a\b\37\1\2\u0135\u0136\7\4\2\2\u0136\u013a\b"+
		"\37\1\2\u0137\u0138\7\3\2\2\u0138\u013a\b\37\1\2\u0139\u0133\3\2\2\2\u0139"+
		"\u0135\3\2\2\2\u0139\u0137\3\2\2\2\u013a=\3\2\2\2\u013b\u013c\7\5\2\2"+
		"\u013c\u013d\b \1\2\u013d?\3\2\2\2\u013e\u013f\7\b\2\2\u013f\u0140\b!"+
		"\1\2\u0140A\3\2\2\2\u0141\u0142\78\2\2\u0142\u0143\b\"\1\2\u0143C\3\2"+
		"\2\2\u0144\u0145\7\16\2\2\u0145\u0146\b#\1\2\u0146E\3\2\2\2\u0147\u014a"+
		"\5B\"\2\u0148\u0149\7%\2\2\u0149\u014b\5\4\3\2\u014a\u0148\3\2\2\2\u014a"+
		"\u014b\3\2\2\2\u014bG\3\2\2\2\u014c\u014d\7-\2\2\u014d\u014e\5B\"\2\u014e"+
		"I\3\2\2\2\u014f\u015c\7+\2\2\u0150\u0155\5F$\2\u0151\u0152\7.\2\2\u0152"+
		"\u0154\5F$\2\u0153\u0151\3\2\2\2\u0154\u0157\3\2\2\2\u0155\u0153\3\2\2"+
		"\2\u0155\u0156\3\2\2\2\u0156\u015a\3\2\2\2\u0157\u0155\3\2\2\2\u0158\u0159"+
		"\7.\2\2\u0159\u015b\5H%\2\u015a\u0158\3\2\2\2\u015a\u015b\3\2\2\2\u015b"+
		"\u015d\3\2\2\2\u015c\u0150\3\2\2\2\u015c\u015d\3\2\2\2\u015d\u015f\3\2"+
		"\2\2\u015e\u0160\7,\2\2\u015f\u015e\3\2\2\2\u015f\u0160\3\2\2\2\u0160"+
		"\u0167\3\2\2\2\u0161\u0162\7+\2\2\u0162\u0164\5H%\2\u0163\u0165\7,\2\2"+
		"\u0164\u0163\3\2\2\2\u0164\u0165\3\2\2\2\u0165\u0167\3\2\2\2\u0166\u014f"+
		"\3\2\2\2\u0166\u0161\3\2\2\2\u0167K\3\2\2\2\u0168\u016b\5B\"\2\u0169\u016b"+
		"\5J&\2\u016a\u0168\3\2\2\2\u016a\u0169\3\2\2\2\u016bM\3\2\2\2\u016c\u016d"+
		"\5\4\3\2\u016dO\3\2\2\2\u016e\u016f\5L\'\2\u016f\u0170\7$\2\2\u0170\u0171"+
		"\5N(\2\u0171Q\3\2\2\2+Zaiqy\u0081\u0089\u0091\u0099\u009f\u00a6\u00ac"+
		"\u00b4\u00bd\u00c9\u00d7\u00df\u00e2\u00e5\u00f0\u00f5\u00fd\u0101\u0103"+
		"\u0106\u010e\u0112\u0120\u0128\u012c\u012e\u0131\u0139\u014a\u0155\u015a"+
		"\u015c\u015f\u0164\u0166\u016a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}