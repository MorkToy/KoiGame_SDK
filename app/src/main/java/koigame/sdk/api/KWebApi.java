package koigame.sdk.api;

import org.json.JSONObject;

import koigame.sdk.bean.pay.Currency;
import koigame.sdk.bean.pay.KPayInfo;


public interface KWebApi {

	public final static String ACCESS_TOKEN = "accessToken";
	public final static String TOKEN = "token";
	public final static String REFRESH_TOKEN = "refreshToken";
	public final static String GAMEID = "gameId";
	public final static String SITEID = "siteId";
	public final static String ACCOUNT_NAME = "accountName";
	public final static String EXTRADATA="extraData";
	public final static String CHANNEL = "channel";
	public final static String ACCOUNTID = "accountId";
	public final static String PUSH_INFO = "pushInfo";
	public final static String ACCOUNT_TYPE = "accountType";

	public final static String ORDERNO = "orderId";
	public final static String SRCNO = "srcNO";
	public final static String BALANCE = "balance";

	public final static String AREAID = "areaCode";
	public final static String ROLEID = "roleId";
	public final static String ROLENAME = "roleName";
	public final static String USERID = "userId";
	public final static String AUTH_VALUE = "authValue";
	public final static String PASSWD = "password";
	public final static String NEWPASSWD = "newPassword";
	public final static String OBJECTID = "objId";
	public final static String NICKNAME = "nickName";
	public final static String EMAIL = "email";
	public final static String AVATAR_URL = "avatarUrl";
	public final static String UNIQUE_KEY = "deviceSerial";
	public final static String PROMOTE_CODE = "promoteCode";
	public final static String PAYAMOUNT = "payAmount";
	public final static String GOODSCOUNT = "goodsCount";

	public final static String CURRENCY = "currency";
	public final static String PRODUCTID = "productId";
	public final static String GOODSCODE = "goodsCode";
	public final static String PRODUCT_NAME = "productName";
	public final static String ORDERDESC = "orderDesc";
	public final static String QUANTITY = "quantity";
	public final static String PAYTYPE = "payType";
	public final static String SHOPTYPE = "shopType";

	public final static String GAME_VERSION = "gameVersion";
	public final static String APP_VERSION = "appVersion";
	public final static String APK_NEEDUPDATE = "apkNeedUpdate";
	public final static String APK_FORUPDATE = "apkForceUpdate";
	public final static String APK_UPDATE_URL = "apkUpdateURL";

	public final static String YEAR = "year";
	public final static String MONTH = "month";
	public final static String APP_ID = "appId";
	public final static String TIMESTR = "time";
	public final static String SIGN = "sign";
	public final static String SIGNCODE = "signCode";
	public final static String MODEL = "model";
	public final static String PLATFORM = "platform";
	public final static String MODE = "mode";
	public final static String SDK_VERSION = "sdkVersion";
	
	public final static String DEVICE_TYPE = "deviceType";
	public final static String MACADDRESS = "macAddress";
	public final static String CONNECTTYPE = "connectType";
	public final static String SYSTEMVER = "systemVersion";
	public final static String CHANNEL_ID = "channelId";
	public final static String PHONENUM = "phoneNum";

	public final static String CURRTIME = "time";
	public final static String RESOLUTION = "resolution";
	public final static String PACKAGENAME = "packageName";
	public final static String ROLELEVEL = "roleLevel";

	/**
	 * 创建hilink账号
	 */
	JSONObject createAccount(String accountName, String passwd) throws KServiceException;

	/**
	 * 创建手机账号.
	 */
	JSONObject createPhoneAccount(String accountName, String passwd, String code) throws KServiceException;

	/**
	 * 绑定（注册）哈邻账号
	 */
	JSONObject bindAccount(String accessToken, String authValue, String passwd) throws KServiceException;

	/**
	 * 登录
	 */
	JSONObject login(String authValue, String passwd, String accoutType) throws KServiceException;

	/**
	 * accessToken 登陆
	 */
	JSONObject loginWithToken(String accountName, String accessToken, String refreshToken) throws KServiceException;

	/**
	 * 三方账号登陆
	 */
	JSONObject autoLogin(String uid, int authValueType) throws KServiceException;

	/**
	 * 设备激活,
	 */
	JSONObject activeDevice() throws KServiceException;

	/**
	 * 返回分配的个人账号.
	 */
	JSONObject getDispatcherAccountName() throws KServiceException;

	/**
	 * 给手机号发送短信.
	 */
	JSONObject sendSmsToUser(String accountName) throws KServiceException;

	/**
	 * 给账号绑定手机号.
	 */
	JSONObject bindPhoneAccount(String signCode, String accountName, String userId) throws KServiceException;

	/**
	 * 通过短信修改密码.
	 */
	JSONObject updatePasswordBySms(String signCode, String accountName, String phoneNum, String password, String accountType) throws KServiceException;

	/**
	 * 判断身份证合法.
	 */
	JSONObject checkBindIdCard(String idCard, String realName, String userId) throws KServiceException;

