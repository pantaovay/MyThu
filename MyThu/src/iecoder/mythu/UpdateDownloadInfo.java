package iecoder.mythu;

/*
 * ���̸߳���JTextArea
 */
public class UpdateDownloadInfo implements Runnable {
	String str;
	public UpdateDownloadInfo(String s) {
		this.str = s;
	}
	public void run() {
		WindowDownloadInfo.downloadTextArea.append(this.str + '\n');
	}

}
