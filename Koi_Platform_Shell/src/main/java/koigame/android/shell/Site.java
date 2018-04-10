package koigame.android.shell;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import koigame.sdk.util.StringUtils;

public class Site implements Serializable {

	private static HashMap m_byString = new HashMap();

	private static Site[] m_byInt = new Site[500];

	/**
	 * Every site has an integer code which is also stored in the db.
	 */
	private int code;

	/**
	 * Every site has a String code.
	 */
	private String name;

	/**
	 * Says whether or not this is an "external" site, meaning that it uses a
	 * non-SIE pars module.
	 */
	private boolean external;

	public static final Site TEST = new Site(0, "test", false);

	public static final Site NONE = new Site(1, "none", false);

	/**
	 * Means that no valid site has been set.
	 */
	public static final Site UNKNOWN = new Site(2, "unknow", false);

	public static final Site MOBILE = new Site(3, "mobile", true);

	public static final Site MOBILE_IPHONE = new Site(4, "mobile_iphone", true);

	public static final Site MOBILE_IPAD = new Site(5, "mobile_ipad", true);

	public static final Site MOBILE_ANDROID = new Site(6, "mobile_android",
			true);

	public static final Site MOBILE_ANDROID_GOOGLE = new Site(7,
			"mobile_android_google", true);

	public static final Site MOBILE_ANDROID_BAIDU = new Site(8,
			"mobile_android_baidu", true); // 百度移动应用

	public static final Site MOBILE_ANDROID_360 = new Site(9,
			"mobile_android_360", true); // 360应用中心

	public static final Site MOBILE_ANDROID_HIAPK = new Site(10,
			"mobile_android_hiapk", true); // 安卓市场

	public static final Site MOBILE_ANDROID_GFAN = new Site(11,
			"mobile_android_gfan", true); // 机锋市场

	public static final Site MOBILE_ANDROID_91 = new Site(12,
			"mobile_android_91", true); // 91手机应用

	public static final Site MOBILE_ANDROID_DINGDANG = new Site(13,
			"mobile_android_dingdang", true); // 叮当下载

	public static final Site MOBILE_ANDROID_MUMAYI = new Site(14,
			"mobile_android_mumayi", true); // 木蚂蚁

	public static final Site MOBILE_ANDROID_LIQU = new Site(15,
			"mobile_android_liqu", true); // 历趣市场

	public static final Site MOBILE_ANDROID_DOWNJOY = new Site(16,
			"mobile_android_downjoy", true); // 当乐

	public static final Site MOBILE_ANDROID_UC = new Site(17,
			"mobile_android_uc", true); // UC

	public static final Site MOBILE_ANDROID_APPCHINA = new Site(18,
			"mobile_android_appchina", true); // 应用汇

	public static final Site MOBILE_ANDROID_NDUOA = new Site(19,
			"mobile_android_nduoa", true); // N多市场

	public static final Site MOBILE_ANDROID_ANZHI = new Site(20,
			"mobile_android_anzhi", true); // 安智市场

	public static final Site MOBILE_ANDROID_WANDOUJIA = new Site(21,
			"mobile_android_wandoujia", true); // 豌豆荚

	public static final Site MOBILE_ANDROID_XDA = new Site(22,
			"mobile_android_xda", true); // XDA

	public static final Site MOBILE_ANDROID_RUAN8 = new Site(23,
			"mobile_android_ruan8", true); // 软吧

	public static final Site MOBILE_ANDROID_YINYONG = new Site(24,
			"mobile_android_yinyong", true); // 应用搜

	public static final Site MOBILE_ANDROID_EOEMARKET = new Site(25,
			"mobile_android_eoemarket", true); // 优亿市场

	public static final Site MOBILE_ANDROID_YOUMI = new Site(26,
			"mobile_android_youmi", true); // 有米

	public static final Site MOBILE_ANDROID_DOMOB = new Site(27,
			"mobile_android_domob", true); // 多盟

	public static final Site MOBILE_ANDROID_BAORUAN = new Site(28,
			"mobile_android_baoruan", true); // 宝软

	public static final Site MOBILE_ANDROID_ANSHOUJI = new Site(29,
			"mobile_android_anshouji", true); // 安机应用市场

