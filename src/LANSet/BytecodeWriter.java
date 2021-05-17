package LANSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import LANSet.Bytecode.Bytecode;

import static LANSet.Bytecode.Constants.*;
import static LANSet.LANSetLexer.*;

//TODO This class should become fully static at some point
class BytecodeWriter {
    
    private Bytecode program;

    BytecodeWriter(Bytecode program){
        this.program = program;
    }

    void addLong(Vector<Long> code, Long l){
        code.add(program.nByte(l,2));
        code.add(program.nByte(l,1));
    }
    
    //Post: code contains the bytecode to create the boolean result of the comparison
    private void addComparisonGlueCode(Vector<Long> code){
        addLong(code, 7L);
        code.add(program.ICONST_0);
        code.add(program.GOTO);
        addLong(code, 4L);
        code.add(program.ICONST_1);
    } 
    
    //Pre: left term is already at the top of the operand stack
    void compareIntegerTypes(Vector<Long> code, LANSetParser.Equality_operatorContext operator, Vector<Long> rightTermCode){
        code.addAll(rightTermCode);
        code.add(compareIntInstructions[operator.tk_type-TK_EQUALS]);
        addComparisonGlueCode(code);
    }

    //Pre: both floats are already at the top of the operand stack
    void compareFloatTypes(Vector<Long> code, LANSetParser.Equality_operatorContext operator){
        if(operator.tk_type == TK_GREATER || operator.tk_type == TK_GREATEREQ)
            code.add(program.FCMPG);
        else
            code.add(program.FCMPL);

        code.add(compareFloatInstructions[operator.tk_type-TK_EQUALS]);
        addComparisonGlueCode(code);
    }

    void initVector(Vector<Long> code, C_TYPE type, int size){
        if(size <= 0){
            //TODO errorSemantic = true;
            System.err.println("Vector size must be a positive integer but was " + size);
        }
        else {
            code.add(program.BIPUSH);
            code.add(Integer.toUnsignedLong(size));
            code.add(program.NEWARRAY);
            code.add(CTypeToJVMTypeID.get(type));
        }
    }
    
    Vector<Long> generateReadCode(C_TYPE type, Long storeDir){
        Vector<Long> code = new Vector<>();
        code.add(Bytecode.INVOKESTATIC);

        switch(type){
            case CHAR_TYPE:
                addLong(code, program.mGetChar);
                code.add(program.ISTORE);
                break;
            case INT_TYPE:
                addLong(code, program.mGetInt);
                code.add(program.ISTORE);
                break;
            case FLOAT_TYPE:
                addLong(code, program.mGetFloat);
                code.add(program.FSTORE);
                break;
            case BOOL_TYPE:
                addLong(code, program.mGetBoolean);
                code.add(program.ISTORE);
                break;
            default:
                //TODO semanticError = true;
                System.err.println("INVALID BYTECODE TYPE");
                break;
        }
        code.add(storeDir);

        return code;
    }

    Vector<Long> generateWriteCode(C_TYPE type){
        Vector<Long> code = new Vector<>();
        code.add(Bytecode.INVOKESTATIC);

        switch(type){
            case CHAR_TYPE:
                addLong(code, program.mPutChar);
                break;
            case INT_TYPE:
                addLong(code, program.mPutInt);
                break;
            case FLOAT_TYPE:
                addLong(code, program.mPutFloat);
                break;
            case BOOL_TYPE:
                addLong(code, program.mPutBoolean);
                break;
            case STRING_TYPE:
                addLong(code, program.mPutString);
                break;
            default:
                //TODO semanticError = true;
                System.err.println("INVALID BYTECODE TYPE");
                break;
        }

        return code;
    }

    //Final attributes
    private final Long[] compareIntInstructions = {program.IF_ICMPEQ, program.IF_ICMPNE, program.IF_ICMPLT,
            program.IF_ICMPLE, program.IF_ICMPGT, program.IF_ICMPGE};

    private final Long[] compareFloatInstructions = {program.IFEQ, program.IFNE, program.IFLT, program.IFLE,
            program.IFGT, program.IFGE};

    private static final Map<C_TYPE, Long> CTypeToJVMTypeID = new HashMap<C_TYPE, Long>() {{
        //put(C_TYPE.BOOL_TYPE, T_BOOLEAN); //TODO eventually use the real types
        //put(C_TYPE.CHAR_TYPE, T_CHAR);
        put(C_TYPE.BOOL_TYPE, T_INT);
        put(C_TYPE.CHAR_TYPE, T_INT);
        put(C_TYPE.INT_TYPE, T_INT);
        put(C_TYPE.FLOAT_TYPE, T_FLOAT);
    }};

}
