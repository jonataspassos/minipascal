package parser;

import ast.*;
import scanner.Scanner;
import scanner.ScannerException;
import scanner.Token;

public class Parser {
	private Token currentToken;
	private Scanner scanner;

	public static void main(String[] args) throws ParserException {

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

	public Parser(String pathTkn, String pathSrc) {
		this.scanner = new Scanner(pathTkn, pathSrc);
	}

	public AST parse() throws ParserException {
		AST tree;
		acceptIt();
		tree = parseProgram();
		if (currentToken.kind != Token.endOfFileCode) {
			throw new ParserException(currentToken, "EOF");
		}
		return tree;
	}

	private AssignmentCommand parseAssignmentCommand() throws ParserException {
		AssignmentCommand command = new AssignmentCommand(currentToken.line, currentToken.column);

		command.setVariable(parseVariable());// <variable>
		accept(PascalToken.tAssgn); // :=
		command.setExpression(parseExpression()); // <expression>

		return command;
	}

	private ConditionalCommand parseConditionalCommand() throws ParserException {
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

	private IterativeCommand parseIterativeCommand() throws ParserException {
		IterativeCommand command = new IterativeCommand(currentToken.line, currentToken.column);
		accept(PascalToken.tWhile);// while
		command.setExpression(parseExpression());// <expression>
		accept(PascalToken.tDo);// do
		command.setCommand(parseCommand());// <command>

		return command;
	}

	private boolean FF1Command() {// First or Follow 1
		byte kind = currentToken.kind;
		return kind == PascalToken.tIdentifier || kind == PascalToken.tIf || kind == PascalToken.tWhile
				|| kind == PascalToken.tBegin;
	}

	private MultiCommand parseMultiCommand() throws ParserException {
		MultiCommand command = new MultiCommand(currentToken.line, currentToken.column);

		accept(PascalToken.tBegin);// begin
		while (FF1Command()) {// (
			command.addCommand(parseCommand());// <command>
			accept(PascalToken.tSemiColon);// ;
		} // )*
		accept(PascalToken.tEnd);// end
		return command;
	}

	private Command parseCommand() throws ParserException {
		switch (currentToken.kind) {
		case PascalToken.tIdentifier: // atribuição
			return parseAssignmentCommand();

		case PascalToken.tIf: // condicional
			return parseConditionalCommand();

		case PascalToken.tWhile: // iterativo
			return parseIterativeCommand();

		case PascalToken.tBegin: // multi-command
			return parseMultiCommand();

		default:
			throw new ParserException(currentToken,
					"um comando iniciado por: " + getSpelling(PascalToken.tIdentifier) + " ou \n"
							+ getSpelling(PascalToken.tIf) + " ou \n" + getSpelling(PascalToken.tWhile) + " ou \n"
							+ getSpelling(PascalToken.tBegin));
		}
	}

	private boolean FF1Declaration() {
		return currentToken.kind == PascalToken.tVar;
	}

	private Declaration parseDeclaration() throws ParserException {
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

	private Expression parseExpression() throws ParserException {
		Expression expression = new Expression(currentToken.line, currentToken.column);
		expression.setA(parseSimpleExpression(), 0);// <simple-expression>
		if (FF1OpRel()) {// (
			expression.setOp(parseOpRel());// <op-rel>
			expression.setA(parseSimpleExpression(), 1); // <simple-expression>
		} // | vazio )

		return expression;
	}

	private SimpleExpression parseSimpleExpression() throws ParserException {
		SimpleExpression expression = new SimpleExpression(currentToken.line, currentToken.column);
		expression.addA(parseTerm());// <term>
		while (FF1OpAd()) {// (
			expression.addOp(parseOpAd());// <op-ad>
			expression.addA(parseTerm());// <term>
		} // )*

		return expression;
	}

	private Term parseTerm() throws ParserException {
		Term term = new Term(currentToken.line, currentToken.column);
		term.addA(parseFactor());// <factor>
		while (FF1OpMul()) {// (
			term.addOp(parseOpMul());// <op-mul>
			term.addA(parseFactor());// <factor>
		} // )*

		return term;
	}

	private Factor parseFactor() throws ParserException {
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
			throw new ParserException(currentToken,
					"um Fator como: " + getSpelling(PascalToken.tIdentifier) + " ou \n"
							+ getSpelling(PascalToken.tFalse) + " ou \n" + getSpelling(PascalToken.tTrue) + " ou \n"
							+ getSpelling(PascalToken.tIntegerLit) + " ou \n" + getSpelling(PascalToken.tFloatLit)
							+ " ou \n" + getSpelling(PascalToken.tLParen) + "..." + getSpelling(PascalToken.tRParen));
		}
	}

	private Program parseProgram() throws ParserException {
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

	private Type parseType() throws ParserException {
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
			throw new ParserException(currentToken,
					" um tipo como: " + getSpelling(PascalToken.tInteger) + " ou \n" + getSpelling(PascalToken.tReal)
							+ " ou \n" + getSpelling(PascalToken.tBoolean) + " ou \n" + getSpelling(PascalToken.tArray)
							+ "[... ~ ...] of ...");
		}
		return type;
	}

	private Variable parseVariable() throws ParserException {
		Variable variable = new Variable(currentToken.line, currentToken.column);
		variable.setId(accept(PascalToken.tIdentifier).spelling); // <id>
		while (currentToken.kind == PascalToken.tLBracket) {// (
			acceptIt(); // "["
			variable.addIndexer(parseExpression());// <expression>
			accept(PascalToken.tRBracket);// "]"
		} // )*

		return variable;
	}

	private boolean FF1OpAd() {
		byte kind = currentToken.kind;
		return kind == PascalToken.tPlusSgn || kind == PascalToken.tDash || kind == PascalToken.tOr;
	}

	private OpAd parseOpAd() throws ParserException {
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
			throw new ParserException(currentToken, "Um operador como: " + getSpelling(PascalToken.tPlusSgn) + " ou \n"
					+ getSpelling(PascalToken.tDash) + " ou \n" + getSpelling(PascalToken.tOr));
		}
	}

	private boolean FF1OpMul() {
		byte kind = currentToken.kind;
		return kind == PascalToken.tAsterisk || kind == PascalToken.tFwdSlash || kind == PascalToken.tAnd;
	}

	private OpMul parseOpMul() throws ParserException {
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
			throw new ParserException(currentToken, "Um operador como: " + getSpelling(PascalToken.tAsterisk) + " ou \n"
					+ getSpelling(PascalToken.tFwdSlash) + " ou \n" + getSpelling(PascalToken.tAnd));
		}
	}

