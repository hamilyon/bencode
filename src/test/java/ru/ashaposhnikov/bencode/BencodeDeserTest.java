package ru.ashaposhnikov.bencode;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.java.quickcheck.generator.iterable.Iterables;

import org.junit.Test;

import static net.java.quickcheck.generator.CombinedGeneratorsIterables.someLists;
import static net.java.quickcheck.generator.CombinedGeneratorsIterables.someMaps;
import static net.java.quickcheck.generator.PrimitiveGenerators.integers;

public class BencodeDeserTest {
	public static final ArbitraryStructureGenerator ARBITRARY_STRUCTURE_GENERATOR = new ArbitraryStructureGenerator();

	@Test
	public void testDeserializesInt() {
		assertEquals(1, BencodeDeser.deserialize("i1e".getBytes()));
	}

	@Test
	public void testDeserializesString() {
		assertEquals("PILLOW", BencodeDeser.deserialize("6:PILLOW".getBytes()));
	}

	@Test
	public void testDeserializesOneMoreString() {
		assertEquals("PILLOW_SUBMARINE",
			BencodeDeser.deserialize("16:PILLOW_SUBMARINE".getBytes()));
	}

	@Test
	public void testDeserializesList() {
		assertEquals(Arrays.asList(new Integer[] {11,12,22}),
				BencodeDeser.deserialize("li11ei12ei22ee".getBytes())
				);
	}

	@Test
	public void testDeserializesMap() {
		assertEquals(new HashMap(),
				BencodeDeser.deserialize("de".getBytes())
		);
	}

	@Test
	public void testSerializesNonTrivialMap() {
		List<String> keyList = Arrays.asList(new String[] {"11"});
		List<Integer> valueList = Arrays.asList(new Integer[] {22});
		HashMap resultMap = new HashMap();
		resultMap.put(keyList, valueList);
		assertEquals(resultMap,
				BencodeDeser.deserialize("dl2:11eli22eee".getBytes())
				);
	}

	@Test
	public void testArbitraryLists() {
		for (List<Integer> ints : someLists(integers())) {
			assertEquals(ints, BencodeDeser.deserialize(BencodeSer.serialize(ints)));
		}
	}	

	@Test
	public void testArbitraryMaps() {
		for (Map<Integer, Integer> map : someMaps(integers(), integers(), integers(0, 15))) {
			assertEquals(map, BencodeDeser.deserialize(BencodeSer.serialize(map)));
		}
	}	

	@Test
	public void testArbitraryDeepStructures() {
		for (Map map : Iterables.toIterable(ARBITRARY_STRUCTURE_GENERATOR)) {
			assertEquals(map, BencodeDeser.deserialize(BencodeSer.serialize(map)));
		}
	}
	
}
