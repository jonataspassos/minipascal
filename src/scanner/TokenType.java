package scanner;

/**
 * Classe responsável por armazenar todas as informações necessárias de um tipo
 * de token
 * 
 * @see - Código base
 * @see - Nome
 * @see - tipo
 * @see - qualquer
 * @see - lista de tokens reservados daquele tipo
 * 
 * 
 * @see nStart - Guardará o código inicial do tipo instanciado
 *      <p>
 *      Os outros códigos são calculados na ordem que os tokens daquele tipo
 *      aparecem Basicamente se ele é o primeiro token, ele recebe o código
 *      nCurrentAtribute + 1 Se ele é o segundo token, ele recebe o código
 *      nCurrentAtribute + 2
 *      </p>
 * 
 * @see name - Gardará o nome do tipo instanciado(Ainda desnecessário)
 * 
 * @see type - Guardará o tipo de símbolo (a - alfanumérico; n - Numerico; s -
 *      Símbolo;)
 * 
 * @see any - Se este tipo é extensível
 * 
 *      <p>
 *      A ideia dessa flag é informar se para aquele tipo, outros que não estão
 *      naquela lista tambem são válidos Sendo válidos, receberão o código
 *      inicial nCurrentAtribute Não sendo válidos, o Patern procurará o token
 *      que pode ser válido mesmo quebrando ele em partes sem separadores
 * 
 *      Para um tipo com any verdadeiro, qualquer token é válido. Mas, os que
 *      estão na lista de tokens receberão um código específico e reservado a
 *      ele.
 * 
 *      Para um tipo com any falso: Ex.: '?' não está na lista,'(' está, ')'
 *      está, '<' está, '>' está, '<>' está
 * 
 *      Pattern recebe "?" O léxico responde com o código de token inválido
 * 
 *      Pattern recebe "()". Ele reconhece '(' e ')' separadamente. O sintático
 *      deverá ser capaz de saber o que fazer.
 * 
 *      Pattern recebe "<>". Ele reconhece '<>'.
 * 
 *      Pattern recebe "><". Ele reconhece '>' e '<' separadamente. O sintático
 *      deverá ser capaz de saber o que fazer.
 *      </p>
 * 
 * @see list - Guardará a lista de tokens do tipo instanciado
 *      <p>
 *      Esta lista guarda todos os tokens especiais da linguagem para aquele
 *      tipo. Precisará conter os tokens em ordem crescente na tabela ascii, se
 *      não, o algoritmo não funcionará. Cada um dos tokens especificados nessa
 *      lista possuirá o proprio código, partindo do nStart até a posição do
 *      mesmo no vetor
 *      </p>
 * 
 * 
 */
public class TokenType {
	public final byte nStart;
	public final String name;
	public final char type;
	public final boolean any;
	public final String[] list;

	public TokenType(byte nStart, String name, char type, boolean any, String[] list) {
		super();
		this.nStart = nStart;
		this.name = name;
		this.type = type;
		this.any = any;
		this.list = list;
	}

	/**
	 * Converte um array da classe Object para um array de TokenType
	 * */
	public static TokenType[] convert(Object[] v) {
		TokenType[] r = new TokenType[v.length];
		for (int i = 0; i < v.length; i++) {
			if (v[i] instanceof TokenType) {
				r[i] = (TokenType) v[i];
			}
		}
		return r;
	}

	@Override
	public String toString() {
		return "" + nStart + " " + name + " " + type + (any ? " any" : "");
	}

}