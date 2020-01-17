package ast;

public abstract class Lit extends Factor {
	public Lit(int line, int column) {
		super(line, column);
	}

	public abstract void setValue(String value);

	// Padr�o Visitor
	public abstract void visit(Visitor v);
}
