package ast;

public abstract class Lit extends Factor {
	public Lit(int line, int column) {
		super(line, column);
	}

	public abstract void setValue(String value);

	// Padrão Visitor
	public abstract void visit(Visitor v);
}
