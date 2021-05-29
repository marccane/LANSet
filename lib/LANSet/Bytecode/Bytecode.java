package LANSet.Bytecode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

//	Ajuda a la confeccio de codi en Bytecode. Practiques de MEC
//
//
//
//
//	Classe Bytecode
//
//
//
//
//
//
//
//	Josep Suy, Marc Massot 		Ultima modificacio: 8 de maig de 2007
//
//
//
//

public class Bytecode implements Constants {

//variables auxiliars. Son per inicialitzar el fitxer bytecode

    private Long cThis;
    private Long cException;

    private Long mInit;
    public Long mPutChar;
    public Long mPutFloat;
    public Long mPutInt;
    public Long mPutBoolean;
    public Long mPutString;
    public Long mPutLnString;
    public Long mGetInt;
    public Long mGetFloat;
    public Long mGetChar;
    public Long mGetBoolean;
    private Long mGetString;
    private Long mMain;
    public Long mInitStatic;

    private Long aCode;
    private Long aExceptions;
    private Long aConstantValue;

    //Variables del fitxer bytecode

    private String fileName;

    private Long magic;
    private Long minor_version;
    private Long major_version;
    private Long constant_pool_counter;
    private Vector<cp_info> constant_pool = new Vector<>(50);
    private Long access_flags;
    private Long this_class;
    private Long super_class;
    private Long interface_count;
    //private Vector<Long> interfaces=new Vector<>(5);
    private Long field_count;
    private Vector<field_info> fields = new Vector<>(50);
    private Long method_count;
    private Vector<method_info> methods = new Vector<>(50);
    private Long attribute_count;
    private Vector<attribute_info> attributes = new Vector<>(50);

    private Long CPCercar(String s) {
        long ii = 0L;
        long resultat = 0L;

        Iterator<cp_info> i = constant_pool.iterator();
        while (i.hasNext() && (resultat == 0L)) {
            if (i.next().equal(s)) {
                resultat = ii + 1;
            }
            ii++;
        }
        return resultat;
    }

