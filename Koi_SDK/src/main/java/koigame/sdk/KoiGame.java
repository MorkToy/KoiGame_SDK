package koigame.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import koigame.sdk.api.KServiceException;
import koigame.sdk.api.KThread;
import koigame.sdk.api.KWebApiImpl;
import koigame.sdk.bean.pay.KPayEvent;
import koigame.sdk.bean.pay.KPayInfo;
import koigame.sdk.bean.pay.KPayProxy;
import koigame.sdk.bean.user.KLoginEvent;
import koigame.sdk.bean.user.KLoginInfo;
import koigame.sdk.bean.user.KSiteConfig;
import koigame.sdk.bean.user.KUserInfo;
import koigame.sdk.bean.user.KUserSession;
import koigame.sdk.bean.version.KVersionChecker;
import koigame.sdk.game.KGamePrice;
import koigame.sdk.game.KGameProxy;
import koigame.sdk.util.ActivityKeep;
import koigame.sdk.util.AndroidUtils;
import koigame.sdk.util.PreferenceUtils;
import koigame.sdk.util.RUtils;
import koigame.sdk.util.StringUtils;
import koigame.sdk.view.KoiLoginActivity;
import koigame.sdk.view.KoiPayActivity;
import koigame.sdk.view.KoiUserCenterActivity;
import koigame.sdk.view.dialog.KoiAutoLoginDialog;


public class KoiGame {

	public static final String TAG = "KoiGame";

	public static final int SCREEN_ORIENTATION_LANDSCAPE = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

	public static final int SCREEN_ORIENTATION_PORTRAIT = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

	public static boolean inited = false;

	public static KLoginInfo loginInfo;

	public static int screenOrientation = SCREEN_ORIENTATION_PORTRAIT;

	public static KoiCallback.LogoutCallback logoutCallback;

	public static void init(Context context) {
		PreferenceUtils.instance(context);
		KMetaData.init(context);
		KUserSession.instance().init(context);
		RUtils.init(context.getPackageName() + ".R");
		KConstant.URL = "http://" + KConstant.PLATFORM_HOST + ":"
				+ KConstant.PLATFORM_PORT+"/platform";
		KConstant.PAY_URL = "http://" + KConstant.PLATFORM_HOST + ":"
				+ KConstant.PAY_PORT;
		Log.i(TAG, "KoiGame init " + KConstant.URL + ", gameCode = " + KMetaData.GameCode);
		inited = true;
	}

	/**
	 * 初始化.
	 */
	public static void init(Context context, int screenOrientation) {
		KoiGame.screenOrientation = screenOrientation;
		ActivityKeep.getInstance().setMainActivity((Activity) context);
		init(context);
	}

