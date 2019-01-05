/*  LANS Lexer and Parser
*   MADE BY: Marc Cane Salamia and Enric Rodr�guez Galan
*/

grammar LANSet;

@header{

}

@parser::members{

SymTable<Registre> TS = new SymTable<Registre>(1000);
boolean errorSintactic = false;
boolean errorSemantic = false;
//Override
public void notifyErrorListeners(Token offendingToken, String msg, RecognitionException e)
{
    //Si volem conservar el comportament inicial
    super.notifyErrorListeners(offendingToken,msg,e);
    //Codi personalitzat
    errorSintactic=true;
}

public boolean checkAndLogIdentifier(Token t){
    if(!identifierInUse(t.getText())){
        TS.inserir(t.getText(),new Registre(t.getText(),t.getType(),t.getLine(),t.getCharPositionInLine()));
        return false;
    } else return true;
}

public boolean identifierInUse(String id){
    if(TS.existeix(id)){
        //errorSemantic = true;
        System.out.println("Semantic error: The identifier " + id + " is already in use.");
        return true;
    }
    else return false;
}

}

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

TK_IDENTIFIER: LETTER ('_' | LETTER | DIGIT)*;

TK_INTEGER: '1'..'9' DIGIT* | '0' ;
TK_CHARACTER: '\'' SINGLE_CHAR '\'' ;
TK_BOOLEAN: 'true' | 'false';
TK_REAL: DECIMAL | DECIMAL 'E' ('-')? DIGIT+ ;
TK_STRING: '"' SINGLE_CHAR* '"';
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
TK_NEQUALS: '=!';

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

start: type_declaration_block?
       func_declaration_block
       const_declaration_block?
       KW_PROGRAM TK_IDENTIFIER
       var_declaration_block?
       sentence*
       KW_ENDPROGRAM
       func_implementation_block;

/////////////////////////// TYPE DECLARATION BLOCK ////////////////////////////

type_declaration_block: KW_TYPEBLOCK
                        (type_declaration TK_SEMICOLON)*
                        KW_ENDTYPEBLOCK;

type_declaration: TK_IDENTIFIER TK_COLON TK_BASETYPE        |
                  TK_IDENTIFIER TK_COLON vector_definition  |
                  TK_IDENTIFIER TK_COLON tuple_definition;

vector_definition: KW_VECTOR TK_BASETYPE KW_SIZE TK_INTEGER (KW_START TK_INTEGER)?;

tuple_definition: KW_TUPLE
                  (TK_BASETYPE TK_IDENTIFIER TK_SEMICOLON)+
                  KW_ENDTUPLE;

///////////////////////////////////////////////////////////////////////////////


///////////////////// ACTION/FUNCTION DECLARATION BLOCK ///////////////////////

func_declaration_block: (action_declaration | function_declaration)*;

action_declaration: KW_ACTION TK_IDENTIFIER TK_LPAR formal_parameters? TK_RPAR TK_SEMICOLON;

function_declaration: KW_FUNCTION TK_IDENTIFIER TK_LPAR formal_parameters? TK_RPAR 
                      KW_RETURN TK_BASETYPE TK_SEMICOLON;

formal_parameters: (KW_IN | KW_INOUT)? type TK_IDENTIFIER (TK_COMMA (KW_IN | KW_INOUT)? type TK_IDENTIFIER)*;

type returns [int t, String text]
    :
    typ = (TK_BASETYPE | TK_IDENTIFIER) {$t = $typ.type; $text = $typ.text;}
    ; //Be careful with this

///////////////////////////////////////////////////////////////////////////////


///////////////////////// CONSTANT DECLARATION BLOCK //////////////////////////

const_declaration_block: KW_CONSTBLOCK (const_declaration TK_SEMICOLON)* KW_ENDCONSTBLOCK;

const_declaration: bt=TK_BASETYPE id=TK_IDENTIFIER TK_ASSIGNMENT value=basetype_value {
                    System.out.println("He vist l'identificador " + $id.text);
                    System.out.println("Tipus: " + $bt.type + " " + $value.t + " " + TK_REAL);

                    if ($bt.text == "enter" && $value.t == TK_INTEGER){}
                    else if ($bt.text == "boolea" && $value.t == TK_INTEGER){}
                    else if ($bt.text == "car" && $value.t == TK_INTEGER){}
                    else if ($bt.text == "real" && $value.t == TK_INTEGER){}
                    else if ($bt.text == "real" && $value.t == TK_INTEGER){}
                    else{
                        errorSintactic=true;
                    }

                    if(!identifierInUse($id.text)){
                        checkAndLogIdentifier($id);
                    }
                    };

basetype_value returns [int t]: tk=(TK_INTEGER | TK_BOOLEAN | TK_CHARACTER | TK_REAL) {$t=$tk.type;};

///////////////////////////////////////////////////////////////////////////////
  

///////////////////////// VARIABLE DECLARATION BLOCK //////////////////////////

var_declaration_block: KW_VARIABLEBLOCK
                       (var_declaration TK_SEMICOLON)*
                       KW_ENDVARIABLEBLOCK;

var_declaration returns []
    :
    t = type{ if($type.t == TK_IDENTIFIER) identifierInUse($type.text); }
    id = TK_IDENTIFIER {errorSemantic = checkAndLogIdentifier($id);}
    ;

