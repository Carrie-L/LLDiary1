package com.carrie.lldiary.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.carrie.lldiary.dao.EmojiUpdateTime;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EMOJI_UPDATE_TIME".
*/
public class EmojiUpdateTimeDao extends AbstractDao<EmojiUpdateTime, String> {

    public static final String TABLENAME = "EMOJI_UPDATE_TIME";

    /**
     * Properties of entity EmojiUpdateTime.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property ObjectId = new Property(0, String.class, "objectId", true, "OBJECT_ID");
        public final static Property Url = new Property(1, String.class, "url", false, "URL");
        public final static Property Path = new Property(2, String.class, "path", false, "PATH");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property Status = new Property(4, Integer.class, "status", false, "STATUS");
        public final static Property UpdateAt = new Property(5, String.class, "updateAt", false, "UPDATE_AT");
        public final static Property InsertTime = new Property(6, String.class, "insertTime", false, "INSERT_TIME");
    };


    public EmojiUpdateTimeDao(DaoConfig config) {
        super(config);
    }
    
    public EmojiUpdateTimeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EMOJI_UPDATE_TIME\" (" + //
                "\"OBJECT_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: objectId
                "\"URL\" TEXT," + // 1: url
                "\"PATH\" TEXT," + // 2: path
                "\"NAME\" TEXT," + // 3: name
                "\"STATUS\" INTEGER," + // 4: status
                "\"UPDATE_AT\" TEXT," + // 5: updateAt
                "\"INSERT_TIME\" TEXT);"); // 6: insertTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EMOJI_UPDATE_TIME\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, EmojiUpdateTime entity) {
        stmt.clearBindings();
 
        String objectId = entity.getObjectId();
        if (objectId != null) {
            stmt.bindString(1, objectId);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(2, url);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(3, path);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(5, status);
        }
 
        String updateAt = entity.getUpdateAt();
        if (updateAt != null) {
            stmt.bindString(6, updateAt);
        }
 
        String insertTime = entity.getInsertTime();
        if (insertTime != null) {
            stmt.bindString(7, insertTime);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public EmojiUpdateTime readEntity(Cursor cursor, int offset) {
        EmojiUpdateTime entity = new EmojiUpdateTime( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // objectId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // url
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // path
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // status
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // updateAt
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // insertTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, EmojiUpdateTime entity, int offset) {
        entity.setObjectId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setUrl(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPath(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setStatus(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setUpdateAt(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setInsertTime(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(EmojiUpdateTime entity, long rowId) {
        return entity.getObjectId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(EmojiUpdateTime entity) {
        if(entity != null) {
            return entity.getObjectId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}