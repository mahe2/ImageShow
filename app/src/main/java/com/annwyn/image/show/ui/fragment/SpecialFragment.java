package com.annwyn.image.show.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.annwyn.image.show.R;
import com.annwyn.image.show.adapter.CommonAdapter;
import com.annwyn.image.show.adapter.SpecialAdapter;
import com.annwyn.image.show.connector.SpecialConnector;
import com.annwyn.image.show.model.Special;
import com.annwyn.image.show.presenter.SpecialPresenter;
import com.annwyn.image.show.presenter.impl.SpecialPresenterImpl;
import com.annwyn.image.show.ui.widget.PullToRefreshLayout;
import com.annwyn.image.show.utils.ParamUtils;

import java.util.List;

/**
 * 专题页面
 * Created by annwyn on 2016/7/17.
 */
public class SpecialFragment extends BaseFragment implements SpecialConnector {

    public static SpecialFragment newInstance() {
        return new SpecialFragment();
    }

    private PullToRefreshLayout refreshLayout;

    private SpecialPresenter presenter;

    private int page = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter = new SpecialPresenterImpl(this.getContext(), this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_special;
    }

    @Override
    protected void initializeView(View view) {
        this.initializeToolbar(R.string.nav_special, true);

        this.refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.fragment_special_table);
        this.refreshLayout.setLayoutManager(new LinearLayoutManager(this.getContext()));
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
    public void loadDataComplete(List<Special> specials) {
        SpecialAdapter adapter = (SpecialAdapter) this.refreshLayout.getAdapter();
        if(adapter == null) {
            adapter = new SpecialAdapter(this.getContext(), specials);
            adapter.setOnItemClickListener(this.onItemClickListener);
            this.refreshLayout.setAdapter(adapter);
            this.refreshLayout.loadComplete();
        } else if(page == 1) {
            adapter.update(specials);
            adapter.notifyDataSetChanged();
        } else {
            int start = adapter.getDataCount();
            adapter.add(specials);
            adapter.notifyItemRangeInserted(start, specials.size());
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

    private CommonAdapter.OnItemClickListener<Special> onItemClickListener = new CommonAdapter.OnItemClickListener<Special>() {
        @Override
        public void onItemClick(ViewGroup parent, View v, int position, Special special) {
            activity.startListActivity(getContext(), special, true);
        }
    };

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

    @Override
    protected boolean onBackPressed() {
        if(this.refreshLayout.isLoading()) { // 正在loading数据
            this.refreshLayout.loadComplete();
            return true;
        }
        return super.onBackPressed();
    }
}
