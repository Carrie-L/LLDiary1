package com.carrie.lldiary.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.carrie.lldiary.App;
import com.carrie.lldiary.dao.Ann;
import com.carrie.lldiary.dao.AnnDao;
import com.carrie.lldiary.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.carrie.lldiary.base.DatabaseManager.DB_PATH;
import static com.google.android.gms.common.internal.zze.DB;

/**
 * Created by new on 2017/4/14.
 */

public class OldDBHelper {
    private static final String TAG="OldDBHelper";
    private static final String DB_MONEY="MoneyBook.db";
    private static final String DB_ANN="anniversary.db";
    private static final String DB_DIARY="diary.db";
    private static final String DB_NOTE="note.db";
    private static final String DB_PLAN="plan.db";
    private static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/com.carrie.lldiary/databases/";
    //ANN
    public static void getOldAnnData(){
        SQLiteDatabase db=openDatabase(DB_ANN);
        if(db!=null){
            LogUtil.i(TAG,"getOldAnnData_db!=null");
            Cursor cursor=db.rawQuery("select * from anniversary",null);
            DBHelper dbHelper=App.dbHelper;
            if(cursor!=null){
                Ann entity;
                while (cursor.moveToNext()){
                    long id=cursor.getInt(0);
//                    entity=new Ann(id,cursor.getString(1),null,Integer.valueOf(cursor.getString(cursor.getColumnIndex("image"))),
//                            cursor.getString(cursor.getColumnIndex("date")),
//                            !cursor.getString(cursor.getColumnIndex("isRemind")).contains("不"));
//                    LogUtil.i(TAG,"ann="+entity.toString());
//                    dbHelper.insertAnn(entity);
                }
                cursor.close();
                db.close();
            }
        }else{
            LogUtil.i(TAG,"getOldAnnData_db==null");
        }

    }

    public static SQLiteDatabase openDatabase(String dbfile) {
        LogUtil.i(TAG,"openDatabase:"+dbfile);
        SQLiteDatabase db=null ;
        if(new File(DB_PATH+dbfile).exists()){
            LogUtil.e(TAG, "数据库存在，直接打开");
            db = SQLiteDatabase.openDatabase(DB_PATH+dbfile, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        }
        return db;
    }
}
