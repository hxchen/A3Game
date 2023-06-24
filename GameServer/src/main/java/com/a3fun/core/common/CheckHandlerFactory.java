package com.a3fun.core.common;

import com.a3fun.pudding.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckHandlerFactory {

	private static final Logger log = LoggerFactory.getLogger(CheckHandlerFactory.class);

	private static final Map<String, AbstractCheckHandler> handlers;

	static {
		final Map<String, AbstractCheckHandler> m = new HashMap<>();
		try {
			List<String> classNames = ClassUtils.getAllClassesNameByPackageName(AbstractCheckHandler.class.getPackage().getName() + ".handler", true);
			if (classNames != null && classNames.size() > 0) {
				for (String className : classNames) {
					AbstractCheckHandler handler = (AbstractCheckHandler) Class.forName(className).newInstance();
					m.put(className, handler);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		handlers = Collections.unmodifiableMap(m);
	}

	public static AbstractCheckHandler getCheckHandler(Class<? extends AbstractCheckHandler> clas) {
		AbstractCheckHandler handler = handlers.get(clas.getName());
		if (handler == null) {
			log.error("找不到对应的config检测处理器{}", clas.getName());
			throw new RuntimeException("找不到对应的config检测处理器" + clas.getName());
		}
		return handler;
	}
}
