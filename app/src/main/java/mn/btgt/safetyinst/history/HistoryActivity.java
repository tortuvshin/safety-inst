package mn.btgt.safetyinst.history;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mn.btgt.safetyinst.AppMain;
import mn.btgt.safetyinst.R;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import mn.btgt.safetyinst.database.model.SignData;
import mn.btgt.safetyinst.database.repo.SignDataRepo;
import mn.btgt.safetyinst.utils.ConnectionDetector;
import mn.btgt.safetyinst.utils.SAFCONSTANT;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HistoryActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout = null;
    private Handler sendHandler;
    private Handler mHandler;
    private SignDataRepo signDataRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Түүх");
        }

        sendHandler = new Handler(Looper.getMainLooper());
        signDataRepo = new SignDataRepo();

        final RecyclerView mRecyclerView = findViewById(R.id.history_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = findViewById(R.id.swipeHistory);
        swipeRefreshLayout.setColorSchemeResources(R.color.bg_screen1, R.color.bg_screen2, R.color.bg_screen3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mHandler = new Handler(Looper.getMainLooper());
        SignDataRepo signDataRepo = new SignDataRepo();
        final List<SignData> signData = signDataRepo.selectAll();

        RecyclerView.Adapter mAdapter = new HistoryAdapter(this, signData);
        mRecyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView.Adapter mAdapter = new HistoryAdapter(HistoryActivity.this, signData);
                        mRecyclerView.setAdapter(mAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.history_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.send_server:
                signSendServer();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    public void signSendServer(){
        if (ConnectionDetector.isNetworkAvailable(AppMain.getContext()) && signDataRepo.count() > 0){
            swipeRefreshLayout.setRefreshing(true);

            final SignDataRepo signData = new SignDataRepo();

            List<SignData> sDataList = signData.selectAll();

            JSONArray sArray = new JSONArray();

            OkHttpClient client = new OkHttpClient();
            MultipartBody.Builder formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("time", String.valueOf(System.currentTimeMillis()))
                    .addFormDataPart("imei", SAFCONSTANT.getImei(AppMain.getContext()));

            try
            {
                for (SignData sData : sDataList)
                {
                    if (sData.getSendStatus().equals("false")) {
                        JSONObject sJSON = new JSONObject();
                        sJSON.put("id", sData.getId());
                        sJSON.put("user_id", sData.getUserId());
                        sJSON.put("note_id", sData.getsNoteId());
                        sJSON.put("user_name", sData.getUserName());
                        sJSON.put("note_name", sData.getsNoteName());
                        sJSON.put("signature_name", sData.getSignName());
                        sJSON.put("photo_name", sData.getPhotoName());
                        sJSON.put("view_date", sData.getViewDate());
                        sJSON.put("send_status", sData.getSendStatus());
                        sArray.put(sJSON);
                        formBody.addFormDataPart(sData.getSignName(), sData.getSignName(), RequestBody.create(MediaType.parse("image/*"), sData.getSignData()));
                        formBody.addFormDataPart(sData.getPhotoName(), sData.getPhotoName(), RequestBody.create(MediaType.parse("image/*"), sData.getPhoto()));
                    }
                }
                formBody.addFormDataPart("json_data", sArray.toString());

                Logger.d(sArray.toString());
            } catch (JSONException je) {
                je.printStackTrace();
            }

            if (sArray.length() == 0){
                Toast.makeText(AppMain.getContext(), "Илгээх өгөгдөл байхгүй байна", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            MultipartBody requestBody = formBody.build();

            Request request = new Request.Builder()
                    .url(SAFCONSTANT.SEND_URL)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("app", SAFCONSTANT.APP_NAME)
                    .addHeader("appV", SAFCONSTANT.getAppVersion(AppMain.getContext()))
                    .addHeader("Imei", SAFCONSTANT.getImei(AppMain.getContext()))
                    .addHeader("AndroidId", SAFCONSTANT.getAndroiId(AppMain.getContext()))
                    .addHeader("nuuts", SAFCONSTANT.getSecretCode(SAFCONSTANT.getImei(AppMain.getContext()), String.valueOf(System.currentTimeMillis())))
                    .post(requestBody)
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
                                JSONObject resp = ob.getJSONObject(0);

                                if (resp.getString("success").equals("1")) {
                                    List<SignData> signDatas = signData.selectAll();
                                    for (SignData sData : signDatas)
                                    {
                                        SignData upSignData = new SignData();
                                        if (sData.getSendStatus().equals("false")) {
                                            upSignData.setId(sData.getId());
                                            upSignData.setsNoteId(sData.getsNoteId());
                                            upSignData.setUserId(sData.getUserId());
                                            upSignData.setViewDate(sData.getViewDate());
                                            upSignData.setUserName(sData.getUserName());
                                            upSignData.setsNoteName(sData.getsNoteName());
                                            upSignData.setPhotoName(sData.getsNoteId());
                                            upSignData.setPhoto(sData.getPhoto());
                                            upSignData.setSignName(sData.getSignName());
                                            upSignData.setSignData(sData.getSignData());
                                            upSignData.setSendStatus("true");
                                            signDataRepo.update(upSignData);
                                        }
                                    }
                                    Toast.makeText(AppMain.getContext(), R.string.send_info_success, Toast.LENGTH_SHORT).show();
                                }


                                Toast.makeText(AppMain.getContext(), R.string.send_info_success, Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout.setRefreshing(false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Logger.e("ERROR : ", e.getMessage() + " ");
                            }
                        }
                    });
                }
            });
        }
    }
}
