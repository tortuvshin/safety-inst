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

    private static final String PREF_NAME = "SafetyInst";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String LOGGED_IN = "isLoggedIn";
    private static final String LOGGED_USER_ID = "loggedUserId";
    private static final String LOGGED_USERNAME = "loggedUsername";
    private static final String SUBMIT_SNOTE_ID = "submitSnoteId";
    private static final String SUBMIT_SNOTE_NAME = "submitSnoteName";

    @SuppressLint("CommitPrefEdits")
    public PrefManager(Context context) {
        Context context1 = context;
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    /**
     * Application хамгийн анх ачааллахад
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

    public void setUserId(String userid) {
        editor.putString(LOGGED_USER_ID, userid);
        editor.commit();
    }

    public String getUserId () {
        return pref.getString(LOGGED_USER_ID, "");
    }

    public void setUsername(String username) {
        editor.putString(LOGGED_USERNAME, username);
        editor.commit();
    }

    public String getUserName () {
        return pref.getString(LOGGED_USERNAME, "");
    }

    public void setSnoteId(String snoteid) {
        editor.putString(SUBMIT_SNOTE_ID, snoteid);
        editor.commit();
    }

    public String getSnoteId () {
        return pref.getString(SUBMIT_SNOTE_ID, "");
    }

    public void setSnoteName(String snotename) {
        editor.putString(SUBMIT_SNOTE_NAME, snotename);
        editor.commit();
    }

    public String getSnoteName () {
        return pref.getString(SUBMIT_SNOTE_NAME, "");
    }

    /**
     * нэвтэрсэн хэрэглэгч байгаа эсэх
     * @return хэрэглэгч нэвтэрсэн эсвэл нэвтрээгүй байна
     */
    public boolean isLoggedIn(){
        return pref.getBoolean(LOGGED_IN, false);
    }
}