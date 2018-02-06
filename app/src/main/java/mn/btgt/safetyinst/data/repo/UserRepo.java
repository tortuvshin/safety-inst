package mn.btgt.safetyinst.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mn.btgt.safetyinst.data.DatabaseHelper;
import mn.btgt.safetyinst.data.model.User;
/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class UserRepo extends DatabaseHelper {

    public static final String TABLE_USERS      = "users";
    private static final String USER_ID          = "id";
    private static final String USER_NAME        = "name";
    private static final String USER_POSITION    = "position";
    private static final String USER_PHONE       = "phone";
    private static final String USER_IMEI        = "imei";
    private static final String USER_EMAIL       = "email";
    private static final String USER_PASS        = "pass";
    private static final String USER_AVATAR      = "profile";
    private static final String USER_LAST_SIGNED = "lastSigned";

    private static final int USER_ID_INDEX       = 0;
    private static final int USER_NAME_INDEX     = 1;
    private static final int USER_POSITION_INDEX = 2;
    private static final int USER_PHONE_INDEX    = 3;
    private static final int USER_IMEI_INDEX     = 4;
    private static final int USER_EMAIL_INDEX    = 5;
    private static final int USER_PASS_INDEX     = 6;
    private static final int USER_AVATAR_INDEX   = 7;
    private static final int USER_LASTS_INDEX    = 8;

    public static final String CREATE_TABLE_USERS = "CREATE TABLE "+TABLE_USERS+" (" +
            USER_ID + " TEXT PRIMARY KEY," +
            USER_NAME + " TEXT," +
            USER_POSITION + " TEXT," +
            USER_PHONE + " INT," +
            USER_IMEI + " TEXT," +
            USER_EMAIL + " TEXT," +
            USER_PASS + " TEXT," +
            USER_AVATAR + " TEXT," +
            USER_LAST_SIGNED + " TEXT);";

    private static final String[] PROJECTIONS_USERS = {
            USER_ID,
            USER_NAME,
            USER_POSITION,
            USER_PHONE,
            USER_IMEI,
            USER_EMAIL,
            USER_PASS,
            USER_AVATAR,
            USER_LAST_SIGNED
    };

    public UserRepo(Context context) {
        super(context);
    }

    public void create(User user) {
        if (user == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(USER_ID, user.getId());
            cv.put(USER_NAME, user.getName());
            cv.put(USER_POSITION, user.getPosition());
            cv.put(USER_PHONE, user.getPhone());
            cv.put(USER_IMEI, user.getImei());
            cv.put(USER_EMAIL, user.getEmail());
            cv.put(USER_PASS, user.getPassword());
            cv.put(USER_AVATAR, user.getAvatar());
            cv.put(USER_LAST_SIGNED, user.getLastSigned());
            db.insert(TABLE_USERS, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public User select(int id) {
        SQLiteDatabase db = getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(TABLE_USERS, PROJECTIONS_USERS, USER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        User user = new User();
        user.setId(cursor.getString(USER_ID_INDEX));
        user.setName(cursor.getString(USER_NAME_INDEX));
        user.setPosition(cursor.getString(USER_POSITION_INDEX));
        user.setPhone(cursor.getInt(USER_PHONE_INDEX));
        user.setImei(cursor.getString(USER_IMEI_INDEX));
        user.setEmail(cursor.getString(USER_EMAIL_INDEX));
        user.setPassword(cursor.getString(USER_PASS_INDEX));
        user.setAvatar(cursor.getString(USER_AVATAR_INDEX));
        user.setLastSigned(cursor.getString(USER_LASTS_INDEX));
        cursor.close();
        return user;
    }

    public Cursor checkUser(String password){

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{
                        USER_ID,
                        USER_NAME,
                        USER_POSITION,
                        USER_PHONE,
                        USER_IMEI,
                        USER_EMAIL,
                        USER_PASS,
                        USER_AVATAR,
                        USER_LAST_SIGNED
                },
//                USER_NAME + "='" + username + "' AND " +
                        USER_PASS + "='" + password + "'", null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public List<User> selectAll() {
        List<User> users = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getString(USER_ID_INDEX));
                user.setName(cursor.getString(USER_NAME_INDEX));
                user.setPosition(cursor.getString(USER_POSITION_INDEX));
                user.setPhone(cursor.getInt(USER_PHONE_INDEX));
                user.setImei(cursor.getString(USER_IMEI_INDEX));
                user.setEmail(cursor.getString(USER_EMAIL_INDEX));
                user.setPassword(cursor.getString(USER_PASS_INDEX));
                user.setAvatar(cursor.getString(USER_AVATAR_INDEX));
                user.setLastSigned(cursor.getString(USER_LASTS_INDEX));
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }
    public int update(User user) {
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
        cv.put(USER_AVATAR, user.getAvatar());
        cv.put(USER_LAST_SIGNED, user.getLastSigned());
        int rowCount = db.update(TABLE_USERS, cv, USER_ID + "=?",
                new String[]{String.valueOf(user.getId())});
        db.close();
        return rowCount;
    }

    public void delete(User user) {
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

    public void deleteAll()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_USERS, null, null);
    }

    public int count() {
        String query = "SELECT * FROM  " + TABLE_USERS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}