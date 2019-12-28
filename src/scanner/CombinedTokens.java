package scanner;

import java.util.ArrayList;

import utils.MyString;
import utils.StringBuffer;

/**
 * Esta classe serve para agrupar tokens que representarão um unico token
 * Basicamente são subconjuntos que seriam tokens válidos sozinhos mas
 * combinados representam outro token. Esta classe é usada tanto na definição
 * das regras como na utilização delas.
 */
public class CombinedTokens {
	private ArrayList<Token> parts = new ArrayList<Token>();
	final byte kind;

	/**
	 * Este construtor adiciona na lista de tokens o token enviado e salva como
	 * código o mesmo do token enviado
	 */
	CombinedTokens(Token start) {
		this.kind = start.kind;
		this.parts.add(start);
	}

	/**
	 * Este construtor recebe a descrição do token combinado no formato de regra, o
	 * código que essa regra deverá conter e a base de onde serão retirados os
	 * padrões de tokens. As regras deverão estar no seguinte formato: <t:kind> t:
	 * tipo do token kind: código do token quando sozinho
	 * 
	 */
	CombinedTokens(byte kind, String[] tokens, PatternToken pt) {
		// Salva o código
		this.kind = kind;
		Token currentToken;

		// Percorre sobre a lista de regras recebidas
		for (String i : tokens) {

			// Transforma string em buffer
			StringBuffer b = new StringBuffer(i);

			// Consome o caractere de representação
			b.consumeChar();// <

			// Consome o tipo e busca o padrão de tokens daquele tipo
			TokenType tt = pt.getType(b.consumeChar());// type

			// Consome o caractere de representação
			b.consumeChar();// :

			// Consome o código, convertendo para inteiro
			String numberS = "";
			while (b.getTypeChar() == 'n') {
				numberS += b.consumeChar();
			}
			byte number = MyString.toByte(numberS);

			// Verifica se o código não representa um extensível
			if (number - tt.nStart > 0) {
				currentToken = new Token(number, tt.list[number - tt.nStart - 1], 0, 0);

			} else {// Se não representa, define um valor de spelling genêrico para guardar na regra

				switch (tt.type) {
				case 'n':
					currentToken = new Token(tt.nStart, "0", 0, 0);
					break;
				default:
					currentToken = new Token(tt.nStart, "a0", 0, 0);
					break;
				}
			}

			// Adiciona o token à regra
			this.parts.add(currentToken);
		}
	}

	// Construtor genêrico, não utilizado no projeto
	CombinedTokens(byte kind, ArrayList<Token> parts) {
		this.kind = kind;
		this.parts = parts;
	}

	/**
	 * Este método agrupa todos os spellings dos tokens internos e retorna em uma
	 * unica String
	 */
	String spelling() {
		String ret = "";
		for (Token i : parts) {
			ret += i.spelling;
		}
		return ret;
	}

	/**
	 * Este método retorna o token agrupado com a linha e a coluna do primeiro token
	 */
	Token getToken() {
		int line = parts.size() != 0 ? parts.get(0).line : 0;
		int column = parts.size() != 0 ? parts.get(0).column : 0;
		return new Token(kind, spelling(), line, column);
	}

	/**
	 * Retorna a lista de tokens
	 */
	public ArrayList<Token> getParts() {
		return parts;
	}

	/**
	 * Retorna o token do indice passado por parâmetro
	 */
	public Token getParts(int i) {
		if (i >= 0 && i < parts.size())
			return parts.get(i);
		else
			return null;
	}

	/**
	 * Retorna o token construido como inválido baseado no spelling total com a
	 * linha e a coluna do primeiro token
	 */
	public Token invalid() {
		int line = parts.size() != 0 ? parts.get(0).line : 0;
		int column = parts.size() != 0 ? parts.get(0).column : 0;
		return new Token(Token.invalidCode, this.spelling(), line, column);
	}

	/**
	 * Retorna um token verificado dentro da regra passada por parametro Se não for
	 * do mesmo tipo, retornará um token inválido Se for do mesmo tipo, retornará um
	 * token com o tipo da regra com a linha e a coluna do primeiro token
	 */
	public Token valid(CombinedTokens verif) {
		int line = parts.size() != 0 ? parts.get(0).line : 0;
		int column = parts.size() != 0 ? parts.get(0).column : 0;
		if (verif.parts.size() != this.parts.size()) {
			return invalid();
		}
		for (int i = 0; i < verif.getParts().size(); i++)
			if (verif.getParts(i).kind != this.getParts(i).kind)
				return invalid();
		return new Token(verif.kind, this.spelling(), line, column);

	}

	/**
	 * Adiciona um token à lista interna
	 */
	public void addToken(Token tk) {
		this.parts.add(tk);
	}

	/**
	 * Converte um array da classe object para um array da classe CombinedTokens
	 */
	public static CombinedTokens[] convert(Object[] v) {
		CombinedTokens[] r = new CombinedTokens[v.length];
		for (int i = 0; i < v.length; i++) {
			if (v[i] instanceof CombinedTokens) {
				r[i] = (CombinedTokens) v[i];
			}
		}
		return r;
	}

}