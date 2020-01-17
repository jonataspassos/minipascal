package ast;

public class OpBolMul extends Op {
	// * | / | and
	public static final char tAnd = 'a';

	public OpBolMul(int line, int column) {
		super(line, column);
	}

	public OpBolMul setOp(char op) {
		super.setOp(op);
		return this;
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		//v.visitOpBolMul(this);
	};

}
