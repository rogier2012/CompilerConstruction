grammar TypesAttr;

import TypesVocab;

@members {

    private Type getHat(Type type1,Type type2){
        if (type1 == Type.NUM && type2 == Type.NUM){
            return Type.NUM;
        } else if (type1 == Type.STR && type2 == Type.NUM){
            return Type.STR;
        } else {
            return Type.ERR;
        }
    }

    private Type getPlus(Type type1,Type type2){
        if (type1 == Type.NUM && type2 == Type.NUM){
            return Type.NUM;
        } else if (type1 == Type.BOOL && type2 == Type.BOOL){
            return Type.BOOL;
        } else if (type1 == Type.STR){
            return Type.STR;
        } else if (type2 == Type.STR){
            return Type.STR;
        } else {
            return Type.ERR;
        }
    }

    private Type getEquals(Type type1,Type type2){
        if (type1 == type2){
            return Type.BOOL;
        } else {
            return Type.ERR;
        }
     }


}

type  returns [ Type val ]
    : t0 = type HAT t1=type
      {$val = getHat($t0.val,$t1.val);}
    | t0 = type PLUS t1=type
      {$val = getPlus($t0.val,$t1.val);}
    | t0 = type EQUALS t1=type
      {$val = getEquals($t0.val,$t1.val);}
    | LPAR t0 = type RPAR
      {$val = $t0.val;}
    | NUMBER
      {$val = Type.NUM;}
    | BOOLEAN
      {$val = Type.BOOL;}
    | STRING
      {$val = Type.STR;}
    ;