package ast;

import checker.OpType;
import utils.MyString;

public class IntLit extends Lit {

	private int value;

	public IntLit(int line, int column) {
		super(line, column);
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

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitIntLit(this);
	}

	@Override
	public Type getType() {
		return OpType.integ;
	};
}
