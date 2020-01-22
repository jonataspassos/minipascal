package ast;

import checker.OpType;
import utils.MyString;
import utils.StringBuffer;

public class FloatLit extends Lit {

	private float value;

	public FloatLit(int line, int column) {
		super(line, column);
	}

	public float getValue() {
		return value;
	}
	public int getA() {
		float vReal = this.value;
		
		return 0;
	}
	public int getB() {
		if(value>1e+16)
			return 0;
		return 0;
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
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitFloatLit(this);
	}
	
	
	//Context
	@Override
	public Type getType() {
		return OpType.real;
	};
}
