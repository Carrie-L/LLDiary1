package com.carrie.lldiary.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.SettingAdapter;
import com.carrie.lldiary.base.BaseFragment;

public class LeftDrawerFragment extends BaseFragment implements OnItemClickListener{
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

	@Override
	public void initData(Bundle savedInstanceState) {
		BaseFragment.sClsName="LeftDrawer";		
	}

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container) {
		View view=inflater.inflate(R.layout.layout, null);
		mScreenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();

		//mDrawerLayout = (DrawerLayout) findViewById(R.id.layout_drawer);
		initLeftDrawer();
		return view;
	}
	

	public void initLeftDrawer() {
		list = new ArrayList<String>();
		List<String> lists = new ArrayList<String>();
		list.add("开启提醒");
		list.add("桌面换肤");
		list.add("密码锁");
		list.add("创建桌面快捷图标");
		list.add("自动检测新版本");
		list.add("关于恋恋日记本");
		list.add("反馈问题");
		adapter = new SettingAdapter(list, mContext);
		System.out.println("list=" + list);
		lv_setting_drawer.setAdapter(adapter);
		lv_setting_drawer.setOnItemClickListener(this);

		mTitle = (String) ((Activity) mContext).getTitle();
		mDrawerToggle = new ActionBarDrawerToggle((Activity) mContext, mDrawerLayout,
				R.drawable.drawer, R.string.drawer_open, R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				((Activity) mContext).getActionBar().setTitle(mTitle);
				((Activity) mContext).invalidateOptionsMenu();// 自动回调onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActivity().getActionBar().setTitle("设置");
				getActivity().invalidateOptionsMenu();// 自动回调onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		// 开启ActionBar上APP ICON的功能
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	public void onPostCreate(Bundle savedInstanceState) {
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
		// TODO Auto-generated method stub
		
	}

}
