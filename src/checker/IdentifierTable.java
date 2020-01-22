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
		//estará declarando duas variáveis com o mesmo nome
	}
	public Declaration retrieve(String id) {
		for(IdentifierTuple i : this.table) {
			if(i.getId().equals(id)) {
				i.setRetrived();
				return i.getDec(); 
			}
		}
		return null;
		//estará consultando variavel não declarada
	}
	
	public void check() throws CheckerException {
		MultiCheckerException mce = new MultiCheckerException();
		for(IdentifierTuple i : this.table) {
			if(!i.isRetrived())
				mce.add(new CheckerException(i.getDec(), "The id ' "+i.id+" ' was declared but was not used") {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public String getMessage() {
						String ast = (""+this.ast);
						if(ast.charAt(ast.length()-1)=='\n')
							ast = ast.substring(0, ast.length()-1);
						
						ast = ast.replaceAll("\n", "\n\t");
						return "WARNING ["+this.getLine()+" : "+this.getColumn()+"] Context Warning:\n"+
								"\t--------------------------------\n"+
								"\t"+ast.substring(0, ast.length()<=50?ast.length():50)+"\n"+
								"\t--------------------------------\n"+
								"\t"+this.message+"\n\n";
					}
				});
		}
		mce.check();
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