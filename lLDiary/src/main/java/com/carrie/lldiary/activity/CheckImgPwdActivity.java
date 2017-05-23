package com.carrie.lldiary.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.carrie.lldiary.R;
import com.carrie.lldiary.R.id;
import com.carrie.lldiary.R.layout;
import com.carrie.lldiary.utils.AlertDialogUtils;
import com.carrie.lldiary.utils.GestureLock.OnDrawFinishedListener;
import com.carrie.lldiary.utils.Constant;
import com.carrie.lldiary.utils.LockPatternUtils;
import com.carrie.lldiary.utils.LockPatternView;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.utils.LockPatternView.Cell;
import com.carrie.lldiary.utils.LockPatternView.DisplayMode;
import com.carrie.lldiary.utils.LockPatternView.OnPatternListener;
import com.carrie.lldiary.utils.MD5Util;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;

public class CheckImgPwdActivity extends Activity implements OnClickListener, OnPatternListener {
	private static final int WHOLE = 1;
	protected static final String TAG = null;
	/*
	 * @ViewInject(R.id.LockView) private GestureLock lockView;
	 */
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	/*
	 * @ViewInject(R.id.ib_back_check) private ImageButton ib_back;
	 */
	private SharedPreferences sp;
	private String password;
	private Context mContext;

	@ViewInject(R.id.LockView)
	private LockPatternView lockPatternView;
	private LockPatternUtils lockPatternUtils;
	
	@ViewInject(R.id.forgetPwd)
	private TextView tvForgetPwd;
//	@ViewInject(R.id.et_input_username)
//	private EditText et_username;
	@ViewInject(R.id.tv_password)
	private TextView tvPassword;
	private AlertDialog dialog;
	
	private LayoutInflater mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_check_pwd);
		MyActivityManager.add(this);
		ViewUtils.inject(this);
		// ib_back.setOnClickListener(this);
		tv_title.setText("验证密码锁");
		mContext = this;

		sp = getSharedPreferences("config", 0);
//		password = sp.getString("image_password", "");
		// lockView.setOnDrawFinishedListener(this);
		tvForgetPwd.setOnClickListener(this);
		lockPatternUtils = new LockPatternUtils(this);
		lockPatternView.setOnPatternListener(this);
	}

	/**
	 * 当按back键时，直接回到主界面
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 是否触发按键为back键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

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
	
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			password=(String) msg.obj;
			tvPassword.setVisibility(View.VISIBLE);
			tvPassword.setText("您的密码是："+password+"\r\n[图案锁:左上角第一个圆点为0]\r\n[0,1,2]\r\n[3,4,5]\r\n[6,7,8]");
		};
	};

	// ------------------验证密码-------------------------
	@Override
	public void onPatternStart() {
	}

	@Override
	public void onPatternCleared() {
	}

	@Override
	public void onPatternCellAdded(List<Cell> pattern) {
	}

	@Override
	public void onPatternDetected(List<Cell> pattern) {
		int cbPwd = sp.getInt("CbPwd", 0);

		int result = lockPatternUtils.checkPattern(pattern);
		LogUtil.i(TAG, "result=" + result);
		if (result == 0) {
			lockPatternView.setDisplayMode(DisplayMode.Wrong);
			Toast.makeText(CheckImgPwdActivity.this, "密码错误,请重新验证", Toast.LENGTH_SHORT).show();
		} else if (result == 1) {
			Toast.makeText(CheckImgPwdActivity.this, "密码正确", Toast.LENGTH_SHORT).show();
			if (cbPwd == WHOLE) {
				Intent intent = new Intent(mContext, HomeActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(mContext, DiaryActivity.class);
				startActivity(intent);
			}
			finish();
		}
	}

	// 友盟统计、百度统计
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		StatService.onPageStart(this, "验证图案锁");
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		StatService.onPageEnd(this, "验证图案锁");
	}

}
