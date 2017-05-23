package com.carrie.lldiary.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.carrie.lldiary.entity.MoneyEntity;

import java.util.List;

public class DeleteDialog {
	
	protected static final String TABLE_NAME = null;
	private static Handler mHandler;
	

	public static void delete(Context context, final int position, final SQLiteDatabase db, final List<MoneyEntity> entities){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		builder.setTitle("确定删除吗？")
//				.setMessage("删除后将不可恢复")
//				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						db.delete(TABLE_NAME, "content=?",
//								new String[] { entities.get(position)
//										.getContent() });
//						entities.remove(position);
//						new Thread(new Runnable() {
//							@Override
//							public void run() {
//								Message msg = new Message();
//								msg.what = 0;
//								mHandler.sendMessage(msg);
//							}
//						}).start();
//						dialog.dismiss();
//						db.close();
//					}
//				})
//				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				}).show();
		
	}

}
