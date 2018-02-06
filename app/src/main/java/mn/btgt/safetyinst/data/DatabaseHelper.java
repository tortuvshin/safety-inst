package mn.btgt.safetyinst.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orhanobut.logger.Logger;

import mn.btgt.safetyinst.AppMain;
import mn.btgt.safetyinst.data.model.Category;
import mn.btgt.safetyinst.data.model.SNote;
import mn.btgt.safetyinst.data.model.Settings;
import mn.btgt.safetyinst.data.model.SignData;
import mn.btgt.safetyinst.data.model.User;
import mn.btgt.safetyinst.data.repo.CategoryRepo;
import mn.btgt.safetyinst.data.repo.SNoteRepo;
import mn.btgt.safetyinst.data.repo.SettingsRepo;
import mn.btgt.safetyinst.data.repo.SignDataRepo;
import mn.btgt.safetyinst.data.repo.UserRepo;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context myContext;

    private static final int    DATABASE_VERSION = 33;
    private static final String DATABASE_NAME    = "safety.db";

    private static DatabaseHelper sInstance;

    public DatabaseHelper( ) {
        super(AppMain.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Deprecated
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Logger.d("New database helper");
        myContext = context;
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