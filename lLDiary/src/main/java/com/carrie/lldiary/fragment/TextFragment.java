package com.carrie.lldiary.fragment;

import com.carrie.lldiary.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TextFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_test, container,false);
		view.findViewById(R.id.iv).setBackgroundColor(getResources().getColor(R.color.fruit_green));
		return view;
	}

}
