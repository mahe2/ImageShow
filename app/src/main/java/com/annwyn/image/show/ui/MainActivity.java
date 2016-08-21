package com.annwyn.image.show.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Toast;

import com.annwyn.image.show.R;
import com.annwyn.image.show.ui.fragment.DashboardFragment;
import com.annwyn.image.show.ui.fragment.HomeFragment;
import com.annwyn.image.show.ui.fragment.SpecialFragment;
import com.annwyn.image.show.ui.support.SupportFragment;
import com.annwyn.image.show.utils.HttpUtils;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AlertDialog networkDialog;

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        this.initializeReceiver();
        this.initializeNavigation(this);
        this.startFragment(savedInstanceState);
    }

    private void startFragment(Bundle savedInstanceState) {
        startRootFragment(R.id.activity_main_container, HomeFragment.newInstance());
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        this.closeDrawer();
        switch (item.getItemId()) {
            case R.id.nav_home: {
                SupportFragment fragment = this.findStackFragment(HomeFragment.class);
                if (fragment == null)
                    fragment = HomeFragment.newInstance();
                this.startFragment(fragment);
                break;
            }
            case R.id.nav_dashboard: {
                SupportFragment fragment = this.findStackFragment(DashboardFragment.class);
                if (fragment == null)
                    fragment = DashboardFragment.newInstance();
                this.startFragment(fragment);
                break;
            }
            case R.id.nav_special: {
                SpecialFragment fragment = (SpecialFragment) this.findStackFragment(SpecialFragment.class);
                if (fragment == null)
                    fragment = SpecialFragment.newInstance();
                this.startFragment(fragment);
                break;
            }
        }
        return false;
    }

    @Override
    protected void onHomeMenuClick() {
        this.openDrawer();
    }

    @Override
    protected void onBackPressedSupport() {
        if (this.drawerLayout != null && this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.closeDrawer();
            return;
        }
        this.close();
    }

    private void close() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.exit_msg)
                .setNegativeButton(R.string.negative, this.dialogClickListener)
                .setPositiveButton(R.string.positive, this.dialogClickListener)
                .setCancelable(false)
                .create();
        dialog.show();
    }

    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            if (which == -1)
                finish();
        }
    };

    private void initializeReceiver() {
        if(this.receiver != null) return;
        this.receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                HttpUtils.getInstance().checkConnect(context);
                if(!HttpUtils.getInstance().isConnect())
                    Toast.makeText(context, context.getString(R.string.network_error_msg), Toast.LENGTH_LONG).show();
            }
        };
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(this.receiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(this.receiver != null)
            this.unregisterReceiver(this.receiver);
    }

}
