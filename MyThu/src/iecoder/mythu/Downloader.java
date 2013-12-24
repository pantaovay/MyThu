package iecoder.mythu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

public class Downloader extends Thread {
	public HttpClient httpClient;
	public String courseWarePath;
	public String courseDir;

	/*
	 * ���캯��
	 * @param httpClient HttpClientʵ��
	 * @param courseWarePath �μ�����
	 * @param courseDir �μ�����Ŀ��Ŀ¼
	 */
	public Downloader(HttpClient httpClient, String courseWarePath, String courseDir) {
		this.httpClient = httpClient;
		this.courseWarePath = courseWarePath;
		this.courseDir = courseDir;
	}

	/*
	 * ����ļ���
	 * @param response httpClient������յ�����Ӧ
	 */
	public static String getFileName(HttpResponse response) {
		Header contentHeader = response.getFirstHeader("Content-Disposition");
		String filename = null;
		if (contentHeader != null) {
			HeaderElement[] values = contentHeader.getElements();
			if (values.length == 1) {
				NameValuePair param = values[0].getParameterByName("filename");
				if (param != null) {
					try {
						filename = param.getValue();
						byte[] tmp = filename.getBytes("ISO-8859-1");
						filename = new String(tmp, "GBK");
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}
		return filename;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try {
			HttpGet courseWareFile = new HttpGet("http://learn.tsinghua.edu.cn" + this.courseWarePath);
			HttpResponse response = this.httpClient.execute(courseWareFile);
			// ��ȡ�ļ���
			String filename = Downloader.getFileName(response);
			File file = new File(this.courseDir + "/" + filename);
			if (!file.exists()) {
				FileOutputStream out = new FileOutputStream(file);
				InputStream in = response.getEntity().getContent();
				byte buffer[] = new byte[32768];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				in.close();
				out.close();
				// TODO
				System.out.println(filename + " download completes.");
			} else {
				// TODO
				System.out.println(filename + " exists.");
			}
			EntityUtils.consume(response.getEntity());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}
}