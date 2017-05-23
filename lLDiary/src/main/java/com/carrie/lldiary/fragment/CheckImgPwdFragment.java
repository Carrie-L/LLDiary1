package com.carrie.lldiary.fragment;

import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.carrie.lldiary.R;
import com.carrie.lldiary.activity.CheckImgPwdActivity;
import com.carrie.lldiary.activity.DiaryActivity;
import com.carrie.lldiary.activity.HomeActivity;
import com.carrie.lldiary.base.BaseFragment;
import com.carrie.lldiary.utils.GestureLock.OnDrawFinishedListener;
import com.carrie.lldiary.utils.AlertDialogUtils;
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

/**
 * 验证图片密码锁
 * @author Carrie
 *
 */
public class CheckImgPwdFragment extends BaseFragment implements OnClickListener, OnPatternListener{
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

	@ViewInject(R.id.LockView)
	private LockPatternView lockPatternView;
	private LockPatternUtils lockPatternUtils;
	
	@ViewInject(R.id.forgetPwd)
	private TextView tvForgetPwd;
	@ViewInject(R.id.tv_password)
	private TextView tvPassword;
	private AlertDialog dialog;
	
	private LayoutInflater mInflater;

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container) {
		BaseFragment.sClsName="CheckImgPwd";
		View view=inflater.inflate(R.layout.fragment_check_pwd, null);
		ViewUtils.inject(this,view);
		tv_title.setText("验证密码锁");
		return view;
	}
	
	@Override
	public void initData(Bundle savedInstanceState) {
		sp = mContext.getSharedPreferences("config", 0);
		password = sp.getString(Constant.SP_PASSWORD, "");
		tvForgetPwd.setOnClickListener(this);
		lockPatternUtils = new LockPatternUtils(mContext);
		lockPatternView.setOnPatternListener(this);
	}

	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			password=(String) msg.obj;
			tvPassword.setVisibility(View.VISIBLE);
			tvPassword.setText("您的密码是："+password+"\r\n[图案锁:左上角第一个圆点为0]\r\n[0,1,2]\r\n[3,4,5]\r\n[6,7,8]");
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
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onPatternStart() {}
	@Override
	public void onPatternCleared() {}
	@Override
	public void onPatternCellAdded(List<Cell> pattern) {}
	@Override
	public void onPatternDetected(List<Cell> pattern) {
		int result = lockPatternUtils.checkPattern(pattern);
		if (result == 0) {
			lockPatternView.setDisplayMode(DisplayMode.Wrong);
			Toast.makeText(mContext, "密码错误,请重新验证", Toast.LENGTH_SHORT).show();
		} else if (result == 1) {
			Toast.makeText(mContext, "验证通过", Toast.LENGTH_SHORT).show();
			getFragmentManager().beginTransaction()
			.replace(R.id.fl_main_content, new SetImgPwdFrag())
			.commit();
		}
	}



}
