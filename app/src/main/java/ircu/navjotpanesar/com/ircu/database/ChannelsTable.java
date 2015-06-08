package ircu.navjotpanesar.com.ircu.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Navjot on 1/2/2015.
 */

public class ChannelsTable {

    public static final String TABLE_CHANNELS = "channels";

    //TODO: make table unique by channel
    public class COLUMNS{
        public static final String ID = "_id";
        public static final String CHANNEL = "channel";
        public static final String SERVER = "server";
    }


    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CHANNELS + "(" + COLUMNS.ID
            + " integer primary key autoincrement, "
            + COLUMNS.CHANNEL
            + " text not null, "
            + COLUMNS.SERVER
            + " text not null);";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ChannelsTable.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANNELS);
        onCreate(db);
    }

}