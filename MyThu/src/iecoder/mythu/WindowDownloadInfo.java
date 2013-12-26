package iecoder.mythu;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/*
 * 下载窗口
 */
public class WindowDownloadInfo {
	public static JFrame downloadFrame;
	public static Container downloadContainer;
	public static JTextArea downloadTextArea;

	/*
	 * 绘制GUI
	 */
	public static void createUI() {
		WindowDownloadInfo.downloadFrame = new JFrame("下载课件");
		WindowDownloadInfo.downloadFrame.setVisible(true);
		WindowDownloadInfo.downloadContainer = downloadFrame.getContentPane();
		WindowDownloadInfo.downloadContainer.setLayout(new GridLayout(20, 2));
		WindowDownloadInfo.downloadFrame.setBounds(new Rectangle(600, 500));

		WindowDownloadInfo.downloadTextArea = new JTextArea();
		WindowDownloadInfo.downloadTextArea.setText("");
		 WindowDownloadInfo.downloadTextArea.setBounds(new Rectangle(600,
		 500));
		WindowDownloadInfo.downloadContainer
				.add(WindowDownloadInfo.downloadTextArea);
	}

	/*
	 * 更新下载信息
	 */
	public static void addInfo(final String str) {
		// WindowDownloadInfo.downloadTextArea.append(str + '\n');
		// WindowDownloadInfo.downloadTextArea.setCaretPosition(WindowDownloadInfo.downloadTextArea.getText().length()
		// - 1);
		// Runnable run = new UpdateDownloadInfo(str);
		// Thread T = new Thread(run);
		// T.start();
		// WindowDownloadInfo.downloadTextArea.setCaretPosition(WindowDownloadInfo.downloadTextArea.getDocument().getLength());

		(new Thread() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						WindowDownloadInfo.downloadTextArea.append(str + '\n');
					}
				});
			}
		}).start();
	}
}
