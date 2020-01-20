package scanner;

import utils.Buffer;
import utils.FileBuffer;

/**
 * Objeto preparado para devolver um token sempre que solicitado
 * <p>
 * Basicamente, esta classe abstrai o buffer normal, devolvendo n�o mais
 * caracteres mas os tokens j� tratados , logo, os caracteres est�o para o
 * Buffer como os tokens est�o para o Scanner.
 * </p>
 * <p>
 * A ideia aqui � especificar no momento da instancia, o arquivo que cont�m
 * todas as especifica��es de token da linguagem (Exemplo no arquivo *.tkn) e o
 * arquivo com o c�digo fonte a ser avaliado.
 * </p>
 * <p>
 * Ele reconhecer� todos os tokens da linguagem, e construir� o PatternToken do
 * scanner. Guardar� o c�digo fonte e esperar� ser solicitado. Como sa�da,
 * oferecer� um objeto da classe Token para as proximas etapas do compilador
 * </p>
 */
public class Scanner {
	private PatternToken pt;
	private Buffer sourceCode;
	
	/**
	 * Envie como parametro de constru��o os buffers j� instanciados de:
	 * 
	 * @param pt         - paterrn token (de onde ser� construido o PatternToken)
	 * @param sourceCode - Que ser� consumido sempre que solicitado um novo token
	 */
	public Scanner(Buffer pt, Buffer sourceCode) {
		super();
		this.pt = new PatternToken(pt);
		this.sourceCode = sourceCode;
	}

	/**
	 * Envie como parametro de constru��o os caminhos dos arquivos para criar os
	 * buffers de:
	 * 
	 * @param pathPattern    - paterrn token (de onde ser� construido o
	 *                       PatternToken)
	 * @param pathSourceCode - Que ser� consumido sempre que solicitado um novo
	 *                       token
	 */
	public Scanner(String pathPattern, String pathSourceCode) {
		super();
		this.pt = new PatternToken(new FileBuffer(pathPattern));
		this.sourceCode = new FileBuffer(pathSourceCode);
	}

	/**
	 * @return verdadeiro se o buffer de sourceCode estiver vazio
	 */
	public boolean isEmpty() {
		return this.sourceCode.isEmpty();
	}

