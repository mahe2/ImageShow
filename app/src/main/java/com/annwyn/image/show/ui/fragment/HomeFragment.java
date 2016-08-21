package com.annwyn.image.show.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.annwyn.image.show.R;
import com.annwyn.image.show.adapter.BannerAdapter;
import com.annwyn.image.show.adapter.CommonAdapter;
import com.annwyn.image.show.adapter.DetailAdapter;
import com.annwyn.image.show.connector.HomeConnector;
import com.annwyn.image.show.model.Detail;
import com.annwyn.image.show.model.Special;
import com.annwyn.image.show.presenter.HomePresenter;
import com.annwyn.image.show.presenter.impl.HomePresenterImpl;
import com.annwyn.image.show.ui.widget.BannerLayout;
import com.annwyn.image.show.ui.widget.PullToRefreshLayout;
import com.annwyn.image.show.utils.ParamUtils;

import java.util.List;

/**
 *
 * Created by annwyn on 2016/7/16.
 */
public class HomeFragment extends BaseFragment implements HomeConnector {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private HomePresenter presenter;

    private int page = 1;

    private PullToRefreshLayout refreshLayout;

    private BannerLayout bannerLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter = new HomePresenterImpl(this.getContext(), this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initializeView(View view) {
        this.initializeToolbar(R.string.app_name, true);

        this.bannerLayout = (BannerLayout) view.findViewById(R.id.fragment_home_banner_layout);
        this.refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.fragment_home_refresh_layout);
        this.refreshLayout.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        this.refreshLayout.setOnPullToRefreshListener(this.onPullToRefreshListener);
        this.refreshLayout.setColorSchemeColors(ContextCompat.getColor(this.getContext(), R.color.refresh_1),
                ContextCompat.getColor(this.getContext(), R.color.refresh_2),
                ContextCompat.getColor(this.getContext(), R.color.refresh_3),
                ContextCompat.getColor(this.getContext(), R.color.refresh_4));

        this.onPullToRefreshListener.refresh();
        ParamUtils.setProgressOffset(this.getContext(), this.refreshLayout);
        this.refreshLayout.startLoading();
    }

    @Override
    public void initializeBanner(List<Special> specials) {
        if(this.bannerLayout.getAdapter() != null) // 只会加载一次
            return;
        BannerAdapter adapter = new BannerAdapter(this.getContext(), specials);
        adapter.setOnItemClickListener(this.bannerItemClick);
        this.bannerLayout.setAdapter(adapter);
        this.bannerLayout.startLoop();
    }

    @Override
    public void loadDataComplete(List<Detail> details) {
        DetailAdapter adapter = (DetailAdapter) this.refreshLayout.getAdapter();
        if (adapter == null) {
            adapter = new DetailAdapter(this.getContext(), details);
            adapter.setOnItemClickListener(this.tableItemClick);
            this.refreshLayout.setAdapter(adapter);
            this.refreshLayout.loadComplete();
        } else if (this.page == 1) {
            adapter.update(details);
            adapter.notifyDataSetChanged();
        } else {
            int start = adapter.getDataCount();
            adapter.add(details);
            adapter.notifyItemRangeInserted(start, details.size());
        }
    }

    @Override
    public void setPageNumber(int pageNumber) {
        this.page = pageNumber;
    }

    @Override
    public void loadComplete() {
        if(this.refreshLayout != null && this.refreshLayout.isLoading()) {
            this.refreshLayout.loadComplete();
        }
    }

    private PullToRefreshLayout.OnPullToRefreshListener onPullToRefreshListener = new PullToRefreshLayout.OnPullToRefreshListener() {
        @Override
        public void refresh() {
            presenter.loadData(1);
        }

        @Override
        public void loadMore() {
            presenter.loadData(page + 1);
        }
    };

    private BannerLayout.OnItemClickListener<Special> bannerItemClick = new BannerLayout.OnItemClickListener<Special>() {

        @Override
        public void itemClick(ViewGroup parent, View convert, int position, Special special) {
            activity.startListActivity(getContext(), special, true);
        }
    };

    private CommonAdapter.OnItemClickListener<Detail> tableItemClick = new CommonAdapter.OnItemClickListener<Detail>() {

        @Override
        public void onItemClick(ViewGroup parent, View v, int position, Detail detail) {
            activity.startDetailActivity(getContext(), detail);
        }
    };

    @Override
    protected boolean onBackPressed() {
        if(this.refreshLayout != null && this.refreshLayout.isLoading()) {
            this.refreshLayout.loadComplete();
            return true;
        }
        return false;
    }
}
