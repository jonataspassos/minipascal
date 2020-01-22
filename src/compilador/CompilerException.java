package compilador;

public class CompilerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int errorCode;
	private int line;
	private int column;

	public CompilerException(int errorCode, int line, int column) {
		super();
		this.errorCode = errorCode;
		this.line = line;
		this.column = column;
	}

	@Override
	public String getMessage() {
		return "ERROR ["+(line+1)+" : "+(column+1)+"] ";
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public String toString() {
		return this.getMessage();
	}
	
	@Override
	public void printStackTrace() {
		System.err.println(this.getMessage());
	}
	

}
