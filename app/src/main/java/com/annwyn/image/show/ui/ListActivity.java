package com.annwyn.image.show.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.annwyn.image.show.R;
import com.annwyn.image.show.adapter.CommonAdapter;
import com.annwyn.image.show.adapter.DetailAdapter;
import com.annwyn.image.show.connector.ListConnector;
import com.annwyn.image.show.model.Dashboard;
import com.annwyn.image.show.model.Detail;
import com.annwyn.image.show.model.Special;
import com.annwyn.image.show.presenter.ListPresenter;
import com.annwyn.image.show.presenter.impl.ListPresenterImpl;
import com.annwyn.image.show.ui.widget.PullToRefreshLayout;
import com.annwyn.image.show.utils.HttpUtils;
import com.annwyn.image.show.utils.ParamUtils;

import java.util.List;

/**
 *
 * Created by Administrator on 2016/7/19.
 */
public class ListActivity extends BaseActivity implements ListConnector {

    private String link;

    private int page;

    private ListPresenter presenter;

    private PullToRefreshLayout refreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_list);
        if(HttpUtils.getInstance().isConnect()) {
            this.presenter = new ListPresenterImpl(this, this);
            this.initView();
        } else {
            this.showError("", null);
        }
    }

    @Override
    public void showError(String msg, Exception e) {
        if (!HttpUtils.getInstance().isConnect()) {
            ParamUtils.showNetworkDialog(this, R.string.pre_page, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    finish();
                }
            }).show();
        }
    }

    private String initializeArgs() {
        Intent intent = this.getIntent();
        if (intent.getBooleanExtra(ARG_ACTIVITY_BEAN_SPECIAL, false)) {
            Special special = (Special) intent.getSerializableExtra(ARG_ACTIVITY_BEAN);
            this.link = special.getDetail();
            return special.getName();
        } else {
            Dashboard dashboard = (Dashboard) intent.getSerializableExtra(ARG_ACTIVITY_BEAN);
            this.link = dashboard.getLink();
            return dashboard.getName();
        }
    }

    private void initView() {
        this.initializeToolbar(this.initializeArgs(), false);

        this.refreshLayout = (PullToRefreshLayout) this.findViewById(R.id.activity_list_table);
        this.refreshLayout.setLayoutManager(new StaggeredGridLayoutManager(BaseActivity.GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL));
        this.refreshLayout.setOnPullToRefreshListener(this.onPullToRefreshListener);
        this.refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.refresh_1),
                ContextCompat.getColor(this, R.color.refresh_2),
                ContextCompat.getColor(this, R.color.refresh_3),
                ContextCompat.getColor(this, R.color.refresh_4));
        //noinspection ConstantConditions
        this.findViewById(R.id.activity_list_top).setOnClickListener(this.onClickListener);

        this.onPullToRefreshListener.refresh();
        ParamUtils.setProgressOffset(this, this.refreshLayout);
        this.refreshLayout.startLoading();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            refreshLayout.getRecyclerView().scrollToPosition(0);
        }
    };

    @Override
    public void loadDataComplete(List<Detail> details) {
        DetailAdapter adapter = (DetailAdapter) this.refreshLayout.getAdapter();
        if(adapter == null) {
            adapter = new DetailAdapter(this, details);
            adapter.setOnItemClickListener(this.onItemClickListener);
            this.refreshLayout.setAdapter(adapter);
            this.refreshLayout.loadComplete();
        } else if(page == 1) {
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
            presenter.loadData(link, 1);
        }

        @Override
        public void loadMore() {
            presenter.loadData(link, page + 1);
        }
    };

    private CommonAdapter.OnItemClickListener<Detail> onItemClickListener = new CommonAdapter.OnItemClickListener<Detail>() {
        @Override
        public void onItemClick(ViewGroup parent, View v, int position, Detail detail) {
            startDetailActivity(ListActivity.this, detail);
        }
    };

    @Override
    protected void onBackPressedSupport() {
        if(this.refreshLayout.isLoading()) {
            this.refreshLayout.loadComplete();
            return;
        }
        super.onBackPressedSupport();
    }

}
