package koigame.sdk.api;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import koigame.sdk.KConstant;
import koigame.sdk.bean.pay.Currency;
import koigame.sdk.bean.pay.KPayInfo;
import koigame.sdk.bean.user.KUserSession;
import koigame.sdk.util.HHttpConnection;
import koigame.sdk.util.JSONUtils;
import koigame.sdk.util.StringUtils;


public class KWebApiImpl implements KWebApi {

    private static final String TAG = "KWebApiImpl";
    private static KWebApiImpl instance;

    private KWebApiImpl() {
    }

    public static KWebApiImpl instance() {
        if (instance == null) {
            instance = new KWebApiImpl();
        }
        return instance;
    }

    private JSONObject execute(KPostMethod post) throws KServiceException {
        return execute(post, post.getRequestParamList());

    }

    private JSONObject execute(KPostMethod post, List<NameValuePair> list) throws KServiceException {
        String responseString = null;
        try {
            responseString = HHttpConnection.syncConnect(post.getUrl(), list);
            Log.d(TAG, responseString);
        } catch (java.net.UnknownHostException e) {
            throw KServiceException.CONNECT;
        } catch (IOException e) {
            throw KServiceException.CONNECT;
        } catch (Exception e) {
            throw new KServiceException(e.getMessage());
        }
        responseString = URLDecoder.decode(responseString);
        JSONObject props = JSONUtils.build(responseString);
        //KUserSession.instance().setLoginShowWeb(JSONUtils.getBoolean(props, "popup"));
        if (!JSONUtils.isOK(props)) {
            JSONObject errorData = JSONUtils.getJSONObject(props, "data");
            throw new KServiceException(JSONUtils.getInt(errorData, JSONUtils.ERROR_CODE), JSONUtils.getString(errorData,
                    JSONUtils.ERROR_MSG));
        }
        return props;
    }

    private JSONObject executeNodecode(KPostMethod post) throws KServiceException {
        return executeNodecode(post, post.getRequestParamList());

    }

    private JSONObject executeNodecode(KPostMethod post, List<NameValuePair> list) throws KServiceException {
        String responseString = null;
        try {
            responseString = HHttpConnection.syncConnect(post.getUrl(), list);
            Log.d(TAG, responseString);
        } catch (java.net.UnknownHostException e) {
            throw KServiceException.CONNECT;
        } catch (IOException e) {
            throw KServiceException.CONNECT;
        } catch (Exception e) {
            throw new KServiceException(e.getMessage());
        }
        JSONObject props = JSONUtils.build(responseString);
        //KUserSession.instance().setLoginShowWeb(JSONUtils.getBoolean(props, "popup"));
        if (!JSONUtils.isOK(props)) {
            JSONObject errorData = JSONUtils.getJSONObject(props, "data");
            throw new KServiceException(JSONUtils.getInt(errorData, JSONUtils.ERROR_CODE), JSONUtils.getString(errorData,
                    JSONUtils.ERROR_MSG));
        }
        return props;
    }

    @Override
    public JSONObject createAccount(String accountName, String passwd) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/user/registerAccount");
        NameValuePair authValueNV = new BasicNameValuePair(ACCOUNT_NAME, accountName);
        NameValuePair passwdNV = new BasicNameValuePair(PASSWD, passwd);
        NameValuePair accountTypeNV = new BasicNameValuePair(ACCOUNT_TYPE, "2");


