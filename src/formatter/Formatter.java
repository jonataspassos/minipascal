package formatter;

import ast.AST;
import ast.Program;
import ast.Visitor;
import parser.Parser;
import printer.Printer;

public abstract class Formatter implements Visitor {
	protected String out = "";
	protected int level = 0;

	protected void identation() {
		for (int i = 0; i < level; i++) {
			this.out += "\t";
		}
	}

	protected void identation(boolean increase) {
		this.level += increase ? 1 : -1;
		for (int i = 0; i < level; i++) {
			this.out += "\t";
		}
	}

	public String format(AST ast) {
		this.out = "";
		if(ast==null)
			return "";
		ast.visit(this);
		return this.out;
	}
}
