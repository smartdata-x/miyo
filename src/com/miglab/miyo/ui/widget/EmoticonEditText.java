package com.miglab.miyo.ui.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import com.miglab.miyo.entity.Emoticon;
import com.miglab.miyo.util.EmoticonUtil;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/16.
 */
public class EmoticonEditText extends EditText {

    private Context context;
    private int mItemHeight;
    private int mItemWidth;
    private int mFontHeight;

    private List<Emoticon> emoticonList = null;
    public EmoticonEditText(Context context) {
        super(context);
    }

    public EmoticonEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mFontHeight = getFontHeight();
        mItemHeight = mFontHeight;
        mItemWidth = mFontHeight;




    }

    public EmoticonEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        Log.e("text", ":" + text);
        Log.e("start",":" + start);
        Log.e("lengthBefore",":" + lengthBefore);
        Log.e("lengthAfter", ":" + lengthAfter);
        if(lengthAfter > 0) {
            int end = start + lengthAfter;
            String key = text.toString().substring(start, end);
            Pattern pattern = Pattern.compile("\\[\\\\\\d+\\]");
            Log.e("key", ":" + key);
            Matcher matcher = pattern.matcher(key);
            boolean b = matcher.find();
            Log.e("find", ":" + b);

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } catch (ArrayIndexOutOfBoundsException e) {
            setText(getText().toString());
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void setGravity(int gravity) {
        try {
            super.setGravity(gravity);
        } catch (ArrayIndexOutOfBoundsException e) {
            setText(getText().toString());
            super.setGravity(gravity);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        try {
            super.setText(text, type);
        } catch (ArrayIndexOutOfBoundsException e) {
            setText(text.toString());
        }
    }

    private int getFontHeight() {
        Paint paint = new Paint();
        paint.setTextSize(getTextSize());
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.bottom - fm.top);
    }

    public interface OnTextChangedInterface {
        void onTextChanged(CharSequence argo);
    }

    OnTextChangedInterface onTextChangedInterface;

    public void setOnTextChangedInterface(OnTextChangedInterface i) {
        onTextChangedInterface = i;
    }


    public interface OnSizeChangedListener {
        void onSizeChanged();
    }

    OnSizeChangedListener onSizeChangedListener;

    public void setOnSizeChangedListener(OnSizeChangedListener i) {
        onSizeChangedListener = i;
    }
}
