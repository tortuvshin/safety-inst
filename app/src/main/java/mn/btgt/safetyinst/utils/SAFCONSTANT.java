package mn.btgt.safetyinst.utils;

import android.Manifest;
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

    public static final String SHARED_PREF_NAME = "manager_preferences";
    public static final String PREF_PRINTER_ADDRESS = "printer_address";
    public static final String PREF_HEAD = "head";
    public static final String PREF_FOOT = "foot";
    public static final String PREF_LOCK_PRICE = "lockprice";
    public static final String PREF_LOCK_DISCOUNT = "lockdiscount";
    public static final String PREF_DEFAULT_VALUE = "default_value";
    public static final String PREF_DEFAULT_CODE = "default_code";

    public static final String SETTINGS_KEY_PRINTER_FONT= "printer_font";
    public static final String SETTINGS_KEY_PRINTER_CODEPAGE= "printer_codepage";
    public static final String SETTINGS_KEY_RD = "company_rd";
    public static final String SETTINGS_KEY_PRINT_LOGO = "print_logo";
    public static final String SETTINGS_KEY_PRINT_WIDTH = "print_chars";
    public static final String SETTINGS_KEY_PRINTER_IP = "print_ip";
    public static final String SETTINGS_KEY_PRINTER_PORT = "print_port";
    public static final String SETTINGS_KEY_TAX_NOAT_TYPE = "tax_noat_type";
    public static final String SETTINGS_KEY_TAX_NHAT_TYPE = "tax_nhat_type";

    public static final String DEFAULT_PRINTER_FONT = "LATIN";
    public static final double MAX_AMOUNT = 10000000d;
    public static final double MAX_AMOUNT_QTY = 10000d;
    public static final long LAST_SEND_DELAY = 600000; // 10 * 60 * 1000 : 10 minut delay

    public static final int PRINTER_CHAR_WIDTH = 60;

    public static final int REQUEST_CONNECT_DEVICE_SECURE = 1;

    //location service
    public static final int put_offline_interval = 20;
    public static final int MIN_TIME_BW_UPDATES = 5000;//5secund
    public static String last_printer_address = "";
    public static final int MESSAGE_DEVICE_NAME = 1;
    public static final int MESSAGE_TOAST = 2;
    public static final int MESSAGE_READ = 3;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    public static Context my_context;
    private static BluetoothAdapter mBluetoothAdapter;
    public static BluetoothPrintService mPrintService;
    public static String userlogo="";
    public static String company_name;
    public static String company_rd;
    public static String padaan_head;
    public  static String padaan_foot;
    public  static String printer_font;
    public  static int codePage;
    public  static int printer_port;
    public  static String printer_ip;
    public  static Boolean show_Logo;
    public static String appV  = "error";
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 10;
    public static final String PREF_IMEI = "device_imei";
    public static final String PREF_ANDROID = "device_android_id";



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
        String androidId = Settings.Secure.getString(myContext.getContentResolver(), Settings.Secure.ANDROID_ID);
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

    public static String getImei(Context myContext) {
        TelephonyManager mngr = (TelephonyManager) myContext.getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        assert mngr != null;
        return mngr.getDeviceId() == null ? "35451605332605" : mngr.getDeviceId();
    }


    public static void sendData(byte[] data) {
        try {
            // the text typed by the user
            mPrintService.write(data);
            // tell the user data were sent
        } catch (NullPointerException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("printer handler ", "msg : " + msg.toString());
            switch (msg.what) {
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    Toast.makeText(my_context, "BT Printer connected", Toast.LENGTH_SHORT).show();
//					MainActivity.togglePrinter.setChecked(true);
//					MainActivity.togglePrinter.setEnabled(true);
                    break;
                case 2:
                    Toast.makeText(my_context, "BT Printer NOT connect!", Toast.LENGTH_SHORT).show();
//					MainActivity.togglePrinter.setChecked(false);
//					MainActivity.togglePrinter.setEnabled(true);
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
                /*
                Intent enableBluetooth = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
                 */
            } else if ( mPrintService !=null && mPrintService.getState() == BluetoothPrintService.STATE_CONNECTED) {
                Log.d("bt printer","connected baina");
            } else {
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
//            serverIntent = new Intent(act, DeviceListActivity.class);
//            act.startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            Toast.makeText(act.getApplicationContext(), "Bluetooth printer not selected", Toast.LENGTH_LONG).show();
        } else {
            // Standard SerialPortService ID

            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(printer_address);
            // Attempt to connect to the device
            if (!last_printer_address.equals(printer_address)) {

                SharedPreferences.Editor editor = act.getSharedPreferences(
                        SAFCONSTANT.SHARED_PREF_NAME, act.MODE_PRIVATE).edit();
                editor.putString(SAFCONSTANT.PREF_PRINTER_ADDRESS, printer_address);
                editor.commit();

                last_printer_address = printer_address;
            }
            mPrintService.connect(device, true);
        }

    }
    public static void closeBT(){
        if (mPrintService != null && mPrintService.getState() == BluetoothPrintService.STATE_CONNECTED)
            mPrintService.stop();
    }
    public static boolean checkprinter(){
        if (mPrintService == null) return false;
        if (mBluetoothAdapter == null) return false;
        if (mPrintService.getState() == BluetoothPrintService.STATE_CONNECTED) return true;
        else return false;
    }
    public static void printBill(boolean copy) {
        Log.d("printer settings",printer_font + " , cp :" + codePage + " , logo:"+show_Logo);
        EscPosPrinter my_print = new EscPosPrinter(printer_font, codePage, show_Logo);
        if (userlogo.length() > 4) {
            File sd = Environment.getExternalStorageDirectory();
            File banner = new File(sd, userlogo);
            Bitmap largeIcon = null;
            if (banner.exists()) {
                largeIcon = BitmapFactory.decodeFile(banner.getAbsolutePath());
                my_print.set_align("CENTER");
                if (largeIcon !=null)
                    my_print.image(largeIcon, largeIcon.getWidth(), largeIcon.getHeight());
            }
        }
        if (padaan_head.length() > 2 ) {
            my_print.set_align("CENTER");
            my_print.text(padaan_head);
        }

        my_print.text(company_name);
        if (company_rd.length() > 2) my_print.text("РД : " + company_rd);

        my_print.set_align("LEFT");
        my_print.set_align("RIGHT");
        my_print.set_charType("B");
        if (padaan_foot.length() > 2 ) {
            my_print.set_align("CENTER");
            my_print.text(padaan_foot);
        }
        my_print.text("");
        my_print.text("--x--x--x--x----");
        my_print.text("");
        my_print.text("");
        my_print.text("");
        my_print.cut();
        SAFCONSTANT.sendData(my_print.prepare());
        my_print.clearData();

    }
    public static int printPhoto(String photoPath) {
        Log.d("printer settings",printer_font + " , cp :" + codePage + " , logo:"+photoPath);
        EscPosPrinter my_print = new EscPosPrinter(printer_font, codePage, show_Logo);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
        if ( bitmap.getWidth() > 400 ) return 10;
        else if ( bitmap.getHeight() > 800 ) return 11;
        my_print.image(bitmap, bitmap.getWidth(), bitmap.getHeight());
        my_print.text("");
        my_print.cut();
        SAFCONSTANT.sendData(my_print.prepare());
        my_print.clearData();
        return 1;
    }
}
