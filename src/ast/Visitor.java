package ast;

public interface Visitor {

	public void visitAggregateType(AggregateType ast);

	public void visitAssignmentCommand(AssignmentCommand ast);

	public void visitConditionalCommand(ConditionalCommand ast);

	public void visitIterativeCommand(IterativeCommand ast);

	public void visitMultiCommand(MultiCommand ast);

	public void visitDeclaration(Declaration ast);

	public void visitExpression(Expression ast);

	public void visitSimpleExpression(SimpleExpression ast);

	public void visitTerm(Term ast);

	public void visitProgram(Program ast);

	public void visitPrimitiveType(PrimitiveType ast);

	public void visitVariable(Variable ast);

	public void visitOpAd(OpAd ast);

	public void visitOpMul(OpMul ast);

	public void visitOpRel(OpRel ast);

	public void visitBoolLit(BoolLit ast);

	public void visitFloatLit(FloatLit ast);

	public void visitIntLit(IntLit ast);

}