    public Bytecode(String s) {
        fileName = s;
        magic = 0xCAFEBABEL;
        minor_version = 0L;
        major_version = 49L;
        constant_pool_counter = 0L;
        access_flags = ACC_SUPER;
        interface_count = 0L;
        field_count = 0L;
        method_count = 0L;
        attribute_count = 0L;

        cThis = addClassName(s);
        this_class = cThis;
        Long cObject = addClassName("java/lang/Object");
        super_class = cObject;
        Long cInputStreamReader = addClassName("java/io/InputStreamReader");
        Long cInputStream = addClassName("java/io/InputStream");
        Long cPrintStream = addClassName("java/io/PrintStream");
        Long cBufferedReader = addClassName("java/io/BufferedReader");
        Long cFloat = addClassName("java/lang/Float");
        Long cInteger = addClassName("java/lang/Integer");
        Long cBoolean = addClassName("java/lang/Boolean");
        cException = addClassName("java/lang/Exception");
        Long cSystem = addClassName("java/lang/System");
        Long cString = addClassName("java/lang/String");

        Long mInitObject = addMethodName(cObject, "<init>", "()V");

        mInit = addMethodName(cThis, "<init>", "()V");
        Long mPrintString = addMethodName(cPrintStream, "print", "(Ljava/lang/String;)V");
        Long mPrintLnString = addMethodName(cPrintStream, "println", "(Ljava/lang/String;)V");
        Long mPrintInt = addMethodName(cPrintStream, "print", "(I)V");
        Long mPrintFloat = addMethodName(cPrintStream, "print", "(F)V");
        Long mPrintChar = addMethodName(cPrintStream, "print", "(C)V");
        mPutChar = addMethodName(cThis, "put", "(C)V");
        mPutFloat = addMethodName(cThis, "put", "(F)V");
        mPutInt = addMethodName(cThis, "put", "(I)V");
        mPutBoolean = addMethodName(cThis, "put", "(Z)V");
        mPutString = addMethodName(cThis, "put", "(Ljava/lang/String;)V");
        mPutLnString = addMethodName(cThis, "putln", "(Ljava/lang/String;)V");
        mGetInt = addMethodName(cThis, "getInt", "()I");
        mGetFloat = addMethodName(cThis, "getFloat", "()F");
        mGetChar = addMethodName(cThis, "getChar", "()C");
        mGetBoolean = addMethodName(cThis, "getBoolean", "()Z");
        mGetString = addMethodName(cThis, "getString", "()Ljava/lang/String;");
        mMain = addMethodName(cThis, "main", "([Ljava/lang/String;)V");
        mInitStatic = addMethodName(cThis, "mInitStatic", "()V");

        Long mInitException = addMethodName(cException, "<init>", "(Ljava/lang/String;)V");
        Long mInitFloat = addMethodName(cFloat, "<init>", "(Ljava/lang/String;)V");
        Long mInitInputStreamReader = addMethodName(cInputStreamReader, "<init>", "(Ljava/io/InputStream;)V");
        Long mInitBufferedReader = addMethodName(cBufferedReader, "<init>", "(Ljava/io/Reader;)V");
        Long mReadLineBufferedReader = addMethodName(cBufferedReader, "readLine", "()Ljava/lang/String;");
        Long mParseInt = addMethodName(cInteger, "parseInt", "(Ljava/lang/String;)I");
        Long mParseFloat = addMethodName(cFloat, "parseFloat", "(Ljava/lang/String;)F");
        Long mValueOfInt = addMethodName(cInteger, "valueOf", "(I)Ljava/lang/Integer;");
        Long mValueOfFloat = addMethodName(cFloat, "valueOf", "(F)Ljava/lang/Float;");
        Long mRead = addMethodName(cInputStream, "read", "()I");
        Long mCharAt = addMethodName(cString, "charAt", "(I)C");

        Long sCert = addConstant("S", "Cert");
        Long sFals = addConstant("S", "Fals");
        Long sEmpty = addConstant("S", "");

        aCode = addAttributeName("Code");
        aExceptions = addAttributeName("Exceptions");
        aConstantValue = addAttributeName("ConstantValue");

        //Static fields
        Long fGlobalBufferedReader = addStaticFieldName(cThis,"$globalBufferedReader", "Ljava/io/BufferedReader;");

        //Constant fields
        Long fIn = addFieldName(cSystem, "in", "Ljava/io/InputStream;");
        Long fOut = addFieldName(cSystem, "out", "Ljava/io/PrintStream;");

        Vector<Long> codeInitStatic = new Vector<>(30);
        codeInitStatic.add(NEW);
        codeInitStatic.add(0L);
        codeInitStatic.add(cInputStreamReader);

        codeInitStatic.add(DUP); //par1 inputStreamReader ref

        codeInitStatic.add(GETSTATIC); //par2 System.in
        codeInitStatic.add(0L);
        codeInitStatic.add(fIn);

        codeInitStatic.add(INVOKESPECIAL); //init
        codeInitStatic.add(0L);
        codeInitStatic.add(mInitInputStreamReader);

        codeInitStatic.add(ASTORE_0);  //store inputStreamReader ref

        codeInitStatic.add(NEW);
        codeInitStatic.add(0L);
        codeInitStatic.add(cBufferedReader);

        codeInitStatic.add(DUP);  //par 1 cBuff ref

        codeInitStatic.add(ALOAD_0); //par 2 inputStreamReader ref

        codeInitStatic.add(INVOKESPECIAL);
        codeInitStatic.add(0L);
        codeInitStatic.add(mInitBufferedReader);

        codeInitStatic.add(PUTSTATIC);
        codeInitStatic.add(0L);
        codeInitStatic.add(fGlobalBufferedReader);

        codeInitStatic.add(RETURN);
        addFunctionCode(mInitStatic, 77L, 7L, codeInitStatic);

        Vector<Long> codePutInt = new Vector<>(8);
        codePutInt.add(GETSTATIC);
        codePutInt.add(0L);
        codePutInt.add(fOut);
        codePutInt.add(ILOAD_0);
        codePutInt.add(INVOKEVIRTUAL);
        codePutInt.add(0L);
        codePutInt.add(mPrintInt);
        codePutInt.add(RETURN);
        addFunctionCode(mPutInt, 2L, 1L, codePutInt);


        Vector<Long> codePutFloat = new Vector<>(8);
        codePutFloat.add(GETSTATIC);
        codePutFloat.add(0L);
        codePutFloat.add(fOut);
        codePutFloat.add(FLOAD_0);
        codePutFloat.add(INVOKEVIRTUAL);
        codePutFloat.add(0L);
        codePutFloat.add(mPrintFloat);
        codePutFloat.add(RETURN);
        addFunctionCode(mPutFloat, 2L, 1L, codePutFloat);


        Vector<Long> codePutChar = new Vector<>(8);
        codePutChar.add(GETSTATIC);
        codePutChar.add(0L);
        codePutChar.add(fOut);
        codePutChar.add(ILOAD_0);
        codePutChar.add(INVOKEVIRTUAL);
        codePutChar.add(0L);
        codePutChar.add(mPrintChar);
        codePutChar.add(RETURN);
        addFunctionCode(mPutChar, 2L, 1L, codePutChar);


        Vector<Long> codePutBoolean = new Vector<>(18);
        codePutBoolean.add(GETSTATIC);
        codePutBoolean.add(0L);
        codePutBoolean.add(fOut);
        codePutBoolean.add(ILOAD_0);
        codePutBoolean.add(IFEQ);
        codePutBoolean.add(0L);
        codePutBoolean.add(8L);
        codePutBoolean.add(LDC);
        codePutBoolean.add(sCert);
        codePutBoolean.add(GOTO);
        codePutBoolean.add(0L);
        codePutBoolean.add(5L);
        codePutBoolean.add(LDC);
        codePutBoolean.add(sFals);
        codePutBoolean.add(INVOKEVIRTUAL);
        codePutBoolean.add(0L);
        codePutBoolean.add(mPrintString);
        codePutBoolean.add(RETURN);
        addFunctionCode(mPutBoolean, 2L, 1L, codePutBoolean);


        Vector<Long> codePutString = new Vector<>(8);
        codePutString.add(GETSTATIC);
        codePutString.add(0L);
        codePutString.add(fOut);
        codePutString.add(ALOAD_0);
        codePutString.add(INVOKEVIRTUAL);
        codePutString.add(0L);
        codePutString.add(mPrintString);
        codePutString.add(RETURN);
        addFunctionCode(mPutString, 2L, 1L, codePutString);


        Vector<Long> codePutLnString = new Vector<>(8);
        codePutLnString.add(GETSTATIC);
        codePutLnString.add(0L);
        codePutLnString.add(fOut);
        codePutLnString.add(ALOAD_0);
        codePutLnString.add(INVOKEVIRTUAL);
        codePutLnString.add(0L);
        codePutLnString.add(mPrintLnString);
        codePutLnString.add(RETURN);
        addFunctionCode(mPutLnString, 2L, 1L, codePutLnString);


        Vector<Long> codeGetInt = new Vector<>(28);
        codeGetInt.add(GETSTATIC);
        codeGetInt.add(0L);
        codeGetInt.add(fGlobalBufferedReader);
        codeGetInt.add(INVOKEVIRTUAL);
        codeGetInt.add(0L);
        codeGetInt.add(mReadLineBufferedReader);
        codeGetInt.add(INVOKESTATIC);
        codeGetInt.add(0L);
        codeGetInt.add(mParseInt);
        codeGetInt.add(IRETURN);
        addFunctionCode(mGetInt, 77L, 7L, codeGetInt);


        Vector<Long> codeGetFloat = new Vector<>(28);
        codeGetFloat.add(FCONST_0);
        codeGetFloat.add(FSTORE_0);
        codeGetFloat.add(NEW);
        codeGetFloat.add(0L);
        codeGetFloat.add(cInputStreamReader);
        codeGetFloat.add(DUP);
        codeGetFloat.add(GETSTATIC);
        codeGetFloat.add(0L);
        codeGetFloat.add(fIn);
        codeGetFloat.add(INVOKESPECIAL);
        codeGetFloat.add(0L);
        codeGetFloat.add(mInitInputStreamReader);
        codeGetFloat.add(ASTORE_1);
        codeGetFloat.add(NEW);
        codeGetFloat.add(0L);
        codeGetFloat.add(cBufferedReader);
        codeGetFloat.add(DUP);
        codeGetFloat.add(ALOAD_1);
        codeGetFloat.add(INVOKESPECIAL);
        codeGetFloat.add(0L);
        codeGetFloat.add(mInitBufferedReader);
        codeGetFloat.add(ASTORE_2);
        codeGetFloat.add(ALOAD_2);
        codeGetFloat.add(INVOKEVIRTUAL);
        codeGetFloat.add(0L);
        codeGetFloat.add(mReadLineBufferedReader);
        codeGetFloat.add(ASTORE_3);
        codeGetFloat.add(ALOAD_3);
        codeGetFloat.add(INVOKESTATIC);
        codeGetFloat.add(0L);
        codeGetFloat.add(mParseFloat);
        codeGetFloat.add(FSTORE_0);
        codeGetFloat.add(FLOAD_0);
        codeGetFloat.add(FRETURN);
        addFunctionCode(mGetFloat, 3L, 4L, codeGetFloat);


        Vector<Long> codeGetChar = new Vector<>(28);
        codeGetChar.add(ICONST_0);
        codeGetChar.add(ISTORE_0);
        codeGetChar.add(NEW);
        codeGetChar.add(0L);
        codeGetChar.add(cInputStreamReader);
        codeGetChar.add(DUP);
        codeGetChar.add(GETSTATIC);
        codeGetChar.add(0L);
        codeGetChar.add(fIn);
        codeGetChar.add(INVOKESPECIAL);
        codeGetChar.add(0L);
        codeGetChar.add(mInitInputStreamReader);
        codeGetChar.add(ASTORE_1);
        codeGetChar.add(NEW);
        codeGetChar.add(0L);
        codeGetChar.add(cBufferedReader);
        codeGetChar.add(DUP);
        codeGetChar.add(ALOAD_1);
        codeGetChar.add(INVOKESPECIAL);
        codeGetChar.add(0L);
        codeGetChar.add(mInitBufferedReader);
        codeGetChar.add(ASTORE_2);
        codeGetChar.add(ALOAD_2);
        codeGetChar.add(INVOKEVIRTUAL);
        codeGetChar.add(0L);
        codeGetChar.add(mReadLineBufferedReader);
        codeGetChar.add(ASTORE_3);
        codeGetChar.add(ALOAD_3);
        codeGetChar.add(ICONST_0);
        codeGetChar.add(INVOKEVIRTUAL);
        codeGetChar.add(0L);
        codeGetChar.add(mCharAt);
        codeGetChar.add(ISTORE_0);
        codeGetChar.add(ILOAD_0);
        codeGetChar.add(IRETURN);
        addFunctionCode(mGetChar, 3L, 4L, codeGetChar);


        Vector<Long> codeGetString = new Vector<>(28);
        codeGetString.add(LDC);
        codeGetString.add(sEmpty);
        codeGetString.add(ASTORE_0);
        codeGetString.add(NEW);
        codeGetString.add(0L);
        codeGetString.add(cInputStreamReader);
        codeGetString.add(DUP);
        codeGetString.add(GETSTATIC);
        codeGetString.add(0L);
        codeGetString.add(fIn);
        codeGetString.add(INVOKESPECIAL);
        codeGetString.add(0L);
        codeGetString.add(mInitInputStreamReader);
        codeGetString.add(ASTORE_1);
        codeGetString.add(NEW);
        codeGetString.add(0L);
        codeGetString.add(cBufferedReader);
        codeGetString.add(DUP);
        codeGetString.add(ALOAD_1);
        codeGetString.add(INVOKESPECIAL);
        codeGetString.add(0L);
        codeGetString.add(mInitBufferedReader);
        codeGetString.add(ASTORE_2);
        codeGetString.add(ALOAD_2);
        codeGetString.add(INVOKEVIRTUAL);
        codeGetString.add(0L);
        codeGetString.add(mReadLineBufferedReader);
        codeGetString.add(ASTORE_3);
        codeGetString.add(ALOAD_3);
        codeGetString.add(ASTORE_0);
        codeGetString.add(ALOAD_0);
        codeGetString.add(ARETURN);
        addFunctionCode(mGetString, 3L, 4L, codeGetString);


        Vector<Long> codeGetBoolean = new Vector<>(8);
        codeGetBoolean.add(INVOKESTATIC);
        codeGetBoolean.add(0L);
        codeGetBoolean.add(mGetString);
        codeGetBoolean.add(ASTORE_1);
        codeGetBoolean.add(ALOAD_1);
        codeGetBoolean.add(ICONST_0);
        codeGetBoolean.add(INVOKEVIRTUAL);
        codeGetBoolean.add(0L);
        codeGetBoolean.add(mCharAt);
        codeGetBoolean.add(BIPUSH);
        codeGetBoolean.add(67L);
        codeGetBoolean.add(IF_ICMPNE);
        codeGetBoolean.add(0L);
        codeGetBoolean.add(8L);
        codeGetBoolean.add(ICONST_1);
        codeGetBoolean.add(ISTORE_0);
        codeGetBoolean.add(GOTO);
        codeGetBoolean.add(0L);
        codeGetBoolean.add(5L);
        codeGetBoolean.add(ICONST_0);
        codeGetBoolean.add(ISTORE_0);
        codeGetBoolean.add(ILOAD_0);
        codeGetBoolean.add(IRETURN);
        addFunctionCode(mGetBoolean, 3L, 2L, codeGetBoolean);
/*

	Vector<Long> codeGetBoolean=new Vector<Long>(8);
  	codeGetBoolean.add(INVOKESTATIC);
   	codeGetBoolean.add(0L);
   	codeGetBoolean.add(mGetString);
   	codeGetBoolean.add(ASTORE_1);
   	codeGetBoolean.add(ALOAD_1);
   	codeGetBoolean.add(LDC);
   	codeGetBoolean.add(sCert);	
   	codeGetBoolean.add(IF_ACMPNE);
   	codeGetBoolean.add(0L);
   	codeGetBoolean.add(8L);
  	codeGetBoolean.add(ICONST_1);
  	codeGetBoolean.add(ISTORE_0);
   	codeGetBoolean.add(GOTO);
   	codeGetBoolean.add(0L);
   	codeGetBoolean.add(5L);
  	codeGetBoolean.add(ICONST_0);
  	codeGetBoolean.add(ISTORE_0);
   	codeGetBoolean.add(ILOAD_0);
   	codeGetBoolean.add(IRETURN);
	addFunctionCode(mGetBoolean,2L,2L,codeGetBoolean);

*/

        Vector<Long> codeInit = new Vector<>(8);
        codeInit.add(ALOAD_0);
        codeInit.add(INVOKESPECIAL);
        codeInit.add(0L);
        codeInit.add(mInitObject);
        codeInit.add(RETURN);
        addFunctionCode(mInit, 1L, 1L, codeInit);
    }

