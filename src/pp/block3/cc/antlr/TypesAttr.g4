grammar TypesAttr;

import TypesVocab;

@members {

    private Type getType(String text){
       if (text.equals("true") || text.equals("false"){
            return Type.BOOL;
       } else if (text.matches("-?\\d+(\\.\\d+)?")){
            return Type.NUM;
       } else {
            return Type.STR;
       }
    }

    private Type getHat(Type type1,Type type2){
        if (type1 == Type.NUM && type2 == Type.NUM){
            return Type.NUM;
        } else if (type1 == TYPE.STR && type2 == Type.NUM){
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
    : t0 = type HAT e1=type
      {$val = getHat(t0.val,t1.val);}
    | t0 = type PLUS e1=type
      {$val = getPlus(t0.val,t1.val);}
    |t0 = type EQUALS e1=type
      {$val = getEquals(t0.val,t1.val);}
    | LPAR type RPAR
      {$val = type.val;}
    | NUMBER
      {$val = getType($NUMBER.text);}
    | BOOLEAN
      {$val = getType($BOOLEAN.text);}
    | STRING
      {$val = getType($STRING.text);}
    ;