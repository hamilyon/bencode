package ru.ashaposhnikov.bencode;

import static net.java.quickcheck.generator.CombinedGenerators.nonEmptyLists;
import static net.java.quickcheck.generator.PrimitiveGenerators.integers;
import static net.java.quickcheck.generator.PrimitiveGenerators.letterStrings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.java.quickcheck.Generator;

class ArbitraryStructureGenerator implements Generator<Map> {

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
				map.put(str1.next(), next());
			} else if (choice == 1) {
				map.put(str1.next(), strs.next());
			} else if (choice == 2) {
				map.put(str1.next(), ints.next());
			} else {
				map.put(str1.next(), str1.next());
			}
		}
		return map;
	}
}