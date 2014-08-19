package ru.ashaposhnikov.bencode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class BencodeSer {
	private BencodeSer() { }
	
	private static byte[] serialize(Object object) {
		if (object instanceof Integer) {
			return serializeInteger((Integer) object);
		} else if (object instanceof String) {
			return serializeString((String) object);
		} else if (object instanceof Iterable) {
			return serializeList((Iterable) object);
		} else if (object instanceof Map) {
			return serializeMap((Map) object);
		} else {
			throw new IllegalArgumentException("Structure contains not serializable object " + object);
		}
	}

	public static byte[] serializeInteger(Integer integer) {
		return ("i" + integer + "e").getBytes();
	}

	public static byte[] serializeString(String string) {
		return (String.valueOf(string.length()) + ":" + string).getBytes();
	}

	public static byte[] serializeList(Iterable<? extends Object> list) {
		List<byte[]> result = new ArrayList<byte[]>();
		result.add("l".getBytes());
		for (Object object : list) {
			result.add(serialize(object));
		}
		result.add("e".getBytes());
		return ArrayUtil.concatAll(result);
	}

	public static byte[] serializeMap(Map map) {
		List<byte[]> result = new ArrayList<byte[]>();
		result.add("d".getBytes());
		Set<Entry> entrySet = map.entrySet();
		for (Entry entry : entrySet) {
			result.add(serialize(entry.getKey()));
			result.add(serialize(entry.getValue()));			
		}
		result.add("e".getBytes());
		return ArrayUtil.concatAll(result);
	}

}
