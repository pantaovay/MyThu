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
	 * ����ϵͳ���UI
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

		MenuItem mainItem = new MenuItem("������");
		MenuItem aboutItem = new MenuItem("��������");
		MenuItem homeworkItem = new MenuItem("�鿴��ҵ");
		CheckboxMenuItem autoHomeworkItem = new CheckboxMenuItem("�Զ���ʾ��ҵ");
		MenuItem homeworkFrequencyItem = new MenuItem("������ʾƵ��");
		MenuItem exitItem = new MenuItem("�˳�");

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

		// ˫��ϵͳͼ���¼�
		trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WindowMain.f.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		// �鿴�������¼�
		mainItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WindowMain.f.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		// ���������¼�
		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "�廪��ѧ����ѧ�����֡���IECoder 2013");
			}
		});
		
		// ��ʾδ����ҵ
		homeworkItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WindowHomeworkInfo.createUI();
			}
		});
		
		// ��ҵ�Զ�����
		autoHomeworkItem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int autoHomeworkItemId = e.getStateChange();
				// ѡ�����Զ�����
				if(autoHomeworkItemId == ItemEvent.SELECTED) {
					try {
						HomeworkCronRunner.task();
					} catch (SchedulerException e1) {
						e1.printStackTrace();
					}
				} else {
					// ȡ��ѡ���������Զ�����
					try {
						HomeworkCronRunner.stop();
					} catch (SchedulerException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		// ������ҵ��ʾƵ��
		homeworkFrequencyItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String frequency = JOptionPane.showInputDialog(null, "����1-10�����֣�Сʱ����", 
						"������ʾƵ��", 1);
				if(frequency != null) {
					HomeworkCronRunner.frequency = frequency;
				}
			}
		});
		// �˳�
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tray.remove(trayIcon);
				// ɾ��course�е�����
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
	 * ��ȡͼƬ����
	 * @param path ͼƬ·��
	 * @param descripation ͼƬ��Դ����
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
