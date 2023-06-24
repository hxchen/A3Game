package com.a3fun.pudding.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtils {

	private static final Logger log = LoggerFactory.getLogger(ClassUtils.class);

	public static List<String> getAllClassesNameByPackageName(String packageName, boolean isIncludeSubPackage) throws Exception {
		if (packageName == null)
			return null;
		List<String> result = new ArrayList<String>();
		Set<Class<?>> pp = getAllClassInPackageIncludeSub(packageName);
		if (pp.size() == 0) {
			log.warn("=================={}目录空==============", packageName);
		}
		for (Class<?> c : pp) {
			// 是否带包名
			if (isIncludeSubPackage) {
				result.add(c.getCanonicalName());
			} else {
				result.add(c.getSimpleName());
			}
		}
		return result;
	}

	public static Set<Class<?>> getAllClassInPackageIncludeSub(String pack) {
		Set<Class<?>> classes = new LinkedHashSet<>();
		boolean recursive = true;
		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if ("file".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
				} else if ("jar".equals(url.getProtocol())) {
					JarFile jarFile = null;
					try {
						JarURLConnection conn = (JarURLConnection) url.openConnection();
						jarFile = conn.getJarFile();
					} catch (IOException e) {
						continue;
					}
					for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
						JarEntry entry = entries.nextElement();
						String entryName = entry.getName();
						if (entryName.startsWith(packageDirName) && entryName.endsWith(".class") && entryName.indexOf("$") == -1) {
							String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
							try {
								classes.add(Class.forName(className));
							} catch (ClassNotFoundException e) {
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classes;
	}

	private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		File[] files = dir.listFiles(file -> (recursive && file.isDirectory()) || (file.getName().endsWith(".class")));
		for (File file : files) {
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
			} else {
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					classes.add(Class.forName(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
