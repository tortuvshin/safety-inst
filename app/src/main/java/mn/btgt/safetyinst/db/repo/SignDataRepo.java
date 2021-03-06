package mn.btgt.safetyinst.db.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import mn.btgt.safetyinst.db.DatabaseManager;
import mn.btgt.safetyinst.db.model.Settings;
import mn.btgt.safetyinst.db.model.SignData;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */
@Deprecated
public class SignDataRepo {

    public SignDataRepo() {
        SignData signData = new SignData();
    }

    public static String create(){
        return  "CREATE TABLE "+ SignData.TABLE_SIGNDATAS+" (" +
                SignData.SIGNDATA_ID + " TEXT PRIMARY KEY," +
                SignData.SIGNDATA_USER_ID + " TEXT," +
                SignData.SIGNDATA_SNOTE_ID + " TEXT," +
                SignData.SIGNDATA_USERNAME + " TEXT," +
                SignData.SIGNDATA_SNOTE_NAME + " TEXT," +
                SignData.SIGNDATA_VIEWDATE + " TEXT," +
                SignData.SIGNDATA_SIGN_NAME + " TEXT," +
                SignData.SIGNDATA_SIGN_DATA + " BLOB," +
                SignData.SIGNDATA_PHOTO_NAME + " TEXT," +
                SignData.SIGNDATA_PHOTO + " BLOB," +
                SignData.SIGNDATA_SENDSTATUS + " TEXT);";
    }

