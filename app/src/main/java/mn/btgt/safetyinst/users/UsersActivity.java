package mn.btgt.safetyinst.users;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import mn.btgt.safetyinst.R;
import mn.btgt.safetyinst.history.HistoryActivity;
import mn.btgt.safetyinst.activity.SettingsActivity;
import mn.btgt.safetyinst.database.repo.SettingsRepo;
import mn.btgt.safetyinst.database.repo.UserRepo;
import mn.btgt.safetyinst.database.model.User;
import mn.btgt.safetyinst.utils.ConnectionDetector;
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

public class UsersActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    private SwipeRefreshLayout swipeRefreshLayout = null;
    private Handler mHandler;
    private UserRepo userRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        final RecyclerView mRecyclerView = findViewById(R.id.users_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = findViewById(R.id.swipeUserList);
        swipeRefreshLayout.setColorSchemeResources(R.color.bg_screen1, R.color.bg_screen2, R.color.bg_screen3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        final TextView compName = findViewById(R.id.company_name);

        userRepo = new UserRepo();
        mHandler = new Handler(Looper.getMainLooper());
        UserRepo userRepo = new UserRepo();
        final SettingsRepo settingsRepo = new SettingsRepo();

        final List<User> users = userRepo.selectAll();

        if (settingsRepo.select("company")!=null)
            compName.setText(settingsRepo.select("company"));

        RecyclerView.Adapter mAdapter = new UsersAdapter(this, users);
        mRecyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getUsers();
                        RecyclerView.Adapter mAdapter = new UsersAdapter(UsersActivity.this, users);
                        mRecyclerView.setAdapter(mAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        com.github.clans.fab.FloatingActionButton historyFab = findViewById(R.id.history_fab);
        historyFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UsersActivity.this, HistoryActivity.class));
            }
        });
        com.github.clans.fab.FloatingActionButton settingsFab = findViewById(R.id.settings_fab);
        settingsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UsersActivity.this, SettingsActivity.class));
            }
        });
    }

    public void getUsers() {

        if (!ConnectionDetector.isNetworkAvailable(UsersActivity.this)){
            Toast.makeText(UsersActivity.this, R.string.no_internet, Toast.LENGTH_LONG).show();
            return;
        }

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

                            int success = setting.getInt("success");
                            int error = setting.getInt("error");
                            if (success == 0 && error == -11) {
                                Toast.makeText(UsersActivity.this,
                                        R.string.imei_unlisted,
                                        Toast.LENGTH_SHORT).show();
                                return;
                            } else if (success== 0 && error == 900){
                                Toast.makeText(UsersActivity.this,
                                        getString(R.string.error) + error,
                                        Toast.LENGTH_SHORT).show();
                            } else if ( success == 1) {

                                if (users.length() > 0) {
                                    userRepo.deleteAll();

                                    for (int i = 0; i < users.length(); i++) {
                                        User user = new User();
                                        user.setId(users.getJSONObject(i).getString("id"));
                                        user.setName(users.getJSONObject(i).getString("name"));
                                        user.setPosition(users.getJSONObject(i).getString("job"));
                                        user.setPhone(1);
                                        user.setImei(SAFCONSTANT.getImei(UsersActivity.this));
                                        user.setEmail("");
                                        user.setPassword(users.getJSONObject(i).getString("pass"));
                                        user.setAvatar(users.getJSONObject(i).getString("photo"));
                                        user.setLastSigned("");
                                        userRepo.insert(user);
                                    }
                                } else {
                                    Toast.makeText(UsersActivity.this, R.string.empty_user, Toast.LENGTH_LONG)
                                            .show();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.click_to_again, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
