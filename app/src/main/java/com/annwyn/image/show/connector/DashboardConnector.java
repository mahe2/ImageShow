package com.annwyn.image.show.connector;

import com.annwyn.image.show.model.Dashboard;

import java.util.List;

public interface DashboardConnector extends BaseConnector {

    void loadDataComplete(List<Dashboard> dashboards);
}
