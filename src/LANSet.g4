/*  LANS Lexer and Parser
*   MADE BY: Marc Cane Salamia and Enric Rodr�guez Galan
*/

grammar LANSet;

@header{
    import java.util.Vector;
}

@parser::members{

    //types
static final String INVALID_TYPE = "NULL";
static final String CHAR_TYPE = "car";
static final String INT_TYPE = "enter";
static final String FLOAT_TYPE = "real";
static final String BOOL_TYPE = "boolea";
static final String STRING_TYPE = "string";

static final String BOOL_TRUE = "cert";
static final String BOOL_FALSE = "fals";

static final String BYTECODE_VOIDTYPE = "V";
static final String BYTECODE_CHARTYPE = "C";
static final String BYTECODE_INTTYPE = "I";
static final String BYTECODE_FLOATTYPE = "F";
static final String BYTECODE_BOOLTYPE = "Z";
static final String BYTECODE_STRINGTYPE = "S";

	//supertypes
static final String CONSTANT_SUPERTYPE = "constant";
static final String VARIABLE_SUPERTYPE = "variable";
static final String ALIAS_SUPERTYPE = "alias";
static final String FUNCTION_SUPERTYPE = "funcio";
static final String ACTION_SUPERTYPE = "accio";
static final String TUPLE_SUPERTYPE = "tupla";
static final String VECTOR_SUPERTYPE = "vector";

SymTable<Registre> TS = new SymTable<Registre>(1000);
Bytecode program;
Long lineBreak;
Long nVar = 0L;
boolean errorSintactic = false;
boolean errorSemantic = false;

String classFile = "";

//Override
public void notifyErrorListeners(Token offendingToken, String msg, RecognitionException e)
{
    //Si volem conservar el comportament inicial
    super.notifyErrorListeners(offendingToken,msg,e);
    //Codi personalitzat
    errorSintactic=true;
}

public void setLANSClassFile(String classfile){
    classFile = classfile;
}

public String bytecodeType(String type){
    String idType = BYTECODE_VOIDTYPE;
    switch(type){
        case CHAR_TYPE:
            idType = BYTECODE_CHARTYPE;
            break;
        case INT_TYPE:
            idType = BYTECODE_INTTYPE;
            break;
        case FLOAT_TYPE:
            idType = BYTECODE_FLOATTYPE;
            break;
        case BOOL_TYPE:
            idType = BYTECODE_BOOLTYPE;
            break;
        case STRING_TYPE:
            idType = BYTECODE_STRINGTYPE;
            break;
        default:
            //do nothing
            break;
    }

    return idType;
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
    return type.equals(CHAR_TYPE) || type.equals(INT_TYPE) || type.equals(FLOAT_TYPE) || type.equals(BOOL_TYPE);
}

public void registerBasetypeVariable(String type, Token id){
    TS.inserir(id.getText(), new Registre(id.getText(), VARIABLE_SUPERTYPE, type, id.getLine(), id.getCharPositionInLine()));
}

public void registerAlias(Token id, Token type){
    String bType = processBaseType(type.getText());
    Registre r = new Registre(id.getText(), ALIAS_SUPERTYPE, bType, id.getLine(), id.getCharPositionInLine());
    TS.inserir(id.getText(), r);
}

public Registre registerConstant(Token id, Token type){
    String bType = processBaseType(type.getText());
    Registre r = new Registre(id.getText(), CONSTANT_SUPERTYPE, bType, id.getLine(), id.getCharPositionInLine());
    TS.inserir(id.getText(), r);
    return r;
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
    System.out.println("Unsupported type " + type + " at line " + line);
}

public void ternaryTypeMismatchError(String t1, String t2, int line){
    System.out.println("Type mismatch error at line " + line + ": Type " + t1 + " does not match with " + t2 + ". Both ternary inner expressions must be the same type.");
}

public void operatorTypeMismatchError(String type, String op, int line, String expected){
    System.out.println("Type mismatch Error at line " + line + ": operator \'" + op + "\' does not work with \'" + type + "\' expressions. Expected " + expected + " instead.");
}

public Vector<Long> generateWriteCode(String type){
    Vector <Long> code = new Vector<>();

    code.add(program.INVOKESTATIC);

    if(type.equals(BYTECODE_CHARTYPE)){
        code.add(program.nByte(program.mPutChar,2));
        code.add(program.nByte(program.mPutChar,1));
    }
    else if(type.equals(BYTECODE_INTTYPE)){
        code.add(program.nByte(program.mPutInt,2));
        code.add(program.nByte(program.mPutInt,1));
    }
    else if(type.equals(BYTECODE_FLOATTYPE)){
        code.add(program.nByte(program.mPutFloat,2));
        code.add(program.nByte(program.mPutFloat,1));
    }
    else if(type.equals(BYTECODE_BOOLTYPE)){
        code.add(program.nByte(program.mPutBoolean,2));
        code.add(program.nByte(program.mPutBoolean,1));
    }
    else if(type.equals(BYTECODE_STRINGTYPE)){
        code.add(program.nByte(program.mPutString,2));
        code.add(program.nByte(program.mPutString,1));
    }

    return code;
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

start
    @init{
        program = new Bytecode(classFile);
        lineBreak = program.addConstant(BYTECODE_STRINGTYPE, "\n");
        Vector<Long> code = new Vector<>(10);
    }
@after{
        if (!errorSemantic)
        {
            code.add(program.RETURN);
            program.addMainCode(10L,10L,code);
            program.write();
            System.out.println(classFile + ".class generat");
        }
        else System.out.println(classFile + ".class NO generat");

        //program.show();
    }
    :  type_declaration_block?
       func_declaration_block
       const_declaration_block?
       KW_PROGRAM TK_IDENTIFIER
       var_declaration_block?
       (sentence{code.addAll($sentence.code);})*
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
    KW_ENDTUPLE {System.out.println("TODO: tuple definition");};

///////////////////////////////////////////////////////////////////////////////


///////////////////// ACTION/FUNCTION DECLARATION BLOCK ///////////////////////

func_declaration_block: (action_declaration | function_declaration)* {System.out.println("TODO: function declaration block");};

action_declaration: KW_ACTION TK_IDENTIFIER TK_LPAR formal_parameters? TK_RPAR TK_SEMICOLON {System.out.println("TODO: action declaration");};

function_declaration: KW_FUNCTION TK_IDENTIFIER TK_LPAR formal_parameters? TK_RPAR 
                      KW_RETURN TK_BASETYPE TK_SEMICOLON {System.out.println("TODO: function declaration");};

formal_parameters: (KW_IN | KW_INOUT)? type TK_IDENTIFIER (TK_COMMA (KW_IN | KW_INOUT)? type TK_IDENTIFIER)* {System.out.println("TODO: formal parameters");};

type returns [int tkType, String text, int line]
    :
    typ = (TK_BASETYPE | TK_IDENTIFIER) {$tkType = $typ.type; $text = $typ.text; $line = $typ.line; }
    ; //Be careful with this

///////////////////////////////////////////////////////////////////////////////


///////////////////////// CONSTANT DECLARATION BLOCK //////////////////////////

const_declaration_block
: KW_CONSTBLOCK (const_declaration TK_SEMICOLON)* KW_ENDCONSTBLOCK;

const_declaration:
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
                Registre r = registerConstant($id,$bt);
                Long dir = program.addConstName(r.getText(), bytecodeType(r.getType()), $value.text);
                r.putDir(dir);
            }
            else if ($bt.text.equals(FLOAT_TYPE) && valueType.equals(INT_TYPE)){
                Registre r = registerConstant($id,$bt);
                Long dir = program.addConstName(r.getText(), bytecodeType(r.getType()), $value.text);
                r.putDir(dir);
            }
            else{
                typeMismatchError2($id.text, $id.line, valueType, $bt.text);
                errorSemantic=true;
            }
        }
    };

