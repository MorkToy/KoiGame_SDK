package koigame.sdk.bean.user;

import org.json.JSONObject;

import koigame.sdk.util.JSONUtils;


public class KMetaDataConfig {

	private String wechatAppId;
	private String weiboAppKey;
	private boolean gumpWebpay;
	private String promoteTurnOn;


	public KMetaDataConfig(JSONObject props) {
		try {
			String metaDataConfig = JSONUtils.getString(props, "metaDataConfig");
			JSONObject metaDataObj = JSONUtils.build(metaDataConfig);
			
			wechatAppId = JSONUtils.getString(metaDataObj, "wechatAppId");
			weiboAppKey = JSONUtils.getString(metaDataObj, "weiboAppKey");
			gumpWebpay = JSONUtils.getBoolean(metaDataObj, "gumpWebpay");
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getWechatAppId() {
		return wechatAppId;
	}


	public void setWechatAppId(String wechatAppId) {
		this.wechatAppId = wechatAppId;
	}


	public String getWeiboAppKey() {
		return weiboAppKey;
	}


	public void setWeiboAppKey(String weiboAppKey) {
		this.weiboAppKey = weiboAppKey;
	}

	public boolean isGumpWebpay() {
		return gumpWebpay;
	}

	public void setGumpWebpay(boolean gumpWebpay) {
		this.gumpWebpay = gumpWebpay;
	}

	public String getPromoteTurnOn() {
		return promoteTurnOn;
	}

	public void setPromoteTurnOn(String promoteTurnOn) {
		this.promoteTurnOn = promoteTurnOn;
	}
	
	


}