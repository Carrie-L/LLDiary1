package com.carrie.lldiary.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



public class TabFragmentAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragments;

	public TabFragmentAdapter(FragmentManager fm,ArrayList<Fragment> lists) {
		super(fm);
		this.fragments=lists;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}

}
