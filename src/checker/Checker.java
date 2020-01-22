package checker;

import ast.AST;
import ast.AggregateType;
import ast.AssignmentCommand;
import ast.BoolLit;
import ast.Command;
import ast.ConditionalCommand;
import ast.Declaration;
import ast.Expression;
import ast.Factor;
import ast.FloatLit;
import ast.IntLit;
import ast.IterativeCommand;
import ast.MultiCommand;
import ast.Op;
import ast.OpAd;
import ast.OpMul;
import ast.OpRel;
import ast.PrimitiveType;
import ast.Program;
import ast.SimpleExpression;
import ast.Term;
import ast.Type;
import ast.Variable;
import ast.Visitor;

public class Checker implements Visitor {

	private IdentifierTable identifierTable;
	private MultiCheckerException mce = new MultiCheckerException();
	
	public void check(AST ast) throws CheckerException {
		ast.visit(this);
		try {
			identifierTable.check();
		}catch(CheckerException ce) {
			ce.printStackTrace();
		}
		mce.check();
	}

	public Checker() {
		super();
		this.identifierTable = new IdentifierTable();
	}

	@Override
	public void visitAggregateType(AggregateType ast) {
		if (ast.getIndex(0) > ast.getIndex(1)) {
			mce.add(new CheckerException(ast, "Start index is great than End index"));
		}
		ast.getType().visit(this);
	}

	@Override
	public void visitAssignmentCommand(AssignmentCommand ast) {
		ast.getVariable().visit(this);// Sets declaration of variable or cause exception
		ast.getExpression().visit(this);// Sets type of expression
		try {
			if (ast.getVariable().getType() == null
					|| !ast.getVariable().getType().equals(ast.getExpression().getType())) {
				mce.add(new CheckerException(ast,
						"Assignment cant't be made because the types are incompatibles.\n\t" + ast.getVariable()
								+ " is a " + ast.getVariable().getType() + "and expression is a "
								+ ast.getExpression().getType()));
			}
		} catch (ClassCastException e) {
			// TODO quando o tipo ret não for aggregateType, mas for indexado dentro, será
			// uma indexação de uma variável primitiva
			mce.add(new CheckerException(ast.getVariable(),
					"This varible is not a aggregate Type and can not be indexed"));
		}
	}

	@Override
	public void visitConditionalCommand(ConditionalCommand ast) {
		ast.getExpression().visit(this);
		if (!ast.getExpression().getType().equals(OpType.bool)) {
			mce.add(new CheckerException(ast,
					"The command If should recive a boolean value, but the expression returns a "
							+ ast.getExpression().getType() + " value"));
		}
		ast.getCommand(true).visit(this);
		if (ast.getCommand(false) != null)
			ast.getCommand(false).visit(this);

	}

	@Override
	public void visitIterativeCommand(IterativeCommand ast) {
		ast.getExpression().visit(this);
		if (!ast.getExpression().getType().equals(OpType.bool)) {
			mce.add(new CheckerException(ast,
					"The command While should recive a boolean value, but the expression returns a "
							+ ast.getExpression().getType() + " value"));
		}
		ast.getCommand().visit(this);
	}

	@Override
	public void visitMultiCommand(MultiCommand ast) {
		for (Object i : ast.getCommand().toArray())
			((Command) i).visit(this);

	}

	@Override
	public void visitDeclaration(Declaration ast) {
		for (String i : ast.getId()) {
			if (identifierTable.enter(i, ast)) {
				Declaration d = identifierTable.retrieve(i);
				mce.add(new CheckerException(ast, "This identifier already was declared! see [" + d.getLine() + " : "
						+ d.getColumn() + "]:\n\t" + d));
			}
		}

		ast.getType().visit(this);
	}

	@Override
	public void visitExpression(Expression ast) {
		for (SimpleExpression i : ast.getA()) {
			if (i != null)
				i.visit(this);
		}
		if (ast.getOp() != null) {
			ast.getOp().visit(this);

			Type[] operands = new Type[2];
			Op op = ast.getOp();
			try {
				operands[0] = ast.getA(0).getType();
			} catch (ClassCastException e) {
				// TODO
				System.out.println(ast.getA(0));
				mce.add(new CheckerException(ast.getA(0),
						"This varible is not a aggregate Type and can not be indexed"));
			}
			try {
				operands[1] = ast.getA(1).getType();
			} catch (ClassCastException e) {
				// TODO
				System.out.println(ast.getA(1));
				mce.add(new CheckerException(ast.getA(1),
						"This varible is not a aggregate Type and can not be indexed"));
			}
			OpType opt = OpType.search(op.getOp(), operands);
			op.setOpType(opt);
			if (opt.ret == null) {
				mce.add(new CheckerException(ast,
						"This operation is " + opt.description() + "! Check the type of operands"));
			}
		}

	}

