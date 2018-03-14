package mn.btgt.safetyinst.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cloud.techstar.imageloader.ImageLoader;
import mn.btgt.safetyinst.R;
import mn.btgt.safetyinst.database.repo.UserRepo;
import mn.btgt.safetyinst.database.model.User;
import mn.btgt.safetyinst.utils.PrefManager;
import mn.btgt.safetyinst.utils.SAFCONSTANT;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class LoginImeiActivity extends AppCompatActivity {

    private AppCompatButton loginBtn;
    private AppCompatEditText passText;
    private TextView usernameText;

    private UserRepo userRepo;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView imageView = findViewById(R.id.login_user_avatar);
        usernameText = findViewById(R.id.username);
        TextView positionText = findViewById(R.id.position);
        passText = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login);

        userRepo = new UserRepo();
        prefManager = new PrefManager(this);
        ImageLoader imageLoader = new ImageLoader(this);

        Intent iGet = getIntent(); // Mercury дээрээс Intent ээр ирсэн өгөгдөл

        // Mercury-ээс ХАБ-руу дамжиж ирсэн бол шууд зааварчилгаа харуулна
        if (iGet.getStringExtra("username") == null){
            // Ажилтнуудын жагсаалтаас сонгосон хүний id
            if (iGet.getStringExtra("user_id") != null){
                int id = Integer.parseInt(iGet.getStringExtra("user_id"));
                User user = userRepo.select(id);
                imageLoader.DisplayImage(SAFCONSTANT.WEB_URL +"/upload/300x300/"+user.getAvatar(), imageView);
                usernameText.setText(user.getName());
                positionText.setText(user.getPosition());
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

        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);

        if (password.trim().length() > 0) {

            Cursor checkedUser = userRepo.checkUser(password);
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
                } else {
                    passText.setText("");
                    passText.startAnimation(shake);
                    Toast.makeText(LoginImeiActivity.this, R.string.username_pass_incorrect, Toast.LENGTH_SHORT).show();
                    return;
                }
                stopManagingCursor(checkedUser);
                checkedUser.close();

            } else {
                Toast.makeText(getApplicationContext(),
                        R.string.error_database_query,
                        Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            passText.startAnimation(shake);
            Toast.makeText(LoginImeiActivity.this, R.string.enter_username_pass,Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
