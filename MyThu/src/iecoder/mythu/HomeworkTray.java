package iecoder.mythu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.quartz.SchedulerException;

public class HomeworkTray {
	public static Boolean exist = false;
	/*
	 * 绘制系统监控UI
	 */
	public static void createUI() {
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported!");
			return;
		}
		final PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(createImage("assets/tsinghua.jpg", "tsinghua icon"));
		trayIcon.setImageAutoSize(true);
		final SystemTray tray = SystemTray.getSystemTray();

		MenuItem mainItem = new MenuItem("主窗口");
		MenuItem aboutItem = new MenuItem("关于我们");
		MenuItem homeworkItem = new MenuItem("查看作业");
		CheckboxMenuItem autoHomeworkItem = new CheckboxMenuItem("自动提示作业");
		MenuItem homeworkFrequencyItem = new MenuItem("设置提示频率");
		MenuItem exitItem = new MenuItem("退出");

		popup.add(mainItem);
		popup.addSeparator();
		popup.add(aboutItem);
		popup.addSeparator();
		popup.add(homeworkItem);
		popup.addSeparator();
		popup.add(autoHomeworkItem);
		popup.addSeparator();
		popup.add(homeworkFrequencyItem);
		popup.addSeparator();
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
			return;
		}

		// 双击系统图标事件
		trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WindowMain.f.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		// 查看主窗口事件
		mainItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WindowMain.f.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		// 关于我们事件
		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "清华大学网络学堂助手——IECoder 2013");
			}
		});
		
		// 显示未交作业
		homeworkItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WindowHomeworkInfo.createUI();
			}
		});
		
		// 作业自动提醒
		autoHomeworkItem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int autoHomeworkItemId = e.getStateChange();
				// 选中则自动提醒
				if(autoHomeworkItemId == ItemEvent.SELECTED) {
					try {
						HomeworkCronRunner.task();
					} catch (SchedulerException e1) {
						e1.printStackTrace();
					}
				} else {
					// 取消选择则销毁自动提醒
					try {
						HomeworkCronRunner.stop();
					} catch (SchedulerException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		// 设置作业提示频率
		homeworkFrequencyItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String frequency = JOptionPane.showInputDialog(null, "输入1-10的数字（小时）：", 
						"设置提示频率", 1);
				if(frequency != null) {
					HomeworkCronRunner.frequency = frequency;
				}
			}
		});
		// 退出
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tray.remove(trayIcon);
				// 删除course中的内容
				if(WindowLogin.rememberPass == false) {
					try {
						Course.empty();
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
				}
				System.exit(0);
			}
		});
	}

	/*
	 * 获取图片链接
	 * @param path 图片路径
	 * @param descripation 图片资源描述
	 */
	protected static Image createImage(String path, String description) {
		URL imageURL = HomeworkTray.class.getResource(path);

		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
			return null;
		} else {
			return (new ImageIcon(imageURL, description)).getImage();
		}
	}
}
