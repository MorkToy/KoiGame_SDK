package koigame.sdk.bean.user;

import org.json.JSONArray;
import org.json.JSONObject;

import koigame.sdk.bean.version.KVersionInfo;
import koigame.sdk.util.JSONUtils;

public class KLoginInfo {
	public static KVersionInfo versionInfo;
	public static String gameAreaPropses;
	public static KMetaDataConfig metaDataConfig;
	public static JSONObject dataJson;
	public static JSONObject roleJson;
	public static JSONArray worldAnnounceJson;
	public static int chargeMoney;
	public static String worldAnnounceContent = "";

	public KLoginInfo() {
	}

	public KLoginInfo(JSONObject props) {
		dataJson = JSONUtils.getJSONObject(props, "data");
		roleJson = JSONUtils.getJSONObject(dataJson, "areaRoleProps");
		worldAnnounceJson =  JSONUtils.getArray(dataJson, "worldAnnounce");
		chargeMoney = JSONUtils.getInt(dataJson, "chargeMoney");
		versionInfo = new KVersionInfo(dataJson);
		if (worldAnnounceJson != null) {
			worldAnnounceContent = worldAnnounceJson.toString();
		}
		gameAreaPropses = roleJson.toString();
	}

}