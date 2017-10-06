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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import btgt.mn.safetyinst.database.UserTable;

public class LoginImeiActivity extends AppCompatActivity {
    private AppCompatButton loginBtn;
    private EditText passText;
    private TextView positionText;
    private TextView usernameText;
    private TextView regLink;
    UserTable userTable;
    TelephonyManager mngr;
    String imei;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        positionText = (TextView) findViewById(R.id.position);
        usernameText = (TextView) findViewById(R.id.username);
        passText = (EditText) findViewById(R.id.password);
        loginBtn = (AppCompatButton) findViewById(R.id.login);
        regLink = (TextView) findViewById(R.id.linkReg);
        userTable = new UserTable(this);

        mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Intent iGet = getIntent();
        imei = mngr.getDeviceId();
        positionText.setText(iGet.getStringExtra("position"));
        usernameText.setText(iGet.getStringExtra("username"));
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

        if (password.isEmpty() || password.length() < 4) {
            passText.setError("нууц үг 4-өөс олон тэмдэгт байна");
            return;
        } else {
            passText.setError(null);
        }
        if (password.trim().length() > 0) {

            Cursor checkeduser = userTable.checkUser(imei, password);
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
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Toast.makeText(LoginImeiActivity.this, "Тавтай морилно уу", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                }
                            }, 300);
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
