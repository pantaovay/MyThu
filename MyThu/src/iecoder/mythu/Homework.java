package iecoder.mythu;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
 * 作业类
 */
public class Homework {
	public String now;
	public String courseId;
	public HttpClient httpClient;
	
	/*
	 * 构造函数
	 * @param courseId 课程的唯一ID
	 * @param httpClient HttpClient连接实例
	 */
	public Homework(String courseId, HttpClient httpClient) {
		Date today = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		this.now = f.format(today);
		this.courseId = courseId;
		this.httpClient = httpClient;
	}
	
	/*
	 * 获取指定课程id的未交作业
	 * @return 未交作业截止日期数组
	 */
	public ArrayList<String> getHomework() throws ClientProtocolException, IOException {
		ArrayList<String> result = new ArrayList<String>();
		HttpGet courseHomwork = new HttpGet(
				"http://learn.tsinghua.edu.cn/MultiLanguage/lesson/student/hom_wk_brw.jsp?course_id=" + this.courseId);
		HttpResponse courseHomeworkResponse = this.httpClient.execute(courseHomwork);
		String courseHomeworkPage = EntityUtils.toString(courseHomeworkResponse.getEntity());
		Document courseHomeworkPageDOM = Jsoup.parse(courseHomeworkPage);
		Elements courseHomeworkDeadlines = courseHomeworkPageDOM.select("tr.tr2 > td:eq(2)");
		Iterator<Element> courseHomeworkDeadlinesIterator = courseHomeworkDeadlines.iterator();
		while (courseHomeworkDeadlinesIterator.hasNext()) {
			String courseHomeworkDeadline = courseHomeworkDeadlinesIterator.next().html();
			if (courseHomeworkDeadline.compareTo(this.now) >= 0) {
				result.add(courseHomeworkDeadline);
			}
		}
		return result;
	}

}
