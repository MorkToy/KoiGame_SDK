package koigame.android.shell;

import android.os.Bundle;

import koigame.sdk.KoiGame;

/**
 * Created by wudi .
 * on 2018/4/9.
 */

public class MainActivity extends ShellActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenOrientation(KoiGame.SCREEN_ORIENTATION_PORTRAIT);

    }
}