///////////////////////////////////////////////////////////////////////////////


//////////////////////// FUNCTION IMPLEMENTATION BLOCK ////////////////////////

func_implementation_block: (action_definition | function_definition)*;
    
action_definition: KW_ACTION TK_IDENTIFIER TK_LPAR formal_parameters? TK_RPAR
                   var_declaration_block?
                   sentence*
                   KW_ENDACTION;
   
function_definition: KW_FUNCTION TK_IDENTIFIER TK_LPAR formal_parameters? TK_RPAR
                     KW_RETURN TK_BASETYPE
                     var_declaration_block?
                     sentence*
                     KW_RETURN expr TK_SEMICOLON
                     KW_ENDFUNCTION;

///////////////////////////////////////////////////////////////////////////////

/////////////////////////////// SENTENCES BLOCK ///////////////////////////////

sentence: assignment TK_SEMICOLON | 
          conditional | 
          for_loop | 
          while_loop | 
          function_call TK_SEMICOLON | 
          read_operation TK_SEMICOLON | 
          write_operation TK_SEMICOLON | 
          writeln_operation TK_SEMICOLON;

assignment: lvalue TK_ASSIGNMENT expr; //lvalue or expr

lvalue: tuple_acces | vector_acces | TK_IDENTIFIER;

conditional: KW_IF expr /* boolean */ KW_THEN sentence* 
            (KW_ELSE sentence*)?
             KW_ENDIF;

for_loop: KW_FOR TK_IDENTIFIER KW_FROM expr /* integer */ KW_TO expr /* integer */ KW_DO sentence* KW_ENDFOR;

while_loop: KW_WHILE expr /* boolean */ KW_DO sentence* KW_ENDWHILE;

function_call: TK_IDENTIFIER TK_LPAR (expr (TK_COMMA expr)*)? TK_RPAR;

read_operation: KW_INPUT TK_LPAR TK_IDENTIFIER TK_RPAR;

write_operation: KW_OUTPUT TK_LPAR expr (TK_COMMA expr)* TK_RPAR;

writeln_operation: KW_OUTPUTLN TK_LPAR (expr (TK_COMMA expr)*)? TK_RPAR;

///////////////////////////////////////////////////////////////////////////////


/////////////////////////////// SENTENCES BLOCK ///////////////////////////////
expr: ternary | subexpr ; //careful priority

direct_evaluation_expr: constant_value |
                        TK_IDENTIFIER | //constant or variable
                        tuple_acces |
                        vector_acces |
                        function_call;

constant_value: basetype_value | TK_STRING; //For illustrative purposes

tuple_acces: TK_IDENTIFIER TK_DOT TK_IDENTIFIER;

vector_acces: TK_IDENTIFIER TK_LBRACK subexpr /*integer expr*/ TK_RBRACK;

ternary: subexpr /* boolean */ TK_QMARK expr TK_COLON expr;

//HAZARD ZONE
subexpr: term1 (logic_operators term1)*;
//operation: (term1 logic_operators operation) | term1;
logic_operators: TK_AND | TK_OR;

term1: term2 (equality_operator term2)*;
//term1: (term2 equality_operator term1) | term2;
equality_operator: TK_EQUALS | TK_NEQUALS | TK_LESS | TK_LESSEQ | TK_GREATER | TK_GREATEREQ;

term2: term3 (addition_operators term3)* ;
//term2: (term3 addition_operators term2) | term3;
addition_operators: TK_SUB | TK_SUM;

term3: term4 (multiplication_operators term4)* ;
//term3: (term4 multiplication_operators term3) | term4;
multiplication_operators: TK_PROD | TK_DIV | TK_INTDIV | TK_MODULO;

term4: (negation_operators term5) | term5;
//term4: (negation_operators term4) | term5;
negation_operators: KW_NO | TK_INVERT;

term5: direct_evaluation_expr | TK_LPAR expr TK_RPAR;
///////////////////////////////////////////////////////////////////////////////
/*
operation: term1 operation_lr;
operation_lr: logic_operators operation | ;
logic_operators: TK_AND | TK_OR;

term1: term2 term1_lr;
term1_lr: equality_operators term1 | ;
equality_operators: TK_EQUALS | TK_NEQUALS | TK_LESS | TK_LESSEQ | TK_GREATER | TK_GREATEREQ;

term2: term3 term2_lr;
term2_lr: equality_operators term2 | ;
addition_operators: TK_SUM | TK_SUB;

term3: term4 term3_lr;
term3_lr: equality_operators term3 | ;
multiplication_operators: TK_PROD | TK_DIV | TK_INTDIV | TK_MODULO;

term4: (negation_operators term4) | | term5;
negation_operators: KW_NO | TK_INVERT;

term5: direct_evaluation_expr | TK_LPAR expr TK_RPAR | ;*/

dummyrule
@init{System.out.println("recognising dummyrule");}
@after{System.out.println("unrecognising dummyrule");}
    :
    a=dummyrule2 {System.out.println("mako");}
    s=TK_ASSIGNMENT {System.out.println("lateumare");}
    b=dummyrule2 {System.out.println($b.text);}
    ;

dummyrule2 returns [String text] : t = (TK_AND | TK_OR) {$text = $t.text;};