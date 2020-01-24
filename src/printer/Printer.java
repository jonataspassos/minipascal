package printer;

import java.util.ArrayList;

import ast.*;
import checker.CheckerException;
import function.JD3String;
import visualization.Visualization;
import select.Select;

/**
 * Esta classe tem como objetivo gerar o desenho da AST identificada na
 * verificação sintática. Ela utiliza a dependência de uma biblioteca chamada
 * JD3 contendo um conjunto de funções capazes de gerar na saída um código html
 * para exibir a imagem em SVG. Esse html permitirá fazer a movimentação e o
 * zoom na imagem gerada, permitindo observar mais de perto os detalhes pequenos
 * da imagem. Isso ajudará pois a árvore gerada pode ser muito grande e,
 * consequentemente, suas partes podem se tornar muito pequenas na hora de
 * vizualizar. Para melhores detalhes, consulte o manual de utilização do
 * compilador.
 */
public class Printer implements Visitor {
	/**
	 * Altura do retangulo que guarda o texto
	 */
	private static final int heightTextRect = 18;
	/**
	 * Altura do espaçamento vertical entre os blocos (Em alguns casos é utilizado
	 * como espaçamento horizonal)
	 */
	private static final int heightSpace = 10;
	/**
	 * Largura do espaçamento horizontal entre os blocos (apenas quando os blocos
	 * fizerem parte de uma mesma estrutura)
	 */
	private static final int widthSpaceLine = 3;
	/**
	 * Título da vizualização
	 */
	private static final String title = ".::MiniPascal Printer::.";

	/**
	 * Armazena o objeto da vizualização final
	 */
	private Visualization vis;
	/**
	 * Armazenam o tamanho da visualização final, util para o calculo da
	 * recentralização antes de retornar o arquivo pronto
	 */
	private int windowW, windowH;

	/**
	 * Variável temporária que auxilia a comunicação entre os métodos desta classe.
	 * Cada método consulta e sobrescreve conforme a necessidade da comunicação.
	 */
	private Select select;
	/**
	 * Variável temporária que auxilia a comunicação entre os métodos desta classe.
	 * Cada método consulta e sobrescreve conforme a necessidade da comunicação.
	 */
	private int width = 0;
	/**
	 * Variável temporária que auxilia a comunicação entre os métodos desta classe.
	 * Cada método consulta e sobrescreve conforme a necessidade da comunicação.
	 */
	private int height = 0;
	/**
	 * Variável temporária que auxilia a comunicação entre os métodos desta classe.
	 * Cada método consulta e sobrescreve conforme a necessidade da comunicação.
	 */
	private boolean bounding = true;

	/**
	 * Constroi a instancia com os parâmetros padrões
	 */
	public Printer() throws Exception {
		this(640, 480);
	}

	/**
	 * Constroi a instancia personalizando os atributos
	 * 
	 * @param width  - largura da vizualização final (svg do html)
	 * @param height - altura da vizualização final (svg do html)
	 */
	public Printer(int width, int height) throws Exception {
		super();
		// Os blocos com a classe "expression" receberão a funcionalizade de foco, é
		// quando o bloco pode ser clicado e o zoom redirecionado para encaixá-lo
		// completamente na tela
		this.vis = new Visualization(title, width, height, ".expression");

		this.windowW = width;
		this.windowH = height;

		// Determina o comportamento de estilo para cada uma das classes e tags

		// Todos os textos
		this.vis.tStyle().append("text").attr("text-anchor", "middle");

		// Todas as linhas
		this.vis.tStyle().append("line").attr("stroke", "black").attr("stroke-width", "1px");

		// Classes de tipos de identificadores
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
		this.vis.tStyle().append(".null>rect").attr("fill", "gray");
		this.vis.tStyle().append(".variable>rect").attr("fill", "coral");

		// Classes de outras estruturas
		this.vis.tStyle().append(".expression").attr("stroke", "black").attr("stroke-width", "0.2px");
		this.vis.tStyle().append(".expression>rect").attr("fill", "antiquewhite");
		this.vis.tStyle().append(".op>rect").attr("opacity", "0.8");
		this.vis.tStyle().append(".id>rect").attr("fill", "lightgray");
		this.vis.tStyle().append(".declaration>rect").attr("fill", "darkseagreen");
		this.vis.tStyle().append(".command>rect").attr("fill", "goldenrod");
		this.vis.tStyle().append(".program>rect").attr("fill", "rebeccapurple");

		// Inicializa a variavel temporária para configurar o inicio da geração da
		// árvore
		this.select = this.vis.tDynamic();
	}

	/**
	 * Exporta a árvore gerada para o html
	 */
	private void fileOut(String path) {
		this.vis.fileOut(path);
	}

	/**
	 * Ao inicio de cada método é especificado o tamanho(altura e/ou largura) padrão
	 * que ele pode se basear
	 */
	private static final int widthAggregateType = 90;
	private static final int heightAggregateType = 50;

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitAggregateType(AggregateType ast) {
		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		Select s = this.select.append("g").attr("class", "block array");
		// Configura o tamanho que ele retornará como dele
		int width = widthAggregateType;

		// Retangulo de tras do bloco(este que recebe a cor)
		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightAggregateType);

