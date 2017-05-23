package com.carrie.lldiary.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.PlanAdapter;
import com.carrie.lldiary.base.MyBaseActivity;
import com.carrie.lldiary.dao.Plan;
import com.carrie.lldiary.helper.DBHelper;
import com.carrie.lldiary.helper.RecyclerItemClickListener;
import com.carrie.lldiary.helper.SimpleItemTouchHelperCallback;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.utils.RecyclerItemLongClickListener;
import com.carrie.lldiary.view.TitleView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.Collections;

public class PlanActivity extends MyBaseActivity implements OnClickListener{
	private RecyclerView recyclerView;
	private PlanAdapter adapter;
	public static ArrayList<Plan> entities;
	private static final String TAG = "PlanActivity";
	protected static final int REFRESH = 0;
	private Intent intent;
	private DBHelper dbHelper;

	private Plan entity;

	@Override
	public void initData() {
		dbHelper = App.dbHelper;
		entities= AppUtils.initList(entities);
		entities= dbHelper.getAllPlans();
		AppUtils.logArrayListToString(entities);

		if(adapter==null){
			adapter = new PlanAdapter(entities);
			recyclerView.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}

		recyclerView.addOnItemTouchListener(new RecyclerItemLongClickListener(getApplicationContext(), recyclerView, new RecyclerItemLongClickListener.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				entity=entities.get(position);
				intent=new Intent(getApplicationContext(),PlanEditActivity.class);
				intent.putExtra("Plan",entities.get(position));
				startActivity(intent);
				finish();
			}

			@Override
			public void onItemLongClick(View view, int position) {
				//长按改变完成状态
				entity=entities.get(position);
				//已完成——>则改成未完成; 未完成——>则改成已完成
				entity.setFinish(entity.getFinish()?false:true);
				dbHelper.updatePlanState(entity);
				entities.set(position,entity);
				adapter.notifyDataSetChanged();
			}
		}));

		ItemTouchHelper.Callback callback=new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT) {
			@Override
			public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
				int fromPosition=viewHolder.getAdapterPosition();
				int toPosition=target.getAdapterPosition();
				AppUtils.logArrayListToString(entities);
				if(fromPosition<toPosition){
					for(int i=fromPosition;i<toPosition;i++){
						Collections.swap(entities,fromPosition,i+1);
					}
				}else if(fromPosition>toPosition){
					for(int i=fromPosition;i>toPosition;i--){
						Collections.swap(entities,fromPosition,i-1);
					}
				}
				adapter.notifyItemMoved(fromPosition, toPosition);
				AppUtils.logArrayListToString(entities);


				return true;
			}
			@Override
			public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
				int pos=viewHolder.getAdapterPosition();
				dbHelper.deletePlanItem(entities.get(pos));
				entities.remove(pos);
				adapter.notifyItemRemoved(pos);
			}

			@Override
			public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
				super.clearView(recyclerView, viewHolder);
				viewHolder.itemView.setAlpha(SimpleItemTouchHelperCallback.ALPHA_FULL);
				dbHelper.insertPlans(entities);
			}

			@Override
			public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
				if(actionState==ItemTouchHelper.ACTION_STATE_SWIPE){
					final float alpha = SimpleItemTouchHelperCallback.ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
					viewHolder.itemView.setAlpha(alpha);
					viewHolder.itemView.setTranslationX(dX);
				}else{
					super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
				}
			}

		};

		ItemTouchHelper touchHelper=new ItemTouchHelper(callback);
		touchHelper.attachToRecyclerView(recyclerView);
	}

	@Override
	public void goBack() {

	}

	@Override
	public View initView(LayoutInflater inflater,ViewGroup container) {
		View view=inflater.inflate(R.layout.activity_plan,container,false);
		MyActivityManager.add(this);
		PushAgent.getInstance(this).onAppStart();// 统计应用启动数据

		TitleView titleView = (TitleView)findViewById(R.id.title_view);
		titleView.setTitle(getString(R.string.title_activity_plan));
		titleView.setOnRightClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(getApplicationContext(), PlanEditActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

		recyclerView = (RecyclerView) view.findViewById(R.id.rv_showAllPlan);
		recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		recyclerView.setHasFixedSize(true);



		return view;
	}



	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH:
				LogUtil.i(TAG,"刷新界面");
				adapter.notifyDataSetChanged();
				break;
			}
		};
	};


	// 友盟统计
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		StatService.onPageStart(this, "计划表");

		LogUtil.i(TAG,"onResume111111111111111111111");

//		Intent intent=getIntent();
//		if((ArrayList<Plan>) intent.getSerializableExtra("Plans")!=null){
//			entities= (ArrayList<Plan>) intent.getSerializableExtra("Plans");
//			AppUtils.logArrayListToString(entities);
//		}

		if(getSharedPreferences("Config",MODE_PRIVATE).getBoolean("PlanUpdate",false)){
			LogUtil.i(TAG,"onResume22222222222");
			entities=dbHelper.getAllPlans();
//			adapter.notifyDataSetChanged();
			Message msg=new Message();
			msg.what=REFRESH;
			mHandler.sendMessage(msg);
		}
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		StatService.onPageEnd(this, "计划表");
	}

	@Override
	public void onClick(View v) {

	}
}
