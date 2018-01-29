package mn.btgt.safetyinst.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orhanobut.logger.Logger;

import mn.btgt.safetyinst.activity.LoginImeiActivity;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

@SuppressWarnings("ALL")
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context myContext;

    private static final int    DATABASE_VERSION = 31;
    private static final String DATABASE_NAME    = "safety.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(UserTable.CREATE_TABLE_USERS);
        db.execSQL(SNoteTable.CREATE_TABLE_SNOTES);
        db.execSQL(CategoryTable.CREATE_TABLE_CATEGORYS);
        db.execSQL(SignDataTable.CREATE_TABLE_SIGNDATA);
        db.execSQL(SettingsTable.CREATE_TABLE_SETTINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + SNoteTable.TABLE_SNOTE);
        db.execSQL("DROP TABLE IF EXISTS " + CategoryTable.TABLE_CATEGORYS);
        db.execSQL("DROP TABLE IF EXISTS " + SignDataTable.TABLE_SIGNDATAS);
        db.execSQL("DROP TABLE IF EXISTS " + SettingsTable.TABLE_SETTINGS);
        onCreate(db);
        Logger.e("Upgrade database version: "+DATABASE_VERSION);
    }
}