package mn.btgt.safetyinst;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import mn.btgt.safetyinst.database.SettingsTable;

/**
 * Created by turtuvshin on 12/14/17.
 */

public class SettingsProvider extends ContentProvider{
    private SQLiteDatabase database;
    static final String PROVIDER_NAME = "mn.btgt.safetyinst.SettingsProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/settings";
    static final Uri CONTENT_URI = Uri.parse(URL);

    private SettingsTable sTable;
    @Override
    public boolean onCreate() {
        sTable = new SettingsTable(getContext());
        database = sTable.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(SettingsTable.TABLE_SETTINGS);

        Cursor c = qb.query(database, null, null, null, null, null, null);

        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long rowID = database.insert(SettingsTable.TABLE_SETTINGS, "", contentValues);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(
                    CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add record into" + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int rowsDeleted = database.delete(SettingsTable.TABLE_SETTINGS, null, null);
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
