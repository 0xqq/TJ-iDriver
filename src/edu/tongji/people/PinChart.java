package edu.tongji.people;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class PinChart extends View {

	static Canvas c;
	private Paint[] mPaints;
	private RectF mBigOval;
	float[] mSweep = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private int preWidth;
	private mAnimation ani;
	private int centerX;
	private int centerY;
	int valueX;
	int valueY;

	public static float[] humidity = new float[8];
	private float[] times = new float[8]; 
	private String str[] = new String[8];
    private String name[] = {"0~40km/h","40~50km/h","50~60km/h","60~70km/h","70~80km/h","80~90km/h","90~100km/h","100km/h~"};
	private final String color[] = { "#2cbae7", "#ffa500", "#ff5b3b", "#9fa0a4", "#6a71e5", "#f83f5d", "#64a300",
			"#64ef85" };

	public PinChart(Context context) {
		super(context);
		initView();
	}

	public PinChart(Context context, AttributeSet atr) {
		super(context, atr);
		initView();
	}

	public PinChart(Context context, float[] times) {
		super(context);
		this.times = times;
		initView();
	}

	private void initView() {
		for(int i=0;i<8;i++){
			str[i] = times[i]+"%";
			humidity[i] = (float) (3.6*times[i]);
		}
		ani = new mAnimation();
		ani.setDuration(2000);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);// ���ñ�����ɫ(͸��)
		mPaints = new Paint[humidity.length];

		// ���ò�ͬ����Ĳ�ͬ��ɫ
		for (int i = 0; i < humidity.length; i++) {
			mPaints[i] = new Paint();
			mPaints[i].setAntiAlias(true);
			mPaints[i].setStyle(Paint.Style.FILL);
			mPaints[i].setColor(Color.parseColor(color[i]));
		}
		// getWidth������õ���px
		int cicleWidth = getWidth() - dp2px(60);
		centerX = getWidth() / 2;
//		�ײ��ĳ����ε�˵���߶���30px�����ϵײ�
		centerY = (getHeight()-dp2px(60))>(getWidth()-dp2px(60))?((getHeight()-dp2px(60))/2):(getWidth() / 2+dp2px(30));
//		��ʾ�ĳ����ο�
		preWidth = (getWidth() - dp2px(40)) / 4;
		int half = getWidth() / 2;
		int circle_radius = cicleWidth/2;

		mBigOval = new RectF();// ��ͼ�����ܱ߽�
		mBigOval.top = centerY-circle_radius;
		mBigOval.left = half - cicleWidth / 2;
		mBigOval.bottom = centerY+circle_radius;
		mBigOval.right = half + cicleWidth / 2;

		float start = -180;
		Rect bounds = new Rect();
		for (int i = 0; i < humidity.length; i++) {
			/*
			 * canvas.drawarc�ľ������
			 * public void drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint) 
			 * oval :ָ��Բ������������������
			 * startAngle: Բ����ʼ�Ƕȣ���λΪ�ȡ�
			 * sweepAngle: Բ��ɨ���ĽǶȣ�˳ʱ�뷽�򣬵�λΪ��,�����м俪ʼΪ��ȡ� 
			 * useCenter:���ΪTrueʱ���ڻ���Բ��ʱ��Բ�İ������ڣ�ͨ�������������Ρ�
			 *  paint:����Բ���Ļ������ԣ�����ɫ���Ƿ����ȡ�
			 */
//			������������
			canvas.drawArc(mBigOval, start, mSweep[i], true, mPaints[i]);
//			������������
			if (!str[i].equals("0.0%")) {
//				�������ƣ����²���Ƶ���
				mPaints[i].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
				mPaints[i].setAntiAlias(true);
				mPaints[i].setColor(Color.BLACK);
				mPaints[i].setTextSize(sp2px(15));
				measureText(start + 180, humidity[i], cicleWidth / 2.5f, i);
				canvas.drawText(str[i], valueX - mPaints[i].measureText(str[i]) / 2, valueY + bounds.height() / 2,
						mPaints[i]);
			}
			start += humidity[i];
			int j = 1;
			int k;
			if (i < 4) {
				j = 0;
				k = i;
			} else {
				j = 1;
				k = i - 4;
			}
			mPaints[i] = new Paint();
			mPaints[i].setAntiAlias(true);
			mPaints[i].setStyle(Paint.Style.FILL);
			mPaints[i].setColor(Color.parseColor(color[i]));
			canvas.drawRect(new RectF(dp2px(20) + preWidth * k, centerY+circle_radius+ dp2px(j * 30 + 20),
					dp2px(20) + preWidth * (k + 1), centerY+circle_radius+ dp2px(50 + j * 30)), mPaints[i]);
			mPaints[i].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
			mPaints[i].setAntiAlias(true);
			mPaints[i].setColor(Color.WHITE);
			mPaints[i].getTextBounds(name[i], 0, name[i].length(), bounds);
			mPaints[i].setTextSize(sp2px(12));
			canvas.drawText(name[i], dp2px(20) + preWidth * k + preWidth / 2 - mPaints[i].measureText(name[i]) / 2,
					centerY+circle_radius+ dp2px(j * 30 + 20) + (dp2px(30) / 2 + bounds.height() / 2), mPaints[i]);
		}
	}

	/**
	 * ��ʾ��Ӧ�����ֿ�ʼ��x,y����
	 * 
	 * @param start
	 * @param angle
	 * @param radius
	 * @param i
	 */
	private void measureText(float start, float angle, float radius, int i) {
		float temp = start + (angle / 2);

		if (temp < 90) {
			valueX = (int) (centerX - Math.abs(radius * Math.cos((temp / 180) * Math.PI)));
			valueY = (int) (centerY - Math.abs(radius * Math.sin((temp / 180) * Math.PI)));
		} else if (temp > 90 && temp < 180) {
			temp = 180 - temp;
			valueX = centerX + (int) Math.abs((radius * Math.cos((temp / 180) * Math.PI)));
			valueY = centerY - (int) Math.abs((radius * Math.sin((temp / 180) * Math.PI)));
		} else if (temp > 180 && temp < 270) {
			temp = temp - 180;
			valueX = centerX + (int) Math.abs((radius * Math.cos((temp / 180) * Math.PI)));
			valueY = centerY + (int) Math.abs((radius * Math.sin((temp / 180) * Math.PI)));
		} else {
			temp = 360 - temp;
			valueX = centerX - (int) Math.abs((radius * Math.cos((temp / 180) * Math.PI)));
			valueY = centerY + (int) Math.abs((radius * Math.sin((temp / 180) * Math.PI)));
		}
		
//		System.out.println(i+" : "+str[i]+" x: "+valueX+" y: "+valueY);

	}

	private int sp2px(int value) {
		float v = getResources().getDisplayMetrics().scaledDensity;
		return (int) (value * v + 0.5f);
	}

	// dpת��Ϊpx
	private int dp2px(int value) {
		float v = getResources().getDisplayMetrics().density;
		return (int) (value * v + 0.5f);
	}

	public void start() {
		startAnimation(ani);
	}

	class mAnimation extends Animation {
		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			for (int i = 0; i < humidity.length; i++) {
				mSweep[i] = humidity[i] * interpolatedTime;
			}
			invalidate();
		}
	}

}