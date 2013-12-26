package iecoder.mythu;

import java.io.IOException;
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
		this.setHomework();
	}
	
	/*
	 * 设置课程的作业列表
	 */
	private void setHomework() throws ClientProtocolException, IOException {
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
		System.out.println("下载到 " + this.courseName + "......");
		while (courseWaresIterator.hasNext()) {
			Element courseWaresLink = courseWaresIterator.next();
			String courseWarePath = courseWaresLink.attr("href");
			Downloader thread = new Downloader(courseWarePath, this.courseName);
			thread.start();
		}
	}

	/*
	 * 获取课程IDs，名字和存储路径
	 */
	/*public static ArrayList<Course> getCourseIds() throws ClientProtocolException, IOException {
		ArrayList<Course> result = new ArrayList<Course>();
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
			String courseName;
			String originCourseName = course.html();
			if (WindowMain.rootPath == "") {
				courseName = originCourseName;
			} else {
				courseName = WindowMain.rootPath + "/";
				courseName += originCourseName;
			}
			courseName = courseName.toString();
			Matcher match = pattern.matcher(course.attr("href"));
			
			while(match.find()) {
				result.add(new Course(match.group(), courseName, originCourseName));
			}
		}
		return result;
	}*/
	
	

}
