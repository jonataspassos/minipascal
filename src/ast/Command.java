package ast;

public abstract class Command extends AST {

	public Command(int line, int column) {
		super(line, column);
	}

	// Padr�o Visitor
	public abstract void visit(Visitor v);

}
