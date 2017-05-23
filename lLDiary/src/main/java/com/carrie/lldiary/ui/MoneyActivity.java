package com.carrie.lldiary.ui;

import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.MoneyTodayAdapter;
import com.carrie.lldiary.base.BaseActivity;
import com.carrie.lldiary.dao.Money;
import com.carrie.lldiary.helper.DBHelper;
import com.carrie.lldiary.helper.RecyclerItemClickListener;
import com.carrie.lldiary.helper.SimpleItemTouchHelperCallback;
import com.carrie.lldiary.utils.DateUtil;
import com.carrie.lldiary.utils.LogUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 记账本
 */
public class MoneyActivity extends BaseActivity implements View.OnClickListener {
    private static final Integer MONEY_ADD = 1;
    private static final Integer MONEY_EDIT = 2;
    @BindView(R.id.tv_date)
    protected TextView tvToday;
    @BindView(R.id.tv_today_spend)
    protected TextView tvTodaySpend;
    @BindView(R.id.money_today_list)
    protected RecyclerView rvToday;
    @BindView(R.id.tv_old_money_detail)
    protected TextView tvMoneyDtl;
    @BindView(R.id.tv_account_balance)
    protected TextView tvAccountBalance;

    private ArrayList<Money> mTodayList;
    private Money money;
    private float income = 0;
    private float expense = 0;
    private MoneyTodayAdapter todayAdapter;
    private DBHelper dbHelper;

    @Override
    protected void initPre() {
        mLayoutId = R.layout.activity_money_list;
        mTitle = getString(R.string.my_money_book);
        mRightIcon = R.drawable.btn_add;
        TAG = "MoneyActivity";
        titleView.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoneyActivity.this, MoneyEdit2Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, MONEY_ADD);
            }
        });
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        getTodayList();
        setTodayView();
        tvMoneyDtl.setOnClickListener(this);
        tvAccountBalance.setOnClickListener(this);
    }

    private void getTodayList() {
        dbHelper = App.dbHelper;
        mTodayList = dbHelper.getTodayList(DateUtil.getCurrentDate());
        LogUtil.i(TAG, "mTodayList=" + mTodayList.size() + "," + mTodayList.toString());
    }

    private void setTodayView() {
        setRecyclerView();
        getTotalExpense();
        setOnItemClick();
        swipeItem();
    }

    private void getTotalExpense() {
        int size = mTodayList.size();
        for (int i = 0; i < size; i++) {
            money = mTodayList.get(i);
            if (money.getState() > 0) {
                income = income + Float.valueOf(money.getPrice());
            } else if (money.getState() < 0) {
                expense = expense + Float.valueOf(money.getPrice());
            }
        }
        tvTodaySpend.setText(String.format(getString(R.string.today_spend), "+" + income, "-" + expense));
    }

    private void setRecyclerView() {
        LinearLayoutManager llManager = new LinearLayoutManager(this);
        rvToday.setLayoutManager(llManager);
        rvToday.setHasFixedSize(true);

        todayAdapter = new MoneyTodayAdapter(mTodayList);
        rvToday.setAdapter(todayAdapter);
    }

    private void setOnItemClick() {
        rvToday.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MoneyActivity.this, MoneyEdit2Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Money", mTodayList.get(position));
                intent.putExtra("Pos", position);
                startActivityForResult(intent, MONEY_EDIT);
            }
        }));
    }

    private void swipeItem() {
        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                dbHelper.deleteMoneyItem(mTodayList.get(pos));
                mTodayList.remove(pos);
                todayAdapter.notifyItemRemoved(pos);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    final float alpha = SimpleItemTouchHelperCallback.ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvToday);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            money = data.getParcelableExtra("Money");
            if (requestCode == MONEY_EDIT) {
                LogUtil.i(TAG,"--------------MONEY_EDIT----------------");
                int pos=data.getIntExtra("Pos", 0);
                mTodayList.set(pos,money);
                todayAdapter.notifyItemChanged(pos);
            } else if (requestCode == MONEY_ADD) {
                LogUtil.i(TAG,"--------------MONEY_ADD----------------");
                mTodayList.add(money);
                todayAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_old_money_detail:
                Intent intent = new Intent(this, MoneyListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.tv_account_balance:

                break;
        }
    }
}
