package koigame.sdk.bean.pay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Currency implements Serializable {
	//
	private final static Map<Integer, Currency> idsMap = new HashMap<Integer, Currency>();
	private final static Map<String, Currency> codesMap = new HashMap<String, Currency>();

	//
	private int id;
	private String code;
	private String name;

	//
	private String unitName;

	//
	private double factorWithRMB = 1.00; // 虚拟货币与RMB分的汇率（虚拟货币/RMB）。
	private double factorWithPartner = 1.00; // 我方可分比例（我方/总额）。
	private double factorDisplayWithStore = 0.01; // 显示/存储。数据库中存储都是以最小单位进行保存成long型。
	private boolean needDetail = true;

	// See note in javadoc if you add a new entry here!
	public static final Currency UNKNOWN = new Currency(0, "UNKNOWN", "UNKNOWN Currency");

	// hilink currency.
	public static final Currency HIDOU = new Currency(1, "HIDOU", "哈豆", 1.00f, 1.00f);

	// 100
	public static final Currency AUD = new Currency(36, "AUD", "Australian Dollar");
	public static final Currency CAD = new Currency(143, "CAD", "Canadian Dollar");
	public static final Currency DKK = new Currency(208, "DKK", "Danish Krone");
	public static final Currency HKD = new Currency(344, "HKD", "Hong Kong Dollar"); // 港元
	public static final Currency JPY = new Currency(392, "JPY", "Japanese Yen", 1.00f); // 日元
	public static final Currency NZD = new Currency(554, "NZD", "New Zealand Dollar");
	public static final Currency NOK = new Currency(578, "NOK", "Norwegian Krone");
	public static final Currency SEK = new Currency(752, "SEK", "Swedish Krona");
	public static final Currency CHF = new Currency(756, "CHF", "Swiss Franc");
	public static final Currency USD = new Currency(840, "USD", "United States Dollar");

	public static final Currency GBP = new Currency(826, "GBP", "Sterling"); // 英镑
	public static final Currency EUR = new Currency(978, "EUR", "Euro"); // 欧元
	public static final Currency CNY = new Currency(888, "CNY", "CNY", "元", 1.00f, 1.00f); // 人民币
	public static final Currency TWD = new Currency(887, "TWD", "TWD"); // 台币
	public static final Currency THB = new Currency(886, "THB", "THB"); // 泰铢
	public static final Currency KRW = new Currency(885, "KRW", "KRW"); // 韩元

	//
	public static final Currency P91 = new Currency(884, "91", "91豆"); // 91豆
	public static final Currency GFAN = new Currency(883, "GFAN", "机峰券", 0.10f, 0.5f); // 机峰券
	public static final Currency UC = new Currency(882, "UC", "元", 1.00f, 0.4f); // UC券
	public static final Currency DOWNJOY = new Currency(881, "DOWNJOY", "元", 1.00f, 0.5f);// 人民币
	public static final Currency TAOMEE = new Currency(880, "TAOMEE", "米米", 1.00f, 0.5f); // 淘米
	public static final Currency PAY_360 = new Currency(879, "360", "360币", 1.00f, 0.5f); // 360币
	public static final Currency XIAOMI = new Currency(900, "XIAOMI", "米币", 1.00f, 0.5f); // 米币
	public static final Currency HUAWEI = new Currency(901, "HUAWEI", "T宝", 1.00f, 0.5f); // 华为T宝
	public static final Currency TOM = new Currency(902, "TOM", "元", 1.00f, 0.6f); // 人民币
	public static final Currency GAMEONE = new Currency(903, "GAMEONE", "gameone", 1.00f, 0.5f); // gameone
	public static final Currency BAIDU = new Currency(904, "BAIDU", "酷币", 1.00f, 0.5f); // 百度
	public static final Currency DX = new Currency(905, "DX", "元", 1.00f, 0.5f); // 人民币

	// 游戏内货币.

	//
	private Currency(int id, String code, String name, String unitName) {
		this(id, code, name, unitName, 1.00f, 1.00f, 0.01f, true);
	}

	private Currency(int id, String code, String name) {
		this(id, code, name, name, 1.00f, 1.00f, 0.01f, true);
	}

	private Currency(int id, String code, String name, String unitName, double factorWithRMB) {
		this(id, code, name, unitName, factorWithRMB, 1.00f, 0.01f, true);
	}

	private Currency(int id, String code, String name, double factorWithRMB) {
		this(id, code, name, name, factorWithRMB, 1.00f, 0.01f, true);
	}

	private Currency(int id, String code, String name, String unitName, double factorWithRMB, double factorWithPartner) {
		this(id, code, name, unitName, factorWithRMB, factorWithPartner, 0.01f, true);
	}

	private Currency(int id, String code, String name, double factorWithRMB, double factorWithPartner) {
		this(id, code, name, name, factorWithRMB, factorWithPartner, 0.01f, true);
	}

	private Currency(int id, String code, String name, String unitName, double factorWithRMB, double factorWithPartner,
			double factorDisplayWithStore) {
		this(id, code, name, unitName, factorWithRMB, factorWithPartner, factorDisplayWithStore, true);
	}

	private Currency(int id, String code, String name, String unitName, double factorWithRMB, double factorWithPartner,
			double factorDisplayWithStore, boolean needDetail) {
		//
		this.id = id;
		this.code = code;
		this.name = name;

		//
		this.unitName = unitName;

		//
		this.factorWithRMB = factorWithRMB;
		this.factorWithPartner = factorWithPartner;
		this.factorDisplayWithStore = factorDisplayWithStore;
		this.needDetail = needDetail;

		//
		idsMap.put(id, this);
		codesMap.put(code, this);
	}

	public int getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getUnitName() {
		return unitName;
	}

	public double getFactorWithRMB() {
		return factorWithRMB;
	}

	public double getFactorWithPartner() {
		return factorWithPartner;
	}

	public double getFactorDisplayWithStore() {
		return factorDisplayWithStore;
	}

	public boolean isNeedDetail() {
		return needDetail;
	}

	// calculate the store (in db) amount from the display (to user) amount
	public long calculateStoreAmount(double displayAmount) {
		return (long) (displayAmount / factorDisplayWithStore);
	}

	// calculate the display (to user) amount from the store (in db) amount
	public double calculateDisplayAmount(long storeAmount) {
		return factorDisplayWithStore * storeAmount;
	}

	// calculate 分成金额.
	public double calculateRealValueAmount(double parValueAmount) {
		return this.factorWithPartner * parValueAmount;
	}

	// calculate 兑换金额.
	public double calculateExchangeAmount(Currency srcCurrency, double srcAmount) {
		return srcCurrency.getFactorWithRMB() * srcAmount / this.getFactorWithRMB();
	}

	public static Currency getByCode(String code) {
		return codesMap.get(code);
	}

	public static Currency getById(int id) {
		return idsMap.get(id);
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof Currency && id == ((Currency) obj).getId();
	}

	public static List getCurrencyList() {
		return Collections.unmodifiableList(new ArrayList(idsMap.values()));
	}
}