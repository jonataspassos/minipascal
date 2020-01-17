package ast;

public class OpAd extends Op {
	// +| - | or
	public static final char tPlus = '+';
	public static final char tMinus = '-';
	public static final char tOr = 'o';
	

	public OpAd(int line, int column) {
		super(line, column);
	}

	public OpAd setOp(char op) {
		super.setOp(op);
		return this;
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitOpAd(this);
	};
}
