package koigame.sdk.bean.pay;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import koigame.sdk.api.KServiceException;
import koigame.sdk.api.KWebApi;
import koigame.sdk.api.KWebApiImpl;
import koigame.sdk.base.KActivity;
import koigame.sdk.bean.pay.record.KPayItem;
import koigame.sdk.bean.pay.record.KRechargeItem;
import koigame.sdk.bean.user.KUserInfo;
import koigame.sdk.bean.user.KUserSession;
import koigame.sdk.util.JSONUtils;
import koigame.sdk.util.StringUtils;

public class KPayProxy {
	public final static String RETURN_STATUS = "returnStatus";
	public final static String RETURN_STATUS_OK = "0";
	public final static String RETURN_STATUS_ERROR = "-1";
	public final static String ERROR_CODE = "errorCode";
	public final static String ERROR_MSG = "errorMessage";
	protected final int PAY_REQUEST_CODE = 0;
	protected final int LOGIN_REQUEST_CODE = 1;
	protected final int REGIST_REQUEST_CODE = 2;
	protected final int CHARGE_REQUEST_CODE = 3;
	protected final int LOGIN_CHARGE_REQUEST_CODE = 8;

	private static KPayProxy instance = new KPayProxy();

	private KPayProxy() {
	}

	public static KPayProxy instance() {
		return instance;
	}

	private double balance = 0.0;

	private static List<KActivity> payActivites;

	public double getBalance() {
		return balance;
	}

	void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * 加载余额
	 */
	public void loadBalance() {
		new Thread() {

			@Override
			public void run() {
				KUserInfo userInfo = KUserSession.instance().getUserInfo();

				try {
					JSONObject props = KWebApiImpl.instance().getBalance(userInfo.getAccessToken());

					setBalance(JSONUtils.getDouble(props, KWebApi.BALANCE));
				} catch (KServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}.start();
	}

	public List<KActivity> getPayActivities() {
		try {
			if (payActivites != null) {
				return payActivites;
			}
			KUserInfo userInfo = KUserSession.instance().getUserInfo();
			JSONObject props = KWebApiImpl.instance().getPayAcvities(userInfo.getAccessToken());
			JSONArray ja = JSONUtils.getArray(props, "activities");
			payActivites = new ArrayList<KActivity>();
			for (int i = 0; i < ja.length(); i++) {
				String value = JSONUtils.getString(ja, i);
				if (!StringUtils.isEmpty(value)) {
					JSONObject jso = JSONUtils.build(value);
					KActivity activity = new KActivity(jso);
					payActivites.add(activity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			payActivites = new ArrayList<KActivity>();
		}

		return payActivites;
	}

	/**
	 * 请求生成购买和支付订单
	 * 
	 * @param info
	 * @return
	 * @throws KServiceException
	 */
	public KPayInfo getChargeOrderForPay(KPayInfo info) throws KServiceException {
		JSONObject props = KWebApiImpl.instance().getChargeOrderForPay(getUserInfo().getAccessToken(), info);
		JSONObject object = JSONUtils.getJSONObject(props, "data");
		info.setOrderId(JSONUtils.getString(object, KWebApi.ORDERNO));
		String extraData = JSONUtils.getString(object, KWebApi.EXTRADATA);
		if (!TextUtils.isEmpty(extraData)) {
			info.setExtraParams(extraData);
		} else {
			info.setExtraParams("nothing");
		}
		return info;
	}

	/**
	 * 获得充值订单.
	 */
	public KPayInfo getChargeOrder(KPayInfo info) throws KServiceException {
		JSONObject props = KWebApiImpl.instance().getChargeOrder(getUserInfo().getAccessToken(), info);

		info.setOrderId(JSONUtils.getString(props, KWebApi.ORDERNO));
		return info;
	}

	/**
	 * 获得充值记录.
	 */
	List<KRechargeItem> getRechargeList(int year, int month) throws KServiceException {
		JSONObject props = KWebApiImpl.instance().getRechargeList(getUserInfo().getAccessToken(), year, month);
		JSONArray ja = JSONUtils.getArray(props, "rechargeOrders");
		List<KRechargeItem> list = new ArrayList<KRechargeItem>();
		for (int i = 0; i < ja.length(); i++) {
			String value = JSONUtils.getString(ja, i);
			if (!StringUtils.isEmpty(value)) {
				JSONObject jso = JSONUtils.build(value);
				KRechargeItem item = new KRechargeItem(jso);
				list.add(item);
			}
		}
		return list;
	}

	/**
	 * 获得支付记录.
	 */
	List<KPayItem> getPayList(int year, int month) throws KServiceException {
		JSONObject props = KWebApiImpl.instance().getPayList(getUserInfo().getAccessToken(), year, month);
		JSONArray ja = JSONUtils.getArray(props, "payOrders");
		List<KPayItem> list = new ArrayList<KPayItem>();
		for (int i = 0; i < ja.length(); i++) {
			String value = JSONUtils.getString(ja, i);
			if (!StringUtils.isEmpty(value)) {
				JSONObject jso = JSONUtils.build(value);
				KPayItem item = new KPayItem(jso);
				list.add(item);
			}
		}
		return list;
	}

	/**
	 * 余额支付.
	 */
	JSONObject walletPay(KPayInfo payInfo) throws KServiceException {
		JSONObject props = KWebApiImpl.instance().walletPay(getUserInfo().getAccessToken(), payInfo);
		balance = JSONUtils.getFloat(props, KWebApi.BALANCE);
		return props;
	}

	/**
	 * 通知支付成功.
	 */
	JSONObject charge(String orderNO, String numberNO, float amount, Currency currency) throws KServiceException {
		JSONObject props = KWebApiImpl.instance().charge(getUserInfo().getAccessToken(), orderNO, numberNO, amount,
				currency);
		balance = JSONUtils.getFloat(props, KWebApi.BALANCE);
		return props;
	}

	private KUserInfo getUserInfo() {
		return KUserSession.instance().getUserInfo();
	}
}