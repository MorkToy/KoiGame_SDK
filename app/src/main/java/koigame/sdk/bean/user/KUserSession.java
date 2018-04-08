package koigame.sdk.bean.user;

import android.content.Context;

import koigame.sdk.util.PreferenceUtils;
import koigame.sdk.util.StringUtils;


public class KUserSession {
	private static KUserSession instance = new KUserSession();
	public static String USER_ID;
	public static String SITE_ID;
	public static String ACCOUNT_NAME;
	public static String GAME_ID;
	public static String PASSWORD;
	public static String ACCESS_TOKEN;
	public static String AREAID;
	public static String REFRESH_TOKEN;
	public static String ROLEID;
	public static String BINDPHONE;
	
	

	private String sdkUserId;

	private String sdkAccessToken;

	private String sdkUsername;




	private KUserInfo userInfo = new KUserInfo();

	private Context ctx;

	private KUserSession() {

	}

	public static KUserSession instance() {
		return instance;
	}

	public void init(Context ctx) {
		if (ctx == null)
			throw new IllegalArgumentException("The context is null!");
		this.ctx = ctx;
		
		String SPACE = ctx.getPackageName() + ".SPACE.";
		USER_ID = SPACE + "USER_ID";
		SITE_ID = SPACE + "SITE_ID";
		GAME_ID = SPACE + "GAME_ID";
		ACCOUNT_NAME = SPACE + "ACCOUNT_NAME";
		PASSWORD = SPACE + "PASSWORD";
		ACCESS_TOKEN = SPACE + "ACCESS_TOKEN";
		REFRESH_TOKEN = SPACE + "REFRESH_TOKEN";
		ROLEID = SPACE + "ROLE_ID";
		AREAID = "areaId";
		BINDPHONE = "BIND_PHONE";

		buildCachedUserInfo();
	}

	/**
	 * 加载preference数据
	 * 
	 * @return
	 */
	public KUserInfo buildCachedUserInfo() {
		KUserInfo userInfo = new KUserInfo();
		userInfo.setUserId(PreferenceUtils.instance(ctx).getLong(USER_ID, 0));
		userInfo.setSiteId(PreferenceUtils.instance(ctx).getInt(SITE_ID, 0));
		userInfo.setGameId(PreferenceUtils.instance(ctx).getInt(GAME_ID, 0));
		userInfo.setPassword(PreferenceUtils.instance(ctx).getString(PASSWORD, null));
		userInfo.setAccessToken(PreferenceUtils.instance(ctx).getString(ACCESS_TOKEN, null));
		userInfo.setAreaCode(PreferenceUtils.instance(ctx).getInt(AREAID, 0));
		userInfo.setAccountName(PreferenceUtils.instance(ctx).getString(ACCOUNT_NAME, null));
		userInfo.setRefreshToken(PreferenceUtils.instance(ctx).getString(REFRESH_TOKEN, null));
		userInfo.setRoleId(PreferenceUtils.instance(ctx).getString(ROLEID, null));
		return userInfo;
	}
	
	public KUserInfo buildLocalUserInfo(){
		KUserInfo userInfo = new KUserInfo();
		userInfo.setAreaCode(PreferenceUtils.instance().getInt("areaId", 0));
		userInfo.setRoleId(PreferenceUtils.instance().getString("roleId", null));
		return userInfo;
	}

	/**
	 * 
	 * @param userInfo
	 */
	public void saveToPreference(KUserInfo userInfo) {
		if (userInfo.getUserId() > 0) {
			PreferenceUtils.instance().putLong(USER_ID, userInfo.getUserId());
		}
		if (userInfo.getSiteId()>0) {
			PreferenceUtils.instance().putInt(SITE_ID, userInfo.getSiteId());
		}
		if (userInfo.getGameId()>0) {
			PreferenceUtils.instance().putInt(GAME_ID, userInfo.getGameId());
		}
		if (!StringUtils.isEmpty(userInfo.getAccountName())) {
			PreferenceUtils.instance().putString(ACCOUNT_NAME, userInfo.getAccountName());
		}
		if (!StringUtils.isEmpty(userInfo.getPassword())) {
			PreferenceUtils.instance().putString(PASSWORD, userInfo.getPassword());
		}
		if (!StringUtils.isEmpty(userInfo.getAccessToken())) {
			PreferenceUtils.instance().putString(ACCESS_TOKEN, userInfo.getAccessToken());
		}
		if (!StringUtils.isEmpty(userInfo.getRefreshToken())) {
			PreferenceUtils.instance().putString(REFRESH_TOKEN, userInfo.getRefreshToken());
		}
		if (!StringUtils.isEmpty(userInfo.getRoleId())) {
			PreferenceUtils.instance().putString(ROLEID, userInfo.getRoleId());
		}
		if (!StringUtils.isEmpty(userInfo.getBindPhoneNum())  && !userInfo.getBindPhoneNum().equals("0") ) {
			PreferenceUtils.instance().putString(BINDPHONE, userInfo.getBindPhoneNum());
		}
	}

	/**
	 * 清除preference数据
	 */
	public void cleanPreference() {
		PreferenceUtils.instance().remove(GAME_ID);
		PreferenceUtils.instance().remove(USER_ID);
		PreferenceUtils.instance().remove(PASSWORD);
		PreferenceUtils.instance().remove(ACCESS_TOKEN);
		PreferenceUtils.instance().remove(SITE_ID);
		PreferenceUtils.instance().remove(ACCOUNT_NAME);
		PreferenceUtils.instance().remove(REFRESH_TOKEN);
		PreferenceUtils.instance().remove(ROLEID);
		//KUserSession.instance().setUserInfo(null);
	}

	public KUserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(KUserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public String getSdkUserId() {
		return sdkUserId;
	}

	public void setSdkUserId(String sdkUserId) {
		this.sdkUserId = sdkUserId;
	}

	public String getSdkAccessToken() {
		return sdkAccessToken;
	}

	public void setSdkAccessToken(String sdkAccessToken) {
		this.sdkAccessToken = sdkAccessToken;
	}

	public String getSdkUsername() {
		return sdkUsername;
	}

	public void setSdkUsername(String sdkUsername) {
		this.sdkUsername = sdkUsername;
	}


}