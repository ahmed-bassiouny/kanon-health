package com.germanitlab.kanonhealth.ormLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.germanitlab.kanonhealth.models.user.User;
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
    private Dao<User, Integer> doctorsDao = null;
    private Dao<Message, Integer> messagesDao = null;
    private RuntimeExceptionDao<User, Integer> doctorsRuntimeDao = null;
    private RuntimeExceptionDao<Message, Integer> messagesRuntimeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Message.class);
        } catch (Exception e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_create_database), Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);

        }

        // here we try inserting data in the on-create as a test
        RuntimeExceptionDao<User, Integer> dao = getUsersDataDao();
        RuntimeExceptionDao<Message, Integer> daos = getMessagesDataDao();
        // create some entries in the onCreate
        Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, User.class, true);
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

    public Dao<User, Integer> getUsersDao() throws SQLException {
        if (doctorsDao == null) {
            doctorsDao = getDao(User.class);
        }
        return doctorsDao;
    }

    public Dao<Message, Integer> getMessagesDao() throws SQLException {
        if (messagesDao == null) {
            messagesDao = getDao(Message.class);
        }
        return messagesDao;
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<User, Integer> getUsersDataDao() {
        if (doctorsRuntimeDao == null) {
            doctorsRuntimeDao = getRuntimeExceptionDao(User.class);
        }
        return doctorsRuntimeDao;
    }

    public RuntimeExceptionDao<Message, Integer> getMessagesDataDao() {
        if (messagesRuntimeDao == null) {
            messagesRuntimeDao = getRuntimeExceptionDao(Message.class);
        }
        return messagesRuntimeDao;
    }


}
