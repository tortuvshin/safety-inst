package mn.btgt.safetyinst.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orhanobut.logger.Logger;

import mn.btgt.safetyinst.AppMain;
import mn.btgt.safetyinst.database.model.Category;
import mn.btgt.safetyinst.database.model.SNote;
import mn.btgt.safetyinst.database.model.Settings;
import mn.btgt.safetyinst.database.model.SignData;
import mn.btgt.safetyinst.database.model.User;
import mn.btgt.safetyinst.database.repo.CategoryRepo;
import mn.btgt.safetyinst.database.repo.SNoteRepo;
import mn.btgt.safetyinst.database.repo.SettingsRepo;
import mn.btgt.safetyinst.database.repo.SignDataRepo;
import mn.btgt.safetyinst.database.repo.UserRepo;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int    DATABASE_VERSION = 34;
    private static final String DATABASE_NAME    = "safety.db";

    public DatabaseHelper() {
        super(AppMain.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(UserRepo.create());
        db.execSQL(SNoteRepo.create());
        db.execSQL(CategoryRepo.create());
        db.execSQL(SignDataRepo.create());
        db.execSQL(SettingsRepo.create());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + SNote.TABLE_SNOTE);
        db.execSQL("DROP TABLE IF EXISTS " + Category.TABLE_CATEGORYS);
        db.execSQL("DROP TABLE IF EXISTS " + SignData.TABLE_SIGNDATAS);
        db.execSQL("DROP TABLE IF EXISTS " + Settings.TABLE_SETTINGS);
        onCreate(db);
        Logger.e("Upgrade database version: "+DATABASE_VERSION);
    }
}