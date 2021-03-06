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
	/**
	 * Op��es de interface
	 * src_nome.pas [-p path_print.html]
	 * 				[-pc {-force} path_print_pos_check.html]
	 * 				[-f path_formated.pas]
	 * 				[-e path_log_err.txt]
	 * 				[-c path_code.tam]
	 * */
	public static void main(String args[]) throws Exception {
		String path;
		String src;
		Parser parser;
		AST program = null;
		Printer p = null;
		Formatter f;
		Checker ch;
		Coder coder;
		try {
			
			// Verifica��o sint�tica
			path = "src\\files\\grammar-tokens.tkn";
			src = "src\\files\\sc1.pas";

			parser = new Parser(path, src);
			program = null;

			program = parser.parse();

			// Formatando Texto
			f = new PascalFormater();
			String formated = f.format(program);
			JD3String.fileOut("sc1_formatted.pas", formated);

			// Desenho da �rvore
			p = new Printer();
			p.print(program, "sc1_before.html");

			// Verifica��o de Contexto
			ch = new Checker();
			ch.check(program);

			// Desenho da �rvore
			p.print(program, "sc1_after.html");

			// Gera��o de C�digo
			coder = new Coder();
			coder.code(program,"sc1_code.tam");

		} catch (ParserException e) {
			System.err.println(e.getMessage());
		} catch (CheckerException e) {
			System.err.println(e.getMessage());
			p.print(program, "sc1_after.html");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("ERROR : Send a file path to compile!");
		}
	}

	public static void scannerTest(String args[]) {
		// Testando l�xico

		// Lista de tokens v�lidos com seus respectivos tipos e c�digos
		String path = "src\\files\\grammar-tokens.tkn";
		// Representa o c�digo fonte da linguagem a ser compilada
		String teste = "src\\files\\testeponto.txt";

		// Constroi o sint�tico consumindo o arquivo de tokens e construindo o buffer
		// com o c�digo fonte
		Scanner sc = new Scanner(path, teste);

		// Representa��o do sint�tico solicitando token a token recebendo um objeto do
		// tipo Token
		System.out.println(sc.scan());
	}

	public static void parserTest(String[] args) throws ParserException {

		// Lista de tokens v�lidos com seus respectivos tipos e c�digos
		String path = "src\\files\\grammar-tokens.tkn";
		// Representa o c�digo fonte da linguagem a ser compilada
		String src = "src\\files\\sc1.pas";

		// Constroi o sint�tico consumindo o arquivo de tokens e construindo o buffer
		// com o c�digo fonte
		Parser parser = new Parser(path, src);

		// Analisando sintaticamente o c�digo

		parser.parse();

	}
	
	public static void formatterTest(String args[]) throws Exception {
		// Lista de tokens v�lidos com seus respectivos tipos e c�digos
		String path = "src\\files\\grammar-tokens.tkn";
		// Representa o c�digo fonte da linguagem a ser compilada
		String src = "src\\files\\sc1.pas";

		// Constroi o sint�tico consumindo o arquivo de tokens e construindo o buffer
		// com o c�digo fonte
		Parser parser = new Parser(path, src);

		// Analisando sintaticamente o c�digo
		AST program = parser.parse();
		
		//Formatter
		Formatter formatter = new PascalFormater();
		System.out.println(formatter.format(program));
	}
	
	public static void printerTest(String args[]) throws Exception {
		String path = "src\\files\\grammar-tokens.tkn";
		String src = "src\\files\\sc1.pas";
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
		String path = "src\\files\\grammar-tokens.tkn";
		String src = "src\\files\\sc1.pas";
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
