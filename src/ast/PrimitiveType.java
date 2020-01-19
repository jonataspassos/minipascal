package ast;

public class PrimitiveType extends Type {

	public static final char tInt = 'i';
	public static final char tReal = 'r';
	public static final char tBoolean = 'b';
	public static final char tNone = '\0';

	private char type;

	public PrimitiveType(int line, int column) {
		super(line, column);
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
	}

	//Context
	
	@Override
	public boolean equals(Type t) {
		if(t instanceof PrimitiveType)
			return this.getType() == ((PrimitiveType)t).getType();
		else return false;
	};
}
