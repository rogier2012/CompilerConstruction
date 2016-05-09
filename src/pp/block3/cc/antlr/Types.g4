grammar Types;

import TypesVocab;

type : type HAT type # hat
     | type PLUS type # plus
     | type EQUALS type # equals
     | LPAR type RPAR # par
     | NUMBER # num
     | BOOLEAN # bool
     | STRING # string
     ;