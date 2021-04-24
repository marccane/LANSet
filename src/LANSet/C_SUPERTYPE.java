package LANSet;

import static LANSet.Constants.*;

public enum C_SUPERTYPE {
    INVALID_SUPERTYPE,
    CONSTANT_SUPERTYPE,
    VARIABLE_SUPERTYPE,
    ALIAS_SUPERTYPE,
    FUNCTION_SUPERTYPE,
    ACTION_SUPERTYPE,
    TUPLE_SUPERTYPE,
    VECTOR_SUPERTYPE;

    @Override
    public String toString() {
        switch(this) {
            case INVALID_SUPERTYPE: return S_INVALID_SUPERTYPE;
            case CONSTANT_SUPERTYPE: return S_CONSTANT_SUPERTYPE;
            case VARIABLE_SUPERTYPE: return S_VARIABLE_SUPERTYPE;
            case ALIAS_SUPERTYPE: return S_ALIAS_SUPERTYPE;
            case FUNCTION_SUPERTYPE: return S_FUNCTION_SUPERTYPE;
            case ACTION_SUPERTYPE: return S_ACTION_SUPERTYPE;
            case TUPLE_SUPERTYPE: return S_TUPLE_SUPERTYPE;
            case VECTOR_SUPERTYPE: return S_VECTOR_SUPERTYPE;
            default: throw new IllegalArgumentException();
        }
    }
}