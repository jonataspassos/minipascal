package ast;

public abstract class Type extends AST {

	public Type(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
	}

	// Padr�o Visitor
	public abstract void visit(Visitor v);

}
