package iecoder.mythu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*
 * 数据库操作类，保存用户名和密码等
 */
public class UserInfo {
	/*
	 * 插入数据
	 * @param userName 用户名
	 * @param userPass 密码
	 * @param path 用户选择的存储路径
	 */
	public static void insert(String userName, String userPass, String path) throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:MyThu.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			// 先清空用户表
			statement.executeUpdate("DELETE FROM user");
			statement.executeUpdate("INSERT INTO user VALUES('" + userName + "', '" + userPass + "', '" + path + "')");
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	/*
	 * 验证登陆信息
	 * @return 用户信息：用户名、密码和存储路径
	 */
	public static String[] getUserInfo() throws ClassNotFoundException {
		String[] user = new String[3];
		user[0] = "";
		user[1] = "";
		user[2] = "";
		
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:MyThu.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);

			ResultSet result = statement.executeQuery("SELECT * FROM user LIMIT 0, 1");
			while (result.next()) {
				user[0] = result.getString("userid");
				user[1] = result.getString("userpass");
				user[2] = result.getString("path");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
		return user;
	}
	
	/*
	 * 清除认证信息
	 */
	public static void empty() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:MyThu.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);

			statement.executeUpdate("DELETE FROM user");
			statement.executeUpdate("DELETE FROM course");
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
	}
}
