package koigame.sdk.util;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Will parse dates of the form mm/dd/yy OR mm/dd/yyyy
 */
public class DateParserShort extends DateParser {
    public Calendar parse(String dateString) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        Date d = null;
        try {
            d = df.parse(dateString);
        }
        catch (ParseException e) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c;
    }
}
