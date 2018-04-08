package koigame.sdk.game;

/**
 * Created with IntelliJ IDEA. User: Mike Date: 13-8-22 Time: 下午6:14 To change
 * this template use File | Settings | File Templates.
 */
public class KGamePrice {

	private String goodsCode;
	private String relatedGoodsId; // 第三方productId
	private String goodsName;

	private int goodsAmount;
	private double discount = 0.00;

	private int goodsCount;
	private String goodsDesc;
	private String goodsMarkTime;
	private String goodsItem;
    private int shopType;


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

	public int getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(int goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public int getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public String getGoodsMarkTime() {
		return goodsMarkTime;
	}

	public void setGoodsMarkTime(String goodsMarkTime) {
		this.goodsMarkTime = goodsMarkTime;
	}

	public String getGoodsItem() {
		return goodsItem;
	}

	public void setGoodsItem(String goodsItem) {
		this.goodsItem = goodsItem;
	}

    public int getShopType() {
        return shopType;
    }

    public void setShopType(int shopType) {
        this.shopType = shopType;
    }
}
