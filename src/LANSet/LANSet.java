package LANSet;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;

// LANSet Compiler
public class LANSet {

	public static void main(String args[]) throws Exception{

		String lansSourceFile = "", outputFile = "";

		System.out.println("---> LANSet <---");

		if(args.length == 1){
			lansSourceFile = args[0];
			String[] lansSourceFileSplitSep = lansSourceFile.split(File.separator);
			String filenameWithoutPath = lansSourceFileSplitSep[lansSourceFileSplitSep.length-1];
			System.out.println("Compiling " + filenameWithoutPath);
			System.out.println();
			String[] filenameSplit = filenameWithoutPath.split("[.]");
			outputFile = filenameSplit[0];
		}
		else {
			System.out.println("Wrong number of parameters: Input a lans file");
			System.exit(0);
		}

		LANSetLexer lexer = new LANSetLexer(CharStreams.fromFileName(lansSourceFile));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LANSetParser parser = new LANSetParser(tokens);
		ParseTree tree = parser.start();

		LANSetBaseVisitorImpl visitor = new LANSetBaseVisitorImpl();
		//p.setLANSClassFile(outputFile);
		visitor.visit(tree);
	}
}
