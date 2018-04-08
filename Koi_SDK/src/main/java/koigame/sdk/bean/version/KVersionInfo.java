package koigame.sdk.bean.version;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import koigame.sdk.util.JSONUtils;
import koigame.sdk.util.StringUtils;


public class KVersionInfo {
	private boolean apkNeedUpdate = false;
	private boolean resourceNeedUpdate = false;

	private boolean apkUpdateRequired = false;
	private boolean resourceUpdateRequired = false;

	private int apkVerCode;
	private String appVerDesc;
	private String appVerName;

	private KUpdateInfo apkUpdateConfig;
	private List<KUpdateInfo> resourceUpdateConfigs = new ArrayList<KUpdateInfo>();

	public KVersionInfo(JSONObject props) {
		try {
			String versionInfo = JSONUtils.getString(props, "gameVersionInfos");
			JSONObject versionObj = JSONUtils.build(versionInfo);

			apkNeedUpdate = JSONUtils.getBoolean(versionObj, "appNeedUpdate");
			resourceNeedUpdate = JSONUtils.getBoolean(versionObj, "resourceNeedUpdate");
			apkUpdateRequired = JSONUtils.getBoolean(versionObj, "appUpdateRequired");
			resourceUpdateRequired = JSONUtils.getBoolean(versionObj, "resourceUpdateRequired");
			apkVerCode = JSONUtils.getInt(versionObj, "appVerCode");
			appVerDesc = JSONUtils.getString(versionObj, "appVerDesc");
			appVerName = JSONUtils.getString(versionObj, "appVerName");

			String appUpdateConfigJson = JSONUtils.getString(versionObj, "appUpdateConfig");
			if (!StringUtils.isEmpty(appUpdateConfigJson)) {
				apkUpdateConfig = new KUpdateInfo(JSONUtils.build(appUpdateConfigJson));
			}
			resourceUpdateConfigs = JSONUtils.toList(JSONUtils.getArray(versionObj, "resourceUpdateConfigs"),
					KUpdateInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean isApkNeedUpdate() {
		return apkNeedUpdate;
	}

	public void setApkNeedUpdate(boolean apkNeedUpdate) {
		this.apkNeedUpdate = apkNeedUpdate;
	}

	public boolean isResourceNeedUpdate() {
		return resourceNeedUpdate;
	}

	public void setResourceNeedUpdate(boolean resourceNeedUpdate) {
		this.resourceNeedUpdate = resourceNeedUpdate;
	}

	public boolean isApkUpdateRequired() {
		return apkUpdateRequired;
	}

	public void setApkUpdateRequired(boolean apkUpdateRequired) {
		this.apkUpdateRequired = apkUpdateRequired;
	}

	public boolean isResourceUpdateRequired() {
		return resourceUpdateRequired;
	}

	public void setResourceUpdateRequired(boolean resourceUpdateRequired) {
		this.resourceUpdateRequired = resourceUpdateRequired;
	}

	public int getApkVerCode() {
		return apkVerCode;
	}

	public void setApkVerCode(int apkVerCode) {
		this.apkVerCode = apkVerCode;
	}

	public String getAppVerDesc() {
		return appVerDesc;
	}

	public void setAppVerDesc(String appVerDesc) {
		this.appVerDesc = appVerDesc;
	}

	public String getAppVerName() {
		return appVerName;
	}

	public void setAppVerName(String appVerName) {
		this.appVerName = appVerName;
	}

	public KUpdateInfo getApkUpdateConfig() {
		return apkUpdateConfig;
	}

	public void setApkUpdateConfig(KUpdateInfo apkUpdateConfig) {
		this.apkUpdateConfig = apkUpdateConfig;
	}

	public List<KUpdateInfo> getResourceUpdateConfigs() {
		return resourceUpdateConfigs;
	}

	public void setResourceUpdateConfigs(List<KUpdateInfo> resourceUpdateConfigs) {
		this.resourceUpdateConfigs = resourceUpdateConfigs;
	}

}