package LANSet;

import org.antlr.v4.runtime.CharStream;


import LANSet.Bytecode.CMethod;

//Extend LANSetLexer as a workaround to get the declarations of the tokens (TK_X) and keywords (KW_X)
//As a result of this we get a bit of namespace pollution (18 public members)
//We could also access them directly doing LANSetLexer.TK_X, since they are public static
public class Helper extends LANSetLexer { //TODO pensar un nom decent per aixo

    //TODO FIX THIS
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

    public Helper(CharStream _dont_use) throws Exception { //it's obligatory to implement this, don't use
        super(null);
        throw new Exception("This constructor must not be used");
    }

    public static String bytecodeType(String type){
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

    public static String processBaseType(String type){
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

    public static String getStringTypeFromTKIndex(int index){
        String res = INVALID_TYPE;
        if(index == LANSetLexer.TK_INTEGER) res = INT_TYPE;
        else if(index == LANSetLexer.TK_CHARACTER) res = CHAR_TYPE;
        else if(index == LANSetLexer.TK_BOOLEAN) res = BOOL_TYPE;
        else if(index == LANSetLexer.TK_REAL) res = FLOAT_TYPE;
        else if(index == LANSetLexer.TK_STRING) res = STRING_TYPE; //sha de fer algo mes?
        return res;
    }

    public static boolean isBasetype(String type){
        return type.equals(CHAR_TYPE) || type.equals(INT_TYPE) || type.equals(FLOAT_TYPE) || type.equals(BOOL_TYPE);
    }

    public static void repeatedIdentifierError(String id, int line){
        System.out.println("Semantic error at line " + line + ": the identifier " + id + " is already in use.");
    }

    public static void undefinedTypeError(String t, int line){
        System.out.println("Semantic error at line " + line + ": type " + t + " is not defined.");
    }

    public static void undefinedIdentifierError(String id, int line){
        System.out.println("Semantic error at line " + line + ": " + id + " is not defined.");
    }

    public static void identifierIsNotAVariableError(String id, int line){
        System.out.println("Semantic error at line " + line + ": " + id + " is not a variable.");
    }

    public static void nonBasetypeReadingError(String id, String type, int line){
        System.out.println("Semantic error at line " + line + ": Cannot read variable " + id + ". Read operation does not support reading " + type + " variables.");
    }

    public static void typeMismatchError(String type1, String type2, int line){
        System.out.println("Type mismatch error at line " + line + ": Type " + type1 + " does not match with " + type2);
    }

    public static void typeMismatchError2(String id, int line, String foundType, String expectedType){
        System.out.println("Semantic error: variable " + id + " in line " + line + " is type " + foundType + " but should be " + expectedType +".");
    }

    public static void writeOperationUnsupportedTypeError(String type, int line){
        System.out.println("Unsupported type " + type + " at line " + line);
    }

    public static void ternaryTypeMismatchError(String t1, String t2, int line){
        System.out.println("Type mismatch error at line " + line + ": Type " + t1 + " does not match with " + t2 + ". Both ternary inner expressions must be the same type.");
    }

    public static void operatorTypeMismatchError(String type, String op, int line, String expected){
        System.out.println("Type mismatch Error at line " + line + ": operator \'" + op + "\' does not work with \'" + type + "\' expressions. Expected " + expected + " instead.");
    }

}
