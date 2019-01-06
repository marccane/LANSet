/*  LANS Lexer and Parser
*   MADE BY: Marc Cane Salamia and Enric Rodr�guez Galan
*/

grammar LANSet;

@header{

}

@parser::members{

    //types
static final String INVALID_TYPE = "NULL";
static final String CHAR_TYPE = "car";
static final String INT_TYPE = "enter";
static final String FLOAT_TYPE = "real";
static final String BOOL_TYPE = "boolea";
static final String STRING_TYPE = "string";

	//supertypes
static final String CONSTANT_SUPERTYPE = "constant";
static final String VARIABLE_SUPERTYPE = "variable";
static final String ALIAS_SUPERTYPE = "alias";
static final String FUNCTION_SUPERTYPE = "funcio";
static final String ACTION_SUPERTYPE = "accio";
static final String TUPLE_SUPERTYPE = "tupla";
static final String VECTOR_SUPERTYPE = "vector";

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

public String processBaseType(String type){
    String idType = INVALID_TYPE;
    switch(type){
        case CHAR_TYPE:
            idType = CHAR_TYPE;
            break;
        case INT_TYPE:
            idType = INT_TYPE;
            break;
        case FLOAT_TYPE:
            idType = FLOAT_TYPE;
            break;
        case BOOL_TYPE:
            idType = BOOL_TYPE;
            break;
        default:
            //do nothing
            break;
    }

    return idType;
}

public String getStringTypeFromTKIndex(int index){
    String res = INVALID_TYPE;
    if(index == TK_INTEGER) res = INT_TYPE;
    else if(index == TK_CHARACTER) res = CHAR_TYPE;
    else if(index == TK_BOOLEAN) res = BOOL_TYPE;
    else if(index == TK_REAL) res = FLOAT_TYPE;
    else if(index == TK_STRING) res = STRING_TYPE; //sha de fer algo mes?
    return res;
}

public boolean isBasetype(String type){
    return type == CHAR_TYPE || type == INT_TYPE || type == FLOAT_TYPE || type == BOOL_TYPE;
}

public void registerBasetypeVariable(String type, Token id){
    TS.inserir(id.getText(), new Registre(id.getText(), VARIABLE_SUPERTYPE, type, id.getLine(), id.getCharPositionInLine()));
}

public void registerAlias(Token id, Token type){
    String bType = processBaseType(type.getText());
    Registre r = new Registre(id.getText(), ALIAS_SUPERTYPE, bType, id.getLine(), id.getCharPositionInLine());
    TS.inserir(id.getText(), r);
}

public void registerConstant(Token id, Token type){
    String bType = processBaseType(type.getText());
    Registre r = new Registre(id.getText(), CONSTANT_SUPERTYPE, bType, id.getLine(), id.getCharPositionInLine());
    TS.inserir(id.getText(), r);
}

public boolean identifierInUse(String id){
    return TS.existeix(id);
}

public void repeatedIdentifierError(String id, int line){
    System.out.println("Semantic error at line " + line + ": the identifier " + id + " is already in use.");
}

public void undefinedTypeError(String t, int line){
    System.out.println("Semantic error at line " + line + ": type " + t + " is not defined.");
}

public void undefinedIdentifierError(String id, int line){
    System.out.println("Semantic error at line " + line + ": " + id + " is not defined.");
}

public void identifierIsNotAVariableError(String id, int line){
    System.out.println("Semantic error at line " + line + ": " + id + " is not a variable.");
}

public void nonBasetypeReadingError(String id, String type, int line){
    System.out.println("Semantic error at line " + line + ": Cannot read variable " + id + ". Read operation does not support reading " + type + " variables.");
}

public void typeMismatchError(String type1, String type2, int line){
    System.out.println("Type mismatch error at line " + line + ": Type " + type1 + " does not match with " + type2);
}

public void typeMismatchError2(String id, int line, String foundType, String expectedType){
    System.out.println("Semantic error: variable " + id + " in line " + line + " is type " + foundType + " but should be " + expectedType +".");
}

public void writeOperationUnsupportedTypeError(String type, int line){
    System.out.println("Unsupported type error at line " + line + ". Only \'" + STRING_TYPE + "\' expressions can be written to screen.");
}

public void ternaryTypeMismatchError(String t1, String t2, int line){
    System.out.println("Type mismatch error at line " + line + ": Type " + type1 + " does not match with " + type2 + ". Both ternary inner expressions must be the same type.");
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

TK_INTEGER: '1'..'9' DIGIT* | '0' ;
TK_CHARACTER: '\'' SINGLE_CHAR '\'' ;
TK_BOOLEAN: 'true' | 'false';
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

type_declaration
    :
    id=TK_IDENTIFIER TK_COLON btype=TK_BASETYPE {
        if(!identifierInUse($id.text)) registerAlias($id, $btype);
        else {
            errorSemantic = true;
            repeatedIdentifierError($id.text, $id.line);
        }
    }
    |
    id=TK_IDENTIFIER TK_COLON vector_definition {System.out.println("TODO: vector definition");}
    |
    id=TK_IDENTIFIER TK_COLON tuple_definition {System.out.println("TODO: tuple definition");}
    ;

vector_definition: KW_VECTOR TK_BASETYPE KW_SIZE TK_INTEGER (KW_START TK_INTEGER)?;

tuple_definition
    :
    KW_TUPLE
    (TK_BASETYPE TK_IDENTIFIER TK_SEMICOLON)+
    KW_ENDTUPLE;

///////////////////////////////////////////////////////////////////////////////


///////////////////// ACTION/FUNCTION DECLARATION BLOCK ///////////////////////

func_declaration_block: (action_declaration | function_declaration)*;

action_declaration: KW_ACTION TK_IDENTIFIER TK_LPAR formal_parameters? TK_RPAR TK_SEMICOLON;

function_declaration: KW_FUNCTION TK_IDENTIFIER TK_LPAR formal_parameters? TK_RPAR 
                      KW_RETURN TK_BASETYPE TK_SEMICOLON;

formal_parameters: (KW_IN | KW_INOUT)? type TK_IDENTIFIER (TK_COMMA (KW_IN | KW_INOUT)? type TK_IDENTIFIER)*;

type returns [int tkType, String text, int line]
    :
    typ = (TK_BASETYPE | TK_IDENTIFIER) {$tkType = $typ.type; $text = $typ.text; $line = $typ.line; }
    ; //Be careful with this

///////////////////////////////////////////////////////////////////////////////


///////////////////////// CONSTANT DECLARATION BLOCK //////////////////////////

const_declaration_block: KW_CONSTBLOCK (const_declaration TK_SEMICOLON)* KW_ENDCONSTBLOCK;

const_declaration
    :
    bt=TK_BASETYPE
    id=TK_IDENTIFIER TK_ASSIGNMENT
    value=basetype_value {
        //System.out.println("Tipus: " + $bt.type + " " + $value.typ + " " + TK_REAL);

        if(identifierInUse($id.text)){
            repeatedIdentifierError($id.text, $id.line);
            errorSemantic=true;
        }
        else{
            String valueType = $value.typ;
            if ($bt.text.equals(valueType)){
                registerConstant($id,$bt);
            }
            else if ($bt.text.equals(FLOAT_TYPE) && valueType.equals(INT_TYPE)){
                registerConstant($id,$bt);
            }
            else{
                typeMismatchError2($id.text, $id.line, valueType, $bt.text);
                errorSemantic=true;
            }
        }
    };

basetype_value returns [String typ, int line]: tk=(TK_INTEGER | TK_BOOLEAN | TK_CHARACTER | TK_REAL)
    {$typ=getStringTypeFromTKIndex($tk.type); $line = $tk.line;};

///////////////////////////////////////////////////////////////////////////////
  

///////////////////////// VARIABLE DECLARATION BLOCK //////////////////////////

var_declaration_block: KW_VARIABLEBLOCK
                       (var_declaration TK_SEMICOLON)*
                       KW_ENDVARIABLEBLOCK;

var_declaration
    :
    t = type{
            if($type.tkType == TK_IDENTIFIER) {
                //TS.inserir($type.text, new Registre($type.text));
                if(!identifierInUse($type.text)){
                    errorSemantic = true;
                    undefinedTypeError($type.text, $type.line);
                }
            }
        }
    id = TK_IDENTIFIER {
            if(identifierInUse($id.text)){
                errorSemantic = true;
                repeatedIdentifierError($id.text, $id.line);
            }
            else {
                if($type.tkType == TK_IDENTIFIER){ //alias, tuple or vector
                    System.out.println("TODO: alias, tuple or vector variable detected");
                }
                else registerBasetypeVariable($type.text, $id);
            }
        }
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

assignment
    :
    lval=lvalue
    TK_ASSIGNMENT
    e=expr{
        Registre var = TS.obtenir($lval.ident);
        if(var == null){
            errorSemantic = true;
            undefinedIdentifierError($lval.ident, $lval.line);
        }
        else if(var.getSupertype() != VARIABLE_SUPERTYPE){
            errorSemantic = true;
            identifierIsNotAVariableError($lval.ident, $lval.line); //no es el mes adient perque pot ser tuple o vector...
        }
        else if(!$lval.typ.equals($e.typ)){ //falta mirar si es pot promocionar de enter a float
            errorSemantic = true;
            typeMismatchError($lval.typ, $e.typ, $lval.line);
        }
    }; //lvalue or expr

lvalue returns[String typ, int line, String ident]
    :
    tuple_acces {System.out.println("TODO: tuple as an lvalue");}
    |
    vector_acces {System.out.println("TODO: vector as an lvalue");}
    |
    id=TK_IDENTIFIER {
        //registerBasetypeVariable(FLOAT_TYPE,$id); //debug
        Registre r = TS.obtenir($id.text);
        $typ = (r == null) ? INVALID_TYPE : r.getType();
        $line = $id.line;
        $ident = $id.text;
    };

conditional: KW_IF expr /* boolean */ {
                if(!$expr.typ.equals(BOOL_TYPE)){
                    errorSemantic=true;
                    typeMismatchError2("*expressio*", $expr.line, $expr.typ, BOOL_TYPE);
                }
            }
            KW_THEN sentence*
            (KW_ELSE sentence*)?
            KW_ENDIF;

for_loop: KW_FOR TK_IDENTIFIER KW_FROM expr /* integer */ KW_TO expr /* integer */ KW_DO sentence* KW_ENDFOR;

while_loop: KW_WHILE expr /* boolean */ {
                if(!$expr.typ.equals(BOOL_TYPE)){
                    errorSemantic=true;
                    typeMismatchError2("*expressio*", $expr.line, $expr.typ, BOOL_TYPE);
                }
            }
            KW_DO sentence* KW_ENDWHILE;

function_call: TK_IDENTIFIER TK_LPAR (expr (TK_COMMA expr)*)? TK_RPAR;

read_operation
    :
    KW_INPUT
    TK_LPAR
    id=TK_IDENTIFIER {
        //registerBasetypeVariable(FLOAT_TYPE,$id);
        Registre var = TS.obtenir($id.text);
        //var.putSupertype(TUPLE_SUPERTYPE);
        //var.putType("lmoile");

        if(var == null) { errorSemantic = true; undefinedIdentifierError($id.text, $id.line);}
        else if(var.getSupertype() != VARIABLE_SUPERTYPE) {
            errorSemantic = true;
            identifierIsNotAVariableError($id.text, $id.line);
        }
        else if(processBaseType(var.getType()) == INVALID_TYPE){ //is not a basetpye
            errorSemantic = true;
            nonBasetypeReadingError($id.text, var.getType(), $id.line);
        }

    }
    TK_RPAR;

write_operation:
    KW_OUTPUT
    TK_LPAR
    e=expr {
        if(!(isBasetype($e.typ) || $e.typ.equals(STRING_TYPE))){
            errorSemantic = true;
            writeOperationUnsupportedTypeError($e.typ, $e.line);
        }
    }
    (
        TK_COMMA
        ea=expr{
            if(!(isBasetype($ea.typ) || $ea.typ.equals(STRING_TYPE))){
                errorSemantic = true;
                writeOperationUnsupportedTypeError($e.typ, $e.line);
            }
        }
    )*
    TK_RPAR;

writeln_operation:
    KW_OUTPUTLN
    TK_LPAR
    (
        e=expr{
            if(!(isBasetype($e.typ) || $e.typ.equals(STRING_TYPE))){
                errorSemantic = true;
                writeOperationUnsupportedTypeError($e.typ, $e.line);
            }
        }
        (
            TK_COMMA ea=expr{
                if(!(isBasetype($ea.typ) || $ea.typ.equals(STRING_TYPE))){
                    errorSemantic = true;
                    writeOperationUnsupportedTypeError($e.typ, $e.line);
                }
            }
        )*
    )?

    TK_RPAR;

///////////////////////////////////////////////////////////////////////////////


/////////////////////////////// SENTENCES BLOCK ///////////////////////////////
expr returns[String typ, int line]
    :
    ternary {$typ = $ternary.typ; $line = $ternary.line;}
    |
    subexpr {$typ = $subexpr.typ; $line = $subexpr.line;}; //careful priority

direct_evaluation_expr returns[String typ, int line]:
    cv=constant_value {$typ = $cv.typ; $line = $cv.line;} |
    id=TK_IDENTIFIER { //constant or variable
        Registre var = TS.obtenir($id.text);

        $line = $id.line;

        if(var == null){
            undefinedIdentifierError($id.text, $id.line);
            $typ = INVALID_TYPE;
            errorSemantic = true;
        }
        else if(var.getSupertype().equals(VARIABLE_SUPERTYPE)){
            String varType = var.getType();

            if(isBasetype(varType)){
                $typ = varType;
            }
            else{
                Registre t = TS.obtenir(varType); //existence validation is not needed.
                String st = t.getSupertype();

                if(st.equals(ALIAS_SUPERTYPE)) $typ = t.getType();
                else $typ = varType;
            }
        }
        else if (var.getSupertype().equals(CONSTANT_SUPERTYPE)){
            $typ = var.getType();
        }
        else {
            $typ = INVALID_TYPE;
            errorSemantic = true;
        }
    } |
    tuple_acces |
    vector_acces |
    function_call;

constant_value returns [String typ, int line]
    :
    b=basetype_value{$typ = $b.typ; $line = $b.line;}
    |
    s=TK_STRING {$typ = STRING_TYPE; $line = $s.line;}; //For illustrative purposes

tuple_acces: TK_IDENTIFIER TK_DOT TK_IDENTIFIER {System.out.println("TODO: Tuple acces");};

vector_acces: TK_IDENTIFIER TK_LBRACK subexpr /*integer expr*/ TK_RBRACK {System.out.println("TODO: Vector acces");};

ternary returns [String typ, int line]
    :
    cond=subexpr /* boolean */{
        $line = $cond.line;
        if(!$cond.typ.equals(BOOL_TYPE)){
            errorSemantic=true;
            typeMismatchError($cond.typ, BOOL_TYPE, $cond.line);
        }
    }
    TK_QMARK
    e1=expr {$typ = $e1.typ;}
    TK_COLON
    e2=expr{
        if($e1.typ.equals(FLOAT_TYPE) && $e2.typ.equals(INT_TYPE)) $e2.typ = FLOAT_TYPE; //to real promotion
        else if($e2.typ.equals(FLOAT_TYPE) && $e1.typ.equals(INT_TYPE)) $e1.typ = FLOAT_TYPE; //to real promotion
        else if(!$e1.typ.equals($e2.typ)){ //none of both are real
            errorSemantic = true;
            ternaryTypeMismatchError($e1.typ, $e2.typ, $cond.line);
        }
    }
    ;

//HAZARD ZONE
subexpr returns [String typ, int line]
/*@after{
    if($o == null) {$typ = $t1.typ; $line = $t1.line;}
    else{

    }
}*/
: t1=term1 (o=logic_operators t2=term1)*;

//operation: (term1 logic_operators operation) | term1;
logic_operators: TK_AND | TK_OR;

term1 returns [String typ, int line]: term2 (equality_operator term2)*;
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