	private boolean FF1OpRel() {
		byte kind = currentToken.kind;
		return kind == PascalToken.tLessTh || kind == PascalToken.tGreatTh || kind == PascalToken.tLessThEq
				|| kind == PascalToken.tGreatThEq || kind == PascalToken.tEquals || kind == PascalToken.tUnLike;
	}

	private OpRel parseOpRel() throws ParserException {
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
			throw new ParserException(currentToken,
					"um operador como: " + getSpelling(PascalToken.tLessTh) + " ou \n"
							+ getSpelling(PascalToken.tGreatTh) + " ou \n" + getSpelling(PascalToken.tLessThEq)
							+ " ou \n" + getSpelling(PascalToken.tGreatThEq) + " ou \n"
							+ getSpelling(PascalToken.tEquals) + " ou \n" + getSpelling(PascalToken.tUnLike));
		}
	}

	private Token accept(byte expectedKind) throws ParserException {
		if (currentToken.kind == expectedKind) {
			Token ret = currentToken;
			currentToken = scanner.scan();
			return ret;
		} else {
			throw new ParserException(currentToken, getSpelling(expectedKind));
		}
	}

	private Token acceptIt() {
		Token ret = currentToken;
		currentToken = scanner.scan();
		return ret;
	}

	private String getSpelling(int kind) {
		return this.scanner.getPt().getSpelling(kind);
	}

}
