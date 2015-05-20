package com.miglab.miyo.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.miglab.miyo.R;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/20.
 */
public class LoadingDialog extends Dialog {
    private ImageView imageView;
    private TextView textView;
    private String message;
    private Animation animation;
    public LoadingDialog(Context context) {
        this(context, R.style.LoadingDialogStyle);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_loading_dialog);
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.message);
        if(message == null || message.length() ==0) {
            textView.setVisibility(View.GONE);
        }else{
            textView.setVisibility(View.VISIBLE);
            textView.setText(message);
        }
        animation = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(2000);
        animation.setInterpolator(new LinearInterpolator());
        imageView.startAnimation(animation);
    }


    public void setMessage(String message){
        this.message = message;
    }

    @Override
    public void show() {
        super.show();
        if(animation != null && imageView != null){
            imageView.clearAnimation();
            imageView.startAnimation(animation);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(animation != null && imageView != null){
            imageView.clearAnimation();
        }
    }
}
