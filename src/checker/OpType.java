package checker;

import ast.OpAd;
import ast.OpMul;
import ast.OpRel;
import ast.PrimitiveType;
import ast.Type;
import coder.Instructions;

/**
 * Esta classe armazena estaticamente uma tabela com todos os tipos válidos de
 * operação, juntamente com seus códigos objetos para realizá-las
 */
public abstract class OpType {
	/**
	 * Operador
	 */
	public final char op;
	/**
	 * Tipos dos operandos
	 */
	public final Type[] operands;
	/**
	 * Tipo do retorno
	 */
	public final Type ret;
	/**
	 * Objeto contendo as instruções implementadas do código objeto
	 */
	private static Instructions intructions;

	/**
	 * Configura o objeto das instruções
	 */
	public static void setInstructions(Instructions instructions) {
		OpType.intructions = instructions;
	}

	/**
	 * Salva uma instancia genérica cos tipos que serão utilizados
	 */
	public final static Type bool = new PrimitiveType(0, 0).setType(PrimitiveType.tBoolean),
			integ = new PrimitiveType(0, 0).setType(PrimitiveType.tInt),
			real = new PrimitiveType(0, 0).setType(PrimitiveType.tReal), none = null;

	/**
	 * Salva as combinações possíveis de operandos
	 */
	private final static Type[][] operandsType = { // Combinations of operands
			{ bool, bool }, { bool, integ }, { bool, real }, // bool with
			{ integ, bool }, { integ, integ }, { integ, real }, // integ with
			{ real, bool }, { real, integ }, { real, real }, // real with
			{ bool }, { integ }, { real }, // Unary operations for
			{ none, none }, { none } // Not recognizable
	};

