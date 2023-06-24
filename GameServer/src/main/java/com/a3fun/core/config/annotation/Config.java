package com.a3fun.core.config.annotation;

import com.a3fun.core.config.model.AbstractMeta;
import com.a3fun.core.config.model.ConfigFormat;
import com.a3fun.core.config.model.DefaultMeta;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.TYPE })
public @interface Config {
    String name();

    Class<? extends AbstractMeta> metaClass() default DefaultMeta.class;

    ConfigFormat format() default ConfigFormat.JSON;

    boolean autoGen() default false;
}
