package tk.godtao.mythu;

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
	private HttpClient httpclient;
	private String courseWarePath;
	private String courseDir;

	public Downloader(HttpClient httpclient, String courseWarePath, String courseDir) {
		this.httpclient = httpclient;
		this.courseWarePath = courseWarePath;
		this.courseDir = courseDir;
	}

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
                        // convert to gbk encode
						filename = new String(tmp, "GBK");
					} catch (Exception e) {
                        System.out.println(e.getMessage());
					}
				}
			}
		}
		return filename;
	}

	public void run() {

		try {
			HttpGet courseWareFile = new HttpGet("http://learn.tsinghua.edu.cn"
					+ courseWarePath);
			HttpResponse response = httpclient.execute(courseWareFile);
			String filename = Downloader.getFileName(response);
			File file = new File(this.courseDir+"/"+filename);
			if (!file.exists()) {
				FileOutputStream out = new FileOutputStream(file);
				InputStream in = response.getEntity().getContent();
				byte buffer[] = new byte[20480];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				in.close();
				out.close();
				System.out.println(filename + " download completes.");
			} else {
				System.out.println(filename + " exists.");
			}
			EntityUtils.consume(response.getEntity());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}
}
