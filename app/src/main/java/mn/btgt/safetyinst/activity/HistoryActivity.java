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
import android.view.MenuItem;

import java.util.List;

import mn.btgt.safetyinst.adapter.HistoryAdapter;
import mn.btgt.safetyinst.database.model.SignData;
import mn.btgt.safetyinst.database.repo.SignDataRepo;

public class HistoryActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout = null;
    private Handler mHandler;
    private SignDataRepo signDataRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Түүх");
        }

        final RecyclerView mRecyclerView = findViewById(R.id.history_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = findViewById(R.id.swipeHistory);
        swipeRefreshLayout.setColorSchemeResources(R.color.bg_screen1, R.color.bg_screen2, R.color.bg_screen3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mHandler = new Handler(Looper.getMainLooper());
        SignDataRepo signDataRepo = new SignDataRepo();
        final List<SignData> signData = signDataRepo.selectAll();

        RecyclerView.Adapter mAdapter = new HistoryAdapter(this, signData);
        mRecyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView.Adapter mAdapter = new HistoryAdapter(HistoryActivity.this, signData);
                        mRecyclerView.setAdapter(mAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
