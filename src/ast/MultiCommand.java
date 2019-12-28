package ast;

import java.util.ArrayList;

public class MultiCommand extends Command {

	private ArrayList<Command> command = new ArrayList<Command>();

	public MultiCommand(int line, int column) {
		super(line, column);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Command> getCommand() {
		return command;
	}

	public MultiCommand setCommand(Command command, int index) {
		this.command.set(index, command);
		return this;
	}

	public MultiCommand addCommand(Command command) {
		this.command.add(command);
		return this;
	}

	public Command getCommand(int index) {
		return this.command.get(index);
	}

	// Padrão Visitor
	public void visit(Visitor v) {
		v.visitMultiCommand(this);
	};
}
