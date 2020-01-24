package formatter;

import ast.AST;
import ast.Visitor;

/**
 * Classe que configura a utilização da AST para gerar um código de alto nivel
 * na mesma semântica do código fonte Será possível com ela, além de apenas
 * formatar para a linguagem original, traduzir o código para outras linguagens
 * como C e Java.
 */
public abstract class Formatter implements Visitor {
	/**
	 * Armazena a construção do programa formatado
	 */
	protected String out = "";
	/**
	 * Indica a identação atual
	 */
	protected int level = 0;

	/**
	 * Identa quantas vezes quanto a identação atual
	 */
	protected void identation() {
		for (int i = 0; i < level; i++) {
			this.out += "\t";
		}
	}

	/**
	 * Configura a identação e Identa quantas vezes quanto a identação atual
	 * 
	 * @param increase - true para avançar uma identação e false para retornar uma
	 *                 identação
	 */
	protected void identation(boolean increase) {
		this.level += increase ? 1 : -1;
		for (int i = 0; i < level; i++) {
			this.out += "\t";
		}
	}

	/**
	 * Realiza a formatação percorrendo a AST recebida por parâmetro
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
