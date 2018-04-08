package koigame.sdk.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtils {

    /**
     * 转为可使用，两位小数double值
     *
     * @param f
     * @return
     */
    public static double toLegalDouble(double f) {
        BigDecimal b = new BigDecimal(f);
        f = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        return f;
    }

    /**
     * 将double值转为显示格式
     *
     * @param f
     * @return
     */
    public static String toDoubleView(double f) {
        BigDecimal b = new BigDecimal(f);
        f = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(f);
    }

    /**
     * 验证用户名
     *
     * @param username 用户名
     * @return boolean
     */
    public static boolean checkUsername(String username) {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(username);
        return m.matches();
    }

    /**
     * 校验密码.
     */
    public static boolean checkPassword(String password) {
        String regex = "^[a-zA-Z0-9]{6,16}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 验证手机号码
     *
     * @param phoneNumber 手机号码
     * @return boolean
     */
    public static boolean checkPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^1[0-9]{10}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    /**
     * 验证身份证号.
     *
     * @param idCard 身份证号
     * @return boolean
     */
    public static boolean checkIdcard(String idCard) {
        Pattern pattern = Pattern.compile("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");
        Matcher matcher = pattern.matcher(idCard);
        return matcher.matches();
    }

    /**
     * 验证身份证号(只能是中文和英文).
     *
     * @param reaname 名字.
     * @return boolean
     */
    public static boolean checkRealName(String reaname) {
        Pattern pattern = Pattern.compile("[^\\u4E00-\\u9FA5]");
        Matcher matcher = pattern.matcher(reaname);
        return matcher.matches();
    }
}