        post.setRequestBody(new NameValuePair[]{authValueNV, passwdNV, accountTypeNV});
        return execute(post);
    }

    @Override
    public JSONObject createPhoneAccount(String accountName, String passwd, String code) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/user/registerAccount");
        NameValuePair authValueNV = new BasicNameValuePair(ACCOUNT_NAME, accountName);
        NameValuePair passwdNV = new BasicNameValuePair(PASSWD, passwd);
        NameValuePair codeNV = new BasicNameValuePair(SIGNCODE, code);
        NameValuePair accountTypeNV = new BasicNameValuePair(ACCOUNT_TYPE, "4");


        post.setRequestBody(new NameValuePair[]{authValueNV, passwdNV, accountTypeNV, codeNV});
        return execute(post);
    }

    @Override
    public JSONObject bindAccount(String accessToken, String authValue, String passwd) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/user/bind_account");

        NameValuePair accessTokenNV = new BasicNameValuePair(ACCESS_TOKEN, accessToken);
        NameValuePair authValueNV = new BasicNameValuePair(AUTH_VALUE, authValue);
        NameValuePair passwdNV = new BasicNameValuePair(PASSWD, passwd);

        post.setRequestBody(new NameValuePair[]{accessTokenNV, authValueNV, passwdNV});
        return execute(post);
    }

    @Override
    public JSONObject login(String accountName, String passwd, String accoutType) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/user/login");
        NameValuePair authValueNV = new BasicNameValuePair(ACCOUNT_NAME, accountName);
        NameValuePair passwdNV = new BasicNameValuePair(PASSWD, passwd);
        NameValuePair loginTypeNV = new BasicNameValuePair(ACCOUNT_TYPE, accoutType);
        post.setRequestBody(new NameValuePair[]{authValueNV, passwdNV,loginTypeNV});
        return execute(post);
    }

    @Override
    public JSONObject loginWithToken(String accountName, String token, String refreshToken) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/user/tokenLogin");
        NameValuePair authValueNV = new BasicNameValuePair(ACCOUNT_NAME, accountName + "");
        NameValuePair tokenNV = new BasicNameValuePair(KWebApi.ACCESS_TOKEN, token);
        NameValuePair refreshTokenNV = new BasicNameValuePair(KWebApi.REFRESH_TOKEN, refreshToken);

        post.setRequestBody(new NameValuePair[]{tokenNV, authValueNV, refreshTokenNV});
        return execute(post);
    }

    @Override
    public JSONObject autoLogin(String uid, int authValueType) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/user/autoLogin");
        NameValuePair accessTokenNV = new BasicNameValuePair("token", uid);
        NameValuePair accountnameNV = new BasicNameValuePair("accountName", uid);
        post.setRequestBody(new NameValuePair[]{accountnameNV, accessTokenNV});
        return execute(post);
    }

    @Override
    public JSONObject drawPromoteCode(String accessToken, String code, String roleId, int areaId)
            throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/promote/draw");
        NameValuePair codeNV = new BasicNameValuePair(PROMOTE_CODE, code);
        NameValuePair accessTokenNV = new BasicNameValuePair(ACCESS_TOKEN, accessToken);
        NameValuePair roleIdNV = new BasicNameValuePair(ROLEID, roleId);

        post.setRequestBody(new NameValuePair[]{codeNV, accessTokenNV, roleIdNV});
        return execute(post);
    }

    @Override
    public JSONObject checkPromoteCode(String roleId, String code)
            throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/activity/checkPromoteCode");
        NameValuePair codeNV = new BasicNameValuePair(PROMOTE_CODE, code);
        NameValuePair userIdNV = new BasicNameValuePair(USERID, String.valueOf(KUserSession.instance().getUserInfo().getUserId()));
        NameValuePair tokenNV = new BasicNameValuePair(ACCESS_TOKEN, KUserSession.instance().getUserInfo().getAccessToken());
        NameValuePair roleIdNV = new BasicNameValuePair(ROLEID, KUserSession.instance().getUserInfo().getRoleId());

        post.setRequestBody(new NameValuePair[]{codeNV, userIdNV, tokenNV, roleIdNV});
        return execute(post);
    }

    @Override
    public JSONObject checkActive(String userId) throws KServiceException {
        // TODO Auto-generated method stub
        KPostMethod post = new KPostMethod(KConstant.URL + "/activity/checkUserActive");
        NameValuePair useridNV = new BasicNameValuePair(ACCESS_TOKEN, KUserSession.instance().getUserInfo().getAccessToken());
        NameValuePair accessTokenNV = new BasicNameValuePair(USERID, userId);

        post.setRequestBody(new NameValuePair[]{useridNV, accessTokenNV});
        return execute(post);
    }


    @Override
    public JSONObject modifyPassword(String accountName, String accountType, String pwd, String newPwd) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/user/public/modifyPassword");

        NameValuePair accountNameNV = new BasicNameValuePair(ACCOUNT_NAME, accountName);
        NameValuePair accountTypeNV = new BasicNameValuePair(ACCOUNT_TYPE, accountType);
        NameValuePair passwdNV = new BasicNameValuePair(PASSWD, pwd);
        NameValuePair newPasswdNV = new BasicNameValuePair(NEWPASSWD, newPwd);

        post.setRequestBody(new NameValuePair[]{accountNameNV, accountTypeNV, passwdNV, newPasswdNV});
        return execute(post);
    }

    @Override
    public JSONObject modifyNickname(String accessToken, String nickname) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/user/modify_nickname");

        NameValuePair accessTokenNV = new BasicNameValuePair(ACCESS_TOKEN, accessToken);
        NameValuePair nicknameNV = new BasicNameValuePair(NICKNAME, URLEncoder.encode(nickname));

        post.setRequestBody(new NameValuePair[]{accessTokenNV, nicknameNV});
        return execute(post);
    }

    @Override
    public JSONObject getBalance(String accessToken) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/bill/getGoodsList");

        NameValuePair accessTokenNV = new BasicNameValuePair(ACCESS_TOKEN, accessToken);
        post.setRequestBody(new NameValuePair[]{accessTokenNV});
        return execute(post);
    }

    @Override
    public JSONObject getGamePrices(String accessToken) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/billing/getGoodsList");
        NameValuePair roleIdNV = new BasicNameValuePair(ROLEID, KUserSession.instance().getUserInfo().getRoleId());
        NameValuePair shopTypeNV = new BasicNameValuePair("shopType", "1");
        NameValuePair accessTokenNV = new BasicNameValuePair(KWebApi.ACCESS_TOKEN, accessToken);
        post.setRequestBody(new NameValuePair[]{accessTokenNV,roleIdNV,shopTypeNV});
        return execute(post);
    }

    @Override
    public JSONObject getChargeOrderForPay(String accessToken, KPayInfo payInfo) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/billing/createOrder");
        NameValuePair accessTokenNV = new BasicNameValuePair(ACCESS_TOKEN, accessToken);
        NameValuePair productIdNV = new BasicNameValuePair(GOODSCODE, payInfo.getGoodsCode());
        NameValuePair productNameNV = new BasicNameValuePair(PRODUCT_NAME, payInfo.getGoodsName());
        NameValuePair payDescNV = new BasicNameValuePair(ORDERDESC, payInfo.getOrderDesc());
        NameValuePair roleIdNV = new BasicNameValuePair(ROLEID, KUserSession.instance().getUserInfo().getRoleId());
        NameValuePair userIdNV = new BasicNameValuePair(USERID, KUserSession.instance().getUserInfo().getUserId() + "");
        NameValuePair payAmountNV = new BasicNameValuePair(PAYAMOUNT, payInfo.getPayAmount() + "");
        NameValuePair goodsCountNV = new BasicNameValuePair(GOODSCOUNT, payInfo.getGoodsCount() + "");
        NameValuePair payType = new BasicNameValuePair(PAYTYPE, payInfo.getPayType() + "");
        NameValuePair currency = new BasicNameValuePair(CURRENCY, "2");
        post.setRequestBody(new NameValuePair[]{accessTokenNV, productIdNV, productNameNV, payDescNV, payType,
                currency, roleIdNV, userIdNV, payAmountNV, goodsCountNV});
        return executeNodecode(post);
    }

    @Override
    public JSONObject getChargeOrder(String accessToken, KPayInfo payInfo) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/order/get_charge");

        NameValuePair accessTokenNV = new BasicNameValuePair(ACCESS_TOKEN, accessToken);
        NameValuePair currencyNV = new BasicNameValuePair(CURRENCY, payInfo.getCurrency().getCode());

        post.setRequestBody(new NameValuePair[]{accessTokenNV, currencyNV});
        return execute(post);
    }

    @Override
    public JSONObject getRechargeList(String accessToken, int year, int month) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/order/recharge_records");

        NameValuePair accessTokenNV = new BasicNameValuePair(ACCESS_TOKEN, accessToken);
        NameValuePair yearNV = new BasicNameValuePair(YEAR, Integer.valueOf(year).toString());
        NameValuePair monthNV = new BasicNameValuePair(MONTH, Integer.valueOf(month).toString());
        post.setRequestBody(new NameValuePair[]{accessTokenNV, yearNV, monthNV});

        return execute(post);
    }

    @Override
    public JSONObject getPayList(String accessToken, int year, int month) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/order/pay_records");

        NameValuePair accessTokenNV = new BasicNameValuePair(ACCESS_TOKEN, accessToken);
        NameValuePair yearNV = new BasicNameValuePair(YEAR, Integer.valueOf(year).toString());
        NameValuePair monthNV = new BasicNameValuePair(MONTH, Integer.valueOf(month).toString());
        post.setRequestBody(new NameValuePair[]{accessTokenNV, yearNV, monthNV});

        return execute(post);
    }

    @Override
    public JSONObject walletPay(String accessToken, KPayInfo payInfo) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/bill/walletpay");

        return execute(post);
    }

    @Override
    public JSONObject charge(String accessToken, String orderNO, String srcNO, float amount, Currency currency)
            throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/bill/charge");

        NameValuePair accessTokenNV = new BasicNameValuePair(ACCESS_TOKEN, accessToken);
        NameValuePair orderNoNV = new BasicNameValuePair(ORDERNO, orderNO);
        NameValuePair srcNoNV = new BasicNameValuePair(SRCNO, srcNO);
        NameValuePair currencyNV = new BasicNameValuePair(CURRENCY, currency.getCode());
        post.setRequestBody(new NameValuePair[]{accessTokenNV, orderNoNV, srcNoNV, currencyNV});
        return execute(post);
    }

    @Override
    public JSONObject getPayAcvities(String accessToken) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/get_pay_activities");

        NameValuePair accessTokenNV = new BasicNameValuePair(ACCESS_TOKEN, accessToken);
        post.setRequestBody(new NameValuePair[]{accessTokenNV});
        return execute(post);
    }

    @Override
    public JSONObject exitGame(String accessToken) throws KServiceException {
        // TODO Auto-generated method stub
        KPostMethod post = new KPostMethod(KConstant.URL + "/user/logout");

        NameValuePair accessTokenNV = new BasicNameValuePair(ACCESS_TOKEN, accessToken);

        post.setRequestBody(new NameValuePair[]{accessTokenNV});

        return execute(post);
    }

    @Override
    public JSONObject onStopGame(String gameId, String accessToken)
            throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/user/onStop");

        NameValuePair code = new BasicNameValuePair(GAMEID, gameId);

        NameValuePair accessTokenNV = new BasicNameValuePair(ACCESS_TOKEN, accessToken);

        post.setRequestBody(new NameValuePair[]{accessTokenNV, code});

        return execute(post);
    }

    @Override
    public JSONObject onResumeGame(String gameId, String accessToken)
            throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/user/onResume");

        NameValuePair code = new BasicNameValuePair(GAMEID, gameId);

        NameValuePair accessTokenNV = new BasicNameValuePair(ACCESS_TOKEN, accessToken);

        post.setRequestBody(new NameValuePair[]{accessTokenNV, code});

        return execute(post);
    }

    @Override
    public JSONObject activeDevice() throws KServiceException {
        // TODO Auto-generated method stub
        KPostMethod post = new KPostMethod(KConstant.URL + "/game/public/activeDevice");
        post.setRequestBody(new NameValuePair[]{});
        return execute(post);
    }

    @Override
    public JSONObject getDispatcherAccountName() throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/user/public/dispatcherAccountName");
        post.setRequestBody(new NameValuePair[]{});
        return execute(post);
    }

    @Override
    public JSONObject sendSmsToUser(String accountName) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/user/public/sendSmsToUser");
        NameValuePair accountNameNV = new BasicNameValuePair(ACCOUNT_NAME, accountName);
        post.setRequestBody(new NameValuePair[]{accountNameNV});
        return execute(post);
    }

    @Override
    public JSONObject bindPhoneAccount(String signCode, String accountName, String userId) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/user/bindPhoneAccount");
        NameValuePair signCodeNV = new BasicNameValuePair(SIGNCODE, signCode);
        NameValuePair accountNameNV = new BasicNameValuePair(ACCOUNT_NAME, accountName);
        NameValuePair userIdNV = new BasicNameValuePair(USERID, userId);
        NameValuePair accessTokenNV = new BasicNameValuePair(ACCESS_TOKEN, KUserSession.instance().getUserInfo().getAccessToken());
        post.setRequestBody(new NameValuePair[]{signCodeNV, accountNameNV, userIdNV, accessTokenNV});
        return execute(post);
    }

    @Override
    public JSONObject updatePasswordBySms(String signCode, String accountName, String phoneNum, String password, String accountType) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/user/public/updatePasswordBySms");
        NameValuePair signCodeNV = new BasicNameValuePair(SIGNCODE, signCode);
        NameValuePair accountNameNV = new BasicNameValuePair(ACCOUNT_NAME, accountName);
        NameValuePair updateStatusNV = new BasicNameValuePair(NEWPASSWD, password);
        NameValuePair phoneNV = new BasicNameValuePair(PHONENUM, phoneNum);
        NameValuePair accountTypeNV = new BasicNameValuePair(ACCOUNT_TYPE, accountType);
        post.setRequestBody(new NameValuePair[]{updateStatusNV, signCodeNV, accountNameNV, phoneNV,accountTypeNV});
        return execute(post);
    }

    @Override
    public JSONObject checkBindIdCard(String idCard, String realName) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/user/checkBindIdCard");
        NameValuePair idCardNV = new BasicNameValuePair("idCard", idCard);
        NameValuePair realNameNV = new BasicNameValuePair("realName", realName);
        NameValuePair userIdNV = new BasicNameValuePair(USERID, KUserSession.instance().getUserInfo().getUserId() + "");
        NameValuePair tokenNV = new BasicNameValuePair(ACCESS_TOKEN, KUserSession.instance().getUserInfo().getAccessToken());
        post.setRequestBody(new NameValuePair[]{idCardNV, realNameNV, userIdNV, tokenNV});
        return execute(post);
    }

    @Override
    public JSONObject updateClient(int updateStatus) throws KServiceException {
        // TODO Auto-generated method stub
        KPostMethod post = new KPostMethod(KConstant.URL + "/game/public/updateClient");
        NameValuePair updateStatusNV = new BasicNameValuePair("updateStatus", String.valueOf(updateStatus));
        post.setRequestBody(new NameValuePair[]{updateStatusNV});
        return execute(post);
    }

    @Override
    public JSONObject loginUI(String costTime) throws KServiceException {
        // TODO Auto-generated method stub
        KPostMethod post = new KPostMethod(KConstant.URL + "/game/public/loginUI");
        NameValuePair costTimeNV = new BasicNameValuePair("costTime", String.valueOf(costTime));
        post.setRequestBody(new NameValuePair[]{costTimeNV});
        return execute(post);
    }


    @Override
    public JSONObject createRoleEvent(int areaId, long userId, String roleId,
                                      String roleName) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/user/createRoleEvent");
        NameValuePair areaIdNV = new BasicNameValuePair(AREAID, String.valueOf(areaId));
        NameValuePair userIdNV = new BasicNameValuePair(USERID, String.valueOf(userId));
        NameValuePair roleIdNV = new BasicNameValuePair(ROLENAME, roleId);
        post.setRequestBody(new NameValuePair[]{areaIdNV, userIdNV, roleIdNV});
        return execute(post);
    }

    @Override
    public JSONObject loadGame(String arriveTime, long userId, String costTime)
            throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/game/public/loadGame");
        NameValuePair areaIdNV = new BasicNameValuePair("operateTime", arriveTime);
        NameValuePair userIdNV = new BasicNameValuePair(ACCOUNT_NAME, KUserSession.instance().getUserInfo().getAccountName());
        NameValuePair roleIdNV = new BasicNameValuePair("costTime", costTime);
        post.setRequestBody(new NameValuePair[]{areaIdNV, userIdNV, roleIdNV});
        return execute(post);
    }

    @Override
    public JSONObject changeRoleHead(String roleId, String roleHead, String accesstoken)
            throws KServiceException {
        // TODO Auto-generated method stub
        KPostMethod post = new KPostMethod(KConstant.URL + "/game/changeRoleHead");
        NameValuePair roleIdNV = new BasicNameValuePair(ROLEID, roleId);
        NameValuePair roleHeadNv = new BasicNameValuePair("roleHead", roleHead);
        NameValuePair tokenNv = new BasicNameValuePair(ACCESS_TOKEN, accesstoken);
        NameValuePair useridNv = new BasicNameValuePair(USERID, KUserSession.instance().getUserInfo().getUserId() + "");
        post.setRequestBody(new NameValuePair[]{roleIdNV, roleHeadNv, tokenNv, useridNv});
        Log.i(TAG, "changeRoleHead");
        return execute(post);
    }

    @Override
    public JSONObject levelUp(String roleId, int level, String accesstoken) throws KServiceException {
        // TODO Auto-generated method stub
        KPostMethod post = new KPostMethod(KConstant.URL + "/game/recordRoleLevel");
        if (StringUtils.isEmpty(roleId)) {
            roleId = "0";
        }
        NameValuePair roleIdNV = new BasicNameValuePair(ROLEID, roleId);
        NameValuePair roleHeadNv = new BasicNameValuePair("roleLevel", String.valueOf(level));
        NameValuePair tokenNv = new BasicNameValuePair(ACCESS_TOKEN, accesstoken);
        NameValuePair useridNv = new BasicNameValuePair(USERID, KUserSession.instance().getUserInfo().getUserId() + "");
        post.setRequestBody(new NameValuePair[]{roleIdNV, roleHeadNv, tokenNv, useridNv});
        Log.i(TAG, "levelUp");
        return execute(post);
    }

    @Override
    public JSONObject logout(String userId, String roleId, int areaId, String serverName, int level, int money, int gold) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/game/logout");
        NameValuePair roleIdNV = new BasicNameValuePair(ROLEID, roleId);
        NameValuePair roleHeadNv = new BasicNameValuePair("level", String.valueOf(level));
        NameValuePair roleNameNV = new BasicNameValuePair(ROLENAME, KUserSession.instance().getUserInfo().getRoleName());
        NameValuePair tokenNv = new BasicNameValuePair(ACCESS_TOKEN, KUserSession.instance().getUserInfo().getAccessToken());
        NameValuePair useridNv = new BasicNameValuePair(USERID, KUserSession.instance().getSdkUserId() + "");
        NameValuePair moneyNv = new BasicNameValuePair("money", money + "");
        NameValuePair goldNv = new BasicNameValuePair("gold", gold + "");
        post.setRequestBody(new NameValuePair[]{roleIdNV, roleHeadNv, tokenNv, useridNv, moneyNv, goldNv, roleNameNV});
        return execute(post);
    }

    @Override
    public JSONObject enterGame(String userId, String roleId, int areaId, String serverName, int level, int money, int gold) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/game/enterGame");
        NameValuePair roleIdNV = new BasicNameValuePair(ROLEID, roleId);
        NameValuePair roleNameNV = new BasicNameValuePair(ROLENAME, KUserSession.instance().getUserInfo().getRoleName());
        NameValuePair roleHeadNv = new BasicNameValuePair("level", String.valueOf(level));
        NameValuePair tokenNv = new BasicNameValuePair(ACCESS_TOKEN, KUserSession.instance().getUserInfo().getAccessToken());
        NameValuePair useridNv = new BasicNameValuePair(USERID, KUserSession.instance().getSdkUserId() + "");
        NameValuePair moneyNv = new BasicNameValuePair("money", money + "");
        NameValuePair goldNv = new BasicNameValuePair("gold", gold + "");
        post.setRequestBody(new NameValuePair[]{roleIdNV, roleHeadNv, tokenNv, useridNv, moneyNv, goldNv, roleNameNV});
        return execute(post);
    }

    @Override
    public JSONObject chargeRecord(String money, String gold, String orderId) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/game/chargeRecord");
        NameValuePair roleIdNV = new BasicNameValuePair(ROLEID, KUserSession.instance().getUserInfo().getRoleId());
        NameValuePair roleNameNV = new BasicNameValuePair(ROLENAME, KUserSession.instance().getUserInfo().getRoleName());
        NameValuePair roleHeadNv = new BasicNameValuePair("level", KUserSession.instance().getUserInfo().getRoleLevel() + "");
        NameValuePair tokenNv = new BasicNameValuePair(ACCESS_TOKEN, KUserSession.instance().getUserInfo().getAccessToken());
        NameValuePair useridNv = new BasicNameValuePair(USERID, KUserSession.instance().getSdkUserId() + "");
        NameValuePair moneyNv = new BasicNameValuePair("money", money);
        NameValuePair goldNv = new BasicNameValuePair("gold", gold );
        post.setRequestBody(new NameValuePair[]{roleIdNV, roleHeadNv, tokenNv, useridNv, moneyNv, goldNv, roleNameNV});
        return execute(post);
    }

    @Override
    public JSONObject cgRecord(int step) throws KServiceException {
        KPostMethod post = new KPostMethod(KConstant.URL + "/game/public/cgRecord");
        NameValuePair stepNV = new BasicNameValuePair("step", step + "");
        post.setRequestBody(new NameValuePair[]{stepNV});
        return execute(post);
    }


}
