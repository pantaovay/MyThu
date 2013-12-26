package iecoder.mythu;

/*
 * 多线程更新JTextArea
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
