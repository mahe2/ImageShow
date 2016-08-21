package com.annwyn.image.show.ui.support;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.List;

public final class FragmentUtils {

    public static final String ARG_FRAGMENT_CONTAINER_ID = "argument_fragment_container_id";

    protected SupportFragment findStackFragment(Class<? extends SupportFragment> clazz, FragmentManager manager) {
        Fragment fragment = manager.findFragmentByTag(clazz.getName());
        return fragment == null ? null : (SupportFragment) fragment;
    }

    /**
     * 从栈顶开始查找当前正在显示的fragment(isVisible && getUserVisibleHint)
     *
     * @param manager FragmentManager
     * @return SupportFragment
     */
    protected SupportFragment findActiveFragment(FragmentManager manager) {
        List<Fragment> fragments = manager.getFragments();
        if (fragments == null)
            return null;
        for (int i = fragments.size() - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment instanceof SupportFragment) {
                if (fragment.isVisible() && fragment.getUserVisibleHint())  // 对当前用户可见
                    return (SupportFragment) fragment;
            }
        }
        return null;
    }

    /**
     * activity启动后加载对应的container的第一个fragment
     *
     * @param manager     FragmentManager
     * @param containerID 布局id
     * @param to          fragment
     */
    protected void startRootFragment(FragmentManager manager, int containerID, SupportFragment to) {
        this.bindContainerID(containerID, to);
        this.dispatcherFragment(manager, null, to, false);
    }

    /**
     * 开始下一个fragment,并将上一个fragment隐藏
     *
     * @param manager FragmentManager
     * @param from    上一个fragment
     * @param to      下一个fragment
     */
    protected void startFragment(FragmentManager manager, SupportFragment from, SupportFragment to) {
        if (from == null) {
            throw new IllegalArgumentException("from fragment can't be null");
        }
        this.dispatcherFragment(manager, from, to, false);
    }


    /**
     * 分发fragment事务
     *
     * @param manager FragmentManager
     * @param from    上一个fragment
     * @param to      下一个fragment
     * @param needPop 是否需要pop出栈
     */
    private void dispatcherFragment(FragmentManager manager, SupportFragment from, SupportFragment to, boolean needPop) {
        String name = to.getClass().getName();
        FragmentTransaction transaction = manager.beginTransaction();

        if (from != null) {
            this.bindContainerID(from.getContainerID(), to); // 绑定视图id
            if (to.isAdded()) {
                transaction.show(to);
                transaction.addToBackStack(name);
            } else {
                transaction.add(from.getContainerID(), to, name);
            }
            transaction.hide(from);
        } else {
            int containerID = to.getArguments().getInt(ARG_FRAGMENT_CONTAINER_ID);
            transaction.add(containerID, to, name);
        }
        transaction.commit();
    }

    /**
     * 为fragment绑定根布局ID
     * 需要在fragment中获取改ID,在fragment的onCreate中获取
     *
     * @param containerID 布局ID
     * @param to          fragment
     */
    private void bindContainerID(int containerID, SupportFragment to) {
        Bundle arguments = to.getArguments();
        if (arguments == null) {
            arguments = new Bundle();
            to.setArguments(arguments);
        }
        arguments.putInt(ARG_FRAGMENT_CONTAINER_ID, containerID);
    }
//
//    protected SupportFragment findTopFragment(FragmentManager manager) {
//        List<Fragment> fragments = manager.getFragments();
//        if(fragments == null)
//            return null;
//        for (int i = fragments.size() - 1; i >= 0; i--) {
//            Fragment fragment = fragments.get(i);
//            if(fragment instanceof SupportFragment) {
//                return (SupportFragment) fragment;
//            }
//        }
//        return null;
//    }
//

}
