package cs652.j.codegen;

import cs652.j.codegen.model.Block;
import cs652.j.codegen.model.CFile;
import cs652.j.codegen.model.MainMethod;
import cs652.j.codegen.model.OutputModelObject;
import cs652.j.parser.JBaseVisitor;
import cs652.j.parser.JParser;
import cs652.j.semantics.JClass;
import org.antlr.symtab.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerator extends JBaseVisitor<OutputModelObject> {
	public STGroup templates;
	public String fileName;

	public Scope currentScope;
	public JClass currentClass;

	public CodeGenerator(String fileName) {
		this.fileName = fileName;
		templates = new STGroupFile("cs652/j/templates/C.stg");
	}

	public CFile generate(ParserRuleContext tree) {
		CFile file = (CFile)visit(tree);
		return file;
	}

	@Override
	public OutputModelObject visitFile(JParser.FileContext ctx) {
		return new CFile(fileName);
	}

	@Override
	public OutputModelObject visitMain(JParser.MainContext ctx) {
		MainMethod mainMethod=new MainMethod();
		return mainMethod;
	}

	@Override
	public OutputModelObject visitBlock(JParser.BlockContext ctx) {
		Block block=new Block();
		// include block statemets here //for loop with Jparser.statements

		return block;
	}

	@Override
	public OutputModelObject visitLocalVarStat(JParser.LocalVarStatContext ctx) {
		return super.visitLocalVarStat(ctx);
	}

	@Override
	public OutputModelObject visitLocalVariableDeclaration(JParser.LocalVariableDeclarationContext ctx) {
		return super.visitLocalVariableDeclaration(ctx);
	}
}