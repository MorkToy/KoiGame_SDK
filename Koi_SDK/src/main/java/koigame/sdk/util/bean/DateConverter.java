package koigame.sdk.util.bean;
import java.util.Calendar;
import java.util.Date;

public class DateConverter extends AbstractConverter {

    public DateConverter(Object defaultValue) {
        super(defaultValue);
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Convert an input Date/Calendar object into a String.
     * <p/>
     * <b>N.B.</b>If the converter has been configured to with one or more
     * patterns (using <code>setPatterns()</code>), then the first pattern will
     * be used to format the date into a String. Otherwise the default
     * <code>DateFormat</code> for the default locale (and <i>style</i> if
     * configured) will be used.
     *
     * @param value The input value to be converted
     * @return the converted String value.
     * @throws Throwable if an error occurs converting to a String
     */
    protected String convertToString(Object value) {

        Date date = null;
        if (value instanceof Date) {
            date = (Date) value;
        } else if (value instanceof Calendar) {
            date = ((Calendar) value).getTime();
        } else if (value instanceof Long) {
            date = new Date(((Long) value).longValue());
        }

        return value.toString();
    }

    /**
     * Convert the input object into a Date object of the specified type.
     * <p/>
     * This method handles conversions between the following types:
     * <ul>
     * <li><code>java.util.Date</code></li>
     * <li><code>java.util.Calendar</code></li>
     * <li><code>java.sql.Date</code></li>
     * <li><code>java.sql.Time</code></li>
     * <li><code>java.sql.Timestamp</code></li>
     * </ul>
     * <p/>
     * It also handles conversion from a <code>String</code> to any of the above
     * types.
     * <p/>
     * <p/>
     * For <code>String</code> conversion, if the converter has been configured
     * with one or more patterns (using <code>setPatterns()</code>), then the
     * conversion is attempted with each of the specified patterns. Otherwise
     * the default <code>DateFormat</code> for the default locale (and
     * <i>style</i> if configured) will be used.
     *
     * @param targetType Data type to which this value should be converted.
     * @param value      The input value to be converted.
     * @return The converted value.
     * @throws Exception if conversion cannot be performed successfully
     */
    public Object convert(Class targetType, Object value) {

        Class sourceType = value.getClass();

        // Handle java.sql.Timestamp
        if (value instanceof java.sql.Timestamp) {

            java.sql.Timestamp timestamp = (java.sql.Timestamp) value;
            long timeInMillis = ((timestamp.getTime() / 1000) * 1000);
            timeInMillis += timestamp.getNanos() / 1000000;
            return toDate(targetType, timeInMillis);
        }

        // Handle Date (includes java.sql.Date & java.sql.Time)
        if (value instanceof Date) {
            Date date = (Date) value;
            return toDate(targetType, date.getTime());
        }

        // Handle Calendar
        if (value instanceof Calendar) {
            Calendar calendar = (Calendar) value;
            return toDate(targetType, calendar.getTime().getTime());
        }

        // Handle Long
        if (value instanceof Long) {
            Long longObj = (Long) value;
            return toDate(targetType, longObj.longValue());
        }

        // Convert all other types to String & handle
        String stringValue = value.toString().trim();
        // Default String conversion
        return toDate(targetType, stringValue);

    }

    /**
     * Convert a long value to the specified Date type for this
     * <i>Converter</i>.
     * <p/>
     * <p/>
     * This method handles conversion to the following types:
     * <ul>
     * <li><code>java.util.Date</code></li>
     * <li><code>java.util.Calendar</code></li>
     * <li><code>java.sql.Date</code></li>
     * <li><code>java.sql.Time</code></li>
     * <li><code>java.sql.Timestamp</code></li>
     * </ul>
     *
     * @param type  The Date type to convert to
     * @param value The long value to convert.
     * @return The converted date value.
     */
    private Object toDate(Class type, long value) {

        // java.util.Date
        if (type.equals(Date.class)) {
            return new Date(value);
        }

        // java.sql.Date
        if (type.equals(java.sql.Date.class)) {
            return new java.sql.Date(value);
        }

        // java.sql.Time
        if (type.equals(java.sql.Time.class)) {
            return new java.sql.Time(value);
        }

        // java.sql.Timestamp
        if (type.equals(java.sql.Timestamp.class)) {
            return new java.sql.Timestamp(value);
        }

        // java.util.Calendar
        if (type.equals(Calendar.class)) {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(new Date(value));
            calendar.setLenient(false);
            return calendar;
        }

        String msg = getClass() + " cannot handle conversion to '" + type + "'";

        throw new ConversionException(msg);
    }

    /**
     * Default String to Date conversion.
     * <p/>
     * This method handles conversion from a String to the following types:
     * <ul>
     * <li><code>java.sql.Date</code></li>
     * <li><code>java.sql.Time</code></li>
     * <li><code>java.sql.Timestamp</code></li>
     * </ul>
     * <p/>
     * <strong>N.B.</strong> No default String conversion mechanism is provided
     * for <code>java.util.Date</code> and <code>java.util.Calendar</code> type.
     *
     * @param type  The Number type to convert to
     * @param value The String value to convert.
     * @return The converted Number value.
     */
    private Object toDate(Class type, String value) {
        // java.sql.Date
        if (type.equals(java.sql.Date.class)) {
            try {
                return java.sql.Date.valueOf(value);
            } catch (IllegalArgumentException e) {
                throw new ConversionException(
                        "String must be in JDBC format [yyyy-MM-dd] to create a java.sql.Date");
            }
        }

        // java.sql.Time
        if (type.equals(java.sql.Time.class)) {
            try {
                return java.sql.Time.valueOf(value);
            } catch (IllegalArgumentException e) {
                throw new ConversionException(
                        "String must be in JDBC format [HH:mm:ss] to create a java.sql.Time");
            }
        }

        // java.sql.Timestamp
        if (type.equals(java.sql.Timestamp.class)) {
            try {
                return java.sql.Timestamp.valueOf(value);
            } catch (IllegalArgumentException e) {
                throw new ConversionException(
                        "String must be in JDBC format [yyyy-MM-dd HH:mm:ss.fffffffff] "
                                + "to create a java.sql.Timestamp");
            }
        }

        String msg = getClass() + " does not support default String to '"
                + type + "' conversion.";
        throw new ConversionException(msg);
    }

}
