grammar Calc;

import CalcVocab;

expr : expr TIMES expr # times
     | expr PLUS expr  # plus
     | MINUS expr      # negate
     | expr MINUS expr # minus
     | LPAR expr RPAR  # par
     | NUMBER          # number
     ;
