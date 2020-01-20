package scanner;

import utils.Buffer;
import utils.FileBuffer;

/**
 * Objeto preparado para devolver um token sempre que solicitado
 * <p>
 * Basicamente, esta classe abstrai o buffer normal, devolvendo não mais
 * caracteres mas os tokens já tratados , logo, os caracteres estão para o
 * Buffer como os tokens estão para o Scanner.
 * </p>
 * <p>
 * A ideia aqui é especificar no momento da instancia, o arquivo que contém
 * todas as especificações de token da linguagem (Exemplo no arquivo *.tkn) e o
 * arquivo com o código fonte a ser avaliado.
 * </p>
 * <p>
 * Ele reconhecerá todos os tokens da linguagem, e construirá o PatternToken do
 * scanner. Guardará o código fonte e esperará ser solicitado. Como saída,
 * oferecerá um objeto da classe Token para as proximas etapas do compilador
 * </p>
 */
public class Scanner {
	private PatternToken pt;
	private Buffer sourceCode;
	
	/**
	 * Envie como parametro de construção os buffers já instanciados de:
	 * 
	 * @param pt         - paterrn token (de onde será construido o PatternToken)
	 * @param sourceCode - Que será consumido sempre que solicitado um novo token
	 */
	public Scanner(Buffer pt, Buffer sourceCode) {
		super();
		this.pt = new PatternToken(pt);
		this.sourceCode = sourceCode;
	}

	/**
	 * Envie como parametro de construção os caminhos dos arquivos para criar os
	 * buffers de:
	 * 
	 * @param pathPattern    - paterrn token (de onde será construido o
	 *                       PatternToken)
	 * @param pathSourceCode - Que será consumido sempre que solicitado um novo
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
	 * Este método busca no buffer de SourceCode um token e o retorna com todas as
	 * respectivas informações
	 * 
	 * @return um objeto Token preenchido de acordo com as especificações do
	 *         PatternToken
	 * @throws ScannerException 
	 */
	public Token scan(){
		// Consome os separadores e atualiza as linhas e colunas do buffer
		sourceCode.consumeSeparators();

		// Se não tiver mais nada no buffer, retorna Fim do Arquivo
		if (isEmpty()) {
			return Token.eof(this.sourceCode.getLine(), this.sourceCode.getColumn());
		}

		// Procura um TokenType com o tipo de caractere do mesmo tipo do caractere
		// corrente no buffer
		TokenType tt = pt.getType(sourceCode.getTypeChar());

		// Se existir um token type,
		if (tt != null) {
			// chama a função de consumir token com o tokenType encontrado
			Token akc = splitFirstTokenType(tt);
			CombinedTokens cbt = new CombinedTokens(akc);

			// Verifica disponibilidade de regra combinada com o token encontrado

			CombinedTokens[] rules = pt.getCombinedRules();
			int s = 0, // Regra a partir da qual ainda é reconhecível
					e = rules == null ? 0 : rules.length, // Regra a partir da qual não é mais reconhecivel
					i = 0, // Token da regra que se está olhando
					safe = -1;// Guarda o indice da ultima regra válida
			while (s < e) {// não há mais regras para o token atual ou próximo caractere
				int j;
				for (j = s; j < e; j++) {// Leva o cursos de inicio para a primeira regra reconhecivel

					// Se Token corrente encaixa na regra
					if (rules[j].getParts().size() > i && rules[j].getParts(i).kind == akc.kind &&
					// e Não existe um proximo token ou
							(rules[j].getParts().size() <= (i + 1) ||
							// Existe um proximo token e são de mesmo tipo e
									(rules[j].getParts(i + 1).type() == sourceCode.getTypeChar() &&
									// token é do tipo extensível ou
											(rules[j].getParts(i + 1).kind == pt
													.getType(sourceCode.getTypeChar()).nStart ||
											// não é extensivel mas os caracteres são iguais
													rules[j].getParts(i + 1).first() == sourceCode.getChar())))) {

						s = j;// Avança o cursor
						safe = j;// Salva o cursor
						break;
					}
					s = j + 1;// Salta a regra que não encaixa
				}
				for (j = s + 1; j < e; j++) {// Leva o cursor de final para a primeira regra não reconhecivel
					// Token Corrente não encaixa na regra ou
					if (rules[j].getParts().size() <= i || rules[j].getParts(i).kind != akc.kind
					// existe um proximo token e
							|| rules[j].getParts().size() > (i + 1) && (
							// não é do mesmo tipo do token da regra ou
							rules[j].getParts(i + 1).type() != sourceCode.getTypeChar() ||
							// é do mesmo tipo mas não é do tipo extensível e
									rules[j].getParts(i + 1).kind != pt.getType(sourceCode.getTypeChar()).nStart &&
									// e o primeiro caractere é diferente
											rules[j].getParts(i + 1).first() != sourceCode.getChar())) {
						e = j;// Localizou o primeiro token não reconhecivel
					}
				}
				i++;

				// Se há mais de uma regra a ser reconhecida ou
				if ((s + 1) < e
						// só há uma, mas ainda não consumiu o token que faz parte dela
						|| (s == safe && rules.length > s && cbt.getParts().size() < rules[s].getParts().size())) {
					akc = splitFirstTokenType(pt.getType(sourceCode.getTypeChar()));
					/**
					 * Um próximo token é consumido mas não é consumido os separadores. Para que
					 * seja reconhecido como um token combinado, é necessário que eles não estejam
					 * separados se não, serão dois tokens diferentes
					 */
					cbt.addToken(akc);// Salva o próximo token
				}

			}
			if (cbt.getParts().size() > 1) {// Se houveram tokens combindos
				return cbt.valid(rules[safe]);// Verifica se os tokens combinados realmente encaixam na regra e retorna
												// o token já combinado
			}

			return cbt.getToken();// retorna token não combinado
		}
		// Se não existir, informa que o token já é inválido com o caractere não
		// reconhecido consumido.
		return Token.invalid("" + sourceCode.consumeChar(), sourceCode.getLine(), sourceCode.getColumn());
	}

