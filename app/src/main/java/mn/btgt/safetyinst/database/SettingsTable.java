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
    public static final String SETTINGS_COMPANY_NAME    = "company_name";
    public static final String SETTINGS_DEFARTMENT_NAME = "department_name";
    public static final String SETTINGS_IMAGE            = "image";
    public static final String SETTINGS_IMEI            = "imei";
    public static final String SETTINGS_ANDROID_ID      = "android_id";
    public static final String SETTINGS_SNOTE_DATA      = "snote_data";

    private static final int SETTINGS_COMPANY_INDEX    = 0;
    private static final int SETTINGS_DEPARTMENT_INDEX = 1;
    private static final int SETTINGS_IMAGE_INDEX = 1;
    private static final int SETTINGS_IMEI_INDEX       = 2;
    private static final int SETTINGS_ANDROID_INDEX    = 3;
    private static final int SETTINGS_SNOTE_INDEX      = 4;

    private static final String[] PROJECTIONS_SETTINGS = {
            SETTINGS_COMPANY_NAME,
            SETTINGS_DEFARTMENT_NAME,
            SETTINGS_IMAGE,
            SETTINGS_IMEI,
            SETTINGS_ANDROID_ID,
            SETTINGS_SNOTE_DATA
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
            cv.put(SETTINGS_COMPANY_NAME, settings.getCompanyName());
            cv.put(SETTINGS_DEFARTMENT_NAME, settings.getDepartmentName());
            cv.put(SETTINGS_IMAGE, settings.getImage());
            cv.put(SETTINGS_IMEI, settings.getImei());
            cv.put(SETTINGS_ANDROID_ID, settings.getAndroidId());
            cv.put(SETTINGS_SNOTE_DATA, settings.getsNoteData());
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
                setting.setCompanyName(cursor.getString(SETTINGS_COMPANY_INDEX));
                setting.setDepartmentName(cursor.getString(SETTINGS_DEPARTMENT_INDEX));
                setting.setImage(cursor.getString(SETTINGS_IMAGE_INDEX));
                setting.setImei(cursor.getString(SETTINGS_IMEI_INDEX));
                setting.setAndroidId(cursor.getString(SETTINGS_ANDROID_INDEX));
                setting.setsNoteData(cursor.getString(SETTINGS_SNOTE_INDEX));
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
        cv.put(SETTINGS_COMPANY_NAME, settings.getCompanyName());
        cv.put(SETTINGS_DEFARTMENT_NAME, settings.getDepartmentName());
        cv.put(SETTINGS_IMAGE, settings.getImage());
        cv.put(SETTINGS_IMEI, settings.getImei());
        cv.put(SETTINGS_ANDROID_ID, settings.getAndroidId());
        cv.put(SETTINGS_SNOTE_DATA, settings.getsNoteData());
        int rowCount = db.update(TABLE_SETTINGS, cv, SETTINGS_COMPANY_NAME + "=?",
                new String[]{String.valueOf(settings.getCompanyName())});
        db.close();
        return rowCount;
    }

    public void deleteAll()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SETTINGS, null, null);
    }
}