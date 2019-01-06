import java.io.*;

import org.antlr.v4.runtime.*;


// LANSet Compiler.
public class LANSet {
	static SymTable<Registre> TS = new SymTable<Registre>(1000);
	static boolean errorSintactic = false;
	static boolean errorSemantic = false;


	public static void main(String args[]) throws Exception{
		if(args.length == 0){
			System.out.println("Es requereix un fitxer LANS");
			System.exit(0);
		}

		LANSetLexer lexer = new LANSetLexer(new ANTLRFileStream(args[0]));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LANSetParser p = new LANSetParser(tokens);
		if(args.length == 2){
			String classfile = args[1];
			if(!classfile.endsWith(".class"))
			   classfile = classfile + ".class";
			//p.setLANSClassFile(classfile);
		}
		p.start();
	}

}
