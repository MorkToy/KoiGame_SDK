
package koigame.sdk.bean.user;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class KAuthValueType implements Serializable {
    //
    private static Map<Integer, KAuthValueType> idMaps = new HashMap<Integer, KAuthValueType>();

    private static Map<String, KAuthValueType> codeMaps = new HashMap<String, KAuthValueType>();

    //
    public static KAuthValueType DEVICE_SERIAL_KEY = new KAuthValueType(1, "device.serial.key",
            "游客", false);

    public static KAuthValueType LOGINNAME = new KAuthValueType(2, "loginname", "哈邻用户", true);

    public static KAuthValueType EMAIL = new KAuthValueType(3, "email", "邮箱用户", true);

    public static KAuthValueType MOBILE = new KAuthValueType(4, "mobile", "手机用户", true);

    //
    public static KAuthValueType THIRD_OAUTH_FB = new KAuthValueType(10, "oauth.fb", "Facebook用户",
            false);

    public static KAuthValueType THIRD_OAUTH_QQ = new KAuthValueType(11, "oauth.qq", "QQ用户", false);

    public static KAuthValueType THIRD_OAUTH_WEIBO = new KAuthValueType(12, "oauth.weibo", "微博用户",
            false);

    public static KAuthValueType THIRD_OAUTH_RR = new KAuthValueType(13, "oauth.rr", "人人用户", false);

    public static KAuthValueType THIRD_OAUTH_360 = new KAuthValueType(14, "oauth.360", "360用户",
            false);

    public static KAuthValueType THIRD_OAUTH_DUOKU = new KAuthValueType(15, "oauth.baidu", "多酷用户",
            false);

    public static KAuthValueType THIRD_OAUTH_HUAWEI = new KAuthValueType(16, "oauth.huawei",
            "华为用户", false);

    public static KAuthValueType THIRD_OAUTH_91 = new KAuthValueType(17, "oauth.91", "91用户", false);

    public static KAuthValueType THIRD_OAUTH_XIAOMI = new KAuthValueType(18, "oauth.xiaomi",
            "小米用户", false);

    public static KAuthValueType THIRD_OAUTH_DOWNJOY = new KAuthValueType(19, "oauth.downjoy",
            "当乐用户", false);

    public static KAuthValueType THIRD_OAUTH_GFAN = new KAuthValueType(20, "oauth.gfan", "机锋用户",
            false);

    public static KAuthValueType THIRD_OAUTH_TOM = new KAuthValueType(21, "oauth.tom", "Tom用户",
            false);

    public static KAuthValueType THIRD_OAUTH_UC = new KAuthValueType(22, "oauth.uc", "UC用户", false);

    public static KAuthValueType THIRD_OAUTH_WANDOUJIA = new KAuthValueType(23, "oauth.wandoujia",
            "豌豆荚用户", false);

    public static KAuthValueType THIRD_OAUTH_SOHU = new KAuthValueType(24, "oauth.sohu", "畅游用户",
            false);

    public static KAuthValueType THIRD_OAUTH_APPLE_GAME_CENTER = new KAuthValueType(25,
            "oauth.apple.gamecenter", "Apple用户", false);

    public static KAuthValueType THIRD_OAUTH_PP = new KAuthValueType(26, "oauth.pp", "pp助手用户",
            false);

    public static KAuthValueType THIRD_OAUTH_TONGBU = new KAuthValueType(27, "oauth.tongbu",
            "同步推用户", false);

    public static KAuthValueType THIRD_OAUTH_KUAIYONG = new KAuthValueType(28, "oauth.kuaiyong",
            "快用用户", false);

    public static KAuthValueType THIRD_OAUTH_VIETNAM_SOHA = new KAuthValueType(30,
            "oauth.vietnam.soha", "Soha用户", false);

    public static KAuthValueType THIRD_OAUTH_THAILAND = new KAuthValueType(31, "oauth.thailand",
            "Thailand用户", false);

    public static KAuthValueType THIRD_OAUTH_TURKEY = new KAuthValueType(32, "oauth.turkey",
            "Turkey用户", false);

    public static KAuthValueType THIRD_OAUTH_OPPO = new KAuthValueType(33, "oauth.oppo", "OPPO用户",
            false);

    public static KAuthValueType THIRD_OAUTH_LENOVO = new KAuthValueType(34, "oauth.lenovo",
            "联想用户", false);

    public static KAuthValueType THIRD_OAUTH_LEGAME = new KAuthValueType(35, "oauth.legame",
            "legame", false);
    
    public static KAuthValueType THIRD_OAUTH_ANZHI = new KAuthValueType(36, "oauth.anzhi", "anzhi", false);

    public static KAuthValueType THIRD_OAUTH_WECHAT_TOD = new KAuthValueType(101,
            "oauth.wechat.tod", "龙之轨迹微信用户", false);
    
    public static KAuthValueType THIRD_OAUTH_PPS = new KAuthValueType(201, "oauth.pps", "pps用户", false);
    
    public static KAuthValueType THIRD_OAUTH_MEIZU = new KAuthValueType(202, "oauth.meizu", "魅族用户", false);
    public static KAuthValueType THIRD_OAUTH_DX = new KAuthValueType(203, "oauth.dx", "电信爱游戏用户",  false);
    public static KAuthValueType THIRD_OAUTH_EONEGAME = new KAuthValueType(204, "oauth.eonegame", "益玩用户", false);
    public static KAuthValueType THIRD_OAUTH_VIVO = new KAuthValueType(205, "oauth.vivo", "步步高VIVO用户",  false);
    public static KAuthValueType THIRD_OAUTH_ITOOLS = new KAuthValueType(206, "oauth.itools", "itools用户", false);
    public static KAuthValueType THIRD_OAUTH_MM = new KAuthValueType(207, "oauth.mm", "MM平台用户", false);
    
    public static KAuthValueType THIRD_OAUTH_WOSTORE = new KAuthValueType(208, "oauth.wostore", "沃商店用户", false);
    public static KAuthValueType THIRD_OAUTH_MZWAN  = new KAuthValueType(209, "oauth.mzwan", "拇指玩用户", false);
    public static KAuthValueType THIRD_OAUTH_SOGOU  = new KAuthValueType(210, "oauth.sogou", "搜狗用户", false);
    public static KAuthValueType THIRD_OAUTH_YOUMI = new KAuthValueType(211, "oauth.youmi", "有米用户", false);
    public static KAuthValueType THIRD_OAUTH_WAPS = new KAuthValueType(212, "oauth.waps", "万普用户", false);
    public static KAuthValueType THIRD_OAUTH_CUCC = new KAuthValueType(213, "oauth.cucc", "联通宽带用户", false);
    public static KAuthValueType THIRD_OAUTH_4399 = new KAuthValueType(214, "oauth.4399", "4399用户", false);
    public static KAuthValueType THIRD_OAUTH_APPCHINA = new KAuthValueType(215, "oauth.appchina", "应用汇用户", false);
    public static KAuthValueType THIRD_OAUTH_ZHIDIAN = new KAuthValueType(216, "oauth.zhidian", "指点用户", false);
    public static KAuthValueType THIRD_OAUTH_NDUOA = new KAuthValueType(217, "oauth.nduoa", "N多市场用户", false);
    public static KAuthValueType THIRD_OAUTH_MUMAYI = new KAuthValueType(218, "oauth.mumayi", "木蚂蚁用户", false);
    public static KAuthValueType THIRD_OAUTH_YOUKU = new KAuthValueType(219, "oauth.youku", "优酷用户", false);
    public static KAuthValueType THIRD_OAUTH_HAIMA = new KAuthValueType(220, "oauth.haima", "海马用户", false);
    public static KAuthValueType THIRD_OAUTH_I4 = new KAuthValueType(221, "oauth.i4", "爱思用户", false);
    public static KAuthValueType THIRD_OAUTH_XYZS = new KAuthValueType(222, "oauth.xyzs", "XY助手用户", false);
    public static KAuthValueType THIRD_OAUTH_TIANYI = new KAuthValueType(223, "oauth.tianyi", "天翼空间用户", false);
    public static KAuthValueType THIRD_OAUTH_WUKONG = new KAuthValueType(224, "oauth.wukong", "悟空用户", false);
    public static KAuthValueType THIRD_OAUTH_SAMSUNG = new KAuthValueType(225, "oauth.samsung", "三星用户", false);
    public static KAuthValueType THIRD_OAUTH_GIONEE = new KAuthValueType(226, "oauth.gionee", "金立用户", false);
    public static KAuthValueType THIRD_OAUTH_HTC = new KAuthValueType(227, "oauth.htc", "HTC用户", false);
    public static KAuthValueType THIRD_OAUTH_COOLPAD = new KAuthValueType(228, "oauth.coolpad", "酷派用户", false);
    public static KAuthValueType THIRD_OAUTH_TYD = new KAuthValueType(229, "oauth.tyd", "天奕达用户", false);
    public static KAuthValueType THIRD_OAUTH_ZTE = new KAuthValueType(230, "oauth.zte", "中兴用户", false);
    public static KAuthValueType THIRD_OAUTH_YXDOWN = new KAuthValueType(231, "oauth.yxdown", "游戏基地用户", false);
    public static KAuthValueType THIRD_OAUTH_KUGOU = new KAuthValueType(232, "oauth.kugou", "酷狗", false);
    public static KAuthValueType THIRD_OAUTH_KSOFT = new KAuthValueType(233, "oauth.ksoft", "金山", false);
    public static KAuthValueType THIRD_OAUTH_GUMP = new KAuthValueType(234, "oauth.gump", "海外用户", false);
    public static KAuthValueType THIRD_OAUTH_TENCENT = new KAuthValueType(235, "oauth.tenent", "腾讯用户", false);
    public static KAuthValueType THIRD_OAUTH_CHAIMI = new KAuthValueType(236, "oauth.chaimi", "柴米用户", false);
    public static KAuthValueType THIRD_OAUTH_CANDY = new KAuthValueType(237, "oauth.candy", "糖果用户", false);
    public static KAuthValueType THIRD_OAUTH_SNAIL = new KAuthValueType(238, "oauth.snail", "蝸牛用户", false);
    public static KAuthValueType THIRD_OAUTH_BAOFENG    = new KAuthValueType(239, "oauth.baofeng", "暴风影音", false);
    public static KAuthValueType THIRD_OAUTH_UUCUN  = new KAuthValueType(240, "oauth.uucun", "UU村", false);
    public static KAuthValueType THIRD_OAUTH_GOOGLE  = new KAuthValueType(241, "oauth.google", "谷歌", false);
    public static KAuthValueType THIRD_OAUTH_FUJIA  = new KAuthValueType(242, "oauth.fujia", "富佳", false);
    public static KAuthValueType THIRD_OAUTH_JINSHANYUN  = new KAuthValueType(243, "oauth.jinshanyun", "金山云", false);
    public static KAuthValueType THIRD_OAUTH_CLICKALOT  = new KAuthValueType(244, "oauth.clickalot", "clickalot", false);
    public static KAuthValueType THIRD_OAUTH_MKZOO  = new KAuthValueType(245, "oauth.mkzoo", "mkzoo", false);
    public static KAuthValueType THIRD_OAUTH_ANFENG  = new KAuthValueType(246, "oauth.anfeng", "安峰", false);
    public static KAuthValueType THIRD_OAUTH_JOY = new KAuthValueType(267, "oauth.joy", "卓游", false);
    
    //
    private int id;

    private String code;

    private String name;

    private boolean needPwd;

    //
    private KAuthValueType(int id, String code, String name, boolean needPwd) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.needPwd = needPwd;

        //
        idMaps.put(this.id, this);
        codeMaps.put(this.code, this);
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

    public boolean isNeedPwd() {
        return needPwd;
    }

    public static KAuthValueType getById(int id) {
        return idMaps.get(id);
    }

    public static KAuthValueType getByCode(String code) {
        return codeMaps.get(code);
    }

    public int hashCode() {
        return id;
    }

    public String toString() {
        return "AuthValueType: id=[" + id + "], code=[" + code + "].";
    }

    public boolean equals(Object obj) {
        return obj != null && obj instanceof KAuthValueType && id == ((KAuthValueType) obj).getId();
    }
}
