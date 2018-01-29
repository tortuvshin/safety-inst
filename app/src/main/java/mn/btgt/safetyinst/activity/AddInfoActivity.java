package mn.btgt.safetyinst.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import mn.btgt.safetyinst.utils.ConnectionDetector;
import mn.btgt.safetyinst.utils.DbBitmap;
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

    Bitmap bmSignature;
    byte[] btUserPhoto;

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    SignData userSigned;
    Camera.PictureCallback jpegCallback;
    SignDataTable signDataTable;

    private Handler mHandler;
    PrefManager prefManager;

    String signName, photoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mHandler = new Handler(Looper.getMainLooper());
        userSigned = new SignData();
        signDataTable = new SignDataTable(this);
        prefManager = new PrefManager(this);
        final AppCompatButton saveBtn = findViewById(R.id.save);
        AppCompatButton clearBtn = findViewById(R.id.clear);
        final TextView textView = findViewById(R.id.gestureTextView);
        final SignDataTable signDataTable = new SignDataTable(this);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        jpegCallback = new PictureCallback() {

            @SuppressLint({"DefaultLocale", "SdCardPath"})
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream;
                btUserPhoto = data;

                try {
                    outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));

                    outStream.write(data);
                    outStream.close();
                }

                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                catch (IOException e) {
                    e.printStackTrace();
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

        Camera.FaceDetectionListener faceDetectionListener
                = new Camera.FaceDetectionListener(){

            @Override
            public void onFaceDetection(Camera.Face[] faces, Camera camera) {

            }
        };

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
                    Calendar c = Calendar.getInstance();

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    bmSignature = Bitmap.createBitmap(gestureView.getDrawingCache());
                    captureImage(view);
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

                } catch (Exception e) {
                    Logger.d(e);
                    Toast.makeText(AddInfoActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    public void captureImage(View v) throws IOException {
        camera.takePicture(null, null, jpegCallback);
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
                formBody.addFormDataPart(sData.getPhotoName(), sData.getPhotoName(), RequestBody.create(MediaType.parse("image/*"), btUserPhoto));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
}