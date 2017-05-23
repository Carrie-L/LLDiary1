package com.carrie.lldiary.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.carrie.lldiary.R;
import com.carrie.lldiary.entity.Image;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/29 0029.
 */
public class DiaryBgAdapter extends BaseAdapter {
    private ArrayList<Image> lists;
    private Context mContext;
    private ViewHolder holder;
    private LayoutInflater mInflater;

    public DiaryBgAdapter(ArrayList<Image> lists, Context mContext) {
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
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = new ViewHolder();
        if(convertView==null){
            convertView= mInflater.inflate(R.layout.view_bg,parent,false);
            holder.iv_bg= (SimpleDraweeView) convertView.findViewById(R.id.iv_bg_item);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.iv_bg.setImageURI(Uri.parse("res://"+mContext.getPackageName()+"/"+lists.get(position).icon));

        return convertView;
    }

    private class ViewHolder{
        SimpleDraweeView iv_bg;

    }
}
