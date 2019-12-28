package ast;

public class AssignmentCommand extends Command {
	private Variable variable;
	private Expression expression;

	public AssignmentCommand(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
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
	};
}
