package com.carrie.lldiary.base;

import com.baidu.mobstat.StatService;
import com.umeng.analytics.MobclickAgent;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	protected View mRootView;
	protected Context mContext;
	protected LayoutInflater mInflater;
	public static String sClsName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mRootView = initView(inflater, container);

		return mRootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData(savedInstanceState);
	}

	public abstract void initData(Bundle savedInstanceState);

	public abstract View initView(LayoutInflater inflater, ViewGroup container);

	public void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	// 友盟统计|百度统计
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(sClsName);
		MobclickAgent.onResume(mContext);
		StatService.onPageStart(mContext, sClsName);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageStart(sClsName);
		MobclickAgent.onPause(mContext);
		StatService.onPageEnd(mContext, sClsName);
	}

}
