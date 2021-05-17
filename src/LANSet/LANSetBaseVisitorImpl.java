package LANSet;

import java.util.List;
import java.util.Vector;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import LANSet.Bytecode.Bytecode;

import static LANSet.Constants.*;
import static LANSet.LANSetLexer.*;

public class LANSetBaseVisitorImpl extends LANSetBaseVisitor<ReturnStruct>{

    @Override
    public ReturnStruct visitStart(LANSetParser.StartContext ctx) {
        String programID = ctx.prog_id.getText();
        System.out.println("Hola, " +  programID);

        program = new Bytecode(programID);
        bytecodeWriter = new BytecodeWriter(program);
        lineBreak = program.addConstant(BYTECODE_STRINGTYPE, "\n");

        ReturnStruct rs = visitChildren(ctx); //are we processing more than we should in here? should all the code from the start rule be added to the same vector?

        System.out.println("----------------");
        if (!errorSemantic)
        {
            rs.code.add(program.RETURN);
            program.addMainCode(mainStackSize,nVar,rs.code);
            program.write();
            System.out.println("Generated " + programID + ".class");
        }
        else System.err.println(programID + ".class NOT generated");

        if(programID.endsWith("_d")) {
            program.show();
            System.out.println("VisitCount = " + visitCount);
        }

        return rs;
    }

    @Override
    public ReturnStruct visitType_declaration(LANSetParser.Type_declarationContext ctx) {
        ReturnStruct rs = new ReturnStruct();

        if(ctx.btype != null) {
            if (!identifierInUse(ctx.id.getText())) {
                registerAlias(ctx.id, ctx.btype);
            }
            else {
                errorSemantic = true;
                Companion.repeatedIdentifierError(ctx.id.getText(), ctx.id.getLine());
            }
        }
        else if(ctx.vector_definition() != null){
            //rs = visit(ctx.vector_definition()); //not needed
            if (!identifierInUse(ctx.id.getText())) {
                C_TYPE bType = Companion.processBaseType(C_TYPE.fromString(ctx.vector_definition().baseType.getText()));
                Registre r = new Registre(ctx.id.getText(), C_SUPERTYPE.VECTOR_SUPERTYPE, bType, ctx.id.getLine(),
                        ctx.id.getCharPositionInLine(), Integer.parseInt(ctx.vector_definition().size.getText()));
                TS.inserir(ctx.id.getText(), r);
            }
            else {
                errorSemantic = true;
                Companion.repeatedIdentifierError(ctx.id.getText(), ctx.id.getLine());
            }
        }
        else if(ctx.tuple_definition() != null){
            System.err.println("TODO: tuple definition");
        }
        else {
            errorSemantic = true;
            System.err.println("Type declaration error");
        }

        return rs;
    }

    /*@Override
    public ReturnStruct visitVector_definition(LANSetParser.Vector_definitionContext ctx) {
        System.out.println(ctx.baseType.getText());
        return defaultResult();
    }*/

