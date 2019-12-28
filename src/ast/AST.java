package ast;

//Arvore sintática abstrata
//Abstract Sintatic Tree
public abstract class AST {
	private int line;
	private int column;

	public AST(int line, int column) {
		super();
		this.line = line;
		this.column = column;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	// Padrão Visitor
	public abstract void visit(Visitor v);

}
