package iecoder.mythu;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import org.apache.http.client.ClientProtocolException;

/*
 * 作业信息窗口
 */
public class WindowHomeworkInfo {
	/*
	 * 绘制作业窗口
	 */
	public static void createUI() {
		JFrame homeworkFrame = new JFrame("作业");
		homeworkFrame.setVisible(true);
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
		while (resultIter.hasNext()) {
			Course course = resultIter.next();
			// 响应事件
			JLabel courseNameLabel = new JLabel(course.courseName);
			homeworkContainer.add(courseNameLabel);
			JTextArea courseHomeworkTextArea = new JTextArea();
			courseHomeworkTextArea.setEditable(false);
			homeworkContainer.add(courseHomeworkTextArea);
			try {
				course.setHomework();
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			ArrayList<Homework> homework = course.homeWork;
			Iterator<Homework> iter = homework.iterator();
			while (iter.hasNext()) {
				courseHomeworkTextArea.append(iter.next().end + " ");
			}
			courseHomeworkTextArea.setText("共有 " + homework.size() + " 未交作业ֹ"
					+ courseHomeworkTextArea.getText());
		}
	}
}
