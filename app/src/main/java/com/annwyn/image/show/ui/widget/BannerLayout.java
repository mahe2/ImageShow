package com.annwyn.image.show.ui.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.annwyn.image.show.R;
import com.annwyn.image.show.ui.support.BannerScrollBehavior;
import com.annwyn.image.show.utils.DisplayUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 图片轮播控件
 * Created by annwyn on 2016/7/18.
 */
@CoordinatorLayout.DefaultBehavior(BannerScrollBehavior.class)
public class BannerLayout extends RelativeLayout {

    private ViewPager viewPager;

    private LinearLayout pointLayout; // 底部指示器

    private boolean canAutoLoop = false; // 是否开启自动翻页

    private boolean isAutoLoop = false; // 是否在自动循环(翻页的状态值)

    private long autoLoopTime = 5000; // 自动循环间隔

    /**
     * 长度为2的int数组
     * [0]为未选择状态
     * [1]为选中状态
     */
    @DrawableRes
    private int[] pointViewRes = new int[2];

    public BannerLayout(Context context) {
        this(context, null);
    }

    public BannerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        LayoutInflater.from(this.getContext()).inflate(R.layout.layout_banner, this, true);
        this.viewPager = (ViewPager) this.findViewById(R.id.banner_layout_pager);
        this.viewPager.setPageTransformer(true, new DepthPageTransformer()); // 修改ViewPager的翻页效果
        this.setViewPagerScroll();
        this.pointLayout = (LinearLayout) this.findViewById(R.id.banner_layout_point);
        this.pointViewRes[0] = R.drawable.ic_round_unselected;
        this.pointViewRes[1] = R.drawable.ic_round_selected;
    }

    public void setAdapter(LoopAdapter<?> adapter) {
        this.viewPager.setAdapter(adapter);
        this.initPoint(adapter.getData());
        LoopPageChangeListener listener = new LoopPageChangeListener(adapter);
        listener.onPageSelected(0); // 此处调用是需要设定圆圈选中状态, 可以在initPoint方法的循环中设置圆圈选中状态
        this.viewPager.addOnPageChangeListener(listener);
    }

    public PagerAdapter getAdapter() {
        return this.viewPager.getAdapter();
    }

    @SuppressWarnings("unused")
    public ViewPager getViewPager() {
        return this.viewPager;
    }

    private void initPoint(List<?> data) {
        for (int i = 0; i < data.size(); i++) {
            ImageView pointView = new ImageView(this.getContext());
            int padding = DisplayUtils.dp2px(this.getContext(), 5);
            pointView.setPadding(padding, 0, padding, 0);
            this.pointLayout.addView(pointView);
        }
    }

    @SuppressWarnings("unused")
    public void setPointViewRes(int[] imageRes) {
        if (imageRes == null || imageRes.length != 2) {
            throw new IllegalArgumentException("args's length isn't 2");
        }
        this.pointViewRes = imageRes;
    }

    public void startLoop() {
        if (this.isAutoLoop) {
            return;
        }
        this.canAutoLoop = true;
        this.isAutoLoop = true;
        this.postDelayed(this.loopTask, this.autoLoopTime);
    }

    public void stopLoop() {
        this.isAutoLoop = false;
        this.removeCallbacks(this.loopTask);
    }

    /**
     * 修改ViewPager的翻页速度
     */
    private void setViewPagerScroll() {
        //noinspection TryWithIdenticalCatches
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(viewPager, new ViewPagerScroller(this.getContext()));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        this.removeCallbacks(this.loopTask);
        this.loopTask = null;
        super.onDetachedFromWindow();
    }

    @SuppressWarnings("unused")
    public void setPointVisibility(boolean visibility) {
        this.pointLayout.setVisibility(visibility ? VISIBLE : GONE);
    }

    private Runnable loopTask = new Runnable() {
        @Override
        public void run() {
            // view pager不为空,并且自动翻页(判断翻页的状态值)
            if (viewPager != null && isAutoLoop) {
                int position = viewPager.getCurrentItem() + 1;
                viewPager.setCurrentItem(position);
                BannerLayout.this.postDelayed(this, autoLoopTime);
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE && canAutoLoop) {
            // 用户离开BannerLayout控件时,并且为自动翻页的情况下,开启自动翻页
            startLoop();
        } else if (action == MotionEvent.ACTION_DOWN && canAutoLoop) {
            // 用户操作BannerLayout控件时,并且为自动翻页的情况下, 停止自动翻页
            stopLoop();
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 翻页的listener, 主要用于每次翻页时修改圆点的选中和未选中状态
     */
    private class LoopPageChangeListener implements ViewPager.OnPageChangeListener {

        private LoopAdapter<?> adapter;

        private LoopPageChangeListener(LoopAdapter<?> adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            int realPosition = adapter.getRealPosition(position);

            for (int i = 0; i < adapter.getRealCount(); i++) {
                try {
                    ImageView imageView = (ImageView) pointLayout.getChildAt(i);
                    if (i == realPosition)
                        imageView.setImageResource(pointViewRes[1]);
                    else
                        imageView.setImageResource(pointViewRes[0]);

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    /**
     * ViewPager翻页速度
     */
    private class ViewPagerScroller extends Scroller {
        private int mScrollDuration = 1200;// 滑动速度,值越大滑动越慢，滑动太快会使3d效果不明显

        public ViewPagerScroller(Context context) {
            super(context);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }

    }

    /**
     * ViewPager翻页效果
     * http://developer.android.com/training/animation/screen-slide.html
     */
    private class DepthPageTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.75f;

        @Override
        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0);
            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                page.setAlpha(1);
                page.setTranslationX(0);
                page.setScaleX(1);
                page.setScaleY(1);
            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                page.setAlpha(1 - position);
                // Counteract the default slide transition
                page.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);
            }
        }
    }


    /**
     * ViewPager页面的holder
     */
    public static class PagerHolder {

        public View itemView;

        public SparseArray<View> array;

        public PagerHolder(View itemView) {
            this.itemView = itemView;
            this.array = new SparseArray<>();
        }

        @SuppressWarnings("unused")
        public View getConvertView() {
            return this.itemView;
        }

        @SuppressWarnings("unused")
        public View getView(@IdRes int idRes) {
            View view = this.array.get(idRes);
            if (view == null) {
                view = this.itemView.findViewById(idRes);
                this.array.put(idRes, view);
            }
            return view;
        }

    }

    public interface OnItemClickListener<T> {
        void itemClick(ViewGroup parent, View convert, int position, T t);
    }

    /**
     * LoopViewPager的adapter
     *
     * @param <T>
     */
    public static abstract class LoopAdapter<T> extends PagerAdapter {

        private LayoutInflater inflater;

        protected List<T> data;

        protected Context context;

        protected OnItemClickListener<T> onItemClickListener;

        public LoopAdapter(Context context, List<T> data) {
            this.data = data;
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        public List<T> getData() {
            return this.data;
        }

        @Override
        public int getCount() {
            return this.data == null ? 0 : Integer.MAX_VALUE;
        }

        public int getRealCount() {
            return this.data == null ? 0 : this.data.size();
        }

        public int getRealPosition(int position) {
            int count = this.getRealCount();
            return count == 0 ? 0 : position % this.getRealCount();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 判断翻页到最后一页时重新设定当前页码
         * (因为此处设置的页码为Integer.MAX_VALUE,不太可能会到最后一页,所以此处不重新设定边界值)
         *
         * @param container LoopViewPager
         */
        @Override
        public void finishUpdate(ViewGroup container) {
//            if(container instanceof LoopViewPager) {
//                LoopViewPager viewPager = (LoopViewPager) container;
//                int position = viewPager.getCurrentItem();
//                if(position == this.getCount() - 1) {
//                    position = this.getRealPosition(position);
//                    viewPager.setCurrentItem(position, false);
//                }
//            }
        }

        /**
         * ViewPager只会缓存上一个,当前和下一个view
         * 通过container.getChildAt(position)可以获取
         *
         * @param container LoopViewPager
         * @param position  position
         * @return convertView
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int realPosition = this.getRealPosition(position);
            View convertView = this.onCreateView(container, realPosition);
            this.bindListener(container, convertView, realPosition);
            return convertView;
        }


        @SuppressWarnings("unused")
        public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        private void bindListener(final ViewGroup parent, final View convert, final int position) {
            convert.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener == null) {
                        return;
                    }
                    T t = data.get(position);
                    onItemClickListener.itemClick(parent, convert, position, t);
                }
            });
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        protected View onCreateView(ViewGroup parent, int position) {
            View convertView = this.getConvertView(this.inflater, parent);
            if(convertView.getParent() == null)
                parent.addView(convertView);

            PagerHolder holder = new PagerHolder(convertView);
            this.convert(holder, this.data.get(position));
            return convertView;
        }

        /**
         * 记得将新建的view添加进ViewGroup中
         * 获取页面View
         *
         * @param inflater LayoutInflater
         * @param parent   ViewGroup
         * @return View
         */
        public abstract View getConvertView(LayoutInflater inflater, ViewGroup parent);

        public abstract void convert(PagerHolder holder, T t);

    }

}