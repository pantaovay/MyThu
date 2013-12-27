package iecoder.mythu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

public class Downloader extends Thread {
	public String courseWarePath;
	public String courseDir;

	/*
	 * 构造函数
	 * @param courseWarePath 课件链接
	 * @param courseDir 课件下载目标目录
	 */
	public Downloader(ThreadGroup g, String courseWarePath, String courseDir) {
		super(g, courseDir);
		this.courseWarePath = courseWarePath;
		this.courseDir = courseDir;
	}

	/*
	 * 获得文件名
	 * @param response httpClient请求后收到的响应
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
			HttpResponse response = Http.httpClient.execute(courseWareFile);
			// 获取文件名
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
				WindowDownloadInfo.addInfo(filename + " download completes.");
			} else {
				WindowDownloadInfo.addInfo(filename + " exists.");
			}
			EntityUtils.consume(response.getEntity());
			/*ThreadGroup group = Thread.currentThread().getThreadGroup();
			group.list();
			ThreadGroup topGroup = null;
			// 取得此线程组的顶级父线程
			if((group = group.getParent()) != null) {
				topGroup = group;
			}
			// 将有关此线程组的信息打印到标准输出
			topGroup.list();*/
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}
}
