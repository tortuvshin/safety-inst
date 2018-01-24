package mn.btgt.safetyinst.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mn.btgt.safetyinst.entity.SNote;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class SNoteTable extends DatabaseHelper {
   
    static final String TABLE_SNOTE       = "snote";
    static final String SNOTE_ID          = "id";
    static final String SNOTE_CAT_ID      = "category_id";
    static final String SNOTE_NAME        = "name";
    static final String SNOTE_ORDER       = "sorder";
    static final String SNOTE_FRAME_TYPE  = "frame_type";
    static final String SNOTE_FRAME_DATA  = "frame_data";
    static final String SNOTE_VOICE_DATA  = "voice_data";
    static final String SNOTE_TIMEOUT     = "timeout";

    private static final int SNOTE_ID_INDEX         = 0;
    private static final int SNOTE_CAT_ID_INDEX     = 1;
    private static final int SNOTE_NAME_INDEX       = 2;
    private static final int SNOTE_ORDER_INDEX      = 3;
    private static final int SNOTE_FRAME_TYPE_INDEX = 4;
    private static final int SNOTE_FRAME_DATA_INDEX = 5;
    private static final int SNOTE_VOICE_DATA_INDEX = 6;
    private static final int SNOTE_TIMEOUT_INDEX    = 7;
    
    private static final String[] PROJECTIONS_SNOTES = {
            SNOTE_ID,
            SNOTE_CAT_ID,
            SNOTE_NAME,
            SNOTE_ORDER,
            SNOTE_FRAME_TYPE,
            SNOTE_FRAME_DATA,
            SNOTE_VOICE_DATA,
            SNOTE_TIMEOUT
    };

    public SNoteTable(Context context) {
        super(context);
    }

    public void create(SNote sNote) {
        if (sNote == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(SNOTE_ID, sNote.getId());
            cv.put(SNOTE_CAT_ID, sNote.getCategoryId());
            cv.put(SNOTE_NAME, sNote.getName());
            cv.put(SNOTE_ORDER, sNote.getOrder());
            cv.put(SNOTE_FRAME_TYPE, sNote.getFrameType());
            cv.put(SNOTE_FRAME_DATA, sNote.getFrameData());
            cv.put(SNOTE_VOICE_DATA, sNote.getVoiceData());
            cv.put(SNOTE_TIMEOUT, sNote.getTimeout());
            db.insert(TABLE_SNOTE, null, cv);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public SNote select(int id) {
        SQLiteDatabase db = getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(TABLE_SNOTE, PROJECTIONS_SNOTES, SNOTE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }

        SNote sNote = new SNote();
        sNote.setId(cursor.getString(SNOTE_ID_INDEX));
        sNote.setCategoryId(cursor.getString(SNOTE_CAT_ID_INDEX));
        sNote.setName(cursor.getString(SNOTE_NAME_INDEX));
        sNote.setOrder(cursor.getString(SNOTE_ORDER_INDEX));
        sNote.setFrameType(cursor.getInt(SNOTE_FRAME_TYPE_INDEX));
        sNote.setFrameData(cursor.getString(SNOTE_FRAME_DATA_INDEX));
        sNote.setVoiceData(cursor.getString(SNOTE_VOICE_DATA_INDEX));
        sNote.setTimeout(cursor.getInt(SNOTE_TIMEOUT_INDEX));
        cursor.close();
        return sNote;
    }

    public List<SNote> selectAll() {
        List<SNote> sNotes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SNOTE;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                SNote sNote = new SNote();
                sNote.setId(cursor.getString(SNOTE_ID_INDEX));
                sNote.setCategoryId(cursor.getString(SNOTE_CAT_ID_INDEX));
                sNote.setName(cursor.getString(SNOTE_NAME_INDEX));
                sNote.setOrder(cursor.getString(SNOTE_ORDER_INDEX));
                sNote.setFrameType(cursor.getInt(SNOTE_FRAME_TYPE_INDEX));
                sNote.setFrameData(cursor.getString(SNOTE_FRAME_DATA_INDEX));
                sNote.setVoiceData(cursor.getString(SNOTE_VOICE_DATA_INDEX));
                sNote.setTimeout(cursor.getInt(SNOTE_TIMEOUT_INDEX));
                sNotes.add(sNote);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sNotes;
    }
    public int update(SNote sNote) {
        if (sNote == null) {
            return -1;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return -1;
        }
        ContentValues cv = new ContentValues();
        cv.put(SNOTE_ID, sNote.getId());
        cv.put(SNOTE_CAT_ID, sNote.getCategoryId());
        cv.put(SNOTE_NAME, sNote.getName());
        cv.put(SNOTE_ORDER, sNote.getOrder());
        cv.put(SNOTE_FRAME_TYPE, sNote.getFrameType());
        cv.put(SNOTE_FRAME_DATA, sNote.getFrameData());
        cv.put(SNOTE_VOICE_DATA, sNote.getVoiceData());
        cv.put(SNOTE_TIMEOUT, sNote.getTimeout());
        int rowCount = db.update(TABLE_SNOTE, cv, SNOTE_ID + "=?",
                new String[]{String.valueOf(sNote.getId())});
        db.close();
        return rowCount;
    }

    public void delete(SNote sNote) {
        if (sNote == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        db.delete(TABLE_SNOTE, SNOTE_ID + "=?", new String[]{String.valueOf(sNote.getId())});
        db.close();
    }

    public void deleteAll()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SNOTE, null, null);
    }

    public int count() {
        String query = "SELECT * FROM  " + TABLE_SNOTE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
