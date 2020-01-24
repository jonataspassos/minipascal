package parser;

import ast.*;
import scanner.PatternToken;
import scanner.Scanner;
import scanner.Token;

/**
 * Esta classe Realiza a verifica��o sint�tica do codigo fonte. Para tanto, em
 * sua constru��o, precisar� receber a lista de tokens v�lidos reconhec�vel pela
 * PatternToken, e o c�digo fonte da linguagem. O primeiro � todo verificado
 * ainda na constru��o da inst�ncia, j� o segundo, � consumido apenas quando
 * solicitado o processamento de um novo token por este mesmo objeto.
 */
public class Parser {
	/**
	 * token corrente a ser avaliado
	 */
	private Token currentToken;
	/**
	 * L�xico compat�vel com a linguagem verificada
	 */
	private Scanner scanner;
	/**
	 * Acumulador de erros sint�ticos
	 */
	private MultiParserException mpe = new MultiParserException();

	/**
	 * Construtor
	 * 
	 * @param pathTkn - Caminho do arquivo que guarda a lista de tokens para cria��o
	 *                do L�xico(Scanner)
	 * @param pathSrc - Caminho do arquivo contendo o c�digo fonte a ser compilado
	 */
	public Parser(String pathTkn, String pathSrc) {
		this.scanner = new Scanner(pathTkn, pathSrc);
	}

	/**
	 * � quando a verifica��o sint�tica � feita propriamente dita Ela funciona
	 * percorrendo e consumindo o buffer de c�digo fonte, construindo uma �rvore
	 * sint�tica abstrata retornando a raiz da �rvore quando o c�digo for verificado
	 * como correto sint�ticamente
	 * 
	 * @return um objeto AST
	 */
	// Al�m do construtor, � o unico m�todo publico.
	public AST parse() throws ParserException {
		AST tree;
		acceptIt();
		tree = parseProgram();
		if (currentToken.kind != Token.endOfFileCode) {
			mpe.add(new ParserException(currentToken, "EOF"));
		}
		mpe.check();
		return tree;
	}

	/**
	 * Verifica sintaticamente o buffer para reconhecer um comando de atribui��o
	 * 
	 * @return um objeto AssignmentCommand
	 */
	private AssignmentCommand parseAssignmentCommand() throws MultiParserException {
		AssignmentCommand command = new AssignmentCommand(currentToken.line, currentToken.column);

		command.setVariable(parseVariable());// <variable>
		accept(PascalToken.tAssgn); // :=
		command.setExpression(parseExpression()); // <expression>

		return command;
	}

	/**
	 * Verifica sintaticamente o buffer para reconhecer um comando condicional
	 * 
	 * @return um objeto ConditionalCommand
	 */
	private ConditionalCommand parseConditionalCommand() throws MultiParserException {
		ConditionalCommand command = new ConditionalCommand(currentToken.line, currentToken.column);

		accept(PascalToken.tIf);// if
		command.setExpression(parseExpression()); // <expression>

		accept(PascalToken.tThen);// then
		command.setCommand(parseCommand(), true);// <command>
		if (currentToken.kind == PascalToken.tElse) { // ( else
			acceptIt();
			command.setCommand(parseCommand(), false);// <command>
		} // | <vazio> )
		return command;
	}

	/**
	 * Verifica sintaticamente o buffer para reconhecer um comando iterativo
	 * 
	 * @return um objeto IterativeCommand
	 */
	private IterativeCommand parseIterativeCommand() throws MultiParserException {
		IterativeCommand command = new IterativeCommand(currentToken.line, currentToken.column);
		accept(PascalToken.tWhile);// while
		command.setExpression(parseExpression());// <expression>
		accept(PascalToken.tDo);// do
		command.setCommand(parseCommand());// <command>

		return command;
	}

	/**
	 * First Follow 1 de Command Verifica se o pr�ximo token pode ser um primeiro
	 * token de um dos tipos de comando
	 * 
	 * @return verdadeiro ou falso confirmando a verifica��o acima
	 */
	private boolean FF1Command() {// First or Follow 1
		byte kind = currentToken.kind;
		return kind == PascalToken.tIdentifier || kind == PascalToken.tIf || kind == PascalToken.tWhile
				|| kind == PascalToken.tBegin;
	}