	public static final Site MOBILE_ANDROID_ANDROIDMI = new Site(30,
			"mobile_android_androidmi", true); // 安致迷

	public static final Site MOBILE_ANDROID_155 = new Site(31,
			"mobile_android_155", true); // 手游天下

	public static final Site MOBILE_ANDROID_LEXUN = new Site(32,
			"mobile_android_lexun", true); // 乐讯

	public static final Site MOBILE_ANDROID_WAPS = new Site(33,
			"mobile_android_waps", true); // 万浦

	public static final Site MOBILE_ANDROID_SJRJY = new Site(34,
			"mobile_android_sjrjy", true); // 手机软件园

	public static final Site MOBILE_ANDROID_ANZOW = new Site(35,
			"mobile_android_anzow", true); // 安卓软件园

	public static final Site MOBILE_ANDROID_CASEE = new Site(36,
			"mobile_android_casee", true); // 架势

	public static final Site MOBILE_ANDROID_WOOBOO = new Site(37,
			"mobile_android_wooboo", true); // 哇棒

	public static final Site MOBILE_ANDROID_IANDROID = new Site(38,
			"mobile_android_iandroid", true); // 爱卓网

	public static final Site MOBILE_ANDROID_AIMI8 = new Site(39,
			"mobile_android_aimi8", true); // 爱米软件商店

	public static final Site MOBILE_ANDROID_CROSSMO = new Site(40,
			"mobile_android_crossmo", true); // 十字猫

	public static final Site MOBILE_ANDROID_APKZZ = new Site(41,
			"mobile_android_apkzz", true); // 蜘蛛电子市场

	public static final Site MOBILE_ANDROID_1000CHI = new Site(42,
			"mobile_android_1000chi", true); // 千尺下载

	public static final Site MOBILE_ANDROID_KAIQI = new Site(43,
			"mobile_android_kaiqi", true); // 开奇商店

	public static final Site MOBILE_ANDROID_TAOGAO = new Site(44,
			"mobile_android_taogao", true); // 淘告

	public static final Site MOBILE_ANDROID_DAOYOUDAO = new Site(45,
			"mobile_android_daoyoudao", true); // 道有道

	public static final Site MOBILE_ANDROID_592PPC = new Site(46,
			"mobile_android_592ppc", true); // HOT软件市场

	public static final Site MOBILE_ANDROID_YEAHWAP = new Site(47,
			"mobile_android_yeahwap", true); // 易蛙

	public static final Site MOBILE_ANDROID_ANFONE = new Site(48,
			"mobile_android_anfone", true); // 安丰下载

	public static final Site MOBILE_ANDROID_FEILIU = new Site(49,
			"mobile_android_feiliu", true); // 飞流下载

	public static final Site MOBILE_ANDROID_BOXEGG = new Site(50,
			"mobile_android_boxegg", true); // 安卓软件盒子

	public static final Site MOBILE_ANDROID_BORPOR = new Site(51,
			"mobile_android_borpor", true); // 宝瓶网

	public static final Site MOBILE_ANDROID_UUCUN = new Site(52,
			"mobile_android_uucun", true); // 悠悠村

	public static final Site MOBILE_ANDROID_92APK = new Site(53,
			"mobile_android_92apk", true); // 绿巨人安卓网

	public static final Site MOBILE_ANDROID_AZ4SD = new Site(54,
			"mobile_android_az4sd", true); // 安卓4S店

	public static final Site MOBILE_ANDROID_DX = new Site(55,
			"mobile_android_dx", true); // 点信

	public static final Site MOBILE_ANDROID_WINCLICK = new Site(56,
			"mobile_android_winclick", true); // 赢典

	public static final Site MOBILE_ANDROID_ANYOUHUI = new Site(57,
			"mobile_android_anyouhui", true); // 安有汇

	public static final Site MOBILE_ANDROID_WAPMY = new Site(58,
			"mobile_android_wapmy", true); // 哇麦

	public static final Site MOBILE_ANDROID_JUKU = new Site(59,
			"mobile_android_juku", true); // 聚酷

