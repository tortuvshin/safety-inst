package mn.btgt.safetyinst.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import mn.btgt.safetyinst.activity.DeviceListActivity;
import mn.btgt.safetyinst.activity.SettingsActivity;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class SAFCONSTANT {

    public static int    APP_USER_TYPE       = 1;
    public static int    APP_ENV             = 1;
    public static boolean APP_DEBUG          = true;
    public static String APP_NAME            = "iSafe";
    public static String WEB_URL             = "http://demo.mongolgps.com";
    public static String API_URL             = WEB_URL.concat("/phone.php/safe_settings");
    public static String SEND_URL            = WEB_URL.concat("/phone.php/safe_upload");
    public static String SETTINGS_COMPANY    = "company";
    public static String SETTINGS_DEPARTMENT = "department";
    public static String SETTINGS_IMEI       = "imei";
    public static String SETTINGS_ANDROID_ID = "android_id";
    public static String SETTINGS_LOGO       = "company_logo";
    public static String SETTINGS_ISSIGNED   = "is_signed";
    public static String SETTINGS_PRINTER_FONT_ENCODE = "printer_encode";
    public static String SETTINGS_PRINTER_FONT_SIZE = "printer_font_size";

    public static final String SHARED_PREF_NAME = "manager_preferences";
    public static final String PREF_PRINTER_ADDRESS = "printer_address";
    public static final String PREF_HEAD = "head";
    public static final String PREF_FOOT = "foot";

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;

    public static String last_printer_address = "";
    private static final int MESSAGE_DEVICE_NAME = 1;
    private static final int MESSAGE_TOAST = 2;
    private static final int MESSAGE_READ = 3;
    private static final String DEVICE_NAME = "device_name";
    private static final String TOAST = "toast";
    @SuppressLint("StaticFieldLeak")
    private static Context my_context;
    private static BluetoothAdapter mBluetoothAdapter;
    private static BluetoothPrintService mPrintService;


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
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(myContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("ANDROID ID", "ID: "+ androidId);
        return androidId == null ? "915d19ef8b0e30b2" : androidId;
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
        return mngr.getDeviceId() == null ? "35451605332605" : mngr.getDeviceId();
    }

    @SuppressLint("HandlerLeak")
    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("printer handler ", "msg : " + msg.toString());
            switch (msg.what) {
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    Toast.makeText(my_context, "BT Printer connected", Toast.LENGTH_SHORT).show();
                    SettingsActivity.togglePrinter.setChecked(true);
                    SettingsActivity.togglePrinter.setEnabled(true);
                    break;
                case 2:
                    Toast.makeText(my_context, "BT Printer NOT connect!", Toast.LENGTH_SHORT).show();
                    SettingsActivity.togglePrinter.setChecked(false);
                    SettingsActivity.togglePrinter.setEnabled(true);
                    last_printer_address = "";
                    break;
            }

        }
    };

    public static void findBT(Activity act) {
        my_context = act.getApplicationContext();
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Toast.makeText(act.getApplicationContext(), "No bluetooth adapter available", Toast.LENGTH_LONG).show();
            } else if (!mBluetoothAdapter.isEnabled()) {
                Toast.makeText(act.getApplicationContext(), "bluetooth -г асаана уу", Toast.LENGTH_LONG).show();
            }  else if ( mPrintService !=null && mPrintService.getState() == BluetoothPrintService.STATE_CONNECTED) {
                Log.d("bt printer","connected baina");
            }   else{
                openBT(act,last_printer_address);
            }
            //Toast.makeText(myContext, "Bluetooth Device Found", Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openBT(Activity act, String printer_address) {
        Log.d("printer open", "open printer address : " + printer_address);
        if (mPrintService == null)
            mPrintService = new BluetoothPrintService(act, mHandler);
        Intent serverIntent = null;
        if (printer_address == null || printer_address.length() == 0 || SAFCONSTANT.last_printer_address == "") {
            serverIntent = new Intent(act, DeviceListActivity.class);
            act.startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            Toast.makeText(act.getApplicationContext(), "Bluetooth printer not selected", Toast.LENGTH_LONG).show();
        } else {
            // Standard SerialPortService ID
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(printer_address);
            // Attempt to connect to the device
            if (!last_printer_address.equals(printer_address)) {

                SharedPreferences.Editor editor = act.getSharedPreferences(
                        SAFCONSTANT.SHARED_PREF_NAME, act.MODE_PRIVATE).edit();
                editor.putString(SAFCONSTANT.PREF_PRINTER_ADDRESS, printer_address);
                editor.apply();

                last_printer_address = printer_address;
            }
            mPrintService.connect(device, true);
        }
    }
    public static void closeBT(){
        if (mPrintService != null && mPrintService.getState() == BluetoothPrintService.STATE_CONNECTED)
            mPrintService.stop();
    }

    public static boolean checkPrinter(){
        if (mPrintService == null) return false;
        if (mBluetoothAdapter == null) return false;
        if (mPrintService.getState() == BluetoothPrintService.STATE_CONNECTED) return true;
        else return false;
    }

    public static void sendData(byte[] data) {
        try {
            // the text typed by the user
            if (checkPrinter()) {
                mPrintService.write(data);
            }
            // tell the user data were sent
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
