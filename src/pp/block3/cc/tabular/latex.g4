grammar Latex;

table : BEGINTABLE arguments '\n'+ (row)* ENDTABLE '\n'+;
arguments : LBRACK OPTIONS RBRACK;
row :  (|ENTRY) ('&' (|ENTRY))* '\\\\' '\n'+;

LBRACK : '{';
RBRACK : '}';
OPTIONS : ('l'|'c'|'r')+;
BEGINTABLE : '\\begin{tabular}';
ENDTABLE : '\\end{tabular}';
ENTRY : ~('\\' | '\n' | '{' |'}' | '$' | '&' | '#' | 'ˆ' | '_' | '~'| '%')+;


KEYWORDS: ('\\' | '{' |'}' | '$' | '&' | '#' | 'ˆ' | '_' | '~̃'| '%');
COMMENT : ('%'  ~('\r'|'\n')* ('\r'|'\n')+) -> skip;
