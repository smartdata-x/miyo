package com.miglab.miyo.net;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Handler;
import com.miglab.miyo.util.ToolUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.List;

/**
 * @author ZhaoLongQuan
 * @version 创建时间：2015-1-28 上午11:11:43
 * 类说明	批量下载图片并通知adapter更新
 */
public class ImagesDownloadTask extends AsyncTask<Void, Void, Object>{

	private final static String TAG = "ImagesDownloadTask";
	private Handler handler;
	private int count = 0;
	private List<String> images;
	private String path;
	private int msgWhat;
	private boolean sleep;
	private int every = 1;

	public ImagesDownloadTask(Handler handler, List<String> list, String path,
							  int msgWhat, boolean needSleep) {
		this.handler = handler;
		images = list;
		this.path = path;
		this.msgWhat = msgWhat;
		sleep = needSleep;
	}

	public ImagesDownloadTask(Handler handler, List<String> list, String path,
							  int msgWhat, boolean needSleep, int space) {
		this.handler = handler;
		images = list;
		this.path = path;
		this.msgWhat = msgWhat;
		sleep = needSleep;
		if (space > 0)
			every = space;
	}

	@Override
	protected Object doInBackground(Void... params) {
		// TODO Auto-generated method stub
		if (images == null || images.size() <= 0)
			return null;

		try {
			// "开始下载图片:"+MyLog.showStringArr(images));
			for (String url : images) { // 只遍历一次
				try {
					if (url == null || url.length() <= 0)
						continue;

					String imagepath = path + ToolUtil.md5(url);
					File f = new File(imagepath);
					if (f.exists()) {
						continue;
					}

					downloadImage(url, f);
					if (f.exists() && f.length() > 100) {
						//保存图片
					} else {
						//下载失败
					}
					
					if (count != 0 && count % every == 0)
						handler.sendEmptyMessage(msgWhat);

				} catch (Exception e) {
					e.printStackTrace();

				} catch (OutOfMemoryError error) {
					error.printStackTrace();

				} finally {
					count++;
				}
			}

			handler.sendEmptyMessage(msgWhat);

		} catch (Exception e) {
			// TODO: handle exception
			// MyLog.e(TAG, "图片list有问题.");
		}
		return null;
	}

	private void downloadImage(String url, File f) throws Exception {
		HttpClient client = AndroidHttpClient.newInstance("Android");
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		HttpConnectionParams.setSocketBufferSize(params, 3000);
		HttpGet httpGet = null;
		InputStream inputStream = null;
		FileOutputStream outStream = null;
		try {
			httpGet = new HttpGet(url);
			HttpResponse response = client.execute(httpGet);
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode != HttpStatus.SC_OK) {
				// MyLog.d(TAG, "func [loadImage] stateCode=" + stateCode);
				return;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				try {
					inputStream = entity.getContent();
					outStream = new FileOutputStream(f);
					int len = -1;
					byte[] b = new byte[4096];
					while ((len = inputStream.read(b)) != -1) {
						outStream.write(b, 0, len);
					}
					outStream.close();
					inputStream.close();
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					if (outStream != null) {
						outStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (ClientProtocolException e) {
			httpGet.abort();
			e.printStackTrace();
		} catch (IOException e) {
			httpGet.abort();
			e.printStackTrace();
		} finally {
			((AndroidHttpClient) client).close();
		}
		return;

	}
}
