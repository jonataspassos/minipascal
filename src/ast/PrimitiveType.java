package ast;

public class PrimitiveType extends Type {

	public static final char tInt = 'i';
	public static final char tReal = 'r';
	public static final char tBoolean = 'b';

	private char type;

	public PrimitiveType(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
	}

	public char getType() {
		return type;
	}

	public PrimitiveType setType(char type) {
		this.type = type;
		return this;
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitPrimitiveType(this);
	};
}
