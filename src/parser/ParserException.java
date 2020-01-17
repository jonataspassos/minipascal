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
	
	@Override
	public String getMessage() {
		return super.getMessage() + "Erro de Sintaxe\n Este token não era esperado: "+this.invalid.spelling+"\n"
				+ "Esperava-se: "+expects;
	}

}
