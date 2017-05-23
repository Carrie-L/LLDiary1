package com.carrie.lldiary.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.entity.MyUser;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.Constant;
import com.carrie.lldiary.utils.DialogUtils;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.view.DialogView;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 注册/登录
 * 
 * @author Administrator
 * 
 */
public class RegisterActivity extends Activity implements OnClickListener {
	protected static final String TAG = "RegisterActivity";
	private SharedPreferences msp_config;
	private Editor mEditor;
	private TextView tv_head;
	private Button btn_head;
	private EditText et_username;
	private EditText et_userpwd;
	private Button button;
	private boolean isRegisted;
	// private EditText et_userphone;
	// private EditText et_user_authCode;

	/**
	 * true：当前是注册页面；
	 * <p>
	 * false：当前是登录页面
	 */
	boolean currentReg = true;
	private String userName;
	private String userPwd;
	private String userPhone;
	private String userAuthCode;
	private BmobUser user;
	private RelativeLayout rl_head;
	private TextView tv_forget, tv_third;
	private AlertDialog mDialog;
	private DialogView mDialogView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		findView();

		// 自动登录
		if (App.mCurrUser != null) {
			enterHome();
		}

		msp_config = getSharedPreferences(Constant.SP_CONFIG, MODE_PRIVATE);
		mEditor = msp_config.edit();
		isRegisted = msp_config.getBoolean(Constant.IsRegisted, false);

		if (isRegisted) {
			currentReg = false;
		}

//		change(isRegisted);


		enterHome();



		btn_head.setBackgroundColor(AppUtils.getHomeColor());
		rl_head.setBackgroundColor(AppUtils.getHomeColor());
		button.setBackgroundColor(AppUtils.getHomeColor());
		tv_third.setTextColor(AppUtils.getLightHomeColor());

