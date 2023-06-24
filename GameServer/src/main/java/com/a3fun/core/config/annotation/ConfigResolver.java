package com.a3fun.core.config.annotation;

import com.a3fun.core.config.model.ConfigFormat;

public interface ConfigResolver {
    ConfigFormat type();

    Object resolve(Class<?> configClass, Config configAnno, byte[] content);
}
