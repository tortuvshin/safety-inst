package mn.btgt.safetyinst;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import mn.btgt.safetyinst.data.DatabaseHelper;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class AppMain extends Application{

    private static Context context;
    private static DatabaseHelper dbHelper;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this.getApplicationContext();
        dbHelper = new DatabaseHelper();
        DatabaseManager.initializeInstance(dbHelper);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static Context getContext(){
        return context;
    }

}
