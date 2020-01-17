package ast;

import java.util.ArrayList;

public class Program extends AST {

	private String id;
	private ArrayList<Declaration> declaration = new ArrayList<Declaration>();
	private MultiCommand mc;

	public Program(int line, int column) {
		super(line, column);
	}

	public String getId() {
		return id;
	}

	public Program setId(String id) {
		this.id = id;
		return this;
	}

	public ArrayList<Declaration> getDeclaration() {
		return declaration;
	}

	public MultiCommand getMc() {
		return mc;
	}

	public Program setMc(MultiCommand mc) {
		this.mc = mc;

		return this;
	}

	public Program setDeclaration(Declaration a, int index) {
		this.declaration.set(index, a);
		return this;
	}

	public Program addDeclaration(Declaration a) {
		this.declaration.add(a);
		return this;
	}

	public Declaration getDeclaration(int index) {
		return declaration.get(index);
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitProgram(this);
	};
}
