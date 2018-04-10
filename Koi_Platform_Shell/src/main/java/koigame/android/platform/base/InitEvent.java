package koigame.android.platform.base;

import java.util.LinkedList;

public class InitEvent {

	private static LinkedList<InitListener> mInitListeners = new LinkedList<InitListener>();

	public static void add(InitListener listener) {
		if (!mInitListeners.contains(listener)) {
			mInitListeners.add(listener);
		}
	}

	public static void remove(InitListener listener) {
		mInitListeners.remove(listener);
	}

	public static void clear() {
		mInitListeners.clear();
	}

	public static void onInitSuccess() {
		for (InitListener listener : mInitListeners) {
			listener.onSuccessed();
		}

	}

	public static void onInitFail() {
		for (InitListener listener : mInitListeners) {
			listener.onFailed();
		}
	}

	public interface InitListener {
		void onSuccessed();

		void onFailed();
	}

}
