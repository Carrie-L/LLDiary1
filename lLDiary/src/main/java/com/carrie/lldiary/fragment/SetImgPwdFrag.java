package com.carrie.lldiary.fragment;

import java.util.List;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.carrie.lldiary.R;
import com.carrie.lldiary.activity.HomeActivity;
import com.carrie.lldiary.base.BaseFragment;
import com.carrie.lldiary.utils.AlertDialogUtils;
import com.carrie.lldiary.utils.Constant;
import com.carrie.lldiary.utils.GestureLock.OnDrawFinishedListener;
import com.carrie.lldiary.utils.LockPatternUtils;
import com.carrie.lldiary.utils.LockPatternView;
import com.carrie.lldiary.utils.LockPatternView.Cell;
import com.carrie.lldiary.utils.LockPatternView.DisplayMode;
import com.carrie.lldiary.utils.LockPatternView.OnPatternListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SetImgPwdFrag extends BaseFragment implements
		OnClickListener, OnDrawFinishedListener {
	private static final String TAG = "SetImgPwdFrag";
	protected static final int IMG = 1;
	private View view;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.ib_back)
	private ImageButton ib_back;
	@ViewInject(R.id.ib_save)
	private ImageButton ib_save;
	/*@ViewInject(R.id.LockView)
	private GestureLock lockView;*/

	private Intent intent;
	private FragmentManager fm;
	private Context mContext;

	/** 保存密码 */
	private List<Integer> passList;
	private SharedPreferences sp;
	
	@ViewInject(R.id.LockView)
	private LockPatternView lockPatternView;
	private LockPatternUtils lockPatternUtils;

	@Override
	public void initData(Bundle savedInstanceState) {
		ib_back.setOnClickListener(this);
		ib_save.setOnClickListener(this);
//		lockView.setOnDrawFinishedListener(this);
		fm = getFragmentManager();
		
		lockPatternUtils = new LockPatternUtils(mContext);
		lockPatternView.setOnPatternListener(new OnPatternListener() {

			private Editor editor;

			@Override
			public void onPatternStart() {}

			@Override
			public void onPatternCleared() {			}

			@Override
			public void onPatternCellAdded(List<Cell> pattern) {			}

			@Override
			public void onPatternDetected(List<Cell> pattern) {
				if (pattern.size() < 3) {
					Toast.makeText(mContext, "密码不能小于3个点", 0).show();
					lockPatternView.setDisplayMode(DisplayMode.Wrong);
				}else{
					lockPatternUtils.saveLockPattern(pattern);
					Toast.makeText(mContext, "创建成功", Toast.LENGTH_SHORT).show();
					
					sp=mContext.getSharedPreferences("config", 0);
					String userName=sp.getString("username", "");
					
					editor = sp.edit();
					editor.putInt("pwdType", IMG);//设置密码类型，如果为图案锁，则为true
					editor.putString(Constant.SP_INPUT_PASSWORD, "");
					editor.commit();
					
					if(TextUtils.isEmpty(userName)){
						AlertDialogUtils.alertDialog(mContext, "检测到您没有设置账户名", "当您忘记密码时，账户名是您取回密码的唯一口令",R.string.okGO, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								fm.beginTransaction().add(new AccountFragment(), "Account").commit();
							}
						});
					}else{
						SharedPreferences spPwd=mContext.getSharedPreferences(Constant.SP_GETPWD, 0);
						Editor editor=spPwd.edit();
						editor.putString(Constant.SP_USERNAME, userName);
						editor.putString(Constant.SP_PASSWORD, LockPatternUtils.patternToString(pattern));
						editor.commit();
					}
				}
			}
		});
	}

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container) {
		BaseFragment.sClsName="SettingImgPwd";
		
		view = inflater.inflate(R.layout.fragment_setting_imgpwd, null);
		ViewUtils.inject(this, view);
		mContext=getActivity();
		tv_title.setText("设置图案密码锁");

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			intent = new Intent(mContext, HomeActivity.class);
			startActivity(intent);
			break;
		case R.id.ib_save:
			Intent intent = new Intent(mContext, HomeActivity.class);
			startActivity(intent);
			lockPatternView.clearPattern();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean OnDrawFinished(List<Integer> passwordList) {
		if (passwordList.size() < 3) {
			Toast.makeText(mContext, "密码不能小于3个点", 0).show();
			return false;
		} else {
			passList = passwordList;
			return true;// 绘制正确
		}
	}

}
