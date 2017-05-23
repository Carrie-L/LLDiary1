package com.carrie.lldiary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carrie.lldiary.R;
import com.carrie.lldiary.dao.Money;
import com.carrie.lldiary.utils.LogUtil;

import java.util.ArrayList;

public class MoneyListAdapter extends RecyclerView.Adapter<MoneyListAdapter.MoneyViewHolder>  {// implements ItemTouchHelperAdapter
    public ArrayList<Money> entities;
    private Context mContext;
    private static final String TAG="MoneyAdapter";
    private MoneyViewHolder mHolder;

    public MoneyListAdapter(ArrayList<Money> entities, Context mContext) {
        this.entities = entities;
        this.mContext = mContext;
    }


    @Override
    public MoneyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.list_item_money,viewGroup,false);
        return new MoneyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoneyViewHolder holder, int i) {
        mHolder=holder;

        Money money=entities.get(i);
        holder.tv_money_content.setText(money.getContent());
        holder.tv_money_account.setText(money.getAccount());
        holder.icon.setBackgroundResource(mContext.getResources().getIdentifier(money.getClassifyIcon()+"","drawable",mContext.getPackageName()));

        holder.tv_money_price.setText(money.getPrice()+mContext.getString(R.string.total_yuan));

        if(entities.get(i).getState()==1){
            holder.tv_money_state.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.tv_money_state.setText("收入");
        }else if(entities.get(i).getState()==-1){
            holder.tv_money_state.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.tv_money_state.setText("支出");
        }else if(entities.get(i).getState()==0){
            holder.tv_money_state.setText("转账");
        }

        String date=money.getDate();
        if(i>0){
            LogUtil.i(TAG,i+":date="+date+"..."+entities.get(i).getDate()+"*** i-1:"+entities.get(i-1).getDate());
        }

        if(i==0){
            holder.tv_money_date.setText(money.getDate());
            holder.tv_total.setText(showTotalBalance(money.getIncome(),money.getExpense()));
        }else{
            if(date.equals(entities.get(i-1).getDate())){
                LogUtil.i(TAG,"相等");
                holder.rl_top.setVisibility(View.GONE);
            }else{
                LogUtil.i(TAG,"不相等");
                holder.rl_top.setVisibility(View.VISIBLE);
                holder.tv_money_date.setText(money.getDate());
                holder.tv_total.setText(showTotalBalance(money.getIncome(),money.getExpense()));
            }
        }

    }

    private String showTotalBalance(String income,String expense){
        if(income.equals("0")) {
            return mContext.getString(R.string.total_expense) +expense + mContext.getString(R.string.total_yuan);
        }else if(expense.equals("0")){
            return mContext.getString(R.string.total_income) + income + mContext.getString(R.string.total_yuan);
        }else{
            return mContext.getString(R.string.total_income) +income+ mContext.getString(R.string.total_yuan_)+mContext.getString(R.string.total_expense) +expense + mContext.getString(R.string.total_yuan);
        }
    }

    private SpannableStringBuilder setTextRed(String money){
        SpannableStringBuilder builder=new SpannableStringBuilder(money);
        builder.setSpan(new ForegroundColorSpan(Color.RED),0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    private SpannableStringBuilder setTextGreen(String money){
        SpannableStringBuilder builder=new SpannableStringBuilder(money);
        builder.setSpan(new ForegroundColorSpan(Color.GREEN),0,money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public class MoneyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_total,tv_money_content,tv_money_date,tv_money_account,tv_money_state,tv_money_price;
        ImageView icon;
        RelativeLayout rl_top;

        public MoneyViewHolder(View itemView) {
            super(itemView);
            tv_money_date=(TextView) itemView.findViewById(R.id.tv_item_date);
            tv_total=(TextView) itemView.findViewById(R.id.tv_item_total);
            icon= (ImageView) itemView.findViewById(R.id.iv_money_icon);
            tv_money_state=(TextView) itemView.findViewById(R.id.tv_item_money_state);
            tv_money_account=(TextView) itemView.findViewById(R.id.tv_item_money_account);
            tv_money_price=(TextView) itemView.findViewById(R.id.tv_item_money_price);
            tv_money_content=(TextView) itemView.findViewById(R.id.tv_item_money_content);
            rl_top= (RelativeLayout) itemView.findViewById(R.id.rl_money_top);
        }
    }
}
