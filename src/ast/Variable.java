package ast;

import java.util.ArrayList;

public class Variable extends Factor {
	private String id;
	private ArrayList<Expression> indexer = new ArrayList<Expression>();
	
	private Declaration declaration;

	public Variable(int line, int column) {
		super(line, column);
	}
	
	public String getId() {
		return id;
	}

	public Variable setId(String id) {
		this.id = id;
		return this;
	}

	public ArrayList<Expression> getIndexer() {
		return indexer;
	}

	public Variable setIndexer(Expression indexer, int index) {
		this.indexer.set(index, indexer);
		return this;
	}

	public Expression getIndexer(int index) {
		return this.indexer.get(index);
	}

	public Variable addIndexer(Expression indexer) {
		this.indexer.add(indexer);
		return this;
	}

	// Padr�o Visitor
	public void visit(Visitor v) {
		v.visitVariable(this);
	};

	// Contexto
	public Declaration getDeclaration() {
		return declaration;
	}

	public void setDeclaration(Declaration declaration) {
		this.declaration = declaration;
	}
	
	public Type typeIndexed() {
		if(this.declaration!=null) {
			Type ret = this.declaration.getType();
			for (Object i : this.indexer.toArray()) {
				//this can generate a exception cast
				ret = ((AggregateType)ret).getType();
				
			}
			return ret;
		}
		return null;
	}

	@Override
	public Type getType() {
		return typeIndexed();
	}
	
	//Code
	public int getAddress() {
		return declaration.getAddress(this.id);
	}

	public int size() {
		return getType().size();
	}
}
