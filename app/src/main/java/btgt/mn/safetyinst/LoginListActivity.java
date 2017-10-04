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

import btgt.mn.safetyinst.adapter.UserListAdapter;
import btgt.mn.safetyinst.database.UserTable;
import btgt.mn.safetyinst.entity.User;

public class LoginListActivity extends AppCompatActivity {
    private static final String TAG = "LoginList";
    private ListView usersList;
    final Context context = this;
    final ArrayList<String> userListItems = new ArrayList<String>();
    private ArrayAdapter<String> myArrayAdapter;
    UserTable userTable;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_list);
        usersList = (ListView)findViewById(R.id.usersList);

        userTable = new UserTable(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,userListItems);
        usersList.setAdapter(myArrayAdapter);
        usersList.setTextFilterEnabled(true);
        List<User> users = userTable.getAllUsers();

        try {
            for (User user : users){
                String userName = user.getName();
                userListItems.add(0, userName);
            }
        }catch (NullPointerException npe){
            Log.d(TAG,"Алдаа : "+npe);
        }catch (Exception e){
            Log.d(TAG,"Алдаа : "+e);
        }
        mAdapter = new UserListAdapter(userListItems);
        mRecyclerView.setAdapter(mAdapter);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getItemAtPosition(position);
                Intent intent = new Intent(LoginListActivity.this, LoginImeiActivity.class);
                intent.putExtra("username", o.toString());
                startActivity(intent);
            }
        });
        myArrayAdapter.notifyDataSetChanged();
    }
}
