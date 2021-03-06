package ast;

import checker.CheckerException;

public class Expression extends Factor {
	private SimpleExpression[] a = new SimpleExpression[2];
	private OpRel op;

	public Expression(int line, int column) {
		super(line, column);
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

	// Padr�o Visitor
	public void visit(Visitor v) {
		v.visitExpression(this);
	}

	
	//Context
	@Override
	public Type getType() throws CheckerException {
		return this.op!=null?this.op.getType():a[0].getType();
	};
}
