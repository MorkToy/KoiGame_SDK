package koigame.sdk.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import koigame.sdk.api.KServiceException;
import koigame.sdk.api.KThread;
import koigame.sdk.api.KWebApiImpl;
import koigame.sdk.bean.user.KLoginEvent;
import koigame.sdk.bean.user.KLoginInfo;
import koigame.sdk.bean.user.KUserInfo;
import koigame.sdk.bean.user.KUserSession;
import koigame.sdk.util.AndroidUtils;
import koigame.sdk.util.JSONUtils;
import koigame.sdk.util.NumberUtils;
import koigame.sdk.util.RUtils;
import koigame.sdk.util.StringUtils;


public class KoiLoginActivity extends HActivityBase {
    public final String TAG = "koi_login";

    private static final int LOGIN_FAIL = 3;
    private static final int REGISTER_FAIL = 4;
    private static final int FORGETPWDSUCCESS = 21;


    //隐藏和显示切换动画
    /*private TranslateAnimation mShowAction;
    private TranslateAnimation mHiddenAction;*/

    private LinearLayout koi_main_layout;
    private LinearLayout koi_qcuikregister_layout;
    private LinearLayout koi_phoneregister1_layout;
    private LinearLayout koi_normalregister_layout;
    private LinearLayout koi_phoneregister2_layout;
    private LinearLayout koi_layout_register;
    private LinearLayout koi_layout_login;
    private LinearLayout koi_layout_fogetpwd;

    private EditText koi_edt_account;
    private EditText koi_edt_quick_pwd;
    private EditText koi_edt_phAccount;



    private TextView koi_tv_comback;

    /*protected Map<String, LinearLayout> layoutIndex;*/


    private EditText koi_edt_phoneReg_num;
    private EditText koi_edt_checkReg_code;
    private EditText koi_edt_phoneReg_pwd;
    private TextView koi_edt_normalReg_name;
    private TextView koi_edt_normalReg_pwd;
    private TextView koi_edt_normalReg_repwd;
    private EditText koi_edt_login_name;
    private EditText koi_edt_login_pwd;

