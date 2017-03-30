package cs652.j.semantics;

import cs652.j.parser.JBaseListener;
import cs652.j.parser.JParser;
import org.antlr.symtab.*;

import static cs652.j.semantics.ComputeTypes.*;

public class DefineScopesAndSymbols extends JBaseListener {
	private Scope currentScope;

	public DefineScopesAndSymbols(GlobalScope globals) {
		currentScope = globals;
	}

	@Override
	public void enterFile(JParser.FileContext ctx) {
		currentScope.define((Symbol) JINT_TYPE);
		currentScope.define((Symbol) JFLOAT_TYPE);
		currentScope.define((Symbol) JSTRING_TYPE);
		currentScope.define((Symbol) JVOID_TYPE);
	}

	@Override
	public void exitFile(JParser.FileContext ctx) {
        /* No need to pop from global scope */
	}

	@Override
	public void enterMain(JParser.MainContext ctx) {
		JMethod main = new JMethod("main", ctx);
		currentScope.define(main);
		currentScope = main;
		ctx.scope = (JMethod) currentScope;
	}

	@Override
	public void exitMain(JParser.MainContext ctx) {
		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void enterClassDeclaration(JParser.ClassDeclarationContext ctx) {
		String classname = ctx.name.getText();
		JClass classSymbol = new JClass(classname, ctx);
		currentScope.define(classSymbol);
		currentScope = classSymbol;

		if (ctx.superClass != null) {
			classSymbol.setSuperClass(ctx.superClass.getText());
		}
		ctx.scope = (JClass) currentScope;
	}

	@Override
	public void exitClassDeclaration(JParser.ClassDeclarationContext ctx) {
		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void enterMethodDeclaration(JParser.MethodDeclarationContext ctx) {
		String functionName = ctx.ID().getText();
		JMethod methodSymbol = new JMethod(functionName, ctx);
		String typeText = ctx.jType() == null ? "void" : ctx.jType().getText();
		Type type = (Type) currentScope.resolve(typeText);
		methodSymbol.setType(type);
		currentScope.define(methodSymbol);
		currentScope = methodSymbol;
		ctx.scope = (JMethod) currentScope;

		JField jField = new JField("this");
		currentScope.define(jField);
		jField.setType((JClass) currentScope.getEnclosingScope());

	}

	@Override
	public void exitMethodDeclaration(JParser.MethodDeclarationContext ctx) {
		currentScope = currentScope.getEnclosingScope();

	}

	@Override
	public void enterBlock(JParser.BlockContext ctx) {
		Scope localScope = new LocalScope(currentScope);
		currentScope.nest(localScope);
		currentScope = localScope;
		ctx.scope = (LocalScope) currentScope;
	}

	@Override
	public void exitBlock(JParser.BlockContext ctx) {
		currentScope = currentScope.getEnclosingScope();
	}


	@Override
	public void enterFormalParameter(JParser.FormalParameterContext ctx) {
		Type type = (Type) currentScope.resolve(ctx.jType().getText());
		VariableSymbol var = new VariableSymbol(ctx.ID().getText());
		var.setType(type);
		var.setScope(currentScope);
		currentScope.define(var);
	}

	@Override
	public void enterLocalVariableDeclaration(JParser.LocalVariableDeclarationContext ctx) {
		Type type = (Type) currentScope.resolve(ctx.jType().getText());
		VariableSymbol var = new VariableSymbol(ctx.ID().getText());
		var.setType(type);
		var.setScope(currentScope);
		currentScope.define(var);
	}

	@Override
	public void enterFieldDeclaration(JParser.FieldDeclarationContext ctx) {
		Type type = (Type) currentScope.resolve(ctx.jType().getText());
		String varName = ctx.ID().getText();
		JField jField = new JField(varName);
		jField.setType(type);
		jField.setScope(currentScope);
		System.out.println(jField.toString());
		currentScope.define(jField);

	}
}