package com.snaplogic.grammars;// Generated from /Users/yliu/IdeaProjects/Snap-document/ExpressionImpl/src/main/antlr4/com/snaplogic/grammars/SnapExpressions.g4 by ANTLR 4.7

import com.snaplogic.expression.BailErrorStrategy;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SnapExpressionsLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"OctNumber", "HexNumber", "Float", "Digit", "FloatDigit", "Exponent", 
		"QuotedString", "NonTerminatedString", "NonTerminatedDoubleString", "MULTICOMMENT", 
		"NonTerminatedComment", "LINECOMMENT", "Regex", "Escape", "EscapeSlash", 
		"OR", "AND", "XOR", "EQUALS", "NOT_EQUALS", "GT", "LT", "GTE", "LTE", 
		"ADD", "SUB", "MULTIPLY", "DIVIDE", "MOD", "NOT", "QMARK", "COLON", "INSTANCEOF", 
		"TYPEOF", "ARROW", "ASSIGN", "IN", "LBRACKET", "RBRACKET", "LCURLY", "RCURLY", 
		"LPAREN", "RPAREN", "ELLIPSIS", "COMMA", "DOT", "True", "False", "New", 
		"Null", "NaN", "Infinity", "OCT_PREFIX", "HEX_PREFIX", "Id", "IdStart", 
		"IdPart", "PropertyRef", "WS"
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


	    // The most recently produced token.
	    private Token lastToken = null;

	    /**
	     * Return the next token from the character stream and records this last
	     * token in case it resides on the default channel. This recorded token
	     * is used to determine when the lexer could possibly match a regex
	     * literal.
	     *
	     * @return the next token from the character stream.
	     */
	    @Override
	    public Token nextToken() {

	        // Get the next token.
	        Token next = super.nextToken();

	        if (next.getChannel() == Token.DEFAULT_CHANNEL) {
	            // Keep track of the last token on the default channel.
	            this.lastToken = next;
	        }

	        return next;
	    }

	    /**
	     * Returns {@code true} iff the lexer can match a regex literal.
	     *
	     * @return {@code true} iff the lexer can match a regex literal.
	     */
	    private boolean isRegexPossible() {

	        if (this.lastToken == null) {
	            // No token has been produced yet: at the start of the input,
	            // no division is possible, so a regex literal _is_ possible.
	            return true;
	        }

	        switch (this.lastToken.getType()) {
	            case Id:
	            case Null:
	            case True:
	            case False:
	            case RCURLY:
	            case RPAREN:
	            case OctNumber:
	            case HexNumber:
	            case Float:
	            case QuotedString:
	            case Digit:
	                // After any of the tokens above, no regex literal can follow.
	                return false;
	            default:
	                // In all other cases, a regex literal _is_ possible.
	                return true;
	        }
	    }


	public SnapExpressionsLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SnapExpressions.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 7:
			NonTerminatedString_action((RuleContext)_localctx, actionIndex);
			break;
		case 8:
			NonTerminatedDoubleString_action((RuleContext)_localctx, actionIndex);
			break;
		case 10:
			NonTerminatedComment_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void NonTerminatedString_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			 BailErrorStrategy.unterminatedString(_input.toString(),
			      _tokenStartCharIndex, _tokenStartLine, _tokenStartCharPositionInLine); 
			break;
		}
	}
	private void NonTerminatedDoubleString_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			 BailErrorStrategy.unterminatedString(_input.toString(),
			      _tokenStartCharIndex, _tokenStartLine, _tokenStartCharPositionInLine); 
			break;
		}
	}
	private void NonTerminatedComment_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:
			 BailErrorStrategy.unterminatedComment(_input.toString(),
			      _tokenStartCharIndex, _tokenStartLine, _tokenStartCharPositionInLine); 
			break;
		}
	}
	@Override
	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 12:
			return Regex_sempred((RuleContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean Regex_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return isRegexPossible();
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2<\u01a2\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\3\2\3"+
		"\2\6\2|\n\2\r\2\16\2}\3\3\3\3\6\3\u0082\n\3\r\3\16\3\u0083\3\4\3\4\3\4"+
		"\3\4\5\4\u008a\n\4\3\4\3\4\3\4\5\4\u008f\n\4\3\5\3\5\3\5\7\5\u0094\n\5"+
		"\f\5\16\5\u0097\13\5\5\5\u0099\n\5\3\6\6\6\u009c\n\6\r\6\16\6\u009d\3"+
		"\7\3\7\3\7\3\7\7\7\u00a4\n\7\f\7\16\7\u00a7\13\7\3\b\3\b\3\b\7\b\u00ac"+
		"\n\b\f\b\16\b\u00af\13\b\3\b\3\b\3\b\3\b\7\b\u00b5\n\b\f\b\16\b\u00b8"+
		"\13\b\3\b\5\b\u00bb\n\b\3\t\3\t\3\t\7\t\u00c0\n\t\f\t\16\t\u00c3\13\t"+
		"\3\t\3\t\3\n\3\n\3\n\7\n\u00ca\n\n\f\n\16\n\u00cd\13\n\3\n\3\n\3\13\3"+
		"\13\3\13\3\13\7\13\u00d5\n\13\f\13\16\13\u00d8\13\13\3\13\3\13\3\13\3"+
		"\13\3\13\3\f\3\f\3\f\3\f\7\f\u00e3\n\f\f\f\16\f\u00e6\13\f\3\f\3\f\3\r"+
		"\3\r\3\r\3\r\7\r\u00ee\n\r\f\r\16\r\u00f1\13\r\3\r\3\r\3\16\3\16\3\16"+
		"\3\16\7\16\u00f9\n\16\f\16\16\16\u00fc\13\16\3\16\3\16\7\16\u0100\n\16"+
		"\f\16\16\16\u0103\13\16\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3"+
		"\22\3\22\3\22\3\23\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3\27\3"+
		"\27\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3"+
		"\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3"+
		"\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3$\3$\3$\3%\3%\3&\3&\3&\3\'\3\'\3(\3("+
		"\3)\3)\3*\3*\3+\3+\3,\3,\3-\3-\3-\3-\3.\3.\3/\3/\3\60\3\60\3\60\3\60\3"+
		"\60\3\61\3\61\3\61\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\63\3\63\3\63\3"+
		"\63\3\63\3\64\3\64\3\64\3\64\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3"+
		"\65\3\66\3\66\3\67\3\67\3\67\3\67\5\67\u0188\n\67\38\38\78\u018c\n8\f"+
		"8\168\u018f\138\39\59\u0192\n9\3:\3:\3;\3;\6;\u0198\n;\r;\16;\u0199\3"+
		"<\6<\u019d\n<\r<\16<\u019e\3<\3<\t\u00ad\u00b6\u00c1\u00cb\u00d6\u00e4"+
		"\u00fa\2=\3\3\5\4\7\5\t\6\13\7\r\2\17\b\21\t\23\n\25\13\27\f\31\r\33\16"+
		"\35\17\37\20!\21#\22%\23\'\24)\25+\26-\27/\30\61\31\63\32\65\33\67\34"+
		"9\35;\36=\37? A!C\"E#G$I%K&M\'O(Q)S*U+W,Y-[.]/_\60a\61c\62e\63g\64i\65"+
		"k\66m\67o8q9s:u;w<\3\2\21\3\2\629\5\2\62;CHch\3\2\63;\3\2\62;\4\2GGgg"+
		"\4\2--//\3\2$$\3\2))\5\2\f\f\17\17\u202a\u202b\3\2\61\61\5\2iikkoo\4\2"+
		"$$))\6\2&&B\\aac|\6\2\62;C\\aac|\5\2\13\f\17\17\"\"\2\u01bb\2\3\3\2\2"+
		"\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\17\3\2\2\2\2\21"+
		"\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2"+
		"\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3"+
		"\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3"+
		"\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3"+
		"\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2"+
		"\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2"+
		"Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3"+
		"\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2"+
		"\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\3y\3\2\2\2\5\177\3\2\2\2\7\u008e\3"+
		"\2\2\2\t\u0098\3\2\2\2\13\u009b\3\2\2\2\r\u009f\3\2\2\2\17\u00ba\3\2\2"+
		"\2\21\u00bc\3\2\2\2\23\u00c6\3\2\2\2\25\u00d0\3\2\2\2\27\u00de\3\2\2\2"+
		"\31\u00e9\3\2\2\2\33\u00f4\3\2\2\2\35\u0104\3\2\2\2\37\u0107\3\2\2\2!"+
		"\u010a\3\2\2\2#\u010d\3\2\2\2%\u0110\3\2\2\2\'\u0112\3\2\2\2)\u0115\3"+
		"\2\2\2+\u0118\3\2\2\2-\u011a\3\2\2\2/\u011c\3\2\2\2\61\u011f\3\2\2\2\63"+
		"\u0122\3\2\2\2\65\u0124\3\2\2\2\67\u0126\3\2\2\29\u0128\3\2\2\2;\u012a"+
		"\3\2\2\2=\u012c\3\2\2\2?\u012e\3\2\2\2A\u0130\3\2\2\2C\u0132\3\2\2\2E"+
		"\u013d\3\2\2\2G\u0144\3\2\2\2I\u0147\3\2\2\2K\u0149\3\2\2\2M\u014c\3\2"+
		"\2\2O\u014e\3\2\2\2Q\u0150\3\2\2\2S\u0152\3\2\2\2U\u0154\3\2\2\2W\u0156"+
		"\3\2\2\2Y\u0158\3\2\2\2[\u015c\3\2\2\2]\u015e\3\2\2\2_\u0160\3\2\2\2a"+
		"\u0165\3\2\2\2c\u016b\3\2\2\2e\u016f\3\2\2\2g\u0174\3\2\2\2i\u0178\3\2"+
		"\2\2k\u0181\3\2\2\2m\u0187\3\2\2\2o\u0189\3\2\2\2q\u0191\3\2\2\2s\u0193"+
		"\3\2\2\2u\u0195\3\2\2\2w\u019c\3\2\2\2y{\5k\66\2z|\t\2\2\2{z\3\2\2\2|"+
		"}\3\2\2\2}{\3\2\2\2}~\3\2\2\2~\4\3\2\2\2\177\u0081\5m\67\2\u0080\u0082"+
		"\t\3\2\2\u0081\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0081\3\2\2\2\u0083"+
		"\u0084\3\2\2\2\u0084\6\3\2\2\2\u0085\u0086\5\t\5\2\u0086\u0087\5]/\2\u0087"+
		"\u0089\5\13\6\2\u0088\u008a\5\r\7\2\u0089\u0088\3\2\2\2\u0089\u008a\3"+
		"\2\2\2\u008a\u008f\3\2\2\2\u008b\u008c\5\t\5\2\u008c\u008d\5\r\7\2\u008d"+
		"\u008f\3\2\2\2\u008e\u0085\3\2\2\2\u008e\u008b\3\2\2\2\u008f\b\3\2\2\2"+
		"\u0090\u0099\7\62\2\2\u0091\u0095\t\4\2\2\u0092\u0094\t\5\2\2\u0093\u0092"+
		"\3\2\2\2\u0094\u0097\3\2\2\2\u0095\u0093\3\2\2\2\u0095\u0096\3\2\2\2\u0096"+
		"\u0099\3\2\2\2\u0097\u0095\3\2\2\2\u0098\u0090\3\2\2\2\u0098\u0091\3\2"+
		"\2\2\u0099\n\3\2\2\2\u009a\u009c\t\5\2\2\u009b\u009a\3\2\2\2\u009c\u009d"+
		"\3\2\2\2\u009d\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e\f\3\2\2\2\u009f"+
		"\u00a0\t\6\2\2\u00a0\u00a1\t\7\2\2\u00a1\u00a5\t\4\2\2\u00a2\u00a4\t\5"+
		"\2\2\u00a3\u00a2\3\2\2\2\u00a4\u00a7\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a5"+
		"\u00a6\3\2\2\2\u00a6\16\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a8\u00ad\7$\2\2"+
		"\u00a9\u00ac\5\35\17\2\u00aa\u00ac\n\b\2\2\u00ab\u00a9\3\2\2\2\u00ab\u00aa"+
		"\3\2\2\2\u00ac\u00af\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ad\u00ab\3\2\2\2\u00ae"+
		"\u00b0\3\2\2\2\u00af\u00ad\3\2\2\2\u00b0\u00bb\7$\2\2\u00b1\u00b6\7)\2"+
		"\2\u00b2\u00b5\5\35\17\2\u00b3\u00b5\n\t\2\2\u00b4\u00b2\3\2\2\2\u00b4"+
		"\u00b3\3\2\2\2\u00b5\u00b8\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b6\u00b4\3\2"+
		"\2\2\u00b7\u00b9\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b9\u00bb\7)\2\2\u00ba"+
		"\u00a8\3\2\2\2\u00ba\u00b1\3\2\2\2\u00bb\20\3\2\2\2\u00bc\u00c1\7)\2\2"+
		"\u00bd\u00c0\5\35\17\2\u00be\u00c0\n\t\2\2\u00bf\u00bd\3\2\2\2\u00bf\u00be"+
		"\3\2\2\2\u00c0\u00c3\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c1\u00bf\3\2\2\2\u00c2"+
		"\u00c4\3\2\2\2\u00c3\u00c1\3\2\2\2\u00c4\u00c5\b\t\2\2\u00c5\22\3\2\2"+
		"\2\u00c6\u00cb\7$\2\2\u00c7\u00ca\5\35\17\2\u00c8\u00ca\n\b\2\2\u00c9"+
		"\u00c7\3\2\2\2\u00c9\u00c8\3\2\2\2\u00ca\u00cd\3\2\2\2\u00cb\u00cc\3\2"+
		"\2\2\u00cb\u00c9\3\2\2\2\u00cc\u00ce\3\2\2\2\u00cd\u00cb\3\2\2\2\u00ce"+
		"\u00cf\b\n\3\2\u00cf\24\3\2\2\2\u00d0\u00d1\7\61\2\2\u00d1\u00d2\7,\2"+
		"\2\u00d2\u00d6\3\2\2\2\u00d3\u00d5\13\2\2\2\u00d4\u00d3\3\2\2\2\u00d5"+
		"\u00d8\3\2\2\2\u00d6\u00d7\3\2\2\2\u00d6\u00d4\3\2\2\2\u00d7\u00d9\3\2"+
		"\2\2\u00d8\u00d6\3\2\2\2\u00d9\u00da\7,\2\2\u00da\u00db\7\61\2\2\u00db"+
		"\u00dc\3\2\2\2\u00dc\u00dd\b\13\4\2\u00dd\26\3\2\2\2\u00de\u00df\7\61"+
		"\2\2\u00df\u00e0\7,\2\2\u00e0\u00e4\3\2\2\2\u00e1\u00e3\13\2\2\2\u00e2"+
		"\u00e1\3\2\2\2\u00e3\u00e6\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e4\u00e2\3\2"+
		"\2\2\u00e5\u00e7\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e7\u00e8\b\f\5\2\u00e8"+
		"\30\3\2\2\2\u00e9\u00ea\7\61\2\2\u00ea\u00eb\7\61\2\2\u00eb\u00ef\3\2"+
		"\2\2\u00ec\u00ee\n\n\2\2\u00ed\u00ec\3\2\2\2\u00ee\u00f1\3\2\2\2\u00ef"+
		"\u00ed\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0\u00f2\3\2\2\2\u00f1\u00ef\3\2"+
		"\2\2\u00f2\u00f3\b\r\4\2\u00f3\32\3\2\2\2\u00f4\u00f5\6\16\2\2\u00f5\u00fa"+
		"\7\61\2\2\u00f6\u00f9\5\37\20\2\u00f7\u00f9\n\13\2\2\u00f8\u00f6\3\2\2"+
		"\2\u00f8\u00f7\3\2\2\2\u00f9\u00fc\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fa\u00f8"+
		"\3\2\2\2\u00fb\u00fd\3\2\2\2\u00fc\u00fa\3\2\2\2\u00fd\u0101\7\61\2\2"+
		"\u00fe\u0100\t\f\2\2\u00ff\u00fe\3\2\2\2\u0100\u0103\3\2\2\2\u0101\u00ff"+
		"\3\2\2\2\u0101\u0102\3\2\2\2\u0102\34\3\2\2\2\u0103\u0101\3\2\2\2\u0104"+
		"\u0105\7^\2\2\u0105\u0106\t\r\2\2\u0106\36\3\2\2\2\u0107\u0108\7^\2\2"+
		"\u0108\u0109\7\61\2\2\u0109 \3\2\2\2\u010a\u010b\7~\2\2\u010b\u010c\7"+
		"~\2\2\u010c\"\3\2\2\2\u010d\u010e\7(\2\2\u010e\u010f\7(\2\2\u010f$\3\2"+
		"\2\2\u0110\u0111\7`\2\2\u0111&\3\2\2\2\u0112\u0113\7?\2\2\u0113\u0114"+
		"\7?\2\2\u0114(\3\2\2\2\u0115\u0116\7#\2\2\u0116\u0117\7?\2\2\u0117*\3"+
		"\2\2\2\u0118\u0119\7@\2\2\u0119,\3\2\2\2\u011a\u011b\7>\2\2\u011b.\3\2"+
		"\2\2\u011c\u011d\7@\2\2\u011d\u011e\7?\2\2\u011e\60\3\2\2\2\u011f\u0120"+
		"\7>\2\2\u0120\u0121\7?\2\2\u0121\62\3\2\2\2\u0122\u0123\7-\2\2\u0123\64"+
		"\3\2\2\2\u0124\u0125\7/\2\2\u0125\66\3\2\2\2\u0126\u0127\7,\2\2\u0127"+
		"8\3\2\2\2\u0128\u0129\7\61\2\2\u0129:\3\2\2\2\u012a\u012b\7\'\2\2\u012b"+
		"<\3\2\2\2\u012c\u012d\7#\2\2\u012d>\3\2\2\2\u012e\u012f\7A\2\2\u012f@"+
		"\3\2\2\2\u0130\u0131\7<\2\2\u0131B\3\2\2\2\u0132\u0133\7k\2\2\u0133\u0134"+
		"\7p\2\2\u0134\u0135\7u\2\2\u0135\u0136\7v\2\2\u0136\u0137\7c\2\2\u0137"+
		"\u0138\7p\2\2\u0138\u0139\7e\2\2\u0139\u013a\7g\2\2\u013a\u013b\7q\2\2"+
		"\u013b\u013c\7h\2\2\u013cD\3\2\2\2\u013d\u013e\7v\2\2\u013e\u013f\7{\2"+
		"\2\u013f\u0140\7r\2\2\u0140\u0141\7g\2\2\u0141\u0142\7q\2\2\u0142\u0143"+
		"\7h\2\2\u0143F\3\2\2\2\u0144\u0145\7?\2\2\u0145\u0146\7@\2\2\u0146H\3"+
		"\2\2\2\u0147\u0148\7?\2\2\u0148J\3\2\2\2\u0149\u014a\7k\2\2\u014a\u014b"+
		"\7p\2\2\u014bL\3\2\2\2\u014c\u014d\7]\2\2\u014dN\3\2\2\2\u014e\u014f\7"+
		"_\2\2\u014fP\3\2\2\2\u0150\u0151\7}\2\2\u0151R\3\2\2\2\u0152\u0153\7\177"+
		"\2\2\u0153T\3\2\2\2\u0154\u0155\7*\2\2\u0155V\3\2\2\2\u0156\u0157\7+\2"+
		"\2\u0157X\3\2\2\2\u0158\u0159\7\60\2\2\u0159\u015a\7\60\2\2\u015a\u015b"+
		"\7\60\2\2\u015bZ\3\2\2\2\u015c\u015d\7.\2\2\u015d\\\3\2\2\2\u015e\u015f"+
		"\7\60\2\2\u015f^\3\2\2\2\u0160\u0161\7v\2\2\u0161\u0162\7t\2\2\u0162\u0163"+
		"\7w\2\2\u0163\u0164\7g\2\2\u0164`\3\2\2\2\u0165\u0166\7h\2\2\u0166\u0167"+
		"\7c\2\2\u0167\u0168\7n\2\2\u0168\u0169\7u\2\2\u0169\u016a\7g\2\2\u016a"+
		"b\3\2\2\2\u016b\u016c\7p\2\2\u016c\u016d\7g\2\2\u016d\u016e\7y\2\2\u016e"+
		"d\3\2\2\2\u016f\u0170\7p\2\2\u0170\u0171\7w\2\2\u0171\u0172\7n\2\2\u0172"+
		"\u0173\7n\2\2\u0173f\3\2\2\2\u0174\u0175\7P\2\2\u0175\u0176\7c\2\2\u0176"+
		"\u0177\7P\2\2\u0177h\3\2\2\2\u0178\u0179\7K\2\2\u0179\u017a\7p\2\2\u017a"+
		"\u017b\7h\2\2\u017b\u017c\7k\2\2\u017c\u017d\7p\2\2\u017d\u017e\7k\2\2"+
		"\u017e\u017f\7v\2\2\u017f\u0180\7{\2\2\u0180j\3\2\2\2\u0181\u0182\7\62"+
		"\2\2\u0182l\3\2\2\2\u0183\u0184\7\62\2\2\u0184\u0188\7z\2\2\u0185\u0186"+
		"\7\62\2\2\u0186\u0188\7Z\2\2\u0187\u0183\3\2\2\2\u0187\u0185\3\2\2\2\u0188"+
		"n\3\2\2\2\u0189\u018d\5q9\2\u018a\u018c\5s:\2\u018b\u018a\3\2\2\2\u018c"+
		"\u018f\3\2\2\2\u018d\u018b\3\2\2\2\u018d\u018e\3\2\2\2\u018ep\3\2\2\2"+
		"\u018f\u018d\3\2\2\2\u0190\u0192\t\16\2\2\u0191\u0190\3\2\2\2\u0192r\3"+
		"\2\2\2\u0193\u0194\t\17\2\2\u0194t\3\2\2\2\u0195\u0197\5]/\2\u0196\u0198"+
		"\5s:\2\u0197\u0196\3\2\2\2\u0198\u0199\3\2\2\2\u0199\u0197\3\2\2\2\u0199"+
		"\u019a\3\2\2\2\u019av\3\2\2\2\u019b\u019d\t\20\2\2\u019c\u019b\3\2\2\2"+
		"\u019d\u019e\3\2\2\2\u019e\u019c\3\2\2\2\u019e\u019f\3\2\2\2\u019f\u01a0"+
		"\3\2\2\2\u01a0\u01a1\b<\4\2\u01a1x\3\2\2\2\37\2}\u0083\u0089\u008e\u0095"+
		"\u0098\u009d\u00a5\u00ab\u00ad\u00b4\u00b6\u00ba\u00bf\u00c1\u00c9\u00cb"+
		"\u00d6\u00e4\u00ef\u00f8\u00fa\u0101\u0187\u018d\u0191\u0199\u019e\6\3"+
		"\t\2\3\n\3\2\3\2\3\f\4";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}