package mn.btgt.safetyinst.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import mn.btgt.safetyinst.R;
import mn.btgt.safetyinst.database.repo.SignDataRepo;
import mn.btgt.safetyinst.database.model.SignData;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
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

public class NetworkChangeReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(final Context context, final Intent intent) {

        final Handler mHandler = new Handler(Looper.getMainLooper());
        SignDataRepo signDataRepo = new SignDataRepo();
        if (ConnectionDetector.isNetworkAvailable(context) && signDataRepo.count() > 0){
            final SignDataRepo signData = new SignDataRepo();
            List<SignData> sDataList = signData.selectAll();

            JSONArray sArray = new JSONArray();

            OkHttpClient client = new OkHttpClient();
            MultipartBody.Builder formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("time", String.valueOf(System.currentTimeMillis()))
                    .addFormDataPart("imei", SAFCONSTANT.getImei(context));

            try
            {
                for (SignData sData : sDataList)
                {
                    JSONObject sJSON = new JSONObject();
                    sJSON.put("id", sData.getId());
                    sJSON.put("user_id", sData.getUserId());
                    sJSON.put("note_id", sData.getsNoteId());
                    sJSON.put("user_name", sData.getUserName());
                    sJSON.put("note_name", sData.getsNoteName());
                    sJSON.put("signature_name", sData.getSignName());
                    sJSON.put("photo_name", sData.getPhotoName());
                    sJSON.put("view_date", sData.getViewDate());
                    sArray.put(sJSON);
                    formBody.addFormDataPart(sData.getSignName(), sData.getSignName(), RequestBody.create(MediaType.parse("image/*"), sData.getSignData()));
                    formBody.addFormDataPart(sData.getPhotoName(), sData.getPhotoName(), RequestBody.create(MediaType.parse("image/*"), sData.getPhoto()));
                }
                formBody.addFormDataPart("json_data", sArray.toString());
                Logger.d(sArray.toString());
            } catch (JSONException je) {
                je.printStackTrace();
            }

            MultipartBody requestBody = formBody.build();

            Request request = new Request.Builder()
                    .url(SAFCONSTANT.SEND_URL)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("app", SAFCONSTANT.APP_NAME)
                    .addHeader("appV", SAFCONSTANT.getAppVersion(context))
                    .addHeader("Imei", SAFCONSTANT.getImei(context))
                    .addHeader("AndroidId", SAFCONSTANT.getAndroiId(context))
                    .addHeader("nuuts", SAFCONSTANT.getSecretCode(SAFCONSTANT.getImei(context), String.valueOf(System.currentTimeMillis())))
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

                                if (resp.getString("success").equals("1"))
                                    signData.deleteAll();

                                Toast.makeText(context, R.string.send_info_success, Toast.LENGTH_SHORT).show();
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