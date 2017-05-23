package com.carrie.lldiary.db;

import android.content.Context;

/**
 * Created by Administrator on 2016/5/6 0006.
 */
public class EmojiDB {
    private static final String TAG ="EmojiDB" ;
    public final String TABLE_NAME="EmojiUpdateTime";

    private Context mContext;



//    /**
//     * 查询已经下载的emoji，显示在横向列表中
//     */
//    public ArrayList<Emoji> queryDownedEmoji(ArrayList<Emoji> list){
//        SQLiteDatabase db=this.getReadableDatabase();
//        Cursor cursor=db.query(TABLE_NAME,null,"status=?",new String[]{1+""},null,null,null,null);
//        if(cursor!=null){
//            while(cursor.moveToNext()){
//                Emoji emoji=new Emoji();
//                String emojiPath=cursor.getString(cursor.getColumnIndex("emojiPath"));
//                LogUtil.i(TAG,"emojiPath="+emojiPath);
////                emoji.setCharacter(AppUtils.getSubLastStr(emojiPath,'.'));
//                emoji.setFaceName(emojiPath);
//                list.add(emoji);
//            }
//        }
//        cursor.close();
//        db.close();
//        return list;
//    }
//
//    /**
//     * 查询已经下载的emoji，时间大于上次最新时间
//     * <p> emoji.setFaceName(emojiPath);
//     */
//    public ArrayList<Emoji> queryLastestDownedEmoji(ArrayList<Emoji> list,String lastUpdateAt){
//        SQLiteDatabase db=this.getReadableDatabase();
////        Cursor cursor=db.query(TABLE_NAME,null,"status=? and updateAt>?",new String[]{1+"",lastUpdateAt},null,null,null,null);
//        Cursor cursor=db.rawQuery("select * from EmojiUpdateTime where status=1 and insertTime>? order by insertTime asc",new String[]{lastUpdateAt});
//        LogUtil.i(TAG,"cursor="+cursor);
//        if(cursor!=null){
//            while(cursor.moveToNext()){
//                Emoji emoji=new Emoji();
//                String emojiPath=cursor.getString(cursor.getColumnIndex("emojiPath"));
//                LogUtil.i(TAG,"emojiPath="+emojiPath);
////                emoji.setCharacter(AppUtils.getSubLastStr(emojiPath,'.'));
//                emoji.setFaceName(emojiPath);
//                list.add(emoji);
//            }
//        }
//        cursor.close();
//        db.close();
//        return list;
//    }
//
//    //添加对数据库的操作方法
//
////    /**
////     * 插入更新时间和ObjectId  :  EmojiUpateTime
////     * @param db  可为null
////     * @param objectId
////     * @param updateAt
////     */
////    public void insertUpdateTime(SQLiteDatabase db,String objectId,String updateAt) {
////        boolean bool=false;
////        if (db == null) {
////            db = this.getReadableDatabase();
////            bool=true;
////        }
////        ContentValues cv = new ContentValues();
////        cv.put("ObjectId", objectId);
////        cv.put("UpdateAt", updateAt);
////
////        db.insert(TABLE_NAME, null, cv);
////
////        if(bool){
////            db.close();
////        }
////
////    }
//
//
//    /**
//     * 插入更新时间和ObjectId  :  EmojiUpateTime
//     * @param db  可为null
//     *
//     */
//    public void insertUpdateTime(SQLiteDatabase db,EmojiUpdateAt emoji) {
//        boolean bool=false;
//        if (db == null) {
//            db = this.getReadableDatabase();
//            bool=true;
//        }
//
//        LogUtil.i(TAG,"DateUtil.getCurrentTime2()="+DateUtil.getCurrentTime2());
//
//        ContentValues cv = new ContentValues();
//        cv.put("objectId", emoji.objectId);
//        cv.put("updateAt", emoji.updateAt);
//        cv.put("emojiPath",emoji.emojiPath);
//        cv.put("urlPath",emoji.urlPath);
//        cv.put("emojiName",emoji.emojiName);
//        cv.put("status",emoji.status);
//        cv.put("insertTime", DateUtil.getCurrentTime2());
//        db.insert(TABLE_NAME, null, cv);
//
//        if(bool){
//            db.close();
//        }
//
//    }
//
//    /**
//     * 查询EmojiUpateTime
//     * @param db,可为null
//     * @return emojis
//     */
//    public ArrayList<EmojiUpdateAt> queryUpdateTime(SQLiteDatabase db) {
//        ArrayList<EmojiUpdateAt> emojis = new ArrayList<EmojiUpdateAt>();
//        String objectId = "";
//        String updateAt = "";
//        boolean bool=false;
//        if (db == null) {
//            db = this.getReadableDatabase();
//            bool=true;
//        }
//        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, "updateAt desc", null);
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                EmojiUpdateAt emoji = new EmojiUpdateAt();
//                emoji.objectId = cursor.getString(cursor.getColumnIndex("objectId"));
//                emoji.emojiName= cursor.getString(cursor.getColumnIndex("emojiName"));
//                emoji.emojiPath= cursor.getString(cursor.getColumnIndex("emojiPath"));
//                emoji.urlPath= cursor.getString(cursor.getColumnIndex("urlPath"));
//                emoji.updateAt=   cursor.getString(cursor.getColumnIndex("updateAt"));
//                emoji.status=   cursor.getInt(cursor.getColumnIndex("status"));
//                emoji.insertTime=   cursor.getString(cursor.getColumnIndex("insertTime"));
//                emojis.add(emoji);
//            }
//        }
//        cursor.close();
//        if(bool){
//            db.close();
//        }
//        return emojis;
//    }
//
//    /**
//     * 数据是否已经存在
//     * @param db
//     * @param objectId
//     * @return
//     */
//    public boolean isEmojiExsited(SQLiteDatabase db,String objectId){
//        boolean bool=false;
//        if (db == null) {
//            db = this.getReadableDatabase();
//            bool=true;
//        }
//        Cursor cursor=db.query(TABLE_NAME,new String[]{"objectId"},"objectId=?",new String[]{objectId},null,null,null);
//        if(cursor!=null&&cursor.moveToNext()){
//            return true;
//        }
//        cursor.close();
//        if(bool){
//            db.close();
//        }
//        return false;
//    }
//
//    public void updateEmoji(SQLiteDatabase db,EmojiUpdateAt emojiUpdateAt){
//        boolean bool=false;
//        if (db == null) {
//            db = this.getReadableDatabase();
//            bool=true;
//        }
//        ContentValues cv=new ContentValues();
//        cv.put("updateAt", emojiUpdateAt.updateAt);
//        cv.put("emojiPath",emojiUpdateAt.emojiPath);
//        cv.put("urlPath",emojiUpdateAt.urlPath);
//        cv.put("emojiName",emojiUpdateAt.emojiName);
//        cv.put("status",emojiUpdateAt.status);
//        cv.put("insertTime",emojiUpdateAt.insertTime);
//        db.update(TABLE_NAME,cv,"objectId=?",new String[]{emojiUpdateAt.objectId});
//    }
//
//    /**
//     * 更新下载状态
//     * @param db
//     * @param objectId
//     */
//    public void updateEmojiStatus(SQLiteDatabase db, String objectId){
//        boolean bool=false;
//        if (db == null) {
//            db = this.getReadableDatabase();
//            bool=true;
//        }
//        ContentValues cv=new ContentValues();
//        cv.put("status",1);
//        cv.put("insertTime",DateUtil.getCurrentTime2());
//        db.update(TABLE_NAME,cv,"objectId=?",new String[]{objectId});
//    }
//
//    /**
//     * 检测是否已下载
//     * @param db
//     * @param objectId
//     * @return 1 true 已下载，0 false 未下载
//     */
//    public boolean isEmojiDowned(SQLiteDatabase db,String objectId){
//        int status=0;
//        boolean bool=false;
//        if (db == null) {
//            db = this.getReadableDatabase();
//            bool=true;
//        }
//        Cursor cursor=db.query(TABLE_NAME,new String[]{"status"},"objectId=?",new String[]{objectId},null,null,null);
//        if(cursor!=null){
//            while(cursor.moveToNext()){
//                status= cursor.getInt(cursor.getColumnIndex("status"));
//            }
//        }
//        cursor.close();
//        if(bool){
//            db.close();
//        }
//        if(status==1){
//            return true;
//        }
//        return false;
//    }
//
////    /**
////     * 查询EmojiUpateTime
////     * @param db,可为null
////     * @return
////     */
////    public ArrayList<EmojiFile> queryUpdateTime(SQLiteDatabase db) {
////        ArrayList<EmojiFile> emojis = new ArrayList<EmojiFile>();
////        String objectId = "";
////        String updateAt = "";
////        String urlFile="";
////        boolean bool=false;
////        if (db == null) {
////            db = this.getReadableDatabase();
////            bool=true;
////        }
////        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
////        if (cursor != null) {
////            while (cursor.moveToNext()) {
////                objectId = cursor.getString(cursor.getColumnIndex("objectId"));
////                updateAt = cursor.getString(cursor.getColumnIndex("urlFile"));
////                updateAt = cursor.getString(cursor.getColumnIndex("emojiName"));
////                urlFile = cursor.getString(cursor.getColumnIndex("emoji"));
////                updateAt = cursor.getString(cursor.getColumnIndex("updateAt"));
////
////                EmojiFile emoji = new EmojiFile();
////                emoji.setObjectId(objectId);
////                emoji.set
////                emojis.add(emoji);
////            }
////        }
////        cursor.close();
////        if(bool){
////            db.close();
////        }
////        return emojis;
////    }
//
//    /**
//     * 获取最新的时间
//     * @param db null
//     * @return updateAt
//     */
//    public String getLastestUpdateAt(SQLiteDatabase db) {
//        boolean bool=false;
//        if (db == null) {
//            db = this.getReadableDatabase();
//            bool=true;
//        }
//        String updateAt = "";
//        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME+" where updateAt=(select max(updateAt) from "+TABLE_NAME+")", null);
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                updateAt = cursor.getString(cursor.getColumnIndex("updateAt"));
//            }
//        }
//        cursor.close();
//        if(bool){
//            db.close();
//        }
//        return updateAt;
//    }
//
//    /**
//     * 获取最新的时间
//     * @param db null
//     * @return InsertTime
//     */
//    public String getLastestInsertTime(SQLiteDatabase db) {
//        boolean bool=false;
//        if (db == null) {
//            db = this.getReadableDatabase();
//            bool=true;
//        }
//        String updateAt = "";
//        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME+" where insertTime=(select max(insertTime) from "+TABLE_NAME+")", null);
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                updateAt = cursor.getString(cursor.getColumnIndex("insertTime"));
//            }
//        }
//        cursor.close();
//        if(bool){
//            db.close();
//        }
//        return updateAt;
//    }
}