	/**
	 * Verifica sintaticamente o buffer para reconhecer um comando multiplo
	 * 
	 * @return um objeto MultiCommand
	 */
	private MultiCommand parseMultiCommand() throws MultiParserException {
		MultiCommand command = new MultiCommand(currentToken.line, currentToken.column);

		accept(PascalToken.tBegin);// begin
		while (FF1Command()) {// (
			command.addCommand(parseCommand());// <command>
			accept(PascalToken.tSemiColon);// ;
		} // )*
		accept(PascalToken.tEnd);// end
		return command;
	}

	/**
	 * Verifica sintaticamente o buffer para reconhecer um dos comandos
	 * 
	 * @return um objeto Command
	 */
	private Command parseCommand() throws MultiParserException {
		switch (currentToken.kind) {
		case PascalToken.tIdentifier: // atribui��o
			return parseAssignmentCommand();

		case PascalToken.tIf: // condicional
			return parseConditionalCommand();

		case PascalToken.tWhile: // iterativo
			return parseIterativeCommand();

		case PascalToken.tBegin: // multi-command
			return parseMultiCommand();

		default:
			this.mpe.add(new ParserException(currentToken,
					"a command started by : ' " + getSpelling(PascalToken.tIdentifier) + " ' , ' "
							+ getSpelling(PascalToken.tIf) + " ' , ' " + getSpelling(PascalToken.tWhile) + " ' , ' "
							+ getSpelling(PascalToken.tBegin)));
			acceptIt();
			if (FF1Command()) {
				return parseCommand();
			} else {
				throw this.mpe;
			}
		}
	}

	/**
	 * First Follow 1 de Declaration. Verifica se o pr�ximo token pode ser um primeiro
	 * token de um dos tipos de declara��o
	 * 
	 * @return verdadeiro ou falso confirmando a verifica��o acima
	 */
	private boolean FF1Declaration() {
		return currentToken.kind == PascalToken.tVar;
	}

	/**
	 * Verifica sintaticamente o buffer para reconhecer uma declara��o
	 * 
	 * @return um objeto Declaration
	 */
	private Declaration parseDeclaration() throws MultiParserException {
		Declaration declaration = new Declaration(currentToken.line, currentToken.column);
		accept(PascalToken.tVar);// var
		declaration.addId(accept(PascalToken.tIdentifier).spelling);// <id>
		while (currentToken.kind == PascalToken.tComma) {// (
			acceptIt();// , comma
			declaration.addId(accept(PascalToken.tIdentifier).spelling);// <id>
		} // )*
		accept(PascalToken.tColon);// :
		declaration.setType(parseType());// <type>
		accept(PascalToken.tSemiColon);// ;

		return declaration;
	}

	/**
	 * Verifica sintaticamente o buffer para reconhecer uma express�o
	 * 
	 * @return um objeto Expression
	 */
	private Expression parseExpression() throws MultiParserException {
		Expression expression = new Expression(currentToken.line, currentToken.column);
		expression.setA(parseSimpleExpression(), 0);// <simple-expression>
		if (FF1OpRel()) {// (
			expression.setOp(parseOpRel());// <op-rel>
			expression.setA(parseSimpleExpression(), 1); // <simple-expression>
		} // | vazio )

		return expression;
	}

	/**
	 * Verifica sintaticamente o buffer para reconhecer uma express�o simples
	 * 
	 * @return um objeto SimpleExpression
	 */
	private SimpleExpression parseSimpleExpression() throws MultiParserException {
		SimpleExpression expression = new SimpleExpression(currentToken.line, currentToken.column);
		expression.addA(parseTerm());// <term>
		while (FF1OpAd()) {// (
			expression.addOp(parseOpAd());// <op-ad>
			expression.addA(parseTerm());// <term>
		} // )*

		return expression;
	}

	/**
	 * Verifica sintaticamente o buffer para reconhecer um termo
	 * 
	 * @return um objeto Term
	 */
	private Term parseTerm() throws MultiParserException {
		Term term = new Term(currentToken.line, currentToken.column);
		term.addA(parseFactor());// <factor>
		while (FF1OpMul()) {// (
			term.addOp(parseOpMul());// <op-mul>
			term.addA(parseFactor());// <factor>
		} // )*

		return term;
	}

	/**
	 * First Follow 1 de Factor. Verifica se o pr�ximo token pode ser um primeiro
	 * token de um dos tipos de Factor
	 * 
	 * @return verdadeiro ou falso confirmando a verifica��o acima
	 */
	private boolean FF1Factor() {
		byte kind = currentToken.kind;
		return kind == PascalToken.tIdentifier || kind == PascalToken.tFalse || kind == PascalToken.tTrue
				|| kind == PascalToken.tIntegerLit || kind == PascalToken.tFloatLit || kind == PascalToken.tLParen;
	}