	/**
	 * 登陆
	 * 
	 * @param activity
	 *            当前activity
	 *            登陆类型，默认KOI账号登陆
	 */
	public static void login(final Activity activity,
			final KoiCallback.LoginCallback callback) {
		if (!inited) {
			init(activity, SCREEN_ORIENTATION_PORTRAIT);
		}

		// doUpdate(activity);
		// 添加登陆监听
		KLoginEvent.clear();
		KLoginEvent.add(new KLoginEvent.LoginListener() {

			@Override
			public void onSuccessed(KLoginInfo loginInfo) {
				KoiGame.loginInfo = loginInfo;

				// 检查apk更新
				KVersionChecker.instance().checkVersion(activity,
						loginInfo.versionInfo,
						new KVersionChecker.CheckVersionCallback() {

							@Override
							public void onEnterGame() {
								// TODO Auto-generated method stub

								new Thread() { // 获取余额,获取商品配方
									@Override
									public void run() {
										//KPayProxy.instance().loadBalance();
										
										 //KGameProxy.getInstance().loadPackages();
									}
								}.start();

								Long uid = KUserSession.instance()
										.getUserInfo().getUserId();
								String accessToken = KUserSession.instance()
										.getUserInfo().getAccessToken();

								callback.onSuccess(uid.toString(), accessToken);
							}

							@Override
							public void onUpdate() {
								// TODO Auto-generated method stub
								callback.onCancle();
							}

							@Override
							public void onCancleForceUpdate() {
								// TODO Auto-generated method stub
								callback.onCancle();
							}

						});
			}

			@Override
			public void onCancle() {
				callback.onCancle();
			}

			@Override
			public void onError(Exception e) {
				callback.onError(e);
			}

		});

		// 先读取token
		KUserInfo userInfo = KUserSession.instance().buildCachedUserInfo();
		final String accessToken = userInfo.getAccessToken();
		final String accountName = userInfo.getAccountName();
		final String refreshToken = userInfo.getRefreshToken();
		Log.i("infos", "二次登陆 token = " + accessToken +", userId= " + accountName);
		if (!StringUtils.isEmpty(accessToken)) {//!StringUtils.isEmpty(accessToken)

			KThread loginThread = new KThread() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						AndroidUtils.showAutologinProgress(activity);

						SystemClock.sleep(3000);

						KoiAutoLoginDialog koiAutoLoginDialog = AndroidUtils.closeAutologinPorgress(activity);

						if (koiAutoLoginDialog.getProgressStatus() == 1) {
							return;
						}

						AndroidUtils.showCicleProgress(activity, activity.getResources().getString(RUtils.getStringId("koi_login_loading_tip")));

						JSONObject props = KWebApiImpl.instance()
								.loginWithToken(accountName, accessToken, refreshToken);

						AndroidUtils.closeCiclePorgress(activity);

						if (isDestory()) { // 已被用户停止
							return;
						}

						final KUserInfo userInfo = new KUserInfo(props);

						KUserSession.instance().setUserInfo(userInfo);
						KUserSession.instance().saveToPreference(userInfo);
						KSiteConfig.build(props);

						KLoginEvent.onLoginSuccess(new KLoginInfo(props));
					} catch (Exception e) { // 登陆失败，打开登陆页面
						e.printStackTrace();
						AndroidUtils.closeProgress(activity);
						if (!isDestory()) {
							// 开启登陆界面
							Intent intent = new Intent(activity,
									KoiLoginActivity.class);
							activity.startActivity(intent);
						}
					}
				}