	public static final Site MOBILE_ANDROID_WO = new Site(60,
			"mobile_android_wo", true); // 沃门户

	public static final Site MOBILE_IOS_TAOMEE_TW = new Site(64,
			"mobile_ios_taomee_tw", true); // 淘米台湾ios

	public static final Site MOBILE_ANDROID_TAOMEE_TW = new Site(65,
			"mobile_android_taomee_tw", true); // 淘米台湾

	public static final Site MOBILE_ANDROID_TAOMEE = new Site(66,
			"mobile_android_taomee", true); // 淘米

	public static final Site MOBILE_ANDROID_ANZUOSHICHANG = new Site(67,
			"mobile_android_anzuoshichang", true);

	public static final Site MOBILE_ANDROID_BAIBAOXIANG = new Site(68,
			"mobile_android_baibaoxiang", true); // 百宝箱

	public static final Site MOBILE_ANDROID_SNDA = new Site(69,
			"mobile_android_snda", true); // 盛大

	public static final Site MOBILE_IOS_TAOMEE = new Site(70,
			"mobile_ios_taomee", true); //

	public static final Site MOBILE_ANDROID_XIAOMI = new Site(71,
			"mobile_android_xiaomi", true); // 小米

	public static final Site MOBILE_ANDROID_HUAWEI = new Site(72,
			"mobile_android_huawei", true); // 华为

	public static final Site MOBILE_ANDROID_TOM = new Site(73,
			"mobile_android_tom", true); // tom
											// pkgame

	public static final Site MOBILE_ANDROID_DRAGON_SMEET_CN = new Site(74,
			"mobile_android_dragon_smeet_cn", true); // 龙会洲台湾android

	public static final Site MOBILE_ANDROID_9ZHITX = new Site(75,
			"mobile_android_9zhitx", true); // 9zhitx

	public static final Site MOBILE_ANDROID_MZWAN = new Site(76,
			"mobile_android_mzwan", true); // 拇指玩

	public static final Site MOBILE_HILINK = new Site(88, "mobile_hilink", true); // 哈邻

	public static final Site MOBILE_ANDROID_DRAGON_SMEET_TW = new Site(95,
			"mobile_android_dragon_smeet_tw", true); // 龙会洲Android
														// 台湾繁体

	public static final Site MOBILE_IOS_DRAGON_SMEET_CN = new Site(96,
			"mobile_ios_dragon_smeet_cn", true); // 龙会洲台湾ios

	public static final Site SYSTEM_WEB = new Site(97, "SYSTEM_WEB", true);

	public static final Site MOBILE_FACEBOOK = new Site(98, "mobile_facebook",
			true);

	public static final Site MOBILE_WEIBO = new Site(99, "mobile_weibo", true);

	public static final Site MOBILE_QQ = new Site(100, "mobile_qq", true);

	public static final Site MOBILE_ANDROID_A101 = new Site(101,
			"mobile_android_meizu", true); // 魅族

	public static final Site MOBILE_ANDROID_A102 = new Site(102,
			"mobile_android_duowan", true); // 多玩手游

	public static final Site MOBILE_ANDROID_SOHU = new Site(103,
			"mobile_android_sohu", true); // 搜狐

	public static final Site MOBILE_ANDROID_A104 = new Site(104,
			"mobile_android_163sy", true); // 网易

	public static final Site MOBILE_ANDROID_A105 = new Site(105,
			"mobile_android_ewoka", true); // E蜗卡

	public static final Site MOBILE_ANDROID_A106 = new Site(106,
			"mobile_android_isy", true); // 爱手游

	public static final Site MOBILE_ANDROID_A107 = new Site(107,
			"mobile_android_bf073", true); // bufan.com(073)

	public static final Site MOBILE_ANDROID_A108 = new Site(108,
			"mobile_android_sy173", true); // 手游17173

	public static final Site MOBILE_ANDROID_A109 = new Site(109,
			"mobile_android_apk8", true); // apk8

	public static final Site MOBILE_ANDROID_A110 = new Site(110,
			"mobile_android_sina", true); // 新浪

	public static final Site MOBILE_ANDROID_A111 = new Site(111,
			"mobile_android_a111", true); //

