package mn.btgt.safetyinst.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cloud.techstar.imageloader.ImageLoader;
import mn.btgt.safetyinst.R;
import mn.btgt.safetyinst.data.repo.SettingsRepo;
import mn.btgt.safetyinst.data.repo.UserRepo;
import mn.btgt.safetyinst.data.model.User;
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
    ImageLoader imageLoader;
    private Typeface roboto, robotoLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_list);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.users_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        TextView compName = (TextView) findViewById(R.id.company_name);

        imageLoader = new ImageLoader(this);
        userRepo = new UserRepo(this);
        settingsRepo = new SettingsRepo(this);

        List<User> users = userRepo.selectAll();

        if (settingsRepo.select("company")!=null)
            compName.setText(settingsRepo.select("company"));

        RecyclerView.Adapter mAdapter = new UserListAdapter(users);
        mRecyclerView.setAdapter(mAdapter);

        roboto = Typeface.createFromAsset(getAssets(),  "fonts/Roboto-Regular.ttf");
        robotoLight = Typeface.createFromAsset(getAssets(),  "fonts/Roboto-Light.ttf");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSettings);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(LoginListActivity.this, SettingsActivity.class));
            }
        });
    }

    public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
        List<User> users;
        String imageName;
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView imageView;
            private TextView mTextView;
            private TextView mPosText;
            private ViewHolder(View v) {
                super(v);
                v.setOnClickListener(this);
                imageView = (ImageView) v.findViewById(R.id.user_img);
                mTextView = (TextView) v.findViewById(R.id.username_text);
                mPosText = (TextView) v.findViewById(R.id.position_text);
                mTextView.setTypeface(roboto);
                mPosText.setTypeface(robotoLight);
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginListActivity.this, LoginImeiActivity.class);
                intent.putExtra("user_id", users.get(this.getAdapterPosition()).getId());
                Log.d("", users.get(this.getAdapterPosition()).getId());
                startActivity(intent);
            }
        }

        public UserListAdapter(List<User> users) {
            this.users = users;
        }

        @Override
        public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                        int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_list_item, parent, false);
            UserListAdapter.ViewHolder vh = new UserListAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(UserListAdapter.ViewHolder holder, int position) {
            imageName = users.get(position).getAvatar();
            imageLoader.DisplayImage(SAFCONSTANT.WEB_URL +"/upload/300x300/"+users.get(position).getAvatar(), holder.imageView);
            holder.mTextView.setText(users.get(position).getName());
            holder.mPosText.setText(users.get(position).getPosition());
        }

        @Override
        public int getItemCount() {
            return users.size();
        }
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
