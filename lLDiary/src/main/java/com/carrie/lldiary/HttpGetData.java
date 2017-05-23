package com.carrie.lldiary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class HttpGetData extends AsyncTask<String, Void, String> {
	private HttpClient mHttpClient;
	private HttpGet mHttpGet;
	private HttpResponse mHttpResponse;
	private HttpEntity mHttpEntity;
	
	private HttpGetDataListener listener;
	private String url;
	
	
	public HttpGetData(HttpGetDataListener listener, String url) {
		this.listener = listener;
		this.url = url;
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			mHttpClient=new DefaultHttpClient();
			mHttpGet=new HttpGet(url);
			mHttpResponse=mHttpClient.execute(mHttpGet);
			mHttpEntity=mHttpResponse.getEntity();
			InputStream in=mHttpEntity.getContent();
			BufferedReader bReader=new BufferedReader(new InputStreamReader(in));
			String line=null;
			StringBuilder sb=new StringBuilder();
			while((line=bReader.readLine())!=null){
				sb.append(line);
			}
			return sb.toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		listener.getDataUrl(result);
		super.onPostExecute(result);
	}
	
	public interface HttpGetDataListener{
		void getDataUrl(String data);
	}

}
