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

grammar SnapExpressions;

@header {
import java.math.BigInteger;
import java.math.BigDecimal;

import com.snaplogic.api.ExecutionException;

import com.snaplogic.expression.BailErrorStrategy;
import com.snaplogic.expression.Regex;
import com.snaplogic.expression.util.LiteralUtils;
}

@lexer::members {
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
}

//Entry point
eval
    :   exp
    ;

exp
    :  logicalOr ( QMARK logicalOr COLON logicalOr )?
    ;

logicalOr
    :   logicalAnd ( OR logicalAnd )*
    ;

logicalAnd
    :   logicalXor ( AND logicalXor )*
    ;

logicalXor
    :   inOperator ( XOR inOperator )*
    ;

inOperator
    :   equality ( IN equality )*
    ;

equality
    :   relational ( op=( EQUALS | NOT_EQUALS ) relational )*
    ;

relational
    :   addition ( op=( GT | LT | GTE | LTE | INSTANCEOF ) addition )*
    ;

addition
    :   multiplication ( (ADD | SUB ) multiplication ) *
    ;

multiplication
    :   unary ( op=( MULTIPLY | DIVIDE | MOD ) unary)*
    ;

unary
    :   atomicExp
    |   op=(SUB | ADD | NOT | TYPEOF) atomicExp
    ;

atomicExp
    :   functionCall
    |   property
    |   arrowFunction
    |   parens
    |   parensCall
    ;

property: atomic (memberSuffix)*;

functionCall: atomic arguments (memberSuffix)*;

parens: LPAREN exp RPAREN (memberSuffix)*;

parensCall: LPAREN exp RPAREN arguments;

memberSuffix
    :   indexSuffix
    |   method
    |   propertyRef
    |   arguments
    ;

indexSuffix
    :   LBRACKET exp RBRACKET
    ;
propertyRef
    :   PropertyRef
    ;
method
    :   PropertyRef arguments
    ;

expOrSpread
    : exp
    | ELLIPSIS exp
    ;

arguments
    : LPAREN (args+=expOrSpread (COMMA args+=expOrSpread)*)? RPAREN?
    ;

atomic
    :   array               # Arr
    |   objliteral          # ObjectLiteral
    |   True                # True
    |   False               # False
    |   Null                # Null
    |   NaN                 # NaN
    |   Infinity            # Infinity
    |   (fpnum | integer)   # Number
    |   qstring             # String
    |   jsregex             # Regex
    |   idstring            # Id
    ;

array : LBRACKET (expOrSpread (COMMA expOrSpread)* COMMA?)? RBRACKET?;

propertyNameAndValue
    : propertyNameOrExpr ':' exp
    | ELLIPSIS exp
    ;

propertyNameOrExpr : propertyName | propertyNameExpr;

propertyNameExpr : LBRACKET exp RBRACKET;

propertyName returns [String value]
    : Id
      { $value = $Id.text; }
    | qstring
      { $value = $qstring.value; }
    | integer
      { $value = $integer.value.toString(); }
    ;

objliteral : LCURLY (propertyNameAndValue (COMMA propertyNameAndValue)* COMMA?)? RCURLY?;

// We convert the integer literals during lexing/parsing so that we don't have to do it
// everytime we evaluate the expression.
integer returns [BigInteger value]
    :   Digit
        { $value = new BigInteger($Digit.text); }
        # DecimalNumber
    |   HexNumber
        { $value = new BigInteger($HexNumber.text.substring(2), 16); }
        # HexNumber
    |   OctNumber
        { $value = new BigInteger($OctNumber.text.substring(1), 8); }
        # OctalNumber
    ;
fpnum returns [BigDecimal value]
    :   Float
        { $value = new BigDecimal($Float.text); }
    ;

qstring returns [String value]
    :   QuotedString { $value = LiteralUtils.unescapeString($QuotedString.text); }
    ;

idstring returns [String original, String remaining]
    :   Id {
               $original = $Id.text;
               if ($Id.text.startsWith("\$")) {
                   $remaining = $Id.text.substring(1);
               } else {
                   $remaining = null;
               }
           }
    ;

