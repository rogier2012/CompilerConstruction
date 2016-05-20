grammar Number;

/** Number: sequence of digits optionally preceded by a base prefix */
num     : prf seq  #prefixNum
        | seq       #normalNum
        ;

/** Sequence of digits */
seq     : dig       #singleDig
        | dig seq   #seqDig
        ;

/** Prefix: x stands for hexadecimal, b for binary */
prf     : 'x'       #hex
        | 'b'       #bin
        ;

/** Single digit (hexadecimal range) */
dig     : DIGIT ;

/** Digit token */
DIGIT   : [0-9A-F];
