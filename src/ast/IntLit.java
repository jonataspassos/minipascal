package ast;

import utils.MyString;

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
		setValue(MyString.toInt(value));
	}

	// Padr�o Visitor
	public void visit(Visitor v) {
		v.visitIntLit(this);
	};
}
