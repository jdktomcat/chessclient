package com.xmg.chinachess;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class MyView extends SurfaceView implements Callback, Runnable {

	// 刷屏线程
	private Thread drawThread;
	// 是否进入界面并且运行
	private boolean isRunning;
	// 刷新周期（多少毫秒刷新一次）
	private int drawT;
	private SurfaceHolder surfaceHolder;

	public MyView(Context context) {
		this(context, null);
	}

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}

	private void init() {
		this.surfaceHolder = this.getHolder();
		this.surfaceHolder.addCallback(this);
		//设置可获得焦点
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		//设置常量
		this.setKeepScreenOn(true);
		this.drawT = 100;
		setZOrderOnTop(true);// 设置画布 背景透明
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		this.isRunning = true;
		this.drawThread = new Thread(this);
		this.drawThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		isRunning = false;
	}

	@Override
	public void run() {
		while(isRunning){
			Canvas canvas = null;
			try {
				canvas = surfaceHolder.lockCanvas();
				if(canvas != null){
					doDraw(canvas);
				}
			} catch (Exception e) {
			}
			finally{
				if(canvas != null){
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
			try {
				Thread.sleep(drawT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void doDraw(Canvas canvas) {
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));
		Paint paint = new Paint();  
	    paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));  
	    canvas.drawPaint(paint);  
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC));  
	}

	public void setDrawT(int time) {
		this.drawT = time;
	}
}
