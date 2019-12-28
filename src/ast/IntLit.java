package ast;

public class IntLit extends Lit {

	private int value;

	public IntLit(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
	}

	public int getValue() {
		return value;
	}

	public IntLit setValue(int value) {
		this.value = value;
		return this;
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub

	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitIntLit(this);
	};
}
