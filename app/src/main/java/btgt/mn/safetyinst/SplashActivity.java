package btgt.mn.safetyinst;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;

import btgt.mn.safetyinst.database.CategoryTable;
import btgt.mn.safetyinst.database.SNoteTable;
import btgt.mn.safetyinst.database.SettingsTable;
import btgt.mn.safetyinst.database.UserTable;
import btgt.mn.safetyinst.entity.Category;
import btgt.mn.safetyinst.entity.SNote;
import btgt.mn.safetyinst.entity.Settings;
import btgt.mn.safetyinst.entity.User;
import btgt.mn.safetyinst.utils.DbBitmap;
import btgt.mn.safetyinst.utils.PrefManager;
import btgt.mn.safetyinst.utils.SafConstants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private Handler mHandler;
    PrefManager prefManager;
    SettingsTable settingsTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        prefManager = new PrefManager(this);

        mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    connectServer();

                    if (prefManager.isFirstTimeLaunch()) {
                        TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        if (ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }

                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

                        UserTable userTable = new UserTable(SplashActivity.this);
                        userTable.add(new User("1", "Цогтгэрэл", "Програм хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", DbBitmap.getBytes(bm), Calendar.getInstance().getTime().toString()));
                        userTable.add(new User("2", "Ганцоож", "Вэб дизайнер", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", DbBitmap.getBytes(bm), Calendar.getInstance().getTime().toString()));
                        userTable.add(new User("3", "Бат-Эрдэнэ", "Мобайл апп хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", DbBitmap.getBytes(bm), Calendar.getInstance().getTime().toString()));
                        userTable.add(new User("4", "Ганцоож", "Програм хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", DbBitmap.getBytes(bm), Calendar.getInstance().getTime().toString()));
                        userTable.add(new User("5", "Төртүвшин", "Вэб хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", DbBitmap.getBytes(bm), Calendar.getInstance().getTime().toString()));
                        userTable.add(new User("6", "Энхбаяр", "Вэб хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", DbBitmap.getBytes(bm), Calendar.getInstance().getTime().toString()));
                        userTable.add(new User("7", "Цэнд-Аюуш", "Вэб хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", DbBitmap.getBytes(bm), Calendar.getInstance().getTime().toString()));
                        userTable.add(new User("8", "Анхаа", "Вэб хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", DbBitmap.getBytes(bm), Calendar.getInstance().getTime().toString()));
                        userTable.add(new User("9", "Цэнгүүн", "Вэб хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", DbBitmap.getBytes(bm), Calendar.getInstance().getTime().toString()));
                        userTable.add(new User("10", "Тэргүүн", "Вэб хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", DbBitmap.getBytes(bm), Calendar.getInstance().getTime().toString()));
                        SettingsTable settingsTable = new SettingsTable(SplashActivity.this);

                        settingsTable.add(new Settings("BTGT LLC", "Software Development", mngr.getDeviceId(), mngr.getDeviceSoftwareVersion(), "1"));

                        CategoryTable categoryTable = new CategoryTable(SplashActivity.this);
                        categoryTable.add(new Category("1", "Tech", "", "1"));

                        SNoteTable sNoteTable = new SNoteTable(SplashActivity.this);
                        sNoteTable.add(new SNote("1", "1", "Заавар", "1", 1, "Заавар дэлгэрэнгүй", "", 1));
                        sNoteTable.add(new SNote("2", "1", "Заавар1", "1", 1, "Заавар дэлгэрэнгүй", "", 1));
                        sNoteTable.add(new SNote("3", "1", "Заавар2", "1", 1, "Заавар дэлгэрэнгүй", "", 1));
                        sNoteTable.add(new SNote("4", "1", "Заавар3", "1", 1, "Заавар дэлгэрэнгүй", "", 1));
                        sNoteTable.add(new SNote("5", "1", "Заавар4", "1", 1, "Заавар дэлгэрэнгүй", "", 1));
                        sNoteTable.add(new SNote("6", "1", "Заавар", "1", 1, "Заавар дэлгэрэнгүй", "", 1));
                        prefManager.setFirstTimeLaunch(false);
                    }

                    if (prefManager.isLoggedIn()) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception ex) {

                }
            }
        });
    }

    public void connectServer() {

        settingsTable = new SettingsTable(SplashActivity.this);

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("time", Calendar.getInstance().getTime().toString())
                .addFormDataPart("imei", SafConstants.getImei(this))
                .build();

        String uri = SafConstants.WebURL;
        Log.e(TAG, uri + " ");

        Request request = new Request.Builder()
                .url(uri)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("app", SafConstants.APP_NAME)
                .addHeader("appV", SafConstants.myAppVersion(this))
                .addHeader("Imei", SafConstants.getImei(this))
                .addHeader("AndroidId", SafConstants.getAndroiId(this))
                .addHeader("nuuts", SafConstants.getSecretCode(SafConstants.getImei(this), Calendar.getInstance().getTime().toString()))
                .post(formBody)
                .build();

        Log.e(TAG, request.toString());
        Log.e(TAG, "Headers : "+request.headers().toString());

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Server connection failed : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();

                Log.e(TAG, res);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray ob = new JSONArray(String.valueOf(res));
                            Log.e(TAG, ob.toString());
                            String comp = "";
                            for (int i = 0; i < ob.length(); i++) {
                                comp = ob.getJSONObject(i).getString("comp");
                            }
                            Toast.makeText(SplashActivity.this, comp, Toast.LENGTH_LONG)
                                    .show();
                            if (comp == "1") {
                                Toast.makeText(SplashActivity.this, "Амжилттай холбогдсон", Toast.LENGTH_LONG)
                                        .show();
                            } else {
                                Toast.makeText(SplashActivity.this, "Алдаа гарлаа", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ERROR : ", e.getMessage() + " ");
                        }
                    }
                });
            }
        });
    }
}
