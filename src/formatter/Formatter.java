package formatter;

import ast.AST;
import ast.Program;
import ast.Visitor;
import parser.Parser;
import printer.Printer;

public abstract class Formatter implements Visitor {
	protected String out = "";
	protected int level = 0;

	public static void main(String args[]) throws Exception {
		// Lista de tokens válidos com seus respectivos tipos e códigos
		String path = "src\\files\\grammar-tokens.tkn";
		// Representa o código fonte da linguagem a ser compilada
		String src = "src\\files\\sc1.pas";

		// Constroi o sintático consumindo o arquivo de tokens e construindo o buffer
		// com o código fonte
		Parser parser = new Parser(path, src);

		// Analisando sintaticamente o código
		AST program = parser.parse();
		
		Printer p = new Printer();
		((Program)program).getMc().visit(p);
		
		p.fileOut("array.html");
		
		String formated = (new PascalFormater()).format(program);
	}

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
		ast.visit(this);
		return this.out;
	}
}
