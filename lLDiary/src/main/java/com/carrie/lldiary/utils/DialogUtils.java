package com.carrie.lldiary.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.carrie.lldiary.R;

public class DialogUtils {
	
	
	/**
	 * <font color="#f97798">dialog只为提示，只有确定按钮</font>
	 * @param context
	 * @param message <font color="#f97798"></font>
	 * @return void
	 * @version 创建时间：2016年4月27日 下午5:46:31
	 */
	public static void oneBtnDialog(Context context,String message){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setMessage(message).setPositiveButton(context.getString(R.string.OK), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}
	
	/**
	 * <font color="#f97798">dialog只为提示，只有确定按钮</font>
	 * @param context
	 * @param title
	 * @param message <font color="#f97798"></font>
	 * @return void
	 * @version 创建时间：2016年4月27日 下午5:47:55
	 */
	public static void oneBtnDialog(Context context,String title,String message){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle(title).setMessage(message).setPositiveButton(context.getString(R.string.OK), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}

	/**
	 * 弹出具有MaterialDesign风格的对话框
	 * @param activity
	 * @param title
	 * @param msg
     */
	public static void alertMDesignDialog(final Activity activity,String title,String msg){
		new android.support.v7.app.AlertDialog.Builder(activity).setTitle(title).setMessage(msg).setPositiveButton(activity.getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				activity.finish();
			}
		}).setNegativeButton(activity.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}

}
