package koigame.sdk.util;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A static class for date utility methods.
 */
public class DateUtil {
	public static final String DEFAULT_FORMAT = "MMM-dd-yyyy";
	public static final String LONG_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String HOUR_FORMAT = "yyyy-MM-dd HH";
	public static final String DAY_FORMAT = "yyyy-MM-dd";
	public static final String CY_DAY_FORMAT = "yyyyMMddHHmmss";
	public static final String MONTH_FORMAT = "yyyy-MM";
	public static final String YEAR_FORMAT = "yyyy";
	public static final int MILLIS_IN_A_DAY = 24 * 60 * 60 * 1000;

	public static String getHour() {
		return DateUtil.dateFormat(new Date(), HOUR_FORMAT);
	}

	public static String getDay() {
		return DateUtil.dateFormat(new Date(), DAY_FORMAT);
	}

	public static String getMonth() {
		return DateUtil.dateFormat(new Date(), MONTH_FORMAT);
	}

	public static String getYear() {
		return DateUtil.dateFormat(new Date(), YEAR_FORMAT);
	}

	public static String getHour(Date dateTime) {
		return DateUtil.dateFormat(dateTime, HOUR_FORMAT);
	}

	public static String getDay(Date dateTime) {
		return DateUtil.dateFormat(dateTime, DAY_FORMAT);
	}

	public static String getMonth(Date dateTime) {
		return DateUtil.dateFormat(dateTime, MONTH_FORMAT);
	}

	public static String getYear(Date dateTime) {
		return DateUtil.dateFormat(dateTime, YEAR_FORMAT);
	}

	/**
	 * Parse a String and return a date object.
	 * 
	 * @return Returns a Date object. If we could not parse, we return a null
	 *         object.
	 */
	public static Date parse(String dateString) {
		Calendar cur = Calendar.getInstance();
		int year = cur.get(Calendar.YEAR);

		for (int i = 0; i < m_parsers.length; i++) {
			Calendar calendar = m_parsers[i].parse(dateString);
			if (calendar != null)
				return calendar.getTime();
		}
		return null;
	}

	private static DateParser[] m_parsers = { new DateParserShort(),
			new DateParserAlpha(), new DateParserDefault() };

	public static Calendar getCalendar(String day, String month, String year) {
		Date date = null;

		if (day == null || day.length() == 0 || month == null
				|| month.length() == 0 || year == null || year.length() == 0) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		try {
			int iday = Integer.parseInt(day);
			int imonth = Integer.parseInt(month);
			int iyear = Integer.parseInt(year);

			// make sure values are in valid ranges
			if (iday < 1 || iday > 31 || imonth < 1 || imonth > 12 || iyear < 0)
				return null;

			calendar.set(Calendar.YEAR, iyear);
			calendar.set(Calendar.MONTH, imonth - 1);

			int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			if (iday > maxDay)
				return null;

			calendar.set(Calendar.DAY_OF_MONTH, iday);
		} catch (NumberFormatException nfe) {
			return null;
		}
		return calendar;
	}

	public static boolean isValidBirthDate(Calendar calendar) {
		Calendar curCalendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);