	/**
	 * 客户端更新状态.
	 */
	JSONObject updateClient(int updateStatus) throws KServiceException;

	/**
	 * 到达登录界面.
	 */
	JSONObject loginUI(String costTime) throws KServiceException;


	/**
	 * 角色创建日志.
	 */
	JSONObject createRoleEvent(int areaId, long userId, String roleId, String roleName) throws KServiceException;

	/**
	 * 角色创建日志.
	 */
	JSONObject loadGame(String arriveTime, long userId, String costTime) throws KServiceException;

	/**
	 * 领取礼包
	 */
	JSONObject drawPromoteCode(String accessToken, String code, String roleId, int areaId) throws KServiceException;

	/**
	 * 激活码激活.
	 */
	JSONObject checkPromoteCode(String roleId, String code) throws KServiceException;

	/**
	 * 判断该玩家激活码是否激活.
	 * @param userId
	 * @return
	 * @throws KServiceException
	 */
	JSONObject checkActive(String userId) throws KServiceException;


	/**
	 * 修改密码
	 * @throws KServiceException
	 */
	JSONObject modifyPassword(String accountName, String accountType, String pwd, String newPwd) throws KServiceException;

	/**
	 * 修改昵称
	 *
	 * @param accessToken
	 * @param nickname
	 * @return
	 * @throws KServiceException
	 */
	JSONObject modifyNickname(String accessToken, String nickname) throws KServiceException;

	/**
	 * 获得哈邻平台余额
	 *
	 * @param accessToken
	 * @return
	 * @throws KServiceException
	 */
	JSONObject getBalance(String accessToken) throws KServiceException;

	/**
	 * 获得商品配方
	 *
	 * @param accessToken
	 * @return
	 * @throws KServiceException
	 */
	JSONObject getGamePrices(String accessToken) throws KServiceException;

	/**
	 * 获得充值明细
	 */
	JSONObject getRechargeList(String accessToken, int year, int month) throws KServiceException;

	/**
	 * 获得支付明细
	 */
	JSONObject getPayList(String accessToken, int year, int month) throws KServiceException;

	/**
	 * 请求充值订单，同时生成支付订单
	 *
	 * @param accessToken
	 *            用户令牌
	 * @param info
	 *            支付信息
	 * @return
	 * @throws KServiceException
	 */
	JSONObject getChargeOrderForPay(String accessToken, KPayInfo info) throws KServiceException;

	/**
	 * 获得充值订单
	 *
	 * @param accessToken
	 * @param payInfo
	 * @return
	 * @throws KServiceException
	 */
	JSONObject getChargeOrder(String accessToken, KPayInfo payInfo) throws KServiceException;

	/**
	 * 哈豆余额支付
	 *
	 * @param accessToken
	 * @param payInfo
	 * @return
	 * @throws KServiceException
	 */
	JSONObject walletPay(String accessToken, KPayInfo payInfo) throws KServiceException;

	/**
	 * 客户端通知充值成功
	 *
	 * @param accessToken
	 * @param orderNO
	 *            哈邻充值订单号
	 * @param srcNO
	 *            三方订单号
	 * @return
	 * @throws KServiceException
	 */
	JSONObject charge(String accessToken, String orderNO, String srcNO, float amount, Currency currency)
			throws KServiceException;

	/**
	 * 获得游戏充值活动
	 *
	 * @param accessToken
	 * @return
	 * @throws KServiceException
	 */
	JSONObject getPayAcvities(String accessToken) throws KServiceException;

	/**
	 * 游戏退出
	 *
	 * @param accessToken
	 * @return
	 * @throws KServiceException
	 */
	JSONObject exitGame(String accessToken) throws KServiceException;

	/**
	 * 游戏暂停
	 * @param gameCode
	 * @param accessToken
	 * @return
	 * @throws KServiceException
	 */
	JSONObject onStopGame(String gameCode, String accessToken) throws KServiceException;

	/**
	 * 回到游戏
	 * @param gameCode
	 * @param accessToken
	 * @return
	 * @throws KServiceException
	 */
	JSONObject onResumeGame(String gameCode, String accessToken) throws KServiceException;

	/**
	 * 更换角色头像。
	 * @param roleId
	 * @param roleHead
	 * @return
	 * @throws KServiceException
	 */
	JSONObject changeRoleHead(String roleId, String roleHead, String accesstoken) throws KServiceException;


	JSONObject levelUp(String roleId, int level, String accesstoken) throws KServiceException;

	/**
	 * 退出游戏.
	 */
	JSONObject logout(String userId, String roleId, int areaId, String serverName, int level, int money, int gold) throws KServiceException;


	/**
	 * 进入游戏.
	 */
	JSONObject enterGame(String userId, String roleId, int areaId, String serverName, int level, int money, int gold) throws KServiceException;

	JSONObject chargeRecord(String money, String gold, String orderId) throws KServiceException;

	/**
	 * CG步骤.
	 */
	JSONObject cgRecord(int step) throws KServiceException;
	
}
