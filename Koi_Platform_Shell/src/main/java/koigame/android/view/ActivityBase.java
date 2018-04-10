
package koigame.android.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.text.DecimalFormat;

public abstract class ActivityBase extends Activity {
    protected final static int GOT_PAY_ORDER = 2;

    protected final static int SUCCESS = 1;

    protected final static int FAIL = 0;

    public void onCreate(Bundle savedInstanceState) {
        this.onCreate(savedInstanceState, true);
    }

    public void onCreate(Bundle savedInstanceState, boolean cacheContext) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initView();
        initEvent();
        initData();
    }

    public void setContentView(View view) {
        super.setContentView(view);
        initView();
        initEvent();
        initData();
    }

    public abstract void initView();

    public void buildView() {

    }

    public abstract void initEvent();

    public void initData() {

    }

    /**
     * 回收系统资源
     */
    public abstract void release();

    public static void show(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        this.runOnUiThread(new Runnable() {
            public void run() {
                release();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.runOnUiThread(new Runnable() {
            public void run() {
                release();
            }
        });
    }

    private DecimalFormat format = new DecimalFormat("#,###,###,### ");

    public String getFormated(int number) {
        return format.format(number);
    }

    /**
     * 显示进度�?
     * 
     * @param context 环境
     * @param title 标题
     * @param message 信息
     * @param indeterminate 确定�?
     * @param cancelable 可撤�?
     * @return
     */
    public static ProgressDialog showProgress(Context context, CharSequence title,
            CharSequence message, boolean indeterminate, boolean cancelable) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.show();
        return dialog;
    }

}
