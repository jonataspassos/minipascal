package ast;

import java.util.ArrayList;

public class Declaration extends AST {
	private ArrayList<String> id = new ArrayList<String>();
	private Type type;

	public Declaration(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<String> getId() {
		return id;
	}

	public Declaration setId(String id, int index) {
		this.id.set(index, id);
		return this;
	}

	public Declaration addId(String id) {
		this.id.add(id);
		return this;
	}

	public String getId(int index) {
		return this.id.get(index);
	}

	public Type getType() {
		return type;
	}

	public Declaration setType(Type type) {
		this.type = type;
		return this;
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitDeclaration(this);
	};
}
