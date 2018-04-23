package mn.btgt.safetyinst.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mn.btgt.safetyinst.R;
import mn.btgt.safetyinst.adapter.UserListAdapter;
import mn.btgt.safetyinst.database.repo.SettingsRepo;
import mn.btgt.safetyinst.database.repo.UserRepo;
import mn.btgt.safetyinst.database.model.User;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class LoginListActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    private SwipeRefreshLayout swipeRefreshLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_list);

        final RecyclerView mRecyclerView = findViewById(R.id.users_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeUserList);
        swipeRefreshLayout.setColorSchemeResources(R.color.bg_screen1, R.color.bg_screen2, R.color.bg_screen3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        final TextView compName = findViewById(R.id.company_name);

        UserRepo userRepo = new UserRepo();
        final SettingsRepo settingsRepo = new SettingsRepo();

        final List<User> users = userRepo.selectAll();

        if (settingsRepo.select("company")!=null)
            compName.setText(settingsRepo.select("company"));

        RecyclerView.Adapter mAdapter = new UserListAdapter(this, users);
        mRecyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView.Adapter mAdapter = new UserListAdapter(LoginListActivity.this, users);
                        mRecyclerView.setAdapter(mAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        FloatingActionButton fab = findViewById(R.id.fabSettings);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(LoginListActivity.this, SettingsActivity.class));
            }
        });
    }

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
