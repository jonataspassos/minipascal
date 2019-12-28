package ast;

public class OpMul extends AST {
	// * | / | and
	public static final char tProduct = 'p';
	public static final char tDivision = 'd';
	public static final char tAnd = 'a';

	private char op;

	public OpMul(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
	}

	public char getOp() {
		return op;
	}

	public OpMul setOp(char op) {
		this.op = op;
		return this;
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitOpMul(this);
	};

}
