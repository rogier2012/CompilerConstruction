grammar NumberAttr;

@members{
//    private int base = 0;
}

num returns [ int val ]
    : p=prf s=seq[$p.base]
      { $val = $s.val;
        }
    | s=seq[10]
      {
        $val = $s.val;
        }
    ;

prf returns [ int base]
    : HEX
      { $base = 16;}
    | BIN
      { $base = 2;}
    ;

seq[ int base ] returns [ int val, int position ]
    : d=dig
      { $position = 0;
        $val = $d.val* (int)Math.pow($base,$position);

        }
    | d=dig s=seq[$base]
      { $position = $s.position + 1;
      $val = $s.val + ($d.val* (int)Math.pow($base,$position));

        }
    ;

dig returns [ int val ]
    : DIGIT
      { char c = $DIGIT.getText().charAt(0);
        $val = Integer.parseInt($DIGIT().getText(), 16 ); ; }
    ;


/** Digit token */
DIGIT: [0-9A-F];
BIN: 'b';
HEX: 'x';