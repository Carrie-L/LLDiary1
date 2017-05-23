package com.carrie.lldiary.activity;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;


import org.json.JSONException;
import org.json.JSONObject;

import android.R.interpolator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Entity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.carrie.lldiary.HttpGetData;
import com.carrie.lldiary.R;
import com.carrie.lldiary.HttpGetData.HttpGetDataListener;
import com.carrie.lldiary.R.array;
import com.carrie.lldiary.R.id;
import com.carrie.lldiary.R.layout;
import com.carrie.lldiary.adapter.RobotAdapter;
import com.carrie.lldiary.entity.RobotEntity;
import com.carrie.lldiary.utils.DateUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

public class RobotActivity extends Activity implements OnClickListener,HttpGetDataListener {
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.ib_clear)
	private ImageButton ib_clear;
	@ViewInject(R.id.ib_back)
	private ImageButton ib_back;
	@ViewInject(R.id.ib_robot_send)
	private ImageButton ib_send;
	@ViewInject(R.id.et_robot_content)
	private EditText et_content;
	@ViewInject(R.id.lv_robot)
	private ListView lv;	
	@ViewInject(R.id.ib_robot_delete)
	private ImageButton ib_delete;
	
	private RobotAdapter adapter;
	private List<RobotEntity> entities;
	private HttpGetData mData;
	private String content,time,headPhoto,name;
	private ByteArrayInputStream bais;
	private SharedPreferences sp;
	private Drawable photo;
	private RobotEntity entity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_robot);
		MyActivityManager.add(this);
		ViewUtils.inject(this);
		PushAgent.getInstance(this).onAppStart();//统计应用启动数据
		initView();
		initData();
	}

	private void initView() {
		tv_title.setText("小恋机器人");
	}

	private void initData() {
		ib_back.setOnClickListener(this);
		ib_send.setOnClickListener(this);
		ib_clear.setOnClickListener(this);
		ib_delete.setOnClickListener(this);
		entities=new ArrayList<RobotEntity>();
		adapter=new RobotAdapter(entities, this);
		lv.setAdapter(adapter);
		time=DateUtil.date3();
		sp=getSharedPreferences("config", 0);
		
		entity=new RobotEntity(getRandomWelcome(), RobotEntity.RECEIVER, time);
		entities.add(entity);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			break;
		case R.id.ib_robot_send:
			content=et_content.getText().toString().trim();
			String dropKG=content.replace(" ", "");//去掉空格
			String dropHC=dropKG.replace("\n", "");//去掉回车
			et_content.setSelection(dropHC.length());
//			bais = headPhoto();
			entity=new RobotEntity(content, RobotEntity.SEND, time);
			entity.setPhoto(sp.getString("photo", ""));
			entity.setType(RobotEntity.SEND);
			entities.add(entity);
			//如果聊天内容大于100条，清除
			if(entities.size()>100){
				for(RobotEntity i:entities){
					entities.remove(i);
				}
			}
			adapter.notifyDataSetChanged();
			String url="http://www.tuling123.com/openapi/api?key=981b434aa797b7b431c841945daf8a16&info="+dropHC;
			mData=(HttpGetData) new HttpGetData(this,url).execute();
			break;
		case R.id.ib_clear://清空所有消息
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setTitle("清空所有消息吗？")
			.setPositiveButton("是的", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					entities.clear();
					adapter.notifyDataSetChanged();
					dialog.dismiss();
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
			break;
		case R.id.ib_robot_delete:
			et_content.setText("");
			break;
		default:
			break;
		}
	}

	private String getRandomWelcome(){
		String welcome=null;
		String[] welcomeArrays=getResources().getStringArray(R.array.welcome_tips);
		int index=(int) (Math.random()*welcomeArrays.length-1);
		welcome=welcomeArrays[index];
		return welcome;
	}
	
	@Override
	public void getDataUrl(String data) {
		parseJson(data);
	}
	
	public void parseJson(String data){
		try {
			JSONObject  jObject=new JSONObject(data);
			entity = new RobotEntity(jObject.getString("text"), RobotEntity.RECEIVER, time);
			System.out.println("接受了一条消息:"+jObject.getString("text"));
			entity.setType(RobotEntity.RECEIVER);
			entities.add(entity);
			adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从xml文件中装载头像。从XML文件中读取Base64格式的字符串，然后将其解码成字节数组， 最后将字节数组转换成Drawable对象。
	 * 
	 * @return
	 */
//	public ByteArrayInputStream headPhoto() {
//		String imageBase64 = sp.getString("photo", "");
////		byte[] base64Bytes = Base64.decodeBase64(imageBase64.getBytes());
////		base64Bytes = Base64.decodeBase64(imageBase64.getBytes());
//		ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
//		bais = new ByteArrayInputStream(base64Bytes);
//		return bais;
//	}
	
	// 友盟统计
		public void onResume() {
			super.onResume();
			MobclickAgent.onResume(this);
			StatService.onPageStart(this, "小恋机器人");
		}

		public void onPause() {
			super.onPause();
			MobclickAgent.onPause(this);
			StatService.onPageEnd(this, "小恋机器人");
		}
	
	
}
