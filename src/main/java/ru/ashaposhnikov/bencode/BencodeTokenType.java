package ru.ashaposhnikov.bencode;

public enum BencodeTokenType {
	LIST_START,
	INT_START,
	VALUE_END,
	MAP_START,
	STRING_START
}
