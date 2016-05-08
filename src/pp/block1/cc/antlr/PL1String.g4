lexer grammar PL1String;

STRING : '"' (~'"'|'""')* '"';