package com.carrie.lldiary.db;

import com.carrie.lldiary.entity.ContentEntity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DiaryDB extends SQLiteOpenHelper {
	private SQLiteDatabase db;
	private final static String DATABASE_NAME="diary.db";
	private final static String TABLE_NAME="diary";
	private final static int VERSION=1;
	
	private final static String CREATE_TABLE="create table "+TABLE_NAME+"( " +
			"id integer primary key autoincrement, " +
			"title text," +
			"content text," +
			"date text," +
			"bg text);";

	public DiaryDB(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		/*switch (oldVersion) {
		case 1:
			db.execSQL("alert table"+TABLE_NAME+" add column time text");
			break;

		default:
			break;
		}*/
	}

	public void insertDiary(ContentEntity entity){
		db=getWritableDatabase();
		db.execSQL("insert into "+TABLE_NAME+"( title,content,date,bg) values (?,?,?,?)",
				new Object[]{entity.getTitle(),entity.getContent(),entity.getDate(),entity.getBackground()});
	}
}
