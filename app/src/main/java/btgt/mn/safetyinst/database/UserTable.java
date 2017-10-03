package btgt.mn.safetyinst.database;

/**
 * Created by turtuvshin on 9/26/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import btgt.mn.safetyinst.entity.User;

public class UserTable extends DatabaseHelper {

    public static final String TABLE_USERS      = "users";
    public static final String USER_ID          = "id";
    public static final String USER_NAME        = "name";
    public static final String USER_POSITION    = "position";
    public static final String USER_PHONE       = "phone";
    public static final String USER_IMEI        = "imei";
    public static final String USER_EMAIL       = "email";
    public static final String USER_PASS        = "pass";
    public static final String USER_PROFILE     = "profile";
    public static final String USER_LAST_SIGNED = "lastSigned";

    private static final int USER_ID_INDEX       = 0;
    private static final int USER_NAME_INDEX     = 1;
    private static final int USER_POSITION_INDEX = 2;
    private static final int USER_PHONE_INDEX    = 3;
    private static final int USER_IMEI_INDEX     = 4;
    private static final int USER_EMAIL_INDEX    = 5;
    private static final int USER_PASS_INDEX     = 6;
    private static final int USER_PROFILE_INDEX  = 7;
    private static final int USER_LASTS_INDEX    = 8;


    private static final String[] PROJECTIONS_USERS = {
            USER_ID,
            USER_NAME,
            USER_POSITION,
            USER_PHONE,
            USER_IMEI,
            USER_EMAIL,
            USER_PASS,
            USER_PROFILE,
            USER_LAST_SIGNED
    };

    public UserTable(Context context) {
        super(context);
    }

    public void addUser(User user) {
        if (user == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, user.getId());
        cv.put(USER_NAME, user.getName());
        cv.put(USER_POSITION, user.getPosition());
        cv.put(USER_PHONE, user.getPhone());
        cv.put(USER_IMEI, user.getImei());
        cv.put(USER_EMAIL, user.getEmail());
        cv.put(USER_PASS, user.getPassword());
        cv.put(USER_PROFILE, user.getProfile());
        cv.put(USER_LAST_SIGNED, user.getLastSigned());
        db.insert(TABLE_USERS, null, cv);
        db.close();
    }

    public User getUser(int id) {
        SQLiteDatabase db = getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(TABLE_USERS, PROJECTIONS_USERS, USER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        User user = new User(cursor.getString(USER_ID_INDEX),
                cursor.getString(USER_NAME_INDEX),
                cursor.getString(USER_POSITION_INDEX),
                cursor.getInt(USER_PHONE_INDEX),
                cursor.getString(USER_IMEI_INDEX),
                cursor.getString(USER_EMAIL_INDEX),
                cursor.getString(USER_PASS_INDEX),
                cursor.getString(USER_PROFILE_INDEX),
                cursor.getString(USER_LASTS_INDEX));
        cursor.close();
        return user;
    }

    public Cursor checkUser(String imei,String password){

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{
                        USER_ID,
                        USER_NAME,
                        USER_POSITION,
                        USER_PHONE,
                        USER_IMEI,
                        USER_EMAIL,
                        USER_PASS,
                        USER_PROFILE,
                        USER_LAST_SIGNED
                },
                USER_IMEI + "='" + imei + "' AND " +
                        USER_PASS + "='" + password + "'", null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User(cursor.getString(USER_ID_INDEX),
                        cursor.getString(USER_NAME_INDEX),
                        cursor.getString(USER_POSITION_INDEX),
                        cursor.getInt(USER_PHONE_INDEX),
                        cursor.getString(USER_IMEI_INDEX),
                        cursor.getString(USER_EMAIL_INDEX),
                        cursor.getString(USER_PASS_INDEX),
                        cursor.getString(USER_PROFILE_INDEX),
                        cursor.getString(USER_LASTS_INDEX));
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }
    public int updateUser(User user) {
        if (user == null) {
            return -1;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return -1;
        }
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, user.getId());
        cv.put(USER_NAME, user.getName());
        cv.put(USER_POSITION, user.getPosition());
        cv.put(USER_PHONE, user.getPhone());
        cv.put(USER_IMEI, user.getImei());
        cv.put(USER_EMAIL, user.getEmail());
        cv.put(USER_PASS, user.getPassword());
        cv.put(USER_PROFILE, user.getProfile());
        cv.put(USER_LAST_SIGNED, user.getLastSigned());
        int rowCount = db.update(TABLE_USERS, cv, USER_ID + "=?",
                new String[]{String.valueOf(user.getId())});
        db.close();
        return rowCount;
    }

    public void deleteUser(User user) {
        if (user == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        db.delete(TABLE_USERS, USER_ID + "=?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

}