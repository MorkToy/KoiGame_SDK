package koigame.android.platform.base;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import koigame.android.shell.ShellActivity;
import koigame.android.user.LoginEvent;
import koigame.jni.JNIActivity;
import koigame.sdk.KoiCallback;
import koigame.sdk.KoiGame;
import koigame.sdk.api.KWebApiImpl;
import koigame.sdk.bean.pay.KPayInfo;
import koigame.sdk.bean.user.KUserSession;
import koigame.sdk.util.RUtils;
import koigame.sdk.view.dialog.KoiLogoutDailog;

import static koigame.android.platform.base.ConnectionBuilder.TAG;

//import hilink.koi.android.shell.SplashActivity;

public abstract class PlatformConnection {

    public static final int LOGIN_SUCCESS_ID = 101;

    public static final int LOGIN_CANCLE_ID = 102;

    public static final int PAY_SUCCESS_ID = 201;

    public static final int PAY_CANCLE_ID = 202;

    public static ShellActivity mainActivity;

    //public static SplashActivity splashActivity;

    /**
     * 初始化
     *
     * @param activity
     */
    public void init(Activity activity) throws Exception {
    }

    /**
     * 登陆
     */
    public void preLogin() {
        KoiGame.login(mainActivity, new KoiCallback.LoginCallback() {

            @Override
            public void onSuccess(String uid, String accessToken) {
                // TODO Auto-generated method stub


                KUserSession.instance().setSdkUserId(uid);
                KUserSession.instance().setSdkAccessToken(accessToken);
                LoginEvent.onLoginSuccess(KoiGame.loginInfo);
            }

            @Override
            public void onCancle() {
                LoginEvent.onLoginCancle();
            }

            @Override
            public void onError(Exception e) {
                // TODO Auto-generated method stub
            }

        });
    }

    /**
     * 支付
     *
     * @param info
     */
    public void synPay(KPayInfo info) {
        KoiGame.pay(mainActivity, info, new KoiCallback.PayCallback() {

            @Override
            public void onSuccess(String orderNO, String cpOrderNO) {
                Log.i(TAG, "充值成功");
                mainActivity.onPayFinished(new Bundle());

            }

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onCancle() {
                mainActivity.onPayFinished(new Bundle());
                Log.i(TAG, "订单取消");
            }

            @Override
            public void onPayBegin(KPayInfo payInfo, Activity activity, boolean shouldFinishAfterPay) {
                Log.i(TAG, "订单成功");
            }

        }, true);
    }

    /**
     * 用户中心
     *
     * @param activity
     */
    public void memberCenter(Activity activity) {
        Log.i("member", "platform onOpenMemberCenter");
        //Hilink.memberCenter(activity);
    }



    /**
     * 登出
     */
    public void logout(final Activity activity) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                String title = mainActivity.getResources().getString(RUtils.getStringId("hl_tip_title"));
                String message = mainActivity.getResources().getString(RUtils.getStringId("hl_logout_question"));
                String positiveButtonTip = mainActivity.getResources().getString(RUtils.getStringId("hl_ok"));
                String negativeButtonTip = mainActivity.getResources().getString(RUtils.getStringId("hl_cancel"));

                AlertDialog tipDialog = new AlertDialog.Builder(activity).setMessage(message).setTitle(title)
                        .setPositiveButton(positiveButtonTip, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                TimerTask task = new TimerTask() {

                                    public void run() {
                                        try {
                                            KUserSession.instance().cleanPreference();
                                            restart(mainActivity);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                Timer timer = new Timer();
                                timer.schedule(task, 100);
                            }
                        }).setNegativeButton(negativeButtonTip, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                tipDialog.setCancelable(true);
                tipDialog.show();

            }
        });
    }

    public void onCreate() {
        Log.i("PlatformConnection", "life:onCreate");
    }

    public void onPause() {
    }

    public void onResume() {
        //Hilink.onResumeForServer();
    }

    public void onStop() {
        //Hilink.onStopForServer();
    }

    public void onDestory() {
        KUserSession.instance().cleanPreference();
    }

    public void onRestart() {
    }

    public void onNewIntent() {

    }

    public void onAreaSelected() {
    }

    public void onEnterGame(Bundle bundle) {
    }

    public void submitExtraData(int dataType, int roleLevel, int roleId, int moneyNum, int gem) {

    }

    public void onUseItem(Bundle bundle) {
    }

    public void onBuyItem(Bundle bundle) {
    }

    public void onLevelUp(Bundle bundle) {
    }

    public void logEvent(int actionId) {
    }

    public void loginBut() {

    }

    public void appFlyerEvent(String type, String value) {

    }

    public void onSetCurrencyCode(String code) {

    }

    public void onloadGame() {

    }

    public void onSelectArea() {

    }

    public void onWebPay() {

    }

    public String getSDKVersion() {
        return "1.08.008";
    }

    /**
     * 退出时调用
     */
    public void exit() {
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                KoiLogoutDailog dailog = new KoiLogoutDailog(mainActivity, RUtils.getStyle("koi_dialog"), "确定要退出游戏吗?", new KoiLogoutDailog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            destoryGame();
                        } else {
                            dialog.cancel();
                        }

                    }
                });
                dailog.show();
            }
        });
    }


    public void notifyLogout(){
        ExecutorService exec = Executors.newFixedThreadPool(1);
        Callable<String> call = new Callable<String>() {
            @Override
            public String call() throws Exception {
                JSONObject returnInfo = KWebApiImpl.instance().logout(KUserSession.instance().getSdkUserId() + "", KUserSession.instance().getUserInfo().getRoleId() + "",
                        KUserSession.instance().getUserInfo().getAreaCode(), KUserSession.instance().getUserInfo().getAreaName(), KUserSession.instance().getUserInfo().getRoleLevel(),
                        KUserSession.instance().getUserInfo().getGem(), KUserSession.instance().getUserInfo().getGold());
                return returnInfo.toString();
            }
        };
        try{
            Future<String> future = exec.submit(call);
            String obj = future.get(500, TimeUnit.MILLISECONDS);
            Log.i("leyingConnection","请求退出成功："+obj);
            destoryGame();
        }catch (TimeoutException te){
            Log.i("leyingConnection","请求退出超时");
            te.printStackTrace();
            destoryGame();
        }catch (Exception e){
            Log.i("leyingConnection","请求退出失败");
            e.printStackTrace();
            destoryGame();
        }
        exec.shutdown();
    }

    /**
     * 界面回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    /**
     * 设置activity
     *
     * @param act
     */
    public void setActivity(ShellActivity act) {
        mainActivity = act;
    }


    public void creatRole() {

    }

    public void getRoleLevel(String level) {

    }

    /**
     * 重启游戏.
     */
    public void restart(Activity activity) {
        /*Intent intent1=new Intent(mainActivity,killSelfService.class);
        intent1.putExtra("PackageName",mainActivity.getPackageName());
        intent1.putExtra("Delayed",2000);
        mainActivity.startService(intent1);
        koi.android.os.Process.killProcess(koi.android.os.Process.myPid());*/
        Intent intent = mainActivity.getPackageManager().getLaunchIntentForPackage(mainActivity.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent restartIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager mgr = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
        notifyLogout();
    }



    protected static void destoryGame() {
        mainActivity.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }


    protected final String[] getSdkParams(int size) throws Exception {
        String[] params = new String[size];
        Properties paramProperties = new Properties();
        paramProperties.load(mainActivity.getAssets().open("params.properties"));
        for (int i = 0; i < size; i++) {
            params[i] = paramProperties.getProperty("p" + i);
        }
        return params;
    }

}
