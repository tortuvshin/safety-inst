package mn.btgt.safetyinst.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import mn.btgt.safetyinst.SplashActivity;

/**
 * Created by turtuvshin on 10/11/17.
 */

public class SafConstants {

    public static String APP_NAME            = "iSafe";
    public static int    APP_USER_TYPE       = 1;
    public static String APP_ENV             = "development";
    public static boolean APP_DEBUG          = true;
    public static String WEB_URL             = "http://demo.mongolgps.com";
    public static String API_URL             = WEB_URL +"/phone.php/safe_settings";
    public static String SEND_URL            = WEB_URL +"/phone.php/safe_upload";
    public static String SETTINGS_COMPANY    = "company";
    public static String SETTINGS_DEPARTMENT = "department";
    public static String SETTINGS_IMEI       = "imei";
    public static String SETTINGS_ANDROID_ID = "android_id";
    public static String SETTINGS_LOGO       = "company_logo";
    public static String SETTINGS_ISSIGNED   = "is_signed";

    public static String getSecretCode(String imei, String time){
        return MD5_Hash(imei + "-BTGT-" + time);
    }

    @Nullable
    private static String MD5_Hash(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    public static String getAndroiId(Context myContext){
        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(myContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("ANDROID ID", "ID: "+ androidId);
        return (APP_ENV.equals("development")) ? "355694060878908" : androidId;
    }

    public static String getAppVersion(Context myContext){
        PackageInfo pi;
        try {
            pi = myContext.getPackageManager().getPackageInfo(myContext.getPackageName(), 0);
            return "v" + pi.versionName + "." + pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "00";
        }
    }

    @SuppressLint("HardwareIds")
    public static String getImei(Context myContext) {
        TelephonyManager mngr = (TelephonyManager) myContext.getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        assert mngr != null;
        return (APP_ENV.equals("development")) ? "355694060878908" : mngr.getDeviceId();
    }
}
