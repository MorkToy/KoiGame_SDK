package koigame.android.platform.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import java.util.Properties;

/**
 * 统计代理
 * 
 * @author Mike
 * 
 */
public class StatisticsDelegate {

	/**
	 * 初始化
	 */
	public void init(Activity activity) {
	}

	/**
	 * 点击充值
	 * 
	 * @param activity
	 */
	public void chargeClicked(Activity activity) {
	}
	
	/**
	 * 选择分区
	 * @param activity
	 */
	public void onAreaSelected(Activity activity) {
	}

	/**
	 * 进入游戏
	 * 
	 * @param bundle
	 * @param activity
	 */
	public void onEnterGame(Bundle bundle, Activity activity) {
	}
	
	
	public void submitExtraData(int dataType, int roleLevel, int moneyNum, int vipLevel, int gem) {
		
	}

	/**
	 * 记录事件
	 */
	public void logEvent(Activity activity, int actionId) {
	}
	
	/**
	 * 记录appflyer的自定义数据
	 * @param activity
	 * @param type
	 * @param value
	 */
	public void appFlyerEvent(Activity activity,String type,String value){
		
	}
	
	/**
	 * 购买用的币种以及金额
	 * @param code
	 * @param price
	 */
	public void onSetCurrencyCode(String code){
		
	}
	
	/**
	 * 主activity销毁
	 */
	public void onDestory() {
		
	}
	/**
	 * 登陆按钮日志
	 */
	public void loginBut(Activity activity){
		
	}
	/**
	 * 獲取SDK版本號
	 */
	public void getSdkVersion(String sdkVersion){
		
	}
	
	protected final String[] getSdkParams(Context context,int size) throws Exception {
		String[] params = new String[size];
		Properties paramProperties = new Properties();
		paramProperties.load(context.getAssets().open("params.properties"));
		for (int i = 0; i < size; i++) {
			params[i] = paramProperties.getProperty("p" + i);
		}
		return params;
	}

}