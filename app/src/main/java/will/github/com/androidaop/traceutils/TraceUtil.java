package will.github.com.androidaop.traceutils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by will on 2018/3/9.
 */

public class TraceUtil {
    private final String TAG = "TraceUtil";

    /**
     * 当Activity执行了onCreate时触发
     *
     * @param activity
     */
    public static void onActivityCreate(Activity activity) {
        Toast.makeText(activity
                , activity.getClass().getName() + "call onCreate"
                , Toast.LENGTH_LONG).show();
    }


    /**
     * 当Activity执行了onDestroy时触发
     *
     * @param activity
     */
    public static void onActivityDestroy(Activity activity) {
        Toast.makeText(activity
                , activity.getClass().getName() + "call onDestroy"
                , Toast.LENGTH_LONG).show();
    }
}
