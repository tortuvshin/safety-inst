package mn.btgt.safetyinst.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import mn.btgt.safetyinst.R;
import mn.btgt.safetyinst.adapter.ImagePreviewAdapter;
import mn.btgt.safetyinst.database.model.FaceResult;
import mn.btgt.safetyinst.database.model.Settings;
import mn.btgt.safetyinst.database.model.SignData;
import mn.btgt.safetyinst.database.repo.SettingsRepo;
import mn.btgt.safetyinst.database.repo.SignDataRepo;
import mn.btgt.safetyinst.utils.CameraErrorCallback;
import mn.btgt.safetyinst.utils.ConnectionDetector;
import mn.btgt.safetyinst.utils.DbBitmap;
import mn.btgt.safetyinst.utils.EscPosPrinter;
import mn.btgt.safetyinst.utils.ImageUtils;
import mn.btgt.safetyinst.utils.PrefManager;
import mn.btgt.safetyinst.utils.SAFCONSTANT;
import mn.btgt.safetyinst.utils.Util;
import mn.btgt.safetyinst.views.FaceOverlayView;
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

public final class FaceDetectActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private int numberOfCameras; // Төхөөрөмжийн камерийн тоо

    public static final String TAG = FaceDetectActivity.class.getSimpleName();
    private static final String BROADCAST_ADDRESS_SAFE = "btgt_isafe_broadcast";

    Bitmap bmSignature; // гарын үсэг
    byte[] btUserPhoto; // зураг

    private Camera mCamera;
    private int cameraId = 0;

    // Дэлгэцийн эргэлт болон босоо хэвтээ зэргийг хянах .
    private int mDisplayRotation;
    private int mDisplayOrientation;

    private int previewWidth;
    private int previewHeight;

    private SurfaceView mView; // Камерийн өгөгдлийн харагдац

    private FaceOverlayView mFaceView;

    private final CameraErrorCallback mErrorCallback = new CameraErrorCallback();

    private static final int MAX_FACE = 10;
    private boolean isThreadWorking = false;
    private Handler handler;
    private FaceDetectThread detectThread = null;
    private int prevSettingWidth;
    private int prevSettingHeight;
    private android.media.FaceDetector fdet;

    private FaceResult faces[];
    private FaceResult faces_previous[];
    private int Id = 0;

    private String BUNDLE_CAMERA_ID = "camera";

    private HashMap<Integer, Integer> facesCount = new HashMap<>();
    private RecyclerView recyclerView;
    private ImagePreviewAdapter imagePreviewAdapter;
    private ArrayList<Bitmap> facesBitmap;

    SignData userSigned;

    private SharedPreferences sharedPrefs;
    SignDataRepo signDataRepo;
    SettingsRepo settingsRepo;

    private Handler mHandler;
    PrefManager prefManager;

    String signName, photoName;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    /**
     * UI болон нүүрний таних үйл явцыг эхлүүлж байна.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.activity_face_detect);

        mView = (SurfaceView) findViewById(R.id.surfaceview);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mFaceView = new FaceOverlayView(this);
        addContentView(mFaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        handler = new Handler();
        faces = new FaceResult[MAX_FACE];
        faces_previous = new FaceResult[MAX_FACE];
        for (int i = 0; i < MAX_FACE; i++) {
            faces[i] = new FaceResult();
            faces_previous[i] = new FaceResult();
        }

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Хувийн мэдээлэл");
        }

        if (icicle != null)
            cameraId = icicle.getInt(BUNDLE_CAMERA_ID, 1);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        mHandler = new Handler(Looper.getMainLooper());
        userSigned = new SignData();
        signDataRepo = new SignDataRepo();
        prefManager = new PrefManager(this);
        final AppCompatButton saveBtn = findViewById(R.id.save);
        AppCompatButton clearBtn = findViewById(R.id.clear);
        final TextView textView = findViewById(R.id.gestureTextView);

        sharedPrefs = getSharedPreferences(SAFCONSTANT.SHARED_PREF_NAME, MODE_PRIVATE);
        String last_printer_address = sharedPrefs.getString(SAFCONSTANT.PREF_PRINTER_ADDRESS, "");

        settingsRepo = new SettingsRepo();

        SAFCONSTANT.last_printer_address = last_printer_address;


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
                    saveSignData();
                } catch (Exception e) {
                    Toast.makeText(FaceDetectActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
                }

                if (settingsRepo.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_ENCODE).length() > 0 && settingsRepo.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_SIZE) != null) {
                    printBill();

                } else{
                    Toast.makeText(FaceDetectActivity.this, R.string.font_encode_error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private  void saveSignData(){
        Log.d("photo","size : "+btUserPhoto.length + " data :"+btUserPhoto.toString());
        userSigned.setsNoteId(prefManager.getSnoteId());
        userSigned.setUserId(prefManager.getUserId());
        userSigned.setViewDate(dateFormat.format(calendar.getTime()));
        userSigned.setUserName(prefManager.getUserName());
        userSigned.setsNoteName(prefManager.getSnoteName());
        userSigned.setPhotoName(photoName);
        userSigned.setPhoto(btUserPhoto);
        userSigned.setSignName(signName);
        userSigned.setSignData(DbBitmap.getBytes(bmSignature));
        userSigned.setSendStatus("0");
        signDataRepo.insert(userSigned);
        settingsRepo.insert(new Settings(SAFCONSTANT.SETTINGS_ISSIGNED, "yes"));
        openDialog();
    }

    public void openDialog(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this, R.style.AlertDialog);
        alertDialogBuilder.setTitle(R.string.work_success);
        alertDialogBuilder.setMessage(R.string.has_been_saved);
        alertDialogBuilder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (!ConnectionDetector.isNetworkAvailable(FaceDetectActivity.this)){
                            Toast.makeText(FaceDetectActivity.this, R.string.no_internet, Toast.LENGTH_LONG).show();
                        } else {
                            sendInfo();
                        }
                        finish();
                    }
                });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void printBill() {

        EscPosPrinter bill = new EscPosPrinter(settingsRepo.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_ENCODE), Integer.valueOf(settingsRepo.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_SIZE)), 1);
        Bitmap newLogo = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.logo), 120, 120, false);
        bill.set_align("CENTER");
        bill.image(newLogo, newLogo.getWidth(), newLogo.getHeight());
        bill.set_charType("B");
        bill.text(settingsRepo.select(SAFCONSTANT.SETTINGS_COMPANY));
        bill.text("");
        bill.text("Зааварчилгаатай");
        bill.text("танилцсан баримт");
        bill.text("--------------------------");
        bill.set_charType("NORMAL");
        bill.set_align("LEFT");
        bill.set_charType("B");
        bill.text(prefManager.getSnoteName());
        bill.set_charType("NORMAL");
        bill.text("Салбар хэлтэс: ".concat(settingsRepo.select(SAFCONSTANT.SETTINGS_DEPARTMENT)));
        bill.text("Ажилтан: ".concat(prefManager.getUserName()));
        bill.text("Огноо: ".concat(dateFormat.format(calendar.getTime())));
        bill.text("Гарын үсэг: ");
        bill.set_align("RIGHT");
        Bitmap newSignature = Bitmap.createScaledBitmap(bmSignature, 200, 140, true);
        bill.image(newSignature, newSignature.getWidth(), newSignature.getHeight());
        bill.set_align("CENTER");
        bill.qrcode(prefManager.getUserName(),200,200);
        bill.set_charType("I");
        bill.text("Ажлын амжилт хүсэе.");
        bill.text("");
        bill.text("");
        bill.text("");
        bill.text("");
        bill.cut();
        SAFCONSTANT.sendData(bill.prepare());
        bill.clearData();
    }

    /*
    * Зааварчилгаатай танилцсан хэрэглэгчийн мэдээллийг сэрвэрлүү илгээх
    **/
    public void sendInfo() {

        SignDataRepo signData = new SignDataRepo();
        List<SignData> sDataList = signData.selectAll();

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
                                signDataRepo.deleteAll();

                            Toast.makeText(FaceDetectActivity.this, R.string.send_info_success, Toast.LENGTH_SHORT).show();
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        SurfaceHolder holder = mView.getHolder();
        holder.addCallback(this);
        holder.setFormat(ImageFormat.NV21);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_camera, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            case R.id.switchCam:

                if (numberOfCameras == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Switch Camera").setMessage("Your device have one camera").setNeutralButton("Close", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                    return true;
                }

                cameraId = (cameraId + 1) % numberOfCameras;
                recreate();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Камерийг дахин ачааллах.
     */
    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG, "onResume");
        startPreview();
    }

    /**
     * Камер зогсоох
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detector, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetData();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_CAMERA_ID, cameraId);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        resetData();

        //Find the total number of cameras available
        numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                if (cameraId == 0) cameraId = i;
            }
        }

        mCamera = Camera.open(cameraId);

        Camera.getCameraInfo(cameraId, cameraInfo);
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mFaceView.setFront(true);
        }

        try {
            mCamera.setPreviewDisplay(mView.getHolder());
        } catch (Exception e) {
            Log.e(TAG, "Could not preview the image.", e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        // We have no surface, return immediately:
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        // Try to stop the current preview:
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // Ignore...
        }

        configureCamera(width, height);
        setDisplayOrientation();
        setErrorCallback();

        // Create media.FaceDetector
        float aspect = (float) previewHeight / (float) previewWidth;
        fdet = new android.media.FaceDetector(prevSettingWidth, (int) (prevSettingWidth * aspect), MAX_FACE);


        // Everything is configured! Finally start the camera preview again:
        startPreview();
    }

    private void setErrorCallback() {
        mCamera.setErrorCallback(mErrorCallback);
    }

    private void setDisplayOrientation() {
        // Now set the display orientation:
        mDisplayRotation = Util.getDisplayRotation(FaceDetectActivity.this);
        mDisplayOrientation = Util.getDisplayOrientation(mDisplayRotation, cameraId);

        mCamera.setDisplayOrientation(mDisplayOrientation);

        if (mFaceView != null) {
            mFaceView.setDisplayOrientation(mDisplayOrientation);
        }
    }

    private void configureCamera(int width, int height) {
        Camera.Parameters parameters = mCamera.getParameters();
        // Set the PreviewSize and AutoFocus:
        setOptimalPreviewSize(parameters, width, height);
        setAutoFocus(parameters);
        // And set the parameters:
        mCamera.setParameters(parameters);
    }

    private void setOptimalPreviewSize(Camera.Parameters cameraParameters, int width, int height) {
        List<Camera.Size> previewSizes = cameraParameters.getSupportedPreviewSizes();
        float targetRatio = (float) width / height;
        Camera.Size previewSize = Util.getOptimalPreviewSize(this, previewSizes, targetRatio);
        previewWidth = previewSize.width;
        previewHeight = previewSize.height;

        Log.e(TAG, "previewWidth" + previewWidth);
        Log.e(TAG, "previewHeight" + previewHeight);

        /**
         * Calculate size to scale full frame bitmap to smaller bitmap
         * Detect face in scaled bitmap have high performance than full bitmap.
         * The smaller image size -> detect faster, but distance to detect face shorter,
         * so calculate the size follow your purpose
         */
        if (previewWidth / 4 > 360) {
            prevSettingWidth = 360;
            prevSettingHeight = 270;
        } else if (previewWidth / 4 > 320) {
            prevSettingWidth = 320;
            prevSettingHeight = 240;
        } else if (previewWidth / 4 > 240) {
            prevSettingWidth = 240;
            prevSettingHeight = 160;
        } else {
            prevSettingWidth = 160;
            prevSettingHeight = 120;
        }

        cameraParameters.setPreviewSize(previewSize.width, previewSize.height);

        mFaceView.setPreviewWidth(previewWidth);
        mFaceView.setPreviewHeight(previewHeight);
    }

    private void setAutoFocus(Camera.Parameters cameraParameters) {
        List<String> focusModes = cameraParameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
            cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    }

    private void startPreview() {
        if (mCamera != null) {
            isThreadWorking = false;
            mCamera.startPreview();
            mCamera.setPreviewCallback(this);
            counter = 0;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.setPreviewCallbackWithBuffer(null);
        mCamera.setErrorCallback(null);
        mCamera.release();
        mCamera = null;
    }


    @Override
    public void onPreviewFrame(byte[] _data, Camera _camera) {
        if (!isThreadWorking) {
            if (counter == 0)
                start = System.currentTimeMillis();

            isThreadWorking = true;
            waitForFdetThreadComplete();
            detectThread = new FaceDetectThread(handler, this);
            detectThread.setData(_data);
            detectThread.start();
        }
    }

    private void waitForFdetThreadComplete() {
        if (detectThread == null) {
            return;
        }

        if (detectThread.isAlive()) {
            try {
                detectThread.join();
                detectThread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    // fps detect face (not FPS of camera)
    long start, end;
    int counter = 0;
    double fps;

    /**
     * Нүүр таних
     */
    private class FaceDetectThread extends Thread {
        private Handler handler;
        private byte[] data = null;
        private Context ctx;
        private Bitmap faceCroped;

        public FaceDetectThread(Handler handler, Context ctx) {
            this.ctx = ctx;
            this.handler = handler;
        }


        public void setData(byte[] data) {
            this.data = data;
        }

        public void run() {
//            Log.i("FaceDetectThread", "running");

            float aspect = (float) previewHeight / (float) previewWidth;
            int w = prevSettingWidth;
            int h = (int) (prevSettingWidth * aspect);

            Bitmap bitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.RGB_565);
            // face detection: first convert the image from NV21 to RGB_565
            YuvImage yuv = new YuvImage(data, ImageFormat.NV21,
                    bitmap.getWidth(), bitmap.getHeight(), null);
            // TODO: make rect a member and use it for width and height values above
            Rect rectImage = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            // TODO: use a threaded option or a circular buffer for converting streams?
            //see http://ostermiller.org/convert_java_outputstream_inputstream.html
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            if (!yuv.compressToJpeg(rectImage, 100, baout)) {
                Log.e("CreateBitmap", "compressToJpeg failed");
            }

            BitmapFactory.Options bfo = new BitmapFactory.Options();
            bfo.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeStream(
                    new ByteArrayInputStream(baout.toByteArray()), null, bfo);

            Bitmap bmp = Bitmap.createScaledBitmap(bitmap, w, h, false);

            float xScale = (float) previewWidth / (float) prevSettingWidth;
            float yScale = (float) previewHeight / (float) h;

            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            int rotate = mDisplayOrientation;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT && mDisplayRotation % 180 == 0) {
                if (rotate + 180 > 360) {
                    rotate = rotate - 180;
                } else
                    rotate = rotate + 180;
            }

            switch (rotate) {
                case 90:
                    bmp = ImageUtils.rotate(bmp, 90);
                    xScale = (float) previewHeight / bmp.getWidth();
                    yScale = (float) previewWidth / bmp.getHeight();
                    break;
                case 180:
                    bmp = ImageUtils.rotate(bmp, 180);
                    break;
                case 270:
                    bmp = ImageUtils.rotate(bmp, 270);
                    xScale = (float) previewHeight / (float) h;
                    yScale = (float) previewWidth / (float) prevSettingWidth;
                    break;
            }

            fdet = new android.media.FaceDetector(bmp.getWidth(), bmp.getHeight(), MAX_FACE);

            android.media.FaceDetector.Face[] fullResults = new android.media.FaceDetector.Face[MAX_FACE];
            fdet.findFaces(bmp, fullResults);

            for (int i = 0; i < MAX_FACE; i++) {
                if (fullResults[i] == null) {
                    faces[i].clear();
                } else {
                    PointF mid = new PointF();
                    fullResults[i].getMidPoint(mid);

                    mid.x *= xScale;
                    mid.y *= yScale;

                    float eyesDis = fullResults[i].eyesDistance() * xScale;
                    float confidence = fullResults[i].confidence();
                    float pose = fullResults[i].pose(android.media.FaceDetector.Face.EULER_Y);
                    int idFace = Id;

                    Rect rect = new Rect(
                            (int) (mid.x - eyesDis * 1.20f),
                            (int) (mid.y - eyesDis * 0.55f),
                            (int) (mid.x + eyesDis * 1.20f),
                            (int) (mid.y + eyesDis * 1.85f));

                    // Зөвхөн нүүрний хэмжээ > 100x100
                    if (rect.height() * rect.width() > 100 * 100) {
                        for (int j = 0; j < MAX_FACE; j++) {
                            float eyesDisPre = faces_previous[j].eyesDistance();
                            PointF midPre = new PointF();
                            faces_previous[j].getMidPoint(midPre);

                            RectF rectCheck = new RectF(
                                    (midPre.x - eyesDisPre * 1.5f),
                                    (midPre.y - eyesDisPre * 1.15f),
                                    (midPre.x + eyesDisPre * 1.5f),
                                    (midPre.y + eyesDisPre * 1.85f));

                            if (rectCheck.contains(mid.x, mid.y) && (System.currentTimeMillis() - faces_previous[j].getTime()) < 1000) {
                                idFace = faces_previous[j].getId();
                                break;
                            }
                        }

                        if (idFace == Id) Id++;

                        faces[i].setFace(idFace, mid, eyesDis, confidence, pose, System.currentTimeMillis());

                        faces_previous[i].set(faces[i].getId(), faces[i].getMidEye(), faces[i].eyesDistance(), faces[i].getConfidence(), faces[i].getPose(), faces[i].getTime());

                        // if focus in a face 5 frame -> take picture face display in RecyclerView
                        // because of some first frame have low quality
                        if (facesCount.get(idFace) == null) {
                            facesCount.put(idFace, 0);
                        } else {
                            int count = facesCount.get(idFace) + 1;
                            if (count <= 5)
                                facesCount.put(idFace, count);

                            // Crop хийсэн царайны зургыг RecylerView дээр харуулах
                            if (count == 5) {
                                faceCroped = ImageUtils.cropFace(faces[i], bitmap, rotate);
                                if (faceCroped != null) {
                                    handler.post(new Runnable() {
                                        public void run() {
                                            imagePreviewAdapter.add(faceCroped);
                                            btUserPhoto = DbBitmap.getBytes(faceCroped);
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }

            handler.post(new Runnable() {
                public void run() {
                    //Царайны дүрсийг FaceView дээр зурах
                    mFaceView.setFaces(faces);

                    //FPS тооцоолох
                    end = System.currentTimeMillis();
                    counter++;
                    double time = (double) (end - start) / 1000;
                    if (time != 0)
                        fps = counter / time;

                    mFaceView.setFPS(fps);

                    if (counter == (Integer.MAX_VALUE - 1000))
                        counter = 0;

                    isThreadWorking = false;
                }
            });
        }
    }

    private void resetData() {
        if (imagePreviewAdapter == null) {
            facesBitmap = new ArrayList<>();
            imagePreviewAdapter = new ImagePreviewAdapter(FaceDetectActivity.this, facesBitmap, new ImagePreviewAdapter.ViewHolder.OnItemClickListener() {
                @Override
                public void onClick(View v, int position) {
                    imagePreviewAdapter.setCheck(position);
                    imagePreviewAdapter.notifyDataSetChanged();
                }
            });
            recyclerView.setAdapter(imagePreviewAdapter);
        } else {
            imagePreviewAdapter.clearAll();
        }
    }
}
