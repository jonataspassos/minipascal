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

/**
 * Esta classe é responsável por toda a verificação de contexto do código fonte
 * sobre a AST Ela também decorará a arvore com informações sobre o contexto
 * fundamentais para a geração de código
 */
public class Checker implements Visitor {

	/**
	 * Guarda a tabela de identificadores com suas respectivas declarações
	 */
	private IdentifierTable identifierTable;
	/**
	 * Armazena a lista de erros (não propagados)
	 */
	private MultiCheckerException mce = new MultiCheckerException();

	/**
	 * Realiza a verificação de regras de contexto
	 */
	public void check(AST ast) throws CheckerException {
		ast.visit(this);
		mce.check();
		try {
			identifierTable.check();
		} catch (CheckerException ce) {
			ce.printStackTrace();
		}
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
			if (ast.getVariable().getType() == null || ast.getExpression().getType() == null) {
				return;
			}

			Type[] assOperands = new Type[2];
			assOperands[0] = ast.getVariable().getType();
			assOperands[1] = ast.getExpression().getType();
			OpType opt = OpType.searchAss(assOperands);
			if (opt.ret == null) {
				mce.add(new CheckerException(ast,
						"Assignment cant't be made because the types are incompatibles.\n\t" + ast.getVariable()
								+ " is a " + ast.getVariable().getType() + "and expression is a "
								+ ast.getExpression().getType()));
				return;
			}

			if (opt.op == '~') {
				(new CheckerException(ast,
						"Coersion was necessary because the types are differents. \n\t" + ast.getVariable() + " is a "
								+ ast.getVariable().getType() + " and expression is a "
								+ ast.getExpression().getType()) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public String getMessage() {
						String ast = ("" + this.ast);
						if (ast.charAt(ast.length() - 1) == '\n')
							ast = ast.substring(0, ast.length() - 1);

						ast = ast.replaceAll("\n", "\n\t");
						return "WARNING [" + this.getLine() + " : " + this.getColumn() + "] Context Warning:\n"
								+ "\t--------------------------------\n" + "\t"
								+ ast.substring(0, ast.length() <= 50 ? ast.length() : 50) + "\n"
								+ "\t--------------------------------\n" + "\t" + this.message + "\n\n";
					}
				}).printStackTrace();
			}
			ast.setAssType(opt);
		} catch (CheckerException ce) {
			mce.add(ce.setAst(ast));
		}

	}

	@Override
	public void visitConditionalCommand(ConditionalCommand ast) {
		ast.getExpression().visit(this);
		try {
			if (ast.getExpression().getType() != null) {

				if (!OpType.bool.equals(ast.getExpression().getType())) {
					mce.add(new CheckerException(ast,
							"The command If should recive a boolean value, but the expression returns a "
									+ ast.getExpression().getType() + " value"));
				}
			}
		} catch (CheckerException ce) {
			mce.add(ce.setAst(ast));
		}
		ast.getCommand(true).visit(this);
		if (ast.getCommand(false) != null)
			ast.getCommand(false).visit(this);

	}

	@Override
	public void visitIterativeCommand(IterativeCommand ast) {
		ast.getExpression().visit(this);
		try {
			if (ast.getExpression().getType() != null) {

				if (!OpType.bool.equals(ast.getExpression().getType())) {
					mce.add(new CheckerException(ast,
							"The command While should recive a boolean value, but the expression returns a "
									+ ast.getExpression().getType() + " value"));
				}
			}
		} catch (CheckerException ce) {
			mce.add(ce.setAst(ast));
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
		boolean any_null = false;
		for (SimpleExpression i : ast.getA()) {
			if (i != null) {
				i.visit(this);
				try {
					any_null = any_null || i.getType() == null;
				} catch (CheckerException ce) {
					any_null = true;
				}
			}
			if (any_null)
				return;
		}
		if (ast.getOp() != null) {
			ast.getOp().visit(this);

			Type[] operands = new Type[2];
			Op op = ast.getOp();
			try {
				operands[0] = ast.getA(0).getType();
			} catch (CheckerException ce) {
				mce.add(ce.setAst(ast));
			}
			try {
				operands[1] = ast.getA(1).getType();
			} catch (CheckerException ce) {
				mce.add(ce.setAst(ast));
			}
			OpType opt = OpType.search(op.getOp(), operands);
			op.setOpType(opt);
			if (opt.ret == null) {
				mce.add(new CheckerException(ast,
						"This operation is " + opt.description() + "! Check the type of operands to operator\n\t"
								+ ast.getA(0) + " " + op + " " + ast.getA(1) + "\n\t" + " '" + operands[0]
								+ "' " + op + " '" + operands[1] + "' "));
			}
		}

	}

	@Override
	public void visitSimpleExpression(SimpleExpression ast) {

		boolean any_null = false;
		for (Term i : ast.getA()) {
			if (i != null) {
				i.visit(this);
				try {
					any_null = any_null || i.getType() == null;
				} catch (CheckerException ce) {
					any_null = true;
				}
			}
			if (any_null)
				return;
		}

		if (ast.getOp().size() > 0) {
			Type[] operands = new Type[2];
			Op op = ast.getOp(ast.getOp().size() - 1);
			op.visit(this);

			try {
				operands[0] = ast.getA(ast.getOp().size() - 1).getType();
			} catch (CheckerException ce) {
				mce.add(ce.setAst(ast));
			}
			try {
				operands[1] = ast.getA(ast.getOp().size()).getType();
			} catch (CheckerException ce) {
				mce.add(ce.setAst(ast));
			}
			OpType opt = OpType.search(op.getOp(), operands);
			op.setOpType(opt);
			if (opt.ret == null) {
				mce.add(new CheckerException(ast,
						"This operation is " + opt.description() + "! Check the type of operands to operator\n\t"
								+ ast.getA(ast.getOp().size() - 1) + " " + op + " "
								+ ast.getA(ast.getOp().size()) + "\n\t" + " '" + operands[0] + "' " + op + " '"
								+ operands[1] + "' "));
				return;
			}
			for (int i = ast.getOp().size() - 2; i >= 0; i--) {
				op = ast.getOp(i);
				op.visit(this);
				try {
					operands[0] = ast.getA(i).getType();
				} catch (CheckerException ce) {
					mce.add(ce.setAst(ast));
				}
				operands[1] = ast.getOp(i + 1).getType();
				opt = OpType.search(op.getOp(), operands);
				op.setOpType(opt);
				if (opt.ret == null) {
					mce.add(new CheckerException(ast,
							"This operation is " + opt.description() + "! Check the type of operands to operator\n\t"
									+ ast.getA(i) + " " + op + " " + ast.getA(i + 1) + "\n\t" + " '"
									+ operands[0] + "' " + op + " '" + operands[1] + "' "));
					return;
				}
			}
		}

	}

	@Override
	public void visitTerm(Term ast) {
		boolean any_null = false;
		for (Factor i : ast.getA()) {
			if (i != null) {
				i.visit(this);
				try {
					any_null = any_null || i.getType() == null;
				} catch (CheckerException ce) {
					mce.add(ce.setAst(ast));
					any_null = true;
				}
			}
			if (any_null)
				return;
		}

		if (ast.getOp().size() > 0) {
			Type[] operands = new Type[2];
			Op op = ast.getOp(0);
			op.visit(this);
			try {
				operands[0] = ast.getA(0).getType();
			} catch (CheckerException ce) {
				mce.add(ce.setAst(ast));
			}
			try {
				operands[1] = ast.getA(1).getType();
			} catch (CheckerException ce) {
				mce.add(ce.setAst(ast));
			}
			OpType opt = OpType.search(op.getOp(), operands);
			op.setOpType(opt);
			if (opt.ret == null) {
				mce.add(new CheckerException(ast,
						"This operation is " + opt.description() + "! Check the type of operands to operator\n\t"
								+ ast.getA(0) + " " + op + " " + ast.getA(1) + "\n\t" + " '" + operands[0]
								+ "' " + op + " '" + operands[1] + "' "));
				return;
			}
			for (int i = 1; i < ast.getOp().size(); i++) {
				op = ast.getOp(i);
				op.visit(this);

				operands[0] = ast.getOp(i - 1).getType();
				try {
					operands[1] = ast.getA(i + 1).getType();
				} catch (CheckerException ce) {
					mce.add(ce.setAst(ast));
				}

				opt = OpType.search(op.getOp(), operands);
				op.setOpType(opt);
				if (opt.ret == null) {
					mce.add(new CheckerException(ast,
							"This operation is " + opt.description() + "! Check the type of operands to operator\n\t"
									+ ast.getA(i) + " " + op + " " + ast.getA(i + 1) + "\n\t" + " '"
									+ operands[0] + "' " + op + " '" + operands[1] + "' "));
					return;
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
			return;
		}
		for (Expression i : ast.getIndexer()) {
			i.visit(this);
			try {
				if (!i.getType().equals(OpType.integ)) {
					mce.add(new CheckerException(ast,
							"The command If should recive a boolean value, but the expression returns a " + i.getType()
									+ " value"));
				}
			} catch (CheckerException ce) {
				mce.add(ce.setAst(ast));
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
