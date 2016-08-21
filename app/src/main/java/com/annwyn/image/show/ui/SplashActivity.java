package com.annwyn.image.show.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.annwyn.image.show.R;
import com.annwyn.image.show.connector.SplashConnector;
import com.annwyn.image.show.presenter.SplashPresenter;
import com.annwyn.image.show.presenter.impl.SplashPresenterImpl;
import com.annwyn.image.show.utils.HttpUtils;

public class SplashActivity extends BaseActivity implements SplashConnector {

    private ImageView splash;

    private SplashPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.showStatusBar();
        this.setContentView(R.layout.activity_splash);
        this.presenter = new SplashPresenterImpl(this, this);

        this.initView();
    }

    private void initView() {
        this.splash = (ImageView) this.findViewById(R.id.activity_splash_loading);
        this.initAnimation();
        this.presenter.startLoading(this.readSharedPreferences());
    }

    private void initAnimation() {
        Animation animation = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(3000);
        splash.setAnimation(animation);
        animation.startNow();
    }

    @Override
    public void loadComplete(boolean hasRefresh) {
        if (hasRefresh) this.updateSharedPreferences();
        this.splash.postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        }, 2000);
    }

    private void startMainActivity() {
        if(HttpUtils.getInstance().checkConnect(this)) {
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        } else {
            showError("", null);
        }
    }

    private void updateSharedPreferences() {
        SharedPreferences.Editor editor = this.readSharedPreferences().edit();
        editor.putLong(BaseActivity.SHARED_LAST_UPDATE, System.currentTimeMillis());
        editor.apply();
    }

    private void showStatusBar() {
//        WindowManager.LayoutParams params = this.getWindow().getAttributes();
//        params.flags &= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        this.getWindow().setAttributes(params);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

}
