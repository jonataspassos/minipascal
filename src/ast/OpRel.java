package ast;

public class OpRel extends Op {
	// < | > | <= | >= | = | <>
	public static final char tLessT = '<';
	public static final char tGreatT = '>';
	public static final char tLessEqT = '{';
	public static final char tGreatEqT = '}';
	public static final char tEq = '=';
	public static final char tNotEq = '!';

	public OpRel(int line, int column) {
		super(line, column);
	}
	
	public OpRel setOp(char op) {
		super.setOp(op);
		return this;
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitOpRel(this);
	};

}
