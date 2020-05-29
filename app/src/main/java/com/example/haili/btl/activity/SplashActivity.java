package com.example.haili.btl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;


import com.example.haili.btl.R;
import com.example.haili.btl.define.Conts;
import com.example.haili.btl.sqlite.DatabaseUtil;
import com.example.haili.btl.util.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {

    private static int SPLASH_TIME_OUT = 1000;

    @BindView(R.id.content_splash)
    RelativeLayout contentSplash;

    private SharedPreferencesUtil preUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        loadAnimation();
        initDataBase();

//        Log.e("CREATE TABLE PLACE", SqliteHelper.CREATE_TABLE_PLACE_SQL);
//        Log.e("CREATE CATOGY", SqliteHelper.CREATE_TABLE_CATEGORY_SQL);
//        Log.e("INSEST", SqliteHelper.INSERT_CATEGORY_SQL);
    }

    private void loadAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha_background);
        contentSplash.setAnimation(animation);
        animation.setAnimationListener(this);
    }

    private void initDataBase() {
        preUtil = new SharedPreferencesUtil(this);
        final boolean firstCreateDb = preUtil.getBoolean(Conts.FIRST_RUN_APP, false);
        if (!firstCreateDb) {
            new CreateDataBase(this, firstCreateDb).execute();
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, CategoriesActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    class CreateDataBase extends AsyncTask<Void, Void, Void> {

        private Context context;
        private boolean firstRun;

        public CreateDataBase(Context context, boolean firstRun) {
            this.context = context;
            this.firstRun = firstRun;
        }

        @Override
        protected Void doInBackground(Void... params) {
            DatabaseUtil databaseUtil = DatabaseUtil.getInstance(context);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            preUtil.saveBoolean(Conts.FIRST_RUN_APP, true);
        }
    }
}