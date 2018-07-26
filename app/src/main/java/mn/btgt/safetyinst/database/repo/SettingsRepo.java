package mn.btgt.safetyinst.database.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mn.btgt.safetyinst.database.DatabaseManager;
import mn.btgt.safetyinst.database.model.Settings;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */
@Deprecated
public class SettingsRepo {

    public SettingsRepo() {
        Settings settings = new Settings();
    }

    public static String create(){
        return "CREATE TABLE "+ Settings.TABLE_SETTINGS+" (" +
                Settings.SETTINGS_KEY + " TEXT PRIMARY KEY," +
                Settings.SETTINGS_VALUE + " TEXT NOT NULL);";
    }

    public void insert(Settings settings) {
        if (settings == null) {
            return;
        }
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return;
        }
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(Settings.SETTINGS_KEY, settings.getKey());
            cv.put(Settings.SETTINGS_VALUE, settings.getValue());
            db.replace(Settings.TABLE_SETTINGS, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public void insertList(List<Settings> SList){
        if (SList == null) {
            return;
        }
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return;
        }
        db.beginTransaction();
        try {
            for( Settings iset : SList ){
                ContentValues values = new ContentValues();
                values.put(Settings.SETTINGS_KEY, iset.getKey());
                values.put(Settings.SETTINGS_VALUE, iset.getValue());
                db.replace(Settings.TABLE_SETTINGS, null, values);
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

    public String select(String key) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(Settings.TABLE_SETTINGS, new String[]{
                        Settings.SETTINGS_KEY,
                        Settings.SETTINGS_VALUE
                }, Settings.SETTINGS_KEY + "=?",
                new String[]{key}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        String value = cursor.getString(Settings.SETTINGS_VALUE_INDEX);
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return value;
    }

    public List<Settings> selectAll() {
        List<Settings> settings = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Settings.TABLE_SETTINGS;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Settings setting = new Settings();
                setting.setKey(cursor.getString(Settings.SETTINGS_KEY_INDEX));
                setting.setValue(cursor.getString(Settings.SETTINGS_VALUE_INDEX));
                settings.add(setting);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return settings;
    }

    public int update(Settings settings) {
        if (settings == null) {
            return -1;
        }
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return -1;
        }
        ContentValues cv = new ContentValues();
        cv.put(Settings.SETTINGS_KEY, settings.getKey());
        cv.put(Settings.SETTINGS_VALUE, settings.getValue());
        int rowCount = db.update(Settings.TABLE_SETTINGS, cv, Settings.SETTINGS_KEY + "=?",
                new String[]{String.valueOf(settings.getKey())});
        DatabaseManager.getInstance().closeDatabase();
        return rowCount;
    }

    public void deleteAll()
    {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Settings.TABLE_SETTINGS, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }
}