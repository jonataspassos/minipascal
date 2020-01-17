package checker;

import ast.OpAd;
import ast.OpMul;
import ast.OpRel;
import ast.PrimitiveType;
import ast.Type;

public abstract class OpType {
	public final char op;
	public final Type[] operands;
	public final Type ret;

	public final static Type bool = new PrimitiveType(0, 0).setType(PrimitiveType.tBoolean),
			integ = new PrimitiveType(0, 0).setType(PrimitiveType.tInt),
			real = new PrimitiveType(0, 0).setType(PrimitiveType.tReal);

	private final static Type[][] operandsType = { // Combinations of operands
			{ bool, bool }, { bool, integ }, { bool, real }, // bool with
			{ integ, bool }, { integ, integ }, { integ, real }, // integ with
			{ real, bool }, { real, integ }, { real, real }, // real with
			{ bool }, { integ }, { real }// Unary operations for
	};

	public final static OpType[] table = { // Operations Types
			new OpType(OpAd.tPlus, operandsType[4], integ) { // id:00 0 := 0+0

				@Override
				public String sample() {
					return "0 := 0+0";
				}

				@Override
				public String description() {
					return "sum the integers resulting in an integer";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return "";
				}
			}, new OpType(OpAd.tPlus, operandsType[5], real) { // id:01 0.0 := 0+0.0

				@Override
				public String sample() {
					return "0.0 := 0+0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and " + OpType.operandsType[3];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpAd.tPlus, operandsType[7], real) {// id:02 0.0 := 0.0+0

				@Override
				public String sample() {
					return "0.0 := 0.0+0";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and " + OpType.operandsType[3];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpAd.tPlus, operandsType[8], real) {// id:03 0.0 := 0.0+0.0

				@Override
				public String sample() {
					return "0.0 := 0.0+0.0";
				}

				@Override
				public String description() {
					return "sum the real numbers resulting in a real";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpAd.tMinus, operandsType[4], integ) {// id:04 0 := 0-0

				@Override
				public String sample() {
					return "0 := 0-0";
				}

				@Override
				public String description() {
					return "subtracts integers resulting in an integer";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpAd.tMinus, operandsType[5], real) {// id:05 0.0 := 0-0.0

				@Override
				public String sample() {
					return "0.0 := 0-0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and " + OpType.operandsType[7];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpAd.tMinus, operandsType[7], real) {// id:06 0.0 := 0.0-0

				@Override
				public String sample() {
					return "0.0 := 0.0-0";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and " + OpType.operandsType[7];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpAd.tMinus, operandsType[8], real) {// id:07 0.0 := 0.0-0.0

				@Override
				public String sample() {
					return "0.0 := 0.0-0.0";
				}

				@Override
				public String description() {
					return "subtracts the real numbers resulting in a real";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpAd.tOr, operandsType[0], bool) {// id:08 T := T or T

				@Override
				public String sample() {
					return "T := T or T";
				}

				@Override
				public String description() {
					return "query boolean operands until the first return true; otherwise return false";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpMul.tProduct, operandsType[4], integ) {// id:09 0 := 0*0

				@Override
				public String sample() {
					return "0 := 0*0";
				}

				@Override
				public String description() {
					return "multiply the integers numbers resulting in a integer";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpMul.tProduct, operandsType[5], real) {// id:10 0.0 := 0*0.0

				@Override
				public String sample() {
					return "0.0 := 0*0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and " + OpType.operandsType[12];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpMul.tProduct, operandsType[7], real) {// id:11 0.0 := 0.0*0

				@Override
				public String sample() {
					return "0.0 := 0.0*0";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and " + OpType.operandsType[12];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpMul.tProduct, operandsType[8], real) {// id:12 0.0 := 0.0*0.0

				@Override
				public String sample() {
					return "0.0 := 0.0*0.0";
				}

				@Override
				public String description() {
					return "multiply the real numbers resulting in a real";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpMul.tDivision, operandsType[4], integ) {// id:13 0 := 0/1

				@Override
				public String sample() {
					return "0 := 0/1";
				}

				@Override
				public String description() {
					return "divide the first integer by second integer resulting in a integer (truncates the rest) (Runtime Error if second equals 0)";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpMul.tDivision, operandsType[5], real) {// id:14 0.0 := 0/1.0

				@Override
				public String sample() {
					return "0.0 := 0/1.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and "  + OpType.operandsType[16];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpMul.tDivision, operandsType[7], real) {// id:15 0.0 := 0.0/1

				@Override
				public String sample() {
					return "0.0 := 0.0/1";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and " + OpType.operandsType[16];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpMul.tDivision, operandsType[8], real) {// id:16 0.0 := 0.0/1.0

				@Override
				public String sample() {
					return "0.0 := 0.0/1.0";
				}

				@Override
				public String description() {
					return "divide the first real by second real resulting in a real(ressults in NaN if second equals 0)";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpMul.tAnd, operandsType[0], bool) {// id:17 T := T and T

				@Override
				public String sample() {
					return "T := T and T";
				}

				@Override
				public String description() {
					return "query boolean operands until the first returns false; otherwise returns true";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tEq, operandsType[0], bool) {// id:18 F := T = F

				@Override
				public String sample() {
					return "F := T = F";
				}

				@Override
				public String description() {
					return "compares boolean operands and returns true if they are equal or false otherwise";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tEq, operandsType[4], bool) {// id:19 T := 0 = 0

				@Override
				public String sample() {
					return "T := 0 = 0";
				}

				@Override
				public String description() {
					return "compares integer operands and returns true if they are equal or false otherwise";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tEq, operandsType[5], bool) {// id:20 T := 0 = 0.0

				@Override
				public String sample() {
					return "T := 0 = 0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and " + OpType.operandsType[22];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tEq, operandsType[7], bool) {// id:21 T := 0.0 = 0

				@Override
				public String sample() {
					return "T := 0.0 = 0";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and "  + OpType.operandsType[22];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tEq, operandsType[8], bool) {// id:22 T := 0.0 = 0.0

				@Override
				public String sample() {
					return "T := 0.0 = 0.0";
				}

				@Override
				public String description() {
					return "compares real operands and returns true if they are equal or false otherwise";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tNotEq, operandsType[0], bool) {// id:23 F := T <> F

				@Override
				public String sample() {
					return "F := T <> F";
				}

				@Override
				public String description() {
					return "compares boolean operands and returns false if they are equal or true otherwise";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tNotEq, operandsType[4], bool) {// id:24 T := 0 <> 0

				@Override
				public String sample() {
					return "T := 0 <> 0";
				}

				@Override
				public String description() {
					return "compares integer operands and returns false if they are equal or true otherwise";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tNotEq, operandsType[5], bool) {// id:25 T := 0 <> 0.0

				@Override
				public String sample() {
					return "T := 0 <> 0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and "  + OpType.operandsType[27];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tNotEq, operandsType[7], bool) {// id:26 T := 0.0 <> 0

				@Override
				public String sample() {
					return "T := 0.0 <> 0";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and "  + OpType.operandsType[27];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tNotEq, operandsType[8], bool) {// id:27 T := 0.0 <> 0.0

				@Override
				public String sample() {
					return "T := 0.0 <> 0.0";
				}

				@Override
				public String description() {
					return "compares real operands and returns false if they are equal or true otherwise";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tGreatT, operandsType[4], bool) {// id:28 F := 0 > 0

				@Override
				public String sample() {
					return "F := 0 > 0";
				}

				@Override
				public String description() {
					return "compares integer operands and returns true if first is greater than second or false otherwise";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tGreatT, operandsType[5], bool) {// id:29 F := 0 > 0.0

				@Override
				public String sample() {
					return "F := 0 > 0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and "  + OpType.operandsType[31];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tGreatT, operandsType[7], bool) {// id:30 F := 0.0 > 0

				@Override
				public String sample() {
					return "F := 0.0 > 0";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and " + OpType.operandsType[31];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tGreatT, operandsType[8], bool) {// id:31 F := 0.0 > 0.0

				@Override
				public String sample() {
					return "F := 0.0 > 0.0";
				}

				@Override
				public String description() {
					return "compares real operands and returns true if first is greater than second or false otherwise";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tLessT, operandsType[4], bool) {// id:32 F := 0 < 0

				@Override
				public String sample() {
					return "F := 0 < 0";
				}

				@Override
				public String description() {
					return "compares integer operands and returns true if first is less than second or false otherwise";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tLessT, operandsType[5], bool) {// id:33 F := 0 < 0.0

				@Override
				public String sample() {
					return "F := 0 < 0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and "  + OpType.operandsType[35];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tLessT, operandsType[7], bool) {// id:34 F := 0.0 < 0

				@Override
				public String sample() {
					return "F := 0.0 < 0";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and "  + OpType.operandsType[35];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tLessT, operandsType[8], bool) {// id:35 F := 0.0 < 0.0

				@Override
				public String sample() {
					return "F := 0.0 < 0.0";
				}

				@Override
				public String description() {
					return "compares real operands and returns true if first is less than second or false otherwise";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tGreatEqT, operandsType[4], bool) {// id:36 T := 0 >= 0

				@Override
				public String sample() {
					return "T := 0 >= 0";
				}

				@Override
				public String description() {
					return "compares integer operands and returns true if first is equals or greater than second or false otherwise";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tGreatEqT, operandsType[5], bool) {// id:37 T := 0 >= 0.0

				@Override
				public String sample() {
					return "T := 0 >= 0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and "  + OpType.operandsType[39];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tGreatEqT, operandsType[7], bool) {// id:38 T := 0.0 >= 0

				@Override
				public String sample() {
					return "T := 0.0 >= 0";
				}

				@Override
				public String description() {
					return "make a conversion on the secon operand (integer to real) and "  + OpType.operandsType[39];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tGreatEqT, operandsType[8], bool) {// id:39 T := 0.0 >= 0.0

				@Override
				public String sample() {
					return "T := 0.0 >= 0.0";
				}

				@Override
				public String description() {
					return "compares real operands and returns true if first is equals or greater than second or false otherwise";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tLessEqT, operandsType[4], bool) {// id:40 T := 0 <= 0

				@Override
				public String sample() {
					return "T := 0 <= 0";
				}

				@Override
				public String description() {
					return "compares integer operands and returns true if first is equals or less than second or false otherwise";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tLessEqT, operandsType[5], bool) {// id:41 T := 0 <= 0.0

				@Override
				public String sample() {
					return "T := 0 <= 0.0";
				}

				@Override
				public String description() {
					return "make a conversion on the first operand (integer to real) and "  + OpType.operandsType[42];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tLessEqT, operandsType[7], bool) {// id:42 T := 0.0 <= 0

				@Override
				public String sample() {
					return "T := 0.0 <= 0";
				}

				@Override
				public String description() {
					return "make a conversion on the second operand (integer to real) and "  + OpType.operandsType[42];
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			}, new OpType(OpRel.tLessEqT, operandsType[8], bool) {// id:43 T := 0.0 <= 0.0

				@Override
				public String sample() {
					return "T := 0.0 <= 0.0";
				}

				@Override
				public String description() {
					return "compares real operands and returns true if first is equals or less than second or false otherwise";
				}

				@Override
				public String instruction() {
					// TODO Auto-generated method stub
					return null;
				}

			} };

	private static Type[] search(Type[] operands) {
		for( Type[] i : OpType.operandsType) {
			if(i.length == operands.length) {
				boolean thereUnequal = false;
				for(int j=0;j<i.length;j++) {
					if(!operands[j].equals(i[j]))
						thereUnequal = true;
				}
				if(!thereUnequal)
					return i;
			}
		}
		return null;
	}
	
	public static OpType search(char op, Type[] operands) {
		operands = search(operands);
		if(operands != null)
			for (OpType i : OpType.table) {
				if(op == i.op && operands == i.operands)
					return i;
			}
		return null;
	}
	
	public OpType(char op, Type[] operands, Type ret) {
		super();
		this.op = op;
		this.operands = operands;
		this.ret = ret;
	}

	public abstract String sample();

	public abstract String description();

	public abstract String instruction();

	@Override
	public String toString() {
		return this.description();
	}
	
}