	@Override
	public void visitSimpleExpression(SimpleExpression ast) {
		for (Object i : ast.getA().toArray()) {
			((Term) i).visit(this);
		}

		if (ast.getOp().size() > 0) {
			Type[] operands = new Type[2];
			Op op = ast.getOp(ast.getOp().size() - 1);
			op.visit(this);

			try {
				operands[0] = ast.getA(ast.getOp().size() - 1).getType();
			} catch (ClassCastException e) {
				// TODO
				System.out.println(ast.getA(ast.getOp().size() - 1));
				mce.add(new CheckerException(ast.getA(ast.getOp().size() - 1),
						"This varible is not a aggregate Type and can not be indexed"));
			}
			try {
				operands[1] = ast.getA(ast.getOp().size()).getType();
			} catch (ClassCastException e) {
				// TODO
				mce.add(new CheckerException(ast.getA(ast.getOp().size()),
						"This varible is not a aggregate Type and can not be indexed"));
			}
			OpType opt = OpType.search(op.getOp(), operands);
			op.setOpType(opt);
			if (opt.ret == null) {
				mce.add(new CheckerException(ast,
						"This operation is " + opt.description() + "! Check the type of operands"));
			}
			for (int i = ast.getOp().size() - 2; i >= 0; i--) {
				op = ast.getOp(i);
				op.visit(this);
				try {
					operands[0] = ast.getA(i).getType();
				} catch (ClassCastException e) {
					// TODO
					System.out.println(ast.getA(i));
					mce.add(new CheckerException(ast.getA(i),
							"This varible is not a aggregate Type and can not be indexed"));
				}
				try {
					operands[1] = ast.getOp(i + 1).getType();
				} catch (ClassCastException e) {
					// TODO
					System.out.println(ast.getA(i + 1));
					mce.add(new CheckerException(ast.getA(i + 1),
							"This varible is not a aggregate Type and can not be indexed"));
				}
				opt = OpType.search(op.getOp(), operands);
				op.setOpType(opt);
				if (opt.ret == null) {
					mce.add(new CheckerException(ast,
							"This operation is " + opt.description() + "! Check the type of operands"));
				}
			}
		}

	}

	@Override
	public void visitTerm(Term ast) {
		for (Object i : ast.getA().toArray()) {
			((Factor) i).visit(this);
		}

		if (ast.getOp().size() > 0) {
			Type[] operands = new Type[2];
			Op op = ast.getOp(0);
			op.visit(this);
			try {
				operands[0] = ast.getA(0).getType();
			} catch (ClassCastException e) {
				// TODO
				System.out.println(ast.getA(0));
				mce.add(new CheckerException(ast.getA(0),
						"This varible is not a aggregate Type and can not be indexed"));
			}
			try {
				operands[1] = ast.getA(1).getType();
			} catch (ClassCastException e) {
				// TODO
				System.out.println(ast.getA(1));
				mce.add(new CheckerException(ast.getA(1),
						"This varible is not a aggregate Type and can not be indexed"));
			}
			OpType opt = OpType.search(op.getOp(), operands);
			op.setOpType(opt);
			if (opt.ret == null) {
				mce.add(new CheckerException(ast,
						"This operation is " + opt.description() + "! Check the type of operands"));
			}
			for (int i = 1; i < ast.getOp().size(); i++) {
				op = ast.getOp(i);
				op.visit(this);

				try {
					operands[0] = ast.getOp(i - 1).getType();
				} catch (ClassCastException e) {
					// TODO
					System.out.println(ast.getA(i - 1));
					mce.add(new CheckerException(ast.getA(i - 1),
							"This varible is not a aggregate Type and can not be indexed"));
				}
				try {
					operands[1] = ast.getA(i + 1).getType();
				} catch (ClassCastException e) {
					// TODO
					System.out.println(ast.getA(i + 1));
					mce.add(new CheckerException(ast.getA(i + 1),
							"This varible is not a aggregate Type and can not be indexed"));
				}

				opt = OpType.search(op.getOp(), operands);
				op.setOpType(opt);
				if (opt.ret == null) {
					mce.add(new CheckerException(ast,
							"This operation is " + opt.description() + "! Check the type of operands"));
				}
			}
		}

	}

	@Override
	public void visitProgram(Program ast) {
		for (Object i : ast.getDeclaration().toArray()) {
			((Declaration) i).visit(this);
		}
		ast.getMc().visit(this);
	}

	@Override
	public void visitPrimitiveType(PrimitiveType ast) {
	}

	@Override
	public void visitVariable(Variable ast) {
		Declaration dec = this.identifierTable.retrieve(ast.getId());
		if (dec != null)
			ast.setDeclaration(dec);
		else {
			mce.add(new CheckerException(ast, "This variable wasn't declared!"));
		}
		for (Expression i : ast.getIndexer()) {
			i.visit(this);
			if (!i.getType().equals(OpType.integ)) {
				mce.add(new CheckerException(ast,
						"The command If should recive a boolean value, but the expression returns a " + i.getType()
								+ " value"));
			}
		}
	}

	@Override
	public void visitOpAd(OpAd ast) {
	}

	@Override
	public void visitOpMul(OpMul ast) {
	}

	@Override
	public void visitOpRel(OpRel ast) {
	}

	@Override
	public void visitBoolLit(BoolLit ast) {
	}

	@Override
	public void visitFloatLit(FloatLit ast) {
	}

	@Override
	public void visitIntLit(IntLit ast) {
	}

	
	

}
