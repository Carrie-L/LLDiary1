package com.carrie.lldiary.db;

import com.carrie.lldiary.entity.AccountEntity;
import com.carrie.lldiary.entity.AnnEntity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountDB extends SQLiteOpenHelper {

	private SQLiteDatabase db;
	private final static String DATABASE_NAME="llDiary.db";
	private final static String TABLE_NAME="account";
	private final static int VERSION=1;
	
	private final static String CREATE_TABLE="create table "+TABLE_NAME+"( " +
			"id integer primary key autoincrement, " +
			"userName text," +
			"password text +);";

	public AccountDB(Context context) {
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
	
	public void insertAccount(String username,String pwd){
		db=getWritableDatabase();
		db.execSQL("insert into "+TABLE_NAME+"( userName,password) values (?,?)",
				new Object[]{username,pwd});
	}
	
	public void insertAccountPwd(String pwd){
		db=getWritableDatabase();
		db.execSQL("insert into "+TABLE_NAME+"( password) values (?)",
				new Object[]{pwd});
	}
	
	public void insertAccountName(String username){
		db=getWritableDatabase();
		db.execSQL("insert into "+TABLE_NAME+"( userName) values (?)",
				new Object[]{username});
	}

}