	/**
	 * Este método é privado pois a interface de acesso ao Scanner é através do
	 * método getToken(). Este método depende das verificações do método
	 * retroaludido (Tranquilo?!) e só deverá ser chamado pelo mesmo.
	 * 
	 * Este método recebe como parâmetro um TokenType para que ele possa verificar o
	 * código do token, juntamente com as outras informações Um método que pode se
	 * assemelhar a este é o da classe Buffer, splitFirst(). Ele consome um "Token",
	 * até encontrar um separador
	 * 
	 * No caso deste método, a ideia básica é consumir até um token válido do tipo
	 * recebido, separar token reconhecíveis de não reconheciveis para garantir que
	 * a linguagem seja reconhecida da forma correta.
	 * @throws ScannerException 
	 */
	private Token splitFirstTokenType(TokenType type){
		String ret = "";
		// A limpeza de separadores e a verificação de buffer vazio já foi feita no
		// método anterior

		byte i = 0, // guardará o índice caractere reconhecido até então
				s = 0, // (start) guardará o indice do vetor list do TokenType type a partir do qual
						// ainda pode
						// ser o token encontrado no buffer
				e = type == null ? 0 : (byte)type.list.length; // (end) guardará o indice do vetor list do TokenType type a
															// partir do qual não
		// pode
		// ser o token encontrado no buffer

		byte safe = e;// Guarda o indice do ultimo elemento da lista do TokenType reconhecido até
						// então
		// Utilizado apenas em tipos não extensíveis(any == false)

		// Olha se o caractere inicial do token é do tipo dos caracteres iniciais do
		// tipo de token
		if (this.sourceCode.firstCharToken(type.type))
			// Olha se os caracteres do meio para o final do token são do tipo dos mesmos
			// caracteres no tipo de token
			while (this.sourceCode.otherCharToken(type.type)) {
				/*
				 * Esta diferença acontece unicamente com os alfanumericos. Eles são
				 * alfanumericos poque podem conter numeros mas não podem começar com numeros.
				 * Confira como estão implementadas as funções firstCharToken e otherCharToken
				 */

				// Obtém o caractere corrente a ser avaliado
				char temp = this.sourceCode.getChar();
				byte j;
				// Procura o caractere corrente no caractere de mesmo indice i dos tokens da
				// lista to TokenType
				for (j = s; j < e; j++) {
					// Objerva se o tamanho do token da lista é menor que o tamanho do token até
					// então reconhecido
					if (type.list[j].length() > i) {
						// Observa se o caractere encontrado é igual ao caractere do token da lista
						if (type.list[j].charAt(i) == temp) {
							// Salva o ultimo token reconhecido
							safe = j;
							// Salva o começo dos tokens que podem ser iguais
							s = j;
							// Finaliza este laço
							break;
						}
					}
					// salta o token da lista
					s = (byte) (j + 1);
					// (Este token não será mais avaliado para esta chamada,
					// apenas na proxima chamada do método)
				}
				// Analisa até onde vai os caracteres de mesmo indice iguais ao caractere
				// encontrado
				// Com o objetivo de encontrar o novo 'e'(indice apartir do qual não poderá ser
				// o token encontrado)
				for (j = (byte) (s + 1); j < e; j++) {
					if (type.list[j].length() <= i || type.list[j].charAt(i) > temp) {
						e = j;
					}
				}

				// Apenas para os tipos não extensíveis
				// Se o start é maior ou igual ao end, significa que não há um token na lista
				// para a sequencia de caracteres encontrada
				if (s >= e && !type.any) {
					// Verifica se houve algum até o caractere anterior que pudesse ser reconhecido
					if (safe != type.list.length)
						// Havendo, retorna até o caractere anterior, sem consumir o que causou o não
						// reconhecimento
						return new Token((byte) (type.nStart + safe + 1), ret, this.sourceCode.getLine(),
								this.sourceCode.getColumn());
					// esse caractere que não foi reconhecido junto com os anteriores, pode ser
					// válido ou não sozinho ou na sequencia seginte de caracteres
					// sendo assim, a próxima chamada dessa função se encarregará de decidir e
					// tratar

					// Não havendo uma atribuição de safe em algum passo da verificação, a sequencia
					// de caracteres encontrados não se encaixa em nenhum dos tokens da lista e a
					// função retorna token inválido com o caractere não reconhecido
					return Token.invalid("" + this.sourceCode.consumeChar(), this.sourceCode.getLine(),
							this.sourceCode.getColumn());

				}

				// Adiciona o caractere a String que representa o spelling do token a ser
				// retornado
				ret += temp;

				// Avança com o indice do caractere do token até então encontrado.
				i++;

				// Avança um caractere no buffer de código fonte
				this.sourceCode.consumeChar();
			} // Finaliza o while quando o proximo caractere não for do tipo avaliavel pelo
				// tokentype, seja isso um caractere de outro tipo reconhecível ou um separador

		// Se o start ainda for menor que o end,
		if (e > s) {
			// E o comprimento das strings, ret(token encontrado) e a da lista no indice
			// start(possivel token reconhecido) forem iguais,
			if (ret.length() == type.list[s].length())
				// A string ret conterá a mesma string encontrada no indice start da lista de
				// tokentype. Sendo assim, o código retornado será o código base do tipo mais o
				// indice start(s)
				return new Token((byte) (type.nStart + s + 1), ret, this.sourceCode.getLine(), this.sourceCode.getColumn());
		}

		// Esta linha só sera executada se a string ret não estiver contida na lista de
		// tokens especiais e o tipo de token avaliado for extensível, o que faz com que
		// qualquer token daquele tipo seja válido, retornando com o código base do tipo

		// O comando if seguinte foi comentado, devido a sua caracteristica
		// desnecessária já que todos os outros casos já foram tratados acima
//		if (type.any)
		return new Token(type.nStart, ret, this.sourceCode.getLine(), this.sourceCode.getColumn());

	}

	public PatternToken getPt() {
		return pt;
	}
	
	

}