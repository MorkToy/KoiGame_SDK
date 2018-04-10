package koigame.android.api;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.List;

import koigame.sdk.KConstant;
import koigame.sdk.KMetaData;
import koigame.sdk.api.KWebApi;
import koigame.sdk.bean.user.KUserSession;
import koigame.sdk.util.HHttpConnection;
import koigame.sdk.util.JSONUtils;


public class WebApiImpl implements WebApi {
	private static WebApiImpl instance = new WebApiImpl();
	private static String TAG = "WebApi";

	private WebApiImpl() {
	}

	public static WebApiImpl instance() {
		return instance;
	}

	private JSONObject execute(PostMethod post) throws ServiceException {
		return execute(post, post.getRequestParamList());

	}

	private JSONObject execute(PostMethod post, List<NameValuePair> list) throws ServiceException {
		String responseString = null;
		JSONObject props = new JSONObject();
		try {
			responseString = HHttpConnection.syncConnect(post.getUrl(), list);
			responseString = URLDecoder.decode(responseString, "utf-8");
			Log.d(TAG, responseString);
			props = JSONUtils.build(responseString);
			if (!JSONUtils.isOK(props)) {
				throw new ServiceException(JSONUtils.getInt(props, JSONUtils.ERROR_CODE), JSONUtils.getString(props,
						JSONUtils.ERROR_MSG));
			}
		} catch (UnknownHostException e) {
			throw ServiceException.CONNECT;
		} catch (IOException e) {
			throw ServiceException.CONNECT;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}

		return props;
	}

	private String executeForString(PostMethod post, List<NameValuePair> list) throws ServiceException {
		String responseString = null;
		try {
			responseString = HHttpConnection.syncConnect(post.getUrl(), list);
			responseString = URLDecoder.decode(responseString, "utf-8");
		} catch (UnknownHostException e) {
			throw ServiceException.CONNECT;
		} catch (IOException e) {
			throw ServiceException.CONNECT;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}

		return responseString;
	}

	@Override
	public JSONObject thirdOauthLogin(String uid, String accessToken, String appId,
			String extraData) throws ServiceException {
		PostMethod post = new PostMethod(KConstant.URL + "/user/thirdOauthLogin");

		NameValuePair userIdNV = new BasicNameValuePair(KWebApi.USERID, uid);
		NameValuePair extraDataNV = new BasicNameValuePair(KWebApi.EXTRADATA, extraData);
		NameValuePair accessTokenNV = new BasicNameValuePair(KWebApi.TOKEN, accessToken);

		NameValuePair appIdNV = new BasicNameValuePair(KWebApi.APP_ID, appId);
		NameValuePair accountNameNV = new BasicNameValuePair(KWebApi.ACCOUNT_NAME, extraData);

		post.setRequestBody(new NameValuePair[] { userIdNV, extraDataNV, accessTokenNV, appIdNV,accountNameNV });
		return execute(post);
	}

	@Override
	public JSONObject getUserRoleInfo(long uid,String accessToken) throws ServiceException {
		PostMethod post = new PostMethod(KConstant.URL + "/game/getRole");

		NameValuePair accessTokenNV = new BasicNameValuePair(KWebApi.ACCESS_TOKEN, accessToken);
		NameValuePair userIdNV = new BasicNameValuePair(KWebApi.USERID, uid + "");

		post.setRequestBody(new NameValuePair[] { accessTokenNV, userIdNV });
		Log.i(TAG, "getUserRoleInfo");
		return execute(post);
	}

	@Override
	public JSONObject checkRoleName(String accessToken, String roleName, String roleId) throws ServiceException {
		PostMethod post = new PostMethod(KConstant.URL + "/game/checkRoleName");

		NameValuePair accessTokenNV = new BasicNameValuePair(KWebApi.ACCESS_TOKEN, accessToken);
		NameValuePair roleIdNV = new BasicNameValuePair(KWebApi.ROLEID, roleId);
		NameValuePair roleNameNV = new BasicNameValuePair(WebApi.ROLE_NAME, URLEncoder.encode(roleName));
		NameValuePair keyNV = new BasicNameValuePair(KWebApi.UNIQUE_KEY, KMetaData.UniqueKey);
		NameValuePair useridNv = new BasicNameValuePair("userId", KUserSession.instance().getUserInfo().getUserId() + "");

		post.setRequestBody(new NameValuePair[] { accessTokenNV, roleIdNV, roleNameNV, keyNV, useridNv });
		Log.i(TAG, "checkRoleName");
		return execute(post);
	}
	
	

