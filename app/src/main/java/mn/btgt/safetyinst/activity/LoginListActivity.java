package mn.btgt.safetyinst.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import agency.techstar.imageloader.ImageLoader;
import mn.btgt.safetyinst.R;
import mn.btgt.safetyinst.database.SettingsTable;
import mn.btgt.safetyinst.database.UserTable;
import mn.btgt.safetyinst.entity.User;
import mn.btgt.safetyinst.utils.SafConstants;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class LoginListActivity extends AppCompatActivity {
    private static final String TAG = "LoginList";

    UserTable userTable;
    SettingsTable settingsTable;
    ImageLoader imageLoader;

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
        userTable = new UserTable(this);
        settingsTable = new SettingsTable(this);

        List<User> users = userTable.getAll();

        if (settingsTable.get("company")!=null)
            compName.setText(settingsTable.get("company"));

        RecyclerView.Adapter mAdapter = new UserListAdapter(users);
        mRecyclerView.setAdapter(mAdapter);
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
            imageLoader.DisplayImage(SafConstants.WEB_URL +"/upload/300x300/"+users.get(position).getAvatar(), holder.imageView);
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
