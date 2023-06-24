package com.a3fun.core.common;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

import java.beans.Introspector;

public class SpringPackageBeanNameGenerator extends AnnotationBeanNameGenerator {

	@Override
	protected String buildDefaultBeanName(BeanDefinition definition) {
		return Introspector.decapitalize(definition.getBeanClassName());
	}

}
