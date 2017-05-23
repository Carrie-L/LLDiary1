package com.carrie.lldiary.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.carrie.lldiary.R;
import com.carrie.lldiary.utils.LogUtil;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

	protected static final int UPDATE_NOW = 0;
	protected static final int IGNORE = 1;
	protected static final int NOT_NOW = 2;
	protected static final int ENTER_HOME = 0;
	private SharedPreferences sp;
	TextView tv_splash_version, tv_update_progress;
	private static Context mContext;
	@ViewInject(R.id.progressBar1)
	private ProgressBar progressBar;



	protected static final int WHOLE = 1;
	protected static final String TAG = "SplashActivity";
	
	String accessKey = "17430gg0QImZdL3YZSDv";
    String secretKey = "26460a3cfa6da139f27cda012307827c40d8213b";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		mContext = this;



		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("版本号：" + getVersionName());
		tv_update_progress = (TextView) findViewById(R.id.tv_update_progress);
		sp = getSharedPreferences("config", MODE_PRIVATE);

		FeedbackAgent agent = new FeedbackAgent(this);
		agent.sync();// 反馈回复通知
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		mPushAgent.onAppStart();// 统计应用启动数据
		String device_token = UmengRegistrar.getRegistrationId(this);
		// Log.i("SplachActivity", device_token);

		/*
		 * agent.openFeedbackPush(); PushAgent.getInstance(this).enable();
		 * FeedbackPush.getInstance(this).init(false);
		 */
		update();
		// 检测用户是否开启了自动更新.只要不为否，则都自动更新
		if (sp.getString("autoCheckUpdate", "").equals("no")) {
			delayEnter();
		} else {
			// update();
		}

//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				getObject();
//			}
//		}).start();

	}


	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ENTER_HOME:
				enterHome();
				break;

			default:
				break;
			}
		};
	};
	private Intent intent;
	private String resultString;

	/**
	 * 延迟2s进入主界面
	 */
	public void delayEnter() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = ENTER_HOME;
				handler.sendMessage(msg);
			}
		};
		timer.schedule(task, 1000);
	}

	/**
	 * 进入主界面
	 */
	protected void enterHome() {
		int cbPwd = sp.getInt("CbPwd", 0);
		if (cbPwd == WHOLE) {
			intent = new Intent(this, WholePasswordActivity.class);
		} else {
	//		intent = new Intent(this, HomeActivity.class);
			intent = new Intent(this, RegisterActivity.class);
		}

		startActivity(intent);
		finish();
	}

	/**
	 * 得到应用程序的版本信息
	 * 
	 * @return
	 */
	public static String getVersionName() {
		PackageManager pManager = mContext.getPackageManager();
		try {
			PackageInfo packInfo = pManager.getPackageInfo("com.carrie.lldiary", 0);
			return packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	// 友盟统计
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void update() {
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
					if (UmengUpdateAgent.isIgnore(mContext, updateInfo)) {
						LogUtil.i(TAG, "忽略更新");
						delayEnter();
					}
					UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
						@Override
						public void onClick(int status) {
							switch (status) {
							case UpdateStatus.Update:// 用户选择现在更新;
								tv_update_progress.setVisibility(View.VISIBLE);
								tv_update_progress.setText("正在下载更新文件...");
								enterHome();
								break;
							case UpdateStatus.Ignore:// 用户选择忽略该版;
								tv_update_progress.setVisibility(View.VISIBLE);
								tv_update_progress.setText("正在载入...");
								enterHome();
								break;
							case UpdateStatus.NotNow:// 用户选择以后再说，点击回退键，关闭对话框。
														// 监听下载进度
								delayEnter();
								tv_update_progress.setVisibility(View.VISIBLE);
								tv_update_progress.setText("正在载入...");
								break;
							default:
								break;
							}
						}
					});
					break;
				case UpdateStatus.No: // has no update
					delayEnter();
					break;
				case UpdateStatus.NoneWifi: // none wifi
					delayEnter();
					break;
				case UpdateStatus.Timeout: // time out
					Toast.makeText(mContext, "超时", Toast.LENGTH_SHORT).show();
					delayEnter();
					break;
				}
			}
		});
		UmengUpdateAgent.update(this);
		// // 自定义对话框

	}
}
