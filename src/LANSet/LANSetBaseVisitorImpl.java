package LANSet;

import LANSet.Bytecode.Bytecode;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Vector;

import static LANSet.LANSetLexer.*;
import static LANSet.LANSetParser.TK_IDENTIFIER;

public class LANSetBaseVisitorImpl extends LANSetBaseVisitor<ReturnStruct>{

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
        nextResult.code.addAll(aggregate.code);
        return nextResult;
    }

    @Override
    protected boolean shouldVisitNextChild(RuleNode node, ReturnStruct currentResult) {
        return true; //TODO: Can we optimize?
    }

    @Override
    public ReturnStruct visitStart(LANSetParser.StartContext ctx) {
        System.out.println("Hola, " +  ctx.prog_id.getText());

        for(int i=0; i<ctx.getChildCount(); i++){
            ParseTree pt = ctx.getChild(i);
            ReturnStruct rs = visit(pt);
            code.addAll(rs.code);
        }

        /*visitType_declaration_block(ctx.type_declaration_block());
        visitFunc_declaration_block(ctx.func_declaration_block());*/

        System.out.println(visitCount);

        return null;
        //return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitType_declaration_block(LANSetParser.Type_declaration_blockContext ctx) {
        System.out.println("visitType_declaration_block");
        return visitChildren(ctx);
        /*if(ctx != null){ //perquè apareix com a opcional en algun lloc
            //TODO
        }
        else System.out.println("Soc null");
        return null;*/
    }

    @Override
    public ReturnStruct visitType_declaration(LANSetParser.Type_declarationContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitVector_definition(LANSetParser.Vector_definitionContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitTuple_definition(LANSetParser.Tuple_definitionContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitFunc_declaration_block(LANSetParser.Func_declaration_blockContext ctx) {
        System.out.println("visitFunc_declaration_block");
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
    public ReturnStruct visitConst_declaration_block(LANSetParser.Const_declaration_blockContext ctx) {
        System.out.println("visitConst_declaration_block");
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitConst_declaration(LANSetParser.Const_declarationContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitBasetype_value(LANSetParser.Basetype_valueContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitVar_declaration_block(LANSetParser.Var_declaration_blockContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitVar_declaration(LANSetParser.Var_declarationContext ctx) {
        ReturnStruct rs = visitChildren(ctx);
        if(ctx.type().tkType == TK_IDENTIFIER) {
            System.out.println("TODO: alias, tuple or vector variable detected");
            if(!identifierInUse(ctx.type().text)){
                errorSemantic = true;
                Companion.undefinedTypeError(ctx.type().text, ctx.type().line);
            }
        }
        else if(identifierInUse(ctx.id.getText())){
            errorSemantic = true;
            Companion.repeatedIdentifierError(ctx.id.getText(), ctx.id.getLine());
        }
        else {
            registerBasetypeVariable(C_TYPE.fromString(ctx.type().getText()), ctx.id);
            TS.obtenir(ctx.id.getText()).putDir(nVar++);
        }
        return rs;
    }

    @Override
    public ReturnStruct visitFunc_implementation_block(LANSetParser.Func_implementation_blockContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitAction_definition(LANSetParser.Action_definitionContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitFunction_definition(LANSetParser.Function_definitionContext ctx) {
        return visitChildren(ctx);
    }

    /*@Override
    public ReturnStruct visitSentence(LANSetParser.SentenceContext ctx) {
        return visitChildren(ctx);
    }*/

    @Override
    public ReturnStruct visitAssignment(LANSetParser.AssignmentContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitLvalue(LANSetParser.LvalueContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitConditional(LANSetParser.ConditionalContext ctx) {
        System.out.println("Condicional " + ctx.expr().getText());
        ReturnStruct rs = new ReturnStruct();
        Vector<Long> c1Code = new Vector<>(), c2Code = new Vector<>();

        for (LANSetParser.SentenceContext sentence: ctx.sentence())
            c1Code.addAll(visit(sentence).code);
        for (LANSetParser.Else_sentenceContext sentence: ctx.else_sentence())
            c2Code.addAll(visit(sentence).code); //espero que entri a la sentencia real i retorni el valor correctament...

        if(false){
        //if(ctx.expr().typ!=C_TYPE.BOOL_TYPE){ //temporarily out
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
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitWhile_loop(LANSetParser.While_loopContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitFunction_call(LANSetParser.Function_callContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitRead_operation(LANSetParser.Read_operationContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitWrite_operation(LANSetParser.Write_operationContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitWriteln_operation(LANSetParser.Writeln_operationContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitExpr(LANSetParser.ExprContext ctx) {
        return visitChildren(ctx); //Ok
    }

    /*@Override
    public ReturnStruct visitDirect_evaluation_expr(LANSetParser.Direct_evaluation_exprContext ctx) {
        return visitChildren(ctx);
    }*/

    @Override
    public ReturnStruct visitConstant_value(LANSetParser.Constant_valueContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitTuple_acces(LANSetParser.Tuple_accesContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitVector_acces(LANSetParser.Vector_accesContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitTernary(LANSetParser.TernaryContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitSubexpr(LANSetParser.SubexprContext ctx) {
        boolean hasOperator = false;
        ReturnStruct rs = visit(ctx.t1);
        ctx.typ = ctx.t1.typ;
        ctx.line = ctx.t1.line;
        //ReturnStruct rs = visitChildren(ctx);
        for(int i=0;i<ctx.logic_operators().size();i++){
            LANSetParser.Logic_operatorsContext operator = ctx.logic_operators(i);
            visit(operator);
            if(!hasOperator){ //if there's at least one operation, typecheck of t1 is needed
                hasOperator = true;
                if(ctx.t1.typ != C_TYPE.BOOL_TYPE){
                    errorSemantic = true;
                    Companion.operatorTypeMismatchError(ctx.t1.typ, operator.text, operator.line, C_TYPE.BOOL_TYPE);
                }
            }
            LANSetParser.Term1Context t2 = ctx.term1(i+1);
            ReturnStruct rsT2 = visit(t2);
            if(ctx.t1.typ!=C_TYPE.BOOL_TYPE){ //check if the right operand is boolean
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
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitTerm1(LANSetParser.Term1Context ctx) {
        boolean hasOperator = false;
        C_TYPE leftType;

        ReturnStruct rs = visit(ctx.t1);
        ctx.typ = ctx.t1.typ;
        ctx.line = ctx.t1.line;
        leftType = ctx.t1.typ;

        for(int i=0;i<ctx.equality_operator().size();i++){
            LANSetParser.Equality_operatorContext operator = ctx.equality_operator(i);
            visit(operator);
            if(!hasOperator){ //first left operand found, typecheck needed
                hasOperator = true;
                if(operator.tk_type == TK_EQUALS || operator.tk_type == TK_NEQUALS ) { // == or !=
                    if( !(Companion.isBasetype(leftType)) ){ //if the operator isn't a basic type
                        errorSemantic = true;
                        Companion.operatorTypeMismatchError(ctx.t1.typ, operator.text, operator.line, "basic type");
                        //impossible to propagate a "less restrictive" type, so let's propagate the original one.
                    }
                    //otherwise, leftType should be propagated
                }
                else{ //other operations only work on integer or real numbers
                    if( !(leftType==C_TYPE.INT_TYPE || leftType==C_TYPE.FLOAT_TYPE) ){
                        errorSemantic = true;
                        Companion.operatorTypeMismatchError(ctx.t1.typ, operator.text, operator.line, C_TYPE.INT_TYPE + " or " + C_TYPE.FLOAT_TYPE);
                        leftType = C_TYPE.INT_TYPE; //typing error, propagate less restrictive type in order to continue semantic analysis
                    }
                    //otherwise, leftType should be propagated
                }
            }
            else{ System.err.println("FAIG ALGO, NO EM BORRIS (hasOperator)");}

            LANSetParser.Term2Context t2 = ctx.term2(i+1);
            ReturnStruct rsT2 = visit(t2);

            //Todo: simplify, clean or simply rewrite
            //region gatell
            {
                boolean t1fcast = false, t2fcast = false;

                if (operator.tk_type == TK_EQUALS || operator.tk_type == TK_NEQUALS) { // == or !=
                    if (!(Companion.isBasetype(t2.typ))) { //if the operator isn't a basic type
                        errorSemantic = true;
                        Companion.operatorTypeMismatchError(ctx.t1.typ, operator.text, operator.line, "basic type");
                        //impossible to propagate a "less restrictive" type, so let's propagate the original one.
                    } else if (leftType == C_TYPE.INT_TYPE || t2.typ == C_TYPE.INT_TYPE) {
                        if (leftType == C_TYPE.INT_TYPE && t2.typ == C_TYPE.FLOAT_TYPE) {

                            rs.code.add(program.I2F);
                            rs.code.addAll(rsT2.code);
                            rs.code.add(program.FCMPL);

                            //falta canviar segons si es == o !=

                            leftType = C_TYPE.FLOAT_TYPE;
                        } else if (leftType == C_TYPE.FLOAT_TYPE && t2.typ == C_TYPE.INT_TYPE) {

                            rs.code.addAll(rsT2.code);
                            rs.code.add(program.I2F);
                            rs.code.add(program.FCMPL);

                            //falta canviar segons si es == o !=

                            leftType = C_TYPE.FLOAT_TYPE; //unnecessary assignment
                        } else if (!(leftType == C_TYPE.INT_TYPE && t2.typ == C_TYPE.INT_TYPE)) { //error, leftType and t2 types doesn't match //cane: aquest error no es pot trigerejar mai
                            errorSemantic = true;
                            Companion.typeMismatchError(leftType, t2.typ, operator.line);
                        } else { //both integers
                            System.err.println("NO IMPLEMENTAT!");
                        }
                    } else if (leftType != t2.typ) {
                        errorSemantic = true;
                        Companion.typeMismatchError(leftType, t2.typ, operator.line);
                    } else { //both equals but not integers (bytecode integers) (cane: aka both are float, car or bool)
                        System.err.println("NO IMPLEMENTAT!");
                    }

                    leftType = C_TYPE.BOOL_TYPE;
                } //end == or !=

                else { //other operations only work on integer or real numbers
                    if (!(t2.typ == C_TYPE.INT_TYPE || t2.typ == C_TYPE.FLOAT_TYPE)) {
                        errorSemantic = true;
                        Companion.operatorTypeMismatchError(leftType, operator.text, operator.line, C_TYPE.INT_TYPE + " or " + C_TYPE.FLOAT_TYPE);
                    } else { //if its integer or real
                        if (t2.typ == C_TYPE.FLOAT_TYPE) {

                            if (leftType == C_TYPE.INT_TYPE) rs.code.add(program.I2F);
                            rs.code.addAll(rsT2.code);
                            rs.code.add(program.FCMPL);

                            if (operator.tk_type == TK_LESS) rs.code.add(program.IFLT);
                            else if (operator.tk_type == TK_LESSEQ) rs.code.add(program.IFLE);
                            else if (operator.tk_type == TK_GREATER) rs.code.add(program.IFGT);
                            else rs.code.add(program.IFGE); //GREATEREQ

                            leftType = C_TYPE.FLOAT_TYPE; //integer promotion if needed
                        } else if (leftType == C_TYPE.FLOAT_TYPE) { //otherwise promotion depends on leftType, so no changes needed

                            rs.code.addAll(rsT2.code);
                            rs.code.add(program.I2F);
                            rs.code.add(program.FCMPL);

                            if (operator.tk_type == TK_LESS) rs.code.add(program.IFLT);
                            else if (operator.tk_type == TK_LESSEQ) rs.code.add(program.IFLE);
                            else if (operator.tk_type == TK_GREATER) rs.code.add(program.IFGT);
                            else rs.code.add(program.IFGE); //GREATEREQ

                            //leftType is automatically propagated
                        } else { //both are integers
                            rs.code.addAll(rsT2.code);

                            if (operator.tk_type == TK_LESS) rs.code.add(program.IF_ICMPLT);
                            else if (operator.tk_type == TK_LESSEQ) rs.code.add(program.IF_ICMPLE);
                            else if (operator.tk_type == TK_GREATER) rs.code.add(program.IF_ICMPGT);
                            else rs.code.add(program.IF_ICMPGE); //GREATEREQ
                        }

                        Long jump = 8L; //jump to true
                        rs.code.add(program.nByte(jump, 2));
                        rs.code.add(program.nByte(jump, 1));

                        //section of code dedicated only to compare two numbers //

                        rs.code.add(program.BIPUSH);
                        rs.code.add(0L); //put a 0

                        jump = 5L; //bipush length
                        rs.code.add(program.GOTO);
                        rs.code.add(program.nByte(jump, 2));
                        rs.code.add(program.nByte(jump, 1));

                        rs.code.add(program.BIPUSH);
                        rs.code.add(1L); //put a 1

                        //////////////////////////////////////////////////////////
                    }

                    leftType = C_TYPE.BOOL_TYPE;
                }
            }

        }
        //@after
        ctx.typ = leftType;
        return rs;
        //return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitEquality_operator(LANSetParser.Equality_operatorContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitTerm2(LANSetParser.Term2Context ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitAddition_operators(LANSetParser.Addition_operatorsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitTerm3(LANSetParser.Term3Context ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitMultiplication_operators(LANSetParser.Multiplication_operatorsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitTerm4(LANSetParser.Term4Context ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitNegation_operators(LANSetParser.Negation_operatorsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public ReturnStruct visitTerm5(LANSetParser.Term5Context ctx) {
        return visitChildren(ctx);
    }

    private int visitCount = 0;
    @Override
    public ReturnStruct visit(ParseTree parseTree) {
        visitCount++;
        return parseTree.accept(this);
    }

    /*int visitCount = 0;
    @Override
    public ReturnStruct visit(ParseTree parseTree) {
        visitCount++;
        //System.out.println("Soc un node qualsevol");
        //return visit(parseTree);
        return null;
    }

    @Override
    public ReturnStruct visitChildren(RuleNode ruleNode) {
        return visitChildren(ruleNode);
    }

    @Override
    public ReturnStruct visitTerminal(TerminalNode terminalNode) {
        return visitTerminal(terminalNode);
    }

    @Override
    public ReturnStruct visitErrorNode(ErrorNode errorNode) {
        return visitErrorNode(errorNode);
    }*/

    //Warning: zona radioactiva
    //todo moure?
    private SymTable<Registre> TS = new SymTable<Registre>(1000);
    private Bytecode program;
    private Long lineBreak;
    private Long nVar = 0L;
    private boolean errorSemantic;
    Vector<Long> code = new Vector<>();

    //constants
    private static final Long mainStackSize = 500L;

    private String classFile = "";

    //TODO: comprovar si aixo servia per algo
    /*
    @Override
    public void notifyErrorListeners(Token offendingToken, String msg, RecognitionException e)
    {
        //Si volem conservar el comportament inicial
        super.notifyErrorListeners(offendingToken,msg,e);
        //Codi personalitzat
    }*/

    //non static methods
    private void setLANSClassFile(String cf){
        classFile = cf;
    }

    private void registerBasetypeVariable(C_TYPE type, Token id){
        TS.inserir(id.getText(), new Registre(id.getText(), C_SUPERTYPE.VARIABLE_SUPERTYPE, type, id.getLine(), id.getCharPositionInLine()));
    }

    private void registerAlias(Token id, Token type){
        C_TYPE bType = Companion.processBaseType(C_TYPE.fromString(type.getText()));
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