	/**
	 * Este m�todo busca no buffer de SourceCode um token e o retorna com todas as
	 * respectivas informa��es
	 * 
	 * @return um objeto Token preenchido de acordo com as especifica��es do
	 *         PatternToken
	 * @throws ScannerException 
	 */
	public Token scan(){
		// Consome os separadores e atualiza as linhas e colunas do buffer
		sourceCode.consumeSeparators();

		// Se n�o tiver mais nada no buffer, retorna Fim do Arquivo
		if (isEmpty()) {
			return Token.eof(this.sourceCode.getLine(), this.sourceCode.getColumn());
		}

		// Procura um TokenType com o tipo de caractere do mesmo tipo do caractere
		// corrente no buffer
		TokenType tt = pt.getType(sourceCode.getTypeChar());

		// Se existir um token type,
		if (tt != null) {
			// chama a fun��o de consumir token com o tokenType encontrado
			Token akc = splitFirstTokenType(tt);
			CombinedTokens cbt = new CombinedTokens(akc);

			// Verifica disponibilidade de regra combinada com o token encontrado

			CombinedTokens[] rules = pt.getCombinedRules();
			int s = 0, // Regra a partir da qual ainda � reconhec�vel
					e = rules == null ? 0 : rules.length, // Regra a partir da qual n�o � mais reconhecivel
					i = 0, // Token da regra que se est� olhando
					safe = -1;// Guarda o indice da ultima regra v�lida
			while (s < e) {// n�o h� mais regras para o token atual ou pr�ximo caractere
				int j;
				for (j = s; j < e; j++) {// Leva o cursos de inicio para a primeira regra reconhecivel

					// Se Token corrente encaixa na regra
					if (rules[j].getParts().size() > i && rules[j].getParts(i).kind == akc.kind &&
					// e N�o existe um proximo token ou
							(rules[j].getParts().size() <= (i + 1) ||
							// Existe um proximo token e s�o de mesmo tipo e
									(rules[j].getParts(i + 1).type() == sourceCode.getTypeChar() &&
									// token � do tipo extens�vel ou
											(rules[j].getParts(i + 1).kind == pt
													.getType(sourceCode.getTypeChar()).nStart ||
											// n�o � extensivel mas os caracteres s�o iguais
													rules[j].getParts(i + 1).first() == sourceCode.getChar())))) {

						s = j;// Avan�a o cursor
						safe = j;// Salva o cursor
						break;
					}
					s = j + 1;// Salta a regra que n�o encaixa
				}
				for (j = s + 1; j < e; j++) {// Leva o cursor de final para a primeira regra n�o reconhecivel
					// Token Corrente n�o encaixa na regra ou
					if (rules[j].getParts().size() <= i || rules[j].getParts(i).kind != akc.kind
					// existe um proximo token e
							|| rules[j].getParts().size() > (i + 1) && (
							// n�o � do mesmo tipo do token da regra ou
							rules[j].getParts(i + 1).type() != sourceCode.getTypeChar() ||
							// � do mesmo tipo mas n�o � do tipo extens�vel e
									rules[j].getParts(i + 1).kind != pt.getType(sourceCode.getTypeChar()).nStart &&
									// e o primeiro caractere � diferente
											rules[j].getParts(i + 1).first() != sourceCode.getChar())) {
						e = j;// Localizou o primeiro token n�o reconhecivel
					}
				}
				i++;

				// Se h� mais de uma regra a ser reconhecida ou
				if ((s + 1) < e
						// s� h� uma, mas ainda n�o consumiu o token que faz parte dela
						|| (s == safe && rules.length > s && cbt.getParts().size() < rules[s].getParts().size())) {
					akc = splitFirstTokenType(pt.getType(sourceCode.getTypeChar()));
					/**
					 * Um pr�ximo token � consumido mas n�o � consumido os separadores. Para que
					 * seja reconhecido como um token combinado, � necess�rio que eles n�o estejam
					 * separados se n�o, ser�o dois tokens diferentes
					 */
					cbt.addToken(akc);// Salva o pr�ximo token
				}

			}
			if (cbt.getParts().size() > 1) {// Se houveram tokens combindos
				return cbt.valid(rules[safe]);// Verifica se os tokens combinados realmente encaixam na regra e retorna
												// o token j� combinado
			}

			return cbt.getToken();// retorna token n�o combinado
		}
		// Se n�o existir, informa que o token j� � inv�lido com o caractere n�o
		// reconhecido consumido.
		return Token.invalid("" + sourceCode.consumeChar(), sourceCode.getLine(), sourceCode.getColumn());
	}

