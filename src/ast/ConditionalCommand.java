package ast;

public class ConditionalCommand extends Command {
	private Expression expression;
	private Command[] command = new Command[2];

	public ConditionalCommand(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
	}

	public Expression getExpression() {
		return expression;
	}

	public ConditionalCommand setExpression(Expression expression) {
		this.expression = expression;

		return this;
	}

	public Command getCommand(boolean cond) {
		return command[cond ? 0 : 1];
	}

	public ConditionalCommand setCommand(Command command, boolean cond) {
		this.command[cond ? 0 : 1] = command;
		return this;
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitConditionalCommand(this);
	};
}
