lexer grammar SimpleExprLexer;

ID      : [a-zA-Z]+ ;
INT     : [0-9]+ ;
NEWLINE : ';' | ('\r'? '\n') ;
WS      : [ \t]+ -> skip ;

MUL     : '*' ;
DIV     : '/' ;
ADD     : '+' ;
SUB     : '-' ;

LPAR    : '(' ;
RPAR    : ')' ;

ASSIGN  : '=' ;
