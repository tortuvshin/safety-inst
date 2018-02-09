package mn.btgt.safetyinst;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import mn.btgt.safetyinst.database.DatabaseHelper;
import mn.btgt.safetyinst.database.DatabaseManager;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class AppMain extends Application{

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this.getApplicationContext();
        DatabaseHelper dbHelper = new DatabaseHelper();
        DatabaseManager.initializeInstance(dbHelper);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static Context getContext(){
        return context;
    }

}
