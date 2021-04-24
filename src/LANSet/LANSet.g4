/*  LANS Lexer and Parser
*   MADE BY: Marc Cane Salamia and Enric Rodr�guez Galan
*/

grammar LANSet;

@header{
    import java.util.Vector;
    import LANSet.Bytecode.*;
}

@parser::members{}

//////////////////// FRAGMENTS ////////////////////

fragment DIGIT: '0'..'9';
fragment LETTER: 'a'..'z' | 'A'..'Z';
fragment DECIMAL: TK_INTEGER '.' DIGIT+ ;
fragment SINGLE_CHAR: '\u0020'..'\u0021' | '\u0023'..'\u007E' ;

///////////////////////////////////////////////////

//////////////////// KEYWORDS ////////////////////

KW_PROGRAM: 'programa';
KW_ENDPROGRAM: 'fprograma';

KW_TYPEBLOCK: 'tipus';
KW_ENDTYPEBLOCK: 'ftipus';
KW_VARIABLEBLOCK: 'variables';
KW_ENDVARIABLEBLOCK: 'fvariables';
KW_CONSTBLOCK: 'constants';
KW_ENDCONSTBLOCK: 'fconstants';

KW_FUNCTION: 'funcio';
KW_ENDFUNCTION: 'ffuncio';
KW_ACTION: 'accio';
KW_ENDACTION: 'faccio';
KW_TUPLE: 'tupla';
KW_ENDTUPLE: 'ftupla';
KW_VECTOR: 'vector';

KW_SIZE: 'mida';
KW_START: 'inici';

KW_RETURN: 'retorna';
KW_IN: 'ent';
KW_INOUT: 'entsor';

KW_DO: 'fer';
KW_IF: 'si';
KW_THEN: 'llavors';
KW_ELSE: 'altrament';
KW_ENDIF: 'fsi';

KW_FOR: 'per';
KW_ENDFOR: 'fper';
KW_FROM: 'de';
KW_TO: 'fins';

KW_WHILE: 'mentre';
KW_ENDWHILE: 'fmentre';

KW_NO: 'no';

KW_INPUT: 'llegir';
KW_OUTPUT: 'escriure';
KW_OUTPUTLN: 'escriureln';

///////////////////////////////////////////////////

////////////////// GENERAL TOKENS /////////////////

TK_BASETYPE: 'car' | 'enter' | 'real' | 'boolea'; //If it's a custom type, it will be an TK_IDENTIFIER (careful)

TK_INTEGER: '1'..'9' DIGIT* | '0' ;
TK_CHARACTER: '\'' SINGLE_CHAR '\'' ;
TK_BOOLEAN: 'cert' | 'fals';
TK_REAL: DECIMAL | DECIMAL 'E' ('-')? DIGIT+ ;
TK_STRING: '"' SINGLE_CHAR* '"';

TK_IDENTIFIER: LETTER ('_' | LETTER | DIGIT)*;
//TK_VECTOR: KW_VECTOR TK_BASETYPE KW_SIZE TK_INTEGER (KW_START TK_INTEGER)?; //Made in Parser

TK_ASSIGNMENT: ':=';

TK_SUM: '+';
TK_SUB: '-';
TK_INVERT: '~';
TK_PROD: '*';
TK_DIV: '/';
TK_INTDIV: '\\';
TK_MODULO: '%';

TK_LESS: '<';
TK_GREATER: '>';
TK_LESSEQ: '<=';
TK_GREATEREQ: '>=';
TK_EQUALS: '==';
TK_NEQUALS: '!=';

TK_OR: '|';
TK_AND: '&';

TK_LPAR: '(';
TK_RPAR: ')';
TK_LBRACK: '[';
TK_RBRACK: ']';

TK_COLON: ':';
TK_SEMICOLON: ';';
TK_COMMA: ',';
TK_DOT: '.';
TK_QMARK: '?';

TK_COMMENT: '//' (.)*? ('\n' | '\r') -> skip;
TK_MULTICOMMENT: '/*' (.)*? '*/' -> skip;

SPACES: (' ' | '\n' | '\r' | '\t') -> skip;

///////////////////////////////////////////////////

inici: (~EOF)+ ; //Lexer testing rule

////////////////////////////////// MAIN RULE //////////////////////////////////

start //locals []
    :  type_declaration_block?
       func_declaration_block
       const_declaration_block?
       KW_PROGRAM prog_id=TK_IDENTIFIER
       var_declaration_block?
       (sentence)*
       KW_ENDPROGRAM
       func_implementation_block;

