package mn.btgt.safetyinst.database;

/**
 * Created by turtuvshin on 9/26/17.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context myContext;

    private static final String TAG = "DatabaseHelper : ";
    private static final int    DATABASE_VERSION = 10;
    private static final String DATABASE_NAME    = "safety.db";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE "+UserTable.TABLE_USERS+" (" +
            UserTable.USER_ID + " TEXT PRIMARY KEY," +
            UserTable.USER_NAME + " TEXT," +
            UserTable.USER_POSITION + " TEXT," +
            UserTable.USER_PHONE + " INT," +
            UserTable.USER_IMEI + " TEXT," +
            UserTable.USER_EMAIL + " TEXT," +
            UserTable.USER_PASS + " TEXT," +
            UserTable.USER_AVATAR + " TEXT," +
            UserTable.USER_LAST_SIGNED + " TEXT)";

    private static final String CREATE_TABLE_SNOTES = "CREATE TABLE "+ SNoteTable.TABLE_SNOTE+" (" +
            SNoteTable.SNOTE_ID + " TEXT PRIMARY KEY," +
            SNoteTable.SNOTE_CAT_ID + " INT," +
            SNoteTable.SNOTE_NAME + " TEXT," +
            SNoteTable.SNOTE_ORDER + " TEXT," +
            SNoteTable.SNOTE_FRAME_TYPE + " INT," +
            SNoteTable.SNOTE_FRAME_DATA + " TEXT," +
            SNoteTable.SNOTE_VOICE_DATA + " TEXT," +
            SNoteTable.SNOTE_TIMEOUT + " INT)";

    private static final String CREATE_TABLE_CATEGORYS = "CREATE TABLE "+ CategoryTable.TABLE_CATEGORYS+" (" +
            CategoryTable.CATEGORY_ID + " TEXT PRIMARY KEY," +
            CategoryTable.CATEGORY_NAME + " TEXT," +
            CategoryTable.CATEGORY_ICON + " TEXT," +
            CategoryTable.CATEGORY_ORDER + " TEXT)";

    private static final String CREATE_TABLE_SIGNDATA = "CREATE TABLE "+ SignDataTable.TABLE_SIGNDATAS+" (" +
            SignDataTable.SIGNDATA_ID + " TEXT PRIMARY KEY," +
            SignDataTable.SIGNDATA_USER_ID + " TEXT," +
            SignDataTable.SIGNDATA_SNOTE_ID + " TEXT," +
            SignDataTable.SIGNDATA_VIEWDATE + " TEXT," +
            SignDataTable.SIGNDATA_USERSIGN + " BLOB," +
            SignDataTable.SIGNDATA_PHOTO + " BLOB," +
            SignDataTable.SIGNDATA_SENDSTATUS + " TEXT)";

    private static final String CREATE_TABLE_SETTINGS = "CREATE TABLE "+ SettingsTable.TABLE_SETTINGS+" (" +
            SettingsTable.SETTINGS_KEY + " TEXT PRIMARY KEY," +
            SettingsTable.SETTINGS_VALUE + " TEXT," +
            SettingsTable.SETTINGS_IMAGE + " TEXT," +
            SettingsTable.SETTINGS_IMEI + " TEXT," +
            SettingsTable.SETTINGS_ANDROID_ID + " TEXT," +
            SettingsTable.SETTINGS_SNOTE_DATA + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_SNOTES);
        db.execSQL(CREATE_TABLE_CATEGORYS);
        db.execSQL(CREATE_TABLE_SIGNDATA);
        db.execSQL(CREATE_TABLE_SETTINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + SNoteTable.TABLE_SNOTE);
        db.execSQL("DROP TABLE IF EXISTS " + CategoryTable.TABLE_CATEGORYS);
        db.execSQL("DROP TABLE IF EXISTS " + SignDataTable.TABLE_SIGNDATAS);
        db.execSQL("DROP TABLE IF EXISTS " + SettingsTable.TABLE_SETTINGS);
        onCreate(db);
    }
}
