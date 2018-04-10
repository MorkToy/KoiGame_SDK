package koigame.android.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * 货币工具类
 * @author Mike
 */
public class CurrcyUtils {
    
    public static String getSymbol(Locale locale) {
        try {
            NumberFormat formater = NumberFormat.getCurrencyInstance(locale);
            return formater.getCurrency().getSymbol();
        } catch (Exception e) {
            return "";
        }
    }
}
