package ast;

public abstract class Factor extends AST {

	public Factor(int line, int column) {
		super(line, column);
	}
	
	// Padrão Visitor
	public abstract void visit (Visitor v);

	// Context
	public abstract Type getType();
	
}
