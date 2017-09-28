package btgt.mn.safetyinst;

/**
 * Created by turtuvshin on 9/26/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserTable extends DatabaseHelper {

    public static final String TABLE_USERS   = "users";
    public static final String USER_ID       = "id";
    public static final String USER_NAME     = "name";
    public static final String USER_IMEI     = "imei";
    public static final String USER_PASS     = "pass";
    public static final String USER_ROLE     = "role";

    private static final int USER_ID_INDEX       = 0;
    private static final int USER_NAME_INDEX     = 1;
    private static final int USER_PASS_INDEX     = 2;
    private static final int USER_IMEI_INDEX     = 3;
    private static final int USER_ROLE_INDEX     = 4;

    private static final String[] PROJECTIONS_USERS = {USER_ID, USER_NAME, USER_IMEI, USER_PASS, USER_ROLE};

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
        cv.put(USER_IMEI, user.getName());
        cv.put(USER_PASS, user.getPassword());
        cv.put(USER_ROLE, user.getRole());
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
                cursor.getString(USER_IMEI_INDEX),
                cursor.getString(USER_PASS_INDEX),
                cursor.getString(USER_ROLE_INDEX));
        cursor.close();
        return user;
    }


    public Cursor checkUser(String username,String password){

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{USER_ID, USER_NAME, USER_IMEI, USER_PASS, USER_ROLE},
                USER_NAME + "='" + username + "' AND " +
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
                String id = cursor.getString(USER_ID_INDEX);
                String name = cursor.getString(USER_NAME_INDEX);
                String imei = cursor.getString(USER_IMEI_INDEX);
                String pass = cursor.getString(USER_PASS_INDEX);
                String role = cursor.getString(USER_ROLE_INDEX);
                User user = new User(id, name, imei, pass, role);
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
        cv.put(USER_IMEI, user.getImei());
        cv.put(USER_PASS, user.getPassword());
        cv.put(USER_ROLE, user.getRole());
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