	public static final Site MOBILE_ANDROID_A112 = new Site(112,
			"mobile_android_a112", true); //

	public static final Site MOBILE_ANDROID_A113 = new Site(113,
			"mobile_android_a113", true); // 隆宇大厦

	public static final Site MOBILE_ANDROID_A114 = new Site(114,
			"mobile_android_a114", true); // 测试

	public static final Site MOBILE_ANDROID_A115 = new Site(115,
			"mobile_android_a115", true); //

	public static final Site MOBILE_ANDROID_QXZ1 = new Site(116,
			"mobile_android_qxz1", true); // 七匣子
											// 1

	public static final Site MOBILE_ANDROID_QXZ2 = new Site(117,
			"mobile_android_qxz2", true); // 七匣子
											// 2

	public static final Site MOBILE_ANDROID_A118 = new Site(118,
			"mobile_android_tieba", true); // 贴吧

	public static final Site MOBILE_ANDROID_A119 = new Site(119,
			"mobile_android_a119", true); //

	public static final Site MOBILE_ANDROID_VIETNAM_SOHA = new Site(120,
			"mobile_android_vietnam_soha", true); // 越南soha koi.android

	public static final Site MOBILE_ANDROID_THAILAND = new Site(121,
			"mobile_android_thailand", true); // 泰国

	public static final Site MOBILE_IOS_VIETNAM_SOHA = new Site(122,
			"mobile_ios_vietnam_soha", true); // 越南soha ios

	public static final Site MOBILE_IOS_THAILAND = new Site(123,
			"mobile_ios_thailand", true); // 泰国ios

	public static final Site MOBILE_ANDROID_TURKEY = new Site(124,
			"mobile_android_turkey", true); // 土耳其android

	public static final Site MOBILE_IOS_TURKEY = new Site(125,
			"mobile_ios_turkey", true); // 土耳其ios

	public static final Site MOBILE_ANDROID_XINMA = new Site(126,
			"mobile_android_xinma", true); // 新加坡马来android

	public static final Site MOBILE_IOS_XINMA = new Site(127,
			"mobile_ios_xinma", true); // 新加坡马来ios

	public static final Site MOBILE_ANDROID_INDONESIA = new Site(128,
			"mobile_android_indonesia", true); // 印度尼西亚android

	public static final Site MOBILE_IOS_INDONESIA = new Site(129,
			"mobile_ios_indonesia", true); // 印度尼西亚ios

	public static final Site MOBILE_ANDROID_A130 = new Site(130,
			"mobile_android_a130", true); //

	public static final Site MOBILE_ANDROID_A131 = new Site(131,
			"mobile_android_a131", true); //

	public static final Site MOBILE_ANDROID_A132 = new Site(132,
			"mobile_android_a132", true); //

	public static final Site MOBILE_ANDROID_A133 = new Site(133,
			"mobile_android_a133", true); //

	public static final Site MOBILE_ANDROID_A134 = new Site(134,
			"mobile_android_a134", true); //

	public static final Site MOBILE_ANDROID_A135 = new Site(135,
			"mobile_android_a135", true); //

	public static final Site MOBILE_ANDROID_A136 = new Site(136,
			"mobile_android_a136", true); //

	public static final Site MOBILE_ANDROID_A137 = new Site(137,
			"mobile_android_a137", true); //

	public static final Site MOBILE_ANDROID_A138 = new Site(138,
			"mobile_android_a138", true); //

	public static final Site MOBILE_ANDROID_A139 = new Site(139,
			"mobile_android_a139", true); //

	public static final Site MOBILE_ANDROID_A140 = new Site(140,
			"mobile_android_a140", true); //

	public static final Site MOBILE_ANDROID_A141 = new Site(141,
			"mobile_android_a141", true); //

	public static final Site MOBILE_ANDROID_A142 = new Site(142,
			"mobile_android_a142", true); //

	public static final Site MOBILE_ANDROID_A143 = new Site(143,
			"mobile_android_a143", true); //

	public static final Site MOBILE_ANDROID_A144 = new Site(144,
			"mobile_android_a144", true); //

