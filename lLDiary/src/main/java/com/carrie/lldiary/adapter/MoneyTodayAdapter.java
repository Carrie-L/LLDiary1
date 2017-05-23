package com.carrie.lldiary.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.dao.Money;
import com.carrie.lldiary.utils.AppUtils;

import java.util.ArrayList;

/**
 * Created by new on 2017/5/5.
 * 今天记账列表
 */

public class MoneyTodayAdapter extends RecyclerView.Adapter<MoneyTodayAdapter.TodayViewHolder> {
    private ArrayList<Money> mTodayList;
    private Resources resources;
    private Money money;
    private float income = 0;
    private float expense = 0;
    private Context mContext;

    public MoneyTodayAdapter(ArrayList<Money> mTodayList) {
        this.mTodayList = mTodayList;
    }

    @Override
    public TodayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        resources = parent.getResources();
        return new TodayViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monet_child, parent, false));
    }

    @Override
    public int getItemCount() {
        return mTodayList.size();
    }

    @Override
    public void onBindViewHolder(TodayViewHolder holder, int position) {
        money = mTodayList.get(position);
        holder.ivIcon.setBackground(resources.getDrawable(AppUtils.getResourceIdentifier(resources, money.getClassifyIcon(), "drawable")));
        holder.tvAccount.setText(money.getAccount());
        holder.tvContent.setText(money.getContent());

        if (money.getState() > 0) {
            income = income + Float.valueOf(money.getPrice());
            holder.tvSpend.setText(String.format(mContext.getString(R.string.income), money.getPrice()));
            holder.tvSpend.setTextColor(resources.getColor(R.color.light_green_accent));
        } else {
            expense = expense + Float.valueOf(money.getPrice());
            holder.tvSpend.setText(String.format(mContext.getString(R.string.expense), money.getPrice()));
            holder.tvSpend.setTextColor(resources.getColor(R.color.colorAccent));
        }
    }

    static class TodayViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivIcon;
        private final TextView tvSpend;
        private final TextView tvAccount;
        private final TextView tvContent;

        private TodayViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_classify_icon);
            tvSpend = (TextView) itemView.findViewById(R.id.tv_spend);
            tvAccount = (TextView) itemView.findViewById(R.id.tv_account);
            tvContent = (TextView) itemView.findViewById(R.id.tv_money_content);
        }
    }


}
