import java.io.*;
import java.util.*;


//
//
//
//
//
//	Fitxer per provar la classe Bytecode
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





class provaBytecode {
public static void main(String[] args) {

	Bytecode x=new Bytecode("Exemple");



/////////////////////////////////////////////////////////////////////////////////////////////////////



  	Long c1=x.addConstant("S","Hola a tothom");
  	Long c2=x.addConstant("S","\n");
  	Long c3=x.addConstant("S"," + ");
  	Long c4=x.addConstant("S"," - 1 = ");
  	Long c5=x.addConstant("S","Entra un real     : ");
  	Long c6=x.addConstant("S","Entra un enter    : ");
  	Long c7=x.addConstant("S","Entra un caracter : ");
  	Long c8=x.addConstant("S","Entra un boolea   : ");
  	Long c9=x.addConstant("S","Constant Real     : ");
  	Long c10=x.addConstant("S","Constant Entera   : ");
  	Long c11=x.addConstant("S","Constant Boolea   : ");
  	Long c12=x.addConstant("S","Constant Char     : ");
  	
	Long idSpin=x.addFunctionDef("spin","()V");
  	Long idA2g=x.addFunctionDef("a2g","(II)I");
  	Long idReal=x.addConstName("PI","F","3.14159");
  	Long idEnter=x.addConstName("Enter","I","308");
  	Long idBool=x.addConstName("Bool","Z","Cert");
  	Long idCar=x.addConstName("Car","C","a");



/////////////////////////////////////////////////////////////////////////////////////////////////////



	// Vector de Long per col.locar el codi del Main
  	Vector<Long> codeMain=new Vector<Long>(100);
 

   	// Imprimim un missatge per pantalla
    codeMain.add(x.LDC_W);
	codeMain.add(x.nByte(c1,2));
	codeMain.add(x.nByte(c1,1));
    codeMain.add(x.INVOKESTATIC);
	codeMain.add(x.nByte(x.mPutLnString,2));
	codeMain.add(x.nByte(x.mPutLnString,1));
 

   	// Fem la crida a la accio spin
    codeMain.add(x.INVOKESTATIC);
	codeMain.add(x.nByte(idSpin,2));
	codeMain.add(x.nByte(idSpin,1));
 

   	// Fem la crida a la funcio a2g()
    	codeMain.add(x.BIPUSH);
	codeMain.add(10l);
    	codeMain.add(x.ISTORE_1);
    	codeMain.add(x.ICONST_5);
    	codeMain.add(x.ISTORE_2);
    	codeMain.add(x.ILOAD_1);
    	codeMain.add(x.ILOAD_2);
    	codeMain.add(x.INVOKESTATIC);
	codeMain.add(x.nByte(idA2g,2));
	codeMain.add(x.nByte(idA2g,1));
    codeMain.add(x.ISTORE_0);

	// Imprimir 10+5-1=14
    	codeMain.add(x.ILOAD_1);
    	codeMain.add(x.INVOKESTATIC);
	codeMain.add(x.nByte(x.mPutInt,2));
	codeMain.add(x.nByte(x.mPutInt,1));
    	codeMain.add(x.LDC_W);
	codeMain.add(x.nByte(c3,2));
	codeMain.add(x.nByte(c3,1));
    	codeMain.add(x.INVOKESTATIC);
	codeMain.add(x.nByte(x.mPutString,2));
	codeMain.add(x.nByte(x.mPutString,1));
    	codeMain.add(x.ILOAD_2);
    	codeMain.add(x.INVOKESTATIC);
	codeMain.add(x.nByte(x.mPutInt,2));
	codeMain.add(x.nByte(x.mPutInt,1));
    	codeMain.add(x.LDC_W);
	codeMain.add(x.nByte(c4,2));
	codeMain.add(x.nByte(c4,1));
    	codeMain.add(x.INVOKESTATIC);
	codeMain.add(x.nByte(x.mPutString,2));
	codeMain.add(x.nByte(x.mPutString,1));
    	codeMain.add(x.ILOAD_0);
    	codeMain.add(x.INVOKESTATIC);
	codeMain.add(x.nByte(x.mPutInt,2));
	codeMain.add(x.nByte(x.mPutInt,1));
    	codeMain.add(x.LDC_W);
	codeMain.add(x.nByte(c2,2));
	codeMain.add(x.nByte(c2,1));
    	codeMain.add(x.INVOKESTATIC);
	codeMain.add(x.nByte(x.mPutString,2));
	codeMain.add(x.nByte(x.mPutString,1));


	// Imprimir el real PI
    	codeMain.add(x.LDC_W);
	codeMain.add(x.nByte(c9,2));
	codeMain.add(x.nByte(c9,1));
    	codeMain.add(x.INVOKESTATIC);
	codeMain.add(x.nByte(x.mPutString,2));
	codeMain.add(x.nByte(x.mPutString,1));
    	codeMain.add(x.LDC_W);
	codeMain.add(x.nByte(idReal,2));
	codeMain.add(x.nByte(idReal,1));
    	codeMain.add(x.INVOKESTATIC);
	codeMain.add(x.nByte(x.mPutFloat,2));
	codeMain.add(x.nByte(x.mPutFloat,1));
    	codeMain.add(x.LDC_W);
	codeMain.add(x.nByte(c2,2));
	codeMain.add(x.nByte(c2,1));
    	codeMain.add(x.INVOKESTATIC);
	codeMain.add(x.nByte(x.mPutString,2));
	codeMain.add(x.nByte(x.mPutString,1));


	// Imprimir l'enter Enter
    	codeMain.add(x.LDC_W);
	codeMain.add(x.nByte(c10,2));
	codeMain.add(x.nByte(c10,1));
    	codeMain.add(x.INVOKESTATIC);
	codeMain.add(x.nByte(x.mPutString,2));
	codeMain.add(x.nByte(x.mPutString,1));
    	codeMain.add(x.LDC_W);
	codeMain.add(x.nByte(idEnter,2));
	codeMain.add(x.nByte(idEnter,1));
    	codeMain.add(x.INVOKESTATIC);
	codeMain.add(x.nByte(x.mPutInt,2));
	codeMain.add(x.nByte(x.mPutInt,1));
    	codeMain.add(x.LDC_W);
	codeMain.add(x.nByte(c2,2));
	codeMain.add(x.nByte(c2,1));
    	codeMain.add(x.INVOKESTATIC);
	codeMain.add(x.nByte(x.mPutString,2));
	codeMain.add(x.nByte(x.mPutString,1));


  	Vector<Long> codeMain2=new Vector<Long>(100);

	// Imprimir el boolea Bool
    	codeMain2.add(x.LDC_W);
	codeMain2.add(x.nByte(c11,2));
	codeMain2.add(x.nByte(c11,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mPutString,2));
	codeMain2.add(x.nByte(x.mPutString,1));
    	codeMain2.add(x.LDC_W);
	codeMain2.add(x.nByte(idBool,2));
	codeMain2.add(x.nByte(idBool,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mPutBoolean,2));
	codeMain2.add(x.nByte(x.mPutBoolean,1));
    	codeMain2.add(x.LDC_W);
	codeMain2.add(x.nByte(c2,2));
	codeMain2.add(x.nByte(c2,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mPutString,2));
	codeMain2.add(x.nByte(x.mPutString,1));


	// Imprimir el caracter Car
    	codeMain2.add(x.LDC_W);
	codeMain2.add(x.nByte(c12,2));
	codeMain2.add(x.nByte(c12,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mPutString,2));
	codeMain2.add(x.nByte(x.mPutString,1));
    	codeMain2.add(x.LDC_W);
	codeMain2.add(x.nByte(idCar,2));
	codeMain2.add(x.nByte(idCar,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mPutChar,2));
	codeMain2.add(x.nByte(x.mPutChar,1));
    	codeMain2.add(x.LDC_W);
	codeMain2.add(x.nByte(c2,2));
	codeMain2.add(x.nByte(c2,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mPutString,2));
	codeMain2.add(x.nByte(x.mPutString,1));


	// Entrar un enter 
   	codeMain2.add(x.LDC_W);
	codeMain2.add(x.nByte(c6,2));
	codeMain2.add(x.nByte(c6,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mPutString,2));
	codeMain2.add(x.nByte(x.mPutString,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mGetInt,2));
	codeMain2.add(x.nByte(x.mGetInt,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mPutInt,2));
	codeMain2.add(x.nByte(x.mPutInt,1));
    	codeMain2.add(x.LDC_W);
	codeMain2.add(x.nByte(c2,2));
	codeMain2.add(x.nByte(c2,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mPutString,2));
	codeMain2.add(x.nByte(x.mPutString,1));
 

	// Entrar un real 
   	codeMain2.add(x.LDC_W);
	codeMain2.add(x.nByte(c5,2));
	codeMain2.add(x.nByte(c5,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mPutString,2));
	codeMain2.add(x.nByte(x.mPutString,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mGetFloat,2));
	codeMain2.add(x.nByte(x.mGetFloat,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mPutFloat,2));
	codeMain2.add(x.nByte(x.mPutFloat,1));
    	codeMain2.add(x.LDC_W);
	codeMain2.add(x.nByte(c2,2));
	codeMain2.add(x.nByte(c2,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mPutString,2));
	codeMain2.add(x.nByte(x.mPutString,1));


	// Entrar un caracter 
    	codeMain2.add(x.LDC_W);
	codeMain2.add(x.nByte(c7,2));
	codeMain2.add(x.nByte(c7,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mPutString,2));
	codeMain2.add(x.nByte(x.mPutString,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mGetChar,2));
	codeMain2.add(x.nByte(x.mGetChar,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mPutChar,2));
	codeMain2.add(x.nByte(x.mPutChar,1));
    	codeMain2.add(x.LDC_W);
	codeMain2.add(x.nByte(c2,2));
	codeMain2.add(x.nByte(c2,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mPutString,2));
	codeMain2.add(x.nByte(x.mPutString,1));


	// Entrar un Boolea 
    	codeMain2.add(x.LDC_W);
	codeMain2.add(x.nByte(c8,2));
	codeMain2.add(x.nByte(c8,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mPutString,2));
	codeMain2.add(x.nByte(x.mPutString,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mGetBoolean,2));
	codeMain2.add(x.nByte(x.mGetBoolean,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.mPutBoolean,2));
	codeMain2.add(x.nByte(x.mPutBoolean,1));
    	codeMain2.add(x.LDC_W);
	codeMain2.add(x.nByte(c2,2));
	codeMain2.add(x.nByte(c2,1));
    	codeMain2.add(x.INVOKESTATIC);
	codeMain2.add(x.nByte(x.nByte(x.mPutString,1),2));
	codeMain2.add(x.nByte(x.nByte(x.mPutString,1),1));


	// Acabament 
    	codeMain2.add(x.RETURN);
 

	// Afegim a l'objecte Bytecode el codi del Main 
	codeMain.addAll(codeMain2);
 	x.addMainCode(2l,4l,codeMain);



/////////////////////////////////////////////////////////////////////////////////////////////////////



	// Vector de Long per col.locar el codi del Spin
	Vector<Long> codeSpin=new Vector<Long>(15);
 

  	codeSpin.add(x.ICONST_0);
   	codeSpin.add(x.ISTORE_0);
   	codeSpin.add(x.GOTO);
   	codeSpin.add(x.nByte(16l,2));
   	codeSpin.add(x.nByte(16l,1));
   	codeSpin.add(x.IINC);
   	codeSpin.add(0l);
   	codeSpin.add(1l);
   	codeSpin.add(x.ILOAD_0);
   	codeSpin.add(x.INVOKESTATIC);
  	codeSpin.add(x.nByte(x.mPutInt,2));
   	codeSpin.add(x.nByte(x.mPutInt,1));
   	codeSpin.add(x.LDC_W);
   	codeSpin.add(x.nByte(c2,2));
   	codeSpin.add(x.nByte(c2,1));
   	codeSpin.add(x.INVOKESTATIC);
   	codeSpin.add(x.nByte(x.mPutString,2));
   	codeSpin.add(x.nByte(x.mPutString,1));
   	codeSpin.add(x.ILOAD_0);
   	codeSpin.add(x.BIPUSH);
   	codeSpin.add(100l);
   	codeSpin.add(x.IF_ICMPLT);
   	codeSpin.add(x.nByte(-16l,2));
   	codeSpin.add(x.nByte(-16l,1));
   	codeSpin.add(x.RETURN);


	// Afegim a l'objecte Bytecode el codi del Spin 
  	x.addFunctionCode(idSpin,2l,1l,codeSpin);



/////////////////////////////////////////////////////////////////////////////////////////////////////



	// Vector de Long per col.locar el codi del A2g
	Vector<Long> codeA2g=new Vector<Long>(15);


 	codeA2g.add(x.ILOAD_0);
 	codeA2g.add(x.ILOAD_1);
 	codeA2g.add(x.IADD);
 	codeA2g.add(x.ICONST_1);
 	codeA2g.add(x.ISUB);
 	codeA2g.add(x.IRETURN);

	// Afegim a l'objecte Bytecode el codi del A2g 
	x.addFunctionCode(idA2g,2l,2l,codeA2g);



/////////////////////////////////////////////////////////////////////////////////////////////////////

  

	// Escriure el fitxer Example.class
	x.write();


	// Escriure per pantalla
	x.show();

};




}