	public static final Site MOBILE_ANDROID_A145 = new Site(145,
			"mobile_android_a145", true); //

	public static final Site MOBILE_ANDROID_A146 = new Site(146,
			"mobile_android_a146", true); //

	public static final Site MOBILE_ANDROID_A147 = new Site(147,
			"mobile_android_a147", true); //

	public static final Site MOBILE_ANDROID_A148 = new Site(148,
			"mobile_android_a148", true); //

	public static final Site MOBILE_ANDROID_A149 = new Site(149,
			"mobile_android_a149", true); // 百度贴吧

	public static final Site MOBILE_ANDROID_A150 = new Site(150,
			"mobile_android_a150", true); // 琵琶网

	public static final Site MOBILE_IOS_HILINK = new Site(200,
			"mobile_ios_hilink", true);

	public static final Site MOBILE_IOS_91 = new Site(201, "mobile_ios_91",
			true);

	public static final Site MOBILE_IOS_XIAOMI = new Site(202,
			"mobile_ios_xiaomi", true);

	public static final Site MOBILE_IOS_BAIDU = new Site(203,
			"mobile_ios_baidu", true); // 百度移动应用

	public static final Site MOBILE_IOS_360 = new Site(204, "mobile_ios_360",
			true); // 360应用中心

	public static final Site MOBILE_IOS_GFAN = new Site(205, "mobile_ios_gfan",
			true);

	public static final Site MOBILE_IOS_WANDOUJIA = new Site(206,
			"mobile_ios_wandoujia", true);

	public static final Site MOBILE_IOS_PP = new Site(207, "mobile_ios_pp",
			true); // pp助手

	public static final Site MOBILE_IOS_TONGBU = new Site(208,
			"mobile_ios_tongbu", true); // 同步推

	public static final Site MOBILE_IOS_SOHU = new Site(209, "mobile_ios_sohu",
			true); // sohu

	public static final Site MOBILE_IOS_APPSTORE = new Site(210,
			"mobile_ios_appstore", true); // ios
											// app
											// store

	public static final Site MOBILE_IOS_KUAIYONG = new Site(211,
			"mobile_ios_kuaiyong", true); // 快用

	public static final Site MOBILE_ANDROID_OPPO = new Site(220,
			"mobile_android_oppo", true); // oppo

	public static final Site MOBILE_ANDROID_LENOVO = new Site(221,
			"mobile_android_lenovo", true); // 联想

	public static final Site MOBILE_ANDROID_LEGAME = new Site(222,
			"mobile_android_legame", true); // legame

	public static final Site MOBILE_ANDROID_PPS = new Site(300,
			"mobile_android_pps", true); // pps

	public static final Site MOBILE_ANDROID_EONEGAME = new Site(301,
			"mobile_android_eonegame", true); // 益玩

	public static final Site MOBILE_ANDROID_VIVO = new Site(302,
			"mobile_android_vivo", true); // 步步高vivo

	public static final Site MOBILE_IOS_ITOOLS = new Site(303,
			"mobile_ios_itools", true); // itools（iOS）

