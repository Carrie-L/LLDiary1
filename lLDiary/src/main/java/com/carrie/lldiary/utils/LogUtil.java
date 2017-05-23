package com.carrie.lldiary.utils;

import android.util.Log;
/**
 * 封装log
 * @author Carrie
 *
 */

public class LogUtil {
	public static final int VERBOSE=1;
	public static final int DEBUG=2;
	public static final int INFO=3;
	public static final int WARNING=4;
	public static final int ERROR=5;
	public static final int LEVEL=0;
	public static final String MY_LOG="_Carrie";
	//判断：修改LEVEL的值。LEVEL的值小于Log级别，打印Log日志；LEVEL的值大于Log级别，不打印
	public static void v(String tag,String msg){
		if(LEVEL<=VERBOSE)
			Log.v(tag+MY_LOG,msg);
	}
	public static void d(String tag,String msg){
		if(LEVEL<=DEBUG)			
			Log.d(tag+MY_LOG,msg);
	}
	public static void i(String tag,String msg){
		if(LEVEL<=INFO){			
			Log.i(tag+MY_LOG,msg);
		}
	}
	public static void w(String tag,String msg){
		if(LEVEL<=WARNING){
			Log.w(tag+MY_LOG,msg);
		}
	}
	public static void e(String tag,String msg){
		if(LEVEL<=ERROR){
			Log.e(tag+MY_LOG,msg);
		}
	}

}
