package koigame.sdk.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;

import java.io.File;

public class ResourcesUtils {

	/**
	 * 读取资源文字
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		return null;
	}

	/**
	 * 获得图片
	 *
	 * @return
	 */
	public static BitmapDrawable getDrawable(Context context, String path) {
		try {
			if (new File(path).exists()) {
				return new BitmapDrawable(context.getResources(), path);
			}
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * 缩放图片，使适应屏幕
	 * 
	 * @param activity
	 * @param drawable
	 * @return
	 */
	public static BitmapDrawable set2FitScreen(Activity activity, BitmapDrawable drawable) {
		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int newWidth = metric.widthPixels; // 屏幕宽度（像素）
		int newHeight = metric.heightPixels; // 屏幕高度（像素）

		Bitmap bitmap = drawable.getBitmap();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		float scale = Math.min(scaleWidth, scaleHeight);

		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		// create the new Bitmap object
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		BitmapDrawable bmd = new BitmapDrawable(activity.getResources(), resizedBitmap);
		return bmd;
	}

}
