package ast;

public class FloatLit extends Lit {

	private float value;

	public FloatLit(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
	}

	public float getValue() {
		return value;
	}

	public FloatLit setValue(float value) {
		this.value = value;
		return this;
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub

	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitFloatLit(this);
	};
}
