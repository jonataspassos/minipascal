package ast;

import java.util.ArrayList;

import checker.CheckerException;

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

	// Padrão Visitor
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
	
	public Type typeIndexed() throws CheckerException {
		if(this.declaration!=null) {
			Type ret = this.declaration.getType();
			for (int i = 0;i<this.indexer.size();i++) {
				try {
				ret = ((AggregateType)ret).getType();
				}catch (ClassCastException e) {
					throw new CheckerException(ret, "The variable ' "+ret+" ' is not a aggregate Type and can not be indexed");
				}
				
			}
			return ret;
		}
		return null;
	}

	@Override
	public Type getType() throws CheckerException {
		return typeIndexed();
	}
	
	//Code
	public int getAddress() {
		return declaration.getAddress(this.id);
	}

	public int size() throws CheckerException {
		return getType().size();
	}
}
