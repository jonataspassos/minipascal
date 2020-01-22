package parser;

import compilador.CompilerException;
import scanner.Token;

public class ParserException extends CompilerException{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	private Token invalid;
	private String expects;
	
	public ParserException(Token t, String e) {
		super(1, t.line, t.column);
		this.invalid = t;
		this.expects = e;		
	}
	public ParserException() {	
		super(1,0,0);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + "Parser Exception\n\tThis token wasn't expected: ' "+this.invalid.spelling+" '\n"
				+ "\tWas expected: ' "+expects+" '\n";
	}

}
