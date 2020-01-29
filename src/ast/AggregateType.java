package ast;

public class AggregateType extends Type {

	private int[] index = new int[2];
	private Type type;

	public AggregateType(int line, int column) {
		super(line, column);
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

	//Context
	@Override
	public boolean equals(Type t) {
		if(t instanceof AggregateType) {
			boolean ret = true;
			ret = ret && (this.index[1]-this.index[0])==(((AggregateType)t).index[1]-((AggregateType)t).index[0]);//Index size
			ret = ret && (this.getType().equals(((AggregateType)t).getType()));//Type of array
			
			return ret;
		}
		return false;
	}

	@Override
	public int size() {
		return (this.getIndex(1)-this.getIndex(0)+1)* this.getType().size();
	}
	
	
}
