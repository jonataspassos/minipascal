package checker;

import java.util.ArrayList;

import ast.Declaration;

public class IdentifierTable {
	private ArrayList<IdentifierTuple> table;

	public IdentifierTable() {
		super();
		this.table = new ArrayList<IdentifierTuple>();
	}
	
	public boolean enter(String id,Declaration dec){
		if(this.retrieve(id)==null) {
			this.table.add(new IdentifierTuple(id, dec));
			return false;
		}	
		return true;
		//TODO estará declarando duas variáveis com o mesmo nome
	}
	public Declaration retrieve(String id) {
		for(Object i : this.table.toArray()) {
			if(((IdentifierTuple)i).getId().equals(id)) {
				((IdentifierTuple)i).setRetrived();
				return ((IdentifierTuple)i).getDec(); 
			}
		}
		return null;
	}
	
	
}

class IdentifierTuple{
	final String id;
	final Declaration dec;
	private boolean retrived;
	
	public IdentifierTuple(String id, Declaration dec) {
		super();
		this.id = id;
		this.dec = dec;
		this.retrived = false;
	}

	public boolean isRetrived() {
		return retrived;
	}

	public void setRetrived() {
		this.retrived = true;
	}

	public String getId() {
		return id;
	}

	public Declaration getDec() {
		return dec;
	}

}