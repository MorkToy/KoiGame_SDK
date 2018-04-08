package koigame.sdk.bean.pay;

import android.app.Activity;

public class KPayEvent {

	private static PayListener mPayListener;

	public static void setPayListener(PayListener payListener) {
		mPayListener = payListener;
	}

	/**
	 * 支付成功
	 * 
	 * @param loginInfo
	 */
	public static void onPaySuccess(String orderNO, String number) {
		mPayListener.onSuccessed(orderNO, number);

	}

	public static void onPayCancle() {
		mPayListener.onCancle();
	}

	/**
	 * 
	 * @param props
	 */
	public static void onPayError(Exception e) {
		mPayListener.onError(e);
	}

	/**
	 * 是否不使用哈邻平台支付
	 * 
	 * @param buyInfo
	 * @param activity
	 * @param shouldFinishAfterPay
	 */
	public static void onPayBegin(KPayInfo buyInfo, Activity activity, boolean shouldFinishAfterPay) {
		mPayListener.onPayBegin(buyInfo, activity, shouldFinishAfterPay);
	}

	public interface PayListener {

		void onSuccessed(String orderNO, String number);

		void onCancle();

		void onError(Exception e);

		void onPayBegin(KPayInfo buyInfo, Activity activity, boolean shouldFinishAfterPay);
	}

}
