package mn.btgt.safetyinst.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mn.btgt.safetyinst.data.DatabaseHelper;
import mn.btgt.safetyinst.data.model.Settings;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class SettingsRepo extends DatabaseHelper {

    public static final String TABLE_SETTINGS = "settings";
    public static final String SETTINGS_KEY   = "settings_key";
    private static final String SETTINGS_VALUE = "settings_value";

    private static final int SETTINGS_KEY_INDEX = 0;
    private static final int SETTINGS_VALUE_INDEX = 1;

    public static final String CREATE_TABLE_SETTINGS = "CREATE TABLE "+ TABLE_SETTINGS+" (" +
            SETTINGS_KEY + " TEXT PRIMARY KEY," +
            SETTINGS_VALUE + " TEXT NOT NULL);";

    private static final String[] PROJECTIONS_SETTINGS = {
            SETTINGS_KEY,
            SETTINGS_VALUE
    };

    public SettingsRepo(Context context) {
        super(context);
    }

    public void insert(Settings settings) {
        if (settings == null) {
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            return;
        }
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(SETTINGS_KEY, settings.getKey());
            cv.put(SETTINGS_VALUE, settings.getValue());
            db.replace(TABLE_SETTINGS, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }

    public void insertList(List<Settings> SList){
        if (SList == null) {
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            return;
        }
        db.beginTransaction();
        try {
            for( Settings iset : SList ){
                ContentValues values = new ContentValues();
                values.put(SETTINGS_KEY, iset.getKey());
                values.put(SETTINGS_VALUE, iset.getValue());
                db.replace(TABLE_SETTINGS, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }

    public String select(String key) {
        SQLiteDatabase db = getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(TABLE_SETTINGS, PROJECTIONS_SETTINGS, SETTINGS_KEY + "=?",
                new String[]{key}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        String value = cursor.getString(SETTINGS_VALUE_INDEX);
        cursor.close();
        return value;
    }

    public List<Settings> selectAll() {
        List<Settings> settings = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SETTINGS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Settings setting = new Settings();
                setting.setKey(cursor.getString(SETTINGS_KEY_INDEX));
                setting.setValue(cursor.getString(SETTINGS_VALUE_INDEX));
                settings.add(setting);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return settings;
    }
    public int update(Settings settings) {
        if (settings == null) {
            return -1;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return -1;
        }
        ContentValues cv = new ContentValues();
        cv.put(SETTINGS_KEY, settings.getKey());
        cv.put(SETTINGS_VALUE, settings.getValue());
        int rowCount = db.update(TABLE_SETTINGS, cv, SETTINGS_KEY + "=?",
                new String[]{String.valueOf(settings.getKey())});
        db.close();
        return rowCount;
    }

    public void deleteAll()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SETTINGS, null, null);
    }
}