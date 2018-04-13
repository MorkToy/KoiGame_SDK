package koigame.android.shell;


import android.app.Activity;

import koigame.sdk.KoiApplication;
import koigame.sdk.KoiGame;


public class ShellApplication extends KoiApplication {

	private static final String TAG = "ShellApplication";
	public Activity act;
	public Activity jniAcitivty;
	private static final String AF_DEV_KEY = "Rb4QUsjTdgtCPZv5tWUoEo";

	@Override
	public void onCreate() {
		super.onCreate();
		KoiGame.init(this);
		MetaData.init(getApplicationContext());

	}
}