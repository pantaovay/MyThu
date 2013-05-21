package tk.godtao.mythu;

//import java.io.Console;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

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

public class MyThu implements ActionListener {
	static JFrame f = null;
	JTextPane downloadInformation;
	JTextField rootPath;
	JButton begin;
	String userid;
	String userpass;

	public MyThu(String userid, String userpass) throws Exception {
		this.userid = userid;
		this.userpass = userpass;
		f = new JFrame("MyThu");
		Container contentPane = f.getContentPane();

		JPanel panel = new JPanel();

		rootPath = new JTextField("设置根目录");
		panel.add(rootPath);

		downloadInformation = new JTextPane();
		panel.add(downloadInformation);

		begin = new JButton("开始");
		panel.add(begin);
		begin.addActionListener(this);

		contentPane.add(panel);
		f.getRootPane().setDefaultButton(begin);
		f.pack();
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				System.exit(0);
			}
		});
		f.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		// 输入用户名和密码
		/*
		 * Scanner sc = new Scanner(System.in); System.out.print("请输入用户名：");
		 * String userid = sc.next(); System.out.print("请输入密码："); String
		 * userpass = sc.next(); sc.close();
		 */

		// 屏蔽密码输出，但是需要真正的console支持
		// Console cons = System.console();
		// String userid = cons.readLine("请输入用户名：");
		// String userpass = new String(cons.readPassword("请输入密码："));
		try {
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
			login.add(new BasicNameValuePair("submit1", "登陆"));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(login,
					"utf-8");
			httpPost.setEntity(entity);

			HttpResponse loginResponse = httpclient.execute(httpPost);
			HttpEntity loginEntity = loginResponse.getEntity();
			if (loginEntity != null) {
				String loginResult = EntityUtils.toString(loginEntity);
				if (loginResult.indexOf("loginteacher_action.jsp") != -1) {
					System.out.println("登陆成功！");
					// 解析课程
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
						// 每个课程建立文件夹
						String courseName = rootPath.getText() + "/";
						courseName += course.html();
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
							System.out.println("下载到文件夹  " + courseName
									+ "......");
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
					System.out.println("登陆失败");
				}
			}
			EntityUtils.consume(loginEntity);
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
	}
}
