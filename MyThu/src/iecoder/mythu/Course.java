package iecoder.mythu;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * 课程类，处理课件下载等
 */
public class Course {
	public String courseId;
	public String courseName;
	public ArrayList<Homework> homeWork;
	
	/*
	 * 构造函数
	 * @param courseId 课程唯一ID
	 * @param originCourseName 课程名
	 * 
	 */

	public Course(String courseId, String courseName) throws ClientProtocolException, IOException {
		this.courseId = courseId;
		this.courseName = courseName;
		this.homeWork = new ArrayList<Homework>();
	}
	
	/*
	 * 设置课程的作业列表
	 */
	public void setHomework() throws ClientProtocolException, IOException {
		HttpGet courseHomwork = new HttpGet(
				"http://learn.tsinghua.edu.cn/MultiLanguage/lesson/student/hom_wk_brw.jsp?course_id=" + this.courseId);
		HttpResponse courseHomeworkResponse = Http.httpClient.execute(courseHomwork);
		String courseHomeworkPage = EntityUtils.toString(courseHomeworkResponse.getEntity());
		Document courseHomeworkPageDOM = Jsoup.parse(courseHomeworkPage);
		// 获取作业列表
		Elements title = courseHomeworkPageDOM.select("tr.tr2 > td:eq(0) > a");
		Elements start = courseHomeworkPageDOM.select("tr.tr2 > td:eq(1)");
		Elements end = courseHomeworkPageDOM.select("tr.tr2 > td:eq(2)");
		Elements isSubmitted = courseHomeworkPageDOM.select("tr.tr2 > td:eq(3)");
		
		// 遍历作业
		int homeworkCount = title.size();
		for (int i = 0; i < homeworkCount; i++) {
			if (isSubmitted.get(i).html().trim() == "已经提交") {
				this.homeWork.add(new Homework(title.get(i).html(), start.get(i)
						.html(), end.get(i).html(), true));
			} else {
				this.homeWork.add(new Homework(title.get(i).html(), start.get(i)
						.html(), end.get(i).html(), false));
			}
		}
	}

	/*
	 * 下载课件
	 * @return
	 */
	public void getCourseware() throws ClientProtocolException, IOException {
		// 下载的绝对路径
		String coursePath = WindowMain.rootPath + '/' + this.courseName;
		// 创建课程目录
		File courseDir = new File(coursePath);
		if (!courseDir.isDirectory()) {
			courseDir.mkdir();
		}
		HttpGet courseWare = new HttpGet(
				"http://learn.tsinghua.edu.cn/MultiLanguage/lesson/student/download.jsp?course_id="
						+ this.courseId);
		HttpResponse courseWareResponse = Http.httpClient.execute(courseWare);
		String courseWarePage = EntityUtils.toString(courseWareResponse
				.getEntity());
		Document courseWarePageDOM = Jsoup.parse(courseWarePage);
		Elements courseWares = courseWarePageDOM
				.select("a[href~=.*uploadFile.*]");
		Iterator<Element> courseWaresIterator = courseWares.iterator();
		// TODO 将输出导入到下载课件的GUI里
		System.out.println("下载到 " + coursePath + "......");
		while (courseWaresIterator.hasNext()) {
			Element courseWaresLink = courseWaresIterator.next();
			String courseWarePath = courseWaresLink.attr("href");
			Downloader thread = new Downloader(courseWarePath, coursePath);
			thread.start();
		}
	}

	/*
	 * 抓取课程信息存入数据库
	 */
	public static void setCourses() throws ClientProtocolException, IOException, ClassNotFoundException {
		HttpGet httpGet = new HttpGet(
				"http://learn.tsinghua.edu.cn/MultiLanguage/lesson/student/MyCourse.jsp?language=cn");
		HttpResponse courseResponse = Http.httpClient.execute(httpGet);
		String coursePage = EntityUtils.toString(courseResponse.getEntity());
		Document coursePageDOM = Jsoup.parse(coursePage);
		Elements courses = coursePageDOM.select("a[href~=.*course_locate.*]");
		Iterator<Element> coursesIterator = courses.iterator();
		while (coursesIterator.hasNext()) {
			String regEx = "\\d+";
			Pattern pattern = Pattern.compile(regEx);
			Element course = coursesIterator.next();
			String courseName = course.html().toString();
			Matcher match = pattern.matcher(course.attr("href"));
			while(match.find()) {
				insert(match.group(), courseName);
			}
		}
	}
	
	/*
	 * 插入数据
	 * @param courseId 课程ID
	 * @param courseName 课程名
	 */
	private static void insert(String courseId, String courseName) throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:MyThu.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			// 检查是否 已存在，不存在课程ID才插入
			ResultSet result = statement.executeQuery("SELECT * FROM course WHERE courseid=" + courseId);
			if(!result.next()) {
				System.out.println("test");
				statement.executeUpdate("INSERT INTO course values('" + courseId + "', '" + courseName + "')");
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
	}

	/*
	 * 获取课程信息
	 * @return 课程信息数组
	 */
	public static ArrayList<Course> getCourses() throws ClassNotFoundException, ClientProtocolException, IOException {
		// 课程数组
		ArrayList<Course> courses = new ArrayList<Course>();
		
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:MyThu.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);

			ResultSet result = statement.executeQuery("SELECT * FROM course");
			while (result.next()) {
				courses.add(new Course(result.getString("courseid"), result.getString("coursename")));
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
		return courses;
	}

}
