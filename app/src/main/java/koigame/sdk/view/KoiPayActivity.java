package koigame.sdk.view;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import koigame.sdk.util.RUtils;

public class KoiPayActivity extends Activity {

	public final String TAG = "koi_login";
	
	private TranslateAnimation mShowAction;
	private TranslateAnimation mHiddenAction;
	
	private Map<String, LinearLayout> layoutIndex;
	private LinearLayout currentIndexLy;
	private LinearLayout lastIndexLy;
	
	private int pageIndex = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(RUtils.getLayoutId("koi_payment"));
		initDatas();
		initView();
		initAnim();
	}
	
	
	public void initDatas() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
		LayoutParams p = getWindow().getAttributes();
		p.height = (int) (dm.heightPixels * 0.45);
		p.width = (int) (dm.widthPixels * 0.8);
		
		
		layoutIndex = new HashMap<String, LinearLayout>();
	}
	
	public void initView() {
		
	}
	
	
	public void doAlipay(View view) {
		
	}
	
	public void doWechatPay(View view) {
		
	}
	
	
	/**
	 * 切换动画.
	 * @param current
	 * @param next
	 */
	public void switchAnim(LinearLayout current, LinearLayout next) {
		current.startAnimation(mHiddenAction);
		current.setVisibility(View.GONE);
		
		next.startAnimation(mShowAction);
		next.setVisibility(View.VISIBLE);
		
		recordSwitch(next);
		Log.i(TAG, "当前页面为 " + pageIndex);
	}
	
	
	/**
	 * 回退动画.
	 * @param view
	 */
	public void comeback(View view) {
		Log.i(TAG, "回退动画 当前页面为 " + pageIndex);
		currentIndexLy = layoutIndex.get("PAGE" + pageIndex);
		lastIndexLy = layoutIndex.get("PAGE" + (pageIndex -1));
		
		currentIndexLy.startAnimation(mHiddenAction);
		currentIndexLy.setVisibility(View.GONE);
		
		lastIndexLy.startAnimation(mShowAction);
		lastIndexLy.setVisibility(View.VISIBLE);
		
		/*if (lastIndexLy == koi_main_layout) {
			koi_tv_comback.setVisibility(View.GONE);
		}*/
		removeRecord();
		Log.i(TAG, "回退后 当前页面为 " + pageIndex);
	}
	
	/**
	 * 记录每次页面跳转，方便回退页面.
	 */
	public void recordSwitch(LinearLayout current) {
		pageIndex++;
		layoutIndex.put("PAGE" + pageIndex, current);
		//koi_tv_comback.setVisibility(View.VISIBLE);
	}
	
	public void removeRecord() {
		layoutIndex.remove("PAGE" + pageIndex);
		pageIndex--;
		
	}
	
	public void initAnim() {
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
