package me.hsgamer.contentproviderdemo.provider;

import static me.hsgamer.contentproviderdemo.provider.StudentContract.TABLE_NAME;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StudentProvider extends ContentProvider {
    private StudentDatabase database;

    private static final int URI_MULTI_CODE = 1;
    private static final int URI_SINGLE_CODE = 2;
    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(StudentContract.AUTHORITY, StudentContract.BASE_PATH, URI_MULTI_CODE);
        URI_MATCHER.addURI(StudentContract.AUTHORITY, StudentContract.BASE_PATH + "/#", URI_SINGLE_CODE);
    }

    @Override
    public boolean onCreate() {
        database = new StudentDatabase(getContext());
        return database.getWritableDatabase() != null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case URI_MULTI_CODE:
                return StudentContract.MIME_MULTI_STUDENT;
            case URI_SINGLE_CODE:
                return StudentContract.MIME_SINGLE_STUDENT;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME);
        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = StudentContract.ID;
        }

        switch (URI_MATCHER.match(uri)) {
            case URI_MULTI_CODE: {
                Cursor cursor = queryBuilder.query(database.getWritableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }
            case URI_SINGLE_CODE: {
                Cursor cursor = queryBuilder.query(database.getWritableDatabase(), projection, StudentContract.ID + " = ?", new String[]{uri.getLastPathSegment()}, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long rowId = database.getWritableDatabase().insert(TABLE_NAME, "", contentValues);
        if (rowId > 0) {
            Uri singleUri = ContentUris.withAppendedId(StudentContract.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(singleUri, null);
            return singleUri;
        }
        throw new SQLiteException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        switch (URI_MATCHER.match(uri)) {
            case URI_MULTI_CODE:
                count = database.getWritableDatabase().delete(TABLE_NAME, selection, selectionArgs);
                break;
            case URI_SINGLE_CODE:
                count = database.getWritableDatabase().delete(TABLE_NAME, StudentContract.ID + " = ?", new String[]{uri.getLastPathSegment()});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        switch (URI_MATCHER.match(uri)) {
            case URI_MULTI_CODE:
                count = database.getWritableDatabase().update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case URI_SINGLE_CODE:
                count = database.getWritableDatabase().update(TABLE_NAME, values, StudentContract.ID + " = ?", new String[]{uri.getLastPathSegment()});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
