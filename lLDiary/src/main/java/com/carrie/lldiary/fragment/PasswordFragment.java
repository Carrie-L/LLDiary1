package com.carrie.lldiary.fragment;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.carrie.lldiary.R;
import com.carrie.lldiary.activity.CheckImgPwdActivity;
import com.carrie.lldiary.activity.HomeActivity;
import com.carrie.lldiary.base.BaseFragment;
import com.carrie.lldiary.utils.AlertDialogUtils;
import com.carrie.lldiary.utils.Constant;
import com.carrie.lldiary.utils.MD5Util;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PasswordFragment extends BaseFragment implements OnClickListener {
	private static final int INPUT = 0;
	private static final int IMG = 1;
	protected static final int WHOLE = 1;
	protected static final int DIARY = 2;
	protected static final int COLOR = 3;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.ib_back)
	private ImageButton ib_back;
	@ViewInject(R.id.ib_add)
	private ImageButton ib_add;
	@ViewInject(R.id.tv_setting_image_password)
	private TextView imagePwd;
	@ViewInject(R.id.tv_setting_input_password)
	private TextView inputPwd;
	@ViewInject(R.id.cb_whole_password)
	private CheckBox cb_whole_password;
	@ViewInject(R.id.cb_diary_password)
	private CheckBox cb_diary_password;

	private View view;
	private Intent intent;
	private FragmentManager fm;

	@ViewInject(R.id.et_input_password)
	EditText et_input_password;
	@ViewInject(R.id.et_confirm_password)
	EditText et_confirm_password;
	@ViewInject(R.id.btn_confirm)
	Button btn_confirm;
	@ViewInject(R.id.btn_cancel)
	Button btn_cancel;

	private AlertDialog dialog;
	private SharedPreferences sp;
	private String imgPwd;

	@Override
	public void initData(Bundle savedInstanceState) {
		ib_back.setOnClickListener(this);
		imagePwd.setOnClickListener(this);
		inputPwd.setOnClickListener(this);
		
		cb_whole_password.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					cb_diary_password.setChecked(false);
				}
				Editor editor=sp.edit();
				editor.putInt("CbPwd",WHOLE);//加密整个软件
				editor.commit();
			}
		});
		cb_diary_password.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					cb_whole_password.setChecked(false);
				}
				Editor editor=sp.edit();
				editor.putInt("CbPwd",DIARY);//只加密日记本
				editor.commit();
			}
		});
		fm = getFragmentManager();
		
	}

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container) {
		BaseFragment.sClsName="SettingPassword";
		
		view = inflater.inflate(R.layout.fragment_password, null);
		ViewUtils.inject(this, view);

		tv_title.setText("密码锁");
		ib_add.setVisibility(View.INVISIBLE);
		
		sp = mContext.getSharedPreferences("config", 0);
		int cbPwd=sp.getInt("CbPwd", 0);
		System.out.println("cbPwd:"+cbPwd);
		if(cbPwd==WHOLE){
			cb_whole_password.setChecked(true);
		}else if(cbPwd==DIARY){
			cb_diary_password.setChecked(true);
		}else{
			cb_whole_password.setChecked(false);
			cb_diary_password.setChecked(false);
		}
		
		int pwdType=sp.getInt("pwdType",2);
		if(pwdType==IMG){
			imagePwd.setTextColor(getResources().getColor(R.color.red));
		}else if(pwdType==INPUT){
			inputPwd.setTextColor(getResources().getColor(R.color.red));
		}
		

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			intent = new Intent(mContext, HomeActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_setting_image_password:
			imgPwd = sp.getString(Constant.SP_IMG_PASSWORD, "");
			if(TextUtils.isEmpty(imgPwd)){
				fm.beginTransaction()
				.replace(R.id.fl_main_content, new SetImgPwdFrag())
				.commit();
			}else{
				fm.beginTransaction()
				.replace(R.id.fl_main_content, new CheckImgPwdFragment())
				.commit();
			}
			break;
		case R.id.tv_setting_input_password:
			imgPwd = sp.getString(Constant.SP_INPUT_PASSWORD, "");
			if(TextUtils.isEmpty(imgPwd)){
				showSetPswdDialog();
			}else{
				AlertDialogUtils.showDialog(mContext, "验证密码", null, new OnClickListener() {
					@Override
					public void onClick(View v) {
						/*AlertDialogUtils.tvForgetPwd.setVisibility(View.VISIBLE);
						AlertDialogUtils.tvForgetPwd.setOnClickListener(this);*/
						
						String pwd=AlertDialogUtils.et.getText().toString().trim();
						if(imgPwd.equals(MD5Util.md5Password(pwd))){
							Toast.makeText(mContext, "验证通过", 0).show();
							AlertDialogUtils.dialog.dismiss();
							showSetPswdDialog();
						}else{
							Toast.makeText(mContext, "密码不正确", 0).show();
							AlertDialogUtils.et.setText("");
						}
					}
				});
			}
			break;
		case R.id.forgetPwd:
			String strRed="<FONT COLOR=#ff0000>[账户名]</FONT>";
			AlertDialogUtils.showDialog(mContext, "输入密码口令", "该口令为您的"+Html.fromHtml(strRed)+"，用于取回您的密码",new OnClickListener() {
				@Override
				public void onClick(View v) {
					String name = AlertDialogUtils.et.getText().toString().trim();
					SharedPreferences sp=mContext.getSharedPreferences(Constant.SP_GETPWD, 0);
					String username=sp.getString("userName", "");
					if(name.equals(username)){
						String password=sp.getString(Constant.SP_PASSWORD, "");
						Message msg=new Message();
						msg.obj=password;
						mHandler.sendMessage(msg);
						AlertDialogUtils.dialog.dismiss();
					}else{
						Toast.makeText(mContext, "您输入的账户名不正确，请重新输入", 0).show();
						AlertDialogUtils.et.setText("");
					}
				}
			});

		default:
			break;
		}
	}
	
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case COLOR:
				
				break;

			default:
				break;
			}
			String password=(String) msg.obj;
			AlertDialogUtils.tvTitle.setText("您的密码是："+password);
		};
	};

	/**
	 * 设置密码对话框
	 */
	private void showSetPswdDialog() {
		View view = mInflater.inflate(R.layout.dialog_set_input_password, null);
		ViewUtils.inject(this, view);
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

		btn_confirm.setOnClickListener(new OnClickListener() {
			private String password;
			private String passwprd_confirm;

			@Override
			public void onClick(View v) {
				password = et_input_password.getText().toString().trim();
				passwprd_confirm = et_confirm_password.getText().toString()
						.trim();
				if (TextUtils.isEmpty(password)
						|| TextUtils.isEmpty(passwprd_confirm)) {
					Toast.makeText(mContext, "密码不能为空", 0).show();
				} else if (password.equals(passwprd_confirm)) {// 判断密码是否一致
					Editor editor = sp.edit();
					editor.putString(Constant.SP_INPUT_PASSWORD,password);
					editor.putInt("pwdType", INPUT).commit();//设置密码类型，如果为输入框，则为false
					editor.putString(Constant.SP_IMG_PASSWORD, "");
					editor.commit();
					imagePwd.setTextColor(getResources().getColor(R.color.black));
//					imagePwd.setTextColor(getResources().getColor(R.color.red));
					dialog.dismiss();
					
					SharedPreferences sp2=mContext.getSharedPreferences(Constant.SP_GETPWD, 0);
					Editor editor2=sp2.edit();
					editor2.putString(Constant.SP_PASSWORD,password);
					editor2.commit();
					
					Toast.makeText(mContext, "设置密码成功", 0).show();
				} else {
					Toast.makeText(mContext, "密码不一致", 0).show();
					et_confirm_password.setText("");
				}
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		builder.setView(view);
		dialog = builder.show();
	}

	

}
