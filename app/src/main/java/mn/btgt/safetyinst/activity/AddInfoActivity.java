package mn.btgt.safetyinst.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import mn.btgt.safetyinst.R;
import mn.btgt.safetyinst.database.SettingsTable;
import mn.btgt.safetyinst.database.SignDataTable;
import mn.btgt.safetyinst.model.Settings;
import mn.btgt.safetyinst.model.SignData;
import mn.btgt.safetyinst.utils.CompressionUtils;
import mn.btgt.safetyinst.utils.ConnectionDetector;
import mn.btgt.safetyinst.utils.DbBitmap;
import mn.btgt.safetyinst.utils.EscPosPrinter;
import mn.btgt.safetyinst.utils.PrefManager;
import mn.btgt.safetyinst.utils.SAFCONSTANT;
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

public class AddInfoActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private static final String TAG = AddInfoActivity.class.getSimpleName();
    private static final String BROADCAST_ADDRESS_SAFE = "btgt_isafe_broadcast";
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;

    Bitmap bmSignature;
    byte[] btUserPhoto;

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    SignData userSigned;
    Camera.PictureCallback jpegCallback;

    private SharedPreferences sharedPrefs;
    SignDataTable signDataTable;
    SettingsTable settingsTable;

    private Handler mHandler;
    PrefManager prefManager;

    String signName, photoName;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mHandler = new Handler(Looper.getMainLooper());
        userSigned = new SignData();
        signDataTable = new SignDataTable(this);
        prefManager = new PrefManager(this);
        final AppCompatButton saveBtn = findViewById(R.id.save);
        AppCompatButton clearBtn = findViewById(R.id.clear);
        final TextView textView = findViewById(R.id.gestureTextView);

        sharedPrefs = getSharedPreferences(SAFCONSTANT.SHARED_PREF_NAME, MODE_PRIVATE);
        String last_printer_address = sharedPrefs.getString(SAFCONSTANT.PREF_PRINTER_ADDRESS, "");

        settingsTable = new SettingsTable(getApplicationContext());

        SAFCONSTANT.padaan_head = sharedPrefs.getString(SAFCONSTANT.PREF_HEAD, "");
        SAFCONSTANT.padaan_foot = sharedPrefs.getString(SAFCONSTANT.PREF_FOOT, "");
        SAFCONSTANT.last_printer_address = last_printer_address;

        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        jpegCallback = new PictureCallback() {

            @SuppressLint({"DefaultLocale", "SdCardPath"})
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream;
                try {
                    outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
                    btUserPhoto = CompressionUtils.compress(data);
                    outStream.write(btUserPhoto);
                    outStream.close();
                    Intent i = new Intent(BROADCAST_ADDRESS_SAFE).putExtra("VALUE",  "photo_done");
                    AddInfoActivity.this.sendBroadcast(i);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    Toast.makeText(AddInfoActivity.this, R.string.cannot_capture, Toast.LENGTH_SHORT).show();
                }
            }
        };

        final GestureOverlayView gestureView = findViewById(R.id.signaturePad);
        gestureView.setDrawingCacheEnabled(true);
        gestureView.addOnGestureListener(new GestureOverlayView.OnGestureListener() {
            @Override
            public void onGestureStarted(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
                textView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onGesture(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {

            }

            @Override
            public void onGestureEnded(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {

            }

            @Override
            public void onGestureCancelled(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {

            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshCamera();
                gestureView.clear(true);
            }
        });

        String singStr = UUID.randomUUID().toString().substring(0, 12).replaceAll("-", "").replaceAll("_", "");
        String photoStr = UUID.randomUUID().toString().substring(0, 12).replaceAll("-", "").replaceAll("_", "");
        signName = "hub_si_".concat(singStr).concat(".jpg");
        photoName = "hub_pn_".concat(photoStr).concat(".jpg");

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    bmSignature = Bitmap.createBitmap(gestureView.getDrawingCache());
                    camera.takePicture(null, null, jpegCallback);

                } catch (Exception e) {
                    Toast.makeText(AddInfoActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
                }

                if (settingsTable.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_ENCODE).length() > 0 && settingsTable.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_SIZE) != null) {
                    SAFCONSTANT.sendData(EscPosPrinter.getTestData80(
                            settingsTable.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_ENCODE),
                            Integer.valueOf(settingsTable.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_SIZE)),
                            AddInfoActivity.this ));
                }else{
                    Toast.makeText(AddInfoActivity.this, R.string.font_encode_error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(BROADCAST_ADDRESS_SAFE);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String value = intent.getStringExtra("VALUE");
                switch (value){
                    case "photo_done": //
                        savedata();
                        break;
                    case "photo_error": //
                        break;
                }
            }
        };
        this.registerReceiver(mReceiver, intentFilter);
    }
    private  void savedata(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.d("photo","size : "+btUserPhoto.length + " data :"+btUserPhoto.toString());
        userSigned.setsNoteId(prefManager.getSnoteId());
        userSigned.setUserId(prefManager.getUserId());
        userSigned.setViewDate(df.format(c.getTime()));
        userSigned.setUserName(prefManager.getUserName());
        userSigned.setsNoteName(prefManager.getSnoteName());
        userSigned.setPhotoName(photoName);
        userSigned.setPhoto(btUserPhoto);
        userSigned.setSignName(signName);
        userSigned.setSignData(DbBitmap.getBytes(bmSignature));
        userSigned.setSendStatus("0");
        signDataTable.create(userSigned);
        SettingsTable settingsTable = new SettingsTable(AddInfoActivity.this);
        settingsTable.insert(new Settings(SAFCONSTANT.SETTINGS_ISSIGNED, "yes"));
        openDialog();
    }

    public void openDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialog);
        alertDialogBuilder.setTitle(R.string.work_success);
        alertDialogBuilder.setMessage(R.string.has_been_saved);
        alertDialogBuilder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (ConnectionDetector.isNetworkAvailable(AddInfoActivity.this))
                            sendInfo();
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /*
    * Зааварчилгаатай танилцсан хэрэглэгчийн мэдээллийг сэрвэрлүү илгээх
    **/
    public void sendInfo() {
        if (!ConnectionDetector.isNetworkAvailable(this)){
            Toast.makeText(AddInfoActivity.this, R.string.no_internet, Toast.LENGTH_LONG).show();
            return;
        }

        List<SignData> sDataList = signDataTable.selectAll();

        JSONArray sArray = new JSONArray();

        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("time", String.valueOf(System.currentTimeMillis()))
                .addFormDataPart("imei", SAFCONSTANT.getImei(this));

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
                formBody.addFormDataPart(sData.getPhotoName(), sData.getPhotoName(), RequestBody.create(MediaType.parse("image/*"), CompressionUtils.decompress(sData.getPhoto())));
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
                .addHeader("appV", SAFCONSTANT.getAppVersion(this))
                .addHeader("Imei", SAFCONSTANT.getImei(this))
                .addHeader("AndroidId", SAFCONSTANT.getAndroiId(this))
                .addHeader("nuuts", SAFCONSTANT.getSecretCode(SAFCONSTANT.getImei(this), String.valueOf(System.currentTimeMillis())))
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
                                signDataTable.deleteAll();

                            Toast.makeText(AddInfoActivity.this, R.string.send_info_success, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Logger.e("ERROR : ", e.getMessage() + " ");
                        }
                    }
                });
            }
        });
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open(1);
            camera.setDisplayOrientation(90);
        }

        catch (RuntimeException e) {
            e.printStackTrace();
            return;
        }

        Camera.Parameters param;
        param = camera.getParameters();
        param.setPreviewSize(352, 288);
        camera.setParameters(param);

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
        }else if (resultCode == Activity.RESULT_OK){
            switch (requestCode) {
                case REQUEST_CONNECT_DEVICE_SECURE:
                    // When DeviceListActivity returns with a device to connect
                    String address = data.getExtras().getString("device_address");
                    Log.d("ACTIVRES", "BT onActivityResult address : " + address);
                    SAFCONSTANT.last_printer_address = address;
                    SAFCONSTANT.openBT(AddInfoActivity.this,address);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_print_on) {
            SAFCONSTANT.findBT(AddInfoActivity.this);
        } else if (id == R.id.action_print_off) {
            SAFCONSTANT.closeBT();
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(AddInfoActivity.this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}