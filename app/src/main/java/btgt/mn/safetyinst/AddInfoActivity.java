package btgt.mn.safetyinst;

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
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import btgt.mn.safetyinst.database.SignDataTable;
import btgt.mn.safetyinst.entity.SignData;
import btgt.mn.safetyinst.entity.User;
import btgt.mn.safetyinst.utils.ConnectionDetector;
import btgt.mn.safetyinst.utils.DbBitmap;
import btgt.mn.safetyinst.utils.SafConstants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddInfoActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private static final String TAG = AddInfoActivity.class.getSimpleName();

    Bitmap bm;

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    SignData userSigned;
    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;
    SignDataTable signDataTable;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        mHandler = new Handler(Looper.getMainLooper());
        userSigned = new SignData();
        signDataTable = new SignDataTable(this);
        final AppCompatButton saveBtn = (AppCompatButton) findViewById(R.id.save);
        AppCompatButton clearBtn = (AppCompatButton) findViewById(R.id.clear);
        final TextView textView = (TextView) findViewById(R.id.gestureTextView);
        final SignDataTable signDataTable = new SignDataTable(this);
        
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        jpegCallback = new PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
            FileOutputStream outStream = null;
            try {
                outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));

                outStream.write(data);
                outStream.close();
                userSigned.setPhoto(data);
            }

            catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            catch (IOException e) {
                e.printStackTrace();
            }

            finally {
            }
            }
        };

        final GestureOverlayView gestureView = (GestureOverlayView) findViewById(R.id.signaturePad);
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

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            try {
                    bm = Bitmap.createBitmap(gestureView.getDrawingCache());
                    captureImage(view);
                    userSigned.setsNoteId("1");
                    userSigned.setUserId("1");
                    userSigned.setViewDate(System.currentTimeMillis());
                    userSigned.setPhoto(DbBitmap.getBytes(bm));
                    userSigned.setUserSign(DbBitmap.getBytes(bm));
                    userSigned.setSendStatus("");

                    signDataTable.add(userSigned);

                    openDialog();

            } catch (Exception e) {
                Log.d("Gestures", e.getMessage());
                Toast.makeText(AddInfoActivity.this, "Алдаа гарлаа", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            }
        });
    }

    public void openDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialog);
        alertDialogBuilder.setTitle("Ажлын амжилт хүсэе");
        alertDialogBuilder.setMessage("Таны мэдээлэл хадгалагдлаа");
                alertDialogBuilder.setPositiveButton("OK",
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
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }
        catch (Exception e) {
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
            System.err.println(e);
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
            System.err.println(e);
            return;
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

    public void sendInfo() {
        if (!ConnectionDetector.isNetworkAvailable(this)){
            Toast.makeText(AddInfoActivity.this, "Интернетэд холбогдоогүй байна!!!", Toast.LENGTH_LONG).show();
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 85, baos);
        byte[] imageBytes = baos.toByteArray();

        String randomChunk = UUID.randomUUID().toString().substring(0, 8).replaceAll("-", "");
        String imageName = randomChunk.concat(".jpg");

        List<SignData> sDataList = signDataTable.getAll();

        JSONArray sArray = new JSONArray();
        try
        {
            for (SignData sData : sDataList)
            {
                JSONObject sJSON = new JSONObject();
                sJSON.put("id", sData.getId());
                sJSON.put("user_id", sData.getUserId());
                sJSON.put("note_id", sData.getsNoteId());
                sJSON.put("view_date", sData.getViewDate());
                sJSON.put("user_sign", sData.getUserSign());
                sJSON.put("user_photo", sData.getPhoto());

                sArray.put(sJSON);
            }
            Log.e(TAG, sArray.toString());
        } catch (JSONException je) {
            je.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("time", Calendar.getInstance().getTime().toString())
                .addFormDataPart("imei", SafConstants.getImei(this))
                .addFormDataPart("json_data", sArray.toString())
                .build();
        Log.e(TAG, sArray.toString());
        String uri = SafConstants.SendUrl;
        Log.e(TAG, uri + " ");

        Request request = new Request.Builder()
                .url(uri)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("app", SafConstants.APP_NAME)
                .addHeader("appV", SafConstants.getAppVersion(this))
                .addHeader("Imei", SafConstants.getImei(this))
                .addHeader("AndroidId", SafConstants.getAndroiId(this))
                .addHeader("nuuts", SafConstants.getSecretCode(SafConstants.getImei(this), System.currentTimeMillis()))
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
                            JSONObject resp = ob.getJSONObject(0);
                            if (resp.getString("success").equals("1"))
                                signDataTable.deleteAll();
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
