package koigame.sdk.view;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import koigame.sdk.KoiCallback;
import koigame.sdk.KoiGame;
import koigame.sdk.bean.user.KUserSession;
import koigame.sdk.util.AndroidUtils;
import koigame.sdk.util.RUtils;
import koigame.sdk.util.bean.PermissionInterface;
import koigame.sdk.view.dialog.KoiLogoutDailog;


public class MainActivity extends Activity implements PermissionInterface {
    public String TAG = "MainActivity";
    private TextView koi_tv_accountname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(RUtils.getLayoutId("main"));
        KoiGame.init(this);
        koi_tv_accountname = (TextView) findViewById(RUtils.getViewId("koi_tv_accountname"));
    }



    public void doLogin(View view) {
        /*Intent intent = new Intent(this, KoiLoginActivity.class);
		startActivity(intent);*/
        KoiGame.login(this, new KoiCallback.LoginCallback() {
            @Override
            public void onSuccess(String uid, String accessToken) {
                koi_tv_accountname.setText("账号：" + KUserSession.instance().getUserInfo().getAccountName() + ",userId = " + uid);
            }

            @Override
            public void onCancle() {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public void doPay(View view) {
        Intent intent = new Intent(this, KoiPayActivity.class);
        startActivity(intent);
    }

    public void doCenter(View view) {
        Intent intent = new Intent(this, KoiUserCenterActivity.class);
        startActivity(intent);
    }

    public void doLogout(View view) {
        koi_tv_accountname.setText("账号未登录");
        KoiGame.logout();
    }

    public void doShowPregress(View view) {
        AndroidUtils.showCicleProgress(this, "正在加载...");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10000) {

        }
    }

    @Override
    public int getPermissionsRequestCode() {
        Log.i(TAG, "getPermissionsRequestCode 权限请求成功");
        return 10000;
    }

    @Override
    public String[] getPermissions() {
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        };
    }

    @Override
    public void requestPermissionsSuccess() {

    }

    @Override
    public void requestPermissionsFail() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }
        return false;
    }

    public void exit() {
        KoiLogoutDailog dailog = new KoiLogoutDailog(this, RUtils.getStyle("koi_dialog"), "确定要退出游戏吗?", new KoiLogoutDailog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                } else {
                    dialog.cancel();
                }

            }
        });
        dailog.show();
    }
}
