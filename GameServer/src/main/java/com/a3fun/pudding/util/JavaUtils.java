package com.a3fun.pudding.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * java语言和jdk类库层面的一些便捷方法
 *
 * @author zheng.sun
 */
public class JavaUtils {

	public static final Object[] EMPTY_OBJS = new Object[0];

	public static final Class<?>[] EMPTY_CLASSES = new Class[0];

	private static final boolean IS_WINDOWS;

	private static final int DEFAULT_SUB_COLLECTION_COUNT = 1000;

	static {
		String os = SystemPropertyUtils.get("os.name", "").toLowerCase();
		// windows
		IS_WINDOWS = os.contains("win");
	}

	/**
	 * Return {@code true} if the JVM is running on Windows
	 *
	 */
	public static boolean isWindows() {
		return IS_WINDOWS;
	}

	/**
	 * 以字符串形式返回堆栈信息
	 * 
	 * @param t
	 * @return
	 */
	public static String stackTraceToString(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		pw.flush();
		return sw.toString();
	}


	public static RuntimeException sneakyThrow(Throwable t) {
		if (t == null)
			throw new NullPointerException("t");
		JavaUtils.<RuntimeException>sneakyThrow0(t);
		return null;
	}

	@SuppressWarnings("unchecked")
	private static <T extends Throwable> void sneakyThrow0(Throwable t) throws T {
		throw (T) t;
	}

	public static String getSimpleName(Class<?> clazz) {
		if (clazz.isArray()) {
			return getSimpleName(clazz.getComponentType()) + "[]";
		}
		String sname = null;
		String className = clazz.getName();
		int idx = className.lastIndexOf('.');
		if (idx != -1) {
			sname = className.substring(idx + 1);
		} else {
			sname = className;
		}
		return sname;
	}

	/**
	 * 获得sun.misc.Unsafe的实例
	 * 
	 * @return
	 */
	@SuppressWarnings("restriction")
	public static sun.misc.Unsafe getUnsafe() {
		try {
			Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			sun.misc.Unsafe unsafe = (sun.misc.Unsafe) f.get(null);
			return unsafe;
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw sneakyThrow(e);
		}
	}

	public static boolean bool(Collection<?> c) {
		return c != null && c.size() > 0;
	}

	public static boolean bool(Map<?, ?> m) {
		return m != null && m.size() > 0;
	}

	public static boolean bool(CharSequence s) {
		return s != null && s.length() > 0;
	}

	public static boolean bool(Object[] objs) {
		return objs != null && objs.length > 0;
	}

	public static boolean bool(Long id) {
		return id != null && id > 0;
	}

	/**
	 * 把一个collection~按照每个子Collection长度为1000的方式~分成几个子List的列表
	 * @param objs
	 * @param <T>
	 * @return
	 */
	public static <T extends Object> List<List<T>>  parseCollection(Collection<T> objs) {
		return parseCollection(objs, DEFAULT_SUB_COLLECTION_COUNT);
	}

	/**
	 * 把一个collection~按照固定长度~分成几个子List的列表
	 * @param objs
	 * @param subCount
	 * @param <T>
	 * @return
	 */
	public static <T extends Object> List<List<T>>  parseCollection(Collection<T> objs, int subCount) {
		List<List<T>> result = new ArrayList<>();
		List<T> objList = new ArrayList<>();
		int cnt = 0;
		for(T obj : objs) {
			if(cnt % subCount == 0) {
				objList = new ArrayList<>();
				result.add(objList);
			}
			objList.add(obj);
			cnt ++;
		}

		return result;
	}

}
