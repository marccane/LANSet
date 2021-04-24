package LANSet;

import LANSet.Bytecode.Bytecode;

import java.util.Vector;

class BytecodeWriter {

    Bytecode program; //aixo és necessari perquè la classe Bytecode necessita ser instanciada per accedir a metodes que podrien ser estatica
    //Si no fos per aixo, aquesta classe podria ser estatica
    //Companion companion;

    BytecodeWriter(Bytecode program /*, Companion companion*/){
        this.program = program;
        //this.companion = companion;
    }

    //bytecode types
    static final String BYTECODE_VOIDTYPE = "V";
    static final String BYTECODE_CHARTYPE = "C";
    static final String BYTECODE_INTTYPE = "I";
    static final String BYTECODE_FLOATTYPE = "F";
    static final String BYTECODE_BOOLTYPE = "Z";
    static final String BYTECODE_STRINGTYPE = "S";

    private Vector<Long> generateReadCode(C_TYPE type, Long storeDir){
        Vector<Long> code = new Vector<>();

        code.add(Bytecode.INVOKESTATIC);

        switch(type){
            case CHAR_TYPE:
                code.add(program.nByte(program.mGetChar,2));
                code.add(program.nByte(program.mGetChar,1));
                code.add(program.ISTORE);
                break;
            case INT_TYPE:
                code.add(program.nByte(program.mGetInt,2));
                code.add(program.nByte(program.mGetInt,1));
                code.add(program.ISTORE);
                break;
            case FLOAT_TYPE:
                code.add(program.nByte(program.mGetFloat,2));
                code.add(program.nByte(program.mGetFloat,1));
                code.add(program.FSTORE);
                break;
            case BOOL_TYPE:
                code.add(program.nByte(program.mGetBoolean,2));
                code.add(program.nByte(program.mGetBoolean,1));
                code.add(program.ISTORE);
                break;
            default:
                System.err.println("INVALID BYTECODE TYPE");
                break;
        }
        code.add(storeDir);

        return code;
    }

    private Vector<Long> generateWriteCode(C_TYPE type){
        Vector<Long> code = new Vector<>();

        code.add(Bytecode.INVOKESTATIC);

        switch(type){
            case CHAR_TYPE:
                code.add(program.nByte(program.mPutChar,2));
                code.add(program.nByte(program.mPutChar,1));
                break;
            case INT_TYPE:
                code.add(program.nByte(program.mPutInt,2));
                code.add(program.nByte(program.mPutInt,1));
                break;
            case FLOAT_TYPE:
                code.add(program.nByte(program.mPutFloat,2));
                code.add(program.nByte(program.mPutFloat,1));
                break;
            case BOOL_TYPE:
                code.add(program.nByte(program.mPutBoolean,2));
                code.add(program.nByte(program.mPutBoolean,1));
                break;
            case STRING_TYPE:
                code.add(program.nByte(program.mPutString,2));
                code.add(program.nByte(program.mPutString,1));
                break;
            default:
                System.err.println("INVALID BYTECODE TYPE");
                break;
        }

        return code;
    }

    Vector<Long> write_operation(C_TYPE eType, int eLine, Vector<Long> eCode){
        Vector<Long> code = new Vector<>();

        code.addAll(eCode);
        code.addAll(generateWriteCode(eType));

        return code;
    }
}
