#!/bin/sh
if [ -d "out" ]; then
	rm out -r  #builds will always be clean
fi
mkdir -p out
javac -cp lib/antlr-4.9.2-complete.jar -d out src/generated/*.java lib/LANSet/Bytecode/*.java src/LANSet/*.java
jar cfm lansetc.jar other/Manifest.txt -C out .
