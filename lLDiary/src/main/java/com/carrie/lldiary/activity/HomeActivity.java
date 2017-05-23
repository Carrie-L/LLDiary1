package com.carrie.lldiary.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.SettingAdapter;
import com.carrie.lldiary.fragment.AboutFragment;
import com.carrie.lldiary.fragment.AccountFragment;
import com.carrie.lldiary.fragment.ChangeStyleFragment;
import com.carrie.lldiary.fragment.HomeFragment;
import com.carrie.lldiary.fragment.PasswordFragment;
import com.carrie.lldiary.service.MyPushIntentService;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.fragment.FeedbackFragment;
import com.umeng.fb.push.FeedbackPush;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 侧边栏功能
 	* @Description: TODO
 	* @author Carrie
 	* @version 创建时间：2016年4月26日 下午7:50:16
 */
public class HomeActivity extends Activity implements OnItemClickListener, Toolbar.OnMenuItemClickListener {
	protected static final String TAG = null;

	private long exitTime = 0;

	private FragmentManager fm;
	private int mScreenWidth;
	private String mTitle;
	private ActionBarDrawerToggle mDrawerToggle;
	private List<String> list;
	// private ArrayAdapter<String> adapter;
	private ListView lv_setting_drawer;
	private DrawerLayout mDrawerLayout;
	private SettingAdapter adapter;
	private SharedPreferences sp;

	private FeedbackAgent fb;
	
	private Context mContext;
	private Toolbar toolbar;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setBehindContentView(R.layout.fragment_menu);//滑动的那个页面
		setContentView(R.layout.activity_home);
		MyActivityManager.add(this);
		mScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
		
		mContext=getApplicationContext();

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		toolbar.inflateMenu(R.menu.tool_bar);
		toolbar.setNavigationIcon(R.drawable.ic_menu);
		toolbar.setOnMenuItemClickListener(this);

