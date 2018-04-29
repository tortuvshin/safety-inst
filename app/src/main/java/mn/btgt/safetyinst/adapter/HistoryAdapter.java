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
import mn.btgt.safetyinst.database.model.SNote;
import mn.btgt.safetyinst.database.model.SignData;
import mn.btgt.safetyinst.utils.ImageUtils;
import mn.btgt.safetyinst.utils.SAFCONSTANT;
import mn.btgt.safetyinst.utils.Util;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private final Context context;
    private List<SignData> signData;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private CardView cardView;
        private TextView mTextView;
        private TextView mSnoteText;
        private TextView timeText;
        private ViewHolder(View v) {
            super(v);

            Typeface roboto = Typeface.createFromAsset(context.getAssets(),  "fonts/Roboto-Regular.ttf");
            Typeface robotoLight = Typeface.createFromAsset(context.getAssets(),  "fonts/Roboto-Light.ttf");

            cardView = v.findViewById(R.id.history_card_view);
            imageView = v.findViewById(R.id.his_img);
            mTextView = v.findViewById(R.id.his_user);
            mSnoteText = v.findViewById(R.id.snote_text);
            timeText = v.findViewById(R.id.time_text);
            mTextView.setTypeface(roboto);
            mSnoteText.setTypeface(robotoLight);
        }
    }

    public HistoryAdapter(Context context, List<SignData> signData) {
        this.context = context;
        this.signData = signData;
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        HistoryAdapter.ViewHolder vh = new HistoryAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {

        holder.imageView.setImageBitmap(ImageUtils.getImage(signData.get(position).getPhoto()));
        holder.mTextView.setText(signData.get(position).getUserName());
        holder.mSnoteText.setText(signData.get(position).getsNoteName());
        holder.timeText.setText(signData.get(position).getViewDate());
    }

    @Override
    public int getItemCount() {
        return signData.size();
    }
}