    public void write() {
        try {
            FileOutputStream f = new FileOutputStream(fileName + ".class");

            f.write(this.toByte(magic, 4));
            f.write(this.toByte(minor_version, 2));
            f.write(this.toByte(major_version, 2));
            constant_pool_counter = (long) (constant_pool.size() + 1);
            f.write(this.toByte(constant_pool_counter, 2));
            for (LANSet.Bytecode.cp_info cp_info : constant_pool) {
                cp_info.write(f);
            }
            f.write(this.toByte(access_flags, 2));
            f.write(this.toByte(this_class, 2));
            f.write(this.toByte(super_class, 2));
            //interface_count=new Long(interfaces.size());
            interface_count = 0L;
            f.write(this.toByte(interface_count, 2));

            field_count = (long) fields.size();
            f.write(this.toByte(field_count, 2));
            for (field_info field : fields) {
                field.write(f);
            }

            method_count = (long) methods.size();
            f.write(this.toByte(method_count, 2));
            for (method_info method : methods) {
                method.write(f);
            }
            attribute_count = (long) attributes.size();
            f.write(this.toByte(attribute_count, 2));

            for (attribute_info attribute : attributes) {
                attribute.write(f);
            }
        } catch (IOException s) {s.printStackTrace();}

    }

    public void show() {

        System.out.println("-----------------------------------------------------------------");
        System.out.println("Fitxer        : " + fileName + ".class");
        //System.out.println("Numero Magic  : "+magic);
        //System.out.println("Minor_version : "+minor_version);
        //System.out.println("Major_version : "+major_version);
        System.out.println("-----------------------------------------------------------------");
        constant_pool_counter = (long) (constant_pool.size() + 1);
        System.out.print("CONSTANT POOL");
        System.out.println("         Elements : " + (constant_pool_counter - 1));
        System.out.println("-----------------------------------------------------------------");
        Iterator<cp_info> itercp = constant_pool.iterator();
        int i = 1;
        while (itercp.hasNext()) {
            System.out.print("   " + i + "	: ");
            itercp.next().show();
            System.out.println();
            i++;
        }
/*	System.out.println("-----------------------------------------------------------------");
	field_count=new Long(fields.size());
	System.out.print("FIELDS        ");System.out.println("       Elements : "+field_count);
	System.out.println("-----------------------------------------------------------------");
	i=1;
	Iterator<field_info> iterf=fields.iterator();
	while (iterf.hasNext()) {
		System.out.print("   "+i + "	: ");
		iterf.next().show();
		System.out.println();i++;
	}
*/
        System.out.println("-----------------------------------------------------------------");
        method_count = (long) methods.size();
        System.out.print("METHODS       ");
        System.out.println("         Elements : " + method_count);
        System.out.println("-----------------------------------------------------------------");
        i = 1;
        for (method_info method : methods) {
            System.out.print("   " + i + "	: ");
            method.show();
            System.out.println();
            i++;
        }
/*	System.out.println("-----------------------------------------------------------------");
	attribute_count=new Long(attributes.size());
	System.out.print("ATTRIBUTES      ");System.out.println("         Elements : "+attribute_count);
	System.out.println("-----------------------------------------------------------------");
	i=1;
	Iterator<attribute_info> itera=attributes.iterator();
	while (itera.hasNext()) {
		System.out.print("   "+i + "	: ");
		itera.next().show();
		System.out.println();i++;
	}
*/
    }

