#!/bin/sh
java -jar lib/antlr-4.9.2-complete.jar src/LANSet/LANSet.g4 -no-listener -visitor -o tmp
if [ -d "src/generated" ]; then
	rm src/generated -r
fi
mv tmp/src/LANSet src/generated
rmdir tmp/src
rmdir tmp
