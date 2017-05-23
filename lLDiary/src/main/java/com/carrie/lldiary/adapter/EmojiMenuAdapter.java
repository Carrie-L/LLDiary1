package com.carrie.lldiary.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.entity.Emoji;
import com.carrie.lldiary.utils.LogUtil;

import java.util.ArrayList;

public class EmojiMenuAdapter extends Adapter<EmojiMenuAdapter.ViewHolder> {
	private static final String TAG = "EmojiRecyclerAdapter";
	private ArrayList<Emoji> datas;
	private Context mContext;
	private View view;

	public EmojiMenuAdapter(ArrayList<Emoji> data) {
		super();
		this.datas = data;
	}

	protected final static class ViewHolder extends RecyclerView.ViewHolder{
		private ImageView iv_menu;

		public ViewHolder(View itemView) {
			super(itemView);
			iv_menu = (ImageView) itemView.findViewById(R.id.iv_emoji_menu);
		}
		
	}

	@Override
	public int getItemCount() {
		return datas.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		int resId=datas.get(position).getId();
		LogUtil.i(TAG,"resId="+resId);
		if(resId==0){
			viewHolder.iv_menu.setImageBitmap(BitmapFactory.decodeFile(datas.get(position).getFaceName()));
		}else{
			viewHolder.iv_menu.setImageResource(datas.get(position).getId());
		}

		
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		view = LayoutInflater.from(arg0.getContext()).inflate(R.layout.item_emoji_menu,arg0,false);
		return new ViewHolder(view);
	}

	public void insertItem(int pos){

	}


}
