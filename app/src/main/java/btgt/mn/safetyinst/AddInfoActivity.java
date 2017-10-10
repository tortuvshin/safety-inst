package btgt.mn.safetyinst;

import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import btgt.mn.safetyinst.database.SNoteTable;
import btgt.mn.safetyinst.database.SignDataTable;
import btgt.mn.safetyinst.entity.SNote;
import btgt.mn.safetyinst.entity.SignData;
import btgt.mn.safetyinst.utils.DbBitmap;

public class AddInfoActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    private static final int CAMERA_REQUEST = 1888;

    Bitmap photo;

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);

        AppCompatButton saveBtn = (AppCompatButton) findViewById(R.id.save);
        AppCompatButton clearBtn = (AppCompatButton) findViewById(R.id.clear);

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
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshCamera();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            try {
                GestureOverlayView gestureView = (GestureOverlayView) findViewById(R.id.signaturePad);
                gestureView.setDrawingCacheEnabled(true);
                Bitmap bm = Bitmap.createBitmap(gestureView.getDrawingCache());
                captureImage(view);
                signDataTable.add(new SignData("1", "1", "1", Calendar.getInstance().getTime().toString(), DbBitmap.getBytes(bm), DbBitmap.getBytes(bm), ""));

                Toast.makeText(AddInfoActivity.this, "Амжилттай хадгаллаа", Toast.LENGTH_LONG).show();
             } catch (Exception e) {
                Log.v("Gestures", e.getMessage());
                Toast.makeText(AddInfoActivity.this, "Алдаа гарлаа", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
             }
            }
        });

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
//            }
//        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//            photo = (Bitmap) data.getExtras().get("data");
////            imageView.setImageBitmap(photo);
//        }
//    }

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
