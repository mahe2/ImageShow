package com.annwyn.image.show.ui.support;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * Created by annwyn on 2016/7/16.
 */
public abstract class SupportFragment extends Fragment {

    protected FragmentUtils utils;

    private int containerID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initializeArgs(this.getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(this.getView() == null) {
            return inflater.inflate(this.getContentLayout(), container, false);
        } else {
            return this.getView();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initializeView(this.getView());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SupportActivity) {
            this.utils = ((SupportActivity) context).initializeUtils();
        }
    }

	/**
	 * 初始化传入的参数
	 * 重写该方法时务必调用父类方法
	 * 在onCreate时调用
	 * @param bundle bundle
	 */
    protected void initializeArgs(Bundle bundle) {
	    // 获取根布局
        this.containerID = bundle.getInt(FragmentUtils.ARG_FRAGMENT_CONTAINER_ID);
    }

//	/**
//     * @see FragmentUtils#startFragment(FragmentManager, SupportFragment, SupportFragment)
//     * @param from 上一个fragment
//     * @param to 下一个fragment
//     */
//    protected void startFragment(SupportFragment from, SupportFragment to) {
//        this.utils.startFragment(this.getFragmentManager(), from, to);
//    }
//
//    /**
//     * @see FragmentUtils#startFragmentWithPop(FragmentManager, SupportFragment, SupportFragment)
//     * @param from 上一个fragment
//     * @param to 下一个fragment
//     */
//    protected void startFragmentWithPop(SupportFragment from, SupportFragment to) {
//        this.utils.startFragmentWithPop(this.getFragmentManager(), from, to);
//    }
//
//    /**
//     * @see FragmentUtils#startRootFragment(FragmentManager, int, SupportFragment)
//     * @param containerID 布局id
//     * @param to fragment
//     */
//    protected void startRootFragment(int containerID, SupportFragment to) {
//        this.utils.startRootFragment(this.getChildFragmentManager(), containerID, to);
//    }
//
//    protected void startChildFragment(SupportFragment from, SupportFragment to) {
//        this.utils.startFragment(this.getChildFragmentManager(), from, to);
//    }
//
//    protected void startChildFragmentWithPop(SupportFragment from, SupportFragment to) {
//        this.utils.startFragmentWithPop(this.getChildFragmentManager(), from, to);
//    }

	/**
	 * 获取fragment所在activity的布局ID
	 */
    protected int getContainerID() {
        return this.containerID;
    }

    /**
     * 管理fragment内部的回退事件
     * @return true 消费此次回退事件
     */
    protected boolean onBackPressed() {
        return false;
    }

    /**
     * 获取fragment的布局文件
     * @return int
     */
    protected abstract int getContentLayout();

    /**
     * 初始化view
     * @param view view
     */
    protected abstract void initializeView(View view);
}