    private Long addClassName(String s) {
        CONSTANT_Class_info x;
        CONSTANT_Utf8_info y;
        long i;

        y = new CONSTANT_Utf8_info(s);
        constant_pool.add(y);
        i = constant_pool.size();
        x = new CONSTANT_Class_info(i);
        constant_pool.add(x);
        i = constant_pool.size();
        return (i);
    }

    private Long addMethodName(Long class_idx, String name, String desc) {
        CONSTANT_Utf8_info n, d;
        CONSTANT_NameAndType_info x;
        CONSTANT_Methodref_info y;
        long i, j, k, l;

        i = CPCercar(name);
        if (i == 0L) {
            n = new CONSTANT_Utf8_info(name);
            constant_pool.add(n);
            i = constant_pool.size();
        }
        j = CPCercar(desc);
        if (j == 0L) {
            d = new CONSTANT_Utf8_info(desc);
            constant_pool.add(d);
            j = constant_pool.size();
        }
        x = new CONSTANT_NameAndType_info(i, j);
        constant_pool.add(x);
        k = constant_pool.size();
        y = new CONSTANT_Methodref_info(class_idx, k);
        constant_pool.add(y);
        l = constant_pool.size();

        return (l);
    }

    private Long addFieldName(Long class_idx, String name, String desc) {
        CONSTANT_Utf8_info n, d;
        CONSTANT_NameAndType_info x;
        CONSTANT_Fieldref_info y;
        long i, j, k, l;

        i = CPCercar(name);
        if (i == 0L) {
            n = new CONSTANT_Utf8_info(name);
            constant_pool.add(n);
            i = constant_pool.size();
        }
        j = CPCercar(desc);
        if (j == 0L) {
            d = new CONSTANT_Utf8_info(desc);
            constant_pool.add(d);
            j = constant_pool.size();
        }
        x = new CONSTANT_NameAndType_info(i, j);
        constant_pool.add(x);
        k = constant_pool.size();
        y = new CONSTANT_Fieldref_info(class_idx, k);
        constant_pool.add(y);
        l = constant_pool.size();

        return l;
    }

