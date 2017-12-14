package mn.btgt.safetyinst.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mn.btgt.safetyinst.entity.Settings;

/**
 * Created by turtuvshin on 10/3/17.
 */

public class SettingsTable extends DatabaseHelper {
    
    public static final String TABLE_SETTINGS          = "settings";
    public static final String SETTINGS_KEY = "key";
    public static final String SETTINGS_VALUE = "value";

    private static final int SETTINGS_COMPANY_INDEX    = 0;
    private static final int SETTINGS_DEPARTMENT_INDEX = 1;

    private static final String[] PROJECTIONS_SETTINGS = {
            SETTINGS_KEY,
            SETTINGS_VALUE
    };

    public SettingsTable(Context context) {
        super(context);
    }

    public void add(Settings settings) {
        if (settings == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(SETTINGS_KEY, settings.getKey());
            cv.put(SETTINGS_VALUE, settings.getValue());
            db.insert(TABLE_SETTINGS, null, cv);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public List<Settings> get() {
        List<Settings> settings = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SETTINGS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Settings setting = new Settings();
                setting.setKey(cursor.getString(SETTINGS_COMPANY_INDEX));
                setting.setValue(cursor.getString(SETTINGS_DEPARTMENT_INDEX));
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
