package btgt.mn.safetyinst.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import btgt.mn.safetyinst.entity.SNote;

/**
 * Created by turtuvshin on 10/3/17.
 */

public class SNoteTable extends DatabaseHelper {
   
    public static final String TABLE_SNOTE       = "snote";
    public static final String SNOTE_ID          = "id";
    public static final String SNOTE_CAT_ID      = "category_id";
    public static final String SNOTE_NAME        = "name";
    public static final String SNOTE_ORDER       = "sorder";
    public static final String SNOTE_FRAME_TYPE  = "frame_type";
    public static final String SNOTE_FRAME_DATA  = "frame_data";
    public static final String SNOTE_VOICE_DATA  = "voice_data";
    public static final String SNOTE_TIMEOUT     = "timeout";

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

    public void addSNote(SNote sNote) {
        if (sNote == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
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
        db.insert(TABLE_SNOTE, null, cv);
        db.close();
    }

    public SNote getSNote(int id) {
        SQLiteDatabase db = getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(TABLE_SNOTE, PROJECTIONS_SNOTES, SNOTE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }

        SNote sNote = new SNote(cursor.getString(SNOTE_ID_INDEX),
                cursor.getInt(SNOTE_CAT_ID_INDEX),
                cursor.getString(SNOTE_NAME_INDEX),
                cursor.getString(SNOTE_ORDER_INDEX),
                cursor.getInt(SNOTE_FRAME_TYPE_INDEX),
                cursor.getString(SNOTE_FRAME_DATA_INDEX),
                cursor.getString(SNOTE_VOICE_DATA_INDEX),
                cursor.getInt(SNOTE_TIMEOUT_INDEX));
        cursor.close();
        return sNote;
    }

    public List<SNote> getAllSNotes() {
        List<SNote> sNotes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SNOTE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                SNote sNote = new SNote(cursor.getString(SNOTE_ID_INDEX),
                        cursor.getInt(SNOTE_CAT_ID_INDEX),
                        cursor.getString(SNOTE_NAME_INDEX),
                        cursor.getString(SNOTE_ORDER_INDEX),
                        cursor.getInt(SNOTE_FRAME_TYPE_INDEX),
                        cursor.getString(SNOTE_FRAME_DATA_INDEX),
                        cursor.getString(SNOTE_VOICE_DATA_INDEX),
                        cursor.getInt(SNOTE_TIMEOUT_INDEX));
                sNotes.add(sNote);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sNotes;
    }
    public int updateSNote(SNote sNote) {
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

    public void deleteSNote(SNote sNote) {
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
}
