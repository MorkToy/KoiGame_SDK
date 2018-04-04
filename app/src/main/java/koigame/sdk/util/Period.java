package koigame.sdk.util;
import java.util.HashMap;
import java.util.Iterator;

public class Period {

    private static HashMap<String, Period> m_byString = new HashMap<String, Period>();
    private static Period[] m_byInt = new Period[20];

    /**
     * Every site has an integer code which is also stored in the db.
     */
    private int code;
    /**
     * Every site has a String code.
     */
    private String name;


    /**
     * SECOND, MINUTE, HOUR, DAY, WEEK, MONTH, QUARTER, YEAR;
     * Means that no valid site has been set.
     */
    public static final Period SECOND       = new Period(0, "SECOND");
    public static final Period MINUTE       = new Period(1, "MINUTE");
    public static final Period HOUR         = new Period(2, "HOUR");
    public static final Period DAY          = new Period(3, "DAY");
    public static final Period WEEK         = new Period(4, "WEEK");
    public static final Period MONTH        = new Period(5, "MONTH");
    public static final Period QUARTER      = new Period(6, "QUARTER");
    public static final Period YEAR         = new Period(7, "YEAR");

    /**
     * Return the Period associated with a string.  If the string is bad,
     * a null is returned.
     */
    public static Period getByString(String sval) {
        return (Period) m_byString.get(sval);
    }

    /**
     * Return the Period associated with an int code.  If the code is bad,
     * a null is returned.
     */
    public static Period getByInt(int ival) {
        if (0 <= ival && ival < m_byInt.length) {
            return (m_byInt[ival]);
        }

        return null;
    }

    /**
     * Returns an iterator over the list of Periods
     */
    public static Iterator<Period> iterator() {
        return m_byString.values().iterator();
    }

    public static int size() {
        return m_byString.size();
    }

    /**
     * NEVER USE THIS constructor. Unfortunately we need it in order to
     * implement Externalizable.
     */
    public Period() {
    }

    private Period(int code, String name) {
        this.code = code;
        this.name = name;

        m_byString.put(name, this);
        m_byInt[code] = this;
    }


    public int getCode() {
        return code;
    }

    public final int getInt() {
        return code;
    }

    public boolean reconstitute(String s) {
        Period Period = (Period) m_byString.get(s);
        if (Period == null)
            return false;

        code = Period.code;
        return true;
    }

    public String deconstitute() {
        return name;
    }

    /**
     * Return the String id for this game.
     */
    public String getGameId() {
        return deconstitute();
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (code != period.code) return false;
        if (name != null ? !name.equals(period.name) : period.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Period{" +
                "code=" + code +
                ", name='" + name + '\'' +
                '}';
    }
}
