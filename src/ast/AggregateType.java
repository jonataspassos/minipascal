package ast;

public class AggregateType extends Type {

	private int[] index = new int[2];
	private Type type;

	public AggregateType(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
	}

	public int[] getIndex() {
		return index;
	}

	public AggregateType setIndex(int index, int i) {
		this.index[i] = index;
		return this;
	}

	public int getIndex(int i) {
		return this.index[i];
	}

	public Type getType() {
		return type;
	}

	public AggregateType setType(Type type) {
		this.type = type;
		return this;
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitAggregateType(this);
	}
	
	
}
