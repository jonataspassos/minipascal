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
import formatter.Formatter;
import formatter.PascalFormater;
import parser.Parser;
import printer.Printer;

public class Checker implements Visitor {

	private IdentifierTable identifierTable;

	public Checker() {
		super();
		this.identifierTable = new IdentifierTable();
	}

	@Override
	public void visitAggregateType(AggregateType ast) {
		if(ast.getIndex(0)>ast.getIndex(1)) {
			//TODO erro, indice inicial maior que indice final
			System.err.print("Erro AggregateType");
			System.out.println(ast);
		}
		ast.getType().visit(this);
	}

	@Override
	public void visitAssignmentCommand(AssignmentCommand ast) {
		ast.getVariable().visit(this);//Sets declaration of variable or cause exception
		ast.getExpression().visit(this);//Sets type of expression
		
		if(ast.getVariable().getType()==null || !ast.getVariable().getType().equals(ast.getExpression().getType())) {
			//TODO erro, tipos incompat�veis
			System.err.print("Erro AssignmentCommand");
			System.out.println(ast);
		}
	}

	@Override
	public void visitConditionalCommand(ConditionalCommand ast) {
		ast.getExpression().visit(this);
		if(!ast.getExpression().getType().equals(OpType.bool)) {
			//TODO erro, express�o deve retornar um valor l�gico
			System.err.print("Erro ConditionalCommand");
			System.out.println(ast);
		}
		ast.getCommand(true).visit(this);
		if(ast.getCommand(false)!=null)
			ast.getCommand(false).visit(this);

	}

	@Override
	public void visitIterativeCommand(IterativeCommand ast) {
		ast.getExpression().visit(this);
		if(!ast.getExpression().getType().equals(OpType.bool)) {
			//TODO erro, express�o deve retornar um valor l�gico
			System.err.print("Erro IterativeCommand");
			System.out.println(ast);
		}
		ast.getCommand().visit(this);
	}

	@Override
	public void visitMultiCommand(MultiCommand ast) {
		for (Object i : ast.getCommand().toArray())
			((Command)i).visit(this);

	}

	@Override
	public void visitDeclaration(Declaration ast) {
		for (Object i : ast.getId().toArray()) {
			if(identifierTable.enter((String) i, ast)) {
				// TODO Tratar nome j� declarado
				System.err.print("Erro Declaration");
				System.out.println(ast);
			}
		}
		
		ast.getType().visit(this);
	}

	@Override
	public void visitExpression(Expression ast) {
		for (SimpleExpression i : ast.getA()) {
			if(i != null)
				i.visit(this);
		}
		if(ast.getOp()!=null) {
			ast.getOp().visit(this);
			
			Type[] operands = new Type[2];
			Op op = ast.getOp();
			
			operands[0] = ast.getA(0).getType();
			operands[1] = ast.getA(1).getType();
			OpType opt = OpType.search(op.getOp(), operands);
			op.setOpType(opt);
			if(opt.ret==null){
				//TODO opera��o n�o existe
				System.err.print("Erro Expression");
				System.out.println(ast);
			}
		}

	}

	@Override
	public void visitSimpleExpression(SimpleExpression ast) {
		for (Object i : ast.getA().toArray()) {
			((Term)i).visit(this);
		}
		
		if(ast.getOp().size()>0) {
			Type[] operands = new Type[2];
			Op op = ast.getOp(ast.getOp().size()-1);
			op.visit(this);
			
			operands[0] = ast.getA(ast.getOp().size()-1).getType();
			operands[1] = ast.getA(ast.getOp().size()).getType();
			OpType opt = OpType.search(op.getOp(), operands);
			op.setOpType(opt);
			if(opt.ret==null){
				//TODO opera��o n�o existe
				System.err.print("Erro SimpleExpression");
				System.out.println(ast);
			}
			for(int i=ast.getOp().size()-2;i>=0;i--) {
				op = ast.getOp(i);
				op.visit(this);
				operands[0] = ast.getA(i).getType();
				operands[1] = ast.getOp(i+1).getType();
				op.setOpType(opt);
				if(opt.ret==null){
					//TODO opera��o n�o existe
					System.err.print("Erro SimpleExpression");
					System.out.println(ast);
				}
			}
		}
		
	}

	@Override
	public void visitTerm(Term ast) {
		for (Object i : ast.getA().toArray()) {
			((Factor)i).visit(this);
		}
		
		if(ast.getOp().size()>0) {
			Type[] operands = new Type[2];
			Op op = ast.getOp(0);
			op.visit(this);
			
			operands[0] = ast.getA(0).getType();
			
			operands[1] = ast.getA(1).getType();
			OpType opt = OpType.search(op.getOp(), operands);
			op.setOpType(opt);
			if(opt.ret==null){
				//TODO opera��o n�o existe
				System.err.print("Erro Term");
				System.out.println(ast);
			}
			for(int i=1;i<ast.getOp().size();i++) {
				op = ast.getOp(i);
				op.visit(this);
				
				operands[0] = ast.getOp(i-1).getType();
				operands[1] = ast.getA(i+1).getType();
				
				opt = OpType.search(op.getOp(), operands);
				op.setOpType(opt);
				if(opt.ret==null){
					//TODO opera��o n�o existe
					System.err.print("Erro Term");
					System.out.println(ast);
				}
			}
		}

	}

	@Override
	public void visitProgram(Program ast) {
		for( Object i : ast.getDeclaration().toArray()) {
			((Declaration)i).visit(this);
		}
		ast.getMc().visit(this);
	}

	@Override
	public void visitPrimitiveType(PrimitiveType ast) {
	}

	@Override
	public void visitVariable(Variable ast){
		Declaration dec = this.identifierTable.retrieve(ast.getId());
		if(dec != null)
			ast.setDeclaration(dec);
		else {
			//TODO quando ele retornar declara��o nula, ele estar� referenciando uma variavel n�o declarada 
			System.err.print("Erro Variable");
			System.out.println(ast);
		}
		for(Object i : ast.getIndexer().toArray()) {
			((Expression)i).visit(this);
			if(!((Expression)i).getType().equals(OpType.integ)) {
				System.err.print("Erro Indexer");
				System.out.println(i);
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
	
	public void check(AST ast) {
		ast.visit(this);
	}

}
