package ast;

public abstract class Factor extends AST {

	public Factor(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
	}
	
	// Padr�o Visitor
	public abstract void visit (Visitor v);
}
