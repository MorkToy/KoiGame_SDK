package koigame.sdk.util;

import android.os.Handler;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.List;

public class HHttpConnection {
	private static int TRY_TIMES = 3;

	public static enum HttpMethod {
		GET, POST
	}

	public static void asyncConnect(final String url, final HttpMethod method, final Callback callback) {
		asyncConnect(url, null, method, callback);
	}

	public static void syncConnect(final String url, final HttpMethod method, final Callback callback) {
		syncConnect(url, null, method, callback);
	}

	public static void asyncConnect(final String url, final List<NameValuePair> listParams, final HttpMethod method,
			final Callback callback) {
		Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			public void run() {
				syncConnect(url, listParams, method, callback);
			}
		};
		handler.post(runnable);
	}

	public static void syncConnect(final String url, final List<NameValuePair> listParams, final HttpMethod method,
			final Callback callback) {
		String json = null;
		BufferedReader reader = null;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpUriRequest request = getRequest(url, listParams, method);
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Log.i("request", "status=200");
				reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuilder sb = new StringBuilder();
				for (String s = reader.readLine(); s != null; s = reader.readLine()) {
					sb.append(s);
				}
				json = sb.toString();
			}
		} catch (ClientProtocolException e) {
			Log.e("HttpConnection", e.getMessage(), e);
		} catch (IOException e) {
			Log.e("HttpConnection", e.getMessage(), e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				// ignore me
			}
		}
		callback.execute(json);
	}

	public static String syncConnect(final String url, final List<NameValuePair> listParams)
			throws UnknownHostException, IOException {
		Log.i("myinfos", "提交的参数"+listParams.toString());
		return syncConnect(url, listParams, HttpMethod.POST);
	}

	public static String syncConnect(final String url, final List<NameValuePair> listParams, final HttpMethod method)
			throws UnknownHostException, IOException {
		String json = null;
		BufferedReader reader = null;

		for (int i = 0; i < TRY_TIMES; i++) {
			boolean success = true;
			try {
				HttpClient client = new DefaultHttpClient();
				HttpUriRequest request = getRequest(url, listParams, method);
				HttpResponse response = client.execute(request);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					StringBuilder sb = new StringBuilder();
					for (String s = reader.readLine(); s != null; s = reader.readLine()) {
						sb.append(s);
					}
					json = sb.toString();
				}
			} catch (ClientProtocolException e) {
				success = false;
				Log.e("HttpConnection", e.getMessage(), e);
				if (i >= (TRY_TIMES - 1)) {
					throw e;
				}
			} catch (UnknownHostException e) {
				success = false;
				Log.e("HttpConnection", e.getMessage(), e);
				if (i >= (TRY_TIMES - 1)) {
					throw e;
				}
			} catch (IOException e) {
				success = false;
				Log.e("HttpConnection", e.getMessage(), e);
				if (i >= (TRY_TIMES - 1)) {
					throw e;
				}
			} catch (Throwable t) {
				success = false;
				Log.e("HttpConnection", t.getMessage(), t);
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
					// ignore me
				}
			}
			if (success) {
				break;
			} else {
				try {
					Thread.sleep(10 * 1000);
				} catch (InterruptedException e) {
				}
			}
		}
		return json;
	}

	private static HttpUriRequest getRequest(String url, List<NameValuePair> listParams, HttpMethod method) {
		if (method.equals(HttpMethod.POST)) {
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(listParams,HTTP.UTF_8);
				HttpPost request = new HttpPost(url);
				request.setEntity(entity);
				return request;
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		} else {
			if (url.indexOf("?") < 0) {
				url += "?";
			}
			if (listParams != null) {

				for (NameValuePair nv : listParams) {
					url += "&" + nv.getName() + "=" + URLEncoder.encode(nv.getValue());
				}
			}
			HttpGet request = new HttpGet(url);
			return request;
		}
	}

	public interface Callback {
		/**
		 * Call back method will be execute after the http request return.
		 * 
		 * @param response
		 *            the response of http request. The value will be null if
		 *            any error occur.
		 */
		void execute(String response);
	}
}