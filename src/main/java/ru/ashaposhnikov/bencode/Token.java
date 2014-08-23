package ru.ashaposhnikov.bencode;

public class Token {
	public BencodeTokenType type;
	public Object value;
	public Token(BencodeTokenType type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}
}
