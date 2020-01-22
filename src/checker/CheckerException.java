package checker;

import ast.AST;
import compilador.CompilerException;

public class CheckerException extends CompilerException{
	
	protected AST ast;
	protected String message;

	public CheckerException(AST ast, String message) {
		super(2, ast.getLine(), ast.getColumn());
		this.ast = ast;
		this.message = message;
	}

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
		
		return super.getMessage()+"Context Exception: \n"+
					"\t--------------------------------\n"+
					"\t"+ast.substring(0, ast.length()<=50?ast.length():50)+"\n"+
					"\t--------------------------------\n"+
					"\t"+this.message+"\n\n\n"
					
					;
	}

}
