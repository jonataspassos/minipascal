package coder;

import ast.AST;
import ast.AggregateType;
import ast.AssignmentCommand;
import ast.BoolLit;
import ast.Command;
import ast.ConditionalCommand;
import ast.Declaration;
import ast.Expression;
import ast.FloatLit;
import ast.IntLit;
import ast.IterativeCommand;
import ast.MultiCommand;
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
import checker.OpType;
import function.JD3String;

public class Coder implements Visitor {
	private Instructions instructions = new TAMIntructions();
	private String code = "";
	private int ST = 0;

	@Override
	public void visitAggregateType(AggregateType ast) {
		code += instructions.getPush(ast.size(),null);
		ST+=ast.size();
	}

	@Override
	public void visitAssignmentCommand(AssignmentCommand ast) {
		code += instructions.comment("AssignmentCommand "+ast);
		ast.getExpression().visit(this);//Evaluate Expression
		this.varMode = true;//store
		ast.getVariable().visit(this);
	}

	@Override
	public void visitConditionalCommand(ConditionalCommand ast) {
		code += instructions.comment("ConditionalCommand");
		ast.getExpression().visit(this);//Evaluate Expression
		String lbEndIf = createLabel();
		code +=instructions.getJumpIf((byte)0, 0, lbEndIf," if expression than");
		ast.getCommand(true).visit(this);//Execute
		
		if(ast.hasElseCommand()) {
			String lbEndElse = createLabel();
			code +=instructions.getJump(0, lbEndElse,"");
			code += instructions.applyLabel(lbEndIf,"else");
			ast.getCommand(false).visit(this);//Execute
			code += instructions.applyLabel(lbEndElse,"endIf");
			
		}else {
			code += instructions.applyLabel(lbEndIf,"endIf");
		}
		
		
	}

	@Override
	public void visitIterativeCommand(IterativeCommand ast) {
		code += instructions.comment("IterativeCommand");
		String lbStartWhile = createLabel();
		String lbEndWhile = createLabel();
		code += instructions.applyLabel(lbStartWhile,"while");
		ast.getExpression().visit(this);//Evaluate Expression
		code +=instructions.getJumpIf((byte)0, 0, lbEndWhile,"");
		code += instructions.comment("do");
		ast.getCommand().visit(this);//Execute
		code +=instructions.getJump(0, lbStartWhile,"");
		code += instructions.applyLabel(lbEndWhile,"endWhile");
		

	}

	@Override
	public void visitMultiCommand(MultiCommand ast) {
		code += instructions.comment("MultiCommand");
		code += instructions.comment("begin");
		for (Command command : ast.getCommand()) {
			command.visit(this);//Execute
			code += "\n";
		}
		code += instructions.comment("end");
		

	}

	@Override
	public void visitDeclaration(Declaration ast) {
		ast.setAddress(ST);
		for(String id :ast.getId()) {
			ast.getType().visit(this);
			
			code+=instructions.comment(id+" : "+ast.getType());
		}

	}

	@Override
	public void visitExpression(Expression ast) {
		ast.getA(0).visit(this);
		if(ast.getOp()!=null) {
			ast.getA(1).visit(this);
			ast.getOp().visit(this);
		}

	}

	@Override
	public void visitSimpleExpression(SimpleExpression ast) {
		ast.getA(0).visit(this);
		int i=0;
		for(OpAd op:ast.getOp()) {
			i++;
			ast.getA(i).visit(this);
			op.visit(this);
		}
	}

	@Override
	public void visitTerm(Term ast) {
		this.varMode=false;
		ast.getA(0).visit(this);
		int i=0;
		for(OpMul op:ast.getOp()) {
			i++;
			this.varMode=false;
			ast.getA(i).visit(this);
			op.visit(this);
		}
	}

	@Override
	public void visitProgram(Program ast) {
		code += instructions.comment("program "+ast.getId());
		code += instructions.comment("declarations");
		for(Declaration declaration: ast.getDeclaration()) {
			declaration.visit(this);
		}
		ast.getMc().visit(this);
		code += instructions.comment("endProgram");
	}