    @Override
    public ReturnStruct visitTuple_definition(LANSetParser.Tuple_definitionContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitAction_declaration(LANSetParser.Action_declarationContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitFunction_declaration(LANSetParser.Function_declarationContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitFormal_parameters(LANSetParser.Formal_parametersContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitType(LANSetParser.TypeContext ctx) {
        ctx.tkType = ctx.typ.getType();
        ctx.text = ctx.typ.getText();
        ctx.line = ctx.typ.getLine();
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitConst_declaration(LANSetParser.Const_declarationContext ctx) {
        ReturnStruct rs = visit(ctx.value);

        if(identifierInUse(ctx.id.getText())){
            Companion.repeatedIdentifierError(ctx.id.getText(), ctx.id.getLine());
            errorSemantic=true;
        }
        else{
            C_TYPE valueType = ctx.value.typ;
            if (ctx.bt.getText().equals(valueType.toString())){ //TODO simplify this
                Registre r = registerConstant(ctx.id,ctx.bt);
                Long dir = program.addConstName(r.getText(), Companion.bytecodeType(r.getType()), ctx.value.text);
                r.putDir(dir); //TODO fix
            }
            else if (ctx.bt.getText().equals(C_TYPE.FLOAT_TYPE.toString()) && valueType == C_TYPE.INT_TYPE){
                Registre r = registerConstant(ctx.id,ctx.bt);
                Long dir = program.addConstName(r.getText(), Companion.bytecodeType(r.getType()), ctx.value.text);
                r.putDir(dir); //TODO fix
            }
            else{
                Companion.typeMismatchError2(ctx.id.getText(), ctx.id.getLine(), valueType.toString(), C_TYPE.fromString(ctx.bt.getText()));
                errorSemantic=true;
            }
        }
        return rs;
    }

    @Override
    public ReturnStruct visitBasetype_value(LANSetParser.Basetype_valueContext ctx) {
        Long value = -1L;
        ReturnStruct rs = new ReturnStruct();
        ctx.text = ctx.tk.getText();
        ctx.typ = Companion.cTypeFromTokenID(ctx.tk.getType());
        ctx.line = ctx.tk.getLine();

        switch(ctx.tk.getType()){
            case TK_INTEGER:
                value = program.addConstant(BYTECODE_INTTYPE, ctx.tk.getText());
                break;
            case TK_BOOLEAN:
                value = program.addConstant(BYTECODE_BOOLTYPE, ctx.tk.getText().equals(BOOL_TRUE)?"Cert":"Fals");
                break;
            case TK_CHARACTER:
                value = program.addConstant(BYTECODE_CHARTYPE, ctx.tk.getText().substring(1,2));
                break;
            case TK_REAL:
                value = program.addConstant(BYTECODE_FLOATTYPE, ctx.tk.getText());
                break;
            default:
                errorSemantic = true;
                System.err.println("Basetype_value default case. This should not happen");
                break;
        }

        rs.code.add(program.LDC_W);
        rs.code.add(program.nByte(value,2));
        rs.code.add(program.nByte(value,1));
        return rs;
    }

    @Override
    public ReturnStruct visitVar_declaration(LANSetParser.Var_declarationContext ctx) {
        ReturnStruct rs = visitChildren(ctx);

        if(identifierInUse(ctx.id.getText())){
            errorSemantic = true;
            Companion.repeatedIdentifierError(ctx.id.getText(), ctx.id.getLine());
        }
        else if(ctx.type().tkType == TK_IDENTIFIER) { //alias, tuple or vector
            Registre typeReg = TS.obtenir(ctx.type().getText());
            if(typeReg == null){
                errorSemantic = true;
                Companion.undefinedTypeError(ctx.type().getText(), ctx.type().line);
            }
            else if(typeReg.getSupertype() == C_SUPERTYPE.ALIAS_SUPERTYPE){
                //TODO review this (look for the documentation we wrote some years ago)
            }
            else if(typeReg.getSupertype() == C_SUPERTYPE.VECTOR_SUPERTYPE){
                Registre vecVariable = new Registre(ctx.id.getText(), C_SUPERTYPE.VECTOR_SUPERTYPE,
                        typeReg.getType(), ctx.id.getLine(), ctx.id.getCharPositionInLine()); //i'm not very confident about the type...
                vecVariable.putDir(nVar++);
                TS.inserir(ctx.id.getText(), vecVariable);

                bytecodeWriter.initVector(rs.code, typeReg.getType(), typeReg.vecSize);
                rs.code.add(program.ASTORE);
                rs.code.add(vecVariable.getDir());
            }
            else if(typeReg.getSupertype() == C_SUPERTYPE.TUPLE_SUPERTYPE){
                errorSemantic = true;
                System.err.println("TODO: tuple variable detected");
            }
            else if(typeReg.getSupertype() == C_SUPERTYPE.VARIABLE_SUPERTYPE){ //we should handle vectors here
                errorSemantic = true;
                System.err.println("WIP");
            }
            else{
                errorSemantic = true;
                System.err.println("Invalid supertype");
            }
        }
        else {
            registerBasetypeVariable(C_TYPE.fromString(ctx.type().getText()), ctx.id);
            TS.obtenir(ctx.id.getText()).putDir(nVar++);
        }
        return rs;
    }

    @Override
    public ReturnStruct visitAction_definition(LANSetParser.Action_definitionContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitFunction_definition(LANSetParser.Function_definitionContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitAssignment(LANSetParser.AssignmentContext ctx) {
        ReturnStruct rsLval = visit(ctx.lval);
        ReturnStruct rsExpr = visit(ctx.e);
        rsLval.code.addAll(rsExpr.code);

        boolean needsPromotion = (ctx.lval.typ == C_TYPE.FLOAT_TYPE && ctx.e.typ == C_TYPE.INT_TYPE);

        if(ctx.lval.typ != ctx.e.typ && !needsPromotion){
            errorSemantic = true;
            Companion.typeMismatchError(ctx.lval.typ, ctx.e.typ, ctx.lval.line);
        }
        else{ //lval es un identificador existent, tipus variable i els tipus casen
            if(needsPromotion)
                rsLval.code.add(program.I2F);
            rsLval.code.add(ctx.lval.storeInstruction);

            if(ctx.lval.dir != null)
                rsLval.code.add(ctx.lval.dir);
        }
        return rsLval;
    }

    @Override
    public ReturnStruct visitLvalueTupleAccess(LANSetParser.LvalueTupleAccessContext ctx){
        ReturnStruct rs = visit(ctx.tuple_acces());
        errorSemantic = true;
        System.err.println("TODO: tuple as an lvalue");
        ctx.storeInstruction = program.PUTFIELD;
        //ctx.dir = ??
        return rs;
    }

    @Override
    public ReturnStruct visitLvalueVectorAccess(LANSetParser.LvalueVectorAccessContext ctx){
        ReturnStruct rs = visit(ctx.vector_acces());
        ctx.typ = ctx.vector_acces().typ;
        ctx.line = ctx.vector_acces().line;

        if(ctx.typ == C_TYPE.FLOAT_TYPE)
            ctx.storeInstruction = program.FASTORE;
        else
            ctx.storeInstruction = program.IASTORE;

        return rs;
    }

    @Override
    public ReturnStruct visitLvalueId(LANSetParser.LvalueIdContext ctx){

        Registre reg = TS.obtenir(ctx.id.getText());
        if(reg == null){
            errorSemantic = true;
            Companion.undefinedIdentifierError(ctx.id.getText(), ctx.id.getLine());
        }
        else if(reg.getSupertype() != C_SUPERTYPE.VARIABLE_SUPERTYPE){
            errorSemantic = true;
            Companion.identifierIsNotAVariableError(ctx.id.getText(), ctx.id.getLine()); //no es el mes adient perque pot ser tuple o vector...
        }
        else{
            if(ctx.typ == C_TYPE.FLOAT_TYPE)
                ctx.storeInstruction = program.FSTORE;
            else
                ctx.storeInstruction = program.ISTORE;
        }

        ctx.typ = (reg == null) ? C_TYPE.INVALID_TYPE : reg.getType();
        ctx.line = ctx.id.getLine();
        ctx.dir = reg.getDir();
        return new ReturnStruct();
    }

    @Override
    public ReturnStruct visitConditional(LANSetParser.ConditionalContext ctx) {
        ReturnStruct rs = visit(ctx.expr());
        Vector<Long> c1Code = new Vector<>();
        Vector<Long> c2Code = new Vector<>();

        for (LANSetParser.SentenceContext sentence: ctx.sentence())
            c1Code.addAll(visit(sentence).code);
        for (LANSetParser.Else_sentenceContext sentence: ctx.else_sentence())
            c2Code.addAll(visit(sentence).code);

        if(ctx.expr().typ != C_TYPE.BOOL_TYPE){
            errorSemantic=true;
            Companion.typeMismatchError2("*expressio*", ctx.expr().line, ctx.expr().typ.toString(), C_TYPE.BOOL_TYPE);
        }
        else{ //no error, codegen
            rs.code.addAll(visit(ctx.expr()).code);
            rs.code.add(program.IFEQ); //if false, jumps to else (c1.size()+6)

            Long jump = 2L + c1Code.size() + 3L + 1L; //if condition is false, jump to c2 (6 = ifjump1+ifjump2+goto+goto1+goto2+1)
            rs.code.add(program.nByte(jump,2));
            rs.code.add(program.nByte(jump,1));
            rs.code.addAll(c1Code);

            jump = c2Code.size()+3L; //if we're on the end of c1, skip c2 (3 = goto1 + goto2 + 1)
            rs.code.add(program.GOTO);
            rs.code.add(program.nByte(jump,2));
            rs.code.add(program.nByte(jump,1));
            rs.code.addAll(c2Code);
        }

        return rs;
    }

    @Override
    public ReturnStruct visitFor_loop(LANSetParser.For_loopContext ctx) {
        ReturnStruct rs = new ReturnStruct();
        Vector<Long> c1Code = new Vector<>();
        Vector<Long> forExpr = new Vector<>();

        ReturnStruct expr1Rs = visit(ctx.expr1);
        ReturnStruct expr2Rs = visit(ctx.expr2);

        Registre var_iter = TS.obtenir(ctx.id.getText());
        if(var_iter == null){
            errorSemantic = true;
            Companion.undefinedIdentifierError(ctx.id.getText(), ctx.id.getLine());
        }
        else if(var_iter.getSupertype() != C_SUPERTYPE.VARIABLE_SUPERTYPE){
            errorSemantic = true;
            Companion.identifierIsNotAVariableError(ctx.id.getText(), ctx.id.getLine());
        }
        else if(var_iter.getType() != C_TYPE.INT_TYPE){
            errorSemantic = true;
            Companion.typeMismatchError2(ctx.id.getText(), ctx.id.getLine(), var_iter.getType().toString(), C_TYPE.INT_TYPE);
        }

        if(ctx.expr1.typ != C_TYPE.INT_TYPE){
            errorSemantic=true;
            Companion.typeMismatchError2("*expressio*", ctx.expr1.line, ctx.expr1.typ.toString(), C_TYPE.INT_TYPE);
        }

        if(ctx.expr2.typ != C_TYPE.INT_TYPE){
            errorSemantic=true;
            Companion.typeMismatchError2("*expressio*", ctx.expr2.line, ctx.expr2.typ.toString(), C_TYPE.INT_TYPE);
        }

        for(LANSetParser.SentenceContext s : ctx.sentence()) {
            ReturnStruct sentenceRs = visit(s);
            c1Code.addAll(sentenceRs.code);
        }

        if(!errorSemantic){
            Registre varIndex = TS.obtenir(ctx.id.getText());
            Long indexPos = varIndex.getDir();

            forExpr.add(program.ILOAD);
            forExpr.add(indexPos);
            forExpr.addAll(expr2Rs.code);
            forExpr.add(program.IF_ICMPGT); //comparar si it == expr2
            Long jump = (long) (c1Code.size() + 9);
            forExpr.add(program.nByte(jump,2)); //saltar al final de tot
            forExpr.add(program.nByte(jump,1));

            //inici: inicialitzar varIndex
            rs.code.addAll(expr1Rs.code);
            rs.code.add(program.ISTORE);
            rs.code.add(indexPos);

            //evaluar varIndex == fi
            rs.code.addAll(forExpr);
            //codi C1
            rs.code.addAll(c1Code);

            //i++
            rs.code.add(program.IINC);
            rs.code.add(indexPos);
            rs.code.add(1L);

            //tornar cap a dalt
            rs.code.add(program.GOTO);
            jump = -(long) (3 + c1Code.size() + forExpr.size());
            rs.code.add(program.nByte(jump,2));
            rs.code.add(program.nByte(jump,1));
        }

        return rs;
    }

    @Override
    public ReturnStruct visitWhile_loop(LANSetParser.While_loopContext ctx) {
        ReturnStruct rs = new ReturnStruct();
        Vector<Long> c1Code = new Vector<>();
        ReturnStruct exprRs = visit(ctx.expr());

        for(LANSetParser.SentenceContext s : ctx.sentence()) {
            ReturnStruct sentenceRs = visit(s);
            c1Code.addAll(sentenceRs.code);
        }

        if(ctx.expr().typ != C_TYPE.BOOL_TYPE){
            errorSemantic=true;
            Companion.typeMismatchError2("*expressio*", ctx.expr().line, ctx.expr().typ.toString(), C_TYPE.BOOL_TYPE);
        }
        else{ //no error, codegen
            rs.code.addAll(exprRs.code);

            rs.code.add(program.IFEQ); //if false, jumps to end of the while (c1.size()+6) (6 = ifjump1+ifjump2+goto+goto1+goto2+1)
            Long jump = 2L + c1Code.size() + 3L + 1L;
            rs.code.add(program.nByte(jump,2));
            rs.code.add(program.nByte(jump,1));
            rs.code.addAll(c1Code);

            jump = 0L-rs.code.size(); //jump to first instruction of expr, so -(c1.size + expr.code.size + 3) and also actual rs.code.size
            rs.code.add(program.GOTO);
            rs.code.add(program.nByte(jump,2));
            rs.code.add(program.nByte(jump,1));
        }
        
        return rs;
    }

    @Override
    public ReturnStruct visitFunction_call(LANSetParser.Function_callContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitRead_operation(LANSetParser.Read_operationContext ctx) {
        ReturnStruct rs = new ReturnStruct();
        Registre var = TS.obtenir(ctx.id.getText());

        if(var == null) {
            errorSemantic = true;
            Companion.undefinedIdentifierError(ctx.id.getText(), ctx.id.getLine());
        }
        else if(var.getSupertype() != C_SUPERTYPE.VARIABLE_SUPERTYPE) {
            errorSemantic = true;
            Companion.identifierIsNotAVariableError(ctx.id.getText(), ctx.id.getLine());
        }
        else if(Companion.processBaseType(var.getType()) == C_TYPE.INVALID_TYPE){ //is not a basetype
            errorSemantic = true;
            Companion.nonBasetypeReadingError(ctx.id.getText(), var.getType(), ctx.id.getLine());
        }
        else{ // no error, codegen
            rs.code.addAll(bytecodeWriter.generateReadCode(var.getType(), var.getDir()));
        }

        return rs;
    }

    private ReturnStruct internal_write(List<LANSetParser.ExprContext> expressions){
        ReturnStruct rs = new ReturnStruct();
        for (LANSetParser.ExprContext expr: expressions) {
            ReturnStruct rsExpr = visit(expr);
            if(!(Companion.isBasetype(expr.typ) || expr.typ == C_TYPE.STRING_TYPE)){
                errorSemantic = true;
                Companion.writeOperationUnsupportedTypeError(expr.typ, expr.line);
            }
            else{
                rs.code.addAll(rsExpr.code);
                rs.code.addAll(bytecodeWriter.generateWriteCode(expr.typ));
            }
        }
        return rs;
    }

    @Override
    public ReturnStruct visitWrite_operation(LANSetParser.Write_operationContext ctx) {
        return internal_write(ctx.expr());
    }

    @Override
    public ReturnStruct visitWriteln_operation(LANSetParser.Writeln_operationContext ctx) {
        ReturnStruct rs = internal_write(ctx.expr());
        rs.code.add(program.LDC_W);
        rs.code.add(program.nByte(lineBreak,2));
        rs.code.add(program.nByte(lineBreak,1));
        rs.code.addAll(bytecodeWriter.generateWriteCode(C_TYPE.STRING_TYPE));
        return rs;
    }

    @Override
    public ReturnStruct visitExpr(LANSetParser.ExprContext ctx) {
        ReturnStruct rs;
        if(ctx.ternary() != null){
            rs = visit(ctx.ternary());
            ctx.typ = ctx.ternary().typ;
            ctx.line = ctx.ternary().line;
        }
        else{
            rs = visit(ctx.subexpr());
            ctx.typ = ctx.subexpr().typ;
            ctx.line = ctx.subexpr().line;
        }
        return rs;
    }

    @Override
    public ReturnStruct visitDirectEvaluationCv(LANSetParser.DirectEvaluationCvContext ctx) {
        ReturnStruct rs = visit(ctx.cv);
        ctx.typ = ctx.cv.typ;
        ctx.line = ctx.cv.line;
        return rs;
    }

    @Override
    public ReturnStruct visitDirectEvaluationId(LANSetParser.DirectEvaluationIdContext ctx) {
        ReturnStruct rs = new ReturnStruct();
        Registre var = TS.obtenir(ctx.id.getText());

        ctx.typ = var.getType(); //TODO SOON, rethink this or otherwise the error won't show up
        ctx.line = ctx.id.getLine();

        if(var == null){
            Companion.undefinedIdentifierError(ctx.id.getText(), ctx.id.getLine());
            ctx.typ = C_TYPE.INVALID_TYPE;
            errorSemantic = true;
        }
        else if(var.getSupertype() == C_SUPERTYPE.VARIABLE_SUPERTYPE){
            C_TYPE varType = var.getType();
            if(Companion.isBasetype(varType)){
                if(varType == C_TYPE.FLOAT_TYPE)
                    rs.code.add(program.FLOAD);
                else
                    rs.code.add(program.ILOAD);

                Long varDir = var.getDir();
                rs.code.add(varDir);
            }
            else { //custom type (alias)
                /*Registre t = TS.obtenir(var.getType()); //existence validation is not needed.
                C_SUPERTYPE st = t.getSupertype();

                if(st == C_SUPERTYPE.ALIAS_SUPERTYPE) ctx.typ = t.getType();
                else ctx.typ = varType;*/
            }
        }
        else if (var.getSupertype() == C_SUPERTYPE.CONSTANT_SUPERTYPE){
            Long constDir = var.getDir();
            rs.code.add(program.LDC_W);
            rs.code.add(program.nByte(constDir,2));
            rs.code.add(program.nByte(constDir,1));
        }
        else {
            ctx.typ = C_TYPE.INVALID_TYPE;
            System.err.println("Error at line: " + ctx.line + ": Use of identifier \'" + ctx.id.getText() +"\' is not allowed here.");
            errorSemantic = true;
        }
        return rs;
    }

    @Override
    public ReturnStruct visitDirectEvaluationTuple(LANSetParser.DirectEvaluationTupleContext ctx) {
        ReturnStruct rs = visit(ctx.tuple_acces());
        ctx.typ = ctx.tuple_acces().typ;
        ctx.line = ctx.tuple_acces().line;
        return rs;
    }

    @Override
    public ReturnStruct visitDirectEvaluationVector(LANSetParser.DirectEvaluationVectorContext ctx) {
        ReturnStruct rs = visit(ctx.vector_acces());
        if(ctx.typ == C_TYPE.FLOAT_TYPE)
            rs.code.add(program.FALOAD);
        else
            rs.code.add(program.IALOAD);

        ctx.typ = ctx.vector_acces().typ;
        ctx.line = ctx.vector_acces().line;
        return rs;
    }

    @Override
    public ReturnStruct visitDirectEvaluationFunction(LANSetParser.DirectEvaluationFunctionContext ctx) {
        ReturnStruct rs = visit(ctx.function_call());
        ctx.typ = ctx.function_call().typ;
        ctx.line = ctx.function_call().line;
        return rs;
    }

    @Override
    public ReturnStruct visitConstant_value(LANSetParser.Constant_valueContext ctx) {
        ReturnStruct rs;
        if(ctx.b != null){ //is basetype
            rs = visit(ctx.b);
            ctx.typ = ctx.b.typ;
            ctx.line = ctx.b.line;
        }
        else{
            rs = new ReturnStruct();
            ctx.typ = C_TYPE.STRING_TYPE;
            ctx.line = ctx.s.getLine();

            Long str = program.addConstant(BYTECODE_STRINGTYPE, ctx.s.getText().substring(1,ctx.s.getText().length()-1));
            rs.code.add(program.LDC_W);
            rs.code.add(program.nByte(str,2));
            rs.code.add(program.nByte(str,1));
        }
        return rs;
    }

    @Override
    public ReturnStruct visitTuple_acces(LANSetParser.Tuple_accesContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitVector_acces(LANSetParser.Vector_accesContext ctx) {
        ReturnStruct rs = new ReturnStruct();
        Registre vecVarReg = TS.obtenir(ctx.vecVar.getText());

        if(vecVarReg == null){
            errorSemantic = true;
            Companion.undefinedIdentifierError(ctx.vecVar.getText(), ctx.vecVar.getLine());
        }
        else if(vecVarReg.getSupertype() != C_SUPERTYPE.VECTOR_SUPERTYPE){
            errorSemantic = true;
            System.err.println(String.format("Error at line %d. %s was expected to be a vector but was a %s %s instead",
                    ctx.vecVar.getLine(), ctx.vecVar.getText(), vecVarReg.getType().toString(), vecVarReg.getSupertype()));
        }
        else{
            ReturnStruct rsExpr = visit(ctx.expr());

            if(ctx.expr().typ != C_TYPE.INT_TYPE){
                errorSemantic = true;
                Companion.typeMismatchError2("*expression*", ctx.expr().line, ctx.expr().typ.toString(), C_TYPE.INT_TYPE);
            }
            else{
                rs.code.add(program.ALOAD); //push arrayref into operand stack
                rs.code.add(vecVarReg.getDir());
                rs.code.addAll(rsExpr.code); //push index of the array

                ctx.typ = vecVarReg.getType();
                ctx.line = ctx.vecVar.getLine();
                //do we need to return the identifier?
            }
        }

        return rs;
    }

    @Override
    public ReturnStruct visitTernary(LANSetParser.TernaryContext ctx) {
        ReturnStruct rs = visit(ctx.condition);
        ctx.line = ctx.condition.line;
        if(ctx.condition.typ != C_TYPE.BOOL_TYPE){
            errorSemantic=true;
            Companion.typeMismatchError(ctx.condition.typ, C_TYPE.BOOL_TYPE, ctx.condition.line);
        }

        ReturnStruct rsE1 = visit(ctx.e1);
        ctx.typ = ctx.e1.typ;

        ReturnStruct rsE2 = visit(ctx.e2);
        boolean e1fcast = false, e2fcast = false;

        if(ctx.e1.typ == C_TYPE.FLOAT_TYPE && ctx.e2.typ == C_TYPE.INT_TYPE) {
            e2fcast = true;
            ctx.typ = C_TYPE.FLOAT_TYPE;
        } //(dolar)e2.typ = C_TYPE.FLOAT_TYPE; //to real promotion
        else if(ctx.e1.typ == C_TYPE.INT_TYPE && ctx.e2.typ == C_TYPE.FLOAT_TYPE) {
            e1fcast = true;
            ctx.typ = C_TYPE.FLOAT_TYPE;
        } //(dolar)e1 b.typ = C_TYPE.FLOAT_TYPE; //to real promotion
        else if(ctx.e1.typ != ctx.e2.typ){ //Neither of these are real
            errorSemantic = true;
            Companion.ternaryTypeMismatchError(ctx.e1.typ, ctx.e2.typ, ctx.condition.line);
        }

        if(!errorSemantic){
            rs.code.add(program.IFEQ); //if false, jumps to e2 (e1.size()+6)
            Long jump = 2L + rsE1.code.size() + 1L + 3L + 1L; //if condition is false, jump to e2 (6 = ifjump1+ifjump2+i2f+goto+goto1+goto2+1)
            rs.code.add(program.nByte(jump,2));
            rs.code.add(program.nByte(jump,1));
            rs.code.addAll(rsE1.code);
            if(e1fcast)
                rs.code.add(program.I2F);
            else
                rs.code.add(program.NOP);

            jump = 2L + rsE2.code.size() + 1L + 1L; //if we're on the end of e1, skip e2 + 4 (gotoinstr + goto1 + goto2 + i2f + 1)
            rs.code.add(program.GOTO);
            rs.code.add(program.nByte(jump,2));
            rs.code.add(program.nByte(jump,1));
            rs.code.addAll(rsE2.code);
            if(e2fcast)
                rs.code.add(program.I2F);
            else
                rs.code.add(program.NOP);
        }

        return rs;
    }

    @Override
    public ReturnStruct visitSubexpr(LANSetParser.SubexprContext ctx) {
        ReturnStruct rs = visit(ctx.t1);
        ctx.typ = ctx.t1.typ;
        ctx.line = ctx.t1.line;
        for(int i=0;i<ctx.logic_operators().size();i++){
            LANSetParser.Logic_operatorsContext operator = ctx.logic_operators(i);
            visit(operator);
            if(i == 0){ //if there's at least one operation, typecheck of t1 is needed
                if(ctx.t1.typ != C_TYPE.BOOL_TYPE){
                    errorSemantic = true;
                    Companion.operatorTypeMismatchError(ctx.t1.typ, operator.text, operator.line, C_TYPE.BOOL_TYPE);
                }
            }
            LANSetParser.Term1Context t2 = ctx.term1(i+1);
            ReturnStruct rsT2 = visit(t2);
            if(ctx.t1.typ != C_TYPE.BOOL_TYPE){ //check if the right operand is boolean
                errorSemantic = true;
                Companion.operatorTypeMismatchError(t2.typ, operator.text, operator.line, C_TYPE.BOOL_TYPE);
            }
            else{ //no error
                //TODO implement short-circuit?
                rs.code.addAll(rsT2.code);

                if(operator.tk_type == TK_AND) rs.code.add(program.IAND);
                else rs.code.add(program.IOR);
            }
        }

        return rs;
    }

    @Override
    public ReturnStruct visitLogic_operators(LANSetParser.Logic_operatorsContext ctx) {
        ctx.text = ctx.tk.getText();
        ctx.tk_type = ctx.tk.getType();
        ctx.line = ctx.tk.getLine();
        return null;
    }

    @Override
    public ReturnStruct visitTerm1(LANSetParser.Term1Context ctx) {

        LANSetParser.Term2Context leftTerm = ctx.leftTerm;
        ReturnStruct rs = visit(leftTerm);
        ctx.line = leftTerm.line;
        ctx.typ = leftTerm.typ;

        LANSetParser.Equality_operatorContext operator = ctx.equality_operator();
        if(operator != null){
            visit(operator);
            ctx.typ = C_TYPE.BOOL_TYPE;
            //Todo: We should be able to simplify checking, some it's likely redundant
            if(operator.tk_type == TK_EQUALS || operator.tk_type == TK_NEQUALS){
                if(!Companion.isBasetype(leftTerm.typ)){
                    errorSemantic = true;
                    Companion.operatorTypeMismatchError(leftTerm.typ, operator.text, operator.line, "basic type");
                }
            }
            else if(!Companion.isNumberType(leftTerm.typ)){ //other operations only work on integer or real numbers
                errorSemantic = true;
                Companion.operatorTypeMismatchError(leftTerm.typ, operator.text, operator.line,
                        C_TYPE.INT_TYPE + " or " + C_TYPE.FLOAT_TYPE);
            }

            LANSetParser.Term2Context rightTerm = ctx.rightTerm;
            ReturnStruct rsRightTerm = visit(rightTerm);

            boolean leftTermIsNumber = Companion.isNumberType(leftTerm.typ);
            boolean rightTermIsNumber = Companion.isNumberType(rightTerm.typ);
            boolean atLeastOneIsNumber = leftTermIsNumber || rightTermIsNumber;
            boolean bothAreNumbers = leftTermIsNumber && rightTermIsNumber;
            if(atLeastOneIsNumber && !bothAreNumbers){
                errorSemantic = true;
                Companion.comparisonNotAllowed(leftTerm.line, leftTerm.typ, rightTerm.typ);
            }
            else if(atLeastOneIsNumber){ //bothAreNumbers is implied
                if (leftTerm.typ == C_TYPE.INT_TYPE && rightTerm.typ == C_TYPE.INT_TYPE) {
                    bytecodeWriter.compareIntegerTypes(rs.code, operator, rsRightTerm.code);
                }
                else{
                    if(leftTerm.typ != C_TYPE.FLOAT_TYPE) rs.code.add(program.I2F);
                    rs.code.addAll(rsRightTerm.code);
                    if(rightTerm.typ != C_TYPE.FLOAT_TYPE) rs.code.add(program.I2F);

                    bytecodeWriter.compareFloatTypes(rs.code, operator);
                }
            }
            else if(leftTerm.typ == rightTerm.typ){
                bytecodeWriter.compareIntegerTypes(rs.code, operator, rsRightTerm.code);
            }
            else{
                errorSemantic = true;
                Companion.comparisonNotAllowed(leftTerm.line, leftTerm.typ, rightTerm.typ);
            }
        }

        return rs;
    }

    @Override
    public ReturnStruct visitEquality_operator(LANSetParser.Equality_operatorContext ctx) {
        ctx.text = ctx.tk.getText();
        ctx.tk_type = ctx.tk.getType();
        ctx.line = ctx.tk.getLine();
        return null;
    }

    @Override
    public ReturnStruct visitTerm2(LANSetParser.Term2Context ctx) {
        C_TYPE leftType;

        ReturnStruct rs = visit(ctx.t1);
        ctx.line = ctx.t1.line;
        leftType = ctx.t1.typ;

        for(int i=0;i<ctx.addition_operators().size();i++) {
            LANSetParser.Addition_operatorsContext operator = ctx.addition_operators(i);
            visit(operator);

            if(i == 0) //first left operand found, typecheck needed
                if(!Companion.isNumberType(leftType)){
                    errorSemantic = true;
                    Companion.operatorTypeMismatchError(ctx.t1.typ, operator.text, operator.line, C_TYPE.INT_TYPE + " or " + C_TYPE.FLOAT_TYPE);
                    //$leftType = C_TYPE.INT_TYPE; //typing error, propagate less restrictive type in order to continue semantic analysis
                }

            LANSetParser.Term3Context t2 = ctx.term3(i + 1);
            ReturnStruct rsT2 = visit(t2);

            if(t2.typ == C_TYPE.FLOAT_TYPE){ //if t2 is float type, integer promotion may be needed

                if(leftType == C_TYPE.INT_TYPE) rs.code.add(program.I2F);
                rs.code.addAll(rsT2.code); //right operand

                if(operator.tk_type == TK_SUM) rs.code.add(program.FADD);
                else rs.code.add(program.FSUB);

                leftType = C_TYPE.FLOAT_TYPE;
            }
            else if (t2.typ == C_TYPE.INT_TYPE){ //if t2 is integer, the result depends on leftType.

                rs.code.addAll(rsT2.code); //right operand

                if(leftType == C_TYPE.FLOAT_TYPE){
                    rs.code.add(program.I2F);
                    if(operator.tk_type == TK_SUM) rs.code.add(program.FADD);
                    else rs.code.add(program.FSUB);
                }
                else{
                    if(operator.tk_type == TK_SUM) rs.code.add(program.IADD);
                    else rs.code.add(program.ISUB);
                }
            }
            else{ //typing error
                errorSemantic = true;
                Companion.operatorTypeMismatchError(t2.typ, operator.text, operator.line, C_TYPE.INT_TYPE + " or " + C_TYPE.FLOAT_TYPE);
                leftType = C_TYPE.INT_TYPE; //typing error, propagate less restrictive type in order to continue semantic analysis
            }
        }

        ctx.typ = leftType;
        return rs;
    }

    @Override
    public ReturnStruct visitAddition_operators(LANSetParser.Addition_operatorsContext ctx) {
        ctx.text = ctx.tk.getText();
        ctx.tk_type = ctx.tk.getType();
        ctx.line = ctx.tk.getLine();
        return null;
    }

    @Override
    public ReturnStruct visitTerm3(LANSetParser.Term3Context ctx) {
        C_TYPE leftType;
        ReturnStruct rs = visit(ctx.t1);
        leftType = ctx.t1.typ;
        for(int i=0;i<ctx.multiplication_operators().size();i++) {
            LANSetParser.Multiplication_operatorsContext operator = ctx.multiplication_operators(i);
            visit(operator); //cal?

            LANSetParser.Term4Context t2 = ctx.term4(i + 1);
            ReturnStruct rsT2 = visit(t2);

            if( (operator.tk_type == TK_INTDIV || operator.tk_type == TK_MODULO) ){ // integer division or modulo
                if( t2.typ != C_TYPE.INT_TYPE ){ //if t2 it's not an integer
                    errorSemantic = true;
                    Companion.operatorTypeMismatchError(t2.typ, operator.text, operator.line, C_TYPE.INT_TYPE);
                }
                else if( leftType != C_TYPE.INT_TYPE ){ //if left operand is not an integer
                    errorSemantic = true;
                    Companion.operatorTypeMismatchError(leftType, operator.text, operator.line, C_TYPE.INT_TYPE);
                }
                else{ //no error
                    rs.code.addAll(rsT2.code); //right operand

                    if(operator.tk_type == TK_INTDIV) rs.code.add(program.IDIV);
                    else rs.code.add(program.IREM);
                }

                leftType = C_TYPE.INT_TYPE;
            }
            else{ //real division and multiplication
                boolean errorLocal = false;

                if(!Companion.isNumberType(t2.typ)){
                    errorSemantic = true;
                    errorLocal = true;
                    Companion.operatorTypeMismatchError(t2.typ, operator.text, operator.line, C_TYPE.INT_TYPE + " or " + C_TYPE.FLOAT_TYPE);
                }

                if(!Companion.isNumberType(leftType)){
                    errorSemantic = true;
                    errorLocal = true;
                    Companion.operatorTypeMismatchError(leftType, operator.text, operator.line, C_TYPE.INT_TYPE + " or " + C_TYPE.FLOAT_TYPE);
                }

                if(operator.tk_type == TK_DIV) {
                    if(leftType == C_TYPE.INT_TYPE) rs.code.add(program.I2F);
                    rs.code.addAll(rsT2.code); //right operand
                    if(t2.typ == C_TYPE.INT_TYPE) rs.code.add(program.I2F);
                    rs.code.add(program.FDIV);

                    leftType = C_TYPE.FLOAT_TYPE; //division always spits a real number.
                }
                else{ //multiplication
                    if(t2.typ == C_TYPE.INT_TYPE && leftType == C_TYPE.INT_TYPE) {
                        rs.code.addAll(rsT2.code); //right operand
                        rs.code.add(program.IMUL);

                        leftType = C_TYPE.INT_TYPE; //if both are integer type
                    }
                    else if(!errorLocal){

                        if(leftType == C_TYPE.INT_TYPE) rs.code.add(program.I2F);
                        rs.code.addAll(rsT2.code); //right operand
                        if(t2.typ == C_TYPE.INT_TYPE) rs.code.add(program.I2F);
                        rs.code.add(program.FMUL);

                        leftType = C_TYPE.FLOAT_TYPE; //at least one of them is real, and the other is real or integer
                    }
                    else leftType = C_TYPE.INT_TYPE;//typing error, propagate less restrictive type in order to continue semantic analysis
                }

            }

        }
        //@after
        ctx.typ = leftType;
        ctx.line = ctx.t1.line;
        return rs;
    }

    @Override
    public ReturnStruct visitMultiplication_operators(LANSetParser.Multiplication_operatorsContext ctx) {
        ctx.text = ctx.tk.getText();
        ctx.tk_type = ctx.tk.getType();
        ctx.line = ctx.tk.getLine();
        return null;
    }

    @Override
    public ReturnStruct visitTerm4(LANSetParser.Term4Context ctx) {
        LANSetParser.Term5Context term5 = ctx.term5();
        ReturnStruct rs = visit(term5);
        LANSetParser.Negation_operatorsContext operator = ctx.negation_operators();
        if(operator != null){
            visit(operator);
            if(operator.tk_type == TK_INVERT && !Companion.isNumberType(term5.typ)){
                errorSemantic = true;
                Companion.operatorTypeMismatchError(term5.typ, operator.text, term5.line, C_TYPE.INT_TYPE + " or " + C_TYPE.FLOAT_TYPE);
            }
            else if (operator.tk_type == KW_NO && term5.typ != C_TYPE.BOOL_TYPE){
                errorSemantic = true;
                Companion.operatorTypeMismatchError(term5.typ, operator.text, term5.line, C_TYPE.BOOL_TYPE);
            }
            else{
                if(operator.tk_type == KW_NO){
                    rs.code.add(program.ICONST_1);
                    rs.code.add(program.IXOR); //1 xor any = !any
                }
                else{ //KW_INVERT
                    if(term5.typ == C_TYPE.INT_TYPE)
                        rs.code.add(program.INEG);
                    else
                        rs.code.add(program.FNEG);
                }
            }
        }
        ctx.typ = term5.typ;
        ctx.line = term5.line;
        return rs;
    }

    @Override
    public ReturnStruct visitNegation_operators(LANSetParser.Negation_operatorsContext ctx) {
        ReturnStruct rs = new ReturnStruct();
        ctx.text = ctx.tk.getText();
        ctx.tk_type = ctx.tk.getType();
        return rs;
    }

    @Override
    public ReturnStruct visitTerm5(LANSetParser.Term5Context ctx) {
        ReturnStruct rs;
        LANSetParser.Direct_evaluation_exprContext directEvalExpr = ctx.direct_evaluation_expr();
        LANSetParser.ExprContext expr = ctx.expr();
        if(directEvalExpr != null){
            rs = visit(directEvalExpr);
            ctx.typ = directEvalExpr.typ;
            ctx.line = directEvalExpr.line;
        }
        else{
            rs = visit(expr);
            ctx.typ = expr.typ;
            ctx.line = expr.line;
        }
        return rs;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    private int visitCount = 0;
    @Override
    public ReturnStruct visit(ParseTree parseTree) {
        visitCount++;
        return parseTree.accept(this);
    }

    @Override
    //TODO investigate
    public ReturnStruct visitTerminal(TerminalNode node) {
        return this.defaultResult();
    }

    @Override
    //TODO investigate
    public ReturnStruct visitErrorNode(ErrorNode node) {
        return this.defaultResult();
    }

    @Override
    public ReturnStruct defaultResult(){
        return new ReturnStruct();
    }

    @Override
    protected ReturnStruct aggregateResult(ReturnStruct aggregate, ReturnStruct nextResult) {
        aggregate.code.addAll(nextResult.code);
        return aggregate;
    }

    @Override
    protected boolean shouldVisitNextChild(RuleNode node, ReturnStruct currentResult) {
        return true; //TODO: Can we optimize?
    }

    //TODO move some variables?
    private SymTable<Registre> TS = new SymTable<Registre>(100);
    private Bytecode program;
    private BytecodeWriter bytecodeWriter;
    private Long lineBreak;
    private Long nVar = 0L;
    private boolean errorSemantic;

    //constants
    private static final Long mainStackSize = 500L;

    //non static methods
    private void registerBasetypeVariable(C_TYPE type, Token id){
        TS.inserir(id.getText(), new Registre(id.getText(), C_SUPERTYPE.VARIABLE_SUPERTYPE, type, id.getLine(), id.getCharPositionInLine()));
    }

    private void registerAlias(Token id, Token type){
        C_TYPE bType = Companion.processBaseType(C_TYPE.fromString(type.getText())); //We cannot use cTypeFromTokenID here
        Registre r = new Registre(id.getText(), C_SUPERTYPE.ALIAS_SUPERTYPE, bType, id.getLine(), id.getCharPositionInLine());
        TS.inserir(id.getText(), r);
    }

    private Registre registerConstant(Token id, Token type){
        C_TYPE bType = Companion.processBaseType(C_TYPE.fromString(type.getText()));
        Registre r = new Registre(id.getText(), C_SUPERTYPE.CONSTANT_SUPERTYPE, bType, id.getLine(), id.getCharPositionInLine());
        TS.inserir(id.getText(), r);
        return r;
    }

    private boolean identifierInUse(String id){
        return TS.existeix(id);
    }
}
