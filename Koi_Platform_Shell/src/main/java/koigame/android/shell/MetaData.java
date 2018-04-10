package koigame.android.shell;

import android.content.Context;

import koigame.sdk.util.AndroidUtils;

public class MetaData {
	public static String ResHostName;
	public static String GameJarName;
	public static String GameActivityName;
	public static String GameDir;
	public static Boolean UsePlatformRes;


	public static String LoginType;
	public static String PayType;
	public static String StatisticsType;
	public static String Mac;

	public static String WechatAppId;
	public static String WechatActivity;
	public static String WeiboAppKey;
	public static boolean GumpWebPay;
	public static String talkData;
	public static String Orientationl;
	public static String promoteTurnOn;

	public final static String LOGIN_TYPE = "login_type";
	public final static String PAY_TYPE = "pay_type";
	public final static String STATISTICS_TYPE = "statistics_type";
	public final static String PLATHOST = "plathost";
	public final static String GAME_JAR_NAME = "gameJarName";
	public final static String GAME_ACTIVITY_NAME = "gameActivityName";
	public final static String GAME_DIR = "gameDir";
	public final static String RES_HOST = "reshost";
	public final static String RES_TYPE = "restype";
	public final static String PORT = "port";
	public final static String WECHAT_APPID = "WECHAT_APPID";
	public final static String WECHAT_ACTIVITY = "WECHAT_ACTIVITY";
	public final static String WEIBO_APPKEY = "WEIBO_APPKEY";
	public final static String GUMPWEBPAY = "GumpWebPay";
	public final static String TALKDATA_APPID = "TALKDATA_APPID";
	public final static String ORIENTATION= "orientationl";
	


	public static void init(Context ctx) {
		Orientationl = AndroidUtils.instance().getMetaData(ctx, ORIENTATION);
		LoginType = AndroidUtils.instance().getMetaData(ctx, LOGIN_TYPE);
		PayType = AndroidUtils.instance().getMetaData(ctx, PAY_TYPE);
		StatisticsType = AndroidUtils.instance().getMetaData(ctx, STATISTICS_TYPE);
		GameJarName = AndroidUtils.instance().getMetaData(ctx, GAME_JAR_NAME);
		GameActivityName = AndroidUtils.instance().getMetaData(ctx, GAME_ACTIVITY_NAME);
		GameDir = "hilink";
		promoteTurnOn = "0";

		String resType = AndroidUtils.instance().getMetaData(ctx, RES_TYPE);
		UsePlatformRes = "platform".equalsIgnoreCase(resType) ? true : false;
		ResHostName = AndroidUtils.instance().getMetaData(ctx, RES_HOST);
		Mac = AndroidUtils.instance().fetchUdid(ctx);

		WeiboAppKey = AndroidUtils.instance().getMetaData(ctx, WEIBO_APPKEY);
		WechatAppId = AndroidUtils.instance().getMetaData(ctx, WECHAT_APPID);
		WechatActivity = AndroidUtils.instance().getMetaData(ctx, WECHAT_ACTIVITY);
		GumpWebPay =Boolean.parseBoolean(AndroidUtils.instance().getMetaData(ctx, GUMPWEBPAY));
		talkData = AndroidUtils.instance().getMetaData(ctx, TALKDATA_APPID);

	}
}