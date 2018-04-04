package koigame.sdk.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import koigame.sdk.util.RUtils;

/**
 * Created by wudi .
 * on 2018/4/4.
 */

public class KoiLogoutDailog extends Dialog implements View.OnClickListener {

    private final String TAG = "KoiLogoutDailog";

    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;

    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;

    public KoiLogoutDailog(Context context) {
        super(context);
        this.mContext = context;
    }

    public KoiLogoutDailog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public KoiLogoutDailog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected KoiLogoutDailog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public KoiLogoutDailog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public KoiLogoutDailog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(RUtils.getLayoutId("koi_common_dialog"));
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (dm.heightPixels * 0.30);
        p.width = (int) (dm.widthPixels * 0.81);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        contentTxt = (TextView)findViewById(RUtils.getViewId("content"));
        submitTxt = (TextView)findViewById(RUtils.getViewId("submit"));
        cancelTxt = (TextView)findViewById(RUtils.getViewId("cancel"));

        submitTxt.setOnClickListener(this);
        cancelTxt.setOnClickListener(this);

        contentTxt.setText(content);
        if(!TextUtils.isEmpty(positiveName)){
            submitTxt.setText(positiveName);
        }

        if(!TextUtils.isEmpty(negativeName)){
            cancelTxt.setText(negativeName);
        }
    }

    /*public void dialogCancel(View view) {
        if(listener != null){
            listener.onClick(this, false);
        }
    }

    public void dialogCommit(View view) {
        if(listener != null){
            listener.onClick(this, true);
        }
    }*/

    @Override
    public void onClick(View view) {
        if (view.getId() == RUtils.getViewId("cancel")) {
            if(listener != null){
                listener.onClick(this, false);
            }
            this.dismiss();
        }
        if (view.getId() == RUtils.getViewId("submit")) {
            if(listener != null){
                listener.onClick(this, true);
            }
        }
    }


    public interface OnCloseListener{
        void onClick(Dialog dialog, boolean confirm);
    }
}
