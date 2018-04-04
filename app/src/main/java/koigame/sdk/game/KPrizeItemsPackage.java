package koigame.sdk.game;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import koigame.sdk.util.JSONUtils;


public class KPrizeItemsPackage {
	//
	private String packageDesc;

	//
	private List<KPrizeItem> items = new ArrayList<KPrizeItem>();

	public String getPackageDesc() {
		return packageDesc;
	}

	public void setPackageDesc(String packageDesc) {
		this.packageDesc = packageDesc;
	}

	public List<KPrizeItem> getItems() {
		return items;
	}

	public void setItems(List<KPrizeItem> items) {
		this.items = items;
	}

	public static KPrizeItemsPackage build(String jsonStr) {
		KPrizeItemsPackage returnValue = new KPrizeItemsPackage();

		JSONObject props = JSONUtils.build(jsonStr);
		returnValue.setPackageDesc(JSONUtils.getString(props, "packageDesc"));

		List<KPrizeItem> itemList = new ArrayList<KPrizeItem>();
		try {
			JSONArray array = JSONUtils.getArray(props, "items");
			for (int i = 0; i < array.length(); i++) {
				Object value = array.get(i);
				if (value != null) {
					itemList.add(KPrizeItem.build(value.toString()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		returnValue.setItems(itemList);
		return returnValue;
	}
}
