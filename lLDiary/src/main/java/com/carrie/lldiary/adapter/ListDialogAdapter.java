package com.carrie.lldiary.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.entity.ListDialog;
import com.carrie.lldiary.helper.ItemTouchHelperAdapter;
import com.carrie.lldiary.helper.ItemTouchHelperViewHolder;
import com.carrie.lldiary.helper.OnItemMoveListener;
import com.carrie.lldiary.helper.RecyclerItemClickListener;
import com.carrie.lldiary.utils.AppUtils;
import com.umeng.fb.util.Res;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Administrator on 2016/6/4 0004.
 */
public class ListDialogAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter {
    private static final String TAG = "ListDialogAdapter";
    private ArrayList<ListDialog> lists;
    private Context mContext;
    Resources resources;

    public ListDialogAdapter(Context context, ArrayList<ListDialog> list) {
        this.mContext = context;
        this.lists = list;
        resources=context.getResources();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.item_list_dialog, null);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ListViewHolder) {
            ListViewHolder holder = (ListViewHolder) viewHolder;
            holder.description.setText(lists.get(i).name);
            holder.icon.setBackground(resources.getDrawable(AppUtils.getResourceIdentifier(resources,lists.get(i).icon,"drawable")));

            if (lists.get(i).balance != null) {
                holder.balance.setVisibility(View.VISIBLE);
                holder.balance.setText(lists.get(i).balance);
            }
        }
    }


    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(lists, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }


    @Override
    public void onItemDismiss(int position) {
        itemMoveListener.remove(true, lists.get(position).name);
        lists.remove(position);
        notifyItemRemoved(position);
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {
        private TextView description, balance;
        private ImageView icon;
        private RelativeLayout rl;

        public ListViewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.tv_list_dialog);
            balance = (TextView) itemView.findViewById(R.id.tv_list_balance);
            icon = (ImageView) itemView.findViewById(R.id.iv_list_dialog);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl_list_dialog);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public void onItemSelected() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                itemView.setBackgroundColor(mContext.getResources().getColor(R.color.primaryColor_transparent_96, null));
            }else{
                itemView.setBackgroundColor(mContext.getResources().getColor(R.color.primaryColor_transparent_96));
            }
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
            itemMoveListener.move(true);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public RecyclerItemClickListener.OnItemClickListener itemClickListener;

    public void setOnItemClickListener(RecyclerItemClickListener.OnItemClickListener listener) {
        itemClickListener = listener;
    }

    private OnItemMoveListener itemMoveListener;

    public void setOnItemMoveListener(OnItemMoveListener listener) {
        itemMoveListener = listener;
    }
}
