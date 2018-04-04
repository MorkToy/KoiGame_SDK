package koigame.sdk.util;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ShareUtils {

	private String TAG = "ShareUtils";
	private static ShareUtils instance;
	private static IWXAPI wxapi;
	private static IWeiboShareAPI mWeiboShareAPI;
	private static Activity activity;
	private static final int THUMB_SIZE = 150;

	public static ShareUtils getInstance(Activity context, String wxappId, String wbappkey) {
		activity = context;
		//微信分享初始化
		if (wxapi == null) {
			wxapi = WXAPIFactory.createWXAPI(context, wxappId);
			boolean issuccess = wxapi.registerApp(wxappId);
			if (!issuccess) {
				AndroidUtils.showToast(context, "微信分享初始化失败", 1);
			}
		}
		//微博分享实例化
		if (mWeiboShareAPI == null) {
			mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, wbappkey);
			mWeiboShareAPI.registerApp();
		}
		
		return instance;
	}
	
	public static interface Type {
        int WeiChatInterfaceType_IsWeiChatInstalled = 1; //判断是否安装微信
        int WeiChatInterfaceType_RequestLogin = 2; //请求登录
        int WeiChatInterfaceType_ShareUrl = 3; //分享链接
        int WeiChatInterfaceType_ShareText = 4; //分享文本
        int WeiChatInterfaceType_ShareMusic = 5;//分享音乐
        int WeiChatInterfaceType_ShareVideo = 6;//分享视频
        int WeiChatInterfaceType_ShareImage = 7;//分享图片
    }

    public static interface Transaction {
        String IsWeiChatInstalled = "isInstalled"; //判断是否安装微信
        String RequestLogin = "login"; //请求登录
        String ShareUrl = "shareUrl"; //分享链接
        String ShareText = "shareText"; //分享文本
        String ShareMusic = "shareMusic";//分享音乐
        String ShareVideo = "shareVideo";//分享视频
        String ShareImage = "shareImage";//分享图片
    }

	public void wechatShare(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			Log.e(TAG, "分享失败，路径为" + filePath);
			AndroidUtils.showToast(activity, "路径错误：" + filePath, 1);
			return;
		}

		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = activity.getApplicationContext()
					.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(
					activity.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		Drawable d = packageManager.getApplicationIcon(applicationInfo);
		BitmapDrawable bd = (BitmapDrawable) d;
		Bitmap bmp = bd.getBitmap();

		WXImageObject imgObj = new WXImageObject();
		imgObj.setImagePath(filePath);

		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;

		// Bitmap bmp = BitmapFactory.decodeFile(path);
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE,
				THUMB_SIZE, true);
		bmp.recycle();
		msg.thumbData = bmpToByteArray(thumbBmp, true);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		SendReq(req);
		Log.e("分享", "分享完毕");
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	public void SendReq(BaseReq req) {
		boolean issuccess = wxapi.sendReq(req);
		if (!issuccess) {
			AndroidUtils.showToast(activity, "微信分享失败", 1);
		} else {
			AndroidUtils.showToast(activity, "微信分享成功", 1);
		}
	}

	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	//------------------------------------------------分割线-------------------------------------------
	
	public void weiboShare() {
		
	}

}
