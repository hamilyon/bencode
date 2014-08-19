package ru.ashaposhnikov.bencode;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public abstract class ArrayUtil {
	private ArrayUtil(){}
	public static byte[] concatAll(List<byte[]> arrays) {
	  int totalLength = 0;
	  for (byte[] array : arrays) {
	    totalLength += array.length;
	  }
	  byte[] result = (byte[]) Array.newInstance(byte.class, totalLength);
	  int offset = 0;
	  for (byte[] array : arrays) {
	    System.arraycopy(array, 0, result, offset, array.length);
	    offset += array.length;
	  }
	  return result;
	}
}
