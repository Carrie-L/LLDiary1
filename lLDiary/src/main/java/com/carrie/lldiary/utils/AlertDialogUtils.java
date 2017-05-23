package com.carrie.lldiary.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.carrie.lldiary.R;
import com.carrie.lldiary.db.AccountDB;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AlertDialogUtils {
	private final String TAG="AlertDialogUtils";
	private static LayoutInflater mInflater;
	@ViewInject(R.id.btn_confirm)
	static
	Button btn_confirm;
	static
	Button btn_cancel;
	public static AlertDialog dialog;
	public static EditText et;
	public static TextView tvTitle,tvMessage;
	private static View view2;
	public static TextView tvForgetPwd;


	public static void alertDialog(Context context, String title, String msg,int resOK,OnClickListener posListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setMessage(msg).setPositiveButton(resOK, posListener)
		.setNegativeButton(R.string.Cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).show();
	}

	/**
	 * 只有一个【R.string.ok】按钮
	 * @param context
	 * @param title
	 * @param msg
	 * @param posListener
	 */
	public static void showOKDialog(Context context, String title, String msg,OnClickListener posListener){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setMessage(msg).setPositiveButton(R.string.ok, posListener).show();
	}

	/**
	 * 只有一个【R.string.cancel】按钮
	 * @param context
	 * @param title
	 * @param msg
	 */
	public static void showCancelDialog(Context context, String title, String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setMessage(msg).setNegativeButton(R.string.btn_cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}

	public static void showDialog(Context context, String title, String msg,View.OnClickListener posListener){
		mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.dialog_set_username, null);
//		ViewUtils.inject(context, view);
		btn_cancel=(Button) view.findViewById(R.id.btn_cancel);
		btn_confirm=(Button) view.findViewById(R.id.btn_confirm);
		tvTitle=(TextView) view.findViewById(R.id.tv_dialog_title);
		tvMessage=(TextView) view.findViewById(R.id.tv_dialog_message);
		et = (EditText) view.findViewById(R.id.et_input_username);
		view2=view.findViewById(R.id.view2);
		tvForgetPwd=(TextView) view.findViewById(R.id.forgetPwd);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		tvTitle.setText(title);
		if(msg==null){
			tvMessage.setVisibility(View.GONE);
			view2.setVisibility(View.INVISIBLE);
		}else{
			tvMessage.setText(msg);
		}
		btn_confirm.setOnClickListener(posListener);
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		builder.setView(view);
		dialog = builder.show();
	}

	/**
	 * 弹出列表选项对话框
	 */
	public static  void itemsChooseDialog(Context context,String title,String[] items,OnClickListener listener){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if(!TextUtils.isEmpty(title)){
			builder.setTitle(title);
		}
		builder.setItems(items,listener );
		builder.create().show();
	}



}
