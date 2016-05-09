grammar Arithmetic;

goal : expr;

expr :  expr '+' term # plusrule
       | expr '-' term # minusrule
       | term # singleruleexpr
        ;

term : term '*' exponent # multiplyrule
     | term '/' exponent # dividerule
     | exponent # singleruleterm
     ;

exponent : <assoc=right > factor '^' exponent # powerrule
     | factor # singleruleexponent
     ;


factor : '(' expr ')'
        | NUM;



NUM : ('1'..'9' ('0'..'9')* | '0');

WS : [ \t\n\r] -> skip;