package ast;

import checker.OpType;

public class AssignmentCommand extends Command {
	private Variable variable;
	private Expression expression;
	private OpType assType;

	public AssignmentCommand(int line, int column) {
		super(line, column);
	}

	public Variable getVariable() {
		return variable;
	}

	public AssignmentCommand setVariable(Variable variable) {
		this.variable = variable;
		return this;
	}

	public Expression getExpression() {
		return expression;
	}

	public AssignmentCommand setExpression(Expression expression) {
		this.expression = expression;
		return this;
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitAssignmentCommand(this);
	}

	// Checker
	public OpType getAssType() {
		return assType;
	}

	public void setAssType(OpType assType) {
		this.assType = assType;
	};
}
