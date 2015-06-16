package com.miglab.miyo.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/16.
 */
public class ResizeLayout extends RelativeLayout {
    private Context mContext;
    private int mMaxParentHeight = 0;
    private ArrayList<Integer> heightList = new ArrayList<Integer>();

    public ResizeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mMaxParentHeight == 0) {
            mMaxParentHeight = h;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureHeight = measureHeight(heightMeasureSpec);
        heightList.add(measureHeight);
        if (mMaxParentHeight != 0) {
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int expandSpec = MeasureSpec.makeMeasureSpec(mMaxParentHeight, heightMode);
            super.onMeasure(widthMeasureSpec, expandSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (heightList.size() >= 2) {
            int oldh = heightList.get(0);
            int newh = heightList.get(heightList.size() - 1);
            int softHeight = mMaxParentHeight - newh;
             /*
             newh         oldh
             500            max         -> ����                softKeyboard = max - 500
             600            500         -> ��С                softKeyboard = max - 600
             500            600         -> ����               softKeyboard = max - 500
             max           500         -> �ر�               softKeyboard = 0
             */
            /**
             * ���������
             */
            if (oldh == mMaxParentHeight) {
                if (mListener != null) {
                    mListener.OnSoftPop(softHeight);
                }
            }
            /**
             * ���������
             */
            else if (newh == mMaxParentHeight) {
                if (mListener != null) {
                    mListener.OnSoftClose(softHeight);
                }
            }
            /**
             * ��������̸߶�
             */
            else {
                if (mListener != null) {
                    mListener.OnSoftChanegHeight(softHeight);
                }
            }
            heightList.clear();
        }
        else {
            heightList.clear();
        }
    }

    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;
        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;
    }

    private OnResizeListener mListener;

    public void setOnResizeListener(OnResizeListener l) {
        mListener = l;
    }

    public interface OnResizeListener {

        /** ����̵��� */
        void OnSoftPop(int height);

        /** ����̹ر� */
        void OnSoftClose(int height);

        /** ����̸߶ȸı� */
        void OnSoftChanegHeight(int height);
    }
}
