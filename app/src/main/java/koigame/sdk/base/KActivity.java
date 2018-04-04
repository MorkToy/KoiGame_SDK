package koigame.sdk.base;

import org.json.JSONObject;

import koigame.sdk.util.JSONUtils;


/**
 * 活动
 * @author Mike
 *
 */
public class KActivity {

	private String name;
	private String desc;
	
	public KActivity(JSONObject props) {
		name = JSONUtils.getString(props, "activityName");
		desc = JSONUtils.getString(props, "activityDesc");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
