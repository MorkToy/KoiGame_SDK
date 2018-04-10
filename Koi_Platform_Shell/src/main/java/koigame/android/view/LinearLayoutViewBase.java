
package koigame.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public abstract class LinearLayoutViewBase<T> extends LinearLayout implements ViewBase<T> {
    public View rootView;

    public ViewChangedListener viewChangedListener;

    public String layoutSrc = null;

    public String dirType;

    public String category;

    public int layoutId;

    private LayoutInflater layoutInflater;

    public Handler handler = new Handler();

    public Context context;

    public LinearLayoutViewBase(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public LinearLayoutViewBase(Context context, int layoutId) {
        this(context, layoutId, true);
    }

    public LinearLayoutViewBase(Context context, int layoutId, boolean shouldInit) {
        super(context);
        this.context = context;
        this.layoutId = layoutId;
        if (shouldInit)
            init();
    }

    public LinearLayoutViewBase(Context context, AttributeSet attrs) {
        this(context, attrs, true);
    }

    public LinearLayoutViewBase(Context context, AttributeSet attrs, boolean shouldInit) {
        super(context, attrs);
        this.context = context;
        if (shouldInit) {
            initAttr(getContext(), attrs);
            init();
        }
    }

    public void init() {
        initLayout();
        initView();
        initEvent();
        initData(null);
    }

    public void initAttr(Context context, AttributeSet attrs) {
        // TypedArray a =
        // context.obtainStyledAttributes(attrs,RUtils.getStyleableId("ViewBase"));
        // layoutSrc = a.getString(RUtils.getStyleableId("ViewBase_layoutSrc"));
        layoutId = context.getResources().getIdentifier(layoutSrc, "layout", "com.sie");
        // dirType = a.getString(RUtils.getStyleableId("ViewBase_dirType"));
        // category = a.getString(RUtils.getStyleableId("ViewBase_category"));
    }

    public void initLayout() {
        layoutInflater = ((LayoutInflater) getContext().getSystemService("layout_inflater"));
        rootView = layoutInflater.inflate(layoutId, this, true);
    }

    /**
     * 通常用于初始化数据
     */
    public void initData(T object) {

    }

    /**
     * 通常用于初始化数据
     */
    public T initData() {
        return null;
    }

    public void update(T object) {

    }

    /**
     * 根据一个对象来填充View
     * 
     * @param object
     */
    public void buildView(T object) {

    }

    /**
     * 如果此View被LayoutManager管理，那么当此View显示时，LayoutManager将调用此方法，给具体子类一次处理UI显示的机会
     */
    public void onFocus() {

    }

    /**
     * 如果此View被LayoutManager管理，那么当此View隐藏时，LayoutManager将调用此方法，给具体子类一次处理UI隐藏的机会
     */
    public void onFreeze() {

    }

    public void onRelease() {

    }

    public void onRelease(View view) {
        if (view != null) {
            view.destroyDrawingCache();
            view = null;
        }
    }

    public void onRelease(ViewGroup view) {
        if (view != null) {
            view.removeAllViews();
            view.destroyDrawingCache();
            view = null;
        }
    }

    public void setOnViewChangedListener(ViewChangedListener listener) {
        viewChangedListener = listener;
    }

    public void changeView(View view) {
        if (viewChangedListener != null && view != null) {
            viewChangedListener.changed(view);
        }
    }

    // 为子类提供便捷的查找View对象的方法
    public View findView(int resId) {
        if (resId == -1)
            return null;
        if (rootView == null)
            return null;

        return rootView.findViewById(resId);
    }

    /**
     * 将一个bitmap变灰
     * 
     * @param bitmap 要变灰的原图
     * @return 变灰图
     */
    public Bitmap toGrayBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap grayImg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(grayImg);

        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return grayImg;
    }

    /**
     * 将一个bitmap圆角
     * 
     * @param bitmap 要圆角的原图
     * @param radius 半径
     * @return 圆角图
     */
    public Bitmap genRoundedCornerBitmap(Bitmap bitmap, int radius) {
        Bitmap retBitmap;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        retBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
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

    public String getSymbol() {
        try {
            NumberFormat formater = NumberFormat.getCurrencyInstance(Locale.CHINA);
            return formater.getCurrency().getSymbol();
        } catch (Exception e) {
            return "";
        }
    }

    private DecimalFormat format = new DecimalFormat("#,###,###,### ");

    public String getFormated(long number) {
        return format.format(number);
    }

    public enum DirType {
        TOP, BOTTOM, MIDDLE, SINGLE;
    }

}