basetype_value returns [String text, String typ, int line, Vector<Long> code]
@init{ $code = new Vector<>(); }
    : tk=(TK_INTEGER | TK_BOOLEAN | TK_CHARACTER | TK_REAL) {
        $text = $tk.text;
        $typ=getStringTypeFromTKIndex($tk.type);
        $line = $tk.line;

        Long value = -1L;

        switch($tk.type){
            case TK_INTEGER:
                value = program.addConstant(BYTECODE_INTTYPE, $tk.text);
                break;
            case TK_BOOLEAN:
                value = program.addConstant(BYTECODE_BOOLTYPE, ($tk.text.equals(BOOL_TRUE)?"Cert":"Fals"));
                break;
            case TK_CHARACTER:
                value = program.addConstant(BYTECODE_CHARTYPE, $tk.text);
                break;
            case TK_REAL:
                value = program.addConstant(BYTECODE_FLOATTYPE, $tk.text);
                break;
            default:
                //do nothing
                break;
        }

        $code.add(program.LDC_W);
        $code.add(program.nByte(value,2));
        $code.add(program.nByte(value,1));
    }
;

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
                else {
                    registerBasetypeVariable($type.text, $id);
                    TS.obtenir($id.text).putDir(nVar++);
                }
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

sentence returns [Vector<Long> code]
@init{ $code = new Vector<>(); }
:
          assignment TK_SEMICOLON {$code.addAll($assignment.code);} |
          conditional {$code.addAll($conditional.code);} |
          for_loop {$code.addAll($for_loop.code);} |
          while_loop {$code.addAll($while_loop.code);} |
          function_call TK_SEMICOLON {$code.addAll($function_call.code);} |
          read_operation TK_SEMICOLON {$code.addAll($read_operation.code);} |
          write_operation TK_SEMICOLON {$code.addAll($write_operation.code);} |
          writeln_operation TK_SEMICOLON {$code.addAll($writeln_operation.code);};