		String username = App.mSP_account.getString(Constant.account_username, "");
		LogUtil.i(TAG, "username=" + username);
		if (!TextUtils.isEmpty(username)) {
			et_username.setText(username);
			et_username.setSelection(username.length());
			et_userpwd.requestFocus();
		}
	}

	private void findView() {
		rl_head = (RelativeLayout) findViewById(R.id.rl_head);
		tv_head = (TextView) findViewById(R.id.tv_head);
		btn_head = (Button) findViewById(R.id.btn_head);
		et_username = (EditText) findViewById(R.id.et_account);
		et_userpwd = (EditText) findViewById(R.id.et_pwd);
		// et_userphone = (EditText) findViewById(R.id.et_phone);
		// et_user_authCode = (EditText) findViewById(R.id.et_authCode);
		button = (Button) findViewById(R.id.button);
		tv_forget = (TextView) findViewById(R.id.tv_forget);
		tv_third = (TextView) findViewById(R.id.tv_third);

		btn_head.setOnClickListener(this);
		button.setOnClickListener(this);

	}

	private void change(boolean bool) {
		if (bool) {
			// 显示登录页面
			tv_head.setText(getString(R.string.login));
			btn_head.setText(getString(R.string.register));
			button.setText(getString(R.string.login));

			tv_forget.setVisibility(View.VISIBLE);
			tv_forget.setTextColor(AppUtils.getLightHomeColor());
			tv_forget.setOnClickListener(this);

		} else {
			// 显示注册页面
			tv_head.setText(getString(R.string.register));
			btn_head.setText(getString(R.string.login));
			button.setText(getString(R.string.register));
			tv_forget.setVisibility(View.GONE);

		}
	}

	private void getInput() {
		userName = et_username.getText().toString().trim();
		userPwd = et_userpwd.getText().toString().trim();

		if (TextUtils.isEmpty(userName)) {
			AppUtils.toast0(getApplicationContext(), getString(R.string.toast_username_null));
			et_username.requestFocus();
			return;
		}

		if (TextUtils.isEmpty(userPwd)) {
			AppUtils.toast0(getApplicationContext(), getString(R.string.toast_userpwd_null));
			et_userpwd.requestFocus();
			return;
		}
	}

	/**
	 * 注册
	 */
	private void register() {
		tv_forget.setVisibility(View.GONE);
		et_username.setText("");
		getInput();

		// if (userPwd.length() <= 6) {
		// AppUtils.toast1(getApplicationContext(),
		// getString(R.string.toast_userpwd_length));
		// return;
		// }

		if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPwd)) {
			user = new BmobUser();
			user.setUsername(userName);
			user.setPassword(userPwd);

			// 注册
			user.signUp(getApplicationContext(), new SaveListener() {
				@Override
				public void onSuccess() {
					AppUtils.toast1(getApplicationContext(), getString(R.string.toast_register_success));

					App.mCurrUser = BmobUser.getCurrentUser(getApplicationContext());

					App.mSP_account.edit().putString(Constant.account_username, userName).putString(Constant.account_userpwd, userPwd).commit();

					enterHome();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					LogUtil.e(TAG, "注册失败: arg0=" + arg0 + "，arg1=" + arg1);

					if (arg1.contains("email") && arg1.contains("already taken")) {// 邮箱已存在
						AppUtils.toast1(getApplicationContext(), getString(R.string.toast_register_failed_email));
					} else if (arg1.contains("username") && arg1.contains("already taken")) {// 用户名已存在
						AppUtils.toast1(getApplicationContext(), getString(R.string.toast_register_failed_username));
						et_username.requestFocus();
					} else if (arg0 == 9010) {// 网络超时
						AppUtils.toast1(getApplicationContext(), getString(R.string.toast_network_outtime));
					} else if (arg0 == 9016) {// 无网络连接，请检查您的手机网络。
						AppUtils.toast1(getApplicationContext(), getString(R.string.toast_network_error));
					}

				}
			});
		}

	}

	/**
	 * 登录
	 */
	private void login() {
		getInput();

		if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPwd)) {
			BmobUser.loginByAccount(getApplicationContext(), userName, userPwd, new LogInListener<MyUser>() {

				private BmobFile bmobFile;

				@Override
				public void done(MyUser user, BmobException arg1) {
					if (user != null) {
						AppUtils.toast0(getApplicationContext(), getString(R.string.toast_login_success));
						App.mCurrUser = BmobUser.getCurrentUser(getApplicationContext());

						mEditor.putBoolean(Constant.IsRegisted, true).commit();
						App.mSP_account.edit().putString(Constant.account_username, userName).commit();

						enterHome();

						// 下载头像
						BmobQuery<MyUser> query = new BmobQuery<MyUser>();
						query.addQueryKeys("avatar");
						query.getObject(getApplicationContext(), App.mCurrUser.getObjectId(), new GetListener<MyUser>() {

							@Override
							public void onSuccess(MyUser arg0) {
								AppUtils.toast0(getApplicationContext(), "查询成功+arg0=" + arg0);
								LogUtil.i(TAG, "查询成功+arg0.toString=" + arg0.toString());
								bmobFile = arg0.avatar;

								if (bmobFile != null) {
									bmobFile.download(getApplicationContext(), new File(AppUtils.avatarPath(userName)),
											new DownloadFileListener() {
												@Override
												public void onSuccess(String arg0) {
													AppUtils.toast0(getApplicationContext(), "下载头像成功");
												}

												@Override
												public void onFailure(int arg0, String arg1) {
													AppUtils.failedLog("下载头像失败", arg0, arg1);
												}
											});
								}
							}

							@Override
							public void onFailure(int arg0, String arg1) {
								AppUtils.failedLog("查询失败", arg0, arg1);
							}
						});
					} else {
						LogUtil.e(TAG, "BmobException: arg1.getErrorCode()=" + arg1.getErrorCode() + "||| arg1=" + arg1 + "，user=" + user);

						AppUtils.bmobFailToast(getApplicationContext(), arg1.getErrorCode());
					}
				}
			});

		}
	}

	/**
	 * 第三方登录
	 */
	private void thirdLogin() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_head:// 注册/登录按钮切换

			change(currentReg);

			currentReg = !currentReg;

			break;

		case R.id.button:// 注册/登录
			if (currentReg) {
				register();
			} else {
				login();
			}

			break;

		case R.id.btn_weibo:
			AppUtils.toast0(getApplicationContext(), "微博登录");
			break;

		case R.id.btn_qq:// QQ授权登录
			AppUtils.toast0(getApplicationContext(), "QQ登录");
			// qqAuthorize();
			break;

		case R.id.btn_weixin:// 微信授权登录
			AppUtils.toast0(getApplicationContext(), "微信登录");
			// 微信登陆，文档可查看：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&lang=zh_CN&token=0ba3e6d1a13e26f864ead7c8d3e90b15a3c6c34c
			// 发起微信登陆授权的请求
			break;

		case R.id.tv_forget:// 忘记用户名/密码
			forgetPwd();

			break;
		default:
			break;
		}
	}

	private void forgetPwd() {
		String email = App.mSP_account.getString(Constant.account_useremail, "");
		if (App.mCurrUser != null) {
			email = App.mCurrUser.getEmail();
			if (TextUtils.isEmpty(email)) {
				AppUtils.toast0(getApplicationContext(), "您尚未绑定邮箱");
			} else {
				BmobUser.resetPasswordByEmail(getApplicationContext(), App.mCurrUser.getEmail(), new ResetPasswordByEmailListener() {
					@Override
					public void onSuccess() {
						AppUtils.toast1(getApplicationContext(), "已发送用户信息到您的邮箱，请在一个小时内进行操作");
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						LogUtil.e(TAG, "重置密码失败：arg=" + arg0 + "，arg1=" + arg1);
						AppUtils.bmobFailToast(getApplicationContext(), arg0);
					}
				});
			}
		} else if (!TextUtils.isEmpty(email)) {
			BmobUser.resetPasswordByEmail(getApplicationContext(), App.mCurrUser.getEmail(), new ResetPasswordByEmailListener() {
				@Override
				public void onSuccess() {
					AppUtils.toast1(getApplicationContext(), "已发送用户信息到您的邮箱，请在一个小时内进行操作");
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					LogUtil.e(TAG, "重置密码失败：arg=" + arg0 + "，arg1=" + arg1);
					AppUtils.bmobFailToast(getApplicationContext(), arg0);
				}
			});
		} else {
			// AppUtils.toast0(getApplicationContext(), "请您先注册或登录");

			mDialogView = new DialogView(this);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			mDialogView.setTitle(R.string.forget_user);
			mDialogView.setHint("请输入您的绑定邮箱");
			mDialogView.setHintSize1("请输入您的绑定邮箱", 14);

			mDialogView.setOnOKClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final String email = mDialogView.getText1();

					if (TextUtils.isEmpty(email)) {
						AppUtils.toast00(getApplicationContext(), R.string.input_email);
						return;
					}
					BmobUser.resetPasswordByEmail(RegisterActivity.this, email, new ResetPasswordByEmailListener() {
						@Override
						public void onSuccess() {
							mDialog.dismiss();
							DialogUtils.oneBtnDialog(RegisterActivity.this, getString(R.string.dialog_reset_pwd));
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							LogUtil.e(TAG, "重置密码失败：arg=" + arg0 + "，arg1=" + arg1);
							AppUtils.bmobFailToast(getApplicationContext(), arg0);
						}
					});
				}
			});
			mDialogView.setOnCancelClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mDialog.dismiss();
				}
			});
			builder.setView(mDialogView);
			mDialog = builder.show();

		}

	}

	private void enterHome() {
		Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
		startActivity(intent);
		finish();
	}

}
