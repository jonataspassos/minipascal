package ast;

public class OpBolAd extends Op {
	// +| - | or
	public static final char tOr = 'o';
	

	public OpBolAd(int line, int column) {
		super(line, column);
	}

	public OpBolAd setOp(char op) {
		super.setOp(op);
		return this;
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		//v.visitOpBolAd(this);
	};
}
