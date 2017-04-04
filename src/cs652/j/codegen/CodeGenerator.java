package cs652.j.codegen;

import cs652.j.codegen.model.*;
import cs652.j.parser.JBaseVisitor;
import cs652.j.parser.JParser;
import cs652.j.semantics.JClass;
import cs652.j.semantics.JField;
import cs652.j.semantics.JMethod;
import org.antlr.symtab.Scope;
import org.antlr.symtab.Symbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CodeGenerator extends JBaseVisitor<OutputModelObject> {
    public final STGroup templates;
    private final  String fileName;


    public CodeGenerator(String fileName) {
        this.fileName = fileName;
        this.templates = new STGroupFile("cs652/j/templates/C.stg");
    }

    public CFile generate(ParserRuleContext tree) {
        return (CFile) visit(tree);
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
            Stat omo = (Stat) visit(stat);
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

        return VarDef.create(new DataType(ctx.jType().getText()), ctx.ID().getText());
    }

    @Override
    public OutputModelObject visitAssignStat(JParser.AssignStatContext ctx) {
        AssignStat assignStat = new AssignStat();
        assignStat.left = (Expr) visit(ctx.expression(0));
        assignStat.right = (Expr) visit(ctx.expression(1));
        if (!assignStat.left.type.equals(assignStat.right.type)) {
            assignStat.right = new TypeCast(assignStat.left.type, assignStat.right);
        }
        return assignStat;
    }

    @Override
    public OutputModelObject visitPrintStat(JParser.PrintStatContext ctx) {
        PrintStat printStat = new PrintStat(ctx.STRING().getText());
        for (JParser.ExpressionContext expression : ctx.expressionList().expression()) {
            printStat.args.add((Expr) visit(expression));
        }
        return printStat;
    }

    @Override
    public OutputModelObject visitClassDeclaration(JParser.ClassDeclarationContext ctx) {
        ClassDef classDef = (ClassDef) visit(ctx.classBody());
        classDef.name = ctx.name.getText();

        classDef.vtable.addAll(getAllMethodNames(ctx.scope));
        return classDef;
    }

    private List<MethodDefVTableInfo> getAllMethodNames(JClass jClass) {
        if (jClass == null) {
            return new ArrayList<>();
        }
        List<MethodDefVTableInfo> vTable = getAllMethodNames((JClass) jClass.resolve(jClass.getSuperClassName()));
        for (JMethod m : (Set<JMethod>) (Set) jClass.getMethods()) {
            MethodDefVTableInfo current = new MethodDefVTableInfo(m.getEnclosingScope().getName(), m.getName());
            boolean add = true;
            for (MethodDefVTableInfo parentMethod : vTable) {
                if (current.funcName.equals(parentMethod.funcName)) {
                    parentMethod.className = current.className;
                    add = false;
                    break;
                }
            }
            if (add) {
                vTable.add(current);
            }
        }
        return vTable;
    }

    @Override
    public OutputModelObject visitClassBody(JParser.ClassBodyContext ctx) {
        ClassDef classDef = new ClassDef();
        for (JParser.ClassBodyDeclarationContext classBodyDeclarationContext : ctx.classBodyDeclaration()) {
            OutputModelObject omo = visit(classBodyDeclarationContext);
            if (omo instanceof MethodDef) {
                classDef.methods.add((MethodDef) omo);
            } else {
                classDef.fields.add((VarDef) visit(classBodyDeclarationContext.fieldDeclaration()));
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
        JMethod scope = ctx.scope;
        MethodDef methodDef = new MethodDef(scope.getEnclosingScope().getName(), scope.getName());
        methodDef.body = (Block) visit(ctx.methodBody());
        methodDef.returnType = scope.getType().getName();

        List<JField> fields = (List) scope.getSymbols();
        for (JField field : fields) {
            String type = field.getType().getName();
            methodDef.args.add(VarDef.createParameter(new DataType(type), field.getName()));
        }

        return methodDef;
    }

    @Override
    public OutputModelObject visitMethodBody(JParser.MethodBodyContext ctx) {
        return visit(ctx.block());
    }

    @Override
    public OutputModelObject visitFieldDeclaration(JParser.FieldDeclarationContext ctx) {
        return VarDef.create(new DataType(ctx.jType().getText()), ctx.ID().getText());
    }

    @Override
    public OutputModelObject visitPrintStringStat(JParser.PrintStringStatContext ctx) {
        return new PrintStringStat(ctx.STRING().getText());
    }

    @Override
    public OutputModelObject visitCtorCall(JParser.CtorCallContext ctx) {
        return new CtorCall(ctx.ID().getText(), null);
    }

    @Override
    public OutputModelObject visitLiteralRef(JParser.LiteralRefContext ctx) {
        return new LiteralRef(ctx.getText(), new DataType(ctx.type.getName()));
    }

    @Override
    public OutputModelObject visitIdRef(JParser.IdRefContext ctx) {
        String symbolName = ctx.getText();
        Scope scope = getBlockContext(ctx).scope;
        JField jField = resolve(scope, symbolName);

        if (jField.getScope() instanceof JClass) {
            return new ThisRef(jField, symbolName);
        } else {
            return new VarRef(jField, symbolName);
        }
    }

    private JField resolve(Scope scope, String symbolName) {
        return (JField) scope.resolve(symbolName);
    }

    private JParser.BlockContext getBlockContext(ParserRuleContext ctx) {
        if (ctx instanceof JParser.BlockContext) {
            return (JParser.BlockContext) ctx;
        }
        return getBlockContext(ctx.getParent());
    }

    @Override
    public OutputModelObject visitMethodCall(JParser.MethodCallContext ctx) {
        return new MethodCall(ctx.ID().getText(), null);
    }

    @Override
    public OutputModelObject visitQMethodCall(JParser.QMethodCallContext ctx) {
        MethodCall methodCall = new MethodCall(ctx.ID().getText(), null);
        JParser.ExpressionContext expressionContext = ctx.expression();

        Expr lhsExpr = (Expr) visit(expressionContext);
        methodCall.receiver = lhsExpr;
        methodCall.receiverType = lhsExpr.type;


        JClass jClass = (JClass) getBlockContext(ctx).scope.resolve(methodCall.receiverType.name);
        JMethod jMethod = (JMethod) jClass.resolveMethod(methodCall.name);

        FuncPtrType funcPtrType = new FuncPtrType();
        funcPtrType.returnType = new DataType(jMethod.getType().getName());
        methodCall.type = funcPtrType.returnType;

        for (JField args : (List<JField>) (List) jMethod.getAllSymbols()) {
            funcPtrType.argTypes.add(new DataType(args.getType().getName()));
        }
        methodCall.fptrType = funcPtrType;

        methodCall.args.add(new TypeCast(lhsExpr));
        if (ctx.expressionList() != null) {
            for (JParser.ExpressionContext arg : ctx.expressionList().expression()) {
                methodCall.args.add((Expr) visit(arg));
            }
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

    @Override
    public OutputModelObject visitWhileStat(JParser.WhileStatContext ctx) {
        return new WhileStat((Expr) visit(ctx.parExpression()), (Stat) visit(ctx.statement()));
    }

    @Override
    public OutputModelObject visitBlockStat(JParser.BlockStatContext ctx) {
        return visit(ctx.block());
    }

    @Override
    public OutputModelObject visitThisRef(JParser.ThisRefContext ctx) {
        return super.visitThisRef(ctx);
    }

    @Override
    public OutputModelObject visitFieldRef(JParser.FieldRefContext ctx) {
        Expr expr = (Expr) visit(ctx.expression());
        JClass jClass = (JClass) getBlockContext(ctx).scope.resolve(expr.type.name);
        JField jField = (JField) jClass.resolve(ctx.ID().getText());
        return new FieldRef(expr, jField);
    }

    @Override
    public OutputModelObject visitReturnStat(JParser.ReturnStatContext ctx) {
        return new ReturnStat((Expr) visit(ctx.expression()));
    }
}