package edu.tongji.roadrecord;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class HProgress extends View {
	
	/**�ֶ���ɫ*/
	 private static final int[] SECTION_COLORS = {Color.RED,Color.YELLOW,Color.GREEN};
	 /**���������ֵ*/
	 private float maxCount;
	 /**��������ǰֵ*/
	 private float currentCount;
	 /**����*/
	 private Paint mPaint;
	 private int mWidth,mHeight;

	 public HProgress(Context context, AttributeSet attrs,
	   int defStyleAttr) {
	  super(context, attrs, defStyleAttr);
	  initView(context);
	 }
	 public HProgress(Context context, AttributeSet attrs) {
	  super(context, attrs);
	  initView(context);
	 }
	 public HProgress(Context context) {
	  super(context);
	  initView(context);
	 }

	 private void initView(Context context) {
		 
	 }
	 @Override
	 protected void onDraw(Canvas canvas) {
	  super.onDraw(canvas);
	  mPaint = new Paint();
	  mPaint.setAntiAlias(true);
	  int round = mWidth/2;
	  System.out.println("max="+maxCount + "  current="+currentCount);
	  mPaint.setColor(Color.rgb(71, 76, 80));
	  RectF rectBg = new RectF(0, 0, mWidth, mHeight);
	  canvas.drawRoundRect(rectBg, round, round, mPaint);
	  mPaint.setColor(Color.BLACK);
	  RectF rectBlackBg = new RectF(2, 2, mWidth-2, mHeight-2);
	  canvas.drawRoundRect(rectBlackBg, round, round, mPaint);

	  float section = 1-currentCount/maxCount;
	  RectF rectProgressBg = new RectF(3, (mHeight-3)*section,mWidth-3,mHeight-3);
	  if(section >= 2.0f/3.0f){//��ɫ
	   if(section != 1.0f){
	    mPaint.setColor(SECTION_COLORS[2]);
	   }else{
	    mPaint.setColor(Color.TRANSPARENT);
	   }
	  }else{
	   int count = (section >= 1.0f/3.0f ) ? 2 : 3;
	   int[] colors = new int[count];
//	        ��������
	   System.arraycopy(SECTION_COLORS, 3-count, colors, 0, count);
	   float[] positions = new float[count];
	   //����
	   if(count == 2){
	    positions[0] = 0.0f;
	    positions[1] = 1.0f;
	   }else{//�����
	    positions[0] = 0.0f;
	    positions[1] = 1-(1.0f/2.0f)/(currentCount/maxCount);
//	    System.out.println("yellow is "+ positions[1]);
	    positions[2] = 1.0f;
	   }
	   positions[positions.length-1] = 1.0f;
	   LinearGradient shader = new LinearGradient(3, (mHeight-3)*section,mWidth-3,mHeight-3, colors,positions, Shader.TileMode.MIRROR);
	   mPaint.setShader(shader);
	  }
	  canvas.drawRoundRect(rectProgressBg, round, round, mPaint);
	 }

	 private int dipToPx(int dip) {
	  float scale = getContext().getResources().getDisplayMetrics().density;
	  return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	 }

	 /***
	  * �������Ľ���ֵ
	  * @param maxCount
	  */
	 public void setMaxCount(float maxCount) {
	  this.maxCount = maxCount;
	 }

	 /***
	  * ���õ�ǰ�Ľ���ֵ
	  * @param currentCount
	  */
	 public void setCurrentCount(float currentCount) {
	  this.currentCount = currentCount > maxCount ? maxCount : currentCount;
	  invalidate();
	 }

	 public float getMaxCount() {
	  return maxCount;
	 }

	 public float getCurrentCount() {
	  return currentCount;
	 }

//	 �ڻ���view֮ǰҪ��������������ڼ��㵱ǰ�ؼ��Ĵ�С
	 @Override
	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	  int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
	  int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
	  int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
	  int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
	  if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
	   mWidth = widthSpecSize;
	  } else {
	   mWidth = 0;
	  }
	  if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
	   mHeight = dipToPx(15);
	  } else {
	   mHeight = heightSpecSize;
	  }
	  setMeasuredDimension(mWidth, mHeight);
	 }

}
