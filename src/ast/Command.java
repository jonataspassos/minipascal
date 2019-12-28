package ast;

public abstract class Command extends AST {

	public Command(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
	}

	// Padrão Visitor
	public abstract void visit(Visitor v);

}