/////////////////////////// TYPE DECLARATION BLOCK ////////////////////////////

type_declaration_block: KW_TYPEBLOCK (type_declaration TK_SEMICOLON)* KW_ENDTYPEBLOCK;

type_declaration
    :   id=TK_IDENTIFIER TK_COLON btype=TK_BASETYPE
    |   id=TK_IDENTIFIER TK_COLON vector_definition
    |   id=TK_IDENTIFIER TK_COLON tuple_definition
    ;

vector_definition: KW_VECTOR TK_BASETYPE KW_SIZE TK_INTEGER (KW_START TK_INTEGER)?;

tuple_definition
    :   KW_TUPLE (TK_BASETYPE TK_IDENTIFIER TK_SEMICOLON)+ KW_ENDTUPLE;

///////////////////////////////////////////////////////////////////////////////

///////////////////// ACTION/FUNCTION DECLARATION BLOCK ///////////////////////

func_declaration_block: (action_declaration | function_declaration)* ;

action_declaration: KW_ACTION TK_IDENTIFIER TK_LPAR formal_parameters? TK_RPAR TK_SEMICOLON ;

function_declaration: KW_FUNCTION TK_IDENTIFIER TK_LPAR formal_parameters? TK_RPAR 
                      KW_RETURN TK_BASETYPE TK_SEMICOLON ;

formal_parameters: (KW_IN | KW_INOUT)? type TK_IDENTIFIER (TK_COMMA (KW_IN | KW_INOUT)? type TK_IDENTIFIER)* ;

type returns [int tkType, String text, int line]
    :   typ = (TK_BASETYPE | TK_IDENTIFIER) ; //Be careful with this

///////////////////////////////////////////////////////////////////////////////

///////////////////////// CONSTANT DECLARATION BLOCK //////////////////////////

const_declaration_block: KW_CONSTBLOCK (const_declaration TK_SEMICOLON)* KW_ENDCONSTBLOCK ;

const_declaration: bt=TK_BASETYPE id=TK_IDENTIFIER TK_ASSIGNMENT value=basetype_value ;

basetype_value returns [String text, C_TYPE typ, int line, Vector<Long> code]
    :   tk=(TK_INTEGER | TK_BOOLEAN | TK_CHARACTER | TK_REAL) ;

///////////////////////////////////////////////////////////////////////////////

///////////////////////// VARIABLE DECLARATION BLOCK //////////////////////////

var_declaration_block: KW_VARIABLEBLOCK (var_declaration TK_SEMICOLON)* KW_ENDVARIABLEBLOCK;

var_declaration: t=type id=TK_IDENTIFIER ;

///////////////////////////////////////////////////////////////////////////////

//////////////////////// FUNCTION IMPLEMENTATION BLOCK ////////////////////////

func_implementation_block: (action_definition | function_definition)*;
    
action_definition: KW_ACTION TK_IDENTIFIER TK_LPAR formal_parameters? TK_RPAR
                   var_declaration_block?
                   sentence*
                   KW_ENDACTION ;
   
function_definition: KW_FUNCTION TK_IDENTIFIER TK_LPAR formal_parameters? TK_RPAR
                     KW_RETURN TK_BASETYPE
                     var_declaration_block?
                     sentence*
                     KW_RETURN expr TK_SEMICOLON
                     KW_ENDFUNCTION ;

///////////////////////////////////////////////////////////////////////////////

/////////////////////////////// SENTENCES BLOCK ///////////////////////////////

sentence returns [Vector<Long> code]
        :   assignment TK_SEMICOLON             #sentenceAssignment
        |   conditional                         #sentenceConditional
        |   for_loop                            #sentenceFor
        |   while_loop                          #sentenceWhile
        |   function_call TK_SEMICOLON          #sentenceFunction
        |   read_operation TK_SEMICOLON         #sentenceAction
        |   write_operation TK_SEMICOLON        #sentenceWrite
        |   writeln_operation TK_SEMICOLON      #sentenceWriteln
        ;

assignment returns [Vector<Long> code]
    :   lval=lvalue TK_ASSIGNMENT e=expr ; //lvalue or expr

lvalue returns[C_TYPE typ, int line, String ident]
    :   tuple_acces
    |   vector_acces
    |   id=TK_IDENTIFIER
    ;

conditional returns [Vector<Long> code]
    : KW_IF expr /* boolean */ KW_THEN (sentence)* (KW_ELSE (sentence)*)? KW_ENDIF ;

