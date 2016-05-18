grammar Emote;


/** Start symbol: main text. */
text : item* EOF;

/** Item in a text. */
item : WORD EXCL?   #wordItem  // word with optional exclamation
     | '(' item* ')' #textItem  // text in new scope
     | '^' WORD     #raiseItem // intensity-raising declaration
     ;

/** Word token. */
WORD : [a-z]+;
/** Exclamation sequence. */
EXCL : [!?]+;
/** Whitespace. */
WS : [ \t\n\r] -> skip;
