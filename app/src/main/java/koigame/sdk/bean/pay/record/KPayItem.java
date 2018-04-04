package koigame.sdk.bean.pay.record;

import org.json.JSONObject;

import java.util.Date;

import koigame.sdk.util.JSONUtils;

public class KPayItem {

	private String orderNO;

	private String game;
	private String areaInfo;

	private String currency;
	private float price;

	private String orderDesc;
	private Date payDate;

	public KPayItem() {

	}

	public KPayItem(JSONObject props) {
		orderNO = JSONUtils.getString(props, "orderNO");
		game = JSONUtils.getString(props, "game");

		areaInfo = JSONUtils.getString(props, "areaInfo");
		price = JSONUtils.getFloat(props, "parValue");
		orderDesc = JSONUtils.getString(props, "orderDesc");
		payDate = new Date(JSONUtils.getLong(props, "date"));
	}

	public String getOrderNO() {
		return orderNO;
	}

	public void setOrderNO(String orderNO) {
		this.orderNO = orderNO;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getAreaInfo() {
		return areaInfo;
	}

	public void setAreaInfo(String areaInfo) {
		this.areaInfo = areaInfo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

}
