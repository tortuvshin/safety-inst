package mn.btgt.safetyinst.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

import mn.btgt.safetyinst.activity.LoginImeiActivity;
import mn.btgt.safetyinst.database.SettingsTable;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class SettingsProvider extends ContentProvider{

    private static final String TAG = SettingsProvider.class.getSimpleName();

    private SQLiteDatabase database;
    static final String PROVIDER_NAME = "mn.btgt.safetyinst.provider.SettingsProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/isafe";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final int uriCode = 1;
    static final UriMatcher uriMatcher;

    static HashMap<String, String> values;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "isafe", uriCode);
        uriMatcher.addURI(PROVIDER_NAME, "isafe/*", uriCode);
    }

    @Override
    public boolean onCreate() {
        SettingsTable sTable = new SettingsTable(getContext());
        database = sTable.getWritableDatabase();
        return database != null;
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
        throw new SQLException("Failed to create record into" + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
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
        int count = 0;
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