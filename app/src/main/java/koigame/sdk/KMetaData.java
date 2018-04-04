package koigame.sdk;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import koigame.sdk.util.AndroidUtils;
import koigame.sdk.util.StringUtils;

public class KMetaData {

	public static String GameCode;
	public static String Site;
	public static String Channel;
	public static String ChannelId = "9998";
	public static int VerionCode;
	public static String VersionName;
	public static int GameVersion;
	public static String UniqueKey;
	public static String Mode;
	public static String Target;
	public static int PatchAccount;
	public static int IsSmall;
	public static String macAddress;
	public static String netType;
	public static String SdkVersion="1.0";

	public final static String CHANNEL = "channel";
	public final static String CHANNEL_ID = "channelId";
	public final static String PLATHOST = "plathost";
	public final static String GAMECODE = "gameCode";
	public final static String SITE = "site";
	public final static String APP_VERSION = "appVersion";
	public final static String GAME_VERSION = "gameVersion";
	public final static String MODE = "mode";
	public final static String TARGET = "target";
	public final static String ISSMALL = "isSmall";
	public final static String PATCHACCOUNT= "patchAccount";
	public final static String PLATFORM_HOST = "platformHost";
	public final static String PLATFORM_PORT = "platformPort";
	public final static String MACADDRESS = "macAddress";
	

	static void init(Application application) {
		Context ctx = application.getApplicationContext();
		init(ctx);
	}

	static void init(Context ctx) {
		GameCode = AndroidUtils.instance().getMetaData(ctx, GAMECODE);
		Site = AndroidUtils.instance().getMetaData(ctx, SITE);
		Channel = AndroidUtils.instance().getMetaData(ctx, CHANNEL);
		Mode = AndroidUtils.instance().getMetaData(ctx, MODE);
		Target = AndroidUtils.instance().getMetaData(ctx, TARGET);
		//IsSmall =Integer.parseInt(AndroidUtils.instance().getMetaData(ctx, ISSMALL));
//		UniqueKey = AndroidUtils.instance().fetchUdid(ctx);
		UniqueKey = "12345678";
//		macAddress = AndroidUtils.instance().getLocalMacAddress(ctx);
		macAddress = "0.0.0.0";
		netType = AndroidUtils.instance().GetNetworkType(ctx);
		Log.i("unmiquekey", UniqueKey+"");
		
		KConstant.PLATFORM_HOST = AndroidUtils.instance().getMetaData(ctx, PLATFORM_HOST);
		KConstant.PLATFORM_PORT = AndroidUtils.instance().getMetaData(ctx, PLATFORM_PORT);
		
		if(StringUtils.isEmpty(AndroidUtils.instance().getMetaData(ctx, ISSMALL))){
			IsSmall=0;
		}else{
			IsSmall=Integer.parseInt(AndroidUtils.instance().getMetaData(ctx, ISSMALL));
		}
		
		if(StringUtils.isEmpty(AndroidUtils.instance().getMetaData(ctx, PATCHACCOUNT))||AndroidUtils.instance().getMetaData(ctx, PATCHACCOUNT).equals("0")){
			PatchAccount=0;
			if (Mode.contains("beta")) {
	            KConstant.PLATFORM_HOST = KConstant.PLATFORM_HOST_BETA;
	            KConstant.PLATFORM_PORT = KConstant.PLATFORM_PORT_BETA;
	            KConstant.DEBUG = true;

			} else if (Mode.contains("release")) {
				KConstant.PLATFORM_HOST = KConstant.PLATFORM_HOST_RELEASE;
	            KConstant.PLATFORM_PORT = KConstant.PLATFORM_PORT_RELEASE;

			} else if (Mode.contains("alpha")){
				KConstant.PLATFORM_HOST = KConstant.PLATFORM_HOST_ALPHA;
				KConstant.PLATFORM_PORT = KConstant.PLATFORM_PORT_ALPHA;
				KConstant.DEBUG = true;
			}
		} else {
			PatchAccount =  Integer.parseInt(AndroidUtils.instance().getMetaData(ctx, PATCHACCOUNT));
			if(Mode.contains("beta")){
				KConstant.PLATFORM_HOST = KConstant.PLATFORM_HOST_BETA;
				KConstant.PLATFORM_PORT = KConstant.PLATFORM_PORT_BETA;
				KConstant.DEBUG = true;
			}else{
				KConstant.PLATFORM_HOST = KConstant.GUMP_HOST_RELEASE;
	            KConstant.PLATFORM_PORT = KConstant.PLATFORM_PORT_RELEASE;
			}
			
		}
		
		
		PackageInfo packaginfo = null;
		try {
			String packageName = ctx.getPackageName();
			packaginfo = ctx.getPackageManager().getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS);
			VerionCode = packaginfo.versionCode;
			VersionName = packaginfo.versionName;
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}
	}

}