	/**
	 * Verifica sintaticamente o buffer para reconhecer um fator
	 * 
	 * @return um objeto Factor
	 */
	private Factor parseFactor() throws MultiParserException {
		Lit lit = null;
		switch (currentToken.kind) {
		case PascalToken.tIdentifier:
			return parseVariable();// <variable> |

		case PascalToken.tFalse:
		case PascalToken.tTrue:
			lit = new BoolLit(currentToken.line, currentToken.column);
			lit.setValue(acceptIt().spelling);// <bool-lit> |
			return lit;

		case PascalToken.tIntegerLit:
			lit = new IntLit(currentToken.line, currentToken.column);
			lit.setValue(acceptIt().spelling);
			return lit;// <int-lit> |

		case PascalToken.tFloatLit:
			lit = new FloatLit(currentToken.line, currentToken.column);
			lit.setValue(acceptIt().spelling);// <float-lit> |
			return lit;

		case PascalToken.tLParen:
			acceptIt();// "("
			Expression expression = parseExpression(); // <expression>
			acceptIt();// ")"

			return expression;
		default:
			this.mpe.add(new ParserException(currentToken,
					"a Factor like : an " + getSpelling(PascalToken.tIdentifier) + " , ' "
							+ getSpelling(PascalToken.tFalse) + " ' , ' " + getSpelling(PascalToken.tTrue) + " ' , ' "
							+ getSpelling(PascalToken.tIntegerLit) + " ' , ' " + getSpelling(PascalToken.tFloatLit)
							+ " ' , ' " + getSpelling(PascalToken.tLParen) + "..." + getSpelling(PascalToken.tRParen)));
			lit = new BoolLit(currentToken.line, currentToken.column);
			lit.setValue("false");// <bool-lit>
			acceptIt();
			if (FF1Factor()) {
				return parseFactor();
			} else {
				throw this.mpe;
			}
		}
	}

	/**
	 * Verifica sintaticamente o buffer para reconhecer um programa
	 * 
	 * @return um objeto Program
	 */
	private Program parseProgram() throws MultiParserException {
		Program program = new Program(currentToken.line, currentToken.column);

		accept(PascalToken.tProgram);// program
		program.setId(accept(PascalToken.tIdentifier).spelling);// <id>
		accept(PascalToken.tSemiColon);// ;
		while (FF1Declaration()) {// (
			program.addDeclaration(parseDeclaration());// <declaration>
		} // )*
		program.setMc(parseMultiCommand());// <multi-command>
		accept(PascalToken.tPeriod);// .

		return program;
	}

	/**
	 * First Follow 1 de Type. Verifica se o pr�ximo token pode ser um primeiro
	 * token de um dos tipos de tipo
	 * 
	 * @return verdadeiro ou falso confirmando a verifica��o acima
	 */
	private boolean FF1Type() {
		byte kind = currentToken.kind;
		return kind == PascalToken.tArray || kind == PascalToken.tInteger || kind == PascalToken.tReal
				|| kind == PascalToken.tBoolean;
	}

	/**
	 * Verifica sintaticamente o buffer para reconhecer uma tipo da linguagem
	 * 
	 * @return um objeto Type
	 */
	private Type parseType() throws MultiParserException {
		Type type;
		switch (currentToken.kind) {
		case PascalToken.tArray:// Type
			type = new AggregateType(currentToken.line, currentToken.column);

			acceptIt(); // | array
			accept(PascalToken.tLBracket); // [
			IntLit il = new IntLit(0, 0);
			il.setValue(accept(PascalToken.tIntegerLit).spelling);// <int-lit>
			((AggregateType) type).setIndex(il.getValue(), 0);

			accept(PascalToken.tTilde);// ~

			il.setValue(accept(PascalToken.tIntegerLit).spelling);// <int-lit>
			((AggregateType) type).setIndex(il.getValue(), 1);

			accept(PascalToken.tRBracket);// ]
			accept(PascalToken.tOf);// of

			((AggregateType) type).setType(parseType());// <type>

			break;
		case PascalToken.tInteger:
			type = new PrimitiveType(currentToken.line, currentToken.column);
			((PrimitiveType) type).setType(PrimitiveType.tInt);
			acceptIt();// integer
			break;
		case PascalToken.tReal:
			type = new PrimitiveType(currentToken.line, currentToken.column);
			((PrimitiveType) type).setType(PrimitiveType.tReal);
			acceptIt();// real
			break;
		case PascalToken.tBoolean:// Primitive Type
			type = new PrimitiveType(currentToken.line, currentToken.column);
			((PrimitiveType) type).setType(PrimitiveType.tBoolean);
			acceptIt();// boolean
			break;

		default:
			mpe.add(new ParserException(currentToken,
					" a type like: " + getSpelling(PascalToken.tInteger) + " ' , ' " + getSpelling(PascalToken.tReal)
							+ " ' , ' " + getSpelling(PascalToken.tBoolean) + " ' , ' "
							+ getSpelling(PascalToken.tArray) + "[... ~ ...] of ..."));
			type = new PrimitiveType(currentToken.line, currentToken.column);
			((PrimitiveType) type).setType(PrimitiveType.tBoolean);
			acceptIt();
			if (FF1Type()) {
				return parseType();
			} else {
				throw this.mpe;
			}
		}
		return type;
	}

