package scanner;

import utils.MyString;

/**
 * Classe respons�vel por abarcar as informa��es de um token capturado do c�digo
 * fonte
 * 
 * @see kind - C�digo do token
 * @see spelling - String do token
 * @see line - Linha do token no arquivo
 * @see column - Coluna do token no arquivo
 * 
 *      Estaticamente, a classe token define:
 * @see invaldToken - C�digo para tokens inv�lidos
 * @see endOfFileCode - C�digo para o token do fim do arquivo
 * @see endOfFile - String para o token do fim do arquivo
 * 
 *      <p>
 *      S�o os atributos que poder�o ser acessados para comparar se � inv�lido
 *      ou se � o fim do arquivo em qualquer parte do desenvolvimento do
 *      compilador
 *      </p>
 */
public class Token {

	public static final String endOfFile = "EOF";
	public static final byte endOfFileCode = 100;
//	public static final String invalid = "ERRO";
	public static final byte invalidCode = -1;

	public final byte kind;
	public final String spelling;
	public final int line;
	public final int column;

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
	
	public char first() {
		return spelling.charAt(0);
	}
	
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
	 * */
	public static Token invalid(String spelling, int line, int column) {
		return new Token(invalidCode, spelling, line, column);
	}
	
	public static Token[] convert(Object[] v) {
		Token[] r = new Token[v.length];
		for (int i = 0; i < v.length; i++) {
			if (v[i] instanceof Token) {
				r[i] = (Token) v[i];
			}
		}
		return r;
	}

}