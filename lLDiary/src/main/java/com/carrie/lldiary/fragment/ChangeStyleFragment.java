package com.carrie.lldiary.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.carrie.lldiary.R;
import com.carrie.lldiary.activity.HomeActivity;
import com.carrie.lldiary.base.BaseFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;

/**
 * 桌面换肤
 * @author Carrie
 *
 */
public class ChangeStyleFragment extends BaseFragment implements
		OnClickListener {
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.ib_style1)
	private ImageButton ib_style1;
	@ViewInject(R.id.ib_style2)
	private ImageButton ib_style2;
	@ViewInject(R.id.ib_style3)
	private ImageButton ib_style3;
	@ViewInject(R.id.ib_save)
	private ImageButton ib_save;
	@ViewInject(R.id.ib_selected)
	private ImageButton ib_selected1;
	@ViewInject(R.id.ib_selected2)
	private ImageButton ib_selected2;
	@ViewInject(R.id.ib_selected3)
	private ImageButton ib_selected3;
	@ViewInject(R.id.ib_back)
	private ImageButton ib_back;


	private SharedPreferences sp;
	public static final int INVISIBLE = 0x00000004;
	public static final int VISIBLE = 0x00000000;

	@Override
	public void initData(Bundle savedInstanceState) {
		ib_save.setOnClickListener(this);
		ib_back.setOnClickListener(this);
		ib_style1.setOnClickListener(this);
		ib_style2.setOnClickListener(this);
		ib_style3.setOnClickListener(this);
		tv_title.setText("桌面换肤");

	}

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container) {
		BaseFragment.sClsName="ChangeStyle";
		
		View view = inflater.inflate(R.layout.layout_change_style, null);
		ViewUtils.inject(this, view);
		sp = mContext.getSharedPreferences("config", 0);
		ib_selected1.setVisibility(INVISIBLE);
		ib_selected2.setVisibility(INVISIBLE);
		ib_selected3.setVisibility(INVISIBLE);
		return view;
	}

	@Override
	public void onClick(View v) {
		Editor editor = sp.edit();
		switch (v.getId()) {
		case R.id.ib_save:
			Intent intent=new Intent(mContext,HomeActivity.class);
			startActivity(intent);
			break;
		case R.id.ib_style1:
			ib_selected1.setVisibility(VISIBLE);
			ib_selected2.setVisibility(INVISIBLE);
			ib_selected3.setVisibility(INVISIBLE);
			editor.putInt("home_diary", R.drawable.home_diary1);
			editor.putInt("home_note", R.drawable.home_note1);
			editor.putInt("home_plan", R.drawable.home_plan1);
			editor.putInt("home_ann", R.drawable.home_ann1);
			editor.putInt("home_robot", R.drawable.home_robot1);
			editor.putInt("home_money", R.drawable.home_money1);
			editor.putInt("home_bg", R.drawable.home_bg1);
			editor.commit();
			break;
		case R.id.ib_style2:
			ib_selected1.setVisibility(INVISIBLE);
			ib_selected2.setVisibility(VISIBLE);
			ib_selected3.setVisibility(INVISIBLE);
			editor.putInt("home_diary", R.drawable.home_diary2);
			editor.putInt("home_note", R.drawable.home_note2);
			editor.putInt("home_plan", R.drawable.home_plan2);
			editor.putInt("home_ann", R.drawable.home_ann2);
			editor.putInt("home_robot", R.drawable.home_robot2);
			editor.putInt("home_money", R.drawable.home_money2);
			editor.putInt("home_bg", R.drawable.bg);
			editor.commit();
			break;
		case R.id.ib_style3:
			ib_selected1.setVisibility(INVISIBLE);
			ib_selected2.setVisibility(INVISIBLE);
			ib_selected3.setVisibility(VISIBLE);
			editor.putInt("home_diary", R.drawable.home_diary3);
			editor.putInt("home_note", R.drawable.home_note3);
			editor.putInt("home_plan", R.drawable.home_plan3);
			editor.putInt("home_ann", R.drawable.home_ann3);
			editor.putInt("home_robot", R.drawable.home_robot3);
			editor.putInt("home_money", R.drawable.home_money3);
			editor.putInt("home_bg", R.drawable.home_bg3);
			editor.commit();
			break;
		case R.id.ib_back:
			Intent intent2=new Intent(mContext,HomeActivity.class);
			startActivity(intent2);
			break;

		default:
			break;
		}
	}
	
}
