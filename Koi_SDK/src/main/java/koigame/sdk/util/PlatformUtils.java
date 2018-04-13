package koigame.sdk.util;

public class PlatformUtils {
	
	private int id;
	
	private String code;

	private String siteName;



	private static PlatformUtils instance = new PlatformUtils();
	
	private PlatformUtils() {}
	
	public static PlatformUtils getInstance() {
		return instance;
	}
	
	public int getGameResouceId(String code) {
		if ("jokes".equals(code)) {
			return 27;
		} else if ("yangwa".equals(code)) {
			return 1001;
		}
		return 27;
	}
	
	public int getSiteResouceId(String site) {
		if ("test".equals(site)) {
			return 28;
		} else if ("sdk_leying".equals(site)) {
			return 29;
		} else if ("sdk_360".equals(site)){
			return 30;
		}else if ("sdk_uc".equals(site)){
			return 32;
		}else if ("sdk_bd".equals(site)){
			return 35;
		}else if ("sdk_xm".equals(site)){
			return 33;
		}else if ("sdk_hw".equals(site)){
			return 36;
		}else if ("sdk_lenovo".equals(site)){
			return 41;
		}else if ("sdk_oppo".equals(site)){
			return 37;
		}else if ("sdk_vivo".equals(site)){
			return 31;
		}else if ("sdk_downjoy".equals(site)){
			return 15;
		}else if ("sdk_jinli".equals(site)){
			return 38;
		}else if ("sdk_ltv".equals(site)){
			return 17;
		}else if ("sdk_anzhi".equals(site)){
			return 18;
		}else if ("sdk_kupai".equals(site)){
			return 39;
		} else if ("sdk_yingyongba".equals(site)) {
			return 34;
		} else if ("sdk_meizu".equals(site)) {
			return 40;
		} else if ("sdk_aofei".equals(site)) {
			return 42;
		}
		else if("mobile_android_youle_google".equals(site)){
			return 201;
		}
		else if("mobile_android_youle_third".equals(site)){
			return 203;
		}
		else if("mobile_android_youle_hk".equals(site)){
			return 205;
		}
		return 0;
	}

	public String getChannelName() {
		if ("test".equals(siteName)) {
			return "test";
		} else if ("sdk_leying".equals(siteName)) {
			return "lyt";
		} else if ("sdk_360".equals(siteName)){
			return "360";
		}else if ("sdk_uc".equals(siteName)){
			return "UC";
		}else if ("sdk_bd".equals(siteName)){
			return "百度";
		}else if ("sdk_xm".equals(siteName)){
			return "小米";
		}else if ("sdk_hw".equals(siteName)){
			return "华为";
		}else if ("sdk_lenovo".equals(siteName)){
			return "联想";
		}else if ("sdk_oppo".equals(siteName)){
			return "OPPO";
		}else if ("sdk_vivo".equals(siteName)){
			return "VIVO";
		}else if ("sdk_downjoy".equals(siteName)){
			return "当乐";
		}else if ("sdk_jinli".equals(siteName)){
			return "金立";
		}else if ("sdk_ltv".equals(siteName)){
			return "乐视";
		}else if ("sdk_anzhi".equals(siteName)){
			return "安智";
		}else if ("sdk_kupai".equals(siteName)){
			return "酷派";
		}else if ("sdk_youlong".equals(siteName)){
			return "游龙";
		}else if ("sdk_m4399".equals(siteName)){
			return "4399";
		}else if ("sdk_mumayi".equals(siteName)){
			return "木蚂蚁";
		}else if ("sdk_pengyouwan".equals(siteName)){
			return "朋友玩";
		}else if ("sdk_youku".equals(siteName)){
			return "优酷";
		}else if ("sdk_youyoucun".equals(siteName)){
			return "悠悠村";
		}else if ("sdk_wandoujia".equals(siteName)){
			return "豌豆荚";
		}else if ("sdk_jifeng".equals(siteName)){
			return "机锋";
		}else if ("sdk_paojiaowang".equals(siteName)){
			return "泡椒网";
		}else if ("sdk_chuyou".equals(siteName)){
			return "07073";
		}else if ("sdk_lhh".equals(siteName)){
			return "乐嗨嗨";
		}else if ("sdk_ouwan".equals(siteName)){
			return "偶玩";
		}else if ("sdk_muzhiwan".equals(siteName)){
			return "拇指玩";
		}else if ("sdk_juhaowang".equals(siteName)){
			return "8868";
		}else if ("sdk_anqu".equals(siteName)){
			return "安趣";
		}else if ("sdk_chongchong".equals(siteName)){
			return "虫虫";
		}else if ("sdk_tangteng".equals(siteName)){
			return "唐腾";
		}else if ("sdk_kuaiyong".equals(siteName)){
			return "快用";
		}else if ("sdk_tt".equals(siteName)){
			return "TT";
		}else if ("sdk_guopan".equals(siteName)){
			return "果盘";
		}else if ("sdk_xiongmaowan".equals(siteName)){
			return "熊猫玩";
		}else if ("sdk_songguo".equals(siteName)){
			return "松果";
		}else if ("sdk_yingyonghui".equals(siteName)){
			return "应用汇";
		}else if ("sdk_kaopu".equals(siteName)){
			return "靠谱";
		}else if ("sdk_zhuoyi".equals(siteName)){
			return "卓易";
		}else if ("sdk_youleyuan".equals(siteName)){
			return "游乐猿";
		}else if ("sdk_htc".equals(siteName)){
			return "htc";
		}else if ("sdk_leying".equals(siteName)){
			return "leying";
		}else if ("sdk_kuaifa".equals(siteName)){
			return "快发";
		}else if ("sdk_papa".equals(siteName)){
			return "啪啪";
		}else if ("sdk_sogou".equals(siteName)){
			return "搜狗";
		}else if ("sdk_pps".equals(siteName)){
			return "pps";
		}else if ("sdk_nox".equals(siteName)){
			return "夜神";
		}else if ("sdk_nubia".equals(siteName)){
			return "努比亚";
		}else if ("sdk_niuniu".equals(siteName)){
			return "牛牛";
		}else if ("sdk_samsung".equals(siteName)){
			return "三星";
		}else if ("sdk_shuowan".equals(siteName)){
			return "说玩";
		}else if ("sdk_benshouji".equals(siteName)){
			return "笨手机";
		}else if ("sdk_itoos".equals(siteName)){
			return "itools";
		}else if ("sdk_haima".equals(siteName)){
			return "海马";
		}

		return "leying";
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

		public void setCode(String code) {
		this.code = code;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
}
