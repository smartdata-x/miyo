package com.miglab.miyo.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import com.miglab.miyo.adapter.EmoticonAdapter;
import com.miglab.miyo.entity.Emoticon;
import com.miglab.miyo.entity.EmoticonPageInfo;
import com.miglab.miyo.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/16.
 */
public class EmoticonViewPager extends ViewPager implements IView,IEmoticonKeyboard {
    private Context mContext;

    private int mHeight = 0;
    private int mMaxEmotionSetPageCount = 0;
    private int mOldPagePosition = -1;

    private List<EmoticonPageInfo> mEmoticonPageInfoList;
    private List<View> mEmoticonViews = new ArrayList<>();
    private EmoticonViewPagerAdapter mEmoticonsViewPagerAdapter;
    private OnEmoticonViewPagerListener mOnEmoticonViewPagerListener;
    private List<IView> mIViewListeners;

    public EmoticonViewPager(Context context) {
        this(context, null);
    }

    public EmoticonViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        EmoticonViewPager.this.post(new Runnable() {
            @Override
            public void run() {
                updateView();
            }
        });
    }

    public void addIViewListener(IView listener) {
        if (mIViewListeners == null) {
            mIViewListeners = new ArrayList<IView>();
        }
        mIViewListeners.add(listener);
    }

    public void setIViewListener(IView listener) {
        addIViewListener(listener);
    }

    private void updateView() {
        if (mEmoticonPageInfoList == null)
            return;
        if (mEmoticonsViewPagerAdapter == null) {
            mEmoticonsViewPagerAdapter = new EmoticonViewPagerAdapter();
            setAdapter(mEmoticonsViewPagerAdapter);
            setOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (mOldPagePosition < 0)
                        mOldPagePosition = 0;
                    int end = 0;
                    int pagerPosition = 0;
                    for (EmoticonPageInfo info : mEmoticonPageInfoList) {
                        int size = getPageCount(info);
                        if (end + size > position) {
                            if (mOnEmoticonViewPagerListener != null) {
                                mOnEmoticonViewPagerListener.emoticonsViewPagerCountChanged(size);
                            }
                            //上一页
                            if (mOldPagePosition - end >= size) {
                                if (position - end >= 0) {
                                    if (mOnEmoticonViewPagerListener != null) {
                                        mOnEmoticonViewPagerListener.playTo(position - end);
                                    }
                                }
                                if (mIViewListeners != null && !mIViewListeners.isEmpty()) {
                                    for (IView listener : mIViewListeners) {
                                        listener.onPageChangeTo(pagerPosition);
                                    }
                                }
                                break;
                            }

                            // 下一页
                            if (mOldPagePosition - end < 0) {
                                if (mOnEmoticonViewPagerListener != null) {
                                    mOnEmoticonViewPagerListener.playTo(0);
                                }
                                if (mIViewListeners != null && !mIViewListeners.isEmpty()) {
                                    for (IView listener : mIViewListeners) {
                                        listener.onPageChangeTo(pagerPosition);
                                    }
                                }
                                break;
                            }
                            // 本页切换
                            if (mOnEmoticonViewPagerListener != null) {
                                mOnEmoticonViewPagerListener.playBy(mOldPagePosition - end, position - end);
                            }
                            break;
                        }
                        pagerPosition++;
                        end += size;
                    }
                    mOldPagePosition = position;
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }

        int screenWidth = DisplayUtil.getDisplayWidthPixels(mContext);
        int maxPagerHeight = mHeight;

        mEmoticonViews.clear();
        mEmoticonsViewPagerAdapter.notifyDataSetChanged();

        for (EmoticonPageInfo info : mEmoticonPageInfoList) {
            List<Emoticon> emoticonList = info.getEmoticonList();
            if (emoticonList != null) {
                int sum = emoticonList.size();
                int row = info.getRow();
                int line = info.getLine();
                int del = info.isShowDelBtn() ? 1 : 0;
                int everyPageMaxNum = row * line - del;
                int pageCount = getPageCount(info);
                mMaxEmotionSetPageCount = Math.max(mMaxEmotionSetPageCount, pageCount);

                int start = 0;
                int end = everyPageMaxNum > sum ? sum : everyPageMaxNum;

                RelativeLayout.LayoutParams gridParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                gridParams.addRule(RelativeLayout.CENTER_VERTICAL);
                int itemHeight = Math.min(
                        (screenWidth - (info.getRow() - 1) * DisplayUtil.dp2px(mContext, info.getHorizontalSpacing())) / info.getRow(),
                        (maxPagerHeight - (info.getLine() - 1) * DisplayUtil.dp2px(mContext, info.getVerticalSpacing())) / info.getLine());
                for (int i = 0; i < pageCount; i++) {
                    RelativeLayout rl = new RelativeLayout(mContext);
                    GridView gridView = new GridView(mContext);
                    gridView.setMotionEventSplittingEnabled(false);
                    gridView.setNumColumns(info.getRow());
                    gridView.setBackgroundColor(Color.TRANSPARENT);
                    gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
                    gridView.setCacheColorHint(0);
                    gridView.setHorizontalSpacing(DisplayUtil.dp2px(mContext, info.getHorizontalSpacing()));
                    gridView.setVerticalSpacing(DisplayUtil.dp2px(mContext, info.getVerticalSpacing()));
                    gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                    gridView.setGravity(Gravity.CENTER);
                    gridView.setVerticalScrollBarEnabled(false);

                    List<Emoticon> list = new ArrayList<Emoticon>();
                    for (int j = start; j < end; j++) {
                        list.add(emoticonList.get(j));
                    }

                    // 删除按钮
                    if (info.isShowDelBtn()) {
                        int count = info.getLine() * info.getRow();
                        while (list.size() < count - 1) {
                            list.add(null);
                        }
                        list.add(new Emoticon(Emoticon.FACE_TYPE_DEL, "drawable://icon_del", null));
                    } else {
                        int count = info.getLine() * info.getRow();
                        while (list.size() < count) {
                            list.add(null);
                        }
                    }

                    EmoticonAdapter adapter = new EmoticonAdapter(mContext, list);
                    adapter.setHeight(itemHeight, DisplayUtil.dp2px(mContext, info.getItemPadding()));
                    gridView.setAdapter(adapter);
                    rl.addView(gridView, gridParams);
                    mEmoticonViews.add(rl);
                    adapter.setOnItemListener(this);

                    start = everyPageMaxNum + i * everyPageMaxNum;
                    end = everyPageMaxNum + (i + 1) * everyPageMaxNum;
                    if (end >= sum) {
                        end = sum;
                    }
                }
            }
        }
        mEmoticonsViewPagerAdapter.notifyDataSetChanged();

        if(mOnEmoticonViewPagerListener != null){
            mOnEmoticonViewPagerListener.emoticonViewPagerInitFinish(mMaxEmotionSetPageCount);
        }
    }

    public void setPageSelect(int position) {
        if (getAdapter() != null && position >= 0 && position < mEmoticonPageInfoList.size()) {
            int count = 0;
            for (int i = 0; i < position; i++) {
                count += getPageCount(mEmoticonPageInfoList.get(i));
            }
            setCurrentItem(count);
        }
    }

    public int getPageCount(EmoticonPageInfo info) {
        int pageCount = 0;
        if (info != null && info.getEmoticonList() != null) {
            int del = info.isShowDelBtn() ? 1 : 0;
            int everyPageMaxSum = info.getRow() * info.getLine() - del;
            pageCount = (int) Math.ceil((double) info.getEmoticonList().size() / everyPageMaxSum);
        }
        return pageCount;
    }

    @Override
    public void onItemClick(Emoticon emoticon) {
        if (mIViewListeners != null && !mIViewListeners.isEmpty()) {
            for (IView listener : mIViewListeners) {
                listener.onItemClick(emoticon);
            }
        }
    }

    @Override
    public void onItemDisplay(Emoticon emoticon) {

    }

    @Override
    public void onPageChangeTo(int position) {

    }

    @Override
    public void setBuilder(EmoticonKeyboardBuilder builder) {
        mEmoticonPageInfoList = builder.builder.getEmoticonSetBeanList();
    }

    private class EmoticonViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mEmoticonViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mEmoticonViews.get(position));
            return mEmoticonViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

    public void setOnIndicatorListener(OnEmoticonViewPagerListener listener) {
        mOnEmoticonViewPagerListener = listener;
    }

    public interface OnEmoticonViewPagerListener {
        void emoticonViewPagerInitFinish(int count);

        void emoticonsViewPagerCountChanged(int count);

        void playTo(int position);

        void playBy(int oldPosition, int newPosition);
    }
}