    private Long addStaticFieldName(Long class_idx, String name, String desc) {
        CONSTANT_Utf8_info n, d;
        CONSTANT_NameAndType_info x;
        CONSTANT_Fieldref_info y;
        long idx_name, idx_desc, k, l;

        idx_name = CPCercar(name);
        if (idx_name == 0L) {
            n = new CONSTANT_Utf8_info(name);
            constant_pool.add(n);
            idx_name = constant_pool.size();
        }
        idx_desc = CPCercar(desc);
        if (idx_desc == 0L) {
            d = new CONSTANT_Utf8_info(desc);
            constant_pool.add(d);
            idx_desc = constant_pool.size();
        }
        x = new CONSTANT_NameAndType_info(idx_name, idx_desc);
        constant_pool.add(x);
        k = constant_pool.size();

        y = new CONSTANT_Fieldref_info(class_idx, k);
        constant_pool.add(y);
        l = constant_pool.size();

        Long access_flags = ACC_STATIC;
        field_info z = new field_info(access_flags, idx_name, idx_desc);
        fields.add(z);

        return l;
    }

    private Long addAttributeName(String val10) {
        CONSTANT_Utf8_info n;
        long i;

        n = new CONSTANT_Utf8_info(val10);
        constant_pool.add(n);
        i = constant_pool.size();
        return i;
    }

