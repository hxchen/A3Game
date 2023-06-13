package com.a3fun.pudding.config.annotation;

import com.a3fun.pudding.config.bo.AbstractMeta;
import com.a3fun.pudding.config.bo.DefaultMeta;

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

    boolean autoGen() default false;
}
