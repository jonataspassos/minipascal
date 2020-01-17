package ast;

import java.util.ArrayList;

public class Term extends AST {

	private ArrayList<Factor> a = new ArrayList<Factor>();
	private ArrayList<OpMul> op = new ArrayList<OpMul>();

	public Term(int line, int column) {
		super(line, column);
	}

	public ArrayList<Factor> getA() {
		return a;
	}

	public ArrayList<OpMul> getOp() {
		return op;
	}

	public Term setA(Factor a, int index) {
		this.a.set(index, a);
		return this;
	}

	public Term setOp(OpMul op, int index) {
		this.op.set(index, op);
		return this;
	}

	public Factor getA(int index) {
		return a.get(index);
	}

	public OpMul getOp(int index) {
		return op.get(index);
	}

	public Term addA(Factor a) {
		this.a.add(a);
		return this;
	}

	public Term addOp(OpMul op) {
		this.op.add(op);
		return this;
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitTerm(this);
	}

	// Context
	public Type getType() {
		return this.op.size()!=0?this.op.get(this.op.size()-1).getType():a.get(0).getType();
	};

}
