package com.carrie.lldiary.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.activity.HomeActivity;
import com.carrie.lldiary.activity.SplashActivity;
import com.carrie.lldiary.base.BaseFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AboutFragment extends BaseFragment {
	@ViewInject(R.id.ib_back)
	private ImageButton ib_back;
	@ViewInject(R.id.ib_add)
	private ImageButton ib_add;
	public static final int INVISIBLE = 0x00000004;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.tv_version)
	private TextView tv_version;

	@Override
	public void initData(Bundle savedInstanceState) {
		BaseFragment.sClsName="About";
		
		ib_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext,HomeActivity.class);
				startActivity(intent);
			}
		});
		ib_add.setVisibility(View.INVISIBLE);
		tv_title.setText("关于");
		tv_version.setText("当前版本："+SplashActivity.getVersionName());
	}

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container) {
		View view=inflater.inflate(R.layout.layout_about, null);
		ViewUtils.inject(this,view);
		return view;
	}

}
