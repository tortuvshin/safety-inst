package mn.btgt.safetyinst.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cloud.techstar.imageloader.ImageLoader;
import mn.btgt.safetyinst.R;
import mn.btgt.safetyinst.adapter.UserListAdapter;
import mn.btgt.safetyinst.database.repo.SettingsRepo;
import mn.btgt.safetyinst.database.repo.UserRepo;
import mn.btgt.safetyinst.database.model.User;
import mn.btgt.safetyinst.utils.SAFCONSTANT;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class LoginListActivity extends AppCompatActivity {

    private static final String TAG = LoginListActivity.class.getSimpleName();

    UserRepo userRepo;
    SettingsRepo settingsRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_list);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.users_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        TextView compName = (TextView) findViewById(R.id.company_name);

        userRepo = new UserRepo();
        settingsRepo = new SettingsRepo();

        List<User> users = userRepo.selectAll();

        if (settingsRepo.select("company")!=null)
            compName.setText(settingsRepo.select("company"));

        RecyclerView.Adapter mAdapter = new UserListAdapter(this, users);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSettings);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(LoginListActivity.this, SettingsActivity.class));
            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.click_to_again, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
