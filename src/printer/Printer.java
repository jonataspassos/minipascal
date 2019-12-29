package printer;

import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import ast.*;
import formatter.Formatter;
import formatter.PascalFormater;
import function.JD3String;
import parser.Parser;
import visualization.Visualization;
import select.AbstractSelect;
import select.Select;

public class Printer implements Visitor {

	private static final int heightTextRect = 18;
	private static final int heightSpace = 10;
	private static final int widthSpaceLine = 3;
	private static final String title = ".::MiniPascal Printer::.";

	private Visualization vis;
	private Select select;
	private int windowW, windowH;
	private int width = 0;
	private int height = 0;
	private boolean bounding = true;

	public static void main(String args[]) throws Exception {
		String path = "src\\files\\grammar-tokens.tkn";
		String src = "src\\files\\sc1.pas";
		Printer p = new Printer();
		Parser parser = new Parser(path, src);

		AST program = parser.parse();
		Formatter f = new PascalFormater();
		String formated = f.format(program);

		p.print(program, "sc1.html");
	}

	public Printer() throws Exception {
		this(640, 480);
	}

	public Printer(int width, int height) throws Exception {
		super();
		this.vis = new Visualization(title, width, height, ".expression");
		
		this.windowW = width;
		this.windowH = height;

		this.vis.tStyle().append("text").attr("text-anchor", "middle");

		this.vis.tStyle().append("line").attr("stroke", "black").attr("stroke-width", "1px");

		this.vis.tStyle().append(".array>rect").attr("fill", "aqua");
		this.vis.tStyle().append(".integer>rect").attr("fill", "dodgerblue");
		this.vis.tStyle().append(".integer>text").attr("fill", "white").attr("stroke", "black").attr("stroke-width",
				"0.2px");
		this.vis.tStyle().append(".real>rect").attr("fill", "darkgreen");
		this.vis.tStyle().append(".real>text").attr("fill", "white").attr("stroke", "black").attr("stroke-width",
				"0.2px");
		this.vis.tStyle().append(".boolean>rect").attr("fill", "blueviolet");
		this.vis.tStyle().append(".boolean>text").attr("fill", "white").attr("stroke", "black").attr("stroke-width",
				"0.2px");
		this.vis.tStyle().append(".none>rect").attr("fill", "gray");
		this.vis.tStyle().append(".variable>rect").attr("fill", "coral");

		this.vis.tStyle().append(".expression").attr("stroke", "black").attr("stroke-width", "0.2px");
		this.vis.tStyle().append(".expression>rect").attr("fill", "antiquewhite");
		this.vis.tStyle().append(".op>rect").attr("opacity", "0.8");
		this.vis.tStyle().append(".id>rect").attr("fill", "lightgray");
		this.vis.tStyle().append(".declaration>rect").attr("fill", "darkseagreen");
		this.vis.tStyle().append(".command>rect").attr("fill", "goldenrod");
		this.vis.tStyle().append(".program>rect").attr("fill", "rebeccapurple");

		this.select = this.vis.tDynamic();
	}

	public void fileOut(String path) {
		this.vis.fileOut(path);
	}

	private static final int widthAggregateType = 90;
	private static final int heightAggregateType = 50;

