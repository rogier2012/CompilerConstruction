grammar Arithmetic;

goal : expr;

expr :  expr '+' term
       | expr '-' term
       | term;


term : term '*' exponent
     | term '/' exponent
     | exponent;

exponent : <assoc=right > factor '^' exponent
     | factor;


factor : '(' expr ')'
        | NUM;



NUM : ('1'..'9' ('0'..'9')* | '0');

WS : [ \t\n\r] -> skip;