	@Override
	public void visitPrimitiveType(PrimitiveType ast) {
		code += instructions.getPush(ast.size(),null);
		ST+=ast.size();
	}
	
	/**
	 * false = LoadMode; true = StoreMode
	 * */
	private boolean varMode = false;//false = LoadMode; true = StoreMode

	@Override
	public void visitVariable(Variable ast) {
		boolean varMode = this.varMode;
		
		Type t = ast.getDeclaration().getType();
		if(t instanceof AggregateType) {
			int addr = ast.getAddress();
			while(t instanceof AggregateType) {
				addr -=((AggregateType)t).getIndex(0)*((AggregateType)t).getType().size();
				t = ((AggregateType)t).getType();
			}
			
			
			code+=instructions.getLoadA(addr, "SB", "");
			t = ast.getDeclaration().getType();
			for(Expression index : ast.getIndexer()) {
				code+=instructions.comment("Index");
				index.visit(this);
				
				String indexValid = createLabel();
				String indexInvalid = createLabel();
				code+=instructions.comment("");
				code+=instructions.comment("Index Validation");
				code+=instructions.getLoad((byte)PrimitiveType.sInt, -PrimitiveType.sInt, "ST", "copy index");
				code+=instructions.getLoadL(((AggregateType)t).getIndex(0), "First index");
				code += instructions.getCall("SB", 0, "geq", ""); 
				code +=instructions.getJumpIf((byte)0, 0, indexInvalid," if not valid");
				code+=instructions.getLoad((byte)PrimitiveType.sInt, -PrimitiveType.sInt, "ST", "copy index");
				code+=instructions.getLoadL(((AggregateType)t).getIndex(1), "Last index");
				code += instructions.getCall("SB", 0, "leq", "");
				code +=instructions.getJumpIf((byte)1, 0, indexValid," if valid");
				code +=instructions.applyLabel(indexInvalid, "");
				code +=instructions.getHalt("");
				code +=instructions.applyLabel(indexValid, "");
				
				code+=instructions.comment("Index Calculate");
				t = ((AggregateType)t).getType();
				code +=instructions.getLoadL(t.size(), "tamanho de cada posi��o");
				code += instructions.getCall("SB", 0, "mul", "com o indice calculado");
				code += instructions.getCall("SB", 0, "sum", "somando endere�o relativo");
			}
			code+=instructions.comment("");
			if(varMode) {
				code+=instructions.getStoreI((byte)ast.size(), ""+ast);
			}else {
				code+=instructions.getLoadI((byte)ast.size(), ""+ast);
			}
			
		}else {
			if(varMode) {
				code+=instructions.getStore((byte)ast.size(), ast.getAddress(), "SB", ""+ast);
			}else {
				code+=instructions.getLoad((byte)ast.size(), ast.getAddress(), "SB", ""+ast);
			}
		}
	}

	@Override
	public void visitOpAd(OpAd ast) {
		code += instructions.comment(ast.getOpType().sample());
		code += ast.getOpType().instruction("SB");
	}

	@Override
	public void visitOpMul(OpMul ast) {
		code += instructions.comment(ast.getOpType().sample());
		code += ast.getOpType().instruction("SB");
	}

	@Override
	public void visitOpRel(OpRel ast) {
		code += instructions.comment(ast.getOpType().sample());	
		code += ast.getOpType().instruction("SB");
	}

	@Override
	public void visitBoolLit(BoolLit ast) {
		code += instructions.getLoadL(ast.isValue()?1:0, "");

	}

	@Override
	public void visitFloatLit(FloatLit ast) {
		code += instructions.getLoadL(ast.getValue(), "");

	}

	@Override
	public void visitIntLit(IntLit ast) {
		code += instructions.getLoadL(ast.getValue(), "");

	}
	
	
	private String createLabel() {
		return this.instructions.createLabel();
	}

	public void code(AST program,String path) {
		OpType.setInstructions(instructions);
		this.code = "";
		program.visit(this);
		JD3String.fileOut(path, this.code); 
		
	}

}
