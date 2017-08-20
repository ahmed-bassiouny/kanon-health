package com.germanitlab.kanonhealth.ormLite;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.models.Message;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bassiouny on 03/08/17.
 */

public class HttpMessageRepositry {
    private DatabaseHelper db;
    Dao<Message, Integer> messagesDao;
    public HttpMessageRepositry(Context context) {
        DatabaseManager databaseManager = new DatabaseManager();
        db = databaseManager.getHelper(context);
        db.setWriteAheadLoggingEnabled(false);
        try {
            messagesDao = db.getHttpMessagesDao();
            messagesDao.setObjectCache(false);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Error In Constructor","HttpMessageRepositry: ", e);
        }
    }
    public void createOrUpate(Message message){
        try {
            messagesDao.createOrUpdate(message);
        } catch (SQLException e) {
            Crashlytics.logException(e);
            Log.e("createOrUpate: ", "HttpMessageRepositry",e);
        }
    }
    public List<Message> getAllMessageChat(int doctorID) {
        List<Message> messages=null;
        try {
            QueryBuilder<Message, Integer> queryBuilder = messagesDao.queryBuilder();
            queryBuilder.where().eq("toID", doctorID).or().eq("fromID", doctorID);
            // prepare our sql statement
            PreparedQuery<Message> preparedQuery = queryBuilder.orderBy("messageID", true).prepare();
            // query for all accounts that have "qwerty" as a password
            messages = messagesDao.query(preparedQuery);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("getAllMessageChat: ", "HttpMessageRepositry",e);
            messages=new ArrayList<>();
        }finally {
            return messages;
        }
    }
}
