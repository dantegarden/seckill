package com.example.seckill.one.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Properties;

public class DBUtils {
	
	private static Map<String, Object> map;
	
	static {
		try {
			InputStream in = DBUtils.class.getClassLoader().getResourceAsStream("application.yml");
			Yaml yml = new Yaml();
			map =(Map)yml.load(in);
			in.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConn() throws Exception{
		Map<String, Map<String,Object>> spring = (Map)map.get("spring");
		Map<String, Object> datasource = (Map)spring.get("datasource");
		String url = (String) datasource.get("url");
		String username = (String) datasource.get("username");
		String password = String.valueOf(datasource.get("password"));
		String driver = (String) datasource.get("driver-class-name");
		Class.forName(driver);
		return DriverManager.getConnection(url,username, password);
	}
}
