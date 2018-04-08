package koigame.sdk.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import koigame.sdk.api.KServiceException;
import koigame.sdk.api.KThread;
import koigame.sdk.api.KWebApiImpl;
import koigame.sdk.bean.user.KUserSession;
import koigame.sdk.util.AndroidUtils;
import koigame.sdk.util.JSONUtils;
import koigame.sdk.util.NumberUtils;
import koigame.sdk.util.RUtils;
import koigame.sdk.util.StringUtils;

/**
 * Created by wudi .
 * on 2018/4/3.
 */

public class KoiUserCenterActivity extends HActivityBase {

    private final String TAG = "KoiUserCenterActivity";
    private final int BINDSUCCESS = 61;
    private final int UPDATEPWDSUCCESS = 71;
    private final int CHECKSUCCESS = 81;

    private TextView koi_tv_center_bind_account;
    private TextView koi_tv_center_bind_phonenum;
    private Button koi_btn_center_bind;
    private LinearLayout koi_main_layout;
    private TextView koi_tv_comback;
    private LinearLayout koi_ly_center_bindnum;
    private boolean initTimer;
    private Button koi_btn_center_sent;
    private CountDownTimer countDownTimer;
    private EditText koi_edt_center_phonenum;
    private EditText koi_edt_center_checkcode;
    private EditText koi_edt_center_oldpwd;
    private EditText koi_edt_center_newpwd;
    private EditText koi_edt_center_re_newpwd;
    private LinearLayout koi_layout_update_pwd;
    private LinearLayout koi_layout_check_idcard;
    private EditText koi_edt_center_check_realname;
    private EditText koi_edt_center_check_idcard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(RUtils.getLayoutId("koi_account_center"));
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        ViewGroup.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (dm.heightPixels * 0.48);
        p.width = (int) (dm.widthPixels * 0.81);
        initView();
        initEvent();
    }

    /**
     * 账号绑定手机号发送验证码.
     *
     * @param view 点击事件.
     */
    public void centerSendSms(View view) {
        String phoneNum = koi_edt_center_phonenum.getText().toString();
        if (!NumberUtils.checkPhoneNumber(phoneNum)) {
            koi_edt_center_phonenum.setError(getResources().getString(RUtils.getStringId("koi_text_phonenum_error")));
            koi_edt_center_phonenum.requestFocus();
            return;
        }
        sendPhoneCodeCommit(phoneNum);
    }

    /**
     * 主页跳转到绑定页面.
     *
     * @param view 点击事件.
     */
    public void bindPhoneNum(View view) {
        switchAnim(koi_main_layout, koi_ly_center_bindnum);
    }

    /**
     * 进入修改密码界面.
     *
     * @param view 点击事件.
     */
    public void btnUpdatePwd(View view) {
        switchAnim(koi_main_layout, koi_layout_update_pwd);
    }

    /**
     * 进入实名认证界面.
     *
     * @param view 点击事件.
     */
    public void btnCheckIdcard(View view) {
        switchAnim(koi_main_layout, koi_layout_check_idcard);
    }

    /**
     * 向服务器申请绑定手机号.
     *
     * @param view 点击事件.
     */
    public void centerBindCommit(View view) {
        String phoneNum = koi_edt_center_phonenum.getText().toString();
        String signCode = koi_edt_center_checkcode.getText().toString();
        long userId = KUserSession.instance().getUserInfo().getUserId();
        if (!NumberUtils.checkPhoneNumber(phoneNum)) {
            koi_edt_center_phonenum.setError(getResources().getString(RUtils.getStringId("koi_text_phonenum_error")));
            koi_edt_center_phonenum.requestFocus();
        }
        if (StringUtils.isEmpty(signCode)) {
            koi_edt_center_checkcode.setError(getResources().getString(RUtils.getStringId("koi_pwd_cannot_null")));
            koi_edt_center_checkcode.requestFocus();
        }
        if (userId == 0) {
            AndroidUtils.showToast(KoiUserCenterActivity.this, "", 1);
            return;
        }
        docenterBindCommit(signCode, phoneNum, userId + "");
    }

    /**
     * 校验修改密码校验.
     *
     * @param view 点击事件.
     */
    public void centerUpdatePwdCommit(View view) {
        String oldPassword = koi_edt_center_oldpwd.getText().toString();
        String newPassword = koi_edt_center_newpwd.getText().toString();
        String reNewPassword = koi_edt_center_re_newpwd.getText().toString();
        if (StringUtils.isEmpty(oldPassword)) {
            koi_edt_center_oldpwd.setError(getResources().getString(RUtils.getStringId("koi_update_input_noempty")));
            koi_edt_center_oldpwd.requestFocus();
        }
        if (!NumberUtils.checkPassword(newPassword)) {
            koi_edt_center_newpwd.setError(getResources().getString(RUtils.getStringId("koi_register_pwd_check")));
            koi_edt_center_newpwd.requestFocus();
        }
        if (!newPassword.equals(reNewPassword)) {
            koi_edt_center_re_newpwd.setError(getResources().getString(RUtils.getStringId("koi_pwd_repeat_error")));
            koi_edt_center_re_newpwd.requestFocus();
        }
        String accountName = KUserSession.instance().getUserInfo().getAccountName();
        if (StringUtils.isEmpty(accountName)) {
            AndroidUtils.showToast(KoiUserCenterActivity.this, getResources().getString(RUtils.getStringId("koi_center_bindsuccess")), 1);
            return;
        }
        String accountType = "2";
        if (NumberUtils.checkPhoneNumber(accountName)) {
            accountType = "4";
        }

        doCenterUpdatePwd(accountName, accountType, oldPassword, newPassword);
    }

    /**
     * 身份证实名认证校验.
     * @param view 点击事件.
     */
    public void centerCheckCommit(View view) {
        String realName = koi_edt_center_check_realname.getText().toString();
        String idcard = koi_edt_center_check_idcard.getText().toString();

        if (StringUtils.isEmpty(realName)) {
            koi_edt_center_check_realname.setError(getResources().getString(RUtils.getStringId("koi_check_reaname_error")));
            koi_edt_center_check_realname.requestFocus();
        }

        if (!NumberUtils.checkIdcard(idcard)) {
            koi_edt_center_check_idcard.setError(getResources().getString(RUtils.getStringId("koi_check_idcard_error")));
            koi_edt_center_check_idcard.requestFocus();
        }

        String accountName = KUserSession.instance().getUserInfo().getAccountName();
        if (StringUtils.isEmpty(accountName)) {
            AndroidUtils.showToast(KoiUserCenterActivity.this, getResources().getString(RUtils.getStringId("koi_center_bindsuccess")), 1);
            return;
        }
        doCenterCheckCommit(idcard, realName);
    }

    /**
     * 身份证实名验证服务器请求.
     */
    public void doCenterCheckCommit(final String idcard, final String realname) {
        KThread progressThread = new KThread() {
            @Override
            public void run() {
                try {
                    AndroidUtils.showCicleProgress(KoiUserCenterActivity.this, getResources().getString(RUtils.getStringId("koi_check_ing")));

                    JSONObject props = KWebApiImpl.instance().checkBindIdCard(idcard, realname);

                    AndroidUtils.closeCiclePorgress(KoiUserCenterActivity.this);

                    Message msg = new Message();
                    msg.what = FAIL;
                    if (JSONUtils.isOK(props)) {
                        msg.what = CHECKSUCCESS;
                    }
                    msg.obj = props;
                    mHandler.sendMessage(msg);
                } catch (KServiceException e) {
                    e.printStackTrace();
                    AndroidUtils.closeCiclePorgress(KoiUserCenterActivity.this);
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
     * 向服务器申请绑定手机号.
     */
    public void doCenterUpdatePwd(final String accountName, final String accountType, final String pwd, final String newPwd) {
        KThread progressThread = new KThread() {
            @Override
            public void run() {
                try {
                    AndroidUtils.showCicleProgress(KoiUserCenterActivity.this, getResources().getString(RUtils.getStringId("koi_update_input_ing")));

                    JSONObject props = KWebApiImpl.instance().modifyPassword(accountName, accountType, pwd, newPwd);

                    AndroidUtils.closeCiclePorgress(KoiUserCenterActivity.this);

                    Message msg = new Message();
                    msg.what = FAIL;
                    if (JSONUtils.isOK(props)) {
                        msg.what = UPDATEPWDSUCCESS;
                    }
                    msg.obj = props;
                    mHandler.sendMessage(msg);
                } catch (KServiceException e) {
                    e.printStackTrace();
                    AndroidUtils.closeCiclePorgress(KoiUserCenterActivity.this);
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
     * 向服务器申请绑定手机号.
     */
    public void docenterBindCommit(final String signCode, final String accountName, final String userId) {
        KThread progressThread = new KThread() {
            @Override
            public void run() {
                try {
                    AndroidUtils.showCicleProgress(KoiUserCenterActivity.this, getResources().getString(RUtils.getStringId("koi_code_send")));

                    JSONObject props = KWebApiImpl.instance().bindPhoneAccount(signCode, accountName, userId);

                    AndroidUtils.closeCiclePorgress(KoiUserCenterActivity.this);

                    Message msg = new Message();
                    msg.what = FAIL;
                    if (JSONUtils.isOK(props)) {
                        msg.what = BINDSUCCESS;
                    }
                    msg.obj = props;
                    mHandler.sendMessage(msg);
                } catch (KServiceException e) {
                    e.printStackTrace();
                    AndroidUtils.closeCiclePorgress(KoiUserCenterActivity.this);
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
     * 手机号码注册第一步，通知服务器发送验证码.
     *
     * @param accountName 手机号.
     */
    public void sendPhoneCodeCommit(final String accountName) {
        KThread progressThread = new KThread() {
            @Override
            public void run() {
                try {
                    AndroidUtils.showCicleProgress(KoiUserCenterActivity.this, getResources().getString(RUtils.getStringId("koi_code_send")));

                    JSONObject props = KWebApiImpl.instance().sendSmsToUser(accountName);

                    AndroidUtils.closeCiclePorgress(KoiUserCenterActivity.this);

                    Message msg = new Message();
                    msg.what = SENDSMSFAIL;
                    if (JSONUtils.isOK(props)) {
                        msg.what = SENDSMSSUCCESS;
                    }
                    msg.obj = props;
                    mHandler.sendMessage(msg);
                } catch (KServiceException e) {
                    e.printStackTrace();
                    AndroidUtils.closeCiclePorgress(KoiUserCenterActivity.this);
                    if (isDestory()) {
                        return;
                    }
                    Message msg = new Message();
                    msg.what = SENDSMSFAIL;
                    msg.obj = e;
                    mHandler.sendMessage(msg);
                }
            }
        };
        progressThread.start();
    }

    @Override
    public void initDatas() {
        super.initDatas();

    }

    @Override
    public void initView() {
        //用户中心主页
        koi_tv_center_bind_account = (TextView) findViewById(RUtils.getViewId("koi_tv_center_bind_account"));
        koi_tv_center_bind_phonenum = (TextView) findViewById(RUtils.getViewId("koi_tv_center_bind_phonenum"));
        koi_btn_center_bind = (Button) findViewById(RUtils.getViewId("koi_btn_center_bind"));
        koi_main_layout = (LinearLayout) findViewById(RUtils.getViewId("koi_main_layout"));
        koi_tv_comback = (TextView) findViewById(RUtils.getViewId("koi_tv_comback"));
        koi_ly_center_bindnum = (LinearLayout) findViewById(RUtils.getViewId("koi_ly_center_bindnum"));
        koi_btn_center_sent = (Button) findViewById(RUtils.getViewId("koi_btn_center_sent"));

        //绑定手机号
        koi_edt_center_phonenum = (EditText) findViewById(RUtils.getViewId("koi_edt_center_phonenum"));
        koi_edt_center_checkcode = (EditText) findViewById(RUtils.getViewId("koi_edt_center_checkcode"));

        //密码修改
        koi_layout_update_pwd = (LinearLayout) findViewById(RUtils.getViewId("koi_layout_update_pwd"));
        koi_edt_center_oldpwd = (EditText) findViewById(RUtils.getViewId("koi_edt_center_oldpwd"));
        koi_edt_center_newpwd = (EditText) findViewById(RUtils.getViewId("koi_edt_center_newpwd"));
        koi_edt_center_re_newpwd = (EditText) findViewById(RUtils.getViewId("koi_edt_center_re_newpwd"));

        //实名认证
        koi_layout_check_idcard = (LinearLayout) findViewById(RUtils.getViewId("koi_layout_check_idcard"));
        koi_edt_center_check_realname = (EditText)findViewById(RUtils.getViewId("koi_edt_center_check_realname"));
        koi_edt_center_check_idcard = (EditText)findViewById(RUtils.getViewId("koi_edt_center_check_idcard"));

        layoutIndex.put("PAGE" + pageIndex, koi_main_layout);
    }


    @Override
    public void initEvent() {
        String accountName = KUserSession.instance().getUserInfo().getAccountName();
        String phone = KUserSession.instance().getUserInfo().getBindPhoneNum() + "";
        if (accountName == null || phone.equals("0")) {
            accountName = "未登录";
            phone = "未绑定";
            koi_btn_center_bind.setClickable(true);
        }
        String phoneNum = KUserSession.instance().getUserInfo().getBindPhoneNum();

        if (!StringUtils.isEmpty(phoneNum) && !"0".equals(phoneNum)) {
            koi_btn_center_bind.setClickable(false);
            koi_btn_center_bind.setText(getResources().getString(RUtils.getStringId("koi_center_bound")));
            koi_btn_center_bind.setBackground(getResources().getDrawable(RUtils.getDrawableId("koi_phone_register_press_shape")));
        }
        koi_tv_center_bind_account.setText(getResources().getString(RUtils.getStringId("koi_center_accountname"), accountName));
        koi_tv_center_bind_phonenum.setText(getResources().getString(RUtils.getStringId("koi_center_bindphone"), phone));
    }

    /**
     * 切换动画.
     *
     * @param current 当前页面.
     * @param next    目标页面.
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
        //countDownTimer.cancel();
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

    /**
     * 每回退一次删除一个下标.
     */
    public void removeRecord() {
        layoutIndex.remove("PAGE" + pageIndex);
        pageIndex--;
    }

    public void initPhoneRegisterTimerClick() {
        if (!initTimer) {
            countDownTimer = new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    koi_btn_center_sent.setText(String.valueOf(l / 1000));
                }

                @Override
                public void onFinish() {
                    koi_btn_center_sent.setClickable(true);
                    koi_btn_center_sent.setBackgroundColor(getResources().getColor(RUtils.getColorId("koi_white")));
                    koi_btn_center_sent.setBackground(getResources().getDrawable(RUtils.getDrawableId("koi_register_btn_accountlogin_select")));
                    koi_btn_center_sent.setText(getResources().getText(RUtils.getStringId("koi_text_resendcode")));
                    initTimer = false;
                }
            };
            koi_btn_center_sent.setBackgroundColor(getResources().getColor(RUtils.getColorId("koi_hint_text")));
            koi_btn_center_sent.setBackground(getResources().getDrawable(RUtils.getDrawableId("koi_account_register_press_shape")));
            koi_btn_center_sent.setClickable(false);
            countDownTimer.start();
            initTimer = true;
        }

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            AndroidUtils.closeProgress(KoiUserCenterActivity.this);
            try {

                switch (msg.what) {
                    case SUCCESS:
                        break;

                    case FAIL:
                        AndroidUtils.showToast(KoiUserCenterActivity.this, getResources().getString(RUtils.getStringId("koi_system_login_error")), 1);
                        break;

                    case SENDSMSSUCCESS: {
                        AndroidUtils.showToast(KoiUserCenterActivity.this, getResources().getString(RUtils.getStringId("koi_text_sendsms_success")), 1);
                        initPhoneRegisterTimerClick();
                        break;
                    }
                    case SENDSMSFAIL: {
                        AndroidUtils.showToast(KoiUserCenterActivity.this, getResources().getString(RUtils.getStringId("koi_text_sendsms_error")), 1);
                        break;
                    }
                    case BINDSUCCESS: {
                        AndroidUtils.showToast(KoiUserCenterActivity.this, getResources().getString(RUtils.getStringId("koi_center_bindsuccess")), 1);
                        KoiUserCenterActivity.this.finish();
                        break;
                    }
                    case UPDATEPWDSUCCESS: {
                        AndroidUtils.showToast(KoiUserCenterActivity.this, getResources().getString(RUtils.getStringId("koi_update_input_success")), 1);
                        KoiUserCenterActivity.this.finish();
                        break;
                    }
                    case CHECKSUCCESS: {
                        AndroidUtils.showToast(KoiUserCenterActivity.this, getResources().getString(RUtils.getStringId("koi_check_idcard_success")), 1);
                        comeback(null);
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
