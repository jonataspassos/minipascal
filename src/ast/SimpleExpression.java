package ast;

import java.util.ArrayList;

public class SimpleExpression extends AST {

	private ArrayList<Term> a = new ArrayList<Term>();
	private ArrayList<OpAd> op = new ArrayList<OpAd>();

	public SimpleExpression(int line, int column) {
		super(line, column);
	}

	public ArrayList<Term> getA() {
		return a;
	}

	public ArrayList<OpAd> getOp() {
		return op;
	}

	public SimpleExpression setA(Term a, int index) {
		this.a.set(index, a);
		return this;
	}

	public SimpleExpression setOp(OpAd op, int index) {
		this.op.set(index, op);
		return this;
	}

	public Term getA(int index) {
		return a.get(index);
	}

	public OpAd getOp(int index) {
		return op.get(index);
	}

	public SimpleExpression addA(Term a) {
		this.a.add(a);
		return this;
	}

	public SimpleExpression addOp(OpAd op) {
		this.op.add(op);
		return this;
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitSimpleExpression(this);
	}

	// Context
	public Type getType() {
		return this.op.size()!=0?this.op.get(0).getType():a.get(0).getType();
	};
}