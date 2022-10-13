# LANSet
ANTLR4 Toy language compiler that generates JVM bytecode

## Building

To build you'll need `JDK 8` or newer. The build process is so simple that I've decided not to use any build tools.

### Compiling
To generate the parser code from the LANSet.g4 file run
```
./generateParser.sh
```
To build the compiler and generate the jar file run
```
./build.sh
```
If there are no errors you should have `lansetc.jar` in the current directory.

### Running the tests
```
./runTests.sh
```

### Compiling a sample program
Let's create a file called `example.lans` and paste some example code:
```
programa example
    escriureln("Hello world!");
fprograma
```
Compile it to generate `example.class`
```
java -jar lansetc.jar example.lans
```
and run it!
```
java example
```
You should see the familiar string printed in your terminal.

### Troubleshooting
- `Exception in thread "main" java.lang.NoClassDefFoundError: org/antlr/v4/runtime/TokenSource`
  - The jar needs to have access to lib/antlr-4.9.2-complete.jar in the current folder.
