package com.carrie.lldiary.adapter;

import java.util.List;

import com.carrie.lldiary.R;
import com.carrie.lldiary.entity.NoteEntity;
import com.carrie.lldiary.entity.PlanEntity;
import com.lidroid.xutils.db.sqlite.CursorUtils.FindCacheSequence;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoteAdapter extends BaseAdapter {
	public List<NoteEntity> entities;
	private LayoutInflater mInflater;
	private Context mContext;
	
	

	public NoteAdapter(List<NoteEntity> entities, Context context) {
		super();
		this.entities = entities;
		this.mContext = context;
		mInflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return entities.size();
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
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.list_item_stiky_note, null);
			holder.tv_note_date=(TextView) convertView.findViewById(R.id.tv_stiky_note_date);
			holder.tv_note_content=(TextView) convertView.findViewById(R.id.tv_stiky_note_content);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.tv_note_date.setText(entities.get(position).getDate());
		holder.tv_note_content.setText(entities.get(position).getContent());
		return convertView;
	}
	
	class ViewHolder{
		private TextView tv_note_date,tv_note_content;
	}

}
