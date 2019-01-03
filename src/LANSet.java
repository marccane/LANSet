import java.io.*;

import org.antlr.v4.runtime.*;


//Jordi LANS Compiler. Exemple de classe i metode main
public class LANSet {

	public static void main(String args[]) throws Exception{
		if(args.length == 0){
			System.out.println("Es requereix un fitxer LANS");
			System.exit(0);
		}
		
		jlcLexer lexer = new jlcLexer(new ANTLRFileStream(args[0]));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		jlcParser p = new jlcParser(tokens);
		if(args.length == 2){
			String classfile = args[1];
			if(!classfile.endsWith(".class"))
			   classfile = classfile + ".class";
			p.setLANSClassFile(classfile);
		}
		p.root();
	}

}
