package com.a3fun.core.common;


import com.a3fun.core.config.model.AbstractMeta;

import java.lang.reflect.Field;

public class FieldValueNullCheckHandler extends AbstractCheckHandler {

	@Override
	public boolean check(AbstractMeta meta, Field field) throws Exception {
		Object value = field.get(meta);
		return value == null;
	}

}
