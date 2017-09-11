package com.xingyanping.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConnectionFactory {
	private static Properties prop = new Properties();
	static {
		try {
			prop.load(ConnectionFactory.class.getResourceAsStream("/com/xingyanping/config/db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName(prop.getProperty("driver"));
		String url = prop.getProperty("url");
		String user = prop.getProperty("user");
		String password = prop.getProperty("password");
		Connection conn = DriverManager.getConnection(url, user, password);
		return conn;
	}
	public static void close(ResultSet rs, Statement stmt, Connection conn) throws SQLException {
		if (rs != null) {
			rs.close();
		}
		if (stmt != null) {
			stmt.close();
		}
		if (conn != null) {
			conn.close();
		}
	}
}
