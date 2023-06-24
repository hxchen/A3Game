package com.a3fun.core.common;

import com.a3fun.core.config.annotation.FieldValueNullCheck;
import com.a3fun.core.config.model.AbstractMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class MetaCheckResolver {

	private static final Logger logger = LoggerFactory.getLogger(MetaCheckResolver.class);

	public static void check(AbstractMeta meta) throws Exception {
		Class<? extends AbstractMeta> class1 = meta.getClass();
		Field[] declaredFields = class1.getDeclaredFields();
		for (Field field : declaredFields) {
			boolean oldAccessible = field.isAccessible();
			field.setAccessible(true);

			FieldValueNullCheck fieldValueNullCheck = field.getAnnotation(FieldValueNullCheck.class);
			if (fieldValueNullCheck != null) {
				AbstractCheckHandler checkHandler = CheckHandlerFactory.getCheckHandler(fieldValueNullCheck.handler());
				boolean check = checkHandler.check(meta, field);
				if (check) {
					StringBuilder info = new StringBuilder().append("策划配置数据检测异常:").append(meta.getClass().getSimpleName()).append(".").append(field.getName()).append(",错误类型")
							.append(fieldValueNullCheck.desc());
					logger.error(info.toString());
					throw new RuntimeException(info.toString());
				}
			}

			field.setAccessible(oldAccessible);
		}
	}
}
