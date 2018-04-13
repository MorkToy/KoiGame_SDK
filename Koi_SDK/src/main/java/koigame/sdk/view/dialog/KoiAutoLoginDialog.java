package koigame.sdk.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import koigame.sdk.KoiGame;
import koigame.sdk.util.RUtils;
import koigame.sdk.view.KoiLoginActivity;
import koigame.sdk.view.progress.CircleProgressView;

/**
 * Created by wudi .
 * on 2018/4/4.
 */

public class KoiAutoLoginDialog extends Dialog implements View.OnClickListener {

    private final String TAG = "KoiLogoutDailog";

    private TextView contentTxt;

    private Context mContext;
    private String content;
    private CircleProgressView koi_define_circle;
    private TextView koi_define_tips;

    private final int TIME_PROGRESS = 10;
    private int curProgress = 0;
    private boolean isOddNumber= false; //是否是奇数阶段
    private TextView koi_switch_account;

    private int returnStatus = 0;

    public KoiAutoLoginDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public KoiAutoLoginDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(RUtils.getLayoutId("koi_progress_autologin"));
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (dm.heightPixels * 0.25);
        p.width = (int) (dm.widthPixels * 0.81);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        koi_define_circle = (CircleProgressView)findViewById(RUtils.getViewId("koi_define_circle"));
        //koi_define_tips = (TextView) findViewById(RUtils.getViewId("koi_define_tips"));
        koi_switch_account = (TextView) findViewById(RUtils.getViewId("koi_switch_account"));
        koi_switch_account.setOnClickListener(this);
        initCircleProgress();
    }

    private void initCircleProgress() {
        handler.postDelayed(runnableProgress, TIME_PROGRESS);
    }

    private Handler handler = new Handler();

    Runnable runnableProgress = new Runnable() {

        @Override
        public void run() {

            if (koi_define_circle != null) {

                if (curProgress == 0) {  //定义奇数和偶数阶段
                    isOddNumber = true;
                } else if (curProgress >= 100) {
                    isOddNumber = false;
                }

                if (isOddNumber) { //奇数阶段累加1
                    curProgress += 1;
                } else {           //偶数阶段递减1
                    curProgress -= 1;
                }

                koi_define_circle.setProgress(curProgress, isOddNumber);

            }
            handler.postDelayed(this, TIME_PROGRESS); //handler自带方法实现定时器
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnableProgress);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == RUtils.getViewId("koi_switch_account")) {
            KoiGame.logout();
            Intent intent = new Intent(mContext, KoiLoginActivity.class);
            mContext.startActivity(intent);
            returnStatus = 1;
            this.dismiss();
        }
    }

    public int getProgressStatus() {
        return returnStatus;
    }
}
