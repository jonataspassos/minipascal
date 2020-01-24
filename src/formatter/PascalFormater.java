package formatter;

import ast.*;

/**
 * Herda da classe Formatter e implementa os métodos obrigados pela interface
 * Visitor. Esta classe configura uma Formatter para a linguagem pascal,
 * entregando na saída o código formatado. Para cada um dos não terminais da
 * gramática da linguagem fonte, esta classe especifica uma forma de adicioná-lo
 * à formatação da saída.
 */
public class PascalFormater extends Formatter {

	/**
	 * Especifica a forma de adição à formatação para o AggregateType 
	 * */
	@Override
	public void visitAggregateType(AggregateType ast) {
		this.out += "array [" + ast.getIndex(0) + "~" + ast.getIndex(1) + "] of ";
		ast.getType().visit(this);
	}
	
	/**
	 * Especifica a forma de adição à formatação para o AssignmentCommand 
	 * */
	@Override
	public void visitAssignmentCommand(AssignmentCommand ast) {
		identation();
		ast.getVariable().visit(this);
		this.out += " := ";
		ast.getExpression().visit(this);
		this.out += ";\n";
	}
	
	/**
	 * Especifica a forma de adição à formatação para o ConditionalCommand 
	 * */
	@Override
	public void visitConditionalCommand(ConditionalCommand ast) {
		identation();
		this.out += "if ";
		ast.getExpression().visit(this);
		this.out += " then\n";
		this.level++;
		ast.getCommand(true).visit(this);
		// this.out += "\n";
		this.level--;
		if (ast.getCommand(false) != null) {
			identation();
			this.out += "else\n";
			this.level++;
			ast.getCommand(false).visit(this);
			// this.out += "\n";
			this.level--;
		}
	}

	/**
	 * Especifica a forma de adição à formatação para o IterativeCommand 
	 * */
	@Override
	public void visitIterativeCommand(IterativeCommand ast) {
		identation();
		this.out += "while ";
		ast.getExpression().visit(this);
		this.out += " then\n";
		this.level++;
		ast.getCommand().visit(this);
		// this.out += "\n";
		this.level--;
	}

	/**
	 * Especifica a forma de adição à formatação para o MultiCommand 
	 * */
	@Override
	public void visitMultiCommand(MultiCommand ast) {
		this.level--;
		identation();
		this.out += "begin\n";
		this.level++;
		for (Object i : ast.getCommand().toArray()) {
			((Command) i).visit(this);
		}
		identation(false);
		this.out += "end\n";
		this.level++;
	}

	/**
	 * Especifica a forma de adição à formatação para a Declaration  
	 * */
	@Override
	public void visitDeclaration(Declaration ast) {
		identation();
		this.out += "var " + ast.getId(0);
		for (int i = 1; i < ast.getId().size(); i++) {
			this.out += ", " + ast.getId(i);
		}
		this.out += " : ";
		ast.getType().visit(this);
		this.out += ";\n";
	}

	/**
	 * Especifica a forma de adição à formatação para a Expression 
	 * */
	@Override
	public void visitExpression(Expression ast) {
		ast.getA(0).visit(this);
		if (ast.getA(1) != null) {
			this.out += " ";
			ast.getOp().visit(this);
			this.out += " ";
			ast.getA(1).visit(this);
		}
	}

	/**
	 * Especifica a forma de adição à formatação para a SimpleExpression 
	 * */
	@Override
	public void visitSimpleExpression(SimpleExpression ast) {
		ast.getA(0).visit(this);
		for (int i = 1; i < ast.getA().size(); i++) {
			this.out += " ";
			ast.getOp(i - 1).visit(this);
			this.out += " ";
			ast.getA(i).visit(this);
		}

	}

	/**
	 * Especifica a forma de adição à formatação para o Term 
	 * */
	@Override
	public void visitTerm(Term ast) {
		boolean exp = ast.getA(0) instanceof Expression;
		this.out += exp ? "(" : "";
		ast.getA(0).visit(this);
		this.out += exp ? ")" : "";

		for (int i = 1; i < ast.getA().size(); i++) {
			ast.getOp(i - 1).visit(this);

			exp = ast.getA(i) instanceof Expression;
			this.out += exp ? "(" : "";
			ast.getA(i).visit(this);
			this.out += exp ? ")" : "";
		}
	}

	/**
	 * Especifica a forma de adição à formatação para o Program 
	 * */
	@Override
	public void visitProgram(Program ast) {
		identation();
		this.out += "program " + ast.getId() + ";\n";
		this.level++;
		for (int i = 0; i < ast.getDeclaration().size(); i++) {
			ast.getDeclaration(i).visit(this);
		}
		ast.getMc().visit(this);
		this.out = this.out.substring(0, this.out.length() - 1) + ".";
	}

	/**
	 * Especifica a forma de adição à formatação para o PrimitiveType 
	 * */
	@Override
	public void visitPrimitiveType(PrimitiveType ast) {
		switch (ast.getType()) {
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

	/**
	 * Especifica a forma de adição à formatação para a Variable 
	 * */
	@Override
	public void visitVariable(Variable ast) {
		this.out += ast.getId();
		for (int i = 0; i < ast.getIndexer().size(); i++) {
			this.out += "[";
			ast.getIndexer(i).visit(this);
			this.out += "]";
		}
	}

	/**
	 * Especifica a forma de adição à formatação para o OpAd 
	 * */
	@Override
	public void visitOpAd(OpAd ast) {
		switch (ast.getOp()) {
		case OpAd.tPlus:
			this.out += "+";
			break;
		case OpAd.tMinus:
			this.out += "-";
			break;
		case OpBolAd.tOr:
			this.out += "or";
			break;
		}
	}

	/**
	 * Especifica a forma de adição à formatação para o OpMul 
	 * */
	@Override
	public void visitOpMul(OpMul ast) {
		switch (ast.getOp()) {
		case OpMul.tProduct:
			this.out += "*";
			break;
		case OpMul.tDivision:
			this.out += "/";
			break;
		case OpBolMul.tAnd:
			this.out += "and";
			break;
		}
	}

	/**
	 * Especifica a forma de adição à formatação para o OpRel 
	 * */
	@Override
	public void visitOpRel(OpRel ast) {
		switch (ast.getOp()) {
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

	/**
	 * Especifica a forma de adição à formatação para o BoolLit 
	 * */
	@Override
	public void visitBoolLit(BoolLit ast) {
		this.out += ast.isValue() ? "true" : "false";
	}

	/**
	 * Especifica a forma de adição à formatação para o FloatLit 
	 * */
	@Override
	public void visitFloatLit(FloatLit ast) {
		this.out += "" + ast.getValue();

	}

	/**
	 * Especifica a forma de adição à formatação para o IntLit 
	 * */
	@Override
	public void visitIntLit(IntLit ast) {
		this.out += "" + ast.getValue();
	}

}
