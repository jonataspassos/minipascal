package ast;

import java.util.ArrayList;

public class Variable extends Factor {
	private String id;
	private ArrayList<Expression> indexer = new ArrayList<Expression>();

	public Variable(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
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

}