	public static final Site MOBILE_ANDROID_MM = new Site(304,
			"mobile_android_mm", true); // MM平台
	public static final Site MOBILE_ANDROID_WOSTORE = new Site(305,
			"mobile_android_wostore", true); // 沃商店
	public static final Site MOBILE_ANDROID_SOGOU = new Site(306,
			"mobile_android_sogou", true); // 搜狗
	public static final Site MOBILE_ANDROID_CUCC = new Site(307,
			"mobile_android_cucc", true); // 联通宽带
	public static final Site MOBILE_ANDROID_4399 = new Site(308,
			"mobile_android_4399", true); // 4399
	public static final Site MOBILE_ANDROID_ZHIDIAN = new Site(309,
			"mobile_android_zhidian", true); // 指点
	public static final Site MOBILE_ANDROID_YOUKU = new Site(310,
			"mobile_android_youku", true); // 优酷
	public static final Site MOBILE_IOS_HAIMA = new Site(311,
			"mobile_ios_haima", true); // 海马
	public static final Site MOBILE_IOS_I4 = new Site(312, "mobile_ios_i4",
			true); // 爱思d
	public static final Site MOBILE_IOS_XYZS = new Site(313, "mobile_ios_xyzs",
			true); // XY助手
	public static final Site MOBILE_ANDROID_TIANYI = new Site(314,
			"mobile_android_tianyi", true); // 天翼空间
	public static final Site MOBILE_ANDROID_WUKONG = new Site(315,
			"mobile_android_wukong", true); // 悟空
	public static final Site MOBILE_ANDROID_SAMSUNG = new Site(316,
			"mobile_android_samsung", true); // 三星
	public static final Site MOBILE_ANDROID_GIONEE = new Site(317,
			"mobile_android_gionee", true); // 金立
	public static final Site MOBILE_ANDROID_HTC = new Site(318,
			"mobile_android_htc", true); // HTC
	public static final Site MOBILE_ANDROID_COOLPAD = new Site(319,
			"mobile_android_coolpad", true); // 酷派
	public static final Site MOBILE_ANDROID_TYD = new Site(320,
			"mobile_android_tyd", true); // 天奕达
	public static final Site MOBILE_ANDROID_ZTE = new Site(321,
			"mobile_android_zte", true); // 中兴
	public static final Site MOBILE_ANDROID_YXDOWN = new Site(322,
			"mobile_android_yxdown", true); // 游戏基地
	public static final Site MOBILE_ANDROID_KUGOU = new Site(324,
			"mobile_android_kugou", true); // 酷狗
	public static final Site MOBILE_ANDROID_PPTV = new Site(325,
			"mobile_android_pptv", true); // PPTV
	public static final Site MOBILE_HILINK_ANDROID_7XZ = new Site(326,
			"mobile_hilink_android_7xz", true); // cps 七匣子
	public static final Site MOBILE_HILINK_ANDROID_EOEMARKET = new Site(327,
			"mobile_hilink_android_eoemarket", true); // cps 优亿市场
	public static final Site MOBILE_HILINK_ANDROID_PTBUS = new Site(328,
			"mobile_hilink_android_ptbus", true); // cps 口袋巴士
	public static final Site MOBILE_HILINK_IOS_PTBUS = new Site(329,
			"mobile_hilink_ios_ptbus", true); // cps 口袋巴士 IOS
	public static final Site MOBILE_HILINK_ANDROID_LEWAOS = new Site(330,
			"mobile_hilink_android_lewaos", true); // cps 乐蛙
	public static final Site MOBILE_HILINK_IOS_APP111 = new Site(331,
			"mobile_hilink_ios_app111", true); // cps 苹果园
	public static final Site MOBILE_HILINK_KSOFT = new Site(332,
			"mobile_android_ksoft", true); // 金山
	public static final Site MOBILE_ANDROID_GUMP_TW = new Site(333,
			"mobile_android_gump_tw", true); // 海外
	public static final Site MOBILE_ANDROID_GUMP_TL = new Site(334,
			"mobile_android_gump_tl", true); // 海外
	public static final Site MOBILE_ANDROID_TENCENT = new Site(335,
			"mobile_android_tencent", true); // 腾讯
	public static final Site MOBILE_ANDROID_CHAIMI = new Site(336,
			"mobile_android_chaimi", true); // 柴米
	public static final Site MOBILE_ANDROID_CANDY = new Site(337,
			"mobile_android_candy", true); // 糖果
	public static final Site MOBILE_ANDROID_SNAIL = new Site(338,
			"mobile_android_snail", true); // 蝸牛
	public static final Site MOBILE_ANDROID_BAOFENG = new Site(339,
			"mobile_android_baofeng", true); // 暴风影音
	public static final Site MOBILE_ANDROID_TENCENT_QQ = new Site(340,
			"mobile_android_tencent_qq", true);
	public static final Site MOBILE_ANDROID_TENCENT_WECHAT = new Site(341,
			"mobile_android_tencent_wechat", true);
	public static final Site MOBILE_ANDROID_HILINK_ANFAN = new Site(342,
			"mobile_android_hilink_anfan", true); // cps 安锋网
	public static final Site MOBILE_ANDROID_HILINK_MKZOO = new Site(343,
			"mobile_android_hilink_mkzoo", true); // cps 游乐猿
	public static final Site MOBILE_ANDROID_HILINK_XIAOT = new Site(344,
			"mobile_android_hilink_xiaot", true); // cps xiaot koi.android
	public static final Site MOBILE_IOS_HILINK_XIAOT = new Site(345,
			"mobile_ios_hilink_xiaot", true); // cps xiaot ios
	public static final Site MOBILE_ANDROID_HILINK_78 = new Site(346,
			"mobile_android_hilink_78", true); // cps 78 koi.android
	public static final Site MOBILE_IOS_HILINK_78 = new Site(347,
			"mobile_ios_hilink_78", true); // cps 78 ios
	public static final Site MOBILE_ANDROID_HILINK_COLG = new Site(348,
			"mobile_android_hilink_colg", true); // cps colg koi.android
	public static final Site MOBILE_IOS_HILINK_COLG = new Site(349,
			"mobile_ios_hilink_colg", true); // cps colg ios
	public static final Site MOBILE_ANDROID_HILINK_7K7K = new Site(350,
			"mobile_android_hilink_7k7k", true); // cps 阿毛动漫 koi.android
	public static final Site MOBILE_IOS_HILINK_7K7K = new Site(351,
			"mobile_ios_hilink_7k7k", true); // cps 阿毛动漫 ios
	public static final Site MOBILE_ANDROID_HILINK_MQMYD = new Site(352,
			"mobile_android_hilink_mqmyd", true); // cps 趣妙多母婴专营店 koi.android
	public static final Site MOBILE_IOS_HILINK_MQMYD = new Site(353,
			"mobile_ios_hilink_mqmyd", true); // cps 趣妙多母婴专营店 ios
	public static final Site MOBILE_ANDROID_JINSHANYUN = new Site(354,
			"mobile_android_jinshanyun", true);
	public static final Site MOBILE_IOS_HILINK_CLICKALOT = new Site(355,
			"mobile_android_clickalot", true);// clickalot
	public static final Site MOBILE_ANDROID_MKZOO = new Site(346,
			"mobile_android_mkzoo", true); //游乐猿
	public static final Site MOBILE_ANDROID_ANFENG = new Site(347,
			"mobile_android_anfeng", true); //安峰

