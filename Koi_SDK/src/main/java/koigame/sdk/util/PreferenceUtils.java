package koigame.sdk.util;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {
	private static PreferenceUtils instance = new PreferenceUtils();
	private static SharedPreferences pre;

	private PreferenceUtils() {

	}

	public static PreferenceUtils instance() {
		return instance;
	}

	public static PreferenceUtils instance(Context ctx) {
		if (pre == null) {
			pre = PreferenceManager.getDefaultSharedPreferences(ctx);
		}
		return instance;
	}

	public void putString(String name, String value) {
		pre.edit().remove(name).commit();
		pre.edit().putString(name, value).commit();
	}

	public void putLong(String name, long value) {
		pre.edit().remove(name).commit();
		pre.edit().putLong(name, value).commit();
	}

	public void putInt(String name, int value) {
		pre.edit().remove(name).commit();
		pre.edit().putInt(name, value).commit();
	}

	public void putFloat(String name, float value) {
		pre.edit().remove(name).commit();
		pre.edit().putFloat(name, value).commit();
	}

	public String getString(String name, String defValue) {
		return pre.getString(name, defValue);
	}

	public long getLong(String name, long defValue) {
		return pre.getLong(name, defValue);
	}

	public int getInt(String name, int defValue) {
		return pre.getInt(name, defValue);
	}

	public float getFloat(String name, float defValue) {
		return pre.getFloat(name, defValue);
	}

	public void putBoolean(String name, Boolean value) {
		pre.edit().remove(name).commit();
		pre.edit().putBoolean(name, value).commit();
	}

	public boolean getBoolean(String name, boolean defValue) {
		return pre.getBoolean(name, defValue);
	}

	public void remove(String name) {
		pre.edit().remove(name).commit();
	}
}