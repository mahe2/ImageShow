package com.annwyn.image.show.ui.support;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.annwyn.image.show.R;
import com.annwyn.image.show.utils.DisplayUtils;

import java.util.List;

/**
 * 对PullToRefreshLayout重新定位,居于targetID之下
 */
public class RefreshLayoutBehavior extends CoordinatorLayout.Behavior<View> {

    private int tempTopBottomOffset = 0;

    private int offsetTop;

    private int layoutTop;

    private int targetID;

    public RefreshLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.refresh_layout);
        this.targetID = array.getResourceId(R.styleable.refresh_layout_target, -1);
        Log.i("targetID", "" + targetID);
        array.recycle();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        this.layoutTop = child.getTop();
        this.offsetTopAndBottom(child);

        if (this.tempTopBottomOffset!= 0) {
            this.setTopAndBottomOffset(child, this.tempTopBottomOffset);
            this.tempTopBottomOffset = 0;
        }

        return true;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        int countTopOffset = dependency.getTop() + dependency.getHeight();
        this.setTopAndBottomOffset(child, countTopOffset);
        return true;
    }

    public void setTopAndBottomOffset(View view, int offsetTop) {
        if(this.offsetTop != offsetTop) {
            this.offsetTop = offsetTop;
            this.offsetTopAndBottom(view);
        }
    }

    private void offsetTopAndBottom(View view) {
        ViewCompat.offsetTopAndBottom(view, this.offsetTop - view.getTop() - layoutTop);
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        if(child.getLayoutParams().height == ViewGroup.LayoutParams.MATCH_PARENT) {
            List<View> dependencies = parent.getDependencies(child); // 查询所有依赖PullToRefreshLayout的类
            View dependency = this.findDependency(dependencies);
            if (dependency != null && ViewCompat.isLaidOut(dependency)) {
                // 此处设计为往上滚动时dependency会慢慢消失,从而child会占满(dependency + child)高度
                // int height = parent.getHeight() - dependency.getTop(); 有时候会造成child的显示不全
                int height = parent.getHeight();
                int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
                parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
                return true;
            }
        }
        return false;
    }

    private View findDependency(List<View> views) {
        if(views.isEmpty())
            return null;
        for (View view : views) {
            if(view.getId() == targetID)
                return view;
        }
        return null;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency.getId() == targetID;
    }

}
