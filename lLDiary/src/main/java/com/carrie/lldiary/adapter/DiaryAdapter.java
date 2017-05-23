package com.carrie.lldiary.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.dao.Diary;
import com.carrie.lldiary.ui.DiaryListActivity;
import com.carrie.lldiary.ui.LabelActivity;

import java.util.ArrayList;

/**
 * Created by new on 2017/4/15.
 */

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {
    private ArrayList<Diary> list;
    private Context mContext;

    public DiaryAdapter(ArrayList<Diary> list) {
        this.list = list;
    }

    public void setList(ArrayList<Diary> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        return new DiaryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diary,parent,false));
    }

    @Override
    public void onBindViewHolder(DiaryViewHolder holder, int position) {
        holder.tvDate.setText(list.get(position).getUpdateDate());
        if(!TextUtils.isEmpty(list.get(position).getTitle())){
            holder.tvTitle.setText(list.get(position).getTitle());
            holder.tvTitle.setVisibility(View.VISIBLE);
        }else{
            holder.tvTitle.setVisibility(View.GONE);
        }
        holder.tvContent.setText(list.get(position).getContent());
        holder.tvLabel.setText(list.get(position).getLabel());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     class DiaryViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle,tvDate,tvContent,tvLabel;

        private DiaryViewHolder(View itemView) {
            super(itemView);
            tvTitle= (TextView) itemView.findViewById(R.id.tv_diary_title);
            tvDate= (TextView) itemView.findViewById(R.id.tv_diary_date);
            tvContent= (TextView) itemView.findViewById(R.id.tv_diary_content);
            tvLabel= (TextView) itemView.findViewById(R.id.tv_diary_label);
        }
    }
}
