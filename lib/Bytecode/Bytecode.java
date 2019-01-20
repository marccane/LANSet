import java.io.*;
import java.util.*;


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


class Bytecode implements Constants {

//variables auxiliars. Son per inicialitzar el fitxer bytecode

        Long cThis=0L;
        Long cObject=0L;
        Long cInputStream;
        Long cInputStreamReader;
        Long cPrintStream;
        Long cBufferedReader;
        Long cFloat;
        Long cInteger;
        Long cBoolean;
        Long cException;
        Long cSystem;
        Long cString;



        Long mInitObject;
        Long mInit;
        Long mPrintString;
        Long mPrintLnString;
        Long mPrintInt;
        Long mPrintFloat;
        Long mPrintChar;
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
        public Long mGetString;
        Long mMain;
        Long mInitException;
        Long mInitFloat;
        Long mInitInputStreamReader;
        Long mInitBufferedReader;
        Long mReadLineBufferedReader;
	Long mParseInt;
	Long mParseFloat;
	Long mValueOfInt;
	Long mValueOfFloat;
	Long mRead;
	Long mCharAt;




        Long sErrorEnter;
        Long sErrorReal;
        Long sCert;
        Long sFals;
        Long sEmpty;

        Long aCode;
        Long aExceptions;
        Long aConstantValue;

        Long fIn;
        Long fOut;

//Variables del fitxer bytecode

	String fileName;

