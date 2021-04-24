package LANSet;

import static LANSet.Constants.*;

public enum C_TYPE {
    INVALID_TYPE,
    CHAR_TYPE,
    INT_TYPE,
    FLOAT_TYPE,
    BOOL_TYPE,
    STRING_TYPE;
    @Override
    public String toString() {
        switch(this) {
            case INVALID_TYPE: return S_INVALID_TYPE;
            case CHAR_TYPE: return S_CHAR_TYPE;
            case INT_TYPE: return S_INT_TYPE;
            case FLOAT_TYPE: return S_FLOAT_TYPE;
            case BOOL_TYPE: return S_BOOL_TYPE;
            case STRING_TYPE: return S_STRING_TYPE;
            default: throw new IllegalArgumentException();
        }
    }

    public static C_TYPE fromString(String typeString){
        switch (typeString){
            case S_INVALID_TYPE: return C_TYPE.INVALID_TYPE;
            case S_CHAR_TYPE: return C_TYPE.CHAR_TYPE;
            case S_INT_TYPE: return C_TYPE.INT_TYPE;
            case S_FLOAT_TYPE: return C_TYPE.FLOAT_TYPE;
            case S_BOOL_TYPE: return C_TYPE.BOOL_TYPE;
            case S_STRING_TYPE: return C_TYPE.STRING_TYPE;
            default: throw new IllegalArgumentException();
        }
    }
}