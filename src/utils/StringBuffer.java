package utils;

public class StringBuffer extends Buffer {
	
	private String buffer;
	
	public StringBuffer(String buffer) {
		super();
		this.buffer = buffer;
	}

	public StringBuffer(String buffer,int tabSize) {
		super(tabSize);
		this.buffer = buffer;
	}

	@Override
	public char getChar() {
		if(!buffer.isEmpty())
			return buffer.charAt(0);
		else
			return '\0';
	}

	@Override
	public char consumeChar() {
		if(!buffer.isEmpty()) {
			char ret = buffer.charAt(0);
			try {
				buffer = buffer.substring(1);
			}catch(Exception e) {
				buffer = "";
			}
			super.incColumn();
			return ret;
		}else {
			return '\0';
		}
	}

	@Override
	public boolean isEmpty() {
		return buffer.isEmpty();
	}
}