	@Override
	public void visitAggregateType(AggregateType ast) {
		Select s = this.select.append("g").attr("class", "array");
		int width = widthAggregateType;

		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightAggregateType);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ".3em").innerText("array");

		s.append("text").attr("y", heightTextRect * 3 / 2 - 1).attr("dy", ".3em").attr("x", "-7")
				.attr("style", "text-anchor: end").innerText("" + ast.getIndex(0));
		s.append("text").attr("y", heightTextRect * 3 / 2 - 1).attr("dy", ".3em").innerText("~");
		s.append("text").attr("y", heightTextRect * 3 / 2 - 1).attr("dy", ".3em").attr("x", "7")
				.attr("style", "text-anchor: start").innerText("" + ast.getIndex(1));

		s.append("text").attr("y", heightTextRect * 5 / 2 - 3).attr("dy", ".3em").innerText("Of");
		s.append("line").attr("x1", 0).attr("x2", 0).attr("y1", heightAggregateType).attr("y2",
				heightAggregateType + heightSpace);

		Select select = this.select;

		this.select = s.append("g").attr("transform", "translate(0," + (heightAggregateType + heightSpace) + ")");
		ast.getType().visit(this);

		this.width = width < this.width ? this.width : width;
		this.height = this.height + heightAggregateType + heightSpace;
		this.select = select;
	}

	private static final int widthAssignmentCommand = 60;

	@Override
	public void visitAssignmentCommand(AssignmentCommand ast) {
		Select s = this.select.append("g").attr("class", "assignment command");

		Select background = s.append("rect").attr("style", "fill:white;opacity:0.7;");

		Select var = s.append("g");
		this.select = var;
		ast.getVariable().visit(this);
		int varW = this.width;
		int varH = this.height;

		Select exp = s.append("g");
		exp.append("line").attr("x1", -heightSpace / 2).attr("x2", 0).attr("y1", 0).attr("y2", 0);
		exp.append("line").attr("x1", -heightSpace / 2).attr("x2", -heightSpace / 2).attr("y1", -heightExpression / 2)
				.attr("y2", 0);
		this.select = exp;
		this.bounding = true;
		ast.getExpression().visit(this);
		int expW = this.width;
		int expH = this.height;

		int width = varW + expW + heightSpace * 3 / 2;
		width = width > widthAssignmentCommand ? width : widthAssignmentCommand;

		var.attr("transform", "translate(" + (varW / 2) + "," + (heightTextRect / 2 + heightSpace) + ")");
		exp.attr("transform",
				"translate(" + (width - expW) + "," + (heightTextRect / 2 + heightSpace + heightExpression / 2) + ")");

		s.append("rect").attr("y", -heightTextRect / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("x", width / 2).attr("dy", ".3em").innerText("... := ...");

		s.append("line").attr("x1", width / 2 - 17).attr("x2", varW / 2).attr("y1", heightTextRect / 2).attr("y2",
				heightSpace + heightTextRect / 2);
		s.append("line").attr("x1", width / 2 + 18).attr("x2", width - expW - heightSpace / 2)
				.attr("y1", heightTextRect / 2).attr("y2", heightSpace + heightTextRect / 2);

		this.width = width;

		this.height = varH > expH ? varH : expH;
		this.height += heightSpace + heightTextRect;

		background.attr("y", -heightTextRect / 2).attr("width", this.width).attr("height", this.height);
	}

	private static final int widthConditionalElseCommand = 125;
	private static final int widthConditionalCommand = 80;

	@Override
	public void visitConditionalCommand(ConditionalCommand ast) {
		Select s = this.select.append("g").attr("class", "conditional command");

		Select background = s.append("rect").attr("style", "fill:white;opacity:0.7;");

		boolean tElse = false;

		Select exp = s.append("g");
		exp.append("line").attr("x1", -heightSpace / 2).attr("x2", 0).attr("y1", 0).attr("y2", 0);
		exp.append("line").attr("x1", -heightSpace / 2).attr("x2", -heightSpace / 2).attr("y1", -heightExpression / 2)
				.attr("y2", 0);
		this.select = exp;
		this.bounding = true;
		ast.getExpression().visit(this);
		int expW = this.width;
		int expH = this.height;

		Select comIf = s.append("g");
		comIf.append("line").attr("x1", -heightSpace / 2).attr("x2", 0).attr("y1", 0).attr("y2", 0);
		comIf.append("line").attr("x1", -heightSpace / 2).attr("x2", -heightSpace / 2).attr("y1", -heightTextRect / 2)
				.attr("y2", 0);
		this.select = comIf;
		ast.getCommand(true).visit(this);
		int comIfW = this.width;
		int comIfH = this.height;
		int comElseW = 0, comElseH = 0;
		Select comElse = null;

		int width = comIfW + expW + heightSpace * 2;

		if (ast.getCommand(false) != null) {
			tElse = true;
			comElse = s.append("g");
			comElse.append("line").attr("x1", -heightSpace / 2).attr("x2", 0).attr("y1", 0).attr("y2", 0);
			comElse.append("line").attr("x1", -heightSpace / 2).attr("x2", -heightSpace / 2)
					.attr("y1", -heightTextRect / 2).attr("y2", 0);
			this.select = comElse;
			ast.getCommand(false).visit(this);
			comElseW = this.width;
			comElseH = this.height;

			width += comElseW + heightSpace;
		}

		if (tElse)
			width = width > widthConditionalElseCommand ? width : widthConditionalElseCommand;
		else
			width = width > widthConditionalCommand ? width : widthConditionalCommand;

		exp.attr("transform",
				"translate(" + (heightSpace) + "," + (heightTextRect / 2 + heightSpace + heightExpression / 2) + ")");
		comIf.attr("transform", "translate(" + (width - comIfW + (tElse ? (-comElseW - heightSpace) : 0)) + ","
				+ (heightTextRect / 2 + heightSpace + heightTextRect / 2) + ")");
		if (tElse) {
			comElse.attr("transform", "translate(" + (width - comElseW) + ","
					+ (heightTextRect / 2 + heightSpace + heightTextRect / 2) + ")");
		}

		s.append("rect").attr("y", -heightTextRect / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("x", width / 2).attr("dy", ".3em")
				.innerText("if ... then ..." + (tElse ? " else ..." : ""));

		s.append("line").attr("x1", width / 2 + (tElse ? -40 : -16)).attr("x2", heightSpace / 2)
				.attr("y1", heightTextRect / 2).attr("y2", heightSpace + heightTextRect / 2);
		s.append("line").attr("x1", width / 2 + (tElse ? 8 : 31))
				.attr("x2", width - comIfW + (tElse ? (-comElseW - heightSpace) : 0) - heightSpace / 2)
				.attr("y1", heightTextRect / 2).attr("y2", heightSpace + heightTextRect / 2);
		if (tElse) {
			s.append("line").attr("x1", width / 2 + 55).attr("x2", width - comElseW - heightSpace / 2)
					.attr("y1", heightTextRect / 2).attr("y2", heightSpace + heightTextRect / 2);
		}

		this.width = width;

		this.height = comIfH > expH ? comIfH : expH;
		if (tElse)
			this.height = this.height > comElseH ? this.height : comElseH;
		this.height += heightSpace + heightTextRect;

		background.attr("y", -heightTextRect / 2).attr("width", this.width).attr("height", this.height);

	}

	private static final int widthIterativeCommand = 95;

	@Override
	public void visitIterativeCommand(IterativeCommand ast) {
		Select s = this.select.append("g").attr("class", "iterative command");

		Select background = s.append("rect").attr("style", "fill:white;opacity:0.7;");

		Select exp = s.append("g");
		exp.append("line").attr("x1", -heightSpace / 2).attr("x2", 0).attr("y1", 0).attr("y2", 0);
		exp.append("line").attr("x1", -heightSpace / 2).attr("x2", -heightSpace / 2).attr("y1", -heightExpression / 2)
				.attr("y2", 0);
		this.select = exp;
		this.bounding = true;
		ast.getExpression().visit(this);
		int expW = this.width;
		int expH = this.height;

		Select com = s.append("g");
		com.append("line").attr("x1", -heightSpace / 2).attr("x2", 0).attr("y1", 0).attr("y2", 0);
		com.append("line").attr("x1", -heightSpace / 2).attr("x2", -heightSpace / 2).attr("y1", -heightTextRect / 2)
				.attr("y2", 0);
		this.select = com;
		ast.getCommand().visit(this);
		int comW = this.width;
		int comH = this.height;

		int width = comW + expW + heightSpace * 2;
		width = width > widthIterativeCommand ? width : widthIterativeCommand;

		exp.attr("transform",
				"translate(" + (heightSpace) + "," + (heightTextRect / 2 + heightSpace + heightExpression / 2) + ")");
		com.attr("transform",
				"translate(" + (width - comW) + "," + (heightTextRect / 2 + heightSpace + heightTextRect / 2) + ")");

		s.append("rect").attr("y", -heightTextRect / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("x", width / 2).attr("dy", ".3em").innerText("while ... do ...");

		s.append("line").attr("x1", width / 2 + 2).attr("x2", heightSpace / 2).attr("y1", heightTextRect / 2).attr("y2",
				heightSpace + heightTextRect / 2);
		s.append("line").attr("x1", width / 2 + 38).attr("x2", width - comW - heightSpace / 2)
				.attr("y1", heightTextRect / 2).attr("y2", heightSpace + heightTextRect / 2);

		this.width = width;

		this.height = comH > expH ? comH : expH;
		this.height += heightSpace + heightTextRect;

		background.attr("y", -heightTextRect / 2).attr("width", this.width).attr("height", this.height);

	}

	private static final int widthMultiCommand = 90;

	@Override
	public void visitMultiCommand(MultiCommand ast) {
		Select s = this.select.append("g").attr("class", "multi command");

		Select background = s.append("rect").attr("style", "fill:white;opacity:0.7;");

		Select commands = s.append("g").attr("transform",
				"translate(" + (heightSpace) + "," + (heightTextRect / 2) + ")");

		int width = widthMultiCommand;
		int height = 0;
		for (int i = 0; i < ast.getCommand().size(); i++) {
			Command c = ast.getCommand(i);
			height += heightSpace;
			Select command = commands.preppend("g").attr("transform",
					"translate(" + (widthSpaceLine * i) + "," + (height + heightTextRect / 2) + ")");
			command.append("line").attr("x1", -heightSpace / 2).attr("x2", 0).attr("y1", 0).attr("y2", 0);
			command.append("line").attr("x1", -heightSpace / 2).attr("x2", -heightSpace / 2)
					.attr("y1", -height - heightTextRect / 2).attr("y2", 0);

			this.select = command;
			c.visit(this);
			height += this.height;
			width = width > (heightSpace + widthSpaceLine * i + this.width) ? width
					: (heightSpace + widthSpaceLine * i + this.width);
		}

		s.append("rect").attr("y", -heightTextRect / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("x", width / 2).attr("dy", ".3em").innerText("begin ... end");

		this.height = height + heightSpace + heightTextRect;
		this.width = width;

		background.attr("y", -heightTextRect / 2).attr("width", this.width).attr("height", this.height);
	}

	private static final int widthDeclaration = 70;

	@Override
	public void visitDeclaration(Declaration ast) {
		Select s = this.select.append("g").attr("class", "declaration");

		Select background = s.append("rect").attr("style", "fill:white;opacity:0.7;");

		Select ids = s.append("g");

		this.select = ids;
		visitListId(ast.getId());
		int idW = this.width;
		int idH = this.height;

		Select type = s.append("g");

		this.select = type;
		ast.getType().visit(this);
		int tyW = this.width;
		int tyH = this.height;

		int width = idW + tyW + heightSpace;
		width = width > widthDeclaration ? width : widthDeclaration;

		ids.attr("transform", "translate(" + (idW / 2) + "," + (heightTextRect / 2 + heightSpace) + ")");
		type.attr("transform", "translate(" + (width - tyW / 2) + "," + (heightTextRect / 2 + heightSpace) + ")");

		s.append("rect").attr("y", -heightTextRect / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("x", width / 2).attr("dy", ".3em").innerText("var ... : ...");

		s.append("line").attr("x1", width / 2).attr("x2", idW / 2).attr("y1", heightTextRect / 2).attr("y2",
				heightTextRect / 2 + heightSpace);
		s.append("line").attr("x1", width / 2 + 25).attr("x2", width - tyW / 2).attr("y1", heightTextRect / 2)
				.attr("y2", heightTextRect / 2 + heightSpace);

		this.width = width;

		this.height = tyH > idH ? tyH : idH;
		this.height += heightSpace + heightTextRect;

		background.attr("y", -heightTextRect / 2).attr("width", this.width).attr("height", this.height);
	}

	private static final int widthId = 22;

	private void visitListId(ArrayList<String> li) {

		int gw = JD3String.greatWidth(li);

		int size = li.size();
		float w = JD3String.width(li.get(gw));
		int width = widthId;
		if (w > widthId) {
			if (w < widthId * 2)
				width = widthId * 2;
			else
				width = widthId * 3;
		}
		w = width;
		width = width + widthSpaceLine * (size - 1);

		Select s = this.select.append("g").attr("class", "list-id").attr("transform",
				"translate(" + (-width / 2) + ",0)");
		s.append("line").attr("x1", "0").attr("x2", width).attr("y1", 0).attr("y2", 0);
		for (int i = li.size() - 1; i >= 0; i--) {
			Select id = s.append("g").attr("class", "id").attr("transform",
					"translate(" + (w / 2 + widthSpaceLine * i) + "," + (heightSpace + (heightTextRect + 2) * i) + ")");
			id.append("line").attr("x1", "0").attr("x2", "0").attr("y2", "0").attr("y1",
					-(heightSpace + (heightTextRect + 2) * i));
			id.append("rect").attr("x", -w / 2).attr("width", w).attr("height", heightTextRect);
			id.append("text").attr("y", heightTextRect / 2).attr("dy", ".25em")
					.innerText(JD3String.filter(li.get(i), w));
		}

		this.width = width;
		this.height = heightSpace + (heightTextRect + 2) * size - 2;
	}

	private static final int widthExpression = 22 + widthSpaceLine;
	private static final int heightExpression = 22;

	@Override
	public void visitExpression(Expression ast) {
		Select s = this.select.append("g").attr("class", "expression");

		if (bounding) {
			s.append("rect").attr("y", -heightExpression / 2)
					.attr("width", widthExpression).attr("height", heightExpression);
			s.append("g").attr("class", "none").append("rect").attr("y", -heightExpression / 2)
					.attr("width", widthSpaceLine).attr("height", heightExpression);
			
			Select exp = s.append("g");
			this.select = exp;
			this.bounding = false;
			ast.visit(this);

			float expW = (widthExpression-widthSpaceLine)*15/16;
			float expH = heightExpression*3/4;
			float scale = (expW/this.width)<(expH/this.height)?(expW/this.width):(expH/this.height);
			exp.attr("transform", "translate("+(widthExpression+widthSpaceLine)/2+","+(-expH/2)+")scale("+scale+","+scale+")");
			
			this.width = widthExpression;
			this.height = heightExpression;
		} else {
			int indexSimpleExpression = this.indexSimpleExpression;
			int indexTerm = this.indexTerm;
			
			this.indexSimpleExpression = 0;
			this.indexTerm = 0;
			
			if(ast.getOp()==null) {
				this.select = s;
				ast.getA(0).visit(this);
			}else {
				Select operator = s.append("g");
				this.select = operator;
				ast.getOp().visit(this);
				int opW = this.width;
				int opH = this.height;

				Select a0 = s.append("g");
				this.select = a0;
				
				
				
				ast.getA(0).visit(this);// SimpleExpression
				int a0W = this.width;
				int a0H = this.height;

				Select a1 = s.append("g");
				this.select = a1;
				
				ast.getA(1).visit(this);// SimpleExpression
				int a1W = this.width;
				int a1H = this.height;
				

				this.width = a0W + a1W + heightSpace;
				this.width = this.width > opW ? this.width : opW;

				this.height = a0H > a1H ? a0H : a1H;
				this.height += heightSpace + opH;

				a0.attr("transform", "translate(" + (a0W - width) / 2 + "," + (opH + heightSpace) + ")");
				a1.attr("transform", "translate(" + (width - a1W) / 2 + "," + (opH + heightSpace) + ")");
				s.append("line").attr("x1", 0).attr("x2", (a0W - width) / 2).attr("y1", opH).attr("y2", opH + heightSpace);
				s.append("line").attr("x1", 0).attr("x2", (width - a1W) / 2).attr("y1", opH).attr("y2", opH + heightSpace);
			}

			this.indexSimpleExpression = indexSimpleExpression;
			this.indexTerm = indexTerm;
		}

		this.bounding = false;
	}

	private int indexSimpleExpression = 0;

	@Override
	public void visitSimpleExpression(SimpleExpression ast) {
		Select s = this.select.append("g").attr("class", "simple-expression none");
		if (ast.getOp().size() == indexSimpleExpression) {
			this.select = s;
			ast.getA(indexSimpleExpression).visit(this);// Term
		} else {

			Select operator = s.append("g");
			this.select = operator;
			ast.getOp(indexSimpleExpression).visit(this);
			int opW = this.width;
			int opH = this.height;

			Select a0 = s.append("g");
			this.select = a0;
			ast.getA(indexSimpleExpression).visit(this);// Term
			int a0W = this.width;
			int a0H = this.height;

			Select a1 = s.append("g");
			this.select = a1;
			indexSimpleExpression++;
			ast.visit(this);// Restante da expressão simples
			int a1W = this.width;
			int a1H = this.height;
			indexSimpleExpression--;

			this.width = a0W + a1W + heightSpace;
			this.width = this.width > opW ? this.width : opW;

			this.height = a0H > a1H ? a0H : a1H;
			this.height += heightSpace + opH;

			a0.attr("transform", "translate(" + (a0W - width) / 2 + "," + (opH + heightSpace) + ")");
			a1.attr("transform", "translate(" + (width - a1W) / 2 + "," + (opH + heightSpace) + ")");
			s.append("line").attr("x1", 0).attr("x2", (a0W - width) / 2).attr("y1", opH).attr("y2", opH + heightSpace);
			s.append("line").attr("x1", 0).attr("x2", (width - a1W) / 2).attr("y1", opH).attr("y2", opH + heightSpace);
		}
	}

	private int indexTerm = 0;

	@Override
	public void visitTerm(Term ast) {
		Select s = this.select.append("g").attr("class", "term none");

		if (ast.getOp().size() == indexTerm) {
			this.select = s;
			ast.getA(ast.getOp().size()-indexTerm).visit(this);// Factor : literal, expression, variable
		} else {

			Select operator = s.append("g");
			this.select = operator;
			System.out.println(ast);
			System.out.println(ast.getOp().size());
			System.out.println(indexTerm);
			ast.getOp(ast.getOp().size()-indexTerm-1).visit(this);
			int opW = this.width;
			int opH = this.height;
			
			Select a0 = s.append("g");
			this.select = a0;
			indexTerm++;
			ast.visit(this);// Restante do termo
			int a0W = this.width;
			int a0H = this.height;
			indexTerm--;

			Select a1 = s.append("g");
			this.select = a1;
			ast.getA(ast.getOp().size()-indexTerm).visit(this);// Factor : literal, expression, variable
			int a1W = this.width;
			int a1H = this.height;

			this.width = a0W + a1W + heightSpace;
			this.width = this.width > opW ? this.width : opW;

			this.height = a0H > a1H ? a0H : a1H;
			this.height += heightSpace + opH;

			a0.attr("transform", "translate(" + (a0W - width) / 2 + "," + (opH + heightSpace) + ")");
			a1.attr("transform", "translate(" + (width - a1W) / 2 + "," + (opH + heightSpace) + ")");
			s.append("line").attr("x1", 0).attr("x2", (a0W - width) / 2).attr("y1", opH).attr("y2", opH + heightSpace);
			s.append("line").attr("x1", 0).attr("x2", (width - a1W) / 2).attr("y1", opH).attr("y2", opH + heightSpace);
		}

	}

	private static final int widthProgram = 60;

	@Override
	public void visitProgram(Program ast) {
		Select s = this.select.append("g").attr("class", "program");
		int width = 0;
		int height = 0;

		Select declarations = s.append("g");
		this.select = declarations;
		visitListDeclaration(ast.getDeclaration());
		int decW = this.width;
		width += this.width;
		height = height > this.height ? height : this.height;

		Select multiCommands = s.append("g");
		multiCommands.append("line").attr("x1", -heightSpace / 2).attr("x2", 0).attr("y1", 0).attr("y2", 0);
		multiCommands.append("line").attr("x1", -heightSpace / 2).attr("x2", -heightSpace / 2)
				.attr("y1", -heightTextRect / 2).attr("y2", 0);
		this.select = multiCommands;
		ast.getMc().visit(this);
		int comW = this.width;
		width += this.width;
		height = height > this.height ? height : this.height;

		width += heightSpace;
		int widthRect = widthProgram;

		String inner = ast.getId();
		float w = JD3String.width(inner);
		if (w > widthProgram) {
			if (w < widthProgram * 2)
				widthRect = widthProgram * 2;
			else
				widthRect = widthProgram * 3;
		}
		inner = JD3String.filter(inner, widthRect);
		width = width > widthRect ? width : widthRect;

		s.append("rect").attr("x", -widthRect / 2).attr("width", widthRect).attr("height",
				heightTextRect * 2 + widthSpaceLine);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ".3em").innerText("program");
		s.append("text").attr("y", heightTextRect * 3 / 2 + widthSpaceLine).attr("dy", ".3em").innerText(inner);

		declarations.attr("transform", "translate(" + (-width / 2 + decW / 2) + ","
				+ (heightTextRect * 2 + widthSpaceLine + heightSpace * 2) + ")");
		multiCommands.attr("transform", "translate(" + (width / 2 - comW) + ","
				+ (heightTextRect * 2 + widthSpaceLine + heightSpace * 2 + heightTextRect / 2) + ")");

		s.append("line").attr("x1", "0").attr("x2", -width / 2 + decW / 2)
				.attr("y1", heightTextRect * 2 + widthSpaceLine)
				.attr("y2", heightTextRect * 2 + widthSpaceLine + heightSpace * 2);
		s.append("line").attr("x1", "0").attr("x2", width / 2 - comW - heightSpace / 2)
				.attr("y1", heightTextRect * 2 + widthSpaceLine)
				.attr("y2", heightTextRect * 2 + widthSpaceLine + heightSpace * 2);

		this.width = width;
		this.height = height;

	}

	private static final int widthListDeclaration = 30;

	private void visitListDeclaration(ArrayList<Declaration> ld) {
		Select s = this.select.append("g").attr("class", "list-declaration");

		Select declarations = s.append("g").attr("transform", "translate(" + (heightSpace) + ",0)");

		int width = widthListDeclaration;
		int height = 0;
		for (int i = 0; i < ld.size(); i++) {
			Declaration d = ld.get(i);
			height += heightSpace;
			Select declaration = declarations.preppend("g").attr("transform",
					"translate(" + (widthSpaceLine * i) + "," + (height + heightTextRect / 2) + ")");
			declaration.append("line").attr("x1", -heightSpace / 2).attr("x2", 0).attr("y1", 0).attr("y2", 0);
			declaration.append("line").attr("x1", -heightSpace / 2).attr("x2", -heightSpace / 2)
					.attr("y1", -height - heightTextRect / 2).attr("y2", 0);

			this.select = declaration;
			d.visit(this);
			height += this.height;
			width = width > (heightSpace + widthSpaceLine * i + this.width) ? width
					: (heightSpace + widthSpaceLine * i + this.width);
		}

		this.height = height;
		this.width = width;

		s.attr("transform", "translate(" + (-width / 2) + ",0)");
		s.append("line").attr("x1", "0").attr("x2", width).attr("y1", 0).attr("y2", 0);

	}

	private static final int widthPrimitiveType = 54;

	@Override
	public void visitPrimitiveType(PrimitiveType ast) {

		String type = "";
		switch (ast.getType()) {
		case PrimitiveType.tBoolean:
			type = "boolean";
			break;
		case PrimitiveType.tInt:
			type = "integer";
			break;
		case PrimitiveType.tReal:
			type = "real";
			break;
		}

		Select s = this.select.append("g").attr("class", type);
		int width = widthPrimitiveType;

		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ".3em").innerText(type);

		this.width = width;
		this.height = heightTextRect;
	}

	private static final int widthVariable = 22;
	private static final int widthIndexer = 16;

	@Override
	public void visitVariable(Variable ast) {
		Select s = this.select.append("g").attr("class", "variable");

		int width = widthVariable;

		float w = JD3String.width(ast.getId());
		if (w > widthVariable) {
			if (w < widthVariable * 2)
				width = widthVariable * 2;
			else
				width = widthVariable * 3;
		}

		this.width = width;
		this.height = heightTextRect;

		if (ast.getIndexer().size() > 0) {
			int maxWidth = width;
			Select indexers = s.append("g");
			for (int i = ast.getIndexer().size() - 1; i >= 0; i--) {
				Select indexer = indexers.append("g").attr("transform", "translate(" + (widthSpaceLine * i) + ","
						+ (heightTextRect + heightSpace + (heightExpression + 3) * i) + ")");
				indexer.append("line").attr("x1", 0).attr("x2", 0).attr("y2", 0).attr("y1",
						-heightSpace - (heightExpression + 3) * i);
				indexer.append("g").attr("class", "indexer array").append("rect").attr("x", -widthIndexer / 2)
						.attr("width", widthIndexer).attr("height", heightTextRect);
				indexer.select(".indexer").append("text").attr("y", widthIndexer / 2).attr("dy", ".25em")
						.innerText("[ ]");
				indexer.select(".indexer").append("line").attr("x1", widthIndexer / 2)
						.attr("x2", widthIndexer / 2 + heightSpace).attr("y1", heightTextRect / 2)
						.attr("y2", heightTextRect / 2);

				Select select = this.select;

				this.select = indexer.select(".indexer").append("g").attr("transform",
						"translate(" + (widthIndexer / 2 + heightSpace) + "," + (heightTextRect / 2) + ")");
				this.bounding = true;
				ast.getIndexer(i).visit(this);
				this.select = select;

				int temp = this.width + heightSpace + widthIndexer + widthSpaceLine * (ast.getIndexer().size() - 1);
				maxWidth = maxWidth > temp ? maxWidth : temp;
			}

			width = maxWidth;
			this.width = maxWidth;
			this.height = heightTextRect + heightSpace + (heightExpression + 3) * ast.getIndexer().size() - 3;

			indexers.attr("transform", "translate(" + (widthIndexer - width) / 2 + ",0)");

		}

		String inner = ast.getId();
		inner = JD3String.filter(inner, width);

		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightTextRect);
		s.append("g").attr("class", "none").append("rect").attr("x", width / 2 - 2).attr("width", 3).attr("height",
				heightTextRect);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ".3em").innerText(inner);

	}

	private static final int widthOp = 24;

	@Override
	public void visitOpAd(OpAd ast) {
		Select s = this.select.append("g").attr("class", "none");
		String inner = "";
		int width = widthOp;

		switch (ast.getOp()) {
		case OpAd.tPlus:
			inner = "+";
			break;
		case OpAd.tMinus:
			inner = "-";
			break;
		case OpAd.tOr:
			inner = "or";
			break;
		}

		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ".3em").innerText(inner);

		this.width = width;
		this.height = heightTextRect;
	}

	@Override
	public void visitOpMul(OpMul ast) {
		Select s = this.select.append("g").attr("class", "none");
		String inner = "";
		int width = widthOp;

		switch (ast.getOp()) {
		case OpMul.tProduct:
			inner = "*";
			break;
		case OpMul.tDivision:
			inner = "/";
			break;
		case OpMul.tAnd:
			inner = "and";
			break;
		}

		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ast.getOp() == OpMul.tProduct ? ".5em" : ".3em")
				.innerText(inner);

		this.width = width;
		this.height = heightTextRect;
	}

	@Override
	public void visitOpRel(OpRel ast) {
		Select s = this.select.append("g").attr("class", "none");
		String inner = "";
		int width = widthOp;

		switch (ast.getOp()) {
		case OpRel.tLessT:
			inner = "<";
			break;
		case OpRel.tGreatT:
			inner = ">";
			break;
		case OpRel.tLessEqT:
			inner = "<=";
			break;
		case OpRel.tGreatEqT:
			inner = ">=";
			break;
		case OpRel.tEq:
			inner = "=";
			break;
		case OpRel.tNotEq:
			inner = "<>";
			break;
		}

		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ".3em").innerText(inner);

		this.width = width;
		this.height = heightTextRect;
	}

	private static final int widthBoolLit = 40;

	@Override
	public void visitBoolLit(BoolLit ast) {
		Select s = this.select.append("g").attr("class", "boolean");
		int width = widthBoolLit;

		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ".3em").innerText(ast.isValue() ? "true" : "false");

		this.width = width;
		this.height = heightTextRect;
	}

	private static final int widthFloatLit = 22;

	@Override
	public void visitFloatLit(FloatLit ast) {
		Select s = this.select.append("g").attr("class", "real");
		int width = widthFloatLit;
		String inner = String.format("%g", ast.getValue());
		float w = JD3String.width(inner);
		while (width < w) {
			width += widthFloatLit;
		}

		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("y", heightTextRect/2).attr("dy", ".3em").innerText(""+ast.getValue());

		this.width = width;
		this.height = heightTextRect;
	}

	private static final int widthIntLit = 22;

	@Override
	public void visitIntLit(IntLit ast) {
		Select s = this.select.append("g").attr("class", "integer");
		int width = widthIntLit;
		String inner = "" + ast.getValue();
		float w = JD3String.width(inner);
		while (width < w) {
			width += widthIntLit;
		}

		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ".3em").innerText("" + ast.getValue());

		this.width = width;
		this.height = heightTextRect;
	}

	public void print(AST ast, String path) throws Exception {
		this.vis.resetDynamic();

		Select s = this.vis.tDynamic().append("g");
		
		this.select = s;
		ast.visit(this);
//		float scaleX = 1;
		float scaleX = this.windowW / this.width;
		scaleX = scaleX > 1.5f ? 1.5f : scaleX;
		s.attr("transform", "translate(" + (this.windowW / 2) + ",0)scale(" + scaleX + "," + scaleX + ")");

		this.width = windowW;
		this.height = windowH;
		

		this.fileOut(path);
	}

}
