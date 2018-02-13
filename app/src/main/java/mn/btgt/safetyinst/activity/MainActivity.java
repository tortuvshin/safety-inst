package mn.btgt.safetyinst.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import java.util.List;

import cloud.techstar.imageloader.ImageLoader;
import mn.btgt.safetyinst.R;
import mn.btgt.safetyinst.database.repo.SNoteRepo;
import mn.btgt.safetyinst.database.model.SNote;
import mn.btgt.safetyinst.utils.PrefManager;
import mn.btgt.safetyinst.views.CustomViewPager;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private int NUM_PAGES = 1;

    private CustomViewPager viewPager;
    private LinearLayout dotsLayout;
    private Button btnPrev, btnNext;

    ProgressBar progressBar;
    PrefManager prefManager;

    List<SNote> sNotes;

    int progressBarValue = 0;
    Handler handler;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        handler = new Handler(Looper.getMainLooper());
        prefManager = new PrefManager(this);
        viewPager = (CustomViewPager) findViewById(R.id.pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnPrev = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        SNoteRepo sNoteRepo = new SNoteRepo();

        sNotes = sNoteRepo.selectAll();

        NUM_PAGES = sNoteRepo.count();
        addBottomDots(0);

        changeStatusBarColor();

        ScreenSlidePagerAdapter myViewPagerAdapter = new ScreenSlidePagerAdapter(sNotes);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

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
     * @param current Хуудасны index
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

            collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
            imgPreview = (ImageView) view.findViewById(R.id.imgPreview);
            noteTitle = (TextView) view.findViewById(R.id.noteTitle);
            noteInfo = (WebView) view.findViewById(R.id.noteInfo);
            imageLoader = new ImageLoader(MainActivity.this);
//            imageLoader.DisplayImage(SAFCONSTANT.WEB_URL+"/upload/300x300/"+sNotes.select(position).getVoiceData(), imgPreview);
            imageLoader.DisplayImage("http://www.zasag.mn/uploads/201310/news/files/d5c04c615f75bad6576c752b3b27d8c0.jpeg", imgPreview);
            coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
            noteTitle.setText(sNotes.get(position).getName());
            prefManager.setSnoteId(sNotes.get(position).getId());
            prefManager.setSnoteName(sNotes.get(position).getName());
            noteInfo.loadDataWithBaseURL("", sNotes.get(position).getFrameData(), "text/html", "UTF-8", "");
            noteInfo.setBackgroundColor(Color.parseColor("#ffffff"));
            noteInfo.getSettings().setJavaScriptEnabled(true);
            noteInfo.getSettings().setDefaultTextEncodingName("UTF-8");

            nestedScrollView = (NestedScrollView) view.findViewById(R.id.sclDetail);
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
