package mn.btgt.safetyinst;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class AppMain extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogAdapter(new AndroidLogAdapter());
    }
}
