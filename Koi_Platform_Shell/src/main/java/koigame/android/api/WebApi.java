package koigame.android.api;

import org.json.JSONObject;

public interface WebApi {

	public final static String PACKAGES = "packages";

	public final static String ROLE_ID = "roleId";

	public final static String ROLE_NAME = "roleName";
	
	public final static String ACCESS_TOKEN = "accessToken";
	
	public final static String DEVICE_TYPE = "deviceType";
	public final static String MACADDRESS = "macAddress";
	public final static String CONNECTTYPE = "connectType";
	public final static String SYSTEMVER = "systemVersion";
	public final static String CHANNEL_ID = "channelId";

	/**
	 * 自动登录
	 */
	JSONObject thirdOauthLogin(String uid, String accessToken, String appId, String extraData)
			throws ServiceException;

	/**
	 * 获得用户角色信息
	 */
	JSONObject getUserRoleInfo(long userId, String accessToken) throws ServiceException;

	/**
	 * 检查角色名
	 */
	JSONObject checkRoleName(String accessToken, String roleName, String roleId) throws ServiceException;

	/**
	 * 分享成功
	 * @return
	 * @throws ServiceException
	 */
	JSONObject shareSuccess() throws ServiceException;

	/**
	 * * 客户端先从第3方平台充值成功后，再向平台发起充值和划账请求。 （目前针对机锋平台，因为他充值成功的消息是由机锋sdk客户端通知平台客户端的）
	 */
	public JSONObject chargeFromGame(String roleId, String userName, String accessToken, String productId)
			throws ServiceException;

	/**
	 * 自定义action访问平台服务器
	 *
	 * @param action
	 * @return
	 * @throws ServiceException
	 */
	String serverPipeline(String action) throws ServiceException;

	/**
	 * 游戏退出
	 *
	 * @param accessToken
	 * @return
	 * @throws HServiceException
	 */
	//JSONObject exitGame(String accessToken) throws HServiceException;

	/**
	 *
	 * @param openId 从手Q登录态中获取的openid的值
	 * @param openKey 从手Q登录态中获取的access_token 的值
	 * @param pay_token 从手Q登录态中获取的pay_token的值
	 * @param appId 应用的唯一ID。可以通过appid查找APP基本信息。
	 * @param timeStamp UNIX时间戳
	 * @param sign 	请求串的签名 	平台来源，$平台-$渠道-$版本-$业务标识
	 * @param pf 跟平台来源和openkey根据规则生成的一个密钥串。
	 * @param pfKey 账户分区ID_角色
	 */
	JSONObject checkBalance(String openId, String openKey, String pay_token, String appId, String timeStamp, String sign, String pf, String pfKey, String zoneId) throws ServiceException;

	JSONObject tencentPayCallback(String openId, String openKey, String appId, String pf, String pfKey, String areaId, String msdkType, String money)throws ServiceException;

}
