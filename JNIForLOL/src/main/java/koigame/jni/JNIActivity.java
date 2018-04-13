package koigame.jni;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.PopupWindow;

import org.json.JSONObject;

import java.lang.reflect.Method;

import org.cocos2dx.lib.Cocos2dxActivity;

public class JNIActivity extends Cocos2dxActivity {

    public static final String TAG = "JNIActivity";

    public String staticLibPath = "";
    // public String staticLibDir = "";

    public static String GameSource;
    public static String Channel;
    public static String AppKey;
    public static String PlatHostName;
    public static String SDKVersion;
    public static String ResPath;
    public static String APKVersion;
    public static String ResVersion;
    public static String AreaInfos;
    public static String UserInfos;
    public static String InitConfig;
    public static String localeInfo;
    public static String SysInfos;
    public static String WorldAnnounce;
    public static String ChargeMoney;

    private static PopupWindow popupWindow;
    private static PowerManager.WakeLock wakeLock;
    private boolean isLoad;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        String packageName = mainActivity.getApplication().getPackageName();
        GameSource = savedInstanceState.getString("source");
        Channel = savedInstanceState.getString("channel");
        AppKey = savedInstanceState.getString("appkey");
        PlatHostName = savedInstanceState.getString("platHost");
        ResPath = savedInstanceState.getString("workDir");
        APKVersion = savedInstanceState.getString("apkVerionName");
        ResVersion = savedInstanceState.getString("gameVersion");
        SDKVersion = savedInstanceState.getString("sdkVersion");
        AreaInfos = savedInstanceState.getString("gameAreas");
        UserInfos = savedInstanceState.getString("userInfos");
        InitConfig = savedInstanceState.getString("initConfig");
        SysInfos = savedInstanceState.getString("sysInfos");
        localeInfo = mainActivity.getResources().getConfiguration().locale
                .toString();
        WorldAnnounce = savedInstanceState.getString("worldAnnounce");
        ChargeMoney = savedInstanceState.getString("chargeMoney");

        Log.i(TAG, "GameSource=" + GameSource + ",PlatHostName=" + PlatHostName
                + ",ResPath=" + ResPath);

        PackageManager packMgr = mainActivity.getApplication()
                .getPackageManager();
        Log.i(TAG, mainActivity.getApplicationInfo().packageName);
        ApplicationInfo info = null;
        try {
            info = packMgr
                    .getApplicationInfo(packageName, MODE_WORLD_WRITEABLE);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        staticLibPath = info.dataDir + "/runlib/libgame.so";
        info = null;
        if (!isLoad) {
            System.load(staticLibPath);
            super.onCreate(savedInstanceState);
        }

        Log.d(TAG, "onCreatefinish");
        isLoad = true;
    }

