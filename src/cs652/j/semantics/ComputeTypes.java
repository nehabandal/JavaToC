package cs652.j.semantics;

import cs652.j.parser.JBaseListener;
import cs652.j.parser.JParser;
import org.antlr.symtab.*;

public class ComputeTypes extends JBaseListener {
	protected StringBuilder buf = new StringBuilder();
	private Scope currentScope;

	public static final Type JINT_TYPE = new JPrimitiveType("int");
	public static final Type JFLOAT_TYPE = new JPrimitiveType("float");
	public static final Type JSTRING_TYPE = new JPrimitiveType("string");
	public static final Type JVOID_TYPE = new JPrimitiveType("void");

	public ComputeTypes(GlobalScope globals) {
		this.currentScope = globals;
	}

	private void pop() {
		currentScope = currentScope.getEnclosingScope();
	}

	@Override
	public void enterMain(JParser.MainContext ctx) {
		currentScope = ctx.scope;
	}

	@Override
	public void exitMain(JParser.MainContext ctx) {
		pop();
	}

	@Override
	public void enterBlock(JParser.BlockContext ctx) {
		currentScope = ctx.scope;
	}

	@Override
	public void exitBlock(JParser.BlockContext ctx) {
		pop();
	}

	@Override
	public void enterLiteralRef(JParser.LiteralRefContext ctx) {
		if (ctx.INT() != null) {
			ctx.type = JINT_TYPE;
		} else {
			ctx.type = JFLOAT_TYPE;
		}
		printLine(ctx.getText(), ctx.type.getName());
	}

	@Override
	public void enterIdRef(JParser.IdRefContext ctx) {
		TypedSymbol var = (TypedSymbol) currentScope.resolve(ctx.ID().getText());
		ctx.type = var.getType();
	}

	@Override
	public void exitIdRef(JParser.IdRefContext ctx) {
		printLine(ctx.getText(), ctx.type.getName());
	}

	@Override
	public void exitCtorCall(JParser.CtorCallContext ctx) {
		Symbol var = currentScope.resolve(ctx.ID().getText());
		printLine(ctx.getText(), var.getName());
	}

	@Override
	public void exitQMethodCall(JParser.QMethodCallContext ctx) {
		JClass clazz = (JClass) ctx.expression().type;
		JMethod method = (JMethod) clazz.resolveMember(ctx.ID().getText());
		ctx.type = method.getType();
		printLine(ctx.getText(), method.getType().getName());
	}

	@Override
	public void exitMethodCall(JParser.MethodCallContext ctx) {
		JMethod method = (JMethod) currentScope.resolve(ctx.ID().getText());
		ctx.type = method.getType();
		printLine(ctx.getText(), method.getType().getName());
	}

	@Override
	public void exitFieldRef(JParser.FieldRefContext ctx) {
		JClass clazz = (JClass) ctx.expression().type;
		JField field = (JField) clazz.resolveMember(ctx.ID().getText());
		ctx.type = field.getType();
		printLine(ctx.getText(), field.getType().getName());
	}

	@Override
	public void exitThisRef(JParser.ThisRefContext ctx) {
		JField thisField = (JField) currentScope.resolve("this");
		ctx.type = thisField.getType();
		printLine("this", thisField.getType().getName());
	}

// S U P P O R T

	public void printLine(String name, String type) {
		buf
				.append(name)
				.append(" is ")
				.append(type)
				.append("\n");
	}
	public String getRefOutput() {
		return buf.toString();
	}
}