assignment returns [Vector<Long> code]
@init{ $code = new Vector<>();}
    :
    lval=lvalue
    TK_ASSIGNMENT
    e=expr{
        $code.addAll($e.code);

        Registre var = TS.obtenir($lval.ident);
        Boolean canBePromoted = ($lval.typ.equals(FLOAT_TYPE) && $e.typ.equals(INT_TYPE));

        if(var == null){
            errorSemantic = true;
            undefinedIdentifierError($lval.ident, $lval.line);
        }
        else if(!var.getSupertype().equals(VARIABLE_SUPERTYPE)){
            errorSemantic = true;
            identifierIsNotAVariableError($lval.ident, $lval.line); //no es el mes adient perque pot ser tuple o vector...
        }
        else if(!$lval.typ.equals($e.typ) && !canBePromoted){
            errorSemantic = true;
            typeMismatchError($lval.typ, $e.typ, $lval.line);
        }
        else{ //lval es un identificador existent, tipus variable i els tipus casen
            if(canBePromoted){ //has to be promoted
                $code.add(program.I2F);
            }
            String lvalVarType = var.getType();

            switch(lvalVarType){ //tipus basics
               case CHAR_TYPE:
                   $code.add(program.ISTORE); //mhhhhh
                   break;
               case INT_TYPE:
                   $code.add(program.ISTORE);
                   break;
               case FLOAT_TYPE:
                   $code.add(program.FSTORE);
                   break;
               case BOOL_TYPE:
                   $code.add(program.ISTORE); //mhhhhh
                   break;
            }
            Long lvalVarDir = var.getDir();
            $code.add(lvalVarDir); //index (long? hauria de ser byte?)
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

conditional returns [Vector<Long> code]
@init{
    $code = new Vector<>();
    Vector<Long> c1Code = new Vector<>();
    Vector<Long> c2Code = new Vector<>();
}
    : KW_IF expr /* boolean */ KW_THEN (sentence{c1Code.addAll($sentence.code);})*
     (KW_ELSE (sentence{c2Code.addAll($sentence.code);})*)? KW_ENDIF{
            if(!$expr.typ.equals(BOOL_TYPE)){
                errorSemantic=true;
                typeMismatchError2("*expressio*", $expr.line, $expr.typ, BOOL_TYPE);
            }
            else{ //no error, codegen
                $code.addAll($expr.code);
                $code.add(program.IFEQ); //if false, jumps to else (c1.size()+6)

                Long jump = 2L + c1Code.size() + 3L + 1L; //if condition is false, jump to c2 (6 = ifjump1+ifjump2+goto+goto1+goto2+1)
                $code.add(program.nByte(jump,2));
                $code.add(program.nByte(jump,1));
                $code.addAll(c1Code);

                jump = c2Code.size()+3L; //if we're on the end of c1, skip c2 (3 = goto1 + goto2 + 1)
                $code.add(program.GOTO);
                $code.add(program.nByte(jump,2));
                $code.add(program.nByte(jump,1));
                $code.addAll(c2Code);
            }
     };

for_loop returns [Vector<Long> code] locals [boolean errorLocal] //7
@init{
    $code = new Vector<>();
    Vector<Long> c1Code = new Vector<>();
    $errorLocal = false;
}
    : KW_FOR id=TK_IDENTIFIER KW_FROM expr1=expr /* integer */ KW_TO expr2=expr /* integer */ {
        Registre var_iter = TS.obtenir($id.text);
        if(var_iter == null){
            $errorLocal = true;
            errorSemantic = true;
            undefinedIdentifierError($id.text, $id.line);
        }
        else if(!var_iter.getSupertype().equals(VARIABLE_SUPERTYPE)){
            $errorLocal = true;
            errorSemantic = true;
            identifierIsNotAVariableError($id.text, $id.line);
        }
        else if(!var_iter.getType().equals(INT_TYPE)){
            $errorLocal = true;
            errorSemantic = true;
            typeMismatchError2($id.text, $id.line, var_iter.getType(), INT_TYPE);
        }

        if(!$expr1.typ.equals(INT_TYPE)){
            $errorLocal = true;
            errorSemantic=true;
            typeMismatchError2("*expressio*", $expr1.line, $expr1.typ, INT_TYPE);
        }

        if(!$expr2.typ.equals(INT_TYPE)){
            $errorLocal = true;
            errorSemantic=true;
            typeMismatchError2("*expressio*", $expr2.line, $expr2.typ, INT_TYPE);
        }

    } KW_DO (sentence {c1Code.addAll($sentence.code);} )* KW_ENDFOR{
        //if()
    }
;

while_loop returns [Vector<Long> code]
@init{
    $code = new Vector<>();
    Vector<Long> c1Code = new Vector<>();
}
    : KW_WHILE expr /* boolean */ KW_DO (sentence {c1Code.addAll($sentence.code);} )* KW_ENDWHILE {
        if(!$expr.typ.equals(BOOL_TYPE)){
            errorSemantic=true;
            typeMismatchError2("*expressio*", $expr.line, $expr.typ, BOOL_TYPE);
        }
        else{ //no error, codegen
            $code.addAll($expr.code);

            $code.add(program.IFEQ); //if false, jumps to end of the while (c1.size()+6) (6 = ifjump1+ifjump2+goto+goto1+goto2+1)
            Long jump = 2L + c1Code.size() + 3L + 1L;
            $code.add(program.nByte(jump,2));
            $code.add(program.nByte(jump,1));
            $code.addAll(c1Code);

            jump = 0L-($code.size()); //jump to first instruction of expr, so -(c1.size + expr.code.size + 3) and also actual $code.size
            $code.add(program.GOTO);
            $code.add(program.nByte(jump,2));
            $code.add(program.nByte(jump,1));
        }
    };

function_call returns [String typ, int line, Vector<Long> code]
@init{ $code = new Vector<>(); }
    : TK_IDENTIFIER TK_LPAR (expr (TK_COMMA expr)*)? TK_RPAR;

read_operation returns [Vector<Long> code]
@init{ $code = new Vector<>(); }
    : KW_INPUT TK_LPAR id=TK_IDENTIFIER TK_RPAR{
        Registre var = TS.obtenir($id.text);

        if(var == null) { errorSemantic = true; undefinedIdentifierError($id.text, $id.line);}
        else if(!var.getSupertype().equals(VARIABLE_SUPERTYPE)) {
           errorSemantic = true;
           identifierIsNotAVariableError($id.text, $id.line);
        }
        else if(processBaseType(var.getType()).equals(INVALID_TYPE)){ //is not a basetpye
           errorSemantic = true;
           nonBasetypeReadingError($id.text, var.getType(), $id.line);
        }
        else{ // no error, codegen
            String bcType = bytecodeType(var.getType());

            $code.add(program.INVOKESTATIC);

            if(bcType.equals(BYTECODE_CHARTYPE)){
                $code.add(program.nByte(program.mGetChar,2));
                $code.add(program.nByte(program.mGetChar,1));
                $code.add(program.ISTORE);
            }
            else if(bcType.equals(BYTECODE_INTTYPE)){
                $code.add(program.nByte(program.mGetInt,2));
                $code.add(program.nByte(program.mGetInt,1));
                $code.add(program.ISTORE);
            }
            else if(bcType.equals(BYTECODE_FLOATTYPE)){
                $code.add(program.nByte(program.mGetFloat,2));
                $code.add(program.nByte(program.mGetFloat,1));
                $code.add(program.FSTORE);
            }
            else if(bcType.equals(BYTECODE_BOOLTYPE)){
                $code.add(program.nByte(program.mGetBoolean,2));
                $code.add(program.nByte(program.mGetBoolean,1));
                $code.add(program.ISTORE);
            }

            $code.add(var.getDir());

        }
    };

write_operation returns [Vector<Long> code]
@init{ $code = new Vector<>(); }
    :
    KW_OUTPUT
    TK_LPAR
    e=expr {
        if(!(isBasetype($e.typ) || $e.typ.equals(STRING_TYPE))){
            errorSemantic = true;
            writeOperationUnsupportedTypeError($e.typ, $e.line);
        }
        else{
            $code.addAll($e.code);

            String bcType = bytecodeType($e.typ);

            $code.addAll(generateWriteCode(bcType));
        }
    }
    (
        TK_COMMA
        ea=expr{
            if(!(isBasetype($ea.typ) || $ea.typ.equals(STRING_TYPE))){
                errorSemantic = true;
                writeOperationUnsupportedTypeError($ea.typ, $ea.line);
            }
            else{
                 $code.addAll($ea.code);

                 String bcType = bytecodeType($ea.typ);

                 $code.addAll(generateWriteCode(bcType));
            }
        }
    )*
    TK_RPAR;

writeln_operation returns [Vector<Long> code]
@init{ $code = new Vector<>(); }
@after{
    $code.add(program.LDC_W);
    $code.add(program.nByte(lineBreak,2));
    $code.add(program.nByte(lineBreak,1));
    $code.addAll(generateWriteCode(BYTECODE_STRINGTYPE));
}
    :
    KW_OUTPUTLN
    TK_LPAR
    (
        e=expr{
            if(!(isBasetype($e.typ) || $e.typ.equals(STRING_TYPE))){
                errorSemantic = true;
                writeOperationUnsupportedTypeError($e.typ, $e.line);
            }
            else{
                $code.addAll($e.code);

                String bcType = bytecodeType($e.typ);

                $code.addAll(generateWriteCode(bcType));
            }
        }
        (
            TK_COMMA ea=expr{
                if(!(isBasetype($ea.typ) || $ea.typ.equals(STRING_TYPE))){
                    errorSemantic = true;
                    writeOperationUnsupportedTypeError($ea.typ, $ea.line);
                }
                else{
                     $code.addAll($ea.code);

                     String bcType = bytecodeType($ea.typ);

                     $code.addAll(generateWriteCode(bcType));
                }
            }
        )*
    )?

    TK_RPAR;

///////////////////////////////////////////////////////////////////////////////


////////////////////////////// EXPRESSIONS BLOCK //////////////////////////////
expr returns[String typ, int line, Vector<Long> code]
@after{$code.toString();}
//@after{System.out.println($typ);}
    :
    ternary {$typ = $ternary.typ; $line = $ternary.line; $code = $ternary.code;}
    |
    subexpr {$typ = $subexpr.typ; $line = $subexpr.line; $code = $subexpr.code;}
    ; //care with priority

direct_evaluation_expr returns[String typ, int line, Vector<Long> code]
@init{$code = new Vector<>();}
@after{$code.toString();}
:
    cv=constant_value {$typ = $cv.typ; $line = $cv.line; $code.addAll($constant_value.code);} |
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
                switch(varType){
                    case CHAR_TYPE:
                        $code.add(program.ILOAD); //mhhhhh
                        break;
                    case INT_TYPE:
                        $code.add(program.ILOAD);
                        break;
                    case FLOAT_TYPE:
                        $code.add(program.FLOAD);
                        break;
                    case BOOL_TYPE:
                        $code.add(program.ILOAD); //mhhhhh
                        break;
                }
                Long varDir = var.getDir();
                $code.add(varDir); //index (long? hauria de ser byte?)
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

            Long constDir = var.getDir();
            $code.add(program.LDC_W);
            $code.add(program.nByte(constDir,2));
            $code.add(program.nByte(constDir,1));
        }
        else {
            $typ = INVALID_TYPE;
            System.out.println("Error at line: " + $line + ": Use of identifier \'" + $id.text +"\' is not allowed here.");
            errorSemantic = true;
        }
    } |
    tuple_acces {$typ = $tuple_acces.typ; $line = $tuple_acces.line;} |
    vector_acces {$typ = $vector_acces.typ; $line = $vector_acces.line;} |
    function_call {$typ = $function_call.typ; $line = $function_call.line;}
    ;

