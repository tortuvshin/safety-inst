package btgt.mn.safetyinst;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import btgt.mn.safetyinst.database.DatabaseHelper;
import btgt.mn.safetyinst.database.UserTable;
import btgt.mn.safetyinst.entity.User;

public class RegisterActivity extends AppCompatActivity {


    private static final String TAG = "RegisterActivity";
    UserTable userTable;
    public SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    Button signUpButton;
    TextView loginLink;
    Animation waveAnimation,shakeAnimation,myShakeAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userTable = new UserTable(this);

        nameEditText = (EditText)findViewById(R.id.sName);
        emailEditText = (EditText)findViewById(R.id.sEmail);
        passwordEditText = (EditText)findViewById(R.id.sPassword);
        confirmPasswordEditText = (EditText)findViewById(R.id.sComfirmPassword);
        signUpButton = (Button)findViewById(R.id.btnSignUp);
        loginLink = (TextView)findViewById(R.id.linkLogin);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();

            }
        });
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }
        signUpButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Уншиж байна...");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onSignupSuccess();
                        progressDialog.dismiss();
                        finish();
                    }
                }, 1000);
    }

    public void onSignupSuccess() {
        signUpButton.setEnabled(true);
        setResult(RESULT_OK, null);

        Log.d(TAG, "Бүртгэл амжилттай боллоо...");
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        userTable.add(new User(null, name, "", 99999999, mngr.getDeviceId(), email, password, "", Calendar.getInstance().getTime().toString()));

        List<User> userInfo = userTable.getAll();

        for (User users : userInfo){
            String log = "ID:"+users.getId() + "\nNAME:"+users.getName()+
                    "\nImei:"+users.getImei()+"\nPASSWORD:"+users.getPassword();
            Log.d(TAG,log);
        }

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Бүртгүүлэхэд алдаа гарлаа", Toast.LENGTH_LONG).show();
        signUpButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameEditText.setError("хамгийн багадаа 3 тэмдэгт");
            valid = false;
        } else {
            nameEditText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("И-мэйл хаягаа зөв оруулна уу");
            valid = false;
        } else {
            emailEditText.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 17) {
            passwordEditText.setError("нууц үг 4-өөс 16 тэмдэгт");
            passwordEditText.setText("");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }
        if (!password.equals(confirmPassword)){
            confirmPasswordEditText.setError("Нууц үг таарахгүй байна");
            valid = false;
        }
        else{
            confirmPasswordEditText.setError(null);
        }

        Cursor user = userTable.checkUser(name,password);

        if(user == null){
            valid = false;
        }
        else{
            startManagingCursor(user);

            if (user.getCount() > 0){
                stopManagingCursor(user);
                user.close();
                valid = false;
            }
        }

        return valid;
    }
}
