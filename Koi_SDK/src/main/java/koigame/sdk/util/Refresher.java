package koigame.sdk.util;
/**
 * A simple utility class to keep track of refreshing that might need
 * to happen.
 */
public class Refresher {
    private int m_interval;
    private long m_lastTime;

    /**
     * @param interval How long between each interval, in msecs.
     */
    public Refresher(int interval) {
        this(interval, System.currentTimeMillis());
    }

    /**
     * @param interval How long between each interval in msecs.
     * @param lastTime The time to use as the "last time" this got
     *                 invoked. Eg, if 0, then calling shouldRefresh() will return true.
     *                 If System.currentTimeMillis(), then you'll have to wait 'interval'
     *                 msecs before shouldRefresh() will return true. The value is in
     *                 msecs since the epoch.
     */
    public Refresher(int interval, long lastTime) {
        m_interval = interval;
        m_lastTime = lastTime;
    }

    /**
     * Will return true if (currentTime - lastTime > interval)
     */
    public synchronized boolean shouldRefresh() {
        long curTime = System.currentTimeMillis();
        if ((curTime - m_lastTime) > m_interval) {
            m_lastTime = curTime;
            return true;
        }
        return false;
    }

    /**
     * Sets the object to be "refreshed" as of this moment.
     */
    public synchronized void refreshed() {
        m_lastTime = System.currentTimeMillis();
    }

    /**
     * Resets the refresher so that the next call to shouldRefresh()
     * will return 'true'.
     */
    public synchronized void reset() {
        m_lastTime = 0;
    }
}