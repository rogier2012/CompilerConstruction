grammar Latex;


table : BEGINTABLE arguments '\n' (rows)* ENDTABLE '\n';
arguments : LBRACK ('l'|'c'|'r')* RBRACK;
rows :  entry ('&' entry)* '\\\\' '\n';
entry : (~(KEYWORDS))+;

LBRACK : '{';
RBRACK : '}';

BEGINTABLE : '\\begin{tabular}';
ENDTABLE : '\\end{tabular}';

KEYWORDS: ('\\' | '{' |'}' | '$' | '&' | '#' | 'ˆ' | '_' | '~̃'| '%');
COMMENT : ('%'  ~('\r'|'\n')* ('\r'|'\n')) -> skip;

WS : [ \t]+ -> skip;