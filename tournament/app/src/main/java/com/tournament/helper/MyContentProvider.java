package com.tournament.helper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tournament.helper.data.provider.DbHelper;

public class MyContentProvider extends ContentProvider {

  private DbHelper mDbHelper;
  public static final int TOURNAMENTS_CODE = 100;

  public static final UriMatcher sUriMatcher = buildUriMatcher();

  public static UriMatcher buildUriMatcher() {
    UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_TOURNAMENTS, TOURNAMENTS_CODE);

    return uriMatcher;
  }

  @Override
  public boolean onCreate() {
    Context context = getContext();
    mDbHelper = new DbHelper(context);

    return true;
  }

  @Nullable
  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    final SQLiteDatabase db = mDbHelper.getReadableDatabase();
    int match = sUriMatcher.match(uri);
    Cursor retCursor = null;
    switch(match) {
      case TOURNAMENTS_CODE:

        retCursor = db.query(Contract.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder);

        break;
      default:
        break;
    }

    retCursor.setNotificationUri(getContext().getContentResolver(), uri);

    return retCursor;
  }

  @Nullable
  @Override
  public String getType(Uri uri) {
    return null;
  }

  @Nullable
  @Override
  public Uri insert(Uri uri, ContentValues values) {

    final SQLiteDatabase db = mDbHelper.getWritableDatabase();
    int match = sUriMatcher.match(uri);
    Uri returnUri = null;
    switch(match) {
      case TOURNAMENTS_CODE:
        long id = db.insert(Contract.TABLE_NAME, null, values);
        if(id > 0) {
          returnUri = ContentUris.withAppendedId(Contract.PATH_TOURNAMENTS_URI, id);
        }
        break;
      default:
        break;
    }

    getContext().getContentResolver().notifyChange(uri, null);

    return returnUri;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int match = sUriMatcher.match(uri);
    switch(match) {
      case TOURNAMENTS_CODE:
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(Contract.TABLE_NAME, selection, selectionArgs);
        break;
      default:
        break;
    }
    return 0;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    return 0;
  }
}
