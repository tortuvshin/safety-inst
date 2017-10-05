package btgt.mn.safetyinst;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import btgt.mn.safetyinst.database.SettingsTable;
import btgt.mn.safetyinst.database.UserTable;
import btgt.mn.safetyinst.entity.Settings;
import btgt.mn.safetyinst.entity.User;

public class LoginListActivity extends AppCompatActivity {
    private static final String TAG = "LoginList";

    final ArrayList<String> userName = new ArrayList<String>();
    final ArrayList<String> userPos = new ArrayList<String>();

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

        Log.d("", sett.toString());

        try {
            for (User user : users){
                userName.add(0, user.getName());
                userPos.add(0, user.getPosition());
            }
            for (Settings settings : sett){
                compName.setText(settings.getCompanyName());
            }


        }catch (NullPointerException npe){
            Log.d(TAG,"Алдаа : "+npe);
        }catch (Exception e){
            Log.d(TAG,"Алдаа : "+e);
        }
        mAdapter = new UserListAdapter(userName, userPos);
        mRecyclerView.setAdapter(mAdapter);
    }


    public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
        ArrayList<String> userNames = new ArrayList<String>();
        ArrayList<String> userPos = new ArrayList<String>();
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView mTextView;
            public TextView mPosText;
            public ViewHolder(View v) {
                super(v);
                v.setOnClickListener(this);
                mTextView = (TextView) v.findViewById(R.id.username_text);
                mPosText = (TextView) v.findViewById(R.id.position_text);

            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginListActivity.this, LoginImeiActivity.class);
                intent.putExtra("username", mTextView.getText().toString());
                startActivity(intent);
            }
        }

        public UserListAdapter(ArrayList<String> userNames,ArrayList<String> userPos) {
            this.userNames = userNames;
            this.userPos = userPos;
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
            holder.mTextView.setText(userNames.get(position));
            holder.mPosText.setText(userPos.get(position));
        }

        @Override
        public int getItemCount() {
            return userNames.size();
        }
    }
}
