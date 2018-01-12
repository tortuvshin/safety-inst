package mn.btgt.safetyinst.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mn.btgt.safetyinst.entity.SNote;
import mn.btgt.safetyinst.entity.Settings;

/**
 * Created by turtuvshin on 10/3/17.
 */

public class SettingsTable extends DatabaseHelper {

    public static final String TABLE_SETTINGS = "settings";
    public static final String SETTINGS_KEY   = "settings_key";
    public static final String SETTINGS_VALUE = "settings_value";

    private static final int SETTINGS_KEY_INDEX = 0;
    private static final int SETTINGS_VALUE_INDEX = 1;

    private static final String[] PROJECTIONS_SETTINGS = {
            SETTINGS_KEY,
            SETTINGS_VALUE
    };

    public SettingsTable(Context context) {
        super(context);
    }

    public void insert(String key, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        values.put(SETTINGS_KEY, key);
        values.put(SETTINGS_VALUE, value);
        db.replace(TABLE_SETTINGS, null, values);
        db.close();
    }

    public void insertList(List<Settings> SList){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("BEGIN TRANSACTION");
        for( Settings iset : SList ){
            ContentValues values = new ContentValues();
            values.put(SETTINGS_KEY, iset.getKey());
            values.put(SETTINGS_VALUE, iset.getValue());
            db.replace(TABLE_SETTINGS, null, values);
        }
        db.execSQL("END TRANSACTION");
        db.close();
    }

    public Settings get(String key) {
        SQLiteDatabase db = getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(TABLE_SETTINGS, PROJECTIONS_SETTINGS, SETTINGS_KEY + "=?",
                new String[]{key}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }

        Settings sett = new Settings();
        sett.setKey(cursor.getString(SETTINGS_KEY_INDEX));
        sett.setValue(cursor.getString(SETTINGS_VALUE_INDEX));
        cursor.close();
        return sett;

    }
    public List<Settings> getAll() {
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