	Long magic;
	Long minor_version;
	Long major_version;
	Long constant_pool_counter;
	Vector<cp_info> constant_pool=new Vector<cp_info>(50);
	Long access_flags;
	Long this_class;
	Long super_class;
	Long interface_count;
	//Vector<Long> interfaces=new Vector<Long>(5);
	Long field_count;
	Vector<field_info> fields=new Vector<field_info>(50);
	Long method_count;
	Vector<method_info> methods=new Vector<method_info>(50);
	Long attribute_count;
	Vector<attribute_info> attributes=new Vector<attribute_info>(50);



private Long CPCercar(String s) {
	Long ii=0L;
	Long resultat=0L;

	Iterator<cp_info> i=constant_pool.iterator();
	while (i.hasNext() && (resultat==0L)){
		if (i.next().equal(s)) {resultat=ii+1;};
		ii++;
	}
	return resultat;
};



Bytecode (String s) {


	fileName=s;
	magic=0xCAFEBABEL;
	minor_version=0L;
	major_version=49L;
	constant_pool_counter=0L;
	access_flags=ACC_SUPER;
	interface_count=0L;	
	field_count=0L;
	method_count=0L;
	attribute_count=0L;





        cThis=addClassName(s);
	  this_class=cThis;
        cObject=addClassName("java/lang/Object");
	  super_class=cObject;
        cInputStreamReader=addClassName("java/io/InputStreamReader");
        cInputStream=addClassName("java/io/InputStream");
        cPrintStream=addClassName("java/io/PrintStream");
        cBufferedReader=addClassName("java/io/BufferedReader");
        cFloat=addClassName("java/lang/Float");
        cInteger=addClassName("java/lang/Integer");
        cBoolean=addClassName("java/lang/Boolean");
        cException=addClassName("java/lang/Exception");
        cSystem=addClassName("java/lang/System");
        cString=addClassName("java/lang/String");



        mInitObject=addMethodName(cObject,"<init>","()V");

        mInit=addMethodName(cThis,"<init>","()V");
        mPrintString=addMethodName(cPrintStream,"print","(Ljava/lang/String;)V");
        mPrintLnString=addMethodName(cPrintStream,"println","(Ljava/lang/String;)V");
        mPrintInt=addMethodName(cPrintStream,"print","(I)V");
        mPrintFloat=addMethodName(cPrintStream,"print","(F)V");
        mPrintChar=addMethodName(cPrintStream,"print","(C)V");
        mPutChar=addMethodName(cThis,"put","(C)V");
        mPutFloat=addMethodName(cThis,"put","(F)V");
        mPutInt=addMethodName(cThis,"put","(I)V");
        mPutBoolean=addMethodName(cThis,"put","(Z)V");
        mPutString=addMethodName(cThis,"put","(Ljava/lang/String;)V");
        mPutLnString=addMethodName(cThis,"putln","(Ljava/lang/String;)V");
        mGetInt=addMethodName(cThis,"getInt","()I");
        mGetFloat=addMethodName(cThis,"getFloat","()F");
        mGetChar=addMethodName(cThis,"getChar","()C");
        mGetBoolean=addMethodName(cThis,"getBoolean","()Z");
        mGetString=addMethodName(cThis,"getString","()Ljava/lang/String;");
        mMain=addMethodName(cThis,"main","([Ljava/lang/String;)V");
        mInitException=addMethodName(cException,"<init>","(Ljava/lang/String;)V");
        mInitFloat=addMethodName(cFloat,"<init>","(Ljava/lang/String;)V");
        mInitInputStreamReader=addMethodName(cInputStreamReader,"<init>","(Ljava/io/InputStream;)V");
        mInitBufferedReader=addMethodName(cBufferedReader,"<init>","(Ljava/io/Reader;)V");
        mReadLineBufferedReader=addMethodName(cBufferedReader,"readLine","()Ljava/lang/String;");
	mParseInt=addMethodName(cInteger,"parseInt","(Ljava/lang/String;)I");
	mParseFloat=addMethodName(cFloat,"parseFloat","(Ljava/lang/String;)F");
	mValueOfInt=addMethodName(cInteger,"valueOf","(I)Ljava/lang/Integer;");
	mValueOfFloat=addMethodName(cFloat,"valueOf","(F)Ljava/lang/Float;");
        mRead=addMethodName(cInputStream,"read","()I");
        mCharAt=addMethodName(cString,"charAt","(I)C");



        sCert=addConstant("S","Cert");
        sFals=addConstant("S","Fals");
        sEmpty=addConstant("S","");
        
        aCode=addAttributeName("Code");
        aExceptions=addAttributeName("Exceptions");
        aConstantValue=addAttributeName("ConstantValue");

        fIn=addFieldName(cSystem,"in","Ljava/io/InputStream;");
        fOut=addFieldName(cSystem,"out","Ljava/io/PrintStream;");







	
	Vector<Long> codePutInt=new Vector<Long>(8);
	codePutInt.add(GETSTATIC);
	codePutInt.add(0L);
	codePutInt.add(fOut);
	codePutInt.add(ILOAD_0);
	codePutInt.add(INVOKEVIRTUAL);
	codePutInt.add(0L);
	codePutInt.add(mPrintInt);
	codePutInt.add(RETURN);
	addFunctionCode(mPutInt,2l,1l,codePutInt);


	Vector<Long> codePutFloat=new Vector<Long>(8);
	codePutFloat.add(GETSTATIC);
	codePutFloat.add(0L);
	codePutFloat.add(fOut);
	codePutFloat.add(FLOAD_0);
	codePutFloat.add(INVOKEVIRTUAL);
	codePutFloat.add(0L);
	codePutFloat.add(mPrintFloat);
	codePutFloat.add(RETURN);
	addFunctionCode(mPutFloat,2l,1l,codePutFloat);


	Vector<Long> codePutChar=new Vector<Long>(8);
	codePutChar.add(GETSTATIC);
	codePutChar.add(0L);
	codePutChar.add(fOut);
	codePutChar.add(ILOAD_0);
	codePutChar.add(INVOKEVIRTUAL);
	codePutChar.add(0L);
	codePutChar.add(mPrintChar);
	codePutChar.add(RETURN);
	addFunctionCode(mPutChar,2l,1l,codePutChar);


	Vector<Long> codePutBoolean=new Vector<Long>(18);
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
	addFunctionCode(mPutBoolean,2l,1l,codePutBoolean);


	Vector<Long> codePutString=new Vector<Long>(8);
	codePutString.add(GETSTATIC);
	codePutString.add(0L);
	codePutString.add(fOut);
	codePutString.add(ALOAD_0);
	codePutString.add(INVOKEVIRTUAL);
	codePutString.add(0L);
	codePutString.add(mPrintString);
	codePutString.add(RETURN);
	addFunctionCode(mPutString,2l,1l,codePutString);


	Vector<Long> codePutLnString=new Vector<Long>(8);
	codePutLnString.add(GETSTATIC);
	codePutLnString.add(0L);
	codePutLnString.add(fOut);
	codePutLnString.add(ALOAD_0);
	codePutLnString.add(INVOKEVIRTUAL);
	codePutLnString.add(0L);
	codePutLnString.add(mPrintLnString);
	codePutLnString.add(RETURN);
	addFunctionCode(mPutLnString,2l,1l,codePutLnString);



	Vector<Long> codeGetInt=new Vector<Long>(28);
   	codeGetInt.add(ICONST_0);
   	codeGetInt.add(ISTORE_0);
   	codeGetInt.add(NEW);
   	codeGetInt.add(0L);
   	codeGetInt.add(cInputStreamReader);
   	codeGetInt.add(DUP);
   	codeGetInt.add(GETSTATIC);
   	codeGetInt.add(0L);
   	codeGetInt.add(fIn);
   	codeGetInt.add(INVOKESPECIAL);
   	codeGetInt.add(0L);
   	codeGetInt.add(mInitInputStreamReader);
   	codeGetInt.add(ASTORE_1);
   	codeGetInt.add(NEW);
   	codeGetInt.add(0L);
   	codeGetInt.add(cBufferedReader);
   	codeGetInt.add(DUP);
   	codeGetInt.add(ALOAD_1);
   	codeGetInt.add(INVOKESPECIAL);
   	codeGetInt.add(0L);
   	codeGetInt.add(mInitBufferedReader);
   	codeGetInt.add(ASTORE_2);
   	codeGetInt.add(ALOAD_2);
   	codeGetInt.add(INVOKEVIRTUAL);
   	codeGetInt.add(0L);
   	codeGetInt.add(mReadLineBufferedReader);
   	codeGetInt.add(ASTORE_3);
   	codeGetInt.add(ALOAD_3);
   	codeGetInt.add(INVOKESTATIC);
   	codeGetInt.add(0L);
   	codeGetInt.add(mParseInt);
   	codeGetInt.add(ISTORE_0);
   	codeGetInt.add(ILOAD_0);
   	codeGetInt.add(IRETURN);
	addFunctionCode(mGetInt,3l,4l,codeGetInt);


	Vector<Long> codeGetFloat=new Vector<Long>(28);
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
	addFunctionCode(mGetFloat,3l,4l,codeGetFloat);


	Vector<Long> codeGetChar=new Vector<Long>(28);
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
	addFunctionCode(mGetChar,3l,4l,codeGetChar);




	Vector<Long> codeGetString=new Vector<Long>(28);
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
	addFunctionCode(mGetString,3l,4l,codeGetString);



	Vector<Long> codeGetBoolean=new Vector<Long>(8);
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
	addFunctionCode(mGetBoolean,3l,2l,codeGetBoolean);
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
	addFunctionCode(mGetBoolean,2l,2l,codeGetBoolean);

*/

	Vector<Long> codeInit=new Vector<Long>(8);
	codeInit.add(ALOAD_0);
	codeInit.add(INVOKESPECIAL);
	codeInit.add(0L);
	codeInit.add(mInitObject);
	codeInit.add(RETURN);
	addFunctionCode(mInit,1l,1l,codeInit);
};

public void write() {
	try{
	FileOutputStream f=new FileOutputStream(fileName+".class");

	Integer aux;
	int i,aa;


	f.write(this.toByte(magic,4));
	f.write(this.toByte(minor_version,2));
	f.write(this.toByte(major_version,2));
	constant_pool_counter=new Long(constant_pool.size()+1);
	f.write(this.toByte(constant_pool_counter,2));
	Iterator<cp_info> itercp=constant_pool.iterator();
	while (itercp.hasNext()) {
		itercp.next().write(f);
	}
	f.write(this.toByte(access_flags,2));
	f.write(this.toByte(this_class,2));
	f.write(this.toByte(super_class,2));
	//interface_count=new Long(interfaces.size());
	interface_count=0L;
	f.write(this.toByte(interface_count,2));
	field_count=new Long(fields.size());
	f.write(this.toByte(field_count,2));
	Iterator<field_info> iterf=fields.iterator();
	while (iterf.hasNext()) {
		iterf.next().write(f);
	}
	method_count=new Long(methods.size());
	f.write(this.toByte(method_count,2));
	Iterator<method_info> iterm=methods.iterator();
	while (iterm.hasNext()) {
		iterm.next().write(f);
	}
	attribute_count=new Long(attributes.size());
	f.write(this.toByte(attribute_count,2));

	Iterator<attribute_info> itera=attributes.iterator();
	while (itera.hasNext()) {
		itera.next().write(f);
	}


	}
	catch (IOException s){}

};


public void show() {
	  
	System.out.println("-----------------------------------------------------------------");
	System.out.println("Fitxer        : "+fileName+".class");
	//System.out.println("Numero Magic  : "+magic);
	//System.out.println("Minor_version : "+minor_version);
	//System.out.println("Major_version : "+major_version);
	System.out.println("-----------------------------------------------------------------");
	constant_pool_counter=new Long(constant_pool.size()+1);
	System.out.print("CONSTANT POOL");System.out.println("         Elements : "+(constant_pool_counter-1));
	System.out.println("-----------------------------------------------------------------");
	Iterator<cp_info> itercp=constant_pool.iterator();
	int i=1;
	while (itercp.hasNext()) {
		System.out.print("   "+i + "	: ");
		itercp.next().show();
		System.out.println();i++;
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
	method_count=new Long(methods.size());
	System.out.print("METHODS       ");System.out.println("         Elements : "+method_count);
	System.out.println("-----------------------------------------------------------------");
	i=1;
	Iterator<method_info> iterm=methods.iterator();
	while (iterm.hasNext()) { 
		System.out.print("   "+i + "	: ");
		iterm.next().show();
		System.out.println();i++;
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
};

private Long addClassName(String s) {
		CONSTANT_Class_info x;
		CONSTANT_Utf8_info y;
		long i;
		
		y=new CONSTANT_Utf8_info(s);
		constant_pool.add(y);
		i=new Long(constant_pool.size());
		x= new CONSTANT_Class_info(i);
		constant_pool.add(x);
		i=new Long(constant_pool.size());
		return (i);			
};

private Long addMethodName(Long class_idx,String name,String desc) {
		CONSTANT_Utf8_info n,d;
		CONSTANT_NameAndType_info x;
		CONSTANT_Methodref_info y;
		long i,j,k,l;

		i=CPCercar(name);
		if (i==0L) {
			n=new CONSTANT_Utf8_info(name);
			constant_pool.add(n);
			i=new Long(constant_pool.size());	
		}
		j=CPCercar(desc);
		if (j==0L) {
			d=new CONSTANT_Utf8_info(desc);
			constant_pool.add(d);
			j=new Long(constant_pool.size());		
		}
		x=new CONSTANT_NameAndType_info(i,j);
		constant_pool.add(x);
		k=new Long(constant_pool.size());		
		y=new CONSTANT_Methodref_info(class_idx,k);
		constant_pool.add(y);
		l=new Long(constant_pool.size());		
		
		return (l);				
};


private Long addFieldName(Long class_idx,String name,String desc){
		CONSTANT_Utf8_info n,d;
		CONSTANT_NameAndType_info x;
		CONSTANT_Fieldref_info y;
		long i,j,k,l;

		i=CPCercar(name);
		if (i==0L) {
			n=new CONSTANT_Utf8_info(name);
			constant_pool.add(n);
			i=new Long(constant_pool.size());	
		}
		j=CPCercar(desc);
		if (j==0L) {
			d=new CONSTANT_Utf8_info(desc);
			constant_pool.add(d);
			j=new Long(constant_pool.size());		
		}
		x=new CONSTANT_NameAndType_info(i,j);
		constant_pool.add(x);
		k=new Long(constant_pool.size());
		y=new CONSTANT_Fieldref_info(class_idx,k);
		constant_pool.add(y);
		l=new Long(constant_pool.size());

		return(l);
};


private Long addAttributeName(String val10){
		CONSTANT_Utf8_info n;
		long i;

		n=new CONSTANT_Utf8_info(val10);
		constant_pool.add(n);
		i=new Long(constant_pool.size());
		return i;
};




public Long addFunctionDef(String name, String desc) {
		return addMethodName(cThis,name,desc);
}; 






public void addFunctionCode(Long ref,Long maxStack,Long nLocals,Vector<Long> code){
	CONSTANT_Methodref_info x;
	CONSTANT_NameAndType_info y;
	method_info z;
	

	x=(CONSTANT_Methodref_info)constant_pool.elementAt(new Integer(ref.toString())-1);
	y=(CONSTANT_NameAndType_info)constant_pool.get(new Integer(x.name_and_type_index.toString())-1);
	Long acc=ACC_PUBLIC|ACC_STATIC;
	if (ref==mInit) {acc=ACC_PUBLIC;};
	z=new method_info(aCode,aExceptions,cException,acc,y.name_index,y.descriptor_index,maxStack,nLocals,code);
	methods.add(z);
	
};	


public void addMainCode(Long maxStack,Long nLocals,Vector<Long> code) {
	CONSTANT_Methodref_info x;
	CONSTANT_NameAndType_info y;
	method_info z;
	Long x1,x2;
	
	x=(CONSTANT_Methodref_info)constant_pool.get(new Integer(mMain.toString())-1);
	y=(CONSTANT_NameAndType_info)constant_pool.get(new Integer(x.name_and_type_index.toString())-1);
	Long acc=ACC_PUBLIC|ACC_STATIC;
	z=new method_info(aCode,aExceptions,cException,acc,y.name_index,y.descriptor_index,maxStack,nLocals,code);
	methods.add(z);
	
};


public Long addConstName(String name,String desc,String val){
	CONSTANT_Utf8_info n,d;
	long i,j,k;
	field_info z;

	i=CPCercar(name);
	if (i==0L) {
		n=new CONSTANT_Utf8_info(name);
		constant_pool.add(n);
		i=new Long(constant_pool.size());	
	}
	j=CPCercar(desc);
	if (j==0L) {
		d=new CONSTANT_Utf8_info(desc);
		constant_pool.add(d);
		j=new Long(constant_pool.size());		
	}
	k=addConstant(desc,val);
	Long acc=ACC_FINAL|ACC_STATIC;
	z=new field_info(aConstantValue,acc,i,j,k);
	fields.add(z);
	return(k);
	
};


public Long addConstant(String desc,String val){
	Long ret;

	ret=99999l;
	switch (desc.charAt(0)) {
	case'I': {
		Integer i=new Integer(val);
		CONSTANT_Integer_info x=new CONSTANT_Integer_info(new Long(i));
		constant_pool.add(x);
		ret=new Long(constant_pool.size());
	}
	break;
	case'Z': {
		Integer i;
		if (val=="Cert") {i=1;} else {i=0;};
		CONSTANT_Integer_info x=new CONSTANT_Integer_info(new Long(i));
		constant_pool.add(x);
		ret=new Long(constant_pool.size());
	}
	break;
	case'C': {
		Integer i=(int)(val.charAt(0));
		CONSTANT_Integer_info x=new CONSTANT_Integer_info(new Long(i));
		constant_pool.add(x);
		ret=new Long(constant_pool.size());
	}
	break;
	case'F': {
		Float r;
		r=Float.parseFloat(val);
		CONSTANT_Float_info x=new CONSTANT_Float_info(r);
		constant_pool.add(x);
		ret=new Long(constant_pool.size());
	}
	break;
	case'S': {
		CONSTANT_Utf8_info x=new CONSTANT_Utf8_info(val);
		constant_pool.add(x);
		Long i=new Long(constant_pool.size());		
		CONSTANT_String_info y=new CONSTANT_String_info(i);
		constant_pool.add(y);
		ret=new Long(constant_pool.size());
	}
	}
	return ret;	
};


public Long addArrayDef(int dimensions, String desc) {
	String s;
	int i;

	s="";
	for (i=0;i<dimensions;i++) s=s+"[";
	s=s+desc;  
	return addClassName(s);
};



private byte[] toByte(Long num, int t) {
	int i;
	Long aux;

	byte[] bytes=new byte[t];
	aux=num;
	i=t-1;
	while (i>=0) { 
		bytes[i]=aux.byteValue(); 
		aux=(Long)aux.rotateRight(aux,8); 
		i--;
	}
	return bytes;
}



public Long nByte(Long num,int t) {
	int i,j;
	Long aux;

	byte[] bytes=new byte[4];
	aux=num;
	i=3;
	while (i>=0) { 
		bytes[i]=aux.byteValue(); 
		aux=(Long)aux.rotateRight(aux,8); 
		i--;
	}
	j=bytes[4-t];
	return (new Long(j));
} 





	


}