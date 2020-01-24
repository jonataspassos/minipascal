package parser;

import java.util.ArrayList;

public class MultiParserException extends ParserException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<ParserException> pes = new ArrayList<ParserException>();

	public void add(ParserException pe) {
		pes.add(pe);
	}
	
	@Override
	public String getMessage() {
		String ret = "";
		for (ParserException i : this.pes) {
			ret += i.getMessage();
		}
		ret+=""+this.pes.size()+" Errors!";
		return ret;
	}
	
	public void check() throws MultiParserException {
		if(this.pes.size()>0)
			throw this;
	}

	
}
