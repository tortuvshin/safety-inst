package mn.btgt.safetyinst.history;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mn.btgt.safetyinst.R;
import mn.btgt.safetyinst.db.model.SignData;
import mn.btgt.safetyinst.utils.ImageUtils;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private final Context context;
    private List<SignData> signData;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private CardView cardView;
        private TextView userText;
        private TextView noteText;
        private TextView timeText;
        private ImageView isSendImage;
        private ViewHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.history_card_view);
            imageView = v.findViewById(R.id.his_img);
            userText = v.findViewById(R.id.his_user);
            noteText = v.findViewById(R.id.snote_text);
            timeText = v.findViewById(R.id.time_text);
            isSendImage = v.findViewById(R.id.is_send);
            isSendImage.setVisibility(View.INVISIBLE);
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
                .inflate(R.layout.item_history, parent, false);
        HistoryAdapter.ViewHolder vh = new HistoryAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {

        holder.imageView.setImageBitmap(ImageUtils.getImage(signData.get(position).getPhoto()));
        holder.userText.setText(signData.get(position).getUserName());
        holder.noteText.setText(signData.get(position).getsNoteName());
        holder.timeText.setText(signData.get(position).getViewDate());
        if(signData.get(position).getSendStatus().equals("true")) {
            holder.isSendImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return signData.size();
    }
}