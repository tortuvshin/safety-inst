package mn.btgt.safetyinst.activity;

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

import mn.btgt.safetyinst.R;
import mn.btgt.safetyinst.database.repo.SNoteRepo;
import mn.btgt.safetyinst.database.repo.SettingsRepo;
import mn.btgt.safetyinst.database.repo.UserRepo;
import mn.btgt.safetyinst.database.model.SNote;
import mn.btgt.safetyinst.database.model.User;
import mn.btgt.safetyinst.database.model.Settings;
import mn.btgt.safetyinst.utils.ConnectionDetector;
import mn.btgt.safetyinst.utils.PrefManager;
import mn.btgt.safetyinst.utils.SAFCONSTANT;
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

    private Handler mHandler;
    private UserRepo userRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        reqPermissions();

        final long startTime = System.currentTimeMillis();

        userRepo = new UserRepo();

        mHandler = new Handler(Looper.getMainLooper());
        Handler handler = new Handler(Looper.getMainLooper());

        if (!ConnectionDetector.isNetworkAvailable(SplashActivity.this)){
            Toast.makeText(SplashActivity.this, R.string.no_internet, Toast.LENGTH_LONG).show();
            if (userRepo.count() > 1){
                openSomeActivity(LoginListActivity.class, true);
            } else {
                openSomeActivity(LoginImeiActivity.class, true);
            }
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    connectServer();
                    long diff = System.currentTimeMillis() - startTime;
                    Logger.d("Сэрвэрээс өгөгдөл татсан хугацаа: "+ Long.toString(diff) + " ms");
                }
            }, 100);
        }
    }

    /**
     * Вэбээс хэрэглэгч, зааварчилгаа, тохиргооны мэдээлэл зэргийг татаж локал баазруу хадгална
     */
    public void connectServer() {

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("time", String.valueOf(System.currentTimeMillis()))
                .addFormDataPart("imei", SAFCONSTANT.getImei(this))
                .addFormDataPart("AndroidId", SAFCONSTANT.getAndroiId(this))
                .build();

        Request request = new Request.Builder()
                .url(SAFCONSTANT.API_URL)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("app", SAFCONSTANT.APP_NAME)
                .addHeader("appV", SAFCONSTANT.getAppVersion(this))
                .addHeader("Imei", SAFCONSTANT.getImei(this))
                .addHeader("AndroidId", SAFCONSTANT.getAndroiId(this))
                .addHeader("nuuts", SAFCONSTANT.getSecretCode(SAFCONSTANT.getImei(this), String.valueOf(System.currentTimeMillis())))
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
                Logger.json(res);
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
                                        R.string.imei_unlisted,
                                        Toast.LENGTH_SHORT).show();
                                openSomeActivity(LoginImeiActivity.class, true);
                                return;
                            } else if (success== 0 && error == 900){
                                Toast.makeText(SplashActivity.this,
                                        getString(R.string.error) + error,
                                        Toast.LENGTH_SHORT).show();
                            } else if ( success == 1) {

                                Logger.json(setting.toString());

                                if (setting.length() > 0) {
                                    SettingsRepo settingsRepo = new SettingsRepo();

                                    List<Settings> settingsList = new ArrayList<>();
                                    settingsList.add(new Settings(SAFCONSTANT.SETTINGS_COMPANY, setting.getString("comp")));
                                    settingsList.add(new Settings(SAFCONSTANT.SETTINGS_DEPARTMENT, setting.getString("comp")));
                                    settingsList.add(new Settings(SAFCONSTANT.SETTINGS_IMEI, SAFCONSTANT.getImei(SplashActivity.this)));
                                    settingsList.add(new Settings(SAFCONSTANT.SETTINGS_ANDROID_ID, SAFCONSTANT.getAndroiId(SplashActivity.this)));
                                    settingsList.add(new Settings(SAFCONSTANT.SETTINGS_LOGO, setting.getString("company_logo")));
                                    settingsList.add(new Settings(SAFCONSTANT.SETTINGS_ISSIGNED, "no"));
                                    settingsList.add(new Settings(SAFCONSTANT.SETTINGS_PRINTER_FONT_SIZE, settingsRepo.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_SIZE) == null ? "11" : settingsRepo.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_SIZE)));
                                    settingsList.add(new Settings(SAFCONSTANT.SETTINGS_PRINTER_FONT_ENCODE, settingsRepo.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_ENCODE) == null ? "LATIN" : settingsRepo.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_ENCODE)));
                                    settingsRepo.insertList(settingsList);
                                } else {
                                    Toast.makeText(SplashActivity.this, R.string.empty_config, Toast.LENGTH_LONG)
                                            .show();
                                }

                                if (notes.length() > 0){
                                    SNoteRepo sNoteRepo = new SNoteRepo();

                                    sNoteRepo.deleteAll();

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
                                        sNoteRepo.insert(sNote);
                                    }

                                } else {
                                    Toast.makeText(SplashActivity.this, R.string.empty_note, Toast.LENGTH_LONG)
                                            .show();
                                }

                                if (users.length() > 0) {
                                    userRepo.deleteAll();

                                    for (int i = 0; i < users.length(); i++) {
                                        User user = new User();
                                        user.setId(users.getJSONObject(i).getString("id"));
                                        user.setName(users.getJSONObject(i).getString("name"));
                                        user.setPosition(users.getJSONObject(i).getString("job"));
                                        user.setPhone(1);
                                        user.setImei(SAFCONSTANT.getImei(SplashActivity.this));
                                        user.setEmail("");
                                        user.setPassword(users.getJSONObject(i).getString("pass"));
                                        user.setAvatar(users.getJSONObject(i).getString("photo"));
                                        user.setLastSigned("");
                                        userRepo.insert(user);
                                    }
                                } else {
                                    Toast.makeText(SplashActivity.this, R.string.empty_user, Toast.LENGTH_LONG)
                                            .show();
                                    if ( userRepo.count() == 0) {
                                        openSomeActivity(LoginImeiActivity.class, true);
                                        return;
                                    }
                                }

                                if (setting.getString("error").equals("0")) {

                                    if (users.length() > 1) {
                                        openSomeActivity(LoginListActivity.class, true);
                                    } else {
                                        openSomeActivity(LoginImeiActivity.class, true);
                                    }
                                } else {
                                    Toast.makeText(SplashActivity.this, R.string.error_server_connection, Toast.LENGTH_LONG).show();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SplashActivity.this, R.string.imei_unlisted, Toast.LENGTH_LONG).show();
                            openSomeActivity(LoginImeiActivity.class, true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void openSomeActivity(Class<?> otherActivityClass, boolean isFinish){
        startActivity(new Intent(getApplicationContext(), otherActivityClass));
        if (isFinish)
            finish();
    }

    public void reqPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(SplashActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
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
