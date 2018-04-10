package koigame.android.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class BitmapUtils {
	public static Drawable createDrawableOnBottomOfRight(Bitmap src, Bitmap water) {
		return new BitmapDrawable(createBitmapOnBottomOfRight(src, water));
	}

	public static Bitmap createBitmapOnBottomOfRight(Bitmap src, Bitmap water) {
		return createBitmap(src, water, src.getWidth() - water.getWidth() + 1, src.getHeight() - water.getHeight() + 1);
	}

	public static Drawable createDrawableOnCenter(Bitmap src, Bitmap water) {
		return new BitmapDrawable(createBitmapOnCenter(src, water));
	}

	public static Bitmap createBitmapOnCenter(Bitmap src, Bitmap water) {
		return createBitmap(src, water, src.getWidth() / 2 - water.getWidth() / 2,
				src.getHeight() / 2 - water.getHeight() / 2);
	}

	public static Bitmap createBitmap(Bitmap src, Bitmap water, float left, float top) {
		Bitmap newb = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(src, 0, 0, null);
		cv.drawBitmap(water, left, top, null);
		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();
		return newb;
	}

	public static Drawable createDrawable(Bitmap src, Bitmap water, float left, float top) {
		return new BitmapDrawable(createBitmap(src, water, left, top));
	}

	public static Bitmap drawable2Bitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
				drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static Bitmap genRoundedCornerBitmap(Bitmap bitmap, int radius) {
		Bitmap retBitmap;
		if (bitmap == null)
			Log.d("BitmapUtils","BitmapUtils.genRoundedCornerBitmap : bitmap is null!");

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		retBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(retBitmap);
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, width, height);
		RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(-12434878);
		canvas.drawRoundRect(rectF, radius, radius, paint);
		PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
		PorterDuffXfermode duffXfermode = new PorterDuffXfermode(mode);
		Xfermode xfermode = paint.setXfermode(duffXfermode);
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return retBitmap;
	}
	
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}