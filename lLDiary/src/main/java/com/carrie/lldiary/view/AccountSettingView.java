package com.carrie.lldiary.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.EmailVerifyListener;
import cn.bmob.v3.listener.UpdateListener;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.activity.RegisterActivity;
import com.carrie.lldiary.fragment.AccountFragment.SdCard;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.CircleImage;
import com.carrie.lldiary.utils.Constant;
import com.carrie.lldiary.utils.LogUtil;

/**
 * 账户管理ITEM处理
 * 
 * @Description: TODO
 * @author Carrie
 * @version 创建时间：2016年4月27日 下午1:02:04
 */
public class AccountSettingView extends RelativeLayout {
	private static final String TAG = "AccountSettingView";
	
	public SettingItemView showUserInfo;// 修改用户名
	
	public SettingItemView modifyName;// 修改用户名
	public SettingItemView modifyPwd;// 修改密码
	public SettingItemView modifyAvatar;// 修改头像

	public SettingItemView bindingEmail;// 绑定邮箱
	public SettingItemView bindingQQ;// 绑定QQ
	public SettingItemView bindingWeibo;// 绑定微博
	public SettingItemView bindingWX;// 绑定微信

	public SettingItemView logOut;// 退出登录

	private DialogView mDialogView;
	private AlertDialog mDialog;
	
	

	private View view;

