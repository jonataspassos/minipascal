package coder;

public class TAMIntructions extends Instructions {

	private static final String charComment = "\t;";
	private static String[] registers = { "CB", "CT", "PB", "PT", "SB", "ST", "HB", "HT", "LB", "L1", "L2", "L3", "L4",
			"L5", "L6", "CP" };
	private static String[] primitiveLabels = { "", "id", "not", "and", "or", "succ", "pred", "neg", "add", "sub",
			"mult", "div", "mod", "lt", "le", "ge", "gt", "eq", "ne", "eol", "eof", "get", "put", "geteol", "puteol",
			"getint", "putint", "new", "dispose" };

	@Override
	public String getLoad(byte n, int d, String r, String comment) {
		return "LOAD(" + n + ") " + d + "[" + r + "] " + comment(comment);
	}

	@Override
	public String getLoadA(int d, String r, String comment) {
		return "LOADA " + d + "[" + r + "] " + comment(comment);
	}

	@Override
	public String getLoadI(byte n, String comment) {
		return "LOADI(" + n + ") " + comment(comment);
	}

	@Override
	public String getLoadL(Object d, String comment) {
		return "LOADL " + d + " " + comment(comment);
	}

	@Override
	public String getStore(byte n, int d, String r, String comment) {
		return "STORE(" + n + ") " + d + "[" + r + "] " + comment(comment);
	}

	@Override
	public String getStoreI(byte n, String comment) {
		return "STOREI(" + n + ") " + comment(comment);
	}

	@Override
	public String getCall(String n, int d, String r, String comment) {
		return "CALL(" + n + ") " + labelOrReg(d, r) + " " + comment(comment);
	}

	@Override
	public String getCallI(String comment) {
		return "CALLI " + comment(comment);
	}

	@Override
	public String getReturn(byte n, int d, String comment) {
		return "RETURN(" + n + ") " + d + " " + comment(comment);
	}

	@Override
	public String getPush(int d, String comment) {
		return "PUSH " + d + " " + comment(comment);
	}

	@Override
	public String getPop(byte n, int d, String comment) {
		return "POP(" + n + ") " + d + " " + comment(comment);
	}

	@Override
	public String getJump(int d, String r, String comment) {
		return "JUMP " + labelOrReg(d, r) + " " + comment(comment);
	}

	@Override
	public String getJumpI(String comment) {
		return "JUMPI " + comment(comment);
	}

	@Override
	public String getJumpIf(byte n, int d, String r, String comment) {
		return "JUMPIF(" + n + ") " + labelOrReg(d, r) + " " + comment(comment);
	}

	@Override
	public String applyLabel(String label, String comment) {
		return label + ": " + comment(comment);
	}

	public String comment(String comment) {
		return comment == null ? "" : (comment.length() > 0 ? charComment + comment : "") + "\n";
	}

	@Override
	public String getHalt(String comment) {
		return "HALT " + comment(comment);
	}

	private String labelOrReg(int d, String r) {
		for (String i : TAMIntructions.registers) {
			if (i.equals(r)) {
				return "" + d + "[" + r + "]";// Reg
			}
		}
		for (int i=0;i<TAMIntructions.primitiveLabels.length;i++) {
			if (TAMIntructions.primitiveLabels[i].equals(r)) {
				return "" + i + "[" + TAMIntructions.registers[2] + "]";// Reg
			}
		}
		return r;// label
	}

}
