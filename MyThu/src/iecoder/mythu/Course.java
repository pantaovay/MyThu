package iecoder.mythu;

import java.io.IOException;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
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
	public HttpClient httpClient;
	
	/*
	 * 构造函数
	 * @param courseId 课程唯一ID
	 * @param courseName 课程名
	 */
	
	public Course(String courseId, String courseName, HttpClient httpClient) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.httpClient = httpClient;
	}
	
	/*
	 * 下载课件
	 * @return
	 */
	public void getCourseware() throws ClientProtocolException, IOException {
		HttpGet courseWare = new HttpGet(
				"http://learn.tsinghua.edu.cn/MultiLanguage/lesson/student/download.jsp?course_id=" + this.courseId);
		HttpResponse courseWareResponse = this.httpClient.execute(courseWare);
		String courseWarePage = EntityUtils.toString(courseWareResponse.getEntity());
		Document courseWarePageDOM = Jsoup.parse(courseWarePage);
		Elements courseWares = courseWarePageDOM.select("a[href~=.*uploadFile.*]");
		Iterator<Element> courseWaresIterator = courseWares.iterator();
		// TODO 将输出导入到下载课件的GUI里
		System.out.println("下载到 " + this.courseName + "......");
		while (courseWaresIterator.hasNext()) {
			Element courseWaresLink = courseWaresIterator.next();
			String courseWarePath = courseWaresLink.attr("href");
			Downloader thread = new Downloader(this.httpClient, courseWarePath, this.courseName);
			thread.start();
		}
	}
	
	

}
