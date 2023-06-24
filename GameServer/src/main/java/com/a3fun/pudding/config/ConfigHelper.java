package com.a3fun.pudding.config;


import com.a3fun.core.config.service.ConfigServiceImpl;
import com.a3fun.pudding.Application;

public class ConfigHelper {

	private static ConfigServiceImpl instance;

	public static ConfigServiceImpl getServiceInstance() {
		if (instance != null) {
			return instance;
		}

		instance = Application.getBean(ConfigServiceImpl.class);
		if (instance == null) {
			throw new IllegalStateException("Spring not initialized yet.");
		}
		return instance;
	}

}
