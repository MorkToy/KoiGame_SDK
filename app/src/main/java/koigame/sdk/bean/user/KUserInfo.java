package koigame.sdk.bean.user;

import org.json.JSONException;
import org.json.JSONObject;

import koigame.sdk.util.JSONUtils;


public class KUserInfo {

	public final static String USER_ID = "userId";
	public final static String SITE_ID = "siteId";
	public final static String GAME_ID = "gameId";
	public final static String PASSWORD = "passwd";
	public final static String ACCESS_TOKEN = "accessToken";
	public final static String ACCOUT_NAME = "accountName";
	public final static String DATA = "data";
	public final static String REFRESHTOKEN = "refreshToken";
	public final static String GAMEAREAROLEVO = "gameAreaRoleVo";
	public final static String ACCOUNT_ID = "accountId";
	public final static String PHONENUM = "bindPhoneNum";

	private long userId;
	private int SiteId;
	private int gameId;
	private int accountId;

	private String accountName;
	private String refreshToken;
	private String gameAreaRoleVo;


	private String password;
	private String accessToken;
	private String roleName;
	private String bindPhoneNum;


	private String roleId = "";
	private int roleLevel;
	private String roleHead;
	private int roleVIPLevel;
	private long createRoleTime;
	private int areaCode = 0;
	private String areaName;
	private int gem;
	private int gold;
	private JSONObject dataJson;

	public KUserInfo() {
	}

	public KUserInfo(JSONObject props) {
		dataJson = JSONUtils.getJSONObject(props,DATA);
		userId = JSONUtils.getLong(dataJson, USER_ID);
		SiteId = JSONUtils.getInt(dataJson, SITE_ID);
		accountName = JSONUtils.getString(dataJson, ACCOUT_NAME);
		gameId = JSONUtils.getInt(dataJson, GAME_ID);
		refreshToken = JSONUtils.getString(dataJson, REFRESHTOKEN);
		gameAreaRoleVo = JSONUtils.getString(dataJson, GAMEAREAROLEVO);
		accessToken = JSONUtils.getString(dataJson, ACCESS_TOKEN);
		bindPhoneNum = JSONUtils.getString(dataJson, PHONENUM);
		accountId = (int)userId;
	}

	public String getString(JSONObject jsonObj, String key) {
		try {
			return jsonObj.getString(key);
		} catch (JSONException e) {
			return null;
		}
	}

	public boolean getBoolean(JSONObject jsonObj, String key) {
		try {
			return jsonObj.getBoolean(key);
		} catch (JSONException e) {
			return false;
		}
	}

	public Double getDouble(JSONObject jsonObj, String key) {
		try {
			return jsonObj.getDouble(key);
		} catch (JSONException e) {
			return 0.0;
		}
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}


	public int getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(int areaCode) {
		this.areaCode = areaCode;
	}

	public int getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(int roleLevel) {
		this.roleLevel = roleLevel;
	}

	public int getRoleVIPLevel() {
		return roleVIPLevel;
	}

	public void setRoleVIPLevel(int roleVIPLevel) {
		this.roleVIPLevel = roleVIPLevel;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getRoleHead() {
		return roleHead;
	}

	public void setRoleHead(String roleHead) {
		this.roleHead = roleHead;
	}

	public long getCreateRoleTime() {
		return createRoleTime;
	}

	public void setCreateRoleTime(long createRoleTime) {
		this.createRoleTime = createRoleTime;
	}
	public int getSiteId() {
		return SiteId;
	}

	public void setSiteId(int siteId) {
		SiteId = siteId;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getGameAreaRoleVo() {
		return gameAreaRoleVo;
	}

	public void setGameAreaRoleVo(String gameAreaRoleVo) {
		this.gameAreaRoleVo = gameAreaRoleVo;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getGem() {
		return gem;
	}

	public void setGem(int gem) {
		this.gem = gem;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public String getBindPhoneNum() {
		return bindPhoneNum;
	}

	public void setBindPhoneNum(String bindPhoneNum) {
		this.bindPhoneNum = bindPhoneNum;
	}
}