    private Button koi_btn_resendreg;
    private CountDownTimer timer;
    private boolean initTimer;
    private EditText koi_edt_forget_name;
    private EditText koi_edt_forget_phone;
    private EditText koi_edt_forget_code;
    private EditText koi_edt_forget_newpwd;
    private Button koi_btn_forget_send;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(RUtils.getLayoutId("koi_account"));
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        ViewGroup.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (dm.heightPixels * 0.48);
        p.width = (int) (dm.widthPixels * 0.81);
        initView();
    }

	/*-------------------------------页面跳转按钮事件 START--------------------------------*/

    /**
     * 进入快速注册界面
     */
    public void quickRegister(View view) {
        switchAnim(koi_main_layout, koi_qcuikregister_layout);
    }

    /**
     * 进入注册账号页面
     */
    public void normalRigsiter(View view) {
        switchAnim(koi_main_layout, koi_normalregister_layout);
    }

    /**
     * 进入账号登录页面
     */
    public void loginAccount(View view) {
        switchAnim(koi_main_layout, koi_layout_login);
    }

    /**
     * 手机号注册按钮触发事件第一步.
     */
    public void phoneRegister(View view) {
        switchAnim(koi_normalregister_layout, koi_phoneregister2_layout);
    }

    /**
     * 手机号注册按钮触发事件第二步(这一步暂时不要了).
     */
    public void sendPhoneCode(View view) {
        String phoneNum = koi_edt_phAccount.getText().toString();
        if (!NumberUtils.checkPhoneNumber(phoneNum)) {
            koi_edt_phAccount.setError(getResources().getString(RUtils.getStringId("koi_text_phonenum_error")));
            koi_edt_phAccount.requestFocus();
            return;
        }
        sendPhoneCodeCommit(phoneNum);
    }

    /**
     * 忘记密码，发送短信.
     * @param view
     */
    public void forgetPwdSend(View view) {
        String phoneNum = koi_edt_forget_phone.getText().toString();
        if (!NumberUtils.checkPhoneNumber(phoneNum)) {
            koi_edt_forget_phone.setError(getResources().getString(RUtils.getStringId("koi_text_phonenum_error")));
            koi_edt_forget_phone.requestFocus();
            return;
        }
        sendPhoneCodeCommit(phoneNum);
        initForgetPwdTimerClick();
    }

    /**
     * 重新发送短信验证码.
     */
    public void reSendCode(View view) {
        String phoneNum = koi_edt_phoneReg_num.getText().toString();
        if (!NumberUtils.checkPhoneNumber(phoneNum)) {
            koi_edt_phoneReg_num.setError(getResources().getString(RUtils.getStringId("koi_text_phonenum_error")));
            koi_edt_phoneReg_num.requestFocus();
            return;
        }
        sendPhoneCodeCommit(phoneNum);
        initPhoneRegisterTimerClick();
    }

    /**
     * 账号注册按钮触发事件.
     */
    public void accountRegister(View view) {
        switchAnim(koi_normalregister_layout, koi_layout_register);
    }

    /**
     * 忘记密码按钮触发事件.
     */
    public void forgetPwd(View view) {
        switchAnim(koi_layout_login, koi_layout_fogetpwd);
    }
	
	/*-------------------------------页面跳转按钮事件 END--------------------------------*/


    /**
     * 通知服务器快速注册.
     */
    public void quickRegisterCommit(View view) {
        final String screenName = koi_edt_account.getEditableText().toString().trim();
        final String password = koi_edt_quick_pwd.getEditableText().toString().trim();

        if (!NumberUtils.checkUsername(screenName)) {
            koi_edt_account.setError(getResources().getString(RUtils.getStringId("koi_register_usn_check")));
            koi_edt_account.requestFocus();
            return;
        }
        if (!NumberUtils.checkPassword(password)) {
            koi_edt_quick_pwd.setError(getResources().getString(RUtils.getStringId("koi_register_pwd_check")));
            koi_edt_quick_pwd.requestFocus();
            return;
        }
        if (AndroidUtils.isIncludeErrorWords(getApplicationContext(), screenName)) {
            koi_edt_account.setError(getResources().getString(RUtils.getStringId("koi_account_has_error_words")));
            koi_edt_account.requestFocus();
            return;
        }
        doRegister(screenName, password, null);
    }

    /**
     * 手机号注册.
     */
    public void phoneRegisterCommit(View view) {
        final String screenName = koi_edt_phoneReg_num.getEditableText().toString().trim();
        final String password = koi_edt_phoneReg_pwd.getEditableText().toString().trim();
        final String checkCode = koi_edt_checkReg_code.getEditableText().toString().trim();
        if (!NumberUtils.checkPhoneNumber(screenName)) {
            koi_edt_phoneReg_num.setError(getResources().getString(RUtils.getStringId("koi_text_phonenum_error")));
            koi_edt_phoneReg_num.requestFocus();
            return;
        }
        if (!NumberUtils.checkPassword(password)) {
            koi_edt_phoneReg_pwd.setError(getResources().getString(RUtils.getStringId("koi_register_pwd_check")));
            koi_edt_phoneReg_pwd.requestFocus();
            return;
        }
        if (StringUtils.isEmpty(checkCode)) {
            koi_edt_checkReg_code.setError(getResources().getString(RUtils.getStringId("koi_pwd_cannot_null")));
            return;
        }
        doRegister(screenName, password, checkCode);
    }
    // koi_edt_normalReg_name
    public void normalRigsiterCommit(View view) {
        final String screenName = koi_edt_normalReg_name.getEditableText().toString().trim();
        final String password = koi_edt_normalReg_pwd.getEditableText().toString().trim();
        final String repassword = koi_edt_normalReg_repwd.getEditableText().toString().trim();
        if (!NumberUtils.checkUsername(screenName)) {
            koi_edt_normalReg_name.setError(getResources().getString(RUtils.getStringId("koi_register_usn_check")));
            koi_edt_normalReg_name.requestFocus();
            return;
        }
        if (!NumberUtils.checkPassword(password)) {
            koi_edt_normalReg_pwd.setError(getResources().getString(RUtils.getStringId("koi_register_pwd_check")));
            koi_edt_normalReg_pwd.requestFocus();
            return;
        }
        if (!password.equals(repassword)) {
            koi_edt_normalReg_repwd.setError(getResources().getString(RUtils.getStringId("koi_pwd_repeat_error")));
            koi_edt_normalReg_repwd.requestFocus();
            return;
        }
        doRegister(screenName, password, null);
    }

    /**
     * 正常登陆.
     */
    public void loginCommit(View view) {
        String screenName = koi_edt_login_name.getEditableText().toString().trim();
        String password = koi_edt_login_pwd.getEditableText().toString().trim();
        String accountType = "2";
        if (!NumberUtils.checkUsername(screenName) && !NumberUtils.checkPhoneNumber(screenName)) {
            koi_edt_login_name.setError(getResources().getString(RUtils.getStringId("koi_text_account_error")));
            koi_edt_login_name.requestFocus();
            return;
        }
        if (!NumberUtils.checkUsername(password)) {
            koi_edt_login_pwd.setError(getResources().getString(RUtils.getStringId("koi_register_pwd_check")));
            koi_edt_login_pwd.requestFocus();
            return;
        }
        if (NumberUtils.checkPhoneNumber(screenName)) {
            accountType = "4";
        }
        doLogin(screenName, password, accountType);
    }

    /**
     * 忘记密码.
     */
    public void forgetPwdCommit(View view) {
        String screenName = koi_edt_forget_name.getEditableText().toString().trim();
        String phoneNum = koi_edt_forget_phone.getEditableText().toString().trim();
        String signCode = koi_edt_forget_code.getEditableText().toString().trim();
        String newPwd = koi_edt_forget_newpwd.getEditableText().toString().trim();
        String accountType = "2";

        if (!NumberUtils.checkUsername(screenName)) {
            koi_edt_forget_name.setError(getResources().getString(RUtils.getStringId("koi_register_usn_check")));
            koi_edt_forget_name.requestFocus();
            return;
        }

        if (!NumberUtils.checkPhoneNumber(phoneNum)) {
            koi_edt_forget_phone.setError(getResources().getString(RUtils.getStringId("koi_text_phonenum_error")));
            koi_edt_forget_phone.requestFocus();
            return;
        }

        if (StringUtils.isEmpty(signCode)) {
            koi_edt_forget_code.setError(getResources().getString(RUtils.getStringId("koi_pwd_cannot_null")));
            koi_edt_forget_code.requestFocus();
            return;
        }

        if (!NumberUtils.checkPassword(newPwd)) {
            koi_edt_forget_newpwd.setError(getResources().getString(RUtils.getStringId("koi_register_pwd_check")));
            koi_edt_forget_newpwd.requestFocus();
            return;
        }

        if (NumberUtils.checkPhoneNumber(screenName)) {
            accountType = "4";
        }

        doModifyPwdBySms(screenName, phoneNum, newPwd,signCode,accountType);
    }

    /**
     * 向服务器请求修改密码.
     * @param accountName 账号
     * @param pwd 密码
     * @param signCode 验证码
     */
    public void doModifyPwdBySms(final String accountName,final String phoneNum, final String pwd, final String signCode, final String accountTyle) {

        KThread progressThread = new KThread() {
            public void run() {
                try {
                    AndroidUtils.showCicleProgress(KoiLoginActivity.this, getResources().getString(RUtils.getStringId("koi_hint_fogget_loading")));

                    JSONObject props = KWebApiImpl.instance().updatePasswordBySms(signCode, accountName, phoneNum, pwd, accountTyle);
                    AndroidUtils.closeCiclePorgress(KoiLoginActivity.this);
                    if (isDestory()) {
                        return;
                    }
                    Message msg = new Message();
                    msg.what = FAIL;
                    if (JSONUtils.isOK(props)) {
                        msg.what = FORGETPWDSUCCESS;
                    }
                    msg.obj = props;
                    mHandler.sendMessage(msg);
                } catch (KServiceException e) {
                    e.printStackTrace();
                    AndroidUtils.closeCiclePorgress(KoiLoginActivity.this);
                    if (isDestory()) {
                        return;
                    }
                    Message msg = new Message();
                    msg.what = FAIL;
                    msg.obj = e;
                    mHandler.sendMessage(msg);
                }
            }
        };
        progressThread.start();
    }

    /**
     * 向服务器请求登录
     * @param screenName 账号
     * @param password 密码
     * @param acccountType 登录类型
     */
    public void doLogin(final String screenName, final String password, final String acccountType) {
        KThread progressThread = new KThread() {
            public void run() {
                try {
                    AndroidUtils.showCicleProgress(KoiLoginActivity.this, getResources().getString(RUtils.getStringId("koi_login_loading_tip")));
                    JSONObject props = KWebApiImpl.instance().login(screenName, password, acccountType);
                    AndroidUtils.closeCiclePorgress(KoiLoginActivity.this);
                    if (isDestory()) {
                        return;
                    }
                    Message msg = new Message();
                    msg.what = SUCCESS;
                    msg.obj = props;
                    mHandler.sendMessage(msg);
                } catch (KServiceException e) {
                    e.printStackTrace();
                    AndroidUtils.closeCiclePorgress(KoiLoginActivity.this);
                    if (isDestory()) {
                        return;
                    }
                    Message msg = new Message();
                    msg.what = LOGIN_FAIL;
                    msg.obj = e;
                    mHandler.sendMessage(msg);
                }
            }
        };
        progressThread.start();
    }

    /**
     * 向服务器请求注册.
     * @param screenName 账号
     * @param password 密码
     * @param signCode 验证码
     */
    public void doRegister(final String screenName, final String password, final String signCode) {
        KThread progressThread = new KThread() {
            public void run() {
                try {
                    AndroidUtils.showCicleProgress(KoiLoginActivity.this, getResources().getString(RUtils.getStringId("koi_register_loading_tip")));
                    JSONObject props = null;
                    if (StringUtils.isEmpty(signCode)) {
                        props = KWebApiImpl.instance().createAccount(screenName, password);
                    } else {
                        props = KWebApiImpl.instance().createPhoneAccount(screenName, password, signCode);
                    }
                    AndroidUtils.closeCiclePorgress(KoiLoginActivity.this);
                    if (isDestory()) {
                        return;
                    }
                    Message msg = new Message();
                    msg.what = SUCCESS;
                    msg.obj = props;
                    mHandler.sendMessage(msg);
                } catch (KServiceException e) {
                    e.printStackTrace();
                    AndroidUtils.closeCiclePorgress(KoiLoginActivity.this);
                    if (isDestory()) {
                        return;
                    }
                    Message msg = new Message();
                    msg.what = REGISTER_FAIL;
                    msg.obj = e;
                    mHandler.sendMessage(msg);
                }
            }
        };
        progressThread.start();
    }

    /**
     * 手机号码注册第一步，通知服务器发送验证码.
     *
     * @param accountName 手机号.
     */
    public void sendPhoneCodeCommit(final String accountName) {
        KThread progressThread = new KThread() {
            @Override
            public void run() {
                try {
                    AndroidUtils.showCicleProgress(KoiLoginActivity.this, getResources().getString(RUtils.getStringId("koi_code_send")));

                    JSONObject props = KWebApiImpl.instance().sendSmsToUser(accountName);

                    AndroidUtils.closeCiclePorgress(KoiLoginActivity.this);

                    Message msg = new Message();
                    msg.what = SENDSMSFAIL;
                    if (JSONUtils.isOK(props)) {
                        msg.what = SENDSMSSUCCESS;
                    }
                    msg.obj = props;
                    mHandler.sendMessage(msg);
                } catch (KServiceException e) {
                    e.printStackTrace();
                    AndroidUtils.closeCiclePorgress(KoiLoginActivity.this);
                    if (isDestory()) {
                        return;
                    }
                    Message msg = new Message();
                    msg.what = REGISTER_FAIL;
                    msg.obj = e;
                    mHandler.sendMessage(msg);
                }
            }
        };
        progressThread.start();
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            try {
                AndroidUtils.closeCiclePorgress(KoiLoginActivity.this);

                switch (msg.what) {
                    case SUCCESS: {
                        JSONObject props = (JSONObject) msg.obj;
                        Log.i(TAG, "login result:" + props.toString());
                        final KUserInfo userInfo = new KUserInfo(props);
                        KUserSession.instance().saveToPreference(userInfo);
                        KUserSession.instance().setUserInfo(userInfo);
                        KLoginEvent.onLoginSuccess(new KLoginInfo(props));
                        finish();
                        break;
                    }
                    case LOGIN_FAIL: {
                        KServiceException e = (KServiceException) msg.obj;
						if (e.getValue() == 201) {
                            koi_edt_login_name.setError(e.getName());
                            koi_edt_login_name.requestFocus();
						} else if (e.getValue() == 206) {
                            koi_edt_login_pwd.setError(e.getName());
                            koi_edt_login_pwd.requestFocus();
						} else {
							String text = StringUtils.isEmpty(e.getName()) ? getResources().getString(
									RUtils.getStringId("koi_system_error")) : e.getName();
							AndroidUtils.showToast(KoiLoginActivity.this, text, Toast.LENGTH_LONG);
						}
                        break;
                    }
                    case REGISTER_FAIL: {
                        KServiceException e = (KServiceException) msg.obj;
                        if (e.getValue() == 211 || e.getValue() == 224) {
                            koi_edt_account.setError(e.getName());
                            koi_edt_account.requestFocus();
                        } else if (e.getValue() == 215) {
                            koi_edt_quick_pwd.setError(e.getName());
                            koi_edt_quick_pwd.requestFocus();
                        } else {
                            String text = StringUtils.isEmpty(e.getName()) ? getResources().getString(
                                    RUtils.getStringId("koi_system_error")) : e.getName();
                            AndroidUtils.showToast(KoiLoginActivity.this, text, Toast.LENGTH_LONG);
                        }
                        break;
                    }
                    case SENDSMSSUCCESS: {
                        //switchAnim(koi_phoneregister1_layout, koi_phoneregister2_layout);
                        AndroidUtils.showToast(KoiLoginActivity.this, getResources().getString(RUtils.getStringId("koi_text_sendsms_success")), 1);
                        initPhoneRegisterTimerClick();
                        break;
                    }
                    case SENDSMSFAIL: {
                        AndroidUtils.showToast(KoiLoginActivity.this, getResources().getString(RUtils.getStringId("koi_text_sendsms_error")), 1);
                        break;
                    }
                    case FORGETPWDSUCCESS: {
                        AndroidUtils.showToast(KoiLoginActivity.this, getResources().getString(RUtils.getStringId("koi_hint_fogget_success")), 1);
                        comeback(null);
                        break;
                    }
                    case FAIL:
                        AndroidUtils.showToast(KoiLoginActivity.this, getResources().getString(RUtils.getStringId("koi_system_login_error")), 1);
                        break;
                }

                super.handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 切换动画.
     */
    public void switchAnim(LinearLayout current, LinearLayout next) {
        current.startAnimation(mHiddenAction);
        current.setVisibility(View.GONE);

        next.startAnimation(mShowAction);
        next.setVisibility(View.VISIBLE);

        recordSwitch(next);
        Log.i(TAG, "当前页面为 " + pageIndex);
    }


    /**
     * 回退动画.
     */
    public void comeback(View view) {
        Log.i(TAG, "回退动画 当前页面为 " + pageIndex);
        currentIndexLy = layoutIndex.get("PAGE" + pageIndex);
        lastIndexLy = layoutIndex.get("PAGE" + (pageIndex - 1));

        currentIndexLy.startAnimation(mHiddenAction);
        currentIndexLy.setVisibility(View.GONE);

        lastIndexLy.startAnimation(mShowAction);
        lastIndexLy.setVisibility(View.VISIBLE);

        if (lastIndexLy == koi_main_layout) {
            koi_tv_comback.setVisibility(View.GONE);
        }
        removeRecord();
        //timer.cancel();
        Log.i(TAG, "回退后 当前页面为 " + pageIndex);
    }

    /**
     * 记录每次页面跳转，方便回退页面.
     */
    public void recordSwitch(LinearLayout current) {
        pageIndex++;
        layoutIndex.put("PAGE" + pageIndex, current);
        koi_tv_comback.setVisibility(View.VISIBLE);
    }

    public void removeRecord() {
        layoutIndex.remove("PAGE" + pageIndex);
        pageIndex--;
    }


    /**
     * 发送短信定时器.
     */
    public void initForgetPwdTimerClick() {
        if (!initTimer) {
            timer = new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    koi_btn_forget_send.setText(String.valueOf(l / 1000));
                }

                @Override
                public void onFinish() {
                    koi_btn_forget_send.setClickable(true);
                    koi_btn_forget_send.setBackgroundColor(getResources().getColor(RUtils.getColorId("koi_white")));
                    koi_btn_forget_send.setBackground(getResources().getDrawable(RUtils.getDrawableId("koi_register_btn_accountlogin_select")));
                    koi_btn_forget_send.setText(getResources().getText(RUtils.getStringId("koi_text_resendcode")));
                    initTimer = false;
                }
            };
            koi_btn_forget_send.setBackgroundColor(getResources().getColor(RUtils.getColorId("koi_hint_text")));
            koi_btn_forget_send.setBackground(getResources().getDrawable(RUtils.getDrawableId("koi_account_register_press_shape")));
            koi_btn_forget_send.setClickable(false);
            timer.start();
            initTimer = true;
        }
    }

    /**
     * 发送短信定时器.
     */
    public void initPhoneRegisterTimerClick() {
        if (!initTimer) {
            timer = new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    koi_btn_resendreg.setText(String.valueOf(l/1000));
                }

                @Override
                public void onFinish() {
                    koi_btn_resendreg.setClickable(true);
                    koi_btn_resendreg.setBackgroundColor(getResources().getColor(RUtils.getColorId("koi_white")));
                    koi_btn_resendreg.setBackground(getResources().getDrawable(RUtils.getDrawableId("koi_register_btn_accountlogin_select")));
                    koi_btn_resendreg.setText(getResources().getText(RUtils.getStringId("koi_text_resendcode")));
                    initTimer = false;
                }
            };
            koi_btn_resendreg.setBackgroundColor(getResources().getColor(RUtils.getColorId("koi_hint_text")));
            koi_btn_resendreg.setBackground(getResources().getDrawable(RUtils.getDrawableId("koi_account_register_press_shape")));
            koi_btn_resendreg.setClickable(false);
            timer.start();
            initTimer = true;
        }

    }

    /**
     * 初始化数据.
     */
    @Override
    public void initDatas() {
        super.initDatas();
        Log.i(TAG, "initDatas");
       // layoutIndex = new HashMap<String, LinearLayout>();
    }

    /**
     * 初始化声明所有控件.
     */
    @Override
    public void initView() {
        Log.i(TAG, "initView");
        //主页面
        koi_main_layout = (LinearLayout) findViewById(RUtils.getViewId("koi_layout_accountsystem_main"));
        //快速注册页面
        koi_qcuikregister_layout = (LinearLayout) findViewById(RUtils.getViewId("koi_layout_quickregister_main"));
        //账号注册选择页面
        koi_normalregister_layout = (LinearLayout) findViewById(RUtils.getViewId("koi_layout_register_main"));
        //手机注册输入页面
        koi_phoneregister1_layout = (LinearLayout) findViewById(RUtils.getViewId("koi_layout_phoneregister_step1"));
        //手机注册验证页面
        koi_phoneregister2_layout = (LinearLayout) findViewById(RUtils.getViewId("koi_layout_phoneregister_step2"));
        //正常账号注册页面
        koi_layout_register = (LinearLayout) findViewById(RUtils.getViewId("koi_layout_register"));
        //账号登录页面
        koi_layout_login = (LinearLayout) findViewById(RUtils.getViewId("koi_layout_login"));
        //忘记密码
        koi_layout_fogetpwd = (LinearLayout) findViewById(RUtils.getViewId("koi_layout_fogetpwd"));
        //回退按钮
        koi_tv_comback = (TextView) findViewById(RUtils.getViewId("koi_tv_comback"));

        // 快速注册/
        koi_edt_account = (EditText)findViewById(RUtils.getViewId("koi_edt_quick_account"));
        koi_edt_quick_pwd = (EditText)findViewById(RUtils.getViewId("koi_edt_quick_pwd"));
        koi_edt_phAccount = (EditText)findViewById(RUtils.getViewId("koi_edt_phAccount"));

        // 手机号注册
        koi_edt_phoneReg_num = (EditText)findViewById(RUtils.getViewId("koi_edt_phoneReg_num"));
        koi_edt_checkReg_code = (EditText)findViewById(RUtils.getViewId("koi_edt_checkReg_code"));
        koi_edt_phoneReg_pwd = (EditText)findViewById(RUtils.getViewId("koi_edt_phoneReg_pwd"));

        // 正常账号注册
        koi_edt_normalReg_name = (EditText)findViewById(RUtils.getViewId("koi_edt_normalReg_name"));
        koi_edt_normalReg_pwd = (EditText)findViewById(RUtils.getViewId("koi_edt_normalReg_pwd"));
        koi_edt_normalReg_repwd = (EditText)findViewById(RUtils.getViewId("koi_edt_normalReg_repwd"));

        //账号密码登陆
        koi_edt_login_name = (EditText)findViewById(RUtils.getViewId("koi_edt_login_name"));
        koi_edt_login_pwd = (EditText)findViewById(RUtils.getViewId("koi_edt_login_pwd"));

        //koi_btn_resendreg
        koi_btn_resendreg = (Button) findViewById(RUtils.getViewId("koi_btn_resendreg"));

        //忘记密码
        koi_edt_forget_name = (EditText)findViewById(RUtils.getViewId("koi_edt_forget_name"));
        koi_edt_forget_phone = (EditText)findViewById(RUtils.getViewId("koi_edt_forget_phone"));
        koi_edt_forget_code = (EditText)findViewById(RUtils.getViewId("koi_edt_forget_code"));
        koi_edt_forget_newpwd = (EditText)findViewById(RUtils.getViewId("koi_edt_forget_newpwd"));
        koi_btn_forget_send = (Button)findViewById(RUtils.getViewId("koi_btn_forget_send"));

        layoutIndex.put("PAGE" + pageIndex, koi_main_layout);
    }

    @Override
    public void initEvent() {

    }

}
