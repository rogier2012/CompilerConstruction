grammar Latex;

table : BEGINTABLE arguments NEWLINE (row)* ENDTABLE NEWLINE;
arguments : LBRACK OPTIONS RBRACK;
row :  rowentry ('&' rowentry)* '\\\\' NEWLINE;
rowentry : (|ENTRY);


LBRACK : '{';
RBRACK : '}';
OPTIONS : ('l'|'c'|'r')+;
BEGINTABLE : '\\begin{tabular}';
ENDTABLE : '\\end{tabular}';
ENTRY : ~('\\' | '\n' | '{' |'}' | '$' | '&' | '#' | 'ˆ' | '_' | '~'| '%')+;


KEYWORDS: ('\\' | '{' |'}' | '$' | '&' | '#' | 'ˆ' | '_' | '~̃'| '%');
NEWLINE : ('\r'|'\n')+;
COMMENT : ('%'  ~('\r'|'\n')* ('\r'|'\n')+) -> skip;
