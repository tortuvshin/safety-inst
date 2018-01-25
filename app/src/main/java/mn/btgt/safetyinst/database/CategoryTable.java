package mn.btgt.safetyinst.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mn.btgt.safetyinst.model.Category;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class CategoryTable extends DatabaseHelper {

    static final String TABLE_CATEGORYS       = "categorys";
    private static final String CATEGORY_ID          = "id";
    private static final String CATEGORY_NAME        = "name";
    private static final String CATEGORY_ICON        = "icon";
    private static final String CATEGORY_ORDER       = "sorder";

    private static final int CATEGORY_ID_INDEX         = 0;
    private static final int CATEGORY_NAME_INDEX       = 1;
    private static final int CATEGORY_ICON_INDEX       = 2;
    private static final int CATEGORY_ORDER_INDEX      = 3;

    static final String CREATE_TABLE_CATEGORYS = "CREATE TABLE "+ TABLE_CATEGORYS+" (" +
            CATEGORY_ID + " TEXT PRIMARY KEY," +
            CATEGORY_NAME + " TEXT," +
            CATEGORY_ICON + " TEXT," +
            CATEGORY_ORDER + " TEXT);";

    private static final String[] PROJECTIONS_CATEGORYS = {
            CATEGORY_ID,
            CATEGORY_NAME,
            CATEGORY_ICON,
            CATEGORY_ORDER
    };

    public CategoryTable(Context context) {
        super(context);
    }

    public void create(Category category) {
        if (category == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(CATEGORY_ID, category.getId());
            cv.put(CATEGORY_NAME, category.getName());
            cv.put(CATEGORY_ICON, category.getIcon());
            cv.put(CATEGORY_ORDER, category.getOrder());
            db.insert(TABLE_CATEGORYS, null, cv);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }

    public Category select(int id) {
        SQLiteDatabase db = getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(TABLE_CATEGORYS, PROJECTIONS_CATEGORYS, CATEGORY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }

        Category category = new Category();
        category.setId(cursor.getString(CATEGORY_ID_INDEX));
        category.setName(cursor.getString(CATEGORY_NAME_INDEX));
        category.setIcon(cursor.getString(CATEGORY_ICON_INDEX));
        category.setOrder(cursor.getString(CATEGORY_ORDER_INDEX));
        cursor.close();
        return category;
    }

    public List<Category> selectAll() {
        List<Category> categorys = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORYS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getString(CATEGORY_ID_INDEX));
                category.setName(cursor.getString(CATEGORY_NAME_INDEX));
                category.setIcon(cursor.getString(CATEGORY_ICON_INDEX));
                category.setOrder(cursor.getString(CATEGORY_ORDER_INDEX));
                categorys.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categorys;
    }

    public int update(Category category) {
        if (category == null) {
            return -1;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return -1;
        }
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_ID, category.getId());
        cv.put(CATEGORY_NAME, category.getName());
        cv.put(CATEGORY_ICON, category.getIcon());
        cv.put(CATEGORY_ORDER, category.getOrder());
        int rowCount = db.update(TABLE_CATEGORYS, cv, CATEGORY_ID + "=?",
                new String[]{String.valueOf(category.getId())});
        db.close();
        return rowCount;
    }

    public void delete(Category category) {
        if (category == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        db.delete(TABLE_CATEGORYS, CATEGORY_ID + "=?", new String[]{String.valueOf(category.getId())});
        db.close();
    }

    public void deleteAll()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CATEGORYS, null, null);
    }

    public int count() {
        String query = "SELECT * FROM  " + TABLE_CATEGORYS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