    public void insert(SignData signData) {
        if (signData == null) {
            return;
        }
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return;
        }
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(SignData.SIGNDATA_ID, signData.getId());
            cv.put(SignData.SIGNDATA_USER_ID, signData.getUserId());
            cv.put(SignData.SIGNDATA_SNOTE_ID, signData.getsNoteId());
            cv.put(SignData.SIGNDATA_USERNAME, signData.getUserName());
            cv.put(SignData.SIGNDATA_SNOTE_NAME, signData.getsNoteName());
            cv.put(SignData.SIGNDATA_VIEWDATE, signData.getViewDate());
            cv.put(SignData.SIGNDATA_SIGN_NAME, signData.getSignName());
            cv.put(SignData.SIGNDATA_SIGN_DATA, signData.getSignData());
            cv.put(SignData.SIGNDATA_PHOTO_NAME, signData.getPhotoName());
            cv.put(SignData.SIGNDATA_PHOTO, signData.getPhoto());
            cv.put(SignData.SIGNDATA_SENDSTATUS, signData.getSendStatus());
            db.insert(SignData.TABLE_SIGNDATAS, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public void insertList(List<SignData> SList){
        if (SList == null) {
            return;
        }
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return;
        }
        db.beginTransaction();
        try {
            for( SignData signData : SList ){
                ContentValues cv = new ContentValues();
                cv.put(SignData.SIGNDATA_ID, signData.getId());
                cv.put(SignData.SIGNDATA_USER_ID, signData.getUserId());
                cv.put(SignData.SIGNDATA_SNOTE_ID, signData.getsNoteId());
                cv.put(SignData.SIGNDATA_USERNAME, signData.getUserName());
                cv.put(SignData.SIGNDATA_SNOTE_NAME, signData.getsNoteName());
                cv.put(SignData.SIGNDATA_VIEWDATE, signData.getViewDate());
                cv.put(SignData.SIGNDATA_SIGN_NAME, signData.getSignName());
                cv.put(SignData.SIGNDATA_SIGN_DATA, signData.getSignData());
                cv.put(SignData.SIGNDATA_PHOTO_NAME, signData.getPhotoName());
                cv.put(SignData.SIGNDATA_PHOTO, signData.getPhoto());
                cv.put(SignData.SIGNDATA_SENDSTATUS, signData.getSendStatus());
                db.replace(Settings.TABLE_SETTINGS, null, cv);
            }
            db.setTransactionSuccessful();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public SignData select(int id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(SignData.TABLE_SIGNDATAS, new String[]{
                    SignData.SIGNDATA_ID,
                    SignData.SIGNDATA_USER_ID,
                    SignData.SIGNDATA_SNOTE_ID,
                    SignData.SIGNDATA_USERNAME,
                    SignData.SIGNDATA_SNOTE_NAME,
                    SignData.SIGNDATA_VIEWDATE,
                    SignData.SIGNDATA_SIGN_NAME,
                    SignData.SIGNDATA_SIGN_DATA,
                    SignData.SIGNDATA_PHOTO_NAME,
                    SignData.SIGNDATA_PHOTO,
                    SignData.SIGNDATA_SENDSTATUS
                }, SignData.SIGNDATA_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }

        SignData signData = new SignData();
        signData.setId(cursor.getString(SignData.SIGNDATA_ID_INDEX));
        signData.setUserId(cursor.getString(SignData.SIGNDATA_USER_ID_INDEX));
        signData.setsNoteId(cursor.getString(SignData.SIGNDATA_SNOTE_ID_INDEX));
        signData.setUserName(cursor.getString(SignData.SIGNDATA_USERNAME_INDEX));
        signData.setsNoteName(cursor.getString(SignData.SIGNDATA_SNOTE_NAME_INDEX));
        signData.setViewDate(cursor.getString(SignData.SIGNDATA_VIEWDATE_INDEX));
        signData.setSignName(cursor.getString(SignData.SIGNDATA_SIGN_NAME_INDEX));
        signData.setSignData(cursor.getBlob(SignData.SIGNDATA_SIGN_DATA_INDEX));
        signData.setPhotoName(cursor.getString(SignData.SIGNDATA_PHOTO_NAME_INDEX));
        signData.setPhoto(cursor.getBlob(SignData.SIGNDATA_PHOTO_INDEX));
        signData.setSendStatus(cursor.getString(SignData.SIGNDATA_SENDSTATUS_INDEX));
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return signData;
    }

    public List<SignData> selectAll() {
        List<SignData> signDatas = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SignData.TABLE_SIGNDATAS;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    SignData signData = new SignData();
                    signData.setId(cursor.getString(SignData.SIGNDATA_ID_INDEX));
                    signData.setUserId(cursor.getString(SignData.SIGNDATA_USER_ID_INDEX));
                    signData.setsNoteId(cursor.getString(SignData.SIGNDATA_SNOTE_ID_INDEX));
                    signData.setUserName(cursor.getString(SignData.SIGNDATA_USERNAME_INDEX));
                    signData.setsNoteName(cursor.getString(SignData.SIGNDATA_SNOTE_NAME_INDEX));
                    signData.setViewDate(cursor.getString(SignData.SIGNDATA_VIEWDATE_INDEX));
                    signData.setSignName(cursor.getString(SignData.SIGNDATA_SIGN_NAME_INDEX));
                    signData.setSignData(cursor.getBlob(SignData.SIGNDATA_SIGN_DATA_INDEX));
                    signData.setPhotoName(cursor.getString(SignData.SIGNDATA_PHOTO_NAME_INDEX));
                    signData.setPhoto(cursor.getBlob(SignData.SIGNDATA_PHOTO_INDEX));
                    signData.setSendStatus(cursor.getString(SignData.SIGNDATA_SENDSTATUS_INDEX));
                    signDatas.add(signData);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
        }
        return signDatas;
    }
    public int update(SignData signData) {
        if (signData == null) {
            return -1;
        }
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return -1;
        }
        ContentValues cv = new ContentValues();
        cv.put(SignData.SIGNDATA_ID, signData.getId());
        cv.put(SignData.SIGNDATA_USER_ID, signData.getUserId());
        cv.put(SignData.SIGNDATA_SNOTE_ID, signData.getsNoteId());
        cv.put(SignData.SIGNDATA_SIGN_DATA, signData.getUserName());
        cv.put(SignData.SIGNDATA_SNOTE_NAME, signData.getsNoteName());
        cv.put(SignData.SIGNDATA_VIEWDATE, signData.getViewDate());
        cv.put(SignData.SIGNDATA_SIGN_NAME, signData.getSignName());
        cv.put(SignData.SIGNDATA_SIGN_DATA, signData.getSignData());
        cv.put(SignData.SIGNDATA_PHOTO_NAME, signData.getPhotoName());
        cv.put(SignData.SIGNDATA_PHOTO, signData.getPhoto());
        cv.put(SignData.SIGNDATA_SENDSTATUS, signData.getSendStatus());
        int rowCount = db.update(SignData.TABLE_SIGNDATAS, cv, SignData.SIGNDATA_ID + "=?",
                new String[]{String.valueOf(signData.getId())});
        DatabaseManager.getInstance().closeDatabase();
        Logger.d("Updated sign data column id "+signData.getId());
        return rowCount;
    }

    public void delete(SignData signData) {
        if (signData == null) {
            return;
        }
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return;
        }
        db.delete(SignData.TABLE_SIGNDATAS, SignData.SIGNDATA_ID + "=?", new String[]{String.valueOf(signData.getId())});
        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteAll()
    {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(SignData.TABLE_SIGNDATAS, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }

    public int count() {
        String query = "SELECT * FROM  " + SignData.TABLE_SIGNDATAS;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return count;
    }

}
