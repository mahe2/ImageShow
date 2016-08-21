package com.annwyn.image.show.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.annwyn.image.show.ImageApplication;
import com.annwyn.image.show.R;
import com.annwyn.image.show.connector.BaseConnector;
import com.annwyn.image.show.ui.support.SupportActivity;
import com.annwyn.image.show.utils.HttpUtils;
import com.annwyn.image.show.utils.ParamUtils;

import java.io.Serializable;

public class BaseActivity extends SupportActivity implements BaseConnector {

    public static final String SHARED_NAME = "image_show";

    public static final String SHARED_LAST_UPDATE = "last_update";

    public static final String ARG_ACTIVITY_IMG = "arg_activity_img";

    public static final String ARG_ACTIVITY_BEAN = "arg_activity_bean";

    public static final String ARG_ACTIVITY_BEAN_SPECIAL = "arg_activity_bean_special"; // 是否从special跳转过来的

    public static final int GRID_SPAN_COUNT = 3;

    protected DrawerLayout drawerLayout;

    public SharedPreferences readSharedPreferences() {
        return this.getSharedPreferences(SHARED_NAME, MODE_PRIVATE);
    }

    @SuppressWarnings("unused")
    protected void initializeToolbar(@StringRes int strRes, boolean isHome) {
        this.initializeToolbar(this.getResources().getString(strRes), isHome);
    }

    protected void initializeToolbar(String name, boolean isHome) {
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.layout_toolbar);
        this.initializeToolbar(toolbar, name, isHome);
    }

    public void initializeToolbar(Toolbar toolbar, String name, final boolean isHome) {
        if (toolbar == null) return;
        toolbar.setTitle(name);
        toolbar.setNavigationIcon(isHome ? R.mipmap.ic_menu : R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHomeMenuClick();
            }
        });
    }

    public void initializeNavigation(NavigationView.OnNavigationItemSelectedListener itemSelectedListener) {
        this.drawerLayout = (DrawerLayout) this.findViewById(R.id.activity_main_drawer);
        if (this.drawerLayout != null) {
            NavigationView navigationView = (NavigationView) this.findViewById(R.id.activity_main_navigation);
            //noinspection ConstantConditions
            navigationView.setNavigationItemSelectedListener(itemSelectedListener);
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    public void closeDrawer() {
//        if(this.drawerLayout != null && this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
        if (this.drawerLayout != null)
            this.drawerLayout.closeDrawers();
    }

    public void openDrawer() {
        if (this.drawerLayout != null)
            this.drawerLayout.openDrawer(GravityCompat.START);
    }

    /**
     * 点击toolbar上home图标时的点击事件
     * 默认是回退事件
     */
    protected void onHomeMenuClick() {
        this.onBackPressed();
    }

    public void startListActivity(Context context, Serializable serializable, boolean isSpecial) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra(ARG_ACTIVITY_BEAN, serializable);
        intent.putExtra(ARG_ACTIVITY_BEAN_SPECIAL, isSpecial);
        startActivity(intent);
    }

    public void startDetailActivity(Context context, Serializable serializable) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(ARG_ACTIVITY_IMG, serializable);
        startActivity(intent);
    }

    @Override
    public void showError(String msg, Exception e) {
        if (!HttpUtils.getInstance().isConnect()) {
            ParamUtils.showNetworkDialog(this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    finish();
                }
            }).show();
        }
    }

}
