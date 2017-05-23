package com.carrie.lldiary.activity;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mobstat.StatService;
import com.carrie.lldiary.R;
import com.carrie.lldiary.R.id;
import com.carrie.lldiary.R.layout;
import com.carrie.lldiary.db.NoteDB;
import com.carrie.lldiary.entity.NoteEntity;
import com.carrie.lldiary.utils.DateUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class NoteEditActivity extends Activity implements OnClickListener {
	private static final String TABLE_NAME = "note";
	private TextView tv_title;
	private EditText et_note_content;
	private NoteEntity entity;
	private List<NoteEntity> entities;
	private NoteDB mNoteDB;
	private ImageButton ib_back, ib_save;
	private Intent intent;
	private SQLiteDatabase db;
	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_note);
		MyActivityManager.add(this);
		PushAgent.getInstance(this).onAppStart();// 统计应用启动数据
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("便利贴");
		et_note_content = (EditText) findViewById(R.id.et_note_content);
		entities = StikyNoteActivity.entities;
		mNoteDB = new NoteDB(this);

		ib_back = (ImageButton) findViewById(R.id.ib_back);
		ib_save = (ImageButton) findViewById(R.id.ib_save);
		ib_back.setOnClickListener(this);
		ib_save.setOnClickListener(this);

		Intent intent = getIntent();
		String content = intent.getStringExtra("content");
		if (content != null) {
			et_note_content.setText(content);
			et_note_content.setSelection(content.length());
		}
	}

	public void editNote() {
		Intent intent = getIntent();
		String content = intent.getStringExtra("content");
		String date = DateUtil.date3();
		db = mNoteDB.getWritableDatabase();
		entity = new NoteEntity();
		entity.setDate(date);
		if (content != null) {
			Cursor cursor = db.query(TABLE_NAME, new String[] { "id" },
					"content=?", new String[] { content }, null, null, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					id = cursor.getInt(cursor.getColumnIndex("id"));
					System.out.println("id=" + id);
				}
				cursor.close();
			}
			content = et_note_content.getText().toString().trim();
			entity.setContent(content);
			entities.add(entity);
			ContentValues cv = new ContentValues();
			cv.put("content", content);
			cv.put("date", date);
			db.update(TABLE_NAME, cv, "id=?",
					new String[] { String.valueOf(id) });
			intent = new Intent(this, StikyNoteActivity.class);
			startActivity(intent);
			finish();
		} else {
			content = et_note_content.getText().toString().trim();
			if (TextUtils.isEmpty(content)) {
				Toast.makeText(this, "随便输点什么吧~", 0).show();
			} else {
				entity.setContent(content);
				entities.add(entity);
				mNoteDB.insertPlan(entity);

				intent = new Intent(this, StikyNoteActivity.class);
				startActivity(intent);
				finish();
			}
		}
		// 自定义事件次数统计
		StatService.onEvent(this, "editNote", "编辑便利贴", 1);
		// 自定义事件时长统计
		StatService.onEventEnd(this, "editNote", "编辑便利贴");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			break;
		case R.id.ib_save:
			StatService.onEventStart(this, "editNote", "编辑便利贴");
			editNote();
		}
	}

	// 友盟统计
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		StatService.onPageStart(this, "便利贴编辑");
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		StatService.onPageEnd(this, "便利贴编辑");
	}
}
