package scanner;

public class InvalidTokenException extends ScannerException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String spelling;
	
	public InvalidTokenException(Token t) {
		super(0, t);
		this.spelling = t.spelling;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getMessage() {
		return super.getMessage()+ "Este token não faz parte desta linguagem\n\t "+spelling;
	}
}
