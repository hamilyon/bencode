package ru.ashaposhnikov.bencode;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.iterable.Iterables;

import org.junit.Test;

import static net.java.quickcheck.generator.CombinedGenerators.nonEmptyLists;
import static net.java.quickcheck.generator.CombinedGeneratorsIterables.someLists;
import static net.java.quickcheck.generator.CombinedGeneratorsIterables.someMaps;
import static net.java.quickcheck.generator.PrimitiveGenerators.integers;
import static net.java.quickcheck.generator.PrimitiveGenerators.letterStrings;

public class BencodeDeserTest {

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
		List<Integer> keyList = Arrays.asList(new Integer[] {11});
		List<Integer> valueList = Arrays.asList(new Integer[] {22});
		HashMap resultMap = new HashMap();
		resultMap.put(keyList, valueList);
		assertEquals(resultMap,
				BencodeDeser.deserialize("dli11eeli22eee".getBytes())
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
		for (Map<Integer, Integer> map : Iterables.toIterable(asg)) {
			assertEquals(map, BencodeDeser.deserialize(BencodeSer.serialize(map)));
		}
	}
	public static final ASG asg = new ASG();
	static class ASG implements Generator<Map> {

		private static final int RANDOM_RANGE = 10;
		private static final int LENGTH_RANGE = 6;
		Generator<List<String>> strs = nonEmptyLists(letterStrings());
		Generator<List<Integer>> ints = nonEmptyLists(integers());
		Generator<String> str1 = letterStrings();
		Generator<Integer> int1 = integers();

		@Override
		public Map next() {
			HashMap map = new HashMap();
			for (int i=0; i<new Random().nextInt(LENGTH_RANGE); i++) {
				Integer choice = new Random().nextInt(RANDOM_RANGE);
				if (choice == 0) {
					map.put(int1.next(), next());
				} else if (choice == 1) {
					map.put(int1.next(), strs.next());
				} else if (choice == 2) {
					map.put(int1.next(), ints.next());
				} else {
					map.put(str1.next(), str1.next());
				}
			}
			return map;
		}
	}
}
