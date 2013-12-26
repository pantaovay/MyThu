package iecoder.mythu;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

/*
 * http连接类，初始化httpClient
 */
public class Http {
	public static HttpClient httpClient;
	
	public Http() {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 80, PlainSocketFactory.getSocketFactory()));
		ClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
		httpClient = new DefaultHttpClient(cm);
	}

}
