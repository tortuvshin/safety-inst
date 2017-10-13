package btgt.mn.safetyinst;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.awt.font.TextAttribute;

import btgt.mn.safetyinst.database.UserTable;
import btgt.mn.safetyinst.entity.User;
import btgt.mn.safetyinst.utils.ImageLoader;
import btgt.mn.safetyinst.utils.PrefManager;
import btgt.mn.safetyinst.utils.SafConstants;

public class LoginImeiActivity extends AppCompatActivity {
    private AppCompatButton loginBtn;
    private EditText passText;
    private TextView positionText;
    private TextView usernameText;
    private TextView regLink;
    private ImageView imageView;
    UserTable userTable;
    String imei;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imageView = (ImageView) findViewById(R.id.login_user_avatar);
        positionText = (TextView) findViewById(R.id.position);
        usernameText = (TextView) findViewById(R.id.username);
        passText = (EditText) findViewById(R.id.password);
        loginBtn = (AppCompatButton) findViewById(R.id.login);
        regLink = (TextView) findViewById(R.id.linkReg);
        userTable = new UserTable(this);
        prefManager = new PrefManager(this);
        ImageLoader imageLoader = new ImageLoader(this);
        Intent iGet = getIntent();

        int id = Integer.parseInt(iGet.getStringExtra("user_id"));
        User user = userTable.get(id);
        imei = SafConstants.getImei(this);
        imageLoader.DisplayImage(SafConstants.WebURL+"/upload/300x300/"+user.getAvatar(), imageView);
        positionText.setText(user.getPosition());
        usernameText.setText(user.getName());
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        regLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginImeiActivity.this, RegisterActivity.class));
            }
        });
    }

    public void login() {

        String password = passText.getText().toString();
        String username = usernameText.getText().toString();

//        if (password.isEmpty() || password.length() < 4) {
//            passText.setError("Нууц үг буруу байна");
//            return;
//        } else {
//            passText.setError(null);
//        }
        if (password.trim().length() > 0) {

            Cursor checkeduser = userTable.checkUser(password);
            if(checkeduser != null){
                startManagingCursor(checkeduser);
                if (checkeduser.getCount() > 0){
                    stopManagingCursor(checkeduser);
                    checkeduser.close();
                    loginBtn.setEnabled(false);
                    final ProgressDialog progressDialog = new ProgressDialog(LoginImeiActivity.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Уншиж байна...");
                    progressDialog.show();
                    new android.os.Handler().post(
                            new Runnable() {
                                public void run() {
                                    Toast.makeText(LoginImeiActivity.this, "Тавтай морилно уу", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
//                                    prefManager.setLogin(true);
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                }
                            });
                }
                else{
                    Toast.makeText(LoginImeiActivity.this, "Хэрэглэгчийн нэр нууц үг буруу байна", Toast.LENGTH_SHORT).show();
                    return;
                }
                stopManagingCursor(checkeduser);
                checkeduser.close();

            }else{
                Toast.makeText(getApplicationContext(),
                        "Database query error",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(LoginImeiActivity.this,"Хэрэглэгчийн нэр нууц үгээ оруулна уу",Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
