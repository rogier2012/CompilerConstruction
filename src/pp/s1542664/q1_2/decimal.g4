lexer grammar Decimal;

/** Float : A number with an optional exponent and postfixed by a 'f' or 'F' */
FLOAT               : NUM  (EXPONENT)? ('f'|'F') ;

/** Double : A number with an optional exponent and postfixed by a 'd' or 'D' */
DOUBLE              : NUM  (EXPONENT)? ('d'|'D')? ;

/** Num : A number which is either a natural or a real number */
fragment NUM        : NATURAL | REAL ;

/** Real number */
fragment REAL       : ((DIGIT)* '.' (DIGIT)+) ;

/** Natural number*/
fragment NATURAL    : DIGIT ;

fragment UNDERSCORE : '_' ;

/** Digit : Can be separated by an underscore */
fragment DIGIT      : ('0'..'9') (UNDERSCORE* ('0'..'9'))* ;
fragment EXPONENT   : ('e' | 'E') DIGIT ;

WS                  : [ \t\r\n]+ -> skip ;
// At least one whitespace char; don't make token

