package ast;

import checker.OpType;

public abstract class Op extends AST {
	
	private char op;
	
	private OpType opType;

	public Op(int line, int column) {
		super(line, column);
	}

	@Override
	public abstract void visit(Visitor v);
	
	public char getOp() {
		return op;
	}
	
	public Op setOp(char op) {
		this.op = op;
		return this;
	}

	//Context
	public OpType getOpType() {
		return opType;
	}

	public void setOpType(OpType type) {
		this.opType = type;
	}
	public Type getType() {
		if(opType!=null)
			return opType.ret;
		return null;
	}

}
