package com.annwyn.image.show.ui;

import android.app.DownloadManager;
import android.app.Presentation;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.annwyn.image.show.R;
import com.annwyn.image.show.connector.DetailConnector;
import com.annwyn.image.show.model.Detail;
import com.annwyn.image.show.presenter.DetailPresenter;
import com.annwyn.image.show.presenter.impl.DetailPresenterImpl;
import com.annwyn.image.show.utils.DownloadUtils;
import com.annwyn.image.show.utils.HttpUtils;
import com.annwyn.image.show.utils.ImageLoaderUtils;
import com.annwyn.image.show.utils.ParamUtils;

/**
 *
 * Created by Administrator on 2016/7/25.
 */
public class DetailActivity extends BaseActivity implements DetailConnector {

    private Detail detail;

    private FloatingActionButton download;

    private DetailPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.showStatusBar();
        setContentView(R.layout.activity_detail);

        this.inflateView();
    }

    private void inflateView() {
        if(HttpUtils.getInstance().isConnect()) {
            ViewStub stub = (ViewStub) this.findViewById(R.id.activity_detail_normal);
            stub.inflate();
            this.presenter = new DetailPresenterImpl(this, this);
            this.detail = (Detail) this.getIntent().getSerializableExtra(ARG_ACTIVITY_IMG);
            this.initView();
        } else {
            ViewStub stub = (ViewStub) this.findViewById(R.id.activity_detail_error);
            stub.inflate();
        }
    }

    private void initView() {
        this.download = (FloatingActionButton) this.findViewById(R.id.activity_detail_download);
        ImageView imageView = (ImageView) this.findViewById(R.id.activity_detail_background);
        ImageLoaderUtils.getInstance().displayImage(this.detail.getOriginal(), imageView);
        //noinspection ConstantConditions
        imageView.setOnClickListener(this.onClickListener);
        this.download.setOnClickListener(this.onClickListener);
    }


    private void showOrHideFAB() {
        if (download.getVisibility() == View.VISIBLE)
            download.hide();
        else
            download.show();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_detail_background: {
                    showOrHideFAB();
                    break;
                }
                case R.id.activity_detail_download: {
                    presenter.downloadImage(detail.getId(), detail.getOriginal());
                    break;
                }
            }
        }
    };

    private void showStatusBar() {
//        WindowManager.LayoutParams params = this.getWindow().getAttributes();
//        params.flags &= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        this.getWindow().setAttributes(params);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

}
