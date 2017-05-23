package com.carrie.lldiary.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;
import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.MoneyListAdapter;
import com.carrie.lldiary.base.MyBaseActivity;
import com.carrie.lldiary.dao.Money;
import com.carrie.lldiary.helper.DBHelper;
import com.carrie.lldiary.helper.RecyclerItemClickListener;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.view.TitleView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;

public class MoneyBookActivity extends MyBaseActivity {
	private static final String TABLE_NAME = "Money";
	private static final String TAG = "MoneyBookActivity";

	private Intent intent;

	public static ArrayList<Money> entities;
	public static ArrayList<Money> totalExpenses;
	private MoneyListAdapter adapter;

	private RecyclerView recyclerView;
	private DBHelper dbHelper;

	@Override
	public void initData() {
		MyActivityManager.add(this);

		PushAgent.getInstance(this).onAppStart();//统计应用启动数据
		entities = AppUtils.initList(entities);
		totalExpenses = AppUtils.initList(totalExpenses);

		queryDB();

		AppUtils.logArrayListToString(entities);
		AppUtils.logArrayListToString(totalExpenses);

		LinearLayoutManager llManager=new LinearLayoutManager(getApplicationContext());
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(llManager);

		if(adapter==null){
			adapter = new MoneyListAdapter(entities, this);
			recyclerView.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}

		ItemTouchHelper.Callback callback=new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
			@Override
			public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
				int position=viewHolder.getAdapterPosition();
				dbHelper.deleteMoneyItem(entities.get(position));
				entities.remove(position);
//				adapter.notifyItemRemoved(position);
				adapter.notifyDataSetChanged();
			}
		};

//		ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
		ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
		itemTouchHelper.attachToRecyclerView(recyclerView);

		recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(getApplicationContext(), MoneyEditActivity.class);
				intent.putExtra("position", position);
				intent.putExtra("MoneyList",entities);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		}));

	}

	@Override
	public void goBack() {

	}

	public static final float ALPHA_FULL = 1.0f;

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container) {
		View view=inflater.inflate(R.layout.activity_money_book,container,false);
		recyclerView = (RecyclerView)view.findViewById(R.id.rv_money);
		TitleView titleView = (TitleView)findViewById(R.id.title_view);
		titleView.setTitle("记账本");
		titleView.setOnRightClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(getApplicationContext(), MoneyEditActivity.class);
				startActivity(intent);
			}
		});
		titleView.setOnLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		titleView.setRightIcon(R.drawable.btn_add);
		return view;
	}


	/**
	 * 查询数据库并显示内容
	 */
	public void queryDB() {
		dbHelper = App.dbHelper;
		entities = dbHelper.getMoneyLists();
		int size = entities.size();
		Money money=null;

		for (int i = 0; i < size; i++) {
			Cursor cursor1 = App.db.rawQuery("select date, sum(income) as income,sum(expense) as expense from " + TABLE_NAME + " where date=? group by date ", new String[]{entities.get(i).getDate()});
			if (cursor1 != null) {
				while (cursor1.moveToNext()) {
					money = entities.get(i);
//					money.price=money.getState()==1?money.getIncome():money.getExpense();
					money.setIncome(cursor1.getString(cursor1.getColumnIndex("income")));
					money.setExpense(cursor1.getString(cursor1.getColumnIndex("expense")));

					entities.set(i, money);
					entities.set(i, money);

					break;
				}
				cursor1.close();
			}
		}

	}

	//友盟统计
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		StatService.onPageStart(this, "记账本");

		LogUtil.d(TAG,"------------onResume------------------");
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		StatService.onPageEnd(this, "记账本");

		LogUtil.d(TAG,"------------onPause------------------");
	}


}
