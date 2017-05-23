package com.carrie.lldiary.adapter;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Space;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.entity.ContentEntity;
import com.carrie.lldiary.utils.DateUtil;

public class LLAdapter extends BaseAdapter {
	public List<ContentEntity> entities;
	private LayoutInflater mInflater;
	private ByteArrayInputStream bais;
	private Context mContext;
	private SharedPreferences sp;
	

	public LLAdapter(List<ContentEntity> entities, Context context) {
		super();
		this.entities = entities;
		this.mContext = context;
		mInflater=LayoutInflater.from(context);
		sp=mContext.getSharedPreferences("diary", 0);
	}

	public LLAdapter(Context context) {
		super();
		this.mContext = context;
		mInflater=LayoutInflater.from(context);
		sp=mContext.getSharedPreferences("diary", 0);
		System.out.println("LLAdapter(context)");
	}

	public LLAdapter(List<ContentEntity> entities) {
		super();
		this.entities = entities;
	}

	@Override
	public int getCount() {
		System.out.println("getCount():"+entities.size());
		return entities.size();
	}

	@Override
	public Object getItem(int position) {
		System.out.println("getItem()");
		return position;
	}

	@Override
	public long getItemId(int position) {
		System.out.println("getItemId()");
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("getView");
		ViewHolder holder=new ViewHolder();
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.list_item_diary, null);
			holder.tv_diary_title=(TextView) convertView.findViewById(R.id.tv_item_diary_title);
			holder.tv_diary_content=(TextView) convertView.findViewById(R.id.tv_item_diary_content);
			holder.tv_diary_date=(TextView) convertView.findViewById(R.id.tv_item_diary_date);						
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.tv_diary_title.setText(entities.get(position).getTitle());
		holder.tv_diary_content.setText(entities.get(position).getContent());
		holder.tv_diary_date.setText(entities.get(position).getDate());				
		return convertView;
	}

	class ViewHolder{
		TextView tv_diary_title,tv_diary_content,tv_diary_date,tv_diary_time;
	}
}
