package ru.ashaposhnikov.bencode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class BencodeDeser {

	private BencodeDeser() { }
	
	public static Object deserialize(byte[] bytes) {
		return deserialize(PeekingIterator.peekingIterator(emitTokens(bytes).iterator()));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object deserialize(PeekingIterator<Token> iterator) {
		ParserState state = ParserState.START;
		List currentList = null;
		Map currentMap = null;
		while (iterator.hasNext()) {
			Token t = iterator.peek();

			if (state != ParserState.START) {
				
				if (t.type == BencodeTokenType.VALUE_END) {
					iterator.next();
					if (state == ParserState.LIST) {
						return currentList;
					} else if (state == ParserState.MAP) {
						return currentMap;
					} else {
						throw new IllegalArgumentException("excess closing token");
					}
					
				} else if (state.equals(ParserState.LIST)) {
					currentList.add(deserialize(iterator));
				} else if (state.equals(ParserState.MAP)) {
					currentMap.put(deserialize(iterator), deserialize(iterator));
				}
				
			} else if (t.type == BencodeTokenType.LIST_START) {
				iterator.next();
				currentList = new ArrayList();
				state = ParserState.LIST;
			} else if (t.type == BencodeTokenType.MAP_START) {
				iterator.next();
				currentMap = new HashMap();
				state = ParserState.MAP;
			} else {
				t = iterator.next();
				return t.value;
			} 
		}
		throw new IllegalArgumentException(
				"no value end detected");
	}

	private static Iterable<Token> emitTokens(final byte[] bytes) {
		
		return new Iterable<Token>() {
			@Override
			public Iterator<Token> iterator() {
				return new Iterator<Token>() {
					private int i = 0;
					@Override
					public boolean hasNext() {
						return i<bytes.length;
					}
					@Override
					public Token next() {
						if (bytes[i] == 'i') {
							int end = getIntegerEnd(bytes, i);
							Integer result = deserializeInteger(bytes, i, end);
							i = end + 1;
							return new Token(BencodeTokenType.INT_START, result);
						} else if (bytes[i] >= '0' && bytes[i] <= (byte) '9') {
							Token result = new Token(BencodeTokenType.STRING_START, 
									deserializeString 
									(bytes, i));
							i = getEndString(bytes, i) + 1;
							return result;
						} else if (bytes[i] == 'd') {
							i = i + 1;
							return new Token(BencodeTokenType.MAP_START, null);
						} else if (bytes[i] == 'l') {
							i = i + 1; 
							return new Token(BencodeTokenType.LIST_START, null);
						} else if (bytes[i] == 'e') {
							i = i + 1;						
							return new Token(BencodeTokenType.VALUE_END, null);
						}
						throw new IllegalArgumentException(
							"Incorrect token start: '" + new String(Arrays.copyOfRange(bytes, i, bytes.length)));
					}
					@Override
					public void remove() {
						throw new RuntimeException("remove not possible");
					}
				};
			}
		};
	}

	private static String deserializeString(byte[] bytes, int i) {
		byte[] stringBytes = Arrays.copyOfRange(bytes, 
				getStartString(bytes, i)+1, 
				getEndString(bytes, i)+1);
		return new String(stringBytes);
	}

	private static int getEndString(byte[] bytes, int i) {
		int startString = getStartString(bytes, i);
		return new Integer(new String(Arrays.copyOfRange(bytes, i, startString))) + startString;
	}

	private static int getStartString(byte[] bytes, int i) {
		for (;i < bytes.length; i++) {
			byte b = bytes[i];
			if (b == ':') {
				return i;
			}
		}
		throw new IllegalArgumentException(
				"deserializing str failed: no terminator");
	}

	private static Integer deserializeInteger(byte[] bytes, int start, int end) {
		return new Integer(new String(Arrays.copyOfRange(
				bytes, start + 1, end)));
	}

	private static int getIntegerEnd(byte[] bytes, int start) {
		for (int i = start; i < bytes.length; i++) {
			byte b = bytes[i];
			if (b == 'e') {
				return i;
			}
		}
		throw new IllegalArgumentException(
				"deserializing int failed: no terminator");
		
	}
}
