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

import java.util.ArrayList;

public class EmojiRecyclerAdapter extends Adapter<EmojiRecyclerAdapter.ViewHolder2> {
	private static final String TAG = "EmojiRecyclerAdapter";
	private ArrayList<Emoji> datas;
	private Context mContext;
	private View view;
	
	public EmojiRecyclerAdapter(ArrayList<Emoji> data) {
		super();
		this.datas = data;
	}

	protected final static class ViewHolder2 extends RecyclerView.ViewHolder{
		private ImageView iv_menu;

		public ViewHolder2(View itemView) {
			super(itemView);
			iv_menu = (ImageView) itemView.findViewById(R.id.item_iv_face);
		}
		
	}

	@Override
	public int getItemCount() {
		return datas.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder2 viewHolder, int position) {
		int resId=datas.get(position).getId();
		if(resId==0){
			viewHolder.iv_menu.setImageBitmap(BitmapFactory.decodeFile(datas.get(position).getFaceName()));
		}else{
			viewHolder.iv_menu.setImageResource(resId);
		}

		
	}

	@Override
	public ViewHolder2 onCreateViewHolder(ViewGroup arg0, int arg1) {
		view = LayoutInflater.from(arg0.getContext()).inflate(R.layout.item_face,arg0,false);
		return new ViewHolder2(view);
	}


}
