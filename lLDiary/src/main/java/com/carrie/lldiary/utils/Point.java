package com.carrie.lldiary.utils;

public class Point {
	//设置点有三个状态：正常，按下，错误（即图片的三种颜色）
	public static int STATE_NORMAL=0;
	public static int STATE_PRESS=1;
	public static int STATE_ERROR=2;
	
	float x;
	float y;
	int state=STATE_NORMAL;//保存当前状态，默认为正常
	
	/**
	 * 要创建的点的坐标
	 * @param x
	 * @param y
	 */
	public Point(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	/**
	 * (三角形斜边 ) 触摸点和圆心的距离 -- 与  圆的半径 -- 的比较
	 * @param  Point a
	 * @return float distance
	 */
	public float distance(Point a){
		float distance=(float)Math.sqrt((x-a.x)*(x-a.x)+(y-a.y)*(y-a.y));
		return distance;
	}
	

}
