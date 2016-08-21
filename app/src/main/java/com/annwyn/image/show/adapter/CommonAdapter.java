package com.annwyn.image.show.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * 通用adapter
 * Created by annwyn on 2016/7/16.
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonAdapter.CommonViewHolder> {

    protected List<T> data;

    protected OnItemClickListener<T> itemClickListener;

    protected LayoutInflater inflater;

    public CommonAdapter(Context context, List<T> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = this.getConvertLayout(this.inflater, parent, viewType);
        CommonViewHolder holder = new CommonViewHolder(itemView);
        this.bindItemListener(parent, holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        this.convert(holder, this.data.get(position));
    }

    protected void bindItemListener(final ViewGroup parent, final CommonViewHolder holder, int viewType) {
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    int position = holder.getAdapterPosition();
                    itemClickListener.onItemClick(parent, view, position, data.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.getDataCount();
    }

    public int getDataCount() {
        return this.data == null ? 0 : this.data.size();
    }

    public List<T> getData() {
        return this.data;
    }

    public void refresh(List<T> list) {
        this.data = list;
        this.notifyDataSetChanged();
    }

    public void add(List<T> list) {
        this.data.addAll(list);
    }

    public void clear() {
        this.data.clear();
    }

    public void update(List<T> list) {
        this.data = list;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }

    public View getConvertLayout(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(this.getConvertLayout(viewType), parent, false);
    }

    public abstract int getConvertLayout(int viewType);

    public abstract void convert(CommonViewHolder holder, T t);

    public static class CommonViewHolder extends RecyclerView.ViewHolder {

        SparseArray<View> views;

        public CommonViewHolder(View itemView) {
            super(itemView);
            this.views = new SparseArray<>();
        }

        public View getConvertView() {
            return this.itemView;
        }

        public View getView(int idRes) {
            View view = this.views.get(idRes);
            if (view == null) {
                view = this.itemView.findViewById(idRes);
                this.views.put(idRes, view);
            }
            return view;
        }

        public CommonViewHolder setText(int idRes, String text) {
            TextView textView = (TextView) this.getView(idRes);
            textView.setText(text);
            return this;
        }

        public CommonViewHolder setImageResource(int idRes, int imgRes) {
            ImageView textView = (ImageView) this.getView(idRes);
            textView.setImageResource(imgRes);
            return this;
        }

        public CommonViewHolder setOnClickListener(int idRes, View.OnClickListener onClickListener) {
            View view = this.getView(idRes);
            view.setOnClickListener(onClickListener);
            return this;
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(ViewGroup parent, View v, int position, T t);
    }

}
