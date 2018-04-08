package koigame.sdk.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DateRange implements java.io.Serializable {
    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static SimpleDateFormat LONG_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Date mStartDate;
    private Date mEndDate;

    protected DateRange() { /* An empty constructor useful for Hibernate */ }

    public DateRange(Date exactDate) {
        if (exactDate == null)
            throw new IllegalArgumentException("date is null");

        mStartDate = mEndDate = exactDate;
    }

    public DateRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null)
            throw new IllegalArgumentException("start or end date is null");

        if (startDate.after(endDate))
            throw new IllegalArgumentException("start date is later than end date: " + startDate + ", " + endDate);

        mStartDate = startDate;
        mEndDate = endDate;
    }


    public DateRange(String startDate, String endDate) {
        if (startDate == null || endDate == null)
            throw new IllegalArgumentException("start or end date is null");

        Date sd = DateUtil.parseStringToDate(startDate, DATE_FORMAT);

        Date ed = DateUtil.parseStringToDate(endDate, DATE_FORMAT);
        if (sd.after(ed))
            throw new IllegalArgumentException("start date is later than end date: " + startDate + ", " + endDate);

        mStartDate = sd;
        mEndDate = ed;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        if (startDate != null && mEndDate != null
                && startDate.after(mEndDate))
            throw new IllegalArgumentException("start date is later than end date");

        mStartDate = startDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        if (mStartDate != null && endDate != null
                && mStartDate.after(endDate))
            throw new IllegalArgumentException("start date is later than end date");

        mEndDate = endDate;
    }

    public boolean isExactDate() {
        return (mStartDate != null && mEndDate != null
                && mStartDate.equals(mEndDate));
    }

    public Date getExactDate() {
        if (isExactDate())
            return mStartDate;

        return null;
    }

    public List<Date> iterator(Period period) {
        List<Date> list = new LinkedList<Date>();
        long start = mStartDate.getTime();
        long end = mEndDate.getTime();

        int code = period.getCode();
        switch (code) {
            case 1:        //MINUTES

                while (start < end) {
                    list.add(new Date(start));
                    start = start + 60000;
                }

                break;
            case 2:        //HOUR

                while (start < end) {
                    list.add(new Date(start));
                    start = start + 60 * 60000;
                }

                break;
            case 3:        //  DAY
                while (start < end) {
                    list.add(new Date(start));
                    start = start + 24 * 60 * 60000;
                }
            case 5:        //  MONTH
                while (start < end) {
                    list.add(new Date(start));
                    start = start + 30 * 24 * 60 * 60000;
                }
                break;
            default:
                return list;
        }
        return list;
    }

    public List<String> iteratorString(Period period) {
        List<String> list = new LinkedList<String>();
        long start = mStartDate.getTime();
        long end = mEndDate.getTime();

        int code = period.getCode();
        switch (code) {
            case 1:        //MINUTES
                while (start < end) {
                    list.add(DateUtil.dateFormat(new Date(start), period));
                    start = start + 60000;
                }
                break;
            case 2:        //HOUR
                while (start < end) {
                    list.add(DateUtil.dateFormat(new Date(start), period));
                    start = start + 60 * 60000;
                }
                break;
            case 3:        //  DAY
                while (start < end) {
                    list.add(DateUtil.dateFormat(new Date(start), period));
                    start = start + 24 * 60 * 60000;
                }
                break;
            case 5:        //  MONTH
                while (start < end) {
                    list.add(DateUtil.dateFormat(new Date(start), period));

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date(start));
                    cal.add(Calendar.MONTH, 1);
                    start = cal.getTime().getTime();
                }
                break;
            default:
                return list;
        }
        return list;
    }

    /**
     * @param date
     * @return true if the date within this object
     */
    public boolean contains(Date date) {
        return contains(new DateRange(date));
    }

    /**
     * @param range
     * @return true if the range within this object
     */
    public boolean contains(DateRange range) {
        return mStartDate.getTime() <= range.mStartDate.getTime() && mEndDate.getTime() >= range.mStartDate.getTime();
    }

    /**
     * @param range
     * @return true if the range and this object are not disjoined
     */
    public boolean isMergeable(DateRange range) {
        return isMergeable(range, 0);
    }

    public boolean isMergeable(DateRange range, int gapIntervalAllowed) {

        if (range == null) {
            throw new IllegalArgumentException("cannot compare with null object");
        }

        DateRange earlyStart, lateStart;

        if (mStartDate.before(range.mStartDate)) { // if this object starts before range
            earlyStart = this;
            lateStart = range;
        } else {
            earlyStart = range;
            lateStart = this;
        }

        long timeDiff = lateStart.mStartDate.getTime() - earlyStart.mEndDate.getTime();

        return timeDiff <= gapIntervalAllowed;
    }

    public void merge(DateRange range) {
        merge(range, 0);
    }

    public void merge(DateRange range, int gapIntervalAllowed) {
        if (!isMergeable(range, gapIntervalAllowed))
            throw new RuntimeException("they are not mergeable");

        if (range.mStartDate.before(mStartDate))
            mStartDate = range.mStartDate;

        if (range.mEndDate.after(mEndDate))
            mEndDate = range.mEndDate;
    }

    public Object clone() {
        return new DateRange((Date) mStartDate.clone(), (Date) mEndDate.clone());
    }

    public String toString() {
        String s;

        if (mStartDate == mEndDate)
            s = "DateRange[" + LONG_SDF.format(mStartDate) + "]";
        else
            s = "DateRange[" + LONG_SDF.format(mStartDate) + "\t-\t" + LONG_SDF.format(mEndDate) + "]";

        return s;
    }

    public int hashCode() {
        if (mStartDate == mEndDate)
            return mStartDate.hashCode();
        else
            return mStartDate.hashCode() + mEndDate.hashCode();
    }

    public boolean equals(Object obj) {
        DateRange range = (DateRange) obj;

        if (range == null)
            return false;

        return mStartDate.equals(range.mStartDate) && mEndDate.equals(range.mEndDate);
    }

    /**
     * Normalize the dates in here to the current day, ie, set the
     * hh:mm:ss:msec fields to 0.
     */
    public void normalizeToDay() {
        if (mStartDate != null)
            mStartDate = DateUtil.normalizeToDay(mStartDate);

        if (mEndDate != null)
            mEndDate = DateUtil.normalizeToDay(mEndDate);
    }

    /**
     * Returns the # of days this date-range specifies, rounding down.
     */
    public int getNumDays() {
        long t1 = mStartDate.getTime();
        long t2 = mEndDate.getTime();
        return (int) ((t2 - t1) / 1000 / 3600 / 24);
    }

    /**
     * Returns the # of days this date-range specifies, rounding down.
     */
    public int getMinutes() {
        long t1 = mStartDate.getTime();
        long t2 = mEndDate.getTime();
        return (int) ((t2 - t1) / 1000 / 60);
    }

    public static DateRange HOUR() {
        return new DateRange(DateTimeUtils.getCurrentHourStartTime(), DateTimeUtils.getCurrentHourEndTime());
    }

    public static DateRange HOUR(int offset) {
        return new DateRange(DateTimeUtils.getCurrentHourStartTime(), DateTimeUtils.getCurrentHourEndTime());
    }


    public static DateRange DAY() {
        return new DateRange(DateTimeUtils.getCurrentDayStartTime(), DateTimeUtils.getCurrentDayEndTime());
    }

    public static DateRange WEEK() {
        return new DateRange(DateTimeUtils.getCurrentWeekDayStartTime(), DateTimeUtils.getCurrentWeekDayEndTime());
    }

    public static DateRange MONTH() {
        return new DateRange(DateTimeUtils.getCurrentMonthStartTime(), DateTimeUtils.getCurrentMonthEndTime());
    }

    public static DateRange MONTH(int offset) {
        return new DateRange(DateTimeUtils.getCurrentMonthStartTime(), DateTimeUtils.getCurrentMonthEndTime());
    }

    public static DateRange QUARTER() {
        return new DateRange(DateTimeUtils.getCurrentQuarterStartTime(), DateTimeUtils.getCurrentQuarterEndTime());
    }

    public static DateRange YEAR() {
        return new DateRange(DateTimeUtils.getCurrentYearStartTime(), DateTimeUtils.getCurrentYearEndTime());
    }

    public static void main(String args[]) {
        System.out.println(DateRange.HOUR());
        System.out.println(DateRange.DAY());
        System.out.println(DateRange.WEEK());
        System.out.println(DateRange.MONTH());
        System.out.println(DateRange.QUARTER());
        System.out.println(DateRange.YEAR());


        DateRange range = new DateRange("2012-10-16 10:00:00", "2012-10-26 18:00:00");
        System.out.println(range.contains(DateUtil.parseStringToDate("2012-10-15 10:00:0", DateUtil.LONG_FORMAT)));
        System.out.println(range.contains(DateUtil.parseStringToDate("2012-10-17 10:00:0", DateUtil.LONG_FORMAT)));
        System.out.println(range.contains(DateUtil.parseStringToDate("2012-20-28 10:00:0", DateUtil.LONG_FORMAT)));
        System.out.println(range.contains(new Date()));
    }
}
