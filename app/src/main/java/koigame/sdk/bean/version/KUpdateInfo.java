package koigame.sdk.bean.version;

import org.json.JSONObject;

import koigame.sdk.util.JSONUtils;


public class KUpdateInfo {

	private String updatePackageUrl;
	private String updateFileName;

	private int updatePackageSize;

	public KUpdateInfo() {

	}

	public KUpdateInfo(JSONObject props) {
		updatePackageUrl = JSONUtils.getString(props, "updatePackageUrl");
		updateFileName = JSONUtils.getString(props, "updateFileName");
		updatePackageSize = JSONUtils.getInt(props, "updatePackageSize");
	}

	public String getUpdatePackageUrl() {
		return updatePackageUrl;
	}

	public void setUpdatePackageUrl(String updatePackageUrl) {
		this.updatePackageUrl = updatePackageUrl;
	}

	public String getUpdateFileName() {
		return updateFileName;
	}

	public void setUpdateFileName(String updateFileName) {
		this.updateFileName = updateFileName;
	}

	public int getUpdatePackageSize() {
		return updatePackageSize;
	}

	public void setUpdatePackageSize(int updatePackageSize) {
		this.updatePackageSize = updatePackageSize;
	}

}
