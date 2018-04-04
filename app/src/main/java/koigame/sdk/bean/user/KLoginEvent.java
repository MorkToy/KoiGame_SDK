package koigame.sdk.bean.user;

import java.util.LinkedList;

public class KLoginEvent {
	private static LinkedList<LoginListener> mLoginListeners = new LinkedList<LoginListener>();

	public static void add(LoginListener listener) {
		if (!mLoginListeners.contains(listener)) {
			mLoginListeners.add(listener);
		}
	}

	public static void remove(LoginListener listener) {
		mLoginListeners.remove(listener);
	}

	public static void clear() {
		mLoginListeners.clear();
	}

	/**
	 * 登陆成功
	 *
	 * @param loginInfo
	 */
	public static void onLoginSuccess(KLoginInfo loginInfo) {
		for (LoginListener listener : mLoginListeners) {
			listener.onSuccessed(loginInfo);
		}

	}

	public static void onLoginCancle() {
		for (LoginListener listener : mLoginListeners) {
			listener.onCancle();
		}
	}

	public static void onLoginError(Exception e) {
		for (LoginListener listener : mLoginListeners) {
			listener.onError(e);
		}
	}

	public interface LoginListener {
		void onSuccessed(KLoginInfo loginInfo);

		void onCancle();

		void onError(Exception e);
	}

}