package ast;

public class OpMul extends Op {
	// * | / | and
	public static final char tProduct = '*';
	public static final char tDivision = '/';
	public static final char tAnd = 'a';

	public OpMul(int line, int column) {
		super(line, column);
	}

	public OpMul setOp(char op) {
		super.setOp(op);
		return this;
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitOpMul(this);
	};

}
