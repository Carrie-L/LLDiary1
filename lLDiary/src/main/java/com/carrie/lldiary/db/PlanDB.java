package com.carrie.lldiary.db;

import com.carrie.lldiary.entity.ContentEntity;
import com.carrie.lldiary.entity.PlanEntity;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PlanDB extends SQLiteOpenHelper {
	private SQLiteDatabase db;
	private final static String DATABASE_NAME="llDiary.db";
	private final static String TABLE_NAME="plan";
	private final static int VERSION=1;
	
	private static final String CREATE_TABLE="create table "+TABLE_NAME+"( " +
			"id integer primary key autoincrement, " +
			"content text," +
			"isComplish text,"+//"未完成"、“已完成”
			"isRemind text,"+
			"date text," +
			"time text);";

	public PlanDB(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	public void insertPlan(PlanEntity entity){
		db=getWritableDatabase();
		db.execSQL("insert into "+TABLE_NAME+"( content,isComplish,isRemind,date,time) values (?,?,?,?,?)",
				new Object[]{entity.getContent(),entity.getIsComplish(),entity.getIsRemind(),entity.getDate(),entity.getTime()});
	}

	
}
