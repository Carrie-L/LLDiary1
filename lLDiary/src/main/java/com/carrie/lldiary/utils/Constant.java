package com.carrie.lldiary.utils;

import android.graphics.Color;

public class Constant {



	public static String SP_GETPWD="GetPwd";
	public static String SP_USERNAME="userName";
	public static String SP_UserEmail="userEmail";
	public static String SP_PASSWORD="password";
	public static String SP_IMG_PASSWORD="img_password";
	public static String SP_INPUT_PASSWORD="input_password";

	public static final String DEFAULT_USER_NAME="default";
	
	//设置信息
	public static final String SP_CONFIG="Config";
	public static final String IsRegisted="IsRegisted";
	public static final String LATEST_INSERT_TIME_EMOJI="emoji_latestInsertTime";//上次最新插入时间

	//账户信息
	public static final String SP_ACCOUUNT="Account";
	public static final String account_phone="mobileNumber";
	public static final String account_username="userName";
	public static final String account_useremail="userEmail";
	public static final String account_userpwd="userPwd";
	
	//主题信息
	public static final String SP_STYLE="Style";
	public static final String STYLE_ALPHA="Alpha";
	public static final String STYLE_RED="Red";
	public static final String STYLE_GREEN="Green";
	public static final String STYLE_BLUE="Blue";
	
	/**主色调：透明，红，绿，蓝*/
	public static int HOME_COLOR=Color.argb(Constant.Alpha, Constant.Red, Constant.Green, Constant.Blue);
	public static int Alpha;
	public static int Red;
	public static int Green;
	public static int Blue;
	
	
	
	
	

}
