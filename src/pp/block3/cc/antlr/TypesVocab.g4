lexer grammar TypesVocab;


HAT : '^';
PLUS : '+';
EQUALS : '=';
LPAR : '(';
RPAR : ')';


NUMBER : [0-9]+;
BOOLEAN : 'true' | 'false';
STRING : '"' ('\\' ["\\] | ~["\\\r\n])* '"';



// ignore whitespace
WS : [ \t\n\r] -> skip;
