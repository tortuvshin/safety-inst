package mn.btgt.safetyinst.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cloud.techstar.imageloader.ImageLoader;
import mn.btgt.safetyinst.R;
import mn.btgt.safetyinst.activity.LoginImeiActivity;
import mn.btgt.safetyinst.database.model.User;
import mn.btgt.safetyinst.utils.SAFCONSTANT;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private final Context context;
    private List<User> users;
    private ImageLoader imageLoader;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private CardView cardView;
        private TextView mTextView;
        private TextView mPosText;
        private ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            cardView = v.findViewById(R.id.user_card_view);
            imageView = v.findViewById(R.id.user_img);
            mTextView = v.findViewById(R.id.username_text);
            mPosText = v.findViewById(R.id.position_text);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, LoginImeiActivity.class);
            intent.putExtra("user_id", users.get(this.getAdapterPosition()).getId());
            Log.d("", users.get(this.getAdapterPosition()).getId());
            context.startActivity(intent);
        }
    }

    public UserListAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_list, parent, false);
        UserListAdapter.ViewHolder vh = new UserListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(UserListAdapter.ViewHolder holder, int position) {
        imageLoader.DisplayImage(SAFCONSTANT.WEB_URL +"/upload/300x300/"+users.get(position).getAvatar(), holder.imageView);
        holder.mTextView.setText(users.get(position).getName());
        holder.mPosText.setText(users.get(position).getPosition());
        setAnimation(holder.cardView, position);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
        viewToAnimate.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}