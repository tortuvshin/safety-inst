package mn.btgt.safetyinst.db.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mn.btgt.safetyinst.db.DatabaseManager;
import mn.btgt.safetyinst.db.model.User;
/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */
@Deprecated
public class UserRepo {

    public UserRepo() {
        User user = new User();
    }

    public static String create(){
        return "CREATE TABLE "+User.TABLE_USERS+" (" +
                User.USER_ID + " TEXT PRIMARY KEY," +
                User.USER_NAME + " TEXT," +
                User.USER_POSITION + " TEXT," +
                User.USER_PHONE + " INT," +
                User.USER_IMEI + " TEXT," +
                User.USER_EMAIL + " TEXT," +
                User.USER_PASS + " TEXT," +
                User.USER_AVATAR + " TEXT," +
                User.USER_LAST_SIGNED + " TEXT);";
    }

    public void insert(User user) {
        if (user == null) {
            return;
        }
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return;
        }
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(User.USER_ID, user.getId());
            cv.put(User.USER_NAME, user.getName());
            cv.put(User.USER_POSITION, user.getPosition());
            cv.put(User.USER_PHONE, user.getPhone());
            cv.put(User.USER_IMEI, user.getImei());
            cv.put(User.USER_EMAIL, user.getEmail());
            cv.put(User.USER_PASS, user.getPassword());
            cv.put(User.USER_AVATAR, user.getAvatar());
            cv.put(User.USER_LAST_SIGNED, user.getLastSigned());
            db.insert(User.TABLE_USERS, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public User select(int id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(User.TABLE_USERS, new String[]{
                        User.USER_ID,
                        User.USER_NAME,
                        User.USER_POSITION,
                        User.USER_PHONE,
                        User.USER_IMEI,
                        User.USER_EMAIL,
                        User.USER_PASS,
                        User.USER_AVATAR,
                        User.USER_LAST_SIGNED
                }, User.USER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        User user = new User();
        user.setId(cursor.getString(User.USER_ID_INDEX));
        user.setName(cursor.getString(User.USER_NAME_INDEX));
        user.setPosition(cursor.getString(User.USER_POSITION_INDEX));
        user.setPhone(cursor.getInt(User.USER_PHONE_INDEX));
        user.setImei(cursor.getString(User.USER_IMEI_INDEX));
        user.setEmail(cursor.getString(User.USER_EMAIL_INDEX));
        user.setPassword(cursor.getString(User.USER_PASS_INDEX));
        user.setAvatar(cursor.getString(User.USER_AVATAR_INDEX));
        user.setLastSigned(cursor.getString(User.USER_LASTS_INDEX));
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return user;
    }

    public Cursor checkUser(String password){

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query(User.TABLE_USERS, new String[]{
                        User.USER_ID,
                        User.USER_NAME,
                        User.USER_POSITION,
                        User.USER_PHONE,
                        User.USER_IMEI,
                        User.USER_EMAIL,
                        User.USER_PASS,
                        User.USER_AVATAR,
                        User.USER_LAST_SIGNED
                },
//                User.USER_NAME + "='" + username + "' AND " +
                        User.USER_PASS + "='" + password + "'", null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        DatabaseManager.getInstance().closeDatabase();
        return cursor;
    }

    public List<User> selectAll() {
        List<User> users = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + User.TABLE_USERS;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getString(User.USER_ID_INDEX));
                user.setName(cursor.getString(User.USER_NAME_INDEX));
                user.setPosition(cursor.getString(User.USER_POSITION_INDEX));
                user.setPhone(cursor.getInt(User.USER_PHONE_INDEX));
                user.setImei(cursor.getString(User.USER_IMEI_INDEX));
                user.setEmail(cursor.getString(User.USER_EMAIL_INDEX));
                user.setPassword(cursor.getString(User.USER_PASS_INDEX));
                user.setAvatar(cursor.getString(User.USER_AVATAR_INDEX));
                user.setLastSigned(cursor.getString(User.USER_LASTS_INDEX));
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return users;
    }
    public int update(User user) {
        if (user == null) {
            return -1;
        }
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return -1;
        }
        ContentValues cv = new ContentValues();
        cv.put(User.USER_ID, user.getId());
        cv.put(User.USER_NAME, user.getName());
        cv.put(User.USER_POSITION, user.getPosition());
        cv.put(User.USER_PHONE, user.getPhone());
        cv.put(User.USER_IMEI, user.getImei());
        cv.put(User.USER_EMAIL, user.getEmail());
        cv.put(User.USER_PASS, user.getPassword());
        cv.put(User.USER_AVATAR, user.getAvatar());
        cv.put(User.USER_LAST_SIGNED, user.getLastSigned());
        int rowCount = db.update(User.TABLE_USERS, cv, User.USER_ID + "=?",
                new String[]{String.valueOf(user.getId())});
        DatabaseManager.getInstance().closeDatabase();
        return rowCount;
    }

    public void delete(User user) {
        if (user == null) {
            return;
        }
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return;
        }
        db.delete(User.TABLE_USERS, User.USER_ID + "=?", new String[]{String.valueOf(user.getId())});
        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteAll()
    {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(User.TABLE_USERS, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }

    public int count() {
        String query = "SELECT * FROM  " + User.TABLE_USERS;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return count;
    }
}