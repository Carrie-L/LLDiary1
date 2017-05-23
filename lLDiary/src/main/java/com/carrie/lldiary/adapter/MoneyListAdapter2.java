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
import com.carrie.lldiary.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by new on 2017/5/5.
 * 今天记账列表
 */

public class MoneyListAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final Integer TYPE_PARENT = 0;
    private static final Integer TYPE_CHILD = 1;

    private ArrayList<Money> list;
    private Resources resources;
    private Money money;
    private float income = 0;
    private float expense = 0;
    private Context mContext;
    private MoneyParentViewHolder parentHolder;
    private MoneyChildViewHolder childHolder;
    private String[] expenses;
    private String TAG;

    public MoneyListAdapter2(ArrayList<Money> list) {
        this.list = list;
//        getTotalExpense();
    }

    private void getTotalExpense() {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            money = list.get(i);
            if (money.getState() > 0) {
                income = income + Float.valueOf(money.getPrice());
            } else if (money.getState() < 0) {
                expense = expense + Float.valueOf(money.getPrice());
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        resources = parent.getResources();
        if (viewType == TYPE_PARENT) {
            return new MoneyParentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month_parent, parent, false));
        } else {
            return new MoneyChildViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monet_child, parent, false));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_PARENT;
        } else if (list.get(position).getDate().equals(list.get(position - 1).getDate())) {
            return TYPE_CHILD;
        } else {
            return TYPE_PARENT;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        money = list.get(position);

        if (getItemViewType(position) == TYPE_PARENT) {
            parentHolder = (MoneyParentViewHolder) holder;
            setView(parentHolder);
            parentHolder.tvDate.setText(money.getDate());
            parentHolder.tvExpense.setText(String.format(mContext.getString(R.string.today_spend), "+" + income, "-" + expense));
            income=expense=0;
        } else {
            childHolder = (MoneyChildViewHolder) holder;
            setView(childHolder);
        }


    }

    private void setView(MoneyChildViewHolder childHolder) {
        childHolder.ivIcon.setBackground(resources.getDrawable(AppUtils.getResourceIdentifier(resources, money.getClassifyIcon(), "drawable")));
        childHolder.tvAccount.setText(money.getAccount());
        childHolder.tvContent.setText(money.getContent());

        if (money.getState() > 0) {
            income = income + Float.valueOf(money.getPrice());
            childHolder.tvSpend.setText(String.format(mContext.getString(R.string.income), money.getPrice()));
            childHolder.tvSpend.setTextColor(resources.getColor(R.color.light_green_accent));
        } else {
            expense = expense + Float.valueOf(money.getPrice());
            childHolder.tvSpend.setText(String.format(mContext.getString(R.string.expense), money.getPrice()));
            childHolder.tvSpend.setTextColor(resources.getColor(R.color.colorAccent));
        }
        LogUtil.i("MoneyListAdapter2","income="+income+",expense="+expense+"，money.getPrice()= "+money.getPrice());
    }

    static class MoneyChildViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivIcon;
        private final TextView tvSpend;
        private final TextView tvAccount;
        private final TextView tvContent;

        private MoneyChildViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_classify_icon);
            tvSpend = (TextView) itemView.findViewById(R.id.tv_spend);
            tvAccount = (TextView) itemView.findViewById(R.id.tv_account);
            tvContent = (TextView) itemView.findViewById(R.id.tv_money_content);
        }
    }

    private static class MoneyParentViewHolder extends MoneyChildViewHolder {
        private TextView tvDate;
        private TextView tvExpense;

        private MoneyParentViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvExpense = (TextView) itemView.findViewById(R.id.tv_money_count);
        }
    }
}
