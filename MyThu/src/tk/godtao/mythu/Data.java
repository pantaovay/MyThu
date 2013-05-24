package tk.godtao.mythu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.codec.digest.DigestUtils;

public class Data {
	public static void insert(String username, String userpass, String path)
			throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");

		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:MyThu.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			/*statement.executeUpdate("insert into user values('" + username
					+ "', '" + Data.sha1(userpass) + "')");*/
			statement.executeUpdate("insert into user values('" + username
					+ "', '" + userpass + "', '" + path + "')");
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

	public static String[] verify() throws ClassNotFoundException {
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

			ResultSet result = statement.executeQuery("select * from user");
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
	
	// 用不到了
	public static String sha1(String password) {
		return DigestUtils.shaHex(password);
	}
}
