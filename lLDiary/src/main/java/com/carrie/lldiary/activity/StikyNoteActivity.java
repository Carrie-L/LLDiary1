package com.carrie.lldiary.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.carrie.lldiary.R;
import com.carrie.lldiary.R.id;
import com.carrie.lldiary.R.layout;
import com.carrie.lldiary.adapter.NoteAdapter;
import com.carrie.lldiary.db.NoteDB;
import com.carrie.lldiary.entity.NoteEntity;
import com.carrie.lldiary.entity.PlanEntity;
import com.carrie.lldiary.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

public class StikyNoteActivity extends Activity implements OnItemClickListener, OnItemLongClickListener, OnClickListener {
	private static final String TABLE_NAME = "note";
	protected static final int REFRESH = 0;
	private TextView tv_title;
	private ListView lv_note;
	private NoteAdapter adapter;
	public  static List<NoteEntity> entities;
	private ImageButton ib_back,ib_add;
	private NoteDB mNoteDB;
	private NoteEntity entity;
	private SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_stiky_note);
		MyActivityManager.add(this);
		PushAgent.getInstance(this).onAppStart();//统计应用启动数据
		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_title.setText("便利贴");
		lv_note=(ListView) findViewById(R.id.lv_stiky_note);
		
		entities=new ArrayList<NoteEntity>();
		entities.clear();
		mNoteDB=new NoteDB(this);
		db=mNoteDB.getWritableDatabase();
		queryDB();
		
		adapter=new NoteAdapter(entities, this);
		lv_note.setAdapter(adapter);
		lv_note.setOnItemClickListener(this);
		lv_note.setOnItemLongClickListener(this);
		
		ib_add = (ImageButton) findViewById(R.id.ib_add);
		ib_back = (ImageButton) findViewById(R.id.ib_back);
		ib_add.setOnClickListener(this);
		ib_back.setOnClickListener(this);
		
	}

	/**
	 * 查询数据库并显示内容
	 */
	public void queryDB() {
		db = mNoteDB.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				String content = cursor.getString(cursor
						.getColumnIndex("content"));
				String date = cursor.getString(cursor.getColumnIndex("date"));
				entity = new NoteEntity();
				entity.setContent(content);
				entity.setDate(date);				
				entities.add(entity);
			}
		}
		db.close();
		cursor.close();
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("确定删除吗？")
				.setMessage("删除后将不可恢复")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						db = mNoteDB.getWritableDatabase();
						db.delete(TABLE_NAME, "content=?",
								new String[] { entities.get(position)
										.getContent() });
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, NoteEditActivity.class);
		intent.putExtra("position", position);
		intent.putExtra("content", entities.get(position).getContent());
		startActivity(intent);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_add:
			Intent intent = new Intent(this, NoteEditActivity.class);
			startActivity(intent);
			break;
		case R.id.ib_back:
			finish();
			break;

		default:
			break;
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH:
				adapter.notifyDataSetChanged();
				break;			
			}
		};
	};
	
	
	// 友盟统计
		public void onResume() {
			super.onResume();
			MobclickAgent.onResume(this);
			StatService.onPageStart(this, "便利贴");
		}

		public void onPause() {
			super.onPause();
			MobclickAgent.onPause(this);
			StatService.onPageEnd(this, "便利贴");
		}
}
