package checker;

import java.util.ArrayList;

import ast.Program;


public class MultiCheckerException extends CheckerException{

	public MultiCheckerException() {
		super(new Program(0, 0),"");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<CheckerException> ces = new ArrayList<CheckerException>();

	
	
	public void add(CheckerException pe) {
		ces.add(pe);
	}
	
	@Override
	public String getMessage() {
		String ret = "";
		for (CheckerException i : this.ces) {
			ret += i.getMessage();
		}
		ret+=""+this.ces.size()+" Errors!";
		return ret;
	}
	
	public void check() throws CheckerException {
		if(this.ces.size()>0)
			throw this;
	}

}
