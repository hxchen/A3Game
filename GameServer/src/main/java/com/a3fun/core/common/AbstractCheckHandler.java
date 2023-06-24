package com.a3fun.core.common;


import com.a3fun.core.config.model.AbstractMeta;

import java.lang.reflect.Field;

public abstract class AbstractCheckHandler {

	public abstract boolean check(AbstractMeta meta, Field field) throws Exception;

}
