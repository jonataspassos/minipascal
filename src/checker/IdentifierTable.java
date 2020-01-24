package checker;

import java.util.ArrayList;

import ast.Declaration;

/**
 * Esta classe armazena os identificadores declarados com suas respectivas
 * declara��es Serve para auxiliar a verifica��o de contexto, referenciando as
 * vari�veis na AST decorada
 */
public class IdentifierTable {
	/**
	 * Informa��es sobre o identificador
	 */
	private ArrayList<IdentifierTuple> table;

	/**
	 * Constroi a tabela de identificadores
	 */
	public IdentifierTable() {
		super();
		this.table = new ArrayList<IdentifierTuple>();
	}

	/**
	 * Deve ser chamada sempre que encontrada uma declara��o, gravando aqui o
	 * identificador e o objeto Declaration para ser recuperado depois
	 * 
	 * @param id  - nome do identificador declarado
	 * @param dec - declara��o onde o identificador se encontra
	 * @return True se o identificador j� tiver sido declarado e false caso n�o
	 */
	public boolean enter(String id, Declaration dec) {
		if (this.retrieve(id) == null) {
			this.table.add(new IdentifierTuple(id, dec));
			return false;
		}
		return true;
		// estar� declarando duas vari�veis com o mesmo nome
	}

	/**
	 * Realiza uma busca na tabela para encontrar o identificador.
	 * 
	 * @param id - identificador desejado
	 * @return Declara��o onde o id se encontra ou null caso n�o haja uma declara��o
	 *         daquele id
	 */
	public Declaration retrieve(String id) {
		for (IdentifierTuple i : this.table) {
			if (i.getId().equals(id)) {
				i.setRetrived();
				return i.getDec();
			}
		}
		return null;
		// estar� consultando variavel n�o declarada
	}

	/**
	 * Procura por identificadores n�o referenciados e lan�a uma exce��o (esta deve
	 * ser capturada como warning e n�o deve parar a compila��o)
	 */
	public void check() throws CheckerException {
		MultiCheckerException mce = new MultiCheckerException();
		for (IdentifierTuple i : this.table) {
			if (!i.isRetrived())
				mce.add(new CheckerException(i.getDec(), "The id ' " + i.id + " ' was declared but was not used") {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public String getMessage() {
						String ast = ("" + this.ast);
						if (ast.charAt(ast.length() - 1) == '\n')
							ast = ast.substring(0, ast.length() - 1);

						ast = ast.replaceAll("\n", "\n\t");
						return "WARNING [" + this.getLine() + " : " + this.getColumn() + "] Context Warning:\n"
								+ "\t--------------------------------\n" + "\t"
								+ ast.substring(0, ast.length() <= 50 ? ast.length() : 50) + "\n"
								+ "\t--------------------------------\n" + "\t" + this.message + "\n\n";
					}
				});
		}
		mce.check();
	}

}

/**
 * Classe interna que armazena o identificador
 * */
class IdentifierTuple {
	
	/**
	 * Nome do identificador
	 * */
	final String id;
	
	/**
	 * Declara��o do identificador
	 * */
	final Declaration dec;
	
	/**
	 * Se j� foi consultado
	 * */
	private boolean retrived;

	/**
	 * Constroi a tupla de identificador
	 * @param id - identificador
	 * @param dec - Declara��o do identificador
	 * */
	public IdentifierTuple(String id, Declaration dec) {
		super();
		this.id = id;
		this.dec = dec;
		this.retrived = false;
	}

	/**
	 * Consulta a utiliza��o
	 * */
	public boolean isRetrived() {
		return retrived;
	}

	/**
	 * Confirma a utiliza��o
	 * */
	public void setRetrived() {
		this.retrived = true;
	}

	public String getId() {
		return id;
	}

	public Declaration getDec() {
		return dec;
	}

}