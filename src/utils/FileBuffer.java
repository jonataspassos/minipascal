package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileBuffer extends Buffer {
	private BufferedReader br;
	private String brReaded;

	public FileBuffer(String path, int tabSize) {
		super(tabSize);
		File f = new File(path); // Creation of File Descriptor for input file
		FileReader fr;
		brReaded = "";

		try {

			fr = new FileReader(f);
			this.br = new BufferedReader(fr); // Creation of BufferedReader object

		} catch (FileNotFoundException e) {
			// TODO Incluir tratamento de exceções
		}

	}
	
	public FileBuffer(String path) {
		this(path,4);
	}

	@Override
	public char getChar() {
		try {
			if (brReaded.isEmpty()) {
				int temp;

				temp = br.read();

				if (temp != -1) {
					this.brReaded += (char)temp;
					return (char)temp;
				} else {
					return '\0';
				}
			} else {
				return this.brReaded.charAt(0);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return '\0';
		}
	}

	@Override
	public char consumeChar() {
		try {
			if (brReaded.isEmpty()) {
				int temp = br.read();
				if (temp != -1) {
					super.incColumn();
					return (char)temp;
				}else
					return '\0';
			} else {
				char ret = brReaded.charAt(0);
				if (brReaded.length() > 1)
					brReaded = brReaded.substring(1);
				else
					brReaded = "";
				
				super.incColumn();
				return ret;

			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return '\0';
		}

	}

	@Override
	public boolean isEmpty() {
		return getChar()=='\0';
	}

}
