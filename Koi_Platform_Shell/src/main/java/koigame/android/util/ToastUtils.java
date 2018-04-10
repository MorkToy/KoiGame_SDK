package koigame.android.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void alert(Context context, String str) {
        Toast toast=Toast.makeText(context, str, Toast.LENGTH_LONG);
        toast.show();
    }
}
