package com.carrie.lldiary.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.entity.Image;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/17 0017.
 */
public class BgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Image> lists;
    private Context mContext;
    private Image entity;

    private final int TYPE_TEXT=0;
    private final int TYPE_BG=1;
    private BgViewHolder bgHolder;
    private BgTextViewHolder textHolder;
    private Resources resources;

    public BgAdapter(ArrayList<Image> lists) {
        this.lists = lists;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        resources = mContext.getResources();

        if(viewType==TYPE_BG){
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_bg,parent,false);
            return new BgViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_bg_text,parent,false);
            return new BgTextViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        entity=lists.get(position);

        if(holder.getItemViewType()==TYPE_BG){
            bgHolder = (BgViewHolder) holder;
            bgHolder.iv_bg.setBackgroundResource(entity.icon);
        }else{
            textHolder = (BgTextViewHolder) holder;
            textHolder.tv_text.setText(entity.name);
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(lists.get(position).icon!=0){
            return TYPE_BG;
        }else{
            return TYPE_TEXT;
        }
    }

    public class BgViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView iv_bg;

        public BgViewHolder(View itemView) {
            super(itemView);
            iv_bg= (SimpleDraweeView) itemView.findViewById(R.id.iv_bg_item);
        }
    }

    public class BgTextViewHolder extends RecyclerView.ViewHolder{
        TextView tv_text;

        public BgTextViewHolder(View itemView) {
            super(itemView);
            tv_text= (TextView) itemView.findViewById(R.id.tv_bg_text);
        }
    }
}