constant_value returns [String typ, int line, Vector<Long> code]
@init{$code = new Vector<>();}
    :
    b=basetype_value{$typ = $b.typ; $line = $b.line; $code = $b.code;}
    |
    s=TK_STRING {
        $typ = STRING_TYPE;
        $line = $s.line;

        Long str = program.addConstant(BYTECODE_STRINGTYPE, $s.text.substring(1,$s.text.length()-1));
        $code.add(program.LDC_W);
        $code.add(program.nByte(str,2));
        $code.add(program.nByte(str,1));
    } //For illustrative purposes
    ;

tuple_acces returns [String typ, int line]: TK_IDENTIFIER TK_DOT TK_IDENTIFIER {System.out.println("TODO: Tuple acces");};

vector_acces returns [String typ, int line]: TK_IDENTIFIER TK_LBRACK subexpr /*integer expr*/ TK_RBRACK {System.out.println("TODO: Vector acces");};

ternary returns [String typ, int line, Vector<Long> code]
@init{$code = new Vector<>();}
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
        if($e1.typ.equals(FLOAT_TYPE) && $e2.typ.equals(INT_TYPE)) $typ = FLOAT_TYPE; //(dolar)e2.typ = FLOAT_TYPE; //to real promotion
        else if($e2.typ.equals(FLOAT_TYPE) && $e1.typ.equals(INT_TYPE)) $typ = FLOAT_TYPE; //(dolar)e1 b.typ = FLOAT_TYPE; //to real promotion
        else if(!$e1.typ.equals($e2.typ)){ //none of both are real
            errorSemantic = true;
            ternaryTypeMismatchError($e1.typ, $e2.typ, $cond.line);
        }
    }
    ;

