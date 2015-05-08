package com.miglab.miyo.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.miglab.miyo.R;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class RoundProgressBar extends View{

	private Paint paint; //画笔对象
	private int roundColor; //圆环颜色
	private int roundProgressColor; //圆环进度的颜色
	private int textColor; //中间百分比的字符颜色
	private float textSize; //中间进度百分比的字符串字体
	private float roundwidth; //圆环的宽度
	private int max; //最大进度
	private int progress; //当前进度
	private boolean textIsDisplayable; //是否显示中间的进度
	private int style; //进度的风格，实心或者空心
	public static final int STROKE = 0; //空心
	public static final int FILL = 1; //实心
	

	public RoundProgressBar(Context context) {
		this(context, null);
	}
	
	public RoundProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		paint = new Paint();
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.roundProgressBar);
		//获取自定义属性和默认值
		//roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor);
		roundColor = mTypedArray.getColor(R.styleable.roundProgressBar_roundColor, Color.RED);
		roundProgressColor = mTypedArray.getColor(R.styleable.roundProgressBar_roundProgressColor, Color.GREEN);
		textColor = mTypedArray.getColor(R.styleable.roundProgressBar_textColor, Color.WHITE);
		textSize = mTypedArray.getDimension(R.styleable.roundProgressBar_textSize, 15);
		roundwidth = mTypedArray.getDimension(R.styleable.roundProgressBar_roundWidth, 15);
		max = mTypedArray.getInteger(R.styleable.roundProgressBar_max, 100);
		textIsDisplayable = mTypedArray.getBoolean(R.styleable.roundProgressBar_textIsDisplayable, true);
		style = mTypedArray.getInt(R.styleable.roundProgressBar_style, 0);
		
		mTypedArray.recycle();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		//画最外层的大圆环
		int center = getWidth() / 2; //获取圆心的x坐标
		int radius = (int) (center - roundwidth / 2); //圆环的半径
		paint.setColor(roundColor); //设置圆环的颜色
		paint.setStyle(Paint.Style.STROKE); // 设置空心
		paint.setStrokeWidth(roundwidth); //设置圆环的宽度
		paint.setAntiAlias(true); //消除锯齿
		canvas.drawCircle(center, center, radius, paint); //画出圆环
		
		//画出进度百分比
		paint.setStrokeWidth(0);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
		
		int percent = (int) ((float) progress / 100);
		int rest = (int) ((float) progress % 100);
		float textWidth = paint.measureText(percent + ":" + rest);
//		if (textIsDisplayable && rest != 0 && style == STROKE) {
//			canvas.drawText(percent + ":" + rest, center - textWidth/2, center + textSize/2, paint);			
//		}
		
		//画圆弧，画圆环的进度
		paint.setStrokeWidth(roundwidth); //设置圆环的宽度
		paint.setColor(roundProgressColor); //设置进度的颜色
		
		RectF oval = new RectF(center - radius, center - radius, 
				center + radius, center + radius); //用于定义的圆弧的形状和大小的界限
		
		switch(style) {
		case STROKE: 
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(oval, -90, 360 * progress / max, false, paint); //根据进度画弧画圆弧，第二个参数为：起始角度，第三个为跨的角度，第四个为true的时候是实心，false的时候为空心
			break;
		case FILL:
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			if (progress != 0) {
				canvas.drawArc(oval, -90, 360 * progress / max, true, paint); //根据进度画弧
			}
			break;
		}
	}
	
	public synchronized int getMax() {
		return max;
	}
	
	//设置进度的最大值
	public synchronized void setMax(int max) {
		if (max < 0) {
			throw new IllegalArgumentException("不能少于0");
		}
		this.max = max;
	}
	
	//获取进度，需要同步
	public synchronized int getProgress() {
		return progress;
	}
	
	//设置进度
	public synchronized void setProgress(int progress) {
		if (progress <0 ) {
			throw new IllegalArgumentException("进步不能少于0");
		}
		if (progress > max) {
			progress = max;
		}
		if (progress <= max) {
			this.progress = progress;
			postInvalidate();
		}
	}
	
	public int getCircleColor() {
		return roundColor;
	}
	
	public void setCircleColor(int circleColor) {
		this.roundColor = circleColor;
	} 
	
	public int getCircleProgressColor() {
		return roundProgressColor;
	}
	
	public void setCircleProgressColor(int circleProgressColor) {
		this.roundProgressColor = circleProgressColor;
	}
	
	public int getTextColor() {
		return textColor;
	}
	
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
	
	public float getTextSize() {
		return textSize;
	}
	
	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}
	
	public float getRoundWidth() {
		return roundwidth;
	}
	
	public void setRoundWidth(float roundWidth) {
		this.roundwidth = roundWidth;
	}

}
