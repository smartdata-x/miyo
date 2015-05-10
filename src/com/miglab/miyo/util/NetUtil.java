package com.miglab.miyo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;


/**
 * @author song
 * @version 创建时间：2015-3-31 下午8:04:27
 * 类说明
 */
public class NetUtil {
	public static int netState = -1; // -1:默认值 0:未联网 1:移动网络2G/3G 2:WIFI

	/**
	 * 判断是否联网
	 * 
	 * @param cont
	 * @return
	 */
	public static boolean isNetConnection(final Context cont) {
		ConnectivityManager manager = (ConnectivityManager) cont
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;
	}

	/**
	 * wifi检测
	 * 
	 * @return 无网络或者2G/3G 返回false; wifi返回true
	 */
	public static boolean isWiFi(final Context con) {
		ConnectivityManager manager = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		} else {
			State state = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.getState();
			if (state.toString().equals("CONNECTED"))// if(wifi ==
														// State.CONNECTED)
				return true;
			else
				return false;
		}

	}

	/**
	 * 获得网络连接类型状态
	 * 
	 * @param context
	 */
	public static void getNetType(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// State state = connManager.getActiveNetworkInfo().getState();
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState(); // 获取网络连接状态
		if (State.CONNECTED == state) { // 判断是否正在使用WIFI网络
			NetUtil.netState = 2;
			// MyLog.v("网络是WIFI");
			return;
		}

		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState(); // 获取网络连接状态
		if (State.CONNECTED == state) { // 判断是否正在使用GPRS网络
			NetUtil.netState = 1;
			// MyLog.v("网络是2G/3G");
			return;
		}

		NetUtil.netState = 0;
		// MyLog.e("网络未连接！");
	}

	/**
	 * Clear current context cookies .
	 * 
	 * @param context
	 *            : current activity context.
	 * 
	 * @return void
	 */
	public static void clearCookies(Context context) {
		@SuppressWarnings("unused")
		CookieSyncManager cookieSyncMngr = CookieSyncManager
				.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}
}

