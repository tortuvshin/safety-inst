package mn.btgt.safetyinst.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by turtuvshin on 10/11/17.
 */

public class SafConstants {

    public static String WebURL = "http://demo.mongolgps.com";
    public static String ApiUrl = WebURL+"/phone.php/safe_settings";
    public static String SendUrl = WebURL+"/phone.php/safe_upload";
    public static String APP_NAME = "iSafe";

    public static String getSecretCode(String imei, String time){
        return MD5_Hash(imei + "-BTGT-" + time);
    }

    @Nullable
    public static String MD5_Hash(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    @NonNull
    public static String getAndroiId(Context myContext){
        String androidId = Settings.Secure.getString(myContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("ANDROID ID", "ID: "+androidId.toString());
        return (androidId.toString() == null) ? "355694060878908" : androidId.toString();
    }

    public static String getAppVersion(Context myContext){
        PackageInfo pi;
        try {
            pi = myContext.getPackageManager().getPackageInfo(myContext.getPackageName(), 0);
            String appV = "v" + pi.versionName + "." + pi.versionCode ;
            return appV;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "00";
        }
    }

    public static String getImei(Context myContext) {
        TelephonyManager mngr = (TelephonyManager) myContext.getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return (mngr.getDeviceId() == null) ? "355694060878908" : mngr.getDeviceId();
    }
}
