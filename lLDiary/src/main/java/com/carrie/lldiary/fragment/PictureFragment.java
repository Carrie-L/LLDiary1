package com.carrie.lldiary.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carrie.lldiary.R;


public class PictureFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_test, container,false);
		view.findViewById(R.id.iv).setBackgroundColor(getResources().getColor(R.color.primaryColor));
		return view;
	}

}
