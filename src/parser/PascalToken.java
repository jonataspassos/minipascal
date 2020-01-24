package parser;

/**
 * Especifica todos os kinds dos tokens aceitos pela linguagem. Este arquivo
 * está em sincronia com o arquivo *.tkn recebido pela linguagem. Qualquer
 * alteração no arquivo deverá ser atualizada neste arquivo, observando a
 * natureza da específica alteração
 */
public class PascalToken {
	public static final byte tIdentifier = 10, tAnd = 11, tArray = 12, tBegin = 13, tBoolean = 14, tDo = 15, tElse = 16,
			tEnd = 17, tFalse = 18, tIf = 19, tInteger = 20, tOf = 21, tOr = 22, tProgram = 23, tReal = 24, tThen = 25,
			tTrue = 26, tVar = 27, tWhile = 28, // Palavras reservadas
			// Símbolos
			tDash = 41, // -
			tLParen = 42, // (
			tRParen = 43, // )
			tAsterisk = 44, // *
			tComma = 45, // ,
			tPeriod = 46, // .
			tFwdSlash = 47, // /
			tColon = 48, // :
			tAssgn = 49, // Assignment :=
			tSemiColon = 50, // ;
			tLBracket = 51, // [
			tRBracket = 52, // ]
			tTilde = 53, // ~
			tPlusSgn = 54, // +
			tLessTh = 55, // Less Than <
			tLessThEq = 56, // Less Than or equals <=
			tUnLike = 57, // <>
			tEquals = 58, // =
			tGreatTh = 59, // Great Than >
			tGreatThEq = 70, // Great Than or equals >=
			tIntegerLit = 80, tFloatLit = 90;

}
