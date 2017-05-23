package com.carrie.lldiary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.carrie.lldiary.R;
import com.carrie.lldiary.activity.PlanActivity;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.view.TitleView;

public abstract class MyBaseActivity extends Activity {
	
	private static final String TAG = "MyBaseActivity";

	private TitleView titleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.e(TAG, "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_base);

		ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
		View parentView = contentFrameLayout.getChildAt(0);
		if (parentView != null && Build.VERSION.SDK_INT >= 14) {
			parentView.setFitsSystemWindows(true);
		}

		FrameLayout frameLayout=(FrameLayout)findViewById(R.id.frame_container);

		View view=initView((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),contentFrameLayout);
		frameLayout.addView(view);

		TitleView titleView = (TitleView)findViewById(R.id.title_view);


		initData();

	}

	public abstract View initView(LayoutInflater inflater,ViewGroup container);

	/**
	 * 初始化页面数据
	 */
	public abstract void initData();

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			goBack();
			overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);
		}
		return super.onKeyDown(keyCode, event);
	}

	public abstract void goBack();



}
