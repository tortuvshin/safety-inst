package mn.btgt.safetyinst;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cloud.techstar.imageloader.ImageLoader;
import mn.btgt.safetyinst.database.repo.SNoteRepo;
import mn.btgt.safetyinst.database.model.SNote;
import mn.btgt.safetyinst.facedetect.FaceDetectActivity;
import mn.btgt.safetyinst.utils.ConnectionDetector;
import mn.btgt.safetyinst.utils.PrefManager;
import mn.btgt.safetyinst.utils.SAFCONSTANT;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author: Turtuvshin Byambaa.
 * URL: https://www.github.com/tortuvshin
 * Зааварчилгаа унших үндсэн цонх
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private int NUM_PAGES = 1;

    private CustomViewPager viewPager;
    private LinearLayout dotsLayout;
    private Button btnNext;
    private SwipeRefreshLayout swipeRefreshLayout = null;
    private ProgressBar progressBar;
    private PrefManager prefManager;

    private List<SNote> sNotes;

    private int progressBarValue = 0;
    private Handler handler;
    private Handler mHandler;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        mHandler = new Handler(Looper.getMainLooper());
        handler = new Handler(Looper.getMainLooper());
        prefManager = new PrefManager(this);
        viewPager = findViewById(R.id.pager);
        dotsLayout = findViewById(R.id.layoutDots);
        Button btnPrev = findViewById(R.id.btn_skip);
        btnNext = findViewById(R.id.btn_next);
        progressBar = findViewById(R.id.progressBar2);
        SNoteRepo sNoteRepo = new SNoteRepo();

        sNotes = sNoteRepo.selectAll();

        if(sNoteRepo.count() == 0) {
            startActivity(new Intent(MainActivity.this, FaceDetectActivity.class));
        }

        NUM_PAGES = sNoteRepo.count();
        addBottomDots(0);

        changeStatusBarColor();

        final ScreenSlidePagerAdapter myViewPagerAdapter = new ScreenSlidePagerAdapter(sNotes);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        swipeRefreshLayout = findViewById(R.id.swipeSnote);
        swipeRefreshLayout.setColorSchemeResources(R.color.bg_screen1, R.color.bg_screen2, R.color.bg_screen3);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSNote();
                        viewPager.setAdapter(myViewPagerAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(-1);
                if (current < NUM_PAGES) {
                    viewPager.setCurrentItem(current);
                } else {
                    addInfo();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < NUM_PAGES) {
                    viewPager.setCurrentItem(current);
                } else {
                    addInfo();
                }
            }
        });

        loader(0); //
    }

    /**
     * Зааварчилгааг уншиж дуустал дараагийн хуудасруу шилжихгүй
     * @param current Идэвхитэй хуудасны index
     */
    public void loader(final int current){

        btnNext.setVisibility(View.INVISIBLE);
        progressBarValue = 0;
        Thread readTh = new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressBarValue < 100)
                {
                    progressBarValue++;
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            progressBar.setProgress(progressBarValue);
                            if (progressBarValue == 100) {
                                btnNext.setVisibility(View.VISIBLE);
                                Thread.interrupted();
                            }
                        }
                    });
                    try {
                        Thread.sleep(sNotes.get(current).getTimeout());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        readTh.start();

    }

    /**
     * Доод хэсэгт хуудасны тоогоор цэг харуулах
     * @param currentPage Идэвхитэй байгаа хуудас
     */
    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[NUM_PAGES];

        int colorsActive = getResources().getColor(R.color.active);
        int colorsInactive = getResources().getColor(R.color.inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void addInfo() {
        finish();
        startActivity(new Intent(MainActivity.this, FaceDetectActivity.class));
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            loader(position);
            if (position == NUM_PAGES - 1) {
                btnNext.setText(getString(R.string.start));
            } else {
                btnNext.setText(getString(R.string.next));
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            viewPager.getParent().requestDisallowInterceptTouchEvent(true);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void getSNote() {

        if (!ConnectionDetector.isNetworkAvailable(MainActivity.this)){
            Toast.makeText(MainActivity.this, R.string.no_internet, Toast.LENGTH_LONG).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("time", String.valueOf(System.currentTimeMillis()))
                .addFormDataPart("imei", SAFCONSTANT.getImei(this))
                .addFormDataPart("AndroidId", SAFCONSTANT.getAndroiId(this))
                .build();

        Request request = new Request.Builder()
                .url(SAFCONSTANT.API_URL)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("app", SAFCONSTANT.APP_NAME)
                .addHeader("appV", SAFCONSTANT.getAppVersion(this))
                .addHeader("Imei", SAFCONSTANT.getImei(this))
                .addHeader("AndroidId", SAFCONSTANT.getAndroiId(this))
                .addHeader("nuuts", SAFCONSTANT.getSecretCode(SAFCONSTANT.getImei(this), String.valueOf(System.currentTimeMillis())))
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Logger.e("Server connection failed : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();
                Logger.json(res);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONArray ob = new JSONArray(String.valueOf(res));
                            if (ob.length() < 1)
                                return;

                            JSONObject setting = ob.getJSONObject(0);
                            JSONArray users = setting.getJSONArray("users");
                            JSONArray notes = setting.getJSONArray("notes");

                            int success = setting.getInt("success");
                            int error = setting.getInt("error");
                            if (success== 0 && error == 900){
                                Toast.makeText(MainActivity.this,
                                        getString(R.string.error) + error,
                                        Toast.LENGTH_SHORT).show();
                            } else if ( success == 1) {

                                Logger.json(notes.toString());

                                if (notes.length() > 0){
                                    SNoteRepo sNoteRepo = new SNoteRepo();

                                    sNoteRepo.deleteAll();

                                    for (int i = 0; i < notes.length(); i++) {
                                        SNote sNote = new SNote();
                                        sNote.setId(notes.getJSONObject(i).getString("id"));
                                        sNote.setCategoryId(notes.getJSONObject(i).getString("category_id"));
                                        sNote.setName(notes.getJSONObject(i).getString("name"));
                                        sNote.setOrder(notes.getJSONObject(i).getString("orderx"));
                                        sNote.setFrameType(notes.getJSONObject(i).getInt("frame_type"));
                                        sNote.setFrameData(notes.getJSONObject(i).getString("frame_data"));
                                        sNote.setVoiceData("");
                                        sNote.setTimeout(notes.getJSONObject(i).getInt("timeout"));
                                        sNoteRepo.insert(sNote);
                                    }

                                } else {
                                    Toast.makeText(MainActivity.this, R.string.empty_note, Toast.LENGTH_LONG)
                                            .show();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    /**
     * Зааварчилгаанууд хуудаслаж харуулах Adapter
     */
    private class ScreenSlidePagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        CollapsingToolbarLayout collapsingToolbar;

        ImageView imgPreview;
        CoordinatorLayout coordinatorLayout;
        ImageLoader imageLoader;
        TextView noteTitle;
        WebView noteInfo;
        NestedScrollView nestedScrollView;
        List<SNote> pagerSNotes;
        ScreenSlidePagerAdapter(List<SNote> sNotes) {
            this.pagerSNotes = sNotes;
        }

        @NonNull
        @SuppressLint({"SetJavaScriptEnabled"})
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert layoutInflater != null;
            View view = layoutInflater.inflate(R.layout.snote_viewer, container, false);

            collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(sNotes.get(position).getName());
            imgPreview = view.findViewById(R.id.imgPreview);
            noteTitle = view.findViewById(R.id.noteTitle);
            noteInfo = view.findViewById(R.id.noteInfo);
            imageLoader = new ImageLoader(MainActivity.this);
//            imageLoader.DisplayImage(SAFCONSTANT.WEB_URL+"/upload/300x300/"+sNotes.select(position).getVoiceData(), imgPreview);
            imageLoader.DisplayImage("http://www.zasag.mn/uploads/201310/news/files/d5c04c615f75bad6576c752b3b27d8c0.jpeg", imgPreview);
            coordinatorLayout = findViewById(R.id.main_content);
            noteTitle.setText(sNotes.get(position).getName());
            prefManager.setSnoteId(sNotes.get(position).getId());
            prefManager.setSnoteName(sNotes.get(position).getName());
            noteInfo.loadDataWithBaseURL("", sNotes.get(position).getFrameData(), "text/html", "UTF-8", "");
            noteInfo.setBackgroundColor(Color.parseColor("#ffffff"));
            noteInfo.getSettings().setJavaScriptEnabled(true);
            noteInfo.getSettings().setDefaultTextEncodingName("UTF-8");

            nestedScrollView = view.findViewById(R.id.sclDetail);
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > oldScrollY) {
                        Log.i(TAG, "Scroll DOWN");
                    }
                    if (scrollY < oldScrollY) {
                        Log.i(TAG, "Scroll UP");
                    }

                    if (scrollY == 0) {
                        Log.i(TAG, "TOP SCROLL");
                    }

                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        Log.i(TAG, "BOTTOM SCROLL");
                    }
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
