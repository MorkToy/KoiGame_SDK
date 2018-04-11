package koigame.android.shell;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import koigame.android.api.ServiceException;
import koigame.android.api.WebApiImpl;
import koigame.android.platform.base.ConnectionBuilder;
import koigame.android.platform.viedio.VideoDialog;
import koigame.android.user.LoginEvent;
import koigame.jni.JNIActivity;
import koigame.sdk.KConstant;
import koigame.sdk.KMetaData;
import koigame.sdk.KoiGame;
import koigame.sdk.api.KServiceException;
import koigame.sdk.api.KWebApiImpl;
import koigame.sdk.bean.pay.KPayInfo;
import koigame.sdk.bean.user.KLoginEvent;
import koigame.sdk.bean.user.KLoginInfo;
import koigame.sdk.bean.user.KUserSession;
import koigame.sdk.bean.version.KUpdateInfo;
import koigame.sdk.download.DownloadProgressListener;
import koigame.sdk.download.FileDownloader;
import koigame.sdk.game.KGameProxy;
import koigame.sdk.util.AndroidUtils;
import koigame.sdk.util.CollectionUtils;
import koigame.sdk.util.JSONUtils;
import koigame.sdk.util.NetUtils;
import koigame.sdk.util.NumberUtils;
import koigame.sdk.util.PermissionHelper;
import koigame.sdk.util.PreferenceUtils;
import koigame.sdk.util.RUtils;
import koigame.sdk.util.ResourceUtils;
import koigame.sdk.util.StringUtils;
import koigame.sdk.util.bean.PermissionInterface;
import koigame.sdk.view.KoiLoginActivity;
import koigame.sdk.view.KoiPayActivity;
import koigame.sdk.view.progress.RoundCornerProgressBar;


public class ShellActivity extends Activity implements PermissionInterface {

    public static final String TAG = "ShellActivity";

    public static String workDir;

    public boolean isStart = true;
    public boolean isPause = true;

    public boolean isGamePause = false;

    private Class mActivityClass;

    private Object mActivityInstance;

    private String libFileDir;

    private RoundCornerProgressBar ivLoaing0;

    private TextView ivLoadingProsess;

    private TextView resLoadingTip,
            viceResLoadingTip;

    private LinearLayout llLoadingContainer;

    private int screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    private static PopupWindow webviewWindow;

    //private JNIActivity JNIActivity;//

    public static final String EXP_PATH = File.separator + "Android"
            + File.separator + "obb" + File.separator;

    private ApplicationInfo appInfo;
    private long lastClickTime;
    private VideoDialog mDlgVideoPlay;
    private String fileName;
    private boolean isPlay = false; // 是否要游戏播放视频
    private boolean isEnterGame = false;
    private String videoName = "gundam.mp4";
    private boolean isHalfPlay = false;
    private Bitmap bitmap = null;
    private View rootView;
    private long loginUiTime;
    //ExecutorService executorService = Executors.newFixedThreadPool(10);

    public int c = 0;

    private int updateState = 0;
    private int updateComplete = 1;
    private int updateFail = -1;
    long selAreaTime;
    long enterGameTime;
    private PermissionHelper mPermissionHelper;

    /**
     * pixels = dps * (density / 160).
     * 像素               = dp * (密度/160)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(RUtils.getLayoutId("main"));
            KoiGame.setScreenOrientation(screenOrientation);
            /*JNIActivity = new JNIActivity();
            JNIActivity.setActivity(ShellActivity.this);*/
            
          //初始化并发起权限申请  
            mPermissionHelper = new PermissionHelper(this, this);  
            mPermissionHelper.requestPermissions(); 

