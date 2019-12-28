package ast;

public class OpRel extends AST {
	// < | > | <= | >= | = | <>
	public static final char tLessT = '<';
	public static final char tGreatT = '>';
	public static final char tLessEqT = '{';
	public static final char tGreatEqT = '}';
	public static final char tEq = '=';
	public static final char tNotEq = '!';

	private char op;

	public OpRel(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
	}

	public char getOp() {
		return op;
	}

	public OpRel setOp(char op) {
		this.op = op;
		return this;
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitOpRel(this);
	};

}
