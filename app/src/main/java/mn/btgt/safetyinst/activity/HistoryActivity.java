package mn.btgt.safetyinst.activity;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import mn.btgt.safetyinst.R;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

import mn.btgt.safetyinst.adapter.HistoryAdapter;
import mn.btgt.safetyinst.adapter.UserListAdapter;
import mn.btgt.safetyinst.database.model.SNote;
import mn.btgt.safetyinst.database.model.User;
import mn.btgt.safetyinst.database.repo.SNoteRepo;
import mn.btgt.safetyinst.database.repo.SettingsRepo;
import mn.btgt.safetyinst.database.repo.UserRepo;

public class HistoryActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout = null;
    private Handler mHandler;
    private SNoteRepo sNoteRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        final RecyclerView mRecyclerView = findViewById(R.id.history_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeHistory);
        swipeRefreshLayout.setColorSchemeResources(R.color.bg_screen1, R.color.bg_screen2, R.color.bg_screen3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mHandler = new Handler(Looper.getMainLooper());
        SNoteRepo sNoteRepo = new SNoteRepo();
        final List<SNote> sNotes = sNoteRepo.selectAll();

        RecyclerView.Adapter mAdapter = new HistoryAdapter(this, sNotes);
        mRecyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView.Adapter mAdapter = new HistoryAdapter(HistoryActivity.this, sNotes);
                        mRecyclerView.setAdapter(mAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }
}
