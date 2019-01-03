
/*  LANS Parser
*   MADE BY: Marc Cane Salamia and Enric Rodríguez Galan
*/


parser grammar LANSetParser;
options {tokenVocab = LANSetLexer;}

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

type: TK_BASETYPE | TK_IDENTIFIER; //Be careful with this

///////////////////////////////////////////////////////////////////////////////


///////////////////////// CONSTANT DECLARATION BLOCK //////////////////////////

const_declaration_block: KW_CONSTBLOCK
                         (const_declaration TK_SEMICOLON)*
                         KW_ENDCONSTBLOCK;

const_declaration: TK_BASETYPE TK_IDENTIFIER TK_ASSIGNMENT basetype_value;

basetype_value: TK_INTEGER | TK_BOOLEAN | TK_CHARACTER | TK_REAL;

///////////////////////////////////////////////////////////////////////////////
  

///////////////////////// VARIABLE DECLARATION BLOCK //////////////////////////

var_declaration_block: KW_VARIABLEBLOCK
                       (var_declaration TK_SEMICOLON)*
                       KW_ENDVARIABLEBLOCK;

var_declaration: type TK_IDENTIFIER;

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