package com.annwyn.image.show.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.annwyn.image.show.R;
import com.annwyn.image.show.model.Special;
import com.annwyn.image.show.ui.widget.BannerLayout;
import com.annwyn.image.show.utils.ImageLoaderUtils;

import java.util.List;

public class BannerAdapter extends BannerLayout.LoopAdapter<Special> {

    public BannerAdapter(Context context, List<Special> data) {
        super(context, data);
    }

    @Override
    public View getConvertView(LayoutInflater inflater, ViewGroup parent) {
//        ImageView imageView = new ImageView(parent.getContext());
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        imageView.setLayoutParams(params);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        return imageView;
        View view = inflater.inflate(R.layout.adapter_banner, parent, false);
        parent.addView(view);
        return view;
    }

    @Override
    public void convert(BannerLayout.PagerHolder holder, Special object) {
        ImageLoaderUtils.getInstance().displayImage(object.getImage(), (ImageView) holder.getConvertView());
    }
}
