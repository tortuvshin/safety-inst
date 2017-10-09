package btgt.mn.safetyinst;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import btgt.mn.safetyinst.database.SettingsTable;
import btgt.mn.safetyinst.database.UserTable;
import btgt.mn.safetyinst.entity.Settings;
import btgt.mn.safetyinst.entity.User;
import btgt.mn.safetyinst.utils.DbBitmap;

public class LoginListActivity extends AppCompatActivity {
    private static final String TAG = "LoginList";

    UserTable userTable;
    SettingsTable settingsTable;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_list);

        userTable = new UserTable(this);
        settingsTable = new SettingsTable(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.users_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        TextView compName = (TextView) findViewById(R.id.company_name);

        List<User> users = userTable.getAll();
        List<Settings> sett = settingsTable.get();

        try {

            for (Settings settings : sett){
                compName.setText(settings.getCompanyName());
            }


        }catch (NullPointerException npe){
            Log.d(TAG,"Алдаа : "+npe);
        }catch (Exception e){
            Log.d(TAG,"Алдаа : "+e);
        }
        mAdapter = new UserListAdapter(users);
        mRecyclerView.setAdapter(mAdapter);
    }


    public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
        List<User> users;
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public ImageView imageView;
            public TextView mTextView;
            public TextView mPosText;
            public ViewHolder(View v) {
                super(v);
                v.setOnClickListener(this);
                imageView = (ImageView) v.findViewById(R.id.user_img);
                mTextView = (TextView) v.findViewById(R.id.username_text);
                mPosText = (TextView) v.findViewById(R.id.position_text);

            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginListActivity.this, LoginImeiActivity.class);
                intent.putExtra("username", mTextView.getText().toString());
                intent.putExtra("position", mPosText.getText().toString());
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
            holder.imageView.setImageBitmap(DbBitmap.getImage(users.get(position).getAvatar()));
            holder.mTextView.setText(users.get(position).getName());
            holder.mPosText.setText(users.get(position).getPosition());
        }

        @Override
        public int getItemCount() {
            return users.size();
        }
    }
}