//HAZARD ZONE
subexpr returns [String typ, int line, Vector<Long> code] locals [boolean hasOperator]
@init{ $hasOperator = false;}
@after{$code.toString();}
    :
    t1=term1 {$typ = $t1.typ; $line = $t1.line; $code = $t1.code;}
    (
        o=logic_operators {
            if(!$hasOperator){ //if there's at least one operation, typecheck of t1 is needed
                $hasOperator = true;
                if(!$t1.typ.equals(BOOL_TYPE)){
                    errorSemantic = true;
                    operatorTypeMismatchError($t1.typ, $o.text, $o.line, BOOL_TYPE);
                }
            }
        }
        t2=term1{
            if(!$t2.typ.equals(BOOL_TYPE)){ //check if the right operand is boolean
                errorSemantic = true;
                operatorTypeMismatchError($t2.typ, $o.text, $o.line, BOOL_TYPE);
            }
            else{ //no error
                $code.addAll($t2.code);

                if($o.tk_type == TK_AND) $code.add(program.IAND);
                else $code.add(program.IOR);
            }
        }
    )*;

//operation: (term1 logic_operators operation) | term1;
logic_operators returns [String text, int tk_type, int line]: tk=(TK_AND | TK_OR){$text = $tk.text; $tk_type = $tk.type; $line = $tk.line;};