		lv_setting_drawer = (ListView) findViewById(R.id.lv_setting_drawer);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.layout_drawer);
		fm = getFragmentManager();
		fm.beginTransaction().replace(R.id.fl_main_content, new HomeFragment())
				.commit();
		PushAgent.getInstance(this).onAppStart();//统计应用启动数据
		initLeftDrawer();
		sp = getSharedPreferences("config", MODE_PRIVATE);

		getScreenSize();
	}

	private void getScreenSize(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width=metrics.widthPixels;
		int height=metrics.heightPixels;
		LogUtil.i(TAG,"width="+width+",height="+height);
		App.mSP_config.edit().putInt("ScreenWidth",width).putInt("ScreenHeight",height).apply();
	}

	public void initLeftDrawer() {
		list = new ArrayList<String>();
		List<String> lists = new ArrayList<String>();
		list.add("账号管理");
		list.add("桌面换肤");
		list.add("密码锁");
		list.add("创建桌面快捷图标");
		list.add("自动检测新版本");
		list.add("关于恋恋日记本");
		list.add("反馈问题");
		adapter = new SettingAdapter(list, this);
		System.out.println("list=" + list);
		lv_setting_drawer.setAdapter(adapter);
		lv_setting_drawer.setOnItemClickListener(this);

		mTitle = (String) getTitle();
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				toolbar, R.string.drawer_open, R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				toolbar.setTitle(mTitle);
				invalidateOptionsMenu();// 自动回调onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				toolbar.setTitle("设置");
				invalidateOptionsMenu();// 自动回调onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		// 开启ActionBar上APP ICON的功能
//		toolbar.setDisplayHomeAsUpEnabled(true);
//		toolbar.setHomeButtonEnabled(true);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// 需要将ActionDrawerToggle与DrawerLayout的状态同步
		// 将ActionBarDrawerToggle中的drawer图标，设置为ActionBar中的Home-Button的Icon
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		//
		boolean isDrawerOpen = mDrawerLayout.isDrawerOpen(lv_setting_drawer);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 将ActionBar上的图标与Drawer结合起来
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			item.setVisible(false);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final Editor editor = sp.edit();
		switch (position) {
		case 0:// 账户管理
			fm.beginTransaction()
					.replace(R.id.fl_main_content, new AccountFragment())
					.commit();
			mDrawerLayout.closeDrawer(lv_setting_drawer);
			break;
		case 1:// 桌面换肤
			fm.beginTransaction()
					.replace(R.id.fl_main_content, new ChangeStyleFragment())
					.commit();
			mDrawerLayout.closeDrawer(lv_setting_drawer);
			break;
		case 2:// 密码锁
			fm.beginTransaction()
					.replace(R.id.fl_main_content, new PasswordFragment())
					.commit();
			mDrawerLayout.closeDrawer(lv_setting_drawer);

			break;
		case 3:// 创建桌面快捷图标
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setTitle("确定创建桌面快捷图标？")
					.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							installShortCut();
							dialog.dismiss();
							mDrawerLayout.closeDrawer(lv_setting_drawer);
						}
					}).setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();
			break;
		case 4:// 自动检测新版本
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("是否自动检测新版本？").setMessage("开启后将在软件启动时检测")
					.setPositiveButton("开启", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							editor.putString("autoCheckUpdate", "yes");
							editor.commit();
							dialog.dismiss();
							Toast.makeText(HomeActivity.this, "已开启", Toast.LENGTH_SHORT).show();
							mDrawerLayout.closeDrawer(lv_setting_drawer);
						}
					}).setNegativeButton("关闭", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							editor.putString("autoCheckUpdate", "no");
							editor.commit();
							dialog.dismiss();
							Toast.makeText(HomeActivity.this, "已关闭",  Toast.LENGTH_SHORT).show();
							mDrawerLayout.closeDrawer(lv_setting_drawer);
						}
					}).show();
			break;
		case 5:// 关于
			fm.beginTransaction()
					.replace(R.id.fl_main_content, new AboutFragment())
					.commit();
			mDrawerLayout.closeDrawer(lv_setting_drawer);
			break;
		case 6:// 反馈(headphoto)
			Intent intent = new Intent();
            intent.setClass(this, ConversationDetailActivity.class);
            String id1 = new FeedbackAgent(this).getDefaultConversation().getId();
            intent.putExtra(FeedbackFragment.BUNDLE_KEY_CONVERSATION_ID, id1);
            startActivity(intent);
			com.umeng.fb.util.Log.LOG = true;
		    setUpUmengFeedback();
		    mDrawerLayout.closeDrawer(lv_setting_drawer);
			break;

		default:
			break;
		}
	}

	/**
	 * 创建桌面快捷图标
	 */
	private void installShortCut() {
		boolean shortcut = sp.getBoolean("shortcut", false);
		if (shortcut) {// 如果桌面已存在快捷图标，则不创建
			return;
		}
		Editor editor = sp.edit();
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(this, R.drawable.logo));// 快捷图标
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "恋恋日记本");// 名称
		Intent clickIntent = new Intent();
		clickIntent.setAction("android.intent.action.MAIN");
		clickIntent.addCategory("android.intent.category.LAUNCHER");
		clickIntent.setClassName(getPackageName(),
				"com.carrie.lldiary.SplashActivity");
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, clickIntent);
		sendBroadcast(intent);
		editor.putBoolean("shortcut", true);
		editor.commit();
	}

	/**
	 * 开启提醒
	 */
	public void openRemind() {
		final Editor editor = sp.edit();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("是否开启提醒？")
				.setPositiveButton("开启", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						editor.putBoolean("isRemind", true);
						editor.commit();
						dialog.dismiss();
						mDrawerLayout.closeDrawer(lv_setting_drawer);
					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						editor.putBoolean("isRemind", false);
						editor.commit();
						dialog.dismiss();
						mDrawerLayout.closeDrawer(lv_setting_drawer);
					}
				}).show();
	}

	private void setUpUmengFeedback() {
		fb = new FeedbackAgent(this);
		// check if the app developer has replied to the feedback or not.
		fb.sync();
		fb.openAudioFeedback();
		fb.openFeedbackPush();

		// fb.setWelcomeInfo();
		fb.setWelcomeInfo("请输入您的意见和建议，您的支持是作者最大的动力，谢谢。");
		FeedbackPush.getInstance(this).init(true);
		PushAgent.getInstance(this).setPushIntentServiceClass(MyPushIntentService.class);

		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = fb.updateUserInfo();
			}
		}).start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			ActivityManager manager = (ActivityManager)   getSystemService(Context.ACTIVITY_SERVICE);    
			List<RunningTaskInfo> runningTasks =manager.getRunningTasks(1);    
			RunningTaskInfo cinfo = runningTasks.get(0);    
			ComponentName component = cinfo.topActivity;    
			//Log.e("current activity is ", component.getClassName());  
			System.out.println("类名="+component.getClassName());
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				MyActivityManager.finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//友盟统计
		public void onResume() {
			super.onResume();
			MobclickAgent.onResume(this);
			StatService.onPageStart(this, "主界面");
		}

		public void onPause() {
			super.onPause();
			MobclickAgent.onPause(this);
			StatService.onPageEnd(this, "主界面");
		}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		int itemId=item.getItemId();
		if(itemId==R.id.action_more_function){
			Toast.makeText(mContext,getString(R.string.tool_bar_more_function),Toast.LENGTH_SHORT).show();
		}else if(itemId==R.id.action_more_setting){
			Toast.makeText(mContext,getString(R.string.tool_bar_setting),Toast.LENGTH_SHORT).show();
		}
		return true;
	}
}
