package koigame.sdk.bean.pay;

/**
 * 购买信息
 * 
 * @author Mike
 * 
 */
public class KPayInfo implements java.io.Serializable {

	private String orderId;
	private String reOrderId;
	private String goodsCode;
	private String relatedGoodsId;
	private String goodsName;
	private int payAmount;
	private int goodsCount;
	private Currency currency;
	private String orderDesc;
	private int payType;
	private String extraParams;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getReOrderId() {
		return reOrderId;
	}

	public void setReOrderId(String reOrderId) {
		this.reOrderId = reOrderId;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getRelatedGoodsId() {
		return relatedGoodsId;
	}

	public void setRelatedGoodsId(String relatedGoodsId) {
		this.relatedGoodsId = relatedGoodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(int payAmount) {
		this.payAmount = payAmount;
	}

	public int getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public String getExtraParams() {
		return extraParams;
	}

	public void setExtraParams(String extraParams) {
		this.extraParams = extraParams;
	}
}