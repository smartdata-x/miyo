package com.miglab.miyo.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.miglab.miyo.R;
import com.miglab.miyo.entity.EmoticonPageInfo;
import com.miglab.miyo.third.universalimageloader.core.ImageLoader;
import com.miglab.miyo.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/16.
 */
public class EmoticonBarView extends RelativeLayout implements IEmoticonKeyboard {
    private LayoutInflater inflater;
    private Context mContext;
    private HorizontalScrollView hsv_toolbar;
    private LinearLayout ly_tool;

    private List<EmoticonPageInfo> mEmoticonPageInfoList;
    private ArrayList<ImageView> mBarBtnList = new ArrayList<ImageView>();
    private List<OnEmoticonBarClickListener> mItemClickListeners;
    private int mBtnWidth = 60;

    public EmoticonBarView(Context context) {
        this(context, null);
    }

    public EmoticonBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.ly_emoticon_menu, this);
        this.mContext = context;
        findView();
    }

    private void findView() {
        hsv_toolbar = (HorizontalScrollView) findViewById(R.id.hsv_toolbar);
        ly_tool = (LinearLayout) findViewById(R.id.ly_tool);
    }

    @Override
    public void setBuilder(EmoticonKeyboardBuilder builder) {
        mEmoticonPageInfoList = builder.builder == null ? null : builder.builder.getEmoticonSetBeanList();
        if(mEmoticonPageInfoList == null){
            return;
        }

        int i = 0;
        for(EmoticonPageInfo bean : mEmoticonPageInfoList){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View toolBtnView = inflater.inflate(R.layout.ly_emoticon_bar_item,null);
            View v_spit = (View)toolBtnView.findViewById(R.id.v_spit);
            ImageView iv_icon = (ImageView)toolBtnView.findViewById(R.id.iv_icon);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(DisplayUtil.dp2px(mContext, mBtnWidth), LayoutParams.MATCH_PARENT);
            iv_icon.setLayoutParams(imgParams);
            ly_tool.addView(toolBtnView);


            ImageLoader.getInstance().displayImage(bean.getIconUri(),iv_icon);

            mBarBtnList.add(iv_icon);

            final int finalI = i;
            iv_icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListeners != null && !mItemClickListeners.isEmpty()) {
                        for (OnEmoticonBarClickListener listener : mItemClickListeners) {
                            listener.onEmoticonBarItemClick(finalI);
                        }
                    }
                }
            });
            i++;
        }
        setBarBtnSelect(0);
    }

    public void setBarBtnSelect(int select) {
        scrollToBtnPosition(select);
        for (int i = 0; i < mBarBtnList.size(); i++) {
            if (select == i) {
                mBarBtnList.get(i).setBackgroundColor(getResources().getColor(R.color.emoticon_bar_btn_select));
            } else {
                mBarBtnList.get(i).setBackgroundColor(getResources().getColor(R.color.emoticon_bar_btn_nomal));
            }
        }
    }

    private void scrollToBtnPosition(final int position){
        int childCount = ly_tool.getChildCount();
        if(position < childCount){
            hsv_toolbar.post(new Runnable() {
                @Override
                public void run() {
                    int mScrollX = hsv_toolbar.getScrollX();

                    int childX = (ly_tool.getChildAt(position)).getScrollX();

                    if(childX < mScrollX){
                        hsv_toolbar.scrollTo(childX,0);
                        return;
                    }

                    int childWidth = (int)ly_tool.getChildAt(position).getWidth();
                    int hsvWidth = hsv_toolbar.getWidth();
                    int childRight = childX + childWidth;
                    int scrollRight = mScrollX + hsvWidth;

                    if(childRight > scrollRight){
                        hsv_toolbar.scrollTo(childRight - scrollRight,0);
                        return;
                    }
                }
            });
        }
    }

    public void addData(int rec){
        if(ly_tool != null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View toolBtnView = inflater.inflate(R.layout.ly_emoticon_bar_item,null);
            ImageView iv_icon = (ImageView)toolBtnView.findViewById(R.id.iv_icon);
            iv_icon.setImageResource(rec);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(DisplayUtil.dp2px(mContext, mBtnWidth), LayoutParams.MATCH_PARENT);
            iv_icon.setLayoutParams(imgParams);
            ly_tool.addView(toolBtnView);
            final int position = mBarBtnList.size();
            mBarBtnList.add(iv_icon);
            iv_icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListeners != null && !mItemClickListeners.isEmpty()) {
                        for (OnEmoticonBarClickListener listener : mItemClickListeners) {
                            listener.onEmoticonBarItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public void addFixedView(View view, boolean isRight) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        LayoutParams hsvParams = (LayoutParams) hsv_toolbar.getLayoutParams();
        if(view.getId() <= 0){
            view.setId(getIdValue());
        }
        if(isRight){
            params.addRule(ALIGN_PARENT_RIGHT);
            hsvParams.addRule(LEFT_OF, view.getId());
        }
        else{
            params.addRule(ALIGN_PARENT_LEFT);
            hsvParams.addRule(RIGHT_OF,view.getId());
        }
        addView(view,params);
        hsv_toolbar.setLayoutParams(hsvParams);
    }

    private int getIdValue(){
        int childCount = getChildCount();
        int id = 1;
        if(childCount == 0){
            return id;
        }
        boolean isKeep = true;
        while (isKeep){
            isKeep = false;
            Random random = new Random();
            id = random.nextInt(100);
            for(int i = 0 ; i < childCount ; i++){
                if(getChildAt(i).getId() == id){
                    isKeep = true;
                    break;
                }
            }
        }
        return id;
    }

    public interface OnEmoticonBarClickListener {
        void onEmoticonBarItemClick(int position);
    }
    public void addOnToolBarItemClickListener(OnEmoticonBarClickListener listener) {
        if (mItemClickListeners == null) {
            mItemClickListeners = new ArrayList<OnEmoticonBarClickListener>();
        }
        mItemClickListeners.add(listener);
    }
    public void setOnToolBarItemClickListener(OnEmoticonBarClickListener listener) { addOnToolBarItemClickListener(listener);}

}
