package com.carrie.lldiary.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.MoneyListAdapter2;
import com.carrie.lldiary.base.BaseActivity;
import com.carrie.lldiary.dao.Money;
import com.carrie.lldiary.helper.DBHelper;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.DateUtil;
import com.carrie.lldiary.utils.LogUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoneyListActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.tv_select)
    protected TextView tvSelect;
    @BindView(R.id.rl_custom_date)
    protected RelativeLayout rlCustomDate;
    @BindView(R.id.tv_start_time)
    protected TextView tvStartDate;
    @BindView(R.id.iv_query)
    protected ImageView ivQuery;
    @BindView(R.id.tv_end_time)
    protected TextView tvEndDate;
    @BindView(R.id.tv_expense_summary)
    protected TextView tvSummary;
    @BindView(R.id.recycler_view_money)
    protected RecyclerView recyclerView;
    private DBHelper dbHelper;
    private String today;
    private ArrayList<Money> list;
    private String monday;
    private Money money;
    private float income = 0;
    private float expense = 0;
    private ArrayList<Money> mClassifyExpenses;
    private StringBuilder sbSummary;

    @Override
    protected void initPre() {
        mLayoutId=R.layout.activity_money_list2;
        mTitle=getString(R.string.my_money_book);
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        init();
        getList(monday,today);
        setRecyclerView();
        getTotalExpense();
        setSummary(monday,today);
    }

    private void init(){
        tvSelect.setOnClickListener(this);
        ivQuery.setOnClickListener(this);

        list=AppUtils.initList(list);
        mClassifyExpenses=AppUtils.initList(mClassifyExpenses);
        sbSummary = new StringBuilder();

        dbHelper = App.dbHelper;
        today = DateUtil.getCurrentDate();

        calculateDate();
    }

    private void calculateDate(){
        monday = DateUtil.getWeekFirstDate();
        LogUtil.i(TAG,"本周第一天 monday = "+ monday);

        String monthFirst=DateUtil.getMonthFirstDate();
        LogUtil.i(TAG,"本月第一天 monthFirst = "+monthFirst);


    }

    private void setSummary(String startDate,String endDate){
        mClassifyExpenses = dbHelper.getClassifyExpense(mClassifyExpenses,startDate,endDate);
        LogUtil.i(TAG,"mClassifyExpenses="+mClassifyExpenses.size()+","+mClassifyExpenses.toString());

        int size=mClassifyExpenses.size();
        Money entity;
        for(int i=0;i<size;i++){
            entity=mClassifyExpenses.get(i);
            sbSummary.append("<br>[ ").append(entity.getClassify()).append(" ]")
                    .append("&#160;&#160;&#160;&#160;&#160;").append(entity.sign(entity.getState())).append(entity.getExpense()).append("元<br />");
        }

        String summary0=String.format(getString(R.string.expense_summary),startDate,endDate,expense+"",income+"",sbSummary.toString());
        LogUtil.i(TAG,"summary0 = "+summary0);
        tvSummary.setText(Html.fromHtml(summary0));



    }

    private void getList(String startDate,String endDate){
        list.clear();
        list = dbHelper.getMoneyList(startDate,endDate);
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

    private void setRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        MoneyListAdapter2 adapter=new MoneyListAdapter2(list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
}
