package scanner;

import java.util.ArrayList;

import utils.Buffer;
import utils.MyString;


public class PatternToken {

	public final static int invalidToken = -3;
	public final static String combined = "combined";

	private TokenType[] list;
	private CombinedTokens[] listCombined;

	/**
	 * Constroi o Reconhecedor de tokens reservados
	 * 
	 * @param Buffer contendo a lista de tokens v�lidos com seus respectivos tipos e
	 *               c�digos
	 */
	public PatternToken(Buffer buffer) {

		String temp;
		/**
		 * Para mais informa��es sobre o que significa cada vari�vel, consulte a
		 * documenta��o da classe TokenType
		 */
		// Guardar� os tipos de tokens (Alfanum�ricos, s�mbolos, n�meros)
		ArrayList<TokenType> tokenType = new ArrayList<TokenType>();

		// Guardar� a lista de tokens do tipo que est� sendo avaliado
		ArrayList<String> listCurrentAtribute = new ArrayList<String>();

		// Gardar� o nome do tipo de token que est� sendo avaliado
		String currentAtribute = "";

		// Guardar� o c�digo inicial do tipo
		byte nCurrentAtribute;

		// Guardar� o tipo do token a-alfanumerico s-simbolo n-numero
		char typeCurrentAtribute;

		// Informa se aquele tipo � um tipo extens�vel
		boolean anyCurrentAtribute;

		// cada itera��o significa um tipo de token para a linguagem
		while (!buffer.isEmpty()) {

			// Consome no buffer o nome do tipo
			currentAtribute = buffer.splitFirst();

			if (MyString.equals(currentAtribute, combined)) {// Salta para o pr�ximo tipo de recinhecimento
				break;
			}

			// consome o c�digo do tipo
			nCurrentAtribute = MyString.toByte(buffer.splitFirst());

			// consome o tipo do tipo :/
			typeCurrentAtribute = buffer.splitFirst().charAt(0);

			if (MyString.equals(buffer.splitFirst(), "{")) {

				// Consome um elemento da lista
				temp = buffer.splitFirst();

				listCurrentAtribute = new ArrayList<String>();

				// Enquanto n�o for o final da lista
				while (!MyString.equals(temp, "}")) {

					// Consome uma um os elementos da lista
					listCurrentAtribute.add(temp);
					temp = buffer.splitFirst();

				}
			}
			// Identifica se aquele tipo � extens�vel ou n�o
			anyCurrentAtribute = buffer.splitFirst().charAt(0) == '+';

			// Cria o tipo de token com as informa��es adquiridas
			tokenType.add(new TokenType(nCurrentAtribute, currentAtribute, typeCurrentAtribute, anyCurrentAtribute,
					MyString.convert(listCurrentAtribute.toArray())));

			buffer.consumeSeparators();
		}

		// Converte os tipos de token para um array e atribui ao atributo local
		this.list = TokenType.convert(tokenType.toArray());

		// Inicia verifica��o de tokens combinados
		/**
		 * Tokens combinados s�o aqueles que existem ou n�o sozinhos mas representam
		 * outro token quando juntos
		 */
		if (MyString.equals(currentAtribute, combined)) {
			// consome o tipo do tipo combinado :/
			typeCurrentAtribute = buffer.splitFirst().charAt(0);

			if (MyString.equals(buffer.splitFirst(), "{")) {
				ArrayList<CombinedTokens> combinedRules = new ArrayList<CombinedTokens>();
				ArrayList<String> tokensToRule;
				do {
					temp = buffer.splitFirst();

					if (MyString.equals(temp, "}"))
						break;
					
					tokensToRule = new ArrayList<String>();

					while (!MyString.equals(temp, "$")) {
						tokensToRule.add(temp);
						temp = buffer.splitFirst();
					}
					nCurrentAtribute = MyString.toByte(buffer.splitFirst());

					combinedRules.add(new CombinedTokens(nCurrentAtribute, MyString.convert(tokensToRule.toArray()), this));
					

				} while (true);
				
				this.listCombined = CombinedTokens.convert(combinedRules.toArray());

			}

		}
	}

	/**
	 * @return o numero de tipos daquela linguagem
	 */
	public int getNTypes() {
		return this.list.length;
	}

	/**
	 * @param indice do tipo de token
	 * @return O tipo de token solicitado
	 */
	public TokenType getType(int i) {
		if (i < list.length && i >= 0) {
			return list[i];
		} else {
			return null;// TODO tratamento de erro
		}
	}

	/**
	 * @param atributo do TokenType
	 * @return O tipo de token solicitado
	 */
	public TokenType getType(char type) {
		for (TokenType i : list) {
			if (i.type == type)
				return i;
		}
		return null;// TODO tratamento de erro

	}

	public CombinedTokens[] getCombinedRules() {
		return this.listCombined;
	}
	
//	public static void main(String []args) {
//		PatternToken patternToken = new PatternToken(new FileBuffer("H:\\faculdade\\com backup\\00Material da Faculdade\\7semestre\\Compiladores\\grammar-tokens - Copy.tkn"));
//		patternToken.listCombined[0].getToken();
//		patternToken.listCombined[1].getToken();
//		patternToken.listCombined[2].getToken();
//	}
}
