package mn.btgt.safetyinst.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orhanobut.logger.Logger;

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
        db.execSQL(UserRepo.CREATE_TABLE_USERS);
        db.execSQL(SNoteRepo.CREATE_TABLE_SNOTES);
        db.execSQL(CategoryRepo.CREATE_TABLE_CATEGORYS);
        db.execSQL(SignDataRepo.CREATE_TABLE_SIGNDATA);
        db.execSQL(SettingsRepo.CREATE_TABLE_SETTINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserRepo.TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + SNoteRepo.TABLE_SNOTE);
        db.execSQL("DROP TABLE IF EXISTS " + CategoryRepo.TABLE_CATEGORYS);
        db.execSQL("DROP TABLE IF EXISTS " + SignDataRepo.TABLE_SIGNDATAS);
        db.execSQL("DROP TABLE IF EXISTS " + SettingsRepo.TABLE_SETTINGS);
        onCreate(db);
        Logger.e("Upgrade database version: "+DATABASE_VERSION);
    }
}