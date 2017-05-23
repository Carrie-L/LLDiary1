package com.carrie.lldiary.utils;

import java.util.ArrayList;
import java.util.List;

import com.carrie.lldiary.R;

import android.R.interpolator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GestureLock extends View {
	private Point[][] points = new Point[3][3];// 3*3的数组
	/** 标记是否已经初始化了，因为onDrawer()方法要多次调用，但初始化只初始化一次 */
	private boolean inited = false;
	// 将资源图片转换成Bitmap
	private Bitmap bitmapPointNormal;
	private Bitmap bitmapPointPress;
	private Bitmap bitmapPointError;
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
	/** 圆形图片的半径 */
	private float bitmapR;

	/** 手指在触摸屏上的位置坐标 */
	float touchX, touchY;
	/**是否在绘制(手的event)*/
	private boolean isDraw=false;
	private ArrayList<Point> pointList=new ArrayList<Point>();
	/** 不为空则表示：result[0]=i;result[1]=j;手指点的坐标在圆内*/
	private int[] ijs;
	private int i;
	private int j;
	
	private Paint linePressPaint=new Paint();//按下状态线条画笔
	private Paint lineErrorPaint=new Paint();//错误状态线条画笔
	
	/**将选中的点的序号（0-8）按顺序存到List中，供外部程序进行处理，比如密码验证*/
	private ArrayList<Integer> passwordList=new ArrayList<Integer>();
	
	private OnDrawFinishedListener listener;

	public GestureLock(Context context) {
		super(context);
	}
	
	public GestureLock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureLock(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		touchX = event.getX();
		touchY = event.getY();
		
		switch (event.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			resetPoints();//重置，例如当确认密码错误时，再次按下重新绘制
			ijs = getSelectedPoint();
			if(ijs!=null){//手指在圆内，开始绘制
				isDraw=true;
				int i=ijs[0];
				int j=ijs[0];
				points[i][j].state=Point.STATE_PRESS;
				pointList.add(points[i][j]);//将点添加到数组列表中，将按照顺序将所有的店连接起来
				passwordList.add(i*3+j);//将二维数组转为一维数组
			}
			break;
			
		case MotionEvent.ACTION_MOVE:
			//判断当手指移动的时候，是否处于绘制状态。如果不为绘制状态，则什么都不做；否则判断手指是否触摸到了其它的点
			if(isDraw){
				ijs=getSelectedPoint();
				if(ijs!=null){
					i = ijs[0];
					j = ijs[1];
					//将选中的点添加到Point中，先判断当前点是否已经添加，因为一个点不允许被绘制两次
					if(!pointList.contains(points[i][j])){
						points[i][j].state=Point.STATE_PRESS;
						pointList.add(points[i][j]);
						passwordList.add(i*3+j);
					}
				}
			}
			break;
			
		case MotionEvent.ACTION_UP:
			/**OnDrawFinished的返回值，默认为false，即不正确*/
			boolean valid=false;
			if(listener!=null&&isDraw){//当前listener不为空 且 正在绘制状态，调用回调方法
				valid=listener.OnDrawFinished(passwordList);
			}
			if(!valid){//错误
				for(Point p:pointList){//将pointList中所有的点的状态设为ERROR
					p.state=Point.STATE_ERROR;
				}
			}
			isDraw=false;//手指UP时停止绘制
			break;
		}
		this.postInvalidate();//及时更新界面
		return true;
	}
	
	/**
	 * 线的绘制
	 * @param canvas  ： 画板
	 * @param a    ： 线段的两个点
	 * @param b    
	 * 
	 */
	public void drawLine(Canvas canvas,Point a,Point b){
		//判断第一个点的状态是否为按下
		if(a.state==Point.STATE_PRESS){
			canvas.drawLine(a.x, a.y, b.x, b.y, linePressPaint);
		}
		if(a.state==Point.STATE_ERROR){
			canvas.drawLine(a.x, a.y, b.x, b.y, lineErrorPaint);
		}
	}
	
	/**
	 * 用来检测当绘制时，手的位置是否属于某一个点（是否在那个点的范围内）
	 * @return result.
	 *  result[0]=i;
		result[1]=j;
	 */
	private int[] getSelectedPoint(){
		Point pTouch=new Point(touchX, touchY);//将当前手的坐标生成一个点
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++){
				if(points[i][j].distance(pTouch)<bitmapR){//手指点在图片上
					int[] result = new int[2];
					result[0]=i;
					result[1]=j;
					return result;
				}
			}
		}
			return null;   //触摸点不在圆内，返回空
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!inited) {// 如果没有初始化过，则调用初始化方法
			init();
		}
		drawPoints(canvas);
		
		if(pointList.size()>0){
			Point aPoint=pointList.get(0);//第一个点
			for(int i=1;i<pointList.size();i++){
				Point bPoint=pointList.get(i);//第i个点
				drawLine(canvas, aPoint, bPoint);//画一条从a到b的线
				aPoint=bPoint;//将b的位置设为下一条线的起点
			}
			/*if(isDraw){
				drawLine(canvas, aPoint, new Point(touchX, touchY));//最后位置时
			}*/
		}
	}

	/**
	 * 将9个点绘制到画布上
	 * 
	 * @param canvas
	 */
	private void drawPoints(Canvas canvas) {
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++) {
				// 每个点都有三种状态(正常，点击，错误),根据当前的状态来绘制不同的图形
				if (points[i][j].state == Point.STATE_NORMAL) {
					// 当前点的状态为正常状态，绘制正常状态图形
					canvas.drawBitmap(bitmapPointNormal, points[i][j].x
							- bitmapR, points[i][j].y - bitmapR, paint);// 水平方向的偏移量和竖直方向的偏移量

				} else if (points[i][j].state == Point.STATE_PRESS) {
					// press
					canvas.drawBitmap(bitmapPointPress, points[i][j].x
							- bitmapR, points[i][j].y - bitmapR, paint);
				} else {
					// error
					canvas.drawBitmap(bitmapPointError, points[i][j].x
							- bitmapR, points[i][j].y - bitmapR, paint);
				}
			}
		}
	}

	private void init() {
		linePressPaint.setColor(Color.YELLOW);//线条颜色
		lineErrorPaint.setColor(Color.RED);//线条颜色
		linePressPaint.setStrokeWidth(5);//线条宽度
		lineErrorPaint.setStrokeWidth(5);//线条宽度
		
		bitmapPointNormal = BitmapFactory.decodeResource(getResources(),
				R.drawable.normal);
		bitmapPointPress = BitmapFactory.decodeResource(getResources(),
				R.drawable.press);
		bitmapPointError = BitmapFactory.decodeResource(getResources(),
				R.drawable.error);

		bitmapR = bitmapPointNormal.getHeight() / 2;
		int width = getWidth();// 获取屏幕高
		int height = getHeight();// 获取屏幕宽
		int offset = Math.abs(width - height) / 2;// 偏移量
		int offsetX, offsetY;// 水平方向的偏移量和竖直方向上的偏移量
		int space;// 边长

		if (width > height)// 横屏
		{
			space = height / 4;
			offsetX = offset;
			offsetY = 0;
		} else {// 竖屏
			space = width / 4;
			offsetX = 0;
			offsetY = offset;
		}
		points[0][0] = new Point(offsetX + space, offsetY + space);
        points[0][1] = new Point(offsetX + space * 2, offsetY + space);
        points[0][2] = new Point(offsetX + space * 3, offsetY + space);

        points[1][0] = new Point(offsetX + space, offsetY + space * 2);
        points[1][1] = new Point(offsetX + space * 2, offsetY + space * 2);
        points[1][2] = new Point(offsetX + space * 3, offsetY + space * 2);

        points[2][0] = new Point(offsetX + space, offsetY + space * 3);
        points[2][1] = new Point(offsetX + space * 2, offsetY + space * 3);
        points[2][2] = new Point(offsetX + space * 3, offsetY + space * 3);
		
        inited = true;// 表明已经初始化过了

	}
	
	/**
	 * clear all points，将所有绘制的点的状态设置为normal
	 */
	public void resetPoints(){
		passwordList.clear();
		pointList.clear();
		for(int i=0;i<points.length;i++){
			for(int j=0;j<points[i].length;j++){
				points[i][j].state=Point.STATE_NORMAL;
			}
		}
		this.postInvalidate();//及时更新界面
	}

	public interface OnDrawFinishedListener{
		/**
		 * 当绘制完成后，判断用户所绘制的是否正确。如果正确。。如果不正确。。。
		 * @param passwordList
		 * @return boolean
		 */
		boolean OnDrawFinished(List<Integer> passwordList);//当绘制完成后自动调用它
	}
	
	public void setOnDrawFinishedListener(OnDrawFinishedListener listener){
		this.listener=listener;
	}
}


