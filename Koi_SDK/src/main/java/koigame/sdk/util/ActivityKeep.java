package koigame.sdk.util;

/**
 * Created by wudi.
 * 2018.
 */

import android.app.Activity;

public class ActivityKeep
{
    private static ActivityKeep instance = new ActivityKeep();

    private Activity mainActivity = null;

    private ActivityKeep()
    {

    }

    public static ActivityKeep getInstance()
    {
        return instance;
    }

    public void setMainActivity( Activity mainActivity )
    {
        this.mainActivity = mainActivity;
    }

    public Activity getMainActivity()
    {
        return mainActivity;
    }

    public void cleanup()
    {
        mainActivity = null;
    }
}