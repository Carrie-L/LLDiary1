package com.carrie.lldiary.db;

import com.carrie.lldiary.entity.AnnEntity;
import com.carrie.lldiary.entity.ContentEntity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AnnDB extends SQLiteOpenHelper {
	private SQLiteDatabase db;
	private final static String DATABASE_NAME="anniversary.db";
	private final static String TABLE_NAME="anniversary";
	private final static int VERSION=1;
	
	private final static String CREATE_TABLE="create table "+TABLE_NAME+"( " +
			"id integer primary key autoincrement, " +
			"content text," +
			"date text," +
			"isRemind text," +
			"image text);";

	public AnnDB(Context context) {
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
	
	public void insertAnn(AnnEntity entity){
		db=getWritableDatabase();
		db.execSQL("insert into "+TABLE_NAME+"( content,date,image,isRemind) values (?,?,?,?)",
				new Object[]{entity.getContent(),entity.getDate(),entity.getImage(),entity.getIsRemind()});
	}

}