            initTalkData();
            // 初始化三方sdk连接
            ConnectionBuilder.build(ShellActivity.this);
            ConnectionBuilder.onCreate();
            //activityKeep.getInstance().setMainActivity(this);
            // languageConfig.getInstance().getSite(this);
            Log.i("builder", "after onCreate");
            appInfo = AndroidUtils.instance().getApplicationInfo(
                    ShellActivity.this);
            libFileDir = appInfo.dataDir + "/runlib";
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                workDir = Environment.getExternalStorageDirectory()
                        .getAbsolutePath()
                        + "/"
                        + MetaData.GameDir
                        + "/"
                        + KMetaData.Site + "/";
            } else {
                workDir = appInfo.dataDir + "/game/";
            }
            rootView = findViewById(android.R.id.content);
            // openAlbum(new Bundle());
            // 设置背景图
            initMainBackground();

        } catch (Exception e) {
            e.printStackTrace();
            AndroidUtils.showToast(ShellActivity.this, getResources()
                            .getString(RUtils.getStringId("hl_init_error")),
                    Toast.LENGTH_LONG);
            return;
        }


        new Thread(new Runnable() {

            public void run() {
                try {
                    Thread.sleep(500);
                    start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 入口
     */
    private void start() {

        if (!NetUtils.instance().isOpenNetwork(ShellActivity.this)) { // 没有连接网络
            final Message msg = new Message();
            msg.what = NO_NET_TIP;
            mHandler.dispatchMessage(msg);
        } else {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    TimerTask task = new TimerTask() {
                        public void run() {
                            try {
                                if (!isGameLoaded()) { // 没有初始化过游戏资源
                                    unPackHL(workDir); // 解压hl包
                                }
                                readGameInfo();

                                login(); // 开启登陆

                            } catch (Exception e) {
                                e.printStackTrace();
                                final Message msg = new Message();
                                msg.what = CLOSE;
                                mHandler.sendMessage(msg);


                                AndroidUtils
                                        .showToast(
                                                ShellActivity.this,
                                                getResources()
                                                        .getString(
                                                                RUtils.getStringId("hl_init_error")),
                                                Toast.LENGTH_LONG);
                            }
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, 100);
                }
            });
        }

    }

    /**
     * 解压游戏包
     * <p>
     * 游戏包名（包括dex，游戏资源等等）
     *
     * @param decompressDir 解压路径
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void unPackHL(final String decompressDir)
            throws FileNotFoundException, IOException {
        final Message msg = new Message();
        msg.what = UNZIP_LOCAL_PROCESS;
        mHandler.dispatchMessage(msg);

        // 删除已有的项目文件
        FileUtils.deleteDirectory(new File(workDir + KMetaData.GameCode));
        AndroidUtils.instance().copyAssetsFile(ShellActivity.this,
                "res/version",
                workDir + "/" + KMetaData.GameCode + "/res/version");

        // 复制so文件
        FileUtils.copyFileToDirectory(new File(appInfo.dataDir
                + "/lib/libgame.so"), new File(libFileDir));

        readGameInfo();
        PreferenceUtils.instance().putBoolean(
                KMetaData.VersionName + ".loaded", true);
        msg.what = CLOSE;
        mHandler.dispatchMessage(msg);
    }

    /**
     * 读取游戏信息
     */
    private void readGameInfo() {
        Log.d(TAG, "read game version from version file");
        try {
            File file = new File(workDir + "/" + KMetaData.GameCode
                    + "/res/version");
            if (!file.exists()) { // version不存在，重新从assets目录copy
                AndroidUtils.instance().copyAssetsFile(ShellActivity.this,
                        "res/version",
                        workDir + "/" + KMetaData.GameCode + "/res/version");
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int gameVersion = Integer.parseInt(reader.readLine().trim());

            KMetaData.GameVersion = gameVersion;
            Log.d(TAG, "GameVersion: " + KMetaData.GameVersion);
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启登陆
     */
    private void login() {

        LoginEvent.clear();
        LoginEvent.add(new LoginEvent.LoginListener() {


            public void onFailed(JSONObject props) {

            }

            public void onSuccessed(final KLoginInfo loginInfo) { // 登陆成功
                // 设置一些metaData的初始值
//				setMetaData(loginInfo);
                Log.i(TAG, "loadGame ####################################################### ");
                Intent testPay = new Intent(ShellActivity.this, KoiPayActivity.class);
                ShellActivity.this.startActivity(testPay);
                /*rootView.setOnClickListener(null);
                if (loginInfo.versionInfo.isResourceNeedUpdate()) {
                    if (!NetUtils.instance()
                            .isWifiConnected(ShellActivity.this)) { // 如果不是wifi连接
                        final Message msg = new Message();
                        msg.what = NO_WIFI_TIP;
                        msg.obj = loginInfo;
                        mHandler.dispatchMessage(msg);
                    } else { // wifi 连接

                        TimerTask task = new TimerTask() {

                            public void run() {
                                try {
                                    if (updateGame(loginInfo.versionInfo
                                            .getResourceUpdateConfigs())) {
                                        Map<String, Object> eventValue = new HashMap<String, Object>();
                                    	eventValue.put("update", 1);

                                        //HWebApiImpl.instance().checkActive(loginInfo.dataJson.getString("userId"));
                                        loadGame(loginInfo);
                                    }
                                } catch (Exception e) {

                                }

                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(task, 100);
                    }
                } else {
                    loadGame(loginInfo);
                }*/

            }

            @Override
            public void onCancle() {
                finish();
            }
        });
        ConnectionBuilder.userConnection.preLogin();
        loginUiTime = new Date().getTime();

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFastClick(10000)) {
                    Log.i(TAG, "start preLogin");
                    ConnectionBuilder.userConnection.preLogin();
                }
            }
        });
    }

    /**
     * 是否在ms毫秒内再次点击
     *
     * @param ms 毫秒数
     * @return
     */
    private boolean isFastClick(int ms) {
        long now = System.currentTimeMillis();
        if (now - lastClickTime > ms) {
            lastClickTime = now;
            return false;
        }
        return true;
    }

    /**
     * 从登陆信息中提取metaData初始值并设置
     *
     * @param loginInfo
     */
//	private void setMetaData(HLoginInfo loginInfo) {
//		MetaData.WechatAppId = loginInfo.metaDataConfig.getWechatAppId();
//		MetaData.WeiboAppKey = loginInfo.metaDataConfig.getWeiboAppKey();
//		MetaData.GumpWebPay = loginInfo.metaDataConfig.isGumpWebpay();
//
//		Log.e("metaDataConfig", "WechatAppId:" + MetaData.WechatAppId
//				+ ",WeiboAppKey:" + MetaData.WeiboAppKey);
//	}

    /**
     * 下载及安装游戏补丁
     *
     * @param gameVersionList
     */
    private boolean updateGame(List<KUpdateInfo> gameVersionList) {
        try {

            int idx = 0;
            int number = 1;
            Message msg = new Message();
            msg.what = DOWNLOAD_PATCH;
            mHandler.dispatchMessage(msg);
            number = CollectionUtils.size(gameVersionList);

            for (KUpdateInfo updateInfo : gameVersionList) {
                idx = idx + 1;
                downloadGamePatch(updateInfo, workDir, number, idx);
            }

            msg = new Message();
            msg.what = INSTALL_PATCH;
            mHandler.dispatchMessage(msg);
            int totalFiles = 0;
            for (KUpdateInfo updateInfo : gameVersionList) {
                totalFiles = totalFiles
                        + getHLFileNumbers(updateInfo.getUpdateFileName());
            }
            int loadedFiles = 0;
            for (KUpdateInfo updateInfo : gameVersionList) {
                Log.d(TAG, "unPackPatch: " + updateInfo.getUpdateFileName());
                unPackPatch(updateInfo.getUpdateFileName(), workDir,
                        totalFiles, loadedFiles);
                loadedFiles = loadedFiles
                        + getHLFileNumbers(updateInfo.getUpdateFileName());
                readGameInfo();

                // 删除补丁包
                FileUtils.forceDelete(new File(workDir
                        + updateInfo.getUpdateFileName()));
            }

            // 复制so
            String[] cpuABI = AndroidUtils.instance().getCPUAbi();
            File soFile = new File(workDir + KMetaData.GameCode + "/libs/"
                    + cpuABI[0] + "/libgame.so");
            if (!soFile.exists()) {
                soFile = new File(workDir + KMetaData.GameCode
                        + "/libs/armeabi/libgame.so");
            }
            if (soFile.exists()) {
                FileUtils.copyFileToDirectory(soFile, new File(libFileDir));
                FileUtils.forceDelete(soFile);
            }

            FileUtils.writeStringToFile(new File(workDir + KMetaData.GameCode
                    + "/res", ".nomedia"), "");
            msg = new Message();
            msg.what = CLOSE;
            mHandler.dispatchMessage(msg);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            AndroidUtils.showToast(ShellActivity.this, getResources()
                            .getString(RUtils.getStringId("hl_update_failed")),
                    Toast.LENGTH_SHORT);

        }
        Message msg = new Message();
        msg.what = CLOSE;
        mHandler.dispatchMessage(msg);


        return false;
    }

    /**
     * 下载游戏更新包
     *
     * @param updateInfo
     * @param workDir
     * @param number
     * @param idx
     */
    private void downloadGamePatch(KUpdateInfo updateInfo, String workDir,
                                   final int number, final int idx) {

        final String fileName = updateInfo.getUpdateFileName();

        try {
            // 开启线程进行下载
            final FileDownloader loader = new FileDownloader(
                    getApplicationContext(), updateInfo.getUpdatePackageUrl(),
                    workDir, fileName, 10);
            Log.d(TAG, "WorkDir:" + workDir + "\tfileName:" + fileName);

            final double length = loader.getFileSize();
            loader.download(new DownloadProgressListener() {

                public void onDownloadSize(final int size) {// 实时获知文件已经下载的数据长度
                    Message msg2 = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt("index", number);
                    msg2.what = SHOWSIZE;
                    msg2.arg1 = (int)(length/1000);
                    msg2.arg2 = (int)(size/1000);
                    msg2.setData(bundle);

                    mHandler.dispatchMessage(msg2);
                }

                @Override
                public void onDownloadFailed() {
                    Log.i(TAG, "onDownloadFailed");
                    AndroidUtils.showToast(
                            ShellActivity.this,
                            getResources()
                                    .getString(
                                            RUtils.getStringId("hl_update_download_failed")),
                            Toast.LENGTH_SHORT);
                    Message msg = new Message();
                    msg.what = CLOSE;
                    mHandler.dispatchMessage(msg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            AndroidUtils.showToast(
                    ShellActivity.this,
                    getResources().getString(
                            RUtils.getStringId("hl_update_download_failed")),
                    Toast.LENGTH_SHORT);
            Message msg = new Message();
            msg.what = CLOSE;
            mHandler.dispatchMessage(msg);
        }
    }

    /**
     * 启动游戏
     *
     * @param loginInfo
     */
    @SuppressLint("NewApi")
    private void loadGame(final KLoginInfo loginInfo) {
        isGamePause = true;
        isStart = false;
        isPause = false;

        runOnUiThread(new Runnable() {

            public void run() {
                try {
                    Log.i(TAG, "loadGame ####################################################### ");
                    Intent testPay = new Intent(ShellActivity.this, KoiLoginActivity.class);
                    ShellActivity.this.startActivity(testPay);
/*
                    final Bundle paramBundle = new Bundle();
                    JSONObject initConfig = new JSONObject();
                    paramBundle.putString("source", "youle"); // source
                    paramBundle.putString("channel", "youle"); // channel
                    paramBundle.putString("platHost", HConstant.PLATFORM_HOST); // 平台服务器地址
                    paramBundle.putString("workDir", workDir
                            + KMetaData.GameCode + "/"); // 游戏包（so和asserts）存放路径
                    paramBundle.putString("gameVersion",
                            String.valueOf(KMetaData.GameVersion)); // 游戏patch包版本号
                    paramBundle.putString("sdkVersion",
                            ConnectionBuilder.userConnection.getSDKVersion());
                    paramBundle.putString("apkVerionName",
                            String.valueOf(KMetaData.VersionName)); // Apk包VersionName
                    paramBundle.putString("gameAreas",
                            loginInfo.gameAreaPropses); // 分区列表
                    paramBundle.putString("sysInfos", generateJosnSystemInfos());
                    paramBundle.putString("initConfig", initConfig.toString());
                    paramBundle.putString("userInfos", generateJsonUserInfos()); // 用户信息
                    paramBundle.putString("appkey", AndroidUtils.instance()
                            .getMetaData(ShellActivity.this, "CMBI_APP_KEY")); // sohu
                    paramBundle.putString("worldAnnounce", loginInfo.worldAnnounceContent);
                    paramBundle.putString("chargeMoney", loginInfo.chargeMoney + "");

                    JNIActivity.onCreate(paramBundle);
                    ConnectionBuilder.userConnection.onloadGame();
                    isEnterGame = true;

                    Log.i("myInfos", generateJosnSystemInfos());
*/
                   // return;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * 解压游戏patch包
     *
     * @param fileName
     * @param workDir
     * @param totalFiles
     * @param loadedFiles
     * @throws FileNotFoundException
     * @throws Exception
     */
    private void unPackPatch(final String fileName, final String workDir,
                             final int totalFiles, final int loadedFiles)
            throws FileNotFoundException, Exception {
        int files = loadedFiles;
        BufferedInputStream bi;
        File file = new File(workDir + fileName);
        InputStream is = new FileInputStream(file);

        CheckedInputStream csumi = new CheckedInputStream(is, new CRC32());
        ZipInputStream in2 = new ZipInputStream(csumi);
        bi = new BufferedInputStream(in2);
        ZipEntry ze;
        while ((ze = in2.getNextEntry()) != null) {
            String entryName = ze.getName();
            if (ze.isDirectory()) {
                File decompressDirFile = new File(workDir + entryName);
                if (!decompressDirFile.exists()) {
                    decompressDirFile.mkdirs();
                }
            } else {
                Log.d(TAG, "zip to :" + workDir + entryName);
                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(workDir + entryName));
                byte[] buffer = new byte[1024];
                int readCount = bi.read(buffer);
                while (readCount != -1) {
                    bos.write(buffer, 0, readCount);
                    readCount = bi.read(buffer);
                }
                bos.close();
                files++;

                final Message msg = new Message();
                msg.what = PROCESS;
                Log.d(TAG, "files: " + files + ", totalFile: " + totalFiles
                        + ", process=" + (int) (files / totalFiles));
                msg.arg1 = (int) (((double) files / (double) totalFiles) * 100);
                mHandler.dispatchMessage(msg);
            }
        }

        bi.close();
    }

    /**
     * 选区完成
     *
     * @param bundle
     */
    public void onAreaSelected(Bundle bundle) {
        // 从游戏获取游戏id
        final int areaId = bundle.getInt("areaId", 1);
        final String areaName = bundle.getString("areaName");
        Log.i("myinfos", "onAreaSelected areaId = " + areaId);
        new Thread() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                String accessToken = KUserSession.instance().getUserInfo()
                        .getAccessToken();
                long uid = KUserSession.instance().getUserInfo().getUserId();
                KUserSession.instance().getUserInfo().setAreaCode(areaId);
                KUserSession.instance().getUserInfo().setAreaName(areaName);
                PreferenceUtils.instance().putInt("areaId", areaId);
                Log.i("myinfos", "onAreaSelected accessToken = " + accessToken);
                try {
                    // 从平台服务器获取用户信息
                    JSONObject obj = WebApiImpl.instance().getUserRoleInfo(uid,
                            accessToken);
                    JSONObject dataJson = JSONUtils.getJSONObject(obj, "data");
                    Log.i("myinfos", "onAreaSelected getUserRoleInfo = " + obj.toString());
                    int roleId = JSONUtils.getInt(dataJson, "roleId");
                    String roleName = JSONUtils.getString(dataJson, "roleName");
                    if (roleName == null) {
                        roleName = "";
                    }
                    String roleHead = JSONUtils.getString(dataJson, "roleHead");
                    int roleLevel = JSONUtils.getInt(dataJson, "roleLevel");
                    String areaCode = JSONUtils.getString(dataJson, "areaId");
                    String createRoleTime = JSONUtils.getString(dataJson, "createTime");

                    Log.i("myinfos", "dataJson :" + dataJson);
                    String loginSign = "loginSign";

                    KUserSession.instance().getUserInfo().setRoleId(String.valueOf(roleId));
                    PreferenceUtils.instance().putString("roleId", String.valueOf(roleId));
                    String SPACE = getPackageName() + ".SPACE.";
                    String roleIdKey =  SPACE + "ROLE_ID";
                    PreferenceUtils.instance().putString(roleIdKey, roleId + "");
                    KUserSession.instance().getUserInfo().setRoleName(roleName);
                    KUserSession.instance().getUserInfo().setRoleLevel(roleLevel);
                    KUserSession.instance().getUserInfo().setAreaCode(areaId);
                    KUserSession.instance().getUserInfo().setCreateRoleTime(Long.parseLong(createRoleTime)/1000);
                    // 通知三方连接
                    ConnectionBuilder.onAreaSelected(ShellActivity.this);
                    Method localMethodLoginSuccess;
                    try {
                        JNIActivity.loginSuccess(String.valueOf(roleId), roleName, loginSign);
                        ConnectionBuilder.userConnection.onSelectArea();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 读取配方包
                    KGameProxy.getInstance().loadPackages();

                    Method localMethodProductConfigs;
                    try {
                        JNIActivity.productConfigResults(KGameProxy.getGamePriceStr());

                        Log.i("myinfos", "loadPackages:" + KGameProxy.getGamePriceStr());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    /**
     * 奥飞聚合SDK数据传输.
     */
    public void submitExtraDate(Bundle bundle) {
        ConnectionBuilder.submitExtraData(bundle.getInt("actionId"), bundle.getInt("roleLevel"), bundle.getInt("roleId")
                , bundle.getInt("moneyNum"), bundle.getInt("gem"));
        if (bundle.getInt("actionId") > 2) {
            onLevelUp(bundle.getInt("roleLevel"));
        }
        KUserSession.instance().getUserInfo().setRoleLevel(bundle.getInt("roleLevel"));
        KUserSession.instance().getUserInfo().setGem(bundle.getInt("gem"));
        KUserSession.instance().getUserInfo().setGold(bundle.getInt("moneyNum"));
        KUserSession.instance().getUserInfo().setRoleId(bundle.getInt("roleId") + "");
    }

    /**
     * 检查角色名
     *
     * @param bundle
     */
    public void checkRoleName(Bundle bundle) {
        final String roleName = bundle.getString("roleName").trim();
        Log.i("roleName", "Name:" + roleName);
        new Thread() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                String accessToken = KUserSession.instance().getUserInfo()
                        .getAccessToken();
                String roleId = KUserSession.instance().getUserInfo()
                        .getRoleId();
                Integer result = 0;
                try {
                    WebApiImpl.instance().checkRoleName(accessToken, roleName,
                            roleId);
                    KUserSession.instance().getUserInfo().setRoleName(roleName);

                    Log.i("roleId", "roleId:" + roleId);
                    result = 1;
                    ConnectionBuilder.userConnection.creatRole();
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
                try {
                    JNIActivity.roleNameCheckResult(roleName, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    /**
     * 是否要播放视频，替换视频的路径
     *
     * @param bundle
     */
    public void openVideo(Bundle bundle) {
        isPlay = bundle.getBoolean("isPlay");
        videoName = bundle.getString("path");
        isHalfPlay = bundle.getBoolean("isHalfPlay");
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showVideoDialog();
            }
        });
    }

    /**
     * 通过dialog对话框实现播放游戏动画
     */
    public void showVideoDialog() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int mScreenWidth = metric.widthPixels;     // 屏幕宽度（像素）
        int mScreenHeight = metric.heightPixels;   // 屏幕高度（像素）

        if (isPlay) {
            if (null != mDlgVideoPlay) {
                mDlgVideoPlay.dismiss();
            }
            /*mDlgVideoPlay = new VideoDialog(JNIActivity, ShellActivity.this, workDir,
                    videoName, R.style.Dialog_Fullscreen);*/
            mDlgVideoPlay.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDlgVideoPlay.getWindow().setLayout(LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT);
            if (isHalfPlay) {
                Window dialogWindow = mDlgVideoPlay.getWindow();
                android.view.WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
                lp.x = (int) (mScreenWidth * 0.25); // 新位置X坐标
                lp.y = (int) (mScreenHeight * 0.25); // 新位置Y坐标
                lp.width = (int) (mScreenWidth * 0.5); // 宽度
                lp.height = (int) (mScreenHeight * 0.5); // 高度
                dialogWindow.setAttributes(lp);
            }
            mDlgVideoPlay.show();
        } else {
            Log.i(TAG, "关闭视频");
        }
    }


    /**
     * 显示公告
     *
     * @param bundle
     */
    public void showAnnounce(Bundle bundle) {
        boolean flag = true;
        String paramCode = bundle.getString("paramCode");
        JSONObject props = JSONUtils.build(paramCode);
        int areaId = JSONUtils.getInt(props, "areaId");
        int type = JSONUtils.getInt(props, "type");
        if (type == 2) {
            Log.i("Announce", "Announce:" + flag);
        }
        if (flag) {
            String url = KConstant.URL + "/game/getAnnounce?gameId="
                    + 27 + "&siteId=" + ResourceUtils.getInstance().getSiteResouceId(KMetaData.Site) + "&areaCode=" + areaId + "&notifyType=" + type;

            Log.e(TAG, url);

            int startX = Double.valueOf(JSONUtils.getDouble(props, "startX"))
                    .intValue();
            int startY = Double.valueOf(JSONUtils.getDouble(props, "startY"))
                    .intValue();
            int width = Double.valueOf(JSONUtils.getDouble(props, "width"))
                    .intValue();
            int height = Double.valueOf(JSONUtils.getDouble(props, "height"))
                    .intValue();
            boolean showVBar = JSONUtils.getBoolean(props, "showVBar");
            boolean showHBar = JSONUtils.getBoolean(props, "showHBar");
            Log.i("myinfos", "showAnnounce:" + props.toString());
            showWebView(url, startX, startY, width, height, showVBar, showHBar);
        }
    }

    /**
     * 显示网页
     *
     * @param bundle
     */
    public void showWebView(Bundle bundle) {
        String paramCode = bundle.getString("paramCode");
        JSONObject props = JSONUtils.build(paramCode);
        String url = JSONUtils.getString(props, "url");
        int startX = Double.valueOf(JSONUtils.getDouble(props, "startX"))
                .intValue();
        int startY = Double.valueOf(JSONUtils.getDouble(props, "startY"))
                .intValue();
        int width = Double.valueOf(JSONUtils.getDouble(props, "width"))
                .intValue();
        int height = Double.valueOf(JSONUtils.getDouble(props, "height"))
                .intValue();
        boolean showVBar = JSONUtils.getBoolean(props, "showVBar");
        boolean showHBar = JSONUtils.getBoolean(props, "showHBar");
        showWebView(url, startX, startY, width, height, showVBar, showHBar);
    }

    /**
     * 关闭网页
     */
    public void closeWebView(Bundle bundle) {
        runOnUiThread(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                if (webviewWindow != null) {
                    webviewWindow.dismiss();
                    webviewWindow = null;
                }
            }
        });
    }

    /**
     * 礼包领取
     *
     * @param bundle
     */
    public void onPromoteCode(Bundle bundle) {
        KoiGame.promoteCode(this);
    }

    /**
     * 打开用户中心
     *
     * @param bundle
     */
    public void onOpenMemberCenter(Bundle bundle) {
        Log.i("member", "shell onOpenMemberCenter");
        ConnectionBuilder.userConnection.memberCenter(this);
    }

    /**
     * 点击购买玩具
     *
     * @param bundle
     */
    public void onBuyToys(Bundle bundle) {
        String goodsId = bundle.getString("goodsId");
        Log.e(TAG, "onBuyToys. goodsId:" + goodsId);
    }

    /**
     * 支付时调用
     *
     * @param bundle
     */
    public void onPay(Bundle bundle) {
        String productId = bundle.getString("goodsCode");
        Log.i("pay", "goodsCode:" + productId);
        ConnectionBuilder.statisticsConnection.chargeClicked(this);

        KPayInfo payInfo = new KPayInfo();
        payInfo.setGoodsCode(productId);
        ConnectionBuilder.payConnection.synPay(payInfo);
    }

    /**
     * 切换账号
     *
     * @param bundle
     */
    public void onSwitchAccount(Bundle bundle) {
       // ConnectionBuilder.userConnection.logout(this, JNIActivity);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Log.i(TAG, "KEYCODE_BACK");
            if (isEnterGame) {
               // ConnectionBuilder.userConnection.logout(this, JNIActivity);
            }
        }
        return false;
    }

    /**
     * 点击退出
     *
     * @param bundle
     */
    public void onExit(Bundle bundle) {
        ConnectionBuilder.userConnection.exit();
    }



    /**
     * 支付成功回调
     *
     * @param bundle
     */
    public void onPayFinished(Bundle bundle) {

    }

    /**
     * 分享
     *
     * @param bundle
     */
    public void onShare(Bundle bundle) {

    }

    /**
     * 游戏代码崩溃调用
     *
     * @param bundle
     * @throws Throwable
     * @throws Exception
     */
    public void onCrash(Bundle bundle) {
        Log.e(TAG, "onCrash");
       /* final String message = bundle.getString("message");
        Intent intent = new Intent(this, HilinkCrashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("message", message);
        startActivity(intent);*/
    }

    /**
     * 直接访问服务器接口
     *
     * @param bundle
     * @return
     */
    public void serverPipeline(Bundle bundle) {
        String action = bundle.getString("action");
        String response = "";
        try {
            Log.i("request", "请求时间：" + System.currentTimeMillis());
            int before = (int) System.currentTimeMillis();
            response = WebApiImpl.instance().serverPipeline(action);
            int after = (int) System.currentTimeMillis();
            Log.i("request", "返回时间：" + System.currentTimeMillis());
            Log.i("request", "消耗时间：" + (after - before));
            Log.e("serverPipeline", response);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        try {
            JNIActivity.serverPipelineResult(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 升级通知
     */
    public void onLevelUp(final int level) {
        final String roleId = KUserSession.instance().getUserInfo().getRoleId();
        new Thread() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    KWebApiImpl.instance().levelUp(roleId, level, KUserSession.instance().getUserInfo().getAccessToken());
                } catch (KServiceException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 使用道具
     *
     * @param bundle
     */
    public void onUseItem(Bundle bundle) {
        // ConnectionBuilder.onUseItem(bundle);
    }

    /**
     * 进入游戏调用
     *
     * @param bundle
     */
    public void onEnterGame(Bundle bundle) {
        final int level = bundle.getInt("roleLevel");
        final int actionId = bundle.getInt("actionId");
        final int money = bundle.getInt("moneyNum");
        final int gem = bundle.getInt("gem");
        final int roleId = bundle.getInt("roleId");
        KUserSession.instance().getUserInfo().setRoleLevel(level);
        submitExtraDate(bundle);
        //onLevelUp(level);
        ConnectionBuilder.onEnterGame(bundle, this);
    }

    /**
     * 三方网页支付
     */
    public void onWebPay() {
        //ConnectionBuilder.payConnection.onWebPay();
    }

    /**
     * 事件记录
     *
     * @param bundle
     */
    public void logEvent(Bundle bundle) {
        // TODO Auto-generated method stub
        int actionId = bundle.getInt("actionId");
        ConnectionBuilder.logEvent(this, actionId);
    }

    public void changeRoleHead(Bundle bundle) {
        final String roleHead = bundle.getString("roleHead");
        final String roleId = KUserSession.instance().getUserInfo().getRoleId();
        new Thread() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    KWebApiImpl.instance().changeRoleHead(roleId, roleHead, KUserSession.instance().getUserInfo().getAccessToken());
                } catch (KServiceException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * 海外appFlyer时间记录
     */
    public void appFlyerEvent(Bundle bundle) {
        String levelName = bundle.getString("levelName");
        Log.i("appFlyer", "roleLevel=" + levelName);
        ConnectionBuilder.appFlyerEvent(this, levelName, "");
    }


    /**
     * 获取商品配方
     *
     * @param bundle
     */
    public void getProductConfigs(Bundle bundle) {
        // 读取配方包
        Log.i("pay", "进入商品配方。。");
        KGameProxy.getInstance().loadPackages();
        try {
            JNIActivity.productConfigResults(KGameProxy.getGamePriceStr());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 充值完成通知游戏
     */
    public void onPaySuccess() {
    }

    /**
     * 分享结果
     *
     * @param isSuccess
     */
    public void onShareResult(boolean isSuccess) {
        Log.e(TAG, "onShareResult : " + isSuccess);
        Integer result = 0;
        if (isSuccess) {
            new Thread() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        WebApiImpl.instance().shareSuccess();
                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            result = 1;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isStart) {
            isGamePause = true;
           // JNIActivity.onResume();
        }
        lastClickTime = 0;

        ConnectionBuilder.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isPause) {
           // JNIActivity.onPause();
        }
        ConnectionBuilder.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        ConnectionBuilder.onStop();
        //JNIActivity.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        ConnectionBuilder.onDestory();
       // JNIActivity.onDestroy();
    }
    @Override
    protected void onRestart() {
    	// TODO Auto-generated method stub
    	super.onRestart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        Log.i("ShellActivity", "onNewIntent");
        ConnectionBuilder.onNewIntent();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 拼接图片在云空间中的路径，也是当做一个Key作为标识

        super.onActivityResult(requestCode, resultCode, data);
        ConnectionBuilder.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        killApp();
    }

    public static final int CLOSE = 0, UNZIP_LOCAL_PROCESS = 1,
            DOWNLOAD_PATCH = 2, PROCESS = 3, INSTALL_PATCH = 4, INIT_SDK = 5,
            SDCARD_TIP = 6, NO_NET_TIP = 7, NO_WIFI_TIP = 8, SHOWSIZE = 9;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UNZIP_LOCAL_PROCESS:
                    showUnZipLocalLoading();
                    break;
                case DOWNLOAD_PATCH:
                    showDownPatchLoading();
                    break;
                case PROCESS:
                    showProcess(msg.arg1);
                    break;
                case SHOWSIZE:
                	showSize(msg.arg1, msg.arg2, msg.getData().getInt("index"));
                	showProcess((int)((( (double) msg.arg2 / (double) msg.arg1) * 100)));
                	break;
                case CLOSE:
                    closeResLoading();
                    break;
                case INSTALL_PATCH:
                    showInstallPatchLoading();
                case INIT_SDK:
                    // showInitSdkLoading();
                    break;
                case SDCARD_TIP:
                    showNoSdcardTip();
                    break;
                case NO_NET_TIP:
                    showNoNetTip();
                    break;
                case NO_WIFI_TIP:
                    KLoginInfo loginInfo = (KLoginInfo) msg.obj;
                    showGameUpdateTip(loginInfo);
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 显示无sdcard提示
     */
    private void showNoSdcardTip() {
        runOnUiThread(new Runnable() {

            public void run() {
                String title = ShellActivity.this.getResources().getString(
                        RUtils.getStringId("app_name"));
                String message = ShellActivity.this.getResources().getString(
                        RUtils.getStringId("hl_sdcard_not_exists"));
                String positiveButtonTip = ShellActivity.this.getResources()
                        .getString(RUtils.getStringId("hl_quit_game"));

                AlertDialog tipDialog = new AlertDialog.Builder(
                        ShellActivity.this)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton(positiveButtonTip,
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        killApp();
                                    }
                                }).create();
                tipDialog.setCancelable(false);
                tipDialog.show();
            }
        });
    }

    /**
     * 显示无网络提示
     */
    private void showNoNetTip() {
        runOnUiThread(new Runnable() {

            public void run() {
                String title = ShellActivity.this.getResources().getString(
                        RUtils.getStringId("app_name"));
                String message = ShellActivity.this.getResources().getString(
                        RUtils.getStringId("hl_check_network"));
                String positiveButtonTip = ShellActivity.this.getResources()
                        .getString(RUtils.getStringId("hl_retry"));
                String negativeButtonTip = ShellActivity.this.getResources()
                        .getString(RUtils.getStringId("hl_quit"));

                AlertDialog tipDialog = new AlertDialog.Builder(
                        ShellActivity.this)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton(positiveButtonTip,
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        start();
                                    }
                                })
                        .setNegativeButton(negativeButtonTip,
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        killApp();
                                    }
                                }).create();
                tipDialog.setCancelable(false);
                tipDialog.show();
            }
        });
    }

    /**
     * 显示无wifi提示
     */
    private void showGameUpdateTip(final KLoginInfo loginInfo) {
        runOnUiThread(new Runnable() {

            public void run() {
                String title = ShellActivity.this.getResources().getString(
                        RUtils.getStringId("hl_update_title"));
                String message = ShellActivity.this.getResources().getString(
                        RUtils.getStringId("hl_update_no_wifi"));

                String positiveButtonTip = ShellActivity.this.getResources()
                        .getString(RUtils.getStringId("hl_update_go_on"));
                String negativeButtonTip = ShellActivity.this.getResources()
                        .getString(RUtils.getStringId("hl_quit_game"));

                double totleSize = 0;
                final List<KUpdateInfo> infoList = loginInfo.versionInfo
                        .getResourceUpdateConfigs();
                for (KUpdateInfo info : infoList) {
                    totleSize = totleSize + info.getUpdatePackageSize();
                }
                Log.e(TAG, "[totleSize] " + totleSize);
                if (totleSize > 0) {
                    String showSize = String.valueOf(totleSize);
                    String unit = "kb";
                    if (totleSize > 1024) {
                        Log.e(TAG, "[totleSize] " + (totleSize / 1024));
                        showSize = NumberUtils.toDoubleView(totleSize / 1024);
                        unit = "MB";
                    }
                    message = message
                            + String.format(
                            ShellActivity.this
                                    .getResources()
                                    .getString(
                                            RUtils.getStringId("hl_update_package_size")),
                            showSize) + unit;
                }

                AlertDialog gameUpdateDialog = new AlertDialog.Builder(
                        ShellActivity.this)
                        .setMessage(message)
                        .setTitle(title)
                        .setPositiveButton(positiveButtonTip,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        TimerTask task = new TimerTask() {

                                            public void run() {
                                                try {
                                                    if (updateGame(infoList)) {
                                                        loadGame(loginInfo);
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        Timer timer = new Timer();
                                        timer.schedule(task, 100);
                                    }
                                })
                        .setNegativeButton(negativeButtonTip,
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        killApp();
                                    }
                                }).create();
                gameUpdateDialog.setCancelable(false);
                gameUpdateDialog.show();
            }
        });
    }

    /**
     * 解压游戏包
     */
    private void showUnZipLocalLoading() {
        runOnUiThread(new Runnable() {

            public void run() {
                if (ivLoaing0 == null) {
                    ivLoaing0 = (RoundCornerProgressBar) findViewById(RUtils.getViewId("hl_loading"));
                    ivLoadingProsess = (TextView) findViewById(RUtils.getViewId("hl_loading_process"));
                }
                if (resLoadingTip == null) {
                    resLoadingTip = (TextView) findViewById(RUtils.getViewId("hl_res_loading_tip_1"));
                    viceResLoadingTip = (TextView) findViewById(RUtils.getViewId("hl_res_loading_tip_2"));
                }

                resLoadingTip.setText(getResources().getString(
                        RUtils.getStringId("hl_unpack_tips")));
                viceResLoadingTip.setText(getResources().getString(
                        RUtils.getStringId("hl_unpack_tips")));

                if (llLoadingContainer == null) {
                    llLoadingContainer = (LinearLayout) findViewById(RUtils.getViewId("hl_loading_container"));
                }
                llLoadingContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 下载更新包
     */
    private void showDownPatchLoading() {
        runOnUiThread(new Runnable() {

            public void run() {
                if (ivLoaing0 == null) {
                    ivLoaing0 = (RoundCornerProgressBar) findViewById(RUtils.getViewId("hl_loading"));
                    ivLoadingProsess = (TextView) findViewById(RUtils.getViewId("hl_loading_process"));
                }

                if (resLoadingTip == null) {
                    resLoadingTip = (TextView) findViewById(RUtils.getViewId("hl_res_loading_tip_1"));
                    viceResLoadingTip = (TextView) findViewById(RUtils.getViewId("hl_res_loading_tip_2"));
                }
                resLoadingTip.setText(getResources().getString(
                        RUtils.getStringId("hl_update_downloading")));
                viceResLoadingTip.setText(getResources().getString(
                        RUtils.getStringId("hl_update_downloading")));

                if (llLoadingContainer == null) {
                    llLoadingContainer = (LinearLayout) findViewById(RUtils.getStringId("hl_loading_container"));
                }
                llLoadingContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 安装更新包进度
     */
    private void showInstallPatchLoading() {
        runOnUiThread(new Runnable() {

            public void run() {
                if (ivLoaing0 == null) {
                    ivLoaing0 = (RoundCornerProgressBar) findViewById(RUtils.getViewId("hl_loading"));
                    ivLoadingProsess = (TextView) findViewById(RUtils.getViewId("hl_loading_process"));
                }

                if (resLoadingTip == null) {
                    resLoadingTip = (TextView) findViewById(RUtils.getViewId("hl_res_loading_tip_1"));
                    viceResLoadingTip = (TextView) findViewById(RUtils.getViewId("hl_res_loading_tip_2"));
                }
                resLoadingTip.setText(getResources().getString(
                        RUtils.getStringId("hl_updating")));
                viceResLoadingTip.setText(getResources().getString(
                        RUtils.getStringId("hl_updating")));

                if (llLoadingContainer == null) {
                    llLoadingContainer = (LinearLayout) findViewById(RUtils.getViewId("hl_loading_container"));
                }
                llLoadingContainer.setVisibility(View.VISIBLE);
                ivLoaing0.setProgress(0);

            }
        });
    }

    /**
     * 显示进度
     *
     * @param process
     */
    private void showProcess(int process) {
        if (process > 100) {
            process = 100;
        }
        final int showProcess = process;
        runOnUiThread(new Runnable() {

            public void run() {
                ivLoaing0.setVisibility(View.VISIBLE);
                ivLoaing0.setProgress(showProcess);
                ivLoadingProsess.setText(showProcess + "%");

            }
        });
    }

    /**
     * 显示下载进度
     * @param length
     * @param size
     */
    public void showSize(final int length,final int size, final int index) {
    		runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					resLoadingTip.setText("正在更新資源(" + size + "/ " +length+ ")KB");
		    		viceResLoadingTip.setText("正在更新資源(" + size + "/ " +length+ ")KB");
				}
			});


    }

    /**
     * 关闭进度显示框
     */
    private void closeResLoading() {
        runOnUiThread(new Runnable() {

            public void run() {
                if (llLoadingContainer == null) {
                    llLoadingContainer = (LinearLayout) findViewById(RUtils.getViewId("hl_loading_container"));
                }

                llLoadingContainer.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 获取hl包文件数
     *
     * @param archive
     * @return
     */
    private int getHLFileNumbers(String archive) {
        try {
            int idx = archive.lastIndexOf("_");
            String total = archive.substring(idx + 1);
            total = StringUtils.substringBefore(total, ".hl");
            return Integer.parseInt(total);
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * 游戏资源是否已初始化
     *
     * @return
     */
    private boolean isGameLoaded() {
        return PreferenceUtils.instance().getBoolean(
                KMetaData.VersionName + ".loaded", false);
    }

    /**
     * wenview显示url
     */
    private void showWebView(final String url, final int startX,
                             final int startY, final int width, final int height,
                             final boolean showVBar, final boolean showHBar) {
        runOnUiThread(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                LayoutParams FILL = new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

                WebView webView = new WebView(ShellActivity.this);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setBackgroundColor(0); // 设置背景色
                webView.setBackgroundDrawable(getResources().getDrawable(
                        RUtils.getDrawableId("hl_login_bg")));
                webView.getBackground().setAlpha(0);
                webView.setVerticalScrollBarEnabled(showVBar);
                webView.setHorizontalScrollBarEnabled(showHBar);
                webView.setLayoutParams(FILL);

                webView.setWebViewClient(new WebViewClient() {
                    public void onReceivedError(WebView view, int errorCode,
                                                String description, String failingUrl) {
                        Log.e("showLinkWebView", "errorCode : " + errorCode
                                + "failingUrl : " + failingUrl);
                    }

                    public boolean shouldOverrideUrlLoading(WebView view,
                                                            String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                webView.loadUrl(url);

                webviewWindow = new PopupWindow(ShellActivity.this);
                webviewWindow.setWidth(width);
                webviewWindow.setHeight(height);
                webviewWindow.setFocusable(false);
                webviewWindow.setContentView(webView);

                webviewWindow.showAtLocation(
                        ((ViewGroup) findViewById(android.R.id.content))
                                .getChildAt(0), Gravity.LEFT | Gravity.TOP,
                        startX, startY);
            }

        });
    }


    /**
     * 自定义数据记录
     *
     * @param bundle
     */
    public void talkingEvent(Bundle bundle) {
        Log.i("TalkData", "talkingEvent");
        String eventId = bundle.getString("event");
        JSONObject jsonObject = JSONUtils.build(bundle.getString("eventData"));
        try {
            HashMap<String, String> hashMap = JSONUtils.toHashMap(jsonObject);
            Log.i("TalkData", "value:" + hashMap.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 记录角色等级
     *
     * @param bundle
     */
    public void getRoleLevel(Bundle bundle) {
        int appLevel = bundle.getInt("level");
        Log.i("TalkData", "getRoleLevel:" + appLevel);
        Log.i(TAG, "getRoleLevel:" + appLevel);
    }

    /**
     * 跳转到指的的网页.
     *
     * @param bundle
     */
    public void JumpToWeb(Bundle bundle) {
        // TODO Auto-generated method stub
        /*String webUrl = bundle.getString("webUrl");
        Intent intent = new Intent(this, ShellForWebview.class);
        intent.putExtra("webUrl", webUrl);
        startActivity(intent);*/
    }

    /**
     * 跟踪游戏消费点
     *
     * @param bundle
     */
    public void onPurchase(Bundle bundle) {
        Log.i("TalkData", "onPurchase");
        String item = bundle.getString("item");
        int itemNumber = bundle.getInt("itemNumber");
        Log.i(TAG, "onPurchase:" + item);
        double priceInVirtualCurrency = bundle.getDouble("priceInVirtualCurrency");
    }

    /**
     *
     */
    public void onReward(Bundle bundle) {
        //double virtualCurrencyAmount,String reason
        String reason = bundle.getString("reason");
        String virtualCurrencyAmount = bundle.getString("virtualCurrencyAmount");
        Log.i("TalkData", "onReward:reason:" + reason + ",orderNo:" + PreferenceUtils.instance().getString("orderNo", "0"));
    }

    /**
     * 任务，关卡，副本
     *
     * @param bundle
     */
    public void duplicateInfo(Bundle bundle) {
        Log.i("TalkData", "duplicateInfo");
        int action = bundle.getInt("action");
        String missionId = bundle.getString("missionId");
        String cause = bundle.getString("cause");

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (!isStart) {
            //JNIActivity.onWindowFocusChanged(hasFocus);
        }
    }


    public void initTalkData() {
        Log.i("TalkData", "initTalkdata:" + MetaData.talkData);
    }


    /**
     * 生成用户信息JSon串
     *
     * @return
     */
    private String generateJsonUserInfos() {
        JSONObject obj = new JSONObject();
        JSONUtils.put(obj, "userId", KUserSession.instance().getSdkUserId());
        JSONUtils.put(obj, "accountName", StringUtils.isEmpty(KUserSession
                .instance().getSdkUsername()) ? KUserSession.instance()
                .getSdkUserId() : KUserSession.instance().getSdkUsername());
        JSONUtils.put(obj, "uniqueKey", KMetaData.UniqueKey);


        Log.e(TAG, obj.toString());
        return obj.toString();
    }

    private String generateJosnSystemInfos() {
        JSONObject obj = new JSONObject();
        JSONUtils.put(obj, "ip", AndroidUtils.instance().getLocalIpAddress(this));
        JSONUtils.put(obj, "device_model", AndroidUtils.instance().getDeviceType());
        JSONUtils.put(obj, "os_name", "android");
//        JSONUtils.put(obj, "mac_addr", AndroidUtils.instance().getLocalMacAddress(this));
        JSONUtils.put(obj, "mac_addr", "0.0.0.0");
        JSONUtils.put(obj, "udid", KMetaData.UniqueKey);
        JSONUtils.put(obj, "app_channel", KMetaData.Channel);
        JSONUtils.put(obj, "app_ver", KMetaData.VerionCode);
        JSONUtils.put(obj, "network", KMetaData.netType);
        JSONUtils.put(obj, "platform_tag", "aofei");
        JSONUtils.put(obj, "server_id", KUserSession.instance().getUserInfo().getAreaCode());
        JSONUtils.put(obj, "group_id", 2);
        JSONUtils.put(obj, "channel_id", KMetaData.Channel);
        JSONUtils.put(obj, "system_version", Build.VERSION.RELEASE);
        JSONUtils.put(obj, "resolution", AndroidUtils.instance().getResolution());
        JSONUtils.put(obj, "package_name", this.getPackageName());

        return obj.toString();

    }

    public void killApp() {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 设置屏幕方向
     *
     * @param screenOrientation
     */
    public void setScreenOrientation(int screenOrientation) {
        this.screenOrientation = screenOrientation;
    }

    /**
     * 初始化首页面背景
     */
    protected void initMainBackground() {
        BitmapDrawable bgDrawable = null;
        /*
         * if (!MetaData.UsePlatformRes) { try { bgDrawable =
		 * ResourcesUtils.getDrawable(this, workDir + KMetaData.GameCode +
		 * "/res/hl_bg.png"); if (bgDrawable == null) { bgDrawable =
		 * (BitmapDrawable) Drawable.createFromStream(
		 * getAssets().open("res/hl_bg.png"), ""); } } catch (Exception e) { } }
		 */
        if (bgDrawable == null) { // 使用平台背景图
            Log.d(TAG, "read resource background from platform");
            bgDrawable = (BitmapDrawable) getResources().getDrawable(
                    RUtils.getDrawableId("hl_login_bg"));
        }
        bgDrawable = dealBgDrawable(bgDrawable);
        ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0)
                .setBackgroundDrawable(bgDrawable);
    }

    /**
     * 处理背景图
     *
     * @param bgDrawable
     * @return
     */
    protected BitmapDrawable dealBgDrawable(BitmapDrawable bgDrawable) {
        return bgDrawable;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult 权限请求成功 : " + requestCode);
        if (requestCode == 10000) {
            new Thread(new Runnable() {

                public void run() {
                    try {
                        Thread.sleep(500);
                        start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
	public int getPermissionsRequestCode() {
		// TODO Auto-generated method stub
        Log.i(TAG, "getPermissionsRequestCode 权限请求成功");
		return 10000;
	}

	@Override
	public String[] getPermissions() {
		// TODO Auto-generated method stub
		return new String[]{  
                Manifest.permission.WRITE_EXTERNAL_STORAGE,  
                Manifest.permission.READ_PHONE_STATE
        };  
	}

	@Override
	public void requestPermissionsSuccess() {
		// TODO Auto-generated method stub
        Log.i(TAG, "requestPermissionsSuccess 权限请求成功");

	}

	@Override
	public void requestPermissionsFail() {
		// TODO Auto-generated method stub
		killApp();
	}

}
