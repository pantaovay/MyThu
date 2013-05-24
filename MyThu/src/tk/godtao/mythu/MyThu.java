/*
 * Author: Tao Pan
 * Email: pantaovay@gmail.com
 * Function: main function
 */
package tk.godtao.mythu;

//import java.io.Console;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

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
	static String rootPath = "";
	static boolean login = false;
	HttpClient httpclient;
	JButton begin;
	String userid;
	String userpass;

	public MyThu(String userid, String userpass) throws Exception {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
				.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 80, PlainSocketFactory
				.getSocketFactory()));
		ClientConnectionManager cm = new PoolingClientConnectionManager(
				schemeRegistry);
		httpclient = new DefaultHttpClient(cm);

		this.userid = userid;
		this.userpass = userpass;
		checkLogin();
	}

	public void MyThuWindow(String path) {
		f = new JFrame("MyThu");
		Container contentPane = f.getContentPane();
		contentPane.setLayout(new GridLayout(3, 2));

		JButton rootPath = new JButton("璁剧疆鏍圭洰褰� "+ path);
		rootPath.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JFileChooser jc = new JFileChooser();
				jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jc.setDialogTitle("MyThu");
				int state = jc.showOpenDialog(null);
				if (state == 1) {
					return;
				} else {
					File folder = jc.getSelectedFile();
					MyThu.rootPath = folder.getAbsolutePath();
					if (Login.rememberPass == true) {
						try {
							Data.insert(Login.name, Login.pass, MyThu.rootPath);
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		contentPane.add(rootPath);
		begin = new JButton("寮�");
		contentPane.add(begin);
		begin.addActionListener(this);

		JButton deadline = new JButton("鏌ョ湅鎴村痉鍏�");
		contentPane.add(deadline);
		deadline.addActionListener(this);

		f.setBounds(200, 150, 400, 130);
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
		f.dispose();
		String cmd = e.getActionCommand();
		// System.out.println(cmd);

		Date today = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		// System.out.println(f.format(today));

		String now = f.format(today);

		JFrame homeworkFrame = new JFrame("鎴村痉鍏�");
		Container homeworkContainer = homeworkFrame.getContentPane();
		homeworkContainer.setLayout(new GridLayout(20, 2));
		homeworkFrame.setBounds(new Rectangle(600, 500));
		// JScrollPane scrollPane = new JScrollPane();
		// JList list = new JList();
		// JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		// scrollPane, list);
		// splitPane.setContinuousLayout(true);
		try {
			HttpGet httpGet = new HttpGet(
					"http://learn.tsinghua.edu.cn/MultiLanguage/lesson/student/MyCourse.jsp?language=cn");
			HttpResponse courseResponse = httpclient.execute(httpGet);
			String coursePage = EntityUtils
					.toString(courseResponse.getEntity());
			Document coursePageDOM = Jsoup.parse(coursePage);
			Elements courses = coursePageDOM
					.select("a[href~=.*course_locate.*]");
			Iterator<Element> coursesIterator = courses.iterator();
			while (coursesIterator.hasNext()) {
				String regEx = "\\d+";
				Pattern pattern = Pattern.compile(regEx);
				Element course = coursesIterator.next();
				// 脙驴赂枚驴脦鲁脤陆篓脕垄脦脛录镁录脨
				String courseName;
				String originCourseName = course.html();
				if (rootPath == "") {
					courseName = originCourseName;
				} else {
					courseName = rootPath + "/";
					courseName += originCourseName;
				}
				courseName = courseName.toString();
				File courseDir = new File(courseName);
				if (!courseDir.isDirectory()) {
					courseDir.mkdir();
				}
				Matcher match = pattern.matcher(course.attr("href"));

				while (match.find()) {
					String course_id = match.group();
					if (cmd.equals("寮�")) {
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
						System.out.println("涓嬭浇鍒版枃浠跺す  " + courseName + "......");
						while (courseWaresIterator.hasNext()) {
							Element courseWaresLink = courseWaresIterator
									.next();
							String courseWarePath = courseWaresLink
									.attr("href");
							Downloader thread = new Downloader(httpclient,
									courseWarePath, courseName);
							thread.start();
						}
					}
					if (cmd.equals("鏌ョ湅鎴村痉鍏�)) {
						HttpGet courseHomwork = new HttpGet(
								"http://learn.tsinghua.edu.cn/MultiLanguage/lesson/student/hom_wk_brw.jsp?course_id="
										+ course_id);
						HttpResponse courseHomeworkResponse = httpclient
								.execute(courseHomwork);
						String courseHomeworkPage = EntityUtils
								.toString(courseHomeworkResponse.getEntity());
						Document courseHomeworkPageDOM = Jsoup
								.parse(courseHomeworkPage);
						JLabel courseNameLabel = new JLabel(originCourseName);
						homeworkContainer.add(courseNameLabel);
						JTextArea courseHomeworkTextArea = new JTextArea();
						courseHomeworkTextArea.setEditable(false);
						/*
						 * Elements courseHomeworkNames = courseHomeworkPageDOM
						 * .select("tr.tr2 > td > a"); Iterator<Element>
						 * courseHomeworkNamesIterator = courseHomeworkNames
						 * .iterator(); JLabel courseNameLabel = new
						 * JLabel(originCourseName);
						 * homeworkContainer.add(courseNameLabel); JTextArea
						 * courseHomeworkTextArea = new JTextArea();
						 * courseHomeworkTextArea.setEditable(false); while
						 * (courseHomeworkNamesIterator.hasNext()) { String
						 * courseHomeworkName = courseHomeworkNamesIterator
						 * .next().html(); //
						 * System.out.println(courseHomeworkName);
						 * courseHomeworkTextArea.append(courseHomeworkName); }
						 */
						Elements courseHomeworkDeadlines = courseHomeworkPageDOM
								.select("tr.tr2 > td:eq(2)");
						Iterator<Element> courseHomeworkDeadlinesIterator = courseHomeworkDeadlines
								.iterator();
						int count = 0;
						while (courseHomeworkDeadlinesIterator.hasNext()) {
							String courseHomeworkDeadline = courseHomeworkDeadlinesIterator
									.next().html();
							// System.out.println(courseHomeworkDeadline);
							if (courseHomeworkDeadline.compareTo(now) >= 0) {
								courseHomeworkTextArea
										.append(courseHomeworkDeadline + "|");
								count++;
							}
						}
						courseHomeworkTextArea.setText("浣犳湁 " + count
								+ " 涓湭浜や綔涓� "
								+ courseHomeworkTextArea.getText());
						homeworkContainer.add(courseHomeworkTextArea);
					}
				}
			}
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
		if (cmd.equals("鏌ョ湅鎴村痉鍏�)) {
			// homeworkFrame.add(splitPane);
			homeworkFrame.setVisible(true);
		}
	}

	public void checkLogin() throws Exception {
		HttpPost httpPost = new HttpPost(
				"https://learn.tsinghua.edu.cn/MultiLanguage/lesson/teacher/loginteacher.jsp");
		List<NameValuePair> login = new ArrayList<NameValuePair>();
		login.add(new BasicNameValuePair("userid", userid));
		login.add(new BasicNameValuePair("userpass", userpass));
		login.add(new BasicNameValuePair("submit1", "鎻愪氦"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(login, "utf-8");
		httpPost.setEntity(entity);
		HttpResponse loginResponse = httpclient.execute(httpPost);
		HttpEntity loginEntity = loginResponse.getEntity();
		if (loginEntity != null) {
			String loginResult = EntityUtils.toString(loginEntity);
			if (loginResult.indexOf("loginteacher_action.jsp") != -1) {
				MyThu.login = true;
			}
		}
		EntityUtils.consume(loginEntity);
	}
}
