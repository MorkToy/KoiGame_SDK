
package koigame.android.shell;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import koigame.sdk.util.RUtils;

/**
 * 闪屏
 * 
 * @author Mike
 */
public class SplashActivity extends Activity {

    private int repeatCount = 0;

    private int splashId = 1;

    private ImageView imgView;

    private Animation anim;

    private View bgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(RUtils.getLayoutId("splash"));
        ShellApplication app=(ShellApplication)getApplication();
       
        // imgView = (ImageView)
        // findViewById(RUtils.getViewId("hl_splash_img"));

        bgView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);

        refreshBgImg();
        anim = AnimationUtils.loadAnimation(this, RUtils.getAnimId("hl_splash"));
        anim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                // imgView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
                repeatCount = repeatCount + 1;
                if (repeatCount % 2 == 0) {
                    splashId++;
                    refreshBgImg();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                // imgView.setVisibility(View.GONE);

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        // Intent mainIntent = new Intent(SplashActivity.this,
                        // MainActivity.class);
                        Intent mainIntent = new Intent("hilink.action.start");
                        mainIntent.addCategory(getPackageName() + ".MAIN");
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        SplashActivity.this.startActivity(mainIntent);
                        SplashActivity.this.finish();
                    }
                });
            }
        });
        bgView.startAnimation(anim);

        bgView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (anim != null)
                    anim.cancel();
            }
        });
    }

    @SuppressLint("NewApi")
    private void refreshBgImg() {
        BitmapDrawable bgDrawable = (BitmapDrawable) getResources().getDrawable(
                RUtils.getDrawableId("hl_splash_" + splashId));
        // bgDrawable = ResourcesUtils.set2FitScreen(this, bgDrawable);"hl_splash_" + splashId
        // bgDrawable.setTileModeXY(TileMode.CLAMP, TileMode.CLAMP);

        int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 16) {
            bgView.setBackground(bgDrawable);
        } else {
            bgView.setBackgroundDrawable(bgDrawable);

        }

        // AndroidUtils.instance().setImageBackground(imgView, bgDrawable);
    }
}
