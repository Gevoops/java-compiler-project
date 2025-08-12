import static compiler.ParserSym.*;
import java_cup.runtime.*;


%%

%public
%class Lexer
%unicode
%cup
%line
%column

%{
    private Symbol Symbol1(int type, Object val) {
      return new Symbol(type, yyline + 1, yycolumn, val);
    }

%}

DIGIT = [0-9]
LETTER = [a-zA-Z]

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]


%%

{WhiteSpace}+ {/*  skip   */}

"/*"([^*]*\*+)*([^*/][^*]*\*+)*"/" { /* skip comments , multiline */}

"(" {return  Symbol1(LPAREN, yytext());}

")" {return  Symbol1(RPAREN, yytext());}

"{" {return  Symbol1(LBRACE, yytext());}

"}" {return  Symbol1(RBRACE, yytext());}

"," {return  Symbol1(COMMA, yytext());}

":" {return  Symbol1(COLON, yytext());}

";" {return  Symbol1(SCOLON, yytext());}

"=" {return  Symbol1(EQUALS, yytext());}

"||" {return  Symbol1(OR, yytext());}

"&&" {return  Symbol1(AND, yytext());}

"!" {return  Symbol1(NOT, yytext());}

"==" | "!=" | "<" | ">" | ">=" | "<=" {return  Symbol1(RELOP, yytext());}

"+" | "-" {return  Symbol1(ADDOP, yytext());}

"*" | "/" {return  Symbol1(MULOP, yytext());}

"break" {return  Symbol1(BREAK, yytext());}

"case" {return  Symbol1(CASE, yytext());}

"default" {return  Symbol1(DEFAULT, yytext());}

"else" {return  Symbol1(ELSE, yytext());}

"float" {return  Symbol1(FLOAT, yytext());}

"if" {return  Symbol1(IF, yytext());}

"input" {return  Symbol1(INPUT, yytext());}

"int" {return  Symbol1(INT, yytext());}

"output" {return  Symbol1(OUTPUT, yytext());}

"switch" {return  Symbol1(SWITCH, yytext());}

"while" {return  Symbol1(WHILE, yytext());}

{LETTER}({LETTER} | {DIGIT})* {return  Symbol1(ID, yytext());}

"-"?{DIGIT}+            {return  Symbol1(NUM, yytext());}
"-"?{DIGIT}+"."{DIGIT}* {return  Symbol1(NUM, yytext());}

cast<int> | cast<float>  {return  Symbol1(CAST,yytext());}

