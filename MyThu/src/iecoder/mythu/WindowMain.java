package iecoder.mythu;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.http.client.ClientProtocolException;

public class WindowMain implements ActionListener {
	static JFrame f = null;
	static String rootPath = "";
	static boolean login = false;
	JButton begin;
	String userid;
	String userpass;

	public WindowMain() throws Exception {
		Login.checkLogin();
	}

	/*
	 * MyThu主窗口，同时生成System Tray，监听作业模块
	 */
	public void MyThuWindow(String path) {
		WindowMain.rootPath = path;
		// 系统监控
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				HomeworkTray.createUI();
			}
		});
		
		f = new JFrame("MyThu");
		Container contentPane = f.getContentPane();
		contentPane.setLayout(new GridLayout(3, 2));

		JButton rootPath = new JButton("设置根目录（默认为记住的目录）" + path);
		rootPath.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JFileChooser jc = new JFileChooser();
				jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jc.setDialogTitle("MyThu选择根目录");
				int state = jc.showOpenDialog(null);
				if (state == 1) {
					return;
				} else {
					File folder = jc.getSelectedFile();
					WindowMain.rootPath = folder.getAbsolutePath();
					if (WindowLogin.rememberPass == true) {
						try {
							UserInfo.insert(Login.userId, Login.userPass, WindowMain.rootPath);
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		contentPane.add(rootPath);
		begin = new JButton("开始");
		contentPane.add(begin);
		begin.addActionListener(this);

		JButton deadline = new JButton("查看作业");
		contentPane.add(deadline);
		deadline.addActionListener(this);
		
		JButton clear = new JButton("clear");
		contentPane.add(clear);
		clear.addActionListener(this);

		f.setBounds(100, 200, 300, 200);
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
		// 隐藏主窗口
		f.dispose();
		// 获取命令
		String cmd = e.getActionCommand();
		if(cmd == "清除") {
			// fuck
		}
		// TODO 作业的GUI
		JFrame homeworkFrame = new JFrame("作业");
		Container homeworkContainer = homeworkFrame.getContentPane();
		homeworkContainer.setLayout(new GridLayout(20, 2));
		homeworkFrame.setBounds(new Rectangle(600, 500));
		ArrayList<Course> result = new ArrayList<Course>();
		try {
			try {
				result = Course.getCourses();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Iterator<Course> resultIter = result.iterator();
		while(resultIter.hasNext()) {
			Course course = resultIter.next();
			// 响应事件
			if (cmd.equals("开始")) {
				// 下载课件
				// TODO 下载的界面
				try {
					course.getCourseware();
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (cmd.equals("查看作业")) {
				// TODO GUI
				JLabel courseNameLabel = new JLabel(course.courseName);
				homeworkContainer.add(courseNameLabel);
				JTextArea courseHomeworkTextArea = new JTextArea();
				courseHomeworkTextArea.setEditable(false);
				try {
					course.setHomework();
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				ArrayList<Homework> homework = course.homeWork;
				Iterator<Homework> iter = homework.iterator();
				while(iter.hasNext()) {
					courseHomeworkTextArea.append(iter.next().end + " ");
				}
				courseHomeworkTextArea.setText("共有 " + homework.size() + " 未交作业ֹ" + courseHomeworkTextArea.getText());
				homeworkContainer.add(courseHomeworkTextArea);
			}
		}
		if (cmd.equals("查看作业")) {
			homeworkFrame.setVisible(true);
		}
	}
}
