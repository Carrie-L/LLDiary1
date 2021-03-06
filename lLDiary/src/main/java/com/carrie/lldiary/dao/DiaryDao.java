package com.carrie.lldiary.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.carrie.lldiary.dao.Diary;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DIARY".
*/
public class DiaryDao extends AbstractDao<Diary, Long> {

    public static final String TABLENAME = "DIARY";

    /**
     * Properties of entity Diary.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Content = new Property(2, String.class, "content", false, "CONTENT");
        public final static Property CreateDate = new Property(3, String.class, "CreateDate", false, "CREATE_DATE");
        public final static Property UpdateDate = new Property(4, String.class, "UpdateDate", false, "UPDATE_DATE");
        public final static Property Weather = new Property(5, String.class, "weather", false, "WEATHER");
        public final static Property Mood = new Property(6, String.class, "mood", false, "MOOD");
        public final static Property Bg = new Property(7, String.class, "bg", false, "BG");
        public final static Property TextSize = new Property(8, Integer.class, "textSize", false, "TEXT_SIZE");
        public final static Property TextColor = new Property(9, Integer.class, "textColor", false, "TEXT_COLOR");
        public final static Property Label = new Property(10, String.class, "label", false, "LABEL");
    };


    public DiaryDao(DaoConfig config) {
        super(config);
    }
    
    public DiaryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DIARY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TITLE\" TEXT," + // 1: title
                "\"CONTENT\" TEXT NOT NULL ," + // 2: content
                "\"CREATE_DATE\" TEXT NOT NULL ," + // 3: CreateDate
                "\"UPDATE_DATE\" TEXT NOT NULL ," + // 4: UpdateDate
                "\"WEATHER\" TEXT," + // 5: weather
                "\"MOOD\" TEXT," + // 6: mood
                "\"BG\" TEXT," + // 7: bg
                "\"TEXT_SIZE\" INTEGER," + // 8: textSize
                "\"TEXT_COLOR\" INTEGER," + // 9: textColor
                "\"LABEL\" TEXT);"); // 10: label
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DIARY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Diary entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
        stmt.bindString(3, entity.getContent());
        stmt.bindString(4, entity.getCreateDate());
        stmt.bindString(5, entity.getUpdateDate());
 
        String weather = entity.getWeather();
        if (weather != null) {
            stmt.bindString(6, weather);
        }
 
        String mood = entity.getMood();
        if (mood != null) {
            stmt.bindString(7, mood);
        }
 
        String bg = entity.getBg();
        if (bg != null) {
            stmt.bindString(8, bg);
        }
 
        Integer textSize = entity.getTextSize();
        if (textSize != null) {
            stmt.bindLong(9, textSize);
        }
 
        Integer textColor = entity.getTextColor();
        if (textColor != null) {
            stmt.bindLong(10, textColor);
        }
 
        String label = entity.getLabel();
        if (label != null) {
            stmt.bindString(11, label);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Diary readEntity(Cursor cursor, int offset) {
        Diary entity = new Diary( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.getString(offset + 2), // content
            cursor.getString(offset + 3), // CreateDate
            cursor.getString(offset + 4), // UpdateDate
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // weather
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // mood
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // bg
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // textSize
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // textColor
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // label
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Diary entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setContent(cursor.getString(offset + 2));
        entity.setCreateDate(cursor.getString(offset + 3));
        entity.setUpdateDate(cursor.getString(offset + 4));
        entity.setWeather(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setMood(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setBg(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setTextSize(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setTextColor(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setLabel(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Diary entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Diary entity) {
        if(entity != null) {
            return entity.getId();
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
