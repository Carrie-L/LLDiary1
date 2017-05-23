package com.carrie.lldiary.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;


public abstract class BasePage{
	protected Context mContext;
	protected LayoutInflater mInflater;
	protected View mRootView;
	protected Bundle bundle;
	
	public void setBundle(Bundle bundle){
		this.bundle=bundle;
	}
	
	public BasePage(Context context){
		this.mContext=context;
		mInflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
		mRootView=initView(mInflater);
		initData();
	}

	/**
	 * 初始化页面布局
	 * @param inflater
	 * @return
	 */
	public abstract View initView(LayoutInflater inflater);
	
	/**
	 * 初始化页面数据
	 */
	public abstract void initData();
	
	/**
	 * 当前页面获取焦点是调用此方法
	 */
	public void onResume(){}
	
	/**
	 * 当前页面失去焦点是调用此方法
	 */
	public void onPause(){}
	
	public View getRootView(){
		return this.mRootView;
	}
	

	
}