jsregex returns [Regex value]
    :   Regex {
        int lastSlash = $Regex.text.lastIndexOf("/");
        String pattern = $Regex.text.substring(1, lastSlash);
        String flags = $Regex.text.substring(lastSlash + 1);
        $value = LiteralUtils.compileRegex(pattern, flags);
    }
    ;

parameterWithDefault
    : idstring (ASSIGN exp)?
    ;

restParameters
    : ELLIPSIS idstring
    ;

coverParenthesisedExpressionAndArrowParameterList
    : LPAREN (parameterWithDefault (COMMA parameterWithDefault)* (COMMA restParameters)?)? RPAREN?
    | LPAREN restParameters RPAREN?
    ;

arrowParameters
    : idstring
    | coverParenthesisedExpressionAndArrowParameterList
    ;

conciseBody
    : exp
    ;

arrowFunction
    : arrowParameters ARROW conciseBody
    ;

OctNumber: OCT_PREFIX [0-7]+;
HexNumber: HEX_PREFIX [0-9A-Fa-f]+;
Float
    : Digit DOT FloatDigit Exponent?
    | Digit Exponent
    ;
Digit: '0' | [1-9][0-9]*;
FloatDigit: [0-9]+;
fragment Exponent: ('e' | 'E') [+\-] [1-9][0-9]*;
QuotedString
  : '"' (Escape | ~( '"' ))*? '"'
	| '\'' (Escape | ~( '\'' ))*? '\''
	;
NonTerminatedString
  : '\'' (Escape | ~( '\'' ))*? { BailErrorStrategy.unterminatedString(_input.toString(),
      _tokenStartCharIndex, _tokenStartLine, _tokenStartCharPositionInLine); }
  ;
NonTerminatedDoubleString
  : '"' (Escape | ~( '"' ))*? { BailErrorStrategy.unterminatedString(_input.toString(),
      _tokenStartCharIndex, _tokenStartLine, _tokenStartCharPositionInLine); }
  ;
MULTICOMMENT : '/*' .*? '*/' -> channel(HIDDEN);
NonTerminatedComment
  : '/*' .*? { BailErrorStrategy.unterminatedComment(_input.toString(),
      _tokenStartCharIndex, _tokenStartLine, _tokenStartCharPositionInLine); }
  ;
LINECOMMENT : '//' ~[\r\n\u2028\u2029]* -> channel(HIDDEN);
Regex: {isRegexPossible()}? '/' (EscapeSlash | ~( '/' ))*? '/'[igm]*;

Escape: '\\'(["']);
EscapeSlash: '\\/';

//operators
OR          : '||';
AND         : '&&';
XOR         : '^';
EQUALS      : '==';
NOT_EQUALS  : '!=';
GT          : '>';
LT          : '<';
GTE         : '>=';
LTE         : '<=';
ADD         : '+';
SUB         : '-';
MULTIPLY    : '*';
DIVIDE      : '/';
MOD         : '%';
NOT         : '!';
QMARK       : '?';
COLON       : ':';
INSTANCEOF  : 'instanceof';
TYPEOF      : 'typeof';
ARROW       : '=>';
ASSIGN      : '=';
IN          : 'in';

//BRACKETS
LBRACKET    : '[';
RBRACKET    : ']';

//CURLIES
LCURLY      : '{';
RCURLY      : '}';

//PARENS
LPAREN      : '(';
RPAREN      : ')';

ELLIPSIS    : '...';

//comma
COMMA       : ',';
DOT         : '.';

//boolean values
True        : 'true';
False       : 'false';

//New Object
New         : 'new';

//null
Null: 'null';
NaN: 'NaN';
Infinity: 'Infinity';
//Number prefix
OCT_PREFIX   : '0';
HEX_PREFIX   : '0x' | '0X';
//Ids
Id: IdStart IdPart*;
IdStart: '$' | '@' | '_' | [a-zA-Z];
IdPart: [a-zA-Z0-9_];
PropertyRef: DOT IdPart+;
// skip whitespace
WS : [ \t\r\n]+ -> channel(HIDDEN);
