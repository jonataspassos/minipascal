package ast;

public class IterativeCommand extends Command {
	private Expression expression;
	private Command command;

	public IterativeCommand(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
	}

	public Expression getExpression() {
		return expression;
	}

	public IterativeCommand setExpression(Expression expression) {
		this.expression = expression;

		return this;
	}

	public Command getCommand() {
		return command;
	}

	public IterativeCommand setCommand(Command command) {
		this.command = command;
		return this;
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitIterativeCommand(this);
	};
}
