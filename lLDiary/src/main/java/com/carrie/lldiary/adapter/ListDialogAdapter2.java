package com.carrie.lldiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.entity.ListDialog;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/4 0004.
 */
public class ListDialogAdapter2 extends BaseAdapter {
    private ArrayList<ListDialog> lists;
    private Context mContext;

    public ListDialogAdapter2(Context context,ArrayList<ListDialog> list) {
        this.mContext=context;
        this.lists=list;
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
        ViewHolder holder=new ViewHolder();
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_list_dialog,null);
        }
        return null;
    }

    public  class ViewHolder{
        private TextView description;
        private ImageView icon;
    }
}
