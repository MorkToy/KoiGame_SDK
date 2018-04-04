package koigame.sdk;

import android.app.Activity;

import koigame.sdk.bean.pay.KPayInfo;

public class KoiCallback {

	/**
	 * 登陆回调
	 * 
	 * @author Mike
	 * 
	 */
	public interface LoginCallback {

		public static final String TAG = "HilinkLoginCallback";

		/**
		 * 登陆成功
		 * 
		 * @param uid
		 *            用户id
		 * @param accessToken
		 *            用户令牌
		 */
		public void onSuccess(String uid, String accessToken);

		/**
		 * 取消登陆
		 */
		public void onCancle();

		/**
		 * 报错
		 * 
		 * @param e
		 */
		public void onError(Exception e);

	}

	/**
	 * 支付回调
	 * 
	 * @author Mike
	 * 
	 */
	public interface PayCallback {

		public static final String TAG = "HilinkPayCallback";

		/**
		 * 支付成功
		 * 
		 * @param orderNO
		 *            哈邻订单号
		 * @param cpOrderNO
		 *            cp订单号
		 */
		public void onSuccess(String orderNO, String cpOrderNO);

		/**
		 * 取消支付
		 */
		public void onCancle();

		/**
		 * 报错
		 * 
		 * @param e
		 */
		public void onError(Exception e);

		/**
		 * 开始支付
		 * 
		 * @param payInfo
		 * @param activity
		 * @param shouldFinishAfterPay
		 */
		public void onPayBegin(KPayInfo payInfo, Activity activity, boolean shouldFinishAfterPay);
	}

	/**
	 * 注销回调
	 * 
	 * @author Mike
	 * 
	 */
	public interface LogoutCallback {

		public void onLogout(Activity activity);

	}

}
