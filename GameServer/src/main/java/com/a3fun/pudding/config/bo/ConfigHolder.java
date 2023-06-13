package com.a3fun.pudding.config.bo;


public class ConfigHolder {

	private ConfigFileBaseInfo baseInfo;;

	/** message digest */
	private final String md;

	private Object config;

	public ConfigHolder(ConfigFileBaseInfo baseInfo, String md, Object config) {
		this.baseInfo = baseInfo;
		this.md = md;
		this.config = config;
	}

	public ConfigFileBaseInfo getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(ConfigFileBaseInfo baseInfo) {
		this.baseInfo = baseInfo;
	}

	public Object getConfig() {
		return config;
	}

	public void setConfig(Object config) {
		this.config = config;
	}

	public String getMd() {
		return md;
	}

}
