package koigame.sdk;

import android.util.Log;

public class KLog {
	
	/**
     * 错误
     * Write By LILIN
     * 2014-5-8
     * @param clazz
     * @param msg
     */
	public static void e(String Tag,String msg){
        if(KConstant.DEBUG){
            Log.e(Tag, msg+"");
        }
    }
    /**
     * 信息
     * Write By LILIN
     * 2014-5-8
     * @param clazz
     * @param msg
     */
    public static void i(String Tag,String msg){
        if(KConstant.DEBUG){
            Log.i(Tag, msg+"");
        }
    }
    /**
     * 警告
     * Write By LILIN
     * 2014-5-8
     * @param clazz
     * @param msg
     */
    public static void w(String Tag,String msg){
        if(KConstant.DEBUG){
            Log.w(Tag, msg+"");
        }
    }
    
    /**
     * 描述
     * Write By LILIN
     * 2014-5-8
     * @param clazz
     * @param msg
     */
    public static void d(String Tag,String msg){
        if(KConstant.DEBUG){
            Log.d(Tag, msg+"");
        }
    }
}
