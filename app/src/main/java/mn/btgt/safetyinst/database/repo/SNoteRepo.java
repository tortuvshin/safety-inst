package mn.btgt.safetyinst.database.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mn.btgt.safetyinst.database.DatabaseHelper;
import mn.btgt.safetyinst.database.DatabaseManager;
import mn.btgt.safetyinst.database.model.SNote;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class SNoteRepo {

    private SNote sNote;

    public SNoteRepo() {
        sNote = new SNote();
    }

    public static String create(){
        return "CREATE TABLE "+ SNote.TABLE_SNOTE+" (" +
                SNote.SNOTE_ID + " TEXT PRIMARY KEY," +
                SNote.SNOTE_CAT_ID + " INT," +
                SNote.SNOTE_NAME + " TEXT," +
                SNote.SNOTE_ORDER + " TEXT," +
                SNote.SNOTE_FRAME_TYPE + " INT," +
                SNote.SNOTE_FRAME_DATA + " TEXT," +
                SNote.SNOTE_VOICE_DATA + " TEXT," +
                SNote.SNOTE_TIMEOUT + " INT);";
    }

    public void insert(SNote sNote) {
        if (sNote == null) {
            return;
        }
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return;
        }
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(SNote.SNOTE_ID, sNote.getId());
            cv.put(SNote.SNOTE_CAT_ID, sNote.getCategoryId());
            cv.put(SNote.SNOTE_NAME, sNote.getName());
            cv.put(SNote.SNOTE_ORDER, sNote.getOrder());
            cv.put(SNote.SNOTE_FRAME_TYPE, sNote.getFrameType());
            cv.put(SNote.SNOTE_FRAME_DATA, sNote.getFrameData());
            cv.put(SNote.SNOTE_VOICE_DATA, sNote.getVoiceData());
            cv.put(SNote.SNOTE_TIMEOUT, sNote.getTimeout());
            db.insert(SNote.TABLE_SNOTE, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public SNote select(int id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(SNote.TABLE_SNOTE, new String[] {
                        SNote.SNOTE_ID,
                        SNote.SNOTE_CAT_ID,
                        SNote.SNOTE_NAME,
                        SNote.SNOTE_ORDER,
                        SNote.SNOTE_FRAME_TYPE,
                        SNote.SNOTE_FRAME_DATA,
                        SNote.SNOTE_VOICE_DATA,
                        SNote.SNOTE_TIMEOUT
                }, SNote.SNOTE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }

        SNote sNote = new SNote();
        sNote.setId(cursor.getString(SNote.SNOTE_ID_INDEX));
        sNote.setCategoryId(cursor.getString(SNote.SNOTE_CAT_ID_INDEX));
        sNote.setName(cursor.getString(SNote.SNOTE_NAME_INDEX));
        sNote.setOrder(cursor.getString(SNote.SNOTE_ORDER_INDEX));
        sNote.setFrameType(cursor.getInt(SNote.SNOTE_FRAME_TYPE_INDEX));
        sNote.setFrameData(cursor.getString(SNote.SNOTE_FRAME_DATA_INDEX));
        sNote.setVoiceData(cursor.getString(SNote.SNOTE_VOICE_DATA_INDEX));
        sNote.setTimeout(cursor.getInt(SNote.SNOTE_TIMEOUT_INDEX));
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return sNote;
    }

    public List<SNote> selectAll() {
        List<SNote> sNotes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SNote.TABLE_SNOTE;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                SNote sNote = new SNote();
                sNote.setId(cursor.getString(SNote.SNOTE_ID_INDEX));
                sNote.setCategoryId(cursor.getString(SNote.SNOTE_CAT_ID_INDEX));
                sNote.setName(cursor.getString(SNote.SNOTE_NAME_INDEX));
                sNote.setOrder(cursor.getString(SNote.SNOTE_ORDER_INDEX));
                sNote.setFrameType(cursor.getInt(SNote.SNOTE_FRAME_TYPE_INDEX));
                sNote.setFrameData(cursor.getString(SNote.SNOTE_FRAME_DATA_INDEX));
                sNote.setVoiceData(cursor.getString(SNote.SNOTE_VOICE_DATA_INDEX));
                sNote.setTimeout(cursor.getInt(SNote.SNOTE_TIMEOUT_INDEX));
                sNotes.add(sNote);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return sNotes;
    }
    public int update(SNote sNote) {
        if (sNote == null) {
            return -1;
        }
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return -1;
        }
        ContentValues cv = new ContentValues();
        cv.put(SNote.SNOTE_ID, sNote.getId());
        cv.put(SNote.SNOTE_CAT_ID, sNote.getCategoryId());
        cv.put(SNote.SNOTE_NAME, sNote.getName());
        cv.put(SNote.SNOTE_ORDER, sNote.getOrder());
        cv.put(SNote.SNOTE_FRAME_TYPE, sNote.getFrameType());
        cv.put(SNote.SNOTE_FRAME_DATA, sNote.getFrameData());
        cv.put(SNote.SNOTE_VOICE_DATA, sNote.getVoiceData());
        cv.put(SNote.SNOTE_TIMEOUT, sNote.getTimeout());
        int rowCount = db.update(SNote.TABLE_SNOTE, cv, SNote.SNOTE_ID + "=?",
                new String[]{String.valueOf(sNote.getId())});
        DatabaseManager.getInstance().closeDatabase();
        return rowCount;
    }

    public void delete(SNote sNote) {
        if (sNote == null) {
            return;
        }
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return;
        }
        db.delete(SNote.TABLE_SNOTE, SNote.SNOTE_ID + "=?", new String[]{String.valueOf(sNote.getId())});
        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteAll()
    {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(SNote.TABLE_SNOTE, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }

    public int count() {
        String query = "SELECT * FROM  " + SNote.TABLE_SNOTE;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return count;
    }
}
