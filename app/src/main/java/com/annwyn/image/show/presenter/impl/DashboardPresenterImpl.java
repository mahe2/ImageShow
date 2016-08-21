package com.annwyn.image.show.presenter.impl;

import android.content.Context;

import com.annwyn.image.show.connector.DashboardConnector;
import com.annwyn.image.show.dao.DashboardDao;
import com.annwyn.image.show.model.Dashboard;
import com.annwyn.image.show.presenter.DashboardPresenter;

import java.util.List;

public class DashboardPresenterImpl implements DashboardPresenter {

    private DashboardConnector connector;

    private DashboardDao dashboardDao;

    public DashboardPresenterImpl(Context context, DashboardConnector connector) {
        this.connector = connector;
        this.dashboardDao = new DashboardDao(context);
    }

    @Override
    public void loadData() {
        List<Dashboard> dashboards = this.dashboardDao.selectAll();
        this.connector.loadDataComplete(dashboards);
    }
}
