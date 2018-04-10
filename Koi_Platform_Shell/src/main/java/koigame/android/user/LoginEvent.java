package koigame.android.user;

import org.json.JSONObject;

import java.util.LinkedList;
import koigame.sdk.bean.user.KLoginInfo;

public class LoginEvent {
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

	public static void onLoginSuccess(KLoginInfo loginInfo) {
		for (LoginListener listener : mLoginListeners) {
			listener.onSuccessed(loginInfo);
		}

	}

	public static void onLoginFail(JSONObject props) {
		for (LoginListener listener : mLoginListeners) {
			listener.onFailed(props);
		}
	}

	public static void onLoginCancle() {
		for (LoginListener listener : mLoginListeners) {
			listener.onCancle();
		}
	}

	public interface LoginListener {
		void onSuccessed(KLoginInfo loginInfo);

		void onFailed(JSONObject props);

		void onCancle();
	}

}