package koigame.sdk.game;

import org.json.JSONObject;

import koigame.sdk.util.JSONUtils;


public class KPrizeItem {

	//
	private String itemName;

	//
	private int amount = 1;

	//
	public KPrizeItem() {
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public static KPrizeItem build(String jsonStr) {
		KPrizeItem returnValue = new KPrizeItem();

		JSONObject props = JSONUtils.build(jsonStr);
		returnValue.setAmount(JSONUtils.getInt(props, "amount"));
		returnValue.setItemName(JSONUtils.getString(props, "itemName"));

		return returnValue;
	}
}
