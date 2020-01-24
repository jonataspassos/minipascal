package scanner;

import utils.MyString;

/**
 * Classe respons�vel por abarcar as informa��es de um token capturado do c�digo
 * fonte
 * 
 * <p><strong>kind</strong> - C�digo do token</br>
 * <strong>spelling</strong> - String do token</br>
 * <strong>line</strong> - Linha do token no arquivo</br>
 * <strong>column</strong> - Coluna do token no arquivo</p>
 * 
 *      Estaticamente, a classe token define:
 * <p><strong>invaldToken</strong> - C�digo para tokens inv�lidos</br>
 * <strong>endOfFileCode</strong> - C�digo para o token do fim do arquivo</br>
 * <strong>endOfFile</strong> - String para o token do fim do arquivo</p>
 * 
 *      <p>
 *      S�o os atributos que poder�o ser acessados para comparar se � inv�lido
 *      ou se � o fim do arquivo em qualquer parte do desenvolvimento do
 *      compilador
 *      </p>
 */
public class Token {

	//Atributos de Classe
	/**
	 * String para o token do fim do arquivo
	 * */
	public static final String endOfFile = "EOF";
	/**
	 * C�digo para o token do fim do arquivo
	 * */
	public static final byte endOfFileCode = 100;
	/**
	 * C�digo para tokens inv�lidos
	 * */
	public static final byte invalidCode = -1;

	//Atributos de Objeto
	/**
	 *  Guarda o c�digo do token
	 * */
	public final byte kind;
	/**
	 * Guarda a cadeia de caracteres do token
	 * */
	public final String spelling;
	/**
	 * Guarda a linha onde o token foi encontrado
	 * */
	public final int line;
	/**
	 * Guarda a coluna onde o token foi encontrado
	 * */
	public final int column;

	/**
	 * Constroi o Token com todos os valores necess�rios
	 * Os atributos s�o constantes e n�o poedr�o ser sobrescritos ap�s a instancia��o
	 * */
	public Token(byte kind, String spelling, int line, int column) {
		super();
		this.kind = kind;
		this.spelling = spelling;
		this.line = line;
		this.column = column;
	}

	@Override
	public String toString() {
		return "" + kind + "\t " + spelling + "\t [" + (line + 1) + " : " + (column + 1) + "]";
	}
	
	/**
	 * @return o primeiro caractere do token
	 * */
	public char first() {
		return spelling.charAt(0);
	}
	
	/**
	 * @returns o tipo do primeiro caractere do token
	 * */
	public char type() {
		return MyString.type(first()); 
	}

	/**
	 * Auxilia o retorno do token do fim do arquivo,simplificando chamada na implementa��o do Scanner.
	 * @param line - informa a linha do token
	 * @param informa a coluna do token
	 * @return um objeto Token informando o fim do arquivo
	 * */
	public static Token eof(int line, int column) {
		return new Token(endOfFileCode, endOfFile, line, column);
	}

	/**
	 * Auxilia o retorno do token inv�lido,simplificando chamada na implementa��o do Scanner.
	 * @param spelling - string do token inv�lido
	 * @param line - informa a linha do token
	 * @param informa a coluna do token
	 * @return um objeto Token informando o token inv�lido
	 * @throws ScannerException 
	 * */
	public static Token invalid(String spelling, int line, int column){
		//throw new InvalidTokenException(new Token(invalidCode,spelling,line,column));
		return new Token(invalidCode, spelling, line, column);
	}

}