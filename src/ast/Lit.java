package ast;

public abstract class Lit extends Factor {
	public Lit(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
	}

	public abstract void setValue(String value);

	// Padrão Visitor
	public abstract void visit(Visitor v);
}
