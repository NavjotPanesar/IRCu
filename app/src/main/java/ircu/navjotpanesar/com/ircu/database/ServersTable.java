package ircu.navjotpanesar.com.ircu.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ircu.navjotpanesar.com.ircu.contentproviders.ChannelsContentProvider;

/**
 * Created by navjot on 05/06/15.
 */
public class ServersTable {
    //TODO: add default servers
    //TODO: make table unique by server

    public static final String TABLE_SERVER = "servers";

    public class COLUMNS{
        public static final String ID = "_id";
        public static final String SERVER = "server";
        public static final String NICK = "nick";
    }

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_SERVER + "(" + COLUMNS.ID
            + " integer primary key autoincrement, "
            + COLUMNS.SERVER
            + " text not null unique, "
            + COLUMNS.NICK
            + " text not null);";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ChannelsTable.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVER);
        onCreate(db);
    }

}
