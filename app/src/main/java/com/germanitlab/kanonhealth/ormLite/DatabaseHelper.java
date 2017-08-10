package com.germanitlab.kanonhealth.ormLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.models.ChatModel;
import com.germanitlab.kanonhealth.api.models.Document;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.models.Table;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Geram IT Lab on 15/05/2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "kanon.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;
    private Context context;
    // the DAO object we use to access the SimpleData table
    private Dao<UserInfo, Integer> doctorsDao = null;
    private Dao<ChatModel, Integer> chatModel = null;
    private Dao<Table, Integer> tablesDao = null;
    private Dao<com.germanitlab.kanonhealth.api.models.Message, Integer> messagesDao = null;
    private Dao<Document, Integer> documentsDao = null;
    private Dao<com.germanitlab.kanonhealth.api.models.Message, Integer> httpMessagesDao = null;
    private Dao<com.germanitlab.kanonhealth.api.models.Document, Integer> httpdocumentsDao = null;
    private RuntimeExceptionDao<UserInfo, Integer> doctorsRuntimeDao = null;
    private RuntimeExceptionDao<com.germanitlab.kanonhealth.api.models.Message, Integer> messagesRuntimeDao = null;
    private RuntimeExceptionDao<com.germanitlab.kanonhealth.api.models.Document, Integer> documentsRuntimeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, UserInfo.class);
            TableUtils.createTable(connectionSource, Message.class);
        } catch (Exception e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_create_database), Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);

        }

        // here we try inserting data in the on-create as a test
        RuntimeExceptionDao<UserInfo, Integer> dao = getUsersDataDao();
        RuntimeExceptionDao<com.germanitlab.kanonhealth.api.models.Message, Integer> messagesDataDao = getMessagesDataDao();
        RuntimeExceptionDao<com.germanitlab.kanonhealth.api.models.Document, Integer> daos = getDocumentDataDao();
        // create some entries in the onCreate
        Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, UserInfo.class, true);
            TableUtils.dropTable(connectionSource, Message.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(database, connectionSource);
        } catch (Exception e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_update_database), Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);

        }
    }

    public Dao<UserInfo, Integer> getUsersDao() throws SQLException {
        if (doctorsDao == null) {
            doctorsDao = getDao(UserInfo.class);
        }
        return doctorsDao;
    }
    public Dao<ChatModel, Integer> getChatModelsDao() throws SQLException {
        if (chatModel == null) {
            chatModel = getDao(ChatModel.class);
        }
        return chatModel;
    }

    public Dao<Table, Integer> getTablesDao() throws SQLException {
        if (tablesDao == null) {
            tablesDao = getDao(Table.class);
        }
        return tablesDao;
    }

    public Dao<com.germanitlab.kanonhealth.api.models.Message, Integer> getMessagesDao() throws SQLException {
        if (messagesDao == null) {
            messagesDao = getDao(com.germanitlab.kanonhealth.api.models.Message.class);
        }
        return messagesDao;
    }
    public Dao<com.germanitlab.kanonhealth.api.models.Message, Integer> getHttpMessagesDao() throws SQLException {
        if (messagesDao == null) {
            messagesDao = getDao(com.germanitlab.kanonhealth.api.models.Message.class);
        }
        return httpMessagesDao;
    }
    public Dao<com.germanitlab.kanonhealth.api.models.Document, Integer> getHttpDocumentDao() throws SQLException {
        if (httpdocumentsDao == null) {
            httpdocumentsDao = getDao(com.germanitlab.kanonhealth.api.models.Document.class);
        }
        return httpdocumentsDao;
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<UserInfo, Integer> getUsersDataDao() {
        if (doctorsRuntimeDao == null) {
            doctorsRuntimeDao = getRuntimeExceptionDao(UserInfo.class);
        }
        return doctorsRuntimeDao;
    }

    public RuntimeExceptionDao<com.germanitlab.kanonhealth.api.models.Message, Integer> getMessagesDataDao() {
        if (messagesRuntimeDao == null) {
            messagesRuntimeDao = getRuntimeExceptionDao(com.germanitlab.kanonhealth.api.models.Message.class);
        }
        return messagesRuntimeDao;
    }

    public RuntimeExceptionDao<com.germanitlab.kanonhealth.api.models.Document, Integer> getDocumentDataDao() {
        if (documentsRuntimeDao == null) {
            documentsRuntimeDao = getRuntimeExceptionDao(Document.class);
        }
        return documentsRuntimeDao;
    }

}
