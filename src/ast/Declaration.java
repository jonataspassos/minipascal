package ast;

import java.util.ArrayList;

public class Declaration extends AST {
	private ArrayList<String> id = new ArrayList<String>();
	
	private Type type;
	private int address;

	public Declaration(int line, int column) {
		super(line, column);
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
	
	//Code
	public void setAddress(int address) {
		this.address = address;
	}
	public int getAddress(String id) {
		return this.id.indexOf(id)*this.type.size()+this.address;
	}
}
