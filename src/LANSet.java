import org.antlr.v4.runtime.*;

import java.io.File;

// LANSet Compiler
public class LANSet {

	public static void main(String args[]) throws Exception{

		String lansSourceFile = "", outputFile = "";
		if(args.length == 1){
			lansSourceFile = args[0];
			String[] lansSourceFileSplitSep = lansSourceFile.split(File.separator);
			String filenameWithoutPath = lansSourceFileSplitSep[lansSourceFileSplitSep.length-1];
			System.out.println(filenameWithoutPath);
			String[] filenameSplit = filenameWithoutPath.split("[.]");
			System.out.println(filenameSplit.length);
			outputFile = filenameSplit[0];
		}
		else {
			System.out.println("Wrong number of parameters: Input a lans file");
			System.exit(0);
		}

		LANSetLexer lexer = new LANSetLexer(CharStreams.fromFileName(lansSourceFile));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LANSetParser p = new LANSetParser(tokens);
		p.setLANSClassFile(outputFile);
		p.start();
	}
}
