package com.annwyn.image.show.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.annwyn.image.show.R;
import com.annwyn.image.show.model.Detail;
import com.annwyn.image.show.ui.widget.PullToRefreshLayout;
import com.annwyn.image.show.utils.ImageLoaderUtils;

import java.util.List;

public class DetailAdapter extends PullToRefreshLayout.RefreshAdapter<Detail> {

    public DetailAdapter(Context context, List<Detail> data) {
        super(context, data);
    }

    @Override
    public int getConvertLayout(int viewType) {
        return R.layout.adapter_detail;
    }

    @Override
    public void convert(CommonViewHolder holder, Detail detail) {
        ImageLoaderUtils.getInstance().displayImage(detail.getSmall(), (ImageView) holder.getView(R.id.adapter_detail_image));
    }
}
