package com.carrie.lldiary.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.dao.Diary;
import com.carrie.lldiary.entity.Label;

import java.util.ArrayList;

/**
 * Created by new on 2017/4/17.
 */

public class LabelAdapter extends RecyclerView.Adapter<LabelAdapter.LabelViewHolder> {

    private int mLayoutId;
    private ArrayList<Label> list;
    private int mPressedPos=0;

    public LabelAdapter(int mLayoutId, ArrayList<Label> list) {
        this.mLayoutId = mLayoutId;
        this.list = list;
    }

    @Override
    public LabelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LabelViewHolder(LayoutInflater.from(parent.getContext()).inflate(mLayoutId,parent,false));
    }

    @Override
    public void onBindViewHolder(LabelViewHolder holder, int position) {
        holder.tvLabe.setText(list.get(position).label);
        if(mPressedPos==position){
            holder.tvLabe.setPressed(true);
        }else{
            holder.tvLabe.setPressed(false);
        }
    }

    public void setPressed(int pos){
        mPressedPos=pos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class LabelViewHolder extends RecyclerView.ViewHolder{
        private TextView tvLabe;

        public LabelViewHolder(View itemView) {
            super(itemView);
            tvLabe= (TextView) itemView.findViewById(R.id.tv_label);
        }
    }

}
