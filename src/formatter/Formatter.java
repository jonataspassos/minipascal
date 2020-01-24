package formatter;

import ast.AST;
import ast.Visitor;

/**
 * Classe que configura a utiliza��o da AST para gerar um c�digo de alto nivel
 * na mesma sem�ntica do c�digo fonte Ser� poss�vel com ela, al�m de apenas
 * formatar para a linguagem original, traduzir o c�digo para outras linguagens
 * como C e Java.
 */
public abstract class Formatter implements Visitor {
	/**
	 * Armazena a constru��o do programa formatado
	 */
	protected String out = "";
	/**
	 * Indica a identa��o atual
	 */
	protected int level = 0;

	/**
	 * Identa quantas vezes quanto a identa��o atual
	 */
	protected void identation() {
		for (int i = 0; i < level; i++) {
			this.out += "\t";
		}
	}

	/**
	 * Configura a identa��o e Identa quantas vezes quanto a identa��o atual
	 * 
	 * @param increase - true para avan�ar uma identa��o e false para retornar uma
	 *                 identa��o
	 */
	protected void identation(boolean increase) {
		this.level += increase ? 1 : -1;
		for (int i = 0; i < level; i++) {
			this.out += "\t";
		}
	}

	/**
	 * Realiza a formata��o percorrendo a AST recebida por par�metro
	 * @param ast - arvore a ser percorrida e formatada
	 * */
	public String format(AST ast) {
		this.out = "";
		if (ast == null)
			return "";
		ast.visit(this);
		return this.out;
	}
}
