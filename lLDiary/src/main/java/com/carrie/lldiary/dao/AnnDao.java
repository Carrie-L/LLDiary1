package com.carrie.lldiary.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.carrie.lldiary.dao.Ann;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ANN".
*/
public class AnnDao extends AbstractDao<Ann, Long> {

    public static final String TABLENAME = "ANN";

    /**
     * Properties of entity Ann.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Content = new Property(1, String.class, "content", false, "CONTENT");
        public final static Property Person = new Property(2, String.class, "person", false, "PERSON");
        public final static Property Relationship = new Property(3, String.class, "relationship", false, "RELATIONSHIP");
        public final static Property Icon = new Property(4, String.class, "icon", false, "ICON");
        public final static Property Date = new Property(5, String.class, "date", false, "DATE");
        public final static Property Remind = new Property(6, String.class, "remind", false, "REMIND");
        public final static Property Remark = new Property(7, String.class, "remark", false, "REMARK");
    };


    public AnnDao(DaoConfig config) {
        super(config);
    }
    
    public AnnDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ANN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"CONTENT\" TEXT," + // 1: content
                "\"PERSON\" TEXT," + // 2: person
                "\"RELATIONSHIP\" TEXT," + // 3: relationship
                "\"ICON\" TEXT," + // 4: icon
                "\"DATE\" TEXT," + // 5: date
                "\"REMIND\" TEXT," + // 6: remind
                "\"REMARK\" TEXT);"); // 7: remark
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ANN\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Ann entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(2, content);
        }
 
        String person = entity.getPerson();
        if (person != null) {
            stmt.bindString(3, person);
        }
 
        String relationship = entity.getRelationship();
        if (relationship != null) {
            stmt.bindString(4, relationship);
        }
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(5, icon);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(6, date);
        }
 
        String remind = entity.getRemind();
        if (remind != null) {
            stmt.bindString(7, remind);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(8, remark);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Ann readEntity(Cursor cursor, int offset) {
        Ann entity = new Ann( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // content
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // person
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // relationship
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // icon
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // date
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // remind
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // remark
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Ann entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setContent(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPerson(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRelationship(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIcon(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDate(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setRemind(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setRemark(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Ann entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Ann entity) {
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
