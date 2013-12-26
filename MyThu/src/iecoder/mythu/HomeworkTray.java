package iecoder.mythu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class HomeworkTray {
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

		MenuItem aboutItem = new MenuItem("About");
		MenuItem homeworkItem = new MenuItem("Homework");
		MenuItem exitItem = new MenuItem("Exit");

		popup.add(aboutItem);
		popup.addSeparator();
		popup.add(homeworkItem);
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
				JOptionPane.showMessageDialog(null,"�廪��ѧ����ѧ�����֡���IECoder");
			}
		});

		// ���������¼�
		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "�廪��ѧ����ѧ�����֡���IECoder");
			}
		});
		
		// ��ʾδ����ҵ
		homeworkItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ���� TODO ��ҵGUI
			}
		});
		
		// �˳�
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tray.remove(trayIcon);
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