term1 returns [String typ, int line, Vector<Long> code] locals [boolean hasOperator, String leftType]
@init{ $code = new Vector<>(); $hasOperator = false;}
@after{$typ = $leftType; $code.toString();}
    :
    t1 = term2 {$leftType = $t1.typ; $line = $t1.line; $code.addAll($t1.code);}
    (
        o = equality_operator{
            if(!$hasOperator){ //first left operand found, typecheck needed
                $hasOperator = true;
                if($o.tk_type == TK_EQUALS || $o.tk_type == TK_NEQUALS ) { // == or !=
                    if( !(isBasetype($leftType)) ){ //if the operator isn't a basic type
                        errorSemantic = true;
                        operatorTypeMismatchError($t1.typ, $o.text, $o.line, "basic type");
                        //impossible to propagate a "less restrictive" type, so let's propagate the original one.
                    }
                    //otherwise, leftType should be propagated
                }
                else{ //other operations only work on integer or real numbers
                    if( !($leftType.equals(INT_TYPE) || $leftType.equals(FLOAT_TYPE)) ){ //
                        errorSemantic = true;
                        operatorTypeMismatchError($t1.typ, $o.text, $o.line, INT_TYPE + " or " + FLOAT_TYPE);
                        $leftType = INT_TYPE; //typing error, propagate less restrictive type in order to continue semantic analysis
                    }
                    //otherwise, leftType should be propagated
                }
            }
        }
        t2 = term2{
            if($o.tk_type == TK_EQUALS || $o.tk_type == TK_NEQUALS ) { // == or !=
                if( !(isBasetype($t2.typ)) ){ //if the operator isn't a basic type
                    errorSemantic = true;
                    operatorTypeMismatchError($t1.typ, $o.text, $o.line, "basic type");
                    //impossible to propagate a "less restrictive" type, so let's propagate the original one.
                }
                else if($leftType.equals(INT_TYPE) || $t2.typ.equals(INT_TYPE)){
                    //maybe useless if statements
                    if($leftType.equals(INT_TYPE) && $t2.typ.equals(FLOAT_TYPE)) $leftType = FLOAT_TYPE;
                    else if($leftType.equals(FLOAT_TYPE) && $t2.typ.equals(INT_TYPE)) $leftType = FLOAT_TYPE;
                    else if( !($leftType.equals(INT_TYPE) && $t2.typ.equals(INT_TYPE)) ){ //error, leftType and t2 types doesn't match
                        errorSemantic = true;
                        typeMismatchError($leftType, $t2.typ, $o.line);
                    }
                }
                else if( !$leftType.equals($t2.typ) ){
                    errorSemantic = true;
                    typeMismatchError($leftType, $t2.typ, $o.line);
                }

                $leftType = BOOL_TYPE;
            }
            else{ //other operations only work on integer or real numbers
                if( !($t2.typ.equals(INT_TYPE) || $t2.typ.equals(FLOAT_TYPE)) ){
                    errorSemantic = true;
                    operatorTypeMismatchError($leftType, $o.text, $o.line, INT_TYPE + " or " + FLOAT_TYPE);
                }
                else{ //if tis integer or real
                    if($t2.typ.equals(FLOAT_TYPE)) $leftType = FLOAT_TYPE; //integer promotion if needed
                    //otherwise promotion depends on leftType, so no changes needed
                }

                $leftType = BOOL_TYPE;
            }
        }
    )*
    ;

