package ircu.navjotpanesar.com.ircu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Navjot on 1/2/2015.
 */
public class ChannelsDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "channelstable.db";
    private static final int DATABASE_VERSION = 1;

    public ChannelsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        ChannelsTable.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        ChannelsTable.onUpgrade(database, oldVersion, newVersion);
    }

}
