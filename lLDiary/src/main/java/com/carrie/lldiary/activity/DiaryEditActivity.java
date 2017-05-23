package com.carrie.lldiary.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.carrie.lldiary.R;
import com.carrie.lldiary.adapter.LLAdapter;
import com.carrie.lldiary.db.DiaryDB;
import com.carrie.lldiary.entity.ContentEntity;
import com.carrie.lldiary.utils.DateUtil;
import com.carrie.lldiary.utils.LogUtil;
import com.carrie.lldiary.view.DialogIconView;
import com.carrie.lldiary.view.EmojiconView;
import com.carrie.lldiary.view.TabViewPager;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.List;

public class DiaryEditActivity extends FragmentActivity implements OnClickListener{
	private static final String TABLE_NAME = "diary";
	@ViewInject(R.id.ib_back)
	private ImageButton ib_back;
	@ViewInject(R.id.ib_save)
	private ImageButton ib_save;
	public static EditText et_content;

	private RelativeLayout mLinearLayout;

	private SharedPreferences sp;
	public static List<ContentEntity> entities;
	private ContentEntity entity;
	private LLAdapter adapter;
	private DiaryDB mDiaryDB;
	private boolean popupShowing = false;
	private int mScreenWidth;
	private int bg;
	private SQLiteDatabase db;
	private String content;
	private String title;
	private int id;
//	private ImageView iv_bg;
	
//	private FaceRelativeLayout mFaceRl;
	private TabViewPager mFaceRl;

	private int weatherIcon,moodIcon;


	public static final int CHANGE_DIARY_BG = 1;
	private static final String TAG = "DiaryEditActivity";
	private ImageView iv_weather;
	private ImageView iv_mood;
	private DialogIconView iconView;
	private AlertDialog mDialog;
	private static SimpleDraweeView sdv_bg;
	private Resources resources;

	public void initData() {
		mDiaryDB = new DiaryDB(this);
		ib_back.setOnClickListener(this);
		ib_save.setOnClickListener(this);

		et_content.setOnClickListener(this);


	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_dairy);
		ViewUtils.inject(this);
		findView();
		MyActivityManager.add(this);

		resources = getResources();

		sp = getSharedPreferences("diary", MODE_PRIVATE);
		entities = DiaryActivity.entities;
		PushAgent.getInstance(this).onAppStart();// 统计应用启动数据
		Intent intent = getIntent();

		iconView = new DialogIconView(getApplicationContext());

		mFaceRl=(TabViewPager) findViewById(R.id.face_rl);
		iv_weather = (ImageView) findViewById(R.id.iv_weather);
		iv_mood = (ImageView) findViewById(R.id.iv_mood);
        TextView tv_date = (TextView) findViewById(R.id.tv_date);

        tv_date.setText(DateUtil.getCurrent_Date_Time_Week());
		weatherIcon=R.drawable.weather_1;
		moodIcon=R.drawable.emoji_1;

		iv_weather.setImageResource(weatherIcon);
		iv_mood.setImageResource(moodIcon);

		iv_weather.setOnClickListener(this);
		iv_mood.setOnClickListener(this);


		initData();
	}

	private void findView(){
		mLinearLayout=(RelativeLayout) findViewById(R.id.ll_editdiary_page);
		et_content=(EditText) findViewById(R.id.et_editdiary_content);
		sdv_bg = (SimpleDraweeView) findViewById(R.id.sdv_diary_bg);
	}

	public static Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CHANGE_DIARY_BG:
				int image= (int) msg.obj;
				sdv_bg.setBackgroundResource(image);
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 编辑日记
	 */
	public void editDiary() {
		String body = getIntent().getStringExtra("body");
		String date = DateUtil.date4();
		db = mDiaryDB.getWritableDatabase();
		entity = new ContentEntity(title, content, date, bg);
		if (body != null) {
			Cursor cursor = db.query(TABLE_NAME, new String[] { "id" },
					"content=?", new String[] { body }, null, null, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					id = cursor.getInt(cursor.getColumnIndex("id"));
					System.out.println("id=" + id);
				}
				cursor.close();
			}
			
			
			content = et_content.getText().toString();
			title = getIntent().getStringExtra("title");

	//		title = et_Title.getText().toString().trim();
			entity.setContent(content);
			entity.setTitle(title);
			entity.setDate(date);
			entity.setBackground(bg);
			entities.add(entity);

			ContentValues cv = new ContentValues();
			cv.put("title", title);
			cv.put("content", content);
			cv.put("date", date);
			cv.put("bg", bg);
			db.update(TABLE_NAME, cv, "id=?",
					new String[] { String.valueOf(id) });
			Intent intent = new Intent(this, DiaryActivity.class);
			startActivity(intent);
			finish();
		} else {
//			title = et_Title.getText().toString().trim();
			
	//		LogUtil.i(TAG, "mFaceRl.getImageString()="+mFaceRl.getImageString());
		
			content=et_content.getText().toString().toString();
//			SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(getApplicationContext(),content );
//			et_content.append(spannableString);
			
			
			if (TextUtils.isEmpty(content)) {
				Toast.makeText(this, "随便输点什么吧~", Toast.LENGTH_SHORT).show();
			} else {
				entity = new ContentEntity(title, content, date, bg);
				entity.setTitle(title);
				entity.setContent(content);
				entity.setDate(date);
				entity.setBackground(bg);
				entities.add(entity);
				System.out.println(entities.size());

				mDiaryDB.insertDiary(entity);

				Intent i = new Intent(this, DiaryActivity.class);
				startActivity(i);
				finish();
			}
		}
		// 自定义事件次数统计
		StatService.onEvent(this, "editDiary", "编辑日记", 1);
		// 自定义事件时长统计
		StatService.onEventEnd(this, "editDiary", "编辑日记");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:// 返回主界面
			finish();
			break;
		case R.id.ib_save:// 保存
			StatService.onEventStart(this, "editDiary", "编辑日记");
			editDiary();
			break;
		case R.id.et_editdiary_content:
			LogUtil.i(TAG, "et_editdiary_content");
			mFaceRl.hideFaceView(true);
			break;

			case R.id.iv_weather:
				selectWeather();
				break;

			case R.id.iv_mood:
				selectMood();
				break;

		default:
			break;
		}
	}

	private void selectWeather(){
		iconView.setTitle("请选择天气");
		iconView.setData("weather_",1,35,40);
		iconView.alertDialog(this);

		iconView.setOnDialogItemClickListener(new EmojiconView.OnDialogItemClickListener() {
			@Override
			public void selectIcon(int resId) {
				((ViewGroup) iconView.getParent()).removeView(iconView);
				iv_weather.setImageResource(resId);
				weatherIcon=resId;
				LogUtil.i(TAG,"天气图标="+resId);
			}
		});
	}

	private void selectMood(){
		iconView.setTitle("请选择心情");
		iconView.setData("emoji_",1,37,40);
		iconView.alertDialog(this);

		iconView.setOnDialogItemClickListener(new EmojiconView.OnDialogItemClickListener() {
			@Override
			public void selectIcon(int resId) {
				((ViewGroup) iconView.getParent()).removeView(iconView);
				iv_mood.setImageResource(resId);
				moodIcon=resId;
				LogUtil.i(TAG,"心情图标="+resId);
			}
		});
	}

	public void changeUI() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = CHANGE_DIARY_BG;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	// 友盟统计
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		StatService.onPageStart(this, "日记编辑");
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		StatService.onPageEnd(this, "日记编辑");
	}


	

}
