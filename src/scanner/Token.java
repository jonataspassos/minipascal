package scanner;

import utils.MyString;

/**
 * Classe responsável por abarcar as informações de um token capturado do código
 * fonte
 * 
 * <p><strong>kind</strong> - Código do token</br>
 * <strong>spelling</strong> - String do token</br>
 * <strong>line</strong> - Linha do token no arquivo</br>
 * <strong>column</strong> - Coluna do token no arquivo</p>
 * 
 *      Estaticamente, a classe token define:
 * <p><strong>invaldToken</strong> - Código para tokens inválidos</br>
 * <strong>endOfFileCode</strong> - Código para o token do fim do arquivo</br>
 * <strong>endOfFile</strong> - String para o token do fim do arquivo</p>
 * 
 *      <p>
 *      São os atributos que poderão ser acessados para comparar se é inválido
 *      ou se é o fim do arquivo em qualquer parte do desenvolvimento do
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
	 * Código para o token do fim do arquivo
	 * */
	public static final byte endOfFileCode = 100;
	/**
	 * Código para tokens inválidos
	 * */
	public static final byte invalidCode = -1;

	//Atributos de Objeto
	/**
	 *  Guarda o código do token
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
	 * Constroi o Token com todos os valores necessários
	 * Os atributos são constantes e não poedrão ser sobrescritos após a instanciação
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
	 * Auxilia o retorno do token do fim do arquivo,simplificando chamada na implementação do Scanner.
	 * @param line - informa a linha do token
	 * @param informa a coluna do token
	 * @return um objeto Token informando o fim do arquivo
	 * */
	public static Token eof(int line, int column) {
		return new Token(endOfFileCode, endOfFile, line, column);
	}

	/**
	 * Auxilia o retorno do token inválido,simplificando chamada na implementação do Scanner.
	 * @param spelling - string do token inválido
	 * @param line - informa a linha do token
	 * @param informa a coluna do token
	 * @return um objeto Token informando o token inválido
	 * @throws ScannerException 
	 * */
	public static Token invalid(String spelling, int line, int column){
		//throw new InvalidTokenException(new Token(invalidCode,spelling,line,column));
		return new Token(invalidCode, spelling, line, column);
	}

}