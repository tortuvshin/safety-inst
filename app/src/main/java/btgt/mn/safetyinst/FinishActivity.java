package btgt.mn.safetyinst;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import btgt.mn.safetyinst.entity.SignData;
import btgt.mn.safetyinst.entity.User;
import btgt.mn.safetyinst.utils.DbBitmap;

public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        ArrayList<SignData> myList = (ArrayList<SignData>)getIntent().getSerializableExtra("signed_user");

        ImageView avatar = (ImageView) findViewById(R.id.user_avatar);
        ImageView sign = (ImageView) findViewById(R.id.user_signature);

        avatar.setImageBitmap(DbBitmap.getImage(myList.get(0).getPhoto()));
        sign.setImageBitmap(DbBitmap.getImage(myList.get(0).getUserSign()));

        AppCompatButton saveBtn = (AppCompatButton) findViewById(R.id.user_save);
    }
}
