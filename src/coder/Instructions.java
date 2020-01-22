package coder;

public abstract class Instructions {

	/**
	 * LOAD(n) d[r]	Busca n-words(16bits) apartir do endereço (d + register r), e coloca na pilha
	 * @param r - registrador referência
	 * @param d - deslocamento a partir do registrador referência para inicio da leitura
	 * @param n - Numero de words(16bits) a serem carregados a partir do inicio da leitura
	 * @param comment - comentário ao final da linha
	 * */
	public abstract String getLoad(byte n, int d, String r,String comment);
	
	/**
	 * LOADA d[r]	coloca o endereço (d + register r) na pilha
	 * @param r - registrador referência
	 * @param d - deslocamento a partir do registrador referência para inicio da leitura
	 * @param comment - comentário ao final da linha
	 * */
	public abstract String getLoadA(int d, String r,String comment);
	
	/**
	 * LOADI(n)	Retira o endereço no topo da pilha e busca n-words(16bits) a partir do endereço retirado e coloca na pilha
	 * @param n - Numero de words(16bits) a serem carregados a partir do inicio da leitura
	 * @param comment - comentário ao final da linha
	 * */
	public abstract String getLoadI(byte n,String comment);

	/**
	 * LOADL d	Coloca uma word(16bits) literal na pilha
	 * @param d - valor a ser salvo na pilha
	 * @param comment - comentário ao final da linha
	 * */
	public abstract String getLoadL(Object d,String comment);
	
	/**
	 * STORE(n) d[r]	Retira n-words(16bits) da pilha e salva apartir do endereço (d + register r)
	 * @param r - registrador referência
	 * @param d - deslocamento a partir do registrador referência para inicio da leitura
	 * @param n - Numero de words(16bits) a serem retirador
	 * @param comment - comentário ao final da linha
	 * */
	public abstract String getStore(byte n, int d, String r,String comment);
	
	/**
	 * LOADI(n)	Retira o endereço no topo da pilha e Retira n-words(16bits) da pilha e salva a partir do endereço retirado
	 * @param n - Numero de words(16bits) a serem retirados
	 * @param comment - comentário ao final da linha
	 * */
	public abstract String getStoreI(byte n,String comment);
	
	/**
	 * CALL(n) d[r]	Call the routine at code address (d + register r), using the address in register n as the static link.
	 * @param r - registrador referência / ou label
	 * @param d - deslocamento a partir do registrador referência / quando label, esse parametro é desconsiderado
	 * @param n - Registrador de link estático
	 * @param comment - comentário ao final da linha
	 * */
	public abstract String getCall(String n, int d, String r,String comment);
	
	/**
	 * CALLI(n)	Retira o endereço no topo da pilha chama o procedimento para o endereço retirado
	 * @param comment - comentário ao final da linha
	 * */
	public abstract String getCallI(String comment);
	
	/**
	 * RETURN(n) d	Retorna da rotina corrente;
	 * retira n-words(16bits) de resultado da pilha, então retira as informações do frame da pilha e retira d words(16bits)(argumentos)
	 * coloca de volta o resultado no topo da pilha
	 * @param d - numero de arbumentos
	 * @param n - tamanho do resultado
	 * @param comment - comentário ao final da linha
	 * */
	public abstract String getReturn(byte n, int d,String comment);
	
	
	
	/**
	 * PUSH d	Push d words (uninitialized) on to the stack.
	 * @param d - numero de words(16bits) a serem alocadas
	 * @param comment - comentário ao final da linha
	 * */
	public abstract String getPush(int d,String comment);
	
	/**
	 * POP(n)  retira n-words(16bits) de resultado da pilha, então retira as informações do frame da pilha e retira d words(16bits)(argumentos)
	 * coloca de volta o resultado no topo da pilha
	 * @param d - numero de arbumentos
	 * @param n - tamanho do resultado
	 * @param comment - comentário ao final da linha
	 * */
	public abstract String getPop(byte n, int d,String comment);
	
	/**
	 * JUMP d[r]	Jump to code address (d + register r).
	 * @param r - registrador referência / ou label
	 * @param d - deslocamento a partir do registrador referência / quando label, esse parametro é desconsiderado
	 * @param comment - comentário ao final da linha
	 * */
	public abstract String getJump(int d,String r,String comment);
	/**
	 * JUMPI	Pop a code address from the stack, then jump to that address.
	 * @param comment - comentário ao final da linha
	 * */
	public abstract String getJumpI(String comment);
	/**
	 * JUMPIF(n) d[r]	Pop a I -word value from the stack, 
	 * then jump to code address (d + register r) if and only if that value equals n.
	 * @param r - registrador referência / ou label
	 * @param d - deslocamento a partir do registrador referência / quando label, esse parametro é desconsiderado
	 * @param n - valor a ser comparado (até 8 bits)
	 * @param comment - comentário ao final da linha
	 * */
	public abstract String getJumpIf(byte n, int d, String r,String comment);
	
	public abstract String applyLabel(String label,String comment);
	
	public abstract String comment(String comment);

	public abstract String getHalt(String comment);
	
	private static int labelCounter = 0;
	
	public String createLabel() {	
		return "lb"+labelCounter++;
	}
}
