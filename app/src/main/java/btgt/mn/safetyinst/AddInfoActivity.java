package btgt.mn.safetyinst;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

import btgt.mn.safetyinst.database.SNoteTable;
import btgt.mn.safetyinst.database.SignDataTable;
import btgt.mn.safetyinst.entity.SNote;
import btgt.mn.safetyinst.entity.SignData;
import btgt.mn.safetyinst.entity.User;
import btgt.mn.safetyinst.utils.DbBitmap;

public class AddInfoActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    private static final int CAMERA_REQUEST = 1888;

    Bitmap photo;

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    SignData userSigned;
    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;

    byte [] avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);

        userSigned = new SignData();
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
                saveBtn.setText(R.string.save);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            try {
                if (saveBtn.getText() == getString(R.string.save)) {
                    Bitmap bm = Bitmap.createBitmap(gestureView.getDrawingCache());
                    captureImage(view);
//                    userSigned = new SignData("1", "1", "1", Calendar.getInstance().getTime().toString(), DbBitmap.getBytes(bm), userSigned.getPhoto(), "");
                    userSigned.setId("1");
                    userSigned.setsNoteId("1");
                    userSigned.setUserId("1");
                    userSigned.setViewDate(Calendar.getInstance().getTime().toString());
                    userSigned.setPhoto(userSigned.getPhoto());
                    userSigned.setUserSign(DbBitmap.getBytes(bm));
                    userSigned.setSendStatus("");

                    Toast.makeText(AddInfoActivity.this, "Амжилттай хадгаллаа", Toast.LENGTH_LONG).show();
                    saveBtn.setText(R.string.send);
                } else {
//                    signDataTable.add(userSigned);
//                    Intent intent = new Intent(AddInfoActivity.this, FinishActivity.class);
//                    ArrayList<SignData> signDatas = new ArrayList<SignData>();
//                    signDatas.add(userSigned);
//                    intent.putExtra("signed_user", signDatas);
//                    startActivity(intent);
                    messageDialog();
                }
            } catch (Exception e) {
                Log.d("Gestures", e.getMessage());
                Toast.makeText(AddInfoActivity.this, "Алдаа гарлаа", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            }
        });
    }
    public void messageDialog() {

        final AppCompatDialog myDialog = new AppCompatDialog(this);
        myDialog.setContentView(R.layout.activity_finish);
        myDialog.setTitle("Таны мэдээлэл");
        myDialog.setCancelable(false);

        ImageView photo = (ImageView) myDialog.findViewById(R.id.user_avatar);
        ImageView signature = (ImageView) myDialog.findViewById(R.id.user_signature);

        photo.setImageBitmap(DbBitmap.getImage(userSigned.getPhoto()));
        signature.setImageBitmap(DbBitmap.getImage(userSigned.getUserSign()));
        Button login = (Button) myDialog.findViewById(R.id.user_save);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.show();
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
}
