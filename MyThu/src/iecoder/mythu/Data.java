package iecoder.mythu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*
 * ���ݿ�����࣬�����û����������
 */
public class Data {
	/*
	 * ��������
	 * @param userName �û���
	 * @param userPass ����
	 * @param path �û�ѡ��Ĵ洢·��
	 */
	public static void insert(String userName, String userPass, String path) throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:MyThu.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.executeUpdate("insert into user values('" + userName + "', '" + userPass + "', '" + path + "')");
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
	 * ��֤��½��Ϣ
	 * @return �û���Ϣ���û���������ʹ洢·��
	 */
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

			ResultSet result = statement.executeQuery("select * from user limit 0, 1");
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
}
