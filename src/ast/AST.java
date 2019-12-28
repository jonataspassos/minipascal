package ast;

//Arvore sint�tica abstrata
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

	// Padr�o Visitor
	public abstract void visit(Visitor v);

}
