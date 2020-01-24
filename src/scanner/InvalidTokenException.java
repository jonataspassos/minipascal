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
	}

	@Override
	public String getMessage() {
		return super.getMessage()+ "This token isn't recognizable in this language\n\t ' "+spelling+" '";
	}
}
