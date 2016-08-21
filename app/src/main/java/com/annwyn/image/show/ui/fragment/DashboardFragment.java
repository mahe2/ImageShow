package com.annwyn.image.show.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.annwyn.image.show.R;
import com.annwyn.image.show.adapter.CommonAdapter;
import com.annwyn.image.show.adapter.DashboardAdapter;
import com.annwyn.image.show.connector.DashboardConnector;
import com.annwyn.image.show.model.Dashboard;
import com.annwyn.image.show.presenter.DashboardPresenter;
import com.annwyn.image.show.presenter.impl.DashboardPresenterImpl;
import com.annwyn.image.show.ui.BaseActivity;

import java.util.List;

/**
 * 分类页面
 * Created by annwyn on 2016/7/17.
 */
public class DashboardFragment extends BaseFragment implements DashboardConnector {

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    private RecyclerView recyclerView;

    private DashboardPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter = new DashboardPresenterImpl(this.getContext(), this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_dashboard;
    }

    @Override
    protected void initializeView(View view) {
        this.initializeToolbar(R.string.nav_dashboard, true);

        this.recyclerView = (RecyclerView) view.findViewById(R.id.fragment_dashboard_table);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), BaseActivity.GRID_SPAN_COUNT));
        this.presenter.loadData();
    }

    @Override
    public void loadDataComplete(List<Dashboard> dashboards) {
        DashboardAdapter adapter = (DashboardAdapter) recyclerView.getAdapter();
        if(adapter == null) {
            adapter = new DashboardAdapter(this.getContext(), dashboards);
            adapter.setOnItemClickListener(this.itemClickListener);
            this.recyclerView.setAdapter(adapter);
        } else {
            adapter.update(dashboards);
            adapter.notifyDataSetChanged();
        }
    }

    private CommonAdapter.OnItemClickListener<Dashboard> itemClickListener = new CommonAdapter.OnItemClickListener<Dashboard>() {
        @Override
        public void onItemClick(ViewGroup parent, View v, int position, Dashboard dashboard) {
            activity.startListActivity(getContext(), dashboard, false);
        }
    };

}
