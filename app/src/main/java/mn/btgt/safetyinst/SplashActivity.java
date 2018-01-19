package mn.btgt.safetyinst;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import agency.techstar.imageloader.ImageLoader;
import mn.btgt.safetyinst.database.SNoteTable;
import mn.btgt.safetyinst.database.SettingsTable;
import mn.btgt.safetyinst.database.UserTable;
import mn.btgt.safetyinst.entity.SNote;
import mn.btgt.safetyinst.entity.Settings;
import mn.btgt.safetyinst.entity.User;
import mn.btgt.safetyinst.utils.ConnectionDetector;
import mn.btgt.safetyinst.utils.PrefManager;
import mn.btgt.safetyinst.utils.SafConstants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private Handler mHandler;
    private Handler handler;

    PrefManager prefManager;
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        reqPermissions();

        final long startTime = System.currentTimeMillis();

        prefManager = new PrefManager(this);
        imageLoader = new ImageLoader(this);

        mHandler = new Handler(Looper.getMainLooper());
        handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                if (!ConnectionDetector.isNetworkAvailable(SplashActivity.this)){
                    Toast.makeText(SplashActivity.this, "Интернетэд холбогдоогүй байна!!!", Toast.LENGTH_LONG).show();
                    openMain();
                } else {

                    connectServer();
                    long diff = System.currentTimeMillis() - startTime;

                    Logger.d("Сэрвэрээс өгөгдөл татсан хугацаа: "+ Long.toString(diff) + " ms");
                }
            }
        });
    }

    public void connectServer() {

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("time", String.valueOf(System.currentTimeMillis()))
                .addFormDataPart("imei", SafConstants.getImei(this))
                .build();

        Request request = new Request.Builder()
                .url(SafConstants.API_URL)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("app", SafConstants.APP_NAME)
                .addHeader("appV", SafConstants.getAppVersion(this))
                .addHeader("Imei", SafConstants.getImei(this))
                .addHeader("AndroidId", SafConstants.getAndroiId(this))
                .addHeader("nuuts", SafConstants.getSecretCode(SafConstants.getImei(this), String.valueOf(System.currentTimeMillis())))
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Logger.e("Server connection failed : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONArray ob = new JSONArray(String.valueOf(res));
                            if (ob.length() < 1)
                                return;
                            JSONObject setting = ob.getJSONObject(0);
                            JSONArray users = setting.getJSONArray("users");
                            JSONArray notes = setting.getJSONArray("notes");

                            int success = setting.getInt("success");
                            int error = setting.getInt("error");
                            if (success == 0 && error == -11) {
                                Toast.makeText(SplashActivity.this,
                                        "Таны Imei бүртгэлгүй байна",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SplashActivity.this, LoginImeiActivity.class));
                                finish();
                                return;
                            } else if (success== 0 && error == 900){
                                Toast.makeText(SplashActivity.this,
                                        "Алдаа: " + error,
                                        Toast.LENGTH_SHORT).show();
                            } else if ( success == 1) {

                                Logger.json(setting.toString());

                                if (setting.length() > 0) {
                                    SettingsTable settingsTable = new SettingsTable(SplashActivity.this);

                                    settingsTable.deleteAll();
                                    List<Settings> settingsList = new ArrayList<Settings>();
                                    settingsList.add(new Settings(SafConstants.SETTINGS_COMPANY, setting.getString("comp")));
                                    settingsList.add(new Settings(SafConstants.SETTINGS_DEPARTMENT, setting.getString("comp")));
                                    settingsList.add(new Settings(SafConstants.SETTINGS_IMEI, SafConstants.getImei(SplashActivity.this)));
                                    settingsList.add(new Settings(SafConstants.SETTINGS_ANDROID_ID, SafConstants.getAndroiId(SplashActivity.this)));
                                    settingsList.add(new Settings(SafConstants.SETTINGS_LOGO, setting.getString("company_logo")));
                                    settingsList.add(new Settings(SafConstants.SETTINGS_ISSIGNED, "no"));

                                    settingsTable.insertList(settingsList);
                                } else {
                                    Toast.makeText(SplashActivity.this, "Тохиргооны мэдээлэл хоосон байна", Toast.LENGTH_LONG)
                                            .show();
                                }

                                if (users.length() > 0) {
                                    UserTable userTable = new UserTable(SplashActivity.this);
                                    userTable.deleteAll();

                                    for (int i = 0; i < users.length(); i++) {
                                        User user = new User();
                                        user.setId(users.getJSONObject(i).getString("id"));
                                        user.setName(users.getJSONObject(i).getString("name"));
                                        user.setPosition(users.getJSONObject(i).getString("job"));
                                        user.setPhone(1);
                                        user.setImei(SafConstants.getImei(SplashActivity.this));
                                        user.setEmail("");
                                        user.setPassword("1234");
                                        user.setAvatar(users.getJSONObject(i).getString("photo"));
                                        user.setLastSigned("");
                                        userTable.add(user);
                                    }
                                } else {
                                    Toast.makeText(SplashActivity.this, "Хэрэглэгчийн мэдээлэл хоосон", Toast.LENGTH_LONG)
                                            .show();
                                }

                                if (notes.length() > 0){
                                    SNoteTable sNoteTable = new SNoteTable(SplashActivity.this);

                                    sNoteTable.deleteAll();

                                    for (int i = 0; i < notes.length(); i++) {
                                        SNote sNote = new SNote();
                                        sNote.setId(notes.getJSONObject(i).getString("id"));
                                        sNote.setCategoryId(notes.getJSONObject(i).getString("category_id"));
                                        sNote.setName(notes.getJSONObject(i).getString("name"));
                                        sNote.setOrder(notes.getJSONObject(i).getString("orderx"));
                                        sNote.setFrameType(notes.getJSONObject(i).getInt("frame_type"));
                                        sNote.setFrameData(notes.getJSONObject(i).getString("frame_data"));
                                        sNote.setVoiceData("");
                                        sNote.setTimeout(notes.getJSONObject(i).getInt("timeout"));
                                        sNoteTable.add(sNote);
                                    }

                                } else {
                                    Toast.makeText(SplashActivity.this, "Зааварчилгаа хоосон байна", Toast.LENGTH_LONG)
                                            .show();
                                }

                                if (setting.getString("error").equals("0")) {
//                                    openMain();
                                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                } else {
                                    Toast.makeText(SplashActivity.this, "Сэрвэртэй холбогдоход алдаа гарлаа", Toast.LENGTH_LONG).show();
                                }
                            }

                        } catch (JSONException e) {
                            Logger.e(e.getMessage());
                            Toast.makeText(SplashActivity.this, "Таны Imei бүртгэлгүй байна", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SplashActivity.this, LoginImeiActivity.class));
                            finish();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void openMain () {
        Intent iGet = getIntent();
        String username = iGet.getStringExtra("username");

        // Mercury-гээс хаб нээсэн бол username intent ирсэн эсэх
        if (username == null) {
            Intent intent = new Intent(SplashActivity.this, LoginListActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, LoginImeiActivity.class);
            intent.putExtra("username", iGet.getStringExtra("username"));
            intent.putExtra("password", iGet.getStringExtra("password"));
            startActivity(intent);
            finish();
        }
    }

    public void reqPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(SplashActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_PHONE_STATE,

                }, 0);
            }
        }
    }
}
