package mn.btgt.safetyinst.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "SafetyInst";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String LOGGED_IN = "isLoggedIn";
    private static final String LOGGED_IN_USERNAME = "loggedUsername";
    private static final String LOGGED_IN_PASS = "loggedPass";
    private static final String SUBMIT_SNOTE = "submitSnote";

    @SuppressLint("CommitPrefEdits")
    public PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    /**
     * Application хамгийн анх ачааллахад
     *
     * @param isFirstTime анх програм нээхэд true
     */
    public void setLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public void setUser(String username) {
        editor.putString(LOGGED_IN_USERNAME, username);
        editor.commit();
    }

    public String getUserName () {
        return pref.getString(LOGGED_IN_USERNAME, "");
    }

    public void setSnote(String snote) {
        editor.putString(SUBMIT_SNOTE, snote);
        editor.commit();
    }

    public String getSnote () {
        return pref.getString(SUBMIT_SNOTE, "");
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