for_loop returns [Vector<Long> code] locals [boolean errorLocal]
    : KW_FOR id=TK_IDENTIFIER KW_FROM expr1=expr /* integer */ KW_TO expr2=expr /* integer */  KW_DO (sentence)* KW_ENDFOR ;

while_loop returns [Vector<Long> code]
    : KW_WHILE expr /* boolean */ KW_DO (sentence  )* KW_ENDWHILE ;

function_call returns [C_TYPE typ, int line, Vector<Long> code]
    : TK_IDENTIFIER TK_LPAR (expr (TK_COMMA expr)*)? TK_RPAR ;

read_operation returns [Vector<Long> code]
    : KW_INPUT TK_LPAR id=TK_IDENTIFIER TK_RPAR;

write_operation returns [Vector<Long> code]
    : KW_OUTPUT TK_LPAR
    e=expr 
    (
        TK_COMMA
        ea=expr
    )*
    TK_RPAR;

writeln_operation returns [Vector<Long> code]
    : KW_OUTPUTLN TK_LPAR
    (
        e=expr
        (
            TK_COMMA
            ea=expr
        )*
    )?
    TK_RPAR;

///////////////////////////////////////////////////////////////////////////////

////////////////////////////// EXPRESSIONS BLOCK //////////////////////////////
expr returns[C_TYPE typ, int line, Vector<Long> code]
    :   ternary
    |   subexpr
    ; //care with priority

direct_evaluation_expr returns[C_TYPE typ, int line, Vector<Long> code]
    :   cv=constant_value       #directEvaluationCv
    |   id=TK_IDENTIFIER        #directEvaluationId
    |   tuple_acces             #directEvaluationTuple
    |   vector_acces            #directEvaluationVector
    |   function_call           #directEvaluationFunction
    ;

constant_value returns [C_TYPE typ, int line, Vector<Long> code]
    :   b=basetype_value
    |   s=TK_STRING  //For illustrative purposes
    ;

tuple_acces returns [C_TYPE typ, int line]: TK_IDENTIFIER TK_DOT TK_IDENTIFIER ;

vector_acces returns [C_TYPE typ, int line]: TK_IDENTIFIER TK_LBRACK subexpr /*integer expr*/ TK_RBRACK ;

ternary returns [C_TYPE typ, int line, Vector<Long> code] locals [boolean localError]
    :   cond=subexpr /* boolean */ TK_QMARK e1=expr TK_COLON e2=expr ;

//HAZARD ZONE
subexpr returns [C_TYPE typ, int line, Vector<Long> code] locals [boolean hasOperator]
    :
    t1=term1
    (
        o=logic_operators 
        t2=term1
    )*
    ;

//operation: (term1 logic_operators operation) | term1;
logic_operators returns [String text, int tk_type, int line]: tk=(TK_AND | TK_OR);

term1 returns [C_TYPE typ, int line, Vector<Long> code] locals [boolean hasOperator, C_TYPE leftType]
    :
    t1 = term2 
    (
        o = equality_operator
        t2 = term2
    )*
    ;

//term1: (term2 equality_operator term1) | term2;
equality_operator returns [String text, int tk_type, int line]: tk=(TK_EQUALS | TK_NEQUALS | TK_LESS | TK_LESSEQ | TK_GREATER | TK_GREATEREQ) ;

term2 returns [C_TYPE typ, int line, Vector<Long> code] locals [boolean hasOperator, C_TYPE leftType]
    :
    t1 = term3 
    (
        o = addition_operators
        t2 = term3
    )*
    ;

//term2: (term3 addition_operators term2) | term3;
addition_operators returns [String text, int tk_type, int line]: tk=(TK_SUB | TK_SUM) ;

term3 returns [C_TYPE typ, int line, Vector<Long> code] locals [C_TYPE leftType]
    :
    t1 = term4 
    (
        o = multiplication_operators
        t2 = term4
    )*
    ;

//term3: (term4 multiplication_operators term3) | term4;
multiplication_operators returns [String text, int tk_type, int line]: tk=(TK_PROD | TK_DIV | TK_INTDIV | TK_MODULO) ;

term4 returns [C_TYPE typ, int line, Vector<Long> code]
    :
    (
        o = negation_operators
        t = term5
    ) 
    |   t = term5
    ;
//term4: (negation_operators term4) | term5;
negation_operators returns [String text, int tk_type]:
    tk = (KW_NO | TK_INVERT) ;

term5 returns [C_TYPE typ, int line, Vector<Long> code]
    :   direct_evaluation_expr
    |   TK_LPAR expr TK_RPAR
    ;