	public AccountSettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setupView();
	}

	public AccountSettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupView();
	}

	public AccountSettingView(Context context) {
		super(context);
		setupView();
	}

	private void setupView() {
		view = LayoutInflater.from(getContext()).inflate(R.layout.view_account_setting, this);
		showUserInfo = (SettingItemView) view.findViewById(R.id.show_user_info);
		modifyName = (SettingItemView) view.findViewById(R.id.modify_username);
		modifyPwd = (SettingItemView) view.findViewById(R.id.modify_userpwd);
		modifyAvatar = (SettingItemView) view.findViewById(R.id.modify_useravatar);
		bindingEmail = (SettingItemView) view.findViewById(R.id.binding_email);
		bindingQQ = (SettingItemView) view.findViewById(R.id.binding_qq);
		bindingWeibo = (SettingItemView) view.findViewById(R.id.binding_weibo);
		bindingWX = (SettingItemView) view.findViewById(R.id.binding_wx);
		logOut = (SettingItemView) view.findViewById(R.id.logout);

		setText();

		setLineColor();

		setIcon();

		setOutsideLineColor();

		setOnClickListener();

	}

	private void findView() {

	}

	private void setText() {
		showUserInfo.setText("你好，"+App.mSP_account.getString(Constant.account_username, ""));
		modifyName.setText(R.string.modify_username);
		modifyPwd.setText(R.string.modify_userpwd);
		modifyAvatar.setText(R.string.modify_useravatar);

		bindingEmail.setText("绑定邮箱");
		bindingQQ.setText(R.string.binding_qq);
		bindingWeibo.setText(R.string.binding_weibo);
		bindingWX.setText(R.string.binding_wx);

		logOut.setText(R.string.log_out);
	}

	/**
	 * <font color="#f97798">设置中间的横线主题颜色</font> <font color="#f97798"></font>
	 * 
	 * @return void
	 * @version 创建时间：2016年4月27日 上午11:55:53
	 */
	private void setLineColor() {
		showUserInfo.setLineColor();
		modifyName.setLineColor();
		modifyPwd.setLineColor();
		modifyAvatar.setLineColor();
		bindingEmail.setLineColor();
		bindingQQ.setLineColor();
		bindingWeibo.setLineColor();
		bindingWX.setLineColor();
		logOut.setLineColor();
	}

	/**
	 * <font color="#f97798">设置外框主题颜色</font> <font color="#f97798"></font>
	 * 
	 * @return void
	 * @version 创建时间：2016年4月27日 上午11:55:38
	 */
	private void setOutsideLineColor() {
		showUserInfo.setTopLineColor();
		modifyName.setTopLineColor();
		bindingEmail.setTopLineColor();
		logOut.setTopLineColor();

		showUserInfo.setBottomLineColor();
		modifyAvatar.setBottomLineColor();
		bindingWX.setBottomLineColor();
		logOut.setBottomLineColor();
	}

	private void setIcon() {
		showUserInfo.setBackground(R.drawable.menu);
		modifyName.setBackground(R.drawable.menu);
		modifyPwd.setBackground(R.drawable.menu);
		modifyAvatar.setBackground(R.drawable.menu);
		bindingEmail.setBackground(R.drawable.menu);
		bindingQQ.setBackground(R.drawable.menu);
		bindingWeibo.setBackground(R.drawable.menu);
		bindingWX.setBackground(R.drawable.menu);
		logOut.setBackground(R.drawable.menu);
	}

	private void setStatus() {

	}

	public void setOnClickListener() {

		showUserInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtils.toast0(getContext(), "*^_^*");
			}
		});
		modifyName.setOnClickListener(new OnModifyNameListener());
		modifyPwd.setOnClickListener(new OnModifyPwdListener());
		
		bindingEmail.setOnClickListener(new OnBindingEmailListener());
		// bindingQQ.setOnClickListener(listener);
		// bindingWeibo.setOnClickListener(listener);
		// bindingWX.setOnClickListener(listener);
		 logOut.setOnClickListener(new OnLogOutListener());
	}
	
	public void setOnAvatarListener(OnClickListener listener){
		modifyAvatar.setOnClickListener(listener);
	}

	private class OnModifyNameListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			mDialogView = new DialogView(getContext());

			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			mDialogView.setTitle(R.string.modify_username);
			mDialogView.setHint(R.string.input_new_username);

			mDialogView.setOnOKClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (TextUtils.isEmpty(mDialogView.getText1())) {
						AppUtils.toast00(getContext(), R.string.input_new_username);
						return;
					}
					BmobUser newUser = new BmobUser();
					newUser.setUsername(mDialogView.getText1());
					newUser.update(getContext(), App.mCurrUser.getObjectId(), new UpdateListener() {

						@Override
						public void onSuccess() {
							AppUtils.toast00(getContext(), R.string.toast_modify_success);
							App.mSP_account.edit().putString(Constant.account_username,mDialogView.getText1()).commit();
							mDialog.dismiss();
							removeView(mDialogView);
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							AppUtils.toast00(getContext(), R.string.toast_modify_failed);
							LogUtil.e(TAG, "修改用户名失败：arg0=" + arg0 + "，arg1=" + arg1);
							AppUtils.bmobFailToast(getContext(), arg0);
						}
					});
				}
			});
			mDialogView.setOnCancelClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mDialog.dismiss();
					removeView(mDialogView);
				}
			});
			builder.setView(mDialogView);
			mDialog = builder.show();
		}
	}

	private class OnModifyPwdListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			mDialogView = new DialogView(getContext());

			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			mDialogView.setTitle(R.string.modify_userpwd);
			mDialogView.setHint(R.string.input_orginal_userpwd);
			mDialogView.showEdit2nd(true, R.string.input_new_userpwd);
			mDialogView.showEdit3rd(true, R.string.input_confirm_userpwd);

			mDialogView.setOnOKClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String pwd = mDialogView.getText1();
					final String newPwd1 = mDialogView.getText2();
					String newPwd2 = mDialogView.getText3();

					if (TextUtils.isEmpty(pwd)) {
						AppUtils.toast00(getContext(), R.string.input_orginal_userpwd);
						mDialogView.setRequestFocus1();
						return;
					}
					if (TextUtils.isEmpty(newPwd1)) {
						AppUtils.toast00(getContext(), R.string.input_new_userpwd);
						mDialogView.setRequestFocus2();
						return;
					}
					if (TextUtils.isEmpty(newPwd2)) {
						mDialogView.setRequestFocus3();
						AppUtils.toast00(getContext(), R.string.input_confirm_userpwd);
						return;
					}
					if (!newPwd1.equals(newPwd2)) {
						AppUtils.toast11(getContext(), R.string.toast_pwd_not_equal);
						return;
					}

					BmobUser.updateCurrentUserPassword(getContext(), pwd, newPwd1, new UpdateListener() {

						@Override
						public void onSuccess() {
							AppUtils.toast00(getContext(), R.string.toast_modify_success);
							App.mSP_account.edit().putString(Constant.account_userpwd, newPwd1).commit();
							mDialog.dismiss();
							removeView(mDialogView);
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							LogUtil.e(TAG, "修改失败+arg0=" + arg0 + "，arg1=" + arg1);
							if (arg0 == 210) {// old password incorrect
								AppUtils.toast11(getContext(), R.string.toast_pwd_not_equal_orginal);
							} else {
										AppUtils.bmobFailToast(getContext(), arg0);
							}
						}
					});
				}
			});
			mDialogView.setOnCancelClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mDialog.dismiss();
					removeView(mDialogView);
				}
			});
			builder.setView(mDialogView);
			mDialog = builder.show();
		}
	}

	private class OnBindingEmailListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			mDialogView = new DialogView(getContext());

			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			mDialogView.setTitle(R.string.binding_email);
			mDialogView.setHint(R.string.hint_email);
			mDialogView.setHintSize1(getContext().getString(R.string.hint_email), 12);

			mDialogView.setOnOKClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final String email = mDialogView.getText1();

					if (TextUtils.isEmpty(email)) {
						AppUtils.toast00(getContext(), R.string.input_email);
						return;
					}
					BmobUser user = new BmobUser();
					user.setEmail(email);
					user.setEmailVerified(true);
					user.update(getContext(), App.mCurrUser.getObjectId(), new UpdateListener() {

						@Override
						public void onSuccess() {
							AppUtils.toast00(getContext(), R.string.toast_binding_success);
							App.mSP_account.edit().putString(Constant.account_useremail,email).commit();
							
							BmobUser.requestEmailVerify(getContext(), email, new EmailVerifyListener() {

								@Override
								public void onSuccess() {
									AppUtils.toast00(getContext(), R.string.toast_binding_success);
									mDialog.dismiss();
									removeView(mDialogView);
									AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
									builder.setMessage(getContext().getString(R.string.toast_binding_email_success))
											.setPositiveButton(getContext().getString(R.string.OK), new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int which) {
													dialog.dismiss();
												}
											}).show();
								}

								@Override
								public void onFailure(int arg0, String arg1) {
									AppUtils.toast00(getContext(), R.string.toast_binding_failed);
									LogUtil.e(TAG, "绑定邮箱失败：arg0=" + arg0 + "，arg1=" + arg1);

									if (arg0 == 205) {
										AppUtils.toast00(getContext(), R.string.toast_email_not_exsits);
									} else if (arg0 == 301) {
										AppUtils.toast00(getContext(), R.string.toast_email_not_valid);
									} else {
										AppUtils.bmobFailToast(getContext(), arg0);
									}
								}
							});
							
							
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							AppUtils.toast00(getContext(), R.string.toast_modify_failed);
							LogUtil.e(TAG, "绑定邮箱失败：arg0=" + arg0 + "，arg1=" + arg1);
							AppUtils.bmobFailToast(getContext(), arg0);
						}
					});
					
				}
			});
			mDialogView.setOnCancelClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mDialog.dismiss();
					removeView(mDialogView);
				}
			});
			builder.setView(mDialogView);
			mDialog = builder.show();

		}
	}
	
	private class OnLogOutListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle("退出登录").setMessage("确定退出登录？")
					.setPositiveButton(getContext().getString(R.string.OK), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {//退出登录
							BmobUser.logOut(getContext());   //清除缓存用户对象
							App.mCurrUser= BmobUser.getCurrentUser(getContext()); // 现在的currentUser是null了
							Intent intent=new Intent(getContext(),RegisterActivity.class);
							getContext().startActivity(intent);
						}
					})
					.setNegativeButton(getContext().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();
		}
		
	}
	
	
}