	/**
	 * Verifica sintaticamente o buffer para reconhecer uma variavel, podendo ser de
	 * um dos tipos simples ou indexando um tipo agregado
	 * 
	 * @return um objeto Variable
	 */
	private Variable parseVariable() throws MultiParserException {
		Variable variable = new Variable(currentToken.line, currentToken.column);
		variable.setId(accept(PascalToken.tIdentifier).spelling); // <id>
		while (currentToken.kind == PascalToken.tLBracket) {// (
			acceptIt(); // "["
			variable.addIndexer(parseExpression());// <expression>
			accept(PascalToken.tRBracket);// "]"
		} // )*

		return variable;
	}

	/**
	 * First Follow 1 de OpAd. Verifica se o pr�ximo token pode ser um primeiro
	 * token de um dos operadores de termos
	 * 
	 * @return verdadeiro ou falso confirmando a verifica��o acima
	 */
	private boolean FF1OpAd() {
		byte kind = currentToken.kind;
		return kind == PascalToken.tPlusSgn || kind == PascalToken.tDash || kind == PascalToken.tOr;
	}

	/**
	 * Verifica sintaticamente o buffer para reconhecer um operador de termos
	 * 
	 * @return um objeto OpAd
	 */
	private OpAd parseOpAd() throws MultiParserException {
		OpAd ret;
		switch (currentToken.kind) {
		case PascalToken.tPlusSgn:
			ret = new OpAd(currentToken.line, currentToken.column);
			acceptIt();
			return ret.setOp(OpAd.tPlus);
		case PascalToken.tDash:
			ret = new OpAd(currentToken.line, currentToken.column);
			acceptIt();
			return ret.setOp(OpAd.tMinus);
		case PascalToken.tOr:
			ret = new OpAd(currentToken.line, currentToken.column);
			acceptIt();
			return ret.setOp(OpAd.tOr);
		default:
			mpe.add(new ParserException(currentToken, "an operator like : " + getSpelling(PascalToken.tPlusSgn)
					+ " ' , ' " + getSpelling(PascalToken.tDash) + " ' , ' " + getSpelling(PascalToken.tOr)));
			ret = new OpAd(currentToken.line, currentToken.column);
			acceptIt();
			if (FF1OpAd()) {
				return parseOpAd();
			} else {
				throw this.mpe;
			}
		}
	}

	/**
	 * First Follow 1 de OpMul. Verifica se o pr�ximo token pode ser um primeiro
	 * token de um dos tipos de operadores de fatores
	 * 
	 * @return verdadeiro ou falso confirmando a verifica��o acima
	 */
	private boolean FF1OpMul() {
		byte kind = currentToken.kind;
		return kind == PascalToken.tAsterisk || kind == PascalToken.tFwdSlash || kind == PascalToken.tAnd;
	}

	/**
	 * Verifica sintaticamente o buffer para reconhecer um operador de fatores
	 * 
	 * @return um objeto OpMul
	 */
	private OpMul parseOpMul() throws MultiParserException {
		OpMul ret;
		switch (currentToken.kind) {
		case PascalToken.tAsterisk:
			ret = new OpMul(currentToken.line, currentToken.column);
			acceptIt();
			return ret.setOp(OpMul.tProduct);
		case PascalToken.tFwdSlash:
			ret = new OpMul(currentToken.line, currentToken.column);
			acceptIt();
			return ret.setOp(OpMul.tDivision);
		case PascalToken.tAnd:
			ret = new OpMul(currentToken.line, currentToken.column);
			acceptIt();
			return ret.setOp(OpMul.tAnd);
		default:
			mpe.add(new ParserException(currentToken, "an operator like: " + getSpelling(PascalToken.tAsterisk)
					+ " ' , ' " + getSpelling(PascalToken.tFwdSlash) + " ' , ' " + getSpelling(PascalToken.tAnd)));
			ret = new OpMul(currentToken.line, currentToken.column);
			acceptIt();
			if (FF1OpMul()) {
				return parseOpMul();
			} else {
				throw this.mpe;
			}
		}
	}

