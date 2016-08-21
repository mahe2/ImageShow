package com.annwyn.image.show.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.annwyn.image.show.R;
import com.annwyn.image.show.model.Dashboard;
import com.annwyn.image.show.utils.ImageLoaderUtils;

import java.util.List;

public class DashboardAdapter extends CommonAdapter<Dashboard> {

    public DashboardAdapter(Context context, List<Dashboard> data) {
        super(context, data);
    }

    @Override
    public int getConvertLayout(int viewType) {
        return R.layout.adapter_dashboard;
    }

    @Override
    public void convert(CommonViewHolder holder, Dashboard dashboard) {
        holder.setText(R.id.adapter_dashboard_title, dashboard.getName());
        ImageLoaderUtils.getInstance().displayImage(dashboard.getIcon(),
                (ImageView) holder.getView(R.id.adapter_dashboard_background));
    }
}
