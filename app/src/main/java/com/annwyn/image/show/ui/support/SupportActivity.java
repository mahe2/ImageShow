package com.annwyn.image.show.ui.support;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.annwyn.image.show.ui.fragment.BaseFragment;

/**
 * activity支持类
 * Created by annwyn on 2016/7/16.
 */
public class SupportActivity extends AppCompatActivity {

    private FragmentUtils utils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initializeUtils();
    }

    protected FragmentUtils initializeUtils() {
        if(this.utils == null) {
            this.utils = new FragmentUtils();
        }
        return this.utils;
    }

    /**
     * @see FragmentUtils#startRootFragment(FragmentManager, int, SupportFragment)
     * @param containerID 布局id
     * @param root fragment
     */
    public void startRootFragment(int containerID, SupportFragment root) {
        this.utils.startRootFragment(this.getSupportFragmentManager(), containerID, root);
    }

    /**
     * @see FragmentUtils#startFragment(FragmentManager, SupportFragment, SupportFragment)
     * @param to 下一个fragment
     */
    public void startFragment(SupportFragment to) {
        FragmentManager manager = this.getSupportFragmentManager();
        BaseFragment from = (BaseFragment) this.utils.findActiveFragment(manager);
        if(from != to) {
            this.utils.startFragment(manager, this.utils.findActiveFragment(manager), to);
        }
    }

    public SupportFragment findStackFragment(Class<? extends SupportFragment> clazz) {
        return this.utils.findStackFragment(clazz, this.getSupportFragmentManager());
    }

    /**
     * 最好不要直接重写该方法(会将back事件传递到当前显示的fragment中)
     * 响应回退事件
     * 如果不需要将back事件传递至fragment中可以重写
     */
    @Override
    public void onBackPressed() {
        // 寻找当前显示的fragment
        SupportFragment fragment = this.utils.findActiveFragment(this.getSupportFragmentManager());
        if (this.dispatchBackPressed(fragment)) // 处处回调fragment中对back事件的处理
            return;
        this.onBackPressedSupport();
    }

    protected void onBackPressedSupport() {
        if (this.getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            this.finish();
        }
    }

    private boolean dispatchBackPressed(SupportFragment fragment) {
        return fragment != null && fragment.onBackPressed();
    }

}
