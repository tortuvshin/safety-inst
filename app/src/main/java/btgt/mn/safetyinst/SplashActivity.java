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
import org.json.JSONObject;

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
    String imei;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imei = SafConstants.getImei(this);

        prefManager = new PrefManager(this);

        mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    connectServer();

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
                .addHeader("appV", SafConstants.getAppVersion(this))
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
                            JSONObject setting = ob.getJSONObject(0);
                            JSONArray users = setting.getJSONArray("users");
                            JSONArray notes = setting.getJSONArray("notes");

                            Log.e(TAG, ob.toString());

                            UserTable userTable = new UserTable(SplashActivity.this);
                            SettingsTable settingsTable = new SettingsTable(SplashActivity.this);
                            SNoteTable sNoteTable = new SNoteTable(SplashActivity.this);
                            settingsTable.add(new Settings(
                                    setting.getString("comp"),
                                    "",
                                    SafConstants.getImei(SplashActivity.this),
                                    SafConstants.getAndroiId(SplashActivity.this),
                                    setting.getString("company_logo")
                                    ));
                            byte[] image = new byte[0];
                            for (int i = 0; i < users.length(); i++) {
                                userTable.add(new User(
                                        users.getJSONObject(i).getString("id"),
                                        users.getJSONObject(i).getString("name"),
                                        users.getJSONObject(i).getString("job"),
                                        users.getJSONObject(i).getInt("id"),
                                        SafConstants.getImei(SplashActivity.this),
                                        "",
                                        users.getJSONObject(i).getString("pass"),
                                        image,
                                        ""
                                ));
                            }

                            for (int i = 0; i < notes.length(); i++) {
                                sNoteTable.add(new SNote(
                                        notes.getJSONObject(i).getString("id"),
                                        notes.getJSONObject(i).getString("id"),
                                        notes.getJSONObject(i).getString("photo"),
                                        notes.getJSONObject(i).getString("id"),
                                        notes.getJSONObject(i).getInt("dur"),
                                        notes.getJSONObject(i).getString("info"),
                                        notes.getJSONObject(i).getString("id"),
                                        notes.getJSONObject(i).getInt("dur")
                                        ));
                            }

                            if (setting.getString("error").equals("0")) {
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
