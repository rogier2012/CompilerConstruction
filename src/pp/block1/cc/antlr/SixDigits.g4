lexer grammar SixDigits;

fragment LETTER : 'A'..'Z' | 'a'..'z';
fragment NUMBER : '0'..'9';
fragment ANUM : LETTER | NUMBER;
WORD : LETTER ANUM ANUM ANUM ANUM ANUM ;