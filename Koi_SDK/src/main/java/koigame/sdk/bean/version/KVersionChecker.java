package koigame.sdk.bean.version;

import android.app.Activity;

import koigame.sdk.util.RUtils;
import koigame.sdk.view.HApkUpdateView;


public class KVersionChecker {
	private static final String TAG = "VersionChecker";
	private static final KVersionChecker instance = new KVersionChecker();

	private KVersionChecker() {
	}

	public static KVersionChecker instance() {
		return instance;
	}

	/**
	 * 检查apk版本.
	 */
	public void checkVersion(final Activity activity, final KVersionInfo versionInfo,
			final CheckVersionCallback checkCallback) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				HApkUpdateView mApkUpdateView = new HApkUpdateView(activity, RUtils.getLayoutId("hl_update"));
				mApkUpdateView.checkVersion(activity, versionInfo, checkCallback);
			}
		});
	}

	/**
	 * 检查回调
	 * 
	 * @author Mike
	 * 
	 */
	public interface CheckVersionCallback {
		/**
		 * 无更新回调
		 */
		public void onEnterGame();

		/**
		 * 更新时回调
		 */
		public void onUpdate();

		/**
		 * 取消强制更新
		 */
		public void onCancleForceUpdate();
	}

}