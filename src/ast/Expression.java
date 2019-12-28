package ast;

public class Expression extends Factor {
	private SimpleExpression[] a = new SimpleExpression[2];
	private OpRel op;

	public Expression(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
	}

	public SimpleExpression[] getA() {
		return a;
	}

	public Expression setA(SimpleExpression a, int index) {
		this.a[index] = a;
		return this;
	}

	public SimpleExpression getA(int index) {
		return this.a[index];
	}

	public OpRel getOp() {
		return op;
	}

	public void setOp(OpRel op) {
		this.op = op;
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitExpression(this);
	};
}
