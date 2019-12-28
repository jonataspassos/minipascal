package ast;

public class OpAd extends AST {
	// +| - | or
	public static final char tPlus = 'm';
	public static final char tMinus = 'n';
	public static final char tOr = 'o';
	private char op;

	public OpAd(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
	}

	public char getOp() {
		return op;
	}

	public OpAd setOp(char op) {
		this.op = op;
		return this;
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitOpAd(this);
	};
}
