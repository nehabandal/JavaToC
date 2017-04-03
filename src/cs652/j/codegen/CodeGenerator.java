package cs652.j.codegen;

import cs652.j.codegen.model.*;
import cs652.j.parser.JBaseVisitor;
import cs652.j.parser.JParser;
import cs652.j.semantics.JField;
import cs652.j.semantics.JMethod;
import org.antlr.v4.runtime.ParserRuleContext;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class CodeGenerator extends JBaseVisitor<OutputModelObject> {
    public STGroup templates;
    public String fileName;


    public CodeGenerator(String fileName) {
        this.fileName = fileName;
        templates = new STGroupFile("cs652/j/templates/C.stg");
    }

    public CFile generate(ParserRuleContext tree) {
        CFile file = (CFile) visit(tree);
        return file;
    }

    @Override
    public OutputModelObject visitFile(JParser.FileContext ctx) {
        CFile cFile = new CFile(fileName);
        for (JParser.ClassDeclarationContext cd : ctx.classDeclaration()) {
            cFile.classes.add((ClassDef) visit(cd));
        }
        cFile.main = (MainMethod) visit(ctx.main());
        return cFile;
    }

    @Override
    public OutputModelObject visitMain(JParser.MainContext ctx) {
        MainMethod mainMethod = new MainMethod();
        mainMethod.body = (Block) visit(ctx.block());
        return mainMethod;
    }

    @Override
    public OutputModelObject visitBlock(JParser.BlockContext ctx) {
        Block block = new Block();
        for (JParser.StatementContext stat : ctx.statement()) {
            OutputModelObject omo = visit(stat);
            if (omo instanceof VarDef) {
                block.locals.add(omo); // include block statemets here //for loop with Jparser.statements
            } else {
                block.instrs.add(omo);
            }
        }
        return block;
    }

    @Override
    public OutputModelObject visitLocalVarStat(JParser.LocalVarStatContext ctx) {
        return visit(ctx.localVariableDeclaration());
    }

    @Override
    public OutputModelObject visitLocalVariableDeclaration(JParser.LocalVariableDeclarationContext ctx) {

        return VarDef.create(new Type(ctx.jType().getText()), ctx.ID().getText());
    }

    @Override
    public OutputModelObject visitAssignStat(JParser.AssignStatContext ctx) {
        AssignStat assignStat = new AssignStat();
        assignStat.left = visit(ctx.expression(0));
        assignStat.right = visit(ctx.expression(1));
        return assignStat;
    }

    @Override
    public OutputModelObject visitPrintStat(JParser.PrintStatContext ctx) {
        PrintStat printStat = new PrintStat(ctx.STRING().getText());
        for (JParser.ExpressionContext expression : ctx.expressionList().expression()) {
            printStat.args.add(expression.getText());
        }
        return printStat;
    }

    @Override
    public OutputModelObject visitClassDeclaration(JParser.ClassDeclarationContext ctx) {
        ClassDef classDef = (ClassDef) visit(ctx.classBody());
        classDef.name = ctx.name.getText();

        for (MethodDef m : classDef.methods) {
            classDef.addVTable(m);
        }
        return classDef;
    }

    @Override
    public OutputModelObject visitClassBody(JParser.ClassBodyContext ctx) {
        ClassDef classDef = new ClassDef();
        for (JParser.ClassBodyDeclarationContext classBodyDeclarationContext : ctx.classBodyDeclaration()) {
            OutputModelObject omo = visit(classBodyDeclarationContext);
            if (omo instanceof MethodDef) {
                classDef.methods.add((MethodDef) omo);
            } else {

            }
        }
        return classDef;
    }

    @Override
    public OutputModelObject visitClassBodyDeclaration(JParser.ClassBodyDeclarationContext ctx) {
        JParser.MethodDeclarationContext methodDeclarationContext = ctx.methodDeclaration();
        if (methodDeclarationContext != null) {
            return visit(methodDeclarationContext);
        } else {
            return visit(ctx.fieldDeclaration());
        }
    }

    @Override
    public OutputModelObject visitMethodDeclaration(JParser.MethodDeclarationContext ctx) {
        MethodDef methodDef = new MethodDef();
        methodDef.body = (Block) visit(ctx.methodBody());
        JMethod scope = ctx.scope;
        methodDef.funcName = scope.getName();
        methodDef.className = scope.getEnclosingScope().getName();
        methodDef.returnType = scope.getType().getName();

        List<JField> fields = (List) scope.getSymbols();
        for (JField field : fields) {
            String type = field.getType().getName();
            methodDef.args.add(VarDef.createParameter(new Type(type), field.getName()));
        }

        return methodDef;
    }

    @Override
    public OutputModelObject visitMethodBody(JParser.MethodBodyContext ctx) {
        return visit(ctx.block());
    }

    @Override
    public OutputModelObject visitFieldDeclaration(JParser.FieldDeclarationContext ctx) {
        return super.visitFieldDeclaration(ctx);
    }

    @Override
    public OutputModelObject visitPrintStringStat(JParser.PrintStringStatContext ctx) {
        return new PrintStringStat(ctx.STRING().getText());
    }

    @Override
    public OutputModelObject visitCtorCall(JParser.CtorCallContext ctx) {
        return new CtorCall(ctx.ID().getText());
    }

    @Override
    public OutputModelObject visitLiteralRef(JParser.LiteralRefContext ctx) {
        return new LiteralRef(ctx.getText());
    }

    @Override
    public OutputModelObject visitIdRef(JParser.IdRefContext ctx) {
        return new VarRef(ctx.getText());
    }

    @Override
    public OutputModelObject visitMethodCall(JParser.MethodCallContext ctx) {
        return new MethodCall();
    }

    @Override
    public OutputModelObject visitQMethodCall(JParser.QMethodCallContext ctx) {
        MethodCall methodCall = new MethodCall();
        JParser.ExpressionContext expressionContext = ctx.expression();

        Expr lhs = new TypeCast(new Type(expressionContext.type.getName()), (Expr) visit(expressionContext));
        methodCall.args.add(lhs);

        for (JParser.ExpressionContext arg : ctx.expressionList().expression()) {
            methodCall.args.add((Expr) visit(arg));
        }
        return methodCall;
    }

    @Override
    public OutputModelObject visitCallStat(JParser.CallStatContext ctx) {
        return new CallStat((Expr) visit(ctx.expression()));
    }

    @Override
    public OutputModelObject visitIfStat(JParser.IfStatContext ctx) {
        List<JParser.StatementContext> statements = ctx.statement();
        IfStat ifStat = statements.size() > 1 ? new IfElseStat() : new IfStat();
        ifStat.condition = (Expr) visit(ctx.parExpression());
        ifStat.stat = (Stat) visit(statements.get(0));
        if (statements.size() > 1) {
            ((IfElseStat) ifStat).elseStat = (Stat) visit(statements.get(1));
        }
        return ifStat;
    }

    @Override
    public OutputModelObject visitParExpression(JParser.ParExpressionContext ctx) {
        return visit(ctx.expression());
    }
}