    public Long addFunctionDef(String name, String desc) {
        return addMethodName(cThis, name, desc);
    }

    public void addFunctionCode(Long ref, Long maxStack, Long nLocals, Vector<Long> code) {
        CONSTANT_Methodref_info x;
        CONSTANT_NameAndType_info y;
        method_info z;

        x = (CONSTANT_Methodref_info) constant_pool.elementAt(new Integer(ref.toString()) - 1);
        y = (CONSTANT_NameAndType_info) constant_pool.get(new Integer(x.name_and_type_index.toString()) - 1);
        Long acc = ACC_PUBLIC | ACC_STATIC;
        if (ref == mInit) {
            acc = ACC_PUBLIC;
        }
        z = new method_info(aCode, aExceptions, cException, acc, y.name_index, y.descriptor_index, maxStack, nLocals, code);
        methods.add(z);
    }

    public void addMainCode(Long maxStack, Long nLocals, Vector<Long> code) {
        CONSTANT_Methodref_info x;
        CONSTANT_NameAndType_info y;
        method_info z;

        x = (CONSTANT_Methodref_info) constant_pool.get(new Integer(mMain.toString()) - 1);
        y = (CONSTANT_NameAndType_info) constant_pool.get(new Integer(x.name_and_type_index.toString()) - 1);
        Long acc = ACC_PUBLIC | ACC_STATIC;
        z = new method_info(aCode, aExceptions, cException, acc, y.name_index, y.descriptor_index, maxStack, nLocals, code);
        methods.add(z);

    }

