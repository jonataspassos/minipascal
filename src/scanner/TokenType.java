package scanner;

/**
 * Classe respons�vel por armazenar todas as informa��es necess�rias de um tipo
 * de token
 * 
 * @see - C�digo base
 * @see - Nome
 * @see - tipo
 * @see - qualquer
 * @see - lista de tokens reservados daquele tipo
 * 
 * 
 * @see nStart - Guardar� o c�digo inicial do tipo instanciado
 *      <p>
 *      Os outros c�digos s�o calculados na ordem que os tokens daquele tipo
 *      aparecem Basicamente se ele � o primeiro token, ele recebe o c�digo
 *      nCurrentAtribute + 1 Se ele � o segundo token, ele recebe o c�digo
 *      nCurrentAtribute + 2
 *      </p>
 * 
 * @see name - Gardar� o nome do tipo instanciado(Ainda desnecess�rio)
 * 
 * @see type - Guardar� o tipo de s�mbolo (a - alfanum�rico; n - Numerico; s -
 *      S�mbolo;)
 * 
 * @see any - Se este tipo � extens�vel
 * 
 *      <p>
 *      A ideia dessa flag � informar se para aquele tipo, outros que n�o est�o
 *      naquela lista tambem s�o v�lidos Sendo v�lidos, receber�o o c�digo
 *      inicial nCurrentAtribute N�o sendo v�lidos, o Patern procurar� o token
 *      que pode ser v�lido mesmo quebrando ele em partes sem separadores
 * 
 *      Para um tipo com any verdadeiro, qualquer token � v�lido. Mas, os que
 *      est�o na lista de tokens receber�o um c�digo espec�fico e reservado a
 *      ele.
 * 
 *      Para um tipo com any falso: Ex.: '?' n�o est� na lista,'(' est�, ')'
 *      est�, '<' est�, '>' est�, '<>' est�
 * 
 *      Pattern recebe "?" O l�xico responde com o c�digo de token inv�lido
 * 
 *      Pattern recebe "()". Ele reconhece '(' e ')' separadamente. O sint�tico
 *      dever� ser capaz de saber o que fazer.
 * 
 *      Pattern recebe "<>". Ele reconhece '<>'.
 * 
 *      Pattern recebe "><". Ele reconhece '>' e '<' separadamente. O sint�tico
 *      dever� ser capaz de saber o que fazer.
 *      </p>
 * 
 * @see list - Guardar� a lista de tokens do tipo instanciado
 *      <p>
 *      Esta lista guarda todos os tokens especiais da linguagem para aquele
 *      tipo. Precisar� conter os tokens em ordem crescente na tabela ascii, se
 *      n�o, o algoritmo n�o funcionar�. Cada um dos tokens especificados nessa
 *      lista possuir� o proprio c�digo, partindo do nStart at� a posi��o do
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