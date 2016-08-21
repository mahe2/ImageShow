package com.annwyn.image.show.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.annwyn.image.show.R;
import com.annwyn.image.show.model.Special;
import com.annwyn.image.show.ui.widget.PullToRefreshLayout;
import com.annwyn.image.show.utils.ImageLoaderUtils;

import java.util.List;

public class SpecialAdapter extends PullToRefreshLayout.RefreshAdapter<Special> {

    public SpecialAdapter(Context context, List<Special> data) {
        super(context, data);
    }

    @Override
    public int getConvertLayout(int viewType) {
        return R.layout.adapter_special;
    }

    @Override
    public void convert(CommonViewHolder holder, Special special) {
        ImageLoaderUtils.getInstance().displayImage(special.getImage(), (ImageView) holder.getView(R.id.adapter_special_icon));
        holder.setText(R.id.adapter_special_name, special.getName());
        holder.setText(R.id.adapter_special_description, special.getDescription());
    }
}
