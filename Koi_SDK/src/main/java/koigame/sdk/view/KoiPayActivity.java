package koigame.sdk.view;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import koigame.sdk.api.KServiceException;
import koigame.sdk.api.KThread;
import koigame.sdk.bean.pay.KPayInfo;
import koigame.sdk.bean.pay.KPayProxy;
import koigame.sdk.bean.user.KUserSession;
import koigame.sdk.util.AndroidUtils;
import koigame.sdk.util.JSONUtils;
import koigame.sdk.util.RUtils;
import koigame.sdk.util.StringUtils;

public class KoiPayActivity extends Activity {

	public final String TAG = "KOI_PAY";
	private final int ALIPAYCALLBACK = 101;
	private final int WECHATCALLBACK = 102;
	
	private TranslateAnimation mShowAction;
	private TranslateAnimation mHiddenAction;
	
	private Map<String, LinearLayout> layoutIndex;
	private LinearLayout currentIndexLy;
	private LinearLayout lastIndexLy;
	private KPayInfo payInfo;
	
	private int pageIndex = 1;
	private IWXAPI msgApi;

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

		payInfo = new KPayInfo();
		payInfo.setGoodsCode("2");
		payInfo.setGoodsName("120gem");
		payInfo.setPayAmount(1);
		payInfo.setGoodsCount(1);
		payInfo.setOrderDesc("120gem");
		payInfo.setPayType(1);
		KUserSession.instance().getUserInfo().setRoleId(58 + "");
		KUserSession.instance().getUserInfo().setUserId(518);
		KUserSession.instance().getUserInfo().setAreaCode(5);
		EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);


		msgApi = WXAPIFactory.createWXAPI(this, null);
		msgApi.registerApp("wx086a76b0fce723f1");

	}
	
	public void initView() {
		
	}
	
	
	public void doAlipay(View view) {
		payInfo.setPayType(1);
		initOrderData(1);
	}
	
	public void doWechatPay(View view) {
		payInfo.setPayType(2);
		initOrderData(2);

	}

	public void initOrderData(final int type) {
		KThread progressThread = new KThread() {

			@Override
			public void run() {
				try {
					AndroidUtils.showCicleProgress(KoiPayActivity.this, KoiPayActivity.this.getResources().getString(RUtils.getStringId("hl_bankunion_request")));
					payInfo = KPayProxy.instance()
							.getChargeOrderForPay(payInfo);
					Log.i(TAG, "alipayorder : " + payInfo.getExtraParams());

					AndroidUtils.closeCiclePorgress(KoiPayActivity.this);
					if (isDestory()) {
						return;
					}
					if (type == 1) {
						aliPay();
					} else {
						wechatPay();
					}

				} catch (KServiceException e) {
					e.printStackTrace();
					AndroidUtils.closeCiclePorgress(KoiPayActivity.this);
					if (isDestory()) {
						return;
					}
					String text = StringUtils.isEmpty(e.getName()) ? KoiPayActivity.this
							.getResources().getString(RUtils.getStringId("hl_error"))
							: e.getName();
					AndroidUtils.showToast(KoiPayActivity.this, text,
							Toast.LENGTH_LONG);
				}
			}
		};
		progressThread.start();
	}

	public void aliPay() {
		//final String orderInfo = info;   // 订单信息

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(KoiPayActivity.this);
				Map<String, String> result = alipay.payV2(payInfo.getExtraParams(), true);

				Message msg = new Message();
				msg.what = ALIPAYCALLBACK;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	public void wechatPay() {
		Log.i(TAG, "wechatPay :" + payInfo.getExtraParams());
		JSONObject payJson = JSONUtils.build(payInfo.getExtraParams());

		final PayReq request = new PayReq();
		request.appId = JSONUtils.getString(payJson, "appid");
		request.partnerId = JSONUtils.getString(payJson, "partnerid");
		request.prepayId = JSONUtils.getString(payJson, "prepayid");
		request.packageValue = JSONUtils.getString(payJson, "package");
		request.nonceStr = JSONUtils.getString(payJson, "noncestr");

		request.timeStamp = JSONUtils.getString(payJson, "timestamp");
		request.sign = JSONUtils.getString(payJson, "sign");
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				msgApi.sendReq(request);
			}
		});

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

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			AndroidUtils.closeCiclePorgress(KoiPayActivity.this);
			try {

				switch (msg.what) {
					case ALIPAYCALLBACK: {
						Log.e("HilinkPayView", msg.toString());
						break;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
}
