package mn.btgt.safetyinst.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mn.btgt.safetyinst.model.SignData;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class SignDataTable extends DatabaseHelper {

    static final String TABLE_SIGNDATAS     = "sign_data";
    private static final String SIGNDATA_ID         = "id";
    private static final String SIGNDATA_USER_ID    = "user_id";
    private static final String SIGNDATA_SNOTE_ID   = "snote_id";
    private static final String SIGNDATA_USERNAME   = "user_name";
    private static final String SIGNDATA_SNOTE_NAME = "snote_name";
    private static final String SIGNDATA_VIEWDATE   = "view_date";
    private static final String SIGNDATA_USERSIGN   = "user_sign";
    private static final String SIGNDATA_USERSIGNDATA = "user_signdata";
    private static final String SIGNDATA_PHOTO      = "photo";
    private static final String SIGNDATA_SENDSTATUS = "send_status";

    private static final int SIGNDATA_ID_INDEX         = 0;
    private static final int SIGNDATA_USER_ID_INDEX    = 1;
    private static final int SIGNDATA_SNOTE_ID_INDEX   = 2;
    private static final int SIGNDATA_USERNAME_INDEX   = 3;
    private static final int SIGNDATA_SNOTE_NAME_INDEX = 4;
    private static final int SIGNDATA_VIEWDATE_INDEX   = 5;
    private static final int SIGNDATA_USERSIGN_INDEX   = 6;
    private static final int SIGNDATA_USERSIGNDATA_INDEX = 7;
    private static final int SIGNDATA_PHOTO_INDEX      = 8;
    private static final int SIGNDATA_SENDSTATUS_INDEX = 9;

    static final String CREATE_TABLE_SIGNDATA = "CREATE TABLE "+ TABLE_SIGNDATAS+" (" +
            SIGNDATA_ID + " TEXT PRIMARY KEY," +
            SIGNDATA_USER_ID + " TEXT," +
            SIGNDATA_SNOTE_ID + " TEXT," +
            SIGNDATA_USERNAME + " TEXT," +
            SIGNDATA_SNOTE_NAME + " TEXT," +
            SIGNDATA_VIEWDATE + " TEXT," +
            SIGNDATA_USERSIGN + " BLOB," +
            SIGNDATA_USERSIGNDATA + " TEXT," +
            SIGNDATA_PHOTO + " BLOB," +
            SIGNDATA_SENDSTATUS + " TEXT);";

    private static final String[] PROJECTIONS_SIGNDATAS = {
            SIGNDATA_ID,
            SIGNDATA_USER_ID,
            SIGNDATA_SNOTE_ID,
            SIGNDATA_USERNAME,
            SIGNDATA_SNOTE_NAME,
            SIGNDATA_VIEWDATE,
            SIGNDATA_USERSIGN,
            SIGNDATA_USERSIGNDATA,
            SIGNDATA_PHOTO,
            SIGNDATA_SENDSTATUS
    };

    public SignDataTable(Context context) {
        super(context);
    }

    public void add(SignData signData) {
        if (signData == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put(SIGNDATA_ID, signData.getId());
        cv.put(SIGNDATA_USER_ID, signData.getUserId());
        cv.put(SIGNDATA_SNOTE_ID, signData.getsNoteId());
        cv.put(SIGNDATA_USERNAME, signData.getUserName());
        cv.put(SIGNDATA_SNOTE_NAME, signData.getsNoteName());
        cv.put(SIGNDATA_VIEWDATE, signData.getViewDate());
        cv.put(SIGNDATA_USERSIGN, signData.getUserSign());
        cv.put(SIGNDATA_USERNAME, signData.getUserName());
        cv.put(SIGNDATA_PHOTO, signData.getPhoto());
        cv.put(SIGNDATA_SENDSTATUS, signData.getSendStatus());
        db.insert(TABLE_SIGNDATAS, null, cv);
        db.close();
    }

    public SignData get(int id) {
        SQLiteDatabase db = getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(TABLE_SIGNDATAS, PROJECTIONS_SIGNDATAS, SIGNDATA_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }

        SignData signData = new SignData();
        signData.setId(cursor.getString(SIGNDATA_ID_INDEX));
        signData.setUserId(cursor.getString(SIGNDATA_USER_ID_INDEX));
        signData.setsNoteId(cursor.getString(SIGNDATA_SNOTE_ID_INDEX));
        signData.setUserName(cursor.getString(SIGNDATA_USERNAME_INDEX));
        signData.setsNoteName(cursor.getString(SIGNDATA_SNOTE_NAME_INDEX));
        signData.setViewDate(cursor.getString(SIGNDATA_VIEWDATE_INDEX));
        signData.setUserSign(cursor.getString(SIGNDATA_USERSIGN_INDEX));
        signData.setUserSignData(cursor.getBlob(SIGNDATA_USERSIGNDATA_INDEX));
        signData.setPhoto(cursor.getBlob(SIGNDATA_PHOTO_INDEX));
        signData.setSendStatus(cursor.getString(SIGNDATA_SENDSTATUS_INDEX));
        cursor.close();
        return signData;
    }

    public List<SignData> getAll() {
        List<SignData> signDatas = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SIGNDATAS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                SignData signData = new SignData();
                signData.setId(cursor.getString(SIGNDATA_ID_INDEX));
                signData.setUserId(cursor.getString(SIGNDATA_USER_ID_INDEX));
                signData.setsNoteId(cursor.getString(SIGNDATA_SNOTE_ID_INDEX));
                signData.setUserName(cursor.getString(SIGNDATA_USERNAME_INDEX));
                signData.setsNoteName(cursor.getString(SIGNDATA_SNOTE_NAME_INDEX));
                signData.setViewDate(cursor.getString(SIGNDATA_VIEWDATE_INDEX));
                signData.setUserSign(cursor.getString(SIGNDATA_USERSIGN_INDEX));
                signData.setUserSignData(cursor.getBlob(SIGNDATA_USERSIGNDATA_INDEX));
                signData.setPhoto(cursor.getBlob(SIGNDATA_PHOTO_INDEX));
                signData.setSendStatus(cursor.getString(SIGNDATA_SENDSTATUS_INDEX));
                signDatas.add(signData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return signDatas;
    }
    public int update(SignData signData) {
        if (signData == null) {
            return -1;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return -1;
        }
        ContentValues cv = new ContentValues();
        cv.put(SIGNDATA_ID, signData.getId());
        cv.put(SIGNDATA_USER_ID, signData.getUserId());
        cv.put(SIGNDATA_SNOTE_ID, signData.getsNoteId());
        cv.put(SIGNDATA_USERNAME, signData.getUserName());
        cv.put(SIGNDATA_SNOTE_NAME, signData.getsNoteName());
        cv.put(SIGNDATA_VIEWDATE, signData.getViewDate());
        cv.put(SIGNDATA_USERSIGN, signData.getUserSign());
        cv.put(SIGNDATA_USERSIGNDATA, signData.getUserSignData());
        cv.put(SIGNDATA_PHOTO, signData.getPhoto());
        cv.put(SIGNDATA_SENDSTATUS, signData.getSendStatus());
        int rowCount = db.update(TABLE_SIGNDATAS, cv, SIGNDATA_ID + "=?",
                new String[]{String.valueOf(signData.getId())});
        db.close();
        return rowCount;
    }

    public void delete(SignData signData) {
        if (signData == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        db.delete(TABLE_SIGNDATAS, SIGNDATA_ID + "=?", new String[]{String.valueOf(signData.getId())});
        db.close();
    }

    public void deleteAll()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SIGNDATAS, null, null);
    }
}
