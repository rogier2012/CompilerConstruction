grammar IndecrementLL;

expr :  f exprP
        ;

exprP : PLUS f exprP
        |
        ;

f       : MINUS MINUS f
        | t
        ;

t       : ID tP
        ;

tP      : PLUS PLUS tP
        |
        ;

ID      : [a-z]+;
PLUS    : '+';
MINUS   : '-';

WS      : [ \t\r\n]+ -> skip ; // At least one whitespace char; don't make token