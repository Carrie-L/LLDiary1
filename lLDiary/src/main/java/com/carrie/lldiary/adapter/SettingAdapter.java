package com.carrie.lldiary.adapter;

import java.util.List;

import com.carrie.lldiary.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SettingAdapter extends BaseAdapter {
	private List<String> lists;
	private Context mContext;
	private LayoutInflater mInflater;
	
	

	public SettingAdapter(List<String> lists, Context mContext) {
		super();
		this.lists = lists;
		this.mContext = mContext;
		mInflater=LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=new ViewHolder();
	//	if(convertView==null){
			convertView=mInflater.inflate(R.layout.list_item_setting, null);
			TextView	tv_name=(TextView) convertView.findViewById(R.id.tv_setting_item_name);
			convertView.setTag(holder);
//		}else{
//			holder=(ViewHolder) convertView.getTag();
//		}
		tv_name.setText(lists.get(position).toString());
		return convertView;
	}
	
	class ViewHolder{
		TextView tv_name;
	}

}
