package com.carrie.lldiary.activity;

import java.util.zip.Inflater;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carrie.lldiary.R;
import com.carrie.lldiary.R.id;
import com.carrie.lldiary.R.layout;
import com.carrie.lldiary.utils.AlertDialogUtils;
import com.carrie.lldiary.utils.Constant;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.utils.MD5Util;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class WholePasswordActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.tv_enter)
	private TextView tv_enter;
	@ViewInject(R.id.et_password)
	private EditText et_password;
	@ViewInject(R.id.forgetPwd)
	private TextView tvForgetPwd;
	@ViewInject(R.id.tv_password)
	private TextView tvPwd;

	private SharedPreferences sp;

	private static final int INPUT = 0;
	private static final int IMG = 1;
	protected static final String TAG = "WholePasswordActivity";
	private String password;
	
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_password);
		ViewUtils.inject(this);
		
		mContext=this;

		sp = getSharedPreferences("config", MODE_PRIVATE);

		int pwdType = sp.getInt("pwdType", 2);
		

		if (pwdType == IMG) {
			Intent intent = new Intent(this, CheckImgPwdActivity.class);
			startActivity(intent);
			finish();
		} else if (pwdType == INPUT) {
			tvForgetPwd.setOnClickListener(this);
			checkPassword();
		} else {
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			finish();
		}

	}

	/**
	 * 验证密码是否正确
	 */
	public void checkPassword() {

		tv_enter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String spPwd=sp.getString(Constant.SP_INPUT_PASSWORD, "");
				LogUtil.i(TAG, "MD5Util.md5(spPwd)="+spPwd);
				password = et_password.getText().toString().trim();
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(WholePasswordActivity.this, "密码不能为空", 0)
							.show();
				}
				
				// 判断密码是否正确
				else if (password.equals(spPwd)) {
					Toast.makeText(WholePasswordActivity.this, "密码正确", 0)
							.show();
					Intent intent = new Intent(WholePasswordActivity.this,
							HomeActivity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(WholePasswordActivity.this, "密码错误", 0)
							.show();
					et_password.setText("");
				}
			}
		});

	}
	
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			password=(String) msg.obj;
			tvPwd.setVisibility(View.VISIBLE);
			tvPwd.setText("您的密码是：\r\n"+password);
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forgetPwd:
			String strRed="<FONT COLOR=#ff0000>[账户名]</FONT>";
			AlertDialogUtils.showDialog(mContext, "输入密码口令", "该口令为您的"+Html.fromHtml(strRed)+"，用于取回您的密码",new OnClickListener() {
				@Override
				public void onClick(View v) {
					String name = AlertDialogUtils.et.getText().toString().trim();
					SharedPreferences sp=getSharedPreferences(Constant.SP_GETPWD, 0);
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
			break;
		default:
			break;
		}
	}

}
