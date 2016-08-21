package com.annwyn.image.show.ui.support;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * 居于AppBarLayout之下,滚动时隐藏自己
 */
public class BannerScrollBehavior extends AppBarLayout.ScrollingViewBehavior {

    private int skippedOffset;

    private boolean isScrolling;

    private int touchSlop;

    public BannerScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.touchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        if (nestedScrollAxes == ViewCompat.SCROLL_AXIS_HORIZONTAL)
            return false;
        this.isScrolling = false;
        this.skippedOffset = 0;
        return true;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (!isScrolling) {
            this.skippedOffset += dy;
            if (Math.abs(skippedOffset) >= touchSlop) { // 滑动了一定的距离才能算真正在滑动
                isScrolling = true;
                target.getParent().requestDisallowInterceptTouchEvent(true);
            }
        }

        if (isScrolling) {
            int currentOffset = getTopAndBottomOffset();
            // currentOffset - dy上一次滑动的距离(在-child.getHeight()和0之间)
            // -child.getHeight()指child完全隐藏掉,0指child完全显示出来
            int newOffset = Math.min(Math.max(-child.getHeight(), currentOffset - dy), 0);
            this.setTopAndBottomOffset(newOffset);
        }
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }


}
