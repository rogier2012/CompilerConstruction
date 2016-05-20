grammar NumberAttr;

@members{
//    private int base = 0;
}

/** Number: sequence of digits optionally preceded by a base prefix */
num returns [ int val ]
    : p=prf s=seq[$p.base]
      { $val = $s.val;
        }
    | s=seq[10]
      {
        $val = $s.val;
        }
    ;

/** Sequence of digits */
prf returns [ int base]
    : HEX
      { $base = 16;}
    | BIN
      { $base = 2;}
    ;

/** Prefix: x stands for hexadecimal, b for binary */
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

/** Single digit (hexadecimal range) */
dig returns [ int val ]
    : DIGIT
      { $val = Integer.parseInt($DIGIT().getText(), 16 ); ; }
    ;


/** Digit token */
DIGIT: [0-9A-F];

/** Binary prefix */
BIN: 'b';

/** Hexadecimal prefix */
HEX: 'x';