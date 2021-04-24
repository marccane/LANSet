package LANSet;

import LANSet.Bytecode.Bytecode;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Vector;

import static LANSet.LANSetParser.TK_IDENTIFIER;

public class LANSetBaseVisitorImpl extends LANSetBaseVisitor<returnStruct>{

    @Override
    public returnStruct visitStart(LANSetParser.StartContext ctx) {
        System.out.println("Hola, " +  ctx.prog_id.getText());

        for(int i=0; i<ctx.getChildCount(); i++){
            ParseTree pt = ctx.getChild(i);
            returnStruct rs = visit(pt);
            //if(pt.getClass() == LANSet.LANSetParser.SentenceActionContext)
            if(rs != null)
                code.addAll(rs.code);
            /*if(rs != null){
                System.out.println("not null");
            }*/
        }
        /*ctx.type_declaration_block();
        ctx.func_declaration_block();
        ctx.const_declaration_block();*/

        /*visitType_declaration_block(ctx.type_declaration_block());
        visitFunc_declaration_block(ctx.func_declaration_block());*/

        //System.out.println(visitCount);

        returnStruct rs = new returnStruct();
        rs.code = code;
        return rs;
        //return visitChildren(ctx);
    }

    @Override
    public returnStruct visitInici(LANSetParser.IniciContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitType_declaration_block(LANSetParser.Type_declaration_blockContext ctx) {
        System.out.println("visitType_declaration_block");
        if(ctx != null){ //perquè apareix com a opcional en algun lloc
            //TODO
            return visitChildren(ctx);
        }
        else System.out.println("Soc null");
        return null;
    }

    @Override
    public returnStruct visitType_declaration(LANSetParser.Type_declarationContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitVector_definition(LANSetParser.Vector_definitionContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitTuple_definition(LANSetParser.Tuple_definitionContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitFunc_declaration_block(LANSetParser.Func_declaration_blockContext ctx) {
        System.out.println("visitFunc_declaration_block");
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitAction_declaration(LANSetParser.Action_declarationContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitFunction_declaration(LANSetParser.Function_declarationContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitFormal_parameters(LANSetParser.Formal_parametersContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitType(LANSetParser.TypeContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitConst_declaration_block(LANSetParser.Const_declaration_blockContext ctx) {
        System.out.println("visitConst_declaration_block");
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitConst_declaration(LANSetParser.Const_declarationContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitBasetype_value(LANSetParser.Basetype_valueContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitVar_declaration_block(LANSetParser.Var_declaration_blockContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitVar_declaration(LANSetParser.Var_declarationContext ctx) {
        if(ctx.t.tkType == TK_IDENTIFIER) {
            System.out.println("TODO: alias, tuple or vector variable detected");
            if(!identifierInUse(ctx.t.text)){
                errorSemantic = true;
                Companion.undefinedTypeError(ctx.t.text, ctx.t.line);
            }
        }
        else if(identifierInUse(ctx.id.getText())){
            errorSemantic = true;
            Companion.repeatedIdentifierError(ctx.id.getText(), ctx.id.getLine());
        }
        else {
            registerBasetypeVariable(C_TYPE.fromString(ctx.t.getText()), ctx.id);
            TS.obtenir(ctx.id.getText()).putDir(nVar++);
        }
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitFunc_implementation_block(LANSetParser.Func_implementation_blockContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitAction_definition(LANSetParser.Action_definitionContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitFunction_definition(LANSetParser.Function_definitionContext ctx) {
        return visitChildren(ctx);
    }

    /*@Override
    public returnStruct visitSentence(LANSetParser.SentenceContext ctx) {
        return visitChildren(ctx);
    }*/

    @Override
    public returnStruct visitAssignment(LANSetParser.AssignmentContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitLvalue(LANSetParser.LvalueContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitConditional(LANSetParser.ConditionalContext ctx) {
        System.out.println("Condicional " + ctx.expr().getText());
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitFor_loop(LANSetParser.For_loopContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitWhile_loop(LANSetParser.While_loopContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitFunction_call(LANSetParser.Function_callContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitRead_operation(LANSetParser.Read_operationContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitWrite_operation(LANSetParser.Write_operationContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitWriteln_operation(LANSetParser.Writeln_operationContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitExpr(LANSetParser.ExprContext ctx) {
        return visitChildren(ctx);
    }

    /*@Override
    public returnStruct visitDirect_evaluation_expr(LANSetParser.Direct_evaluation_exprContext ctx) {
        return visitChildren(ctx);
    }*/

    @Override
    public returnStruct visitConstant_value(LANSetParser.Constant_valueContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitTuple_acces(LANSetParser.Tuple_accesContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitVector_acces(LANSetParser.Vector_accesContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitTernary(LANSetParser.TernaryContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitSubexpr(LANSetParser.SubexprContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitLogic_operators(LANSetParser.Logic_operatorsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitTerm1(LANSetParser.Term1Context ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitEquality_operator(LANSetParser.Equality_operatorContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitTerm2(LANSetParser.Term2Context ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitAddition_operators(LANSetParser.Addition_operatorsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitTerm3(LANSetParser.Term3Context ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitMultiplication_operators(LANSetParser.Multiplication_operatorsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitTerm4(LANSetParser.Term4Context ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitNegation_operators(LANSetParser.Negation_operatorsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public returnStruct visitTerm5(LANSetParser.Term5Context ctx) {
        return visitChildren(ctx);
    }

    int visitCount = 0;
    @Override
    public returnStruct visit(ParseTree parseTree) {
        visitCount++;
        //System.out.println("Soc un node qualsevol");
        //return visit(parseTree);
        //return null;
        return parseTree.accept(this);
    }

    /*int visitCount = 0;
    @Override
    public returnStruct visit(ParseTree parseTree) {
        visitCount++;
        //System.out.println("Soc un node qualsevol");
        //return visit(parseTree);
        return null;
    }

    @Override
    public returnStruct visitChildren(RuleNode ruleNode) {
        return visitChildren(ruleNode);
    }

    @Override
    public returnStruct visitTerminal(TerminalNode terminalNode) {
        return visitTerminal(terminalNode);
    }

    @Override
    public returnStruct visitErrorNode(ErrorNode errorNode) {
        return visitErrorNode(errorNode);
    }*/

    //Warning: zona radioactiva
    private SymTable<Registre> TS = new SymTable<Registre>(1000);
    private Bytecode program;
    private Long lineBreak;
    private Long nVar = 0L;
    private boolean errorSemantic;
    Vector<Long> code;

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
