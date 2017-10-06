package btgt.mn.safetyinst;

import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import btgt.mn.safetyinst.database.SNoteTable;
import btgt.mn.safetyinst.entity.SNote;

public class AddInfoActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    ImageView imageView;
    Bitmap photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);

        imageView = (ImageView) findViewById(R.id.imageView1);
        Button saveBtn = (Button) findViewById(R.id.save);
        Button clearBtn = (Button) findViewById(R.id.clear);

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    GestureOverlayView gestureView = (GestureOverlayView) findViewById(R.id.signaturePad);
                    gestureView.setDrawingCacheEnabled(true);
                    Bitmap bm = Bitmap.createBitmap(gestureView.getDrawingCache());
                    File f = new File(Environment.getExternalStorageDirectory()
                                   + File.separator + "signature.png");
                    f.createNewFile();
                    FileOutputStream os = new FileOutputStream(f);
                    os = new FileOutputStream(f);
                    //compress to specified format (PNG), quality - which is ignored for PNG, and out stream
                    bm.compress(Bitmap.CompressFormat.PNG, 100, os);
                    os.close();
                    Toast.makeText(AddInfoActivity.this, "Амжилттай хадгаллаа", Toast.LENGTH_LONG).show();
                 } catch (Exception e) {
                    Log.v("Gestures", e.getMessage());
                    e.printStackTrace();
                 }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }
}
