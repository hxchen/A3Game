package com.a3fun.core.config.annotation;


import com.a3fun.core.common.FieldValueNullCheckHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface FieldValueNullCheck {

	Class<? extends FieldValueNullCheckHandler> handler() default FieldValueNullCheckHandler.class;

	String desc() default "字段值为null";
}
