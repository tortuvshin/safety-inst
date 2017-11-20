package mn.btgt.safetyinst.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by turtuvshin on 10/3/17.
 */

public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "SafetyInst";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String LOGGED_IN = "isLoggedIn";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    /**
     * Application хамгийн анх ачааллахад
     *
     * @param isFirstTime анх програм нээхэд true
     */
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    /**
     * Application хамгийн анх ачааллаж байгаа эсэх
     */
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    /**
     * Хэрэглэгч нэвтрэх болон гарах үед ашиглана
     *
     * @param isLoggedIn нэвтрэх үед true гарах үед false
     */
    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    /**
     * нэвтэрсэн хэрэглэгч байгаа эсэх
     *
     * @return хэрэглэгч нэвтэрсэн эсвэл нэвтрээгүй байна
     */
    public boolean isLoggedIn(){
        return pref.getBoolean(LOGGED_IN, false);
    }
}