		// Textos do elemento
		// titulo
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ".3em").innerText("array");
		// indice inicial
		s.append("text").attr("y", heightTextRect * 3 / 2 - 1).attr("dy", ".3em").attr("x", "-7")
				.attr("style", "text-anchor: end").innerText("" + ast.getIndex(0));
		// conectivo de intervalo
		s.append("text").attr("y", heightTextRect * 3 / 2 - 1).attr("dy", ".3em").innerText("~");
		// indice final
		s.append("text").attr("y", heightTextRect * 3 / 2 - 1).attr("dy", ".3em").attr("x", "7")
				.attr("style", "text-anchor: start").innerText("" + ast.getIndex(1));
		// conectivo de tipo
		s.append("text").attr("y", heightTextRect * 5 / 2 - 3).attr("dy", ".3em").innerText("Of");
		// linha para baixo
		s.append("line").attr("x1", 0).attr("x2", 0).attr("y1", heightAggregateType).attr("y2",
				heightAggregateType + heightSpace);

		Select select = this.select;

		// Configura o local onde será adicionado o tipo do array, o posicionando no
		// local correto
		this.select = s.append("g").attr("transform", "translate(0," + (heightAggregateType + heightSpace) + ")");
		// Chama o desenho do tipo do array
		ast.getType().visit(this);

		// Calcula o próprio tamanho(altura e largura)
		this.width = width < this.width ? this.width : width;
		this.height = this.height + heightAggregateType + heightSpace;

		this.select = select;
	}

	/**
	 * Ao inicio de cada método é especificado o tamanho(altura e/ou largura) padrão
	 * que ele pode se basear
	 */
	private static final int widthAssignmentCommand = 60;

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitAssignmentCommand(AssignmentCommand ast) {
		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		Select s = this.select.append("g").attr("class", "block assignment command");

		// Adiciona um retângulo de fundo para axuliar a diferenciação entre o bloco e
		// as linhas dos outros blocos que poderão passar por tras
		Select background = s.append("rect").attr("style", "fill:white;opacity:0.7;");

		// Desenha a variável que receberá a atribuição
		Select var = s.append("g");
		this.select = var;
		ast.getVariable().visit(this);
		int varW = this.width;
		int varH = this.height;

		// Desenha a expressão que gerará o valor da atribuição
		Select exp = s.append("g");
		exp.append("line").attr("x1", -heightSpace / 2).attr("x2", 0).attr("y1", 0).attr("y2", 0);
		exp.append("line").attr("x1", -heightSpace / 2).attr("x2", -heightSpace / 2).attr("y1", -heightExpression / 2)
				.attr("y2", 0);
		this.select = exp;
		this.bounding = true;
		ast.getExpression().visit(this);
		int expW = this.width;
		int expH = this.height;

		// calcula a largura final
		int width = varW + expW + heightSpace * 3 / 2;
		width = width > widthAssignmentCommand ? width : widthAssignmentCommand;

		// Posiciona os blocos chamados
		var.attr("transform", "translate(" + (varW / 2) + "," + (heightTextRect / 2 + heightSpace) + ")");
		exp.attr("transform",
				"translate(" + (width - expW) + "," + (heightTextRect / 2 + heightSpace + heightExpression / 2) + ")");

		// Desenha o próprio bloco
		s.append("rect").attr("y", -heightTextRect / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("x", width / 2).attr("dy", ".3em").innerText("... := ...");

		// Desenha as linhas de conexão
		s.append("line").attr("x1", width / 2 - 17).attr("x2", varW / 2).attr("y1", heightTextRect / 2).attr("y2",
				heightSpace + heightTextRect / 2);
		s.append("line").attr("x1", width / 2 + 18).attr("x2", width - expW - heightSpace / 2)
				.attr("y1", heightTextRect / 2).attr("y2", heightSpace + heightTextRect / 2);

		// Calcula o tamanho(largura e altura) do bloco
		this.width = width;
		this.height = varH > expH ? varH : expH;
		this.height += heightSpace + heightTextRect;

		// Redimensiona o retângulo de fundo
		background.attr("y", -heightTextRect / 2).attr("width", this.width).attr("height", this.height);
	}

	/**
	 * Ao inicio de cada método é especificado o tamanho(altura e/ou largura) padrão
	 * que ele pode se basear
	 */
	private static final int widthConditionalElseCommand = 125;
	private static final int widthConditionalCommand = 80;

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitConditionalCommand(ConditionalCommand ast) {
		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		Select s = this.select.append("g").attr("class", "block conditional command");

		// Adiciona um retângulo de fundo para axuliar a diferenciação entre o bloco e
		// as linhas dos outros blocos que poderão passar por tras
		Select background = s.append("rect").attr("style", "fill:white;opacity:0.7;");

		// possui else?
		boolean tElse = false;

		// Desenha a expressão condicional do comando
		Select exp = s.append("g");
		exp.append("line").attr("x1", -heightSpace / 2).attr("x2", 0).attr("y1", 0).attr("y2", 0);
		exp.append("line").attr("x1", -heightSpace / 2).attr("x2", -heightSpace / 2).attr("y1", -heightExpression / 2)
				.attr("y2", 0);
		this.select = exp;
		this.bounding = true;
		ast.getExpression().visit(this);
		int expW = this.width;
		int expH = this.height;

		// Desenha o comando de quando a expressão é verdadeira
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

		// Recalcula largura
		int width = comIfW + expW + heightSpace * 2;

		// Se possui comando else
		if (ast.getCommand(false) != null) {
			// Desenha comando else
			tElse = true;
			// Desenha comando else
			comElse = s.append("g");
			comElse.append("line").attr("x1", -heightSpace / 2).attr("x2", 0).attr("y1", 0).attr("y2", 0);
			comElse.append("line").attr("x1", -heightSpace / 2).attr("x2", -heightSpace / 2)
					.attr("y1", -heightTextRect / 2).attr("y2", 0);
			this.select = comElse;
			ast.getCommand(false).visit(this);
			comElseW = this.width;
			comElseH = this.height;

			// recalcula largura
			width += comElseW + heightSpace;
		}

		// Recalcula largura
		if (tElse)
			width = width > widthConditionalElseCommand ? width : widthConditionalElseCommand;
		else
			width = width > widthConditionalCommand ? width : widthConditionalCommand;

		// Reposiciona os elementos desenhados
		exp.attr("transform",
				"translate(" + (heightSpace) + "," + (heightTextRect / 2 + heightSpace + heightExpression / 2) + ")");
		comIf.attr("transform", "translate(" + (width - comIfW + (tElse ? (-comElseW - heightSpace) : 0)) + ","
				+ (heightTextRect / 2 + heightSpace + heightTextRect / 2) + ")");
		if (tElse) {
			comElse.attr("transform", "translate(" + (width - comElseW) + ","
					+ (heightTextRect / 2 + heightSpace + heightTextRect / 2) + ")");
		}

		// Desenha o próprio blco
		s.append("rect").attr("y", -heightTextRect / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("x", width / 2).attr("dy", ".3em")
				.innerText("if ... then ..." + (tElse ? " else ..." : ""));

		// Desenha as linhas de conexão
		s.append("line").attr("x1", width / 2 + (tElse ? -40 : -16)).attr("x2", heightSpace / 2)
				.attr("y1", heightTextRect / 2).attr("y2", heightSpace + heightTextRect / 2);
		s.append("line").attr("x1", width / 2 + (tElse ? 8 : 31))
				.attr("x2", width - comIfW + (tElse ? (-comElseW - heightSpace) : 0) - heightSpace / 2)
				.attr("y1", heightTextRect / 2).attr("y2", heightSpace + heightTextRect / 2);
		if (tElse) {
			s.append("line").attr("x1", width / 2 + 55).attr("x2", width - comElseW - heightSpace / 2)
					.attr("y1", heightTextRect / 2).attr("y2", heightSpace + heightTextRect / 2);
		}

		// recalcula os tamanhos
		this.width = width;
		this.height = comIfH > expH ? comIfH : expH;
		if (tElse)
			this.height = this.height > comElseH ? this.height : comElseH;
		this.height += heightSpace + heightTextRect;

		// Redimensiona o retangulo de fundo
		background.attr("y", -heightTextRect / 2).attr("width", this.width).attr("height", this.height);

	}

	/**
	 * Ao inicio de cada método é especificado o tamanho(altura e/ou largura) padrão
	 * que ele pode se basear
	 */
	private static final int widthIterativeCommand = 95;

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitIterativeCommand(IterativeCommand ast) {
		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		Select s = this.select.append("g").attr("class", "block iterative command");

		// Adiciona um retângulo de fundo para axuliar a diferenciação entre o bloco e
		// as linhas dos outros blocos que poderão passar por tras
		Select background = s.append("rect").attr("style", "fill:white;opacity:0.7;");

		// Desenha a expressão condicional do comando
		Select exp = s.append("g");
		exp.append("line").attr("x1", -heightSpace / 2).attr("x2", 0).attr("y1", 0).attr("y2", 0);
		exp.append("line").attr("x1", -heightSpace / 2).attr("x2", -heightSpace / 2).attr("y1", -heightExpression / 2)
				.attr("y2", 0);
		this.select = exp;
		this.bounding = true;
		ast.getExpression().visit(this);
		int expW = this.width;
		int expH = this.height;

		// Desenha o comando que será iterado
		Select com = s.append("g");
		com.append("line").attr("x1", -heightSpace / 2).attr("x2", 0).attr("y1", 0).attr("y2", 0);
		com.append("line").attr("x1", -heightSpace / 2).attr("x2", -heightSpace / 2).attr("y1", -heightTextRect / 2)
				.attr("y2", 0);
		this.select = com;
		ast.getCommand().visit(this);
		int comW = this.width;
		int comH = this.height;

		// Recalcula a largura
		int width = comW + expW + heightSpace * 2;
		width = width > widthIterativeCommand ? width : widthIterativeCommand;

		// reposiciona os blocos desenhados
		exp.attr("transform",
				"translate(" + (heightSpace) + "," + (heightTextRect / 2 + heightSpace + heightExpression / 2) + ")");
		com.attr("transform",
				"translate(" + (width - comW) + "," + (heightTextRect / 2 + heightSpace + heightTextRect / 2) + ")");

		// Desenha o proprio bloco
		s.append("rect").attr("y", -heightTextRect / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("x", width / 2).attr("dy", ".3em").innerText("while ... do ...");

		// Desenha as linhas de conexão
		s.append("line").attr("x1", width / 2 + 2).attr("x2", heightSpace / 2).attr("y1", heightTextRect / 2).attr("y2",
				heightSpace + heightTextRect / 2);
		s.append("line").attr("x1", width / 2 + 38).attr("x2", width - comW - heightSpace / 2)
				.attr("y1", heightTextRect / 2).attr("y2", heightSpace + heightTextRect / 2);

		// recalcula os tamanhos(largura e altura)
		this.width = width;
		this.height = comH > expH ? comH : expH;
		this.height += heightSpace + heightTextRect;

		// Redimensiona o retangulo de fundo
		background.attr("y", -heightTextRect / 2).attr("width", this.width).attr("height", this.height);

	}

	/**
	 * Ao inicio de cada método é especificado o tamanho(altura e/ou largura) padrão
	 * que ele pode se basear
	 */
	private static final int widthMultiCommand = 90;

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitMultiCommand(MultiCommand ast) {
		Select s = this.select.append("g").attr("class", "block multi command");

		// Adiciona um retângulo de fundo para axuliar a diferenciação entre o bloco e
		// as linhas dos outros blocos que poderão passar por tras
		Select background = s.append("rect").attr("style", "fill:white;opacity:0.7;");

		// cria onde será desenhada a lista de comandos
		Select commands = s.append("g").attr("transform",
				"translate(" + (heightSpace) + "," + (heightTextRect / 2) + ")");

		int width = widthMultiCommand;
		int height = 0;

		// para cado comando
		for (int i = 0; i < ast.getCommand().size(); i++) {
			Command c = ast.getCommand(i);

			// calcula a posição em y
			height += heightSpace;

			// cria o espaço onde o comando será adicionado
			Select command = commands.preppend("g").attr("transform",
					"translate(" + (widthSpaceLine * i) + "," + (height + heightTextRect / 2) + ")");
			// cria as linhas de conexão
			command.append("line").attr("x1", -heightSpace / 2).attr("x2", 0).attr("y1", 0).attr("y2", 0);
			command.append("line").attr("x1", -heightSpace / 2).attr("x2", -heightSpace / 2)
					.attr("y1", -height - heightTextRect / 2).attr("y2", 0);

			// desenha o comando
			this.select = command;
			c.visit(this);
			// recalcula os tamanhos
			height += this.height;
			width = width > (heightSpace + widthSpaceLine * i + this.width) ? width
					: (heightSpace + widthSpaceLine * i + this.width);
		}

		// Desenha o próprio bloco
		s.append("rect").attr("y", -heightTextRect / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("x", width / 2).attr("dy", ".3em").innerText("begin ... end");

		// Recalcula os tamanhos
		this.height = height + heightSpace + heightTextRect;
		this.width = width;

		// Redimensiona o retangulo de fundo
		background.attr("y", -heightTextRect / 2).attr("width", this.width).attr("height", this.height);
	}

	/**
	 * Ao inicio de cada método é especificado o tamanho(altura e/ou largura) padrão
	 * que ele pode se basear
	 */
	private static final int widthDeclaration = 70;

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitDeclaration(Declaration ast) {
		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		Select s = this.select.append("g").attr("class", "block declaration");

		// Adiciona um retângulo de fundo para axuliar a diferenciação entre o bloco e
		// as linhas dos outros blocos que poderão passar por tras
		Select background = s.append("rect").attr("style", "fill:white;opacity:0.7;");

		// Desenha a lista de ids
		Select ids = s.append("g");
		this.select = ids;
		visitListId(ast.getId());// Este método não faz parte do visitor, mas funciona como um
		int idW = this.width;
		int idH = this.height;

		// Desenha o tipo
		Select type = s.append("g");
		this.select = type;
		ast.getType().visit(this);
		int tyW = this.width;
		int tyH = this.height;

		// Recalcula a largura
		int width = idW + tyW + heightSpace;
		width = width > widthDeclaration ? width : widthDeclaration;

		// Posiciona os blocos desenhados
		ids.attr("transform", "translate(" + (idW / 2) + "," + (heightTextRect / 2 + heightSpace) + ")");
		type.attr("transform", "translate(" + (width - tyW / 2) + "," + (heightTextRect / 2 + heightSpace) + ")");

		// Desenha o próprio bloco
		s.append("rect").attr("y", -heightTextRect / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("x", width / 2).attr("dy", ".3em").innerText("var ... : ...");

		// Desenha as linhas de conexão
		s.append("line").attr("x1", width / 2).attr("x2", idW / 2).attr("y1", heightTextRect / 2).attr("y2",
				heightTextRect / 2 + heightSpace);
		s.append("line").attr("x1", width / 2 + 25).attr("x2", width - tyW / 2).attr("y1", heightTextRect / 2)
				.attr("y2", heightTextRect / 2 + heightSpace);

		// recalcula os tamanhos
		this.width = width;
		this.height = tyH > idH ? tyH : idH;
		this.height += heightSpace + heightTextRect;

		// Redimensiona o retangulo do fundo
		background.attr("y", -heightTextRect / 2).attr("width", this.width).attr("height", this.height);
	}

	/**
	 * Ao inicio de cada método é especificado o tamanho(altura e/ou largura) padrão
	 * que ele pode se basear
	 */
	private static final int widthId = 22;

	/**
	 * Especifica a forma como a lista de ids será gerada na vizualização
	 */
	private void visitListId(ArrayList<String> li) {
		// Obtem o indice da string com maior tamanho "fisico" na lista de id
		int gw = JD3String.greatWidth(li);

		int size = li.size();

		// Obtem o tamanho fisico da maior string
		float w = JD3String.width(li.get(gw));
		// Recalcula a largura dos blocos, até no maximo 3 vezes o tamanho previsto para
		// este bloco
		int width = widthId;
		if (w > widthId) {
			if (w < widthId * 2)
				width = widthId * 2;
			else
				width = widthId * 3;
		}
		// recalcula as larguras
		w = width;
		width = width + widthSpaceLine * (size - 1);

		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		Select s = this.select.append("g").attr("class", "list-id").attr("transform",
				"translate(" + (-width / 2) + ",0)");
		// Desenha a linha de receber as conexões
		s.append("line").attr("x1", "0").attr("x2", width).attr("y1", 0).attr("y2", 0);

		// Para cada id na lista(ordem inversa)
		for (int i = li.size() - 1; i >= 0; i--) {
			// Cria o bloco do id
			Select id = s.append("g").attr("class", "id").attr("transform",
					"translate(" + (w / 2 + widthSpaceLine * i) + "," + (heightSpace + (heightTextRect + 2) * i) + ")");
			// Desenha a linha de conexão
			id.append("line").attr("x1", "0").attr("x2", "0").attr("y2", "0").attr("y1",
					-(heightSpace + (heightTextRect + 2) * i));
			// desenha o bloco do id (o texto pode ser abreviado para caber no retângulo)
			id.append("rect").attr("x", -w / 2).attr("width", w).attr("height", heightTextRect);
			id.append("text").attr("y", heightTextRect / 2).attr("dy", ".25em")
					.innerText(JD3String.filter(li.get(i), w));
		}

		// Recalcula os tamanhos
		this.width = width;
		this.height = heightSpace + (heightTextRect + 2) * size - 2;
	}

	/**
	 * Ao inicio de cada método é especificado o tamanho(altura e/ou largura) padrão
	 * que ele pode se basear
	 */
	private static final int widthExpression = 22 + widthSpaceLine;
	private static final int heightExpression = 22;

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitExpression(Expression ast) {
		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		Select s = this.select.append("g").attr("class", "block expression");

		// Se foi chamado por um comando, será reajustado para caber dentro do tamanho
		// especificado acima.
		// Esta configuração facilita a visualização da arvore, já que a expressão pode
		// ser grande como for, não prejudicará a visualização dos comandos do programa
		if (bounding) {
			// Desenha o retangulo de fundo
			s.append("rect").attr("y", -heightExpression / 2).attr("width", widthExpression).attr("height",
					heightExpression);
			// Desenha uma tarja à esquerda para representar o tipo de retorno da expressao
			String type = ""+null;
			try {
				type = "" + ast.getType();
			} catch (CheckerException ce) {
				
			}
			s.append("g").attr("class", type.split(" ")[0]).append("rect")
					.attr("y", -heightExpression / 2).attr("width", widthSpaceLine).attr("height", heightExpression);

			// Desenhará a expressão propriamente dita que a representa
			Select exp = s.append("g");
			this.select = exp;
			this.bounding = false;// agora não redimensionada
			ast.visit(this);// Recursão

			// Reescala e reposiciona a expressão desenhada para caber dentro do retangulo
			// da expressão
			float expW = (widthExpression - widthSpaceLine) * 15 / 16;
			float expH = heightExpression * 3 / 4;
			float scale = (expW / this.width) < (expH / this.height) ? (expW / this.width) : (expH / this.height);
			exp.attr("transform", "translate(" + (widthExpression + widthSpaceLine) / 2 + "," + (-expH / 2) + ")scale("
					+ scale + "," + scale + ")");

			// Recalcula os tamanhos
			this.width = widthExpression;
			this.height = heightExpression;
		} else {// Se foi chamado por um dos elementos da expressão(expressão ou termo), será
				// desenhado sem redimensionamento
			// Salva os indices do SimpleExpression e Term dentro do contexto que será
			// armazenado na pilha
			int indexSimpleExpression = this.indexSimpleExpression;
			int indexTerm = this.indexTerm;
			// Este método funciona de forma recursiva, já que Expression possui
			// SimpleExpression que possui Term que posssui Factor que podem ser literais,
			// variaveis ou uma nova Expression.

			this.indexSimpleExpression = 0;
			this.indexTerm = 0;

			// Os indices dizem respeito a indexação dos SimpleExpression e Term dentro de
			// si mesmos(para garantir o formato de arvore com operadores binários), e por
			// isso os indices precisam ser salvos e zerados, ja que, a partir deste ponto,
			// os
			// elementos desta expressão podem precisar iterar sobre os proprios elementos

			if (ast.getOp() == null) {// Não há operação de comparação
				// Desenha apenas o primeiro "operando"
				this.select = s;
				ast.getA(0).visit(this);
			} else {// Há operação de comparação
				// Desenha o operador
				Select operator = s.append("g");
				this.select = operator;
				ast.getOp().visit(this);
				int opW = this.width;
				int opH = this.height;

				// Desenha o primeiro operando
				Select a0 = s.append("g");
				this.select = a0;
				ast.getA(0).visit(this);// SimpleExpression
				int a0W = this.width;
				int a0H = this.height;

				// Desenha o segundo operando
				Select a1 = s.append("g");
				this.select = a1;
				ast.getA(1).visit(this);// SimpleExpression
				int a1W = this.width;
				int a1H = this.height;

				// Recalcula os tamanhos
				this.width = a0W + a1W + heightSpace;
				this.width = this.width > opW ? this.width : opW;

				this.height = a0H > a1H ? a0H : a1H;
				this.height += heightSpace + opH;

				// Reposiciona os blocos desenhados
				a0.attr("transform", "translate(" + (a0W - width) / 2 + "," + (opH + heightSpace) + ")");
				a1.attr("transform", "translate(" + (width - a1W) / 2 + "," + (opH + heightSpace) + ")");

				// Desenha as linhas de conexão
				s.append("line").attr("x1", 0).attr("x2", (a0W - width) / 2).attr("y1", opH).attr("y2",
						opH + heightSpace);
				s.append("line").attr("x1", 0).attr("x2", (width - a1W) / 2).attr("y1", opH).attr("y2",
						opH + heightSpace);
			}

			// Ao terminar os processos desta expressão, os indices "empilhados", são
			// devolvidos aos atributos do objeto para continuarem a indexar os
			// SimpleExpression e Term anteriores
			this.indexSimpleExpression = indexSimpleExpression;
			this.indexTerm = indexTerm;
		}

		// A próxima expressão não será redimensionada, a não ser que explicitamente
		// seja solicitado
		this.bounding = false;
	}

	/**
	 * Este especifica o indice que sera obtido da ast. Este método é recursivo e
	 * este é o atributo que gerencia o critério de parada
	 */
	private int indexSimpleExpression = 0;

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitSimpleExpression(SimpleExpression ast) {
		String type = ""+null;
		try {
			type = "" + ast.getType();
		} catch (CheckerException ce) {
			
		}
		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		// Configura o tipo da como classe do bloco
		
		Select s = this.select.append("g").attr("class",
				"block simple-expression " + type.split(" ")[0]);

		// Há operadores de OpAd
		if (ast.getOp().size() == indexSimpleExpression) {// Não há mais operadores
			// Desenha o ultimo operando
			this.select = s;
			ast.getA(indexSimpleExpression).visit(this);// Term
		} else {// Há operadores
			// Desenha o operando, o operador e faz uma chamada recursiva com o próximo
			// indice

			// Desenha o operador
			Select operator = s.append("g");
			this.select = operator;
			ast.getOp(indexSimpleExpression).visit(this);
			int opW = this.width;
			int opH = this.height;

			// Desenha o operando
			Select a0 = s.append("g");
			this.select = a0;
			ast.getA(indexSimpleExpression).visit(this);// Term
			int a0W = this.width;
			int a0H = this.height;

			// Chama recursivamente para desenhar o segundo operando
			// O segundo operando é resultado da operação do proximo operador, ou do ultimo
			// operando da lista(se não houver mais operadores)
			Select a1 = s.append("g");
			this.select = a1;
			indexSimpleExpression++;
			ast.visit(this);// Restante da expressï¿½o simples
			int a1W = this.width;
			int a1H = this.height;
			indexSimpleExpression--;

			// Recalcula os tamanhos
			this.width = a0W + a1W + heightSpace;
			this.width = this.width > opW ? this.width : opW;

			this.height = a0H > a1H ? a0H : a1H;
			this.height += heightSpace + opH;

			// Reposiociona os blocos desenhados
			a0.attr("transform", "translate(" + (a0W - width) / 2 + "," + (opH + heightSpace) + ")");
			a1.attr("transform", "translate(" + (width - a1W) / 2 + "," + (opH + heightSpace) + ")");

			// Desenha as linhas de conexão
			s.append("line").attr("x1", 0).attr("x2", (a0W - width) / 2).attr("y1", opH).attr("y2", opH + heightSpace);
			s.append("line").attr("x1", 0).attr("x2", (width - a1W) / 2).attr("y1", opH).attr("y2", opH + heightSpace);
		}
	}

	/**
	 * Este especifica o indice que sera obtido da ast. Este método é recursivo e
	 * este é o atributo que gerencia o critério de parada
	 */
	private int indexTerm = 0;

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitTerm(Term ast) {
		String type = ""+null;
		try {
			type = "" + ast.getType();
		} catch (CheckerException ce) {
			
		}
		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		Select s = this.select.append("g").attr("class", "block term " + type.split(" ")[0]);

		// Há operadores de OpMul
		if (ast.getOp().size() == indexTerm) {// Não há mais operadores
			// Desenha o ultimo operando
			this.select = s;
			ast.getA(ast.getOp().size() - indexTerm).visit(this);// Factor : literal, expression, variable
		} else {// Há operadores
			// Desenha o operando, o operador e faz uma chamada recursiva com o próximo
			// indice

			// Desenha o operador
			Select operator = s.append("g");
			this.select = operator;
			ast.getOp(ast.getOp().size() - indexTerm - 1).visit(this);
			int opW = this.width;
			int opH = this.height;

			// Desenha o operando
			Select a0 = s.append("g");
			this.select = a0;
			indexTerm++;
			ast.visit(this);// Restante do termo
			int a0W = this.width;
			int a0H = this.height;
			indexTerm--;

			// Chama recursivamente para desenhar o segundo operando
			// O segundo operando é resultado da operação do proximo operador, ou do ultimo
			// operando da lista(se não houver mais operadores)
			Select a1 = s.append("g");
			this.select = a1;
			ast.getA(ast.getOp().size() - indexTerm).visit(this);// Factor : literal, expression, variable
			int a1W = this.width;
			int a1H = this.height;

			//Recalcula os tamanhos
			this.width = a0W + a1W + heightSpace;
			this.width = this.width > opW ? this.width : opW;

			this.height = a0H > a1H ? a0H : a1H;
			this.height += heightSpace + opH;

			//Posiciona os blocos desenhados
			a0.attr("transform", "translate(" + (a0W - width) / 2 + "," + (opH + heightSpace) + ")");
			a1.attr("transform", "translate(" + (width - a1W) / 2 + "," + (opH + heightSpace) + ")");
			
			//Desenha as linhas de conexão
			s.append("line").attr("x1", 0).attr("x2", (a0W - width) / 2).attr("y1", opH).attr("y2", opH + heightSpace);
			s.append("line").attr("x1", 0).attr("x2", (width - a1W) / 2).attr("y1", opH).attr("y2", opH + heightSpace);
		}

	}

	/**
	 * Ao inicio de cada método é especificado o tamanho(altura e/ou largura) padrão
	 * que ele pode se basear
	 */
	private static final int widthProgram = 60;

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitProgram(Program ast) {
		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
				// mesmo
		Select s = this.select.append("g").attr("class", "block program");
		int width = 0;
		int height = 0;

		//Desenha a lista de declarações
		Select declarations = s.append("g");
		this.select = declarations;
		visitListDeclaration(ast.getDeclaration());
		int decW = this.width;
		width += this.width;
		height = height > this.height ? height : this.height;

		//Desenha o comando multiplo
		Select multiCommands = s.append("g");
		multiCommands.append("line").attr("x1", -heightSpace / 2).attr("x2", 0).attr("y1", 0).attr("y2", 0);
		multiCommands.append("line").attr("x1", -heightSpace / 2).attr("x2", -heightSpace / 2)
				.attr("y1", -heightTextRect / 2).attr("y2", 0);
		this.select = multiCommands;
		ast.getMc().visit(this);
		int comW = this.width;
		width += this.width;
		height = height > this.height ? height : this.height;

		//Recalcula os tamanhos
		width += heightSpace;
		int widthRect = widthProgram;

		//Configura o nome do programa para caber no bloco
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

		//Desenha o bloco
		s.append("rect").attr("x", -widthRect / 2).attr("width", widthRect).attr("height",
				heightTextRect * 2 + widthSpaceLine);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ".3em").innerText("program");
		s.append("text").attr("y", heightTextRect * 3 / 2 + widthSpaceLine).attr("dy", ".3em").innerText(inner);

		//Posiciona os blocos desenhados
		declarations.attr("transform", "translate(" + (-width / 2 + decW / 2) + ","
				+ (heightTextRect * 2 + widthSpaceLine + heightSpace * 2) + ")");
		multiCommands.attr("transform", "translate(" + (width / 2 - comW) + ","
				+ (heightTextRect * 2 + widthSpaceLine + heightSpace * 2 + heightTextRect / 2) + ")");

		//Desenha as linhas de conexão
		s.append("line").attr("x1", "0").attr("x2", -width / 2 + decW / 2)
				.attr("y1", heightTextRect * 2 + widthSpaceLine)
				.attr("y2", heightTextRect * 2 + widthSpaceLine + heightSpace * 2);
		s.append("line").attr("x1", "0").attr("x2", width / 2 - comW - heightSpace / 2)
				.attr("y1", heightTextRect * 2 + widthSpaceLine)
				.attr("y2", heightTextRect * 2 + widthSpaceLine + heightSpace * 2);

		
		this.width = width;
		this.height = height;

	}

	/**
	 * Ao inicio de cada método é especificado o tamanho(altura e/ou largura) padrão
	 * que ele pode se basear
	 */
	private static final int widthListDeclaration = 30;

	/**
	 * Especifica a forma como a lista de declarações será gerada na vizualização
	 */
	private void visitListDeclaration(ArrayList<Declaration> ld) {
		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		Select s = this.select.append("g").attr("class", "list-declaration");

		//Cria onde serão desenhadas as declarações
		Select declarations = s.append("g").attr("transform", "translate(" + (heightSpace) + ",0)");

		int width = widthListDeclaration;
		int height = 0;
		
		//Para cada declaração
		for (int i = 0; i < ld.size(); i++) {
			Declaration d = ld.get(i);
			//Calcula o ponto y que será desenhada a declaração
			height += heightSpace;
			
			//Cria o espaço para a declaração
			Select declaration = declarations.preppend("g").attr("transform",
					"translate(" + (widthSpaceLine * i) + "," + (height + heightTextRect / 2) + ")");
			//Desenha as linhas de conexão
			declaration.append("line").attr("x1", -heightSpace / 2).attr("x2", 0).attr("y1", 0).attr("y2", 0);
			declaration.append("line").attr("x1", -heightSpace / 2).attr("x2", -heightSpace / 2)
					.attr("y1", -height - heightTextRect / 2).attr("y2", 0);
			//Desenha a declaração
			this.select = declaration;
			d.visit(this);
			//Recalcula os tamanhos
			height += this.height;
			width = width > (heightSpace + widthSpaceLine * i + this.width) ? width
					: (heightSpace + widthSpaceLine * i + this.width);
		}

		this.height = height;
		this.width = width;

		//Reposiciona as declarações
		s.attr("transform", "translate(" + (-width / 2) + ",0)");
		//desenha a linha que recebe as conexções
		s.append("line").attr("x1", "0").attr("x2", width).attr("y1", 0).attr("y2", 0);

	}

	/**
	 * Ao inicio de cada método é especificado o tamanho(altura e/ou largura) padrão
	 * que ele pode se basear
	 */
	private static final int widthPrimitiveType = 54;

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitPrimitiveType(PrimitiveType ast) {

		//Obtem o nome do tipo a ser desenhado
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

		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		Select s = this.select.append("g").attr("class", "block " + type);
		int width = widthPrimitiveType;

		//Desenha o bloco
		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ".3em").innerText(type);

		//Retorna os tamanhos
		this.width = width;
		this.height = heightTextRect;
	}

	/**
	 * Ao inicio de cada método é especificado o tamanho(altura e/ou largura) padrão
	 * que ele pode se basear
	 */
	private static final int widthVariable = 22;
	private static final int widthIndexer = 16;

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitVariable(Variable ast) {
		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		Select s = this.select.append("g").attr("class", "block variable");

		int width = widthVariable;

		//calcula o tamanho do bloco para "caber" o identificador da variavel
		float w = JD3String.width(ast.getId());
		if (w > widthVariable) {
			if (w < widthVariable * 2)
				width = widthVariable * 2;
			else
				width = widthVariable * 3;
		}

		this.width = width;
		this.height = heightTextRect;

		//Se houverem indexações
		if (ast.getIndexer().size() > 0) {
			//salva o maior tamanho de indexação até então 
			int maxWidth = width;
			Select indexers = s.append("g");
			//Para cada indexação
			for (int i = ast.getIndexer().size() - 1; i >= 0; i--) {
				//Cria o espaço para a indexação
				Select indexer = indexers.append("g").attr("class", "block").attr("transform",
						"translate(" + (widthSpaceLine * i) + ","
								+ (heightTextRect + heightSpace + (heightExpression + 3) * i) + ")");
				//cria a linha de conexão
				indexer.append("line").attr("x1", 0).attr("x2", 0).attr("y2", 0).attr("y1",
						-heightSpace - (heightExpression + 3) * i);
				///cria o bloco de indexãção
				indexer.append("g").attr("class", "indexer array").append("rect").attr("x", -widthIndexer / 2)
						.attr("width", widthIndexer).attr("height", heightTextRect);
				indexer.select(".indexer").append("text").attr("y", widthIndexer / 2).attr("dy", ".25em")
						.innerText("[ ]");
				indexer.select(".indexer").append("line").attr("x1", widthIndexer / 2)
						.attr("x2", widthIndexer / 2 + heightSpace).attr("y1", heightTextRect / 2)
						.attr("y2", heightTextRect / 2);

				//desenha a expressão indexadora
				Select select = this.select;
				this.select = indexer.select(".indexer").append("g").attr("transform",
						"translate(" + (widthIndexer / 2 + heightSpace) + "," + (heightTextRect / 2) + ")");
				this.bounding = true;
				ast.getIndexer(i).visit(this);
				this.select = select;

				//Recalcula os tamanhos
				int temp = this.width + heightSpace + widthIndexer + widthSpaceLine * (ast.getIndexer().size() - 1);
				maxWidth = maxWidth > temp ? maxWidth : temp;
			}

			//Recalcula os tamanhos
			width = maxWidth;
			this.width = maxWidth;
			this.height = heightTextRect + heightSpace + (heightExpression + 3) * ast.getIndexer().size() - 3;

			//Reposiciona as indexações
			indexers.attr("transform", "translate(" + (widthIndexer - width) / 2 + ",0)");

		}

		//O identificador da variável pode ser abreviado para caber dentro do bloco
		String inner = ast.getId();
		inner = JD3String.filter(inner, width);
		
		String type = ""+null;
		try {
			type = "" + ast.getType();
		} catch (CheckerException ce) {
			
		}

		//Desenha o bloco da variável
		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightTextRect);
		//Desenha a tarja à direita representando o tipo da variável
		s.append("g").attr("class", type.split(" ")[0]).append("rect").attr("x", width / 2 - 2)
				.attr("width", 3).attr("height", heightTextRect);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ".3em").innerText(inner);

	}

	/**
	 * Ao inicio de cada método é especificado o tamanho(altura e/ou largura) padrão
	 * que ele pode se basear
	 */
	private static final int widthOp = 24;

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitOpAd(OpAd ast) {
		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		Select s = this.select.append("g").attr("class", ("" + ast.getType()).split(" ")[0]);
		String inner = "";
		int width = widthOp;

		//Obtem o texto que representa o operador
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

		//Desenha o bloco do operador
		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ".3em").innerText(inner);

		this.width = width;
		this.height = heightTextRect;
	}

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitOpMul(OpMul ast) {
		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		Select s = this.select.append("g").attr("class", ("" + ast.getType()).split(" ")[0]);
		String inner = "";
		int width = widthOp;

		//Obtem o texto que representa o operador
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
		
		//Desenha o bloco do operador
		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ast.getOp() == OpMul.tProduct ? ".5em" : ".3em")
				.innerText(inner);

		this.width = width;
		this.height = heightTextRect;
	}

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitOpRel(OpRel ast) {
		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		Select s = this.select.append("g").attr("class", ("" + ast.getType()).split(" ")[0]);
		String inner = "";
		int width = widthOp;
		
		//Obtem o texto que representa o operador
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
		
		//Desenha o bloco do operador
		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ".3em").innerText(inner);

		this.width = width;
		this.height = heightTextRect;
	}

	/**
	 * Ao inicio de cada método é especificado o tamanho(altura e/ou largura) padrão
	 * que ele pode se basear
	 */
	private static final int widthBoolLit = 40;

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitBoolLit(BoolLit ast) {
		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		Select s = this.select.append("g").attr("class", "boolean");
		int width = widthBoolLit;

		//Desenha o bloco do literal
		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ".3em").innerText(ast.isValue() ? "true" : "false");

		this.width = width;
		this.height = heightTextRect;
	}

	/**
	 * Ao inicio de cada método é especificado o tamanho(altura e/ou largura) padrão
	 * que ele pode se basear
	 */
	private static final int widthFloatLit = 22;

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitFloatLit(FloatLit ast) {
		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		Select s = this.select.append("g").attr("class", "real");
		
		//Redimenciona a largura do bloco até caber o valor literal
		int width = widthFloatLit;
		String inner = String.format("%g", ast.getValue());
		float w = JD3String.width(inner);
		while (width < w) {
			width += widthFloatLit;
		}

		//Desenha o bloco do literal
		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ".3em").innerText("" + ast.getValue());

		this.width = width;
		this.height = heightTextRect;
	}

	/**
	 * Ao inicio de cada método é especificado o tamanho(altura e/ou largura) padrão
	 * que ele pode se basear
	 */
	private static final int widthIntLit = 22;

	/**
	 * Especifica a forma como este elemento da AST é gerado na vizualização
	 */
	@Override
	public void visitIntLit(IntLit ast) {
		// Recebe o lugar onde será adicionado de quem o chamou e configura a classe do
		// mesmo
		Select s = this.select.append("g").attr("class", "integer");
		
		//Redimenciona a largura do bloco até caber o valor literal
		int width = widthIntLit;
		String inner = "" + ast.getValue();
		float w = JD3String.width(inner);
		while (width < w) {
			width += widthIntLit;
		}

		//Desenha o bloco do literal
		s.append("rect").attr("x", -width / 2).attr("width", width).attr("height", heightTextRect);
		s.append("text").attr("y", heightTextRect / 2).attr("dy", ".3em").innerText("" + ast.getValue());

		this.width = width;
		this.height = heightTextRect;
	}

	/**
	 * Este métdo é o que é de fato chamado para o desenho da árvore. Ele recebe a
	 * ast e o caminho do arquivo de saída e chama as funções necessárias para dar o
	 * resultado final
	 * 
	 * @param ast  - Arvore a ser desenhada
	 * @param path - Caminho de saída para o arquivo html
	 */
	public void print(AST ast, String path) throws Exception {
		if (ast == null)
			return;
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