	/**
	 * Return the Site associated with a string. If the string is bad, a null is
	 * returned.
	 */
	public static Site getByString(String sval) {
		return (Site) m_byString.get(sval);
	}

	/**
	 * Return the Site associated with an int code. If the code is bad, a null
	 * is returned.
	 */
	public static Site getByInt(int ival) {
		if (0 <= ival && ival < m_byInt.length) {
			return (m_byInt[ival]);
		}

		return null;
	}

	/**
	 * Returns an iterator over the list of sites
	 */
	public static Iterator iterator() {
		return m_byString.values().iterator();
	}

	/**
	 * NEVER USE THIS constructor. Unfortunately we need it in order to
	 * implement Externalizable.
	 */
	public Site() {
	}

	private Site(int code, String name, boolean isExternal) {
		this.code = code;
		this.name = name;
		external = isExternal;

		m_byString.put(name, this);
		m_byInt[code] = this;
	}

	/**
	 * contructor used to sunset a site
	 * 
	 * @param code
	 *            - id for the site
	 * @param name
	 *            - the original site code
	 * @param isExternal
	 * @param srep
	 *            - the replacement site code
	 */
	private Site(int code, String name, boolean isExternal, String srep) {
		this.code = code;
		this.name = name;
		external = isExternal;

		m_byString.put(name, this);
		m_byInt[code] = this;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean isExternal() {
		return external;
	}

	public void setExternal(boolean external) {
		this.external = external;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static boolean isMobile(Site site) {
		if (site == null) {
			return false;
		}
		return StringUtils.contains(site.getName(), "mobile");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Site site = (Site) o;

		if (code != site.code)
			return false;
		if (external != site.external)
			return false;
		if (name != null ? !name.equals(site.name) : site.name != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = code;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (external ? 1 : 0);
		return result;
	}

	@Override
	public String toString() {
		return name;
	}
}
