package btgt.mn.safetyinst;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginImeiActivity extends AppCompatActivity {
    private AppCompatButton loginBtn;
    private EditText passText;
    private TextView imeiCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imeiCode = (TextView) findViewById(R.id.imei_code);
        passText = (EditText) findViewById(R.id.password);
        loginBtn = (AppCompatButton) findViewById(R.id.login);

        TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        imeiCode.setText("Imei: "+mngr.getDeviceId());

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginImeiActivity.this, MainActivity.class));
            }
        });
    }
}
