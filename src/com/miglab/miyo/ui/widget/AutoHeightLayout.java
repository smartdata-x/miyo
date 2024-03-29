package com.miglab.miyo.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.miglab.miyo.util.DisplayUtil;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/16.
 */
public class AutoHeightLayout extends ResizeLayout implements ResizeLayout.OnResizeListener {
    private static final int ID_CHILD = 1;
    public static final int EMOTICON_STATE_NONE = 100;
    public static final int EMOTICON_STATE_FUNC = 102;
    public static final int EMOTICON_STATE_BOTH = 103;

    protected Context mContext;
    protected int mAutoHeightLayoutId;
    protected int mAutoViewHeight;
    protected View mAutoHeightLayoutView;
    protected int mKeyboardState = EMOTICON_STATE_NONE;

    public AutoHeightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mAutoViewHeight = DisplayUtil.getDefKeyboardHeight(mContext);
        setOnResizeListener(this);
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        int childSum = getChildCount();
        if (getChildCount() > 1) {
            throw new IllegalStateException("can host only one direct child");
        }
        super.addView(child, index, params);

        if (childSum == 0) {
            mAutoHeightLayoutId = child.getId();
            if (mAutoHeightLayoutId < 0) {
                child.setId(ID_CHILD);
                mAutoHeightLayoutId = ID_CHILD;
            }
            LayoutParams paramsChild = (LayoutParams) child.getLayoutParams();
            paramsChild.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            child.setLayoutParams(paramsChild);
        } else if (childSum == 1) {
            LayoutParams paramsChild = (LayoutParams) child.getLayoutParams();
            paramsChild.addRule(RelativeLayout.ABOVE, mAutoHeightLayoutId);
            child.setLayoutParams(paramsChild);
        }
    }

    public void setAutoHeightLayoutView(View view) {
        mAutoHeightLayoutView = view;
    }

    public void setAutoViewHeight(final int height) {
        int heightDp = DisplayUtil.px2dp(mContext, height);
        if (heightDp > 0 && heightDp != mAutoViewHeight) {
            mAutoViewHeight = heightDp;
            DisplayUtil.setDefKeyboardHeight(mContext, mAutoViewHeight);
        }

        if (mAutoHeightLayoutView != null) {
            mAutoHeightLayoutView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mAutoHeightLayoutView.getLayoutParams();
            params.height = height;
            mAutoHeightLayoutView.setLayoutParams(params);
        }
    }

    public void hideAutoView(){
        this.post(new Runnable() {
            @Override
            public void run() {
                DisplayUtil.closeSoftKeyboard(mContext);
                setAutoViewHeight(0);
                if (mAutoHeightLayoutView != null) {
                    mAutoHeightLayoutView.setVisibility(View.GONE);
                }
            }
        });
        mKeyboardState = EMOTICON_STATE_NONE;
    }

    public void showAutoView(){
        if (mAutoHeightLayoutView != null) {
            mAutoHeightLayoutView.setVisibility(VISIBLE);
            setAutoViewHeight(DisplayUtil.dp2px(mContext, mAutoViewHeight));
        }
        mKeyboardState = mKeyboardState == EMOTICON_STATE_NONE ? EMOTICON_STATE_FUNC : EMOTICON_STATE_BOTH;
    }

    @Override
    public void OnSoftPop(final int height) {
        mKeyboardState = EMOTICON_STATE_BOTH;
        post(new Runnable() {
            @Override
            public void run() {
                setAutoViewHeight(height);
            }
        });
    }

    @Override
    public void OnSoftClose(int height) {
        mKeyboardState = mKeyboardState == EMOTICON_STATE_BOTH ? EMOTICON_STATE_FUNC : EMOTICON_STATE_NONE;
    }

    @Override
    public void OnSoftChanegHeight(final int height) {
        post(new Runnable() {
            @Override
            public void run() {
                setAutoViewHeight(height);
            }
        });
    }
}
