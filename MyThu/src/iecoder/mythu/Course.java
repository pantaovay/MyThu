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
 * �γ��࣬����μ����ص�
 */
public class Course {
	public String courseId;
	public String courseName;
	public HttpClient httpClient;
	
	/*
	 * ���캯��
	 * @param courseId �γ�ΨһID
	 * @param courseName �γ���
	 */
	
	public Course(String courseId, String courseName, HttpClient httpClient) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.httpClient = httpClient;
	}
	
	/*
	 * ���ؿμ�
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
		// TODO ��������뵽���ؿμ���GUI��
		System.out.println("���ص� " + this.courseName + "......");
		while (courseWaresIterator.hasNext()) {
			Element courseWaresLink = courseWaresIterator.next();
			String courseWarePath = courseWaresLink.attr("href");
			Downloader thread = new Downloader(this.httpClient, courseWarePath, this.courseName);
			thread.start();
		}
	}
	
	

}
