package koigame.android.platform.viedio;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import koigame.jni.JNIActivity;
import koigame.sdk.KMetaData;
import koigame.sdk.util.AndroidUtils;
import koigame.sdk.util.RUtils;

public class VideoDialog extends Dialog implements SurfaceHolder.Callback,
        View.OnClickListener, OnCompletionListener,View.OnTouchListener{

    private Context context;
    private MediaPlayer mMediaPlayer;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mViewPlayB;
    private String workDir;
    private String videoName;
    private JNIActivity jniActivity;
    private AssetFileDescriptor fileDescriptor;
    private int count = 0;
    private final int DOUBLE_TAP_TIMEOUT = 200;
    private MotionEvent mCurrentDownEvent;
    private MotionEvent mPreviousUpEvent;
	private File vedioFile;

    public VideoDialog(Context context, String workDir) {
        super(context);
        this.context = context;
        this.workDir = workDir;
    }

    public VideoDialog(JNIActivity jniActivity, Context context, String workDir, String videoName, int theme) {
        super(context, theme);
        this.jniActivity = jniActivity;
        this.context = context;
        this.workDir = workDir;
        this.videoName = videoName;
        setOwnerActivity((Activity) context);
    }

    @Override
    public void onBackPressed() {
        Log.i("VideoDialog", "onBackPressed");
    }

    @Override
    protected void onStop() {
        Log.i("VideoDialog", "onStop");
        jniActivity.videoComplete(videoName);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("VideoDialog", "onStart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(RUtils.getLayoutId("videoplayer"));
        initUI();
    }

    private void initUI() {
        // TODO Auto-generated method stub
        try {
            mViewPlayB = (SurfaceView) findViewById(RUtils.getViewId("surface"));
            mViewPlayB.setClickable(true);
            mViewPlayB.setFocusable(true);
            mViewPlayB.setFocusableInTouchMode(true);
            //fileDescriptor = context.getAssets().openFd("res/" + videoName);
            vedioFile = new File(workDir + KMetaData.GameCode + "/res/" + videoName);
            if (!vedioFile.exists() || vedioFile == null) {
            	fileDescriptor = context.getAssets().openFd("res/" + videoName);
            }
            mSurfaceHolder = mViewPlayB.getHolder();
            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setFixedSize(320, 240);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            mViewPlayB.setOnClickListener(this);
            mViewPlayB.setOnTouchListener(this);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mPreviousUpEvent != null
                    && mCurrentDownEvent != null
                    && isConsideredDoubleTap(mCurrentDownEvent,
                    mPreviousUpEvent, event)) {
                Log.e("", "Double click=============");
                jniActivity.videoComplete(videoName);
                mMediaPlayer.stop();
                mMediaPlayer.release();
                this.dismiss();
            }
            mCurrentDownEvent = MotionEvent.obtain(event);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mPreviousUpEvent = MotionEvent.obtain(event);
            AndroidUtils.showToast(getOwnerActivity(),"雙擊屏幕跳過視頻哦", Toast.LENGTH_SHORT);
        }
        return true;
    }

    private boolean isConsideredDoubleTap(MotionEvent firstDown,
                                          MotionEvent firstUp, MotionEvent secondDown) {
        if (secondDown.getEventTime() - firstUp.getEventTime() > DOUBLE_TAP_TIMEOUT) {
            return false;
        }
        int deltaX = (int) firstUp.getX() - (int) secondDown.getX();
        int deltaY = (int) firstUp.getY() - (int) secondDown.getY();
        return deltaX * deltaX + deltaY * deltaY < 10000;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        if (null == mMediaPlayer) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.setOnCompletionListener(this);
        }

        try {
            /*mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());*/
        	if (!vedioFile.exists() || vedioFile == null) {
        		mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                        fileDescriptor.getStartOffset(),
                        fileDescriptor.getLength());
        		if (fileDescriptor.getFileDescriptor() == null) {
        			jniActivity.videoComplete(videoName);
        			mMediaPlayer.release();
                    this.dismiss();
        		}
        	} else {
        		mMediaPlayer.setDataSource(vedioFile.getAbsolutePath());
        	}
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
        	
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        Log.i("VideoDialog", "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        jniActivity.videoComplete(videoName);
        mMediaPlayer.stop();
        mMediaPlayer.release();
        this.dismiss();
    }

}
