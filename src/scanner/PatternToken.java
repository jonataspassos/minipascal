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
	 * @param Buffer contendo a lista de tokens válidos com seus respectivos tipos e
	 *               códigos
	 */
	public PatternToken(Buffer buffer) {

		String temp;
		/**
		 * Para mais informações sobre o que significa cada variável, consulte a
		 * documentação da classe TokenType
		 */
		// Guardará os tipos de tokens (Alfanuméricos, símbolos, números)
		ArrayList<TokenType> tokenType = new ArrayList<TokenType>();

		// Guardará a lista de tokens do tipo que está sendo avaliado
		ArrayList<String> listCurrentAtribute = new ArrayList<String>();

		// Gardará o nome do tipo de token que está sendo avaliado
		String currentAtribute = "";

		// Guardará o código inicial do tipo
		byte nCurrentAtribute;

		// Guardará o tipo do token a-alfanumerico s-simbolo n-numero
		char typeCurrentAtribute;

		// Informa se aquele tipo é um tipo extensível
		boolean anyCurrentAtribute;

		// cada iteração significa um tipo de token para a linguagem
		while (!buffer.isEmpty()) {

			// Consome no buffer o nome do tipo
			currentAtribute = buffer.splitFirst();

			if (MyString.equals(currentAtribute, combined)) {// Salta para o próximo tipo de recinhecimento
				break;
			}

			// consome o código do tipo
			nCurrentAtribute = MyString.toByte(buffer.splitFirst());

			// consome o tipo do tipo :/
			typeCurrentAtribute = buffer.splitFirst().charAt(0);

			if (MyString.equals(buffer.splitFirst(), "{")) {

				// Consome um elemento da lista
				temp = buffer.splitFirst();

				listCurrentAtribute = new ArrayList<String>();

				// Enquanto não for o final da lista
				while (!MyString.equals(temp, "}")) {

					// Consome uma um os elementos da lista
					listCurrentAtribute.add(temp);
					temp = buffer.splitFirst();

				}
			}
			// Identifica se aquele tipo é extensível ou não
			anyCurrentAtribute = buffer.splitFirst().charAt(0) == '+';

			// Cria o tipo de token com as informações adquiridas
			tokenType.add(new TokenType(nCurrentAtribute, currentAtribute, typeCurrentAtribute, anyCurrentAtribute,
					MyString.convert(listCurrentAtribute.toArray())));

			buffer.consumeSeparators();
		}

		// Converte os tipos de token para um array e atribui ao atributo local
		this.list = TokenType.convert(tokenType.toArray());

		// Inicia verificação de tokens combinados
		/**
		 * Tokens combinados são aqueles que existem ou não sozinhos mas representam
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
	 * @param atributo do TokenType
	 * @return O tipo de token solicitado
	 */
	public TokenType getType(char type) {
		for (TokenType i : list) {
			if (i.type == type)
				return i;
		}
		return null;//tipo não existe

	}

	public CombinedTokens[] getCombinedRules() {
		return this.listCombined;
	}
	
	/**
	 * Serve para encontrar uma especificação ou exemplo do tipo de token desejado(caso seja extensível) 
	 * através do código do mesmo
	 * @param kind - Código do token desejado
	 * @return String do token
	 * */
	public String getSpelling(int kind) {
		for(TokenType i : this.list) {
			if(i.nStart == kind && i.any) {
				if(i.type == 'n') {
					return "0";
				}else if(i.type == 'a') {
					return "identifier";
				}
			}else if(i.nStart<kind && i.nStart+i.list.length >=kind) {
				return i.list[kind-i.nStart-1];
			}
		}
		for(CombinedTokens i : this.listCombined) {
			if(i.kind == kind) {
				return i.getToken().spelling;
			}
		}
		return "";
	}
}
