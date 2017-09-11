package com.xingyanping.util;

import java.io.IOException;
import java.util.Properties;

public class Config {
	private static final Properties prop = new Properties();
	static {
		try {
			prop.load(Config.class.getResourceAsStream("/com/xingyanping/config/config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String get(String key) {
		return prop.getProperty(key);
	}
}
