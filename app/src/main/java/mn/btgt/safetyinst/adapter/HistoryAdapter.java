package mn.btgt.safetyinst.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import mn.btgt.safetyinst.activity.HistoryActivity;
import mn.btgt.safetyinst.database.model.SNote;

public class HistoryAdapter extends RecyclerView.Adapter {
    public HistoryAdapter(HistoryActivity historyActivity, List<SNote> sNotes) {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
