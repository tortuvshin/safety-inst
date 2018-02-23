package mn.btgt.safetyinst.database.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mn.btgt.safetyinst.database.DatabaseManager;
import mn.btgt.safetyinst.database.model.Category;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class CategoryRepo {

    public CategoryRepo() {
        Category category = new Category();
    }

    public static String create(){
        return "CREATE TABLE "+ Category.TABLE_CATEGORYS+" (" +
                Category.CATEGORY_ID + " TEXT PRIMARY KEY," +
                Category.CATEGORY_NAME + " TEXT," +
                Category.CATEGORY_ICON + " TEXT," +
                Category.CATEGORY_ORDER + " TEXT);";
    }

    public void insert(Category category) {
        if (category == null) {
            return;
        }
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return;
        }
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(Category.CATEGORY_ID, category.getId());
            cv.put(Category.CATEGORY_NAME, category.getName());
            cv.put(Category.CATEGORY_ICON, category.getIcon());
            cv.put(Category.CATEGORY_ORDER, category.getOrder());
            db.insert(Category.TABLE_CATEGORYS, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public Category select(int id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(Category.TABLE_CATEGORYS, new String[]{
                        Category.CATEGORY_ID,
                        Category.CATEGORY_NAME,
                        Category.CATEGORY_ICON,
                        Category.CATEGORY_ORDER
                }, Category.CATEGORY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }

        Category category = new Category();
        category.setId(cursor.getString(Category.CATEGORY_ID_INDEX));
        category.setName(cursor.getString(Category.CATEGORY_NAME_INDEX));
        category.setIcon(cursor.getString(Category.CATEGORY_ICON_INDEX));
        category.setOrder(cursor.getString(Category.CATEGORY_ORDER_INDEX));
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return category;
    }

    public List<Category> selectAll() {
        List<Category> categorys = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Category.TABLE_CATEGORYS;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getString(Category.CATEGORY_ID_INDEX));
                category.setName(cursor.getString(Category.CATEGORY_NAME_INDEX));
                category.setIcon(cursor.getString(Category.CATEGORY_ICON_INDEX));
                category.setOrder(cursor.getString(Category.CATEGORY_ORDER_INDEX));
                categorys.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return categorys;
    }

    public int update(Category category) {
        if (category == null) {
            return -1;
        }
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return -1;
        }
        ContentValues cv = new ContentValues();
        cv.put(Category.CATEGORY_ID, category.getId());
        cv.put(Category.CATEGORY_NAME, category.getName());
        cv.put(Category.CATEGORY_ICON, category.getIcon());
        cv.put(Category.CATEGORY_ORDER, category.getOrder());
        int rowCount = db.update(Category.TABLE_CATEGORYS, cv, Category.CATEGORY_ID + "=?",
                new String[]{String.valueOf(category.getId())});
        DatabaseManager.getInstance().closeDatabase();
        return rowCount;
    }

    public void delete(Category category) {
        if (category == null) {
            return;
        }
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        if (db == null) {
            return;
        }
        db.delete(Category.TABLE_CATEGORYS, Category.CATEGORY_ID + "=?", new String[]{String.valueOf(category.getId())});
        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteAll()
    {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Category.TABLE_CATEGORYS, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }

    public int count() {
        String query = "SELECT * FROM  " + Category.TABLE_CATEGORYS;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return count;
    }
}
