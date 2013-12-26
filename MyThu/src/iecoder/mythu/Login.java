package iecoder.mythu;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/*
 * 登陆类，提供用户登陆验证
 */
public class Login {
	public static String userId;
	public static String userPass;
	
	public static void checkLogin() throws Exception {
		HttpPost httpPost = new HttpPost(
				"https://learn.tsinghua.edu.cn/MultiLanguage/lesson/teacher/loginteacher.jsp");
		List<NameValuePair> login = new ArrayList<NameValuePair>();
		login.add(new BasicNameValuePair("userid", userId));
		login.add(new BasicNameValuePair("userpass", userPass));
		login.add(new BasicNameValuePair("submit1", "提交"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(login, "utf-8");
		httpPost.setEntity(entity);
		HttpResponse loginResponse = Http.httpClient.execute(httpPost);
		HttpEntity loginEntity = loginResponse.getEntity();
		if (loginEntity != null) {
			String loginResult = EntityUtils.toString(loginEntity);
			if (loginResult.indexOf("loginteacher_action.jsp") != -1) {
				WindowMain.login = true;
			}
		}
		EntityUtils.consume(loginEntity);
	}

}