				@Override
				public void destroy() {
					Intent intent = new Intent(activity,
							KoiLoginActivity.class);
					activity.startActivity(intent);
					super.destroy();
				}

			};
			loginThread.start();
		} else {
			// 开启登陆界面
			Intent intent = new Intent(activity, KoiLoginActivity.class);
			activity.startActivity(intent);
		}

	}

	/**
	 * 支付.
	 */
	public static void pay(final Activity activity, final KPayInfo payInfo,
			final KoiCallback.PayCallback callback,
			final boolean useHilinkPay) {
		KPayEvent.setPayListener(new KPayEvent.PayListener() {

			@Override
			public void onSuccessed(String orderNO, String cpOrderNO) {
				callback.onSuccess(orderNO, cpOrderNO);
			}

			@Override
			public void onCancle() {
				callback.onCancle();
			}

			@Override
			public void onError(Exception e) {
				callback.onError(e);
			}

			@Override
			public void onPayBegin(KPayInfo payInfo, Activity activity,
								   boolean shouldFinishAfterPay) {
				
				if (useHilinkPay) {
					AndroidUtils.showToast(activity, "not support", 1);
				} else {
					callback.onPayBegin(payInfo, activity, shouldFinishAfterPay);
				}

			}
		});

		String productId = payInfo.getGoodsCode();
		KGamePrice gamePrice = KGameProxy.getGamePriceMap().get(productId);
		if (gamePrice == null) {
			KPayEvent.onPayError(new Exception("productId wrong"));
			return;
		}

		String productName = gamePrice.getGoodsName();
		int unitPrice = gamePrice.getGoodsAmount();
		double discount = gamePrice.getDiscount();
		String roleId= KUserSession.instance().getUserInfo().getRoleId();
		int areaId= KUserSession.instance().getUserInfo().getAreaCode();
		if(roleId==null||roleId==""||areaId==0){
			KUserSession.instance().buildLocalUserInfo();
		}
		payInfo.setRelatedGoodsId(gamePrice.getRelatedGoodsId());
		payInfo.setGoodsName(productName);
		payInfo.setPayAmount(unitPrice);
		payInfo.setOrderDesc(gamePrice.getGoodsDesc());
		payInfo.setGoodsCount(gamePrice.getGoodsCount());
		payInfo.setPayType(1);

		// 如果是固定充值且没有余额功能，直接进入第三方支付
		if (useHilinkPay) {
			KThread progressThread = new KThread() {

				@Override
				public void run() {
					try {
						AndroidUtils.showProgress(
								activity,
								"",
								activity.getResources().getText(
										R.string.hl_bankunion_request), false,
								true, this);

						KPayInfo info = KPayProxy.instance()
								.getChargeOrderForPay(payInfo);
						AndroidUtils.closeProgress(activity);
						if (isDestory()) {
							return;
						}

						KPayEvent.onPayBegin(info, activity, false);
					} catch (KServiceException e) {
						e.printStackTrace();
						AndroidUtils.closeProgress(activity);
						if (isDestory()) {
							return;
						}
						String text = StringUtils.isEmpty(e.getName()) ? activity
								.getResources().getString(R.string.hl_error)
								: e.getName();
						AndroidUtils.showToast(activity, text,
								Toast.LENGTH_LONG);
					}
				}
			};
			progressThread.start();
		 } else {
			Intent intent = new Intent(activity, KoiPayActivity.class);
			intent.putExtra("payInfo", payInfo);
			activity.startActivity(intent);
		}
	}

	/**
	 * 用户中心
	 * 
	 * @param activity
	 *            当前activity
	 */
	public static void memberCenter(final Activity activity) {
		Intent intent = new Intent(activity, KoiUserCenterActivity.class);
		activity.startActivity(intent);
	}

	/**
	 * 礼包领取.
	 * 
	 * @param activity
	 */
	public static void promoteCode(final Activity activity) {
		Intent intent = new Intent(activity, KoiUserCenterActivity.class);
		activity.startActivity(intent);
	}
	
	/**
	 * 激活码激活.
	 * 
	 * @param activity
	 */
	public static void promoteActiveCode(final Activity activity) {
		/*Intent intent = new Intent(activity, HilinkPromoteCodeActivity.class);
		intent.putExtra("codeType", "activation");
		activity.startActivity(intent);*/
	}

	/**
	 * 登出
	 */
	public static void logout() {
		KUserSession.instance().cleanPreference();

	}

	/**
	 * 退出的时候通知服务器
	 */
	public static void logoutForServer() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					KWebApiImpl.instance().exitGame(
							KUserSession.instance().getSdkAccessToken());
				} catch (KServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 游戏停止时通知服务器
	 */
	public static void onStopForServer() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					KWebApiImpl.instance().onStopGame(KMetaData.GameCode,
							KUserSession.instance().getSdkAccessToken());
				} catch (KServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 玩家返回游戏时通知服务器
	 */
	public static void onResumeForServer() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					KWebApiImpl.instance().onResumeGame(KMetaData.GameCode,
							KUserSession.instance().getSdkAccessToken());
				} catch (KServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 设置注销回调
	 * 
	 * @param logoutCallback
	 */
	public static void setLogoutCallback(
			KoiCallback.LogoutCallback logoutCallback) {
		KoiGame.logoutCallback = logoutCallback;
	}

	/**
	 * 设置方向
	 * 
	 * @param screenOrientation
	 */
	public static void setScreenOrientation(int screenOrientation) {
		KoiGame.screenOrientation = screenOrientation;
	}

}
