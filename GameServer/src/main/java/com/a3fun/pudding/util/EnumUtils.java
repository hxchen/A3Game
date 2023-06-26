package com.a3fun.pudding.util;


import com.a3fun.core.common.IdEnum;
import com.a3fun.core.common.IntEnum;
import com.a3fun.core.util.JavaUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EnumUtils {

	private static final int MAX_INDEX = 9999;

	public static <E extends IntEnum> E[] toArray(E[] enums) {
		int maxIndex = 0;
		for (E e : enums) {
			int curIdx = e.getId();

			if (curIdx < 0) {
				throw new IndexOutOfBoundsException("Enum index cannot be negative: Type=" + e.getClass() + ", index=" + curIdx);
			}

			if (curIdx > MAX_INDEX) {
				throw new IllegalStateException("Enum index is too big: Type=" + e.getClass() + ", index=" + curIdx);
			}

			if (curIdx > maxIndex) {
				maxIndex = curIdx;
			}

		}

		@SuppressWarnings("unchecked")
		E[] enumArray = (E[]) Array.newInstance(enums.getClass().getComponentType(), maxIndex + 1);
		for (E e : enums) {
			int curIdx = e.getId();

			E oldenum = enumArray[curIdx];
			if (oldenum != null) {
				throw new IllegalStateException("Enum has duplicate index: Type=" + e.getClass() + ", index=" + curIdx + ",oldenum=" + oldenum + ",newenum=" + e);
			}
			enumArray[curIdx] = e;
		}
		return enumArray;
	}

	public static <E extends IntEnum> Map<Integer, E> toMap(E[] enums) {
		Map<Integer, E> map = new HashMap<>();

		for (E e : enums) {
			int curIdx = e.getId();

			if (map.containsKey(curIdx)) {
				throw new IllegalStateException("Enum has duplicate index: Type=" + e.getClass() + ", index=" + curIdx);
			}
			map.put(curIdx, e);
		}
		return map;
	}

	public static <T, E extends IdEnum<T>> Map<T, E> toMap(E[] enums) {
		Map<T, E> map = new HashMap<T, E>();

		for (E e : enums) {
			T id = e.getId();

			if (map.containsKey(id)) {
				throw new IllegalStateException("Enum has duplicate id: Type=" + e.getClass() + ", id=" + id);
			}
			map.put(id, e);
		}
		return map;
	}

	public static <T extends Enum<T>> T valueOfIgnoreCase(Class<T> enumType, String name) {
		try {
			return Enum.valueOf(enumType, name);
		} catch (IllegalArgumentException ex) {
			// no match
		}

		T[] enumConstants = enumType.getEnumConstants();
		for (T e : enumConstants) {
			if (e.name().equalsIgnoreCase(name)) {
				return e;
			}
		}

		throw new IllegalArgumentException("No enum constant " + enumType.getCanonicalName() + "." + name);
	}

	@SuppressWarnings("unchecked")
	public static <T extends IntEnum> T valueOf(Class<T> enumType, int value) {
		Method findByIdMethod = null;
		try {
			findByIdMethod = enumType.getMethod("findById", int.class);
		} catch (Exception e) {
			throw JavaUtils.sneakyThrow(e);
		}
		T rst;
		try {
			rst = (T) findByIdMethod.invoke(null, value);
		} catch (Exception e) {
			throw JavaUtils.sneakyThrow(e);
		}
		return rst;
	}

}
