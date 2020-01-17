package ast;

public abstract class Type extends AST {

	public Type(int line, int column) {
		super(line, column);
	}

	// Padrão Visitor
	public abstract void visit(Visitor v);
	
	//Context
	public abstract boolean equals(Type t);

}
