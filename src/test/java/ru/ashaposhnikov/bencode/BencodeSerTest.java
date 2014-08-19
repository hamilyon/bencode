package ru.ashaposhnikov.bencode;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;

public class BencodeSerTest {

	@Test
	public void testSerializesInteger() {
		assertArrayEquals("i1e".getBytes(), BencodeSer.serializeInteger(new Integer(1)));
	}
	
	@Test
	public void testSerializesString() {
		assertArrayEquals("16:yellow_submarine".getBytes(), BencodeSer.serializeString("yellow_submarine"));
	}
	
	@Test
	public void testSerializesList() {
		assertArrayEquals("li11ei12ei22ee".getBytes(), BencodeSer.serializeList(
			Arrays.asList(new Integer[] {11,12,22})));
	}
	
	@Test
	public void testSerializesHeterogenicList() {
		assertArrayEquals("li9e1:8i7ee".getBytes(), BencodeSer.serializeList(
			Arrays.asList(new Object[] {9,"8",7})));
	}
	
	@Test
	public void testSerializesMap() {
		assertArrayEquals("de".getBytes(), BencodeSer.serializeMap(new HashMap()));
	}
	
	@Test
	public void testSerializesNonEmptyMap() {
		HashMap map = new HashMap();
		map.put("1", new ArrayList());
		assertArrayEquals("d1:1lee".getBytes(), BencodeSer.serializeMap(map));
	}
	
	

}
