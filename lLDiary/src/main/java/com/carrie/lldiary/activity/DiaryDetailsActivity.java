package com.carrie.lldiary.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.carrie.lldiary.R;
import com.carrie.lldiary.entity.ContentEntity;
import com.carrie.lldiary.helper.FaceConversionUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

public class DiaryDetailsActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.tv_details_title)
	private TextView tv_title;
	@ViewInject(R.id.tv_details_body)
	private TextView tv_body;
	@ViewInject(R.id.tv_details_time)
	private TextView tv_time;
	@ViewInject(R.id.tv_details_edit)
	private TextView tv_edit;
	@ViewInject(R.id.ib_details_back)
	private ImageButton ib_back;
	@ViewInject(R.id.sv_details_scrolls)
	private ScrollView sv_bg;
	
	private List<ContentEntity> entities;
	private Intent intent;
	private String title;
	private String time;
	private String body;
	private int bg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_details);		
		ViewUtils.inject(this);
		MyActivityManager.add(this);
		
		
		
		entities=DiaryActivity.entities;
		Intent intent=getIntent();
		title = intent.getStringExtra("title");
		time = intent.getStringExtra("date")+" "+intent.getStringExtra("time");;
		body = intent.getStringExtra("content");;		
		bg = intent.getIntExtra("bg",0);;
		tv_title.setText(title);
		tv_time.setText(time);	
		tv_body.setText(body);
		PushAgent.getInstance(this).onAppStart();//统计应用启动数据
		if(bg==0)
			sv_bg.setBackgroundResource(R.drawable.txt_theme01);
		else
			sv_bg.setBackgroundResource(bg);
		tv_edit.setOnClickListener(this);
		ib_back.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_details_edit:
			intent = new Intent(this,DiaryEditActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("time", time);
			intent.putExtra("body", body);
			intent.putExtra("bg", bg);
			startActivity(intent);
			finish();
			break;
		case R.id.ib_details_back:
			finish();
			break;

		default:
			break;
		}
	}

	//友盟统计
		public void onResume() {
			super.onResume();
			MobclickAgent.onResume(this);
			StatService.onPageStart(this, "日记详情页");
		}

		public void onPause() {
			super.onPause();
			MobclickAgent.onPause(this);
			StatService.onPageEnd(this, "日记详情页");
		}
	
}
