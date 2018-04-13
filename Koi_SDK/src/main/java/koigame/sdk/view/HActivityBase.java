package koigame.sdk.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

public abstract class HActivityBase extends Activity {

	private String TAG = "HActivityBase";

	protected final static int SUCCESS = 1;
	protected final static int FAIL = 0;
	protected static final int SENDSMSSUCCESS = 15;
	protected static final int SENDSMSFAIL = 16;

	protected Map<String, LinearLayout> layoutIndex;


	//隐藏和显示切换动画
	protected TranslateAnimation mShowAction;
	protected TranslateAnimation mHiddenAction;


	protected LinearLayout currentIndexLy;
	protected LinearLayout lastIndexLy;

	protected int pageIndex = 1;

	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		initDatas();
		initAnim();

	}

	public abstract void initView();

	public abstract void initEvent();

	public void initDatas() {
		Log.i(TAG, "initDatas");
		layoutIndex = new HashMap<String, LinearLayout>();
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		Log.i(TAG, "setContentView");
	}

	/**
	 * 初始化动画.
	 */
	public void initAnim() {
		Log.i(TAG, "initAnim");
		mShowAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -5.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(500);

		mHiddenAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				5.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mHiddenAction.setDuration(500);
	}


}
