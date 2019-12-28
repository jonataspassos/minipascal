package ast;

public class BoolLit extends Lit {

	private boolean value;

	public BoolLit(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
	}

	public boolean isValue() {
		return value;
	}

	public BoolLit setValue(boolean value) {
		this.value = value;
		return this;
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub

	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitBoolLit(this);
	};
}
