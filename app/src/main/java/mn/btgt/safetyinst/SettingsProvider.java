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

import java.util.HashMap;

import mn.btgt.safetyinst.database.SettingsTable;

/**
 * Created by turtuvshin on 12/14/17.
 */

public class SettingsProvider extends ContentProvider{
    private SQLiteDatabase database;
    static final String PROVIDER_NAME = "mn.btgt.safetyinst.SettingsProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/isafe";
    static final Uri CONTENT_URI = Uri.parse(URL);

    private SettingsTable sTable;

    static final int uriCode = 1;
    static final UriMatcher uriMatcher;
    private static HashMap<String, String> values;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "isafe", uriCode);
        uriMatcher.addURI(PROVIDER_NAME, "isafe/*", uriCode);
    }

    @Override
    public boolean onCreate() {
        sTable = new SettingsTable(getContext());
        database = sTable.getWritableDatabase();
        if (database != null) {
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(SettingsTable.TABLE_SETTINGS);

        switch (uriMatcher.match(uri)) {
            case uriCode:
                qb.setProjectionMap(values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == "") {
            sortOrder = SettingsTable.SETTINGS_KEY;
        }
        Cursor c = qb.query(database, projection, selection, selectionArgs, null,
                null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case uriCode:
                return "vnd.android.cursor.dir/isafe";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
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
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;// Count to tell how many rows deleted
        switch (uriMatcher.match(uri)) {
            case uriCode:
                count = database.delete(SettingsTable.TABLE_SETTINGS, selection, selectionArgs);
                break;
            default:
                count = 0;
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;// Count to tell number of rows updated
        switch (uriMatcher.match(uri)) {
            case uriCode:
                count = database.update(SettingsTable.TABLE_SETTINGS, contentValues, selection, selectionArgs);
                break;
            default:
                count = 0;
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
