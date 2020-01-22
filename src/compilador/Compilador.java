package compilador;

import ast.AST;
import checker.Checker;
import checker.CheckerException;
import coder.Coder;
import formatter.Formatter;
import formatter.PascalFormater;
import function.JD3String;
import parser.Parser;
import parser.ParserException;
import printer.Printer;
import scanner.Scanner;

public class Compilador {
	public static void main(String args[]) throws Exception {

		try {
			// Verificação sintática
			String path = "files\\grammar-tokens.tkn";
			String src = "files\\sc1.pas";

			Parser parser = new Parser(path, src);
			AST program = null;

			program = parser.parse();

			// Formatando Texto
			Formatter f = new PascalFormater();
			String formated = f.format(program);
			JD3String.fileOut("sc1_formatted.pas", formated);

			// Desenho da árvore
			Printer p = new Printer();
			p.print(program, "sc1_before.html");

			// Verificação de Contexto
			Checker ch = new Checker();
			ch.check(program);

			// Desenho da árvore
			p.print(program, "sc1_after.html");

			// Geração de Código
			Coder coder = new Coder();
			coder.code(program,"sc1_code.tam");

		} catch (ParserException e) {
			System.err.println(e.getMessage());
		} catch (CheckerException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void scannerTest(String args[]) {
		// Testando léxico

		// Lista de tokens válidos com seus respectivos tipos e códigos
		String path = "files\\grammar-tokens.tkn";
		// Representa o código fonte da linguagem a ser compilada
		String teste = "H:\\faculdade\\com backup\\00Material da Faculdade\\7semestre\\Compiladores\\outros\\testeponto.txt";

		// Constroi o sintático consumindo o arquivo de tokens e construindo o buffer
		// com o código fonte
		Scanner sc = new Scanner(path, teste);

		// Representação do sintático solicitando token a token recebendo um objeto do
		// tipo Token
		System.out.println(sc.scan());
	}

	public static void parserTest(String[] args) throws ParserException {

		// Lista de tokens válidos com seus respectivos tipos e códigos
		String path = "src\\files\\grammar-tokens.tkn";
		// Representa o código fonte da linguagem a ser compilada
		String src = "src\\files\\sc1.pas";

		// Constroi o sintático consumindo o arquivo de tokens e construindo o buffer
		// com o código fonte
		Parser parser = new Parser(path, src);

		// Analisando sintaticamente o código

		parser.parse();

	}
	
	public static void formatterTest(String args[]) throws Exception {
		// Lista de tokens válidos com seus respectivos tipos e códigos
		String path = "src\\files\\grammar-tokens.tkn";
		// Representa o código fonte da linguagem a ser compilada
		String src = "src\\files\\sc1.pas";

		// Constroi o sintático consumindo o arquivo de tokens e construindo o buffer
		// com o código fonte
		Parser parser = new Parser(path, src);

		// Analisando sintaticamente o código
		AST program = parser.parse();
		
		//Formatter
		Formatter formatter = new PascalFormater();
		System.out.println(formatter.format(program));
	}
	
	public static void printerTest(String args[]) throws Exception {
		String path = "files\\grammar-tokens.tkn";
		String src = "files\\sc1.pas";
		Printer p = new Printer();
		Parser parser = new Parser(path, src);
		AST program = null;

		try {
			program = parser.parse();
		} catch (Exception e) {
			e.printStackTrace();
		}

		p.print(program, "sc1.html");
	}
	
	public static void checkerTest(String args[]) throws Exception {
		String path = "files\\grammar-tokens.tkn";
		String src = "files\\sc1.pas";
		Printer p = new Printer();
		Parser parser = new Parser(path, src);
		AST program = null;

		try {
			program = parser.parse();
		} catch (Exception e) {
			e.printStackTrace();
		}

		p.print(program, "sc1_before.html");

		Checker ch = new Checker();
		ch.check(program);

		p.print(program, "sc1_after.html");
	}
	
}
