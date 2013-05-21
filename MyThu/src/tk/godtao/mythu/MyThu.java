package tk.godtao.mythu;

//import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MyThu {
	public MyThu(String userid, String userpass) throws Exception {
		// �����û���������
		/*
		 * Scanner sc = new Scanner(System.in); System.out.print("�������û�����");
		 * String userid = sc.next(); System.out.print("���������룺"); String
		 * userpass = sc.next(); sc.close();
		 */

		// �������������������Ҫ������console֧��
		//Console cons = System.console();
		//String userid = cons.readLine("�������û�����");
		//String userpass = new String(cons.readPassword("���������룺"));

		// ��½
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
				.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 80, PlainSocketFactory
				.getSocketFactory()));
		ClientConnectionManager cm = new PoolingClientConnectionManager(
				schemeRegistry);
		HttpClient httpclient = new DefaultHttpClient(cm);

		HttpPost httpPost = new HttpPost(
				"https://learn.tsinghua.edu.cn/MultiLanguage/lesson/teacher/loginteacher.jsp");
		List<NameValuePair> login = new ArrayList<NameValuePair>();
		login.add(new BasicNameValuePair("userid", userid));
		login.add(new BasicNameValuePair("userpass", userpass));
		login.add(new BasicNameValuePair("submit1", "��½"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(login, "utf-8");
		httpPost.setEntity(entity);

		HttpResponse loginResponse = httpclient.execute(httpPost);

		try {
			HttpEntity loginEntity = loginResponse.getEntity();
			if (loginEntity != null) {
				String loginResult = EntityUtils.toString(loginEntity);
				if (loginResult.indexOf("loginteacher_action.jsp") != -1) {
					System.out.println("��½�ɹ���");
					// �����γ�
					HttpGet httpGet = new HttpGet(
							"http://learn.tsinghua.edu.cn/MultiLanguage/lesson/student/MyCourse.jsp?language=cn");
					HttpResponse courseResponse = httpclient.execute(httpGet);
					String coursePage = EntityUtils.toString(courseResponse
							.getEntity());
					Document coursePageDOM = Jsoup.parse(coursePage);
					Elements courses = coursePageDOM
							.select("a[href~=.*course_locate.*]");
					Iterator<Element> coursesIterator = courses.iterator();
					while (coursesIterator.hasNext()) {
						String regEx = "\\d+";
						Pattern pattern = Pattern.compile(regEx);
						Element course = coursesIterator.next();
						// ÿ���γ̽����ļ���
						String courseName = course.html();
						File courseDir = new File(courseName);
						if (!courseDir.isDirectory()) {
							courseDir.mkdir();
						}
						Matcher match = pattern.matcher(course.attr("href"));
						while (match.find()) {
							String course_id = match.group();
							HttpGet courseWare = new HttpGet(
									"http://learn.tsinghua.edu.cn/MultiLanguage/lesson/student/download.jsp?course_id="
											+ course_id);
							HttpResponse courseWareResponse = httpclient
									.execute(courseWare);
							String courseWarePage = EntityUtils
									.toString(courseWareResponse.getEntity());
							Document courseWarePageDOM = Jsoup
									.parse(courseWarePage);
							Elements courseWares = courseWarePageDOM
									.select("a[href~=.*uploadFile.*]");
							Iterator<Element> courseWaresIterator = courseWares
									.iterator();
							System.out.println("���ص��ļ���  "+courseName+"......");
							while (courseWaresIterator.hasNext()) {
								Element courseWaresLink = courseWaresIterator
										.next();
								String courseWarePath = courseWaresLink
										.attr("href");
								Downloader thread = new Downloader(httpclient,
										courseWarePath, courseName);
								thread.start();
								/*
								 * String tmp =
								 * courseWaresLink.html().replaceAll( "&.*;",
								 * ""); if (tmp.indexOf("font") != -1) { tmp =
								 * tmp.substring(18, tmp.length() - 7); } //
								 * System.out.println(tmp); if (tmp != "") {
								 * Downloader thread = new Downloader(
								 * httpclient, courseWarePath); thread.start();
								 * }
								 */
							}
						}
					}
				} else {
					System.out.println("��½ʧ��");
				}
			}
			EntityUtils.consume(loginEntity);
		} finally {
			httpPost.releaseConnection();
		}
	}
}
