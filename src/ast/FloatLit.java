package ast;

import utils.MyString;
import utils.StringBuffer;

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
		StringBuffer b= new StringBuffer(value);
		String temp = "0";
		int inteira = 0;
		int decimal = 0;
		int casas = 0;
		while(b.getTypeChar() == 'n') {
			temp +=b.consumeChar();
		}
		inteira = MyString.toInt(temp);
		b.consumeChar();
		temp = "0";
		while(b.getTypeChar() == 'n') {
			temp +=b.consumeChar();
			casas ++;
		}
		decimal = MyString.toInt(temp);

		setValue((float)(inteira + decimal/Math.pow(10.0, casas)));
		System.out.println((float)(inteira + decimal/Math.pow(10.0, casas)));
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitFloatLit(this);
	};
}
