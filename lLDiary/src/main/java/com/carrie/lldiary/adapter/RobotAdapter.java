package com.carrie.lldiary.adapter;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import com.carrie.lldiary.R;
import com.carrie.lldiary.entity.RobotEntity;
import com.carrie.lldiary.utils.CircularImage;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RobotAdapter extends BaseAdapter {
	private List<RobotEntity> entities;
	private Context mContext;
	private LayoutInflater mInflater;
	private ByteArrayInputStream bais;
	private SharedPreferences sp;
	
	private Bitmap bitmap;
	

	public RobotAdapter(List<RobotEntity> entities, Context mContext) {
		super();
		this.entities = entities;
		this.mContext = mContext;
		mInflater=LayoutInflater.from(mContext);
		sp=mContext.getSharedPreferences("config", 0);
		bitmap = BitmapFactory.decodeFile(sp.getString("photo", ""));
	}

	@Override
	public int getCount() {
		return entities.size();
	}

	@Override
	public Object getItem(int position) {
		return entities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RobotEntity entity=entities.get(position);
		int type=entity.getType();
		ViewHolder holder = new ViewHolder();
		//if(convertView==null){
			if(type==RobotEntity.RECEIVER)
				convertView=mInflater.inflate(R.layout.list_item_robot_left, null);
			else if(type==RobotEntity.SEND)
				convertView=mInflater.inflate(R.layout.list_item_robot_right, null);
			holder.tv_content=(TextView) convertView.findViewById(R.id.tv_chatcontent);
			holder.tv_name=(TextView) convertView.findViewById(R.id.tv_username);
			holder.tv_time=(TextView) convertView.findViewById(R.id.tv_sendtime);
			holder.head_photo=(CircularImage) convertView.findViewById(R.id.head_photo);
			convertView.setTag(holder);
	//	}else {
			holder=(ViewHolder) convertView.getTag();
	//	}
		if(entities.get(position).getType()==RobotEntity.RECEIVER){
			holder.head_photo.setImageResource(R.drawable.ann_type6);
			holder.tv_name.setText("Сu");
		}
		else{
			bais = editPhoto();
			holder.head_photo.setImageDrawable(Drawable.createFromStream(
				bais, entities.get(position).getPhoto()));
			String name=sp.getString("username", "");
			if(name!=null){
				holder.tv_name.setText(name);
			}
			else
				holder.tv_name.setText("uu");
		}
		holder.tv_content.setText(entities.get(position).getContent());
		
		holder.tv_time.setText(entities.get(position).getTime());
		return convertView;
	}
	
	public ByteArrayInputStream editPhoto() {
		String imageBase64 = sp.getString("photo", "");
		byte[] base64Bytes = Base64.decodeBase64(imageBase64.getBytes());
		base64Bytes = Base64.decodeBase64(imageBase64.getBytes());
		ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
		bais = new ByteArrayInputStream(base64Bytes);
		return bais;
	}
	
	class ViewHolder {
		private CircularImage head_photo;
		private TextView tv_content,tv_name,tv_time;
	}
	
}
