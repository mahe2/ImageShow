package com.annwyn.image.show.ui.fragment;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;

import com.annwyn.image.show.R;
import com.annwyn.image.show.connector.BaseConnector;
import com.annwyn.image.show.ui.BaseActivity;
import com.annwyn.image.show.ui.support.SupportFragment;

/**
 *
 * Created by annwyn on 2016/7/17.
 */
public abstract class BaseFragment extends SupportFragment implements BaseConnector {

    protected BaseActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof BaseActivity) {
            this.activity = (BaseActivity) context;
        }
    }

    protected void initializeToolbar(@StringRes int stringRes, boolean isHome) {
        this.initializeToolbar(this.getString(stringRes), isHome);
    }

    @SuppressWarnings("ConstantConditions")
    protected void initializeToolbar(String title, final boolean isHome) {
        Toolbar toolbar = (Toolbar) this.getView().findViewById(R.id.layout_toolbar);
        this.activity.initializeToolbar(toolbar, title, isHome);
    }

    @Override
    public void showError(String msg, Exception e) {
        this.activity.showError(msg, e);
    }

}