	/**
	 * Salva os tipos de operações com seus respectivos tipos de operandos e retorno
	 */
	public final static OpType[] table = { // Operations Types
			new OpType(OpAd.tPlus, operandsType[4], integ) { // id:00 0 := 0+0

				@Override
				public String sample() {
					return this.operands[0] + "+" + this.operands[1] + "->" + this.ret + ": 0 := 0+0";
				}

				@Override
				public String description() {
					return "sum the integers resulting in an integer";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "add", "add");
				}
			}, new OpType(OpAd.tPlus, operandsType[5], real) { // id:01 0.0 := 0+0.0

				@Override
				public String sample() {
					return "" + castS(0) + "+" + this.operands[1] + "->" + this.ret + ": 0.0 := 0+0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and " + OpType.table[3];
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return firstToReal(staticLink) + ((OpType) OpType.table[3]).instruction(staticLink);
				}

			}, new OpType(OpAd.tPlus, operandsType[7], real) {// id:02 0.0 := 0.0+0

				@Override
				public String sample() {
					return this.operands[0] + "+" + castS(1) + "->" + this.ret + ":0.0 := 0.0+0";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and "
							+ OpType.table[3].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return secondToReal(staticLink) + ((OpType) OpType.table[3]).instruction(staticLink);
				}

			}, new OpType(OpAd.tPlus, operandsType[8], real) {// id:03 0.0 := 0.0+0.0

				@Override
				public String sample() {
					return this.operands[0] + "+" + this.operands[1] + "->" + this.ret + ":0.0 := 0.0+0.0";
				}

				@Override
				public String description() {
					return "sum the real numbers resulting in a real";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return OpType.intructions.getCall(staticLink, 0, "addR", "add to real");
				}

			}, new OpType(OpAd.tMinus, operandsType[4], integ) {// id:04 0 := 0-0

				@Override
				public String sample() {
					return this.operands[0] + "-" + this.operands[1] + "->" + this.ret + ":0 := 0-0";
				}

				@Override
				public String description() {
					return "subtracts integers resulting in an integer";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return OpType.intructions.getCall(staticLink, 0, "sub", "sub");
				}

			}, new OpType(OpAd.tMinus, operandsType[5], real) {// id:05 0.0 := 0-0.0

				@Override
				public String sample() {
					return "" + castS(0) + "-" + this.operands[1] + "->" + this.ret + ":0.0 := 0-0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and "
							+ OpType.table[7].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return firstToReal(staticLink) + ((OpType) OpType.table[7]).instruction(staticLink);
				}

			}, new OpType(OpAd.tMinus, operandsType[7], real) {// id:06 0.0 := 0.0-0

				@Override
				public String sample() {
					return this.operands[0] + "-" + castS(1) + "->" + this.ret + ":0.0 := 0.0-0";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and "
							+ OpType.table[7].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return secondToReal(staticLink) + ((OpType) OpType.table[7]).instruction(staticLink);
				}

			}, new OpType(OpAd.tMinus, operandsType[8], real) {// id:07 0.0 := 0.0-0.0

				@Override
				public String sample() {
					return this.operands[0] + "-" + this.operands[1] + "->" + this.ret + ":0.0 := 0.0-0.0";
				}

				@Override
				public String description() {
					return "subtracts the real numbers resulting in a real";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "subR", "sub to real");
				}

			}, new OpType(OpAd.tOr, operandsType[0], bool) {// id:08 T := T or T

				@Override
				public String sample() {
					return this.operands[0] + " or " + this.operands[1] + "->" + this.ret + ":T := T or T";
				}

				@Override
				public String description() {
					return "query boolean operands until the first return true; otherwise return false";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return OpType.intructions.getCall(staticLink, 0, "or", "or");
				}

			}, new OpType(OpMul.tProduct, operandsType[4], integ) {// id:09 0 := 0*0

				@Override
				public String sample() {
					return this.operands[0] + "*" + this.operands[1] + "->" + this.ret + ":0 := 0*0";
				}

				@Override
				public String description() {
					return "multiply the integers numbers resulting in a integer";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "mult", "mult");
				}

			}, new OpType(OpMul.tProduct, operandsType[5], real) {// id:10 0.0 := 0*0.0

				@Override
				public String sample() {
					return "" + castS(0) + "*" + this.operands[1] + "->" + this.ret + ":0.0 := 0*0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and "
							+ OpType.table[12].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return firstToReal(staticLink) + ((OpType) OpType.table[12]).instruction(staticLink);
				}

			}, new OpType(OpMul.tProduct, operandsType[7], real) {// id:11 0.0 := 0.0*0

				@Override
				public String sample() {
					return this.operands[0] + "*" + castS(1) + "->" + this.ret + ":0.0 := 0.0*0";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and "
							+ OpType.table[12].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return secondToReal(staticLink) + ((OpType) OpType.table[12]).instruction(staticLink);
				}

			}, new OpType(OpMul.tProduct, operandsType[8], real) {// id:12 0.0 := 0.0*0.0

				@Override
				public String sample() {
					return this.operands[0] + "*" + this.operands[1] + "->" + this.ret + ":0.0 := 0.0*0.0";
				}

				@Override
				public String description() {
					return "multiply the real numbers resulting in a real";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "multR", "mult to real");
				}

			}, new OpType(OpMul.tDivision, operandsType[4], integ) {// id:13 0 := 0/1

				@Override
				public String sample() {
					return this.operands[0] + "/" + this.operands[1] + "->" + this.ret + ":0 := 0/1";
				}

				@Override
				public String description() {
					return "divide the first integer by second integer resulting in a integer (truncates the rest) (Runtime Error if second equals 0)";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					String labelOk = OpType.intructions.createLabel();
					String validation = OpType.intructions.getLoad((byte) PrimitiveType.sInt, -PrimitiveType.sInt, "ST",
							"copy last") + OpType.intructions.getLoadL(0, "")
							+ OpType.intructions.getCall(staticLink, 0, "eq", "eq")
							+ OpType.intructions.getJumpIf((byte) 0, 0, labelOk, "valid division")
							+ OpType.intructions.getHalt("invalid division")
							+ OpType.intructions.applyLabel(labelOk, null);
					;

					return validation + OpType.intructions.getCall(staticLink, 0, "div", "div");
				}

			}, new OpType(OpMul.tDivision, operandsType[5], real) {// id:14 0.0 := 0/1.0

				@Override
				public String sample() {
					return "" + castS(0) + "/" + this.operands[1] + "->" + this.ret + ":0.0 := 0/1.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and "
							+ OpType.table[16].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return firstToReal(staticLink) + ((OpType) OpType.table[16]).instruction(staticLink);
				}

			}, new OpType(OpMul.tDivision, operandsType[7], real) {// id:15 0.0 := 0.0/1

				@Override
				public String sample() {
					return this.operands[0] + "/" + castS(1) + "->" + this.ret + ":0.0 := 0.0/1";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and "
							+ OpType.table[16].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return secondToReal(staticLink) + ((OpType) OpType.table[16]).instruction(staticLink);
				}

			}, new OpType(OpMul.tDivision, operandsType[8], real) {// id:16 0.0 := 0.0/1.0

				@Override
				public String sample() {
					return this.operands[0] + "/" + this.operands[1] + "->" + this.ret + ":0.0 := 0.0/1.0";
				}

				@Override
				public String description() {
					return "divide the first real by second real resulting in a real(ressults in NaN if second equals 0)";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					String labelOk = OpType.intructions.createLabel();
					String validation = OpType.intructions.getLoad((byte) PrimitiveType.sReal, -PrimitiveType.sReal,
							"ST", "copy last") + OpType.intructions.getLoadL(0.0, "")
							+ OpType.intructions.getCall(staticLink, 0, "eq", "eq")
							+ OpType.intructions.getJumpIf((byte) 0, 0, labelOk, "valid division")
							+ OpType.intructions.getHalt("invalid division")
							+ OpType.intructions.applyLabel(labelOk, null);
					;

					return validation + OpType.intructions.getCall(staticLink, 0, "divR", "div to real");
				}

			}, new OpType(OpMul.tAnd, operandsType[0], bool) {// id:17 T := T and T

				@Override
				public String sample() {
					return this.operands[0] + " and " + this.operands[1] + "->" + this.ret + ":T := T and T";
				}

				@Override
				public String description() {
					return "query boolean operands until the first returns false; otherwise returns true";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "and", "and");
				}

			}, new OpType(OpRel.tEq, operandsType[0], bool) {// id:18 F := T = F

				@Override
				public String sample() {
					return this.operands[0] + "=" + this.operands[1] + "->" + this.ret + ":F := T = F";
				}

				@Override
				public String description() {
					return "compares boolean operands and returns true if they are equal or false otherwise";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "eq", "eq");
				}

			}, new OpType(OpRel.tEq, operandsType[4], bool) {// id:19 T := 0 = 0

				@Override
				public String sample() {
					return this.operands[0] + "=" + this.operands[1] + "->" + this.ret + ":T := 0 = 0";
				}

				@Override
				public String description() {
					return "compares integer operands and returns true if they are equal or false otherwise";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "eq", "eq");
				}

			}, new OpType(OpRel.tEq, operandsType[5], bool) {// id:20 T := 0 = 0.0

				@Override
				public String sample() {
					return "" + castS(0) + "=" + this.operands[1] + "->" + this.ret + ":T := 0 = 0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and "
							+ OpType.table[22].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return firstToReal(staticLink) + ((OpType) OpType.table[22]).instruction(staticLink);
				}

			}, new OpType(OpRel.tEq, operandsType[7], bool) {// id:21 T := 0.0 = 0

				@Override
				public String sample() {
					return this.operands[0] + "=" + castS(1) + "->" + this.ret + ":T := 0.0 = 0";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and "
							+ OpType.table[22].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return secondToReal(staticLink) + ((OpType) OpType.table[22]).instruction(staticLink);
				}

			}, new OpType(OpRel.tEq, operandsType[8], bool) {// id:22 T := 0.0 = 0.0

				@Override
				public String sample() {
					return this.operands[0] + "=" + this.operands[1] + "->" + this.ret + ":T := 0.0 = 0.0";
				}

				@Override
				public String description() {
					return "compares real operands and returns true if they are equal or false otherwise";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return OpType.intructions.getCall(staticLink, 0, "eq", "eq");
				}

			}, new OpType(OpRel.tNotEq, operandsType[0], bool) {// id:23 F := T <> F

				@Override
				public String sample() {
					return this.operands[0] + "<>" + this.operands[1] + "->" + this.ret + ":F := T <> F";
				}

				@Override
				public String description() {
					return "compares boolean operands and returns false if they are equal or true otherwise";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "ne", "ne");
				}

			}, new OpType(OpRel.tNotEq, operandsType[4], bool) {// id:24 T := 0 <> 0

				@Override
				public String sample() {
					return this.operands[0] + "<>" + this.operands[1] + "->" + this.ret + ":T := 0 <> 0";
				}

				@Override
				public String description() {
					return "compares integer operands and returns false if they are equal or true otherwise";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "ne", "ne");
				}

			}, new OpType(OpRel.tNotEq, operandsType[5], bool) {// id:25 T := 0 <> 0.0

				@Override
				public String sample() {
					return "" + castS(0) + "<>" + this.operands[1] + "->" + this.ret + ":T := 0 <> 0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and "
							+ OpType.table[27].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return firstToReal(staticLink) + ((OpType) OpType.table[27]).instruction(staticLink);
				}

			}, new OpType(OpRel.tNotEq, operandsType[7], bool) {// id:26 T := 0.0 <> 0

				@Override
				public String sample() {
					return this.operands[0] + "-" + castS(1) + "->" + this.ret + ":T := 0.0 <> 0";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and "
							+ OpType.table[27].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return secondToReal(staticLink) + ((OpType) OpType.table[27]).instruction(staticLink);
				}

			}, new OpType(OpRel.tNotEq, operandsType[8], bool) {// id:27 T := 0.0 <> 0.0

				@Override
				public String sample() {
					return this.operands[0] + "<>" + this.operands[1] + "->" + this.ret + ":T := 0.0 <> 0.0";
				}

				@Override
				public String description() {
					return "compares real operands and returns false if they are equal or true otherwise";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "ne", "ne");
				}

			}, new OpType(OpRel.tGreatT, operandsType[4], bool) {// id:28 F := 0 > 0

				@Override
				public String sample() {
					return this.operands[0] + ">" + this.operands[1] + "->" + this.ret + ":F := 0 > 0";
				}

				@Override
				public String description() {
					return "compares integer operands and returns true if first is greater than second or false otherwise";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "gt", "gt");
				}

			}, new OpType(OpRel.tGreatT, operandsType[5], bool) {// id:29 F := 0 > 0.0

				@Override
				public String sample() {
					return "" + castS(0) + ">" + this.operands[1] + "->" + this.ret + ":F := 0 > 0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and "
							+ OpType.table[31].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return firstToReal(staticLink) + ((OpType) OpType.table[31]).instruction(staticLink);
				}

			}, new OpType(OpRel.tGreatT, operandsType[7], bool) {// id:30 F := 0.0 > 0

				@Override
				public String sample() {
					return this.operands[0] + ">" + castS(1) + "->" + this.ret + ":F := 0.0 > 0";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and "
							+ OpType.table[31].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return secondToReal(staticLink) + ((OpType) OpType.table[31]).instruction(staticLink);
				}

			}, new OpType(OpRel.tGreatT, operandsType[8], bool) {// id:31 F := 0.0 > 0.0

				@Override
				public String sample() {
					return this.operands[0] + ">" + this.operands[1] + "->" + this.ret + ":F := 0.0 > 0.0";
				}

				@Override
				public String description() {
					return "compares real operands and returns true if first is greater than second or false otherwise";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "gtR", "gt to real");
				}

			}, new OpType(OpRel.tLessT, operandsType[4], bool) {// id:32 F := 0 < 0

				@Override
				public String sample() {
					return this.operands[0] + "<" + this.operands[1] + "->" + this.ret + ":F := 0 < 0";
				}

				@Override
				public String description() {
					return "compares integer operands and returns true if first is less than second or false otherwise";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "lt", "lt");
				}

			}, new OpType(OpRel.tLessT, operandsType[5], bool) {// id:33 F := 0 < 0.0

				@Override
				public String sample() {
					return "" + castS(0) + "<" + this.operands[1] + "->" + this.ret + ":F := 0 < 0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and "
							+ OpType.table[35].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return firstToReal(staticLink) + ((OpType) OpType.table[35]).instruction(staticLink);
				}

			}, new OpType(OpRel.tLessT, operandsType[7], bool) {// id:34 F := 0.0 < 0

				@Override
				public String sample() {
					return this.operands[0] + "<" + castS(1) + "->" + this.ret + ":F := 0.0 < 0";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and "
							+ OpType.table[35].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return secondToReal(staticLink) + ((OpType) OpType.table[35]).instruction(staticLink);
				}

			}, new OpType(OpRel.tLessT, operandsType[8], bool) {// id:35 F := 0.0 < 0.0

				@Override
				public String sample() {
					return this.operands[0] + "-" + this.operands[1] + "->" + this.ret + ":F := 0.0 < 0.0";
				}

				@Override
				public String description() {
					return "compares real operands and returns true if first is less than second or false otherwise";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "ltR", "lt to real");
				}

			}, new OpType(OpRel.tGreatEqT, operandsType[4], bool) {// id:36 T := 0 >= 0

				@Override
				public String sample() {
					return this.operands[0] + ">=" + this.operands[1] + "->" + this.ret + ":T := 0 >= 0";
				}

				@Override
				public String description() {
					return "compares integer operands and returns true if first is equals or greater than second or false otherwise";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "ge", "ge");
				}

			}, new OpType(OpRel.tGreatEqT, operandsType[5], bool) {// id:37 T := 0 >= 0.0

				@Override
				public String sample() {
					return "" + castS(0) + ">=" + this.operands[1] + "->" + this.ret + ":T := 0 >= 0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and "
							+ OpType.table[39].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return firstToReal(staticLink) + ((OpType) OpType.table[39]).instruction(staticLink);
				}

			}, new OpType(OpRel.tGreatEqT, operandsType[7], bool) {// id:38 T := 0.0 >= 0

				@Override
				public String sample() {
					return this.operands[0] + ">=" + castS(1) + "->" + this.ret + ":T := 0.0 >= 0";
				}

				@Override
				public String description() {
					return "make a conversion on the secon operand (integer to real) and "
							+ OpType.table[39].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return secondToReal(staticLink) + ((OpType) OpType.table[39]).instruction(staticLink);
				}

			}, new OpType(OpRel.tGreatEqT, operandsType[8], bool) {// id:39 T := 0.0 >= 0.0

				@Override
				public String sample() {
					return this.operands[0] + ">=" + this.operands[1] + "->" + this.ret + ":T := 0.0 >= 0.0";
				}

				@Override
				public String description() {
					return "compares real operands and returns true if first is equals or greater than second or false otherwise";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "geR", "ge to real");
				}

			}, new OpType(OpRel.tLessEqT, operandsType[4], bool) {// id:40 T := 0 <= 0

				@Override
				public String sample() {
					return this.operands[0] + "<=" + this.operands[1] + "->" + this.ret + ":T := 0 <= 0";
				}

				@Override
				public String description() {
					return "compares integer operands and returns true if first is equals or less than second or false otherwise";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "le", "le");
				}

			}, new OpType(OpRel.tLessEqT, operandsType[5], bool) {// id:41 T := 0 <= 0.0

				@Override
				public String sample() {
					return "" + castS(0) + "<=" + this.operands[1] + "->" + this.ret + ":T := 0 <= 0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and "
							+ OpType.table[43].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return firstToReal(staticLink) + ((OpType) OpType.table[43]).instruction(staticLink);
				}

			}, new OpType(OpRel.tLessEqT, operandsType[7], bool) {// id:42 T := 0.0 <= 0

				@Override
				public String sample() {
					return this.operands[0] + "<=" + castS(1) + ")->" + this.ret + ":T := 0.0 <= 0";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and "
							+ OpType.table[43].description();
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}

					return secondToReal(staticLink) + ((OpType) OpType.table[43]).instruction(staticLink);
				}

			}, new OpType(OpRel.tLessEqT, operandsType[8], bool) {// id:43 T := 0.0 <= 0.0

				@Override
				public String sample() {
					return this.operands[0] + "<=" + this.operands[1] + "->" + this.ret + ":T := 0.0 <= 0.0";
				}

				@Override
				public String description() {
					return "compares real operands and returns true if first is equals or less than second or false otherwise";
				}

				@Override
				public String instruction(String staticLink) {
					if (OpType.intructions == null) {
						return "ERRO\n";
					}
					return OpType.intructions.getCall(staticLink, 0, "leR", "le to real");
				}

			} };

	/**
	 * Salva os tipos de atribuições com seus respectivos tipos de objetos
	 * envolvidos(variavel e expressão)
	 */
	public final static OpType[] assigmentTypes = { new OpType('~', operandsType[7], real) {

		@Override
		public String sample() {
			return "varReal := " + castS(1);
		}

		@Override
		public String instruction(String staticLink) {
			return secondToReal(staticLink);
		}

		@Override
		public String description() {
			return "Convert to Real before do the assigment";
		}
	}, new OpType(':', operandsType[0], bool) {

		@Override
		public String sample() {
			return "varBoolean := boolean";
		}

		@Override
		public String instruction(String staticLink) {
			return "";
		}

		@Override
		public String description() {
			return "Only make de assigment";
		}
	}, new OpType(':', operandsType[4], integ) {

		@Override
		public String sample() {
			return "varInteger := integer";
		}

		@Override
		public String instruction(String staticLink) {
			return "";
		}

		@Override
		public String description() {
			return "Only make de assigment";
		}
	}, new OpType(':', operandsType[8], real) {

		@Override
		public String sample() {
			return "varReal := real";
		}

		@Override
		public String instruction(String staticLink) {
			return "";
		}

		@Override
		public String description() {
			return "Only make de assigment";
		}
	}

	};

	/**
	 * Busca a combinação de operandos compativel com o parâmetro
	 * 
	 * @param operands - tipos dos operandos
	 */
	private static Type[] search(Type[] operands) {
		for (Type[] i : OpType.operandsType) {
			if (i.length == operands.length) {
				boolean thereUnequal = false;
				for (int j = 0; j < i.length; j++) {
					if (operands[j] == null || !operands[j].equals(i[j]))
						thereUnequal = true;
				}
				if (!thereUnequal)
					return i;
			}
		}
		return null;
	}

	/**
	 * Busca a operação compativel com o operador e os tipos dos operandos,
	 * retornando a operação exata com seu tipo de retorno especificado
	 */
	public static OpType search(char op, Type[] operands) {
		operands = search(operands);
		if (operands != null)
			for (OpType i : OpType.table) {
				if (op == i.op && operands == i.operands)
					return i;
			}
		return new OpType(op, operandsType[12], null) {

			@Override
			public String sample() {
				return "Not recognizable";
			}

			@Override
			public String instruction(String staticLink) {
				if (OpType.intructions == null) {
					return "ERRO\n";
				}
				return OpType.intructions.getHalt(this.sample());
			}

			@Override
			public String description() {
				return "Not recognizable";
			}
		};
	}

	/**
	 * Busca a atribuição compativel com os tipos dos objetos envolvidos(expressão e
	 * variavel), retornando a operação exata
	 */
	public static OpType searchAss(Type[] operands) {
		operands = search(operands);
		if (operands != null)
			for (OpType i : OpType.assigmentTypes) {
				if (operands == i.operands)
					return i;
			}
		return new OpType(':', operands, null) {

			@Override
			public String sample() {
				return "Not recognizable";
			}

			@Override
			public String instruction(String staticLink) {
				if (OpType.intructions == null) {
					return "ERRO\n";
				}
				return OpType.intructions.getHalt(this.sample());
			}

			@Override
			public String description() {
				return "Not recognizable";
			}
		};
	}

	/**
	 * Representa uma conversao de operando, para se tornar compatível com o outro
	 * operando
	 */
	protected String castS(int index) {
		return "((" + this.operands[index == 1 ? 0 : 1] + ")" + this.operands[index] + ")";
	}

	/**
	 * Representa uma conversão do primeiro operando, utilizando as instruções de
	 * máquina
	 */
	protected String firstToReal(String staticLink) {
		return OpType.intructions.getLoad((byte) PrimitiveType.sInt, -PrimitiveType.sInt - PrimitiveType.sReal, "ST",
				"copy integer(first operand)")
				+ OpType.intructions.getCall(staticLink, 0, "toReal", "convert to real")
				+ OpType.intructions.getLoad((byte) PrimitiveType.sReal, -PrimitiveType.sReal - PrimitiveType.sReal,
						"ST", "copy real(second operand)")
				+ OpType.intructions.getPop((byte) (2 * PrimitiveType.sReal), PrimitiveType.sInt + PrimitiveType.sReal,
						"update operands");
	}

	/**
	 * Representa uma conversão do segundo operando, utilizando as instruções de
	 * máquina
	 */
	protected String secondToReal(String staticLink) {
		return OpType.intructions.getCall(staticLink, 0, "toReal", "convert to real");
	}

	/**
	 * Constroi o tipo de operação
	 */
	public OpType(char op, Type[] operands, Type ret) {
		super();
		this.op = op;
		this.operands = operands;
		this.ret = ret;
	}

	/**
	 * Retorna um exemplo desta operação gernérico (para fins didáticos)
	 */
	public abstract String sample();

	/**
	 * Retorna a descrição da operação (para fins didáticos)
	 */
	public abstract String description();

	/**
	 * Retorna o conjunto de intruções que precisam ser feitos para realizar esta
	 * operação
	 */
	public abstract String instruction(String staticLink);

	@Override
	public String toString() {
		return this.sample();
	}

}
