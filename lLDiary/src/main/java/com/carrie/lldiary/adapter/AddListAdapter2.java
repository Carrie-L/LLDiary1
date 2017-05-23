package com.carrie.lldiary.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.entity.ListDialog;
import com.carrie.lldiary.helper.RecyclerItemClickListener;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.view.DialogRecyclerView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;


/**
 * Created by new on 2017/4/25.
 */

public class AddListAdapter2 extends RecyclerView.Adapter<AddListAdapter2.AddViewHolder> {
    private ArrayList<ListDialog> list;
    private Resources resources;
    private Context mContext;

    public AddListAdapter2(ArrayList<ListDialog> list) {
        this.list = list;
    }

    @Override
    public AddViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        resources = parent.getResources();
        return new AddViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_list, parent, false));
    }

    @Override
    public void onBindViewHolder(AddViewHolder holder, int position) {
        holder.ivIcon.setBackground(resources.getDrawable(AppUtils.getResourceIdentifier(resources, list.get(position).icon, "drawable")));
//        holder.metName.setText(list.get(position).name);
        if (list.get(position).balance != null) {
            holder.metBalance.setText(list.get(position).balance);
        } else {
            holder.metBalance.setVisibility(View.GONE);
        }
//        holder.ivIcon.setImageResource(R.drawable.icon_money);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     class AddViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG ="AddViewHolder" ;
        private ImageView ivIcon;
        private MaterialEditText metName;
        private MaterialEditText metBalance;

        AddViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            metName = (MaterialEditText) itemView.findViewById(R.id.met_name);
            metBalance = (MaterialEditText) itemView.findViewById(R.id.met_balance);
            LogUtil.i(TAG,"AddViewHolder");

            ivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    final DialogRecyclerView dialogView = new DialogRecyclerView(mContext);
//                    AppUtils.getClassifies();
//                    dialogView.setList(bgs);
//                    dialogView.setGridAdapter();
//                    dialogView.alertDialog();
//                    dialogView.setOnItemClickListener(new RecyclerItemClickListener.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            dialogView.cancelDialog();
//                        }
//                    });
                }
            });
        }
    }

}
