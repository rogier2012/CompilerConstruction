lexer grammar Decimal;

FLOAT : NUM  (EXPONENT)? ('f'|'F');
DOUBLE: NUM  (EXPONENT)? ('d'|'D')?;

fragment NUM :  DIGIT | ((DIGIT)* '.' (DIGIT)+);
fragment UNDERSCORE : '_';
fragment DIGIT : ('0'..'9') (UNDERSCORE ('0'..'9'))*;
fragment EXPONENT : ('e' | 'E') DIGIT;

WS    : [ \t\r\n]+ -> skip ; // At least one whitespace char; don't make token