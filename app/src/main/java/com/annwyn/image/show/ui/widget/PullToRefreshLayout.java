package com.annwyn.image.show.ui.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annwyn.image.show.R;
import com.annwyn.image.show.adapter.CommonAdapter;
import com.annwyn.image.show.utils.DisplayUtils;
import com.annwyn.image.show.utils.ParamUtils;

import java.util.List;

/**
 * 上拉刷新,下拉加载更多
 * Created by annwyn on 2016/7/16.
 */
public class PullToRefreshLayout extends SwipeRefreshLayout {

    /**
     * 刷新和加载监听事件
     */
    public interface OnPullToRefreshListener {

        void refresh();

        void loadMore();
    }

    private RecyclerView recyclerView;

    private OnPullToRefreshListener refreshListener;

    private boolean isLoading = false;

    private View footer;

    public PullToRefreshLayout(Context context) {
        this(context, null);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView();
    }

    private void initView() {
        LayoutInflater.from(this.getContext()).inflate(R.layout.layout_pull_to_refresh, this);
        this.recyclerView = (RecyclerView) this.findViewById(R.id.refresh_layout_table);
        this.recyclerView.addOnScrollListener(this.onScrollListener);
        this.setOnRefreshListener(this.swipeRefreshListener);
    }

    private View createFooter() {
        TextView textView = new TextView(this.getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new RecyclerView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        int padding = DisplayUtils.dp2px(this.getContext(), 10);
        textView.setPadding(padding, padding, padding, padding);
        textView.setText(getContext().getString(R.string.loading_msg));
        return textView;
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            // 当前未滚动,listener不为空,不在刷新状态
            if (newState == RecyclerView.SCROLL_STATE_IDLE && refreshListener != null && !isLoading) {
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                int lastVisibleItemPosition = this.getLastVisibleItemPosition(manager);
                // 没有item的情况下不能触发LoadMore
                if (manager.getChildCount() > 0 && lastVisibleItemPosition >= manager.getItemCount() - 1) {
                    if (footer != null)
                        footer.setVisibility(VISIBLE);

                    isLoading = true;
                    setRefreshing(true);
                    refreshListener.loadMore();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        }

        private int getLastVisibleItemPosition(RecyclerView.LayoutManager manager) {
            if (manager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) manager).findLastVisibleItemPosition();
            } else if (manager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager tmp = (StaggeredGridLayoutManager) manager;
                int[] into = new int[tmp.getSpanCount()];
                ((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions(into);
                return ParamUtils.findMax(into);
            } else {
                return -1;
            }
        }

    };

    private OnRefreshListener swipeRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (refreshListener != null && !isLoading) {
                isLoading = true;
                refreshListener.refresh();
            }
        }
    };

    public void setOnPullToRefreshListener(OnPullToRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public void setAdapter(RefreshAdapter<?> adapter) {
        adapter.registerAdapterDataObserver(this.observer);
        if (adapter.getFooterView() == null) { // 添加footer view
            adapter.setFooterView(this.createFooter());
        }
        this.footer = adapter.getFooterView();
        this.footer.setVisibility(GONE);
        this.recyclerView.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        this.recyclerView.setLayoutManager(manager);
    }

    /**
     * 该方法在完成加载更多之后需要调用
     * 如果adapter调用notifyDataSetChanged()之类的方法
     * 并且在该类的setAdapter中注册了AdapterDataObserver
     * 就不需要调用该方法,因为在AdapterDataObserver中会调用该方法隐藏footer和loading的标志位
     */
    public void loadComplete() {
        this.isLoading = false;
        if(this.footer != null) {
            this.footer.setVisibility(GONE);
        }
        this.setRefreshing(false);
    }

    public RecyclerView getRecyclerView(){
        return this.recyclerView;
    }

    public RecyclerView.Adapter getAdapter() {
        return this.recyclerView.getAdapter();
    }

    public boolean isLoading() {
        return this.isLoading;
    }

    public void startLoading() {
        this.isLoading = true;
        this.setRefreshing(true);
    }

    public abstract static class RefreshAdapter<T> extends CommonAdapter<T> {

        public static final int INVALID_TYPE = -1;

        private View footerView;

        public RefreshAdapter(Context context, List<T> data) {
            super(context, data);
        }

        @Override
        public int getItemCount() {
            return super.getItemCount() + (this.footerView == null ? 0 : 1);
        }

        @Override
        public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == INVALID_TYPE) {
                // 添加footer view
                StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                params.setFullSpan(true);
                this.footerView.setLayoutParams(params);
                return new CommonViewHolder(footerView);
            }
            return super.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(CommonViewHolder holder, int position) {
            if (position != this.getDataCount()) {
                // 不为footer view,绑定数据
                super.onBindViewHolder(holder, position);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == this.getDataCount()) {
                // 最后一个是footer view
                return INVALID_TYPE;
            }
            return super.getItemViewType(position);
        }

        public void setFooterView(View footerView) {
            this.footerView = footerView;
        }

        public View getFooterView() {
            return this.footerView;
        }

    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {

        public void onChanged() {
            this.updateView();
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            this.updateView();
        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            this.updateView();
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            this.updateView();
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            this.updateView();
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            this.updateView();
        }

        private void updateView() {
            loadComplete();
        }
    };

}
