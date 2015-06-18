package com.miglab.miyo.ui.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.miglab.miyo.R;
import com.miglab.miyo.entity.Emoticon;
import com.miglab.miyo.util.DisplayUtil;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/16.
 */
public class EmoticonGroup extends AutoHeightLayout implements View.OnClickListener,IEmoticonKeyboard{
    public static int FUNC_CHILLDVIEW_EMOTICON = 0;
    public int mChildViewPosition = -1;

    private EmoticonViewPager mEmoticonViewPager;
    private EmotionIndicatorView mEmoticonIndicatorView;
    private EmoticonBarView mEmoticonBarView;

    private EmoticonEditText et_chat;
    private RelativeLayout rl_input;
    private LinearLayout ly_foot_func;
    private ImageView btn_face;
    private Button btn_send;
    boolean isCloseEmoticon = true;

    private KeyBoardBarViewListener mKeyBoardBarViewListener;

    public EmoticonGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.ly_emoticon_grop, this);
        initView();
    }

    private void initView() {
        mEmoticonViewPager = (EmoticonViewPager) findViewById(R.id.view_epv);
        mEmoticonIndicatorView = (EmotionIndicatorView) findViewById(R.id.view_eiv);
        mEmoticonBarView = (EmoticonBarView) findViewById(R.id.view_etv);

        rl_input = (RelativeLayout) findViewById(R.id.rl_input);
        ly_foot_func = (LinearLayout) findViewById(R.id.ly_foot_func);
        btn_face = (ImageView) findViewById(R.id.btn_face);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_chat = (EmoticonEditText) findViewById(R.id.et_chat);
        setAutoHeightLayoutView(ly_foot_func);
        setListener();
    }

    private void setListener() {
        btn_face.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        mEmoticonViewPager.setOnIndicatorListener(new EmoticonViewPager.OnEmoticonViewPagerListener() {

            @Override
            public void emoticonViewPagerInitFinish(int count) {
                mEmoticonIndicatorView.init(count);
            }

            @Override
            public void emoticonsViewPagerCountChanged(int count) {
                mEmoticonIndicatorView.setIndicatorCount(count);
            }

            @Override
            public void playTo(int position) {
                mEmoticonIndicatorView.playTo(position);
            }

            @Override
            public void playBy(int oldPosition, int newPosition) {
                mEmoticonIndicatorView.playBy(oldPosition, newPosition);
            }
        });
        mEmoticonViewPager.setIViewListener(new IView() {
            @Override
            public void onItemClick(Emoticon emoticon) {
                if (et_chat != null) {
                    et_chat.setFocusable(true);
                    et_chat.setFocusableInTouchMode(true);
                    et_chat.requestFocus();

                    // 删除
                    if (emoticon.eventType == Emoticon.FACE_TYPE_DEL) {
                        int action = KeyEvent.ACTION_DOWN;
                        int code = KeyEvent.KEYCODE_DEL;
                        KeyEvent event = new KeyEvent(action, code);
                        et_chat.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                        return;
                    }
                    // 用户自定义
                    else if (emoticon.eventType == Emoticon.FACE_TYPE_USERDEF) {
                        return;
                    }

                    int index = et_chat.getSelectionStart();
                    Editable editable = et_chat.getEditableText();
                    if (index < 0) {
                        editable.append(emoticon.key);
                    } else {
                        editable.insert(index, emoticon.key);
                    }
                }
            }

            @Override
            public void onItemDisplay(Emoticon emoticon) {

            }

            @Override
            public void onPageChangeTo(int position) {
                mEmoticonBarView.setBarBtnSelect(position);
            }
        });

        mEmoticonBarView.setOnToolBarItemClickListener(new EmoticonBarView.OnEmoticonBarClickListener() {
            @Override
            public void onEmoticonBarItemClick(int position) {
                mEmoticonViewPager.setPageSelect(position);
            }
        });
        et_chat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!et_chat.isFocused()) {
                    et_chat.setFocusable(true);
                    et_chat.setFocusableInTouchMode(true);
                }
                return false;
            }
        });
        et_chat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    setEditableState(true);
                } else {
                    setEditableState(false);
                }
            }
        });
        et_chat.setOnSizeChangedListener(new EmoticonEditText.OnSizeChangedListener() {
            @Override
            public void onSizeChanged() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        if (mKeyBoardBarViewListener != null) {
                            mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState, -1);
                        }
                    }
                });
            }
        });
        et_chat.setOnTextChangedInterface(new EmoticonEditText.OnTextChangedInterface() {
            @Override
            public void onTextChanged(CharSequence arg0) {
                String str = arg0.toString();
                if (TextUtils.isEmpty(str)) {
                    btn_send.setBackgroundResource(R.drawable.btn_send_bg_disable);
                } else {
                    btn_send.setBackgroundResource(R.drawable.btn_send_bg);
                }
            }
        });
    }

    private void setEditableState(boolean b) {
        if (b) {
            et_chat.setFocusable(true);
            et_chat.setFocusableInTouchMode(true);
            et_chat.requestFocus();
            rl_input.setBackgroundResource(R.drawable.input_bg_green);
        } else {
            et_chat.setFocusable(false);
            et_chat.setFocusableInTouchMode(false);
            rl_input.setBackgroundResource(R.drawable.input_bg_gray);
        }
    }

    public void show(int position){
        int childCount = ly_foot_func.getChildCount();
        if(position < childCount){
            for(int i = 0 ; i < childCount ; i++){
                if(i == position){
                    ly_foot_func.getChildAt(i).setVisibility(VISIBLE);
                    mChildViewPosition  = i;
                } else{
                    ly_foot_func.getChildAt(i).setVisibility(GONE);
                }
            }
        }
        post(new Runnable() {
            @Override
            public void run() {
                if (mKeyBoardBarViewListener != null) {
                    mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState, -1);
                }
            }
        });
    }

    public void add(View view){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        ly_foot_func.addView(view, params);
    }

    public void addBarView(int icon){
        if(mEmoticonBarView != null && icon > 0){
            mEmoticonBarView.addData(icon);
        }
    }

    public void addFixedView(View view , boolean isRight){
        if(mEmoticonBarView != null){
            mEmoticonBarView.addFixedView(view, isRight);
        }
    }

    public void clearEditText(){
        if(et_chat != null){
            et_chat.setText("");
        }
    }

    public void del(){
        if(et_chat != null){
            int action = KeyEvent.ACTION_DOWN;
            int code = KeyEvent.KEYCODE_DEL;
            KeyEvent event = new KeyEvent(action, code);
            et_chat.onKeyDown(KeyEvent.KEYCODE_DEL, event);
        }
    }



    public EmoticonViewPager getmEmoticonViewPager() {
        return mEmoticonViewPager;
    }

    public EmotionIndicatorView getmEmoticonIndicatorView() {
        return mEmoticonIndicatorView;
    }

    public EmoticonBarView getmEmoticonBarView() {
        return mEmoticonBarView;
    }

    public EmoticonEditText getEt_chat() {
        return et_chat;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (ly_foot_func != null && ly_foot_func.isShown()) {
                    hideAutoView();
                    btn_face.setImageResource(R.drawable.icon_face_nomal);
                    return true;
                } else {
                    return super.dispatchKeyEvent(event);
                }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_face) {
            switch (mKeyboardState){
                case EMOTICON_STATE_NONE:
                case EMOTICON_STATE_BOTH:
                    show(FUNC_CHILLDVIEW_EMOTICON);
                    btn_face.setImageResource(R.drawable.icon_face_pop);
                    showAutoView();
                    isCloseEmoticon = false;
                    DisplayUtil.closeSoftKeyboard(mContext);
                    break;
                case EMOTICON_STATE_FUNC:
                    if(mChildViewPosition == FUNC_CHILLDVIEW_EMOTICON){
                        btn_face.setImageResource(R.drawable.icon_face_nomal);
                        setEditableState(true);
                        DisplayUtil.openSoftKeyboard(et_chat);
                        mKeyboardState = EMOTICON_STATE_BOTH;
                    }
                    else {
                        show(FUNC_CHILLDVIEW_EMOTICON);
                        btn_face.setImageResource(R.drawable.icon_face_pop);
                    }
                    break;
            }
        }
        else if (id == R.id.btn_send) {
            if(mKeyBoardBarViewListener != null){
                mKeyBoardBarViewListener.OnSendBtnClick(et_chat.getText().toString());
            }
        }
    }

    @Override
    public void OnSoftPop(final int height) {
        super.OnSoftPop(height);
        post(new Runnable() {
            @Override
            public void run() {
                btn_face.setImageResource(R.drawable.icon_face_nomal);
                if (mKeyBoardBarViewListener != null) {
                    mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState, height);
                }
            }
        });
    }

    @Override
    public void OnSoftClose(int height) {
        super.OnSoftClose(height);
        if(!isCloseEmoticon)
            isCloseEmoticon = true;
        else {
            hideAutoView();
            mKeyboardState = EMOTICON_STATE_NONE;
        }
        if(mKeyBoardBarViewListener != null){
            mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState,height);
        }
    }

    @Override
    public void OnSoftChanegHeight(int height) {
        super.OnSoftChanegHeight(height);
        if(mKeyBoardBarViewListener != null){
            mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState, height);
        }
    }

    public void setOnKeyBoardBarViewListener(KeyBoardBarViewListener l) { this.mKeyBoardBarViewListener = l; }

    @Override
    public void setBuilder(EmoticonKeyboardBuilder builder) {
        mEmoticonViewPager.setBuilder(builder);
        mEmoticonBarView.setBuilder(builder);
    }

    public interface KeyBoardBarViewListener {
        void OnKeyBoardStateChange(int state, int height);
        void OnSendBtnClick(String msg);
    }


}
