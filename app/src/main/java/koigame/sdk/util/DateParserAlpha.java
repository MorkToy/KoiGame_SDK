package koigame.sdk.util;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * Will parse dates of the form dd-mmm-yy
 */
public class DateParserAlpha extends DateParser {
    public Calendar parse(String dateString) {
        Calendar c = Calendar.getInstance();

        StringTokenizer t = new StringTokenizer(dateString, "-");
        try {
            String sday = t.nextToken();
            String smon = t.nextToken();
            String syear = t.nextToken();

            int day = Integer.parseInt(sday);
            int mon = p_cvtMon(smon);
            int year = Integer.parseInt(syear);

            //------------------------------------------------
            // Note that the Calendar.set() call wants
            // the real 4-digit year. If we get 2-digits,
            // this is inherently ambiguous and we can
            // only do our best.
            //------------------------------------------------

            if (year < 19)
                year += 2000;
            else if (year < 100)
                year += 1900;

            DateUtil.clearTime(c);
            c.set(year, mon, day);
        }
        catch (Exception e) {
            return null;
        }
        return c;
    }

    private int p_cvtMon(String smon) {
        String usmon = smon.toUpperCase();
        Integer ival = (Integer) m_smonToInt.get(usmon);
        if (ival == null) {
            
            return 0;
        }
        return ival.intValue();
    }

    private static Hashtable m_smonToInt = new Hashtable(12);

    static {
        m_smonToInt.put("JAN", new Integer(0));
        m_smonToInt.put("FEB", new Integer(1));
        m_smonToInt.put("MAR", new Integer(2));
        m_smonToInt.put("APR", new Integer(3));
        m_smonToInt.put("MAY", new Integer(4));
        m_smonToInt.put("JUN", new Integer(5));
        m_smonToInt.put("JUL", new Integer(6));
        m_smonToInt.put("AUG", new Integer(7));
        m_smonToInt.put("SEP", new Integer(8));
        m_smonToInt.put("OCT", new Integer(9));
        m_smonToInt.put("NOV", new Integer(10));
        m_smonToInt.put("DEC", new Integer(11));
    }
}
