package koigame.sdk.bean.pay.record;

import org.json.JSONObject;

import java.util.Date;

import koigame.sdk.util.JSONUtils;


public class KRechargeItem {

	private float price;
	private String rechargeType;
	private String orderNO;
	private Date rechargeDate;

	public KRechargeItem() {

	}

	public KRechargeItem(JSONObject props) {
		orderNO = JSONUtils.getString(props, "orderNO");
		rechargeType = JSONUtils.getString(props, "type");
		price = JSONUtils.getFloat(props, "parValue");
		rechargeDate = new Date(JSONUtils.getLong(props, "date"));
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getRechargeType() {
		return rechargeType;
	}

	public void setRechargeType(String rechargeType) {
		this.rechargeType = rechargeType;
	}

	public String getOrderNO() {
		return orderNO;
	}

	public void setOrderNO(String orderNO) {
		this.orderNO = orderNO;
	}

	public Date getRechargeDate() {
		return rechargeDate;
	}

	public void setRechargeDate(Date rechargeDate) {
		this.rechargeDate = rechargeDate;
	}

}
