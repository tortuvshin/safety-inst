package btgt.mn.safetyinst.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import btgt.mn.safetyinst.R;

/**
 * Created by turtuvshin on 10/4/17.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private String[] mDataset;

    ArrayList<String> userListItems = new ArrayList<String>();
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.username_text);
        }
    }

    public UserListAdapter(ArrayList<String> userList) {
        userListItems = userList;
    }

    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(userListItems.get(position));

    }

    @Override
    public int getItemCount() {
        return userListItems.size();
    }
}

