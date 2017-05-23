package com.carrie.lldiary.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.entity.DiaryBg;

import java.util.ArrayList;

import static android.R.attr.id;

/**
 * Created by new on 2017/4/14.
 */

public class DialogGridAdapter extends RecyclerView.Adapter<DialogGridAdapter.DialogGridViewHolder> {
    private ArrayList<DiaryBg> list;
    private Context mContext;
    private Resources resources;

    public DialogGridAdapter(ArrayList<DiaryBg> list) {
        this.list = list;
    }

    @Override
    public DialogGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        resources = parent.getResources();
        return new DialogGridViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_grid, parent, false));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(DialogGridViewHolder holder, int position) {
        if(list.get(position).bgName.contains("|")){
            int id=resources.getIdentifier(list.get(position).bgName.split("\\|")[1],list.get(position).bgName.split("\\|")[0],"com.carrie.lldiary");
            holder.imageView.setBackground(resources.getDrawable(id));
        }
    }

    static class DialogGridViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        private DialogGridViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_grid);
        }
    }
}
