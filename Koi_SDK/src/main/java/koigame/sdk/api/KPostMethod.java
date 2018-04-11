package koigame.sdk.api;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import koigame.sdk.KConstant;
import koigame.sdk.KMetaData;
import koigame.sdk.bean.user.KUserSession;
import koigame.sdk.util.AndroidUtils;
import koigame.sdk.util.CollectionUtils;
import koigame.sdk.util.DateUtil;
import koigame.sdk.util.ResourceUtils;
import koigame.sdk.util.ActivityKeep;
import koigame.sdk.util.crypto.MD5;

import static koigame.sdk.util.DateUtil.CY_DAY_FORMAT;


public class KPostMethod {

	public static final String TAG = "KPostMethod";

	private String url;
	private List<NameValuePair> list;

	public KPostMethod(String url) {
		super();
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	/**
	 * 设置请求公共参数
	 *
	 */
	public void setRequestBody(NameValuePair[] map) {
		list = new ArrayList<NameValuePair>();

		NameValuePair gameIdNV = new BasicNameValuePair(KWebApi.GAMEID, 27 + "");
		NameValuePair siteIdNV = new BasicNameValuePair(KWebApi.SITEID, ResourceUtils.getInstance().getSiteResouceId(KMetaData.Site) + "");
		NameValuePair channelNV = new BasicNameValuePair(KWebApi.CHANNEL, KMetaData.Site);
		NameValuePair channelidNV = new BasicNameValuePair(KWebApi.CHANNEL_ID, KMetaData.ChannelId);
		NameValuePair areaNV = new BasicNameValuePair(KWebApi.AREAID, Integer.valueOf(
				KUserSession.instance().getUserInfo().getAreaCode()).toString());
		NameValuePair verNV = new BasicNameValuePair(KWebApi.APP_VERSION, String.valueOf(KMetaData.VerionCode));
		NameValuePair gameVerNV = new BasicNameValuePair(KWebApi.GAME_VERSION, String.valueOf(KMetaData.GameVersion));
		NameValuePair keyNV = new BasicNameValuePair(KWebApi.UNIQUE_KEY, KMetaData.UniqueKey);
//		NameValuePair modelNV = new BasicNameValuePair(KWebApi.MODEL, koi.android.os.Build.MODEL);
		NameValuePair modelNV = new BasicNameValuePair(KWebApi.MODEL, "model");
		NameValuePair platformNV = new BasicNameValuePair(KWebApi.PLATFORM, "android");
		NameValuePair sdkVersionNV = new BasicNameValuePair(KWebApi.SDK_VERSION, KMetaData.SdkVersion);
		NameValuePair modeNV = new BasicNameValuePair(KWebApi.MODE, KMetaData.Mode);
		NameValuePair deviceNV = new BasicNameValuePair(KWebApi.DEVICE_TYPE, "1");
		NameValuePair macNV = new BasicNameValuePair(KWebApi.MACADDRESS, KMetaData.macAddress);
		NameValuePair connectNV = new BasicNameValuePair(KWebApi.CONNECTTYPE, KMetaData.netType);
//		NameValuePair systemVersionNV = new BasicNameValuePair(KWebApi.SYSTEMVER, koi.android.os.Build.VERSION.RELEASE);
		NameValuePair systemVersionNV = new BasicNameValuePair(KWebApi.SYSTEMVER, "version.release");

		NameValuePair timeNV = new BasicNameValuePair(KWebApi.CURRTIME, DateUtil.dateFormat(new Date(),CY_DAY_FORMAT));
		NameValuePair resolutionNV = new BasicNameValuePair(KWebApi.RESOLUTION, "");
		NameValuePair packageNameNV = new BasicNameValuePair(KWebApi.PACKAGENAME, "");//ActivityKeep.getInstance().getMainActivity().getPackageName()
		NameValuePair shopTypeNV = new BasicNameValuePair(KWebApi.SHOPTYPE,"0");

		
		NameValuePair roleLevelNV = new BasicNameValuePair(KWebApi.ROLELEVEL, KUserSession.instance().getUserInfo().getRoleLevel() + "");

		list.add(channelNV);
		list.add(channelidNV);
		list.add(siteIdNV);
		list.add(gameIdNV);
		list.add(areaNV);
		list.add(verNV);
		list.add(gameVerNV);
		list.add(keyNV);
		list.add(platformNV);
		list.add(modelNV);
		list.add(sdkVersionNV);
		list.add(modeNV);
		list.add(connectNV);
		list.add(macNV);
		list.add(deviceNV);
		list.add(systemVersionNV);
		list.add(timeNV);
		list.add(resolutionNV);
		list.add(packageNameNV);
		list.add(shopTypeNV);
		list.add(roleLevelNV);

		// list.add(signNV);
		CollectionUtils.addToList(map, list);

	}

	public List<NameValuePair> getRequestParamList() {
		return list;
	}

	/**
	 * 根据请求参数列表获得请求加密
	 * 
	 * @param screenName
	 * @param time
	 * @param source
	 * @return
	 */
	private String getSign(String screenName, String time, String source) {
		StringBuffer buffer = new StringBuffer(KConstant.SIGN);
		buffer.append("_").append(screenName).append("_").append(time).append("_").append(source);

		return MD5.encode(buffer.toString());
	}

	/**
	 * 获得时间字符串
	 * 
	 * @return
	 */
	private String getTime() {
		Date date = new Date();
		String curDateString = DateUtil.formatDateToString(date, TimeZone.getTimeZone("GMT+8:00"),
				"MM-dd-yyyy HH:mm:ss");
		date = DateUtil.parseStringToDate(curDateString, TimeZone.getTimeZone("GMT+8:00"), "MM-dd-yyyy HH:mm:ss");
		return String.valueOf(date.getTime());
	}
	
}