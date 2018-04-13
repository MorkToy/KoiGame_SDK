package koigame.sdk.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import koigame.sdk.api.KServiceException;
import koigame.sdk.api.KThread;
import koigame.sdk.api.KWebApiImpl;
import koigame.sdk.util.AndroidUtils;
import koigame.sdk.util.JSONUtils;
import koigame.sdk.util.NumberUtils;
import koigame.sdk.util.RUtils;
import koigame.sdk.util.StringUtils;

/**
 * Created by wudi .
 * on 2018/4/11.
 */

public class KoiForgetPwdActivity extends HActivityBase {

    private static final int SENDSMSSUCCESS = 15;
    private static final int FORGETPWDSUCCESS = 21;
    private EditText koi_edt_forget_name;
    private EditText koi_edt_forget_phone;
    private EditText koi_edt_forget_code;
    private EditText koi_edt_forget_newpwd;
    private Button koi_btn_forget_send;
    private boolean initTimer;
    private CountDownTimer timer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(RUtils.getLayoutId("koi_account_forget_pwd"));
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        ViewGroup.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (dm.heightPixels * 0.56);
        p.width = (int) (dm.widthPixels * 0.81);
        initView();

    }

    @Override
    public void initDatas() {

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

    @Override
    public void initView() {
        //忘记密码
        koi_edt_forget_name = (EditText)findViewById(RUtils.getViewId("koi_edt_forget_name"));
        koi_edt_forget_phone = (EditText)findViewById(RUtils.getViewId("koi_edt_forget_phone"));
        koi_edt_forget_code = (EditText)findViewById(RUtils.getViewId("koi_edt_forget_code"));
        koi_edt_forget_newpwd = (EditText)findViewById(RUtils.getViewId("koi_edt_forget_newpwd"));
        koi_btn_forget_send = (Button)findViewById(RUtils.getViewId("koi_btn_forget_send"));
    }

    public void comeback(View view) {
        ActivityCompat.finishAfterTransition(this);
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
     * 忘记密码.
     */
    public void forgetPwdCommit(View view) {
        String screenName = koi_edt_forget_name.getEditableText().toString().trim();
        String phoneNum = koi_edt_forget_phone.getEditableText().toString().trim();
        String signCode = koi_edt_forget_code.getEditableText().toString().trim();
        String newPwd = koi_edt_forget_newpwd.getEditableText().toString().trim();
        String accountType = "2";

        if (!NumberUtils.checkUsername(screenName) && !NumberUtils.checkPhoneNumber(phoneNum)) {
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
     * 手机号码注册第一步，通知服务器发送验证码.
     *
     * @param accountName 手机号.
     */
    public void sendPhoneCodeCommit(final String accountName) {
        KThread progressThread = new KThread() {
            @Override
            public void run() {
                try {
                    AndroidUtils.showCicleProgress(KoiForgetPwdActivity.this, getResources().getString(RUtils.getStringId("koi_code_send")));

                    JSONObject props = KWebApiImpl.instance().sendSmsToUser(accountName);

                    AndroidUtils.closeCiclePorgress(KoiForgetPwdActivity.this);

                    Message msg = new Message();
                    msg.what = SENDSMSFAIL;
                    if (JSONUtils.isOK(props)) {
                        msg.what = SENDSMSSUCCESS;
                    }
                    msg.obj = props;
                    mHandler.sendMessage(msg);
                } catch (KServiceException e) {
                    e.printStackTrace();
                    AndroidUtils.closeCiclePorgress(KoiForgetPwdActivity.this);
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
                    AndroidUtils.showCicleProgress(KoiForgetPwdActivity.this, getResources().getString(RUtils.getStringId("koi_hint_fogget_loading")));

                    JSONObject props = KWebApiImpl.instance().updatePasswordBySms(signCode, accountName, phoneNum, pwd, accountTyle);
                    AndroidUtils.closeCiclePorgress(KoiForgetPwdActivity.this);
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
                    AndroidUtils.closeCiclePorgress(KoiForgetPwdActivity.this);
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


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            try {
                AndroidUtils.closeCiclePorgress(KoiForgetPwdActivity.this);

                switch (msg.what) {
                    case FAIL:
                        AndroidUtils.showToast(KoiForgetPwdActivity.this, getResources().getString(RUtils.getStringId("koi_system_login_error")), 1);
                        break;

                    case FORGETPWDSUCCESS: {
                        AndroidUtils.showToast(KoiForgetPwdActivity.this, getResources().getString(RUtils.getStringId("koi_hint_fogget_success")), 1);
                        KoiForgetPwdActivity.this.finish();
                        break;
                    }
                    case SENDSMSSUCCESS: {
                        //switchAnim(koi_phoneregister1_layout, koi_phoneregister2_layout);
                        AndroidUtils.showToast(KoiForgetPwdActivity.this, getResources().getString(RUtils.getStringId("koi_text_sendsms_success")), 1);
                        initForgetPwdTimerClick();
                        break;
                    }
                    case SENDSMSFAIL: {
                        AndroidUtils.showToast(KoiForgetPwdActivity.this, getResources().getString(RUtils.getStringId("koi_text_sendsms_error")), 1);
                        break;
                    }
                }
            } catch (Exception e) {
            e.printStackTrace();}
        }
        };

    @Override
    public void initEvent() {

    }


}
