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
import java.util.List;

/**
 * Created by bassiouny on 03/08/17.
 */

public class HttpMessageRepositry {
    private DatabaseHelper db;
    Dao<Message, Integer> messagesDao;
    private Context context;

    public HttpMessageRepositry(Context context) {
        DatabaseManager databaseManager = new DatabaseManager();
        this.context = context;
        db = databaseManager.getHelper(context);
        try {
            messagesDao = db.getHttpMessagesDao();
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
    public List<Message> getAllMessageChat(int userID, int doctorID) {
        // get chat from me and another user
        try {
            QueryBuilder<Message, Integer> queryBuilder = messagesDao.queryBuilder();
                queryBuilder.where().eq(Message.KEY_TOID, doctorID).or().eq(Message.KEY_FROMID, doctorID);
            // prepare our sql statement
            PreparedQuery<Message> preparedQuery = queryBuilder.orderBy(Message.KEY_ID, true).prepare();
            // query for all accounts that have "qwerty" as a password
            List<Message> messageList = messagesDao.query(preparedQuery);
            return messageList;
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("getAllMessageChat: ", "HttpMessageRepositry",e);
        }
        return null;
    }
}