	/**
	 * First Follow 1 de OpRel. Verifica se o pr�ximo token pode ser um primeiro
	 * token de um dos tipos de operadores de compara��o
	 * 
	 * @return verdadeiro ou falso confirmando a verifica��o acima
	 */
	private boolean FF1OpRel() {
		byte kind = currentToken.kind;
		return kind == PascalToken.tLessTh || kind == PascalToken.tGreatTh || kind == PascalToken.tLessThEq
				|| kind == PascalToken.tGreatThEq || kind == PascalToken.tEquals || kind == PascalToken.tUnLike;
	}

	/**
	 * Verifica sintaticamente o buffer para reconhecer um operador de compara��o
	 * 
	 * @return um objeto OpRel
	 */
	private OpRel parseOpRel() throws MultiParserException {
		OpRel ret;
		switch (currentToken.kind) {
		case PascalToken.tLessTh:
			ret = new OpRel(currentToken.line, currentToken.column);
			acceptIt();
			return ret.setOp(OpRel.tLessT);
		case PascalToken.tGreatTh:
			ret = new OpRel(currentToken.line, currentToken.column);
			acceptIt();
			return ret.setOp(OpRel.tGreatT);
		case PascalToken.tLessThEq:
			ret = new OpRel(currentToken.line, currentToken.column);
			acceptIt();
			return ret.setOp(OpRel.tLessEqT);
		case PascalToken.tGreatThEq:
			ret = new OpRel(currentToken.line, currentToken.column);
			acceptIt();
			return ret.setOp(OpRel.tGreatEqT);
		case PascalToken.tEquals:
			ret = new OpRel(currentToken.line, currentToken.column);
			acceptIt();
			return ret.setOp(OpRel.tEq);
		case PascalToken.tUnLike:
			ret = new OpRel(currentToken.line, currentToken.column);
			acceptIt();
			return ret.setOp(OpRel.tNotEq);
		default:
			mpe.add(new ParserException(currentToken,
					"an operator like : " + getSpelling(PascalToken.tLessTh) + " ' , ' "
							+ getSpelling(PascalToken.tGreatTh) + " ' , ' " + getSpelling(PascalToken.tLessThEq)
							+ " ' , ' " + getSpelling(PascalToken.tGreatThEq) + " ' , ' "
							+ getSpelling(PascalToken.tEquals) + " ' , ' " + getSpelling(PascalToken.tUnLike)));
			ret = new OpRel(currentToken.line, currentToken.column);
			acceptIt();
			if (FF1OpRel()) {
				return parseOpRel();
			} else {
				throw this.mpe;
			}
		}
	}

	/**
	 * Verifica sintaticamente o buffer para reconhecer um token esperado. Caso n�o
	 * for um token com o kind especificado, causar� um erro sint�tico
	 * 
	 * @param expectedKind - C�digo do token esperado
	 * @return um objeto Token
	 */
	private Token accept(byte expectedKind) throws MultiParserException {
		if (currentToken.kind == expectedKind) {
			Token ret = currentToken;
			currentToken = scanner.scan();
			return ret;
		} else {
			mpe.add(new ParserException(currentToken, getSpelling(expectedKind)));
			currentToken = scanner.scan();
			if (currentToken.kind == expectedKind) {
				Token ret2 = currentToken;
				currentToken = scanner.scan();
				return ret2;
			} else {
				throw this.mpe;
			}
		}
	}

	/**
	 * Aceita e transpassa incondicionalmente o currentToken confiando numa
	 * pr�verifica��o
	 * 
	 * @return objeto Token reconhecido
	 */
	private Token acceptIt() {
		Token ret = currentToken;
		currentToken = scanner.scan();
		return ret;
	}

	/**
	 * limpeza de c�digo, criando um atalho para uma chamada de m�todo interna dos
	 * atributos
	 * 
	 * @param kind
	 * @return speeling of kind
	 * @see PatternToken
	 */
	private String getSpelling(int kind) {
		return this.scanner.getPt().getSpelling(kind);
	}

}