		return year > 1900 && year < curCalendar.get(Calendar.YEAR);
	}

	public static boolean isValidBirthDate(Date date) {
		return isValidBirthDate(p_getCalendar(date));
	}

	public static boolean isUnder13(Calendar calendar) {
		return p_isUnder(calendar, 13);
	}

	public static boolean isUnder18(Date date) {
		return isUnder18(p_getCalendar(date));
	}

	public static boolean isUnder13(Date date) {
		return isUnder13(p_getCalendar(date));
	}

	public static boolean isUnder18(Calendar calendar) {
		return p_isUnder(calendar, 18);
	}

	private static boolean p_isUnder(Calendar calendar, int under) {
		Calendar curCalendar = Calendar.getInstance();
		curCalendar.add(Calendar.YEAR, -calendar.get(Calendar.YEAR));
		curCalendar.add(Calendar.MONTH, -calendar.get(Calendar.MONTH));
		curCalendar.add(Calendar.DAY_OF_MONTH,
				-calendar.get(Calendar.DAY_OF_MONTH));
		return curCalendar.get(Calendar.YEAR) < under;
	}

	private static Calendar p_getCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	/**
	 * Makes a timestamp out of the current time and date, replacing spaces,
	 * slashes, and colons with more file-system convenient characters.
	 * 
	 * @return <tt>String</tt> representation of current date and time
	 */
	public static String getTimeStamp() {
		Date startDate = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(
				DateFormat.SHORT, DateFormat.SHORT);
		String dateString = dateFormat.format(startDate);
		dateString = dateString.replace(' ', '.');
		dateString = dateString.replace('/', '.');
		dateString = dateString.replace(':', '.');
		return dateString;
	}

	/**
	 * Uses the SimpleDateFormatter to format the date.
	 * 
	 * @param date
	 * @param format
	 *            if format==null, it will format using the mask MMM-dd-yyyy
	 * @return string
	 */
	public static String dateFormat(Date date, String format) {
		SimpleDateFormat formatter = null;

		if (date == null) {
			return "";
		}
		if (format == null) {
			format = DEFAULT_FORMAT;
		}
		try {
			formatter = new SimpleDateFormat(format);
		} catch (Exception e) {
			return "";
		}

		return formatter.format(date, new StringBuffer(), new FieldPosition(0))
				.toString().toUpperCase();
	}

	public static String dateFormat(Date date) {
		return dateFormat(date, LONG_FORMAT);
	}
	public static String NdateFormat(Date date) {
		return dateFormat(date, CY_DAY_FORMAT);
	}
	public static String dateFormat(Date date, Period period) {
		SimpleDateFormat formatter = null;

		if (date == null) {
			return "";
		}
		String format = DEFAULT_FORMAT;

		int code = period.getCode();
		switch (code) {
		case 2: // HOUR
			format = HOUR_FORMAT;
			break;
		case 3: // DAY
			format = DAY_FORMAT;
			break;
		case 5: // MONTH
			format = MONTH_FORMAT;
			break;
		case 7: // YEAR
			format = YEAR_FORMAT;
			break;
		default:
		}
		try {
			formatter = new SimpleDateFormat(format);
		} catch (Exception e) {
			return "";
		}

		return formatter.format(date, new StringBuffer(), new FieldPosition(0))
				.toString().toUpperCase();
	}

	/**
	 * format the date into specialed foramt and timezone
	 * 
	 * @param date
	 * @param tz
	 * @param dateFormat
	 * @return String
	 */
	public static String formatDateToString(Date date, TimeZone tz,
			String dateFormat) {
		if (dateFormat == null) {
			dateFormat = DEFAULT_FORMAT;
		}

		DateFormat df = new SimpleDateFormat(dateFormat);
		df.setTimeZone(tz);
		return df.format(date);
	}

	/**
	 * parse the string into date with special timezone and format
	 * 
	 * @param dateStr
	 * @param tz
	 * @param dateFormat
	 * @return Date
	 */
	public static Date parseStringToDate(String dateStr, TimeZone tz,
			String dateFormat) {
		if (dateFormat == null) {
			dateFormat = DEFAULT_FORMAT;
		}

		DateFormat df = new SimpleDateFormat(dateFormat);
		df.setTimeZone(tz);

		Date d = null;
		try {
			d = df.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}

		return d;
	}

	public static Date parseStringToDate(String dateStr) {
		return parseStringToDate(dateStr, LONG_FORMAT);
	}

	public static Date parseStringToDate(String dateStr, String dateFormat) {
		if (dateFormat == null) {
			dateFormat = DEFAULT_FORMAT;
		}

		DateFormat df = new SimpleDateFormat(dateFormat);

		Date d = null;
		try {
			d = df.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}

		return d;
	}

	/**
	 * Returns a date object for the passed in date/time in the GMT timezone.
	 * Useful for regression tests when doing date comparisons.
	 * 
	 * @param year
	 *            (1955, not 55)
	 * @param month
	 *            (1-12)
	 * @param day
	 *            The day in the month 1-31
	 * @param hour
	 *            The hour in military time, 0-23.
	 * @param min
	 *            Minute, 0-60.
	 * @param sec
	 *            Second, 0-60.
	 */
	public static Date getDate(int year, int month, int day, int hour, int min,
			int sec) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, sec);
		cal.set(Calendar.MILLISECOND, 0);
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		return cal.getTime();
	}

	/**
	 * Normalizes a day to the current day, ie, sets the hour, min, sec, msec
	 * fields to 0. Assumes the Date is in GMT. If a null is passed in, a null
	 * is returned.
	 */
	public static Date normalizeToDay(Date date) {
		if (date == null)
			return null;

		TimeZone timeZone = (TimeZone.getTimeZone("GMT"));
		return normalizeToDay(date, timeZone);
	}

	/**
	 * Normalizes a day to the current day, ie, sets the hour, min, sec, msec
	 * fields to 0. If a null is passed in, a null is returned. if null time
	 * zone is provided, default is used.
	 */
	public static Date normalizeToDay(Date date, TimeZone timeZone) {
		if (date == null)
			return null;
		if (timeZone == null)
			timeZone = TimeZone.getDefault();

		Calendar c = Calendar.getInstance(timeZone);
		c.setTime(date);
		Calendar cout = Calendar.getInstance(timeZone);
		cout.set(Calendar.YEAR, c.get(Calendar.YEAR));
		cout.set(Calendar.MONTH, c.get(Calendar.MONTH));
		cout.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
		clearTime(cout);
		return cout.getTime();
	}

	/**
	 * Clear the hh:mm:ss:msecs fields from a calendar object. Note that the
	 * passed in object is modified.
	 */
	public static void clearTime(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
	}

	private static class DateParserDefault extends DateParser {
		private SimpleDateFormat m_formatter = new SimpleDateFormat(
				DEFAULT_FORMAT);

		public Calendar parse(String dateString) {
			Calendar calendar = null;
			Date date = null;
			synchronized (m_formatter) {
				try {
					date = m_formatter.parse(dateString, new ParsePosition(0));
				} catch (Exception e) {
					System.out.println("DateParserDefault: could not parse: " + dateString
							+ " using default format of: " + DEFAULT_FORMAT
							+ ", exc=" + e);
				}
			}
			if (date != null) {
				calendar = Calendar.getInstance();
				calendar.setTime(date);
			}
			return calendar;
		}
	}

	public static int getDaysBetweenDates(Date date1, Date date2) {
		if (date1 == null)
			throw new IllegalArgumentException("date1 is null");
		if (date2 == null)
			throw new IllegalArgumentException("date2 is null");

		double msecs = date1.getTime() - date2.getTime();
		int days = (int) Math.ceil(msecs / MILLIS_IN_A_DAY);
		if (days > 0)
			return days;
		return 0;

	}
	
    public static long getHoursBetweenDates(Date date2, Date date1) {
        long day = 0;

        long time2 = date2.getTime();
        long time1 = date1.getTime();
        long diff = time2 - time1;
        return (diff / (60 * 60 * 1000) - day * 24);
    }
}