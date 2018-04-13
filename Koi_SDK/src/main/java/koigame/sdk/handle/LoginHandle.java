package koigame.sdk.handle;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import koigame.sdk.api.KServiceException;
import koigame.sdk.base.PublicKeyConstant;
import koigame.sdk.bean.user.KLoginEvent;
import koigame.sdk.bean.user.KLoginInfo;
import koigame.sdk.bean.user.KUserInfo;
import koigame.sdk.bean.user.KUserSession;
import koigame.sdk.util.AndroidUtils;
import koigame.sdk.util.RUtils;
import koigame.sdk.util.StringUtils;
import koigame.sdk.view.KoiLoginActivity;

/**
 * Created by wudi .
 * on 2018/4/2.
 */

public class LoginHandle extends Handler {

    private final WeakReference<Activity> mActivityReference;
    private EditText koi_edt_account;
    private EditText koi_edt_quick_pwd;

    LoginHandle(Activity activity, EditText editText1, EditText editText2) {
        this.mActivityReference = new WeakReference<Activity>(activity);
        this.koi_edt_account = editText1;
        this.koi_edt_quick_pwd =editText2;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        KoiLoginActivity loginActivity = (KoiLoginActivity)mActivityReference.get();
        switch (msg.what) {
            case PublicKeyConstant.SUCCESS:
                JSONObject props = (JSONObject) msg.obj;
                final KUserInfo userInfo = new KUserInfo(props);
                KUserSession.instance().saveToPreference(userInfo);
                KUserSession.instance().setUserInfo(userInfo);
                loginActivity.finish();
                KLoginEvent.onLoginSuccess(new KLoginInfo(props));
                break;
            case PublicKeyConstant.FAIL:
                break;

            case PublicKeyConstant.REGISTER_FAIL:
                KServiceException e = (KServiceException) msg.obj;
                if (e.getValue() == 211 || e.getValue() == 224) {
                    koi_edt_account.setError(e.getName());
                    koi_edt_account.requestFocus();
                } else if (e.getValue() == 215) {
                    koi_edt_quick_pwd.setError(e.getName());
                    koi_edt_quick_pwd.requestFocus();
                } else {
                    String text = StringUtils.isEmpty(e.getName()) ? loginActivity.getResources().getString(
                            RUtils.getStringId("koi_system_error")) : e.getName();
                    AndroidUtils.showToast(loginActivity, text, Toast.LENGTH_LONG);
                }
                break;

        }
    }
}
