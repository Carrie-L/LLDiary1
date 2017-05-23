package com.carrie.lldiary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carrie.lldiary.entity.NoteEntity;
import com.carrie.lldiary.entity.PlanEntity;

public class NoteDB extends SQLiteOpenHelper{
	private SQLiteDatabase db;
	private final static String DATABASE_NAME="note.db";
	private final static String TABLE_NAME="note";
	private final static int VERSION=1;
	
	private static final String CREATE_TABLE="create table "+TABLE_NAME+"( " +
			"id integer primary key autoincrement, " +
			"content text," +
			"date text);";

	public NoteDB(Context context) {
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
	
	public void insertPlan(NoteEntity entity){
		db=getWritableDatabase();
		db.execSQL("insert into "+TABLE_NAME+"( content,date) values (?,?)",
				new Object[]{entity.getContent(),entity.getDate()});
	}

	


}
