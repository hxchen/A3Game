package com.a3fun.core.util;


public class JavaUtils {



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
	public static RuntimeException sneakyThrow(Throwable t) {
		if (t == null)
			throw new NullPointerException("t");
		JavaUtils.<RuntimeException>sneakyThrow0(t);
		return null;
	}
	private static <T extends Throwable> void sneakyThrow0(Throwable t) throws T {
		throw (T) t;
	}

}
