package com.tudou.android.fw.model.ambassador.impl;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class TudouHttpClient {	
	public static String TAG = "TudouHttpClient";
	
	public static HttpClient customerHttpClient = null;
	public static ClientConnectionManager conMgr = null;

	/**
	 * 用单例模式设计这个连接的工具类
	 * 
	 * @return customerHttpClient 返回这个连接
	 */
	private TudouHttpClient() {
		super();
	}
	
	public static synchronized HttpClient getHttpClient() {

		if (null == customerHttpClient) {
			// 为这个Http连接设置参数
			HttpParams mParams = new BasicHttpParams();
			// 设置字符编码
			HttpProtocolParams.setContentCharset(mParams, HTTP.UTF_8);
			// 设置从连接池中取出连接的超时时间，单位：毫秒
			ConnManagerParams.setTimeout(mParams, 2 * 1000);
			// 设置服务器响应超时
			HttpConnectionParams.setConnectionTimeout(mParams, 10 * 1000);
			// 设置数据请求超时
			HttpConnectionParams.setSoTimeout(mParams, 6* 1000);

			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));

			conMgr = new ThreadSafeClientConnManager(mParams, schReg);
			customerHttpClient = new DefaultHttpClient(conMgr, mParams);
		}
		return customerHttpClient;

	}
}
