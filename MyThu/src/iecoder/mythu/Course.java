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
 * �γ��࣬����μ����ص�
 */
public class Course {
	public String courseId;
	public String courseName;
	public ArrayList<Homework> homeWork;
	
	/*
	 * ���캯��
	 * @param courseId �γ�ΨһID
	 * @param originCourseName �γ���
	 * 
	 */

	public Course(String courseId, String courseName) throws ClientProtocolException, IOException {
		this.courseId = courseId;
		this.courseName = courseName;
		this.setHomework();
	}
	
	/*
	 * ���ÿγ̵���ҵ�б�
	 */
	private void setHomework() throws ClientProtocolException, IOException {
		HttpGet courseHomwork = new HttpGet(
				"http://learn.tsinghua.edu.cn/MultiLanguage/lesson/student/hom_wk_brw.jsp?course_id=" + this.courseId);
		HttpResponse courseHomeworkResponse = Http.httpClient.execute(courseHomwork);
		String courseHomeworkPage = EntityUtils.toString(courseHomeworkResponse.getEntity());
		Document courseHomeworkPageDOM = Jsoup.parse(courseHomeworkPage);
		// ��ȡ��ҵ�б�
		Elements title = courseHomeworkPageDOM.select("tr.tr2 > td:eq(0) > a");
		Elements start = courseHomeworkPageDOM.select("tr.tr2 > td:eq(1)");
		Elements end = courseHomeworkPageDOM.select("tr.tr2 > td:eq(2)");
		Elements isSubmitted = courseHomeworkPageDOM.select("tr.tr2 > td:eq(3)");
		
		// ������ҵ
		int homeworkCount = title.size();
		for (int i = 0; i < homeworkCount; i++) {
			if (isSubmitted.get(i).html().trim() == "�Ѿ��ύ") {
				this.homeWork.add(new Homework(title.get(i).html(), start.get(i)
						.html(), end.get(i).html(), true));
			} else {
				this.homeWork.add(new Homework(title.get(i).html(), start.get(i)
						.html(), end.get(i).html(), false));
			}
		}
	}

	/*
	 * ���ؿμ�
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
		// TODO ��������뵽���ؿμ���GUI��
		System.out.println("���ص� " + this.courseName + "......");
		while (courseWaresIterator.hasNext()) {
			Element courseWaresLink = courseWaresIterator.next();
			String courseWarePath = courseWaresLink.attr("href");
			Downloader thread = new Downloader(courseWarePath, this.courseName);
			thread.start();
		}
	}

	/*
	 * ��ȡ�γ�IDs�����ֺʹ洢·��
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
