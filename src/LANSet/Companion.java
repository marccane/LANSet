package LANSet;

import java.util.*;

public class Companion {

    Companion(){
    }

    private static final Set<LANSetParser.C_TYPE> baseTypes = new HashSet<>(Arrays.asList(
            LANSetParser.C_TYPE.CHAR_TYPE,
            LANSetParser.C_TYPE.INT_TYPE,
            LANSetParser.C_TYPE.FLOAT_TYPE,
            LANSetParser.C_TYPE.BOOL_TYPE
    ));

    private static final Map<Integer, LANSetParser.C_TYPE> tokenIDToCType = new HashMap<Integer, LANSetParser.C_TYPE>() {{
        put(LANSetParser.TK_CHARACTER, LANSetParser.C_TYPE.CHAR_TYPE);
        put(LANSetParser.TK_INTEGER, LANSetParser.C_TYPE.INT_TYPE);
        put(LANSetParser.TK_REAL, LANSetParser.C_TYPE.FLOAT_TYPE);
        put(LANSetParser.TK_BOOLEAN, LANSetParser.C_TYPE.BOOL_TYPE);
        put(LANSetParser.TK_STRING, LANSetParser.C_TYPE.STRING_TYPE);
    }};

    /*private static final Map<LANSetParser.C_TYPE, String> cTypeToString = new HashMap<LANSetParser.C_TYPE, String>() {{
        put(LANSetParser.C_TYPE.CHAR_TYPE, LANSetParser.S_CHAR_TYPE);
        put(LANSetParser.C_TYPE.INT_TYPE, LANSetParser.S_INT_TYPE);
        put(LANSetParser.C_TYPE.FLOAT_TYPE, LANSetParser.S_FLOAT_TYPE);
        put(LANSetParser.C_TYPE.BOOL_TYPE, LANSetParser.S_BOOL_TYPE);
        put(LANSetParser.C_TYPE.STRING_TYPE, LANSetParser.S_STRING_TYPE);
    }};*/

    static String bytecodeType(LANSetParser.C_TYPE type){
        String idType = LANSetParser.BYTECODE_VOIDTYPE;
        switch(type){
            case CHAR_TYPE:
                idType = LANSetParser.BYTECODE_CHARTYPE;
                break;
            case INT_TYPE:
                idType = LANSetParser.BYTECODE_INTTYPE;
                break;
            case FLOAT_TYPE:
                idType = LANSetParser.BYTECODE_FLOATTYPE;
                break;
            case BOOL_TYPE:
                idType = LANSetParser.BYTECODE_BOOLTYPE;
                break;
            case STRING_TYPE:
                idType = LANSetParser.BYTECODE_STRINGTYPE;
                break;
            default:
                System.err.println("Invalid bytecodeType " + type);
                break;
        }

        return idType;
    }

    //Pre: cert
    //Post: retorna el C_TYPE que correspon a la ID del token (type)
    static LANSetParser.C_TYPE cTypeFromTokenID(int type){
        assert(type!=LANSetParser.TK_BASETYPE);
        return tokenIDToCType.getOrDefault(type, LANSetParser.C_TYPE.INVALID_TYPE);
    }

    /*static String stringFromCType(LANSetParser.C_TYPE type){
        return cTypeToString.get(type);
    }*/

    static LANSetParser.C_TYPE processBaseType(LANSetParser.C_TYPE type){
        if(isBasetype(type))
            return type;
        else
            return LANSetParser.C_TYPE.INVALID_TYPE;
    }

    static boolean isBasetype(LANSetParser.C_TYPE type){
        return baseTypes.contains(type);
    }

    static void repeatedIdentifierError(String id, int line){
        System.out.println("Semantic error at line " + line + ": the identifier " + id + " is already in use.");
    }

    static void undefinedTypeError(String t, int line){
        System.out.println("Semantic error at line " + line + ": type " + t + " is not defined.");
    }

    static void undefinedIdentifierError(String id, int line){
        System.out.println("Semantic error at line " + line + ": " + id + " is not defined.");
    }

    static void identifierIsNotAVariableError(String id, int line){
        System.out.println("Semantic error at line " + line + ": " + id + " is not a variable.");
    }

    static void nonBasetypeReadingError(String id, LANSetParser.C_TYPE type, int line){
        System.out.println("Semantic error at line " + line + ": Cannot read variable " + id + ". Read operation does not support reading " + type + " variables.");
    }

    static void typeMismatchError(LANSetParser.C_TYPE type1, LANSetParser.C_TYPE type2, int line){
        System.out.println("Type mismatch error at line " + line + ": Type " + type1 + " does not match with " + type2);
    }

    static void typeMismatchError2(String id, int line, String foundType, LANSetParser.C_TYPE expectedType){
        System.out.println("Semantic error: variable " + id + " in line " + line + " is type " + foundType + " but should be " + expectedType +".");
    }

    static void writeOperationUnsupportedTypeError(LANSetParser.C_TYPE type, int line){
        System.out.println("Unsupported type " + type + " at line " + line);
    }

    static void ternaryTypeMismatchError(LANSetParser.C_TYPE t1, LANSetParser.C_TYPE t2, int line){
        System.out.println("Type mismatch error at line " + line + ": Type " + t1 + " does not match with " + t2 + ". Both ternary inner expressions must be the same type.");
    }

    static void operatorTypeMismatchError(LANSetParser.C_TYPE type, String op, int line, LANSetParser.C_TYPE expectedType){
        System.out.println("Type mismatch Error at line " + line + ": operator \'" + op + "\' does not work with \'" + type + "\' expressions. Expected " + expectedType + " instead.");
    }

    static void operatorTypeMismatchError(LANSetParser.C_TYPE type, String op, int line, String expectedType){
        System.out.println("Type mismatch Error at line " + line + ": operator \'" + op + "\' does not work with \'" + type + "\' expressions. Expected " + expectedType + " instead.");
    }

}
