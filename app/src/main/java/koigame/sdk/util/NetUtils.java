package koigame.sdk.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 网络工具
 * 
 * @author Mike
 * 
 */
public class NetUtils {

	private static NetUtils instance = new NetUtils();

	private NetUtils() {

	}

	public static NetUtils instance() {
		return instance;
	}

	/**
	 * 是否有网络连接
	 * 
	 * @param activity
	 * @return
	 */
	public boolean isOpenNetwork(Context ctx) {
		ConnectivityManager connManager = (ConnectivityManager) ctx.getSystemService(ctx.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}

		return false;
	}

	/**
	 * 是否连接了wifi
	 * 
	 * @param ctx
	 * @return
	 */
	public boolean isWifiConnected(Context ctx) {
		ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(ctx.CONNECTIVITY_SERVICE);

		NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
		if (activeInfo != null && activeInfo.isConnected() && activeInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;

		}
		return false;
	}

	/**
	 * 获取Android本机IP地址
	 * 
	 * @return
	 */
	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return "";
	}

}
