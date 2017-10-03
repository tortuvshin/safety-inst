package btgt.mn.safetyinst.database;

/**
 * Created by turtuvshin on 9/26/17.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import btgt.mn.safetyinst.entity.SNote;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context myContext;

    private static final String TAG = "DatabaseHelper : ";
    private static final int    DATABASE_VERSION = 1;
    private static final String DATABASE_NAME    = "qwe.db";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE "+UserTable.TABLE_USERS+" (" +
            UserTable.USER_ID + " TEXT PRIMARY KEY," +
            UserTable.USER_NAME + " TEXT," +
            UserTable.USER_POSITION + " TEXT," +
            UserTable.USER_PHONE + " INT," +
            UserTable.USER_IMEI + " TEXT," +
            UserTable.USER_EMAIL + " TEXT," +
            UserTable.USER_PASS + " TEXT," +
            UserTable.USER_PROFILE + " TEXT," +
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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_SNOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(UserTable.TABLE_USERS);
        dropTable(SNoteTable.TABLE_SNOTE);
        onCreate(db);
    }

    public void dropTable(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        if (db == null || TextUtils.isEmpty(tableName)) {
            return;
        }
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }
}
