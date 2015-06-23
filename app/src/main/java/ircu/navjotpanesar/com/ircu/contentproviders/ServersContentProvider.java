package ircu.navjotpanesar.com.ircu.contentproviders;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

import ircu.navjotpanesar.com.ircu.database.DatabaseHelper;
import ircu.navjotpanesar.com.ircu.database.ServersTable;

/**
 * Created by navjot on 05/06/15.
 */
public class ServersContentProvider extends ContentProvider {

    // database
    private DatabaseHelper database;

    // used for the UriMacher
    private static final int SERVERS = 10;
    private static final int SERVER_ID = 20;

    private static final String AUTHORITY = "ircu.navjotpanesar.com.ircu.contentprovider.ServersContentProvider";

    private static final String BASE_PATH = "servers";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/servers";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/servers";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, SERVERS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", SERVER_ID);
    }

    @Override
    public boolean onCreate() {
        database = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(ServersTable.TABLE_SERVER);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case SERVERS:
                break;
            case SERVER_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(ServersTable.COLUMNS.ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        long id = 0;
        switch (uriType) {
            case SERVERS:
                id = sqlDB.insert(ServersTable.TABLE_SERVER, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case SERVERS:
                rowsDeleted = sqlDB.delete(ServersTable.TABLE_SERVER, selection,
                        selectionArgs);
                break;
            case SERVER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(ServersTable.TABLE_SERVER,
                            ServersTable.COLUMNS.ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(ServersTable.TABLE_SERVER,
                            ServersTable.COLUMNS.ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case SERVERS:
                rowsUpdated = sqlDB.update(ServersTable.TABLE_SERVER,
                        values,
                        selection,
                        selectionArgs);
                break;
            case SERVER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(ServersTable.TABLE_SERVER,
                            values,
                            ServersTable.COLUMNS.ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(ServersTable.TABLE_SERVER,
                            values,
                            ServersTable.COLUMNS.ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = { ServersTable.COLUMNS.ID,
                ServersTable.COLUMNS.SERVER,
                ServersTable.COLUMNS.NICK };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

}