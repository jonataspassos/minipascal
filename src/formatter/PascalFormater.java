package formatter;

import ast.*;


public class PascalFormater extends Formatter {

	@Override
	public void visitAggregateType(AggregateType ast) {
		this.out += "array [" + ast.getIndex(0) + "~" + ast.getIndex(1) + "] of ";
		ast.getType().visit(this);
	}

	@Override
	public void visitAssignmentCommand(AssignmentCommand ast) {
		identation();
		ast.getVariable().visit(this);
		this.out += " := ";
		ast.getExpression().visit(this);
		this.out += ";\n";
	}

	@Override
	public void visitConditionalCommand(ConditionalCommand ast) {
		identation();
		this.out += "if ";
		ast.getExpression().visit(this);
		this.out += " then\n";
		identation(true);
		ast.getCommand(true).visit(this);
		this.out += "\n";
		this.level--;
		if (ast.getCommand(false) != null) {
			identation();
			this.out += "else\n";
			identation(true);
			ast.getCommand(false).visit(this);
			this.out += "\n";
			this.level--;
		}
	}

	@Override
	public void visitIterativeCommand(IterativeCommand ast) {
		identation();
		this.out += "while ";
		ast.getExpression().visit(this);
		this.out += " then\n";
		identation(true);
		ast.getCommand().visit(this);
		this.out += "\n";
		this.level--;
	}

	@Override
	public void visitMultiCommand(MultiCommand ast) {
		identation(false);
		this.out+= "begin\n";
		this.level++;
		for(Object i : ast.getCommand().toArray()) {
			identation();
			((Command)i).visit(this);
		}
		identation(false);
		this.out+= "end\n";
		this.level++;
	}

	@Override
	public void visitDeclaration(Declaration ast) {
		identation();
		this.out+= "var "+ast.getId(0);
		for(int i=1;i<ast.getId().size();i++) {
			this.out+= ", " + ast.getId(i);
		}
		this.out += " : ";
		ast.getType().visit(this);
		this.out += ";\n";
	}

	@Override
	public void visitExpression(Expression ast) {
		ast.getA(0).visit(this);
		if(ast.getA(1)!=null) {
			this.out += " ";
			ast.getOp().visit(this);
			this.out += " ";
			ast.getA(1).visit(this);
		}
	}

	@Override
	public void visitSimpleExpression(SimpleExpression ast) {
		ast.getA(0).visit(this);
		for(int i=1;i<ast.getA().size();i++) {
			this.out += " ";
			ast.getOp(i-1).visit(this);
			this.out += " ";
			ast.getA(i).visit(this);
		}

	}

	@Override
	public void visitTerm(Term ast) {
		ast.getA(0).visit(this);
		for(int i=1;i<ast.getA().size();i++) {
			this.out += " ";
			ast.getOp(i-1).visit(this);
			this.out += " ";
			ast.getA(i).visit(this);
		}
	}

	@Override
	public void visitProgram(Program ast) {
		identation();
		this.out += "program "+ast.getId() + ";\n";
		this.level++;
		for(int i=0;i<ast.getDeclaration().size();i++) {
			ast.getA(i).visit(this);
		}
		ast.getMc().visit(this);
		this.out = this.out.substring(0,this.out.length()-1)+".";
	}

	@Override
	public void visitPrimitiveType(PrimitiveType ast) {
		switch(ast.getType()) {
		case PrimitiveType.tBoolean:
			this.out += "boolean";
			break;
		case PrimitiveType.tInt:
			this.out += "integer";
			break;
		case PrimitiveType.tReal:
			this.out += "real";
			break;
		}
	}

	@Override
	public void visitVariable(Variable ast) {
		this.out+=ast.getId();
		for(int i=0;i<ast.getIndexer().size();i++) {
			this.out+="[";
			ast.getIndexer(i).visit(this);
			this.out+="]";
		}
	}

	@Override
	public void visitOpAd(OpAd ast) {
		switch(ast.getOp()) {
		case OpAd.tPlus:
			this.out += "+";
			break;
		case OpAd.tMinus:
			this.out += "-";
			break;
		case OpAd.tOr:
			this.out += "or";
			break;
		}
	}

	@Override
	public void visitOpMul(OpMul ast) {
		switch(ast.getOp()) {
		case OpMul.tProduct:
			this.out += "*";
			break;
		case OpMul.tDivision:
			this.out += "/";
			break;
		case OpMul.tAnd:
			this.out += "and";
			break;
		}
	}

	@Override
	public void visitOpRel(OpRel ast) {
		switch(ast.getOp()) {
		case OpRel.tLessT:
			this.out += "<";
			break;
		case OpRel.tGreatT:
			this.out += ">";
			break;
		case OpRel.tLessEqT:
			this.out += "<=";
			break;
		case OpRel.tGreatEqT:
			this.out += ">=";
			break;
		case OpRel.tEq:
			this.out += "=";
			break;
		case OpRel.tNotEq:
			this.out += "<>";
			break;
		}
	}

	@Override
	public void visitBoolLit(BoolLit ast) {
		this.out +=ast.isValue()?"true":"false";
	}

	@Override
	public void visitFloatLit(FloatLit ast) {
		this.out += "" + ast.getValue();

	}

	@Override
	public void visitIntLit(IntLit ast) {
		this.out += "" + ast.getValue();
	}

}
