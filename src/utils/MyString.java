package utils;

public class MyString {

	/**
	 * @returns Verdadeiro se o caractere é um digito
	 */
	public static boolean isDigit(char c) {
		return ((int) c) >= ((int) '0') && ((int) c) <= ((int) '9');
	}

	/**
	 * @returns Verdadeiro se o caractere é uma letra
	 */
	public static boolean isLetter(char c) {
		return (((int) c) >= ((int) 'a') && ((int) c) <= ((int) 'z'))
				|| (((int) c) >= ((int) 'A') && ((int) c) <= ((int) 'Z'));
	}

	/**
	 * @return Verdadeiro se o caractere é um separador
	 * */
	public static boolean isSeparator(char c) {
		return c=='\r' || c=='\n' || c == '\t' || c == ' ';
	}
	
	/**
	 * @return Verdadeiro se o caractere é um símbolo
	 * */
	public static boolean isSymbol(char c) {
		int i = (int)c;
		return !isDigit(c) && !isLetter(c) && !isSeparator(c) && i > ' ' && i!=127;
	}
	
	/**
	 * 
	 * */
	public static char type(char c) {
		if(isDigit(c))
			return 'n';//Digito pode ser parte de um numero ou de um alfanumerico
		if(isLetter(c))
			return 'a';//Alfa numerico
		if(isSymbol(c)) 
			return 's';//Símbolo
		if(isSeparator(c))
			return 'd';
		else
			return '\0';
	}

	/**
	 * @return Verdadeiro se C1 é igual a C2
	 * */
	public static boolean equals(String c1, String c2) {
		if(c1.length()!=c2.length()) {
			return false;
		}
		for(int i=0;i<c1.length();i++) {
			if(c1.charAt(i)!=c2.charAt(i))
				return false;
		}
		return true;
	}
	
	
	/**
	 * Converte vetor da classe Object do java em um Vetor de String
	 * Isso é usado quanso se precisa extrair o vetor formado pelos elementos de um Arraylist.
	 * */
	public static String[] convert(Object [] v) {
		String [] r = new String[v.length];
		for(int i=0;i<v.length;i++) {
			if(v[i] instanceof String) {
				r[i] = (String)v[i];
			}
		}
		return r;
	}
	
	public static int toInt(String string) {
		int ret = 0;
		int s = 1;
		StringBuffer b = new StringBuffer(string);
		
		if(b.getChar()=='-') {
			s = -1;
			b.consumeChar();
		}
		
		while(!b.isEmpty() && b.getTypeChar() == 'n') {
			ret = ret*10 + b.consumeChar()-'0';
		}
		
		return ret*s;
	}
	
	public static byte toByte(String string) {
		byte ret = 0;
		StringBuffer b = new StringBuffer(string);
		while(!b.isEmpty() && b.getTypeChar() == 'n') {
			ret = (byte)(ret*10 + b.consumeChar()-'0');
		}
		
		return ret;
	}
}