    public Long addConstName(String name, String desc, String val) {
        CONSTANT_Utf8_info n, d;
        long i, j, k;
        field_info z;

        i = CPCercar(name);
        if (i == 0L) {
            n = new CONSTANT_Utf8_info(name);
            constant_pool.add(n);
            i = constant_pool.size();
        }
        j = CPCercar(desc);
        if (j == 0L) {
            d = new CONSTANT_Utf8_info(desc);
            constant_pool.add(d);
            j = constant_pool.size();
        }
        k = addConstant(desc, val);
        Long acc = ACC_FINAL | ACC_STATIC;
        z = new field_info(aConstantValue, acc, i, j, k);
        fields.add(z);
        return k;
    }

    public Long addConstant(String desc, String val) {
        long ret;

        ret = 99999L;
        switch (desc.charAt(0)) {
            case 'I': {
                Integer i = new Integer(val);
                CONSTANT_Integer_info x = new CONSTANT_Integer_info(new Long(i));
                constant_pool.add(x);
                ret = constant_pool.size();
            }
            break;
            case 'Z': {
                Integer i = val == "Cert" ? 1 : 0; //WARNING is this correct?
                CONSTANT_Integer_info x = new CONSTANT_Integer_info(new Long(i));
                constant_pool.add(x);
                ret = constant_pool.size();
            }
            break;
            case 'C': {
                Integer i = (int) (val.charAt(0));
                CONSTANT_Integer_info x = new CONSTANT_Integer_info(new Long(i));
                constant_pool.add(x);
                ret = constant_pool.size();
            }
            break;
            case 'F': {
                Float r;
                r = Float.parseFloat(val);
                CONSTANT_Float_info x = new CONSTANT_Float_info(r);
                constant_pool.add(x);
                ret = constant_pool.size();
            }
            break;
            case 'S': {
                CONSTANT_Utf8_info x = new CONSTANT_Utf8_info(val);
                constant_pool.add(x);
                Long i = (long) constant_pool.size();
                CONSTANT_String_info y = new CONSTANT_String_info(i);
                constant_pool.add(y);
                ret = constant_pool.size();
            }
        }
        return ret;
    }

    public Long addArrayDef(int dimensions, String desc) {
        String s;
        int i;

        s = "";
        for (i = 0; i < dimensions; i++) s = s + "[";
        s = s + desc;
        return addClassName(s);
    }

    private byte[] toByte(Long num, int t) {
        int i;
        Long aux;

        byte[] bytes = new byte[t];
        aux = num;
        i = t - 1;
        while (i >= 0) {
            bytes[i] = aux.byteValue();
            aux = Long.rotateRight(aux, 8);
            i--;
        }
        return bytes;
    }

    public Long nByte(Long num, int t) {
        int i, j;
        Long aux;

        byte[] bytes = new byte[4];
        aux = num;
        i = 3;
        while (i >= 0) {
            bytes[i] = aux.byteValue();
            aux = Long.rotateRight(aux, 8);
            i--;
        }
        j = bytes[4 - t];
        return ((long) j);
    }

}