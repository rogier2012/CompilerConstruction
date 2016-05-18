grammar Indecrement;

expr :  expr '+' f
        | f
        ;

f       : '-' f
        | '-''-'f
        | t
        ;

t       : t '+''+'
        | ID
        ;

ID      : [a-z]+;

WS      : [ \t\r\n]+ -> skip ; // At least one whitespace char; don't make token