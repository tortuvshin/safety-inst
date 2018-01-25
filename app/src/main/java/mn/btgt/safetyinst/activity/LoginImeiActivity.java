package mn.btgt.safetyinst.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import agency.techstar.imageloader.ImageLoader;
import mn.btgt.safetyinst.R;
import mn.btgt.safetyinst.database.UserTable;
import mn.btgt.safetyinst.model.User;
import mn.btgt.safetyinst.utils.PrefManager;
import mn.btgt.safetyinst.utils.SafConstants;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class LoginImeiActivity extends AppCompatActivity {

    private static final String TAG = LoginImeiActivity.class.getSimpleName();

    private AppCompatButton loginBtn;
    private AppCompatEditText passText;
    private AppCompatEditText usernameText;

    private UserTable userTable;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView imageView = (ImageView) findViewById(R.id.login_user_avatar);
        usernameText = (AppCompatEditText) findViewById(R.id.username);
        passText = (AppCompatEditText) findViewById(R.id.password);
        loginBtn = (AppCompatButton) findViewById(R.id.login);

        userTable = new UserTable(this);
        prefManager = new PrefManager(this);
        ImageLoader imageLoader = new ImageLoader(this);

        Intent iGet = getIntent();

        if (iGet.getStringExtra("username") == null){
            if (iGet.getStringExtra("user_id") != null){
                int id = Integer.parseInt(iGet.getStringExtra("user_id"));
                User user = userTable.select(id);
                imageLoader.DisplayImage(SafConstants.WEB_URL +"/upload/300x300/"+user.getAvatar(), imageView);
                usernameText.setText(user.getName());
                prefManager.setUserId(iGet.getStringExtra("user_id"));
                prefManager.setUsername(user.getName());
            }
        } else {
            prefManager.setUsername(iGet.getStringExtra("username"));
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public void login() {

        String password = passText.getText().toString();
        String username = usernameText.getText().toString();

        if (password.trim().length() > 0) {

            Cursor checkedUser = userTable.checkUser(password);
            if(checkedUser != null){
                startManagingCursor(checkedUser);
                if (checkedUser.getCount() > 0){
                    stopManagingCursor(checkedUser);
                    checkedUser.close();
                    loginBtn.setEnabled(false);
                    final ProgressDialog progressDialog = new ProgressDialog(LoginImeiActivity.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage(getString(R.string.loading));
                    progressDialog.show();
                    new android.os.Handler().post(
                            new Runnable() {
                                public void run() {
                                    Toast.makeText(LoginImeiActivity.this, R.string.welcome, Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    prefManager.setLogin(true);
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                }
                            });
                }
                else{
                    Toast.makeText(LoginImeiActivity.this, R.string.username_pass_incorrect, Toast.LENGTH_SHORT).show();
                    return;
                }
                stopManagingCursor(checkedUser);
                checkedUser.close();

            }else{
                Toast.makeText(getApplicationContext(),
                        R.string.error_database_query,
                        Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(LoginImeiActivity.this, R.string.enter_username_pass,Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