//term1: (term2 equality_operator term1) | term2;
equality_operator returns [String text, int tk_type, int line]: tk=(TK_EQUALS | TK_NEQUALS | TK_LESS | TK_LESSEQ | TK_GREATER | TK_GREATEREQ) {$text = $tk.text; $tk_type = $tk.type; $line = $tk.line;};

term2 returns [String typ, int line, Vector<Long> code] locals [boolean hasOperator, String leftType]
@init{ $hasOperator = false;}
@after{$typ = $leftType; $code.toString();}
    :
    t1 = term3 {$leftType = $t1.typ; $line = $t1.line; $code = $t1.code;}
    (
        o = addition_operators{
            if(!$hasOperator){ //first left operand found, typecheck needed
                $hasOperator = true;
                if( !($leftType.equals(INT_TYPE) || $leftType.equals(FLOAT_TYPE)) ){ //if is neither an integer or a real number
                    errorSemantic = true;
                    operatorTypeMismatchError($t1.typ, $o.text, $o.line, INT_TYPE + " or " + FLOAT_TYPE);
                    //$leftType = INT_TYPE; //typing error, propagate less restrictive type in order to continue semantic analysis
                }
            }
        }
        t2 = term3{
            if($t2.typ.equals(FLOAT_TYPE)){ //if t2 is float type, integer promotion may be needed
                $leftType = FLOAT_TYPE;
                $code.addAll($t2.code); //right operand

                if($o.tk_type == TK_SUM) $code.add(program.FADD);
                else $code.add(program.FSUB);
            }
            else if ($t2.typ.equals(INT_TYPE)){ //if t2 is integer, the result depends on leftType.

                $code.addAll($t2.code); //right operand

                if($leftType.equals(FLOAT_TYPE)){
                    if($o.tk_type == TK_SUM) $code.add(program.FADD);
                    else $code.add(program.FSUB);
                }
                else{
                    if($o.tk_type == TK_SUM) $code.add(program.IADD);
                    else $code.add(program.ISUB);
                }
            }
            else{ //typing error
                errorSemantic = true;
                operatorTypeMismatchError($t2.typ, $o.text, $o.line, INT_TYPE + " or " + FLOAT_TYPE);
                $leftType = INT_TYPE; //typing error, propagate less restrictive type in order to continue semantic analysis
            }
        }
    )*
    ;

//term2: (term3 addition_operators term2) | term3;
addition_operators returns [String text, int tk_type, int line]: tk=(TK_SUB | TK_SUM) {$text = $tk.text; $tk_type = $tk.type; $line = $tk.line;};