    public void setActivity(Activity paramActivity) {
        mainActivity = paramActivity;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        if (wakeLock != null) {
            wakeLock.release();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        if (wakeLock != null) {
            wakeLock.acquire();
        }
        super.onResume();
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        Log.d(TAG, "onStart");
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        Log.d(TAG, "onStop");
    }

    @Override
    public void onRestart() {
        // TODO Auto-generated method stub
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.d(TAG, "onDestroy");
    }

    /**
     * ��峰��娓告��娓����
     *
     * @return
     */
    public static String getGameSource() {
        return GameSource;
    }

    /**
     * ��峰��娓告��channel
     *
     * @return
     */
    public static String getChannel() {
        return Channel;
    }

    /**
     * ��峰��appKey
     *
     * @return
     */
    public static String getAppKey() {
        return AppKey;
    }

    /**
     * ��峰��骞冲�拌�块����板��
     *
     * @return
     */
    public static String getPlatHostName() {
        return PlatHostName;
    }

    /**
     * ��峰��娓告��璧�婧�璺�寰�
     *
     * @return
     */
    public static String getResPath() {
        Log.d(TAG, "normalMethod");
        return ResPath;
    }

    public static String getAppVersion() {
        Log.d(TAG, "normalMethod");
        return APKVersion;
    }

    public static String getResVersion() {
        Log.d(TAG, "normalMethod");
        return ResVersion;
    }

    public static String getSDKVersion() {

        Log.e(TAG, SDKVersion);
        return SDKVersion == null ? "" : SDKVersion;
    }

    public static String getGameAreas() {
        Log.d(TAG, "normalMethod");
        return AreaInfos;
    }

    public static String getUserInfos() {
        Log.d(TAG, "normalMethod");
        return UserInfos;
    }

    public static String getInitConfig() {
        Log.d(TAG, "normalMethod");
        return InitConfig;
    }

    public static String getLocaleInfo() {
        Log.d(TAG, "normalMethod");
        return localeInfo;
    }

    public static String getSystemInfos() {
        return SysInfos;
    }

    public static String getWorldAnnounce() {
        return WorldAnnounce;
    }

    public static String getChargeMoney() {
        return ChargeMoney;
    }

    /**
     * 娓告��璋����
     *
     * @param eventType
     * @param paramCode
     */
    public static String gameCall(int eventType, String paramCode) {
        Log.e(TAG, "gameCall eventType " + eventType + ", paramCode"
                + paramCode);
        try {
            JSONObject props = new JSONObject();
            int level = 0;
            int itemCount = 0;
            String itemId = null;
            String itemName = null;
            String areaName = null;
            String roleName = null;
            int roleId = 0;
            switch (eventType) {
                case 1://玩家选完区服后调用
                    props = JSONUtils.build(paramCode);
                    int areaId = JSONUtils.getInt(props, "areaId", 1);
                    areaName = JSONUtils.getString(props, "areaName");
                    onAreaSelected(areaId, areaName);
                    break;
                case 2://玩家输入完角色名后通知平台
                    props = JSONUtils.build(paramCode);
                    roleName = JSONUtils.getString(props, "roleName");
                    checkRoleName(roleName);
                    break;
                case 3://分享，(可选)
                    props = JSONUtils.build(paramCode);
                    int shareType = JSONUtils.getInt(props, "shareType");
                    String picture = JSONUtils.getString(props, "picture");
                    String url = JSONUtils.getString(props, "url");
                    String title = JSONUtils.getString(props, "title");
                    String content = JSONUtils.getString(props, "content");

                    onShare(picture, url, title, content, shareType);
                    break;
                case 4:
                    onPromoteCode(1);
                    break;
                case 5://打开用户中心(可选)
                    onOpenMemberCenter();
                    break;
                case 6://支付
                    onPay(paramCode);
                    break;
                case 7:
                    onPayFinished();
                    break;
                case 8://玩家退出游戏的时候调用
                    onExit();
                    break;
                case 9://打开网页,比如公告(可选)
                    showWebView(paramCode);
                    break;
                case 10://关闭网页(可选)
                    closeWebView();
                    break;
                case 11://判断玩家是否连接wifi(可选)
                    isWifiConnected();
                    break;
                case 12://公告(可选)
                    showAnnounce(paramCode);
                    break;
                case 13://崩溃日志记录(可选)
                    onCrash(paramCode);
                    break;
                case 14:
                    noticeAvailMem();
                    break;
                case 15://自定义接口，直接连平台服务器(可选)
                    props = JSONUtils.build(paramCode);
                    String action = JSONUtils.getString(props, "action");
                    serverPipeline(action);
                    break;
                case 16://切换账号(可选)
                    onSwitchAccount();
                    break;
                case 17://玩家角色升级调用(可选)
                    props = JSONUtils.build(paramCode);
                    level = JSONUtils.getInt(props, "level");
                    onLevelUp(level);
                    break;
                case 18://玩家购买游戏内商品(可选)
                    props = JSONUtils.build(paramCode);
                    String item = JSONUtils.getString(props, "item");
                    int itemNumber = JSONUtils.getInt(props, "itemNumber");
                    Double priceInVirtualCurrency = JSONUtils.getDouble(props, "priceInVirtualCurrency");
                    onPurchase(item, itemNumber, priceInVirtualCurrency);
                    break;
                case 19://(可选)
                    props = JSONUtils.build(paramCode);
                    double virtualCurrencyAmount = JSONUtils.getDouble(props, "virtualCurrencyAmount");
                    String reason = JSONUtils.getString(props, "reason");
                    onReward(virtualCurrencyAmount, reason);
                    break;
                case 20://获取商品列表
                    getProductConfigs();
                    break;
                case 21://进入到游戏主界面的时候，调用
                    props = JSONUtils.build(paramCode);
                    int actionId = JSONUtils.getInt(props, "actionId");
                    level = JSONUtils.getInt(props, "roleLevel");
                    roleId = JSONUtils.getInt(props, "roleId");
                    roleName = JSONUtils.getString(props, "roleName");
                    int moneyNum = JSONUtils.getInt(props, "moneyNum");
                    int vipLevel = JSONUtils.getInt(props, "vipLevel");
                    int gem = JSONUtils.getInt(props, "gem");
                    Log.i("jni", props.toString());
                    onEnterGame(actionId, level, roleId, roleName, moneyNum, vipLevel, gem);
                    break;
                case 22://(可选)
                    props = JSONUtils.build(paramCode);
                    int act = JSONUtils.getInt(props, "action");
                    String missionId = JSONUtils.getString(props, "missionId");
                    String cause = JSONUtils.getString(props, "cause");
                    duplicateInfo(act, missionId, cause);
                    break;
                case 23://让手机屏幕不熄灯(可选)
                    lightScreenON();
                    break;
                case 24://(可选)
                    lightScreenOFF();
                    break;
                case 25://重启游戏(可选)
                    restartApp();
                    break;
                case 26://播放CG视频(可选)
                    props = JSONUtils.build(paramCode);
                    boolean isPlay = JSONUtils.getBoolean(props, "isPlay");
                    String path = JSONUtils.getString(props, "path");
                    boolean isHalfPlay = JSONUtils.getBoolean(props, "isHalfPlay");
                    openVideo(isPlay, path, isHalfPlay);
                    break;
                case 27://三方统计埋点事件(可选)
                    props = JSONUtils.build(paramCode);
                    String event = JSONUtils.getString(props, "Event");
                    String value = JSONUtils.getString(props, "eventData");
                    talkingEvent(event, value);
                    break;
                case 28://修改头像(可选)
                    props = JSONUtils.build(paramCode);
                    String roleHead = JSONUtils.getString(props, "roleHead");
                    changeRoleHead(roleHead);
                    break;
                case 29://(可选)
                    props = JSONUtils.build(paramCode);
                    String webUrl = JSONUtils.getString(props, "webUrl");
                    JumpToWeb(webUrl);
                    break;

                case 30://内测返利(可选)
                    props = JSONUtils.build(paramCode);
                    String role = JSONUtils.getString(props, "roleId");
                    OBDoubleMoney(role);
                    break;

                case 31://(忽略)
                    tencentCommunity();
                    break;

                case 100://获取渠道名称
                    return getGameSource();

                case 101://获取渠道名称(可选)
                    return getChannel();

                case 102:
                    return getAppKey();

                case 103://获取平台域名(可选)
                    return getPlatHostName();

                case 104://获取补丁资源目录
                    return getResPath();

                case 105://获取平台大版本号
                    return getAppVersion();

                case 106://获取补丁版本号
                    return getResVersion();

                case 107://获取SDK版本号(可选)
                    return getSDKVersion();

                case 108://获取区服信息
                    return getGameAreas();

                case 109://获取用户信息
                    return getUserInfos();

                case 110://可选
                    return getInitConfig();

                case 111://可选
                    return getLocaleInfo();

                case 112://获取手机设备信息(可选)
                    return getSystemInfos();

                case 113://获取公告内容
                    return getWorldAnnounce();

                case 114://忽略
                    return  "0";

                case 115://玩家封测期间充值的金额，首冲双倍用(可选)
                    return getChargeMoney();

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "no params return";
    }


    // --------------------------------------------------------------------------------分割線------------------------------------------------------------------------------------------/
    public static void openAlbum() {
        Log.i("photo", "jni打开相册");
        platfromCallBack("openAlbum", new Bundle());
    }

    /**
     * 重启应用
     */
    public static void restartApp() {
        Bundle paramBundle = new Bundle();
        platfromCallBack("restartApp", paramBundle);
    }

    /**
     * �����哄�����璋�
     *
     * @param areaId
     */
    public static void onAreaSelected(int areaId, String areaName) {
        Bundle paramBundle = new Bundle();
        paramBundle.putInt("areaId", areaId);
        paramBundle.putString("areaName", areaName);
        platfromCallBack("onAreaSelected", paramBundle);

    }

    /**
     * 妫���ユ�电О
     *
     * @param roleName
     */
    public static void checkRoleName(String roleName) {
        Bundle paramBundle = new Bundle();
        paramBundle.putString("roleName", roleName);
        platfromCallBack("checkRoleName", paramBundle);
    }

    /**
     * ��剧ず������
     *
     * @param paramCode
     */
    public static void showAnnounce(String paramCode) {
        Bundle paramBundle = new Bundle();
        paramBundle.putString("paramCode", paramCode);
        platfromCallBack("showAnnounce", paramBundle);
    }

    public static void duplicateInfo(int action, String missionId, String cause) {
        Log.i(TAG, "duplicateInfo");
        Bundle paramBundle = new Bundle();
        paramBundle.putInt("action", action);
        paramBundle.putString("missionId", missionId);
        paramBundle.putString(cause, cause);
        platfromCallBack("duplicateInfo", paramBundle);
    }

    public static void talkingEvent(String event, String value) {
        Log.i(TAG, "talkingEvent");
        Bundle paramBundle = new Bundle();
        paramBundle.putString("event", event);
        paramBundle.putString("eventData", value);
        platfromCallBack("talkingEvent", paramBundle);
    }

    public static void changeRoleHead(String roleHead) {
        Bundle paramBundle = new Bundle();
        paramBundle.putString("roleHead", roleHead);
        platfromCallBack("changeRoleHead", paramBundle);
    }

    public static void JumpToWeb(String webUrl) {
        Bundle paramBundle = new Bundle();
        paramBundle.putString("webUrl", webUrl);
        platfromCallBack("JumpToWeb", paramBundle);
    }

    /**
     * ��剧ず缃�椤�
     *
     * @param paramCode
     */
    public static void showWebView(String paramCode) {
        Bundle paramBundle = new Bundle();
        paramBundle.putString("paramCode", paramCode);
        platfromCallBack("showWebView", paramBundle);
    }

    /**
     * ��抽��缃�椤�
     */
    public static void closeWebView() {
        Bundle paramBundle = new Bundle();
        platfromCallBack("closeWebView", paramBundle);
    }

    /**
     * ��瑰�诲��浜����璋�
     *
     * @param url
     * @param title
     * @param content
     */
    public static void onShare(String picture, String url, String title,
                               String content, int shareType) {
        Bundle paramBundle = new Bundle();
        paramBundle.putString("picture", picture);
        paramBundle.putString("url", url);
        paramBundle.putString("title", title);
        paramBundle.putString("content", content);
        paramBundle.putInt("shareType", shareType);
        platfromCallBack("onShare", paramBundle);
    }

    /**
     * ��瑰�绘�ㄥ箍���椤甸�㈠��璋�
     *
     * @param type
     */
    public static void onPromoteCode(int type) {
        Bundle paramBundle = new Bundle();
        paramBundle.putInt("type", type);
        platfromCallBack("onPromoteCode", paramBundle);
    }

    /**
     */
    public static void onOpenMemberCenter() {
        Bundle paramBundle = new Bundle();
        platfromCallBack("onOpenMemberCenter", paramBundle);
    }

    /**
     * 调用平台支付.
     */
    public static void onPay(String productId) {
        Bundle paramBundle = new Bundle();
        paramBundle.putString("goodsCode", productId);
        platfromCallBack("onPay", paramBundle);
    }

    /**
     * 充值完成.
     */
    public static void onPayFinished() {
        Bundle paramBundle = new Bundle();
        platfromCallBack("onPayFinished", paramBundle);
    }

    /**
     * 退出游戏接口.
     */
    public static void onExit() {
        Bundle paramBundle = new Bundle();
        platfromCallBack("onExit", paramBundle);
    }

    /**
     * 清理缓存接口.
     */
    public static void onCrash(String message) {
        Bundle paramBundle = new Bundle();
        paramBundle.putString("message", message);
        platfromCallBack("onCrash", paramBundle);
    }

    /**
     * 通用接口，直接传入api接口.
     */
    public static void serverPipeline(String action) {
        Bundle paramBundle = new Bundle();
        paramBundle.putString("action", action);
        platfromCallBack("serverPipeline", paramBundle);
    }

    /**
     * 公测返利接口.
     */
    public static void OBDoubleMoney(String roleId) {
        Bundle paramBundle = new Bundle();
        paramBundle.putString("roleId", roleId);
        platfromCallBack("OBDoubleMoney", paramBundle);
    }

    /**
     * 应用宝社区.
     */
    public static void tencentCommunity() {
        Bundle paramBundle = new Bundle();
        platfromCallBack("tencentCommunity", paramBundle);
    }

    /**
     * 切换账号.
     */
    public static void onSwitchAccount() {
        Bundle paramBundle = new Bundle();
        platfromCallBack("onSwitchAccount", paramBundle);
    }

    public static void onPurchase(String item, int itemNumber, Double priceInVirtualCurrency) {
        Bundle paramBundle = new Bundle();
        paramBundle.putString("item", item);
        paramBundle.putInt("itemNumber", itemNumber);
        paramBundle.putDouble("priceInVirtualCurrency", priceInVirtualCurrency);
        platfromCallBack("onPurchase", paramBundle);

    }

    public static void onReward(double Amount, String reason) {
        Log.i(TAG, "onReward");
        Bundle paramBundle = new Bundle();
        paramBundle.putDouble("virtualCurrencyAmount", Amount);
        paramBundle.putString("reason", reason);
        platfromCallBack("onReward", paramBundle);
    }


    /**
     * ���绾ц�����
     *
     * @param level
     */
    public static void onLevelUp(int level) {
        Log.i(TAG, "onLevelUp");
        Bundle paramBundle = new Bundle();
        paramBundle.putInt("level", level);
        platfromCallBack("getRoleLevel", paramBundle);
    }

    /**
     * 璐�涔伴����峰��璋����
     */
    public static void onBuyItem(int level, int gameCoinCount,
                                 String gameCoinId, String gameCoinName, int itemCount,
                                 String itemId, String itemName) {
        Bundle paramBundle = new Bundle();
        paramBundle.putInt("level", level);
        paramBundle.putInt("gameCoinCount", gameCoinCount);
        paramBundle.putString("gameCoinId", gameCoinId);
        paramBundle.putString("gameCoinName", gameCoinName);
        paramBundle.putInt("itemCount", itemCount);
        paramBundle.putString("itemId", itemId);
        paramBundle.putString("itemName", itemName);
        platfromCallBack("onBuyItem", paramBundle);
    }

    /**
     * 浣跨�ㄩ����峰��璋����
     *
     * @param level
     * @param itemCount
     * @param itemId
     * @param itemName
     */
    public static void onUseItem(int level, int itemCount, String itemId,
                                 String itemName) {
        Bundle paramBundle = new Bundle();
        paramBundle.putInt("level", level);
        paramBundle.putInt("itemCount", itemCount);
        paramBundle.putString("itemId", itemId);
        paramBundle.putString("itemName", itemName);
        platfromCallBack("onUseItem", paramBundle);
    }

    public static void getProductConfigs() {
        Bundle paramBundle = new Bundle();
        platfromCallBack("getProductConfigs", paramBundle);
    }

    public static void onEnterGame(int actionId, int level, int roleId, String roleName, int moneyNum, int vipLevel, int gem) {
        Bundle paramBundle = new Bundle();
        paramBundle.putInt("actionId", actionId);//1：选择服务器，2：创建角色，3：进入游戏，4：等级提升
        paramBundle.putInt("roleLevel", level);
        paramBundle.putInt("roleId", roleId);
        paramBundle.putString("roleName", roleName);
        paramBundle.putInt("moneyNum", moneyNum);
        paramBundle.putInt("vipLevel", vipLevel);
        paramBundle.putInt("gem", gem);
        platfromCallBack("onEnterGame", paramBundle);
    }

    public static void logEvent(int actionId) {
        Bundle paramBundle = new Bundle();
        paramBundle.putInt("actionId", actionId);
        platfromCallBack("logEvent", paramBundle);
    }

    public static void openVideo(boolean isPlay, String path, boolean isHalfPlay) {
        Bundle paramBundle = new Bundle();
        paramBundle.putBoolean("isPlay", isPlay);
        paramBundle.putString("path", path);
        paramBundle.putBoolean("isHalfPlay", isHalfPlay);
        platfromCallBack("openVideo", paramBundle);
    }

    private static void platfromCallBack(String funName, Bundle paramBundle) {
        try {
            Method method = mainActivity.getClass().getSuperclass().getDeclaredMethod(funName,
                    new Class[]{Bundle.class});
            method.setAccessible(true);
            method.invoke(mainActivity, new Object[]{paramBundle});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void lightScreenON() {
        PowerManager pm = (PowerManager) mainActivity
                .getSystemService(Context.POWER_SERVICE);
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "JNI");
        wakeLock.acquire();
    }

    public static void lightScreenOFF() {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
    }

    public static void isWifiConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) mainActivity
                .getSystemService(mainActivity.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()
                && activeInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            wifiConnectResult(1);
            return;
        }
        wifiConnectResult(0);
    }

    /**
     * 璋���ㄦ父�����ュ��
     *
     * @param eventType
     * @param paramCode
     */
    public native static void nativeMethod(int eventType, String paramCode);

    /**
     * 瑙���插��绉版����ュ��璋�
     *
     * @param roleName
     * @param result   1锛������� 0锛�涓�������
     */
    public static void roleNameCheckResult(String roleName, Integer result) {
        JSONObject obj = new JSONObject();
        JSONUtils.put(obj, "roleName", roleName);
        JSONUtils.put(obj, "result", result);

        nativeMethod(1, obj.toString());
    }

    /**
     * 骞冲�扮�婚��瀹�������璋����
     *
     * @param roleId
     * @param roleName
     * @param loginSign
     */
    public static void loginSuccess(String roleId, String roleName,
                                    String loginSign) {
        JSONObject obj = new JSONObject();
        JSONUtils.put(obj, "roleId", roleId);
        JSONUtils.put(obj, "roleName", roleName);
        JSONUtils.put(obj, "loginSign", loginSign);
        Log.i(TAG, "loginSuccess = " + obj.toString());

        nativeMethod(2, obj.toString());
    }

    /**
     * 浼����������������
     *
     * @param productConfig
     */
    public static void productConfigResults(String productConfig) {
        Log.i(TAG, "JNI ----- PROD:" + productConfig);
        nativeMethod(3, productConfig);
    }

    /**
     * wifi杩���ユ�ヨ��
     *
     * @param isConnected
     */
    public static void wifiConnectResult(Integer isConnected) {
        JSONObject obj = new JSONObject();
        JSONUtils.put(obj, "isConnected", isConnected);
        nativeMethod(4, obj.toString());
    }

    /**
     * �����″�ㄨ�����
     *
     * @param result
     */
    public static void serverPipelineResult(String result) {
        nativeMethod(5, result);
    }

    /**
     * 支付完成
     *
     * @param result
     */
    public void onPayFinish() {
        nativeMethod(6, "");
    }

    /**
     * 分享结果
     *
     * @param result
     */
    public void shareResult(Integer result) {
        JSONObject obj = new JSONObject();
        JSONUtils.put(obj, "result", result);
        nativeMethod(7, obj.toString());
    }

    /**
     * 图片路径
     *
     * @param url
     */
    public static void sendImageUrl(String url) {
        JSONObject obj = new JSONObject();
        JSONUtils.put(obj, "picUrl", url);
        nativeMethod(8, obj.toString());
        Log.i("photo", "JNI收到URL=" + url + ",并且发送给了游戏");
    }

    /**
     * 视频播放完毕通知.
     */
    public static void videoComplete(String videoUrl) {
        JSONObject obj = new JSONObject();
        JSONUtils.put(obj, "videoUrl", videoUrl);
        Log.i(TAG, obj.toString());
        nativeMethod(9, "");
    }

    public static void noticeAvailMem() {
        ActivityManager am = (ActivityManager) mainActivity
                .getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);

        JSONObject obj = new JSONObject();
        JSONUtils.put(obj, "availMem", mi.availMem);
        nativeMethod(101, obj.toString());
    }

}