	/**
	 * Este m�todo � privado pois a interface de acesso ao Scanner � atrav�s do
	 * m�todo getToken(). Este m�todo depende das verifica��es do m�todo
	 * retroaludido (Tranquilo?!) e s� dever� ser chamado pelo mesmo.
	 * 
	 * Este m�todo recebe como par�metro um TokenType para que ele possa verificar o
	 * c�digo do token, juntamente com as outras informa��es Um m�todo que pode se
	 * assemelhar a este � o da classe Buffer, splitFirst(). Ele consome um "Token",
	 * at� encontrar um separador
	 * 
	 * No caso deste m�todo, a ideia b�sica � consumir at� um token v�lido do tipo
	 * recebido, separar token reconhec�veis de n�o reconheciveis para garantir que
	 * a linguagem seja reconhecida da forma correta.
	 * @throws ScannerException 
	 */
	private Token splitFirstTokenType(TokenType type){
		String ret = "";
		// A limpeza de separadores e a verifica��o de buffer vazio j� foi feita no
		// m�todo anterior

		byte i = 0, // guardar� o �ndice caractere reconhecido at� ent�o
				s = 0, // (start) guardar� o indice do vetor list do TokenType type a partir do qual
						// ainda pode
						// ser o token encontrado no buffer
				e = type == null ? 0 : (byte)type.list.length; // (end) guardar� o indice do vetor list do TokenType type a
															// partir do qual n�o
		// pode
		// ser o token encontrado no buffer

		byte safe = e;// Guarda o indice do ultimo elemento da lista do TokenType reconhecido at�
						// ent�o
		// Utilizado apenas em tipos n�o extens�veis(any == false)

		// Olha se o caractere inicial do token � do tipo dos caracteres iniciais do
		// tipo de token
		if (this.sourceCode.firstCharToken(type.type))
			// Olha se os caracteres do meio para o final do token s�o do tipo dos mesmos
			// caracteres no tipo de token
			while (this.sourceCode.otherCharToken(type.type)) {
				/*
				 * Esta diferen�a acontece unicamente com os alfanumericos. Eles s�o
				 * alfanumericos poque podem conter numeros mas n�o podem come�ar com numeros.
				 * Confira como est�o implementadas as fun��es firstCharToken e otherCharToken
				 */

				// Obt�m o caractere corrente a ser avaliado
				char temp = this.sourceCode.getChar();
				byte j;
				// Procura o caractere corrente no caractere de mesmo indice i dos tokens da
				// lista to TokenType
				for (j = s; j < e; j++) {
					// Objerva se o tamanho do token da lista � menor que o tamanho do token at�
					// ent�o reconhecido
					if (type.list[j].length() > i) {
						// Observa se o caractere encontrado � igual ao caractere do token da lista
						if (type.list[j].charAt(i) == temp) {
							// Salva o ultimo token reconhecido
							safe = j;
							// Salva o come�o dos tokens que podem ser iguais
							s = j;
							// Finaliza este la�o
							break;
						}
					}
					// salta o token da lista
					s = (byte) (j + 1);
					// (Este token n�o ser� mais avaliado para esta chamada,
					// apenas na proxima chamada do m�todo)
				}
				// Analisa at� onde vai os caracteres de mesmo indice iguais ao caractere
				// encontrado
				// Com o objetivo de encontrar o novo 'e'(indice apartir do qual n�o poder� ser
				// o token encontrado)
				for (j = (byte) (s + 1); j < e; j++) {
					if (type.list[j].length() <= i || type.list[j].charAt(i) > temp) {
						e = j;
					}
				}

				// Apenas para os tipos n�o extens�veis
				// Se o start � maior ou igual ao end, significa que n�o h� um token na lista
				// para a sequencia de caracteres encontrada
				if (s >= e && !type.any) {
					// Verifica se houve algum at� o caractere anterior que pudesse ser reconhecido
					if (safe != type.list.length)
						// Havendo, retorna at� o caractere anterior, sem consumir o que causou o n�o
						// reconhecimento
						return new Token((byte) (type.nStart + safe + 1), ret, this.sourceCode.getLine(),
								this.sourceCode.getColumn());
					// esse caractere que n�o foi reconhecido junto com os anteriores, pode ser
					// v�lido ou n�o sozinho ou na sequencia seginte de caracteres
					// sendo assim, a pr�xima chamada dessa fun��o se encarregar� de decidir e
					// tratar

					// N�o havendo uma atribui��o de safe em algum passo da verifica��o, a sequencia
					// de caracteres encontrados n�o se encaixa em nenhum dos tokens da lista e a
					// fun��o retorna token inv�lido com o caractere n�o reconhecido
					return Token.invalid("" + this.sourceCode.consumeChar(), this.sourceCode.getLine(),
							this.sourceCode.getColumn());

				}

				// Adiciona o caractere a String que representa o spelling do token a ser
				// retornado
				ret += temp;

				// Avan�a com o indice do caractere do token at� ent�o encontrado.
				i++;

				// Avan�a um caractere no buffer de c�digo fonte
				this.sourceCode.consumeChar();
			} // Finaliza o while quando o proximo caractere n�o for do tipo avaliavel pelo
				// tokentype, seja isso um caractere de outro tipo reconhec�vel ou um separador

		// Se o start ainda for menor que o end,
		if (e > s) {
			// E o comprimento das strings, ret(token encontrado) e a da lista no indice
			// start(possivel token reconhecido) forem iguais,
			if (ret.length() == type.list[s].length())
				// A string ret conter� a mesma string encontrada no indice start da lista de
				// tokentype. Sendo assim, o c�digo retornado ser� o c�digo base do tipo mais o
				// indice start(s)
				return new Token((byte) (type.nStart + s + 1), ret, this.sourceCode.getLine(), this.sourceCode.getColumn());
		}

		// Esta linha s� sera executada se a string ret n�o estiver contida na lista de
		// tokens especiais e o tipo de token avaliado for extens�vel, o que faz com que
		// qualquer token daquele tipo seja v�lido, retornando com o c�digo base do tipo

		// O comando if seguinte foi comentado, devido a sua caracteristica
		// desnecess�ria j� que todos os outros casos j� foram tratados acima
//		if (type.any)
		return new Token(type.nStart, ret, this.sourceCode.getLine(), this.sourceCode.getColumn());

	}

	public PatternToken getPt() {
		return pt;
	}
	
	

}