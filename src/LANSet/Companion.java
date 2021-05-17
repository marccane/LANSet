package LANSet;

import java.util.*;

import static LANSet.Constants.*;
import static LANSet.LANSetLexer.*;

abstract class Companion {

    private static final Set<C_TYPE> baseTypes = new HashSet<>(Arrays.asList(
            C_TYPE.CHAR_TYPE,
            C_TYPE.INT_TYPE,
            C_TYPE.FLOAT_TYPE,
            C_TYPE.BOOL_TYPE
    ));

    private static final Map<Integer, C_TYPE> tokenIDToCType = new HashMap<Integer, C_TYPE>() {{
        put(TK_CHARACTER, C_TYPE.CHAR_TYPE);
        put(TK_INTEGER, C_TYPE.INT_TYPE);
        put(TK_REAL, C_TYPE.FLOAT_TYPE);
        put(TK_BOOLEAN, C_TYPE.BOOL_TYPE);
        put(TK_STRING, C_TYPE.STRING_TYPE);
    }};

    /*private static final Map<C_TYPE, String> cTypeToString = new HashMap<C_TYPE, String>() {{
        put(C_TYPE.CHAR_TYPE, S_CHAR_TYPE);
        put(C_TYPE.INT_TYPE, S_INT_TYPE);
        put(C_TYPE.FLOAT_TYPE, S_FLOAT_TYPE);
        put(C_TYPE.BOOL_TYPE, S_BOOL_TYPE);
        put(C_TYPE.STRING_TYPE, S_STRING_TYPE);
    }};*/

    static String bytecodeType(C_TYPE type){
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
                System.err.println("Invalid bytecodeType " + type);
                break;
        }

        return idType;
    }

    //Pre: type is an ANTLR token id
    //Post: retorna el C_TYPE que correspon a la ID del token (type)
    static C_TYPE cTypeFromTokenID(int type){
        if(type == TK_BASETYPE) {
            //TODO errorSemantic = true;
            System.err.println("ERROR in cTypeFromTokenID, type == TK_BASETYPE");
        }
        return tokenIDToCType.getOrDefault(type, C_TYPE.INVALID_TYPE);
    }

    /*static String stringFromCType(C_TYPE type){
        return cTypeToString.get(type);
    }*/

    static C_TYPE processBaseType(C_TYPE type){
        if(isBasetype(type))
            return type;
        else
            return C_TYPE.INVALID_TYPE;
    }

    static boolean isBasetype(C_TYPE type){
        return baseTypes.contains(type);
    }

    static boolean isNumberType(C_TYPE t){
        return t == C_TYPE.INT_TYPE || t == C_TYPE.FLOAT_TYPE;
    }

    static void repeatedIdentifierError(String id, int line){
        System.err.println("Semantic error at line " + line + ": the identifier " + id + " is already in use.");
    }

    static void undefinedTypeError(String t, int line){
        System.err.println("Semantic error at line " + line + ": type " + t + " is not defined.");
    }

    static void undefinedIdentifierError(String id, int line){
        System.err.println("Semantic error at line " + line + ": " + id + " is not defined.");
    }

    static void identifierIsNotAVariableError(String id, int line){
        System.err.println("Semantic error at line " + line + ": " + id + " is not a variable.");
    }

    static void nonBasetypeReadingError(String id, C_TYPE type, int line){
        System.err.println("Semantic error at line " + line + ": Cannot read variable " + id + ". Read operation does not support reading " + type + " variables.");
    }

    static void typeMismatchError(C_TYPE type1, C_TYPE type2, int line){
        System.err.println("Type mismatch error at line " + line + ": Type " + type1 + " does not match with " + type2);
    }

    static void typeMismatchError2(String id, int line, String foundType, C_TYPE expectedType){
        System.err.println("Semantic error: variable " + id + " in line " + line + " is type " + foundType + " but should be " + expectedType +".");
    }

    static void writeOperationUnsupportedTypeError(C_TYPE type, int line){
        System.err.println("Unsupported type " + type + " at line " + line);
    }

    static void ternaryTypeMismatchError(C_TYPE t1, C_TYPE t2, int line){
        System.err.println("Type mismatch error at line " + line + ": Type " + t1 + " does not match with " + t2 + ". Both ternary inner expressions must be the same type.");
    }

    static void operatorTypeMismatchError(C_TYPE type, String op, int line, C_TYPE expectedType){
        System.err.println("Type mismatch error at line " + line + ": operator \'" + op + "\' does not work with \'" + type + "\' expressions. Expected " + expectedType + " instead.");
    }

    static void operatorTypeMismatchError(C_TYPE type, String op, int line, String expectedType){
        System.err.println("Type mismatch error at line " + line + ": operator \'" + op + "\' does not work with \'" + type + "\' expressions. Expected " + expectedType + " instead.");
    }

    static void comparisonNotAllowed(int line, C_TYPE t1, C_TYPE t2){
        System.err.println(String.format("Error at line %d. Comparation between type %s and %s is not allowed.", line, t1, t2));
    }

}
