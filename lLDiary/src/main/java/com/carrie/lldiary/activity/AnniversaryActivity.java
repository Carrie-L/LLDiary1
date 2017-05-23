package com.carrie.lldiary.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.View.OnClickListener;

import com.baidu.mobstat.StatService;
import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.AnnAdapter;
import com.carrie.lldiary.base.BaseActivity;
import com.carrie.lldiary.dao.Ann;
import com.carrie.lldiary.helper.RecyclerItemClickListener;
import com.carrie.lldiary.helper.SimpleItemTouchHelperCallback;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.LogUtil;
import java.util.ArrayList;

/**
 * 纪念日列表
 */
public class AnniversaryActivity extends BaseActivity {
	protected static final String TABLE_NAME = "ANN";
	private static final String TAG="AnniversaryActivity";
	public static final int ANN_EDIT=0;
	public static final int ANN_INSERT=1;
	public ArrayList<Ann> entities;
	private AnnAdapter adapter;

	private Intent intent;
	private Context mContext;

	@Override
	protected void initPre() {
		mLayoutId=R.layout.activity_anniversary;
		mRightIcon=R.drawable.btn_add;
		mTitle=getString(R.string.title_activity_anniversary);
		titleView.setOnRightClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(getApplicationContext(), AnniversaryEditActivity.class);
				startActivityForResult(intent, ANN_INSERT);
			}
		});
	}

	@Override
	public void initData() {
		RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
		mContext = getApplicationContext();
		entities=AppUtils.initList(entities);
		entities= App.dbHelper.getAnns(entities);
		AppUtils.logArrayListToString(entities);

		recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
		recyclerView.setHasFixedSize(true);
		if(adapter==null){
			adapter = new AnnAdapter(entities, this);
			recyclerView.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}

		recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext,new RecyclerItemClickListener.OnItemClickListener(){

			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(mContext, AnnversaryDetailActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("Ann",entities.get(position));
				LogUtil.i(TAG,"put intent data= "+entities.get(position).toString());
				intent.putExtra("pos",position);
				startActivityForResult(intent, ANN_EDIT);
			}
		}));

		ItemTouchHelper.Callback callback=new SimpleItemTouchHelperCallback(adapter);
		ItemTouchHelper touchHelper=new ItemTouchHelper(callback);
		touchHelper.attachToRecyclerView(recyclerView);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.i(TAG,"onActivityResult: 列表——收到了————requestCode="+requestCode+",resultCode="+resultCode);
		if(resultCode==RESULT_OK){
			Ann ann=data.getParcelableExtra("Ann");
			int pos=data.getIntExtra("pos",0);
			if(requestCode==ANN_EDIT){
				entities.set(pos,ann);
			}else if(requestCode==ANN_INSERT){
				entities.add(0,ann);
			}
			adapter.notifyDataSetChanged();
		}
	}

	//友盟统计
	public void onResume() {
		super.onResume();
		StatService.onPageStart(this, "纪念日");
	}

	public void onPause() {
		super.onPause();
		StatService.onPageEnd(this, "纪念日");
	}

}
