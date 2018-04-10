package koigame.android.platform.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import koigame.android.shell.MetaData;
import koigame.android.shell.ShellActivity;
import koigame.sdk.util.StringUtils;


public class ConnectionBuilder {

	public static final String TAG = "ConnectionBuilder";

	public static PlatformConnection userConnection;

	public static PlatformConnection payConnection;

	public static StatisticsDelegate statisticsConnection;

	/**
	 * 根据配置文件创建平台连接
	 * 
	 * @param activity
	 * @throws Exception
	 */
	public static void build(ShellActivity activity) throws Exception {
		build(activity, MetaData.LoginType, MetaData.PayType, MetaData.StatisticsType);
	}

	/**
	 * 创建平台连接
	 * 
	 * @param activity
	 * @throws Exception
	 */
	public static void build(ShellActivity activity, String loginClassStr, String payClassStr, String statisticsClassStr)
			throws Exception {
		if (!StringUtils.isEmpty(statisticsClassStr)) {
			Class statisticsClass = Class.forName(statisticsClassStr);
			statisticsConnection = (StatisticsDelegate) statisticsClass.newInstance();
		} else {
			statisticsConnection = new StatisticsDelegate();
		}
		statisticsConnection.init(activity);

		Class loginClass = Class.forName(loginClassStr);
		userConnection = (PlatformConnection) loginClass.newInstance();
		userConnection.setActivity(activity);
		userConnection.init(activity);

		if (loginClassStr.equals(payClassStr)) { // 如果相同，共用同一连接
			payConnection = userConnection;
			return;
		}

		Class payClass = Class.forName(payClassStr);
		payConnection = (PlatformConnection) payClass.newInstance();
		payConnection.setActivity(activity);
		payConnection.init(activity);
	}

	/**
	 * 选择了分区
	 */
	public static void onAreaSelected(Activity activity) {
		statisticsConnection.onAreaSelected(activity);
		if (userConnection == null) {
			return;
		}
		userConnection.onAreaSelected();
		if (!userConnection.equals(payConnection)) {
			payConnection.onAreaSelected();
		}

	}

	public static void onEnterGame(Bundle bundle, Activity activity) {
		statisticsConnection.onEnterGame(bundle, activity);
		if (userConnection == null) {
			return;
		}
		userConnection.onEnterGame(bundle);
		if (!userConnection.equals(payConnection)) {
			payConnection.onEnterGame(bundle);
		}
	}
	
	public static void submitExtraData(int dataType, int roleLevel, int roleId, int moneyNum, int gem) {
		statisticsConnection.submitExtraData(dataType, roleLevel, roleId, moneyNum, gem);
		if (userConnection == null) {
			return;
		}
		userConnection.submitExtraData(dataType, roleLevel, roleId, moneyNum, gem);
		if (!userConnection.equals(payConnection)) {
			payConnection.submitExtraData(dataType, roleLevel, roleId, moneyNum, gem);
		}
	}

	public static void logEvent(Activity activity, int actionId) {
		statisticsConnection.logEvent(activity, actionId);
		if (userConnection == null) {
			return;
		}
		userConnection.logEvent(actionId);
		if (!userConnection.equals(payConnection)) {
			payConnection.logEvent(actionId);
		}
	}
	
	public static void onSetCurrencyCode(String code){
		statisticsConnection.onSetCurrencyCode(code);
		if (userConnection == null) {
			return;
		}
		userConnection.onSetCurrencyCode(code);
		if (!userConnection.equals(payConnection)) {
			payConnection.onSetCurrencyCode(code);
		}
	}
	
	public static void appFlyerEvent(Activity activity,String type,String value){
		statisticsConnection.appFlyerEvent(activity, type, value);
		if (userConnection == null) {
			return;
		}
		userConnection.appFlyerEvent(type,value);
		if (!userConnection.equals(payConnection)) {
			payConnection.appFlyerEvent(type,value);
		}
	}
	
	public static void loginButEvent(Activity activity){
		statisticsConnection.loginBut(activity);
		if (userConnection == null) {
			return;
		}
		userConnection.loginBut();
		if (!userConnection.equals(payConnection)) {
			payConnection.loginBut();
		}
	}

	public static void onPause() {
		if (userConnection == null) {
			return;
		}
		userConnection.onPause();
		if (!userConnection.equals(payConnection)) {
			payConnection.onPause();
		}
	}

	public static void onResume() {
		if (userConnection == null) {
			return;
		}
		userConnection.onResume();
		if (!userConnection.equals(payConnection)) {
			payConnection.onResume();
		}
	}

	public static void onCreate() {
		if (userConnection == null) {
			return;
		}
		userConnection.onCreate();
		if (!userConnection.equals(payConnection)) {
			payConnection.onCreate();
		}
	}

	public static void onRestart() {
		if (userConnection == null) {
			return;
		}
		userConnection.onRestart();
		if (!userConnection.equals(payConnection)) {
			payConnection.onRestart();
		}
	}

	public static void onStop() {
		if (userConnection == null) {
			return;
		}
		userConnection.onStop();
		if (!userConnection.equals(payConnection)) {
			payConnection.onStop();
		}
	}

	public static void onDestory() {
		statisticsConnection.onDestory();
		if (userConnection == null) {
			return;
		}
		userConnection.onDestory();
		if (!userConnection.equals(payConnection)) {
			payConnection.onDestory();
		}
	}
	
	public static void onNewIntent(){
		if (userConnection == null) {
			return;
		}
		userConnection.onNewIntent();;
		if (!userConnection.equals(payConnection)) {
			payConnection.onNewIntent();
		}
	}

	/**
	 * 界面回调
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public static void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (userConnection == null) {
			return;
		}
		userConnection.onActivityResult(requestCode, resultCode, data);
		if (!userConnection.equals(payConnection)) {
			payConnection.onActivityResult(requestCode, resultCode, data);
		}
	}
}
