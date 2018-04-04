package koigame.sdk;

import android.app.Application;
import android.util.Log;

import koigame.sdk.util.RUtils;

public class KoiApplication extends Application {

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		RUtils.init(getPackageName() + ".R");
		Log.i("koiapplication", "packagename: " + getPackageName() + ".R");
	}
}
