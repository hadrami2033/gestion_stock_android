package com.example.gestionstock.util;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLite;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseChange;
import com.couchbase.lite.DatabaseChangeListener;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Document;
import com.couchbase.lite.ListenerToken;

import java.util.concurrent.Executor;

public class DatabaseManager {
    private static Database database;
    private static DatabaseManager instance = null;
    private ListenerToken listenerToken;
    public  String currentUser = null;
    private static String dbName = "db";

    protected DatabaseManager() {
    }

    public static DatabaseManager getSharedInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public static Database getDatabase() {
        return database;
    }

    // tag::initCouchbaseLite[]
    public void initCouchbaseLite(Context context) {
        CouchbaseLite.init(context);
    }
    // end::initCouchbaseLite[]

    // tag::openOrCreateDatabase[]
    public void openOrCreateDatabase(Context context){
        // tag::databaseConfiguration[]
        DatabaseConfiguration config = new DatabaseConfiguration();
        try {
            // tag::createDatabase[]
            database = new Database(dbName, config);
            // end::createDatabase[]
            //registerForDatabaseChanges();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }
}
