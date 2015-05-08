package com.miglab.miyo.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.miglab.miyo.R;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class RoundImageView extends ImageView{

	public int mBorderThickness = 0;//边框
	public Context mContext;
	public int mBorderColor = 0xffffffff;//颜色
	
	public RoundImageView(Context context) {
		super(context);
		mContext = context;
	}
	
	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setCustomAttributes(attrs);
	}
	
	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		setCustomAttributes(attrs);
	}

	public void setCustomAttributes(AttributeSet attrs) {
		//得到实例，对应的styleable值在values/attrs.xml中设置
		TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.roundImageView);
		//从array中获取对应的值，第二个参数为默认值，如果值在attrs.xml中没有定义，则返回第二个参数的值
		mBorderThickness = array.getDimensionPixelSize(R.styleable.roundImageView_border_thickness, 0);
		mBorderColor = array.getColor(R.styleable.roundImageView_border_color, mBorderColor);
		array.recycle();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		if(null == drawable) {
			return;
		}
		int width = getWidth();
		int height = getHeight();
		if(0 == width || 0 == height) {
			return;
		}
		this.measure(0, 0);
		if(drawable.getClass() == NinePatchDrawable.class) {
			return;
		}
				
		Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
		if (bitmap == null) {
			return;
		}
		Bitmap bitmap2 = bitmap.copy(Config.ARGB_8888, true);

		//半径
		int radius = (width < height ? width : height)/2 - mBorderThickness;
		//画里面的小圆
		Bitmap roundBitmap = getCroppedBitmap(bitmap2, radius);
		
		
		if(0 == mBorderThickness){
			//不显示边框
			canvas.drawBitmap(roundBitmap, 0, 0, null);
		}else{
			//显示边框
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setFilterBitmap(true);
			paint.setDither(true);
			paint.setColor(mBorderColor);
			
			//画大圆
			canvas.drawCircle(width / 2,
					height / 2,
					radius + mBorderThickness, 
					paint);
			canvas.drawBitmap(roundBitmap, width / 2 - radius, height / 2 - radius, null);
		}
	}

	protected Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
		Bitmap scaledSrcBmp,tempBitmap;
		//直径
		int diameter = radius * 2;
		if(bmp.getWidth() != diameter || bmp.getHeight() != diameter) {
			tempBitmap = bmp;
			if(bmp.getWidth() < bmp.getHeight()){
				tempBitmap = Bitmap.createBitmap(bmp,0,(bmp.getHeight()-bmp.getWidth())/2,bmp.getWidth(),bmp.getWidth());
			}else if(bmp.getWidth() > bmp.getHeight()){
				tempBitmap = Bitmap.createBitmap(bmp,(bmp.getWidth()-bmp.getHeight())/2,0,bmp.getHeight(),bmp.getHeight());
			}
			scaledSrcBmp = Bitmap.createScaledBitmap(tempBitmap, diameter, diameter, false);
		}else{
			scaledSrcBmp = bmp;
		}
		Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(), 
				scaledSrcBmp.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		
		Paint paint = new Paint();
		//以图片的长宽画一个方形
		Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight());
		
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(mBorderColor);//画笔颜色
        paint.setAntiAlias(true);
        
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
                scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2, paint);
        //设置图片的相交模式
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
        return output;
	}

	public void setBorder(int border){
		mBorderThickness = border;
		this.invalidate();
	}
}
