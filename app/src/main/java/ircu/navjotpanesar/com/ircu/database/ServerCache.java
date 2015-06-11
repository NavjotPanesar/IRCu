package ircu.navjotpanesar.com.ircu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import java.util.HashMap;

import ircu.navjotpanesar.com.ircu.contentproviders.ChannelsContentProvider;
import ircu.navjotpanesar.com.ircu.pircbot.ChannelItem;
import ircu.navjotpanesar.com.ircu.pircbot.Server;

/**
 * Created by Navjot on 6/9/2015.
 */
public class ServerCache {
    private static DatabaseHelper database;
    private static HashMap<String, Server> serverCache;
    public static Server getServer(String serverAddress, Context context){
        Server server = attemptToRetrieveFromCache(serverAddress);
        if(server == null){
            server = attemptToRetrieveFromDatabase(serverAddress, context);
            if (server != null) {
                serverCache.put(serverAddress, server);
            }
        }
        return server;
    }

    private static Server attemptToRetrieveFromCache(String serverAddress){
        if(serverCache == null){
            serverCache = new HashMap<String, Server>();
        }
        if(serverCache.containsKey(serverAddress)){
            return serverCache.get(serverAddress);
        }
        return null;
    }

    private static Server attemptToRetrieveFromDatabase(String serverAddress, Context context){
        if(database == null){
            database = new DatabaseHelper(context);
        }
        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.appendWhere(ServersTable.COLUMNS.SERVER + "="
                + serverAddress);

        // Set the table
        queryBuilder.setTables(ServersTable.TABLE_SERVER);
        String[] projection = { ServersTable.COLUMNS.ID, ServersTable.COLUMNS.SERVER, ServersTable.COLUMNS.NICK };


        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, null,
                null, null, null, null);

        cursor.move(-1);

        Server server =  null;
        while (cursor.moveToNext()) {
            String serverAddressRetrieved = cursor.getString(cursor.getColumnIndex(ServersTable.COLUMNS.SERVER));
            String nickRetrieved = cursor.getString(cursor.getColumnIndex(ServersTable.COLUMNS.NICK));
            server = new Server(serverAddress, nickRetrieved);
        }

        cursor.close();
        return server;
    }

}
