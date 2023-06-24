package com.a3fun.core.config.model;



import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum MetaServerType {

	BASE("Base"),
	KVK("Season"),

	;

	private static Map<String, MetaServerType> DATA;

	private String key;

	private MetaServerType(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public static MetaServerType find(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		if (DATA == null) {
			DATA = new HashMap<>();
			for (MetaServerType metaServerType : values()) {
				DATA.put(metaServerType.getKey().toLowerCase(), metaServerType);
			}
		}
		String str = key.toLowerCase();
		MetaServerType metaServerType = DATA.get(str);
		if (metaServerType == null) {
			throw new RuntimeException("枚举MetaServerType 缺少对应的值，key=" + key);
		}
		return metaServerType;
	}
}
