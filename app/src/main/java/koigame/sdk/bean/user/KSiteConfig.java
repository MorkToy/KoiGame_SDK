package koigame.sdk.bean.user;

import org.json.JSONObject;

import koigame.sdk.util.JSONUtils;
import koigame.sdk.util.StringUtils;


public class KSiteConfig {

	// 是否显示注册账号
	private static boolean showBindAccount = false;

	// 是否显示修改密码
	private static boolean showModifyPwd = false;
	private static boolean registerAuth = true;
	private static boolean supportAliPay = true;
	private static boolean supportShenZhouPay = true;
	private static boolean supportYeepayGameCard = true;

	private static boolean suppotHidouRecharge = true;

	private static boolean supportBalance = true;
	private static boolean supportWebPay = true;

	public static void build(JSONObject props) {
		build(JSONUtils.getString(props, "siteConfig"));
	}

	//
	public static void build(String value) {
		if (StringUtils.isEmpty(value)) {
			return;
		}

		int index = 1;
		for (char ch : value.toCharArray()) {
			switch (index) {
			case 1:
				setRegisterAuth('1' == ch);

				break;
			case 2:
				setSupportAliPay('1' == ch);

				break;
			case 3:
				setSupportShenZhouPay('1' == ch);

				break;
			case 4:
				setSupportYeepayGameCard('1' == ch);
				break;
			case 5:
				setShowBindAccount('1' == ch);
				break;
			case 6:
				setShowModifyPwd('1' == ch);
				break;
			case 7:
				setSuppotHidouRecharge('1' == ch);
				break;
			case 8:
				setSupportBalance('1' == ch);
				break;
			case 9:
				setSupportWebPay('1' == ch);
			default:
				//
			}
			index++;
		}
	}

	public boolean isRegisterAuth() {
		return registerAuth;
	}

	public static void setRegisterAuth(boolean registerAuth) {
		KSiteConfig.registerAuth = registerAuth;
	}

	public static boolean isSupportAliPay() {
		return supportAliPay;
	}

	public static void setSupportAliPay(boolean supportAliPay) {
		KSiteConfig.supportAliPay = supportAliPay;
	}

	public static boolean isSupportShenZhouPay() {
		return supportShenZhouPay;
	}

	public static void setSupportShenZhouPay(boolean supportShenZhouPay) {
		KSiteConfig.supportShenZhouPay = supportShenZhouPay;
	}

	public static boolean isSupportYeepayGameCard() {
		return supportYeepayGameCard;
	}

	public static void setSupportYeepayGameCard(boolean supportYeepayGameCard) {
		KSiteConfig.supportYeepayGameCard = supportYeepayGameCard;
	}

	public static boolean isShowBindAccount() {
		return showBindAccount;
	}

	public static void setShowBindAccount(boolean showBindAccount) {
		KSiteConfig.showBindAccount = showBindAccount;
	}

	public static boolean isShowModifyPwd() {
		return showModifyPwd;
	}

	public static void setShowModifyPwd(boolean showModifyPwd) {
		KSiteConfig.showModifyPwd = showModifyPwd;
	}

	public static boolean isSuppotHidouRecharge() {
		return suppotHidouRecharge;
	}

	public static void setSuppotHidouRecharge(boolean suppotHidouRecharge) {
		KSiteConfig.suppotHidouRecharge = suppotHidouRecharge;
	}

	public static boolean isSupportBalance() {
		return supportBalance;
	}

	public static void setSupportBalance(boolean supportBalance) {
		KSiteConfig.supportBalance = supportBalance;
	}

	public static boolean isSupportWebPay() {
		return supportWebPay;
	}

	public static void setSupportWebPay(boolean supportWebPay) {
		KSiteConfig.supportWebPay = supportWebPay;
	}
	

}