term3 returns [String typ, int line, Vector<Long> code] locals [String leftType]
@after{
    $typ = $leftType;
    $line = $t1.line;
    $code.toString();
}
    :
    t1 = term4 {$leftType = $t1.typ; $code = $t1.code;}
    (
        o = multiplication_operators
        t2 = term4{
            if( ($o.tk_type == TK_INTDIV || $o.tk_type == TK_MODULO) ){ // integer division or modulo
                if( !$t2.typ.equals(INT_TYPE) ){ //if t2 it's not an integer
                    errorSemantic = true;
                    operatorTypeMismatchError($t2.typ, $o.text, $o.line, INT_TYPE);
                }
                else if( !$leftType.equals(INT_TYPE) ){ //if left operand is not an integer
                    errorSemantic = true;
                    operatorTypeMismatchError($leftType, $o.text, $o.line, INT_TYPE);
                }
                else{ //no error
                    $code.addAll($t2.code); //right operand

                    if($o.tk_type == TK_INTDIV) $code.add(program.IDIV);
                    else $code.add(program.IREM);
                }

                $leftType = INT_TYPE;
            }
            else{ //real division and multiplication
                boolean errorLocal = false;

                if( !($t2.typ.equals(FLOAT_TYPE) || $t2.typ.equals(INT_TYPE)) ){
                    errorSemantic = true;
                    errorLocal = true;
                    operatorTypeMismatchError($t2.typ, $o.text, $o.line, INT_TYPE + " or " + FLOAT_TYPE);
                }

                if( !($leftType.equals(FLOAT_TYPE) || $leftType.equals(INT_TYPE)) ){ //if left operand is not an integer or a real number
                    errorSemantic = true;
                    errorLocal = true;
                    operatorTypeMismatchError($leftType, $o.text, $o.line, INT_TYPE + " or " + FLOAT_TYPE);
                }

                if($o.tk_type == TK_DIV) {
                    $leftType = FLOAT_TYPE; //division always spits a real number.
                    $code.addAll($t2.code); //right operand
                    $code.add(program.FDIV);
                }
                else{ //multiplication
                    if($t2.typ.equals(INT_TYPE) && $leftType.equals(INT_TYPE)) {
                        $leftType = INT_TYPE; //if both are integer type
                        $code.addAll($t2.code); //right operand
                        $code.add(program.IMUL);
                    }
                    else if(!errorLocal){
                        $leftType = FLOAT_TYPE; //at least one of them is real, and the other is real or integer
                        $code.addAll($t2.code); //right operand
                        $code.add(program.FMUL);
                    }
                    else $leftType = INT_TYPE;//typing error, propagate less restrictive type in order to continue semantic analysis
                }

            }
        }
    )*
    ;

//term3: (term4 multiplication_operators term3) | term4;
multiplication_operators returns [String text, int tk_type, int line]: tk=(TK_PROD | TK_DIV | TK_INTDIV | TK_MODULO) {$text = $tk.text; $tk_type = $tk.type; $line = $tk.line;};

term4 returns [String typ, int line, Vector<Long> code]
@init{ $code = new Vector<>(); }
@after{$code.toString();}
    :
    (
        o = negation_operators
        t = term5
    ) {
        if( $o.tk_type == TK_INVERT && !($t.typ.equals(INT_TYPE) || $t.typ.equals(FLOAT_TYPE)) ){ //if not inverting an integer or a real
            errorSemantic = true;
            operatorTypeMismatchError($t.typ, $o.text, $t.line, INT_TYPE + " or " + FLOAT_TYPE);
            $typ = INT_TYPE; //propagate the (less restrictive) operation type, whether there's an error or not, to avoid error propagation.
        }
        else if ( $o.tk_type == KW_NO && !($t.typ.equals(BOOL_TYPE)) ){ //if not negating a boolean
            errorSemantic = true;
            operatorTypeMismatchError($t.typ, $o.text, $t.line, BOOL_TYPE);
            $typ = BOOL_TYPE; //propagate the operation type, whether there's an error or not, to avoid error propagation.
        }
        else{ //no error
            $typ = $t.typ;

            $code.addAll($t.code); //expression code

            if($o.tk_type == KW_NO){ //if we're inverting a boolean expression
                $code.add(program.BIPUSH);
                $code.add(1L); //put a 1
                $code.add(program.IXOR); //1 xor any = !any
            }
            else{ //KW_INVERT
                if($t.typ.equals(INT_TYPE)) $code.add(program.INEG);
                else $code.add(program.FNEG);
            }
        }

        $line = $t.line;
    }
    |
    t = term5 {$typ = $t.typ; $line = $t.line; $code = $t.code;};
//term4: (negation_operators term4) | term5;
negation_operators returns [String text, int tk_type]:
    tk = (KW_NO | TK_INVERT) {$text = $tk.text; $tk_type = $tk.type;};

term5 returns [String typ, int line, Vector<Long> code]
@init{$code = new Vector<>();}
@after{$code.toString();}
    :
    direct_evaluation_expr {$typ = $direct_evaluation_expr.typ; $line = $direct_evaluation_expr.line; $code.addAll($direct_evaluation_expr.code);}
    |
    TK_LPAR expr {$typ = $expr.typ; $line = $expr.line; $code = $expr.code;} TK_RPAR;
///////////////////////////////////////////////////////////////////////////////

///////////////////////////// OLD GARBAGE BLOCK //////////////////////////////
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