package com.hust.grid.leesf.thread;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.hust.grid.leesf.bean.IpInfo;

public class VoteThread extends Thread {
	private BlockingQueue<IpInfo> ipInfoQueue;
	
	public VoteThread(BlockingQueue<IpInfo> ipInfoQueue) {
		this.ipInfoQueue = ipInfoQueue;
	}
	
	@Override
	public void run() {
		HttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 10000);
		HttpConnectionParams.setSoTimeout(params, 15000);
		HttpResponse response = null;
		HttpGet get = null;
		HttpEntity entity = null;
		HttpHost proxy = null;
		while (true) {
			IpInfo ipInfo = null;
			try {
				ipInfo = ipInfoQueue.take();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			proxy = new HttpHost(ipInfo.getIpAddress(),
					ipInfo.getPort());
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
			get = new HttpGet(
					"http://www.hnxdf.com/vote/iRadio_vote.asp?VoTeid=215");
			get.addHeader("Host", "www.hnxdf.com");
			get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			try {
				response = client.execute(get);
				entity = response.getEntity();
				byte[] bytes = EntityUtils.toByteArray(entity);
				String temp = new String(bytes, "gbk");
				byte[] contentData = temp.getBytes("utf-8");
				System.out.println(new String(contentData));
				System.out.println("-----------------------------------");
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
