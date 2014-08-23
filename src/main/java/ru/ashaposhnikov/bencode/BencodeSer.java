package ru.ashaposhnikov.bencode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Set;

public abstract class BencodeSer {
	private BencodeSer() { }
	
	public static byte[] serialize(Object object) {
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

	private static byte[] serializeInteger(Integer integer) {
		return ("i" + integer + "e").getBytes();
	}

	private static byte[] serializeString(String string) {
		return (String.valueOf(string.length()) + ":" + string).getBytes();
	}

	private static byte[] serializeList(Iterable<? extends Object> list) {
		List<byte[]> result = new ArrayList<byte[]>();
		result.add("l".getBytes());
		for (Object object : list) {
			result.add(serialize(object));
		}
		result.add("e".getBytes());
		return ArrayUtil.concatAll(result);
	}

	private static <V> byte[] serializeMap(Map<String, V> map) {
		TreeMap<String, Object> sortedMap = new TreeMap<String, Object>();
		sortedMap.putAll(map);
		List<byte[]> result = new ArrayList<byte[]>();
		result.add("d".getBytes());
		Set<Entry<String,Object>> entrySet = sortedMap.entrySet();

		for (Entry<String, Object> entry : entrySet) {
			result.add(serialize(entry.getKey()));
			result.add(serialize(entry.getValue()));			
		}
		result.add("e".getBytes());
		return ArrayUtil.concatAll(result);
	}

}