	@Override
	public JSONObject shareSuccess() throws ServiceException {
		PostMethod post = new PostMethod(KConstant.URL + "/game/share");

		NameValuePair accessTokenNV = new BasicNameValuePair(KWebApi.ACCESS_TOKEN, KUserSession.instance()
				.getUserInfo().getAccessToken());
		NameValuePair roleIdNV = new BasicNameValuePair(KWebApi.ROLEID, KUserSession.instance().getUserInfo()
				.getRoleId());

		post.setRequestBody(new NameValuePair[] { accessTokenNV, roleIdNV });
		return execute(post);
	}

	@Override
	public JSONObject chargeFromGame(String roleId, String userName, String accessToken, String productId)
			throws ServiceException {
		/*
		 * PostMethod post = new PostMethod(KConstant.URL +
		 * "/payProxy?actionType=chargeFromGame");
		 */
		PostMethod post = new PostMethod(KConstant.URL + "/bill/charge");
		NameValuePair accessTokenNV = new BasicNameValuePair(KWebApi.ACCESS_TOKEN, accessToken);
		NameValuePair uId = new BasicNameValuePair(KWebApi.ROLEID, roleId);
		//NameValuePair orderNV = new BasicNameValuePair(KWebApi.PAY_DESC, userName);
		NameValuePair keyNV = new BasicNameValuePair(KWebApi.UNIQUE_KEY, KMetaData.UniqueKey);
		NameValuePair proId = new BasicNameValuePair(KWebApi.PRODUCTID, productId);
		//NameValuePair price = new BasicNameValuePair(KWebApi.PRICE, "0");
		post.setRequestBody(new NameValuePair[] { accessTokenNV, keyNV, uId, proId });
		return execute(post);
	}

	@Override
	public String serverPipeline(String action) throws ServiceException {
		PostMethod post = new PostMethod(KConstant.URL + action);
		NameValuePair roleIdNV = new BasicNameValuePair(WebApi.ROLE_ID, KUserSession.instance().getUserInfo()
				.getRoleId());
		NameValuePair userIdNV = new BasicNameValuePair(KWebApi.USERID, String.valueOf(KUserSession.instance()
				.getUserInfo().getUserId()));
		NameValuePair accessTokenNV = new BasicNameValuePair(KWebApi.ACCESS_TOKEN, String.valueOf(KUserSession
				.instance().getUserInfo().getAccessToken()));
		post.setRequestBody(new NameValuePair[] { roleIdNV, userIdNV, accessTokenNV });
		return executeForString(post, post.getRequestParamList());
	}

	/*@Override
	public JSONObject exitGame(String accessToken) throws HServiceException {
		// TODO Auto-generated method stub
		PostMethod post = new PostMethod(KConstant.URL + "/user/logout");

		NameValuePair accessTokenNV = new BasicNameValuePair(ACCESS_TOKEN, accessToken);
		
		post.setRequestBody(new NameValuePair[] { accessTokenNV });
		
		return execute(post);
	}*/
	
	@Override
	public JSONObject checkBalance(String openId, String openKey,
			String pay_token, String appId, String timeStamp, String sign,
			String pf, String pfKey, String zoneId) throws ServiceException {
		PostMethod post = new PostMethod(KConstant.URL + "/bill/charge");
		NameValuePair oId = new BasicNameValuePair("openid", openId);
		NameValuePair pToken = new BasicNameValuePair("openkey", openKey);
		NameValuePair aId = new BasicNameValuePair("pay_token", pay_token);
		NameValuePair ts = new BasicNameValuePair("appid", appId);
		NameValuePair si = new BasicNameValuePair("ts", timeStamp);
		NameValuePair pkey = new BasicNameValuePair("sig", sign);
		NameValuePair pk = new BasicNameValuePair("pf", pf);
		NameValuePair zi = new BasicNameValuePair("pfkey", pfKey);
		
		post.setRequestBody(new NameValuePair[] { oId,pToken,aId,ts,si,pkey,pk,zi });
		
		return execute(post);
	}
	@Override
	public JSONObject tencentPayCallback(String openId,String openKey,String appId,String pf,String pfKey,String areaId,String msdkType,String money)throws ServiceException{
		PostMethod post = new PostMethod(KConstant.URL + "/billing/tencentPayCallback");
		NameValuePair oId = new BasicNameValuePair("openid", openId);
		NameValuePair pToken = new BasicNameValuePair("openkey", openKey);
		NameValuePair ts = new BasicNameValuePair("appid", appId);
		NameValuePair pk = new BasicNameValuePair("pf", pf);
		NameValuePair zi = new BasicNameValuePair("pfkey", pfKey);
		NameValuePair area = new BasicNameValuePair("areaId", areaId);
		NameValuePair sdkType = new BasicNameValuePair("msdkType", msdkType);
		NameValuePair mon = new BasicNameValuePair("money", money);
		post.setRequestBody(new NameValuePair[] { oId,pToken,ts,pk,zi,area,sdkType,mon });
		return execute(post);
	}

}
