package com.carrie.lldiary.activity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.baidu.mobstat.StatService;
import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.LLAdapter;
import com.carrie.lldiary.base.MyBaseActivity;
import com.carrie.lldiary.db.DiaryDB;
import com.carrie.lldiary.entity.ContentEntity;
import com.carrie.lldiary.view.TitleView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

public class DiaryActivity extends MyBaseActivity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener {


	public static List<ContentEntity> entities = new ArrayList<ContentEntity>();
	private ContentEntity entity;
	private LLAdapter adapter;
	private SharedPreferences sp;
	private SQLiteDatabase db;
	private DiaryDB mDiaryDB;
	private final static String TABLE_NAME = "diary";
	private static final String tag = "MainActivity";
	private final static int REFRESH = 0;


	private RecyclerView recyclerView;


	@Override
	public View initView(LayoutInflater inflater, ViewGroup container) {
		View view=inflater.inflate(R.layout.activity_diary,container,false);
		recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_diary);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

		TitleView titleView = (TitleView)findViewById(R.id.title_view);
		titleView.setTitle(getString(R.string.my_diary));
		titleView.setOnRightClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(getApplicationContext(), DiaryEditActivity.class);
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
		return view;
	}

	@Override
	public void initData() {
		MyActivityManager.add(this);
		PushAgent.getInstance(this).onAppStart();//统计应用启动数据

	}

	@Override
	public void goBack() {

	}


	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH:
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};

	private Intent intent;

	/**
	 * 查询数据库并显示内容
	 */
	public void queryDB() {
		db = mDiaryDB.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				String title = cursor.getString(cursor.getColumnIndex("title"));
				String content = cursor.getString(cursor
						.getColumnIndex("content"));
				String date = cursor.getString(cursor.getColumnIndex("date"));
				int bg = cursor.getInt(cursor.getColumnIndex("bg"));
				entity = new ContentEntity(title, content, date, bg);
				entity.setTitle(title);
				entity.setContent(content);
				entity.setDate(date);
				entity.setBackground(bg);
				entities.add(entity);
			}
		}
		db.close();
		cursor.close();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			break;
		case R.id.ib_add:
			intent = new Intent(this, DiaryEditActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, DiaryDetailsActivity.class);
		intent.putExtra("position", position);
		intent.putExtra("title", entities.get(position).getTitle());
		intent.putExtra("content", entities.get(position).getContent());
		intent.putExtra("bg", entities.get(position).getBackground());
		intent.putExtra("date", entities.get(position).getDate());
		startActivity(intent);
	}

	// 长按删除日记
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("确定删除吗？")
				.setMessage("删除后将不可恢复")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						db = mDiaryDB.getWritableDatabase();
						db.delete(TABLE_NAME, "title=?",
								new String[] { entities.get(position)
										.getTitle() });
						entities.remove(position);
						new Thread(new Runnable() {
							@Override
							public void run() {
								Message msg = new Message();
								msg.what = REFRESH;
								mHandler.sendMessage(msg);
							}
						}).start();
						dialog.dismiss();
						db.close();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
		return true;
	}

	
	//友盟统计
		public void onResume() {
			super.onResume();
			MobclickAgent.onResume(this);
			StatService.onPageStart(this, "日记本");
		}

		public void onPause() {
			super.onPause();
			MobclickAgent.onPause(this);
			StatService.onPageEnd(this, "日记本");
		}
}
