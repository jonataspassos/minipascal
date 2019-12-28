package utils;

public abstract class Buffer {
	private int line;
	private int column;
	private int beforeColumn;
	private int tabSize;
	public final String eob = "#EOB#";

	public Buffer(int tabSize) {
		super();
		this.line = 0;
		this.column = 0;
		this.tabSize = tabSize;
	}

	public Buffer() {
		super();
		this.line = 0;
		this.column = 0;
		this.tabSize = 4;
	}

	/**
	 * @return o primeiro caractere do buffer, sem consumí-lo
	 */
	public abstract char getChar();

	/**
	 * Consome o primeiro caractere do buffer
	 * 
	 * @return o caractere consumido
	 */
	public abstract char consumeChar();

	/**
	 * Verifica se o buffer está vazio
	 */
	public abstract boolean isEmpty();

	/**
	 * Numero de colunas que o tab ocupa
	 */
	public int getTabSize() {
		return tabSize;
	}

	/**
	 * {@link Buffer#getTabSize()}
	 */
	public void setTabSize(int tabSize) {
		this.tabSize = tabSize;
	}

	/**
	 * Linha corrente do buffer
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Coluna corrente do buffer
	 */
	public int getColumnAfter() {
		return column;
	}

	/**
	 * Coluna antes do ultimo token consumido Sempre após o ultimo separador ou
	 * entre tokens diferentes
	 */
	public int getColumn() {
		return beforeColumn;
	}

	/**
	 * Incrementa em uma unidade a coluna corrente
	 */
	protected void incColumn() {
		// column = beforeColumn;
		column++;
	}

	/**
	 * Compara se o primeiro caractere é um digito
	 */
	public boolean isDigit() {
		return MyString.isDigit(getChar());
	}

	/**
	 * Compara se o primeiro caractere é uma letra
	 */
	public boolean isLetter() {
		return MyString.isLetter(getChar());
	}

	/**
	 * Compara se o primeiro caractere é um separador
	 */
	public boolean isSeparator() {
		return MyString.isSeparator(getChar());
	}

	/**
	 * Compara se o primeiro caractere é um Símbolo
	 */
	public boolean isSymbol() {
		return MyString.isSymbol(getChar());
	}

	/**
	 * Consome os separadores do inicio da string
	 * 
	 * @param s - a string a ser consumida
	 */
	public void consumeSeparators() {
		if (isEmpty())
			return;
		char temp = getChar();
		boolean repete = false;
		do {
			switch (temp) {
			case '\n':
				line++;
				consumeChar();
				column = 0;
				repete = true;
				break;
			case '\r':
				consumeChar();
				column = 0;
				if (getChar() == '\n') {
					consumeChar();
					column = 0;
				}
				line++;
				repete = true;
				break;
			case '\t':
				column += tabSize - (column % tabSize) - 1;
				consumeChar();
				repete = true;
				break;
			case ' ':
				consumeChar();
				repete = true;
				break;
			case '#':// Comentario de linha
				// Os comentários poderão ser feitos tanto iniciando e finalizando ainda na
				// mesma linha , podendo escrever código apos o comentário ou finalizar o
				// comentário com a quebra de linha
				// Ex: <código> #comentário \n
				// <código> #comentário# <código> #comentário \n

				consumeChar();
				while (getChar() != '\r' && getChar() != '\n' && getChar() != '#')
					consumeChar();
				if (getChar() == '#')
					consumeChar();
				break;
			default:
				repete = false;
			}
			if (repete) {
				temp = getChar();
				if (isEmpty()) {
					repete = false;
				}
			}
		} while (repete);
		beforeColumn = column;
	}

	/**
	 * Consome a expressao sem separadores do inicio do buffer
	 * 
	 * @returns a string consumida
	 */
	public String splitFirst() {
		String ret = "";
		if (isEmpty()) {
			return eob;
		}

		consumeSeparators();

		while (!isSeparator() && getChar() != '\0') {
			ret += consumeChar();
			column++;
		}

		return ret;
	}

	/**
	 * Observa se o primeiro caractere do buffer corresponde ao inicio de um token
	 * do tipo desejado
	 * 
	 * @param tipo do token que se deseja avaliar
	 */
	public boolean firstCharToken(char type) {
		switch (type) {
		case 'a':// Alfa-numérico
			return isLetter();
		case 'n':// Numero
			return isDigit();
		case 's':// Simbolo
			return isSymbol();
		default:
			return false;
		}
	}

	/**
	 * Observa se o primeiro caractere do buffer corresponde ao qualquer caractere
	 * de um token do tipo desejado
	 * 
	 * @param tipo do token que se deseja avaliar
	 */
	public boolean otherCharToken(char type) {
		switch (type) {
		case 'a':// Alfa-numerico
			return isLetter() || isDigit() || getChar() == '_';
		case 'n':// Numero
			return isDigit();
		case 's':// Símbolo
			return isSymbol();
		default:
			return false;
		}
	}

	public char getTypeChar() {
		return MyString.type(this.getChar());
	}

}
