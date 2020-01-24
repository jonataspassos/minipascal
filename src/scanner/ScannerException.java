package scanner;

import compilador.CompilerException;

public class ScannerException extends CompilerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ScannerException(int errorCode, Token t) {
		super(errorCode, t.line, t.column);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + "Scanner Exception\n";
	}

}
